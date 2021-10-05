
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoAsientoEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoAsientoEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ENTRADA"/>
 *     &lt;enumeration value="SALIDA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoAsientoEnum")
@XmlEnum
public enum TipoAsientoEnum {

    ENTRADA,
    SALIDA;

    public String value() {
        return name();
    }

    public static TipoAsientoEnum fromValue(String v) {
        return valueOf(v);
    }

}
