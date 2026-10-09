package com.transport.liby.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Getter
public abstract class BaseDatedFetchForm extends BaseFetchForm {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TimeZone timeZone;

    public void setStartDate(LocalDateTime localDate) {
        if (localDate == null) {
            return;
        }
        this.startDate = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0);
    }

    public LocalDateTime getStartDate() {
        return startDate == null
                ? LocalDateTime.of(0, 1, 1, 0, 0)
                : startDate;
    }

    public void setEndDate(LocalDateTime localDate) {
        if (localDate == null) {
            return;
        }
        this.endDate = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 23, 59);
    }

    public LocalDateTime getEndDate() {
        return endDate == null
                ? LocalDateTime.now()
                : endDate ;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

}
