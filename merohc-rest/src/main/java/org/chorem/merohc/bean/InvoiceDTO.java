package org.chorem.merohc.bean;

import org.chorem.merohc.entities.Invoice;
import org.chorem.merohc.entities.InvoiceItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceDTO {

    String id;
    Date emittedDate;
    Date dueDate;
    Date paymentDate;
    String reference;
    String name;
    List<InvoiceItemDTO> invoiceItems;
    String companyId;
    String companyName;


    public InvoiceDTO(){}

    public InvoiceDTO(Invoice invoice){
        super();
        this.id = invoice.getTopiaId();
        this.emittedDate = invoice.getEmittedDate();
        this.dueDate = invoice.getDueDate();
        this.paymentDate = invoice.getPaymentDate();
        this.reference = invoice.getReference();
        this.name = invoice.getName();
        this.invoiceItems = new ArrayList<>();
        for (InvoiceItem item:invoice.getInvoiceItem()){
            InvoiceItemDTO dto = new InvoiceItemDTO(item);
            this.invoiceItems.add(dto);
        }
        if (invoice.getCompany() != null) {
            this.companyId = invoice.getCompany().getTopiaId();
            this.companyName = invoice.getCompany().getName();
        }
    }

    public Date getEmittedDate() {
        return emittedDate;
    }

    public void setEmittedDate(Date emittedDate) {
        this.emittedDate = emittedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<InvoiceItemDTO> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemDTO> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
