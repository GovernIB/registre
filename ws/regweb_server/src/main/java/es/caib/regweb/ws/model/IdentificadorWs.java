package es.caib.regweb.ws.model;

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
public class IdentificadorWs implements Serializable {

    private String numeroRegistroFormateado;
    private Integer numero;
    private Date fecha;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
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
