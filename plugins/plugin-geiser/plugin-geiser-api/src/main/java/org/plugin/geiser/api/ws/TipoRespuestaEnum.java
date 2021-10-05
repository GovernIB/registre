
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoRespuestaEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoRespuestaEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OK"/>
 *     &lt;enumeration value="ERROR_INTERNAL"/>
 *     &lt;enumeration value="ERROR_AUTHENTICATION"/>
 *     &lt;enumeration value="ERROR_AUTHORIZATION"/>
 *     &lt;enumeration value="ERROR_CONVERSION"/>
 *     &lt;enumeration value="ERROR_VALIDATOR"/>
 *     &lt;enumeration value="WARNING_NOTFOUND"/>
 *     &lt;enumeration value="WARNING_MAXLIMIT"/>
 *     &lt;enumeration value="WARNING_DUPLICATE"/>
 *     &lt;enumeration value="WARNING_ANEXO_CONTENT_DELETED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoRespuestaEnum", namespace = "http://types.core.ws.rgeco.geiser.minhap.gob.es/")
@XmlEnum
public enum TipoRespuestaEnum {

    OK,
    ERROR_INTERNAL,
    ERROR_AUTHENTICATION,
    ERROR_AUTHORIZATION,
    ERROR_CONVERSION,
    ERROR_VALIDATOR,
    WARNING_NOTFOUND,
    WARNING_MAXLIMIT,
    WARNING_DUPLICATE,
    WARNING_ANEXO_CONTENT_DELETED;

    public String value() {
        return name();
    }

    public static TipoRespuestaEnum fromValue(String v) {
        return valueOf(v);
    }

}
