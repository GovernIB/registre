
package org.plugin.lema.api.realizadas.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Estado.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="Estado">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACEPTADA"/>
 *     &lt;enumeration value="RECHAZADA"/>
 *     &lt;enumeration value="EXPIRADA"/>
 *     &lt;enumeration value="REALIZADA_TEU"/>
 *     &lt;enumeration value="LEIDA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Estado", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas")
@XmlEnum
public enum Estado {

    ACEPTADA,
    RECHAZADA,
    EXPIRADA,
    REALIZADA_TEU,
    LEIDA;

    public String value() {
        return name();
    }

    public static Estado fromValue(String v) {
        return valueOf(v);
    }

}
