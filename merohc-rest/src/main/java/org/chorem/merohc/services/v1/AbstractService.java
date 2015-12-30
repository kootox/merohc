package org.chorem.merohc.services.v1;

import org.chorem.merohc.entities.MerohcTopiaPersistenceContext;
import org.chorem.merohc.services.MerohcTopiaTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ymartel (martel@codelutin.com)
 */
public class AbstractService {

    @Autowired
    protected MerohcTopiaTransactionManager transactionManager;

    public MerohcTopiaPersistenceContext getPersistenceContext() {
        return transactionManager.get();
    }
}
