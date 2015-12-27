package org.chorem.merohc.services;

import org.chorem.merhoc.entities.MerohcTopiaApplicationContext;
import org.chorem.merhoc.entities.MerohcTopiaPersistenceContext;
import org.chorem.merohc.MerohcApplicationConfig;

public class MerohcPersistenceContextSingleton {
    private static MerohcTopiaPersistenceContext INSTANCE = null;

    public static synchronized MerohcTopiaPersistenceContext getInstance() {
        if(INSTANCE == null) {
            MerohcApplicationConfig configuration = new MerohcApplicationConfig();

            MerohcTopiaApplicationContext applicationContext =
                    new MerohcTopiaApplicationContext(configuration.getTopiaProperties());

            INSTANCE = applicationContext.newPersistenceContext();
            return INSTANCE;
        } else {
            return INSTANCE;
        }

    }

}
