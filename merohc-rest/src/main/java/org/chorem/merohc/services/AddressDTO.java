package org.chorem.merohc.services;

import org.chorem.merhoc.entities.Address;

/**
 * Created by couteau on 19/12/15.
 */
public class AddressDTO {

    protected String address1;

    protected String address2;

    protected String zipCode;

    protected String city;

    protected String country;

    protected String id;

    protected String companyId;

    protected String name;

    public AddressDTO(Address address) {

        super();

        this.address1 = address.getAddress1();
        this.address2 = address.getAddress2();
        this.zipCode = address.getZipCode();
        this.city = address.getCity();
        this.country = address.getCountry();
        this.id = address.getTopiaId();
        this.companyId = address.getCompany().getTopiaId();
        this.name = address.getName();

    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
