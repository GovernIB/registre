
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoFirmaEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoFirmaEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SIN_FIRMA"/>
 *     &lt;enumeration value="EMBEBIDA"/>
 *     &lt;enumeration value="EXTERNA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoFirmaEnum")
@XmlEnum
public enum TipoFirmaEnum {

    SIN_FIRMA,
    EMBEBIDA,
    EXTERNA;

    public String value() {
        return name();
    }

    public static TipoFirmaEnum fromValue(String v) {
        return valueOf(v);
    }

}
