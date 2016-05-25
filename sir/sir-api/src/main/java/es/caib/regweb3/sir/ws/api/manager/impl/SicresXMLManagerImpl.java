package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.sir.api.schema.De_Anexo;
import es.caib.regweb3.sir.api.schema.De_Interesado;
import es.caib.regweb3.sir.api.schema.De_Mensaje;
import es.caib.regweb3.sir.api.schema.Fichero_Intercambio_SICRES_3;
import es.caib.regweb3.sir.api.schema.types.Indicador_PruebaType;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.*;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.sir.ws.api.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.utils.Mensaje;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.Versio;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.utils.Base64;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

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
                "'codigoEntidadRegistralOrigen' no puede estar vacio");
        Assert.hasText(asiento.getNumeroRegistro(),
                "'numeroRegistroEntrada' no puede estar vacio");
        Assert.notNull(asiento.getFechaRegistro(),
                "'fechaEntrada' must not be null");

        // Comprobar los datos de destino
        Assert.hasText(asiento.getCodigoEntidadRegistralDestino(),
                "'codigoEntidadRegistralDestino' no puede estar vacio");

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
                        "'razonSocialInteresado' or ('nombreInteresado' and 'primerApellidoInteresado') no puede estar vacio");


                if (interesado.getCanalPreferenteComunicacionInteresado() != null) {
                    if (interesado.getCanalPreferenteComunicacionInteresado()
                            .equals(CanalNotificacion.DIRECCION_POSTAL)) {
                        Assert.hasText(interesado.getCodigoPaisInteresado(),
                                "'codigoPaisInteresado' no puede estar vacio");
                        Assert.hasText(interesado.getDireccionInteresado(),
                                "'direccionInteresado' no puede estar vacio");

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
                                    "'codigoPostalInteresado' or ('codigoProvinciaInteresado' and 'codigoMunicipioInteresado') no puede estar vacio");
                        }

                    } else if (interesado
                            .getCanalPreferenteComunicacionInteresado()
                            .equals(CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA)) {
                        Assert.hasText(interesado
                                        .getDireccionElectronicaHabilitadaInteresado(),
                                "'direccionElectronicaHabilitadaInteresado' no puede estar vacio");
                    }
                }

                if (interesado.getCanalPreferenteComunicacionRepresentante() != null) {
                    if (interesado
                            .getCanalPreferenteComunicacionRepresentante()
                            .equals(CanalNotificacion.DIRECCION_POSTAL)) {

                        Assert.hasText(interesado.getCodigoPaisRepresentante(),
                                "'codigoPaisRepresentante' no puede estar vacio");
                        Assert.hasText(interesado.getDireccionRepresentante(),
                                "'direccionRepresentante' no puede estar vacio");

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
                                    "'codigoPostalRepresentante' or ('codigoProvinciaRepresentante' and 'codigoMunicipioRepresentante') no puede estar vacio");
                        }

                    } else if (interesado
                            .getCanalPreferenteComunicacionRepresentante()
                            .equals(CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA)) {
                        Assert.hasText(
                                interesado
                                        .getDireccionElectronicaHabilitadaRepresentante(),
                                "'direccionElectronicaHabilitadaRepresentante' no puede estar vacio");
                    }
                }
            }
        }

        // Comprobar los datos de asunto
        Assert.hasText(asiento.getResumen(), "'resumen' no puede estar vacio");

        // Comprobar los datos de los anexos
        if (!CollectionUtils.isEmpty(asiento.getAnexos())) {

            int numAdjuntos = 0; // Número de adjuntos: documentos de tipo
            // "02 - Documento Adjunto" que no son
            // firmas

            for (es.caib.regweb3.sir.core.model.Anexo anexo : asiento.getAnexos()) {

                Assert.hasText(anexo.getNombreFichero(),
                        "'nombreFichero' no puede estar vacio");
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
                "'identificadorIntercambio' no puede estar vacio");
        Assert.notNull(asiento.getTipoRegistro(),
                "'tipoRegistro' must not be null");
        Assert.notNull(asiento.getDocumentacionFisica(),
                "'documentacionFisica' must not be null");
        Assert.notNull(asiento.getIndicadorPrueba(),
                "'indicadorPrueba' must not be null");
        Assert.hasText(asiento.getCodigoEntidadRegistralInicio(),
                "'codigoEntidadRegistralInicio' no puede estar vacio");

        log.info("Asiento registral validado");
    }

    /**
     * @param mensaje
     */
    public void validarMensaje(Mensaje mensaje) {

        log.info("Llamada a validarMensaje");

        Assert.notNull(mensaje, "'mensaje' must not be null");

        Assert.hasText(mensaje.getCodigoEntidadRegistralOrigen(),
                "'codigoEntidadRegistralOrigen' no puede estar vacio");
        Assert.hasText(mensaje.getCodigoEntidadRegistralDestino(),
                "'codigoEntidadRegistralDestino' no puede estar vacio");
        Assert.hasText(mensaje.getIdentificadorIntercambio(),
                "'identificadorIntercambio' no puede estar vacio");
        Assert.notNull(mensaje.getTipoMensaje(),
                "'tipoMensaje' must not be null");

        log.info("Mensaje validado");
    }

    /**
     * @param registroEntrada
     * @return
     */
    public String crearXMLFicheroIntercambioSICRES3(RegistroEntrada registroEntrada) {

        Assert.notNull(registroEntrada, "'registroEntrada' must not be null");
        FicheroIntercambio ficheroIntercambio = new FicheroIntercambio();

        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");

        // Fichero_Intercambio_SICRES_3
        Element rootNode = doc.addElement("Fichero_Intercambio_SICRES_3");

        String codOficinaOrigen = obtenerCodigoOficinaOrigen(registroEntrada);
        String identificadorIntercambio = generarIdentificadorIntercambio(codOficinaOrigen);

        /* Segmento DeOrigenORemitente */
        addDatosOrigenORemitente(rootNode, registroEntrada);

        /* Segmento DeDestino */
        addDatosDestino(rootNode, registroEntrada);

        /* Segmento DeInteresados */
        addDatosInteresados(rootNode, registroEntrada.getRegistroDetalle().getInteresados());

        /* Segmento DeAsunto */
        addDatosAsunto(rootNode, registroEntrada);

        /* Segmento DeAnexo */
        addDatosAnexos(rootNode, registroEntrada, identificadorIntercambio);

        /* Segmento DeInternosControl */
        addDatosInternosControl(rootNode, registroEntrada, identificadorIntercambio);

        /* Segmento DeFormularioGenerico */
        addDatosformularioGenerico(rootNode, registroEntrada);

        //ficheroIntercambio.setFicheroIntercambio(rootNode);

        return doc.asXML();
    }


    /**
     * Añade el Segmento deOrigenORemitente al Fichero de Intercambio
     *
     * @param rootNode
     * @param re
     */
    private void addDatosOrigenORemitente(Element rootNode, RegistroEntrada re) {

        // De_Origen_o_Remitente
        Element rootElement = rootNode.addElement("De_Origen_o_Remitente");
        Element elem = null;

        // Codigo_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(re.getOficina().getCodigo())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Origen");
            elem.addCDATA(re.getOficina().getCodigo());
        }

        // Decodificacion_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(re.getOficina().getDenominacion())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Origen");
            elem.addCDATA(re.getOficina().getDenominacion());
        }

        // Numero_Registro_Entrada
        if (StringUtils.isNotBlank(re.getNumeroRegistroFormateado())) {
            elem = rootElement.addElement("Numero_Registro_Entrada");
            elem.addCDATA(re.getNumeroRegistroFormateado());
        }


        // Fecha_Hora_Entrada
        if (re.getFecha() != null) {
            elem = rootElement.addElement("Fecha_Hora_Entrada");
            elem.addCDATA(SDF.format(re.getFecha()));
        }


        // Timestamp_Entrada
        //deOrigenORemitente.setTimestampEntrada(); // No es necesario

        Entidad entidad = re.getOficina().getOrganismoResponsable().getEntidad();

        // Codigo_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(entidad.getCodigoDir3())) {
            elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Origen");
            elem.addCDATA(entidad.getCodigoDir3());
        }

        // Decodificacion_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(entidad.getNombre())) {
            elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Origen");
            elem.addCDATA(entidad.getNombre());
        }

    }

    /**
     * Añade el Segmento deDestino al Fichero de Intercambio
     *
     * @param rootNode
     * @param re
     */
    private void addDatosDestino(Element rootNode, RegistroEntrada re) {

        // De_Destino
        Element rootElement = rootNode.addElement("De_Destino");
        Element elem = null;

        // Codigo_Entidad_Registral_Destino

        //deDestino.setCodigoEntidadRegistralDestino();  todo: Pendiente averiguar Oficina SIR
        //De momento la ponemos a mano  todo: Eliminar cuando se averigüe Oficina SIR
        elem = rootElement.addElement("Codigo_Entidad_Registral_Destino");
        elem.addCDATA("A04006749");


        // Decodificacion_Entidad_Registral_Destino
        //deDestino.setDecodificacionEntidadRegistralDestino(); todo: Pendiente averiguar Oficina SIR
        //elem = rootElement.addElement("Decodificacion_Entidad_Registral_Destino");
        //elem.addCDATA("");

        // Codigo_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(re.getDestinoExternoCodigo())) {
            elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Destino");
            elem.addCDATA(re.getDestinoExternoCodigo());
        }

        // Decodificacion_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(re.getDestinoExternoDenominacion())) {
            elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Destino");
            elem.addCDATA(re.getDestinoExternoDenominacion());
        }

    }

    protected static String getBase64String(byte[] dato) {
        String result = null;

        result = Base64.encode(dato);

        return result;
    }

    /**
     * Añade el Segmento deInteresado al Fichero de Intercambio
     *
     * @param rootNode
     * @param interesados
     */
    private void addDatosInteresados(Element rootNode, List<Interesado> interesados) {

        if (!CollectionUtils.isEmpty(interesados)) {

            for (Interesado interesado : interesados) {

                if (!interesado.getIsRepresentante()) { // Interesado

                    Element rootElement = rootNode.addElement("De_Interesado");
                    Element elem = null;

                    if (interesado.getTipoDocumentoIdentificacion() != null) {
                        elem = rootElement.addElement("Tipo_Documento_Identificacion_Interesado");
                        elem.addCDATA(String.valueOf(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion())));
                    }
                    if (!StringUtils.isEmpty(interesado.getDocumento())) {
                        elem = rootElement.addElement("Documento_Identificacion_Interesado");
                        elem.addCDATA(interesado.getDocumento());
                    }
                    if (!StringUtils.isEmpty(interesado.getRazonSocial())) {
                        elem = rootElement.addElement("Razon_Social_Interesado");
                        elem.addCDATA(interesado.getRazonSocial());
                    }
                    if (!StringUtils.isEmpty(interesado.getNombre())) {
                        elem = rootElement.addElement("Nombre_Interesado");
                        elem.addCDATA(interesado.getNombre());
                    }
                    if (!StringUtils.isEmpty(interesado.getApellido1())) {
                        elem = rootElement.addElement("Primer_Apellido_Interesado");
                        elem.addCDATA(interesado.getApellido1());
                    }
                    if (!StringUtils.isEmpty(interesado.getApellido2())) {
                        elem = rootElement.addElement("Segundo_Apellido_Interesado");
                        elem.addCDATA(interesado.getApellido2());
                    }
                    if (interesado.getPais() != null) {
                        elem = rootElement.addElement("Pais_Interesado");
                        elem.addCDATA(interesado.getPais().getCodigoPais().toString());
                    }
                    if (interesado.getProvincia() != null) {
                        elem = rootElement.addElement("Provincia_Interesado");
                        elem.addCDATA(interesado.getProvincia().getCodigoProvincia().toString());
                    }
                    if (interesado.getLocalidad() != null) {
                        elem = rootElement.addElement("Municipio_Interesado");
                        elem.addCDATA(interesado.getLocalidad().getCodigoLocalidad().toString());
                    }
                    if (!StringUtils.isEmpty(interesado.getDireccion())) {
                        elem = rootElement.addElement("Direccion_Interesado");
                        elem.addCDATA(interesado.getDireccion());
                    }
                    if (!StringUtils.isEmpty(interesado.getCp())) {
                        elem = rootElement.addElement("Codigo_Postal_Interesado");
                        elem.addCDATA(interesado.getCp());
                    }
                    if (!StringUtils.isEmpty(interesado.getEmail())) {
                        elem = rootElement.addElement("Correo_Electronico_Interesado");
                        elem.addCDATA(interesado.getEmail());
                    }
                    if (!StringUtils.isEmpty(interesado.getTelefono())) {
                        elem = rootElement.addElement("Telefono_Contacto_Interesado");
                        elem.addCDATA(interesado.getTelefono());
                    }
                    if (!StringUtils.isEmpty(interesado.getDireccionElectronica())) {
                        elem = rootElement.addElement("Direccion_Electronica_Habilitada_Interesado");
                        elem.addCDATA(interesado.getDireccionElectronica());
                    }
                    if (interesado.getCanal() != null) {
                        elem = rootElement.addElement("Canal_Preferente_Comunicacion_Interesado");
                        elem.addCDATA(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
                    }

                    // Representante
                    if (interesado.getRepresentante() != null) {
                        Interesado representante = interesado.getRepresentante();

                        if (representante.getTipoDocumentoIdentificacion() != null) {
                            elem = rootElement.addElement("Tipo_Documento_Identificacion_Representante");
                            elem.addCDATA(String.valueOf(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(representante.getTipoDocumentoIdentificacion())));
                        }
                        if (!StringUtils.isEmpty(representante.getDocumento())) {
                            elem = rootElement.addElement("Documento_Identificacion_Representante");
                            elem.addCDATA(representante.getDocumento());
                        }
                        if (!StringUtils.isEmpty(representante.getRazonSocial())) {
                            elem = rootElement.addElement("Razon_Social_Representante");
                            elem.addCDATA(representante.getRazonSocial());
                        }
                        if (!StringUtils.isEmpty(representante.getNombre())) {
                            elem = rootElement.addElement("Nombre_Representante");
                            elem.addCDATA(representante.getNombre());
                        }
                        if (!StringUtils.isEmpty(representante.getApellido1())) {
                            elem = rootElement.addElement("Primer_Apellido_Representante");
                            elem.addCDATA(representante.getApellido1());
                        }
                        if (!StringUtils.isEmpty(representante.getApellido2())) {
                            elem = rootElement.addElement("Segundo_Apellido_Representante");
                            elem.addCDATA(representante.getApellido2());
                        }
                        if (representante.getPais() != null) {
                            elem = rootElement.addElement("Pais_Representante");
                            elem.addCDATA(representante.getPais().getCodigoPais().toString());
                        }
                        if (representante.getProvincia() != null) {
                            elem = rootElement.addElement("Provincia_Representante");
                            elem.addCDATA(representante.getProvincia().getCodigoProvincia().toString());
                        }
                        if (representante.getLocalidad() != null) {
                            elem = rootElement.addElement("Municipio_Representante");
                            elem.addCDATA(representante.getLocalidad().getCodigoLocalidad().toString());
                        }
                        if (!StringUtils.isEmpty(representante.getDireccion())) {
                            elem = rootElement.addElement("Direccion_Representante");
                            elem.addCDATA(representante.getDireccion());
                        }
                        if (!StringUtils.isEmpty(representante.getCp())) {
                            elem = rootElement.addElement("Codigo_Postal_Representante");
                            elem.addCDATA(representante.getCp());
                        }
                        if (!StringUtils.isEmpty(representante.getEmail())) {
                            elem = rootElement.addElement("Correo_Electronico_Representante");
                            elem.addCDATA(representante.getEmail());
                        }
                        if (!StringUtils.isEmpty(representante.getTelefono())) {
                            elem = rootElement.addElement("Telefono_Contacto_Representante");
                            elem.addCDATA(representante.getTelefono());
                        }
                        if (!StringUtils.isEmpty(representante.getDireccionElectronica())) {
                            elem = rootElement.addElement("Direccion_Electronica_Habilitada_Representante");
                            elem.addCDATA(representante.getDireccionElectronica());
                        }
                        if (representante.getCanal() != null) {
                            elem = rootElement.addElement("Canal_Preferente_Comunicacion_Representante");
                            elem.addCDATA(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(representante.getCanal()));
                        }
                    }

                    if (!StringUtils.isEmpty(interesado.getObservaciones())) {
                        elem = rootElement.addElement("Observaciones");
                        elem.addCDATA(interesado.getObservaciones());
                    }

                }

            }
        } else {
            // De_Interesado es elemento obligatoria su presencia aunque sea vacio y no vengan interesados
            Element rootElement = rootNode.addElement("De_Interesado");
        }
    }

    /**
     * Añade el Segmento deAsunto al Fichero de Intercambio
     *
     * @param rootNode
     * @param re
     */
    private void addDatosAsunto(Element rootNode, RegistroEntrada re) {

        // De_Asunto
        Element rootElement = rootNode.addElement("De_Asunto");
        Element elem = null;

        RegistroDetalle registroDetalle = re.getRegistroDetalle();

        // Resumen
        elem = rootElement.addElement("Resumen");
        if (StringUtils.isNotBlank(registroDetalle.getExtracto())) {
            elem.addCDATA(registroDetalle.getExtracto());
        }
        // Codigo_Asunto_Segun_Destino
        if (registroDetalle.getCodigoAsunto() != null) {
            elem = rootElement.addElement("Codigo_Asunto_Segun_Destino");
            elem.addCDATA(registroDetalle.getCodigoAsunto().getCodigo());
        }

        // Referencia_Externa
        if (StringUtils.isNotBlank(registroDetalle.getReferenciaExterna())) {
            elem = rootElement.addElement("Referencia_Externa");
            elem.addCDATA(registroDetalle.getReferenciaExterna());
        }

        // Numero_Expediente
        if (StringUtils.isNotBlank(registroDetalle.getExpediente())) {
            elem = rootElement.addElement("Numero_Expediente");
            elem.addCDATA(registroDetalle.getExpediente());
        }

    }

    /**
     * Añade el Segmento deAnexo al Fichero de Intercambio
     *
     * @param rootNode
     * @param re
     */
    private void addDatosAnexos_old(Element rootNode, RegistroEntrada re, String identificadorIntercambio) {

        int secuencia = 0;

        try {

            //ANTIGUO List<Anexo> anexos = anexoEjb.getByRegistroEntrada(re.getId());
            List<Anexo> anexos = re.getRegistroDetalle().getAnexos(); // Deben pasarnos el re con los anexos cargados
            List<AnexoFull> anexosFull = re.getRegistroDetalle().getAnexosFull(); // Deben pasarnos el re con los anexos cargados


            for (AnexoFull anexoFull : anexosFull) {
                Anexo anexo = anexoFull.getAnexo();
                //Inicio del segmento "De_Anexo" para el mensaje de intercambio.
                //De_Anexo
                Element rootElement = rootNode.addElement("De_Anexo");
                Element elem = null;

                //El custodiaID no puede ser null
                final String custodyID = anexo.getCustodiaID();
                Assert.notNull(anexo.getCustodiaID(), "'custodiaID' must not be null");


                //Obtenemos los documentos físicos del sistema de custodia
                byte[] data = null;
                DocumentCustody dc = null; //Documento asociado
                SignatureCustody sc = null;// Firma del documento Asociado
                String filename = new String();
                String identificador_fichero = new String();
                if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) { //CASO 5: DOC AMB FIRMA ATTACHED
                    //en esta API el documento con la firma se encuentra en DocumentoCustody
                    dc = anexoFull.getDocumentoCustody();
                    if (dc != null) {
                        filename = dc.getName();
                        data = dc.getData();
                    }

                    //Especificamos el resto de propiedades del segmento anexo
                    // Nombre_Fichero_Anexado
                    if (StringUtils.isNotBlank(filename)) {
                        elem = rootElement.addElement("Nombre_Fichero_Anexado");
                        elem.addCDATA(filename);
                    }

                    // Identificador_Fichero
                    elem = rootElement.addElement("Identificador_Fichero");
                    identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, filename);
                    elem.addCDATA(identificador_fichero);
                    secuencia++;

                    // Validez_Documento
                    if (anexo.getValidezDocumento() != null) {
                        elem = rootElement.addElement("Validez_Documento");
                        elem.addCDATA(RegwebConstantes.CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()));
                    }

                    // Tipo_Documento
                    if (anexo.getTipoDocumento() != null) {
                        elem = rootElement.addElement("Tipo_Documento");
                        elem.addCDATA(RegwebConstantes.CODIGO_NTI_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()));
                    }

                    // Certificado
                    //TODO
                    elem = rootElement.addElement("Certificado");
                    //elem.addCDATA(getBase64Sring(anexo.getCertificado()));
                    elem.addCDATA(null);


                    //Firma documento (propiedad Firma Documento del segmento)
                    elem = rootElement.addElement("Firma_Documento");
                    //TODO preguntar a toni como extraer la firma del attached.(De momento ponemos el documento completo)
                    elem.addCDATA(getBase64String(dc.getData()));


                    // TimeStamp
                    elem = rootElement.addElement("TimeStamp");
                    elem.addCDATA(null);

                    // Validacion_OCSP_Certificado
                    elem = rootElement.addElement("Validacion_OCSP_Certificado");
                    elem.addCDATA(null);


                    // Hash
                    if (data != null) {
                        elem = rootElement.addElement("Hash");
                        elem.addCDATA(getBase64String(obtenerHash(data)));
                    }


                    // Tipo_MIME
                    elem = rootElement.addElement("Tipo_MIME");
                    elem.addCDATA(dc.getMime());



                    //Anexo (propiedad Anexo del segmento)
                    if (data != null) {
                        elem = rootElement.addElement("Anexo");
                        elem.addCDATA(getBase64String(data));
                    }

                    //Identificador Fichero Firmado
                    elem = rootElement.addElement("Identificador_Documento_Firmado");
                    elem.addCDATA(identificador_fichero);


                    // Observaciones
                    if (StringUtils.isNotBlank(anexo.getObservaciones())) {
                        elem = rootElement.addElement("Observaciones");
                        elem.addCDATA(anexo.getObservaciones());
                    }


                }
                if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {//CAS 4: FIRMA AMB DOC ORIG. DETACHED
                    //Obtenemos el documento que se encuentra en DocumentoCustody
                    dc = anexoFull.getDocumentoCustody();
                    if (dc != null) {
                        filename = dc.getName();
                        data = dc.getData();
                    }

                    //Especificamos el resto de propiedades del segmento anexo
                    // Nombre_Fichero_Anexado
                    if (StringUtils.isNotBlank(filename)) {
                        elem = rootElement.addElement("Nombre_Fichero_Anexado");
                        elem.addCDATA(filename);
                    }

                    // Identificador_Fichero
                    elem = rootElement.addElement("Identificador_Fichero");
                    identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, filename);
                    elem.addCDATA(identificador_fichero);
                    secuencia++;

                    // Validez_Documento
                    if (anexo.getValidezDocumento() != null) {
                        elem = rootElement.addElement("Validez_Documento");
                        elem.addCDATA(RegwebConstantes.CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()));
                    }

                    // Tipo_Documento
                    if (anexo.getTipoDocumento() != null) {
                        elem = rootElement.addElement("Tipo_Documento");
                        elem.addCDATA(RegwebConstantes.CODIGO_NTI_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()));
                    }

                    // Certificado
                    //TODO
                    elem = rootElement.addElement("Certificado");
                    //elem.addCDATA(getBase64Sring(anexo.getCertificado()));
                    elem.addCDATA(null);


                    //Firma documento (propiedad Firma Documento del segmento)
                    //En este caso la firma de documento va en otro segmento anexo separado
                    elem = rootElement.addElement("Firma_Documento");
                    elem.addCDATA(null);

                    // TimeStamp
                    elem = rootElement.addElement("TimeStamp");
                    elem.addCDATA(null);

                    // Validacion_OCSP_Certificado
                    elem = rootElement.addElement("Validacion_OCSP_Certificado");
                    elem.addCDATA(null);


                    // Hash
                    if (data != null) {
                        elem = rootElement.addElement("Hash");
                        elem.addCDATA(getBase64String(obtenerHash(data)));
                    }


                    // Tipo_MIME
                    if (dc != null) {
                        elem = rootElement.addElement("Tipo_MIME");
                        elem.addCDATA(dc.getMime());
                    }


                    //Anexo (propiedad Anexo del segmento)
                    if (data != null) {
                        elem = rootElement.addElement("Anexo");
                        elem.addCDATA(getBase64String(data));
                    }

                    //Identificador Fichero Firmado
                    elem = rootElement.addElement("Identificador_Documento_Firmado");
                    elem.addCDATA(null);

                    // Observaciones
                    if (StringUtils.isNotBlank(anexo.getObservaciones())) {
                        elem = rootElement.addElement("Observaciones");
                        elem.addCDATA(anexo.getObservaciones());
                    }


                }

                if(anexo.getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {   // Si tiene la firma en otro fichero

                    Element rootElementFirma = rootNode.addElement("De_Anexo");
                    Element elemFirma = null;

                    sc = anexoFull.getSignatureCustody();
                    String filename_firma = new String();
                    byte[] data_firma = null;
                    if (sc != null) {
                        filename_firma = sc.getName();
                        data_firma = sc.getData();

                        elemFirma = rootElementFirma.addElement("Nombre_Fichero_Anexado");
                        elemFirma.addCDATA(filename_firma);

                        elemFirma = rootElementFirma.addElement("Identificador_Fichero");
                        elemFirma.addCDATA(generateIdentificadorFichero(identificadorIntercambio, secuencia, filename_firma));
                        secuencia++;


                        // Validez_Documento
                        //TODO prueba lo ponemos a vacio. Pendiente probar sin ponerlo
                        elemFirma = rootElementFirma.addElement("Validez_Documento");
                        elemFirma.addCDATA(null);

                        //TODO preguntar a felip, al ser la firma del anexo no se que poner.
                        elemFirma = rootElementFirma.addElement("Tipo_Documento");
                        elemFirma.addCDATA(RegwebConstantes.CODIGO_NTI_BY_TIPO_DOCUMENTO.get(RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO));//=03


                        // Certificado
                        //TODO
                        elemFirma = rootElementFirma.addElement("Certificado");
                        //elem.addCDATA(getBase64Sring(anexo.getCertificado()));
                        elemFirma.addCDATA(null);


                        //Firma documento (propiedad Firma Documento del segmento)
                        //En este caso la firma de documento va en otro segmento anexo separado
                        elemFirma = rootElementFirma.addElement("Firma_Documento");
                        elemFirma.addCDATA(null);

                        // TimeStamp
                        elemFirma = rootElementFirma.addElement("TimeStamp");
                        elemFirma.addCDATA(null);

                        // Validacion_OCSP_Certificado
                        elemFirma = rootElementFirma.addElement("Validacion_OCSP_Certificado");
                        elemFirma.addCDATA(null);


                        // Hash
                        if (data != null) {
                            elemFirma = rootElementFirma.addElement("Hash");
                            elemFirma.addCDATA(getBase64String(obtenerHash(data_firma)));
                        }


                        // Tipo_MIME
                        if (sc != null) {
                            elemFirma = rootElementFirma.addElement("Tipo_MIME");
                            elemFirma.addCDATA(sc.getMime());
                        }


                        //Anexo (segmento)
                        if (data_firma != null) {
                            elemFirma = rootElementFirma.addElement("Anexo");
                            elemFirma.addCDATA(getBase64String(data_firma));
                        }


                        //Identificador del Documento Firmado
                        //En este caso apunta al documento del que es firma.
                        elemFirma = rootElementFirma.addElement("Identificador_Documento_Firmado");
                        elemFirma.addCDATA(identificador_fichero);


                    }


                    //secuencia++;
                }
            }
        }catch (Exception e){
            log.info("Error addDatosAnexo");
            e.printStackTrace();

        }

    }

    private void addDatosAnexos(Element rootNode, RegistroEntrada re, String identificadorIntercambio) {
        int secuencia = 0;

        try {

            //ANTIGUO List<Anexo> anexos = anexoEjb.getByRegistroEntrada(re.getId());
            List<Anexo> anexos = re.getRegistroDetalle().getAnexos(); // Deben pasarnos el re con los anexos cargados
            List<AnexoFull> anexosFull = re.getRegistroDetalle().getAnexosFull(); // Deben pasarnos el re con los anexos cargados


            for (AnexoFull anexoFull : anexosFull) {
                Anexo anexo = anexoFull.getAnexo();
                //Inicio del segmento "De_Anexo" para el mensaje de intercambio.
                //De_Anexo
                Element rootElement = rootNode.addElement("De_Anexo");
                Element elem = null;

                //El custodiaID no puede ser null
                final String custodyID = anexo.getCustodiaID();
                Assert.notNull(anexo.getCustodiaID(), "'custodiaID' must not be null");

                montarDeAnexo(rootNode, rootElement, identificadorIntercambio, anexoFull, secuencia);
            }
        } catch (Exception e) {
            log.info("Error addDatosAnexo");
            e.printStackTrace();

        }
    }


    private void montarDeAnexo(Element rootNode, Element rootElement, String identificadorIntercambio, AnexoFull anexoFull, int secuencia) {

        try {
            byte[] data = null;
            DocumentCustody dc = null; //Documento asociado
            SignatureCustody sc = null;// Firma del documento Asociado
            String filename = new String();
            String identificador_fichero = new String();
            Anexo anexo = anexoFull.getAnexo();
            Element elem = null;

            Element rootElementFirma = rootNode.addElement("De_Anexo");
            Element elemFirma = null;
            String filename_firma = new String();
            byte[] data_firma = null;

            //en esta API el documento con la firma se encuentra en DocumentoCustody
            dc = anexoFull.getDocumentoCustody();
            if (dc != null) {
                filename = dc.getName();
                data = dc.getData();
            }

            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) { //Recuperamos la firma
                sc = anexoFull.getSignatureCustody();

                if (sc != null) {
                    filename_firma = sc.getName();
                    data_firma = sc.getData();
                }

                rootElementFirma = rootNode.addElement("De_Anexo");
            }


            //Especificamos el resto de propiedades del segmento anexo
            // Nombre_Fichero_Anexado
            if (StringUtils.isNotBlank(filename)) {
                elem = rootElement.addElement("Nombre_Fichero_Anexado");
                elem.addCDATA(filename);

            }
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                elemFirma = rootElementFirma.addElement("Nombre_Fichero_Anexado");
                elemFirma.addCDATA(filename_firma);
            }


            // Identificador_Fichero
            elem = rootElement.addElement("Identificador_Fichero");
            identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, filename);
            elem.addCDATA(identificador_fichero);
            secuencia++;
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                // Identificador_Fichero
                elemFirma = rootElementFirma.addElement("Identificador_Fichero");
                elemFirma.addCDATA(generateIdentificadorFichero(identificadorIntercambio, secuencia, filename_firma));
                secuencia++;
            }

            // Validez_Documento
            if (anexo.getValidezDocumento() != null) {
                elem = rootElement.addElement("Validez_Documento");
                elem.addCDATA(RegwebConstantes.CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()));
            }
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                elemFirma = rootElementFirma.addElement("Validez_Documento");
                elemFirma.addCDATA(null);
            }


            // Tipo_Documento
            if (anexo.getTipoDocumento() != null) {
                elem = rootElement.addElement("Tipo_Documento");
                elem.addCDATA(RegwebConstantes.CODIGO_NTI_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()));
            }
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                //TODO preguntar a felip, al ser la firma del anexo no se que poner.
                elemFirma = rootElementFirma.addElement("Tipo_Documento");
                elemFirma.addCDATA(RegwebConstantes.CODIGO_NTI_BY_TIPO_DOCUMENTO.get(RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO));//=03
            }

            // Certificado
            //TODO
            elem = rootElement.addElement("Certificado");
            //elem.addCDATA(getBase64Sring(anexo.getCertificado()));
            elem.addCDATA(null);
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                elemFirma = rootElementFirma.addElement("Certificado");
                elemFirma.addCDATA(null);
            }


            //Firma documento (propiedad Firma Documento del segmento)
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                elem = rootElement.addElement("Firma_Documento");
                //TODO preguntar a toni como extraer la firma del attached.(De momento ponemos el documento completo)
                elem.addCDATA(getBase64String(dc.getData()));
            } else {//DETACHED
                elem = rootElement.addElement("Firma_Documento");
                elem.addCDATA(null);

                //En este caso la firma de documento va en otro segmento anexo separado
                elemFirma = rootElementFirma.addElement("Firma_Documento");
                elemFirma.addCDATA(null);
            }


            // TimeStamp
            elem = rootElement.addElement("TimeStamp");
            elem.addCDATA(null);
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                elemFirma = rootElementFirma.addElement("TimeStamp");
                elemFirma.addCDATA(null);
            }

            // Validacion_OCSP_Certificado
            elem = rootElement.addElement("Validacion_OCSP_Certificado");
            elem.addCDATA(null);
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                elemFirma = rootElementFirma.addElement("Validacion_OCSP_Certificado");
                elemFirma.addCDATA(null);
            }


            // Hash
            if (data != null) {
                elem = rootElement.addElement("Hash");
                elem.addCDATA(getBase64String(obtenerHash(data)));
            }
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                if (data != null) {
                    elemFirma = rootElementFirma.addElement("Hash");
                    elemFirma.addCDATA(getBase64String(obtenerHash(data_firma)));
                }
            }


            // Tipo_MIME
            elem = rootElement.addElement("Tipo_MIME");
            elem.addCDATA(dc.getMime());
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                if (sc != null) {
                    elemFirma = rootElementFirma.addElement("Tipo_MIME");
                    elemFirma.addCDATA(sc.getMime());
                }
            }


            //Anexo (propiedad Anexo del segmento)
            if (data != null) {
                elem = rootElement.addElement("Anexo");
                elem.addCDATA(getBase64String(data));
            }
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                if (data_firma != null) {
                    elemFirma = rootElementFirma.addElement("Anexo");
                    elemFirma.addCDATA(getBase64String(data_firma));
                }
            }


            //Identificador Fichero Firmado
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                elem = rootElement.addElement("Identificador_Documento_Firmado");
                elem.addCDATA(identificador_fichero);
            } else {
                elem = rootElement.addElement("Identificador_Documento_Firmado");
                elem.addCDATA(null);

                //En este caso apunta al documento del que es firma.
                elemFirma = rootElementFirma.addElement("Identificador_Documento_Firmado");
                elemFirma.addCDATA(identificador_fichero);
            }


            // Observaciones
            if (StringUtils.isNotBlank(anexo.getObservaciones())) {
                elem = rootElement.addElement("Observaciones");
                elem.addCDATA(anexo.getObservaciones());
            }
            if (anexo.getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                elemFirma = rootElementFirma.addElement("Observaciones");
                elemFirma.addCDATA(null);
            }
        } catch (Exception e) {
            log.info("Error montarDeAnexo");
            e.printStackTrace();
        }

    }




    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param rootNode
     * @param re
     */
    private void addDatosInternosControl(Element rootNode, RegistroEntrada re, String identificadorIntercambio) {

        // De_Internos_Control
        Element rootElement = rootNode.addElement("De_Internos_Control");
        Element elem = null;

        RegistroDetalle registroDetalle = re.getRegistroDetalle();

        // Tipo_Transporte_Entrada
        if (registroDetalle.getTransporte() != null) {
            elem = rootElement.addElement("Tipo_Transporte_Entrada");
            elem.addCDATA(RegwebConstantes.CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        }

        // Numero_Transporte_Entrada
        if (StringUtils.isNotBlank(registroDetalle.getNumeroTransporte())) {
            elem = rootElement.addElement("Numero_Transporte_Entrada");
            elem.addCDATA(registroDetalle.getNumeroTransporte());
        }

        // Nombre_Usuario
        if (StringUtils.isNotBlank(re.getUsuario().getNombreCompleto())) {
            elem = rootElement.addElement("Nombre_Usuario");
            elem.addCDATA(re.getUsuario().getNombreCompleto());
        }

        // Contacto_Usuario
        if (StringUtils.isNotBlank(re.getUsuario().getUsuario().getEmail())) {
            elem = rootElement.addElement("Contacto_Usuario");
            elem.addCDATA(re.getUsuario().getUsuario().getEmail());
        }

        // Identificador_Intercambio
        if (StringUtils.isNotBlank(identificadorIntercambio)) {
            elem = rootElement.addElement("Identificador_Intercambio");
            elem.addCDATA(identificadorIntercambio);
        }


        // Aplicacion_Version_Emisora
        elem = rootElement.addElement("Aplicacion_Version_Emisora");
        elem.addCDATA(Versio.VERSIO_SIR);


        // Tipo_Anotacion
        elem = rootElement.addElement("Tipo_Anotacion");
        elem.addCDATA("02"); // todo: Pendiente de asignar correctamente

        elem = rootElement.addElement("Descripcion_Tipo_Anotacion");
        elem.addCDATA("Envío"); // todo: Pendiente de asignar correctamente

        // Tipo_Registro
        elem = rootElement.addElement("Tipo_Registro");
        elem.addCDATA("0"); // todo: Pendiente definir el tipo de registro

        // Documentacion_Fisica
        if (registroDetalle.getTipoDocumentacionFisica() != null) {
            elem = rootElement.addElement("Documentacion_Fisica");
            elem.addCDATA(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        }

        // Observaciones_Apunte
        if (StringUtils.isNotBlank(registroDetalle.getObservaciones())) {
            elem = rootElement.addElement("Observaciones_Apunte");
            elem.addCDATA(registroDetalle.getObservaciones());
        }

        // Indicador_Prueba
        elem = rootElement.addElement("Indicador_Prueba");
        elem.addCDATA("0"); //todo: añadir propiedad que indique si es pruebas o producción

        // Codigo_Entidad_Registral_Inicio
        String Codigo_Entidad_Registral_Inicio = obtenerCodigoOficinaOrigen(re);
        if (StringUtils.isNotBlank(Codigo_Entidad_Registral_Inicio)) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Inicio");
            elem.addCDATA(Codigo_Entidad_Registral_Inicio);
        }


        // Decodificacion_Entidad_Registral_Inicio
        String Decodificacion_Entidad_Registral_Inicio = obtenerDenominacionOficinaOrigen(re);
        if (StringUtils.isNotBlank(Decodificacion_Entidad_Registral_Inicio)) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Inicio");
            elem.addCDATA(Decodificacion_Entidad_Registral_Inicio);
        }


    }

    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param rootNode
     * @param re
     */
    private void addDatosformularioGenerico(Element rootNode, RegistroEntrada re) {

        // De_Formulario_Generico
        Element rootElement = rootNode.addElement("De_Formulario_Generico");
        Element elem = null;

        RegistroDetalle registroDetalle = re.getRegistroDetalle();

        // Expone
        elem = rootElement.addElement("Expone");
        if (StringUtils.isNotBlank(registroDetalle.getExpone())) {
            elem.addCDATA(registroDetalle.getExpone());
        }

        // Solicita
        elem = rootElement.addElement("Solicita");
        if (StringUtils.isNotBlank(registroDetalle.getSolicita())) {
            elem.addCDATA(registroDetalle.getSolicita());
        }

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

            De_Mensaje msg = new De_Mensaje();
            msg.setCodigo_Entidad_Registral_Origen(mensaje.getCodigoEntidadRegistralOrigen());
            msg.setCodigo_Entidad_Registral_Destino(mensaje.getCodigoEntidadRegistralDestino());
            msg.setIdentificador_Intercambio(mensaje.getIdentificadorIntercambio());
            msg.setDescripcion_Mensaje(mensaje.getDescripcionMensaje());

            if (mensaje.getNumeroRegistroEntradaDestino() != null) {
                msg.setNumero_Registro_Entrada_Destino(mensaje
                        .getNumeroRegistroEntradaDestino());
            }

            msg.setCodigo_Error(mensaje.getCodigoError());

           /*
           // Identificadores de ficheros todo:Revisar
            if (mensaje.getIdentificadoresFicheros() != null) {
                msg.setIdentificador_Fichero(mensaje
                        .getIdentificadoresFicheros().toArray(new String[0]));
            }*/

            // Fecha y hora de entrada en destino
            if (mensaje.getFechaEntradaDestino() != null) {
                msg.setFecha_Hora_Entrada_Destino(SDF.format(mensaje.getFechaEntradaDestino()));
            }

            // Tipo de mensaje
            if (mensaje.getTipoMensaje() != null) {
                msg.setTipo_Mensaje(mensaje.getTipoMensaje().getValue());
            }

            // Indicador de prueba
            if (mensaje.getIndicadorPrueba() != null) {
                msg.setIndicador_Prueba(Indicador_PruebaType.fromValue(mensaje.getIndicadorPrueba().getValue()));
            }

            msg.marshal(stringWriter);

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

            Fichero_Intercambio_SICRES_3 ficheroIntercambioSICRES3 = Fichero_Intercambio_SICRES_3
                    .unmarshal(new StringReader(xml));
            if (ficheroIntercambioSICRES3 != null) {
                ficheroIntercambio = new FicheroIntercambio();
                ficheroIntercambio.setFicheroIntercambio(ficheroIntercambioSICRES3);

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

            De_Mensaje de_mensaje = De_Mensaje.unmarshal(new StringReader(xml));

            if (de_mensaje != null) {

                mensaje = new Mensaje();
                mensaje.setCodigoEntidadRegistralOrigen(de_mensaje
                        .getCodigo_Entidad_Registral_Origen());
                mensaje.setCodigoEntidadRegistralDestino(de_mensaje
                        .getCodigo_Entidad_Registral_Destino());
                mensaje.setIdentificadorIntercambio(de_mensaje
                        .getIdentificador_Intercambio());
                mensaje.setDescripcionMensaje(de_mensaje
                        .getDescripcion_Mensaje());
                mensaje.setNumeroRegistroEntradaDestino(de_mensaje
                        .getNumero_Registro_Entrada_Destino());
                mensaje.setCodigoError(de_mensaje.getCodigo_Error());

                /*
                // Identificadores de ficheros todo:Revisar
                if (deMensaje.getIdentificadorFichero() != null) {
                    mensaje.setIdentificadoresFicheros(Arrays.asList(deMensaje.getIdentificadorFichero()));
                }*/

                // Fecha y hora de entrada en destino
                String fechaEntradaDestino = de_mensaje
                        .getFecha_Hora_Entrada_Destino();
                if (StringUtils.isNotBlank(fechaEntradaDestino)) {
                    mensaje.setFechaEntradaDestino(SDF
                            .parse(fechaEntradaDestino));
                }

                // Tipo de mensaje
                String tipoMensaje = de_mensaje.getTipo_Mensaje();
                if (StringUtils.isNotBlank(tipoMensaje)) {
                    mensaje.setTipoMensaje(TipoMensaje
                            .getTipoMensaje(tipoMensaje));
                }

                // Indicador de prueba
                IndicadorPrueba indicadorPrueba = IndicadorPrueba.valueOf(de_mensaje.getIndicador_Prueba().value());
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
                "'CodigoEntidadRegistralOrigen' no puede estar vacio");

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
                "'NumeroRegistroEntrada' no puede estar vacio");

        // Validar que la fecha y hora de entrada esté informada
        Assert.hasText(ficheroIntercambio.getFechaRegistroXML(),
                "'FechaHoraEntrada' no puede estar vacio");

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
                "'CodigoEntidadRegistralDestino' no puede estar vacio");

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

        // Comprobar los datos de los interesados
        if ((ficheroIntercambio.getFicheroIntercambio() != null)
                && (ficheroIntercambio.getFicheroIntercambio().getDe_InteresadoCount() > 0)) {

            for (De_Interesado interesado : ficheroIntercambio.getFicheroIntercambio().getDe_Interesado()) {

                // Validar el canal preferente de comunicación del interesado
                if (StringUtils.isNotBlank(interesado
                        .getCanal_Preferente_Comunicacion_Interesado())) {
                    Assert.notNull(
                            CanalNotificacion.getCanalNotificacion(interesado
                                    .getCanal_Preferente_Comunicacion_Interesado()),
                            "'CanalPreferenteComunicacionInteresado' is invalid ["
                                    + interesado
                                    .getCanal_Preferente_Comunicacion_Interesado()
                                    + "]");
                }

                // Validar el canal preferente de comunicación del representante
                if (StringUtils.isNotBlank(interesado
                        .getCanal_Preferente_Comunicacion_Representante())) {
                    Assert.notNull(
                            CanalNotificacion.getCanalNotificacion(interesado
                                    .getCanal_Preferente_Comunicacion_Representante()),
                            "'CanalPreferenteComunicacionRepresentante' is invalid ["
                                    + interesado
                                    .getCanal_Preferente_Comunicacion_Representante()
                                    + "]");
                }

                if (StringUtils.isBlank(ficheroIntercambio
                        .getCodigoUnidadTramitacionOrigen())) {

                    Assert.isTrue(
                            StringUtils.isNotBlank(interesado
                                    .getRazon_Social_Interesado())
                                    || (StringUtils.isNotBlank(interesado
                                    .getNombre_Interesado()) && StringUtils.isNotBlank(interesado
                                    .getPrimer_Apellido_Interesado())),
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

                    if (StringUtils.isNotEmpty(interesado.getTipo_Documento_Identificacion_Interesado())) {
                        Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipo_Documento_Identificacion_Interesado()), "'invalid tipoDocumentoIdentificacionInteresado'");
                    }

                    if (StringUtils.isNotEmpty(interesado.getTipo_Documento_Identificacion_Representante())) {
                        Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipo_Documento_Identificacion_Representante()), "invalid 'tipoDocumentoIdentificacionRepresentante'");
                    }

                    if (StringUtils.isNotBlank(interesado
                            .getCanal_Preferente_Comunicacion_Interesado())) {
                        if (CanalNotificacion.DIRECCION_POSTAL.getValue()
                                .equals(interesado
                                        .getCanal_Preferente_Comunicacion_Interesado())) {

                            Assert.hasText(interesado.getPais_Interesado(),
                                    "'paisInteresado' no puede estar vacio");
                            Assert.hasText(
                                    interesado.getDireccion_Interesado(),
                                    "'direccionInteresado' no puede estar vacio");

                            if (CODIGOPAISESPANA.equals(interesado
                                    .getPais_Interesado())) {
                                Assert.isTrue(
                                        StringUtils.isNotBlank(interesado
                                                .getCodigo_Postal_Interesado())
                                                || (StringUtils
                                                .isNotBlank(interesado
                                                        .getProvincia_Interesado()) && StringUtils
                                                .isNotBlank(interesado
                                                        .getMunicipio_Interesado())),
                                        "'codigoPostalInteresado' or ('provinciaInteresado' and 'municipioInteresado') no puede estar vacio");
                            }

                        } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                                .getValue()
                                .equals(interesado
                                        .getCanal_Preferente_Comunicacion_Interesado())) {
                            Assert.hasText(
                                    interesado
                                            .getDireccion_Electronica_Habilitada_Interesado(),
                                    "'direccionElectronicaHabilitadaInteresado' no puede estar vacio");
                        }
                    }

                    if (StringUtils.isNotBlank(interesado
                            .getCanal_Preferente_Comunicacion_Representante())) {
                        if (CanalNotificacion.DIRECCION_POSTAL
                                .getValue()
                                .equals(interesado
                                        .getCanal_Preferente_Comunicacion_Representante())) {

                            Assert.hasText(interesado.getPais_Representante(),
                                    "'paisRepresentante' no puede estar vacio");
                            Assert.hasText(
                                    interesado.getDireccion_Representante(),
                                    "'direccionRepresentante' no puede estar vacio");

                            if (CODIGOPAISESPANA.equals(interesado
                                    .getPais_Representante())) {
                                Assert.isTrue(
                                        StringUtils
                                                .isNotBlank(interesado
                                                        .getCodigo_Postal_Representante())
                                                || (StringUtils
                                                .isNotBlank(interesado
                                                        .getProvincia_Representante()) && StringUtils
                                                .isNotBlank(interesado
                                                        .getMunicipio_Representante())),
                                        "'codigoPostalRepresentante' or ('provinciaRepresentante' and 'municipioRepresentante') no puede estar vacio");
                            }

                        } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                                .getValue()
                                .equals(interesado
                                        .getCanal_Preferente_Comunicacion_Representante())) {
                            Assert.hasText(
                                    interesado
                                            .getDireccion_Electronica_Habilitada_Representante(),
                                    "'direccionElectronicaHabilitadaRepresentante' no puede estar vacio");
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
                "'Resumen' no puede estar vacio");
    }

    /**
     * Validar el segmento de anexos
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoAnexos(FicheroIntercambio ficheroIntercambio) {


        // Validar los documentos
        if ((ficheroIntercambio.getFicheroIntercambio() != null)
                && ArrayUtils.isNotEmpty(ficheroIntercambio.getFicheroIntercambio().getDe_Anexo())) {
            for (De_Anexo anexo : ficheroIntercambio.getFicheroIntercambio()
                    .getDe_Anexo()) {
                validarAnexo(anexo, ficheroIntercambio.getIdentificadorIntercambio());
            }
        }
    }

    /**
     * Valida un anexo del segmento de anexos
     *
     * @param anexo                    Información del anexo
     * @param identificadorIntercambio Identificador de intercambio
     */
    protected void validarAnexo(De_Anexo anexo, String identificadorIntercambio) {

        if (anexo != null) {

            // Validar el nombre del fichero anexado
            Assert.hasText(anexo.getNombre_Fichero_Anexado(),
                    "'NombreFicheroAnexado' no puede estar vacio");
            Assert.isTrue(
                    !StringUtils.containsAny(anexo.getNombre_Fichero_Anexado(),
                            "\\/?*:|<>\";&"),
                    "'NombreFicheroAnexado' has invalid characters ["
                            + anexo.getNombre_Fichero_Anexado() + "]");

            // Validar el identificador de fichero
            validarIdentificadorFichero(anexo, identificadorIntercambio);

            // Validar el campo validez de documento
            if (StringUtils.isNotBlank(anexo.getValidez_Documento())) {
                Assert.notNull(
                        ValidezDocumento
                                .getValidezDocumento(anexo
                                        .getValidez_Documento()),
                        "'ValidezDocumento' is invalid ["
                                + anexo.getValidez_Documento() + "]");
            }

            // Validar el campo tipo de documento
            Assert.hasText(anexo.getTipo_Documento(),
                    "'TipoDocumento' no puede estar vacio");
            Assert.notNull(
                    TipoDocumento.getTipoDocumento(anexo
                            .getTipo_Documento()),
                    "'TipoDocumento' is invalid ["
                            + anexo.getTipo_Documento() + "]");

            // Validar el hash del documento
            // Nota: no se comprueba el código hash de los documentos porque no
            // se especifica con qué algoritmo está generado.
            Assert.isTrue(!ArrayUtils.isEmpty(anexo.getHash()),
                    "'Hash' no puede estar vacio");

            // Validar el tipo MIME
            // TODO: estaba comentado.
            /*if (StringUtils.isNotBlank(anexo.getTipo_MIME())) {
                Assert.isTrue(StringUtils.equalsIgnoreCase(
						anexo.getTipo_MIME(), MimeTypeUtils.getMimeType(anexo
								.getIdentificador_Fichero())),
						"'TipoMIME' does not match 'IdentificadorFichero'");
			}*/


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
    protected void validarIdentificadorFichero(De_Anexo anexo, String identificadorIntercambio) {

        // No vacío
        Assert.hasText(anexo.getIdentificador_Fichero(),
                "'IdentificadorFichero' no puede estar vacio");

        // Validar el tamaño
        Assert.isTrue(StringUtils.length(anexo.getIdentificador_Fichero()) <= LONGITUD_MAX_IDENTIFICADOR_FICHERO,
                "'IdentificadorFichero' is invalid");

        // Validar formato: <Identificador del Intercambio><Código de tipo de archivo><Número Secuencial>.<Extensión del fichero>
        String identificadorFichero = anexo.getIdentificador_Fichero();
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
                "'TipoAnotacion' no puede estar vacio");
        Assert.notNull(
                ficheroIntercambio.getTipoAnotacion(),
                "'TipoAnotacion' is invalid ["
                        + ficheroIntercambio.getTipoAnotacionXML() + "]");

        // Validar que el código de entidad registral de inicio esté informado
        Assert.hasText(ficheroIntercambio.getCodigoEntidadRegistralInicio(),
                "'CodigoEntidadRegistralInicio' no puede estar vacio");

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
                "'IdentificadorIntercambio' no puede estar vacio");

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
     * Validar el segmento de formulario genérico
     *
     * @param ficheroIntercambio Información del fichero de intercambio.
     */
    protected void validarSegmentoFormularioGenerico(
            FicheroIntercambio ficheroIntercambio) {

        Assert.notNull(
                ficheroIntercambio.getExpone(),
                "'expone' must not be null");

        Assert.notNull(
                ficheroIntercambio.getSolicita(),
                "'solicita' must not be null");
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
     * Comprueba con DIR3CAIB que el codigoEntidadRegistral pertenece a una Entidad Válida.
     * @param codigoEntidadRegistral
     * @return
     */
    protected boolean validarCodigoEntidadRegistral(String codigoEntidadRegistral) {

        boolean valido = true;

        if (StringUtils.length(codigoEntidadRegistral) > LONGITUD_CODIGO_ENTIDAD_REGISTRAL) {
            return false;
        }

        try {
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
            OficinaTF oficinaTF = oficinasService.obtenerOficina(codigoEntidadRegistral,null,null);

            if(oficinaTF == null) return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return valido;
    }

    protected boolean validarCodigoUnidadTramitacion(String codigoUnidadTramitacion) {

        boolean valido = true;

        if (StringUtils.length(codigoUnidadTramitacion) > LONGITUD_CODIGO_UNIDAD_TRAMITACION) {
            return false;
        }

        try {
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
            UnidadTF unidadTF = unidadesService.obtenerUnidad(codigoUnidadTramitacion,null,null);

            if(unidadTF == null) return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


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