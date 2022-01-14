package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created 11/12/14 8:22
 *
 * @author mgonzalez
 */
@XmlRootElement
public class CodigoAsuntoWs {

    @Deprecated
    private TipoAsuntoWs tipoAsunto;

    private String codigo;

    private String nombre;



    public TipoAsuntoWs getTipoAsunto() {
      return tipoAsunto;
    }

    public void setTipoAsunto(TipoAsuntoWs tipoAsunto) {
      this.tipoAsunto = tipoAsunto;
    }

    public String getCodigo() {
      return codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }

    public String getNombre() {
      return nombre;
    }

    public void setNombre(String nombre) {
      this.nombre = nombre;
    }
}
