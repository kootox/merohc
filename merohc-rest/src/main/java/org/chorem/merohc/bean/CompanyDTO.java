package org.chorem.merohc.bean;

import org.chorem.merhoc.entities.Company;

public class CompanyDTO {
    protected String name;
    protected String id;

    public CompanyDTO(Company company){
        super();
        this.name = company.getName();
        this.id = company.getTopiaId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
