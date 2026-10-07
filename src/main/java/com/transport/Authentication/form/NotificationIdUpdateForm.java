package com.transport.Authentication.form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationIdUpdateForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.notification.id")
    private String notificationId;

    private String createdById;
}
