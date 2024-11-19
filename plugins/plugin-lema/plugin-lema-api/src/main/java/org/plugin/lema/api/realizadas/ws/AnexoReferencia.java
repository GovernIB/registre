
package org.plugin.lema.api.realizadas.ws;

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
 *     &lt;extension base="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}DetalleAnexo">
 *       &lt;sequence>
 *         &lt;element name="referenciaDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    protected String referenciaDocumento;

    /**
     * Obtiene el valor de la propiedad referenciaDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaDocumento() {
        return referenciaDocumento;
    }

    /**
     * Define el valor de la propiedad referenciaDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaDocumento(String value) {
        this.referenciaDocumento = value;
    }

}
