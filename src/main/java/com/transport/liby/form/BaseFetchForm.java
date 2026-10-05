package com.transport.liby.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseFetchForm extends SessionUserIdForm {
    private String query;
    private Integer pageNum;
    private Integer pageSize;

    private String getQuery(){
        return query == null ? "" : query.trim();
    }

    private Integer getPageNum(){
        return pageNum == null
                ? 0
                : pageNum;
    }

    private Integer getPageSize(){
        return pageSize == null
                ? 40
                : pageSize;
    }
}
