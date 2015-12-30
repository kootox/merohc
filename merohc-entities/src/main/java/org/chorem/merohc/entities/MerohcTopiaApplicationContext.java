package org.chorem.merohc.entities;

import org.chorem.merohc.MerohcApplicationConfig;
import org.nuiton.topia.persistence.TopiaIdFactory;

/**
 * @author ymartel (martel@codelutin.com)
 */
public class MerohcTopiaApplicationContext extends AbstractMerohcTopiaApplicationContext {

    protected MerohcApplicationConfig config;

    public MerohcTopiaApplicationContext(MerohcApplicationConfig config) {
        super(config.getTopiaConfiguration());
        this.config = config;
    }

    @Override
    public TopiaIdFactory getTopiaIdFactory() {
        return super.getTopiaIdFactory();
    }
}
