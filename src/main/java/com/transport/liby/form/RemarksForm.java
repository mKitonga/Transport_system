package com.transport.liby.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemarksForm extends SessionUserIdForm {
    private String remarks;

    public String getRemarks() {
        if(remarks != null){
            remarks = remarks.trim().replaceAll(" +", " ");
        }
        return remarks;
    }
}
