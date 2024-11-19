
package org.plugin.lema.api.realizadas.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AnexoUrl complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AnexoUrl">
 *   &lt;complexContent>
 *     &lt;extension base="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}DetalleAnexo">
 *       &lt;sequence>
 *         &lt;element name="enlaceDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnexoUrl", propOrder = {
    "enlaceDocumento"
})
public class AnexoUrl
    extends DetalleAnexo
{

    @XmlElement(required = true)
    protected String enlaceDocumento;

    /**
     * Obtiene el valor de la propiedad enlaceDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnlaceDocumento() {
        return enlaceDocumento;
    }

    /**
     * Define el valor de la propiedad enlaceDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnlaceDocumento(String value) {
        this.enlaceDocumento = value;
    }

}
