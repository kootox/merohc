package org.chorem.merohc.services;

import org.chorem.merhoc.entities.*;
import org.chorem.merohc.MerohcApplicationConfig;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.nuiton.topia.persistence.TopiaQueryException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class ContactDetailsController {

    MerohcApplicationConfig configuration = new MerohcApplicationConfig();

    MerohcTopiaApplicationContext applicationContext =
            new MerohcTopiaApplicationContext(configuration.getTopiaProperties());

    MerohcTopiaPersistenceContext persistenceContext =
            applicationContext.newPersistenceContext();

    CompanyTopiaDao companyDao =
            persistenceContext.getCompanyDao();

    ContactTopiaDao contactDao = persistenceContext.getContactDao();

    EmailTopiaDao emailDao = persistenceContext.getEmailDao();

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
}
