package es.caib.regweb3.sir.ws.api.utils;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.persistence.ejb.CatLocalidadLocal;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.sir.api.schema.*;
import es.caib.regweb3.sir.api.schema.types.Documentacion_FisicaType;
import es.caib.regweb3.sir.api.schema.types.Tipo_RegistroType;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.*;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FicheroIntercambio {

    public final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;


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
    public TipoAnotacion getTipoAnotacion() {

        String tipoAnotacion = getTipoAnotacionXML();
        if (StringUtils.isNotBlank(tipoAnotacion)) {
            return TipoAnotacion.getTipoAnotacion(tipoAnotacion);
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
     * Obtiene la información del asientoRegistral.
     *
     * @return Información del asiento registral.
     */
    public PreRegistro getPreRegistro() {

        PreRegistro preRegistro = new PreRegistro();
        preRegistro.setEstado(RegwebConstantes.ESTADO_PREREGISTRO_PENDIENTE_PROCESAR);
        RegistroDetalle registroDetalle = new RegistroDetalle();
        List<Interesado> interesados = new ArrayList<Interesado>();
        List<Anexo> anexos = new ArrayList<Anexo>();

        preRegistro.setEstado(RegwebConstantes.ESTADO_PREREGISTRO_PENDIENTE_PROCESAR);

        if (getFicheroIntercambio() != null) {

            // DeOrigenORemitente
            De_Origen_o_Remitente de_Origen_o_Remitente = getFicheroIntercambio().getDe_Origen_o_Remitente();
            if (de_Origen_o_Remitente != null) {

                preRegistro.setCodigoEntidadRegistralOrigen(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen());

                if (!StringUtils.isEmpty(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen())) {
                    preRegistro.setDecodificacionEntidadRegistralOrigen(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen());
                } else {
                    preRegistro.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen(), "oficina"));
                }

                // Oficina Origen
                registroDetalle.setOficinaOrigenExternoCodigo(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen());
                registroDetalle.setOficinaOrigenExternoDenominacion(preRegistro.getDecodificacionEntidadRegistralOrigen());

                preRegistro.setCodigoUnidadTramitacionOrigen(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen());

                if (!StringUtils.isEmpty(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen())) {
                    preRegistro.setCodigoUnidadTramitacionOrigen(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen());

                    if (!StringUtils.isEmpty(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen())) {
                        preRegistro.setDecodificacionUnidadTramitacionOrigen(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen());
                    } else {
                        preRegistro.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen(), "unidad"));
                    }
                }

                registroDetalle.setNumeroRegistroOrigen(de_Origen_o_Remitente.getNumero_Registro_Entrada());
                //preRegistro.setTimestampRegistro(de_Origen_o_Remitente.getTimestampEntrada());

                String fechaRegistro = de_Origen_o_Remitente.getFecha_Hora_Entrada();
                if (StringUtils.isNotBlank(fechaRegistro)) {
                    try {
                        registroDetalle.setFechaOrigen(SDF.parse(fechaRegistro));
                    } catch (ParseException e) {
                        log.error("Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
                        throw new ValidacionException(Errores.ERROR_0037, e);
                    }
                }
            }

            // DeDestino
            De_Destino de_Destino = getFicheroIntercambio().getDe_Destino();
            if (de_Destino != null) {

                preRegistro.setCodigoEntidadRegistralDestino(de_Destino.getCodigo_Entidad_Registral_Destino());
                if (!StringUtils.isEmpty(de_Destino.getDecodificacion_Entidad_Registral_Destino())) {
                    preRegistro.setDecodificacionEntidadRegistralDestino(de_Destino.getDecodificacion_Entidad_Registral_Destino());
                } else {
                    preRegistro.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(de_Destino.getCodigo_Entidad_Registral_Destino(), "oficina"));
                }

                if (!StringUtils.isEmpty(de_Destino.getCodigo_Unidad_Tramitacion_Destino())) {
                    preRegistro.setCodigoUnidadTramitacionDestino(de_Destino.getCodigo_Unidad_Tramitacion_Destino());
                    if (!StringUtils.isEmpty(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino())) {
                        preRegistro.setDecodificacionUnidadTramitacionDestino(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino());
                    } else {
                        preRegistro.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(de_Destino.getCodigo_Unidad_Tramitacion_Destino(), "unidad"));
                    }
                }

            }

            // DeAsunto
            De_Asunto de_Asunto = getFicheroIntercambio().getDe_Asunto();
            if (de_Asunto != null) {
                registroDetalle.setExtracto(de_Asunto.getResumen());
                //registroDetalle.setCodigoAsunto(de_Asunto.getCodigoAsuntoSegunDestino());// todo: Como mapear el CodigoAsunto
                registroDetalle.setReferenciaExterna(de_Asunto.getReferencia_Externa());
                registroDetalle.setExpediente(de_Asunto.getNumero_Expediente());
            }

            // DeInternosControl
            De_Internos_Control de_Internos_Control = getFicheroIntercambio().getDe_Internos_Control();
            if (de_Internos_Control != null) {

                preRegistro.setIdIntercambio(getIdentificadorIntercambio());
                preRegistro.setIndicadorPrueba(de_Internos_Control.getIndicador_Prueba().value());
                registroDetalle.setAplicacion(de_Internos_Control.getAplicacion_Version_Emisora());
                preRegistro.setTipoAnotacion(RegwebConstantes.TIPO_ANOTACION_BY_CODIGO.get(getTipoAnotacionXML()));
                preRegistro.setDescripcionTipoAnotacion(getDescripcionTipoAnotacion());

                registroDetalle.setNumeroTransporte(de_Internos_Control.getNumero_Transporte_Entrada());
                preRegistro.setUsuario(de_Internos_Control.getNombre_Usuario());
                preRegistro.setContactoUsuario(de_Internos_Control.getContacto_Usuario());
                registroDetalle.setObservaciones(de_Internos_Control.getObservaciones_Apunte());
                preRegistro.setCodigoEntidadRegistralInicio(de_Internos_Control.getCodigo_Entidad_Registral_Inicio());

                if (!StringUtils.isEmpty(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio())) {
                    preRegistro.setDecodificacionEntidadRegistralInicio(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio());
                } else {
                    preRegistro.setDecodificacionEntidadRegistralInicio(Dir3CaibUtils.denominacion(de_Internos_Control.getCodigo_Entidad_Registral_Inicio(), "oficina"));
                }

                // Tipo de transporte
                if (StringUtils.isNotBlank(de_Internos_Control.getTipo_Transporte_Entrada())) {
                    registroDetalle.setTransporte(getTipoTransporteEntrada());
                }


                // Tipo de registro
                Tipo_RegistroType tipo_Registro = de_Internos_Control.getTipo_Registro();
                if ((tipo_Registro != null) && StringUtils.isNotBlank(tipo_Registro.value())) {
                    preRegistro.setTipoRegistro(TipoRegistro.getTipoRegistro(de_Internos_Control.getTipo_Registro().value()).getValue());
                }

                // Documentación física
                Documentacion_FisicaType documentacion_Fisica = de_Internos_Control.getDocumentacion_Fisica();
                if ((documentacion_Fisica != null) && StringUtils.isNotBlank(documentacion_Fisica.value())) {
                    registroDetalle.setTipoDocumentacionFisica(Long.valueOf(DocumentacionFisica.getDocumentacionFisica(de_Internos_Control.getDocumentacion_Fisica().value()).getValue()));
                }

            }


            // DeFormularioGenerico
            De_Formulario_Generico de_Formulario_Generico = getFicheroIntercambio().getDe_Formulario_Generico();
            if (de_Formulario_Generico != null) {
                registroDetalle.setExpone(getExpone());
                registroDetalle.setSolicita(getSolicita());
            }

            // DeInteresados
            De_Interesado[] de_Interesados = getFicheroIntercambio().getDe_Interesado();

            if (ArrayUtils.isNotEmpty(de_Interesados)) {
                for (De_Interesado de_Interesado : de_Interesados) {
                    if (de_Interesado != null) {
                        Interesado interesado = transformarInteresado(de_Interesado);
                        interesados.add(interesado);


                    }
                }
                registroDetalle.setInteresados(interesados);
            }


            De_Anexo[] de_Anexos = getFicheroIntercambio().getDe_Anexo();
            if (ArrayUtils.isNotEmpty(de_Anexos)) {
                for (De_Anexo de_Anexo : de_Anexos) {
                    if (de_Anexo != null) {
                        Anexo anexo = new Anexo();
                        //todo: Implementar función por Marilén
                        /*anexo.setNombreFichero(deAnexo.getNombreFicheroAnexado());
                        anexo.setIdentificadorFichero(deAnexo.getIdentificadorFichero());
                        anexo.setIdentificadorDocumentoFirmado(deAnexo.getIdentificadorDocumentoFirmado());
                        anexo.setCertificado(deAnexo.getCertificado());
                        anexo.setFirma(deAnexo.getFirmaDocumento());
                        anexo.setTimestamp(deAnexo.getTimeStamp());
                        anexo.setValidacionOCSPCertificado(deAnexo.getValidacionOCSPCertificado());
                        anexo.setHash(deAnexo.getHash());
                        anexo.setTipoMIME(deAnexo.getTipoMIME());
                        anexo.setObservaciones(deAnexo.getObservaciones());

                        String validezDocumento = deAnexo.getValidezDocumento();
                        if (StringUtils.isNotBlank(validezDocumento)) {
                            anexo.setValidezDocumento(ValidezDocumentoEnum.getValidezDocumento(validezDocumento));
                        }

                        String tipoDocumento = deAnexo.getTipoDocumento();
                        if (StringUtils.isNotBlank(tipoDocumento)) {
                            anexo.setTipoDocumento(TipoDocumentoEnum.getTipoDocumento(tipoDocumento));
                        }

                        preRegistro.getAnexos().add(anexo);*/
                    }
                }
                registroDetalle.setAnexos(anexos);
            }
        }


        preRegistro.setRegistroDetalle(registroDetalle);

        return preRegistro;
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
     * Transforma un {@link De_Interesado} en un {@link Interesado}
     *
     * @param de_Interesado
     * @return
     */
    private Interesado transformarInteresado(De_Interesado de_Interesado ) {

        Interesado interesado = new Interesado();
        interesado.setIsRepresentante(false);

        // Averiguamos que tipo es el Interesado
        if (es.caib.regweb3.utils.StringUtils.isEmpty(de_Interesado.getRazon_Social_Interesado())) {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotBlank(de_Interesado.getRazon_Social_Interesado())) {
            interesado.setRazonSocial(de_Interesado.getRazon_Social_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getNombre_Interesado())) {
            interesado.setNombre(de_Interesado.getNombre_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getPrimer_Apellido_Interesado())) {
            interesado.setApellido1(de_Interesado.getPrimer_Apellido_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getSegundo_Apellido_Interesado())) {
            interesado.setApellido2(de_Interesado.getSegundo_Apellido_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getTipo_Documento_Identificacion_Interesado())) {
            interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(de_Interesado.getTipo_Documento_Identificacion_Interesado().charAt(0)));
        }
        if (StringUtils.isNotBlank(de_Interesado.getTipo_Documento_Identificacion_Interesado())) {
            interesado.setDocumento(de_Interesado.getTipo_Documento_Identificacion_Interesado());
        }

        if (StringUtils.isNotBlank(de_Interesado.getPais_Interesado())) {
            try {
                interesado.setPais(catPaisEjb.findByCodigo(Long.valueOf(de_Interesado.getPais_Interesado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(de_Interesado.getProvincia_Interesado())) {
            try {
                interesado.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(de_Interesado.getProvincia_Interesado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(de_Interesado.getMunicipio_Interesado())) {
            try {
                interesado.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(de_Interesado.getMunicipio_Interesado()), Long.valueOf(de_Interesado.getProvincia_Interesado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(de_Interesado.getDireccion_Interesado())) {
            interesado.setDireccion(de_Interesado.getDireccion_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getCodigo_Postal_Interesado())) {
            interesado.setCp(de_Interesado.getCodigo_Postal_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getCorreo_Electronico_Interesado())) {
            interesado.setEmail(de_Interesado.getCorreo_Electronico_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getTelefono_Contacto_Interesado())) {
            interesado.setTelefono(de_Interesado.getTelefono_Contacto_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getDireccion_Electronica_Habilitada_Interesado())) {
            interesado.setDireccionElectronica(de_Interesado.getDireccion_Electronica_Habilitada_Interesado());
        }
        if (StringUtils.isNotBlank(de_Interesado.getCanal_Preferente_Comunicacion_Interesado())) {
            interesado.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(de_Interesado.getCanal_Preferente_Comunicacion_Interesado()));
        }
        if (StringUtils.isNotBlank(de_Interesado.getObservaciones())) {
            interesado.setObservaciones(de_Interesado.getObservaciones());
        }

        // Si el interesado tiene representante, lo generamos
        if (StringUtils.isNotBlank(de_Interesado.getNombre_Representante()) || StringUtils.isNotBlank(de_Interesado.getRazon_Social_Representante())) {
            interesado.setRepresentante(transformarRepresentante(de_Interesado, interesado));
        }

        return interesado;
    }

    /**
     * Transforma un {@link De_Interesado} en un {@link Interesado}
     *
     * @param deRepresentante
     * @return
     */
    private Interesado transformarRepresentante(De_Interesado deRepresentante, Interesado interesado) {

        Interesado representante = new Interesado();
        representante.setIsRepresentante(true);
        representante.setRepresentado(interesado);

        // Averiguamos que tipo es el Representante
        if (es.caib.regweb3.utils.StringUtils.isEmpty(deRepresentante.getRazon_Social_Representante())) {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotBlank(deRepresentante.getRazon_Social_Representante())) {
            representante.setRazonSocial(deRepresentante.getRazon_Social_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getNombre_Representante())) {
            representante.setNombre(deRepresentante.getNombre_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getPrimer_Apellido_Representante())) {
            representante.setApellido1(deRepresentante.getPrimer_Apellido_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getSegundo_Apellido_Representante())) {
            representante.setApellido2(deRepresentante.getSegundo_Apellido_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getTipo_Documento_Identificacion_Representante())) {
            representante.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(deRepresentante.getTipo_Documento_Identificacion_Representante().charAt(0)));
        }
        if (StringUtils.isNotBlank(deRepresentante.getDocumento_Identificacion_Representante())) {
            representante.setDocumento(deRepresentante.getDocumento_Identificacion_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getPais_Representante())) {
            try {
                representante.setPais(catPaisEjb.findByCodigo(Long.valueOf(deRepresentante.getPais_Representante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deRepresentante.getProvincia_Representante())) {
            try {
                representante.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(deRepresentante.getProvincia_Representante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deRepresentante.getMunicipio_Representante())) {
            try {
                representante.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(deRepresentante.getMunicipio_Representante()), Long.valueOf(deRepresentante.getProvincia_Interesado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deRepresentante.getDireccion_Representante())) {
            representante.setDireccion(deRepresentante.getDireccion_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getCodigo_Postal_Representante())) {
            representante.setCp(deRepresentante.getCodigo_Postal_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getCorreo_Electronico_Representante())) {
            representante.setEmail(deRepresentante.getCorreo_Electronico_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getTelefono_Contacto_Representante())) {
            representante.setTelefono(deRepresentante.getTelefono_Contacto_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getDireccion_Electronica_Habilitada_Representante())) {
            representante.setDireccionElectronica(deRepresentante.getDireccion_Electronica_Habilitada_Representante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getCanal_Preferente_Comunicacion_Representante())) {
            representante.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(deRepresentante.getCanal_Preferente_Comunicacion_Representante()));
        }
        if (StringUtils.isNotBlank(deRepresentante.getObservaciones())) {
            representante.setObservaciones(deRepresentante.getObservaciones());
        }

        return representante;

    }

    @Override
    public String toString() {
        return "FicheroIntercambio{" +
                "ficheroIntercambio=" + ficheroIntercambio +
                '}';
    }
}
