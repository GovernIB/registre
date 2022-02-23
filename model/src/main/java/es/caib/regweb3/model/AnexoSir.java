package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by earrivi on 26/11/2015.
 */
@Entity
@Table(name = "RWE_ANEXO_SIR")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class AnexoSir implements Serializable {

    /**
     * Id del anexo
     */
    private Long id;

    /**
     * Id del RegistroSir al que pertenece
     */
    private RegistroSir registroSir;

    /**
     * Nombre del fichero original.
     */
    private String nombreFichero;

    /**
     * Identificador del fichero intercambiado.
     */
    private String identificadorFichero = null;

    /**
     * Validez del documento.
     */
    private String validezDocumento;

    /**
     * Tipo de documento.
     */
    private String tipoDocumento;

    /**
     * Certificado público del fichero anexo.
     */
    private String certificado;

    /**
     * Firma electrónica del fichero anexo.
     */
    private String firma;

    /**
     * Sello de tiempo del fichero anexo.
     */
    private String timestamp;

    /**
     * Validación OCSP del certificado.
     */
    private String validacionOCSPCertificado;

    /**
     * Huella binaria del fichero anexo.
     */
    private String hash;

    /**
     * Tipo MIME del fichero anexo.
     */
    private String tipoMIME;

    /**
     * Archivo Anexo para almacenar en el directorio local
     */
    private Archivo anexo;

    /**
     * Identificador (identificadorFichero) del documento firmado. Si el anexo es firma de otro
     * documento, se especifica el identificador de del fichero objeto de firma.
     * Este campo tomará el valor de sí mismo para indicar que contiene la firma
     * embebida.
     */
    private String identificadorDocumentoFirmado;

    /**
     * Observaciones del fichero adjunto.
     */
    private String observaciones;

    /**
     * Fichero Anexo codificado en Base64
     */
    private byte[] anexoData;

    /**
     * Indica si el Archivo ha sido purgado del sistema
     */
    private Boolean purgado = false;


    public AnexoSir() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGISTRO_SIR")
    @ForeignKey(name = "RWE_ANEXOSIR_REGSIR_FK")
    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
    }

    @Column(name = "NOMBRE_FICHERO", length = 80, nullable = false)
    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    @Column(name = "IDENTIFICADOR_FICHERO", length = 50, nullable = false)
    public String getIdentificadorFichero() {
        return identificadorFichero;
    }

    public void setIdentificadorFichero(String identificadorFichero) {
        this.identificadorFichero = identificadorFichero;
    }

    @Column(name = "VALIDEZ_DOCUMENTO", length = 2)
    public String getValidezDocumento() {
        return validezDocumento;
    }

    public void setValidezDocumento(String validezDocumento) {
        this.validezDocumento = validezDocumento;
    }

    @Column(name = "TIPO_DOCUMENTO", length = 2, nullable = false)
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Lob
    @Column(name = "CERTIFICADO", length = 2147483647)
    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    @Lob
    @Column(name = "FIRMA", length = 2147483647)
    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    @Lob
    @Column(name = "TIMESTAMP", length = 2147483647)
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Lob
    @Column(name = "VAL_OCSP_CE", length = 2147483647)
    public String getValidacionOCSPCertificado() {
        return validacionOCSPCertificado;
    }

    public void setValidacionOCSPCertificado(String validacionOCSPCertificado) {
        this.validacionOCSPCertificado = validacionOCSPCertificado;
    }

    @Lob
    @Column(name = "HASH", nullable = false, length = 2147483647)
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name = "TIPO_MIME", length = 20)
    public String getTipoMIME() {
        return tipoMIME;
    }

    public void setTipoMIME(String tipoMIME) {
        this.tipoMIME = tipoMIME;
    }

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn (name="ANEXO")
    @ForeignKey(name="RWE_ANEXOSIR_ANEXO_FK")
    public Archivo getAnexo() {
        return anexo;
    }

    public void setAnexo(Archivo anexo) {
        this.anexo = anexo;
    }

    @Column(name = "ID_DOCUMENTO_FIRMADO", length = 50)
    public String getIdentificadorDocumentoFirmado() {
        return identificadorDocumentoFirmado;
    }


    public void setIdentificadorDocumentoFirmado(String identificadorDocumentoFirmado) {
        this.identificadorDocumentoFirmado = identificadorDocumentoFirmado;
    }

    @Column(name = "OBSERVACIONES", length = 50)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Column(name = "PURGADO", nullable = false)
    public Boolean getPurgado() {
        return purgado;
    }

    public void setPurgado(Boolean purgado) {
        this.purgado = purgado;
    }

    @Transient
    public byte[] getAnexoData() {
        return anexoData;
    }

    public void setAnexoData(byte[] anexoData) {
        this.anexoData = anexoData;
    }

    @Transient
    public String getNombreFicheroCorto(){

        String nombreFicheroCorto = getNombreFichero();

        if (nombreFicheroCorto.length() > 20) {
            nombreFicheroCorto = getNombreFichero().substring(0, 20) + "...";
        }

        return nombreFicheroCorto;
    }

    @Transient
    public Long getTamano(){

        return getAnexo().getTamano()/1000;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnexoSir anexo = (AnexoSir) o;

        if (!id.equals(anexo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
