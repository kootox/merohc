package org.chorem.merohc.services;

import org.chorem.merhoc.entities.Contact;

/**
 * Created by couteau on 15/12/15.
 */
public class ContactDTO {

    protected String id;
    protected String firstName;
    protected String lastName;
    protected Boolean active;
    protected String companyId;

    public ContactDTO(Contact contact){
        super();
        this.id=contact.getTopiaId();
        this.firstName = contact.getFirstName();
        this.lastName = contact.getLastName();
        this.active = contact.getActive();
        this.companyId = contact.getCompany().getTopiaId();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
