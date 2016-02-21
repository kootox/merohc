package org.chorem.merohc.bean;

import org.chorem.merohc.entities.Bill;
import org.chorem.merohc.entities.BillItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillDTO {

    String id;
    Date emittedDate;
    Date dueDate;
    Date paymentDate;
    String reference;
    String name;
    List<BillItemDTO> billItems;
    String companyId;
    String companyName;


    public BillDTO(){}

    public BillDTO(Bill bill){
        super();
        this.id = bill.getTopiaId();
        this.emittedDate = bill.getEmittedDate();
        this.dueDate = bill.getDueDate();
        this.paymentDate = bill.getPaymentDate();
        this.reference = bill.getReference();
        this.name = bill.getName();
        this.billItems = new ArrayList<>();
        if (bill.getCompany() != null) {
            this.companyId = bill.getCompany().getTopiaId();
            this.companyName = bill.getCompany().getName();
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

    public List<BillItemDTO> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItemDTO> billItems) {
        this.billItems = billItems;
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
