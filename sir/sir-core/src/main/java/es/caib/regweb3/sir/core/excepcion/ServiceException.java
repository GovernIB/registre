package es.caib.regweb3.sir.core.excepcion;

import es.caib.regweb3.model.sir.Errores;

/**
 * Created by earrivi on 14/01/2016.
 */
public class ServiceException extends RuntimeException {


    private Errores error = null;

    /**
     * Constructor.
     */
    public ServiceException() {
        super();
    }

    public ServiceException(Errores error) {
        super();
        setError(error);
    }

    public ServiceException(Errores error, Throwable cause) {
        super(cause);
        setError(error);
    }

    public Errores getError() {
        return error;
    }

    public void setError(Errores error) {
        this.error = error;
    }

}
