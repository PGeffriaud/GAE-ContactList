package com.zenika.zencontact.resource;

import com.google.common.base.Optional;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.datastore.UserDaoDatastore;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;

/**
 * Created by PierreG on 28/02/17.
 * API v1 (call datastore)
 */
@Component
@RestxResource
public class UserDatastoreResource {

    @GET("/v1/users")
    @PermitAll
    public Iterable<User> getAllUsers() {
        return UserDaoDatastore.getInstance().getAll();
    }

    @GET("/v1/users/{id}")
    @PermitAll
    public Optional<User> getUser(final Long id) {
        User user = UserDaoDatastore.getInstance().get(id);
        return Optional.fromNullable(user);
    }

    @PUT("/v1/users/{id}")
    @PermitAll
    public Optional<User> updateUser(final Long id, final User user) {
        long key = UserDaoDatastore.getInstance().save(user);
        user.id = key;
        return Optional.fromNullable(user);
    }

    @DELETE("/v1/users/{id}")
    @PermitAll
    public void deleteUser(final Long id) {
        UserDaoDatastore.getInstance().delete(id);
    }

    @POST("/v1/users")
    @PermitAll
    public User storeUser(final User user) {
        user.id(UserDaoDatastore.getInstance().save(user));
        return user;
    }
}
