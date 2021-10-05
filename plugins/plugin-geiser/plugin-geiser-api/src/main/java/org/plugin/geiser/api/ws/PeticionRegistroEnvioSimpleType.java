
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para peticionRegistroEnvioSimpleType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="peticionRegistroEnvioSimpleType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}basePeticionRegistroType">
 *       &lt;sequence>
 *         &lt;element name="tipoEnvio" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}TipoEnvio"/>
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
@XmlType(name = "peticionRegistroEnvioSimpleType", propOrder = {
    "tipoEnvio",
    "documentacionFisica"
})
public class PeticionRegistroEnvioSimpleType
    extends BasePeticionRegistroType
{

    @XmlElement(required = true)
    protected TipoEnvio tipoEnvio;
    @XmlElement(required = true)
    protected TipoDocumentacionFisicaEnum documentacionFisica;

    /**
     * Obtiene el valor de la propiedad tipoEnvio.
     * 
     * @return
     *     possible object is
     *     {@link TipoEnvio }
     *     
     */
    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    /**
     * Define el valor de la propiedad tipoEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoEnvio }
     *     
     */
    public void setTipoEnvio(TipoEnvio value) {
        this.tipoEnvio = value;
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
