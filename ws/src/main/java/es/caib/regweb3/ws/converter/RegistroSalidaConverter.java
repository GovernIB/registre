package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb3.persistence.ejb.OficinaLocal;
import es.caib.regweb3.persistence.ejb.TipoAsuntoLocal;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.model.RegistroSalidaResponseWs;
import es.caib.regweb3.ws.model.RegistroSalidaWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 * Conversor entre las clases {@link es.caib.regweb3.ws.model.RegistroSalidaWs} y {@link es.caib.regweb3.model.RegistroSalida}
 * @author earrivi
 */
public class RegistroSalidaConverter extends CommonConverter {

    /**
     * Convierte un {@link es.caib.regweb3.ws.model.RegistroSalidaWs} en un {@link es.caib.regweb3.model.RegistroSalida}
     * @param registroSalidaWs
     * @return
     * @throws Exception
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
     */
    public static RegistroSalida getRegistroSalida(RegistroSalidaWs registroSalidaWs,
        UsuarioEntidad usuario,Libro libro, Oficina oficina,
        Organismo organismo,  CodigoAsuntoLocal codigoAsuntoEjb,
        TipoAsuntoLocal tipoAsuntoEjb, OficinaLocal oficinaEjb) throws Exception, I18NException {

        if (registroSalidaWs == null){
            return  null;
        }

        RegistroSalida registroSalida = new RegistroSalida();
        RegistroDetalle registroDetalle = new RegistroDetalle();
        registroDetalle.setPresencial(false);

        registroSalida.setOrigen(organismo);
        registroSalida.setOficina(oficina);
        registroSalida.setFecha(new Date());
        registroSalida.setUsuario(usuario);
        registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);
        registroSalida.setLibro(libro);
        registroDetalle.setExtracto(registroSalidaWs.getExtracto());
        registroDetalle.setTipoDocumentacionFisica(registroSalidaWs.getDocFisica());
        registroDetalle.setTipoAsunto(getTipoAsunto(registroSalidaWs.getTipoAsunto(), usuario.getEntidad().getId(), tipoAsuntoEjb));
        registroDetalle.setIdioma(getIdiomaRegistro(registroSalidaWs.getIdioma()));

        if (StringUtils.isNotEmpty(registroSalidaWs.getCodigoAsunto())) {registroDetalle.setCodigoAsunto(getCodigoAsunto(registroSalidaWs.getCodigoAsunto(), codigoAsuntoEjb));}
        if (StringUtils.isNotEmpty(registroSalidaWs.getRefExterna())) {registroDetalle.setReferenciaExterna(registroSalidaWs.getRefExterna());}
        if (StringUtils.isNotEmpty(registroSalidaWs.getNumExpediente())) {registroDetalle.setExpediente(registroSalidaWs.getNumExpediente());}
        if (StringUtils.isNotEmpty(registroSalidaWs.getTipoTransporte())) {registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(registroSalidaWs.getTipoTransporte()));}
        if (StringUtils.isNotEmpty(registroSalidaWs.getNumTransporte())) {registroDetalle.setNumeroTransporte(registroSalidaWs.getNumTransporte());}
        if (StringUtils.isNotEmpty(registroSalidaWs.getObservaciones())) {registroDetalle.setObservaciones(registroSalidaWs.getObservaciones());}
        //registroDetalle = getOficinaOrigen(registroSalidaWs.getOficina(),oficinaEjb, registroDetalle); todo Crear propiedad OficinaOrigen en es.caib.regweb3.ws.model.RegistroWs
        registroDetalle.setOficinaOrigen(oficina);
        if(registroSalidaWs.getNumero() != null){registroDetalle.setNumeroRegistroOrigen(String.valueOf(registroSalidaWs.getNumero()));}
        if (registroSalidaWs.getFecha() != null) {registroDetalle.setFechaOrigen(registroSalidaWs.getFecha());}
        if (StringUtils.isNotEmpty(registroSalidaWs.getExpone())) {registroDetalle.setExpone(registroSalidaWs.getExpone());}
        if (StringUtils.isNotEmpty(registroSalidaWs.getSolicita())) {registroDetalle.setSolicita(registroSalidaWs.getSolicita());}
        if (StringUtils.isNotEmpty(registroSalidaWs.getAplicacion())) {registroDetalle.setAplicacion(registroSalidaWs.getAplicacion());}
        if (StringUtils.isNotEmpty(registroSalidaWs.getVersion())) {registroDetalle.setVersion(registroSalidaWs.getVersion());}

        registroSalida.setRegistroDetalle(registroDetalle);

        return registroSalida;
    }

    public static RegistroSalidaResponseWs getRegistroSalidaResponseWs(RegistroSalida registroSalida,
                                                                         String idioma) throws Exception, I18NException {

        if (registroSalida == null) {
            return null;
        }

        Entidad entidad = registroSalida.getEntidad();

        // Creamos los datos comunes mediante RegistroWs
        RegistroSalidaResponseWs registroWs = new RegistroSalidaResponseWs();
        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        registroWs.setEntidadCodigo(entidad.getCodigoDir3());
        registroWs.setEntidadDenominacion(entidad.getNombre());

        registroWs.setNumeroRegistro(registroSalida.getNumeroRegistro());
        registroWs.setNumeroRegistroFormateado(registroSalida.getNumeroRegistroFormateado());
        registroWs.setFechaRegistro(registroSalida.getFecha());

        registroWs.setCodigoUsuario(registroSalida.getUsuario().getUsuario().getIdentificador());
        registroWs.setNombreUsuario(registroSalida.getUsuario().getNombreCompleto());
        registroWs.setContactoUsuario(registroSalida.getUsuario().getUsuario().getEmail());

        registroWs.setOficinaCodigo(registroSalida.getOficina().getCodigo());
        registroWs.setOficinaDenominacion(registroSalida.getOficina().getDenominacion());
        registroWs.setLibroCodigo(registroSalida.getLibro().getCodigo());
        registroWs.setLibroDescripcion(registroSalida.getLibro().getNombre());

        registroWs.setExtracto(registroDetalle.getExtracto());
        registroWs.setDocFisicaCodigo(registroDetalle.getTipoDocumentacionFisica().toString());
        registroWs.setDocFisicaDescripcion(I18NLogicUtils.tradueix(new Locale(idioma), "tipoDocumentacionFisica." + registroDetalle.getTipoDocumentacionFisica()));

        if(registroDetalle.getTipoAsunto() != null){
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroDetalle.getTipoAsunto().getTraduccion(idioma);
            registroWs.setTipoAsuntoCodigo(registroDetalle.getTipoAsunto().getCodigo());
            registroWs.setTipoAsuntoDescripcion(traduccionTipoAsunto.getNombre());
        }

        registroWs.setIdiomaCodigo(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(registroDetalle.getIdioma()));
        registroWs.setIdiomaDescripcion(I18NLogicUtils.tradueix(new Locale(idioma), "idioma." + registroDetalle.getIdioma()));

        if(registroDetalle.getCodigoAsunto() != null){
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(idioma);
            registroWs.setCodigoAsuntoCodigo(registroDetalle.getCodigoAsunto().getCodigo());
            registroWs.setCodigoAsuntoDescripcion(traduccionCodigoAsunto.getNombre());
        }else{
            registroWs.setCodigoAsuntoCodigo(null);
            registroWs.setCodigoAsuntoDescripcion(null);
        }

        registroWs.setRefExterna(registroDetalle.getReferenciaExterna());
        registroWs.setNumExpediente(registroDetalle.getExpediente());

        if(registroDetalle.getTransporte() != null){
            registroWs.setTipoTransporteCodigo(RegwebConstantes.CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
            registroWs.setTipoTransporteDescripcion(I18NLogicUtils.tradueix(new Locale(idioma), "transporte." + registroDetalle.getTransporte()));
        }else{
            registroWs.setTipoTransporteCodigo(null);
            registroWs.setTipoTransporteDescripcion(null);
        }

        registroWs.setNumTransporte(registroDetalle.getNumeroTransporte());
        registroWs.setObservaciones(registroDetalle.getObservaciones());
        registroWs.setNumeroRegistroOrigen(registroDetalle.getNumeroRegistroOrigen());
        registroWs.setFechaOrigen(registroDetalle.getFechaOrigen());
        registroWs.setAplicacion(registroDetalle.getAplicacion());
        registroWs.setVersion(registroDetalle.getVersion());

        registroWs.setExpone(registroDetalle.getExpone());
        registroWs.setSolicita(registroDetalle.getSolicita());

        //Interesados
        if(registroDetalle.getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroDetalle.getInteresados());

            registroWs.setInteresados(interesadosWs);
        }

        // Anexos
        List<AnexoWs> anexosWs = transformarAnexosWs(registroDetalle);
        registroWs.setAnexos(anexosWs);

        // Campos únicos de RegistroEntrada
        if(registroSalida.getOrigen() != null ){
            registroWs.setOrigenCodigo(registroSalida.getOrigen().getCodigo());
            registroWs.setOrigenDenominacion(registroSalida.getOrigen().getDenominacion());
        }else{
            registroWs.setOrigenCodigo(registroSalida.getOrigenExternoCodigo());
            registroWs.setOrigenDenominacion(registroSalida.getOrigenExternoDenominacion());
        }

        return registroWs;

    }
}
