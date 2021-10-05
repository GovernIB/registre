
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para validezDocumentoEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="validezDocumentoEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="COPIA"/>
 *     &lt;enumeration value="COPIA_COMPULSADA"/>
 *     &lt;enumeration value="COPIA_ORIGINAL"/>
 *     &lt;enumeration value="ORIGINAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "validezDocumentoEnum")
@XmlEnum
public enum ValidezDocumentoEnum {

    COPIA,
    COPIA_COMPULSADA,
    COPIA_ORIGINAL,
    ORIGINAL;

    public String value() {
        return name();
    }

    public static ValidezDocumentoEnum fromValue(String v) {
        return valueOf(v);
    }

}
