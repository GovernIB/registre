package es.caib.regweb3.utils.anexo;

import java.util.Date;

/**
 * Created 28/05/14 9:25
 *
 * @author mgonzalez
 */
// TODO acabar de implmentar. Solo contiene la recopilación de los atributos de metadata para el intercambio
public class AnexoMetadata {

         /* SICRES METADATA */

        /**
        * Los nombres de las variables en este apartado en el documento original(XML) siguen el formato
        * Nombre_Fichero_Anexado y se han transformado a nombreFicheroAnexo.
        */
        private String nombreFicheroAnexado;
        private String identificadorFichero; // TODO REVISAR FORMATO EXACTO EN LA NORMA SICRES
        // se obtiene del atributo codigoSicres
        private String validezDocumento;  // 01--> Copia, 02--> Copia compulsada, etc
        // se obtiene del atributo codigoSicres
        private Long tipoDocumento; //01--> Formulario, 02--> Documento Adjunto al formulario

        // Dades de la firma
        private String certificado; // Base64 ( parte publica del certificado)
        private String firmaDocumento; // Base64
        private String timeStamp; // Base64
        private String validacionOCSPCertifcado; //Base64
        private String hash; // Base64  Controlar por programa que sea obligatorio en caso de firma del documento.
        private String tipoMIME;

        private String identificadorDocumentoFirmado; // En el caso que el anexo sea la firma de otro documento.
        private String observaciones;
        /* FIN SICRES */

        /* NTI DOC ELECTRONICO METADATA*/
        /**
        * Los nombres de las variables en este apartado en el documento original(XML) siguen el formato
        *  EstadoElaboracion y se han transformado a estadoElaboracion.
        */
        private String versionNTI;  // url de la version de la norma NTI conforme a la cual se estructura el documento.
        private String identificador;  // FORMATO ES_<Organo>_<AAAA>_<ID_especifico>


        private String organo;  // Ni pot haver N, sera l'oficina que captura o genera el document.  ??????  Será l'identificador de l'organ a dir3.
        private Date fechaCaptura; // FORMAT AAAAMMDDTHH:MM:SS
        private boolean origenCiudadanoAdministracion; // 0 -> Ciudadano 1-> Administracion
        private String estadoElaboracion;  // TipoEstadoElaboracion ( se corresponde con validez_documento. Se ha de mapear).
        private String nombreFormato; // El valor s'agafarà de l'extensió del fitxer.(OJO TENER EN CUENTA LOS VALORES ADMITIDOS POR LA NORMA.

        private Long tipoDocumental; // TD01 -TD99 (resolució, notificació, denuncia, etc)


       /* private List<TipoFirma> tipoFirma;*/   //poden ser N. Un document pot tenir varies firmes amb varis tipus. Sera una dada que ens vendrà. TF01-TF06
       private String identificadorDocOrigen; //Identificador normalizado del documento de origen al que corresponde la copia.


      public String getNombreFicheroAnexado() {
        return nombreFicheroAnexado;
      }


      public void setNombreFicheroAnexado(String nombreFicheroAnexado) {
        this.nombreFicheroAnexado = nombreFicheroAnexado;
      }


      public String getIdentificadorFichero() {
        return identificadorFichero;
      }


      public void setIdentificadorFichero(String identificadorFichero) {
        this.identificadorFichero = identificadorFichero;
      }


      public String getValidezDocumento() {
        return validezDocumento;
      }


      public void setValidezDocumento(String validezDocumento) {
        this.validezDocumento = validezDocumento;
      }


      public Long getTipoDocumento() {
        return tipoDocumento;
      }


      public void setTipoDocumento(Long tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
      }


      public String getCertificado() {
        return certificado;
      }


      public void setCertificado(String certificado) {
        this.certificado = certificado;
      }


      public String getFirmaDocumento() {
        return firmaDocumento;
      }


      public void setFirmaDocumento(String firmaDocumento) {
        this.firmaDocumento = firmaDocumento;
      }


      public String getTimeStamp() {
        return timeStamp;
      }


      public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
      }


      public String getValidacionOCSPCertifcado() {
        return validacionOCSPCertifcado;
      }


      public void setValidacionOCSPCertifcado(String validacionOCSPCertifcado) {
        this.validacionOCSPCertifcado = validacionOCSPCertifcado;
      }


      public String getHash() {
        return hash;
      }


      public void setHash(String hash) {
        this.hash = hash;
      }


      public String getTipoMIME() {
        return tipoMIME;
      }


      public void setTipoMIME(String tipoMIME) {
        this.tipoMIME = tipoMIME;
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


      public String getVersionNTI() {
        return versionNTI;
      }


      public void setVersionNTI(String versionNTI) {
        this.versionNTI = versionNTI;
      }


      public String getIdentificador() {
        return identificador;
      }


      public void setIdentificador(String identificador) {
        this.identificador = identificador;
      }


      public String getOrgano() {
        return organo;
      }


      public void setOrgano(String organo) {
        this.organo = organo;
      }


      public Date getFechaCaptura() {
        return fechaCaptura;
      }


      public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
      }


      public boolean isOrigenCiudadanoAdministracion() {
        return origenCiudadanoAdministracion;
      }


      public void setOrigenCiudadanoAdministracion(boolean origenCiudadanoAdministracion) {
        this.origenCiudadanoAdministracion = origenCiudadanoAdministracion;
      }


      public String getEstadoElaboracion() {
        return estadoElaboracion;
      }


      public void setEstadoElaboracion(String estadoElaboracion) {
        this.estadoElaboracion = estadoElaboracion;
      }


      public String getNombreFormato() {
        return nombreFormato;
      }


      public void setNombreFormato(String nombreFormato) {
        this.nombreFormato = nombreFormato;
      }


      public Long getTipoDocumental() {
        return tipoDocumental;
      }


      public void setTipoDocumental(Long tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
      }


      public String getIdentificadorDocOrigen() {
        return identificadorDocOrigen;
      }


      public void setIdentificadorDocOrigen(String identificadorDocOrigen) {
        this.identificadorDocOrigen = identificadorDocOrigen;
      }

        /* FIN NTI DOC ELECTRONICO METADATA*/



}
