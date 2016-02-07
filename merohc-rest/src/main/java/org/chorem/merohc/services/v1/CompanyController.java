package org.chorem.merohc.services.v1;

import java.util.ArrayList;
import java.util.List;

import org.chorem.merohc.bean.CompanyDTO;
import org.chorem.merohc.bean.ContactDTO;
import org.chorem.merohc.entities.Company;
import org.chorem.merohc.entities.CompanyTopiaDao;
import org.chorem.merohc.entities.Contact;
import org.chorem.merohc.entities.ContactTopiaDao;
import org.nuiton.topia.persistence.TopiaDao;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.nuiton.topia.persistence.TopiaQueryException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
@Transactional
public class CompanyController extends AbstractService {

    @ResponseBody
    @RequestMapping(value="/v1/company", method= RequestMethod.GET)
    public List<CompanyDTO> listCompanies() {
        List<Company> companies = getCompanyDao().findAll();

        List<CompanyDTO> dtos = new ArrayList<CompanyDTO>();

        for (Company company:companies) {
            CompanyDTO dto = new CompanyDTO(company);
            dtos.add(dto);
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/add", method= RequestMethod.POST)
    public CompanyDTO addCompany(@RequestParam(value="name") String name,
                                 @RequestParam(value="type", required = false) String type) {
        Company companyToStore = getCompanyDao().create();
        companyToStore.setName(name);
        CompanyDTO dto = new CompanyDTO(companyToStore);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id:.+}", method= RequestMethod.GET)
    public CompanyDTO getCompany(@PathVariable String id) {
        try {
            Company company = getCompanyDao().forTopiaIdEquals(id).findAny();
            CompanyDTO dto = new CompanyDTO(company);
            return dto;
        } catch (TopiaNoResultException tnre) {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id:.+}", method= RequestMethod.DELETE)
    public void deleteCompany(@PathVariable String id) {
        try {
            CompanyTopiaDao companyDao = getCompanyDao();
            Company company = companyDao.forTopiaIdEquals(id).findAny();
            companyDao.delete(company);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/company", method= RequestMethod.POST)
    public CompanyDTO editCompany(@RequestParam(value="id") String id,
                                  @RequestParam(value="name") String name,
                                  @RequestParam(value="type", required = false) String type) {

        Company company = getCompanyDao().forTopiaIdEquals(id).findAny();
        //FIXME JC151211 - Deal with TopiaNoResultException
        company.setName(name);
        CompanyDTO dto = new CompanyDTO(company);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/contact", method= RequestMethod.GET)
    public List<ContactDTO> getContacts(@PathVariable String id) {

        List<Contact> contacts;

        List <ContactDTO> dtos = new ArrayList<ContactDTO>();

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        try {
            contacts = getContactDao().forCompanyEquals(company).findAll();

            for (Contact contact:contacts) {
                dtos.add(new ContactDTO(contact));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/company/{id}/contact", method= RequestMethod.PUT)
    public ContactDTO addContact(@PathVariable String id,
                                 @RequestBody ContactDTO contact) {

        Company company = getCompanyDao().forTopiaIdEquals(id).findAnyOrNull();

        Contact contactEntity = getContactDao().create();

        if (company != null){
            contactEntity.setFirstName(contact.getFirstName());
            contactEntity.setLastName(contact.getLastName());
            contactEntity.setCompany(company);
            contactEntity.setActive(contact.isActive());
            contactEntity.setDescription(contact.getDescription());
        }

        ContactDTO dto = new ContactDTO(contactEntity);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact", method= RequestMethod.GET)
    public List<ContactDTO> getContacts() {

        List<Contact> contacts = getContactDao().findAll();

        List<ContactDTO> dtos = new ArrayList<ContactDTO>();

        for (Contact contact:contacts) {
            ContactDTO dto = new ContactDTO(contact);
            dtos.add(dto);
        }

        return dtos;
    }


    @ResponseBody
    @RequestMapping(value="/v1/contact/{id:.+}", method= RequestMethod.GET)
    public ContactDTO getContact(@PathVariable String id) {

        ContactDTO dto = null;

        try {
            Contact contact = getContactDao().forTopiaIdEquals(id).findAny();
            dto = new ContactDTO(contact);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact", method= RequestMethod.POST)
    public ContactDTO editContact(@RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String id,
                                  @RequestParam Boolean active,
                                  @RequestParam String description) {

        Contact contact = getContactDao().forTopiaIdEquals(id).findAnyOrNull();

        if (contact != null){
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setActive(active);
            contact.setDescription(description);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        ContactDTO dto = new ContactDTO(contact);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/contact/{id:.+}", method= RequestMethod.DELETE)
    public void deleteContact(@PathVariable String id) {
        try {
            Contact contact = getContactDao().forTopiaIdEquals(id).findAny();
            getContactDao().delete(contact);
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
}
