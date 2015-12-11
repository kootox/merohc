package org.chorem.merohc;

/**
 * Created by couteau on 05/12/15.
 */
public class MerohcTechnicalException  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MerohcTechnicalException() {
    }

    public MerohcTechnicalException(String message) {
        super(message);
    }

    public MerohcTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MerohcTechnicalException(Throwable cause) {
        super(cause);
    }
}