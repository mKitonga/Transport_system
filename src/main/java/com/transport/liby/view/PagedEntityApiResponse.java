package com.transport.liby.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedEntityApiResponse<T> extends EntityApiResponse<List<T>> {

    private Long totalElementCount;
    private int pageCount;
    private int pageSize;
    private int pageNum;
    private boolean hasPrevious;
    private boolean hasNext;

    public PagedEntityApiResponse() {}

    public PagedEntityApiResponse(Page<?> page, List<T> data) {
        setData(data);
        setTotalElementCount(page.getTotalElements());
        setPageCount(page.getTotalPages());
        setPageSize(page.getSize());
        setPageNum(page.getNumber());
        setHasPrevious(page.hasPrevious());
        setHasNext(page.hasNext());
    }

    public PagedEntityApiResponse(boolean status, int statusCode, String message, List<T> data) {
        super(status, statusCode, message, data);
    }

}
