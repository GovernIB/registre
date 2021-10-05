
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para estadoAsientoEnum.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="estadoAsientoEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SIN_DATOS"/>
 *     &lt;enumeration value="PENDIENTE_ENVIO"/>
 *     &lt;enumeration value="ENVIADO_PENDIENTE_CONFIRMACION"/>
 *     &lt;enumeration value="ENVIADO_PENDIENTE_CONFIRMACION_MANUAL"/>
 *     &lt;enumeration value="RECIBIDO_PENDIENTE_CONFIRMACION"/>
 *     &lt;enumeration value="RECIBIDO_PENDIENTE_CONFIRMACION_MANUAL"/>
 *     &lt;enumeration value="ENVIADO_CONFIRMADO"/>
 *     &lt;enumeration value="RECIBIDO_CONFIRMADO"/>
 *     &lt;enumeration value="ENVIADO_RECHAZADO"/>
 *     &lt;enumeration value="RECIBIDO_RECHAZADO"/>
 *     &lt;enumeration value="ANULADO"/>
 *     &lt;enumeration value="REENVIADO"/>
 *     &lt;enumeration value="EN_TRAMITE"/>
 *     &lt;enumeration value="ASIGNADO"/>
 *     &lt;enumeration value="FINALIZADO"/>
 *     &lt;enumeration value="REENVIADO_RECHAZADO"/>
 *     &lt;enumeration value="RECTIFICADO"/>
 *     &lt;enumeration value="ENVIO_PROCESO"/>
 *     &lt;enumeration value="RECIBIDO_RECHAZADO_CIUDADANO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "estadoAsientoEnum")
@XmlEnum
public enum EstadoAsientoEnum {

    SIN_DATOS,
    PENDIENTE_ENVIO,
    ENVIADO_PENDIENTE_CONFIRMACION,
    ENVIADO_PENDIENTE_CONFIRMACION_MANUAL,
    RECIBIDO_PENDIENTE_CONFIRMACION,
    RECIBIDO_PENDIENTE_CONFIRMACION_MANUAL,
    ENVIADO_CONFIRMADO,
    RECIBIDO_CONFIRMADO,
    ENVIADO_RECHAZADO,
    RECIBIDO_RECHAZADO,
    ANULADO,
    REENVIADO,
    EN_TRAMITE,
    ASIGNADO,
    FINALIZADO,
    REENVIADO_RECHAZADO,
    RECTIFICADO,
    ENVIO_PROCESO,
    RECIBIDO_RECHAZADO_CIUDADANO;

    public String value() {
        return name();
    }

    public static EstadoAsientoEnum fromValue(String v) {
        return valueOf(v);
    }

}
