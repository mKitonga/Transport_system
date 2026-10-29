package com.transport.authentication.Repository;

import com.transport.authentication.Entity.UsernameType;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import com.transport.authentication.Entity.VerificationCode;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends BaseJpaRepository<VerificationCode> {
    Optional<VerificationCode> findByUsernameTypeAndUsername(UsernameType usernameType, String username);
}
