
package org.plugin.lema.api.realizadas.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Opciones complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Opciones">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="opcion" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Opcion" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Opciones", propOrder = {
    "opcion"
})
public class Opciones2 {

    protected List<Opcion2> opcion;

    /**
     * Gets the value of the opcion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the opcion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOpcion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Opcion2 }
     * 
     * 
     */
    public List<Opcion2> getOpcion() {
        if (opcion == null) {
            opcion = new ArrayList<Opcion2>();
        }
        return this.opcion;
    }

}
