package es.caib.regweb3.persistence.utils;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.interdoc.ws.api.Fitxer;
import es.caib.interdoc.ws.api.Metadada;
import es.caib.interdoc.ws.api.ObtenerReferenciaRequestInfo;
import es.caib.interdoc.ws.api.ObtenerReferenciaWs;
import es.caib.regweb3.model.*;

import es.caib.regweb3.model.sir.TipoDocumentoIdentificacion;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.ReferenciaUnicaUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.gob.ad.registros.sir.gestionEni.bean.ContenidoBean;
import es.gob.ad.registros.sir.interService.bean.*;
import es.gob.ad.registros.sir.interService.interSincroDIR3.service.IServiciosOfiService;
import es.gob.administracionelectronica.eni.xsd.v1_0.documento_e.metadatos.TipoDocumental;
import es.gob.administracionelectronica.eni.xsd.v1_0.documento_e.metadatos.TipoMetadatos;
import org.apache.commons.lang.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /**
     * Transforma un registro de Entrada a un AsientoBean para enviarlo mediante la libreria SIR.
     *
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    public AsientoBean transformarRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException, DatatypeConfigurationException {

        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        AsientoBean asientoBean = new AsientoBean();
        if (registroEntrada != null) {

            //Entidad Registral Origen
            asientoBean.setCdEnRgOrigen(registroEntrada.getOficina().getCodigo());
            asientoBean.setDsEnRgOrigen(registroEntrada.getOficina().getDenominacion());
            asientoBean.setNuRgOrigen(registroEntrada.getNumeroRegistroFormateado());
            asientoBean.setFeRgOrigen(registroEntrada.getFecha());
            asientoBean.setFeRgPresentacion(registroEntrada.getFecha());
            asientoBean.setTsRgOrigen(new Timestamp(registroEntrada.getFecha().getTime()).toString());

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

            transformarAnexos(registroEntrada.getRegistroDetalle().getAnexosFull(), asientoBean, registroDetalle.getIdentificadorIntercambio());

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
            asientoBean.setTsRgOrigen(new Timestamp(registroSalida.getFecha().getTime()).toString());

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


            transformarAnexos(registroSalida.getRegistroDetalle().getAnexosFull(), asientoBean, registroDetalle.getIdentificadorIntercambio());
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


    private AnexoBean transformarAnexo(Anexo anexo, int secuencia, String identificadorIntercambio) throws DatatypeConfigurationException {

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
        contenidoBean.setNombreFormato("application/xml");
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

        return anexoBean;
    }

    private void transformarAnexos(List<AnexoFull> anexosFull, AsientoBean asientoBean, String identificadorIntercambio) throws DatatypeConfigurationException {

        int secuencia = 0;
        if (!anexosFull.isEmpty()) {
            for (AnexoFull anexoFull : anexosFull) {
                if (anexoFull != null) {
                    es.gob.ad.registros.sir.interService.bean.AnexoBean anexoBean;

                    Anexo anexo = anexoFull.getAnexo();
                    anexoBean = transformarAnexo(anexo, secuencia, identificadorIntercambio);
                    secuencia++;
                    asientoBean.getAnexosBean().add(anexoBean);
                }
            }
        }

    }

    private InteresadoBean transformarInteresado(Interesado interesado) {

        //  InteresadoSir interesado = new InteresadoSir();
        es.gob.ad.registros.sir.interService.bean.InteresadoBean interesadoBean = new InteresadoBean();

        // Información del interesado
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


        Long tipoDocumento = interesado.getTipoDocumentoIdentificacion();
        if (tipoDocumento != null) {
            interesadoBean.setTipoDocumentoIdentificacionInteresado(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(tipoDocumento)));
        }

        Long canalPreferente = interesado.getCanal();
        if (canalPreferente != null) {
            interesadoBean.setCanalPreferenteComunicacionInteresado(CODIGO_BY_CANALNOTIFICACION.get(canalPreferente));
        }

        //CAMPOS NUEVOS SICRES4
        interesadoBean.setSolicitaNotificacionEmailInteresado(interesado.getAvisoCorreoElectronico());
        interesadoBean.setSolicitaNotificacionSMSInteresado(interesado.getAvisoNotificacionSMS());
        interesadoBean.setReceptorNotificacionInteresado(interesado.getReceptorNotificaciones());
        interesadoBean.setCodigoDirectoriosUnificadosInteresado(interesado.getCodDirectoriosUnificados());

        // Información del representante
        Interesado representante = interesado.getRepresentante();
        if (representante != null) {
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
            if (canalPreferente != null) {
                interesadoBean.setCanalPreferenteComunicacionRepresentante(CODIGO_BY_CANALNOTIFICACION.get(canalPreferente));
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
    private String obtenerDenominacionUnidadTramitacionDestino(RegistroDetalle registroDetalle) {

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
    private String obtenerCodigoUnidadTramitacionDestino(RegistroDetalle registroDetalle) {

        List<Interesado> interesados = registroDetalle.getInteresados();

        for (Interesado interesado : interesados) {
            if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {

                return interesado.getCodigoDir3();
            }
        }

        return null;
    }

    public String guardarDocumentoInterdoc(AnexoFull anexoFull, Long idEntidad, String documentoInteresado) throws I18NException {

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
            metadada.setValor(formatter.format(anexoFull.getAnexo().getFechaCaptura()));
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
}
