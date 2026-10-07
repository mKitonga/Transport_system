package com.transport.Authentication.Service;

import com.transport.Authentication.Entity.VerificationCode;
import com.transport.Authentication.Repository.VerificationCodeRepository;
import com.transport.Authentication.form.VerificationCodeRequestForm;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class VerificationCodeService extends BaseJpaRepoEditService<VerificationCode, VerificationCodeRepository> {
    private final Integer MAX_CODE_COUNT = 3;
    private final Integer VERIFICATION_CODE_LEN = 5;
    private final Integer CODE_VALIDITY_IN_MINUTES = 15;
    private final Integer NUMBER_OF_MINUTES_BEFORE_RETRY = 15;

    private BCryptPasswordEncoder passwordEncoder;
    private boolean logOtpToConsole;

    public VerificationCode generateVerificationCode(VerificationCodeRequestForm form) {
        var optional = repository.findByUsernameTypeAndUsername(form.getUsernameType(), form.getUsername());
        VerificationCode verificationCode = optional.orElseGet(VerificationCode::new);
        var timeBeforeResend = verificationCode.getTimeLastSent().plusMinutes(NUMBER_OF_MINUTES_BEFORE_RETRY);

        if (verificationCode.getRetryCount() >= MAX_CODE_COUNT && timeBeforeResend.isAfter(LocalDateTime.now())) {
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "error.verification.code.limit.exceeded"
            );
        }

        verificationCode.setUsernameType(form.getUsernameType());
        verificationCode.setVerificationCodeUse(form.getVerificationCodeUse());
        verificationCode.setUsername(form.getUsername());
        verificationCode.setRetryCount(verificationCode.getRetryCount() + 1);

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(CODE_VALIDITY_IN_MINUTES);
        verificationCode.setExpiryTime(expiryTime);

        String code = RandomStringUtils.randomNumeric(VERIFICATION_CODE_LEN);
        verificationCode.setOtp(passwordEncoder.encode(code));
        verificationCode.setTimeLastSent(LocalDateTime.now());
        verificationCode = repository.save(verificationCode);

        if (logOtpToConsole) {
            log.info("[DEV ONLY] OTP for {}: {}", form.getUsername(), code);
        } else {
            log.info("Verification code generated for username: {}", form.getUsername());
        }

        return verificationCode;
    }

    public void verifyOtp(String entityId, String otp) {
        VerificationCode verificationCode = findByEntityId(entityId);

        if (verificationCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            repository.delete(verificationCode);
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "error.verification.code.expired"
            );
        }

        boolean codeMatches = passwordEncoder.matches(otp, verificationCode.getOtp());

        if (!codeMatches) {
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "invalid.verification.code"
            );
        }
    }

    public void deleteVerificationCode(String entityId) {
        VerificationCode verificationCode = findByEntityId(entityId);
        repository.delete(verificationCode);
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${verification.otp.log-enabled:false}")
    public void setLogOtpToConsole(boolean logOtpToConsole) {
        this.logOtpToConsole = logOtpToConsole;
    }
}
