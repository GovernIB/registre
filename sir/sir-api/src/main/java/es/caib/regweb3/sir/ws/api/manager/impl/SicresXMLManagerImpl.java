package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.*;
import es.caib.regweb3.sir.core.schema.DeMensaje;
import es.caib.regweb3.sir.core.schema.FicheroIntercambioSICRES3;
import es.caib.regweb3.sir.core.schema.ObjectFactory;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.Versio;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.Base64;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SicresXMLManagerImpl implements SicresXMLManager {

    public final Logger log = Logger.getLogger(getClass());

    private static final String VALIDACIONDIRECTORIOCOMUNPARAMNAME = "validar.codigos.directorio.comun";
    private static final String MAXTAMANOANEXOSPARAMNAME = "max.tamaño.anexos";
    private static final String MAXNUMANEXOSPARAMNAME = "max.num.anexos";

    private static final String CODIGOPAISESPANA = "0724";
    private static final int LONGITUD_CODIGO_ENTIDAD_REGISTRAL = 21;
    private static final int LONGITUD_CODIGO_UNIDAD_TRAMITACION = 21;
    private static final int LONGITUD_IDENTIFICADOR_INTERCAMBIO = 33;
    private static final int LONGITUD_MAX_IDENTIFICADOR_FICHERO = 50;

    private static final String DEFAULT_FILE_EXTENSION = "bin";
    private static final int DEFAULT_MAX_NUM_FILES = 0; // Máximo número de documentos
    private static final long DEFAULT_MAX_FILE_SIZE = 0; // Máximo tamaño por documento (3MB)

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * Map con los valores de los campos del xml que deben estar en formato base64 junto con su expresión xpath de seleccion
     */
    private Map<String, String> base64Fields;

    /**
     * Constructor.
     */
    public SicresXMLManagerImpl() {
    }

    public void setBase64Fields(Map<String, String> base64Fields) {
        this.base64Fields = base64Fields;
    }

    protected void setupBase64Field() {
        LinkedHashMap base64Fields = new LinkedHashMap<String, String>();
        base64Fields.put("Hash", "//Hash/text()");
        base64Fields.put("TimestampEntrada", "//TimestampEntrada/text()");
        base64Fields.put("Certificado", "//Certificado/text()");
        base64Fields.put("FirmaDocumento", "//FirmaDocumento/text()");
        base64Fields.put("TimeStamp", "//TimeStamp/text()");
        base64Fields.put("ValidacionOCSPCertificado", "//ValidacionOCSPCertificado/text()");
        base64Fields.put("Anexo", "//Anexo/text()");
        setBase64Fields(base64Fields);
    }


    public void validarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) {

        log.info("Llamada a validarFicheroIntercambio");

        Assert.notNull(ficheroIntercambio, "'ficheroIntercambio' must not be null");

		/*
         * Validar los segmentos del fichero de intercambio
		 */

        validarSegmentoOrigen(ficheroIntercambio);
        validarSegmentoDestino(ficheroIntercambio);
        validarSegmentoControl(ficheroIntercambio);
        validarSegmentoInteresados(ficheroIntercambio);
        validarSegmentoAsunto(ficheroIntercambio);
        validarSegmentoAnexos(ficheroIntercambio);

        validarSegmentoFormularioGenerico(ficheroIntercambio);

        log.info("Fichero de intercambio validado");
    }


    public void validarAsientoRegistral(AsientoRegistral asiento) {

        log.info("Llamada a validarAsientoRegistral");


        Assert.notNull(asiento, "'asiento' must not be null");

        // Comprobar los datos de origen
        Assert.hasText(asiento.getCodigoEntidadRegistralOrigen(),
                "'codigoEntidadRegistralOrigen' must not be empty");
        Assert.hasText(asiento.getNumeroRegistro(),
                "'numeroRegistroEntrada' must not be empty");
        Assert.notNull(asiento.getFechaRegistro(),
                "'fechaEntrada' must not be null");

        // Comprobar los datos de destino
        Assert.hasText(asiento.getCodigoEntidadRegistralDestino(),
                "'codigoEntidadRegistralDestino' must not be empty");

        // Comprobar los datos de los interesados
        if (!CollectionUtils.isEmpty(asiento.getInteresados())
                && StringUtils.isBlank(asiento
                .getCodigoUnidadTramitacionOrigen())) {
            for (es.caib.regweb3.sir.core.model.Interesado interesado : asiento.getInteresados()) {

                Assert.isTrue(
                        StringUtils.isNotBlank(interesado
                                .getRazonSocialInteresado())
                                || (StringUtils.isNotBlank(interesado
                                .getNombreInteresado()) && StringUtils
                                .isNotBlank(interesado
                                        .getPrimerApellidoInteresado())),
                        "'razonSocialInteresado' or ('nombreInteresado' and 'primerApellidoInteresado') must not be empty");


                if (interesado.getCanalPreferenteComunicacionInteresado() != null) {
                    if (interesado.getCanalPreferenteComunicacionInteresado()
                            .equals(CanalNotificacion.DIRECCION_POSTAL)) {
                        Assert.hasText(interesado.getCodigoPaisInteresado(),
                                "'codigoPaisInteresado' must not be empty");
                        Assert.hasText(interesado.getDireccionInteresado(),
                                "'direccionInteresado' must not be empty");

                        if (CODIGOPAISESPANA.equals(interesado
                                .getCodigoPaisInteresado())) {
                            Assert.isTrue(
                                    StringUtils.isNotBlank(interesado
                                            .getCodigoPostalInteresado())
                                            || (StringUtils
                                            .isNotBlank(interesado
                                                    .getCodigoProvinciaInteresado()) && StringUtils
                                            .isNotBlank(interesado
                                                    .getCodigoMunicipioInteresado())),
                                    "'codigoPostalInteresado' or ('codigoProvinciaInteresado' and 'codigoMunicipioInteresado') must not be empty");
                        }

                    } else if (interesado
                            .getCanalPreferenteComunicacionInteresado()
                            .equals(CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA)) {
                        Assert.hasText(interesado
                                        .getDireccionElectronicaHabilitadaInteresado(),
                                "'direccionElectronicaHabilitadaInteresado' must not be empty");
                    }
                }

                if (interesado.getCanalPreferenteComunicacionRepresentante() != null) {
                    if (interesado
                            .getCanalPreferenteComunicacionRepresentante()
                            .equals(CanalNotificacion.DIRECCION_POSTAL)) {

                        Assert.hasText(interesado.getCodigoPaisRepresentante(),
                                "'codigoPaisRepresentante' must not be empty");
                        Assert.hasText(interesado.getDireccionRepresentante(),
                                "'direccionRepresentante' must not be empty");

                        if (CODIGOPAISESPANA.equals(interesado
                                .getCodigoPaisRepresentante())) {
                            Assert.isTrue(
                                    StringUtils.isNotBlank(interesado
                                            .getCodigoPostalRepresentante())
                                            || (StringUtils
                                            .isNotBlank(interesado
                                                    .getCodigoProvinciaRepresentante()) && StringUtils
                                            .isNotBlank(interesado
                                                    .getCodigoMunicipioRepresentante())),
                                    "'codigoPostalRepresentante' or ('codigoProvinciaRepresentante' and 'codigoMunicipioRepresentante') must not be empty");
                        }

                    } else if (interesado
                            .getCanalPreferenteComunicacionRepresentante()
                            .equals(CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA)) {
                        Assert.hasText(
                                interesado
                                        .getDireccionElectronicaHabilitadaRepresentante(),
                                "'direccionElectronicaHabilitadaRepresentante' must not be empty");
                    }
                }
            }
        }

        // Comprobar los datos de asunto
        Assert.hasText(asiento.getResumen(), "'resumen' must not be empty");

        // Comprobar los datos de los anexos
        if (!CollectionUtils.isEmpty(asiento.getAnexos())) {

            int numAdjuntos = 0; // Número de adjuntos: documentos de tipo
            // "02 - Documento Adjunto" que no son
            // firmas

            for (Anexo anexo : asiento.getAnexos()) {

                Assert.hasText(anexo.getNombreFichero(),
                        "'nombreFichero' must not be empty");
                Assert.notNull(anexo.getTipoDocumento(),
                        "'tipoDocumento' must not be null");
                Assert.notNull(anexo.getHash(), "'hash' must not be null");

                // Si en documento es de tipo "02 - Documento Adjunto"
                if (TipoDocumento.DOCUMENTO_ADJUNTO.equals(anexo.getTipoDocumento())) {
                    numAdjuntos++;
                }

              /*
                // Comprobar si hay que aplicar filtro de tamaño de ficheros
                long maxFileSize = getMaxFileSize();
                if (maxFileSize > 0) {
                    InfoDocumento infoAnexo = getAnexoManager()
                            .getInfoContenidoAnexo(anexo.getId());
                    Assert.isTrue((infoAnexo == null)
                                    || (infoAnexo.getTamano() <= getMaxFileSize()),
                            "Attachment '" + anexo.getNombreFichero()
                                    + "' is too big");
                }*/
            }

            // Comprobar si hay que aplicar filtro de número de ficheros
            int maxNumFiles = getMaxNumFiles();
            if (maxNumFiles > 0) {
                Assert.isTrue(numAdjuntos <= getMaxNumFiles(), "There are too many attachments [" + numAdjuntos + "]");
            }
        }

        // Comprobar los datos de internos o de control
        Assert.hasText(asiento.getIdentificadorIntercambio(),
                "'identificadorIntercambio' must not be empty");
        Assert.notNull(asiento.getTipoRegistro(),
                "'tipoRegistro' must not be null");
        Assert.notNull(asiento.getDocumentacionFisica(),
                "'documentacionFisica' must not be null");
        Assert.notNull(asiento.getIndicadorPrueba(),
                "'indicadorPrueba' must not be null");
        Assert.hasText(asiento.getCodigoEntidadRegistralInicio(),
                "'codigoEntidadRegistralInicio' must not be empty");

        log.info("Asiento registral validado");
    }

    /**
     * @param mensaje
     */
    public void validarMensaje(Mensaje mensaje) {

        log.info("Llamada a validarMensaje");

        Assert.notNull(mensaje, "'mensaje' must not be null");

        Assert.hasText(mensaje.getCodigoEntidadRegistralOrigen(),
                "'codigoEntidadRegistralOrigen' must not be empty");
        Assert.hasText(mensaje.getCodigoEntidadRegistralDestino(),
                "'codigoEntidadRegistralDestino' must not be empty");
        Assert.hasText(mensaje.getIdentificadorIntercambio(),
                "'identificadorIntercambio' must not be empty");
        Assert.notNull(mensaje.getTipoMensaje(),
                "'tipoMensaje' must not be null");

        log.info("Mensaje validado");
    }

    /**
     * @param registroEntrada
     * @return
     */
    public FicheroIntercambio crearFicheroIntercambioSICRES3(RegistroEntrada registroEntrada) {

        Assert.notNull(registroEntrada, "'registroEntrada' must not be null");
        FicheroIntercambio ficheroIntercambio = new FicheroIntercambio();

        ObjectFactory objFactory = new ObjectFactory();
        FicheroIntercambioSICRES3 fiSICRES3 = objFactory.createFicheroIntercambioSICRES3();

        String codOficinaOrigen = obtenerCodigoOficinaOrigen(registroEntrada);
        String identificadorIntercambio = generarIdentificadorIntercambio(codOficinaOrigen);

        /* Segmento DeOrigenORemitente */
        addDatosOrigenORemitente(fiSICRES3, registroEntrada);

        /* Segmento DeDestino */
        addDatosDestino(fiSICRES3, registroEntrada);

        /* Segmento DeInteresados */
        addDatosInteresados(fiSICRES3, registroEntrada.getRegistroDetalle().getInteresados());

        /* Segmento DeAsunto */
        addDatosAsunto(fiSICRES3, registroEntrada);

        /* Segmento DeAnexo */
        addDatosAnexos(fiSICRES3, registroEntrada, identificadorIntercambio);

        /* Segmento DeInternosControl */
        addDatosInternosControl(fiSICRES3, registroEntrada, identificadorIntercambio);

        /* Segmento DeFormularioGenerico */
        addDatosformularioGenerico(fiSICRES3, registroEntrada);

        ficheroIntercambio.setFicheroIntercambio(fiSICRES3);

        return ficheroIntercambio;
    }


    /**
     * Añade el Segmento deOrigenORemitente al Fichero de Intercambio
     *
     * @param ficheroIntercambio
     * @param re
     */
    private void addDatosOrigenORemitente(FicheroIntercambioSICRES3 ficheroIntercambio, RegistroEntrada re) {

        ObjectFactory objFactory = new ObjectFactory();
        FicheroIntercambioSICRES3.DeOrigenORemitente deOrigenORemitente = objFactory.createFicheroIntercambioSICRES3DeOrigenORemitente();

        // Codigo_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(re.getOficina().getCodigo())) {
            deOrigenORemitente.setCodigoEntidadRegistralOrigen(re.getOficina().getCodigo());
        }

        // Decodificacion_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(re.getOficina().getDenominacion())) {
            deOrigenORemitente.setDecodificacionEntidadRegistralOrigen(re.getOficina().getDenominacion());
        }

        // Numero_Registro_Entrada
        if (StringUtils.isNotBlank(re.getNumeroRegistroFormateado())) {
            deOrigenORemitente.setNumeroRegistroEntrada(re.getNumeroRegistroFormateado());
        }


        // Fecha_Hora_Entrada
        if (re.getFecha() != null) {
            deOrigenORemitente.setFechaHoraEntrada(SDF.format(re.getFecha()));
        }


        // Timestamp_Entrada
        //deOrigenORemitente.setTimestampEntrada(); // No es necesario

        Entidad entidad = re.getOficina().getOrganismoResponsable().getEntidad();

        // Codigo_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(entidad.getCodigoDir3())) {
            deOrigenORemitente.setCodigoUnidadTramitacionOrigen(entidad.getCodigoDir3());
        }

        // Decodificacion_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(entidad.getNombre())) {
            deOrigenORemitente.setDecodificacionUnidadTramitacionOrigen(entidad.getNombre());
        }

        ficheroIntercambio.setDeOrigenORemitente(deOrigenORemitente);
    }

    /**
     * Añade el Segmento deDestino al Fichero de Intercambio
     *
     * @param ficheroIntercambio
     * @param re
     */
    private void addDatosDestino(FicheroIntercambioSICRES3 ficheroIntercambio, RegistroEntrada re) {

        ObjectFactory objFactory = new ObjectFactory();
        FicheroIntercambioSICRES3.DeDestino deDestino = objFactory.createFicheroIntercambioSICRES3DeDestino();

        // Codigo_Entidad_Registral_Destino

        //deDestino.setCodigoEntidadRegistralDestino();  todo: Pendiente averiguar Oficina SIR
        //De momento la ponemos a mano  todo: Eliminar cuando se averigüe Oficina SIR
        deDestino.setCodigoEntidadRegistralDestino("A04006749");

        // Decodificacion_Entidad_Registral_Destino
        //deDestino.setDecodificacionEntidadRegistralDestino(); todo: Pendiente averiguar Oficina SIR

        // Codigo_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(re.getDestinoExternoCodigo())) {
            deDestino.setCodigoUnidadTramitacionDestino(re.getDestinoExternoCodigo());
        }

        // Decodificacion_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(re.getDestinoExternoDenominacion())) {
            deDestino.setDecodificacionUnidadTramitacionDestino(re.getDestinoExternoDenominacion());
        }

        ficheroIntercambio.setDeDestino(deDestino);
    }

    /**
     * Añade el Segmento deInteresado al Fichero de Intercambio
     *
     * @param ficheroIntercambio
     * @param interesados
     */
    private void addDatosInteresados(FicheroIntercambioSICRES3 ficheroIntercambio, List<Interesado> interesados) {

        ObjectFactory objFactory = new ObjectFactory();

        if (!CollectionUtils.isEmpty(interesados)) {

            for (Interesado interesado : interesados) {

                if (!interesado.getIsRepresentante()) { // Interesado

                    FicheroIntercambioSICRES3.DeInteresado deInteresado = objFactory.createFicheroIntercambioSICRES3DeInteresado();

                    if (interesado.getTipoDocumentoIdentificacion() != null) {
                        deInteresado.setTipoDocumentoIdentificacionInteresado(String.valueOf(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion())));
                    }
                    if (!StringUtils.isEmpty(interesado.getDocumento())) {
                        deInteresado.setDocumentoIdentificacionInteresado(interesado.getDocumento());
                    }
                    if (!StringUtils.isEmpty(interesado.getRazonSocial())) {
                        deInteresado.setRazonSocialInteresado(interesado.getRazonSocial());
                    }
                    if (!StringUtils.isEmpty(interesado.getNombre())) {
                        deInteresado.setNombreInteresado(interesado.getNombre());
                    }
                    if (!StringUtils.isEmpty(interesado.getApellido1())) {
                        deInteresado.setPrimerApellidoInteresado(interesado.getApellido1());
                    }
                    if (!StringUtils.isEmpty(interesado.getApellido2())) {
                        deInteresado.setSegundoApellidoInteresado(interesado.getApellido2());
                    }
                    if (interesado.getPais() != null) {
                        deInteresado.setPaisInteresado(interesado.getPais().getCodigoPais().toString());
                    }
                    if (interesado.getProvincia() != null) {
                        deInteresado.setProvinciaInteresado(interesado.getProvincia().getCodigoProvincia().toString());
                    }
                    if (interesado.getLocalidad() != null) {
                        deInteresado.setMunicipioInteresado(interesado.getLocalidad().getCodigoLocalidad().toString());
                    }
                    if (!StringUtils.isEmpty(interesado.getDireccion())) {
                        deInteresado.setDireccionInteresado(interesado.getDireccion());
                    }
                    if (!StringUtils.isEmpty(interesado.getCp())) {
                        deInteresado.setCodigoPostalInteresado(interesado.getCp());
                    }
                    if (!StringUtils.isEmpty(interesado.getEmail())) {
                        deInteresado.setCorreoElectronicoInteresado(interesado.getEmail());
                    }
                    if (!StringUtils.isEmpty(interesado.getTelefono())) {
                        deInteresado.setTelefonoContactoInteresado(interesado.getTelefono());
                    }
                    if (!StringUtils.isEmpty(interesado.getDireccionElectronica())) {
                        deInteresado.setDireccionElectronicaHabilitadaInteresado(interesado.getDireccionElectronica());
                    }
                    if (interesado.getCanal() != null) {
                        deInteresado.setCanalPreferenteComunicacionInteresado(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
                    }

                    // Representante
                    if (interesado.getRepresentante() != null) {
                        Interesado representante = interesado.getRepresentante();

                        if (representante.getTipoDocumentoIdentificacion() != null) {
                            deInteresado.setTipoDocumentoIdentificacionRepresentante(String.valueOf(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(representante.getTipoDocumentoIdentificacion())));
                        }
                        if (!StringUtils.isEmpty(representante.getDocumento())) {
                            deInteresado.setDocumentoIdentificacionRepresentante(representante.getDocumento());
                        }
                        if (!StringUtils.isEmpty(representante.getRazonSocial())) {
                            deInteresado.setRazonSocialRepresentante(representante.getRazonSocial());
                        }
                        if (!StringUtils.isEmpty(representante.getNombre())) {
                            deInteresado.setNombreRepresentante(representante.getNombre());
                        }
                        if (!StringUtils.isEmpty(representante.getApellido1())) {
                            deInteresado.setPrimerApellidoRepresentante(representante.getApellido1());
                        }
                        if (!StringUtils.isEmpty(representante.getApellido2())) {
                            deInteresado.setSegundoApellidoRepresentante(representante.getApellido2());
                        }
                        if (representante.getPais() != null) {
                            deInteresado.setPaisRepresentante(representante.getPais().getCodigoPais().toString());
                        }
                        if (representante.getProvincia() != null) {
                            deInteresado.setProvinciaRepresentante(representante.getProvincia().getCodigoProvincia().toString());
                        }
                        if (representante.getLocalidad() != null) {
                            deInteresado.setMunicipioRepresentante(representante.getLocalidad().getCodigoLocalidad().toString());
                        }
                        if (!StringUtils.isEmpty(representante.getDireccion())) {
                            deInteresado.setDireccionRepresentante(representante.getDireccion());
                        }
                        if (!StringUtils.isEmpty(representante.getCp())) {
                            deInteresado.setCodigoPostalRepresentante(representante.getCp());
                        }
                        if (!StringUtils.isEmpty(representante.getEmail())) {
                            deInteresado.setCorreoElectronicoRepresentante(representante.getEmail());
                        }
                        if (!StringUtils.isEmpty(representante.getTelefono())) {
                            deInteresado.setTelefonoContactoRepresentante(representante.getTelefono());
                        }
                        if (!StringUtils.isEmpty(representante.getDireccionElectronica())) {
                            deInteresado.setDireccionElectronicaHabilitadaRepresentante(representante.getDireccionElectronica());
                        }
                        if (representante.getCanal() != null) {
                            deInteresado.setCanalPreferenteComunicacionRepresentante(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(representante.getCanal()));
                        }
                    }

                    if (!StringUtils.isEmpty(interesado.getObservaciones())) {
                        deInteresado.setObservaciones(interesado.getObservaciones());
                    }

                    ficheroIntercambio.getDeInteresado().add(deInteresado);
                }

            }
        } else {
            // De_Interesado es elemento obligatoria su presencia aunque sea vacio y no vengan interesados
            ficheroIntercambio.getDeInteresado().add(objFactory.createFicheroIntercambioSICRES3DeInteresado());
        }
    }

    /**
     * Añade el Segmento deAsunto al Fichero de Intercambio
     *
     * @param ficheroIntercambio
     * @param re
     */
    private void addDatosAsunto(FicheroIntercambioSICRES3 ficheroIntercambio, RegistroEntrada re) {

        ObjectFactory objFactory = new ObjectFactory();
        FicheroIntercambioSICRES3.DeAsunto deAsunto = objFactory.createFicheroIntercambioSICRES3DeAsunto();

        RegistroDetalle registroDetalle = re.getRegistroDetalle();

        // Resumen
        if (StringUtils.isNotBlank(registroDetalle.getExtracto())) {
            deAsunto.setResumen(registroDetalle.getExtracto());
        }
        // Codigo_Asunto_Segun_Destino
        if (registroDetalle.getCodigoAsunto() != null) {
            deAsunto.setCodigoAsuntoSegunDestino(registroDetalle.getCodigoAsunto().getCodigo());
        }

        // Referencia_Externa
        if (StringUtils.isNotBlank(registroDetalle.getReferenciaExterna())) {
            deAsunto.setReferenciaExterna(registroDetalle.getReferenciaExterna());
        }

        // Numero_Expediente
        if (StringUtils.isNotBlank(registroDetalle.getExpediente())) {
            deAsunto.setNumeroExpediente(registroDetalle.getExpediente());
        }

        ficheroIntercambio.setDeAsunto(deAsunto);

    }

    /**
     * Añade el Segmento deAnexo al Fichero de Intercambio
     *
     * @param ficheroIntercambio
     * @param re
     */
    private void addDatosAnexos(FicheroIntercambioSICRES3 ficheroIntercambio, RegistroEntrada re, String identificadorIntercambio) {

      /*  ObjectFactory objFactory = new ObjectFactory();
        int secuencia = 0;

        try {

            List<Anexo> anexos = anexoEjb.getByRegistroEntrada(re.getId());

            for (Integer i = 0; i <anexos.size(); i++) {
                FicheroIntercambioSICRES3.DeAnexo deAnexo = objFactory.createFicheroIntercambioSICRES3DeAnexo();
                Anexo anexo = anexos.get(i);

                final String custodyID = anexo.getCustodiaID();

                Assert.notNull(anexo.getCustodiaID(), "'custodiaID' must not be null");

                byte[] data = null;
                AnnexCustody file = anexoEjb.getInstance().getDocumentInfoOnly(custodyID);
                if (file == null) {
                    file = anexoEjb.getInstance().getSignatureInfoOnly(custodyID);
                    if (file != null) {
                        data = anexoEjb.getInstance().getSignature(custodyID);
                    }
                } else {
                    data = anexoEjb.getInstance().getDocument(custodyID);
                }

                if (file == null) {
                    log.error("El anexo con id " + anexo.getId() + " del registro de entrada "
                            + re.getId() + " no tiene ni documento ni firma definidos", new Exception());
                    continue;
                }


                // Nombre_Fichero_Anexado
                if (StringUtils.isNotBlank(file.getName())) {
                    deAnexo.setNombreFicheroAnexado(file.getName());
                }

                // Identificador_Fichero
                if(anexo.getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                    deAnexo.setIdentificadorFichero(generateIdentificadorFichero(identificadorIntercambio, secuencia, file.getName()));
                    secuencia++;
                }

                // Validez_Documento
                if(anexo.getValidezDocumento() != null){
                    deAnexo.setValidezDocumento(RegwebConstantes.CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()));
                }

                // Tipo_Documento
                if(anexo.getTipoDocumento() != null){
                    deAnexo.setTipoDocumento("02"); //todo: Modificar en el futuro según si el documento viene de Sistra
                }

                // Certificado
                deAnexo.setCertificado(null);

                // Firma_Documento
                deAnexo.setFirmaDocumento(null);

                // TimeStamp
                deAnexo.setTimeStamp(null);

                // Hash
                if(data != null){
                    deAnexo.setHash(obtenerHash(data));
                }

                // Tipo_MIME
                if(StringUtils.isNotBlank(file.getMime())&& file.getMime().length() > 20){
                    deAnexo.setTipoMIME(null);    // Parrafo 48 de la Guia de Aplicación SICRES3
                }else{
                    deAnexo.setTipoMIME(file.getMime());
                }

                // Anexo
                if(data != null){
                    deAnexo.setAnexo(Base64.encode(data).getBytes());
                }

                // Observaciones
                if(StringUtils.isNotBlank(anexo.getObservaciones())){
                    deAnexo.setObservaciones(anexo.getObservaciones());
                }

                ficheroIntercambio.getDeAnexo().add(deAnexo);

                if(anexo.getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {   // Si tiene la firma en otro fichero

                    FicheroIntercambioSICRES3.DeAnexo deAnexoFirma = objFactory.createFicheroIntercambioSICRES3DeAnexo();

                    SignatureCustody sc = anexoEjb.getFirma(custodyID);
                    String filename_firma = sc.getName();
                    deAnexoFirma.setNombreFicheroAnexado(filename_firma);

                    deAnexoFirma.setIdentificadorFichero(generateIdentificadorFichero(identificadorIntercambio, secuencia, file.getName()));
                    deAnexoFirma.setTipoDocumento(RegwebConstantes.CODIGO_NTI_BY_TIPO_DOCUMENTO.get(RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO)); //=03
                    deAnexoFirma.setHash(obtenerHash(sc.getData()));
                    deAnexoFirma.setIdentificadorDocumentoFirmado(deAnexo.getIdentificadorFichero());

                    ficheroIntercambio.getDeAnexo().add(deAnexoFirma);
                    secuencia++;
                }
            }
        }catch (Exception e){
            log.info("Error addDatosAnexo");
            e.printStackTrace();

        }*/
    }

    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param ficheroIntercambio
     * @param re
     */
    private void addDatosInternosControl(FicheroIntercambioSICRES3 ficheroIntercambio, RegistroEntrada re, String identificadorIntercambio) {

        ObjectFactory objFactory = new ObjectFactory();
        FicheroIntercambioSICRES3.DeInternosControl deInternosControl = objFactory.createFicheroIntercambioSICRES3DeInternosControl();

        RegistroDetalle registroDetalle = re.getRegistroDetalle();

        // Tipo_Transporte_Entrada
        if (registroDetalle.getTransporte() != null) {
            deInternosControl.setTipoTransporteEntrada(RegwebConstantes.CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        }

        // Numero_Transporte_Entrada
        if (StringUtils.isNotBlank(registroDetalle.getNumeroTransporte())) {
            deInternosControl.setNumeroTransporteEntrada(registroDetalle.getNumeroTransporte());
        }

        // Nombre_Usuario
        if (StringUtils.isNotBlank(re.getUsuario().getNombreCompleto())) {
            deInternosControl.setNombreUsuario(re.getUsuario().getNombreCompleto());
        }

        // Contacto_Usuario
        if (StringUtils.isNotBlank(re.getUsuario().getUsuario().getEmail())) {
            deInternosControl.setContactoUsuario(re.getUsuario().getUsuario().getEmail());
        }

        // Identificador_Intercambio
        if (StringUtils.isNotBlank(identificadorIntercambio)) {
            deInternosControl.setIdentificadorIntercambio(identificadorIntercambio);
        }


        // Aplicacion_Version_Emisora
        deInternosControl.setAplicacionVersionEmisora(Versio.VERSIO_SIR);

        // Tipo_Anotacion
        deInternosControl.setTipoAnotacion("02"); // todo: Pendiente de asignar correctamente
        deInternosControl.setDescripcionTipoAnotacion("Envío"); // todo: Pendiente de asignar correctamente

        // Tipo_Registro
        deInternosControl.setTipoRegistro("0");

        // Documentacion_Fisica
        if (registroDetalle.getTipoDocumentacionFisica() != null) {
            deInternosControl.setDocumentacionFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        }

        // Observaciones_Apunte
        if (StringUtils.isNotBlank(registroDetalle.getObservaciones())) {
            deInternosControl.setObservacionesApunte(registroDetalle.getObservaciones());
        }

        // Indicador_Prueba
        deInternosControl.setIndicadorPrueba("0"); //todo: añadir propiedad que indique si es pruebas o producción

        // Codigo_Entidad_Registral_Inicio
        deInternosControl.setCodigoEntidadRegistralInicio(obtenerCodigoOficinaOrigen(re));

        // Decodificacion_Entidad_Registral_Inicio
        deInternosControl.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(re));

        ficheroIntercambio.setDeInternosControl(deInternosControl);
    }

    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param ficheroIntercambio
     * @param re
     */
    private void addDatosformularioGenerico(FicheroIntercambioSICRES3 ficheroIntercambio, RegistroEntrada re) {

        ObjectFactory objFactory = new ObjectFactory();
        FicheroIntercambioSICRES3.DeFormularioGenerico deFormularioGenerico = objFactory.createFicheroIntercambioSICRES3DeFormularioGenerico();

        RegistroDetalle registroDetalle = re.getRegistroDetalle();

        // Expone
        if (StringUtils.isNotBlank(registroDetalle.getExpone())) {
            deFormularioGenerico.setExpone(registroDetalle.getExpone());
        } else {
            deFormularioGenerico.setExpone(new String());
        }

        // Solicita
        if (StringUtils.isNotBlank(registroDetalle.getSolicita())) {
            deFormularioGenerico.setSolicita(registroDetalle.getSolicita());
        } else {
            deFormularioGenerico.setSolicita(new String());
        }

        ficheroIntercambio.setDeFormularioGenerico(deFormularioGenerico);
    }


    /**
     * Devuelve un XML con el mensaje de propósito general con el objetivo de
     * realizar tareas de avisos y gestión de flujo del intercambio, conforme a
     * la normativa SICRES 3.0.
     *
     * @param mensaje Información del mensaje.
     * @return XML de mensaje en formato SICRES 3.0
     */
    public String createXMLMensaje(Mensaje mensaje) {

        Assert.notNull(mensaje, "'mensaje' must not be null");

        StringWriter stringWriter = new StringWriter();

        try {

            DeMensaje msg = new DeMensaje();
            msg.setCodigoEntidadRegistralOrigen(mensaje.getCodigoEntidadRegistralOrigen());
            msg.setCodigoEntidadRegistralDestino(mensaje.getCodigoEntidadRegistralDestino());
            msg.setIdentificadorIntercambio(mensaje.getIdentificadorIntercambio());
            msg.setDescripcionMensaje(mensaje.getDescripcionMensaje());

            if (mensaje.getNumeroRegistroEntradaDestino() != null) {
                msg.setNumeroRegistroEntradaDestino(mensaje
                        .getNumeroRegistroEntradaDestino());
            }

            msg.setCodigoError(mensaje.getCodigoError());

           /*
           // Identificadores de ficheros todo:Revisar
            if (mensaje.getIdentificadoresFicheros() != null) {
                msg.setIdentificador_Fichero(mensaje
                        .getIdentificadoresFicheros().toArray(new String[0]));
            }*/

            // Fecha y hora de entrada en destino
            if (mensaje.getFechaEntradaDestino() != null) {
                msg.setFechaHoraEntradaDestino(SDF.format(mensaje.getFechaEntradaDestino()));
            }

            // Tipo de mensaje
            if (mensaje.getTipoMensaje() != null) {
                msg.setTipoMensaje(mensaje.getTipoMensaje().getValue());
            }

            // Indicador de prueba
            if (mensaje.getIndicadorPrueba() != null) {
                msg.setIndicadorPrueba(mensaje.getIndicadorPrueba().getValue());
            }


            JAXBContext jc = JAXBContext.newInstance(es.caib.regweb3.sir.core.schema.DeMensaje.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            m.marshal(msg, stringWriter);

        } catch (Exception e) {
            log.error("Error al crear el XML del mensaje", e);
            throw new SIRException(e.getMessage());
        }

        return stringWriter.toString();
    }

    /**
     * {@inheritDoc}
     */
    public FicheroIntercambio parseXMLFicheroIntercambio(String xml) throws ValidacionException {

        FicheroIntercambio ficheroIntercambio = null;

        log.info("Parseando el XML del fichero de intercambio...");

        try {
            JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller u = jc.createUnmarshaller();

            // validation purpose
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(this.getClass().getClassLoader().getResource("xsd/ficheroIntercambio.xsd")); // validation purpose
            u.setSchema(schema);
            u.setEventHandler(new DefaultValidationEventHandler());

            StringReader reader = new StringReader(xml);
            FicheroIntercambioSICRES3 fiSICRES3 = (FicheroIntercambioSICRES3) u.unmarshal(reader);


            if (fiSICRES3 != null) {
                ficheroIntercambio = new FicheroIntercambio();
                ficheroIntercambio.setFicheroIntercambio(fiSICRES3);

                //Realizamos una validación de los campos del xml que deben estar en base64 en caso de estar presentes
                validateBase64Fields(xml);
            }

        } catch (Throwable e) {
            log.info("Error al parsear el XML del fichero de intercambio: [" + xml + "]", e);
            throw new ValidacionException(Errores.ERROR_0037, e);
        }


        return ficheroIntercambio;
    }


    public Mensaje parseXMLMensaje(String xml) {

        Mensaje mensaje = null;

        log.info("Parseando el XML del mensaje...");

        try {

            JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller u = jc.createUnmarshaller();
            StringReader reader = new StringReader(xml);

            DeMensaje deMensaje = (DeMensaje) u.unmarshal(reader);
            if (deMensaje != null) {

                mensaje = new Mensaje();
                mensaje.setCodigoEntidadRegistralOrigen(deMensaje
                        .getCodigoEntidadRegistralOrigen());
                mensaje.setCodigoEntidadRegistralDestino(deMensaje
                        .getCodigoEntidadRegistralDestino());
                mensaje.setIdentificadorIntercambio(deMensaje
                        .getIdentificadorIntercambio());
                mensaje.setDescripcionMensaje(deMensaje
                        .getDescripcionMensaje());
                mensaje.setNumeroRegistroEntradaDestino(deMensaje
                        .getNumeroRegistroEntradaDestino());
                mensaje.setCodigoError(deMensaje.getCodigoError());

                /*
                // Identificadores de ficheros todo:Revisar
                if (deMensaje.getIdentificadorFichero() != null) {
                    mensaje.setIdentificadoresFicheros(Arrays.asList(deMensaje.getIdentificadorFichero()));
                }*/

                // Fecha y hora de entrada en destino
                String fechaEntradaDestino = deMensaje
                        .getFechaHoraEntradaDestino();
                if (StringUtils.isNotBlank(fechaEntradaDestino)) {
                    mensaje.setFechaEntradaDestino(SDF
                            .parse(fechaEntradaDestino));
                }

                // Tipo de mensaje
                String tipoMensaje = deMensaje.getTipoMensaje();
                if (StringUtils.isNotBlank(tipoMensaje)) {
                    mensaje.setTipoMensaje(TipoMensaje
                            .getTipoMensaje(tipoMensaje));
                }

                // Indicador de prueba
                IndicadorPrueba indicadorPrueba = IndicadorPrueba.valueOf(deMensaje.getIndicadorPrueba());
                if ((indicadorPrueba != null)
                        && StringUtils.isNotBlank(indicadorPrueba.getValue())) {
                    mensaje.setIndicadorPrueba(IndicadorPrueba.getIndicadorPrueba(indicadorPrueba.getValue()));
                }
            }

        } catch (Throwable e) {
            log.error("Error al parsear el XML del mensaje: [" + xml + "]",
                    e);
            throw new ValidacionException(Errores.ERROR_0037, e);
        }


        return mensaje;
    }

    /**
     * Obtiene la Extensión de un Fichero a partir de su nombre
     *
     * @param nombreFichero
     * @return extensión del fichero
     */
    protected String getExtension(String nombreFichero) {
        String extension = "";

        int i = nombreFichero.lastIndexOf('.');
        if (i > 0) {
            extension = nombreFichero.substring(i + 1);
        }

        return extension;
    }

    /**
     * Validar el segmento de origen o remitente
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoOrigen(FicheroIntercambio ficheroIntercambio) {

        // Validar que el código de entidad registral de origen esté informado
        Assert.hasText(ficheroIntercambio.getCodigoEntidadRegistralOrigen(),
                "'CodigoEntidadRegistralOrigen' must not be empty");

        // Validar el código de entidad registral de origen en DIR3
        Assert.isTrue(validarCodigoEntidadRegistral(ficheroIntercambio
                        .getCodigoEntidadRegistralOrigen()),
                "'CodigoEntidadRegistralOrigen' is invalid");

        // Validar el código de unidad de tramitación de origen en DIR3
        if (StringUtils.isNotBlank(ficheroIntercambio
                .getCodigoUnidadTramitacionOrigen())) {
            Assert.isTrue(validarCodigoUnidadTramitacion(ficheroIntercambio
                            .getCodigoUnidadTramitacionOrigen()),
                    "'CodigoUnidadTramitacionOrigen' is invalid");
        }

        // Validar que el número de registro de entrada en origen esté informado
        Assert.hasText(ficheroIntercambio.getNumeroRegistro(),
                "'NumeroRegistroEntrada' must not be empty");

        // Validar que la fecha y hora de entrada esté informada
        Assert.hasText(ficheroIntercambio.getFechaRegistroXML(),
                "'FechaHoraEntrada' must not be empty");

        // Validar el formato de la fecha de entrada
        Assert.isTrue(
                StringUtils.equals(ficheroIntercambio.getFechaRegistroXML(),
                        SDF.format(ficheroIntercambio.getFechaRegistro())),
                "'FechaHoraEntrada' is invalid ["
                        + ficheroIntercambio.getFechaRegistroXML() + "]");

    }

    /**
     * Validar el segmento de destino
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoDestino(
            FicheroIntercambio ficheroIntercambio) {

        // Validar que el código de entidad registral de destino esté informado
        Assert.hasText(ficheroIntercambio.getCodigoEntidadRegistralDestino(),
                "'CodigoEntidadRegistralDestino' must not be empty");

        // Validar el código de entidad registral de destino en DIR3
        Assert.isTrue(validarCodigoEntidadRegistral(ficheroIntercambio
                        .getCodigoEntidadRegistralDestino()),
                "'CodigoEntidadRegistralDestino' is invalid");

        // Validar el código de unidad de tramitación de destino en DIR3
        if (StringUtils.isNotBlank(ficheroIntercambio
                .getCodigoUnidadTramitacionDestino())) {
            Assert.isTrue(validarCodigoUnidadTramitacion(ficheroIntercambio
                            .getCodigoUnidadTramitacionDestino()),
                    "'CodigoUnidadTramitacionDestino' is invalid");
        }

        /*
        //validamos que el destino está configurado en el módulo
        String entidadDestinoConfigurada=getConfiguracionManager().getValorConfiguracion(ficheroIntercambio.getCodigoEntidadRegistralDestino()+".entidad.configurada");
        Assert.isTrue(StringUtils.isNotBlank(entidadDestinoConfigurada),
                "'CodigoEntidadRegistralDestino' is not configurated");*/
    }

    /**
     * Validar el segmento de interesados
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoInteresados(FicheroIntercambio ficheroIntercambio) {

		/*
         * TODO SIR-RC-PR-126
		 *
		 * Recepción de fichero de intercambio
		 * correspondiente a un asiento registral con los campos mínimos
		 * requeridos por la norma SICRES3, CON un interesado persona jurídica y
		 * sin representante sin informar, con canal preferente de notificación
		 * la dirección postal de España.
		 */

        // Comprobar los datos de los interesados
        if ((ficheroIntercambio.getFicheroIntercambio() != null)
                && (ficheroIntercambio.getFicheroIntercambio()
                .getDeInteresado().size() > 0)) {

            for (FicheroIntercambioSICRES3.DeInteresado interesado : ficheroIntercambio
                    .getFicheroIntercambio().getDeInteresado()) {

                // Validar el canal preferente de comunicación del interesado
                if (StringUtils.isNotBlank(interesado
                        .getCanalPreferenteComunicacionInteresado())) {
                    Assert.notNull(
                            CanalNotificacion.getCanalNotificacion(interesado
                                    .getCanalPreferenteComunicacionInteresado()),
                            "'CanalPreferenteComunicacionInteresado' is invalid ["
                                    + interesado
                                    .getCanalPreferenteComunicacionInteresado()
                                    + "]");
                }

                // Validar el canal preferente de comunicación del representante
                if (StringUtils.isNotBlank(interesado
                        .getCanalPreferenteComunicacionRepresentante())) {
                    Assert.notNull(
                            CanalNotificacion.getCanalNotificacion(interesado
                                    .getCanalPreferenteComunicacionRepresentante()),
                            "'CanalPreferenteComunicacionRepresentante' is invalid ["
                                    + interesado
                                    .getCanalPreferenteComunicacionRepresentante()
                                    + "]");
                }

                if (StringUtils.isBlank(ficheroIntercambio
                        .getCodigoUnidadTramitacionOrigen())) {

                    Assert.isTrue(
                            StringUtils.isNotBlank(interesado
                                    .getRazonSocialInteresado())
                                    || (StringUtils.isNotBlank(interesado
                                    .getNombreInteresado()) && StringUtils.isNotBlank(interesado
                                    .getPrimerApellidoInteresado()) || "O".equalsIgnoreCase(interesado.getTipoDocumentoIdentificacionInteresado())),
                            "'razonSocialInteresado' or ('nombreInteresado' and 'primerApellidoInteresado') must not be empty");

					/*
					Assert.isTrue(
							StringUtils
									.isNotBlank(interesado
											.getCanalPreferenteComunicacionInteresado())
									|| StringUtils
											.isNotBlank(interesado
													.getCanalPreferenteComunicacionRepresentante()),
							"'canalPreferenteComunicacionInteresado' or 'canalPreferenteComunicacionRepresentante' must not be null");
					 */

                    if (StringUtils.isNotEmpty(interesado.getTipoDocumentoIdentificacionInteresado())) {
                        Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipoDocumentoIdentificacionInteresado()), "'invalid tipoDocumentoIdentificacionInteresado'");
                    }

                    if (StringUtils.isNotEmpty(interesado.getTipoDocumentoIdentificacionRepresentante())) {
                        Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipoDocumentoIdentificacionRepresentante()), "invalid 'tipoDocumentoIdentificacionRepresentante'");
                    }

                    if (StringUtils.isNotBlank(interesado
                            .getCanalPreferenteComunicacionInteresado())) {
                        if (CanalNotificacion.DIRECCION_POSTAL.getValue()
                                .equals(interesado
                                        .getCanalPreferenteComunicacionInteresado())) {

                            Assert.hasText(interesado.getPaisInteresado(),
                                    "'paisInteresado' must not be empty");
                            Assert.hasText(
                                    interesado.getDireccionInteresado(),
                                    "'direccionInteresado' must not be empty");

                            if (CODIGOPAISESPANA.equals(interesado
                                    .getPaisInteresado())) {
                                Assert.isTrue(
                                        StringUtils.isNotBlank(interesado
                                                .getCodigoPostalInteresado())
                                                || (StringUtils
                                                .isNotBlank(interesado
                                                        .getProvinciaInteresado()) && StringUtils
                                                .isNotBlank(interesado
                                                        .getMunicipioInteresado())),
                                        "'codigoPostalInteresado' or ('provinciaInteresado' and 'municipioInteresado') must not be empty");
                            }

                        } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                                .getValue()
                                .equals(interesado
                                        .getCanalPreferenteComunicacionInteresado())) {
                            Assert.hasText(
                                    interesado
                                            .getDireccionElectronicaHabilitadaInteresado(),
                                    "'direccionElectronicaHabilitadaInteresado' must not be empty");
                        }
                    }

                    if (StringUtils.isNotBlank(interesado
                            .getCanalPreferenteComunicacionRepresentante())) {
                        if (CanalNotificacion.DIRECCION_POSTAL
                                .getValue()
                                .equals(interesado
                                        .getCanalPreferenteComunicacionRepresentante())) {

                            Assert.hasText(interesado.getPaisRepresentante(),
                                    "'paisRepresentante' must not be empty");
                            Assert.hasText(
                                    interesado.getDireccionRepresentante(),
                                    "'direccionRepresentante' must not be empty");

                            if (CODIGOPAISESPANA.equals(interesado
                                    .getPaisRepresentante())) {
                                Assert.isTrue(
                                        StringUtils
                                                .isNotBlank(interesado
                                                        .getCodigoPostalRepresentante())
                                                || (StringUtils
                                                .isNotBlank(interesado
                                                        .getProvinciaRepresentante()) && StringUtils
                                                .isNotBlank(interesado
                                                        .getMunicipioRepresentante())),
                                        "'codigoPostalRepresentante' or ('provinciaRepresentante' and 'municipioRepresentante') must not be empty");
                            }

                        } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                                .getValue()
                                .equals(interesado
                                        .getCanalPreferenteComunicacionRepresentante())) {
                            Assert.hasText(
                                    interesado
                                            .getDireccionElectronicaHabilitadaRepresentante(),
                                    "'direccionElectronicaHabilitadaRepresentante' must not be empty");
                        }
                    }
                }
            }
        }
    }

    /**
     * Validar el segmento de asunto
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoAsunto(FicheroIntercambio ficheroIntercambio) {

        // Validar que el resumen esté informado
        Assert.hasText(ficheroIntercambio.getResumen(),
                "'Resumen' must not be empty");
    }

    /**
     * Validar el segmento de anexos
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoAnexos(FicheroIntercambio ficheroIntercambio) {
/*
        // Validar los documentos
        if ((ficheroIntercambio.getFicheroIntercambio() != null)
                && ArrayUtils.isNotEmpty(ficheroIntercambio.getFicheroIntercambio().getDeAnexo())) {
            for (FicheroIntercambioSICRES3.DeAnexo anexo : ficheroIntercambio.getFicheroIntercambio()
                    .getDeAnexo()) {
                validarAnexo(anexo, ficheroIntercambio.getIdentificadorIntercambio());
            }
        }*/
    }

    /**
     * Valida un anexo del segmento de anexos
     *
     * @param anexo                    Información del anexo
     * @param identificadorIntercambio Identificador de intercambio
     */
    protected void validarAnexo(FicheroIntercambioSICRES3.DeAnexo anexo, String identificadorIntercambio) {

        if (anexo != null) {

            // Validar el nombre del fichero anexado
            Assert.hasText(anexo.getNombreFicheroAnexado(),
                    "'NombreFicheroAnexado' must not be empty");
            Assert.isTrue(
                    !StringUtils.containsAny(anexo.getNombreFicheroAnexado(),
                            "\\/?*:|<>\";&"),
                    "'NombreFicheroAnexado' has invalid characters ["
                            + anexo.getNombreFicheroAnexado() + "]");

            // Validar el identificador de fichero
            validarIdentificadorFichero(anexo, identificadorIntercambio);

            // Validar el campo validez de documento
            if (StringUtils.isNotBlank(anexo.getValidezDocumento())) {
                Assert.notNull(
                        ValidezDocumento
                                .getValidezDocumento(anexo
                                        .getValidezDocumento()),
                        "'ValidezDocumento' is invalid ["
                                + anexo.getValidezDocumento() + "]");
            }

            // Validar el campo tipo de documento
            Assert.hasText(anexo.getTipoDocumento(),
                    "'TipoDocumento' must not be empty");
            Assert.notNull(
                    TipoDocumento.getTipoDocumento(anexo
                            .getTipoDocumento()),
                    "'TipoDocumento' is invalid ["
                            + anexo.getTipoDocumento() + "]");

            // Validar el hash del documento
            // Nota: no se comprueba el código hash de los documentos porque no
            // se especifica con qué algoritmo está generado.
            Assert.isTrue(!ArrayUtils.isEmpty(anexo.getHash()),
                    "'Hash' must not be empty");

            // Validar el tipo MIME
			/*
			if (StringUtils.isNotBlank(anexo.getTipoMIME())) {
				Assert.isTrue(StringUtils.equalsIgnoreCase(
						anexo.getTipoMIME(), MimeTypeUtils.getMimeType(anexo
								.getIdentificadorFichero())),
						"'TipoMIME' does not match 'IdentificadorFichero'");
			}
			*/

			/*
			 * TODO SIR-RC-PR-096
			 *
			 * Recepción de fichero de intercambio correspondiente
			 * a un asiento registral con los campos mínimos requeridos por la
			 * norma SICRES3 y con un fichero anexado y con los campos del
			 * "Hash", "Certificado del firmante", "Firma del documento",
			 * "Sello de tiempo de la firma" y "Validación OCSP del certificado"
			 * informados (no válidos en codificación Base64).
			 */

			/*
			 * TODO SIR-RC-PR-100
			 *
			 * Recepción de fichero de intercambio
			 * correspondiente a un asiento registral con los campos mínimos
			 * requeridos por la norma SICRES3 y con el campo "Anexo" (no
			 * válido).
			 */

        }
    }


    /**
     * Valida el identificador de fichero de un anexo del segmento de anexos
     *
     * @param anexo                    Información del anexo
     * @param identificadorIntercambio Identificador de intercambio
     */
    protected void validarIdentificadorFichero(FicheroIntercambioSICRES3.DeAnexo anexo, String identificadorIntercambio) {

        // No vacío
        Assert.hasText(anexo.getIdentificadorFichero(),
                "'IdentificadorFichero' must not be empty");

        // Validar el tamaño
        Assert.isTrue(StringUtils.length(anexo.getIdentificadorFichero()) <= LONGITUD_MAX_IDENTIFICADOR_FICHERO,
                "'IdentificadorFichero' is invalid");

        // Validar formato: <Identificador del Intercambio><Código de tipo de archivo><Número Secuencial>.<Extensión del fichero>
        String identificadorFichero = anexo.getIdentificadorFichero();
        Assert.isTrue(StringUtils.startsWith(identificadorFichero, identificadorIntercambio),
                "'IdentificadorFichero' does not match 'IdentificadorIntercambio'");

        identificadorFichero = StringUtils.substringAfter(identificadorFichero, identificadorIntercambio + "");
        String[] tokens = StringUtils.split(identificadorFichero, ".");
        Assert.isTrue(ArrayUtils.getLength(tokens) == 3,
                "'IdentificadorFichero' is invalid");

        Assert.isTrue(StringUtils.equals(tokens[0], "01"),
                "'IdentificadorFichero' is invalid"); // Código de tipo de archivo de 2 dígitos
        Assert.isTrue(StringUtils.length(tokens[1]) <= 4,
                "'IdentificadorFichero' is invalid"); // Número secuencial de hasta 4 dígitos
        Assert.isTrue(StringUtils.isNumeric(tokens[1]),
                "'IdentificadorFichero' is invalid"); // Número secuencial compuesto por solo dígitos
        Assert.hasText(tokens[2],
                "'IdentificadorFichero' is invalid"); // Extensión del fichero
    }

    /**
     * Validar el segmento de internos y control
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoControl(
            FicheroIntercambio ficheroIntercambio) {

        // Validar el tipo de transporte
        if (StringUtils.isNotBlank(ficheroIntercambio.getTipoTransporteXML())) {
            Assert.notNull(ficheroIntercambio.getTipoTransporte(),
                    "'TipoTransporteEntrada' is invalid ["
                            + ficheroIntercambio.getTipoTransporteXML() + "]");
        }

        // Validar el identificador de intercambio
        validarIdentificadorIntercambio(ficheroIntercambio);

        // Validar el tipo de anotación
        Assert.hasText(ficheroIntercambio.getTipoAnotacionXML(),
                "'TipoAnotacion' must not be empty");
        Assert.notNull(
                ficheroIntercambio.getTipoAnotacion(),
                "'TipoAnotacion' is invalid ["
                        + ficheroIntercambio.getTipoAnotacionXML() + "]");

        // Validar que el código de entidad registral de inicio esté informado
        Assert.hasText(ficheroIntercambio.getCodigoEntidadRegistralInicio(),
                "'CodigoEntidadRegistralInicio' must not be empty");

        // Validar el código de entidad registral de inicio en DIR3
        Assert.isTrue(validarCodigoEntidadRegistral(ficheroIntercambio
                        .getCodigoEntidadRegistralInicio()),
                "'CodigoEntidadRegistralInicio' is invalid");

        // Validar el identificador de intercambio, tiene que realizarse despues de la validacion del código de entidad registral de inicio
        validarIdentificadorIntercambio(ficheroIntercambio);
    }

    /**
     * Validar el identificador de intercambio.
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarIdentificadorIntercambio(FicheroIntercambio ficheroIntercambio) {

        // Comprobar que no esté vacío
        Assert.hasText(ficheroIntercambio.getIdentificadorIntercambio(),
                "'IdentificadorIntercambio' must not be empty");

        Assert.isTrue(ficheroIntercambio.getIdentificadorIntercambio().length() <= LONGITUD_IDENTIFICADOR_INTERCAMBIO,
                "'IdentificadorIntercambio' is invalid");

        // Comprobar el formato del identificiador de intercambio: <Código Entidad Registral Origen><AA><Número Secuencial>
        String[] tokens = StringUtils.split(
                ficheroIntercambio.getIdentificadorIntercambio(), "_");

        Assert.isTrue(ArrayUtils.getLength(tokens) == 3,
                "'IdentificadorIntercambio' is invalid");
        Assert.isTrue(
                StringUtils.length(tokens[0]) <= LONGITUD_CODIGO_ENTIDAD_REGISTRAL,
                "'IdentificadorIntercambio' is invalid"); // Código de la entidad registral
        Assert.isTrue(
                StringUtils.equals(tokens[0],
                        ficheroIntercambio.getCodigoEntidadRegistralInicio()),
                "'IdentificadorIntercambio' does not match 'CodigoEntidadRegistralInicio'");
        Assert.isTrue(
                StringUtils.length(tokens[1]) == 2,
                "'IdentificadorIntercambio' is invalid"); // Año con 2 dígitos
        Assert.isTrue(StringUtils.isNumeric(tokens[1]),
                "'IdentificadorIntercambio' is invalid"); //numerico

        Assert.isTrue(
                StringUtils.length(tokens[2]) == 8,
                "'IdentificadorIntercambio' is invalid"); // Número secuencia de 8 dígitos

    }

    /**
     * Genera el Hash mediante MD5 del contenido del documento y lo codifica en base64
     *
     * @param documentoData
     * @return
     * @throws Exception
     */
    protected byte[] obtenerHash(byte[] documentoData) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(documentoData);

        return Base64.encode(digest).getBytes();

    }

    /**
     * Genera el identificador de intercambio a partir del código de la oficina de origen
     *
     * @param codOficinaOrigen
     * @return
     * @throws Exception
     */
    protected String generarIdentificadorIntercambio(String codOficinaOrigen) {

        String identificador = "";
        SimpleDateFormat anyo = new SimpleDateFormat("yy"); // Just the year, with 2 digits

        identificador = codOficinaOrigen + "_" + anyo.format(Calendar.getInstance().getTime()) + "_" + getIdToken(); //todo: Añadir secuencia real


        return identificador;
    }

    /**
     * Calcula una cadena de ocho dígitos a partir del instante de tiempo actual.
     *
     * @return la cadena (String) de ocho digitos
     */
    private static final AtomicLong TIME_STAMP = new AtomicLong();

    private String getIdToken() {
        long now = System.currentTimeMillis();
        while (true) {
            long last = TIME_STAMP.get();
            if (now <= last)
                now = last + 1;
            if (TIME_STAMP.compareAndSet(last, now))
                break;
        }
        long unsignedValue = Long.toString(now).hashCode() & 0xffffffffl;
        String result = Long.toString(unsignedValue);
        if (result.length() > 8) {
            result = result.substring(result.length() - 8, result.length());
        } else {
            result = String.format("%08d", unsignedValue);
        }
        return result;
    }

    /**
     * Metodo que genera identificador de anxso según el patron
     * identificadorIntercambio_01_secuencia.extension
     * donde secuencia es cadena que repesenta secuencia en formato 0001 (leftpading con 0 y máximo de 4 caracteres)
     * donde extesion es la extension del anexo
     *
     * @param identificadorIntercambio
     * @param secuencia
     * @param fileName
     * @return
     */
    protected String generateIdentificadorFichero(String identificadorIntercambio, int secuencia, String fileName) {

        String result = new StringBuffer()
                .append(identificadorIntercambio)
                .append("_01_")
                .append(StringUtils.leftPad(
                        String.valueOf(secuencia), 4, "0"))
                .append(".").append(getExtension(fileName)).toString();

        return result;
    }


    /**
     * Obtiene el código Oficina de Origen dependiendo de si es interna o externa
     *
     * @param re
     * @return
     * @throws Exception
     */
    protected String obtenerCodigoOficinaOrigen(RegistroEntrada re) {
        String codOficinaOrigen = null;

        if ((re.getRegistroDetalle().getOficinaOrigenExternoCodigo() == null) && (re.getRegistroDetalle().getOficinaOrigen() == null)) {
            codOficinaOrigen = re.getOficina().getCodigo();
        } else if (re.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null) {
            codOficinaOrigen = re.getRegistroDetalle().getOficinaOrigenExternoCodigo();
        } else {
            codOficinaOrigen = re.getRegistroDetalle().getOficinaOrigen().getCodigo();
        }

        return codOficinaOrigen;
    }

    /**
     * Obtiene el denominación Oficina de Origen dependiendo de si es interna o externa
     *
     * @param re
     * @return
     * @throws Exception
     */
    protected String obtenerDenominacionOficinaOrigen(RegistroEntrada re) {
        String denominacionOficinaOrigen = null;

        if ((re.getRegistroDetalle().getOficinaOrigenExternoCodigo() == null) && (re.getRegistroDetalle().getOficinaOrigen() == null)) {
            denominacionOficinaOrigen = re.getOficina().getCodigo();
        } else if (re.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null) {
            denominacionOficinaOrigen = re.getRegistroDetalle().getOficinaOrigenExternoDenominacion();
        } else {
            denominacionOficinaOrigen = re.getRegistroDetalle().getOficinaOrigen().getDenominacion();
        }

        return denominacionOficinaOrigen;
    }

    /**
     * Validar el segmento de formulario genérico
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoFormularioGenerico(
            FicheroIntercambio ficheroIntercambio) {
        // No hay validaciones
    }

    protected boolean validarCodigoEntidadRegistral(
            String codigoEntidadRegistral) {

        boolean valido = true;

        if (StringUtils.length(codigoEntidadRegistral) > LONGITUD_CODIGO_ENTIDAD_REGISTRAL) {
            return false;
        }

      /*  if (isValidacionDirectorioComun()) {

            Criterios<CriterioOficinaEnum> criterios = new Criterios<CriterioOficinaEnum>();
            criterios.addCriterio(new Criterio<CriterioOficinaEnum>(
                    CriterioOficinaEnum.OFICINAID, codigoEntidadRegistral));

            valido = (getServicioConsultaDirectorioComun().countOficinas(
                    criterios) > 0);
        }*/

        return valido;
    }

    protected boolean validarCodigoUnidadTramitacion(String codigoUnidadTramitacion) {

        boolean valido = true;

        if (StringUtils.length(codigoUnidadTramitacion) > LONGITUD_CODIGO_UNIDAD_TRAMITACION) {
            return false;
        }

		/*
		 * No validar contra el directorio común porque pueden ser códigos de
		 * subdirecciones y éstas no se identifican en el DIR3.
		 */
//		if (isValidacionDirectorioComun()) {
//
//			Criterios<CriterioUnidadOrganicaEnum> criterios = new Criterios<CriterioUnidadOrganicaEnum>();
//			criterios.addCriterio(new Criterio<CriterioUnidadOrganicaEnum>(
//					CriterioUnidadOrganicaEnum.UOID, codigoUnidadTramitacion));
//
//			valido = (getServicioConsultaDirectorioComun()
//					.countUnidadesOrganicas(criterios) > 0);
//		}

        return valido;
    }


    protected int getMaxNumFiles() {

        int maxNumFiles = DEFAULT_MAX_NUM_FILES;

      /*  if (getConfiguracionManager() != null) {

            // Número máximo de adjuntos
            String strMaxNumFiles = getConfiguracionManager()
                    .getValorConfiguracion(
                            MAXNUMANEXOSPARAMNAME);
            log.info("Valor de {} en base de datos: [{}]",
                    MAXNUMANEXOSPARAMNAME, strMaxNumFiles);

            if (StringUtils.isNotBlank(strMaxNumFiles)) {
                maxNumFiles = Integer.valueOf(strMaxNumFiles);
            }
        }*/

        return maxNumFiles;
    }

    /**
     * Realizamos una validación de los campos del xml que deben estar en base64 en caso de estar presentes
     *
     * @param xml
     */
    protected void validateBase64Fields(String xml) {
      /*  XPathReaderUtil reader = null;

        // procesamos el xml para procesar las peticiones xpath
        try {
            reader = new XPathReaderUtil(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log
                    .error("Error al parsear el XML del fichero de intercambio en la validación campos en Base64"
                            + "[" + xml + "]");
            throw new ValidacionException(Errores.ERROR_0037);
        }

        // obtenemos el nombre de los campos en base64 junto con su expresion
        // xpath
        Map<String, String> fieldsBase64 = getBase64Fields();
        Set fieldsNameBase64 = fieldsBase64.keySet();

        for (Iterator iterator = fieldsNameBase64.iterator(); iterator
                .hasNext();) {

            // obtenemos la expresion xpath de los campos que deben estar en
            // base64
            String fieldBase64Name = (String) iterator.next();
            String expression = (String) fieldsBase64.get(fieldBase64Name);

            //realizams la consulta en xpath
            NodeList results = (NodeList) reader.read(expression,
                    XPathConstants.NODESET);

            // verificamos que si los resultados obtenidos son distinto de vacio están en base64
            if (results != null) {
                for (int i = 0; i < results.getLength(); i++) {
                    Node item = results.item(i);
                    String value = item.getNodeValue();
                    if (StringUtils.isNotBlank(value)
                            && !Base64.isBase64(value)) {
                        log
                                .error("Error al parsear el XML del fichero de intercambio: Campo no codificado en Base64"
                                        + fieldBase64Name + "[" + xml + "]");
                        throw new ValidacionException(Errores.ERROR_0037);
                    }
                }
            }
        }*/
    }
}