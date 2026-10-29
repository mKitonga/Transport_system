package com.transport.authentication.Service;

import com.transport.authentication.Entity.UsernameType;

public interface OnUsernameVerificationListener {
    void onSuccessfulVerification(UsernameType usernameType, String authUserId);
}
