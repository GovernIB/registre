
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AnexoReferencia complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AnexoReferencia">
 *   &lt;complexContent>
 *     &lt;extension base="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}DetalleAnexo">
 *       &lt;sequence>
 *         &lt;element name="referenciaDocumento" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnexoReferencia", propOrder = {
    "referenciaDocumento"
})
public class AnexoReferencia
    extends DetalleAnexo
{

    @XmlElement(required = true)
    protected byte[] referenciaDocumento;

    /**
     * Obtiene el valor de la propiedad referenciaDocumento.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getReferenciaDocumento() {
        return referenciaDocumento;
    }

    /**
     * Define el valor de la propiedad referenciaDocumento.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setReferenciaDocumento(byte[] value) {
        this.referenciaDocumento = value;
    }

}
