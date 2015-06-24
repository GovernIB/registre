package es.caib.regweb3.persistence.utils;

import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (generics)
 * 
 * Date: 13/01/15
 */
public class Respuesta<T> implements Serializable {

    public String mensaje;
    public T object;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
