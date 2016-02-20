package org.chorem.merohc.services.v1;

import org.chorem.merohc.bean.BillCategoryDTO;
import org.chorem.merohc.bean.InvoiceCategoryDTO;
import org.chorem.merohc.entities.BillCategory;
import org.chorem.merohc.entities.BillCategoryTopiaDao;
import org.chorem.merohc.entities.InvoiceCategory;
import org.chorem.merohc.entities.InvoiceCategoryTopiaDao;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@Transactional
public class ReferentialController extends AbstractService {

    @ResponseBody
    @RequestMapping(value="/v1/referential/invoiceCategory", method= RequestMethod.GET)
    public List<InvoiceCategoryDTO> listInvoiceCategories() {
        List<InvoiceCategory> categories = getInvoiceCategoryDao().findAll();

        List<InvoiceCategoryDTO> dtos = new ArrayList<>();

        for (InvoiceCategory category:categories) {
            InvoiceCategoryDTO dto = new InvoiceCategoryDTO(category);
            dtos.add(dto);
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/invoiceCategory", method= RequestMethod.PUT)
    public InvoiceCategoryDTO addInvoiceCategory(@RequestBody InvoiceCategoryDTO invoiceCategoryDTO) {
        //FIXME ymartel 20160219 : check if name does not already exist
        InvoiceCategory categoryToStore = getInvoiceCategoryDao().create();
        categoryToStore.setName(invoiceCategoryDTO.getName());
        InvoiceCategoryDTO dto = new InvoiceCategoryDTO(categoryToStore);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/invoiceCategory/{id:.+}", method= RequestMethod.GET)
    public InvoiceCategoryDTO getInvoiceCategory(@PathVariable String id) {
        try {
            InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(id).findAny();
            InvoiceCategoryDTO dto = new InvoiceCategoryDTO(category);
            return dto;
        } catch (TopiaNoResultException tnre) {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/invoiceCategory/{id:.+}", method= RequestMethod.DELETE)
    public void deleteInvoiceCategory(@PathVariable String id) {
        try {
            InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(id).findAny();
            getInvoiceCategoryDao().delete(category);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/invoiceCategory", method= RequestMethod.POST)
    public InvoiceCategoryDTO editInvoiceCategory(@RequestParam(value="id") String id,
                                                  @RequestParam(value="name") String name) {

        InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(id).findAny();
        //FIXME JC151211 - Deal with TopiaNoResultException
        category.setName(name);
        InvoiceCategoryDTO dto = new InvoiceCategoryDTO(category);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/billCategory", method= RequestMethod.GET)
    public List<BillCategoryDTO> listBillCategories() {
        List<BillCategory> categories = getBillCategoryDao().findAll();

        List<BillCategoryDTO> dtos = new ArrayList<>();

        for (BillCategory category:categories) {
            BillCategoryDTO dto = new BillCategoryDTO(category);
            dtos.add(dto);
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/billCategory", method= RequestMethod.PUT)
    public BillCategoryDTO addBillCategory(@RequestBody BillCategoryDTO billCategoryDTO) {
        //FIXME jcouteau 20160220 : check if name does not already exist
        BillCategory categoryToStore = getBillCategoryDao().create();
        categoryToStore.setName(billCategoryDTO.getName());
        BillCategoryDTO dto = new BillCategoryDTO(categoryToStore);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/billCategory/{id:.+}", method= RequestMethod.GET)
    public BillCategoryDTO getBillCategory(@PathVariable String id) {
        try {
            BillCategory category = getBillCategoryDao().forTopiaIdEquals(id).findAny();
            BillCategoryDTO dto = new BillCategoryDTO(category);
            return dto;
        } catch (TopiaNoResultException tnre) {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/billCategory/{id:.+}", method= RequestMethod.DELETE)
    public void deleteBillCategory(@PathVariable String id) {
        try {
            BillCategory category = getBillCategoryDao().forTopiaIdEquals(id).findAny();
            getBillCategoryDao().delete(category);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/referential/billCategory", method= RequestMethod.POST)
    public BillCategoryDTO editBillCategory(@RequestBody BillCategoryDTO billCategoryDTO) {

        BillCategory category = getBillCategoryDao().forTopiaIdEquals(billCategoryDTO.getId()).findAny();
        //FIXME jcouteau 20160220 - Deal with TopiaNoResultException
        category.setName(billCategoryDTO.getName());
        BillCategoryDTO dto = new BillCategoryDTO(category);
        return dto;
    }

    protected InvoiceCategoryTopiaDao getInvoiceCategoryDao() {
        return getPersistenceContext().getInvoiceCategoryDao();
    }

    protected BillCategoryTopiaDao getBillCategoryDao() {
        return getPersistenceContext().getBillCategoryDao();
    }
}