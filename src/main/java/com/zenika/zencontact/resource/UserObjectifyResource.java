package com.zenika.zencontact.resource;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.base.Optional;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.domain.blob.PhotoService;
import com.zenika.zencontact.persistence.objectify.UserDaoObjectify;
import com.zenika.zencontact.resource.fetch.PartnerBirthdayService;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by PierreG on 28/02/17.
 * API v2 (call datastore)
 */
@Component
@RestxResource
public class UserObjectifyResource {

    Logger LOG = Logger.getLogger(UserObjectifyResource.class.getSimpleName());

    public static final String USERS_KEY = "a-users";
    MemcacheService cache = MemcacheServiceFactory.getMemcacheService();

    @GET("/v2/users")
    @PermitAll
    public Iterable<User> getAllUsers() {

        List<User> contacts = (List<User>) cache.get(USERS_KEY);
        if (contacts == null) {
            LOG.warning("cache is missing - refill it");
            contacts = UserDaoObjectify.getInstance().getAll();
            cache.put(USERS_KEY, contacts, Expiration.byDeltaSeconds(240));
        } else {
            LOG.warning("cache hit");
        }
        return contacts;
    }

    @GET("/v2/users/{id}")
    @PermitAll
    public Optional<User> getUser(final Long id) {
        User user = UserDaoObjectify.getInstance().get(id);
        PhotoService.getInstance().prepareDownloadURL(user);
        PhotoService.getInstance().prepareUploadURL(user);
        return Optional.fromNullable(user);
    }

    @PUT("/v2/users/{id}")
    @PermitAll
    public Optional<User> updateUser(final Long id, final User user) {
        cache.delete(USERS_KEY);
        long key = UserDaoObjectify.getInstance().save(user);
        user.id = key;
        return Optional.fromNullable(user);
    }

    @DELETE("/v2/users/{id}")
    @PermitAll
    public void deleteUser(final Long id) {
        PhotoService.getInstance().deleteOldBlob(id);
        UserDaoObjectify.getInstance().delete(id);
        cache.delete(USERS_KEY);
    }

    @POST("/v2/users")
    @PermitAll
    public User storeUser(final User user) {
        cache.delete(USERS_KEY);

        String bithdate = PartnerBirthdayService.getInstance().findBirthdata(user.firstName, user.lastName);
        if(bithdate != null) {
            try {
                user.birthdate(new SimpleDateFormat("yyyy-MM-dd").parse(bithdate));
            } catch (ParseException e) {
                LOG.severe(e.getMessage());
            }
        }

        user.id(UserDaoObjectify.getInstance().save(user));

        return user;
    }
}
