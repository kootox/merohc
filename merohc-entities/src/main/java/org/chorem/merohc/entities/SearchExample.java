package org.chorem.merohc.entities;

import org.nuiton.topia.persistence.TopiaEntity;
import org.nuiton.util.pagination.PaginationParameter;

import java.io.Serializable;
import java.util.List;

/**
 * @author ymartel (martel@codelutin.com)
 */
public abstract class SearchExample<E extends TopiaEntity> implements Serializable {

    public static final int DEFAULT_PAGE_SIZE = -1;
    public static final int DEFAULT_PAGE = 0;

    public PaginationParameter getPaginationParameter() {

        PaginationParameter paginationParameter = null;
        if (this.limit != null) {
            int wantedPage = this.page != null ? this.page.intValue() : 0;
            paginationParameter = PaginationParameter.of(wantedPage, this.limit.intValue(), getOrderClause(), isOrderDesc());
        } else {
            paginationParameter = PaginationParameter.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, getOrderClause(), isOrderDesc());
        }

        return paginationParameter;
    }

    protected E example;

    protected List<String> globalSearch;

    // Pagination Parameters
    protected Integer limit;
    protected Integer page;

    public E getExample() {
        return example;
    }

    public void setExample(E example) {
        this.example = example;
    }

    public List<String> getGlobalSearch() {
        return globalSearch;
    }

    public void setGlobalSearch(List<String> globalSearch) {
        this.globalSearch = globalSearch;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit == null) {
            this.limit = DEFAULT_PAGE_SIZE;
        } else {
            this.limit = limit;
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page == null) {
            this.page = DEFAULT_PAGE;
        } else {
            this.page = page;
        }
    }

    public abstract String getOrderClause();
    public abstract boolean isOrderDesc();
}
