package es.caib.regweb3.ws.converter;

import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.model.AsientoRegistralWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.Date;
import java.util.List;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;
import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_SALIDA;

/**
 * Created by mgonzalez on 14/02/2019.
 */
public class AsientoRegistralConverter extends CommonConverter {


   /**
    * Convierte un {@link es.caib.regweb3.ws.model.RegistroEntradaWs} en un {@link es.caib.regweb3.model.RegistroEntrada}
    * @param asientoRegistral
    * @return
    * @throws Exception
    * @throws I18NException
    */
   public static RegistroEntrada getRegistroEntrada(AsientoRegistralWs asientoRegistral,
                                                    UsuarioEntidad usuario, Libro libro, Oficina oficina, Organismo destinoInterno, UnidadTF destinoExterno,
                                                    CodigoAsuntoLocal codigoAsuntoEjb, TipoAsuntoLocal tipoAsuntoEjb)
      throws Exception, I18NException {

      if (asientoRegistral == null){
         return  null;
      }

      RegistroEntrada registroEntrada = new RegistroEntrada();
      RegistroDetalle registroDetalle = new RegistroDetalle();

      //Marcamos como no presencial
      registroDetalle.setPresencial(false);

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
      registroDetalle.setExtracto(asientoRegistral.getResumen()); //Extracto
      registroDetalle.setTipoDocumentacionFisica(new Long(asientoRegistral.getTipoDocumentacionFisicaCodigo()));
      registroDetalle.setTipoAsunto(getTipoAsunto(asientoRegistral.getTipoAsunto(),usuario.getEntidad().getId(), tipoAsuntoEjb));
      registroDetalle.setIdioma(asientoRegistral.getIdioma());
      registroDetalle.setCodigoSia(asientoRegistral.getCodigoSia());

      if(StringUtils.isNotEmpty(asientoRegistral.getCodigoAsunto())){registroDetalle.setCodigoAsunto(getCodigoAsunto(asientoRegistral.getCodigoAsunto(), codigoAsuntoEjb));}
      if(StringUtils.isNotEmpty(asientoRegistral.getReferenciaExterna())){registroDetalle.setReferenciaExterna(asientoRegistral.getReferenciaExterna());}
      if(StringUtils.isNotEmpty(asientoRegistral.getNumeroExpediente())){registroDetalle.setExpediente(asientoRegistral.getNumeroExpediente());}
      if(StringUtils.isNotEmpty(asientoRegistral.getTipoTransporte())){registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(asientoRegistral.getTipoTransporte()));}
      if(StringUtils.isNotEmpty(asientoRegistral.getNumeroTransporte())){registroDetalle.setNumeroTransporte(asientoRegistral.getNumeroTransporte());}
      if(StringUtils.isNotEmpty(asientoRegistral.getObservaciones())){registroDetalle.setObservaciones(asientoRegistral.getObservaciones());}
      registroDetalle.setOficinaOrigen(oficina);
      if(StringUtils.isNotEmpty(asientoRegistral.getExpone())){registroDetalle.setExpone(asientoRegistral.getExpone());}
      if(StringUtils.isNotEmpty(asientoRegistral.getSolicita())){registroDetalle.setSolicita(asientoRegistral.getSolicita());}
      if(StringUtils.isNotEmpty(asientoRegistral.getAplicacion())){registroDetalle.setAplicacion(asientoRegistral.getAplicacionTelematica());}
      if(StringUtils.isNotEmpty(asientoRegistral.getVersion())){registroDetalle.setVersion(asientoRegistral.getVersion());}

      registroEntrada.setRegistroDetalle(registroDetalle);

      return registroEntrada;
   }


   /**
    * Convierte un {@link es.caib.regweb3.ws.model.RegistroSalidaWs} en un {@link es.caib.regweb3.model.RegistroSalida}
    * @param asientoRegistral
    * @return
    * @throws Exception
    * @throws org.fundaciobit.genapp.common.i18n.I18NException
    */
   public static RegistroSalida getRegistroSalida(AsientoRegistralWs asientoRegistral,
                                                  UsuarioEntidad usuario,Libro libro, Oficina oficina,
                                                  Organismo organismo,  CodigoAsuntoLocal codigoAsuntoEjb,
                                                  TipoAsuntoLocal tipoAsuntoEjb) throws Exception, I18NException {

      if (asientoRegistral == null){
         return  null;
      }

      RegistroSalida registroSalida = new RegistroSalida();
      RegistroDetalle registroDetalle = new RegistroDetalle();

      //Marcamos como no presencial
      registroDetalle.setPresencial(false);

      registroSalida.setOrigen(organismo);
      registroSalida.setOficina(oficina);
      registroSalida.setFecha(new Date());
      registroSalida.setUsuario(usuario);
      registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);
      registroSalida.setLibro(libro);
      registroDetalle.setExtracto(asientoRegistral.getResumen());
      registroDetalle.setTipoDocumentacionFisica(new Long(asientoRegistral.getTipoDocumentacionFisicaCodigo()));
      registroDetalle.setTipoAsunto(getTipoAsunto(asientoRegistral.getTipoAsunto(), usuario.getEntidad().getId(), tipoAsuntoEjb));
      registroDetalle.setIdioma(asientoRegistral.getIdioma());
      registroDetalle.setCodigoSia(asientoRegistral.getCodigoSia());

      if (StringUtils.isNotEmpty(asientoRegistral.getCodigoAsunto())) {registroDetalle.setCodigoAsunto(getCodigoAsunto(asientoRegistral.getCodigoAsunto(), codigoAsuntoEjb));}
      if (StringUtils.isNotEmpty(asientoRegistral.getReferenciaExterna())) {registroDetalle.setReferenciaExterna(asientoRegistral.getReferenciaExterna());}
      if (StringUtils.isNotEmpty(asientoRegistral.getNumeroExpediente())) {registroDetalle.setExpediente(asientoRegistral.getNumeroExpediente());}
      if (StringUtils.isNotEmpty(asientoRegistral.getTipoTransporte())) {registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(asientoRegistral.getTipoTransporte()));}
      if (StringUtils.isNotEmpty(asientoRegistral.getNumeroTransporte())) {registroDetalle.setNumeroTransporte(asientoRegistral.getNumeroTransporte());}
      if (StringUtils.isNotEmpty(asientoRegistral.getObservaciones())) {registroDetalle.setObservaciones(asientoRegistral.getObservaciones());}
      registroDetalle.setOficinaOrigen(oficina);
      if (StringUtils.isNotEmpty(asientoRegistral.getExpone())) {registroDetalle.setExpone(asientoRegistral.getExpone());}
      if (StringUtils.isNotEmpty(asientoRegistral.getSolicita())) {registroDetalle.setSolicita(asientoRegistral.getSolicita());}
      if (StringUtils.isNotEmpty(asientoRegistral.getAplicacion())) {registroDetalle.setAplicacion(asientoRegistral.getAplicacion());}
      if (StringUtils.isNotEmpty(asientoRegistral.getVersion())) {registroDetalle.setVersion(asientoRegistral.getVersion());}

      registroSalida.setRegistroDetalle(registroDetalle);

      return registroSalida;
   }


   /**
    * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs}, a partir de un {@link es.caib.regweb3.model.RegistroEntrada}
    * @param registroEntrada
    * @param idioma
    * @param oficioRemisionEjb
    * @param trazabilidadEjb
    * @return
    * @throws Exception
    * @throws I18NException
    */
   public static AsientoRegistralWs getAsientoRegistralBean(RegistroEntrada registroEntrada,
                                                            String idioma, OficioRemisionLocal oficioRemisionEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception, I18NException {

      if (registroEntrada == null) {
         return null;
      }

      Entidad entidad = registroEntrada.getOficina().getOrganismoResponsable().getEntidad();

      AsientoRegistralWs asientoRegistral = new AsientoRegistralWs(REGISTRO_ENTRADA);
      RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

      asientoRegistral.setEstado(registroEntrada.getEstado());

      asientoRegistral.setEntidadCodigo(entidad.getCodigoDir3());
      asientoRegistral.setEntidadDenominacion(entidad.getNombre());

      asientoRegistral.setNumeroRegistro(registroEntrada.getNumeroRegistro());
      asientoRegistral.setNumeroRegistroFormateado(registroEntrada.getNumeroRegistroFormateado());
      asientoRegistral.setFechaRegistro(registroEntrada.getFecha());
      asientoRegistral.setCodigoUsuario(registroEntrada.getUsuario().getUsuario().getIdentificador());

      asientoRegistral.setEntidadRegistralInicioCodigo(registroEntrada.getOficina().getCodigo());//Oficina
      asientoRegistral.setEntidadRegistralInicioDenominacion(registroEntrada.getOficina().getDenominacion());
      asientoRegistral.setLibroCodigo(registroEntrada.getLibro().getCodigo());

      //Campos comunes
      setAsientoRegistralComun(asientoRegistral,registroDetalle, entidad, idioma, oficioRemisionEjb, trazabilidadEjb);

      // Campos únicos de RegistroEntrada
      if(registroEntrada.getDestino() != null ){
         asientoRegistral.setUnidadTramitacionDestinoCodigo(registroEntrada.getDestino().getCodigo());
         asientoRegistral.setUnidadTramitacionDestinoDenominacion(registroEntrada.getDestino().getDenominacion());
      }else{
         asientoRegistral.setUnidadTramitacionDestinoCodigo(registroEntrada.getDestinoExternoCodigo());
         asientoRegistral.setUnidadTramitacionDestinoDenominacion(registroEntrada.getDestinoExternoDenominacion());
      }

      return asientoRegistral;

   }

   /**
    * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs} con anexos, a partir de un {@link es.caib.regweb3.model.RegistroEntrada}
    * @param registroEntrada
    * @param idioma
    * @param oficioRemisionEjb
    * @param anexoEjb
    * @param trazabilidadEjb
    * @return
    * @throws Exception
    * @throws I18NException
    */
   public static AsientoRegistralWs getAsientoRegistralBeanConAnexos(RegistroEntrada registroEntrada,
                                                                     String idioma, OficioRemisionLocal oficioRemisionEjb, AnexoLocal anexoEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception, I18NException {

      AsientoRegistralWs asientoRegistral = getAsientoRegistralBean(registroEntrada,idioma,oficioRemisionEjb, trazabilidadEjb);

      if(registroEntrada.getRegistroDetalle().getAnexos() != null){
         List<AnexoWs> anexosWs = procesarAnexosWs(registroEntrada.getRegistroDetalle().getAnexos(), anexoEjb, registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getId());

         asientoRegistral.setAnexos(anexosWs);
      }

      return asientoRegistral;


   }


   /**
    * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs}, a partir de un {@link es.caib.regweb3.model.RegistroSalida}
    * @param registroSalida
    * @param idioma
    * @param oficioRemisionEjb
    * @param trazabilidadEjb
    * @return
    * @throws Exception
    * @throws I18NException
    */
   public static AsientoRegistralWs getAsientoRegistralBean(RegistroSalida registroSalida,
                                                            String idioma, OficioRemisionLocal oficioRemisionEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception, I18NException {

      if (registroSalida == null) {
         return null;
      }

      Entidad entidad = registroSalida.getOficina().getOrganismoResponsable().getEntidad();

      AsientoRegistralWs asientoRegistral = new AsientoRegistralWs(REGISTRO_SALIDA);
      RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

      asientoRegistral.setEstado(registroSalida.getEstado());

      asientoRegistral.setEntidadCodigo(entidad.getCodigoDir3());
      asientoRegistral.setEntidadDenominacion(entidad.getNombre());

      asientoRegistral.setNumeroRegistro(registroSalida.getNumeroRegistro());
      asientoRegistral.setNumeroRegistroFormateado(registroSalida.getNumeroRegistroFormateado());
      asientoRegistral.setFechaRegistro(registroSalida.getFecha());

      asientoRegistral.setCodigoUsuario(registroSalida.getUsuario().getUsuario().getIdentificador());
      asientoRegistral.setEntidadRegistralInicioCodigo(registroSalida.getOficina().getCodigo());
      asientoRegistral.setEntidadRegistralInicioDenominacion(registroSalida.getOficina().getDenominacion());
      asientoRegistral.setLibroCodigo(registroSalida.getLibro().getCodigo());

      //Campos comunes
      setAsientoRegistralComun(asientoRegistral, registroDetalle, entidad, idioma, oficioRemisionEjb, trazabilidadEjb);


      // Campos únicos de RegistroSalida
      if (registroSalida.getOrigen() != null) {
         asientoRegistral.setUnidadTramitacionOrigenCodigo(registroSalida.getOrigen().getCodigo());
         asientoRegistral.setUnidadTramitacionOrigenDenominacion(registroSalida.getOrigen().getDenominacion());
      } else {
         asientoRegistral.setUnidadTramitacionOrigenCodigo(registroSalida.getOrigenExternoCodigo());
         asientoRegistral.setUnidadTramitacionOrigenDenominacion(registroSalida.getOrigenExternoDenominacion());
      }

      return asientoRegistral;

   }

   /**
    * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs} con anexos, a partir de un {@link es.caib.regweb3.model.RegistroSalida}
    * @param registroSalida
    * @param idioma
    * @param oficioRemisionEjb
    * @param anexoEjb
    * @param trazabilidadEjb
    * @return
    * @throws Exception
    * @throws I18NException
    */
   public static AsientoRegistralWs getAsientoRegistralBeanConAnexos(RegistroSalida registroSalida,
                                                                     String idioma, OficioRemisionLocal oficioRemisionEjb, AnexoLocal anexoEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception, I18NException {

      AsientoRegistralWs asientoRegistral = getAsientoRegistralBean(registroSalida,idioma,oficioRemisionEjb,trazabilidadEjb);

      if(registroSalida.getRegistroDetalle().getAnexos() != null){
         List<AnexoWs> anexosWs = procesarAnexosWs(registroSalida.getRegistroDetalle().getAnexos(), anexoEjb, registroSalida.getOficina().getOrganismoResponsable().getEntidad().getId());

         asientoRegistral.setAnexos(anexosWs);
      }

      return asientoRegistral;
   }

   /**
    * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs}, a partir de un {@link es.caib.regweb3.model.RegistroDetalle}
    * @param asientoRegistral
    * @param registroDetalle
    * @param entidad
    * @param idioma
    * @param oficioRemisionEjb
    * @param trazabilidadEjb
    * @throws Exception
    */
   private static void setAsientoRegistralComun(AsientoRegistralWs asientoRegistral, RegistroDetalle registroDetalle, Entidad entidad, String idioma, OficioRemisionLocal oficioRemisionEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception{

      asientoRegistral.setResumen(registroDetalle.getExtracto());
      asientoRegistral.setTipoDocumentacionFisicaCodigo(registroDetalle.getTipoDocumentacionFisica());

      // Tipo Asunto
      TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroDetalle.getTipoAsunto().getTraduccion(idioma);
      asientoRegistral.setTipoAsunto(registroDetalle.getTipoAsunto().getCodigo());
      asientoRegistral.setTipoAsuntoDenominacion(traduccionTipoAsunto.getNombre());

      asientoRegistral.setIdioma(registroDetalle.getIdioma());

      // Código Asunto
      if(registroDetalle.getCodigoAsunto() != null){
         TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(idioma);
         asientoRegistral.setCodigoAsunto(registroDetalle.getCodigoAsunto().getCodigo());
         asientoRegistral.setCodigoAsuntoDenominacion(traduccionCodigoAsunto.getNombre());
      }else{
         asientoRegistral.setCodigoAsunto(null);
         asientoRegistral.setCodigoAsuntoDenominacion(null);
      }

      asientoRegistral.setReferenciaExterna(registroDetalle.getReferenciaExterna());
      asientoRegistral.setNumeroExpediente(registroDetalle.getExpediente());

      if(registroDetalle.getTransporte() != null){
         asientoRegistral.setTipoTransporte(RegwebConstantes.CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
      }else{
         asientoRegistral.setTipoTransporte(null);

      }

      asientoRegistral.setNumeroTransporte(registroDetalle.getNumeroTransporte());
      asientoRegistral.setObservaciones(registroDetalle.getObservaciones());

      // Oficina Origen
      if(registroDetalle.getOficinaOrigen() != null){
         asientoRegistral.setEntidadRegistralOrigenCodigo(registroDetalle.getOficinaOrigen().getCodigo());
         asientoRegistral.setEntidadRegistralOrigenDenominacion(registroDetalle.getOficinaOrigen().getDenominacion());
      }else{
         asientoRegistral.setEntidadRegistralOrigenCodigo(registroDetalle.getOficinaOrigenExternoCodigo());
         asientoRegistral.setEntidadRegistralOrigenDenominacion(registroDetalle.getOficinaOrigenExternoDenominacion());
      }

      asientoRegistral.setAplicacion(registroDetalle.getAplicacion());
      asientoRegistral.setVersion(registroDetalle.getVersion());

      asientoRegistral.setExpone(registroDetalle.getExpone());
      asientoRegistral.setSolicita(registroDetalle.getSolicita());

      asientoRegistral.setCodigoSia(registroDetalle.getCodigoSia());
      asientoRegistral.setPresencial(registroDetalle.getPresencial());
      asientoRegistral.setTipoEnvioDocumentacion(registroDetalle.getTipoEnvioDocumentacion());

      // Campos dependientes del Estado
      if (asientoRegistral.getEstado().equals(RegwebConstantes.REGISTRO_OFICIO_ACEPTADO)) {
         OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(registroDetalle.getIdentificadorIntercambio(), asientoRegistral.getEntidadRegistralInicioCodigo());
         if (oficioRemision != null) {
            asientoRegistral.setFechaRegistroDestino(oficioRemision.getFechaEntradaDestino());
            asientoRegistral.setNumeroRegistroDestino(oficioRemision.getNumeroRegistroEntradaDestino());
         }
      } else if (asientoRegistral.getEstado().equals(RegwebConstantes.REGISTRO_RECHAZADO)) {

         List<TrazabilidadSir> trazabilidades = trazabilidadEjb.getByIdIntercambio(registroDetalle.getIdentificadorIntercambio(), entidad.getId());

         for (TrazabilidadSir trazaSir : trazabilidades) {
            if (RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO.equals(trazaSir.getTipo())) {
               asientoRegistral.setMotivo(trazaSir.getObservaciones());
            }
         }

      } else if (asientoRegistral.getEstado().equals(RegwebConstantes.REGISTRO_REENVIADO)) {
         List<TrazabilidadSir> trazabilidades = trazabilidadEjb.getByIdIntercambio(registroDetalle.getIdentificadorIntercambio(), entidad.getId());

         for (TrazabilidadSir trazaSir : trazabilidades) {
            if (RegwebConstantes.TRAZABILIDAD_SIR_REENVIO.equals(trazaSir.getTipo())) {
               asientoRegistral.setMotivo(trazaSir.getObservaciones());
            }
         }
      }

      //Interesados
      if(registroDetalle.getInteresados() != null){
         List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroDetalle.getInteresados());

         asientoRegistral.setInteresados(interesadosWs);
      }
   }

}
