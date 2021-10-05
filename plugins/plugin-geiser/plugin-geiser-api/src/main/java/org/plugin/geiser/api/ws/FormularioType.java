
package org.plugin.geiser.api.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para formularioType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="formularioType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="campos" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}campoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="secciones" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}seccionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="plazos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="silencioAdministrativo" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoSilencioAdministrativoEnum" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formularioType", propOrder = {
    "titulo",
    "campos",
    "secciones",
    "plazos",
    "silencioAdministrativo"
})
public class FormularioType {

    @XmlElement(required = true)
    protected String titulo;
    protected List<CampoType> campos;
    protected List<SeccionType> secciones;
    protected String plazos;
    protected TipoSilencioAdministrativoEnum silencioAdministrativo;

    /**
     * Obtiene el valor de la propiedad titulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define el valor de la propiedad titulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
    }

    /**
     * Gets the value of the campos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the campos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCampos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CampoType }
     * 
     * 
     */
    public List<CampoType> getCampos() {
        if (campos == null) {
            campos = new ArrayList<CampoType>();
        }
        return this.campos;
    }

    /**
     * Gets the value of the secciones property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the secciones property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecciones().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SeccionType }
     * 
     * 
     */
    public List<SeccionType> getSecciones() {
        if (secciones == null) {
            secciones = new ArrayList<SeccionType>();
        }
        return this.secciones;
    }

    /**
     * Obtiene el valor de la propiedad plazos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlazos() {
        return plazos;
    }

    /**
     * Define el valor de la propiedad plazos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlazos(String value) {
        this.plazos = value;
    }

    /**
     * Obtiene el valor de la propiedad silencioAdministrativo.
     * 
     * @return
     *     possible object is
     *     {@link TipoSilencioAdministrativoEnum }
     *     
     */
    public TipoSilencioAdministrativoEnum getSilencioAdministrativo() {
        return silencioAdministrativo;
    }

    /**
     * Define el valor de la propiedad silencioAdministrativo.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoSilencioAdministrativoEnum }
     *     
     */
    public void setSilencioAdministrativo(TipoSilencioAdministrativoEnum value) {
        this.silencioAdministrativo = value;
    }

}
