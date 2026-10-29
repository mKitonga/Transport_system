package com.transport.fares.form;

import com.transport.liby.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchFaresForm extends BaseFetchForm {
    private String routeId;
}
