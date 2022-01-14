package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (herència)
 */
@XmlRootElement
@Deprecated
public class RegistroSalidaWs extends RegistroWs {

    private String origen;

    public String getOrigen() {
      return origen;
    }


    public void setOrigen(String origen) {
      this.origen = origen;
    }
}
