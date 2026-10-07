package com.transport.User.service;

import com.transport.Authentication.Entity.UsernameType;
import com.transport.User.entity.User;
import com.transport.User.entity.UserStatus;
import com.transport.User.form.userEditForm;
import com.transport.User.repository.UserRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.SessionUserIdForm;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseUserEditService<U extends User, R extends UserRepository<U>>
        extends BaseJpaRepoEditService<U, R> {

    protected U register(userEditForm form) {
        U user = getNewUser();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setUserStatus(UserStatus.PENDING_APPROVAL);
        user.setUserType(form.getUserType());
        return save(user);
    }

    public U edit(String userId, userEditForm form) {
        U user = findByEntityId(userId);
        if (!StringUtils.equals(user.getPhoneNumber(), form.getPhoneNumber())) {
            checkPhoneNumberExists(form.getPhoneNumber(), userId);
            user.setPhoneNumber(form.getPhoneNumber());
        }
        if (!StringUtils.equals(user.getEmail(), form.getEmail())) {
            checkEmailExists(form.getEmail(), userId);
            user.setEmail(form.getEmail());
        }
        user.setName(form.getName());
        return save(user);
    }

    private void checkPhoneNumberExists(String phoneNumber, String userId) {
        boolean exists = repository.existsByPhoneNumberAndEntityIdNot(phoneNumber, userId);
        if (exists) {
            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS, "error.user.phone.number.exists");
        }
    }

    private void checkEmailExists(String email, String userId) {
        if (StringUtils.isBlank(email)) {
            return;
        }
        boolean exists = repository.existsByEmailAndEntityIdNot(email, userId);
        if (exists) {
            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS, "error.user.email.exists");
        }
    }

    public U suspend(String userId, SessionUserIdForm form) {
        U user = findByEntityId(userId);
        user.setUserStatus(UserStatus.SUSPENDED);
        return save(user);
    }

    public U activate(String userId, SessionUserIdForm form) {
        U user = findByEntityId(userId);
        user.setUserStatus(UserStatus.ACTIVE);
        return save(user);
    }

    protected abstract U getNewUser();
}
