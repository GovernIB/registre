
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para peticionConsultaType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="peticionConsultaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nuRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="incluirJustificante" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="incluirContenidoAnexo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="incluirContenidoAnexoCSV" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "peticionConsultaType", propOrder = {
    "nuRegistro",
    "incluirJustificante",
    "incluirContenidoAnexo",
    "incluirContenidoAnexoCSV"
})
public class PeticionConsultaType {

    @XmlElement(required = true)
    protected String nuRegistro;
    protected boolean incluirJustificante;
    protected boolean incluirContenidoAnexo;
    protected boolean incluirContenidoAnexoCSV;

    /**
     * Obtiene el valor de la propiedad nuRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuRegistro() {
        return nuRegistro;
    }

    /**
     * Define el valor de la propiedad nuRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuRegistro(String value) {
        this.nuRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad incluirJustificante.
     * 
     */
    public boolean isIncluirJustificante() {
        return incluirJustificante;
    }

    /**
     * Define el valor de la propiedad incluirJustificante.
     * 
     */
    public void setIncluirJustificante(boolean value) {
        this.incluirJustificante = value;
    }

    /**
     * Obtiene el valor de la propiedad incluirContenidoAnexo.
     * 
     */
    public boolean isIncluirContenidoAnexo() {
        return incluirContenidoAnexo;
    }

    /**
     * Define el valor de la propiedad incluirContenidoAnexo.
     * 
     */
    public void setIncluirContenidoAnexo(boolean value) {
        this.incluirContenidoAnexo = value;
    }

    /**
     * Obtiene el valor de la propiedad incluirContenidoAnexoCSV.
     * 
     */
    public boolean isIncluirContenidoAnexoCSV() {
        return incluirContenidoAnexoCSV;
    }

    /**
     * Define el valor de la propiedad incluirContenidoAnexoCSV.
     * 
     */
    public void setIncluirContenidoAnexoCSV(boolean value) {
        this.incluirContenidoAnexoCSV = value;
    }

}
