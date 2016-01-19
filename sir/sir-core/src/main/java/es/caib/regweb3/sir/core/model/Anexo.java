package es.caib.regweb3.sir.core.model;

/**
 * Created by earrivi on 26/11/2015.
 */
public class Anexo {

    /**
     * Identificador del fichero intercambiado.
     */
    private String identificadorFichero = null;

    /**
     * Nombre del fichero original.
     */
    private String nombreFichero;

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

//	/**
//	 * UID del contenido del anexo en el gestor documental.
//	 */
//	private String uidGestorDocumental;

    /**
     * Identificador interno del documento firmado. Si el anexo es firma de otro
     * documento, se especifica el identificador del anexo objeto de firma.
     * Este campo tomará el valor de sí mismo para indicar que contiene la firma
     * embebida.
     */
    private String identificadorFicheroFirmado;

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


    public Anexo() {
    }


    public String getIdentificadorFichero() {
        return identificadorFichero;
    }

    public void setIdentificadorFichero(String identificadorFichero) {
        this.identificadorFichero = identificadorFichero;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public ValidezDocumento getValidezDocumento() {
        return validezDocumento;
    }

    public void setValidezDocumento(ValidezDocumento validezDocumento) {
        this.validezDocumento = validezDocumento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public byte[] getCertificado() {
        return certificado;
    }

    public void setCertificado(byte[] certificado) {
        this.certificado = certificado;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getValidacionOCSPCertificado() {
        return validacionOCSPCertificado;
    }

    public void setValidacionOCSPCertificado(byte[] validacionOCSPCertificado) {
        this.validacionOCSPCertificado = validacionOCSPCertificado;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public String getTipoMIME() {
        return tipoMIME;
    }

    public void setTipoMIME(String tipoMIME) {
        this.tipoMIME = tipoMIME;
    }

    public String getIdentificadorFicheroFirmado() {
        return identificadorFicheroFirmado;
    }

    public void setIdentificadorFicheroFirmado(String identificadorFicheroFirmado) {
        this.identificadorFicheroFirmado = identificadorFicheroFirmado;
    }

    public String getIdentificadorDocumentoFirmado() {
        return identificadorDocumentoFirmado;
    }

    public void setIdentificadorDocumentoFirmado(String identificadorDocumentoFirmado) {
        this.identificadorDocumentoFirmado = identificadorDocumentoFirmado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
