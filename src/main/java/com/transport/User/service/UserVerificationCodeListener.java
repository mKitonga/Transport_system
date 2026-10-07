package com.transport.User.service;

import com.transport.Authentication.Entity.UsernameType;
import com.transport.Authentication.Service.OnUsernameVerificationListener;
import com.transport.User.entity.User;
import com.transport.User.repository.UserRepository;
import com.transport.liby.service.BaseJpaRepoEditService;

import static com.transport.Authentication.Entity.UsernameType.EMAIL;
import static com.transport.Authentication.Entity.UsernameType.PHONE_NUMBER;

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
