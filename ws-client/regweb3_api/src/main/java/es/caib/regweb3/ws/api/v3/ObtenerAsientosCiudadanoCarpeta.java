
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Clase Java para obtenerAsientosCiudadanoCarpeta complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerAsientosCiudadanoCarpeta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pageNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idioma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaInicio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaFin" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="numeroRegistroFormateado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estados" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="extracto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultPorPagina" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerAsientosCiudadanoCarpeta", propOrder = {
    "entidad",
    "documento",
    "pageNumber",
    "idioma",
    "fechaInicio",
    "fechaFin",
    "numeroRegistroFormateado",
    "estados",
    "extracto",
    "resultPorPagina"
})
public class ObtenerAsientosCiudadanoCarpeta {

    protected String entidad;
    protected String documento;
    protected Integer pageNumber;
    protected String idioma;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaInicio;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaFin;
    protected String numeroRegistroFormateado;
    @XmlElement(type = Integer.class)
    protected List<Integer> estados;
    protected String extracto;
    protected Integer resultPorPagina;

    /**
     * Obtiene el valor de la propiedad entidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Define el valor de la propiedad entidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad(String value) {
        this.entidad = value;
    }

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumento(String value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad pageNumber.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Define el valor de la propiedad pageNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPageNumber(Integer value) {
        this.pageNumber = value;
    }

    /**
     * Obtiene el valor de la propiedad idioma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Define el valor de la propiedad idioma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdioma(String value) {
        this.idioma = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaInicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Define el valor de la propiedad fechaInicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaInicio(Timestamp value) {
        this.fechaInicio = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaFin.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaFin() {
        return fechaFin;
    }

    /**
     * Define el valor de la propiedad fechaFin.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaFin(Timestamp value) {
        this.fechaFin = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroRegistroFormateado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    /**
     * Define el valor de la propiedad numeroRegistroFormateado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistroFormateado(String value) {
        this.numeroRegistroFormateado = value;
    }

    /**
     * Gets the value of the estados property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the estados property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEstados().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getEstados() {
        if (estados == null) {
            estados = new ArrayList<Integer>();
        }
        return this.estados;
    }

    /**
     * Obtiene el valor de la propiedad extracto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtracto() {
        return extracto;
    }

    /**
     * Define el valor de la propiedad extracto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtracto(String value) {
        this.extracto = value;
    }

    /**
     * Obtiene el valor de la propiedad resultPorPagina.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getResultPorPagina() {
        return resultPorPagina;
    }

    /**
     * Define el valor de la propiedad resultPorPagina.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setResultPorPagina(Integer value) {
        this.resultPorPagina = value;
    }

}
