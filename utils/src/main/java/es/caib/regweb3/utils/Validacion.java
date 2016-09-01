package es.caib.regweb3.utils;

import java.io.Serializable;

/**
 * Bean básico para reutilizar
 * @author earrivi on 05/03/2015.
 */
public class Validacion implements Serializable{

    private Boolean valido;
    private String codigoError;
    private String textoError;


    public Validacion() {
    }

    public Validacion(Boolean valido, String codigoError, String textoError) {
        this.valido = valido;
        this.codigoError = codigoError;
        this.textoError = textoError;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public String getTextoError() {
        return textoError;
    }

    public void setTextoError(String textoError) {
        this.textoError = textoError;
    }
}
