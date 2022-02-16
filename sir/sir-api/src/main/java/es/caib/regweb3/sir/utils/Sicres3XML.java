package es.caib.regweb3.sir.utils;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.model.utils.TipoRegistro;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.schema.De_Anexo;
import es.caib.regweb3.sir.core.schema.De_Interesado;
import es.caib.regweb3.sir.core.schema.De_Mensaje;
import es.caib.regweb3.sir.core.schema.Fichero_Intercambio_SICRES_3;
import es.caib.regweb3.sir.core.schema.types.Indicador_PruebaType;
import es.caib.regweb3.sir.core.utils.Assert;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.utils.MimeTypeUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.Versio;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.caib.regweb3.utils.RegwebConstantes.*;

/**
 *
 */
public class Sicres3XML {

    public final Logger log = Logger.getLogger(getClass());

    private static final String CODIGO_PAIS_ESPANA = "724";
    private static final int LONGITUD_CODIGO_ENTIDAD_REGISTRAL = 21;
    private static final int LONGITUD_CODIGO_UNIDAD_TRAMITACION = 21;
    private static final int LONGITUD_IDENTIFICADOR_INTERCAMBIO = 33;
    private static final int LONGITUD_MAX_IDENTIFICADOR_FICHERO = 50;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    // Service WS Dir3Caib
    private Dir3CaibObtenerOficinasWs oficinasService;
    private Dir3CaibObtenerUnidadesWs unidadesService;

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


    private Map<String, String> getBase64Fields() {
        return base64Fields;
    }

    private void setBase64Fields(Map<String, String> base64Fields) {
        this.base64Fields = base64Fields;
    }

    protected void setupBase64Field() {
        LinkedHashMap<String,String> base64Fields = new LinkedHashMap<String, String>();
        base64Fields.put("Hash", "//Hash/text()");
        base64Fields.put("Timestamp_Entrada", "//Timestamp_Entrada/text()");
        base64Fields.put("Certificado", "//Certificado/text()");
        base64Fields.put("Firma_Documento", "//Firma_Documento/text()");
        base64Fields.put("TimeStamp", "//TimeStamp/text()");
        base64Fields.put("Validacion_OCSP_Certificado", "//Validacion_OCSP_Certificado/text()");
        base64Fields.put("Anexo", "//Anexo/text()");
        setBase64Fields(base64Fields);
    }


    /**
     * Convierte el xml recibido en un {@link es.caib.regweb3.sir.core.utils.FicheroIntercambio}
     *
     */
    public FicheroIntercambio parseXMLFicheroIntercambio(String xml) throws ValidacionException {

        FicheroIntercambio ficheroIntercambio = null;

        //log.info("Parseando el XML del fichero de intercambio...");

        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            Fichero_Intercambio_SICRES_3 ficheroIntercambioSICRES3 = (Fichero_Intercambio_SICRES_3) Unmarshaller.unmarshal(Fichero_Intercambio_SICRES_3.class,isr);

            if (ficheroIntercambioSICRES3 != null) {
                ficheroIntercambio = new FicheroIntercambio(ficheroIntercambioSICRES3);

                //Realizamos una validación de los campos del xml que deben estar en base64 en caso de estar presentes
                validateBase64Fields(xml);
            }

        } catch (Throwable e) {

            // Comprobamos si el error es en alguno de los campos de Código Entidad, si es así no podemos componer el mensaje de error
            if(e instanceof MarshalException){

                log.info("Error al parsear el Fichero de Intercambio a partir del xml.");

                CharSequence cs1 = "_codigo_Entidad_Registral_Origen";
                CharSequence cs2 = "_codigo_Entidad_Registral_Destino";
                CharSequence cs3 = "_identificador_Intercambio";

                if (e.getLocalizedMessage().contains(cs1) || e.getLocalizedMessage().contains(cs2) || e.getLocalizedMessage().contains(cs3)){

                    log.info("Error al parsear el xml en algun campo del segmento De_Destino o De_Origen_o_Remitente, no se podra enviar el mensaje de error.", e);
                    throw new ValidacionException(Errores.ERROR_COD_ENTIDAD_INVALIDO, "Error al parsear el xml en algun campo del segmento De_Destino o De_Origen_o_Remitente, no se podra enviar el mensaje de error.", e);
                }

            }else if(e instanceof ValidacionException){
                throw new ValidacionException(Errores.ERROR_0037,((ValidacionException) e).getMensajeError(), e);
            }

            throw new ValidacionException(Errores.ERROR_0037,e.getMessage(), e);
        }

        return ficheroIntercambio;
    }

    /**
     * Valida un Fichero de Intercambio recibido
     * @param fichero
     */
    public void validarFicheroIntercambio(FicheroIntercambio fichero, Dir3CaibObtenerOficinasWs oficinasService, Dir3CaibObtenerUnidadesWs unidadesService) {

        //log.info("Validando FicheroIntercambio...");

        Assert.notNull(fichero, "El resultado de parsear el xml del 'ficheroIntercambio' no puede ser null");

        // Obtenemos los Service para Dir3
        try {
            this.oficinasService = oficinasService;
            this.unidadesService = unidadesService;
        } catch (Exception e) {
            e.printStackTrace();
        }

		// Validamos los distintos segmentos del fichero de intercambio

        validarSegmentoOrigen(fichero);
        validarSegmentoDestino(fichero);
        validarSegmentoControl(fichero);
        validarSegmentoInteresados(fichero);
        validarSegmentoAsunto(fichero);
        validarSegmentoAnexos(fichero);
        validarSegmentoFormularioGenerico(fichero);

        //log.info("Fichero de intercambio validado");
    }

    /**
     * Validar el segmento de origen o remitente del Fichero de Intercambio
     *
     * @param fichero Información del fichero de intercambio.
     */
    private void validarSegmentoOrigen(FicheroIntercambio fichero) {

        // Validar que el código de entidad registral de origen esté informado
        Assert.hasText(fichero.getCodigoEntidadRegistralOrigen(),
                "El campo 'CodigoEntidadRegistralOrigen' del SegmentoOrigen, no puede estar vacio.");

        // Validar el código de entidad registral de origen en DIR3
        Assert.isTrue(validarCodigoEntidadRegistral(fichero.getCodigoEntidadRegistralOrigen()), "El campo 'CodigoEntidadRegistralOrigen'del SegmentoOrigen, no es valido");

        // Validar el código de unidad de tramitación de origen en DIR3
        if (StringUtils.isNotBlank(fichero.getCodigoUnidadTramitacionOrigen())) {
            Assert.isTrue(validarCodigoUnidadTramitacion(fichero.getCodigoUnidadTramitacionOrigen()),
                    "El campo 'CodigoUnidadTramitacionOrigen' del SegmentoOrigen no es valido.");
        }

        // Validar que el número de registro de entrada en origen esté informado
        Assert.hasText(fichero.getNumeroRegistro(), "El campo 'NumeroRegistroEntrada' del SegmentoOrigen, no puede estar vacio.");

        // Validar que la fecha y hora de entrada esté informada
        Assert.hasText(fichero.getFechaRegistroXML(), "El campo 'FechaHoraEntrada' del SegmentoOrigen, no puede estar vacio.");

        // Validar el formato de la fecha de entrada
        Assert.isTrue(StringUtils.equals(fichero.getFechaRegistroXML(),
                SDF.format(fichero.getFechaRegistro())),
                "El campo 'FechaHoraEntrada' SegmentoOrigen no es valido [" + fichero.getFechaRegistroXML() + "]");

        // Validar que la Fecha de entrada no sea superior a la actual
        Assert.isTrue(fichero.getFechaRegistro().before(new Date()), "El campo 'FechaHoraEntrada' del SegmentoOrigen, es mayor que la fecha actual.");

       //log.info("SegmentoOrigen validado!");
    }

    /**
     * Validar el segmento de destino del Fichero de Intercambio
     *
     * @param fichero Información del fichero de intercambio.
     */
    private void validarSegmentoDestino(FicheroIntercambio fichero) {

        // Validar que el código de entidad registral de destino esté informado
        Assert.hasText(fichero.getCodigoEntidadRegistralDestino(),
                "El campo 'CodigoEntidadRegistralDestino' del SegmentoDestino, no puede estar vacio.");

        // Validar el código de entidad registral de destino en DIR3
        Assert.isTrue(validarCodigoEntidadRegistral(fichero.getCodigoEntidadRegistralDestino()), "El campo 'CodigoEntidadRegistralDestino'del SegmentoDestino, no es valido");

        // Validar el código de unidad de tramitación de destino en DIR3
        if (StringUtils.isNotBlank(fichero.getCodigoUnidadTramitacionDestino())) {
            Assert.isTrue(validarCodigoUnidadTramitacion(fichero.getCodigoUnidadTramitacionDestino()),
                    "El campo 'CodigoUnidadTramitacionDestino' del SegmentoDestino, no es valido o no existe en DIR3.");
        }

        //log.info("SegmentoDestino validado!");
    }

    /**
     * Validar el segmento de interesados del Fichero de Intercambio
     *
     * @param fichero Información del fichero de intercambio.
     */
    private void validarSegmentoInteresados(FicheroIntercambio fichero) {

        // Comprobar los datos de los interesados
        if ((fichero.getFicheroIntercambio() != null)
                && (fichero.getFicheroIntercambio().getDe_InteresadoCount() > 0)) {

            for (De_Interesado interesado : fichero.getFicheroIntercambio().getDe_Interesado()) {

                // Si es un Registro de Entrada, ha de existir al menos un Interesado
                if(fichero.getTipoRegistro().equals(TipoRegistro.ENTRADA)){

                    if((StringUtils.isBlank(interesado.getNombre_Interesado()) && StringUtils.isBlank(interesado.getPrimer_Apellido_Interesado()))){
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getRazon_Social_Interesado()),
                                "Los campos 'NombreInteresado' y 'PrimerApellidoInteresado' o 'RazonSocialInteresado' del SegmentoInteresados, estan vacios.");
                    }else{
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getNombre_Interesado()),
                                "El campo 'NombreInteresado' del SegmentoInteresados, no puede estar vacio.");
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getPrimer_Apellido_Interesado()),
                                "El campo 'PrimerApellidoInteresado' del SegmentoInteresados, no puede estar vacio.");
                    }

                }else if(fichero.getTipoRegistro().equals(TipoRegistro.SALIDA)){

                    // Si no hay ningún Interesado
                    if(StringUtils.isBlank(interesado.getRazon_Social_Interesado()) && (StringUtils.isBlank(interesado
                            .getNombre_Interesado()) && StringUtils.isBlank(interesado.getPrimer_Apellido_Interesado()))){

                        // Comprobar que el campo CodigoUnidadTramitacionOrigen está informado y es valido
                        Assert.hasText(fichero.getCodigoUnidadTramitacionOrigen(), "No existe ningun Interesado en el SegmentoInteresados, y ademas el campo 'CodigoUnidadTramitacionOrigen' esta vacio.");

                        // Validar el código de unidad de tramitación de origen en DIR3
                        Assert.isTrue(validarCodigoUnidadTramitacion(fichero.getCodigoUnidadTramitacionOrigen()),
                                "No existe ningun Interesado en el SegmentoInteresados, y ademas el campo 'CodigoUnidadTramitacionOrigen' no es valido.");

                    }
                }

                /* INTERESADO */

                // Tipo Documento Identificación Interesado
                if (StringUtils.isNotEmpty(interesado.getTipo_Documento_Identificacion_Interesado())) {
                    Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipo_Documento_Identificacion_Interesado()), "'El valor ["+interesado.getTipo_Documento_Identificacion_Interesado()+"] del campo 'tipoDocumentoIdentificacionInteresado' del SegmentoInteresados, no esta dentro de la lista de permitidos.");

                    // Validar que el Documento concuerda con su tipo documento identificación
                    // Eliminat segons requeriment de Certificació SIR
                    /* Validacion validacionDocumento = null;
                    try {
                        validacionDocumento = DocumentoUtils.comprobarDocumento(interesado.getDocumento_Identificacion_Interesado(), RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(interesado.getTipo_Documento_Identificacion_Interesado().charAt(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        validacionDocumento = new Validacion(Boolean.FALSE, "", "");
                    }

                    Assert.isTrue(validacionDocumento.getValido(), "El campo 'documento' del SegmentoInteresados, no es correcto."); */

                    // Validar si es Salida y tipo documento 'O'
                    if (fichero.getTipoRegistro().equals(TipoRegistro.SALIDA) && interesado.getTipo_Documento_Identificacion_Interesado().equals(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN))) {
                        Assert.hasText(interesado.getDocumento_Identificacion_Interesado(), "El campo 'DocumentoIdentificacionInteresado' está vacío.");
                    }else {
                        // Validar que el Tipo Documento concuerda con Nombre o Razon Social
                        if (interesado.getTipo_Documento_Identificacion_Interesado().equals(String.valueOf(TIPODOCUMENTOID_CIF)) ||
                                interesado.getTipo_Documento_Identificacion_Interesado().equals(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN))) {
                            Assert.isTrue(StringUtils.isNotBlank(interesado.getRazon_Social_Interesado()),
                                    "El campo 'RazonSocialInteresado' del SegmentoInteresados, no puede estar vacio.");
                        } else {
                            Assert.isTrue(StringUtils.isNotBlank(interesado.getNombre_Interesado()) &&
                                            StringUtils.isNotBlank(interesado.getPrimer_Apellido_Interesado()),
                                    "Los campos 'NombreInteresado' y 'PrimerApellidoInteresado' del SegmentoInteresados, no pueden estar vacios.");
                        }
                    }
                }


                // Validar el canal preferente de comunicación del interesado
                if (StringUtils.isNotBlank(interesado.getCanal_Preferente_Comunicacion_Interesado())) {
                    Assert.notNull(CanalNotificacion.getCanalNotificacion(interesado.getCanal_Preferente_Comunicacion_Interesado()),
                            "El valor ["+interesado.getCanal_Preferente_Comunicacion_Interesado()+"] del campo 'CanalPreferenteComunicacionInteresado' del SegmentoInteresados, no esta dentro de la lista de valores permitidos.");

                    if (CanalNotificacion.DIRECCION_POSTAL.getValue().equals(interesado.getCanal_Preferente_Comunicacion_Interesado())) {

                        Assert.hasText(interesado.getPais_Interesado(), "El campo 'paisInteresado' del SegmentoInteresados, no puede estar vacio.");
                        Assert.hasText(interesado.getDireccion_Interesado(), "El campo 'direccionInteresado' del SegmentoInteresados, no puede estar vacio.");

                        if (CODIGO_PAIS_ESPANA.equals(interesado.getPais_Interesado())) {
                            Assert.isTrue(StringUtils.isNotBlank(interesado
                                            .getCodigo_Postal_Interesado()) || (StringUtils
                                            .isNotBlank(interesado.getProvincia_Interesado()) && StringUtils
                                            .isNotBlank(interesado.getMunicipio_Interesado())),
                                    "Los campos 'codigoPostalInteresado' o  la combinacion de ('provinciaInteresado' y 'municipioInteresado') del SegmentoInteresados, no pueden estar vacios.");
                        }

                    } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                            .getValue().equals(interesado.getCanal_Preferente_Comunicacion_Interesado())) {
                        Assert.hasText(
                                interesado.getDireccion_Electronica_Habilitada_Interesado(),
                                "El campo 'direccionElectronicaHabilitadaInteresado' del SegmentoInteresados, no puede estar vacio.");
                    }

                }

                /*REPRESENTANTE*/

                // Tipo Documento Identificación Representante
                if (StringUtils.isNotEmpty(interesado.getTipo_Documento_Identificacion_Representante())) {
                    Assert.notNull(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacion(interesado.getTipo_Documento_Identificacion_Representante()), "'El valor ["+interesado.getTipo_Documento_Identificacion_Representante()+"] del campo 'tipoDocumentoIdentificacionRepresentante' del SegmentoInteresados, no esta dentro de la lista de permitidos.");
                    // Validar que el Documento concuerda con su tipo documento identificación
                    // Eliminat segons requeriment de Certificació SIR
                    /* Validacion validacionDocumento = null;
                    try {
                        validacionDocumento = DocumentoUtils.comprobarDocumento(interesado.getDocumento_Identificacion_Representante(), RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(interesado.getTipo_Documento_Identificacion_Representante().charAt(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        validacionDocumento = new Validacion(Boolean.FALSE, "", "");
                    }
                    Assert.isTrue(validacionDocumento.getValido(), "El campo 'documento' del SegmentoInteresados, no es valido."); */

                    // Validar que el Tipo Documento concuerda con Nombre o Razon Social
                    if (interesado.getTipo_Documento_Identificacion_Representante().equals(String.valueOf(TIPODOCUMENTOID_CIF)) ||
                            interesado.getTipo_Documento_Identificacion_Representante().equals(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN))) {
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getRazon_Social_Representante()),
                                "El campo 'razonSocialRepresentante' del SegmentoInteresados, no puede estar vacio.");
                    } else {
                        Assert.isTrue(StringUtils.isNotBlank(interesado.getNombre_Representante()) &&
                                        StringUtils.isNotBlank(interesado.getPrimer_Apellido_Representante()),
                                "Los campos 'nombreRepresentante' y 'primerApellidoRepresentante' del SegmentoInteresados, no puede etar vacio.");
                    }
                }

                // Validar el canal preferente de comunicación del representante
                if (StringUtils.isNotBlank(interesado.getCanal_Preferente_Comunicacion_Representante())) {
                    Assert.notNull(CanalNotificacion.getCanalNotificacion(interesado.getCanal_Preferente_Comunicacion_Representante()), "El valor ["+interesado.getCanal_Preferente_Comunicacion_Representante()+"] del campo 'CanalPreferenteComunicacionRepresentante' del SegmentoInteresados, no esta dentro de la lista de valores permitidos.");
                    if (CanalNotificacion.DIRECCION_POSTAL.getValue().equals(interesado.getCanal_Preferente_Comunicacion_Representante())) {

                        Assert.hasText(interesado.getPais_Representante(),
                                "El campo 'paisRepresentante' del SegmentoInteresados, no puede estar vacio.");
                        Assert.hasText(
                                interesado.getDireccion_Representante(),
                                "El campo 'direccionRepresentante' del SegmentoInteresados, no puede estar vacio.");

                        if (CODIGO_PAIS_ESPANA.equals(interesado
                                .getPais_Representante())) {
                            Assert.isTrue(
                                    StringUtils
                                            .isNotBlank(interesado.getCodigo_Postal_Representante())
                                            || (StringUtils
                                            .isNotBlank(interesado.getProvincia_Representante()) && StringUtils
                                            .isNotBlank(interesado.getMunicipio_Representante())),
                                    "Los campos 'codigoPostalRepresentante' o la combinación de ('provinciaRepresentante' y 'municipioRepresentante') del SegmentoInteresados, no pueden estar vacios.");
                        }

                    } else if (CanalNotificacion.DIRECCION_ELECTRONICA_HABILITADA
                            .getValue().equals(interesado
                                    .getCanal_Preferente_Comunicacion_Representante())) {
                        Assert.hasText(
                                interesado.getDireccion_Electronica_Habilitada_Representante(),
                                "El campo 'direccionElectronicaHabilitadaRepresentante' del SegmentoInteresados, no puede estar vacio.");
                    }
                }

                //log.info("Interesado validado!");

            }
        }

        //log.info("SegmentoInteresados validado!");
    }

    /**
     * Validar el segmento de asunto del Fichero de Intercambio
     *
     * @param fichero Información del fichero de intercambio.
     */
    private void validarSegmentoAsunto(FicheroIntercambio fichero) {

        // Validar que el resumen esté informado
        Assert.hasText(fichero.getResumen(), "El campo 'Resumen' del SegmentoAsunto, no puede estar vacio.");

        //log.info("SegmentoAsunto validado!");
    }

    /**
     * Validar el segmento de anexos del Fichero de Intercambio
     *
     * @param fichero Información del fichero de intercambio.
     */
    private void validarSegmentoAnexos(FicheroIntercambio fichero) {

        // Validar los documentos
        if ((fichero.getFicheroIntercambio() != null)
                && ArrayUtils.isNotEmpty(fichero.getFicheroIntercambio().getDe_Anexo())) {
            De_Anexo[] anexos = fichero.getFicheroIntercambio().getDe_Anexo();
            for (De_Anexo anexo : anexos) {
                validarAnexo(anexo, fichero.getIdentificadorIntercambio());

                //Si el anexo tiene identificador de documento firmado significa que es firma de otro anexo, se debe comprobar que es así.
                if (StringUtils.isNotEmpty(anexo.getIdentificador_Documento_Firmado())) {
                    Boolean firmaDeOtroAnexo = false;
                    for (De_Anexo anexo2 : anexos) {
                        if (anexo2.getIdentificador_Fichero().equals(anexo.getIdentificador_Documento_Firmado())) {
                            firmaDeOtroAnexo = true;
                        }
                    }
                    Assert.isTrue(firmaDeOtroAnexo, "El anexo del SegmentoAnexos, no es firma de ningun otro anexo.");
                }

            }
        }
        //log.info("SegmentoAnexos validado!");
    }

    /**
     * Valida un anexo del segmento de anexos del Fichero de Intercambio
     *
     * @param anexo                    Información del anexo
     * @param identificadorIntercambio Identificador de intercambio
     */
    private void validarAnexo(De_Anexo anexo, String identificadorIntercambio) {

        if (anexo != null) {

            // Validar el nombre del fichero anexado
            Assert.hasText(anexo.getNombre_Fichero_Anexado(), "El campo 'NombreFicheroAnexado' del SegmentoAnexos, no puede estar vacio.");
            Assert.isTrue(!StringUtils.containsAny(anexo.getNombre_Fichero_Anexado(), RegwebConstantes.CARACTERES_NO_PERMITIDOS_SIR),
                    "El campo 'NombreFicheroAnexado' del SegmentoAnexos, tiene caracteres no validos [" + anexo.getNombre_Fichero_Anexado() + "]");

            // Validar el campo tipo de documento
            Assert.hasText(anexo.getTipo_Documento(), "El campo 'TipoDocumento' del SegmentoAnexos, no puede estar vacio.");
            Assert.notNull(TipoDocumento.getTipoDocumento(anexo.getTipo_Documento()), "El valor [" + anexo.getTipo_Documento() + "] del campo 'TipoDocumento' del SegmentoAnexos, no esta dentro de la lista de permitidos.");

            // Validar el identificador de fichero
            validarIdentificadorFichero(anexo, identificadorIntercambio);

            // Si no es un Fichero Técnico (TipoDocumento = 03), validmos la extensión y el Timo Mime
            if(!TipoDocumento.FICHERO_TECNICO_INTERNO.getValue().equals(anexo.getTipo_Documento())){
                // Obtenemos la extensión del fichero
                String identificadorFichero = StringUtils.substringAfter(anexo.getIdentificador_Fichero(), identificadorIntercambio + "_");
                String[] tokens = StringUtils.split(identificadorFichero, "_.");
                String extensionFichero = tokens[2].toLowerCase();

                // Validamos si la extensión está permitida
                Assert.isTrue(Arrays.asList(RegwebConstantes.ANEXO_EXTENSIONES_SIR).contains(extensionFichero), "La extensión del fichero [" + extensionFichero + "] no está permitida");

                // Validar el tipo MIME
                if (StringUtils.isNotBlank(anexo.getTipo_MIME())) {
                    Assert.isTrue(StringUtils.equalsIgnoreCase(anexo.getTipo_MIME(), MimeTypeUtils.getMimeTypeExtension(extensionFichero)),
                            "El Tipo mime: "+anexo.getTipo_MIME()+" del SegmentoAnexos, no coincide con el indicado en el campo 'IdentificadorFichero': " + identificadorFichero);
                }
            }

            // Validar el campo validez de documento
            if (StringUtils.isNotBlank(anexo.getValidez_Documento())) {
                Assert.notNull(ValidezDocumento.getValidezDocumento(anexo.getValidez_Documento()),
                        "El campo 'ValidezDocumento' del SegmentoAnexos, no es valido [" + anexo.getValidez_Documento() + "]");
            }

            // Validar el hash del documento
            // Nota: no se comprueba el código hash de los documentos porque no
            // se especifica con qué algoritmo está generado.
            Assert.isTrue(!ArrayUtils.isEmpty(anexo.getHash()), "El campo 'Hash' del SegmentoAnexos, no puede estar vacio.");

            // Validar el contenido del anexo
            Assert.isTrue(anexo.getAnexo().length > 0, "El campo 'Anexo' del SegmentoAnexos, no puede estar vacio.");

            //log.info("Anexo '"+anexo.getNombre_Fichero_Anexado()+"' validado!");
        }
    }


    /**
     * Valida el identificador de fichero de un anexo del segmento de anexos del Fichero de Intercambio
     * Patron: <Identificador del Intercambio>_<Código de tipo de archivo>_<Número Secuencial>.<Extensión del fichero>
     *
     * @param anexo                    Información del anexo
     * @param identificadorIntercambio Identificador de intercambio
     */
    private void validarIdentificadorFichero(De_Anexo anexo, String identificadorIntercambio) {

        // No vacío
        Assert.hasText(anexo.getIdentificador_Fichero(), "El valor del campo 'IdentificadorFichero' del SegmentoAnexos, no puede estar vacio.");

        // Validar el tamaño
        Assert.isTrue(StringUtils.length(anexo.getIdentificador_Fichero()) <= LONGITUD_MAX_IDENTIFICADOR_FICHERO,
                "El valor del campo 'IdentificadorFichero': ["+anexo.getIdentificador_Fichero()+"] del SegmentoAnexos, es demasiado largo.");

        // Validar formato: <Identificador del Intercambio>_<Código de tipo de archivo>_<Número Secuencial>.<Extensión del fichero>

        String identificadorFichero = anexo.getIdentificador_Fichero();
        Assert.isTrue(StringUtils.startsWith(identificadorFichero, identificadorIntercambio), "El valor del campo 'IdentificadorFichero': ["+anexo.getIdentificador_Fichero()+"] del SegmentoAnexos, no concuerda con el 'Identificador Intercambio'.");

        identificadorFichero = StringUtils.substringAfter(identificadorFichero, identificadorIntercambio + "_");

        // Obtenemos los tres valores separados: <Código de tipo de archivo> <Número Secuencial> <Extensión del fichero>
        String[] tokens = StringUtils.split(identificadorFichero, "_.");

        // Validar presencia de Extensión de Fichero
        Assert.isTrue(identificadorFichero.contains("."), "El valor del campo 'IdentificadorFichero': ["+identificadorFichero+"] del SegmentoAnexos, no contiene la extension del fichero.");

        Assert.isTrue(ArrayUtils.getLength(tokens) == 3, "El campo 'IdentificadorFichero': ["+identificadorFichero+"] del SegmentoAnexos, no es valido, no contiene el tipo de archivo o el numero secuencial");

        // Código de tipo de archivo de 2 dígitos
        Assert.isTrue(StringUtils.equals(tokens[0], "01"), "El valor del campo 'IdentificadorFichero': ["+identificadorFichero+"] del SegmentoAnexos, no es valido, el tipo de archivo es distinto de [01].");
        // Número secuencial de hasta 4 dígitos
        Assert.isTrue(StringUtils.length(tokens[1]) <= 4, "El valor del campo 'IdentificadorFichero': ["+identificadorFichero+"] del SegmentoAnexos, no es valido, el numero secuencial es superior a 4 digitos.");
        // Número secuencial compuesto por solo dígitos
        Assert.isTrue(StringUtils.isNumeric(tokens[1]), "El valor del campo 'IdentificadorFichero': ["+identificadorFichero+"] del SegmentoAnexos, no es valido, hay un error en el numero secuencial, no es de tipo numerico.");
        // Extensión del fichero
        Assert.hasText(tokens[2], "El valor del campo 'IdentificadorFichero': ["+identificadorFichero+"] del SegmentoAnexos, no es valido, la extension del fichero no esta informada.");
    }

    /**
     * Validar el segmento de internos y control del Fichero de Intercambio
     *
     * @param fichero Información del fichero de intercambio.
     */
    private void validarSegmentoControl(FicheroIntercambio fichero) {

        // Validar el tipo de transporte
        if(StringUtils.isNotBlank(fichero.getTipoTransporteXML())){
            Assert.notNull(TipoTransporte.getTipoTransporte(fichero.getTipoTransporteXML()),"El valor [" + fichero.getTipoTransporteXML() + "] del campo 'TipoTransporteEntrada' del SegmentoAnexos, no esta dentro de la lista de permitidos.");
        }

        // Validar el tipo de anotación
        Assert.hasText(fichero.getTipoAnotacionXML(), "El campo 'TipoAnotacion' del SegmentoControl, no puede estar vacio");
        Assert.notNull(fichero.getTipoAnotacion(), "El valor [" + fichero.getTipoAnotacionXML() + "] del campo 'TipoAnotacion' del SegmentoControl, no esta dentro de la lista de permitidos.");

        // Validar que el código de entidad registral de inicio esté informado
        Assert.hasText(fichero.getCodigoEntidadRegistralInicio(), "El campo 'CodigoEntidadRegistralInicio' del SegmentoControl, no puede estar vacio");

        // Validar el código de entidad registral de inicio en DIR3
        Assert.isTrue(validarCodigoEntidadRegistral(fichero.getCodigoEntidadRegistralInicio()), "El campo 'CodigoEntidadRegistralInicio' del SegmentoControl, no es valido");

        // Validar el identificador de intercambio, tiene que realizarse despues de la validacion del código de entidad registral de inicio
        validarIdentificadorIntercambio(fichero);

        //log.info("SegmentoControl validado!");
    }

    /**
     * Validar el identificador de intercambio. del Fichero de Intercambio
     * Patrón:  <Código_Entidad_Registral_Origen><AA><Número Secuencial>
     * @param fichero Información del fichero de intercambio.
     */
    private void validarIdentificadorIntercambio(FicheroIntercambio fichero) {

        // Comprobar que no esté vacío
        Assert.hasText(fichero.getIdentificadorIntercambio(),
                "El campo 'IdentificadorIntercambio' del SegmentoControl, no puede estar vacio");

        Assert.isTrue(fichero.getIdentificadorIntercambio().length() <= LONGITUD_IDENTIFICADOR_INTERCAMBIO,
                "El campo 'IdentificadorIntercambio' del SegmentoControl, no es valido, su longitud es superior a 33 caracteres.");

        // Comprobar el formato del identificiador de intercambio: <Código_Entidad_Registral_Origen><AA><Número Secuencial>
        String[] tokens = StringUtils.split(fichero.getIdentificadorIntercambio(), "_");

        Assert.isTrue(ArrayUtils.getLength(tokens) == 3, "El campo 'IdentificadorIntercambio' del SegmentoControl, no es valido");

        // CodigoEntidadRegistral
        Assert.isTrue(StringUtils.length(tokens[0]) <= LONGITUD_CODIGO_ENTIDAD_REGISTRAL,
                "El campo 'IdentificadorIntercambio' del SegmentoControl, no es valido, la longitud del 'CodigoEntidadRegistral' es superior a 21"); // Código de la entidad registral
        //Assert.isTrue(validarCodigoEntidadRegistral(tokens[0]), "El campo 'IdentificadorIntercambio' del SegmentoControl, no es valido, el 'CodigoEntidadRegistral' no existe en el Directorio Comun"); // Código de la entidad registral

        Assert.isTrue(StringUtils.equals(tokens[0], fichero.getCodigoEntidadRegistralInicio()),
                "El campo 'Identificador_Intercambio' del SegmentoControl, no es valido, el 'Codigo_Entidad_Registral_Origen' no concuerda con 'CodigoEntidadRegistralInicio'");

        // Año
        Integer year = Integer.parseInt(new SimpleDateFormat("yy").format(new Date()));
        Assert.isTrue(StringUtils.length(tokens[1]) == 2, "El campo 'IdentificadorIntercambio' del SegmentoControl, no es valido, el año no esta formado por dos caracteres"); // Año con 2 dígitos
        Assert.isTrue(StringUtils.isNumeric(tokens[1]), "El campo 'IdentificadorIntercambio' del SegmentoControl, no es valido, el año no es un campo numérico"); //numerico
        Assert.isTrue(Integer.parseInt(tokens[1])<= year,  "El campo 'Identificador_Intercambio' del SegmentoControl, no es valido, el año indicado es mayor que el actual"); // Año menor o igual al actual

        // Número secuencia de 8 dígitos
        Assert.isTrue(StringUtils.length(tokens[2]) == 8, "El campo 'IdentificadorIntercambio' del SegmentoControl, no es valido, la longitud del numero secuencial es mayor de 8 caracteres.");
        Assert.isTrue(StringUtils.isNumeric(tokens[2]), "El valor del campo 'IdentificadorFichero' del SegmentoControl, no es valido, hay un error en el numero secuencial, no es de tipo numerico.");

        //log.info("IdentificadorIntercambio validado!");
    }

    /**
     * Validar el segmento de formulario genérico del Fichero de Intercambio
     *
     * @param fichero Información del fichero de intercambio.
     */
    private void validarSegmentoFormularioGenerico(FicheroIntercambio fichero) {

        Assert.notNull(fichero.getExpone(), "El campo 'expone' del SegmentoFormularioGenerico, no puede ser null");

        Assert.notNull(fichero.getSolicita(), "El campo 'solicita' del SegmentoFormularioGenerico, no puede ser null");

        Assert.isTrue(StringUtils.length(fichero.getExpone()) <= 4000,"El campo 'expone' del SegmentoFormularioGenerico, tiene mas de 4000 caracteres");

        Assert.isTrue(StringUtils.length(fichero.getSolicita()) <= 4000,"El campo 'solicita' del SegmentoFormularioGenerico, tiene mas de 4000 caracteres");

        if(StringUtils.isNotEmpty(fichero.getExpone())){
            Assert.hasText(fichero.getSolicita(), "El campo 'solicita' del SegmentoFormularioGenerico, no puedo estar vacio");
        }

        //log.info("SegmentoFormularioGenerico validado!");
    }

    /**
     * Valida un Mensaje de Control recibido o que va a ser enviado
     * @param mensaje
     */
    public void validarMensaje(MensajeControl mensaje) {

        Assert.notNull(mensaje, "El campo 'mensaje' no puede ser null");

        Assert.hasText(mensaje.getCodigoEntidadRegistralOrigen(), "El campo 'codigoEntidadRegistralOrigen' no puede estar vacio");
        Assert.hasText(mensaje.getCodigoEntidadRegistralDestino(), "El campo 'codigoEntidadRegistralDestino' no puede estar vacio");
        Assert.hasText(mensaje.getIdentificadorIntercambio(), "El campo 'identificadorIntercambio' no puede estar vacio");
        Assert.notNull(mensaje.getTipoMensaje(), "El campo 'tipoMensaje' no puede ser null");
    }

    /**
     * Crea el xml del Fichero de Intercambio a partir de un RegistroSir
     * @param registroSir
     * @return
     */
    public Document crearXMLFicheroIntercambioSICRES3(RegistroSir registroSir) throws Exception   {

        Assert.notNull(registroSir, "El registroSir no puede ser null");

        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");

        // Fichero_Intercambio_SICRES_3
        Element rootNode = doc.addElement("Fichero_Intercambio_SICRES_3");

        /* Segmento DeOrigenORemitente */
        addDatosOrigenORemitente(rootNode, registroSir);

        /* Segmento DeDestino */
        addDatosDestino(rootNode, registroSir);

        /* Segmento DeInteresados */
        addDatosInteresados(rootNode, registroSir.getInteresados());

        /* Segmento DeAsunto */
        addDatosAsunto(rootNode, registroSir);

        /* Segmento DeAnexo */
        addDatosAnexos(rootNode, registroSir);

        /* Segmento DeInternosControl */
        addDatosInternosControl(rootNode, registroSir);

        /* Segmento DeFormularioGenerico */
        addDatosformularioGenerico(rootNode, registroSir);

        return doc;
    }


    /**
     * Añade el Segmento deOrigenORemitente al Fichero de Intercambio
     *
     * @param rootNode
     * @param registroSir
     */
    private void addDatosOrigenORemitente(Element rootNode, RegistroSir registroSir) {

        // De_Origen_o_Remitente
        Element rootElement = rootNode.addElement("De_Origen_o_Remitente");
        Element elem = null;

        // Codigo_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(registroSir.getCodigoEntidadRegistralOrigen())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Origen");
            elem.addCDATA(registroSir.getCodigoEntidadRegistralOrigen());
        }

        // Decodificacion_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(registroSir.getDecodificacionEntidadRegistralOrigen())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Origen");
            elem.addCDATA(registroSir.getDecodificacionEntidadRegistralOrigen());
        }

        // Numero_Registro_Entrada
        if (StringUtils.isNotBlank(registroSir.getNumeroRegistro())) {
            elem = rootElement.addElement("Numero_Registro_Entrada");
            elem.addCDATA(registroSir.getNumeroRegistro());
        }


        // Fecha_Hora_Entrada
        if (registroSir.getFechaRegistro() != null) {
            elem = rootElement.addElement("Fecha_Hora_Entrada");
            elem.addCDATA(SDF.format(registroSir.getFechaRegistro()));
        }


        // Timestamp_Entrada
        if (registroSir.getTimestampRegistro() != null) {
            elem = rootElement.addElement("Timestamp_Entrada");
            elem.addCDATA(registroSir.getTimestampRegistro());
        }


        // Codigo_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(registroSir.getCodigoUnidadTramitacionOrigen())) {
            elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Origen");
            elem.addCDATA(registroSir.getCodigoUnidadTramitacionOrigen());
        }

        // Decodificacion_Unidad_Tramitacion_Origen
        if (StringUtils.isNotBlank(registroSir.getDecodificacionUnidadTramitacionOrigen())) {
            elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Origen");
            elem.addCDATA(registroSir.getDecodificacionUnidadTramitacionOrigen());
        }

    }

    /**
     * Añade el Segmento deDestino al Fichero de Intercambio
     *
     * @param rootNode
     * @param registroSir
     */
    private void addDatosDestino(Element rootNode, RegistroSir registroSir) {

        // De_Destino
        Element rootElement = rootNode.addElement("De_Destino");
        Element elem = null;

        // Codigo_Entidad_Registral_Destino
        if (StringUtils.isNotBlank(registroSir.getCodigoEntidadRegistralDestino())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Destino");
            elem.addCDATA(registroSir.getCodigoEntidadRegistralDestino());
        }

        // Decodificacion_Entidad_Registral_Destino
        if (StringUtils.isNotBlank(registroSir.getDecodificacionEntidadRegistralDestino())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Destino");
            elem.addCDATA(registroSir.getDecodificacionEntidadRegistralDestino());
        }

        // Codigo_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(registroSir.getCodigoUnidadTramitacionDestino())) {
            elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Destino");
            elem.addCDATA(registroSir.getCodigoUnidadTramitacionDestino());
        }

        // Decodificacion_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(registroSir.getDecodificacionUnidadTramitacionDestino())) {
            elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Destino");
            elem.addCDATA(registroSir.getDecodificacionUnidadTramitacionDestino());
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

        if (!isEmptyCollection(interesadosSir)) {

            for (InteresadoSir interesadoSir : interesadosSir) {

                Element rootElement = rootNode.addElement("De_Interesado");
                Element elem = null;

                // Tipo_Documento_Identificacion_Interesado
                if (interesadoSir.getTipoDocumentoIdentificacionInteresado() != null) {
                    elem = rootElement.addElement("Tipo_Documento_Identificacion_Interesado");
                    elem.addCDATA(interesadoSir.getTipoDocumentoIdentificacionInteresado());
                }
                // Documento_Identificacion_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getDocumentoIdentificacionInteresado())) {
                    elem = rootElement.addElement("Documento_Identificacion_Interesado");
                    elem.addCDATA(interesadoSir.getDocumentoIdentificacionInteresado());
                }
                // Razon_Social_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getRazonSocialInteresado())) {
                    elem = rootElement.addElement("Razon_Social_Interesado");
                    elem.addCDATA(interesadoSir.getRazonSocialInteresado());
                }
                // Nombre_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getNombreInteresado())) {
                    elem = rootElement.addElement("Nombre_Interesado");
                    elem.addCDATA(interesadoSir.getNombreInteresado());
                }
                // Primer_Apellido_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getPrimerApellidoInteresado())) {
                    elem = rootElement.addElement("Primer_Apellido_Interesado");
                    elem.addCDATA(interesadoSir.getPrimerApellidoInteresado());
                }
                // Segundo_Apellido_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getSegundoApellidoInteresado())) {
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
                if (StringUtils.isNotEmpty(interesadoSir.getDireccionInteresado())) {
                    elem = rootElement.addElement("Direccion_Interesado");
                    elem.addCDATA(interesadoSir.getDireccionInteresado());
                }
                // Codigo_Postal_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getCodigoPostalInteresado())) {
                    elem = rootElement.addElement("Codigo_Postal_Interesado");
                    elem.addCDATA(interesadoSir.getCodigoPostalInteresado());
                }
                // Correo_Electronico_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getCorreoElectronicoInteresado())) {
                    elem = rootElement.addElement("Correo_Electronico_Interesado");
                    elem.addCDATA(interesadoSir.getCorreoElectronicoInteresado());
                }
                // Telefono_Contacto_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getTelefonoInteresado())) {
                    elem = rootElement.addElement("Telefono_Contacto_Interesado");
                    elem.addCDATA(interesadoSir.getTelefonoInteresado());
                }
                // Direccion_Electronica_Habilitada_Interesado
                if (StringUtils.isNotEmpty(interesadoSir.getDireccionElectronicaHabilitadaInteresado())) {
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
                if (StringUtils.isNotEmpty(interesadoSir.getDireccionRepresentante())) {
                    elem = rootElement.addElement("Direccion_Representante");
                    elem.addCDATA(interesadoSir.getDireccionRepresentante());
                }
                // Codigo_Postal_Representante
                if (StringUtils.isNotEmpty(interesadoSir.getCodigoPostalRepresentante())) {
                    elem = rootElement.addElement("Codigo_Postal_Representante");
                    elem.addCDATA(interesadoSir.getCodigoPostalRepresentante());
                }
                // Correo_Electronico_Representante
                if (StringUtils.isNotEmpty(interesadoSir.getCorreoElectronicoRepresentante())) {
                    elem = rootElement.addElement("Correo_Electronico_Representante");
                    elem.addCDATA(interesadoSir.getCorreoElectronicoRepresentante());
                }
                // Telefono_Contacto_Representante
                if (StringUtils.isNotEmpty(interesadoSir.getTelefonoRepresentante())) {
                    elem = rootElement.addElement("Telefono_Contacto_Representante");
                    elem.addCDATA(interesadoSir.getTelefonoRepresentante());
                }
                // Direccion_Electronica_Habilitada_Representante
                if (StringUtils.isNotEmpty(interesadoSir.getDireccionElectronicaHabilitadaRepresentante())) {
                    elem = rootElement.addElement("Direccion_Electronica_Habilitada_Representante");
                    elem.addCDATA(interesadoSir.getDireccionElectronicaHabilitadaRepresentante());
                }
                // Canal_Preferente_Comunicacion_Representante
                if (interesadoSir.getCanalPreferenteComunicacionRepresentante() != null) {
                    elem = rootElement.addElement("Canal_Preferente_Comunicacion_Representante");
                    elem.addCDATA(interesadoSir.getCanalPreferenteComunicacionRepresentante());
                }

                // Observaciones
                if (StringUtils.isNotEmpty(interesadoSir.getObservaciones())) {
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
     * @param registroSir
     */
    private void addDatosAsunto(Element rootNode, RegistroSir registroSir) {

        // De_Asunto
        Element rootElement = rootNode.addElement("De_Asunto");
        Element elem = null;

        // Resumen
        elem = rootElement.addElement("Resumen");
        if (StringUtils.isNotBlank(registroSir.getResumen())) {
            elem.addCDATA(registroSir.getResumen());
        }
        // Codigo_Asunto_Segun_Destino
        if (registroSir.getCodigoAsunto() != null) {
            elem = rootElement.addElement("Codigo_Asunto_Segun_Destino");
            elem.addCDATA(registroSir.getCodigoAsunto());
        }

        // Referencia_Externa
        if (StringUtils.isNotBlank(registroSir.getReferenciaExterna())) {
            elem = rootElement.addElement("Referencia_Externa");
            elem.addCDATA(registroSir.getReferenciaExterna());
        }

        // Numero_Expediente
        if (StringUtils.isNotBlank(registroSir.getNumeroExpediente())) {
            elem = rootElement.addElement("Numero_Expediente");
            elem.addCDATA(registroSir.getNumeroExpediente());
        }

    }

    private void addDatosAnexos(Element rootNode, RegistroSir registroSir) throws Exception  {
        for (AnexoSir anexoSir : registroSir.getAnexos()) {

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
                elem.addCDATA(anexoSir.getCertificado());
            }

            //Firma documento (propiedad Firma Documento del segmento)
            if (anexoSir.getFirma() != null) {
                elem = rootElement.addElement("Firma_Documento");
                elem.addCDATA(anexoSir.getFirma());
            }

            // TimeStamp
            if (anexoSir.getTimestamp() != null) {
                elem = rootElement.addElement("TimeStamp");
                elem.addCDATA(anexoSir.getTimestamp());
            }

            // Validacion_OCSP_Certificado
            if (anexoSir.getValidacionOCSPCertificado() != null) {
                elem = rootElement.addElement("Validacion_OCSP_Certificado");
                elem.addCDATA(anexoSir.getValidacionOCSPCertificado());
            }

            // Hash
            if (anexoSir.getHash() != null) {
                elem = rootElement.addElement("Hash");
                elem.addCDATA(anexoSir.getHash());
            }else{
                //log.info("getHash es null");
            }

            // Tipo_MIME
            // Enviar solo tipos mimes inferiores a 20 y aceptados por SIR, si no, no se informa.
            if (anexoSir.getTipoMIME() != null) {
                elem = rootElement.addElement("Tipo_MIME");
                elem.addCDATA(anexoSir.getTipoMIME());
            }

            //Anexo (propiedad Anexo del segmento)
            if (anexoSir.getAnexoData() != null) {
                elem = rootElement.addElement("Anexo");
                elem.addCDATA(getBase64String(anexoSir.getAnexoData()));
            }else{
                //log.info("getAnexoData es null");
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
     * @param registroSir
     */
    private void addDatosInternosControl(Element rootNode, RegistroSir registroSir) {

        // De_Internos_Control
        Element rootElement = rootNode.addElement("De_Internos_Control");
        Element elem = null;

        // Tipo_Transporte_Entrada
        if (registroSir.getTipoTransporte() != null) {
            elem = rootElement.addElement("Tipo_Transporte_Entrada");
            elem.addCDATA(registroSir.getTipoTransporte());
        }

        // Numero_Transporte_Entrada
        if (StringUtils.isNotBlank(registroSir.getNumeroTransporte())) {
            elem = rootElement.addElement("Numero_Transporte_Entrada");
            elem.addCDATA(registroSir.getNumeroTransporte());
        }

        // Nombre_Usuario
        if (StringUtils.isNotBlank(registroSir.getNombreUsuario())) {
            elem = rootElement.addElement("Nombre_Usuario");
            elem.addCDATA(registroSir.getNombreUsuario());
        }

        // Contacto_Usuario
        if (StringUtils.isNotBlank(registroSir.getContactoUsuario())) {
            elem = rootElement.addElement("Contacto_Usuario");
            elem.addCDATA(registroSir.getContactoUsuario());
        }

        // Identificador_Intercambio
        if (StringUtils.isNotBlank(registroSir.getIdentificadorIntercambio())) {
            elem = rootElement.addElement("Identificador_Intercambio");
            elem.addCDATA(registroSir.getIdentificadorIntercambio());
        }

        // Aplicacion_Version_Emisora
        elem = rootElement.addElement("Aplicacion_Version_Emisora");
        elem.addCDATA(Versio.VERSIO_SIR);

        // Tipo_Anotacion
        elem = rootElement.addElement("Tipo_Anotacion");
        elem.addCDATA(registroSir.getTipoAnotacion());

        elem = rootElement.addElement("Descripcion_Tipo_Anotacion");
        elem.addCDATA(registroSir.getDecodificacionTipoAnotacion());

        // Tipo_Registro
        elem = rootElement.addElement("Tipo_Registro");
        elem.addCDATA(registroSir.getTipoRegistro().getValue());

        // Documentacion_Fisica
        if (StringUtils.isNotBlank(registroSir.getDocumentacionFisica())) {
            elem = rootElement.addElement("Documentacion_Fisica");
            elem.addCDATA(registroSir.getDocumentacionFisica());
        }

        // Observaciones_Apunte
        if (StringUtils.isNotBlank(registroSir.getObservacionesApunte())) {
            elem = rootElement.addElement("Observaciones_Apunte");
            elem.addCDATA(registroSir.getObservacionesApunte());
        }

        // Indicador_Prueba
        elem = rootElement.addElement("Indicador_Prueba");
        elem.addCDATA(registroSir.getIndicadorPrueba().getValue());

        // Codigo_Entidad_Registral_Inicio
        if (StringUtils.isNotBlank(registroSir.getCodigoEntidadRegistralInicio())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Inicio");
            elem.addCDATA(registroSir.getCodigoEntidadRegistralInicio());
        }

        // Decodificacion_Entidad_Registral_Inicio
        if (StringUtils.isNotBlank(registroSir.getDecodificacionEntidadRegistralInicio())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Inicio");
            elem.addCDATA(registroSir.getDecodificacionEntidadRegistralInicio());
        }


    }

    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param rootNode
     * @param registroSir
     */
    private void addDatosformularioGenerico(Element rootNode, RegistroSir registroSir) {

        // De_Formulario_Generico
        Element rootElement = rootNode.addElement("De_Formulario_Generico");
        Element elem = null;

        // Expone
        elem = rootElement.addElement("Expone");
        if (StringUtils.isNotBlank(registroSir.getExpone())) {
            elem.addCDATA(registroSir.getExpone());
        }

        // Solicita
        elem = rootElement.addElement("Solicita");
        if (StringUtils.isNotBlank(registroSir.getSolicita())) {
            elem.addCDATA(registroSir.getSolicita());
        }

    }

    /**
     * @param registroEntrada
     * @return
     */
    public String crearXMLFicheroIntercambioSICRES3(RegistroEntrada registroEntrada) throws Exception   {

        Assert.notNull(registroEntrada, "La variable 'registroEntrada' no puede ser null");

        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");

        // Fichero_Intercambio_SICRES_3
        Element rootNode = doc.addElement("Fichero_Intercambio_SICRES_3");

        /* Segmento DeOrigenORemitente */
        addDatosOrigenORemitente(rootNode, registroEntrada);

        /* Segmento DeDestino */
        addDatosDestino(rootNode, registroEntrada);

        /* Segmento DeInteresados */
        addDatosInteresados(rootNode, registroEntrada.getRegistroDetalle());

        /* Segmento DeAsunto */
        addDatosAsunto(rootNode, registroEntrada.getRegistroDetalle());

        /* Segmento DeAnexo */
        addDatosAnexos(rootNode, registroEntrada.getRegistroDetalle());

        /* Segmento DeInternosControl */
        addDatosInternosControl(rootNode, registroEntrada);

        /* Segmento DeFormularioGenerico */
        addDatosformularioGenerico(rootNode, registroEntrada.getRegistroDetalle());

        return doc.asXML();
    }

    /**
     * Añade el Segmento deOrigenORemitente al Fichero de Intercambio
     *
     * @param rootNode
     * @param registro
     */
    private void addDatosOrigenORemitente(Element rootNode, RegistroEntrada registro) {

        // De_Origen_o_Remitente
        Element rootElement = rootNode.addElement("De_Origen_o_Remitente");
        Element elem = null;

        // Codigo_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(registro.getOficina().getCodigo())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Origen");
            elem.addCDATA(registro.getOficina().getCodigo());
        }

        // Decodificacion_Entidad_Registral_Origen
        if (StringUtils.isNotBlank(registro.getOficina().getDenominacion())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Origen");
            elem.addCDATA(registro.getOficina().getDenominacion());
        }

        // Numero_Registro_Entrada
        if (StringUtils.isNotBlank(registro.getNumeroRegistroFormateado())) {
            elem = rootElement.addElement("Numero_Registro_Entrada");
            elem.addCDATA(registro.getNumeroRegistroFormateado());
        }

        // Fecha_Hora_Entrada
        if (registro.getFecha() != null) {
            elem = rootElement.addElement("Fecha_Hora_Entrada");
            elem.addCDATA(SDF.format(registro.getFecha()));
        }

        // Timestamp_Entrada
        //deOrigenORemitente.setTimestampEntrada(); // No es necesario

        Entidad entidad = registro.getOficina().getOrganismoResponsable().getEntidad();

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

        //log.info("Segmento De_Origen_o_Remitente creado");

    }

    /**
     * Añade el Segmento deDestino al Fichero de Intercambio
     *
     * @param rootNode
     * @param registro
     */
    private void addDatosDestino(Element rootNode, RegistroEntrada registro) {

        // De_Destino
        Element rootElement = rootNode.addElement("De_Destino");
        Element elem = null;

        // Codigo_Entidad_Registral_Destino
        if (StringUtils.isNotBlank(registro.getRegistroDetalle().getCodigoEntidadRegistralDestino())) {
            elem = rootElement.addElement("Codigo_Entidad_Registral_Destino");
            elem.addCDATA(registro.getRegistroDetalle().getCodigoEntidadRegistralDestino());
        }

        // Decodificacion_Entidad_Registral_Destino
        if (StringUtils.isNotBlank(registro.getRegistroDetalle().getDecodificacionEntidadRegistralDestino())) {
            elem = rootElement.addElement("Decodificacion_Entidad_Registral_Destino");
            elem.addCDATA(registro.getRegistroDetalle().getDecodificacionEntidadRegistralDestino());
        }

        // Codigo_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(registro.getDestinoExternoCodigo())) {
            elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Destino");
            elem.addCDATA(registro.getDestinoExternoCodigo());
        }

        // Decodificacion_Unidad_Tramitacion_Destino
        if (StringUtils.isNotBlank(registro.getDestinoExternoDenominacion())) {
            elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Destino");
            elem.addCDATA(registro.getDestinoExternoDenominacion());
        }

        //log.info("Segmento De_Destino creado");

    }

    /**
     * Añade el Segmento deInteresado al Fichero de Intercambio
     *
     * @param rootNode
     * @param registroDetalle
     */
    private void addDatosInteresados(Element rootNode, RegistroDetalle registroDetalle) {

        List<Interesado> interesados = registroDetalle.getInteresados();

        if (!isEmptyCollection(interesados)) {

            for (Interesado interesado : interesados) {

                Element rootElement = rootNode.addElement("De_Interesado");
                Element elem = null;

                if (!interesado.getIsRepresentante()) { // Interesado

                    // Tipo_Documento_Identificacion_Interesado
                    if (interesado.getTipoDocumentoIdentificacion() != null) {
                        elem = rootElement.addElement("Tipo_Documento_Identificacion_Interesado");
                        elem.addCDATA(String.valueOf(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion())));
                    }
                    // Documento_Identificacion_Interesado
                    if (StringUtils.isNotEmpty(interesado.getDocumento())) {
                        elem = rootElement.addElement("Documento_Identificacion_Interesado");
                        elem.addCDATA(interesado.getDocumento());
                    }
                    // Razon_Social_Interesado
                    if (StringUtils.isNotEmpty(interesado.getRazonSocial())) {
                        elem = rootElement.addElement("Razon_Social_Interesado");
                        elem.addCDATA(interesado.getRazonSocial());
                    }
                    // Nombre_Interesado
                    if (StringUtils.isNotEmpty(interesado.getNombre())) {
                        elem = rootElement.addElement("Nombre_Interesado");
                        elem.addCDATA(interesado.getNombre());
                    }
                    // Primer_Apellido_Interesado
                    if (StringUtils.isNotEmpty(interesado.getApellido1())) {
                        elem = rootElement.addElement("Primer_Apellido_Interesado");
                        elem.addCDATA(interesado.getApellido1());
                    }
                    // Segundo_Apellido_Interesado
                    if (StringUtils.isNotEmpty(interesado.getApellido2())) {
                        elem = rootElement.addElement("Segundo_Apellido_Interesado");
                        elem.addCDATA(interesado.getApellido2());
                    }

                    // Pais_Interesado
                    if (interesado.getPais() != null) {
                        elem = rootElement.addElement("Pais_Interesado");
                        elem.addCDATA(interesado.getPais().getCodigoPais().toString());
                    }
                    // Provincia_Interesado
                    if (interesado.getProvincia() != null) {
                        elem = rootElement.addElement("Provincia_Interesado");
                        elem.addCDATA(interesado.getProvincia().getCodigoProvincia().toString());
                    }
                    // Municipio_Interesado
                    if (interesado.getLocalidad() != null) {
                        elem = rootElement.addElement("Municipio_Interesado");
                        elem.addCDATA(interesado.getLocalidad().getCodigoLocalidad().toString());
                    }
                    // Direccion_Interesado
                    if (StringUtils.isNotEmpty(interesado.getDireccion())) {
                        elem = rootElement.addElement("Direccion_Interesado");
                        elem.addCDATA(interesado.getDireccion());
                    }
                    // Codigo_Postal_Interesado
                    if (StringUtils.isNotEmpty(interesado.getCp())) {
                        elem = rootElement.addElement("Codigo_Postal_Interesado");
                        elem.addCDATA(interesado.getCp());
                    }
                    // Correo_Electronico_Interesado
                    if (StringUtils.isNotEmpty(interesado.getEmail())) {
                        elem = rootElement.addElement("Correo_Electronico_Interesado");
                        elem.addCDATA(interesado.getEmail());
                    }
                    // Telefono_Contacto_Interesado
                    if (StringUtils.isNotEmpty(interesado.getTelefono())) {
                        elem = rootElement.addElement("Telefono_Contacto_Interesado");
                        elem.addCDATA(interesado.getTelefono());
                    }
                    // Direccion_Electronica_Habilitada_Interesado
                    if (StringUtils.isNotEmpty(interesado.getDireccionElectronica())) {
                        elem = rootElement.addElement("Direccion_Electronica_Habilitada_Interesado");
                        elem.addCDATA(interesado.getDireccionElectronica());
                    }
                    // Canal_Preferente_Comunicacion_Interesado
                    if (interesado.getCanal() != null) {
                        elem = rootElement.addElement("Canal_Preferente_Comunicacion_Interesado");
                        elem.addCDATA(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
                    }

                }

                if (interesado.getRepresentante() != null) { // Representante

                    Interesado representante = interesado.getRepresentante();

                    // Tipo_Documento_Identificacion_Representante
                    if (representante.getTipoDocumentoIdentificacion() != null) {
                        elem = rootElement.addElement("Tipo_Documento_Identificacion_Representante");
                        elem.addCDATA(String.valueOf(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(representante.getTipoDocumentoIdentificacion())));
                    }
                    // Documento_Identificacion_Representante
                    if (representante.getDocumento() != null) {
                        elem = rootElement.addElement("Documento_Identificacion_Representante");
                        elem.addCDATA(representante.getDocumento());
                    }
                    // Razon_Social_Representante
                    if (representante.getRazonSocial() != null) {
                        elem = rootElement.addElement("Razon_Social_Representante");
                        elem.addCDATA(representante.getRazonSocial());
                    }
                    // Nombre_Representante
                    if (representante.getNombre() != null) {
                        elem = rootElement.addElement("Nombre_Representante");
                        elem.addCDATA(representante.getNombre());
                    }
                    // Primer_Apellido_Representante
                    if (representante.getApellido1() != null) {
                        elem = rootElement.addElement("Primer_Apellido_Representante");
                        elem.addCDATA(representante.getApellido1());
                    }
                    // Segundo_Apellido_Representante
                    if (representante.getApellido2() != null) {
                        elem = rootElement.addElement("Segundo_Apellido_Representante");
                        elem.addCDATA(representante.getApellido2());
                    }

                    // Pais_Representante
                    if (representante.getPais() != null) {
                        elem = rootElement.addElement("Pais_Representante");
                        elem.addCDATA(representante.getPais().getCodigoPais().toString());
                    }
                    // Provincia_Representante
                    if (representante.getProvincia() != null) {
                        elem = rootElement.addElement("Provincia_Representante");
                        elem.addCDATA(representante.getProvincia().getCodigoProvincia().toString());
                    }
                    // Municipio_Representante
                    if (representante.getLocalidad() != null) {
                        elem = rootElement.addElement("Municipio_Representante");
                        elem.addCDATA(representante.getLocalidad().getCodigoLocalidad().toString());
                    }
                    // Direccion_Representante
                    if (StringUtils.isNotEmpty(representante.getDireccion())) {
                        elem = rootElement.addElement("Direccion_Representante");
                        elem.addCDATA(representante.getDireccion());
                    }
                    // Codigo_Postal_Representante
                    if (StringUtils.isNotEmpty(representante.getCp())) {
                        elem = rootElement.addElement("Codigo_Postal_Representante");
                        elem.addCDATA(representante.getCp());
                    }
                    // Correo_Electronico_Representante
                    if (StringUtils.isNotEmpty(representante.getEmail())) {
                        elem = rootElement.addElement("Correo_Electronico_Representante");
                        elem.addCDATA(representante.getEmail());
                    }
                    // Telefono_Contacto_Representante
                    if (StringUtils.isNotEmpty(representante.getTelefono())) {
                        elem = rootElement.addElement("Telefono_Contacto_Representante");
                        elem.addCDATA(representante.getTelefono());
                    }
                    // Direccion_Electronica_Habilitada_Representante
                    if (StringUtils.isNotEmpty(representante.getDireccionElectronica())) {
                        elem = rootElement.addElement("Direccion_Electronica_Habilitada_Representante");
                        elem.addCDATA(representante.getDireccionElectronica());
                    }
                    // Canal_Preferente_Comunicacion_Representante
                    if (representante.getCanal() != null) {
                        elem = rootElement.addElement("Canal_Preferente_Comunicacion_Representante");
                        elem.addCDATA(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(representante.getCanal()));
                    }
                }



                // Observaciones
                if (StringUtils.isNotEmpty(interesado.getObservaciones())) {
                    elem = rootElement.addElement("Observaciones");
                    elem.addCDATA(interesado.getObservaciones());
                }

            }
        } else {
            // De_Interesado es elemento obligatoria su presencia aunque sea vacio y no vengan interesados
            Element rootElement = rootNode.addElement("De_Interesado");
        }

        //log.info("Segmento De_Interesado creado");
    }

    /**
     * Añade el Segmento deAsunto al Fichero de Intercambio
     *
     * @param rootNode
     * @param registroDetalle
     */
    private void addDatosAsunto(Element rootNode, RegistroDetalle registroDetalle) {

        // De_Asunto
        Element rootElement = rootNode.addElement("De_Asunto");
        Element elem = null;

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

        //log.info("Segmento De_Asunto creado");

    }

    /**
     *
     * @param rootNode
     * @param registroDetalle
     * @throws Exception
     */
    private void addDatosAnexos(Element rootNode, RegistroDetalle registroDetalle) throws Exception  {

        int secuencia = 0;

        for (AnexoFull anexoFull : registroDetalle.getAnexosFull()) {

            final int modoFirma = anexoFull.getAnexo().getModoFirma();
            Anexo anexo = anexoFull.getAnexo();

            switch (modoFirma){

                case MODO_FIRMA_ANEXO_ATTACHED:
                    SignatureCustody sc = anexoFull.getSignatureCustody();

                    String identificadorFichero = generateIdentificadorFichero(registroDetalle.getIdentificadorIntercambio(), secuencia, sc.getName());
                    secuencia++;

                    crearAnexo(rootNode, sc.getName(),identificadorFichero, CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            anexo.getHash(),sc.getMime(),sc.getData(), identificadorFichero, anexo.getObservaciones());

                    break;

                case MODO_FIRMA_ANEXO_DETACHED:

                    DocumentCustody dc = anexoFull.getDocumentoCustody();

                    identificadorFichero = generateIdentificadorFichero(registroDetalle.getIdentificadorIntercambio(), secuencia, dc.getName());
                    secuencia++;

                    crearAnexo(rootNode, dc.getName(),identificadorFichero, CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            anexo.getHash(),dc.getMime(),dc.getData(), null,anexo.getObservaciones());

                    SignatureCustody scFirma = anexoFull.getSignatureCustody();
                    String identificadorFicheroFirmado = generateIdentificadorFichero(registroDetalle.getIdentificadorIntercambio(), secuencia, dc.getName());
                    secuencia++;

                    crearAnexo(rootNode, scFirma.getName(),identificadorFicheroFirmado, CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            anexo.getHash(),scFirma.getMime(),scFirma.getData(), identificadorFichero, anexo.getObservaciones());
                    break;
            }
        }

        //log.info("Segmento De_Anexo creado");
    }

    private void crearAnexo(Element rootNode, String nombreFichero, String identificadorFichero, String validezDocumento, String tipoDocumento, byte[] certificado,
                            String firma, byte[] timeStamp, byte[] validacionOCSPCertificado, byte[] hash, String tipoMime, byte[] anexo, String identificadorFicheroFirmado, String observaciones){

        Element elem;
        Element rootElement = rootNode.addElement("De_Anexo");

        // Nombre_Fichero_Anexado
        if (StringUtils.isNotBlank(nombreFichero)) {
            elem = rootElement.addElement("Nombre_Fichero_Anexado");
            elem.addCDATA(nombreFichero);
        }

        // Identificador_Fichero
        elem = rootElement.addElement("Identificador_Fichero");
        elem.addCDATA(identificadorFichero);


        // Validez_Documento
        elem = rootElement.addElement("Validez_Documento");
        if (validezDocumento == null) {
            elem.addCDATA(null);
        } else {
            elem.addCDATA(validezDocumento);
        }

        // Tipo_Documento
        elem = rootElement.addElement("Tipo_Documento");
        if (tipoDocumento == null) {
            elem.addCDATA(null);
        } else {
            elem.addCDATA(tipoDocumento);
        }

        // Certificado
        if (certificado != null) {
            elem = rootElement.addElement("Certificado");
            elem.addCDATA(getBase64String(certificado));
        }

        //Firma documento (propiedad Firma Documento del segmento)
        if (firma != null) {
            elem = rootElement.addElement("Firma_Documento");
            elem.addCDATA(firma);
        }

        // TimeStamp
        if (timeStamp != null) {
            elem = rootElement.addElement("TimeStamp");
            elem.addCDATA(getBase64String(timeStamp));
        }

        // Validacion_OCSP_Certificado
        if (validacionOCSPCertificado != null) {
            elem = rootElement.addElement("Validacion_OCSP_Certificado");
            elem.addCDATA(getBase64String(validacionOCSPCertificado));
        }

        // Hash
        if (hash != null) {
            elem = rootElement.addElement("Hash");
            elem.addCDATA(org.apache.commons.codec.binary.StringUtils.newStringUtf8(hash));
        }else{
            //log.info("hash es null");
        }

        // Tipo_MIME
        if (tipoMime != null && tipoMime.length() <= ANEXO_TIPOMIME_MAXLENGTH_SIR) {
            elem = rootElement.addElement("Tipo_MIME");
            elem.addCDATA(tipoMime);
        }

        //Anexo (propiedad Anexo del segmento)
        if (anexo != null) {
            elem = rootElement.addElement("Anexo");
            elem.addCDATA(getBase64String(anexo));
        }else{
            //log.info("anexo es null");
        }

        //Identificador Fichero Firmado
        elem = rootElement.addElement("Identificador_Documento_Firmado");
        elem.addCDATA(identificadorFicheroFirmado);

        // Observaciones
        if (StringUtils.isNotBlank(observaciones)) {
            elem = rootElement.addElement("Observaciones");
            elem.addCDATA(observaciones);
        }
    }

    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param rootNode
     * @param registro
     */
    private void addDatosInternosControl(Element rootNode, RegistroEntrada registro) {

        // De_Internos_Control
        Element rootElement = rootNode.addElement("De_Internos_Control");
        Element elem = null;

        RegistroDetalle registroDetalle = registro.getRegistroDetalle();

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
        if (StringUtils.isNotBlank(registro.getUsuario().getNombreCompleto())) {
            elem = rootElement.addElement("Nombre_Usuario");
            elem.addCDATA(registro.getUsuario().getNombreCompleto());
        }

        // Contacto_Usuario
        if (StringUtils.isNotBlank(registro.getUsuario().getUsuario().getEmail())) {
            elem = rootElement.addElement("Contacto_Usuario");
            elem.addCDATA(registro.getUsuario().getUsuario().getEmail());
        }

        // Identificador_Intercambio
        if (StringUtils.isNotBlank(registroDetalle.getIdentificadorIntercambio())) {
            elem = rootElement.addElement("Identificador_Intercambio");
            elem.addCDATA(registroDetalle.getIdentificadorIntercambio());
        }

        // Aplicacion_Version_Emisora
        elem = rootElement.addElement("Aplicacion_Version_Emisora");
        elem.addCDATA(Versio.VERSIO_SIR);

        // Tipo_Anotacion
        elem = rootElement.addElement("Tipo_Anotacion");
        elem.addCDATA(registroDetalle.getTipoAnotacion());

        elem = rootElement.addElement("Descripcion_Tipo_Anotacion");
        elem.addCDATA(registroDetalle.getDecodificacionTipoAnotacion());

        // Tipo_Registro
        elem = rootElement.addElement("Tipo_Registro");
        elem.addCDATA(TipoRegistro.ENTRADA.getValue());

        // Documentacion_Fisica
        elem = rootElement.addElement("Documentacion_Fisica");
        elem.addCDATA(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));


        // Observaciones_Apunte
        if (StringUtils.isNotBlank(registroDetalle.getObservaciones())) {
            elem = rootElement.addElement("Observaciones_Apunte");
            elem.addCDATA(registroDetalle.getObservaciones());
        }

        // Indicador_Prueba
        elem = rootElement.addElement("Indicador_Prueba");
        elem.addCDATA(registroDetalle.getIndicadorPrueba().getValue());

        // Codigo_Entidad_Registral_Inicio
        elem = rootElement.addElement("Codigo_Entidad_Registral_Inicio");
        elem.addCDATA(obtenerCodigoOficinaOrigen(registro));


        // Decodificacion_Entidad_Registral_Inicio
        elem = rootElement.addElement("Decodificacion_Entidad_Registral_Inicio");
        elem.addCDATA(obtenerDenominacionOficinaOrigen(registro));

    }

    /**
     * Añade el Segmento deInternosControl al Fichero de Intercambio
     *
     * @param rootNode
     * @param registroDetalle
     */
    private void addDatosformularioGenerico(Element rootNode, RegistroDetalle registroDetalle) {

        // De_Formulario_Generico
        Element rootElement = rootNode.addElement("De_Formulario_Generico");
        Element elem = null;

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
     * Crea el xml de un Mensaje de Control para ser enviado
     *
     * @param mensaje Información del mensaje.
     * @return XML de mensaje en formato SICRES 3.0
     */
    public String createXMLMensaje(MensajeControl mensaje) {

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
                msg.setTipo_Mensaje(mensaje.getTipoMensaje());
            }

            // Indicador de prueba
            if (mensaje.getIndicadorPrueba() != null) {
                msg.setIndicador_Prueba(Indicador_PruebaType.fromValue(mensaje.getIndicadorPrueba().getValue()));
            }

            Marshaller.marshal(msg,stringWriter);

        } catch (Exception e) {
            log.error("Error al crear el XML del mensaje", e);
            throw new SIRException(e.getMessage());
        }

        return stringWriter.toString();
    }

    /**
     * Convierte el xml recibido en un {@link es.caib.regweb3.model.sir.MensajeControl}
     * @param xml
     * @return
     */
    public MensajeControl parseXMLMensaje(String xml) {

        MensajeControl mensaje = null;

        try {

            De_Mensaje de_mensaje = (De_Mensaje) Unmarshaller.unmarshal(De_Mensaje.class,new StringReader(xml));


            if (de_mensaje != null) {

                mensaje = new MensajeControl(RegwebConstantes.TIPO_COMUNICACION_RECIBIDO);
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
                    mensaje.setTipoMensaje(tipoMensaje);
                }

                // Indicador de prueba
                IndicadorPrueba indicadorPrueba = IndicadorPrueba.getIndicadorPrueba(de_mensaje.getIndicador_Prueba().value());
                if ((indicadorPrueba != null) && StringUtils.isNotBlank(indicadorPrueba.getValue())) {
                    mensaje.setIndicadorPrueba(indicadorPrueba);
                }
            }

        } catch (Throwable e) {
            log.error("Error al parsear el XML del mensaje: [" + xml + "]", e);
            throw new ValidacionException(Errores.ERROR_0037, "Error al parsear el mensaje de control", e);
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
    private String generateIdentificadorFichero(String identificadorIntercambio, int secuencia, String fileName) {

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
    private boolean validarCodigoEntidadRegistral(String codigoEntidadRegistral) {

        OficinaTF oficinaTF = null;

        if (StringUtils.length(codigoEntidadRegistral) > LONGITUD_CODIGO_ENTIDAD_REGISTRAL) {
            log.info("Tamaño CodigoEntidadRegistral demasiado largo");
            return false;
            //throw new IllegalArgumentException("CodigoEntidadRegistral demasiado largo");
        }

        try {
            oficinaTF = oficinasService.obtenerOficina(codigoEntidadRegistral,null,null);

        } catch (Exception e) {
            log.info("Error en validarCodigoEntidadRegistral: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }

        if(oficinaTF == null){
            log.info("Oficina "+codigoEntidadRegistral+" no encontrada en Dir3");
            return false;
            //throw new IllegalArgumentException("CodigoEntidadRegistral "+codigoEntidadRegistral+" no encontrada en Dir3");
        }

        return true;
    }

    /**
     * Valida contra el DIR3 el codigoUnidadTramitacion
     * @param codigoUnidadTramitacion
     * @return
     */
    private boolean validarCodigoUnidadTramitacion(String codigoUnidadTramitacion) {

        UnidadTF unidadTF = null;

        if (StringUtils.length(codigoUnidadTramitacion) > LONGITUD_CODIGO_UNIDAD_TRAMITACION) {
            throw new IllegalArgumentException("Tamaño CODIGO_UNIDAD_TRAMITACION demasiado largo");
        }

        try {
            unidadTF = unidadesService.buscarUnidad(codigoUnidadTramitacion);

        } catch (Exception e) {
            log.info("Error en validarCodigoUnidadTramitacion: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }

        if(unidadTF == null){
            throw new IllegalArgumentException("Unidad "+codigoUnidadTramitacion+" no encontrada en Dir3");
        }

        return true;
    }


    /**
     * Realizamos una validación de los campos del xml que deben estar en base64 en caso de estar presentes
     *
     * @param xml
     */
    private void validateBase64Fields(String xml) {
        XPathReaderUtil reader = null;

        // Procesamos el xml para procesar las peticiones xpath
        try {
            reader = new XPathReaderUtil(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.error("Error al parsear el XML del fichero de intercambio en la validación campos en Base64");
            throw new ValidacionException(Errores.ERROR_0037,e.getMessage(),e);
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
                        throw new ValidacionException(Errores.ERROR_0037,"El campo '"+fieldName+"', no esta codificado en Base64.", null);
                    }
                }
            }
        }
    }

    /**
     * Obtiene el código Oficina de Origen dependiendo de si es interna o externa
     *
     * @param re
     * @return
     * @throws Exception
     */
    private String obtenerCodigoOficinaOrigen(RegistroEntrada re) {
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
    private String obtenerDenominacionOficinaOrigen(RegistroEntrada re) {
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



    private static boolean isEmptyCollection(Collection collection) {
      return (collection == null || collection.isEmpty());
    }
    
    
}