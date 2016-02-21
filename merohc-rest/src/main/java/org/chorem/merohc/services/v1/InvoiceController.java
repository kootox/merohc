package org.chorem.merohc.services.v1;

import org.chorem.merohc.bean.InvoiceDTO;
import org.chorem.merohc.bean.InvoiceItemDTO;
import org.chorem.merohc.entities.Company;
import org.chorem.merohc.entities.CompanyTopiaDao;
import org.chorem.merohc.entities.Invoice;
import org.chorem.merohc.entities.InvoiceCategory;
import org.chorem.merohc.entities.InvoiceCategoryTopiaDao;
import org.chorem.merohc.entities.InvoiceItem;
import org.chorem.merohc.entities.InvoiceItemTopiaDao;
import org.chorem.merohc.entities.InvoiceTopiaDao;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@Transactional
public class InvoiceController extends AbstractService {

    @ResponseBody
    @RequestMapping(value="/v1/invoice", method= RequestMethod.GET)
    public List<InvoiceDTO> listInvoices() {
        List<Invoice> invoices = getInvoiceDao().findAll();

        List<InvoiceDTO> dtos = new ArrayList<>();

        for (Invoice invoice:invoices) {
            InvoiceDTO dto = new InvoiceDTO(invoice);
            dtos.add(dto);
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/invoice", method= RequestMethod.PUT)
    public InvoiceDTO addInvoice(@RequestBody InvoiceDTO dto) {
        Invoice invoiceToStore = getInvoiceDao().create();
        invoiceToStore.setEmittedDate(dto.getEmittedDate());
        invoiceToStore.setDueDate(dto.getDueDate());
        invoiceToStore.setPaymentDate(dto.getPaymentDate());
        invoiceToStore.setReference(dto.getReference());
        invoiceToStore.setName(dto.getName());
        Company company = getCompanyDao().forTopiaIdEquals(dto.getCompanyId()).findAny();
        invoiceToStore.setCompany(company);
        List<InvoiceItem> items = new ArrayList<>();
        for(InvoiceItemDTO itemDTO:dto.getInvoiceItems()){
            InvoiceItem item = getInvoiceItemDao().create();
            item.setDescription(itemDTO.getDescription());
            item.setAmount(itemDTO.getAmount());
            item.setVATRate(itemDTO.getVATRate());
            InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(itemDTO.getInvoiceCategoryId()).findAny();
            item.setInvoiceCategory(category);
            item.setInvoice(invoiceToStore);
            items.add(item);
        }

        //transformation entity -> dto des items
        List<InvoiceItemDTO> storedDTOs = new ArrayList<>();
        for (InvoiceItem item:items){
            InvoiceItemDTO itemDTO = new InvoiceItemDTO(item);
            storedDTOs.add(itemDTO);
        }

        dto = new InvoiceDTO(invoiceToStore);
        dto.setInvoiceItems(storedDTOs);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/invoice/{id:.+}", method= RequestMethod.GET)
    public InvoiceDTO getInvoice(@PathVariable String id) {
        try {
            Invoice invoice = getInvoiceDao().forTopiaIdEquals(id).findAny();
            InvoiceDTO dto = new InvoiceDTO(invoice);
            for(InvoiceItemDTO itemDTO:dto.getInvoiceItems()){
                InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(itemDTO.getInvoiceCategoryId()).findAny();
                itemDTO.setInvoiceCategoryName(category.getName());
            }

            return dto;
        } catch (TopiaNoResultException tnre) {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/invoice/{id:.+}", method= RequestMethod.DELETE)
    public void deleteInvoice(@PathVariable String id) {
        try {
            InvoiceTopiaDao invoiceDao = getInvoiceDao();
            Invoice invoice = invoiceDao.forTopiaIdEquals(id).findAny();
            invoiceDao.delete(invoice);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/invoice", method= RequestMethod.POST)
    public InvoiceDTO editInvoice(@RequestBody InvoiceDTO dto) {
        Invoice invoiceToStore = getInvoiceDao().forTopiaIdEquals(dto.getId()).findAnyOrNull();
        invoiceToStore.setEmittedDate(dto.getEmittedDate());
        invoiceToStore.setDueDate(dto.getDueDate());
        invoiceToStore.setPaymentDate(dto.getPaymentDate());
        invoiceToStore.setReference(dto.getReference());
        invoiceToStore.setName(dto.getName());
        List<InvoiceItem> items = new ArrayList<>();
        for(InvoiceItemDTO itemDTO:dto.getInvoiceItems()){
            InvoiceItem item = getInvoiceItemDao().create();
            item.setDescription(itemDTO.getDescription());
            item.setAmount(itemDTO.getAmount());
            item.setVATRate(itemDTO.getVATRate());
            InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(itemDTO.getInvoiceCategoryId()).findAny();
            item.setInvoiceCategory(category);
            items.add(item);
        }

        //transformation entity -> dto des items
        List<InvoiceItemDTO> storedDTOs = new ArrayList<>();
        for (InvoiceItem item:items){
            InvoiceItemDTO itemDTO = new InvoiceItemDTO(item);
            storedDTOs.add(itemDTO);
        }

        dto = new InvoiceDTO(invoiceToStore);
        dto.setInvoiceItems(storedDTOs);
        return dto;
    }

    protected InvoiceTopiaDao getInvoiceDao() {
        return getPersistenceContext().getInvoiceDao();
    }

    protected CompanyTopiaDao getCompanyDao() {
        return getPersistenceContext().getCompanyDao();
    }

    protected InvoiceItemTopiaDao getInvoiceItemDao() {
        return getPersistenceContext().getInvoiceItemDao();
    }

    protected InvoiceCategoryTopiaDao getInvoiceCategoryDao(){
        return getPersistenceContext().getInvoiceCategoryDao();
    }
}
