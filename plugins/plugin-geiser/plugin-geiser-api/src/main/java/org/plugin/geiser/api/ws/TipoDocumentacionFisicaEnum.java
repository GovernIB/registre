
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoDocumentacionFisicaEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoDocumentacionFisicaEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DOCUMENTACION_FISICA_REQUERIDA"/>
 *     &lt;enumeration value="DOCUMENTACION_FISICA_COMPLEMENTARIA"/>
 *     &lt;enumeration value="SIN_DOCUMENTACION_FISICA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoDocumentacionFisicaEnum")
@XmlEnum
public enum TipoDocumentacionFisicaEnum {

    DOCUMENTACION_FISICA_REQUERIDA,
    DOCUMENTACION_FISICA_COMPLEMENTARIA,
    SIN_DOCUMENTACION_FISICA;

    public String value() {
        return name();
    }

    public static TipoDocumentacionFisicaEnum fromValue(String v) {
        return valueOf(v);
    }

}
