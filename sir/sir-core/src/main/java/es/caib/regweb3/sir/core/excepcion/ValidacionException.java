package es.caib.regweb3.sir.core.excepcion;

import es.caib.regweb3.model.sir.Errores;

/**
 *
 */
public class ValidacionException extends SIRException {

    // Código de error de validación.
    private Errores errorValidacion = null;

    // Descripción del error
    private String mensajeError = null;

    //Excepción que ha causado el error.
    private Throwable errorException = null;


    public ValidacionException(Errores errorValidacion, String mensajeError, Throwable errorException) {
        super(errorValidacion.getName());
        setErrorValidacion(errorValidacion);
        setMensajeError(mensajeError);
        setErrorException(errorException);
    }


    public ValidacionException(Errores error) {
        super(error.getName());
        setErrorValidacion(error);
    }

    public ValidacionException(Errores error, String mensajeError) {
        super(error.getName());
        setErrorValidacion(error);
        setMensajeError(mensajeError);
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

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}
