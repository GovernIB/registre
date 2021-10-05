
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoEnvio.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoEnvio">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ENVIO_DESTINO"/>
 *     &lt;enumeration value="ENVIO_INTERESADO"/>
 *     &lt;enumeration value="ENVIO_NOTIFICA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TipoEnvio")
@XmlEnum
public enum TipoEnvio {

    ENVIO_DESTINO,
    ENVIO_INTERESADO,
    ENVIO_NOTIFICA;

    public String value() {
        return name();
    }

    public static TipoEnvio fromValue(String v) {
        return valueOf(v);
    }

}
