package org.chorem.merohc.entities;

import java.util.Date;

/**
 * @author ymartel (martel@codelutin.com)
 */
public class InvoiceSearchExample extends SearchExample<Invoice> {

    protected Date emittedAfterDate;
    protected Date emittedBeforeDate;

    protected Date dueAfterDate;
    protected Date dueBeforeDate;

    protected Date paymentAfterDate;
    protected Date paymentBeforeDate;

    protected String orderClause;
    protected boolean orderDesc;

    public Date getEmittedAfterDate() {
        return emittedAfterDate;
    }

    public void setEmittedAfterDate(Date emittedAfterDate) {
        this.emittedAfterDate = emittedAfterDate;
    }

    public Date getEmittedBeforeDate() {
        return emittedBeforeDate;
    }

    public void setEmittedBeforeDate(Date emittedBeforeDate) {
        this.emittedBeforeDate = emittedBeforeDate;
    }

    public Date getDueAfterDate() {
        return dueAfterDate;
    }

    public void setDueAfterDate(Date dueAfterDate) {
        this.dueAfterDate = dueAfterDate;
    }

    public Date getDueBeforeDate() {
        return dueBeforeDate;
    }

    public void setDueBeforeDate(Date dueBeforeDate) {
        this.dueBeforeDate = dueBeforeDate;
    }

    public Date getPaymentAfterDate() {
        return paymentAfterDate;
    }

    public void setPaymentAfterDate(Date paymentAfterDate) {
        this.paymentAfterDate = paymentAfterDate;
    }

    public Date getPaymentBeforeDate() {
        return paymentBeforeDate;
    }

    public void setPaymentBeforeDate(Date paymentBeforeDate) {
        this.paymentBeforeDate = paymentBeforeDate;
    }

    @Override
    public String getOrderClause() {
        return Invoice.PROPERTY_DUE_DATE;
    }

    public void setOrderClause(String orderClause) {
        this.orderClause = orderClause;
    }

    @Override
    public boolean isOrderDesc() {
        return true;
    }

    public void setOrderDesc(boolean orderDesc) {
        this.orderDesc = orderDesc;
    }
}
