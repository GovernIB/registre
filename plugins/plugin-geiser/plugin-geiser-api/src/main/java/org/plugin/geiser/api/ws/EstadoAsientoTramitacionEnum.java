
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para estadoAsientoTramitacionEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="estadoAsientoTramitacionEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EN_CURSO"/>
 *     &lt;enumeration value="CONFIRMADO"/>
 *     &lt;enumeration value="RECHAZADO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "estadoAsientoTramitacionEnum", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/")
@XmlEnum
public enum EstadoAsientoTramitacionEnum {

    EN_CURSO,
    CONFIRMADO,
    RECHAZADO;

    public String value() {
        return name();
    }

    public static EstadoAsientoTramitacionEnum fromValue(String v) {
        return valueOf(v);
    }

}
