package es.caib.regweb3.persistence.utils;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.interdoc.ws.api.Fitxer;
import es.caib.interdoc.ws.api.Metadada;
import es.caib.interdoc.ws.api.ObtenerReferenciaRequestInfo;
import es.caib.interdoc.ws.api.ObtenerReferenciaWs;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.CanalNotificacion;
import es.caib.regweb3.model.sir.TipoDocumento;
import es.caib.regweb3.model.sir.TipoDocumentoIdentificacion;
import es.caib.regweb3.model.sir.TipoTransporte;
import es.caib.regweb3.model.utils.*;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.ReferenciaUnicaUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.gob.ad.registros.sir.gestionEni.bean.ContenidoBean;
import es.gob.ad.registros.sir.gestionEni.bean.FirmaBean;
import es.gob.ad.registros.sir.interService.bean.AnexoBean;
import es.gob.ad.registros.sir.interService.bean.AsientoBean;
import es.gob.ad.registros.sir.interService.bean.InteresadoBean;
import es.gob.ad.registros.sir.interService.bean.OtrosMetadatos;
import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.interSincroDIR3.service.IServiciosOfiService;
import es.gob.ad.registros.sir.interService.service.IConsultaService;
import es.gob.administracionelectronica.eni.xsd.v1_0.documento_e.metadatos.TipoDocumental;
import es.gob.administracionelectronica.eni.xsd.v1_0.documento_e.metadatos.TipoMetadatos;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ejb.EJB;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static es.caib.regweb3.utils.RegwebConstantes.*;

/**
 * @author mgonzalez
 * @version 1
 * 08/02/2023
 */
@Component
public class LibSirUtils {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    IServiciosOfiService serviciosOfiService;

    @Autowired
    IConsultaService consultaService;

    @EJB(mappedName = TipoDocumentalLocal.JNDI_NAME)
    public TipoDocumentalLocal tipoDocumentalEjb;


    /**
     * Transforma un registro de Entrada a un AsientoBean para enviarlo mediante la libreria SIR.
     *
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    public AsientoBean transformarRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException, DatatypeConfigurationException, InterException {

        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        AsientoBean asientoBean = new AsientoBean();
        if (registroEntrada != null) {

            //Entidad Registral Origen
            asientoBean.setCdEnRgOrigen(registroEntrada.getOficina().getCodigo());
            asientoBean.setDsEnRgOrigen(registroEntrada.getOficina().getDenominacion());
            asientoBean.setNuRgOrigen(registroEntrada.getNumeroRegistroFormateado());
            asientoBean.setFeRgOrigen(registroEntrada.getFecha());
            asientoBean.setFeRgPresentacion(registroEntrada.getFecha());
            asientoBean.setTsRgOrigen(Base64.encodeBase64String(new Timestamp(registroEntrada.getFecha().getTime()).toString().getBytes()));
            asientoBean.setTsRgPresentacion(Base64.encodeBase64String(new Timestamp(registroEntrada.getFecha().getTime()).toString().getBytes()));

            //Unidad Tramitación Origen
            asientoBean.setCdUnTrOrigen(registroEntrada.getOficina().getOrganismoResponsable().getCodigo());
            asientoBean.setDsUnTrOrigen(registroEntrada.getOficina().getOrganismoResponsable().getDenominacion());


            //Unidad Tramitación Destino ( no es obligatoria, no la informamos)
            asientoBean.setCdUnTrDestino(registroEntrada.getDestinoExternoCodigo());
            asientoBean.setDsUnTrDestino(registroEntrada.getDestinoExternoDenominacion());

            //Usuario y tipo Registro
            asientoBean.setNoUsuario(registroEntrada.getUsuario().getNombreCompleto());
            asientoBean.setCtUsuario(registroEntrada.getUsuario().getUsuario().getEmail());
            asientoBean.setCdTpRegistro(REGISTRO_ENTRADA_LIBSIR);


            //Entidad Registral Inicio
            asientoBean.setCdEnRgInicio(RegistroUtils.obtenerCodigoOficinaOrigen(registroDetalle, registroEntrada.getOficina().getCodigo()));
            asientoBean.setDsEnRgInicio(RegistroUtils.obtenerDenominacionOficinaOrigen(registroDetalle, registroEntrada.getOficina().getDenominacion()));

            //Unidad Tramitacion Inicio
            asientoBean.setCdUnTrInicio(RegistroUtils.obtenerCodigoOficinaOrigen(registroDetalle, registroEntrada.getOficina().getCodigo()));
            asientoBean.setDsUnTrInicio(RegistroUtils.obtenerDenominacionOficinaOrigen(registroDetalle, registroEntrada.getOficina().getDenominacion()));

            //Campos de registro detalle
            transformarRegistroDetalle(registroDetalle, asientoBean, registroEntrada.getDestino());

            //METADATOS
            Set<MetadatoRegistroEntrada> metadatosRE = registroEntrada.getMetadatosRegistroEntrada();
            Set<MetadatoRegistroEntrada> metadatoREGeneral = metadatosRE.stream().filter(metadato -> metadato.getTipo().equals(METADATO_GENERAL)).collect(Collectors.toSet());
            Set<MetadatoRegistroEntrada> metadatoREParticular = metadatosRE.stream().filter(metadato -> metadato.getTipo().equals(METADATO_PARTICULAR)).collect(Collectors.toSet());

            Set<OtrosMetadatos> otrosMetadatosGeneral = metadatoREGeneral.stream()
                    .map(metadato -> {
                        OtrosMetadatos otroMetadato = new OtrosMetadatos();
                        otroMetadato.setCampo(metadato.getCampo());
                        otroMetadato.setValor(metadato.getValor());
                        return otroMetadato;
                    }).collect(Collectors.toSet());

            Set<OtrosMetadatos> otrosMetadatosParticular = metadatoREParticular.stream()
                    .map(metadato -> {
                        OtrosMetadatos otroMetadato = new OtrosMetadatos();
                        otroMetadato.setCampo(metadato.getCampo());
                        otroMetadato.setValor(metadato.getValor());
                        return otroMetadato;
                    }).collect(Collectors.toSet());

            asientoBean.setOtrosMetadatosGenerales(otrosMetadatosGeneral);
            asientoBean.setOtrosMetadatosParticulares(otrosMetadatosParticular);


            //INTERESADOS
            List<Interesado> interesados = registroDetalle.getInteresados();
            if (!interesados.isEmpty()) {
                for (Interesado interesado : interesados) {
                    if (interesado != null) {
                        InteresadoBean interesadoBean = transformarInteresado(interesado);
                        asientoBean.getInteresadosBean().add(interesadoBean);
                    }
                }
            }

            asientoBean.setAnexosBean(transformarAnexos(registroEntrada.getRegistroDetalle().getAnexosFull(), registroDetalle.getIdentificadorIntercambio()));

        }
        return asientoBean;

    }


    /**
     * Transforma un registro de Entrada a un AsientoBean para enviarlo mediante la libreria SIR.
     *
     * @param registroSalida
     * @return
     * @throws I18NException
     */
    public AsientoBean transformarRegistroSalida(RegistroSalida registroSalida) throws I18NException, DatatypeConfigurationException {

        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        AsientoBean asientoBean = new AsientoBean();
        if (registroSalida != null) {

            //Entidad Registral Origen
            asientoBean.setCdEnRgOrigen(registroSalida.getOficina().getCodigo());
            asientoBean.setDsEnRgOrigen(registroSalida.getOficina().getDenominacion());
            asientoBean.setNuRgOrigen(registroSalida.getNumeroRegistroFormateado());
            asientoBean.setFeRgOrigen(registroSalida.getFecha());
            asientoBean.setFeRgPresentacion(registroSalida.getFecha());
            asientoBean.setTsRgOrigen(Base64.encodeBase64String(new Timestamp(registroSalida.getFecha().getTime()).toString().getBytes()));
            asientoBean.setTsRgPresentacion(Base64.encodeBase64String(new Timestamp(registroSalida.getFecha().getTime()).toString().getBytes()));

            //Unidad Tramitación Origen
            asientoBean.setCdUnTrOrigen(registroSalida.getOficina().getOrganismoResponsable().getCodigo());
            asientoBean.setDsUnTrOrigen(registroSalida.getOficina().getOrganismoResponsable().getDenominacion());


            //Unidad Tramitación Destino ( no es obligatoria, no la informamos)
            asientoBean.setCdUnTrDestino(obtenerCodigoUnidadTramitacionDestino(registroDetalle));
            String destinoExternoDecodificacion = obtenerDenominacionUnidadTramitacionDestino(registroDetalle);
            if (es.caib.regweb3.utils.StringUtils.isNotEmpty(destinoExternoDecodificacion)) {
                asientoBean.setDsUnTrDestino(destinoExternoDecodificacion);
            } else {
                asientoBean.setDsUnTrDestino(null);
            }


            //Usuario y tipo Registro
            asientoBean.setNoUsuario(registroSalida.getUsuario().getNombreCompleto());
            asientoBean.setCtUsuario(registroSalida.getUsuario().getUsuario().getEmail());
            asientoBean.setCdTpRegistro(REGISTRO_SALIDA_LIBSIR);


            //Entidad Registral Inicio
            asientoBean.setCdEnRgInicio(RegistroUtils.obtenerCodigoOficinaOrigen(registroDetalle, registroSalida.getOficina().getCodigo()));
            asientoBean.setDsEnRgInicio(RegistroUtils.obtenerDenominacionOficinaOrigen(registroDetalle, registroSalida.getOficina().getDenominacion()));

            //Unidad Tramitacion Inicio
            asientoBean.setCdUnTrInicio(RegistroUtils.obtenerCodigoOficinaOrigen(registroDetalle, registroSalida.getOficina().getCodigo()));
            asientoBean.setDsUnTrInicio(RegistroUtils.obtenerDenominacionOficinaOrigen(registroDetalle, registroSalida.getOficina().getDenominacion()));

            //Campos de registro detalle
            transformarRegistroDetalle(registroDetalle, asientoBean, null);

            //METADATOS
            Set<MetadatoRegistroSalida> metadatosRE = registroSalida.getMetadatosRegistroSalida();
            Set<MetadatoRegistroSalida> metadatoREGeneral = metadatosRE.stream().filter(metadato -> metadato.getTipo().equals(METADATO_GENERAL)).collect(Collectors.toSet());
            Set<MetadatoRegistroSalida> metadatoREParticular = metadatosRE.stream().filter(metadato -> metadato.getTipo().equals(METADATO_PARTICULAR)).collect(Collectors.toSet());

            Set<OtrosMetadatos> otrosMetadatosGeneral = metadatoREGeneral.stream()
                    .map(metadato -> {
                        OtrosMetadatos otroMetadato = new OtrosMetadatos();
                        otroMetadato.setCampo(metadato.getCampo());
                        otroMetadato.setValor(metadato.getValor());
                        return otroMetadato;
                    }).collect(Collectors.toSet());

            Set<OtrosMetadatos> otrosMetadatosParticular = metadatoREParticular.stream()
                    .map(metadato -> {
                        OtrosMetadatos otroMetadato = new OtrosMetadatos();
                        otroMetadato.setCampo(metadato.getCampo());
                        otroMetadato.setValor(metadato.getValor());
                        return otroMetadato;
                    }).collect(Collectors.toSet());

            asientoBean.setOtrosMetadatosGenerales(otrosMetadatosGeneral);
            asientoBean.setOtrosMetadatosParticulares(otrosMetadatosParticular);


            //INTERESADOS Irá siempre vacio, porque el destinatario va informado en el segmento DeDestino
            /*List<Interesado> interesados = registroDetalle.getInteresados();
            if (!interesados.isEmpty()) {
                for (Interesado interesado : interesados) {
                    if (interesado != null) {
                        InteresadoBean interesadoBean = transformarInteresado(interesado);
                        asientoBean.getInteresadosBean().add(interesadoBean);
                    }
                }
            }*/


            asientoBean.setAnexosBean(transformarAnexos(registroSalida.getRegistroDetalle().getAnexosFull(), registroDetalle.getIdentificadorIntercambio()));
        }
        return asientoBean;

    }


    /**
     * Crea un Interesado tipo Persona Juridica a partir del Código Unidad De Gestión de destino o si no está informado,
     * a partir del Código Entidad Registral de destino
     *
     * @return
     */
    private InteresadoBean crearInteresadoJuridicoRegistroSir(String codigoUnidadTramitacionDestino, String decodificacionUnidadTramitacionDestino, String codigoEntidadRegistralDestino, Long idEntidad) {

        InteresadoBean interesadoSalida = new InteresadoBean();

        if (StringUtils.isNotBlank(codigoUnidadTramitacionDestino)) {

            interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
            interesadoSalida.setDocumentoIdentificacionInteresado(codigoUnidadTramitacionDestino);

            if (StringUtils.isNotBlank(decodificacionUnidadTramitacionDestino)) {
                interesadoSalida.setRazonSocialInteresado(decodificacionUnidadTramitacionDestino);
            } else {
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), codigoUnidadTramitacionDestino, RegwebConstantes.UNIDAD));

            }

        } else {
            try {
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));

                OficinaTF oficinaTF = oficinasService.obtenerOficina(codigoEntidadRegistralDestino, null, null);

                interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
                interesadoSalida.setDocumentoIdentificacionInteresado(oficinaTF.getCodUoResponsable());
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), oficinaTF.getCodUoResponsable(), RegwebConstantes.UNIDAD));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return interesadoSalida;
    }


    private AnexoBean transformarAnexo(Anexo anexo, int secuencia, String identificadorIntercambio, String tipoMime) throws DatatypeConfigurationException {

        es.gob.ad.registros.sir.interService.bean.AnexoBean anexoBean = new AnexoBean();

        anexoBean.setNombreFichero(es.caib.regweb3.utils.StringUtils.eliminarCaracteresProhibidosArxiu(anexo.getNombreFichero()));
        String identificador_fichero = RegistroUtils.generateIdentificadorFichero(identificadorIntercambio, secuencia, anexo.getNombreFichero());

        anexoBean.setIdentificadorFichero(identificador_fichero);
        if (anexo.isJustificante()) {
            anexoBean.setTipoAnexo(TIPO_ANEXO_JUSTIF_REG_SICRES4);
        } else {
            anexoBean.setTipoAnexo(TIPO_DOCUMENTO_TIPO_ANEXO.get(anexo.getTipoDocumento()));
        }
        anexoBean.setResumen(anexo.getResumen());
        anexoBean.setCodigoFormulario(anexo.getCodigoFormulario());
        anexoBean.setObservaciones(anexo.getObservaciones());

        //Referencia Única del anexo
        ContenidoBean contenidoBean = new ContenidoBean();
        contenidoBean.setContenido(anexo.getIdentificadorRFU().getBytes(StandardCharsets.UTF_8));
        contenidoBean.setNombreFormato(tipoMime);
        anexoBean.setContenidoBean(contenidoBean);


        //URL repositorio Referencia Única
        anexoBean.setUrlRepositorio(anexo.getEndpointRFU());

        //METADATOS SICRES4
        if (anexo.getMetadatosAnexos() != null) {
            Set<MetadatoAnexo> metadatoAnexoGeneral = anexo.getMetadatosAnexos().stream().filter(metadato -> metadato.getTipo().equals(METADATO_GENERAL)).collect(Collectors.toSet());
            Set<MetadatoAnexo> metadatoAnexoParticular = anexo.getMetadatosAnexos().stream().filter(metadato -> metadato.getTipo().equals(METADATO_PARTICULAR)).collect(Collectors.toSet());
            Set<OtrosMetadatos> otrosMetadatosAnexGeneral = metadatoAnexoGeneral.stream()
                    .map(metadato -> {
                        OtrosMetadatos otroMetadato = new OtrosMetadatos();
                        otroMetadato.setCampo(metadato.getCampo());
                        otroMetadato.setValor(metadato.getValor());
                        return otroMetadato;
                    }).collect(Collectors.toSet());

            Set<OtrosMetadatos> otrosMetadatosAnexParticular = metadatoAnexoParticular.stream()
                    .map(metadato -> {
                        OtrosMetadatos otroMetadato = new OtrosMetadatos();
                        otroMetadato.setCampo(metadato.getCampo());
                        otroMetadato.setValor(metadato.getValor());
                        return otroMetadato;
                    }).collect(Collectors.toSet());

            anexoBean.setOtrosMetadatosGenerales(otrosMetadatosAnexGeneral);
            anexoBean.setOtrosMetadatosParticulares(otrosMetadatosAnexParticular);

            //METADATOS ENI
            Set<MetadatoAnexo> metadatosAnexoENI = anexo.getMetadatosAnexos().stream().filter(metadato -> metadato.getTipo().equals(METADATO_NTI)).collect(Collectors.toSet());
            TipoMetadatos tipoMetadatos = new TipoMetadatos();
            for (MetadatoAnexo metadatoAnexo : metadatosAnexoENI) {

                String str = metadatoAnexo.getCampo();
                switch (str) {
                    case "fechaCaptura":
                        tipoMetadatos.setFechaCaptura(DatatypeFactory.newInstance().newXMLGregorianCalendar(metadatoAnexo.getValor()));
                        break;
                    case "origenCiudadanoAdministracion":
                        tipoMetadatos.setOrigenCiudadanoAdministracion(Boolean.parseBoolean(metadatoAnexo.getValor()));
                        break;
                    case "tipoDocumental":
                        tipoMetadatos.setTipoDocumental(TipoDocumental.fromValue(metadatoAnexo.getValor()));
                        break;
                }

            }
            anexoBean.setTipoMetadatos(tipoMetadatos);
        }

        //Metadatos obligatorios SICRES4
        Set<OtrosMetadatos> metadatoAnexoGeneral = new HashSet<>();

        //Hash
        OtrosMetadatos otrosMetadatos = new OtrosMetadatos();
        otrosMetadatos.setCampo("Hash");
        otrosMetadatos.setValor(Base64.encodeBase64String(anexo.getHash()));
        metadatoAnexoGeneral.add(otrosMetadatos);

        //Algoritmo Hash
        otrosMetadatos = new OtrosMetadatos();
        otrosMetadatos.setCampo("AlgoritmoHash");
        otrosMetadatos.setValor("SHA256");
        metadatoAnexoGeneral.add(otrosMetadatos);

        anexoBean.setOtrosMetadatosGenerales(metadatoAnexoGeneral);

        return anexoBean;
    }


    private Set<AnexoBean> transformarAnexos(List<AnexoFull> anexosFull, String identificadorIntercambio) throws DatatypeConfigurationException {

        Set<AnexoBean> anexoBeans= new HashSet<>();
        int secuencia = 0;
        if (!anexosFull.isEmpty()) {
            for (AnexoFull anexoFull : anexosFull) {
                if (anexoFull != null) {
                    es.gob.ad.registros.sir.interService.bean.AnexoBean anexoBean;
                    Anexo anexo = anexoFull.getAnexo();
                    anexoBean = transformarAnexo(anexo, secuencia, identificadorIntercambio, anexoFull.getMime());
                    secuencia++;
                    anexoBeans.add(anexoBean);
                }
            }
        }
        return anexoBeans;

    }

    private InteresadoBean transformarInteresado(Interesado interesado) {

        //  InteresadoSir interesado = new InteresadoSir();
        es.gob.ad.registros.sir.interService.bean.InteresadoBean interesadoBean = new InteresadoBean();

        //Tipo Interesado
        interesadoBean.setTipoPersonaInteresado(TIPOS_PERSONA_SICRES4.get(interesado.getTipo()));

        // Información del interesado
        Long tipoDocumento = interesado.getTipoDocumentoIdentificacion();
        if (tipoDocumento != null) {
            interesadoBean.setTipoDocumentoIdentificacionInteresado(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(tipoDocumento)));
        }
        interesadoBean.setTipoDocumentoIdentificacionInteresado("X");
        interesadoBean.setDocumentoIdentificacionInteresado(interesado.getDocumento());
        interesadoBean.setRazonSocialInteresado(interesado.getRazonSocial());
        interesadoBean.setNombreInteresado(interesado.getNombre());
        interesadoBean.setPrimerApellidoInteresado(interesado.getApellido1());
        interesadoBean.setSegundoApellidoInteresado(interesado.getApellido2());
        if (interesado.getPais() != null) {
            interesadoBean.setPaisInteresado(Long.toString(interesado.getPais().getCodigoPais()));
        }
        if (interesado.getProvincia() != null) {
            interesadoBean.setProvinciaInteresado(Long.toString(interesado.getProvincia().getCodigoProvincia()));
        }
        if (interesado.getLocalidad() != null) {
            interesadoBean.setMunicipioInteresado(Long.toString(interesado.getLocalidad().getCodigoLocalidad()));
        }
        interesadoBean.setDireccionInteresado(interesado.getDireccion());
        interesadoBean.setCodPostalInteresado(interesado.getCp());
        interesadoBean.setCorreoElectronicoInteresado(interesado.getEmail());
        interesadoBean.setTelefonoFijoInteresado(interesado.getTelefono());
        interesadoBean.setTelefonoMovilInteresado(interesado.getTelefonoMovil());


        Long canalPreferente = interesado.getCanal();
        if (canalPreferente != -1) {
            interesadoBean.setCanalPreferenteComunicacionInteresado(CODIGO_BY_CANALNOTIFICACION.get(canalPreferente));
        }else{ // se debe indicar a null cuando no está informado
            interesadoBean.setCanalPreferenteComunicacionInteresado(null);
        }

        //CAMPOS NUEVOS SICRES4
        interesadoBean.setSolicitaNotificacionEmailInteresado(interesado.getAvisoCorreoElectronico());
        interesadoBean.setSolicitaNotificacionSMSInteresado(interesado.getAvisoNotificacionSMS());
        interesadoBean.setReceptorNotificacionInteresado(interesado.getReceptorNotificaciones());
        interesadoBean.setCodigoDirectoriosUnificadosInteresado(interesado.getCodDirectoriosUnificados());

        // Información del representante
        Interesado representante = interesado.getRepresentante();
        if (representante != null) {
            interesadoBean.setTipoPersonaRepresentante(RegwebConstantes.TIPOS_PERSONA_SICRES4.get(representante.getTipo()));
            interesadoBean.setDocumentoIdentificacionRepresentante(representante.getDocumento());
            interesadoBean.setRazonSocialRepresentante(representante.getRazonSocial());
            interesadoBean.setNombreRepresentante(representante.getNombre());
            interesadoBean.setPrimerApellidoRepresentante(representante.getApellido1());
            interesadoBean.setSegundoApellidoRepresentante(representante.getApellido2());
            if (representante.getPais() != null) {
                interesadoBean.setPaisRepresentante(Long.toString(representante.getPais().getCodigoPais()));
            }
            if (representante.getProvincia() != null) {
                interesadoBean.setProvinciaRepresentante(Long.toString(representante.getProvincia().getCodigoProvincia()));
            }
            if (representante.getLocalidad() != null) {
                interesadoBean.setMunicipioRepresentante(Long.toString(representante.getLocalidad().getCodigoLocalidad()));
            }
            interesadoBean.setDireccionRepresentante(representante.getDireccion());
            interesadoBean.setCodPostalRepresentante(representante.getCp());
            interesadoBean.setCorreoElectronicoRepresentante(representante.getEmail());
            interesadoBean.setTelefonoFijoRepresentante(representante.getTelefono());
            interesadoBean.setTelefonoMovilRepresentante(representante.getTelefonoMovil());


            tipoDocumento = representante.getTipoDocumentoIdentificacion();
            if (tipoDocumento != null) {
                interesadoBean.setTipoDocumentoIdentificacionRepresentante(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(tipoDocumento)));
            }

            canalPreferente = representante.getCanal();
            if (canalPreferente !=-1) {
                interesadoBean.setCanalPreferenteComunicacionRepresentante(CODIGO_BY_CANALNOTIFICACION.get(canalPreferente));
            }else{// TODO PENDIENTE DE INCIDENCIA 1945947
                interesadoBean.setCanalPreferenteComunicacionRepresentante(null);
            }

            //CAMPOS NUEVOS SICRES4
            interesadoBean.setSolicitaNotificacionEmailRepresentante(representante.getAvisoCorreoElectronico());
            interesadoBean.setSolicitaNotificacionSMSRepresentante(representante.getAvisoNotificacionSMS());
            interesadoBean.setReceptorNotificacionRepresentante(representante.getReceptorNotificaciones());
            interesadoBean.setCodigoDirectoriosUnificadosRepresentante(representante.getCodDirectoriosUnificados());
        }

        interesadoBean.setObservaciones(interesado.getObservaciones());

        return interesadoBean;
    }

    private void transformarRegistroDetalle(RegistroDetalle registroDetalle, AsientoBean asientoBean, Organismo organismo) {

        //Entidad Registral Destino
        asientoBean.setCdEnRgDestino(registroDetalle.getCodigoEntidadRegistralDestino());
        asientoBean.setDsEnRgDestino(registroDetalle.getDecodificacionEntidadRegistralDestino());


        asientoBean.setDsResumen(registroDetalle.getExtracto());
        //Destino
        if (organismo != null) {
            TraduccionCodigoAsunto tra = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
            asientoBean.setCdAsunto(tra.getNombre());
        }
        asientoBean.setRfExterna(registroDetalle.getReferenciaExterna());
        asientoBean.setNuExpediente(registroDetalle.getExpediente());
        asientoBean.setCdTpTransporte(CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        asientoBean.setNuTransporte(registroDetalle.getTransporte() != null ? registroDetalle.getTransporte().toString() : "");
        asientoBean.setCdIntercambio(registroDetalle.getIdentificadorIntercambio());
        asientoBean.setApVersion(registroDetalle.getAplicacion());
        asientoBean.setCdTpAnotacion(registroDetalle.getTipoAnotacion());
        asientoBean.setDsTpAnotacion(registroDetalle.getDecodificacionTipoAnotacion());

        //Documentación Física
        if (registroDetalle.getTipoDocumentacionFisica() != null) {
            asientoBean.setCdDocFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        }
        asientoBean.setDsObservaciones(registroDetalle.getObservaciones());

        //Indicador de Prueba
        if (registroDetalle.getIndicadorPrueba() != null) {
            asientoBean.setCdInPrueba(registroDetalle.getIndicadorPrueba().getValue());
        }

        //Expone / Solicita
        asientoBean.setDsExpone(registroDetalle.getExpone());
        asientoBean.setDsSolicita(registroDetalle.getSolicita());

        //TODO CAMPOS NUEVOS SICRES4
        asientoBean.setModoRegistro(registroDetalle.getPresencial() ? "01" : "02"); // 01 PRESENCIAL, 02 ELECTRÓNICO
        asientoBean.setCdSia(registroDetalle.getCodigoSia());
        //TODO MIRAR SI EL DESTINO ESTA EN RFU.
        asientoBean.setReferenciaUnica(serviciosOfiService.isOficinaConRU((registroDetalle.getCodigoEntidadRegistralDestino())));
        //TODO PEDIR A EDU
        asientoBean.setCdIntercambioPrevio("????");

    }


    /**
     * @param registroDetalle
     * @return
     */
    public static String obtenerDenominacionUnidadTramitacionDestino(RegistroDetalle registroDetalle) {

        List<Interesado> interesados = registroDetalle.getInteresados();

        for (Interesado interesado : interesados) {
            if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {

                return interesado.getRazonSocial();
            }
        }

        return null;
    }

    /**
     * @param registroDetalle
     * @return
     */
    public static String obtenerCodigoUnidadTramitacionDestino(RegistroDetalle registroDetalle) {

        List<Interesado> interesados = registroDetalle.getInteresados();

        for (Interesado interesado : interesados) {
            if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {

                return interesado.getCodigoDir3();
            }
        }

        return null;
    }

    public static String guardarDocumentoInterdoc(AnexoFull anexoFull, Long idEntidad, String documentoInteresado) throws I18NException {

        Anexo anexo = anexoFull.getAnexo();

        ObtenerReferenciaWs referenciaWs = ReferenciaUnicaUtils.getObtenerReferenciaService(PropiedadGlobalUtil.getInterDocServer(idEntidad), PropiedadGlobalUtil.getInterDocUsername(idEntidad), PropiedadGlobalUtil.getInterDocPassword(idEntidad));

        //Preparamos el objeto a enviar
        ObtenerReferenciaRequestInfo obtenerReferenciaRequestInfo = new ObtenerReferenciaRequestInfo();
        obtenerReferenciaRequestInfo.setAplicacioId(anexo.getRegistroDetalle().getAplicacion());
        //TODO REVISAR SI SON OFICINAS O UNIDADES LO QUE SE TIENE QUE ENVIAR
        obtenerReferenciaRequestInfo.setEmisor(anexo.getRegistroDetalle().getOficinaOrigen().getCodigo());
        obtenerReferenciaRequestInfo.setReceptor(anexo.getRegistroDetalle().getCodigoEntidadRegistralDestino());

        if (anexo.isJustificante()) {
            obtenerReferenciaRequestInfo.setUuid(anexo.getCustodiaID());
        } else {
            //Preparamos el Documento a enviar
            Fitxer fitxer = new Fitxer();
            fitxer.setData(anexoFull.getData());
            fitxer.setDescripcio(anexoFull.getTituloCorto());
            fitxer.setMime(anexoFull.getMime());
            fitxer.setNom(anexoFull.getFileName());
            obtenerReferenciaRequestInfo.setDocument(fitxer);
            //Se envia el documento/codigodir3 del interesado
            obtenerReferenciaRequestInfo.getInteressats().add(documentoInteresado);
            obtenerReferenciaRequestInfo.setCsv("estoeselcsv");
            //obtenerReferenciaRequestInfo.setCsv(anexoFull.getAnexo().getCsv());

            //ENVIO DE METADATOS NECESARIOS PARA ARCHIVO
            List<Metadata> metadatosAnexo = anexoFull.getMetadatas();

            Metadada metadada;
            if (metadatosAnexo != null) {
                for (Metadata metadata : metadatosAnexo) {
                    metadada = new Metadada();
                    if (anexoFull.getAnexo().getScan()) { //Metadatos de escaneo
                        if (metadata.getKey().equals(MetadataConstants.EEMGDE_RESOLUCION)) {
                            metadada.setClau("eni:resolucion");
                            metadada.setValor(metadata.getValue());
                            obtenerReferenciaRequestInfo.getMetadades().add(metadada);
                        }
                        if (metadata.getKey().equals(MetadataConstants.EEMGDE_PROFUNDIDAD_COLOR)) {
                            metadada.setClau("eni:profundidad_color");
                            metadada.setValor(metadata.getValue());
                            obtenerReferenciaRequestInfo.getMetadades().add(metadada);
                        }
                        if (metadata.getKey().equals(MetadataConstants.EEMGDE_IDIOMA)) {
                            metadada.setClau("eni:idioma");
                            metadada.setValor(metadata.getValue());
                            obtenerReferenciaRequestInfo.getMetadades().add(metadada);
                        }
                    }
                    if (metadata.getKey().equals(MetadataConstants.ENI_DESCRIPCION)) {
                        metadada.setClau("eni:descripcion");
                        metadada.setValor(metadata.getValue());
                        obtenerReferenciaRequestInfo.getMetadades().add(metadada);
                    }

                }

            }
            //Metadata CM:TITLE
            metadada = new Metadada();
            metadada.setClau("cm:title");
            metadada.setValor(anexoFull.getAnexo().getTitulo());
            obtenerReferenciaRequestInfo.getMetadades().add(metadada);


            //Fecha de Captura
            metadada = new Metadada();
            GregorianCalendar fecha = new GregorianCalendar();
            fecha.setTime(anexoFull.getAnexo().getFechaCaptura());
            metadada.setClau("eni:fecha_inicio");
            DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_SICRES4);
            metadada.setValor(formatter.format(fecha.getTime()));
            obtenerReferenciaRequestInfo.getMetadades().add(metadada);

        }

        try {
            return referenciaWs.creaReferencia(obtenerReferenciaRequestInfo);
        } catch (Exception e) {
            throw new I18NException(e, "anexo.error.guardando.interdoc",
                    new I18NArgumentString(String.valueOf(anexoFull.getAnexo().getId())),
                    new I18NArgumentString(e.getMessage()));
        }


    }

    public static Fitxer obtenerDocumentoInterdoc(String referencia, String usuari, String clau, Long idEntidad) throws I18NException {

        ObtenerReferenciaWs referenciaWs = ReferenciaUnicaUtils.getObtenerReferenciaService(PropiedadGlobalUtil.getInterDocServer(idEntidad), PropiedadGlobalUtil.getInterDocUsername(idEntidad), PropiedadGlobalUtil.getInterDocPassword(idEntidad));

        try {
            return referenciaWs.descarregarDocument(referencia, usuari, clau);
        } catch (Exception e) {
            throw new I18NException(e, "anexo.error.guardando.interdoc",
                    new I18NArgumentString(referencia),
                    new I18NArgumentString(e.getMessage()));
        }


    }


    public static RegistroSir transformarAsientoBean(AsientoBean asientoBean, Entidad entidad) throws I18NException, InterException {
        final SimpleDateFormat SDF = new SimpleDateFormat(FORMATO_FECHA_SICRES4);

        RegistroSir registroSir = null;

        if (asientoBean != null) {
            registroSir = new RegistroSir();
            registroSir.setFechaRecepcion(new Date());

            //Entidad Registral Origen
            registroSir.setCodigoEntidadRegistralOrigen(asientoBean.getCdEnRgOrigen());
            if (StringUtils.isNotEmpty(asientoBean.getDsEnRgOrigen())) {
                registroSir.setDecodificacionEntidadRegistralOrigen(asientoBean.getDsEnRgOrigen());
            } else {
                registroSir.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()), asientoBean.getCdEnRgOrigen(), RegwebConstantes.OFICINA));
            }

            registroSir.setNumeroRegistro(asientoBean.getNuRgOrigen());
            registroSir.setFechaRegistro(asientoBean.getFeRgOrigen());
            registroSir.setTimestampRegistro(asientoBean.getTsRgOrigen());

            //Unidad Tramitación Origen
            registroSir.setCodigoUnidadTramitacionOrigen(asientoBean.getCdUnTrOrigen());
            if (StringUtils.isNotEmpty(asientoBean.getDsUnTrOrigen())) {
                registroSir.setDecodificacionUnidadTramitacionOrigen(asientoBean.getDsUnTrOrigen());
            } else {
                registroSir.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()), asientoBean.getCdUnTrOrigen(), RegwebConstantes.UNIDAD));
            }

            //Entidad Registral
            registroSir.setCodigoEntidadRegistral(asientoBean.getCdEnRgDestino());

            //Entidad Registral Destino
            registroSir.setCodigoEntidadRegistralDestino(asientoBean.getCdEnRgDestino());
            if (StringUtils.isNotEmpty(asientoBean.getDsEnRgDestino())) {
                registroSir.setDecodificacionEntidadRegistralDestino(asientoBean.getDsEnRgDestino());
            } else {
                registroSir.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()), asientoBean.getCdEnRgDestino(), RegwebConstantes.OFICINA));
            }
            //Unidad Tramitación Destino
            registroSir.setCodigoUnidadTramitacionDestino(asientoBean.getCdUnTrDestino());
            if (StringUtils.isNotEmpty(asientoBean.getDsUnTrDestino())) {
                registroSir.setDecodificacionUnidadTramitacionDestino(asientoBean.getDsUnTrDestino());
            } else {
                registroSir.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()), asientoBean.getCdUnTrDestino(), RegwebConstantes.UNIDAD));
            }

            registroSir.setResumen(asientoBean.getDsResumen());
            registroSir.setCodigoAsunto(asientoBean.getCdAsunto());
            registroSir.setReferenciaExterna(asientoBean.getRfExterna());
            registroSir.setNumeroExpediente(asientoBean.getNuExpediente());
            registroSir.setTipoTransporte(TipoTransporte.getTipoTransporteValue(asientoBean.getCdTpTransporte()));
            registroSir.setNombreUsuario(asientoBean.getNoUsuario());
            registroSir.setContactoUsuario(asientoBean.getCtUsuario());
            registroSir.setIdentificadorIntercambio(asientoBean.getCdIntercambio());
            registroSir.setAplicacion(asientoBean.getApVersion());
            registroSir.setTipoAnotacion(asientoBean.getCdTpAnotacion());
            registroSir.setDecodificacionTipoAnotacion(asientoBean.getDsTpAnotacion());
            registroSir.setTipoRegistro(TipoRegistro.getTipoRegistro(asientoBean.getCdTpRegistro()));

            //Documentación Física
            if (StringUtils.isNotEmpty(asientoBean.getCdDocFisica())) {
                registroSir.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisicaValue(asientoBean.getCdDocFisica()));
            }
            registroSir.setObservacionesApunte(asientoBean.getDsObservaciones());

            //Indicador de Prueba
            if (StringUtils.isNotEmpty(asientoBean.getCdInPrueba())) {
                registroSir.setIndicadorPrueba(IndicadorPrueba.getIndicadorPrueba(asientoBean.getCdInPrueba()));
            }

            //Entidad Registral Inicio
            registroSir.setCodigoEntidadRegistralInicio(asientoBean.getCdEnRgInicio());
            if (StringUtils.isNotEmpty(asientoBean.getDsEnRgInicio())) {
                registroSir.setDecodificacionEntidadRegistralOrigen(asientoBean.getDsEnRgInicio());
            } else {
                registroSir.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()), asientoBean.getCdEnRgInicio(), RegwebConstantes.OFICINA));
            }
            registroSir.setExpone(asientoBean.getDsExpone());
            registroSir.setSolicita(asientoBean.getDsSolicita());
            registroSir.setEstado(EstadoRegistroSir.getEstadoRegistroSir(asientoBean.getCdEstado())); // TODO DEFINIR NUEVOS ESTADOS LIBSIR
            //TODO CAMPOS NUEVOS SICRES4
            registroSir.setModoRegistro(asientoBean.getModoRegistro().substring(1));
            registroSir.setFechaRegistroPresentacion(asientoBean.getFeRgPresentacion());
            registroSir.setCodigoSia(asientoBean.getCdSia());
            registroSir.setReferenciaUnica(asientoBean.isReferenciaUnica()); //SI
            //  asientoBean.getCdIntercambioPrevio();// Este campo irá en RE /RS /RDETALLE (Revisado Edu)
            registroSir.setCodigoUnidadtramitacionInicio(asientoBean.getCdUnTrInicio()); //Campos nuevos de internos y control SI SE AÑADE
            registroSir.setDecodificacionUnidadTramitacionInicio(asientoBean.getDsUnTrInicio());

            //METADATOS GENERALES Y PARTICULARES
            Set<MetadatoRegistroSir> metadatos = asientoBean.getOtrosMetadatosGenerales().stream()
                    .map(metadato -> new MetadatoRegistroSir(METADATO_GENERAL, metadato.getCampo(), metadato.getValor()) {
                    }).collect(Collectors.toSet());

            metadatos.addAll(asientoBean.getOtrosMetadatosParticulares().stream()
                    .map(metadato -> new MetadatoRegistroSir(METADATO_PARTICULAR, metadato.getCampo(), metadato.getValor()) {
                    }).collect(Collectors.toSet()));

            registroSir.setMetadatosRegistroSir(metadatos);


            //INTERESADOS
            Set<es.gob.ad.registros.sir.interService.bean.InteresadoBean> interesadosBean = asientoBean.getInteresadosBean();
            if (!interesadosBean.isEmpty()) {
                for (es.gob.ad.registros.sir.interService.bean.InteresadoBean interesadoBean : interesadosBean) {
                    if (interesadoBean != null) {

                        // Si se trata de una Salida y no tiene Interesados
                        if (asientoBean.getCdTpRegistro().equals(TipoRegistro.SALIDA.getValue()) && StringUtils.isBlank(interesadoBean.getRazonSocialInteresado())
                                && (StringUtils.isBlank(interesadoBean.getNombreInteresado()) && StringUtils.isBlank(interesadoBean.getPrimerApellidoInteresado()))) {

                            // Creamos uno a partir de la Entidad destino
                            registroSir.getInteresados().add(crearInteresadoJuridicoAsientoBean(asientoBean.getCdUnTrDestino(), asientoBean.getDsUnTrDestino(), asientoBean.getCdEnRgDestino(), entidad.getId()));

                        } else {
                            InteresadoSir interesadoSir = transformarInteresadoBean(interesadoBean);
                            registroSir.getInteresados().add(interesadoSir);
                        }

                    }
                }
            }

            //ANEXOS
            Set<es.gob.ad.registros.sir.interService.bean.AnexoBean> anexosBean = asientoBean.getAnexosBean();
            if (!anexosBean.isEmpty()) {
                for (es.gob.ad.registros.sir.interService.bean.AnexoBean anexoBean : anexosBean) {
                    if (anexoBean != null) {
                        AnexoSir anexo = transformarAnexoBean(anexoBean, asientoBean.getCdIntercambio());
                        registroSir.getAnexos().add(anexo);
                    }
                }
            }

        }

        return registroSir;
    }

    /**
     * Crea un Interesado tipo Persona Juridica a partir del Código Unidad De Gestión de destino o si no está informado,
     * a partir del Código Entidad Registral de destino
     *
     * @return
     */
    private static InteresadoSir crearInteresadoJuridicoAsientoBean(String cdUnTrDestino, String dsUnTrDestino, String cdEnRgDestino, Long idEntidad) {

        InteresadoSir interesadoSalida = new InteresadoSir();

        if (StringUtils.isNotBlank(cdUnTrDestino)) {

            interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
            interesadoSalida.setDocumentoIdentificacionInteresado(cdUnTrDestino);

            if (StringUtils.isNotBlank(dsUnTrDestino)) {
                interesadoSalida.setRazonSocialInteresado(dsUnTrDestino);
            } else {
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), cdUnTrDestino, RegwebConstantes.UNIDAD));

            }


        } else {
            try {
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));

                OficinaTF oficinaTF = oficinasService.obtenerOficina(cdEnRgDestino, null, null);

                interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
                interesadoSalida.setDocumentoIdentificacionInteresado(oficinaTF.getCodUoResponsable());
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), oficinaTF.getCodUoResponsable(), RegwebConstantes.UNIDAD));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return interesadoSalida;
    }

    private static InteresadoSir transformarInteresadoBean(InteresadoBean interesadoBean) throws I18NException {
        InteresadoSir interesado = new InteresadoSir();

        // Información del interesado
        interesado.setDocumentoIdentificacionInteresado(interesadoBean.getDocumentoIdentificacionInteresado());
        interesado.setRazonSocialInteresado(interesadoBean.getRazonSocialInteresado());
        interesado.setNombreInteresado(interesadoBean.getNombreInteresado());
        interesado.setPrimerApellidoInteresado(interesadoBean.getPrimerApellidoInteresado());
        interesado.setSegundoApellidoInteresado(interesadoBean.getSegundoApellidoInteresado());
        interesado.setCodigoPaisInteresado(interesadoBean.getPaisInteresado());
        interesado.setCodigoProvinciaInteresado(interesadoBean.getProvinciaInteresado());
        interesado.setCodigoMunicipioInteresado(interesadoBean.getMunicipioInteresado());
        interesado.setDireccionInteresado(interesadoBean.getDireccionInteresado());
        interesado.setCodigoPostalInteresado(interesadoBean.getCodPostalInteresado());
        interesado.setCorreoElectronicoInteresado(interesadoBean.getCorreoElectronicoInteresado());
        interesado.setTelefonoInteresado(interesadoBean.getTelefonoFijoInteresado());
        interesado.setTelefonoMovilInteresado(interesadoBean.getTelefonoMovilInteresado());

        String tipoDocumento = interesadoBean.getTipoDocumentoIdentificacionInteresado();
        if (StringUtils.isNotBlank(tipoDocumento)) {
            interesado.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
        }

        String canalPreferente = interesadoBean.getCanalPreferenteComunicacionInteresado();
        if (StringUtils.isNotBlank(canalPreferente)) {
            interesado.setCanalPreferenteComunicacionInteresado(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
        }

        //CAMPOS NUEVOS SICRES4 TODO AVISOS SMS Y CORREOELECTRONICO
        interesado.setAvisoCorreoElectronicoInteresado(interesadoBean.isSolicitaNotificacionEmailInteresado());
        interesado.setAvisoNotificacionSMSInteresado(interesadoBean.isSolicitaNotificacionSMSInteresado());
        interesado.setReceptorNotificacionesInteresado(interesadoBean.isReceptorNotificacionInteresado());
        interesado.setCodigoDirectorioUnificadosInteresado(interesadoBean.getCodigoDirectoriosUnificadosInteresado());

        // Información del representante
        interesado.setDocumentoIdentificacionRepresentante(interesadoBean.getDocumentoIdentificacionRepresentante());
        interesado.setRazonSocialRepresentante(interesadoBean.getRazonSocialRepresentante());
        interesado.setNombreRepresentante(interesadoBean.getNombreRepresentante());
        interesado.setPrimerApellidoRepresentante(interesadoBean.getPrimerApellidoRepresentante());
        interesado.setSegundoApellidoRepresentante(interesadoBean.getSegundoApellidoRepresentante());
        interesado.setCodigoPaisRepresentante(interesadoBean.getPaisRepresentante());
        interesado.setCodigoProvinciaRepresentante(interesadoBean.getProvinciaRepresentante());
        interesado.setCodigoMunicipioRepresentante(interesadoBean.getMunicipioRepresentante());
        interesado.setDireccionRepresentante(interesadoBean.getDireccionRepresentante());
        interesado.setCodigoPostalRepresentante(interesadoBean.getCodPostalRepresentante());
        interesado.setCorreoElectronicoRepresentante(interesadoBean.getCorreoElectronicoRepresentante());
        interesado.setTelefonoRepresentante(interesadoBean.getTelefonoFijoRepresentante());
        interesado.setTelefonoMovilRepresentante(interesadoBean.getTelefonoMovilInteresado());

        tipoDocumento = interesadoBean.getTipoDocumentoIdentificacionRepresentante();
        if (StringUtils.isNotBlank(tipoDocumento)) {
            interesado.setTipoDocumentoIdentificacionRepresentante(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
        }

        canalPreferente = interesadoBean.getCanalPreferenteComunicacionRepresentante();
        if (StringUtils.isNotBlank(canalPreferente)) {
            interesado.setCanalPreferenteComunicacionRepresentante(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
        }

        //CAMPOS NUEVOS SICRES4 TODO AVISOS SMS Y CORREOELECTRONICO
        interesado.setAvisoCorreoElectronicoRepresentante(interesadoBean.isSolicitaNotificacionEmailRepresentante());
        interesado.setAvisoCorreoElectronicoRepresentante(interesadoBean.isSolicitaNotificacionSMSRepresentante());
        interesado.setReceptorNotificacionesRepresentante(interesadoBean.isReceptorNotificacionRepresentante());
        interesado.setCodigoDirectorioUnificadosRepresentante(interesadoBean.getCodigoDirectoriosUnificadosRepresentante());

        interesado.setObservaciones(interesadoBean.getObservaciones());

        return interesado;
    }


    private static AnexoSir transformarAnexoBean(AnexoBean anexoBean, String idIntercambio) throws I18NException, InterException {
        AnexoSir anexo = new AnexoSir();

        anexo.setNombreFichero(es.caib.regweb3.utils.StringUtils.eliminarCaracteresProhibidosArxiu(anexoBean.getNombreFichero()));
        anexo.setIdentificadorFichero(anexoBean.getIdentificadorFichero());
        anexo.setIdentificadorDocumentoFirmado(anexoBean.getIdentificadorDocumentoFirmado());
        anexo.setTipoMIME(anexoBean.getContenidoBean().getNombreFormato());
        anexo.setObservaciones(anexoBean.getObservaciones());

        String tipoAnexo = anexoBean.getTipoAnexo();
        if (StringUtils.isNotBlank(tipoAnexo)) {
            anexo.setTipoDocumento(TipoDocumento.getTipoDocumentoValue(tipoAnexo));
        }

        //CAMPOS NUEVOS SICRES4
        anexo.setResumen(anexoBean.getResumen());
        anexo.setCodigoFormulario(anexoBean.getCodigoFormulario());
        anexo.setUrlRepositorio(anexoBean.getUrlRepositorio());

        //FirmaBean
        FirmaBean firmaBean = anexoBean.getFirmaBean();
        if (firmaBean != null) {

            anexo.setTipoFirma(firmaBean.getTipoFirma() != null ? firmaBean.getTipoFirma().value() : "");
            if (firmaBean.getContenidoFirma() != null && firmaBean.getContenidoFirma().getCsv() != null) {
                anexo.setCsv(firmaBean.getContenidoFirma().getCsv() != null ? firmaBean.getContenidoFirma().getCsv().getValorCSV() : "");
                anexo.setRegulacionCsv(firmaBean.getContenidoFirma().getCsv() != null ? firmaBean.getContenidoFirma().getCsv().getRegulacionGeneracionCSV() : "");
            }
            if (firmaBean.getContenidoFirma() != null && firmaBean.getContenidoFirma().getFirmaConCertificadoBean() != null) {
                anexo.setFirmaBase64(firmaBean.getContenidoFirma().getFirmaConCertificadoBean() != null ? firmaBean.getContenidoFirma().getFirmaConCertificadoBean().getFirmaBase64() : null);
                //Incidencia 1900484
                anexo.setReferenciaFirma(firmaBean.getContenidoFirma().getFirmaConCertificadoBean()!=null?(String)firmaBean.getContenidoFirma().getFirmaConCertificadoBean().getReferenciaFirma():"");
            }
        }

        //METADATOS SICRES4
        Set<MetadatoAnexoSir> metadatosAnexos = anexoBean.getOtrosMetadatosGenerales().stream()
                .map(metadato -> new MetadatoAnexoSir(METADATO_GENERAL, metadato.getCampo(), metadato.getValor()) {
                }).collect(Collectors.toSet());

        metadatosAnexos.addAll(anexoBean.getOtrosMetadatosParticulares().stream()
                .map(metadato -> new MetadatoAnexoSir(METADATO_PARTICULAR, metadato.getCampo(), metadato.getValor()) {
                }).collect(Collectors.toSet()));


        // LIBSIR estos son los metadatos ENI (documentoElectrónico)
        TipoMetadatos tiposMetadato = anexoBean.getTipoMetadatos();

        MetadatoAnexoSir metadatoAnexoSir;

        DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_SICRES4);

        if (tiposMetadato != null) {
            //fechaCaptura
            metadatoAnexoSir = new MetadatoAnexoSir(METADATO_NTI, "fechaCaptura", formatter.format(tiposMetadato.getFechaCaptura().toGregorianCalendar().getTime()));
          //  metadatoAnexoSir = new MetadatoAnexoSir(METADATO_NTI, "fechaCaptura", formatter.format(new Date()));
            metadatosAnexos.add(metadatoAnexoSir);

            //origenCiudadanoAdministracion
            metadatoAnexoSir = new MetadatoAnexoSir(METADATO_NTI, "origenCiudadanoAdministracion", tiposMetadato.isOrigenCiudadanoAdministracion() ? "1" : "0");
         //   metadatoAnexoSir = new MetadatoAnexoSir(METADATO_NTI, "origenCiudadanoAdministracion", "1");
            metadatosAnexos.add(metadatoAnexoSir);

            //tipoDocumental
            metadatoAnexoSir = new MetadatoAnexoSir(METADATO_NTI, "tipoDocumental", tiposMetadato.getTipoDocumental().value());
           // metadatoAnexoSir = new MetadatoAnexoSir(METADATO_NTI, "tipoDocumental", "TD01");
            metadatosAnexos.add(metadatoAnexoSir);

        }

        anexo.setMetadatosAnexos(metadatosAnexos);
        return anexo;
    }
}
