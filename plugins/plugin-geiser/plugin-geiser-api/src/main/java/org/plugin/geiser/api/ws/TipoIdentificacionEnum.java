
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoIdentificacionEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoIdentificacionEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NIF"/>
 *     &lt;enumeration value="CIF"/>
 *     &lt;enumeration value="PASAPORTE"/>
 *     &lt;enumeration value="DOCUMENTO_IDENTIFICACION_EXTRANJEROS"/>
 *     &lt;enumeration value="OTROS_PERSONA_FISICA"/>
 *     &lt;enumeration value="CODIGO_DE_ORIGEN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoIdentificacionEnum")
@XmlEnum
public enum TipoIdentificacionEnum {

    NIF,
    CIF,
    PASAPORTE,
    DOCUMENTO_IDENTIFICACION_EXTRANJEROS,
    OTROS_PERSONA_FISICA,
    CODIGO_DE_ORIGEN;

    public String value() {
        return name();
    }

    public static TipoIdentificacionEnum fromValue(String v) {
        return valueOf(v);
    }

}
