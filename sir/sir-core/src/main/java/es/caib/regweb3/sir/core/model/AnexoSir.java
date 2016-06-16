package es.caib.regweb3.sir.core.model;

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
     * Id del Asiento Registral al que pertenece
     */
    private AsientoRegistralSir idAsientoRegistralSir;

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
    private ValidezDocumento validezDocumento;

    /**
     * Tipo de documento.
     */
    private TipoDocumento tipoDocumento;

    /**
     * Certificado público del fichero anexo.
     */
    private byte[] certificado;

    /**
     * Firma electrónica del fichero anexo.
     */
    private byte[] firma;

    /**
     * Sello de tiempo del fichero anexo.
     */
    private byte[] timestamp;

    /**
     * Validación OCSP del certificado.
     */
    private byte[] validacionOCSPCertificado;

    /**
     * Huella binaria del fichero anexo.
     */
    private byte[] hash;

    /**
     * Tipo MIME del fichero anexo.
     */
    private String tipoMIME;

    /**
     * Fichero Anexo codificado en Base64
     */
    private byte[] anexo;

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ASIENTO_REGISTRAL")
    @ForeignKey(name = "RWE_ANEXOSIR_ASIREG_FK")
    public AsientoRegistralSir getIdAsientoRegistralSir() {
        return idAsientoRegistralSir;
    }

    public void setIdAsientoRegistralSir(AsientoRegistralSir idAsientoRegistralSir) {
        this.idAsientoRegistralSir = idAsientoRegistralSir;
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

    @Column(name = "VALIDEZ_DOCUMENTO", length = 2, nullable = true)
    public ValidezDocumento getValidezDocumento() {
        return validezDocumento;
    }

    public void setValidezDocumento(ValidezDocumento validezDocumento) {
        this.validezDocumento = validezDocumento;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TIPO_DOCUMENTO", length = 2, nullable = false)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Column(name = "CERTIFICADO", nullable = true)
    public byte[] getCertificado() {
        return certificado;
    }

    public void setCertificado(byte[] certificado) {
        this.certificado = certificado;
    }

    @Column(name = "FIRMA", nullable = true)
    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    @Column(name = "TIMESTAMP", nullable = true)
    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "VAL_OCSP_CERTIFICADO", nullable = true)
    public byte[] getValidacionOCSPCertificado() {
        return validacionOCSPCertificado;
    }

    public void setValidacionOCSPCertificado(byte[] validacionOCSPCertificado) {
        this.validacionOCSPCertificado = validacionOCSPCertificado;
    }

    @Column(name = "HASH", nullable = false)
    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    @Column(name = "TIPO_MIME", length = 20, nullable = true)
    public String getTipoMIME() {
        return tipoMIME;
    }

    public void setTipoMIME(String tipoMIME) {
        this.tipoMIME = tipoMIME;
    }

    @Column(name = "ANEXO", nullable = true)
    @Lob
    public byte[] getAnexo() {
        return anexo;
    }

    public void setAnexo(byte[] anexo) {
        this.anexo = anexo;
    }

    @Column(name = "ID_DOCUMENTO_FIRMADO", length = 50, nullable = true)
    public String getIdentificadorDocumentoFirmado() {
        return identificadorDocumentoFirmado;
    }


    public void setIdentificadorDocumentoFirmado(String identificadorDocumentoFirmado) {
        this.identificadorDocumentoFirmado = identificadorDocumentoFirmado;
    }

    @Column(name = "OBSERVACIONES", length = 50, nullable = true)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
