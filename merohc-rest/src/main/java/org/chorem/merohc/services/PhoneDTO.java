package org.chorem.merohc.services;

import org.chorem.merhoc.entities.Company;
import org.chorem.merhoc.entities.Contact;
import org.chorem.merhoc.entities.Phone;

/**
 * Created by couteau on 20/12/15.
 */
public class PhoneDTO {
    protected String id;
    protected String number;
    protected String name;
    protected String type;
    protected String companyId;
    protected String contactId;

    public PhoneDTO (Phone phone) {
        this.id = phone.getTopiaId();
        this.number = phone.getNumber();
        this.type = phone.getType();
        this.name = phone.getName();

        Company company = phone.getCompany();
        if (null != company){
            this.companyId = company.getTopiaId();
        }

        Contact contact = phone.getContact();
        if (null != contact){
            this.contactId = contact.getTopiaId();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
