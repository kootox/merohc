package org.chorem.merohc.bean;

import org.chorem.merohc.entities.BillItem;

public class  BillItemDTO {

    String id;
    String description;
    Double amount;
    Double VATRate;
    String invoiceId;
    String billCategoryId;
    String billCategoryName;

    public BillItemDTO(){}

    public BillItemDTO(BillItem item) {
        this.id = item.getTopiaId();
        this.description = item.getDescription();
        this.amount = item.getAmount();
        this.VATRate = item.getVATRate();
        if (item.getBill() != null){
            this.invoiceId = item.getBill().getTopiaId();
        }
        if (item.getBillCategory() != null) {
            this.billCategoryId = item.getBillCategory().getTopiaId();
            this.billCategoryName = item.getBillCategory().getName();
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

    public String getBillCategoryId() {
        return billCategoryId;
    }

    public void setBillCategoryId(String billCategoryId) {
        this.billCategoryId = billCategoryId;
    }

    public String getBillCategoryName() {
        return billCategoryName;
    }

    public void setBillCategoryName(String billCategoryName) {
        this.billCategoryName = billCategoryName;
    }
}