package com.transport.sts.form;

import com.transport.liby.form.BaseFetchForm;
import com.transport.user.entity.UserStatus;
import java.time.LocalDateTime;

/**
 * Form used for searching and paginating STS users.
 */
public class StsUserFetchForm extends BaseFetchForm {

    private UserStatus userStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // ---------------------------------------------------------------------
    // Getters & setters
    // ---------------------------------------------------------------------
    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
