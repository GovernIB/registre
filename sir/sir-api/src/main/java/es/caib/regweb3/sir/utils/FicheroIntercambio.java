package es.caib.regweb3.sir.utils;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.AsientoRegistralSir;
import es.caib.regweb3.model.InteresadoSir;
import es.caib.regweb3.model.utils.DocumentacionFisica;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.model.utils.TipoRegistro;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.persistence.utils.ArchivoManager;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.sir.api.schema.*;
import es.caib.regweb3.sir.api.schema.types.Documentacion_FisicaType;
import es.caib.regweb3.sir.api.schema.types.Indicador_PruebaType;
import es.caib.regweb3.sir.api.schema.types.Tipo_RegistroType;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.*;
import es.caib.regweb3.utils.MimeTypeUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Agrupa funcionalidades comunes para interactuar con {@link Fichero_Intercambio_SICRES_3}, la clase que representa
 * el FicheroIntercambio.xsd
 */
public class FicheroIntercambio {

    public final Logger log = Logger.getLogger(getClass());

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * Información del fichero de intercambio
     */
    private Fichero_Intercambio_SICRES_3 ficheroIntercambio = null;

    /**
     * Constructor.
     */
    public FicheroIntercambio() {
        super();
    }

    public Fichero_Intercambio_SICRES_3 getFicheroIntercambio() {
        return ficheroIntercambio;
    }

    public void setFicheroIntercambio(
            Fichero_Intercambio_SICRES_3 ficheroIntercambio) {
        this.ficheroIntercambio = ficheroIntercambio;
    }

    public String getCodigoEntidadRegistralOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getCodigo_Entidad_Registral_Origen();
        }

        return null;
    }

    public String getDescripcionEntidadRegistralOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getDecodificacion_Entidad_Registral_Origen();
        }

        return null;
    }

    public String getCodigoUnidadTramitacionOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getCodigo_Unidad_Tramitacion_Origen();
        }

        return null;
    }

    public String getDescripcionUnidadTramitacionOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getDecodificacion_Unidad_Tramitacion_Origen();
        }

        return null;
    }

    public String getCodigoEntidadRegistralDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getCodigo_Entidad_Registral_Destino();
        }

        return null;
    }

    public String getDescripcionEntidadRegistralDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getDecodificacion_Entidad_Registral_Destino();
        }

        return null;
    }

    public String getCodigoUnidadTramitacionDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getCodigo_Unidad_Tramitacion_Destino();
        }

        return null;
    }

    public String getDescripcionUnidadTramitacionDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getDecodificacion_Unidad_Tramitacion_Destino();
        }

        return null;
    }

    public String getNumeroRegistro() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getNumero_Registro_Entrada();
        }

        return null;
    }

    public String getFechaRegistroXML() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getFecha_Hora_Entrada();
        }

        return null;
    }

    public Date getFechaRegistro() {
        String fechaRegistro = getFechaRegistroXML();
        if (StringUtils.isNotBlank(fechaRegistro)) {
            try {
                return SDF.parse(fechaRegistro);
            } catch (ParseException e) {
                log.error("Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
            }
        }

        return null;
    }

    public byte[] getTimestampRegistro() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getTimestamp_Entrada();
        }

        return null;
    }

    public String getCodigoAsunto() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getCodigo_Asunto_Segun_Destino();
        }

        return null;
    }

    public String getNumeroExpediente() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getNumero_Expediente();
        }

        return null;
    }

    public String getReferenciaExterna() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getReferencia_Externa();
        }

        return null;
    }

    public String getResumen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getResumen();
        }

        return null;
    }

    public String getCodigoEntidadRegistralInicio() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getCodigo_Entidad_Registral_Inicio();
        }

        return null;
    }

    public String getDescripcionEntidadRegistralInicio() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getDecodificacion_Entidad_Registral_Inicio();
        }

        return null;
    }

    public String getNombreUsuario() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getNombre_Usuario();
        }

        return null;
    }

    public String getContactoUsuario() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getContacto_Usuario();
        }

        return null;
    }

    public String getTipoTransporteXML() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getTipo_Transporte_Entrada();
        }

        return null;
    }

    public TipoTransporte getTipoTransporte() {
        String tipoTransporte = getTipoTransporteXML();
        if (StringUtils.isNotBlank(tipoTransporte)) {
            return TipoTransporte.getTipoTransporte(tipoTransporte);
        }

        return null;
    }

    public Long getTipoTransporteEntrada() {
        String tipoTransporte = getFicheroIntercambio().getDe_Internos_Control().getTipo_Transporte_Entrada();
        if (StringUtils.isNotBlank(tipoTransporte)) {
            return RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(tipoTransporte);
        }

        return null;
    }


    public String getNumeroTransporte() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getNumero_Transporte_Entrada();
        }

        return null;
    }


    /**
     * Obtiene el identificador de intercambio.
     *
     * @return Identificador de intercambio.
     */
    public String getIdentificadorIntercambio() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getIdentificador_Intercambio();
        }

        return null;
    }

    /**
     * Obtiene la información de la aplicación emisora.
     *
     * @return Aplicación emisora.
     */
    public String getAplicacionEmisora() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getAplicacion_Version_Emisora();
        }

        return null;
    }

    public String getTipoAnotacionXML() {

        if (getFicheroIntercambio() != null) {
            De_Internos_Control de_Internos_Control = getFicheroIntercambio().getDe_Internos_Control();
            if (de_Internos_Control != null) {
                return de_Internos_Control.getTipo_Anotacion();
            }
        }

        return null;
    }

    /**
     * Obtiene el tipo de anotación.
     *
     * @return Tipo de anotación.
     */
    public String getTipoAnotacion() {

        String tipoAnotacion = getTipoAnotacionXML();
        if (StringUtils.isNotBlank(tipoAnotacion)) {
            return TipoAnotacion.getTipoAnotacionValue(tipoAnotacion);
        }

        return null;
    }

    public String getDescripcionTipoAnotacion() {

        if (getFicheroIntercambio() != null) {
            De_Internos_Control de_Internos_Control = getFicheroIntercambio().getDe_Internos_Control();
            if (de_Internos_Control != null) {
                return de_Internos_Control.getDescripcion_Tipo_Anotacion();
            }
        }

        return null;
    }

    public TipoRegistro getTipoRegistro() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)){

            Tipo_RegistroType tipoRegistro = getFicheroIntercambio().getDe_Internos_Control().getTipo_Registro();
            if ((tipoRegistro != null) && StringUtils.isNotBlank(tipoRegistro.value())) {
                return TipoRegistro.getTipoRegistro(tipoRegistro.value());
            }
        }

        return null;
    }

    public String getObservacionesApunte() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getObservaciones_Apunte();
        }

        return null;
    }


    public String getExpone() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Formulario_Generico() != null)) {
            return getFicheroIntercambio().getDe_Formulario_Generico().getExpone();
        }

        return null;
    }

    public String getSolicita() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Formulario_Generico() != null)) {
            return getFicheroIntercambio().getDe_Formulario_Generico().getSolicita();
        }

        return null;
    }

    /**
     * Crea un AsientoRegistralSir, a partir del FicheroIntercambio.
     *
     * @return Información del asientoRegistralSir registral.
     */
    public AsientoRegistralSir getAsientoRegistralSir(WebServicesMethodsLocal webServicesMethodsEjb) {

        AsientoRegistralSir asientoRegistralSir = null;

        if (getFicheroIntercambio() != null) {

            asientoRegistralSir = new AsientoRegistralSir();

            // Segmento De_Origen_o_Remitente
            De_Origen_o_Remitente de_Origen_o_Remitente = getFicheroIntercambio().getDe_Origen_o_Remitente();
            if (de_Origen_o_Remitente != null) {

                asientoRegistralSir.setCodigoEntidadRegistralOrigen(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen());

                if (!StringUtils.isEmpty(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen())) {
                    asientoRegistralSir.setDecodificacionEntidadRegistralOrigen(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen());
                } else {
                    asientoRegistralSir.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen(), RegwebConstantes.OFICINA));
                }

                asientoRegistralSir.setCodigoUnidadTramitacionOrigen(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen());

                if (!StringUtils.isEmpty(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen())) {
                    asientoRegistralSir.setDecodificacionUnidadTramitacionOrigen(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen());
                } else {
                    asientoRegistralSir.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen(), RegwebConstantes.UNIDAD));
                }


                asientoRegistralSir.setNumeroRegistro(de_Origen_o_Remitente.getNumero_Registro_Entrada());
                asientoRegistralSir.setTimestampRegistro(de_Origen_o_Remitente.getTimestamp_Entrada());

                String fechaRegistro = de_Origen_o_Remitente.getFecha_Hora_Entrada();
                if (StringUtils.isNotBlank(fechaRegistro)) {
                    try {
                        asientoRegistralSir.setFechaRegistro(SDF.parse(fechaRegistro));
                    } catch (ParseException e) {
                        log.error("Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
                        throw new ValidacionException(Errores.ERROR_0037, e);
                    }
                }
            }

            // Segmento De_Destino
            De_Destino de_Destino = getFicheroIntercambio().getDe_Destino();
            if (de_Destino != null) {

                asientoRegistralSir.setCodigoEntidadRegistralDestino(de_Destino.getCodigo_Entidad_Registral_Destino());
                if (!StringUtils.isEmpty(de_Destino.getDecodificacion_Entidad_Registral_Destino())) {
                    asientoRegistralSir.setDecodificacionEntidadRegistralDestino(de_Destino.getDecodificacion_Entidad_Registral_Destino());
                } else {
                    asientoRegistralSir.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(de_Destino.getCodigo_Entidad_Registral_Destino(), RegwebConstantes.OFICINA));
                }

                if (!StringUtils.isEmpty(de_Destino.getCodigo_Unidad_Tramitacion_Destino())) {
                    asientoRegistralSir.setCodigoUnidadTramitacionDestino(de_Destino.getCodigo_Unidad_Tramitacion_Destino());
                    if (!StringUtils.isEmpty(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino())) {
                        asientoRegistralSir.setDecodificacionUnidadTramitacionDestino(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino());
                    } else {
                        asientoRegistralSir.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(de_Destino.getCodigo_Unidad_Tramitacion_Destino(), RegwebConstantes.UNIDAD));
                    }
                }

            }

            // Segmento De_Asunto de_Asunto
            De_Asunto de_Asunto = getFicheroIntercambio().getDe_Asunto();
            if (de_Asunto != null) {
                asientoRegistralSir.setResumen(de_Asunto.getResumen());
                asientoRegistralSir.setCodigoAsunto(de_Asunto.getCodigo_Asunto_Segun_Destino());
                asientoRegistralSir.setReferenciaExterna(de_Asunto.getReferencia_Externa());
                asientoRegistralSir.setNumeroExpediente(de_Asunto.getNumero_Expediente());
            }

            // Segmento De_Internos_Control
            De_Internos_Control de_Internos_Control = getFicheroIntercambio().getDe_Internos_Control();
            if (de_Internos_Control != null) {

                asientoRegistralSir.setIdentificadorIntercambio(de_Internos_Control.getIdentificador_Intercambio());
                asientoRegistralSir.setAplicacion(de_Internos_Control.getAplicacion_Version_Emisora());
                asientoRegistralSir.setTipoAnotacion(getTipoAnotacion());
                asientoRegistralSir.setDecodificacionTipoAnotacion(de_Internos_Control.getDescripcion_Tipo_Anotacion());
                asientoRegistralSir.setNumeroTransporte(de_Internos_Control.getNumero_Transporte_Entrada());
                asientoRegistralSir.setNombreUsuario(de_Internos_Control.getNombre_Usuario());
                asientoRegistralSir.setContactoUsuario(de_Internos_Control.getContacto_Usuario());
                asientoRegistralSir.setObservacionesApunte(de_Internos_Control.getObservaciones_Apunte());

                asientoRegistralSir.setCodigoEntidadRegistralInicio(de_Internos_Control.getCodigo_Entidad_Registral_Inicio());
                if (!StringUtils.isEmpty(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio())) {
                    asientoRegistralSir.setDecodificacionEntidadRegistralInicio(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio());
                } else {
                    asientoRegistralSir.setDecodificacionEntidadRegistralInicio(Dir3CaibUtils.denominacion(de_Internos_Control.getCodigo_Entidad_Registral_Inicio(), RegwebConstantes.OFICINA));
                }


                // Tipo de transporte
                String tipoTransporte = de_Internos_Control.getTipo_Transporte_Entrada();
                if (StringUtils.isNotBlank(tipoTransporte)) {
                    asientoRegistralSir.setTipoTransporte(TipoTransporte.getTipoTransporteValue(tipoTransporte));
                }

                // Tipo de registro
                Tipo_RegistroType tipo_Registro = de_Internos_Control.getTipo_Registro();
                if ((tipo_Registro != null) && StringUtils.isNotBlank(tipo_Registro.value())) {
                    asientoRegistralSir.setTipoRegistro(TipoRegistro.getTipoRegistro(tipo_Registro.value()));
                }

                // Documentación física
                Documentacion_FisicaType documentacion_Fisica = de_Internos_Control.getDocumentacion_Fisica();
                if ((documentacion_Fisica != null) && StringUtils.isNotBlank(documentacion_Fisica.value())) {
                    asientoRegistralSir.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisicaValue(documentacion_Fisica.value()));
                }

                // Indicador de prueba
                Indicador_PruebaType indicadorPrueba = de_Internos_Control.getIndicador_Prueba();
                if ((indicadorPrueba != null) && StringUtils.isNotBlank(indicadorPrueba.value())){
                    asientoRegistralSir.setIndicadorPrueba(IndicadorPrueba.getIndicadorPrueba(indicadorPrueba.value()));
                }

            }

            // Segmento De_Formulario_Generico
            De_Formulario_Generico de_Formulario_Generico = getFicheroIntercambio().getDe_Formulario_Generico();
            if (de_Formulario_Generico != null) {
                asientoRegistralSir.setExpone(de_Formulario_Generico.getExpone());
                asientoRegistralSir.setSolicita(de_Formulario_Generico.getSolicita());
            }

            // Segmento De_Interesado
            De_Interesado[] de_Interesados = getFicheroIntercambio().getDe_Interesado();
            if (ArrayUtils.isNotEmpty(de_Interesados)) {
                for (De_Interesado de_Interesado : de_Interesados) {
                    if (de_Interesado != null) {

                        // Si se trata de una Salida y no tiene Interesados
                        if(getTipoRegistro().equals(TipoRegistro.SALIDA) &&
                                StringUtils.isBlank(de_Interesado.getRazon_Social_Interesado())
                                || (StringUtils.isBlank(de_Interesado.getNombre_Interesado()) && StringUtils.isBlank(de_Interesado.getPrimer_Apellido_Interesado()))){

                            // Creamos uno a partir de la Entidad destino
                            asientoRegistralSir.getInteresados().add(crearInteresadoJuridico());

                        }else{

                            InteresadoSir interesado = new InteresadoSir();

                            // Información del interesado
                            interesado.setDocumentoIdentificacionInteresado(de_Interesado.getDocumento_Identificacion_Interesado());
                            interesado.setRazonSocialInteresado(de_Interesado.getRazon_Social_Interesado());
                            interesado.setNombreInteresado(de_Interesado.getNombre_Interesado());
                            interesado.setPrimerApellidoInteresado(de_Interesado.getPrimer_Apellido_Interesado());
                            interesado.setSegundoApellidoInteresado(de_Interesado.getSegundo_Apellido_Interesado());
                            interesado.setCodigoPaisInteresado(de_Interesado.getPais_Interesado());
                            interesado.setCodigoProvinciaInteresado(de_Interesado.getProvincia_Interesado());
                            interesado.setCodigoMunicipioInteresado(de_Interesado.getMunicipio_Interesado());
                            interesado.setDireccionInteresado(de_Interesado.getDireccion_Interesado());
                            interesado.setCodigoPostalInteresado(de_Interesado.getCodigo_Postal_Interesado());
                            interesado.setCorreoElectronicoInteresado(de_Interesado.getCorreo_Electronico_Interesado());
                            interesado.setTelefonoInteresado(de_Interesado.getTelefono_Contacto_Interesado());
                            interesado.setDireccionElectronicaHabilitadaInteresado(de_Interesado.getDireccion_Electronica_Habilitada_Interesado());

                            String tipoDocumento = de_Interesado.getTipo_Documento_Identificacion_Interesado();
                            if (StringUtils.isNotBlank(tipoDocumento)) {
                                interesado.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
                            }

                            String canalPreferente = de_Interesado.getCanal_Preferente_Comunicacion_Interesado();
                            if (StringUtils.isNotBlank(canalPreferente)) {
                                interesado.setCanalPreferenteComunicacionInteresado(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
                            }

                            // Información del representante
                            interesado.setDocumentoIdentificacionRepresentante(de_Interesado.getDocumento_Identificacion_Representante());
                            interesado.setRazonSocialRepresentante(de_Interesado.getRazon_Social_Representante());
                            interesado.setNombreRepresentante(de_Interesado.getNombre_Representante());
                            interesado.setPrimerApellidoRepresentante(de_Interesado.getPrimer_Apellido_Representante());
                            interesado.setSegundoApellidoRepresentante(de_Interesado.getSegundo_Apellido_Representante());
                            interesado.setCodigoPaisRepresentante(de_Interesado.getPais_Representante());
                            interesado.setCodigoProvinciaRepresentante(de_Interesado.getProvincia_Representante());
                            interesado.setCodigoMunicipioRepresentante(de_Interesado.getMunicipio_Representante());
                            interesado.setDireccionRepresentante(de_Interesado.getDireccion_Representante());
                            interesado.setCodigoPostalRepresentante(de_Interesado.getCodigo_Postal_Representante());
                            interesado.setCorreoElectronicoRepresentante(de_Interesado.getCorreo_Electronico_Representante());
                            interesado.setTelefonoRepresentante(de_Interesado.getTelefono_Contacto_Representante());
                            interesado.setDireccionElectronicaHabilitadaRepresentante(de_Interesado.getDireccion_Electronica_Habilitada_Representante());

                            tipoDocumento = de_Interesado.getTipo_Documento_Identificacion_Representante();
                            if (StringUtils.isNotBlank(tipoDocumento)) {
                                interesado.setTipoDocumentoIdentificacionRepresentante(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
                            }

                            canalPreferente = de_Interesado.getCanal_Preferente_Comunicacion_Representante();
                            if (StringUtils.isNotBlank(canalPreferente)) {
                                interesado.setCanalPreferenteComunicacionRepresentante(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
                            }

                            interesado.setObservaciones(de_Interesado.getObservaciones());

                            asientoRegistralSir.getInteresados().add(interesado);
                        }




                    }
                }
            }

            // Segmento De_Anexos
            De_Anexo[] de_Anexos = getFicheroIntercambio().getDe_Anexo();
            if (ArrayUtils.isNotEmpty(de_Anexos)) {
                for (De_Anexo de_Anexo : de_Anexos) {
                    if (de_Anexo != null) {
                        AnexoSir anexo = new AnexoSir();

                        anexo.setNombreFichero(de_Anexo.getNombre_Fichero_Anexado());
                        anexo.setIdentificadorFichero(de_Anexo.getIdentificador_Fichero());
                        anexo.setIdentificadorDocumentoFirmado(de_Anexo.getIdentificador_Documento_Firmado());
                        anexo.setCertificado(de_Anexo.getCertificado());
                        anexo.setFirma(de_Anexo.getFirma_Documento());
                        anexo.setTimestamp(de_Anexo.getTimeStamp());
                        anexo.setValidacionOCSPCertificado(de_Anexo.getValidacion_OCSP_Certificado());
                        anexo.setHash(de_Anexo.getHash());
                        //Si el tipo mime es null, se obtiene del nombre del fichero
                        if (de_Anexo.getTipo_MIME() == null || de_Anexo.getTipo_MIME().isEmpty()) {
                            anexo.setTipoMIME(MimeTypeUtils.getMimeTypeFileName(de_Anexo.getNombre_Fichero_Anexado()));
                        } else {
                            anexo.setTipoMIME(de_Anexo.getTipo_MIME());
                        }

                        try {
                            ArchivoManager am = new ArchivoManager(webServicesMethodsEjb, de_Anexo.getNombre_Fichero_Anexado(), anexo.getTipoMIME(), de_Anexo.getAnexo());
                            anexo.setAnexo(am.prePersist());
                        } catch (Exception e) {
                            log.info("Error al crear el Anexo en el sistema de archivos", e);
                            throw new ServiceException(Errores.ERROR_0045,e);
                        }

                        anexo.setObservaciones(de_Anexo.getObservaciones());

                        String validezDocumento = de_Anexo.getValidez_Documento();
                        if (StringUtils.isNotBlank(validezDocumento)) {
                            anexo.setValidezDocumento(ValidezDocumento.getValidezDocumentoValue(validezDocumento));
                        }

                        String tipoDocumento = de_Anexo.getTipo_Documento();
                        if (StringUtils.isNotBlank(tipoDocumento)) {
                            anexo.setTipoDocumento(TipoDocumento.getTipoDocumentoValue(tipoDocumento));
                        }

                        asientoRegistralSir.getAnexos().add(anexo);
                    }
                }
            }
        }

        return asientoRegistralSir;
    }


    public String marshallObject() {
        String result;
        JAXBContext jc = null;
        StringWriter sw = new StringWriter();

        try {
            jc = JAXBContext.newInstance(Fichero_Intercambio_SICRES_3.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


            m.marshal(ficheroIntercambio, sw);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        result = sw.toString();

        return result;
    }


    /**
     * Obtiene el número de anexos.
     *
     * @return Número de anexos
     */
    public int countAnexos() {

        if (getFicheroIntercambio() != null) {
            return getFicheroIntercambio().getDe_AnexoCount();
        }

        return 0;
    }

    /**
     * Establece el contenido del anexo.
     *
     * @param secuencia Ordinal del anexo
     * @param contenido Contenido del anexo.
     */
    public void setContenidoAnexo(int secuencia, byte[] contenido) {
        if (secuencia < getFicheroIntercambio().getDe_AnexoCount()) {
            getFicheroIntercambio().getDe_Anexo()[secuencia].setAnexo(contenido);
        }
    }

    /**
     * Obtiene el contenido de un anexo.
     *
     * @param secuencia Ordinal del anexo.
     * @return Contenido del anexo.
     */
    public byte[] getContenidoAnexo(int secuencia) {
        if (secuencia < getFicheroIntercambio().getDe_AnexoCount()) {
            return getFicheroIntercambio().getDe_Anexo()[secuencia].getAnexo();
        } else {
            return null;
        }
    }

    /**
     * Crea un Interesado tipo Persona Juridica a partir del Código Unidad De Gestión de destino o si no está informado,
     * a partir del Código Entidad Registral de destino
     * @return
     */
    private InteresadoSir crearInteresadoJuridico(){

        InteresadoSir interesadoSalida = new InteresadoSir();

        if(StringUtils.isNotBlank(getCodigoUnidadTramitacionDestino())){

            interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
            interesadoSalida.setDocumentoIdentificacionInteresado(getCodigoUnidadTramitacionDestino());

            if(StringUtils.isNotBlank(getDescripcionUnidadTramitacionDestino())){
                interesadoSalida.setRazonSocialInteresado(getDescripcionUnidadTramitacionDestino());
            }else{
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(getCodigoUnidadTramitacionDestino(),RegwebConstantes.UNIDAD));

            }


        }else{
            try {
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();

                OficinaTF oficinaTF = oficinasService.obtenerOficina(getCodigoEntidadRegistralDestino(),null,null);

                interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
                interesadoSalida.setDocumentoIdentificacionInteresado(oficinaTF.getCodUoResponsable());
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(oficinaTF.getCodUoResponsable(),RegwebConstantes.UNIDAD));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return interesadoSalida;
    }


    @Override
    public String toString() {
        return "FicheroIntercambio{" +
                "ficheroIntercambio=" + ficheroIntercambio +
                '}';
    }
}
