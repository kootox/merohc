package org.chorem.merohc.services;

import java.util.ArrayList;
import java.util.List;

import org.chorem.merhoc.entities.*;
import org.chorem.merohc.MerohcApplicationConfig;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.nuiton.topia.persistence.TopiaQueryException;
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

    EmployeeTopiaDao employeeDao = persistenceContext.getEmployeeDao();

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

    @RequestMapping(value="/v1/company/{id}/employee", method= RequestMethod.GET)
    public List<EmployeeDTO> getEmployees(@PathVariable String id) {

        List<Employee> employees;

        List <EmployeeDTO> dtos = new ArrayList<EmployeeDTO>();

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        try {
            employees = employeeDao.forCompanyEquals(company)
                                   .findAll();

            for (Employee employee:employees) {
                dtos.add(new EmployeeDTO(employee));
            }

        } catch (TopiaQueryException eee) {
            //FIXME JC151216 silent exception
            //no result found so keep empty list
        }

        return dtos;
    }

    @RequestMapping(value="/v1/company/{id}/employee/add", method= RequestMethod.PUT)
    public EmployeeDTO addEmployee(@PathVariable String id,
                                   @RequestParam String firstName,
                                   @RequestParam String lastName) {

        Company company = companyDao.forTopiaIdEquals(id).findAnyOrNull();

        Employee employee = employeeDao.create();

        if (company != null){
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setCompany(company);
        }

        persistenceContext.commit();
        EmployeeDTO dto = new EmployeeDTO(employee);
        return dto;
    }

    @RequestMapping(value="/v1/company/{companyId:.+}/employee/{id:.+}", method= RequestMethod.GET)
    public EmployeeDTO getEmployee(@PathVariable String companyId,
                                   @PathVariable String id) {

        EmployeeDTO dto = null;

        try {
            Employee employee = employeeDao.forTopiaIdEquals(id).findAny();
            Company company = companyDao.forTopiaIdEquals(companyId).findAny();
            if (employee.getCompany().equals(company)) {
                dto = new EmployeeDTO(employee);
            }
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }

        return dto;
    }

    @RequestMapping(value="/v1/company/{companyId}/employee", method= RequestMethod.POST)
    public EmployeeDTO editEmployee(@PathVariable String companyId,
                                    @RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String id) {

        Company company = companyDao.forTopiaIdEquals(companyId).findAnyOrNull();

        Employee employee = employeeDao.forTopiaIdEquals(id).findAnyOrNull();

        if (employee != null && company != null){
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setCompany(company);
        } else {
            //FIXME JC151216 Should throw an exception
        }

        persistenceContext.commit();
        EmployeeDTO dto = new EmployeeDTO(employee);
        return dto;
    }

    @RequestMapping(value="/v1/company/{companyId:.+}/employee/{id:.+}", method= RequestMethod.DELETE)
    public void deleteEmployee(@PathVariable String companyId,
                               @PathVariable String id) {
        try {
            Employee employee = employeeDao.forTopiaIdEquals(id).findAny();
            Company company = companyDao.forTopiaIdEquals(companyId).findAny();
            if (employee.getCompany().equals(company)) {
                employeeDao.delete(employee);
                persistenceContext.commit();
            }
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }


}
