package org.chorem.merohc.entities;

import org.apache.commons.lang3.StringUtils;
import org.chorem.merohc.DaoUtils;
import org.nuiton.util.pagination.PaginationParameter;
import org.nuiton.util.pagination.PaginationResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ymartel (martel@codelutin.com)
 */
public class InvoiceTopiaDao extends AbstractInvoiceTopiaDao<Invoice> {

    public PaginationResult<Invoice> findByExample(InvoiceSearchExample searchExample) {

        StringBuilder hqlBuilder = new StringBuilder(" FROM " + Invoice.class.getName() + " I");
        hqlBuilder.append(" WHERE 1=1 "); // Just because next clause will begin with operator

        Map<String, Object> args = new HashMap<>();

        Invoice example = searchExample.getExample();
        if (example != null) {
            if (example.getCompany() != null) {
                String companyClause = DaoUtils.andAttributeEquals("I", Invoice.PROPERTY_COMPANY, args, example.getCompany());
                hqlBuilder.append(companyClause);
            }

            if (example.getProject() != null) {
                String projectClause = DaoUtils.andAttributeEquals("I", Invoice.PROPERTY_PROJECT, args, example.getProject());
                hqlBuilder.append(projectClause);
            }

            if (StringUtils.isNotBlank(example.getReference())) {
                String referenceClause = DaoUtils.andAttributeLike("I", Invoice.PROPERTY_REFERENCE, args, example.getReference());
                hqlBuilder.append(referenceClause);
            }
        }

        if (searchExample.getEmittedBeforeDate() != null) {
            String emittedBeforeClause = DaoUtils.andAttributeLesserOrEquals("I", Invoice.PROPERTY_EMITTED_DATE, args, searchExample.getEmittedBeforeDate());
            hqlBuilder.append(emittedBeforeClause);
        }

        if (searchExample.getEmittedAfterDate() != null) {
            String emittedAfterClause = DaoUtils.andAttributeGreaterOrEquals("I", Invoice.PROPERTY_EMITTED_DATE, args, searchExample.getEmittedAfterDate());
            hqlBuilder.append(emittedAfterClause);
        }

        if (searchExample.getDueBeforeDate() != null) {
            String dueBeforeClause = DaoUtils.andAttributeLesserOrEquals("I", Invoice.PROPERTY_DUE_DATE, args, searchExample.getDueBeforeDate());
            hqlBuilder.append(dueBeforeClause);
        }

        if (searchExample.getDueAfterDate() != null) {
            String dueAfterClause = DaoUtils.andAttributeGreaterOrEquals("I", Invoice.PROPERTY_DUE_DATE, args, searchExample.getDueAfterDate());
            hqlBuilder.append(dueAfterClause);
        }

        if (searchExample.getPaymentBeforeDate() != null) {
            String paymentBeforeClause = DaoUtils.andAttributeLesserOrEquals("I", Invoice.PROPERTY_PAYMENT_DATE, args, searchExample.getPaymentBeforeDate());
            hqlBuilder.append(paymentBeforeClause);
        }

        if (searchExample.getPaymentAfterDate() != null) {
            String paymentAfterClause = DaoUtils.andAttributeGreaterOrEquals("I", Invoice.PROPERTY_PAYMENT_DATE, args, searchExample.getPaymentAfterDate());
            hqlBuilder.append(paymentAfterClause);
        }

        // Add the prefix for order clause
        searchExample.setOrderClause("D." + searchExample.getOrderClause());

        PaginationParameter paginationParameter = searchExample.getPaginationParameter();

        PaginationResult<Invoice> invoices = forHql(hqlBuilder.toString(), args).findPage(paginationParameter);
        return invoices;
    }

} //InvoiceTopiaDao