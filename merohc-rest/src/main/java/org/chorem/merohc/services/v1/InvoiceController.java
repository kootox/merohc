package org.chorem.merohc.services.v1;

import org.chorem.merohc.bean.InvoiceDTO;
import org.chorem.merohc.bean.InvoiceItemDTO;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
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

    //FIXME JC160108 Should replace POST by PUT and remove /add in url when issue #1 is fixed
/*    @ResponseBody
    @RequestMapping(value="/v1/invoice/add", method= RequestMethod.POST)
    public InvoiceDTO addInvoice(@RequestParam(value = "emittedDate") Date emittedDate,
                                 @RequestParam(value = "dueDate") Date dueDate,
                                 @RequestParam(value = "paymentDate") Date paymentDate,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "reference") String reference,
                                 @RequestParam(value = "invoiceItems[]")  InvoiceItemDTO[] invoiceitems) {
        Invoice invoiceToStore = getInvoiceDao().create();
        invoiceToStore.setEmittedDate(emittedDate);
        invoiceToStore.setDueDate(dueDate);
        invoiceToStore.setPaymentDate(paymentDate);
        invoiceToStore.setReference(reference);
        invoiceToStore.setName(name);
        List<InvoiceItem> items = new ArrayList<>();
        for(InvoiceItemDTO itemDTO:invoiceitems){
            InvoiceItem item = getInvoiceItemDao().create();
            item.setDescription(itemDTO.getDescription());
            item.setAmount(itemDTO.getAmount());
            item.setVATRate(itemDTO.getVATRate());

            InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(itemDTO.getInvoiceCategoryId()).findAny();
            item.setInvoiceCategory(category);

            items.add(item);
        }

        //transformation dto -> entity des items
        invoiceToStore.setInvoiceItem(items);
        InvoiceDTO dto = new InvoiceDTO(invoiceToStore);
        return dto;
    }*/

    @ResponseBody
    @RequestMapping(value="/v1/invoice/add", method= RequestMethod.POST)
    public InvoiceDTO addInvoice(@RequestBody InvoiceDTO dto) {
        System.out.println(dto);
        System.out.println(dto.getName());
        System.out.println(dto.getReference());
        System.out.println(dto.getDueDate());
        System.out.println(dto.getInvoiceItems());
        Invoice invoiceToStore = getInvoiceDao().create();
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

        //transformation dto -> entity des items
        invoiceToStore.setInvoiceItem(items);
        dto = new InvoiceDTO(invoiceToStore);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/invoice/{id:.+}", method= RequestMethod.GET)
    public InvoiceDTO getInvoice(@PathVariable String id) {
        try {
            Invoice invoice = getInvoiceDao().forTopiaIdEquals(id).findAny();
            InvoiceDTO dto = new InvoiceDTO(invoice);
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
    public InvoiceDTO editInvoice(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "emittedDate") Date emittedDate,
                                  @RequestParam(value = "dueDate") Date dueDate,
                                  @RequestParam(value = "paymentDate") Date paymentDate,
                                  @RequestParam(value = "reference") String reference,
                                  @RequestParam(value = "name")  String name,
                                  @RequestParam(value = "invoiceItems[]")  InvoiceItemDTO[] invoiceitems) {

        Invoice invoice = getInvoiceDao().forTopiaIdEquals(id).findAny();
        //FIXME JC151211 - Deal with TopiaNoResultException
        invoice.setEmittedDate(emittedDate);
        invoice.setDueDate(dueDate);
        invoice.setPaymentDate(paymentDate);
        invoice.setReference(reference);
        invoice.setName(name);
        List<InvoiceItem> items = new ArrayList<>();
        for(InvoiceItemDTO itemDTO:invoiceitems){
            InvoiceItem item = getInvoiceItemDao().create();
            item.setDescription(itemDTO.getDescription());
            item.setAmount(itemDTO.getAmount());
            item.setVATRate(itemDTO.getVATRate());

            InvoiceCategory category = getInvoiceCategoryDao().forTopiaIdEquals(itemDTO.getInvoiceCategoryId()).findAny();
            item.setInvoiceCategory(category);

            items.add(item);
        }

        //transformation dto -> entity des items
        invoice.setInvoiceItem(items);
        InvoiceDTO dto = new InvoiceDTO(invoice);
        return dto;
    }

    protected InvoiceTopiaDao getInvoiceDao() {
        return getPersistenceContext().getInvoiceDao();
    }

    protected InvoiceItemTopiaDao getInvoiceItemDao() {
        return getPersistenceContext().getInvoiceItemDao();
    }

    protected InvoiceCategoryTopiaDao getInvoiceCategoryDao(){
        return getPersistenceContext().getInvoiceCategoryDao();
    }
}
