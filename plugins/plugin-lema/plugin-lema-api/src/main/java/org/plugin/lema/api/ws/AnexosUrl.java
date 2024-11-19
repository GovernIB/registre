
package org.plugin.lema.api.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AnexosUrl complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AnexosUrl">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anexoUrl" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}AnexoUrl" maxOccurs="5"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnexosUrl", propOrder = {
    "anexoUrl"
})
public class AnexosUrl {

    @XmlElement(required = true)
    protected List<AnexoUrl> anexoUrl;

    /**
     * Gets the value of the anexoUrl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anexoUrl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnexoUrl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnexoUrl }
     * 
     * 
     */
    public List<AnexoUrl> getAnexoUrl() {
        if (anexoUrl == null) {
            anexoUrl = new ArrayList<AnexoUrl>();
        }
        return this.anexoUrl;
    }

}
