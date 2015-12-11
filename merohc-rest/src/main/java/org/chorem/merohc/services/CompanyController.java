package org.chorem.merohc.services;

import java.util.ArrayList;
import java.util.List;

import org.chorem.merhoc.entities.CompanyTopiaDao;
import org.chorem.merhoc.entities.Company;
import org.chorem.merhoc.entities.MerohcTopiaApplicationContext;
import org.chorem.merhoc.entities.MerohcTopiaPersistenceContext;
import org.chorem.merohc.MerohcApplicationConfig;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class CompanyController {

    MerohcApplicationConfig configuration = new MerohcApplicationConfig();

    MerohcTopiaApplicationContext applicationContext =
        new MerohcTopiaApplicationContext(configuration.getTopiaProperties());

    MerohcTopiaPersistenceContext persistenceContext =
        applicationContext.newPersistenceContext();

    CompanyTopiaDao companyDao =
        persistenceContext.getCompanyDao();

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

    @RequestMapping(value="/v1/company/add", method= RequestMethod.PUT)
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


}
