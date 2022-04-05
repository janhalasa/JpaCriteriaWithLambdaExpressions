package com.halasa.criterialambda.dao;

import java.util.List;

public class ResultPage<T> {

    private final Long totalCount;
    private final Integer pageSize;
    private final Integer pageNumber;
    private final List<T> results;

    public ResultPage(
            Long totalCount,
            Integer pageNumber,
            Integer pageSize,
            List<T> results) {
        this.totalCount = totalCount;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.results = results;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public List<T> getResults() {
        return results;
    }
}
