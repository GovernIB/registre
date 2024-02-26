package es.caib.regweb3.ws.converter;

import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb3.persistence.ejb.OficinaLocal;
import es.caib.regweb3.persistence.ejb.TipoAsuntoLocal;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.model.RegistroEntradaResponseWs;
import es.caib.regweb3.ws.model.RegistroEntradaWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 * Conversor entre las clases {@link es.caib.regweb3.ws.model.RegistroEntradaWs} y {@link es.caib.regweb3.model.RegistroEntrada}
 * @author earrivi
 */
public class RegistroEntradaConverter extends CommonConverter {

    /**
     * Convierte un {@link es.caib.regweb3.ws.model.RegistroEntradaWs} en un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param registroEntradaWs
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public static RegistroEntrada getRegistroEntrada(RegistroEntradaWs registroEntradaWs, UsuarioEntidad usuarioAplicacion,
                                                     UsuarioEntidad usuario, Libro libro, Oficina oficina, Organismo destinoInterno, UnidadTF destinoExterno,
        CodigoAsuntoLocal codigoAsuntoEjb, TipoAsuntoLocal tipoAsuntoEjb, OficinaLocal oficinaEjb)
            throws Exception {

        if (registroEntradaWs == null){
            return  null;
        }

        RegistroEntrada registroEntrada = new RegistroEntrada();
        RegistroDetalle registroDetalle = new RegistroDetalle();

        // Presencial
        if(Configuracio.isCAIB() && usuarioAplicacion.getUsuario().getIdentificador().equals("$sistra_regweb")){
            // Script temporal para identificar los Pre-Registros desde Sistra1
            registroDetalle.setPresencial(!usuarioAplicacion.getId().equals(usuario.getId())); // Los marcamos como presenciales para poder editarlos
            registroDetalle.setObservaciones("Origen pre-registre Sistra");
        }else{
            registroDetalle.setPresencial(false);
        }


        if (destinoInterno == null) {
            registroEntrada.setDestino(null);
            registroEntrada.setDestinoExternoCodigo(destinoExterno.getCodigo());
            registroEntrada.setDestinoExternoDenominacion(destinoExterno.getDenominacion());
        } else {
            registroEntrada.setDestino(destinoInterno);
        }

        registroEntrada.setOficina(oficina);
        registroEntrada.setFecha(new Date());
        registroEntrada.setUsuario(usuario);
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
        registroEntrada.setLibro(libro);
        registroDetalle.setExtracto(registroEntradaWs.getExtracto());
        registroDetalle.setTipoDocumentacionFisica(registroEntradaWs.getDocFisica());
        registroDetalle.setTipoAsunto(getTipoAsunto(registroEntradaWs.getTipoAsunto(),usuario.getEntidad().getId(), tipoAsuntoEjb));
        registroDetalle.setIdioma(getIdiomaRegistro(registroEntradaWs.getIdioma()));

        if(StringUtils.isNotEmpty(registroEntradaWs.getCodigoAsunto())){registroDetalle.setCodigoAsunto(getCodigoAsunto(registroEntradaWs.getCodigoAsunto(), codigoAsuntoEjb));}
        if(StringUtils.isNotEmpty(registroEntradaWs.getRefExterna())){registroDetalle.setReferenciaExterna(registroEntradaWs.getRefExterna());}
        if(StringUtils.isNotEmpty(registroEntradaWs.getNumExpediente())){registroDetalle.setExpediente(registroEntradaWs.getNumExpediente());}
        if(StringUtils.isNotEmpty(registroEntradaWs.getTipoTransporte())){registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(registroEntradaWs.getTipoTransporte()));}
        if(StringUtils.isNotEmpty(registroEntradaWs.getNumTransporte())){registroDetalle.setNumeroTransporte(registroEntradaWs.getNumTransporte());}
        if(StringUtils.isNotEmpty(registroEntradaWs.getObservaciones())){registroDetalle.setObservaciones(registroEntradaWs.getObservaciones());}
        //registroDetalle = getOficinaOrigen(registroEntradaWs.getOficina(),oficinaEjb, registroDetalle); todo Crear propiedad OficinaOrigen en es.caib.regweb3.ws.model.RegistroWs
        registroDetalle.setOficinaOrigen(oficina);
        if(registroEntradaWs.getNumero() != null){registroDetalle.setNumeroRegistroOrigen(String.valueOf(registroEntradaWs.getNumero()));}
        if(registroEntradaWs.getFecha() != null){registroDetalle.setFechaOrigen(registroEntradaWs.getFecha());}
        if(StringUtils.isNotEmpty(registroEntradaWs.getExpone())){registroDetalle.setExpone(registroEntradaWs.getExpone());}
        if(StringUtils.isNotEmpty(registroEntradaWs.getSolicita())){registroDetalle.setSolicita(registroEntradaWs.getSolicita());}
        if(StringUtils.isNotEmpty(registroEntradaWs.getAplicacion())){registroDetalle.setAplicacion(registroEntradaWs.getAplicacion());}
        if(StringUtils.isNotEmpty(registroEntradaWs.getVersion())){registroDetalle.setVersion(registroEntradaWs.getVersion());}

        registroEntrada.setRegistroDetalle(registroDetalle);

        return registroEntrada;
    }


    public static RegistroEntradaResponseWs getRegistroEntradaResponseWs(RegistroEntrada registroEntrada,
                                            String idioma) throws Exception {

        if (registroEntrada == null) {
            return null;
        }

        Entidad entidad = registroEntrada.getEntidad();

        // Creamos los datos comunes mediante RegistroWs
        RegistroEntradaResponseWs registroWs = new RegistroEntradaResponseWs();
        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        registroWs.setEntidadCodigo(entidad.getCodigoDir3());
        registroWs.setEntidadDenominacion(entidad.getNombre());

        registroWs.setNumeroRegistro(registroEntrada.getNumeroRegistro());
        registroWs.setNumeroRegistroFormateado(registroEntrada.getNumeroRegistroFormateado());
        registroWs.setFechaRegistro(registroEntrada.getFecha());

        registroWs.setCodigoUsuario(registroEntrada.getUsuario().getUsuario().getIdentificador());
        registroWs.setNombreUsuario(registroEntrada.getUsuario().getNombreCompleto());
        registroWs.setContactoUsuario(registroEntrada.getUsuario().getUsuario().getEmail());

        registroWs.setOficinaCodigo(registroEntrada.getOficina().getCodigo());
        registroWs.setOficinaDenominacion(registroEntrada.getOficina().getDenominacion());
        registroWs.setLibroCodigo(registroEntrada.getLibro().getCodigo());
        registroWs.setLibroDescripcion(registroEntrada.getLibro().getNombre());

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
        if(registroEntrada.getDestino() != null ){
            registroWs.setDestinoCodigo(registroEntrada.getDestino().getCodigo());
            registroWs.setDestinoDenominacion(registroEntrada.getDestino().getDenominacion());
        }else{
            registroWs.setDestinoCodigo(registroEntrada.getDestinoExternoCodigo());
            registroWs.setDestinoDenominacion(registroEntrada.getDestinoExternoDenominacion());
        }

        return registroWs;
    }

}
