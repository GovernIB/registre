
package es.caib.regweb3.sir.ws.wssir9;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="envioMensajeDatosControlAAplicacionReturn" type="{http://bean.cct.map.es}RespuestaWS"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "envioMensajeDatosControlAAplicacionReturn"
})
@XmlRootElement(name = "envioMensajeDatosControlAAplicacionResponse")
public class EnvioMensajeDatosControlAAplicacionResponse {

    @XmlElement(required = true)
    protected RespuestaWS envioMensajeDatosControlAAplicacionReturn;

    /**
     * Gets the value of the envioMensajeDatosControlAAplicacionReturn property.
     *
     * @return possible object is
     * {@link RespuestaWS }
     */
    public RespuestaWS getEnvioMensajeDatosControlAAplicacionReturn() {
        return envioMensajeDatosControlAAplicacionReturn;
    }

    /**
     * Sets the value of the envioMensajeDatosControlAAplicacionReturn property.
     *
     * @param value allowed object is
     *              {@link RespuestaWS }
     */
    public void setEnvioMensajeDatosControlAAplicacionReturn(RespuestaWS value) {
        this.envioMensajeDatosControlAAplicacionReturn = value;
    }

}
