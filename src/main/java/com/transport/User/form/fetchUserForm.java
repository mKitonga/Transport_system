package com.transport.User.form;

import com.transport.User.entity.UserStatus;
import com.transport.liby.form.BaseDatedFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class fetchUserForm extends BaseDatedFetchForm {
    private UserStatus userStatus;
}
