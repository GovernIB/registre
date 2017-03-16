package es.caib.regweb3.sir.utils;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.api.schema.De_Anexo;
import es.caib.regweb3.sir.api.schema.De_Interesado;
import es.caib.regweb3.sir.api.schema.De_Mensaje;
import es.caib.regweb3.sir.api.schema.Fichero_Intercambio_SICRES_3;
import es.caib.regweb3.sir.api.schema.types.Indicador_PruebaType;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.*;
import es.caib.regweb3.utils.MimeTypeUtils;
import es.caib.regweb3.utils.Versio;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.exolab.castor.xml.MarshalException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static es.caib.regweb3.utils.RegwebConstantes.*;


public class Sicres3XML {

    public final Logger log = Logger.getLogger(getClass());

    private static final String VALIDACIONDIRECTORIOCOMUNPARAMNAME = "validar.codigos.directorio.comun";
    private static final String MAXTAMANOANEXOSPARAMNAME = "max.tamaño.anexos";
    private static final String MAXNUMANEXOSPARAMNAME = "max.num.anexos";

    private static final String CODIGO_PAIS_ESPANA = "724";
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
    public Sicres3XML() {
        super();
        setupBase64Field();
    }


    public Map<String, String> getBase64Fields() {
        return base64Fields;
    }

    public void setBase64Fields(Map<String, String> base64Fields) {
        this.base64Fields = base64Fields;
    }

    protected void setupBase64Field() {
        LinkedHashMap base64Fields = new LinkedHashMap<String, String>();
        base64Fields.put("Hash", "//Hash/text()");
        base64Fields.put("Timestamp_Entrada", "//Timestamp_Entrada/text()");
        base64Fields.put("Certificado", "//Certificado/text()");
        base64Fields.put("Firma_Documento", "//Firma_Documento/text()");
        base64Fields.put("TimeStamp", "//TimeStamp/text()");
        base64Fields.put("Validacion_OCSP_Certificado", "//Validacion_OCSP_Certificado/text()");
        base64Fields.put("Anexo", "//Anexo/text()");
        setBase64Fields(base64Fields);
    }


    public void validarFicheroIntercambio(FicheroIntercambio fichero) {

        log.info("Validando FicheroIntercambio...");

        Assert.notNull(fichero, "La variable 'ficheroIntercambio' no puede ser null");

		/*
         * Validar los segmentos del fichero de intercambio
		 */

        validarSegmentoOrigen(fichero);
        validarSegmentoDestino(fichero);
        validarSegmentoControl(fichero);
        validarSegmentoInteresados(fichero);
        validarSegmentoAsunto(fichero);
        validarSegmentoAnexos(fichero);
        validarSegmentoFormularioGenerico(fichero);

        log.info("Fichero de intercambio validado");
    }


    public void validarAsientoRegistral(AsientoRegistralSir asiento) {

        log.info("Llamada a validarAsientoRegistral");


        Assert.notNull(asiento, "La variable 'asiento' no puede ser null");

        // Comprobar los datos de origen
        Assert.hasText(asiento.getCodigoEntidadRegistralOrigen(), "El campo 'codigoEntidadRegistralOrigen' no puede estar vacio");
        Assert.hasText(asiento.getNumeroRegistro(), "El campo 'numeroRegistroEntrada' no puede estar vacio");
        Assert.notNull(asiento.getFechaRegistro(), "El campo 'fechaEntrada' no puede ser null");

        // Comprobar los datos de destino
        Assert.hasText(asiento.getCodigoEntidadRegistralDestino(), "El campo 'codigoEntidadRegistralDestino' no puede estar vacio");

        // Comprobar los datos de los interesados
        if (!CollectionUtils.isEmpty(asiento.getInteresados()) && StringUtils.isBlank(asiento
                .getCodigoUnidadTramitacionOrigen())) {
            for (InteresadoSir interesado : asiento.getInteresados()) {

                Assert.isTrue(
                        StringUtils.isNotBlank(interesado.getRazonSocialInteresado())
                                || (StringUtils.isNotBlank(interesado
                                .getNombreInteresado()) && StringUtils
                                .isNotBlank(interesado.getPrimerApellidoInteresado())),
                        "Los campos 'razonSocialInteresado' o ('nombreInteresado' y 'primerApellidoInteresado') no pueden estar vacios");


                if (interesado.getCanalPreferenteComunicacionInteresado() != null) {
                    if (interesado.getCanalPreferenteComunicacionInteresado()
                            .equals(CanalNotificacion.DIRECCION_POSTAL.getValue())) {
                        Assert.hasText(interesado.getCodigoPaisInteresado(),
                                "'codigoPaisInteresado' no puede estar vacio");
                        Assert.hasText(interesado.getDireccionInteresado(),
                                "'direccionInteresado' no puede estar vacio");

                        if (CODIGO_PAIS_ESPANA.equals(interesado.getCodigoPaisInteresado())) {
                            Assert.isTrue(StringUtils.isNotBlank(interesado.getCodigoPostalInteresado())
                                            || (StringUtils
                                            .isNotBlank(interesado.getCodigoProvinciaInteresado()) && StringUtils
                                            .isNotBlank(interesado.getCodigoMunicipioInteresado())),
                                    "Los campos 'codigoPostalInteresado' o ('codigoProvinciaInteresado' y 'codigoMunicipioInteresado') no pueden estar vacios");
                        }

                    } else if (interesado
                            .getCanalPreferenteComunicacionInteresado()
                            .equals(CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA.getValue())) {
                        Assert.hasText(interesado.getDireccionElectronicaHabilitadaInteresado(),
                                "El campo 'direccionElectronicaHabilitadaInteresado' no puede estar vacio");
                    }
                }

                if (interesado.getCanalPreferenteComunicacionRepresentante() != null) {
                    if (interesado.getCanalPreferenteComunicacionRepresentante().equals(CanalNotificacion.DIRECCION_POSTAL.getValue())) {

                        Assert.hasText(interesado.getCodigoPaisRepresentante(), "El campo 'codigoPaisRepresentante' no puede estar vacio");
                        Assert.hasText(interesado.getDireccionRepresentante(), "El campo 'direccionRepresentante' no puede estar vacio");

                        if (CODIGO_PAIS_ESPANA.equals(interesado.getCodigoPaisRepresentante())) {
                            Assert.isTrue(
                                    StringUtils.isNotBlank(interesado.getCodigoPostalRepresentante())
                                            || (StringUtils.isNotBlank(interesado
                                            .getCodigoProvinciaRepresentante()) && StringUtils.isNotBlank(interesado
                                            .getCodigoMunicipioRepresentante())),
                                    "Los campos 'codigoPostalRepresentante' o ('codigoProvinciaRepresentante' y 'codigoMunicipioRepresentante') no pueden estar vacios");
                        }

                    } else if (interesado.getCanalPreferenteComunicacionRepresentante()
                            .equals(CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA.getValue())) {
                        Assert.hasText(
                                interesado
                                        .getDireccionElectronicaHabilitadaRepresentante(),
                                "El campo 'direccionElectronicaHabilitadaRepresentante' no puede estar vacio");
                    }
                }
            }
        }

        // Comprobar los datos de asunto
        Assert.hasText(asiento.getResumen(), "El campo 'resumen' no puede estar vacio");

        // Comprobar los datos de los anexos
        if (!CollectionUtils.isEmpty(asiento.getAnexos())) {

            int numAdjuntos = 0; // Número de adjuntos: documentos de tipo
            // "02 - Documento Adjunto" que no son
            // firmas

            for (AnexoSir anexo : asiento.getAnexos()) {

                Assert.hasText(anexo.getNombreFichero(), "El campo 'nombreFichero' no puede estar vacio");
                Assert.notNull(anexo.getTipoDocumento(), "El campo 'tipoDocumento' no puede ser null");
                Assert.notNull(anexo.getHash(), "El campo 'hash' no puede ser null");

                // Si en documento es de tipo "02 - Documento Adjunto"
                if (TipoDocumento.DOCUMENTO_ADJUNTO.getValue().equals(anexo.getTipoDocumento())) {
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
        Assert.hasText(asiento.getIdentificadorIntercambio(), "El campo 'identificadorIntercambio' no puede estar vacio");
        Assert.notNull(asiento.getTipoRegistro(), "El campo 'tipoRegistro' no puede ser null");
        Assert.notNull(asiento.getDocumentacionFisica(), "El campo 'documentacionFisica' no puede ser null");
        Assert.notNull(asiento.getIndicadorPrueba(), "El campo 'indicadorPrueba' no puede ser null");
        Assert.hasText(asiento.getCodigoEntidadRegistralInicio(), "El campo 'codigoEntidadRegistralInicio' no puede estar vacio");

        log.info("Asiento registral validado");
    }

    /**
     * @param mensaje
     */
    public void validarMensaje(Mensaje mensaje) {

        Assert.notNull(mensaje, "El campo 'mensaje' no puede ser null");

        Assert.hasText(mensaje.getCodigoEntidadRegistralOrigen(), "El campo 'codigoEntidadRegistralOrigen' no puede estar vacio");
        Assert.hasText(mensaje.getCodigoEntidadRegistralDestino(), "El campo 'codigoEntidadRegistralDestino' no puede estar vacio");
        Assert.hasText(mensaje.getIdentificadorIntercambio(), "El campo 'identificadorIntercambio' no puede estar vacio");
        Assert.notNull(mensaje.getTipoMensaje(), "El campo 'tipoMensaje' no puede ser null");

        log.info("Mensaje (" + mensaje.getTipoMensaje().getName()+") validado");
    }

    /**
     * @param asientoRegistralSir
     * @return
     */
    public String crearXMLFicheroIntercambioSICRES3(AsientoRegistralSir asientoRegistralSir) throws Exception   {

        Assert.notNull(asientoRegistralSir, "La variable 'asientoRegistralSir' no puede ser null");

        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");

        // Fichero_Intercambio_SICRES_3
        Element rootNode = doc.addElement("Fichero_Intercambio_SICRES_3");

        /* Segmento DeOrigenORemitente */
        addDatosOrigenORemitente(rootNode, asientoRegistralSir);

        /* Segmento DeDestino */
        addDatosDestino(rootNode, asientoRegistralSir);

        /* Segmento DeInteresados */
        addDatosInteresados(rootNode, asientoRegistralSir.getInteresados());

        /* Segmento DeAsunto */
        addDatosAsunto(rootNode, asientoRegistralSir);

        /* Segmento DeAnexo */
        addDatosAnexos(rootNode, asientoRegistralSir);

        /* Segmento DeInternosControl */
        addDatosInternosControl(rootNode, asientoRegistralSir);

        /* Segmento DeFormularioGenerico */
        addDatosformularioGenerico(rootNode, asientoRegistralSir);

        return doc.asXML();
    }


    /**
     * Añade el Segmento deOrigenORemitente al Fichero de Intercambio
     *
     * @param rootNode
     * @param asiento
     */
    private void addDatosOrigenORemitente(Element rootNode, AsientoRegistralSir asiento) {

        // De_Origen_o_Remitente
        Element rootElement = rootNode.addElement("De_Origen_o_Remitente");
        Element elem = null;

        // Codigo_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(asiento.getCodigoEntidadRegistralOrigen())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Origen");
            elem.addCDATA(asiento.getCodigoEntidadRegistralOrigen());
        }

        // Decodificacion_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(asiento.getDecodificacionEntidadRegistralOrigen())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Origen");
            elem.addCDATA(asiento.getDecodificacionEntidadRegistralOrigen());
        }

        // Numero_Registro_Entrada
        if (StringUtils.isNotBlank(asiento.getNumeroRegistro())) {
            elem = rootElement.addElement("Numero_Registro_Entrada");
            elem.addCDATA(asiento.getNumeroRegistro());
        }


        // Fecha_Hora_Entrada
        if (asiento.getFechaRegistro() != null) {
            elem = rootElement.addElement("Fecha_Hora_Entrada");
            elem.addCDATA(SDF.format(asiento.getFechaRegistro()));
        }


        // Timestamp_Entrada
        //deOrigenORemitente.setTimestampEntrada(); // No es necesario

        // Codigo_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(asiento.getCodigoUnidadTramitacionOrigen())) {
            elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Origen");
            elem.addCDATA(asiento.getCodigoUnidadTramitacionOrigen());
        }

        // Decodificacion_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(asiento.getDecodificacionUnidadTramitacionOrigen())) {
            elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Origen");
            elem.addCDATA(asiento.getDecodificacionUnidadTramitacionOrigen());
        }

    }

    /**
     * Añade el Segmento deDestino al Fichero de Intercambio
     *
     * @param rootNode
     * @param asiento
     */
    private void addDatosDestino(Element rootNode, AsientoRegistralSir asiento) {

        // De_Destino
        Element rootElement = rootNode.addElement("De_Destino");
        Element elem = null;

        // Codigo_Entidad_Registral_Destino
        if (StringUtils.isNotBlank(asiento.getCodigoEntidadRegistralDestino())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Destino");
            elem.addCDATA(asiento.getCodigoEntidadRegistralDestino());
        }

        // Decodificacion_Entidad_Registral_Destino
        if (StringUtils.isNotBlank(asiento.getDecodificacionEntidadRegistralDestino())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Destino");
            elem.addCDATA(asiento.getDecodificacionEntidadRegistralDestino());
        }

        // Codigo_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(asiento.getCodigoUnidadTramitacionDestino())) {
            elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Destino");
            elem.addCDATA(asiento.getCodigoUnidadTramitacionDestino());
        }

        // Decodificacion_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(asiento.getDecodificacionUnidadTramitacionDestino())) {
            elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Destino");
            elem.addCDATA(asiento.getDecodificacionUnidadTramitacionDestino());
        }

    }

    protected static String getBase64String(byte[] dato) {
        String result = null;

        result = Base64.encodeBase64String(dato);

        return result;
    }

    /**
     * Añade el Segmento deInteresado al Fichero de Intercambio
     *
     * @param rootNode
     * @param interesadosSir
     */
    private void addDatosInteresados(Element rootNode, List<InteresadoSir> interesadosSir) {

        if (!CollectionUtils.isEmpty(interesadosSir)) {

            for (InteresadoSir interesadoSir : interesadosSir) {

                Element rootElement = rootNode.addElement("De_Interesado");
                Element elem = null;

                // Tipo_Documento_Identificacion_Interesado
                if (interesadoSir.getTipoDocumentoIdentificacionInteresado() != null) {
                    elem = rootElement.addElement("Tipo_Documento_Identificacion_Interesado");
                    elem.addCDATA(interesadoSir.getTipoDocumentoIdentificacionInteresado());
                }
                // Documento_Identificacion_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getDocumentoIdentificacionInteresado())) {
                    elem = rootElement.addElement("Documento_Identificacion_Interesado");
                    elem.addCDATA(interesadoSir.getDocumentoIdentificacionInteresado());
                }
                // Razon_Social_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getRazonSocialInteresado())) {
                    elem = rootElement.addElement("Razon_Social_Interesado");
                    elem.addCDATA(interesadoSir.getRazonSocialInteresado());
                }
                // Nombre_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getNombreInteresado())) {
                    elem = rootElement.addElement("Nombre_Interesado");
                    elem.addCDATA(interesadoSir.getNombreInteresado());
                }
                // Primer_Apellido_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getPrimerApellidoInteresado())) {
                    elem = rootElement.addElement("Primer_Apellido_Interesado");
                    elem.addCDATA(interesadoSir.getPrimerApellidoInteresado());
                }
                // Segundo_Apellido_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getSegundoApellidoInteresado())) {
                    elem = rootElement.addElement("Segundo_Apellido_Interesado");
                    elem.addCDATA(interesadoSir.getSegundoApellidoInteresado());
                }

                // Tipo_Documento_Identificacion_Representante
                if (interesadoSir.getTipoDocumentoIdentificacionRepresentante() != null) {
                    elem = rootElement.addElement("Tipo_Documento_Identificacion_Representante");
                    elem.addCDATA(interesadoSir.getTipoDocumentoIdentificacionRepresentante());
                }
                // Documento_Identificacion_Representante
                if (interesadoSir.getDocumentoIdentificacionRepresentante() != null) {
                    elem = rootElement.addElement("Documento_Identificacion_Representante");
                    elem.addCDATA(interesadoSir.getDocumentoIdentificacionRepresentante());
                }
                // Razon_Social_Representante
                if (interesadoSir.getRazonSocialRepresentante() != null) {
                    elem = rootElement.addElement("Razon_Social_Representante");
                    elem.addCDATA(interesadoSir.getRazonSocialRepresentante());
                }
                // Nombre_Representante
                if (interesadoSir.getNombreRepresentante() != null) {
                    elem = rootElement.addElement("Nombre_Representante");
                    elem.addCDATA(interesadoSir.getNombreRepresentante());
                }
                // Primer_Apellido_Representante
                if (interesadoSir.getPrimerApellidoRepresentante() != null) {
                    elem = rootElement.addElement("Primer_Apellido_Representante");
                    elem.addCDATA(interesadoSir.getPrimerApellidoRepresentante());
                }
                // Segundo_Apellido_Representante
                if (interesadoSir.getSegundoApellidoRepresentante() != null) {
                    elem = rootElement.addElement("Segundo_Apellido_Representante");
                    elem.addCDATA(interesadoSir.getSegundoApellidoRepresentante());
                }

                // Pais_Interesado
                if (interesadoSir.getCodigoPaisInteresado() != null) {
                    elem = rootElement.addElement("Pais_Interesado");
                    elem.addCDATA(interesadoSir.getCodigoPaisInteresado());
                }
                // Provincia_Interesado
                if (interesadoSir.getCodigoProvinciaInteresado() != null) {
                    elem = rootElement.addElement("Provincia_Interesado");
                    elem.addCDATA(interesadoSir.getCodigoProvinciaInteresado());
                }
                // Municipio_Interesado
                if (interesadoSir.getCodigoMunicipioInteresado() != null) {
                    elem = rootElement.addElement("Municipio_Interesado");
                    elem.addCDATA(interesadoSir.getCodigoMunicipioInteresado());
                }
                // Direccion_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getDireccionInteresado())) {
                    elem = rootElement.addElement("Direccion_Interesado");
                    elem.addCDATA(interesadoSir.getDireccionInteresado());
                }
                // Codigo_Postal_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getCodigoPostalInteresado())) {
                    elem = rootElement.addElement("Codigo_Postal_Interesado");
                    elem.addCDATA(interesadoSir.getCodigoPostalInteresado());
                }
                // Correo_Electronico_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getCorreoElectronicoInteresado())) {
                    elem = rootElement.addElement("Correo_Electronico_Interesado");
                    elem.addCDATA(interesadoSir.getCorreoElectronicoInteresado());
                }
                // Telefono_Contacto_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getTelefonoInteresado())) {
                    elem = rootElement.addElement("Telefono_Contacto_Interesado");
                    elem.addCDATA(interesadoSir.getTelefonoInteresado());
                }
                // Direccion_Electronica_Habilitada_Interesado
                if (!StringUtils.isEmpty(interesadoSir.getDireccionElectronicaHabilitadaInteresado())) {
                    elem = rootElement.addElement("Direccion_Electronica_Habilitada_Interesado");
                    elem.addCDATA(interesadoSir.getDireccionElectronicaHabilitadaInteresado());
                }
                // Canal_Preferente_Comunicacion_Interesado
                if (interesadoSir.getCanalPreferenteComunicacionInteresado() != null) {
                    elem = rootElement.addElement("Canal_Preferente_Comunicacion_Interesado");
                    elem.addCDATA(interesadoSir.getCanalPreferenteComunicacionInteresado());
                }

                // Pais_Representante
                if (interesadoSir.getCodigoPaisRepresentante() != null) {
                    elem = rootElement.addElement("Pais_Representante");
                    elem.addCDATA(interesadoSir.getCodigoPaisRepresentante());
                }
                // Provincia_Representante
                if (interesadoSir.getCodigoProvinciaRepresentante() != null) {
                    elem = rootElement.addElement("Provincia_Representante");
                    elem.addCDATA(interesadoSir.getCodigoProvinciaRepresentante());
                }
                // Municipio_Representante
                if (interesadoSir.getCodigoMunicipioRepresentante() != null) {
                    elem = rootElement.addElement("Municipio_Representante");
                    elem.addCDATA(interesadoSir.getCodigoMunicipioRepresentante());
                }
                // Direccion_Representante
                if (!StringUtils.isEmpty(interesadoSir.getDireccionRepresentante())) {
                    elem = rootElement.addElement("Direccion_Representante");
                    elem.addCDATA(interesadoSir.getDireccionRepresentante());
                }
                // Codigo_Postal_Representante
                if (!StringUtils.isEmpty(interesadoSir.getCodigoPostalRepresentante())) {
                    elem = rootElement.addElement("Codigo_Postal_Representante");
                    elem.addCDATA(interesadoSir.getCodigoPostalRepresentante());
                }
                // Correo_Electronico_Representante
                if (!StringUtils.isEmpty(interesadoSir.getCorreoElectronicoRepresentante())) {
                    elem = rootElement.addElement("Correo_Electronico_Representante");
                    elem.addCDATA(interesadoSir.getCorreoElectronicoRepresentante());
                }
                // Telefono_Contacto_Representante
                if (!StringUtils.isEmpty(interesadoSir.getTelefonoRepresentante())) {
                    elem = rootElement.addElement("Telefono_Contacto_Representante");
                    elem.addCDATA(interesadoSir.getTelefonoRepresentante());
                }
                // Direccion_Electronica_Habilitada_Representante
                if (!StringUtils.isEmpty(interesadoSir.getDireccionElectronicaHabilitadaRepresentante())) {
                    elem = rootElement.addElement("Direccion_Electronica_Habilitada_Representante");
                    elem.addCDATA(interesadoSir.getDireccionElectronicaHabilitadaRepresentante());
                }
                // Canal_Preferente_Comunicacion_Representante
                if (interesadoSir.getCanalPreferenteComunicacionRepresentante() != null) {
                    elem = rootElement.addElement("Canal_Preferente_Comunicacion_Representante");
                    elem.addCDATA(interesadoSir.getCanalPreferenteComunicacionRepresentante());
                }

                // Observaciones
                if (!StringUtils.isEmpty(interesadoSir.getObservaciones())) {
                    elem = rootElement.addElement("Observaciones");
                    elem.addCDATA(interesadoSir.getObservaciones());
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
     * @param asiento
     */
    private void addDatosAsunto(Element rootNode, AsientoRegistralSir asiento) {

        // De_Asunto
        Element rootElement = rootNode.addElement("De_Asunto");
        Element elem = null;

        // Resumen
        elem = rootElement.addElement("Resumen");
        if (StringUtils.isNotBlank(asiento.getResumen())) {
            elem.addCDATA(asiento.getResumen());
        }
        // Codigo_Asunto_Segun_Destino
        if (asiento.getCodigoAsunto() != null) {
            elem = rootElement.addElement("Codigo_Asunto_Segun_Destino");
            elem.addCDATA(asiento.getCodigoAsunto());
        }

        // Referencia_Externa
        if (StringUtils.isNotBlank(asiento.getReferenciaExterna())) {
            elem = rootElement.addElement("Referencia_Externa");
            elem.addCDATA(asiento.getReferenciaExterna());
        }

        // Numero_Expediente
        if (StringUtils.isNotBlank(asiento.getNumeroExpediente())) {
            elem = rootElement.addElement("Numero_Expediente");
            elem.addCDATA(asiento.getNumeroExpediente());
        }

    }

    private void addDatosAnexos(Element rootNode, AsientoRegistralSir asiento) throws Exception  {

        for (AnexoSir anexoSir : asiento.getAnexos()) {

            Element elem;
            Element rootElement = rootNode.addElement("De_Anexo");

            // Nombre_Fichero_Anexado
            if (StringUtils.isNotBlank(anexoSir.getNombreFichero())) {
                elem = rootElement.addElement("Nombre_Fichero_Anexado");
                elem.addCDATA(anexoSir.getNombreFichero());
            }

            // Identificador_Fichero
            elem = rootElement.addElement("Identificador_Fichero");
            elem.addCDATA(anexoSir.getIdentificadorFichero());


            // Validez_Documento
            elem = rootElement.addElement("Validez_Documento");
            if (anexoSir.getValidezDocumento() == null) {
                elem.addCDATA(null);
            } else {
                elem.addCDATA(anexoSir.getValidezDocumento());
            }

            // Tipo_Documento
            elem = rootElement.addElement("Tipo_Documento");
            if (anexoSir.getTipoDocumento() == null) {
                elem.addCDATA(null);
            } else {
                elem.addCDATA(anexoSir.getTipoDocumento());
            }

            // Certificado
            if (anexoSir.getCertificado() != null) {
                elem = rootElement.addElement("Certificado");
                elem.addCDATA(getBase64String(anexoSir.getCertificado()));
            }

            //Firma documento (propiedad Firma Documento del segmento)
            if (anexoSir.getFirma() != null) {
                elem = rootElement.addElement("Firma_Documento");
                elem.addCDATA(getBase64String(anexoSir.getFirma()));
            }

            // TimeStamp
            if (anexoSir.getTimestamp() != null) {
                elem = rootElement.addElement("TimeStamp");
                elem.addCDATA(getBase64String(anexoSir.getTimestamp()));
            }

            // Validacion_OCSP_Certificado
            if (anexoSir.getValidacionOCSPCertificado() != null) {
                elem = rootElement.addElement("Validacion_OCSP_Certificado");
                elem.addCDATA(getBase64String(anexoSir.getValidacionOCSPCertificado()));
            }

            // Hash
            if (anexoSir.getAnexoData() != null) {
                elem = rootElement.addElement("Hash");
                elem.addCDATA(getBase64String(anexoSir.getAnexoData()));
            }

            // Tipo_MIME
            if (anexoSir.getTipoMIME() != null || anexoSir.getTipoMIME().length() <= ANEXO_TIPOMIME_MAXLENGTH_SIR) {
                elem = rootElement.addElement("Tipo_MIME");
                elem.addCDATA(anexoSir.getTipoMIME());
            }

            //Anexo (propiedad Anexo del segmento)
            if (anexoSir.getAnexoData() != null) {
                elem = rootElement.addElement("Anexo");
                elem.addCDATA(getBase64String(anexoSir.getAnexoData()));
            }

            //Identificador Fichero Firmado
            elem = rootElement.addElement("Identificador_Documento_Firmado");
            elem.addCDATA(anexoSir.getIdentificadorDocumentoFirmado());

            // Observaciones
            if (StringUtils.isNotBlank(anexoSir.getObservaciones())) {
                elem = rootElement.addElement("Observaciones");
                elem.addCDATA(anexoSir.getObservaciones());
            }
        }

    }





    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param rootNode
     * @param asiento
     */
    private void addDatosInternosControl(Element rootNode, AsientoRegistralSir asiento) {

        // De_Internos_Control
        Element rootElement = rootNode.addElement("De_Internos_Control");
        Element elem = null;

        // Tipo_Transporte_Entrada
        if (asiento.getTipoTransporte() != null) {
            elem = rootElement.addElement("Tipo_Transporte_Entrada");
            elem.addCDATA(asiento.getTipoTransporte());
        }

        // Numero_Transporte_Entrada
        if (StringUtils.isNotBlank(asiento.getNumeroTransporte())) {
            elem = rootElement.addElement("Numero_Transporte_Entrada");
            elem.addCDATA(asiento.getNumeroTransporte());
        }

        // Nombre_Usuario
        if (StringUtils.isNotBlank(asiento.getNombreUsuario())) {
            elem = rootElement.addElement("Nombre_Usuario");
            elem.addCDATA(asiento.getNombreUsuario());
        }

        // Contacto_Usuario
        if (StringUtils.isNotBlank(asiento.getContactoUsuario())) {
            elem = rootElement.addElement("Contacto_Usuario");
            elem.addCDATA(asiento.getContactoUsuario());
        }

        // Identificador_Intercambio
        if (StringUtils.isNotBlank(asiento.getIdentificadorIntercambio())) {
            elem = rootElement.addElement("Identificador_Intercambio");
            elem.addCDATA(asiento.getIdentificadorIntercambio());
        }

        // Aplicacion_Version_Emisora
        elem = rootElement.addElement("Aplicacion_Version_Emisora");
        elem.addCDATA(Versio.VERSIO_SIR);

        // Tipo_Anotacion
        elem = rootElement.addElement("Tipo_Anotacion");
        elem.addCDATA(asiento.getTipoAnotacion());

        elem = rootElement.addElement("Descripcion_Tipo_Anotacion");
        elem.addCDATA(asiento.getDecodificacionTipoAnotacion());

        // Tipo_Registro
        elem = rootElement.addElement("Tipo_Registro");
        elem.addCDATA(asiento.getTipoRegistro().getValue());

        // Documentacion_Fisica
        if (StringUtils.isNotBlank(asiento.getDocumentacionFisica())) {
            elem = rootElement.addElement("Documentacion_Fisica");
            elem.addCDATA(asiento.getDocumentacionFisica());
        }

        // Observaciones_Apunte
        if (StringUtils.isNotBlank(asiento.getObservacionesApunte())) {
            elem = rootElement.addElement("Observaciones_Apunte");
            elem.addCDATA(asiento.getObservacionesApunte());
        }

        // Indicador_Prueba
        elem = rootElement.addElement("Indicador_Prueba");
        elem.addCDATA(asiento.getIndicadorPrueba().getValue());

        // Codigo_Entidad_Registral_Inicio
        if (StringUtils.isNotBlank(asiento.getCodigoEntidadRegistralInicio())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Inicio");
            elem.addCDATA(asiento.getCodigoEntidadRegistralInicio());
        }

        // Decodificacion_Entidad_Registral_Inicio
        if (StringUtils.isNotBlank(asiento.getDecodificacionEntidadRegistralInicio())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Inicio");
            elem.addCDATA(asiento.getDecodificacionEntidadRegistralInicio());
        }


    }

    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param rootNode
     * @param asiento
     */
    private void addDatosformularioGenerico(Element rootNode, AsientoRegistralSir asiento) {

        // De_Formulario_Generico
        Element rootElement = rootNode.addElement("De_Formulario_Generico");
        Element elem = null;

        // Expone
        elem = rootElement.addElement("Expone");
        if (StringUtils.isNotBlank(asiento.getExpone())) {
            elem.addCDATA(asiento.getExpone());
        }

        // Solicita
        elem = rootElement.addElement("Solicita");
        if (StringUtils.isNotBlank(asiento.getSolicita())) {
            elem.addCDATA(asiento.getSolicita());
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

        Assert.notNull(mensaje, "La variable 'mensaje' no puede ser null");

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
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            Fichero_Intercambio_SICRES_3 ficheroIntercambioSICRES3 = Fichero_Intercambio_SICRES_3.unmarshal(isr);

            if (ficheroIntercambioSICRES3 != null) {
                ficheroIntercambio = new FicheroIntercambio();
                ficheroIntercambio.setFicheroIntercambio(ficheroIntercambioSICRES3);

                //Realizamos una validación de los campos del xml que deben estar en base64 en caso de estar presentes
                validateBase64Fields(xml);
            }

        } catch (Throwable e) {

            // Comprobamos si el error es en alguno de los campos de Código Entidad, si es así no podemos componer el mensaje de error
            if(e instanceof MarshalException){

                CharSequence cs1 = "_codigo_Entidad_Registral_Origen";
                CharSequence cs2 = "_codigo_Entidad_Registral_Destino";

                if (e.getLocalizedMessage().contains(cs1) || e.getLocalizedMessage().contains(cs2)){

                    log.info("Error al parsear el xml en campos _codigo_Entidad_Registral, no se enviará mensaje de error.", e);
                    throw new ValidacionException(Errores.ERROR_COD_ENTIDAD_INVALIDO);
                }
            }

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
                mensaje.setCodigoEntidadRegistralOrigen(de_mensaje.getCodigo_Entidad_Registral_Origen());
                mensaje.setCodigoEntidadRegistralDestino(de_mensaje.getCodigo_Entidad_Registral_Destino());
                mensaje.setIdentificadorIntercambio(de_mensaje.getIdentificador_Intercambio());
                mensaje.setDescripcionMensaje(de_mensaje.getDescripcion_Mensaje());
                mensaje.setNumeroRegistroEntradaDestino(de_mensaje.getNumero_Registro_Entrada_Destino());
                mensaje.setCodigoError(de_mensaje.getCodigo_Error());

                /*
                // Identificadores de ficheros todo:Revisar
                if (deMensaje.getIdentificadorFichero() != null) {
                    mensaje.setIdentificadoresFicheros(Arrays.asList(deMensaje.getIdentificadorFichero()));
                }*/

                // Fecha y hora de entrada en destino
                String fechaEntradaDestino = de_mensaje.getFecha_Hora_Entrada_Destino();
                if (StringUtils.isNotBlank(fechaEntradaDestino)) {
                    mensaje.setFechaEntradaDestino(SDF.parse(fechaEntradaDestino));
                }

                // Tipo de mensaje
                String tipoMensaje = de_mensaje.getTipo_Mensaje();
                if (StringUtils.isNotBlank(tipoMensaje)) {
                    mensaje.setTipoMensaje(TipoMensaje.getTipoMensaje(tipoMensaje));
                }

                // Indicador de prueba
                IndicadorPrueba indicadorPrueba = IndicadorPrueba.valueOf(de_mensaje.getIndicador_Prueba().value());
                if ((indicadorPrueba != null)
                        && StringUtils.isNotBlank(indicadorPrueba.getValue())) {
                    mensaje.setIndicadorPrueba(IndicadorPrueba.getIndicadorPrueba(indicadorPrueba.getValue()));
                }
            }

        } catch (Throwable e) {
            log.error("Error al parsear el XML del mensaje: [" + xml + "]", e);
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
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarSegmentoOrigen(FicheroIntercambio fichero) {

        // Validar que el código de entidad registral de origen esté informado
        Assert.hasText(fichero.getCodigoEntidadRegistralOrigen(),
                "El campo 'CodigoEntidadRegistralOrigen' no puede estar vacio");

        // Validar el código de entidad registral de origen en DIR3
        if (!validarCodigoEntidadRegistral(fichero.getCodigoEntidadRegistralOrigen())) {
            throw new ValidacionException(Errores.ERROR_COD_ENTIDAD_INVALIDO);
        }

        // Validar el código de unidad de tramitación de origen en DIR3
        if (StringUtils.isNotBlank(fichero.getCodigoUnidadTramitacionOrigen())) {
            Assert.isTrue(validarCodigoUnidadTramitacion(fichero.getCodigoUnidadTramitacionOrigen()),
                    "El campo 'CodigoUnidadTramitacionOrigen' no es válido");
        }

        // Validar que el número de registro de entrada en origen esté informado
        Assert.hasText(fichero.getNumeroRegistro(), "El campo 'NumeroRegistroEntrada' no puede estar vacio");

        // Validar que la fecha y hora de entrada esté informada
        Assert.hasText(fichero.getFechaRegistroXML(), "El campo 'FechaHoraEntrada' no puede estar vacio");

        // Validar el formato de la fecha de entrada
        Assert.isTrue(StringUtils.equals(fichero.getFechaRegistroXML(),
                SDF.format(fichero.getFechaRegistro())),
                "El campo 'FechaHoraEntrada' no es válido [" + fichero.getFechaRegistroXML() + "]");

        // Validar que la Fecha de entrada no sea superior a la actual
        Assert.isTrue(fichero.getFechaRegistro().before(new Date()), "El campo 'FechaHoraEntrada' es mayor que now()");

        log.info("SegmentoOrigen validado!");
    }

    /**
     * Validar el segmento de destino
     *
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarSegmentoDestino(FicheroIntercambio fichero) {

        // Validar que el código de entidad registral de destino esté informado
        Assert.hasText(fichero.getCodigoEntidadRegistralDestino(),
                "El campo 'CodigoEntidadRegistralDestino' no puede estar vacio");

        // Validar el código de entidad registral de destino en DIR3
        if (!validarCodigoEntidadRegistral(fichero.getCodigoEntidadRegistralDestino())) {
            throw new ValidacionException(Errores.ERROR_COD_ENTIDAD_INVALIDO);
        }

        // Validar el código de unidad de tramitación de destino en DIR3
        if (StringUtils.isNotBlank(fichero.getCodigoUnidadTramitacionDestino())) {
            Assert.isTrue(validarCodigoUnidadTramitacion(fichero.getCodigoUnidadTramitacionDestino()),
                    "El campo 'CodigoUnidadTramitacionDestino' no es válido o no existe en DIR3");
        }

        log.info("SegmentoDestino validado!");
    }

    /**
     * Validar el segmento de interesados
     *
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarSegmentoInteresados(FicheroIntercambio fichero) {

        // Comprobar los datos de los interesados
        if ((fichero.getFicheroIntercambio() != null)
                && (fichero.getFicheroIntercambio().getDe_InteresadoCount() > 0)) {

            for (De_Interesado interesado : fichero.getFicheroIntercambio().getDe_Interesado()) {

                // Si es un Registro de Entrada, ha de existir al menos un Interesado
                if(fichero.getTipoRegistro().equals(TipoRegistro.ENTRADA)){
                    Assert.isTrue(StringUtils.isNotBlank(interesado.getRazon_Social_Interesado())
                                    || (StringUtils.isNotBlank(interesado
                                    .getNombre_Interesado()) && StringUtils.isNotBlank(interesado.getPrimer_Apellido_Interesado())),
                            "No existe ningún Interesado, es necesario que haya al menos uno");

                }else if(fichero.getTipoRegistro().equals(TipoRegistro.SALIDA)){

                    // Si no hay ningún Interesado
                    if(StringUtils.isBlank(interesado.getRazon_Social_Interesado()) || (StringUtils.isBlank(interesado
                            .getNombre_Interesado()) && StringUtils.isBlank(interesado.getPrimer_Apellido_Interesado()))){

                        // Comprobar que el campo CodigoUnidadTramitacionOrigen está informado y es válido
                        Assert.hasText(fichero.getCodigoUnidadTramitacionOrigen(), "El campo 'CodigoUnidadTramitacionOrigen' no puede estar vacio");

                        // Validar el código de unidad de tramitación de origen en DIR3
                        Assert.isTrue(validarCodigoUnidadTramitacion(fichero.getCodigoUnidadTramitacionOrigen()),
                                "El campo 'CodigoUnidadTramitacionOrigen' no es válido ");

                    }
                }

                /* INTERESADO */

                // Tipo Documento Identificación Interesado
                if (StringUtils.isNotEmpty(interesado.getTipo_Documento_Identificacion_Interesado())) {
                    Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipo_Documento_Identificacion_Interesado()), "'El campo tipoDocumentoIdentificacionInteresado' no puede ser null");

                    // Validar que el Documento concuerda con su tipo documento identificación
                    // Eliminat segons requeriment de Certificació SIR
                    /* Validacion validacionDocumento = null;
                    try {
                        validacionDocumento = DocumentoUtils.comprobarDocumento(interesado.getDocumento_Identificacion_Interesado(), RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(interesado.getTipo_Documento_Identificacion_Interesado().charAt(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        validacionDocumento = new Validacion(Boolean.FALSE, "", "");
                    }

                    Assert.isTrue(validacionDocumento.getValido(), "El campo 'documento' no es correcto"); */

                    // Validar que el Tipo Documento concuerda con Nombre o Razon Social
                    if (interesado.getTipo_Documento_Identificacion_Interesado().equals(String.valueOf(TIPODOCUMENTOID_CIF)) ||
                            interesado.getTipo_Documento_Identificacion_Interesado().equals(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN))) {
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getRazon_Social_Interesado()),
                                "El campo  'razonSocialInteresado' no puede estar vacio");
                    } else {
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getNombre_Interesado()) &&
                                        StringUtils.isNotBlank(interesado.getPrimer_Apellido_Interesado()),
                                "Los campos 'nombreInteresado' y 'primerApellidoInteresado' no pueden estar vacios");
                    }
                }


                // Validar el canal preferente de comunicación del interesado
                if (StringUtils.isNotBlank(interesado.getCanal_Preferente_Comunicacion_Interesado())) {
                    Assert.notNull(
                            CanalNotificacion.getCanalNotificacion(interesado.getCanal_Preferente_Comunicacion_Interesado()),
                            "El campo 'CanalPreferenteComunicacionInteresado' no puede ser null ["
                                    + interesado.getCanal_Preferente_Comunicacion_Interesado() + "]");

                    if (CanalNotificacion.DIRECCION_POSTAL.getValue().equals(interesado.getCanal_Preferente_Comunicacion_Interesado())) {

                        Assert.hasText(interesado.getPais_Interesado(), "El campo  'paisInteresado' no puede estar vacio");
                        Assert.hasText(interesado.getDireccion_Interesado(), "El campo  'direccionInteresado' no puede estar vacio");

                        if (CODIGO_PAIS_ESPANA.equals(interesado.getPais_Interesado())) {
                            Assert.isTrue(StringUtils.isNotBlank(interesado
                                            .getCodigo_Postal_Interesado()) || (StringUtils
                                            .isNotBlank(interesado.getProvincia_Interesado()) && StringUtils
                                            .isNotBlank(interesado.getMunicipio_Interesado())),
                                    "Los campos 'codigoPostalInteresado' o  la combinación de ('provinciaInteresado' y 'municipioInteresado') no pueden estar vacios");
                        }

                    } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                            .getValue().equals(interesado.getCanal_Preferente_Comunicacion_Interesado())) {
                        Assert.hasText(
                                interesado.getDireccion_Electronica_Habilitada_Interesado(),
                                "El campo 'direccionElectronicaHabilitadaInteresado' no puede estar vacio");
                    }

                }

                /*REPRESENTANTE*/

                // Tipo Documento Identificación Interesado
                if (StringUtils.isNotEmpty(interesado.getTipo_Documento_Identificacion_Representante())) {
                    Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipo_Documento_Identificacion_Representante()), "El campo 'tipoDocumentoIdentificacionRepresentante' no puede ser null");

                    // Validar que el Documento concuerda con su tipo documento identificación
                    // Eliminat segons requeriment de Certificació SIR
                    /* Validacion validacionDocumento = null;
                    try {
                        validacionDocumento = DocumentoUtils.comprobarDocumento(interesado.getDocumento_Identificacion_Representante(), RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(interesado.getTipo_Documento_Identificacion_Representante().charAt(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        validacionDocumento = new Validacion(Boolean.FALSE, "", "");
                    }
                    Assert.isTrue(validacionDocumento.getValido(), "El campo  'documento' no es válido"); */

                    // Validar que el Tipo Documento concuerda con Nombre o Razon Social
                    if (interesado.getTipo_Documento_Identificacion_Representante().equals(String.valueOf(TIPODOCUMENTOID_CIF)) ||
                            interesado.getTipo_Documento_Identificacion_Representante().equals(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN))) {
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getRazon_Social_Representante()),
                                "El campo 'razonSocialRepresentante' no puede estar vacio");
                    } else {
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getNombre_Representante()) &&
                                        StringUtils.isNotBlank(interesado.getPrimer_Apellido_Representante()),
                                "El campo 'nombreRepresentante' and 'primerApellidoRepresentante' no puede etar vacio");
                    }
                }

                // Validar el canal preferente de comunicación del representante
                if (StringUtils.isNotBlank(interesado.getCanal_Preferente_Comunicacion_Representante())) {
                    Assert.notNull(
                            CanalNotificacion.getCanalNotificacion(interesado
                                    .getCanal_Preferente_Comunicacion_Representante()),
                            "El campo 'CanalPreferenteComunicacionRepresentante' no puede ser null ["
                                    + interesado.getCanal_Preferente_Comunicacion_Representante() + "]");

                    if (CanalNotificacion.DIRECCION_POSTAL.getValue().equals(interesado
                            .getCanal_Preferente_Comunicacion_Representante())) {

                        Assert.hasText(interesado.getPais_Representante(),
                                "El campo 'paisRepresentante' no puede estar vacio");
                        Assert.hasText(
                                interesado.getDireccion_Representante(),
                                "El campo 'direccionRepresentante' no puede estar vacio");

                        if (CODIGO_PAIS_ESPANA.equals(interesado
                                .getPais_Representante())) {
                            Assert.isTrue(
                                    StringUtils
                                            .isNotBlank(interesado.getCodigo_Postal_Representante())
                                            || (StringUtils
                                            .isNotBlank(interesado.getProvincia_Representante()) && StringUtils
                                            .isNotBlank(interesado.getMunicipio_Representante())),
                                    "Los campos 'codigoPostalRepresentante' o la combinación de ('provinciaRepresentante' y 'municipioRepresentante') no pueden estar vacios");
                        }

                    } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                            .getValue().equals(interesado
                                    .getCanal_Preferente_Comunicacion_Representante())) {
                        Assert.hasText(
                                interesado.getDireccion_Electronica_Habilitada_Representante(),
                                "El campo 'direccionElectronicaHabilitadaRepresentante' no puede estar vacio");
                    }
                }

                log.info("Interesado validado!");

            }
        }

        log.info("SegmentoInteresados validado!");
    }

    /**
     * Validar el segmento de asunto
     *
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarSegmentoAsunto(FicheroIntercambio fichero) {

        // Validar que el resumen esté informado
        Assert.hasText(fichero.getResumen(), "El campo  'Resumen' no puede estar vacio");

        log.info("SegmentoAsunto validado!");
    }

    /**
     * Validar el segmento de anexos
     *
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarSegmentoAnexos(FicheroIntercambio fichero) {

        // Validar los documentos
        if ((fichero.getFicheroIntercambio() != null)
                && ArrayUtils.isNotEmpty(fichero.getFicheroIntercambio().getDe_Anexo())) {
            De_Anexo[] anexos = fichero.getFicheroIntercambio().getDe_Anexo();
            for (De_Anexo anexo : anexos) {
                validarAnexo(anexo, fichero.getIdentificadorIntercambio());

                //Si el anexo tiene identificador de documento firmado significa que es firma de otro anexo, se debe comprobar que es así.
                if (!StringUtils.isEmpty(anexo.getIdentificador_Documento_Firmado())) {
                    Boolean firmaDeOtroAnexo = false;
                    for (De_Anexo anexo2 : anexos) {
                        if (anexo2.getIdentificador_Fichero().equals(anexo.getIdentificador_Documento_Firmado())) {
                            firmaDeOtroAnexo = true;
                        }
                    }
                    Assert.isTrue(firmaDeOtroAnexo, "El anexo no es firma de ningun otro anexo");
                }

            }
        }
        log.info("SegmentoAnexos validado!");
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
                    "El campo 'NombreFicheroAnexado' no puede estar vacio");
            Assert.isTrue(!StringUtils.containsAny(anexo.getNombre_Fichero_Anexado(), "\\/?*:|<>\";&"),
                    "El campo 'NombreFicheroAnexado' tiene caracteres no válidos [" + anexo.getNombre_Fichero_Anexado() + "]");

            // Validar el identificador de fichero
            validarIdentificadorFichero(anexo, identificadorIntercambio);

            // Validar el campo validez de documento
            if (StringUtils.isNotBlank(anexo.getValidez_Documento())) {
                Assert.notNull(
                        ValidezDocumento.getValidezDocumento(anexo.getValidez_Documento()),
                        "El campo 'ValidezDocumento' no es válido [" + anexo.getValidez_Documento() + "]");
            }

            // Validar el campo tipo de documento
            Assert.hasText(anexo.getTipo_Documento(), "El campo  'TipoDocumento' no puede estar vacio");
            Assert.notNull(TipoDocumento.getTipoDocumento(anexo.getTipo_Documento()),
                    "El campo  'TipoDocumento' no puede ser null [" + anexo.getTipo_Documento() + "]");

            // Validar el hash del documento
            // Nota: no se comprueba el código hash de los documentos porque no
            // se especifica con qué algoritmo está generado.
            Assert.isTrue(!ArrayUtils.isEmpty(anexo.getHash()), "El campo 'Hash' no puede estar vacio");

            // Validar el tipo MIME
            // TODO: estaba comentado.
            /*if (StringUtils.isNotBlank(anexo.getTipo_MIME())) {
                Assert.isTrue(StringUtils.equalsIgnoreCase(
						anexo.getTipo_MIME(), MimeTypeUtils.getMimeType(anexo
								.getIdentificador_Fichero())),
						"'TipoMIME' does not match 'IdentificadorFichero'");
			}*/

            // Validar el contenido del anexo
            Assert.isTrue(anexo.getAnexo().length > 0, "El campo  'Anexo' no puede estar vacio");

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
            log.info("Anexo '"+anexo.getNombre_Fichero_Anexado()+"' validado!");
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
        Assert.hasText(anexo.getIdentificador_Fichero(), "El campo 'IdentificadorFichero' no puede estar vacio");

        // Validar el tamaño
        Assert.isTrue(StringUtils.length(anexo.getIdentificador_Fichero()) <= LONGITUD_MAX_IDENTIFICADOR_FICHERO,
                "El campo 'IdentificadorFichero' no es válido");

        // Validar formato: <Identificador del Intercambio><Código de tipo de archivo><Número Secuencial>.<Extensión del fichero>
        String identificadorFichero = anexo.getIdentificador_Fichero();
        Assert.isTrue(StringUtils.startsWith(identificadorFichero, identificadorIntercambio),
                "El campo 'IdentificadorFichero' no concuerda con 'IdentificadorIntercambio'");

        identificadorFichero = StringUtils.substringAfter(identificadorFichero, identificadorIntercambio + "_");
        String[] tokens = StringUtils.split(identificadorFichero, "_.");
        Assert.isTrue(ArrayUtils.getLength(tokens) == 3, "El campo 'IdentificadorFichero' no es válido");

        Assert.isTrue(StringUtils.equals(tokens[0], "01"),
                "El campo 'IdentificadorFichero' no es válido, hay un error en el tipo de archivo"); // Código de tipo de archivo de 2 dígitos
        Assert.isTrue(StringUtils.length(tokens[1]) <= 4,
                "El campo 'IdentificadorFichero' no es válido, hay un error en número secuencial de 4 digitos"); // Número secuencial de hasta 4 dígitos
        Assert.isTrue(StringUtils.isNumeric(tokens[1]),
                "El campo 'IdentificadorFichero' no es válido, hay un error en el número secuencial numérico"); // Número secuencial compuesto por solo dígitos
        Assert.hasText(tokens[2], "El campo 'IdentificadorFichero' no es válido, hay un error en la Extensión del fichero"); // Extensión del fichero

        // Validar el tipo MIME
        if (StringUtils.isNotBlank(anexo.getTipo_MIME())) {

            //Si el anexo es de tipo FICHERO TECNICO INTERNO, no se debe comprobar el MIME.
            if (!anexo.getTipo_Documento().equals(TipoDocumento.FICHERO_TECNICO_INTERNO.getValue())) {
                Assert.isTrue(StringUtils.equalsIgnoreCase(
                        anexo.getTipo_MIME(), MimeTypeUtils.getMimeTypeExtension(tokens[2])),
                        "El campo 'TipoMIME' no coincide con el indicado en 'IdentificadorFichero'");

                String mimePermitidos = PropiedadGlobalUtil.getMIMEPermitidos();
                Assert.isTrue(mimePermitidos.contains(anexo.getTipo_MIME()),"El tipo MIME recibido no está permitido");
            }
        }
    }

    /**
     * Validar el segmento de internos y control
     *
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarSegmentoControl(FicheroIntercambio fichero) {

        // Validar el tipo de transporte
        if (StringUtils.isNotBlank(fichero.getTipoTransporteXML())) {
            Assert.notNull(fichero.getTipoTransporte(),
                    "El campo 'TipoTransporteEntrada' no puede ser null [" + fichero.getTipoTransporteXML() + "]");
        }

        // Validar el tipo de anotación
        Assert.hasText(fichero.getTipoAnotacionXML(), "El campo 'TipoAnotacion' no puede estar vacio");
        Assert.notNull(fichero.getTipoAnotacion(), "El campo 'TipoAnotacion' no puede ser null [" + fichero.getTipoAnotacionXML() + "]");

        // Validar que el código de entidad registral de inicio esté informado
        Assert.hasText(fichero.getCodigoEntidadRegistralInicio(), "El campo 'CodigoEntidadRegistralInicio' no puede estar vacio");

        // Validar el código de entidad registral de inicio en DIR3
        Assert.isTrue(validarCodigoEntidadRegistral(fichero.getCodigoEntidadRegistralInicio()), "El campo 'CodigoEntidadRegistralInicio' no es válido");

        // Validar el identificador de intercambio, tiene que realizarse despues de la validacion del código de entidad registral de inicio
        validarIdentificadorIntercambio(fichero);

        log.info("SegmentoControl validado!");
    }

    /**
     * Validar el identificador de intercambio.
     *
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarIdentificadorIntercambio(FicheroIntercambio fichero) {

        // Comprobar que no esté vacío
        Assert.hasText(fichero.getIdentificadorIntercambio(),
                "El campo 'IdentificadorIntercambio' no puede estar vacio");

        Assert.isTrue(fichero.getIdentificadorIntercambio().length() <= LONGITUD_IDENTIFICADOR_INTERCAMBIO,
                "El campo 'IdentificadorIntercambio' no es válido");

        // Comprobar el formato del identificiador de intercambio: <Código_Entidad_Registral_Origen><AA><Número Secuencial>
        String[] tokens = StringUtils.split(fichero.getIdentificadorIntercambio(), "_");

        Assert.isTrue(ArrayUtils.getLength(tokens) == 3, "El campo 'IdentificadorIntercambio' no es válido");
        Assert.isTrue(StringUtils.length(tokens[0]) <= LONGITUD_CODIGO_ENTIDAD_REGISTRAL,
                "El campo 'IdentificadorIntercambio' no es válido, hay un error en la longitud esperada"); // Código de la entidad registral
        Assert.isTrue(StringUtils.equals(tokens[0], fichero.getCodigoEntidadRegistralInicio()),
                "El campo 'IdentificadorIntercambio' no concuerda con 'CodigoEntidadRegistralInicio'");
        Assert.isTrue(StringUtils.length(tokens[1]) == 2, "El campo 'IdentificadorIntercambio' no es válido"); // Año con 2 dígitos
        Assert.isTrue(StringUtils.isNumeric(tokens[1]), "El campo 'IdentificadorIntercambio' no es válido, no es un campo numérico"); //numerico


        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        Integer year = Integer.parseInt(sdf.format(new Date()));

        Assert.isTrue(Integer.parseInt(tokens[1])<= year,  "El campo 'IdentificadorIntercambio' no es válido, el año indicado es mayor que el actual"); // Año menor o igual al actual

        Assert.isTrue(StringUtils.length(tokens[2]) == 8, "El campo 'IdentificadorIntercambio' no es válido"); // Número secuencia de 8 dígitos

        log.info("IdentificadorIntercambio validado!");
    }

    /**
     * Validar el segmento de formulario genérico
     *
     * @param fichero Información del fichero de intercambio.
     */
    protected void validarSegmentoFormularioGenerico(FicheroIntercambio fichero) {

        Assert.notNull(fichero.getExpone(), "El campo 'expone' no puede ser null");

        Assert.notNull(fichero.getSolicita(), "El campo 'solicita' no puede ser null");

        if(StringUtils.isNotEmpty(fichero.getExpone())){
            Assert.hasText(fichero.getSolicita(), "El campo 'solicita' no puedo estar vacio");
        }


        log.info("SegmentoFormularioGenerico validado!");
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
     * Comprueba con DIR3CAIB que el codigoEntidadRegistral pertenece a una Entidad Válida.
     * @param codigoEntidadRegistral
     * @return
     */
    protected boolean validarCodigoEntidadRegistral(String codigoEntidadRegistral) {

        if (StringUtils.length(codigoEntidadRegistral) > LONGITUD_CODIGO_ENTIDAD_REGISTRAL) {
            log.info("Tamaño CODIGO_ENTIDAD_REGISTRAL demasiado largo");
            return false;
        }

        try {
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
            OficinaTF oficinaTF = oficinasService.obtenerOficina(codigoEntidadRegistral,null,null);

            if(oficinaTF == null){
                log.info("Oficina "+codigoEntidadRegistral+" no encontrada en Dir3");
                return false;
            }

        } catch (Exception e) {
            log.error("Error en validarCodigoEntidadRegistral: " + e.getMessage(), e);
            return false;
        }

        return true;
    }

    /**
     * Valida contra el DIR3 el codigoUnidadTramitacion
     * @param codigoUnidadTramitacion
     * @return
     */
    protected boolean validarCodigoUnidadTramitacion(String codigoUnidadTramitacion) {

        if (StringUtils.length(codigoUnidadTramitacion) > LONGITUD_CODIGO_UNIDAD_TRAMITACION) {
            return false;
        }

        try {
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
            UnidadTF unidadTF = unidadesService.obtenerUnidad(codigoUnidadTramitacion,null,null);

            return unidadTF != null;

        } catch (Exception e) {
            log.error("Error en validarCodigoUnidadTramitacion: " + e.getMessage(), e);
            return false;
        }
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
        XPathReaderUtil reader = null;

        // Procesamos el xml para procesar las peticiones xpath
        try {
            reader = new XPathReaderUtil(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.error("Error al parsear el XML del fichero de intercambio en la validación campos en Base64");
            throw new ValidacionException(Errores.ERROR_0037);
        }

        // Obtenemos el nombre de los campos en base64 junto con su expresion xpath
        Map<String, String> fieldsBase64 = getBase64Fields();

        for (String fieldName : fieldsBase64.keySet()) {

            // Obtenemos la expresion xpath de los campos que deben estar en base64
            String expression = fieldsBase64.get(fieldName);

            // Realizams la consulta en xpath
            NodeList results = (NodeList) reader.read(expression, XPathConstants.NODESET);

            // Verificamos que si los resultados obtenidos son distinto de vacio están en base64
            if (results != null) {
                for (int i = 0; i < results.getLength(); i++) {
                    Node item = results.item(i);
                    String value = item.getNodeValue();
                    if (StringUtils.isNotBlank(value) && !Base64.isBase64(value)) {
                        log.error("Error al parsear el XML del fichero de intercambio: Campo no codificado en Base64 "
                                + fieldName);
                        throw new ValidacionException(Errores.ERROR_0037);
                    }
                }
            }
        }
    }
}