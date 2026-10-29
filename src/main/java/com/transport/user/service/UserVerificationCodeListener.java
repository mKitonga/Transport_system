package com.transport.user.service;

import com.transport.authentication.Entity.UsernameType;
import com.transport.authentication.Service.OnUsernameVerificationListener;
import com.transport.user.entity.User;
import com.transport.user.repository.UserRepository;
import com.transport.liby.service.BaseJpaRepoEditService;

public class UserVerificationCodeListener<U extends User, R extends UserRepository<U>> extends BaseJpaRepoEditService<U, R>
        implements OnUsernameVerificationListener {

    @Override
    public void onSuccessfulVerification(UsernameType usernameType, String authUserId) {
        if(!repository.existsByEntityId(authUserId)){
            return;
        }
        U user = findByEntityId(authUserId);
        switch (usernameType){
            case EMAIL -> user.setEmailVerified(true);
            case PHONE_NUMBER -> user.setPhoneNumberVerified(true);
        }
        save(user);
    }
}
