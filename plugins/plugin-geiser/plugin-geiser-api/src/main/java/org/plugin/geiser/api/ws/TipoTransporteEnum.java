
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoTransporteEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoTransporteEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SERVICIO_MENSAJEROS"/>
 *     &lt;enumeration value="CORREO_POSTAL"/>
 *     &lt;enumeration value="CORREO_POSTAL_CERTIFICADO"/>
 *     &lt;enumeration value="BUROFAX"/>
 *     &lt;enumeration value="EN_MANO"/>
 *     &lt;enumeration value="FAX"/>
 *     &lt;enumeration value="OTROS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoTransporteEnum")
@XmlEnum
public enum TipoTransporteEnum {

    SERVICIO_MENSAJEROS,
    CORREO_POSTAL,
    CORREO_POSTAL_CERTIFICADO,
    BUROFAX,
    EN_MANO,
    FAX,
    OTROS;

    public String value() {
        return name();
    }

    public static TipoTransporteEnum fromValue(String v) {
        return valueOf(v);
    }

}
