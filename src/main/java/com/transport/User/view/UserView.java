package com.transport.User.view;

import com.transport.User.entity.UserStatus;
import com.transport.User.entity.User;
import com.transport.User.entity.UserType;
import com.transport.liby.view.BaseView;

public class UserView<T extends User> extends BaseView<T> {
    public UserView(T entity) {
        super(entity);
    }

    public String getPhoneNumber(){
        return entity.getPhoneNumber();
    }
    public String getName(){
        return entity.getName();
    }
    public String getEmail(){
        return entity.getEmail();
    }
    public UserStatus getUserStatus(){
        return entity.getUserStatus();
    }
    public UserType getUserType(){return entity.getUserType();}
}