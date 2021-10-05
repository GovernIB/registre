
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoDocAnexoEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoDocAnexoEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FORMULARIO"/>
 *     &lt;enumeration value="DOCUMENTO_ADJUNTO"/>
 *     &lt;enumeration value="FICHERO_TECNICO_INTERNO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoDocAnexoEnum")
@XmlEnum
public enum TipoDocAnexoEnum {

    FORMULARIO,
    DOCUMENTO_ADJUNTO,
    FICHERO_TECNICO_INTERNO;

    public String value() {
        return name();
    }

    public static TipoDocAnexoEnum fromValue(String v) {
        return valueOf(v);
    }

}
