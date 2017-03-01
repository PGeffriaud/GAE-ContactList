package com.zenika.zencontact.resource;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.UserRepository;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;

@Component
@RestxResource
public class UserResource {

    @GET("/v0/users")
    @PermitAll
    /**
     * Get the users defined in a arraylist : only mock objects
     */
    public Iterable<User> getAllUsers() {
        return UserRepository.USERS;
    }

    @GET("/v0/users/{id}")
    @PermitAll
    public Optional<User> getUser(final Long id) {
        Iterable<User> users = UserRepository.USERS;
        Predicate<User> getUserById = new Predicate<User>() {
            public boolean apply(User user) {
                return user.id == id;
            }
        };
        User user = Iterables.find(users, getUserById, null);
        return Optional.fromNullable(user);
    }

    @PUT("/v0/users/{id}")
    @PermitAll
    public Optional<User> updateUser(final Long id, final User user) {
        Iterable<User> users = UserRepository.USERS;
        Predicate<User> getUserById = new Predicate<User>() {
            public boolean apply(User user) {
                return user.id == id;
            }
        };
        UserRepository.USERS.set(Iterables.indexOf(users, getUserById), user);
        return Optional.fromNullable(user);
    }

    @DELETE("/v0/users/{id}")
    @PermitAll
    public void deleteUser(final Long id) {
        Iterable<User> users = UserRepository.USERS;
        Predicate<User> getUserById = new Predicate<User>() {
            public boolean apply(User user) {
                return user.id == id;
            }
        };
        UserRepository.USERS.remove(Iterables.indexOf(users, getUserById));
    }

    @POST("/v0/users")
    @PermitAll
    public User storeUser(final User user) {
        if(user.id == null) {
            user.id = Long.valueOf(UserRepository.USERS.size());
        }
        if(Strings.isNullOrEmpty(user.password)) {
            user.password = "azerty";
        }
        UserRepository.USERS.add(user);
        return user;
    }

}
