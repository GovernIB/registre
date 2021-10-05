
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoSilencioAdministrativoEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoSilencioAdministrativoEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="POSITIVO"/>
 *     &lt;enumeration value="NEGATIVO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoSilencioAdministrativoEnum")
@XmlEnum
public enum TipoSilencioAdministrativoEnum {

    POSITIVO,
    NEGATIVO;

    public String value() {
        return name();
    }

    public static TipoSilencioAdministrativoEnum fromValue(String v) {
        return valueOf(v);
    }

}
