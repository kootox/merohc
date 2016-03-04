package org.chorem.merohc.bean;

import org.chorem.merohc.entities.Company;

public class CompanyDTO {
    protected String name;
    protected String id;
    protected String account;

    public CompanyDTO(Company company){
        super();
        this.name = company.getName();
        this.id = company.getTopiaId();
        this.account = company.getAccount();
    }

    public CompanyDTO() {
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
