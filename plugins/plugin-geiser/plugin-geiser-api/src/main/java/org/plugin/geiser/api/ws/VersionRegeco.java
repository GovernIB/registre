
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para VersionRegeco.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="VersionRegeco">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="V1"/>
 *     &lt;enumeration value="V2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VersionRegeco", namespace = "http://types.core.ws.rgeco.geiser.minhap.gob.es/")
@XmlEnum
public enum VersionRegeco {

    @XmlEnumValue("V1")
    V_1("V1"),
    @XmlEnumValue("V2")
    V_2("V2");
    private final String value;

    VersionRegeco(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VersionRegeco fromValue(String v) {
        for (VersionRegeco c: VersionRegeco.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
