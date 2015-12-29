package org.chorem.merohc.services.v1;

import org.chorem.merhoc.entities.*;
import org.chorem.merohc.bean.AddressDTO;
import org.chorem.merohc.bean.EmailDTO;
import org.chorem.merohc.bean.PhoneDTO;
import org.chorem.merohc.services.MerohcPersistenceContextSingleton;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.nuiton.topia.persistence.TopiaQueryException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class ContactDetailsController {

    MerohcTopiaPersistenceContext persistenceContext = MerohcPersistenceContextSingleton.getInstance();

    CompanyTopiaDao companyDao =
            persistenceContext.getCompanyDao();

    ContactTopiaDao contactDao = persistenceContext.getContactDao();

    EmailTopiaDao emailDao = persistenceContext.getEmailDao();

    AddressTopiaDao addressDao = persistenceContext.getAddressDao();

    PhoneTopiaDao phoneDao = persistenceContext.getPhoneDao();

    @RequestMapping(value="/v1/company/{id}/email", method= RequestMethod.GET)
    public List<EmailDTO> getEmailsForCompany(@PathVariable String id) {

        List<Email> emails;

        List <EmailDTO> dtos = new ArrayList<EmailDTO>();

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            emails = emailDao.forCompanyEquals(company).findAll();

            for (Email email:emails) {
                dtos.add(new EmailDTO(email));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/contact/{id}/email", method= RequestMethod.GET)
    public List<EmailDTO> getEmailsForContact(@PathVariable String id) {

        List<Email> emails;

        List <EmailDTO> dtos = new ArrayList<EmailDTO>();

        Contact contact = contactDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            emails = emailDao.forContactEquals(contact).findAll();

            for (Email email:emails) {
                dtos.add(new EmailDTO(email));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/company/{id}/email", method= RequestMethod.PUT)
    public EmailDTO addEmailToCompany(@PathVariable String id,
                                 @RequestParam String value,
                                 @RequestParam String name) {

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        Email email = emailDao.create();

        if (company != null){
            email.setName(name);
            email.setEmail(value);
            email.setCompany(company);
        }

        persistenceContext.commit();
        EmailDTO dto = new EmailDTO(email);
        return dto;
    }

    @RequestMapping(value="/v1/contact/{id}/email", method= RequestMethod.PUT)
    public EmailDTO addEmailToContact(@PathVariable String id,
                                      @RequestParam String value,
                                      @RequestParam String name) {

        Contact contact = contactDao.forTopiaIdEquals(id).findAnyOrNull();

        Email email = emailDao.create();

        if (contact != null){
            email.setName(name);
            email.setEmail(value);
            email.setContact(contact);
        }

        persistenceContext.commit();
        EmailDTO dto = new EmailDTO(email);
        return dto;
    }

    @RequestMapping(value="/v1/email/{id:.+}", method= RequestMethod.GET)
    public EmailDTO getEmail(@PathVariable String id) {

        EmailDTO dto = null;

        try {
            Email email = emailDao.forTopiaIdEquals(id).findAny();
            dto = new EmailDTO(email);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @RequestMapping(value="/v1/email", method= RequestMethod.POST)
    public EmailDTO editEmail(@RequestParam String name,
                              @RequestParam String value,
                              @RequestParam String id) {

        Email email = emailDao.forTopiaIdEquals(id).findAnyOrNull();

        if (email != null){
            email.setName(name);
            email.setEmail(value);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        persistenceContext.commit();
        EmailDTO dto = new EmailDTO(email);
        return dto;
    }

    @RequestMapping(value="/v1/email/{id:.+}", method= RequestMethod.DELETE)
    public void deleteEmail(@PathVariable String id) {
        try {
            Email email = emailDao.forTopiaIdEquals(id).findAny();
            emailDao.delete(email);
            persistenceContext.commit();
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @RequestMapping(value="/v1/company/{id}/address", method= RequestMethod.GET)
    public List<AddressDTO> getAddressesForCompany(@PathVariable String id) {

        List<Address> addresses;

        List <AddressDTO> dtos = new ArrayList<AddressDTO>();

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            addresses = addressDao.forCompanyEquals(company).findAll();

            for (Address address:addresses) {
                dtos.add(new AddressDTO(address));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/contact/{id}/address", method= RequestMethod.GET)
    public List<AddressDTO> getAddressesForContact(@PathVariable String id) {

        List<Address> addresses;

        List <AddressDTO> dtos = new ArrayList<AddressDTO>();

        Contact contact = contactDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            addresses = addressDao.forContactEquals(contact).findAll();

            for (Address address:addresses) {
                dtos.add(new AddressDTO(address));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/company/{id}/address", method= RequestMethod.PUT)
    public AddressDTO addAddressToCompany(@PathVariable String id,
                                          @RequestParam String address1,
                                          @RequestParam String address2,
                                          @RequestParam String zipCode,
                                          @RequestParam String city,
                                          @RequestParam String country,
                                          @RequestParam String name) {

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        Address address = addressDao.create();

        if (company != null){
            address.setName(name);
            address.setAddress1(address1);
            address.setAddress2(address2);
            address.setCity(city);
            address.setZipCode(zipCode);
            address.setCountry(country);
            address.setCompany(company);
        }

        persistenceContext.commit();
        AddressDTO dto = new AddressDTO(address);
        return dto;
    }

    @RequestMapping(value="/v1/contact/{id}/address", method= RequestMethod.PUT)
    public AddressDTO addAddressToContact(@PathVariable String id,
                                          @RequestParam String address1,
                                          @RequestParam String address2,
                                          @RequestParam String zipCode,
                                          @RequestParam String city,
                                          @RequestParam String country,
                                          @RequestParam String name) {

        Contact contact = contactDao.forTopiaIdEquals(id).findAnyOrNull();

        Address address = addressDao.create();

        if (contact != null){
            address.setName(name);
            address.setAddress1(address1);
            address.setAddress2(address2);
            address.setCity(city);
            address.setZipCode(zipCode);
            address.setCountry(country);
            address.setContact(contact);
        }

        persistenceContext.commit();
        AddressDTO dto = new AddressDTO(address);
        return dto;
    }

    @RequestMapping(value="/v1/address/{id:.+}", method= RequestMethod.GET)
    public AddressDTO getAddress(@PathVariable String id) {

        AddressDTO dto = null;

        try {
            Address address = addressDao.forTopiaIdEquals(id).findAny();
            dto = new AddressDTO(address);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @RequestMapping(value="/v1/address", method= RequestMethod.POST)
    public AddressDTO editAddress(@RequestParam String id,
                                  @RequestParam String address1,
                                  @RequestParam String address2,
                                  @RequestParam String zipCode,
                                  @RequestParam String city,
                                  @RequestParam String country,
                                  @RequestParam String name) {

        Address address = addressDao.forTopiaIdEquals(id).findAnyOrNull();

        if (address != null){
            address.setName(name);
            address.setAddress1(address1);
            address.setAddress2(address2);
            address.setCity(city);
            address.setZipCode(zipCode);
            address.setCountry(country);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        persistenceContext.commit();
        AddressDTO dto = new AddressDTO(address);
        return dto;
    }

    @RequestMapping(value="/v1/address/{id:.+}", method= RequestMethod.DELETE)
    public void deleteAddress(@PathVariable String id) {
        try {
            Address address = addressDao.forTopiaIdEquals(id).findAny();
            addressDao.delete(address);
            persistenceContext.commit();
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @RequestMapping(value="/v1/company/{id}/phone", method= RequestMethod.GET)
    public List<PhoneDTO> getPhonesForCompany(@PathVariable String id) {

        List<Phone> phones;

        List <PhoneDTO> dtos = new ArrayList<PhoneDTO>();

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            phones = phoneDao.forCompanyEquals(company).findAll();

            for (Phone phone:phones) {
                dtos.add(new PhoneDTO(phone));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/contact/{id}/phone", method= RequestMethod.GET)
    public List<PhoneDTO> getPhonesForContact(@PathVariable String id) {

        List<Phone> phones;

        List <PhoneDTO> dtos = new ArrayList<PhoneDTO>();

        Contact contact = contactDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            phones = phoneDao.forContactEquals(contact).findAll();

            for (Phone phone:phones) {
                dtos.add(new PhoneDTO(phone));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/company/{id}/phone", method= RequestMethod.PUT)
    public PhoneDTO addPhoneToCompany(@PathVariable String id,
                                      @RequestParam String number,
                                      @RequestParam String type,
                                      @RequestParam String name) {

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        Phone phone = phoneDao.create();

        if (company != null){
            phone.setName(name);
            phone.setNumber(number);
            phone.setType(type);
            phone.setCompany(company);
        }

        persistenceContext.commit();
        PhoneDTO dto = new PhoneDTO(phone);
        return dto;
    }

    @RequestMapping(value="/v1/contact/{id}/phone", method= RequestMethod.PUT)
    public PhoneDTO addPhoneToContact(@PathVariable String id,
                                      @RequestParam String number,
                                      @RequestParam String type,
                                      @RequestParam String name) {

        Contact contact = contactDao.forTopiaIdEquals(id).findAnyOrNull();

        Phone phone = phoneDao.create();

        if (contact != null){
            phone.setName(name);
            phone.setNumber(number);
            phone.setType(type);
            phone.setContact(contact);
        }

        persistenceContext.commit();
        PhoneDTO dto = new PhoneDTO(phone);
        return dto;
    }

    @RequestMapping(value="/v1/phone/{id:.+}", method= RequestMethod.GET)
    public PhoneDTO getPhone(@PathVariable String id) {

        PhoneDTO dto = null;

        try {
            Phone phone = phoneDao.forTopiaIdEquals(id).findAny();
            dto = new PhoneDTO(phone);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @RequestMapping(value="/v1/phone", method= RequestMethod.POST)
    public PhoneDTO editPhone(@RequestParam String id,
                              @RequestParam String number,
                              @RequestParam String type,
                              @RequestParam String name) {

        Phone phone = phoneDao.forTopiaIdEquals(id).findAnyOrNull();

        if (phone != null){
            phone.setName(name);
            phone.setNumber(number);
            phone.setType(type);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        persistenceContext.commit();
        PhoneDTO dto = new PhoneDTO(phone);
        return dto;
    }

    @RequestMapping(value="/v1/phone/{id:.+}", method= RequestMethod.DELETE)
    public void deletePhone(@PathVariable String id) {
        try {
            Phone phone = phoneDao.forTopiaIdEquals(id).findAny();
            phoneDao.delete(phone);
            persistenceContext.commit();
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }
}
