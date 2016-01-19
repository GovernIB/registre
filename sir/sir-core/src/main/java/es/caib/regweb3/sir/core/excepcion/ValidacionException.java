package es.caib.regweb3.sir.core.excepcion;

import es.caib.regweb3.sir.core.model.Errores;

/**
 *
 */
public class ValidacionException extends SIRException {

    /**
     * Código de error de validación.
     */
    private Errores errorValidacion = null;

    /**
     * Excepción que ha causado el error.
     */
    private Throwable errorException = null;


    public ValidacionException(Errores errorValidacion, Throwable errorException) {
        super(errorValidacion.getValue());
        this.errorValidacion = errorValidacion;
        this.errorException = errorException;
    }


    public ValidacionException(Errores error) {
        super(error.getValue());
    }

    public Errores getErrorValidacion() {
        return errorValidacion;
    }

    public void setErrorValidacion(Errores errorValidacion) {
        this.errorValidacion = errorValidacion;
    }

    public Throwable getErrorException() {
        return errorException;
    }

    public void setErrorException(Throwable errorException) {
        this.errorException = errorException;
    }
}
