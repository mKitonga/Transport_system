package com.transport.User.form;

import com.transport.User.entity.UserType;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.text.WordUtils;
import com.transport.liby.form.SessionUserIdForm;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userEditForm extends SessionUserIdForm {
    private static final int MAX_NAME_LEN = 100;
    @NotBlank(message = "error.invalid.name")
    private String name;

    @Email(message = "error.invalid.email")
    private String email;

    @NotBlank(message = "error.invalid.phone.number")
    private String phoneNumber;

    @NotNull
    private UserType userType;

    public String getName() {
        if (name != null && name.length() > MAX_NAME_LEN) {
            name = name.substring(0, MAX_NAME_LEN);
        }
        return name == null ? null :
                WordUtils.capitalize(name.toLowerCase()).trim();
    }
    public String getEmail() {
        return StringUtils.isBlank(email) ? null : email.trim().toLowerCase();
    }
    public String getPhoneNumber() {
        return phoneNumber == null ? null : phoneNumber.trim();
    }
}
