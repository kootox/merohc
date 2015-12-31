package org.chorem.merohc.services;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorem.merohc.entities.MerohcTopiaPersistenceContext;
import org.chorem.merohc.entities.MerohcTopiaApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * @author ymartel (martel@codelutin.com)
 */
@Component
public class MerohcTopiaTransactionManager extends AbstractPlatformTransactionManager
    implements Supplier<MerohcTopiaPersistenceContext> {

    private static final Log log = LogFactory.getLog(MerohcTopiaTransactionManager.class);

    @Autowired
    protected MerohcTopiaApplicationContext applicationContext;

    protected ThreadLocal<PersistenceContextContainer> localTx = new ThreadLocal<PersistenceContextContainer>();

    private class PersistenceContextContainer {
        protected MerohcTopiaPersistenceContext persistenceContext;
        protected boolean rollbackOnly = false;
    }

    @Override
    protected Object doGetTransaction() throws TransactionException {
        if (log.isDebugEnabled()) {
            log.debug("Get container");
        }
        PersistenceContextContainer result = localTx.get();
        if (result == null) {
            if (log.isDebugEnabled()) {
                log.debug("New container");
            }
            result = new PersistenceContextContainer();
            localTx.set(result);
        }
        return result;
    }

    @Override
    protected boolean isExistingTransaction(Object transaction) throws TransactionException {
        PersistenceContextContainer container = (PersistenceContextContainer) transaction;
        return container.persistenceContext != null;
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
        if (log.isDebugEnabled()) {
            log.debug("doBegin transaction");
        }
        PersistenceContextContainer container = (PersistenceContextContainer) transaction;
        Preconditions.checkState(!container.rollbackOnly, "Transaction is marked as rollbackOnly, cannot begin transaction");
        MerohcTopiaPersistenceContext newTransaction = applicationContext.newPersistenceContext();
        container.persistenceContext = newTransaction;

//        localTx.set(container);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
        if (log.isDebugEnabled()) {
            log.debug("doCommit transaction");
        }
        PersistenceContextContainer container = (PersistenceContextContainer) status.getTransaction();
        Preconditions.checkState(!container.rollbackOnly, "Transaction is marked as rollbackOnly, cannot begin transaction");
        MerohcTopiaPersistenceContext persistenceContext = container.persistenceContext;

        Preconditions.checkState(persistenceContext != null);

        persistenceContext.commit();

        if (status.isNewTransaction()) { // This is the exit of the first transactional method used
            if (log.isDebugEnabled()) {
                log.debug("This is the exit of the first transactional method used, close context");
            }
            localTx.remove();
            persistenceContext.close();
//        } else {
//            localTx.set(container);
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
        if (log.isDebugEnabled()) {
            log.debug("doRollback transaction");
        }
        PersistenceContextContainer container = (PersistenceContextContainer) status.getTransaction();
        MerohcTopiaPersistenceContext persistenceContext = container.persistenceContext;

        Preconditions.checkState(persistenceContext != null);

        persistenceContext.rollback();

        if (status.isNewTransaction()) { // This is the exit of the first transactional method used
            if (log.isDebugEnabled()) {
                log.debug("This is the exit of the first transactional method used, close context");
            }
            localTx.remove();
            persistenceContext.close();
//        } else {
//            localTx.set(container);
        }
    }

    @Override
    public MerohcTopiaPersistenceContext get() {

        PersistenceContextContainer container = localTx.get();
        Preconditions.checkState(container != null, "No transaction available");

        MerohcTopiaPersistenceContext persistenceContext = container.persistenceContext;
        Preconditions.checkState(persistenceContext != null, "No transaction available");

        return persistenceContext;
    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
        PersistenceContextContainer container = (PersistenceContextContainer) status.getTransaction();
        container.rollbackOnly = true;
    }
}
