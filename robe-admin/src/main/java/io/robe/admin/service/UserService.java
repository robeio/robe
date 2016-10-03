package io.robe.admin.service;

import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.entity.User;
import org.jvnet.hk2.annotations.Service;

/**
 * Created by kamilbukum on 23/09/16.
 */
@Service
public class UserService {

    @UnitOfWork
    public void addUSer(User user){

    }

    @UnitOfWork
    public void sendInformation(){

    }
}
