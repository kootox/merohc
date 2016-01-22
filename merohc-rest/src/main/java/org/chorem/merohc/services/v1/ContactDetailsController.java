package org.chorem.merohc.services.v1;

import org.chorem.merohc.bean.AddressDTO;
import org.chorem.merohc.bean.EmailDTO;
import org.chorem.merohc.bean.PhoneDTO;
import org.chorem.merohc.entities.Address;
import org.chorem.merohc.entities.AddressTopiaDao;
import org.chorem.merohc.entities.Company;
import org.chorem.merohc.entities.CompanyTopiaDao;
import org.chorem.merohc.entities.Contact;
import org.chorem.merohc.entities.ContactTopiaDao;
import org.chorem.merohc.entities.Email;
import org.chorem.merohc.entities.EmailTopiaDao;
import org.chorem.merohc.entities.Phone;
import org.chorem.merohc.entities.PhoneTopiaDao;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.nuiton.topia.persistence.TopiaQueryException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@Transactional
public class ContactDetailsController extends AbstractService {

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/email", method= RequestMethod.GET)
    public List<EmailDTO> getEmailsForCompany(@PathVariable String id) {

        List<Email> emails;

        List <EmailDTO> dtos = new ArrayList<EmailDTO>();

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        try {
            emails = getEmailDao().forCompanyEquals(company).findAll();

            for (Email email:emails) {
                dtos.add(new EmailDTO(email));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact/{id}/email", method= RequestMethod.GET)
    public List<EmailDTO> getEmailsForContact(@PathVariable String id) {

        List<Email> emails;

        List <EmailDTO> dtos = new ArrayList<EmailDTO>();

        Contact contact = getContactDao().forTopiaIdEquals(id).findAnyOrNull();

        try {
            emails = getEmailDao().forContactEquals(contact).findAll();

            for (Email email:emails) {
                dtos.add(new EmailDTO(email));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/email", method= RequestMethod.PUT)
    public EmailDTO addEmailToCompany(@PathVariable String id,
                                      @RequestBody EmailDTO email) {

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        Email emailEntity = getEmailDao().create();

        if (company != null){
            emailEntity.setName(email.getName());
            emailEntity.setEmail(email.getCompanyId());
            emailEntity.setCompany(company);
        }

        getPersistenceContext().commit();
        EmailDTO dto = new EmailDTO(emailEntity);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact/{id}/email", method= RequestMethod.PUT)
    public EmailDTO addEmailToContact(@PathVariable String id,
                                      @RequestBody EmailDTO email) {

        Contact contact = getContactDao().forTopiaIdEquals(id).findAnyOrNull();

        Email emailEntity = getEmailDao().create();

        if (contact != null){
            emailEntity.setName(email.getName());
            emailEntity.setEmail(email.getValue());
            emailEntity.setContact(contact);
        }

        EmailDTO dto = new EmailDTO(emailEntity);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/email/{id:.+}", method= RequestMethod.GET)
    public EmailDTO getEmail(@PathVariable String id) {

        EmailDTO dto = null;

        try {
            Email email = getEmailDao().forTopiaIdEquals(id).findAny();
            dto = new EmailDTO(email);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/email", method= RequestMethod.POST)
    public EmailDTO editEmail(@RequestParam String name,
                              @RequestParam String value,
                              @RequestParam String id) {

        Email email = getEmailDao().forTopiaIdEquals(id).findAnyOrNull();

        if (email != null){
            email.setName(name);
            email.setEmail(value);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        EmailDTO dto = new EmailDTO(email);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/email/{id:.+}", method= RequestMethod.DELETE)
    public void deleteEmail(@PathVariable String id) {
        try {
            EmailTopiaDao emailDao = getEmailDao();
            Email email = emailDao.forTopiaIdEquals(id).findAny();
            emailDao.delete(email);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/address", method= RequestMethod.GET)
    public List<AddressDTO> getAddressesForCompany(@PathVariable String id) {

        List<Address> addresses;

        List <AddressDTO> dtos = new ArrayList<AddressDTO>();

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        try {
            addresses = getAddressDao().forCompanyEquals(company).findAll();

            for (Address address:addresses) {
                dtos.add(new AddressDTO(address));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact/{id}/address", method= RequestMethod.GET)
    public List<AddressDTO> getAddressesForContact(@PathVariable String id) {

        List<Address> addresses;

        List <AddressDTO> dtos = new ArrayList<AddressDTO>();

        Contact contact = getContactDao().forTopiaIdEquals(id).findAnyOrNull();

        try {
            addresses = getAddressDao().forContactEquals(contact).findAll();

            for (Address address:addresses) {
                dtos.add(new AddressDTO(address));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/address", method= RequestMethod.PUT)
    public AddressDTO addAddressToCompany(@PathVariable String id,
                                          @RequestBody AddressDTO addressDTO) {

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        Address address = getAddressDao().create();

        if (company != null){
            address.setName(addressDTO.getName());
            address.setAddress1(addressDTO.getAddress1());
            address.setAddress2(addressDTO.getAddress2());
            address.setCity(addressDTO.getCity());
            address.setZipCode(addressDTO.getZipCode());
            address.setCountry(addressDTO.getCountry());
            address.setCompany(company);
        }

        AddressDTO dto = new AddressDTO(address);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact/{id}/address", method= RequestMethod.PUT)
    public AddressDTO addAddressToContact(@PathVariable String id,
                                          @RequestBody AddressDTO addressDTO) {

        Contact contact = getContactDao().forTopiaIdEquals(id).findAnyOrNull();

        Address address = getAddressDao().create();

        if (contact != null){
            address.setName(addressDTO.getName());
            address.setAddress1(addressDTO.getAddress1());
            address.setAddress2(addressDTO.getAddress2());
            address.setCity(addressDTO.getCity());
            address.setZipCode(addressDTO.getZipCode());
            address.setCountry(addressDTO.getCountry());
            address.setContact(contact);
        }

        AddressDTO dto = new AddressDTO(address);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/address/{id:.+}", method= RequestMethod.GET)
    public AddressDTO getAddress(@PathVariable String id) {

        AddressDTO dto = null;

        try {
            Address address = getAddressDao().forTopiaIdEquals(id).findAny();
            dto = new AddressDTO(address);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/address", method= RequestMethod.POST)
    public AddressDTO editAddress(@RequestParam String id,
                                  @RequestParam String address1,
                                  @RequestParam String address2,
                                  @RequestParam String zipCode,
                                  @RequestParam String city,
                                  @RequestParam String country,
                                  @RequestParam String name) {

        Address address = getAddressDao().forTopiaIdEquals(id).findAnyOrNull();

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

        AddressDTO dto = new AddressDTO(address);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/address/{id:.+}", method= RequestMethod.DELETE)
    public void deleteAddress(@PathVariable String id) {
        try {
            AddressTopiaDao addressDao = getAddressDao();
            Address address = addressDao.forTopiaIdEquals(id).findAny();
            addressDao.delete(address);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/phone", method= RequestMethod.GET)
    public List<PhoneDTO> getPhonesForCompany(@PathVariable String id) {

        List<Phone> phones;

        List <PhoneDTO> dtos = new ArrayList<PhoneDTO>();

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        try {
            phones = getPhoneDao().forCompanyEquals(company).findAll();

            for (Phone phone:phones) {
                dtos.add(new PhoneDTO(phone));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact/{id}/phone", method= RequestMethod.GET)
    public List<PhoneDTO> getPhonesForContact(@PathVariable String id) {

        List<Phone> phones;

        List <PhoneDTO> dtos = new ArrayList<PhoneDTO>();

        Contact contact = getContactDao().forTopiaIdEquals(id).findAnyOrNull();

        try {
            phones = getPhoneDao().forContactEquals(contact).findAll();

            for (Phone phone:phones) {
                dtos.add(new PhoneDTO(phone));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/phone", method= RequestMethod.PUT)
    public PhoneDTO addPhoneToCompany(@PathVariable String id,
                                      @RequestBody PhoneDTO phoneDTO) {

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        Phone phone = getPhoneDao().create();

        if (company != null){
            phone.setName(phoneDTO.getName());
            phone.setNumber(phoneDTO.getNumber());
            phone.setType(phoneDTO.getType());
            phone.setCompany(company);
        }

        PhoneDTO dto = new PhoneDTO(phone);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact/{id}/phone", method= RequestMethod.PUT)
    public PhoneDTO addPhoneToContact(@PathVariable String id,
                                      @RequestBody PhoneDTO phoneDTO) {

        Contact contact = getContactDao().forTopiaIdEquals(id).findAnyOrNull();

        Phone phone = getPhoneDao().create();

        if (contact != null){
            phone.setName(phoneDTO.getName());
            phone.setNumber(phoneDTO.getNumber());
            phone.setType(phoneDTO.getType());
            phone.setContact(contact);
        }

        PhoneDTO dto = new PhoneDTO(phone);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/phone/{id:.+}", method= RequestMethod.GET)
    public PhoneDTO getPhone(@PathVariable String id) {

        PhoneDTO dto = null;

        try {
            Phone phone = getPhoneDao().forTopiaIdEquals(id).findAny();
            dto = new PhoneDTO(phone);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/phone", method= RequestMethod.POST)
    public PhoneDTO editPhone(@RequestParam String id,
                              @RequestParam String number,
                              @RequestParam String type,
                              @RequestParam String name) {

        Phone phone = getPhoneDao().forTopiaIdEquals(id).findAnyOrNull();

        if (phone != null){
            phone.setName(name);
            phone.setNumber(number);
            phone.setType(type);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        PhoneDTO dto = new PhoneDTO(phone);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/phone/{id:.+}", method= RequestMethod.DELETE)
    public void deletePhone(@PathVariable String id) {
        try {
            PhoneTopiaDao phoneDao = getPhoneDao();
            Phone phone = phoneDao.forTopiaIdEquals(id).findAny();
            phoneDao.delete(phone);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    protected CompanyTopiaDao getCompanyDao() {
        return getPersistenceContext().getCompanyDao();
    }

    protected ContactTopiaDao getContactDao() {
        return getPersistenceContext().getContactDao();
    }

    protected EmailTopiaDao getEmailDao() {
        return getPersistenceContext().getEmailDao();
    }

    protected PhoneTopiaDao getPhoneDao() {
        return getPersistenceContext().getPhoneDao();
    }

    protected AddressTopiaDao getAddressDao() {
        return getPersistenceContext().getAddressDao();
    }
}
