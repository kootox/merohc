package org.chorem.merohc.services.v1;

import org.chorem.merohc.bean.BillDTO;
import org.chorem.merohc.bean.BillItemDTO;
import org.chorem.merohc.bean.InvoiceDTO;
import org.chorem.merohc.bean.InvoiceItemDTO;
import org.chorem.merohc.entities.Bill;
import org.chorem.merohc.entities.BillCategory;
import org.chorem.merohc.entities.BillCategoryTopiaDao;
import org.chorem.merohc.entities.BillItem;
import org.chorem.merohc.entities.BillItemTopiaDao;
import org.chorem.merohc.entities.BillTopiaDao;
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

            List<InvoiceItem> items = getInvoiceItemDao().forInvoiceEquals(invoice).findAll();
            List<InvoiceItemDTO> itemsDTO = new ArrayList<>();
            for(InvoiceItem item:items){
                InvoiceItemDTO itemDTO = new InvoiceItemDTO(item);
                InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(itemDTO.getInvoiceCategoryId()).findAny();
                itemDTO.setInvoiceCategoryName(category.getName());
                itemsDTO.add(itemDTO);
            }

            dto.setInvoiceItems(itemsDTO);

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
            item.setInvoice(invoiceToStore);
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

    @ResponseBody
    @RequestMapping(value="/v1/bill", method= RequestMethod.GET)
    public List<BillDTO> listBills() {
        List<Bill> bills = getBillDao().findAll();

        List<BillDTO> dtos = new ArrayList<>();

        for (Bill bill:bills) {
            BillDTO dto = new BillDTO(bill);
            dtos.add(dto);
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/bill", method= RequestMethod.PUT)
    public BillDTO addBill(@RequestBody BillDTO dto) {
        Bill billToStore = getBillDao().create();
        billToStore.setEmittedDate(dto.getEmittedDate());
        billToStore.setDueDate(dto.getDueDate());
        billToStore.setPaymentDate(dto.getPaymentDate());
        billToStore.setReference(dto.getReference());
        billToStore.setName(dto.getName());
        Company company = getCompanyDao().forTopiaIdEquals(dto.getCompanyId()).findAny();
        billToStore.setCompany(company);
        List<BillItem> items = new ArrayList<>();
        for(BillItemDTO itemDTO:dto.getBillItems()){
            BillItem itemToStore = getBillItemDao().create();
            itemToStore.setDescription(itemDTO.getDescription());
            itemToStore.setAmount(itemDTO.getAmount());
            itemToStore.setVATRate(itemDTO.getVATRate());
            itemToStore.setBill(billToStore);
            BillCategory category = getBillCategoryDao().forTopiaIdEquals(itemDTO.getBillCategoryId()).findAny();
            itemToStore.setBillCategory(category);
            items.add(itemToStore);
        }

        //transformation entity -> dto des items
        List<BillItemDTO> storedDTOs = new ArrayList<>();
        for (BillItem item:items){
            BillItemDTO itemDTO = new BillItemDTO(item);
            storedDTOs.add(itemDTO);
        }

        dto = new BillDTO(billToStore);
        dto.setBillItems(storedDTOs);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/bill/{id:.+}", method= RequestMethod.GET)
    public BillDTO getBill(@PathVariable String id) {
        try {
            Bill bill = getBillDao().forTopiaIdEquals(id).findAny();
            BillDTO dto = new BillDTO(bill);

            List<BillItem> items = getBillItemDao().forBillEquals(bill).findAll();
            List<BillItemDTO> itemsDTO = new ArrayList<>();
            for(BillItem item:items){
                BillItemDTO itemDTO = new BillItemDTO(item);
                BillCategory category = getBillCategoryDao().forTopiaIdEquals(itemDTO.getBillCategoryId()).findAny();
                itemDTO.setBillCategoryName(category.getName());
                itemsDTO.add(itemDTO);
            }

            dto.setBillItems(itemsDTO);

            return dto;
        } catch (TopiaNoResultException tnre) {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/bill/{id:.+}", method= RequestMethod.DELETE)
    public void seleteBill(@PathVariable String id) {
        try {
            BillTopiaDao billDao = getBillDao();
            Bill bill = billDao.forTopiaIdEquals(id).findAny();
            billDao.delete(bill);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/bill", method= RequestMethod.POST)
    public BillDTO editBill(@RequestBody BillDTO dto) {
        Bill billToStore = getBillDao().forTopiaIdEquals(dto.getId()).findAnyOrNull();
        billToStore.setEmittedDate(dto.getEmittedDate());
        billToStore.setDueDate(dto.getDueDate());
        billToStore.setPaymentDate(dto.getPaymentDate());
        billToStore.setReference(dto.getReference());
        billToStore.setName(dto.getName());
        Company company = getCompanyDao().forTopiaIdEquals(dto.getCompanyId()).findAny();
        billToStore.setCompany(company);
        List<BillItem> items = new ArrayList<>();
        for(BillItemDTO itemDTO:dto.getBillItems()){
            BillItem itemToStore = getBillItemDao().create();
            itemToStore.setDescription(itemDTO.getDescription());
            itemToStore.setAmount(itemDTO.getAmount());
            itemToStore.setVATRate(itemDTO.getVATRate());
            itemToStore.setBill(billToStore);
            BillCategory category = getBillCategoryDao().forTopiaIdEquals(itemDTO.getBillCategoryId()).findAny();
            itemToStore.setBillCategory(category);
            items.add(itemToStore);
        }

        //transformation entity -> dto des items
        List<BillItemDTO> storedDTOs = new ArrayList<>();
        for (BillItem item:items){
            BillItemDTO itemDTO = new BillItemDTO(item);
            storedDTOs.add(itemDTO);
        }

        dto = new BillDTO(billToStore);
        dto.setBillItems(storedDTOs);
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

    protected BillTopiaDao getBillDao() {
        return getPersistenceContext().getBillDao();
    }

    protected BillItemTopiaDao getBillItemDao() {
        return getPersistenceContext().getBillItemDao();
    }

    protected BillCategoryTopiaDao getBillCategoryDao(){
        return getPersistenceContext().getBillCategoryDao();
    }
}
