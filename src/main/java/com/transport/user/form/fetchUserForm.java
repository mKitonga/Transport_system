package com.transport.user.form;

import com.transport.user.entity.UserStatus;
import com.transport.liby.form.BaseDatedFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class fetchUserForm extends BaseDatedFetchForm {
    private UserStatus userStatus;
}
