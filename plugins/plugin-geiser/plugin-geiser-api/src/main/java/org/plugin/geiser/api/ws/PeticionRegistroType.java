
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para peticionRegistroType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="peticionRegistroType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}basePeticionRegistroType">
 *       &lt;sequence>
 *         &lt;element name="estado" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}estadoAsientoEnum"/>
 *         &lt;element name="documentacionFisica" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoDocumentacionFisicaEnum"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "peticionRegistroType", propOrder = {
    "estado",
    "documentacionFisica"
})
public class PeticionRegistroType
    extends BasePeticionRegistroType
{

    @XmlElement(required = true)
    protected EstadoAsientoEnum estado;
    @XmlElement(required = true)
    protected TipoDocumentacionFisicaEnum documentacionFisica;

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link EstadoAsientoEnum }
     *     
     */
    public EstadoAsientoEnum getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoAsientoEnum }
     *     
     */
    public void setEstado(EstadoAsientoEnum value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad documentacionFisica.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumentacionFisicaEnum }
     *     
     */
    public TipoDocumentacionFisicaEnum getDocumentacionFisica() {
        return documentacionFisica;
    }

    /**
     * Define el valor de la propiedad documentacionFisica.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumentacionFisicaEnum }
     *     
     */
    public void setDocumentacionFisica(TipoDocumentacionFisicaEnum value) {
        this.documentacionFisica = value;
    }

}
