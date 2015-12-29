package org.chorem.merohc.services.v1;

import java.util.ArrayList;
import java.util.List;

import org.chorem.merhoc.entities.*;
import org.chorem.merohc.bean.CompanyDTO;
import org.chorem.merohc.bean.ContactDTO;
import org.chorem.merohc.services.MerohcPersistenceContextSingleton;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.nuiton.topia.persistence.TopiaQueryException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class CompanyController {

    MerohcTopiaPersistenceContext persistenceContext = MerohcPersistenceContextSingleton.getInstance();

    CompanyTopiaDao companyDao =
        persistenceContext.getCompanyDao();

    ContactTopiaDao contactDao = persistenceContext.getContactDao();

    @RequestMapping("/v1/company")
    public List<CompanyDTO> listCompanies() {
        List<Company> companies = companyDao.findAll();

        List<CompanyDTO> dtos = new ArrayList<CompanyDTO>();

        for (Company company:companies) {
            CompanyDTO dto = new CompanyDTO(company);
            dtos.add(dto);
        }

        return dtos;
    }

    @RequestMapping(value="/v1/company", method= RequestMethod.PUT)
    public CompanyDTO addCompany(@RequestParam(value="name") String name,
                                 @RequestParam(value="type", required = false) String type) {
        Company companyToStore = companyDao.create();
        companyToStore.setName(name);
        persistenceContext.commit();
        CompanyDTO dto = new CompanyDTO(companyToStore);
        return dto;
    }

    @RequestMapping(value="/v1/company/{id:.+}", method= RequestMethod.GET)
    public CompanyDTO getCompany(@PathVariable String id) {
        try {
            Company company = companyDao.forTopiaIdEquals(id).findAny();
            CompanyDTO dto = new CompanyDTO(company);
            return dto;
        } catch (TopiaNoResultException tnre) {
            return null;
        }
    }

    @RequestMapping(value="/v1/company/{id:.+}", method= RequestMethod.DELETE)
    public void deleteCompany(@PathVariable String id) {
        try {
            Company company = companyDao.forTopiaIdEquals(id).findAny();
            companyDao.delete(company);
            persistenceContext.commit();
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @RequestMapping(value="/v1/company", method= RequestMethod.POST)
    public CompanyDTO editCompany(@RequestParam(value="id") String id,
                                  @RequestParam(value="name") String name,
                                  @RequestParam(value="type", required = false) String type) {

        Company company = companyDao.forTopiaIdEquals(id).findAny();
        //FIXME JC151211 - Deal with TopiaNoResultException
        company.setName(name);
        persistenceContext.commit();
        CompanyDTO dto = new CompanyDTO(company);
        return dto;
    }

    @RequestMapping(value="/v1/company/{id}/contact", method= RequestMethod.GET)
    public List<ContactDTO> getContacts(@PathVariable String id) {

        List<Contact> contacts;

        List <ContactDTO> dtos = new ArrayList<ContactDTO>();

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            contacts = contactDao.forCompanyEquals(company)
                                   .findAll();

            for (Contact contact:contacts) {
                dtos.add(new ContactDTO(contact));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/company/{id}/contact", method= RequestMethod.PUT)
    public ContactDTO addContact(@PathVariable String id,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam Boolean active,
                                 @RequestParam String description) {

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        Contact contact = contactDao.create();

        if (company != null){
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setCompany(company);
            contact.setActive(active);
            contact.setDescription(description);
        }

        persistenceContext.commit();
        ContactDTO dto = new ContactDTO(contact);
        return dto;
    }

    @RequestMapping(value="/v1/contact/{id:.+}", method= RequestMethod.GET)
    public ContactDTO getContact(@PathVariable String id) {

        ContactDTO dto = null;

        try {
            Contact contact = contactDao.forTopiaIdEquals(id).findAny();
            dto = new ContactDTO(contact);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @RequestMapping(value="/v1/contact", method= RequestMethod.POST)
    public ContactDTO editContact(@RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String id,
                                  @RequestParam Boolean active,
                                  @RequestParam String description) {

        Contact contact = contactDao.forTopiaIdEquals(id).findAnyOrNull();

        if (contact != null){
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setActive(active);
            contact.setDescription(description);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        persistenceContext.commit();
        ContactDTO dto = new ContactDTO(contact);
        return dto;
    }

    @RequestMapping(value="/v1/contact/{id:.+}", method= RequestMethod.DELETE)
    public void deleteContact(@PathVariable String id) {
        try {
            Contact contact = contactDao.forTopiaIdEquals(id).findAny();
            contactDao.delete(contact);
            persistenceContext.commit();
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }
}
