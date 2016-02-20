package org.chorem.merohc.bean;

import org.chorem.merohc.entities.BillCategory;

import java.io.Serializable;

public class BillCategoryDTO implements Serializable {

    protected String id;
    protected String name;

    public BillCategoryDTO() {}

    public BillCategoryDTO (BillCategory category) {
        this.id = category.getTopiaId();
        this.name = category.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}