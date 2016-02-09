package es.caib.regweb3.sir.core.utils;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.persistence.ejb.CatLocalidadLocal;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoAnotacion;
import es.caib.regweb3.sir.core.model.TipoTransporte;
import es.caib.regweb3.sir.core.schema.FicheroIntercambioSICRES3;
import es.caib.regweb3.utils.RegwebConstantes;
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
    private FicheroIntercambioSICRES3 ficheroIntercambio = null;

    /**
     * Constructor.
     */
    public FicheroIntercambio() {
        super();
    }

    public FicheroIntercambioSICRES3 getFicheroIntercambio() {
        return ficheroIntercambio;
    }

    public void setFicheroIntercambio(
            FicheroIntercambioSICRES3 ficheroIntercambio) {
        this.ficheroIntercambio = ficheroIntercambio;
    }

    public String getCodigoEntidadRegistralOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeOrigenORemitente() != null)) {
            return getFicheroIntercambio().getDeOrigenORemitente().getCodigoEntidadRegistralOrigen();
        }

        return null;
    }

    public String getDescripcionEntidadRegistralOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeOrigenORemitente() != null)) {
            return getFicheroIntercambio().getDeOrigenORemitente().getDecodificacionEntidadRegistralOrigen();
        }

        return null;
    }

    public String getCodigoUnidadTramitacionOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeOrigenORemitente() != null)) {
            return getFicheroIntercambio().getDeOrigenORemitente().getCodigoUnidadTramitacionOrigen();
        }

        return null;
    }

    public String getDescripcionUnidadTramitacionOrigen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeOrigenORemitente() != null)) {
            return getFicheroIntercambio().getDeOrigenORemitente().getDecodificacionUnidadTramitacionOrigen();
        }

        return null;
    }

    public String getCodigoEntidadRegistralDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeDestino() != null)) {
            return getFicheroIntercambio().getDeDestino().getCodigoEntidadRegistralDestino();
        }

        return null;
    }

    public String getDescripcionEntidadRegistralDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeDestino() != null)) {
            return getFicheroIntercambio().getDeDestino().getDecodificacionEntidadRegistralDestino();
        }

        return null;
    }

    public String getCodigoUnidadTramitacionDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeDestino() != null)) {
            return getFicheroIntercambio().getDeDestino().getCodigoUnidadTramitacionDestino();
        }

        return null;
    }

    public String getDescripcionUnidadTramitacionDestino() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeDestino() != null)) {
            return getFicheroIntercambio().getDeDestino().getDecodificacionUnidadTramitacionDestino();
        }

        return null;
    }

    public String getNumeroRegistro() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeOrigenORemitente() != null)) {
            return getFicheroIntercambio().getDeOrigenORemitente().getNumeroRegistroEntrada();
        }

        return null;
    }

    public String getFechaRegistroXML() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeOrigenORemitente() != null)) {
            return getFicheroIntercambio().getDeOrigenORemitente().getFechaHoraEntrada();
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
                && (getFicheroIntercambio().getDeOrigenORemitente() != null)) {
            return getFicheroIntercambio().getDeOrigenORemitente().getTimestampEntrada();
        }

        return null;
    }

    public String getCodigoAsunto() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeAsunto() != null)) {
            return getFicheroIntercambio().getDeAsunto().getCodigoAsuntoSegunDestino();
        }

        return null;
    }

    public String getNumeroExpediente() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeAsunto() != null)) {
            return getFicheroIntercambio().getDeAsunto().getNumeroExpediente();
        }

        return null;
    }

    public String getReferenciaExterna() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeAsunto() != null)) {
            return getFicheroIntercambio().getDeAsunto().getReferenciaExterna();
        }

        return null;
    }

    public String getResumen() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeAsunto() != null)) {
            return getFicheroIntercambio().getDeAsunto().getResumen();
        }

        return null;
    }

    public String getCodigoEntidadRegistralInicio() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getCodigoEntidadRegistralInicio();
        }

        return null;
    }

    public String getDescripcionEntidadRegistralInicio() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getDecodificacionEntidadRegistralInicio();
        }

        return null;
    }

    public String getNombreUsuario() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getNombreUsuario();
        }

        return null;
    }

    public String getContactoUsuario() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getContactoUsuario();
        }

        return null;
    }

    public String getTipoTransporteXML() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getTipoTransporteEntrada();
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
        String tipoTransporte = getFicheroIntercambio().getDeInternosControl().getTipoTransporteEntrada();
        if (StringUtils.isNotBlank(tipoTransporte)) {
            return RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(tipoTransporte);
        }

        return null;
    }


    public String getNumeroTransporte() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getNumeroTransporteEntrada();
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
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getIdentificadorIntercambio();
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
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getAplicacionVersionEmisora();
        }

        return null;
    }

    public String getTipoAnotacionXML() {

        if (getFicheroIntercambio() != null) {
            FicheroIntercambioSICRES3.DeInternosControl deInternosControl = getFicheroIntercambio().getDeInternosControl();
            if (deInternosControl != null) {
                return deInternosControl.getTipoAnotacion();
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
            FicheroIntercambioSICRES3.DeInternosControl deInternosControl = getFicheroIntercambio().getDeInternosControl();
            if (deInternosControl != null) {
                return deInternosControl.getDescripcionTipoAnotacion();
            }
        }

        return null;
    }


    public String getObservacionesApunte() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeInternosControl() != null)) {
            return getFicheroIntercambio().getDeInternosControl().getObservacionesApunte();
        }

        return null;
    }


    public String getExpone() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeFormularioGenerico() != null)) {
            return getFicheroIntercambio().getDeFormularioGenerico().getExpone();
        }

        return null;
    }

    public String getSolicita() {
        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDeFormularioGenerico() != null)) {
            return getFicheroIntercambio().getDeFormularioGenerico().getSolicita();
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

        if (getFicheroIntercambio() != null) {

            // DeOrigenORemitente
            FicheroIntercambioSICRES3.DeOrigenORemitente deOrigenoRemitente = getFicheroIntercambio().getDeOrigenORemitente();
            if (deOrigenoRemitente != null) {

                preRegistro.setCodigoEntidadRegistralOrigen(deOrigenoRemitente.getCodigoEntidadRegistralOrigen());

                if (!StringUtils.isEmpty(deOrigenoRemitente.getDecodificacionEntidadRegistralOrigen())) {
                    preRegistro.setDecodificacionEntidadRegistralOrigen(deOrigenoRemitente.getDecodificacionEntidadRegistralOrigen());
                } else {
                    preRegistro.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(deOrigenoRemitente.getCodigoEntidadRegistralOrigen(), "oficina"));
                }

                preRegistro.setCodigoUnidadTramitacionOrigen(deOrigenoRemitente.getCodigoUnidadTramitacionOrigen());

                if (!StringUtils.isEmpty(deOrigenoRemitente.getCodigoUnidadTramitacionOrigen())) {
                    preRegistro.setCodigoUnidadTramitacionOrigen(deOrigenoRemitente.getCodigoUnidadTramitacionOrigen());

                    if (!StringUtils.isEmpty(deOrigenoRemitente.getDecodificacionUnidadTramitacionOrigen())) {
                        preRegistro.setDecodificacionUnidadTramitacionOrigen(deOrigenoRemitente.getDecodificacionUnidadTramitacionOrigen());
                    } else {
                        preRegistro.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(deOrigenoRemitente.getCodigoUnidadTramitacionOrigen(), "unidad"));
                    }
                }

                registroDetalle.setNumeroRegistroOrigen(deOrigenoRemitente.getNumeroRegistroEntrada());
                //preRegistro.setTimestampRegistro(deOrigenoRemitente.getTimestampEntrada());

                String fechaRegistro = deOrigenoRemitente.getFechaHoraEntrada();
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
            FicheroIntercambioSICRES3.DeDestino deDestino = getFicheroIntercambio().getDeDestino();
            if (deDestino != null) {

                preRegistro.setCodigoEntidadRegistralDestino(deDestino.getCodigoEntidadRegistralDestino());
                if (!StringUtils.isEmpty(deDestino.getDecodificacionEntidadRegistralDestino())) {
                    preRegistro.setDecodificacionEntidadRegistralDestino(deDestino.getDecodificacionEntidadRegistralDestino());
                } else {
                    preRegistro.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(deDestino.getCodigoEntidadRegistralDestino(), "oficina"));
                }

                if (!StringUtils.isEmpty(deDestino.getCodigoUnidadTramitacionDestino())) {
                    preRegistro.setCodigoUnidadTramitacionDestino(deDestino.getCodigoUnidadTramitacionDestino());
                    if (!StringUtils.isEmpty(deDestino.getDecodificacionUnidadTramitacionDestino())) {
                        preRegistro.setDecodificacionUnidadTramitacionDestino(deDestino.getDecodificacionUnidadTramitacionDestino());
                    } else {
                        preRegistro.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(deDestino.getCodigoUnidadTramitacionDestino(), "unidad"));
                    }
                }

            }

            // DeAsunto
            FicheroIntercambioSICRES3.DeAsunto deAsunto = getFicheroIntercambio().getDeAsunto();
            if (deAsunto != null) {
                registroDetalle.setExtracto(deAsunto.getResumen());
                //registroDetalle.setCodigoAsunto(deAsunto.getCodigoAsuntoSegunDestino());// todo: Como mapear el CodigoAsunto
                registroDetalle.setReferenciaExterna(deAsunto.getReferenciaExterna());
                registroDetalle.setExpediente(deAsunto.getNumeroExpediente());
            }

            // DeInternosControl
            FicheroIntercambioSICRES3.DeInternosControl deInternosControl = getFicheroIntercambio().getDeInternosControl();
            if (deInternosControl != null) {

                preRegistro.setIdIntercambio(getIdentificadorIntercambio());
                preRegistro.setIndicadorPrueba(deInternosControl.getIndicadorPrueba());
                registroDetalle.setAplicacion(deInternosControl.getAplicacionVersionEmisora());
                preRegistro.setTipoAnotacion(RegwebConstantes.TIPO_ANOTACION_BY_CODIGO.get(getTipoAnotacionXML()));
                preRegistro.setDescripcionTipoAnotacion(getDescripcionTipoAnotacion());

                registroDetalle.setNumeroTransporte(deInternosControl.getNumeroTransporteEntrada());
                preRegistro.setUsuario(deInternosControl.getNombreUsuario());
                preRegistro.setContactoUsuario(deInternosControl.getContactoUsuario());
                registroDetalle.setObservaciones(deInternosControl.getObservacionesApunte());
                preRegistro.setCodigoEntidadRegistralInicio(deInternosControl.getCodigoEntidadRegistralInicio());

                if (!StringUtils.isEmpty(deInternosControl.getDecodificacionEntidadRegistralInicio())) {
                    preRegistro.setDecodificacionEntidadRegistralInicio(deInternosControl.getDecodificacionEntidadRegistralInicio());
                } else {
                    preRegistro.setDecodificacionEntidadRegistralInicio(Dir3CaibUtils.denominacion(deInternosControl.getCodigoEntidadRegistralInicio(), "oficina"));
                }

                // Tipo de transporte
                if (StringUtils.isNotBlank(deInternosControl.getTipoTransporteEntrada())) {
                    registroDetalle.setTransporte(getTipoTransporteEntrada());
                }


                // Tipo de registro
                String tipoRegistro = deInternosControl.getTipoRegistro();
                if (StringUtils.isNotBlank(tipoRegistro)) {
                    preRegistro.setTipoRegistro(deInternosControl.getTipoRegistro());
                }

                // Documentación física
                if (StringUtils.isNotBlank(deInternosControl.getDocumentacionFisica())) {
                    registroDetalle.setTipoDocumentacionFisica(Long.valueOf(deInternosControl.getDocumentacionFisica()));
                }

            }


            // DeFormularioGenerico
            FicheroIntercambioSICRES3.DeFormularioGenerico deFormularioGenerico = getFicheroIntercambio().getDeFormularioGenerico();
            if (deFormularioGenerico != null) {
                registroDetalle.setExpone(getExpone());
                registroDetalle.setSolicita(getSolicita());
            }

            // DeInteresados
            List<FicheroIntercambioSICRES3.DeInteresado> deInteresados = getFicheroIntercambio().getDeInteresado();

            if (deInteresados.size() > 0) {
                for (FicheroIntercambioSICRES3.DeInteresado deInteresado : deInteresados) {
                    if (deInteresado != null) {
                        Interesado interesado = transformarInteresado(deInteresado);
                        interesados.add(interesado);


                    }
                }
                registroDetalle.setInteresados(interesados);
            }


            List<FicheroIntercambioSICRES3.DeAnexo> deAnexos = getFicheroIntercambio().getDeAnexo();
            if (deInteresados.size() > 0) {
                for (FicheroIntercambioSICRES3.DeAnexo deAnexo : deAnexos) {
                    if (deAnexo != null) {
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
            jc = JAXBContext.newInstance(FicheroIntercambioSICRES3.class);
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
            return getFicheroIntercambio().getDeAnexo().size();
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
        if (secuencia < getFicheroIntercambio().getDeAnexo().size()) {
            getFicheroIntercambio().getDeAnexo().get(secuencia).setAnexo(contenido);
        }
    }

    /**
     * Obtiene el contenido de un anexo.
     *
     * @param secuencia Ordinal del anexo.
     * @return Contenido del anexo.
     */
    public byte[] getContenidoAnexo(int secuencia) {
        if (secuencia < getFicheroIntercambio().getDeAnexo().size()) {
            return getFicheroIntercambio().getDeAnexo().get(secuencia).getAnexo();
        } else {
            return null;
        }
    }

    /**
     * Transforma un {@link es.caib.regweb3.sir.core.schema.FicheroIntercambioSICRES3.DeInteresado} en un {@link es.caib.regweb3.model.Interesado}
     *
     * @param deInteresado
     * @return
     */
    private Interesado transformarInteresado(FicheroIntercambioSICRES3.DeInteresado deInteresado) {

        Interesado interesado = new Interesado();
        interesado.setIsRepresentante(false);

        // Averiguamos que tipo es el Interesado
        if (es.caib.regweb3.utils.StringUtils.isEmpty(deInteresado.getRazonSocialInteresado())) {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotBlank(deInteresado.getRazonSocialInteresado())) {
            interesado.setRazonSocial(deInteresado.getRazonSocialInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getNombreInteresado())) {
            interesado.setNombre(deInteresado.getNombreInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getPrimerApellidoInteresado())) {
            interesado.setApellido1(deInteresado.getPrimerApellidoInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getSegundoApellidoInteresado())) {
            interesado.setApellido2(deInteresado.getSegundoApellidoInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getTipoDocumentoIdentificacionInteresado())) {
            interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(deInteresado.getTipoDocumentoIdentificacionInteresado().charAt(0)));
        }
        if (StringUtils.isNotBlank(deInteresado.getDocumentoIdentificacionInteresado())) {
            interesado.setDocumento(deInteresado.getDocumentoIdentificacionInteresado());
        }

        if (StringUtils.isNotBlank(deInteresado.getPaisInteresado())) {
            try {
                interesado.setPais(catPaisEjb.findByCodigo(Long.valueOf(deInteresado.getPaisInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deInteresado.getProvinciaInteresado())) {
            try {
                interesado.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(deInteresado.getProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deInteresado.getMunicipioInteresado())) {
            try {
                interesado.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(deInteresado.getMunicipioInteresado()), Long.valueOf(deInteresado.getProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deInteresado.getDireccionInteresado())) {
            interesado.setDireccion(deInteresado.getDireccionInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getCodigoPostalInteresado())) {
            interesado.setCp(deInteresado.getCodigoPostalInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getCorreoElectronicoInteresado())) {
            interesado.setEmail(deInteresado.getCorreoElectronicoInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getTelefonoContactoInteresado())) {
            interesado.setTelefono(deInteresado.getTelefonoContactoInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getDireccionElectronicaHabilitadaInteresado())) {
            interesado.setDireccionElectronica(deInteresado.getDireccionElectronicaHabilitadaInteresado());
        }
        if (StringUtils.isNotBlank(deInteresado.getCanalPreferenteComunicacionInteresado())) {
            interesado.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(deInteresado.getCanalPreferenteComunicacionInteresado()));
        }
        if (StringUtils.isNotBlank(deInteresado.getObservaciones())) {
            interesado.setObservaciones(deInteresado.getObservaciones());
        }

        // Si el interesado tiene representante, lo generamos
        if (StringUtils.isNotBlank(deInteresado.getNombreRepresentante()) || StringUtils.isNotBlank(deInteresado.getRazonSocialRepresentante())) {
            interesado.setRepresentante(transformarRepresentante(deInteresado, interesado));
        }

        return interesado;
    }

    /**
     * Transforma un {@link es.caib.regweb3.sir.core.schema.FicheroIntercambioSICRES3.DeInteresado} en un {@link es.caib.regweb3.model.Interesado}
     *
     * @param deRepresentante
     * @return
     */
    private Interesado transformarRepresentante(FicheroIntercambioSICRES3.DeInteresado deRepresentante, Interesado interesado) {

        Interesado representante = new Interesado();
        representante.setIsRepresentante(true);
        representante.setRepresentado(interesado);

        // Averiguamos que tipo es el Representante
        if (es.caib.regweb3.utils.StringUtils.isEmpty(deRepresentante.getRazonSocialRepresentante())) {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (StringUtils.isNotBlank(deRepresentante.getRazonSocialRepresentante())) {
            representante.setRazonSocial(deRepresentante.getRazonSocialRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getNombreRepresentante())) {
            representante.setNombre(deRepresentante.getNombreRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getPrimerApellidoRepresentante())) {
            representante.setApellido1(deRepresentante.getPrimerApellidoRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getSegundoApellidoRepresentante())) {
            representante.setApellido2(deRepresentante.getSegundoApellidoRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getTipoDocumentoIdentificacionRepresentante())) {
            representante.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(deRepresentante.getTipoDocumentoIdentificacionRepresentante().charAt(0)));
        }
        if (StringUtils.isNotBlank(deRepresentante.getDocumentoIdentificacionRepresentante())) {
            representante.setDocumento(deRepresentante.getDocumentoIdentificacionRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getPaisRepresentante())) {
            try {
                representante.setPais(catPaisEjb.findByCodigo(Long.valueOf(deRepresentante.getPaisRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deRepresentante.getProvinciaRepresentante())) {
            try {
                representante.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(deRepresentante.getProvinciaRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deRepresentante.getMunicipioRepresentante())) {
            try {
                representante.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(deRepresentante.getMunicipioRepresentante()), Long.valueOf(deRepresentante.getProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(deRepresentante.getDireccionRepresentante())) {
            representante.setDireccion(deRepresentante.getDireccionRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getCodigoPostalRepresentante())) {
            representante.setCp(deRepresentante.getCodigoPostalRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getCorreoElectronicoRepresentante())) {
            representante.setEmail(deRepresentante.getCorreoElectronicoRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getTelefonoContactoRepresentante())) {
            representante.setTelefono(deRepresentante.getTelefonoContactoRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getDireccionElectronicaHabilitadaRepresentante())) {
            representante.setDireccionElectronica(deRepresentante.getDireccionElectronicaHabilitadaRepresentante());
        }
        if (StringUtils.isNotBlank(deRepresentante.getCanalPreferenteComunicacionRepresentante())) {
            representante.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(deRepresentante.getCanalPreferenteComunicacionRepresentante()));
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
