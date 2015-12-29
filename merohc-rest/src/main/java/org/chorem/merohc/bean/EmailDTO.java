package org.chorem.merohc.bean;

import org.chorem.merhoc.entities.Company;
import org.chorem.merhoc.entities.Contact;
import org.chorem.merhoc.entities.Email;

public class EmailDTO {

    protected String value;

    protected String name;

    protected String id;

    protected String companyId;

    protected String contactId;

    public EmailDTO(Email email){
        super();
        this.id = email.getTopiaId();
        this.name = email.getName();
        this.value = email.getEmail();

        Company company = email.getCompany();
        if (null != company){
            this.companyId = company.getTopiaId();
        }

        Contact contact = email.getContact();
        if (null != contact){
            this.contactId = contact.getTopiaId();
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String email) {
        this.value = email;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
