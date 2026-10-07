package com.transport.User.service;

import com.transport.User.entity.User;
import com.transport.User.repository.UserRepository;
import com.transport.liby.service.BaseJpaRepoReadService;

public abstract class BaseUserReadService<U extends User, R extends UserRepository<U>> extends BaseJpaRepoReadService<U, R> {

    public boolean existsByPhoneNumber(String phoneNumber){
        var spec = repository.notDeleted()
                .and(repository.phoneNumberIs(phoneNumber));
        return repository.exists(spec);
    }

    public boolean existsByEmail(String email){
        var spec = repository.notDeleted()
                .and(repository.emailIs(email));
        return repository.exists(spec);
    }
}
