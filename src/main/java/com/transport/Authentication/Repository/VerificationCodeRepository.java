package com.transport.Authentication.Repository;

import com.transport.Authentication.Entity.UsernameType;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import com.transport.Authentication.Entity.VerificationCode;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends BaseJpaRepository<VerificationCode> {
    Optional<VerificationCode> findByUsernameTypeAndUsername(UsernameType usernameType, String username);
}
