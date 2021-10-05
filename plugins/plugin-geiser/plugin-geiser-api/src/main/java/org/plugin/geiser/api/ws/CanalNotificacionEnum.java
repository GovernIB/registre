
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para canalNotificacionEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="canalNotificacionEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DIRECCION_POSTAL"/>
 *     &lt;enumeration value="DIRECCION_ELECTRONICA_HABILITADA"/>
 *     &lt;enumeration value="COMPARECENCIA_ELECTRONICA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "canalNotificacionEnum")
@XmlEnum
public enum CanalNotificacionEnum {

    DIRECCION_POSTAL,
    DIRECCION_ELECTRONICA_HABILITADA,
    COMPARECENCIA_ELECTRONICA;

    public String value() {
        return name();
    }

    public static CanalNotificacionEnum fromValue(String v) {
        return valueOf(v);
    }

}
