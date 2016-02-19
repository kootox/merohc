package org.chorem.merohc.bean;

import org.chorem.merohc.entities.InvoiceCategory;

public class InvoiceCategoryDTO {

    protected String id;
    protected String name;

    public InvoiceCategoryDTO() {}

    public InvoiceCategoryDTO (InvoiceCategory category) {
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
