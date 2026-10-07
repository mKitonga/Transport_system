package com.transport.liby.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseFetchForm extends SessionUserIdForm {
    private String query;
    private Integer pageNum;
    private Integer pageSize;

    public String getQuery(){
        return query == null ? "" : query.trim();
    }

    public Integer getPageNum(){
        return pageNum == null
                ? 0
                : pageNum;
    }

    public Integer getPageSize(){
        return pageSize == null
                ? 40
                : pageSize;
    }
}
