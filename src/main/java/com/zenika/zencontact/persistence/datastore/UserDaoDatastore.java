package com.zenika.zencontact.persistence.datastore;

import com.google.appengine.api.datastore.*;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.UserDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by PierreG on 28/02/17.
 * CRUD methods for User entities in Google datastore
 */
public class UserDaoDatastore implements UserDao {

    private static UserDaoDatastore INSTANCE = new UserDaoDatastore();

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public static UserDaoDatastore getInstance() {
        return INSTANCE;
    }

    @Override
    public long save(User contact) {
        Entity e = new Entity("User");

        // If contact already exist
        if(contact.id != null) {
            Key k = KeyFactory.createKey("User", contact.id);
            try {
                e = datastore.get(k);
            } catch (EntityNotFoundException e1) {
                throw new RuntimeException(e1);
            }
        }

        // Fill entity with contact value
        e.setProperty("firstname", contact.firstName);
        e.setProperty("lastname", contact.lastName);
        e.setProperty("email", contact.email);
        e.setProperty("notes", contact.notes);
        if(contact.birthdate != null) {
            e.setProperty("birthdate", contact.birthdate);
        }

        // Save or update contact
        Key key = datastore.put(e);
        return key.getId();
    }

    @Override
    public void delete(Long id) {
        Key key = KeyFactory.createKey("User", id);
        datastore.delete(key);
    }

    @Override
    public User get(Long id) {
        Entity e;
        try {
            e = datastore.get(KeyFactory.createKey("User", id));
        } catch (EntityNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        return User.create()
                .id(e.getKey().getId())
                .firstName((String) e.getProperty("firstname"))
                .lastName((String) e.getProperty("lastname"))
                .email((String) e.getProperty("email"))
                .birthdate((Date) e.getProperty("birthdate"))
                .notes((String) e.getProperty("notes"));
    }

    @Override
    public List<User> getAll() {
        ArrayList<User> contacts = new ArrayList<>();

        Query q = new Query("User")
            .addProjection(new PropertyProjection("firstname", String.class))
            .addProjection(new PropertyProjection("lastname", String.class))
            .addProjection(new PropertyProjection("email", String.class))
            .addProjection(new PropertyProjection("notes", String.class));

        PreparedQuery pq = datastore.prepare(q);
        for (Entity e : pq.asIterable()) {
            contacts.add(User.create()
                    .id(e.getKey().getId())
                    .firstName((String) e.getProperty("firstname"))
                    .lastName((String) e.getProperty("lastname"))
                    .email((String) e.getProperty("email"))
                    .notes((String) e.getProperty("notes")));
        }

        return contacts;
    }
}
