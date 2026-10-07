package com.transport.Authentication.Service;

import com.transport.Authentication.Entity.UsernameType;

public interface OnUsernameVerificationListener {
    void onSuccessfulVerification(UsernameType usernameType, String authUserId);
}
