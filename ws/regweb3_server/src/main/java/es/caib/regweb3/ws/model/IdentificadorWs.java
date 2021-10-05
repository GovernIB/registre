package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *
 * @author anadal
 */
@XmlRootElement
@Deprecated
public class IdentificadorWs implements Serializable {

    private String numeroRegistroFormateado;
    private String numero;
    private Date fecha;

    public IdentificadorWs() {
    }

    public IdentificadorWs(String numeroRegistroFormateado, String numero, Date fecha) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
        this.numero = numero;
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumeroRegistroFormateado() {
      return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
      this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

}
