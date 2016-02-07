package org.chorem.merohc.bean;

import org.chorem.merohc.entities.InvoiceItem;

public class InvoiceItemDTO {

    String id;
    String description;
    Double amount;
    Double VATRate;
    String invoiceId;
    String invoiceCategoryId;

    public InvoiceItemDTO(){}

    public InvoiceItemDTO(InvoiceItem item) {
        this.id = item.getTopiaId();
        this.description = item.getDescription();
        this.amount = item.getAmount();
        this.VATRate = item.getVATRate();
        if (item.getInvoice() != null){
            this.invoiceId = item.getInvoice().getTopiaId();
        }
        if (item.getInvoiceCategory() != null) {
            this.invoiceCategoryId = item.getInvoiceCategory().getTopiaId();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getVATRate() {
        return VATRate;
    }

    public void setVATRate(Double VATRate) {
        this.VATRate = VATRate;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceCategoryId() {
        return invoiceCategoryId;
    }

    public void setInvoiceCategoryId(String invoiceCategoryId) {
        this.invoiceCategoryId = invoiceCategoryId;
    }
}
