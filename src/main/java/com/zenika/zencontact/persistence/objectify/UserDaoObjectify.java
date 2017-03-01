package com.zenika.zencontact.persistence.objectify;

import com.google.appengine.api.blobstore.BlobKey;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.UserDao;

import java.util.List;

/**
 * Created by PierreG on 28/02/17.
 * CRUD operations with objectify
 */
public class UserDaoObjectify implements UserDao {

    private static UserDaoObjectify INSTANCE = new UserDaoObjectify();

    private UserDaoObjectify() {
        super();
        ObjectifyService.factory().register(User.class);
    }

    public static UserDaoObjectify getInstance() {
        return INSTANCE;
    }

    public BlobKey fetchOldBlob(Long id) {
        return this.get(id).photoKey;
    }

    @Override
    public long save(User contact) {
        return ObjectifyService.ofy().save().entity(contact).now().getId();
    }

    @Override
    public void delete(Long id) {
        ObjectifyService.ofy().delete().key(Key.create(User.class, id)).now();
    }

    @Override
    public User get(Long id) {
        return ObjectifyService.ofy().load().key(Key.create(User.class, id)).now();
    }

    @Override
    public List<User> getAll() {
        return ObjectifyService.ofy().load().type(User.class).list();
    }
}
