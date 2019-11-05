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

import static es.caib.regweb3.utils.RegwebConstantes.*;

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
      //registroDetalle.setTipoAsunto(getTipoAsunto(asientoRegistral.getTipoAsunto(),usuario.getEntidad().getId(), tipoAsuntoEjb));
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
      //registroDetalle.setTipoAsunto(getTipoAsunto(asientoRegistral.getTipoAsunto(), usuario.getEntidad().getId(), tipoAsuntoEjb));
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
    * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs}, a partir de un {@link es.caib.regweb3.model.IRegistro}
    * @param usuario
    * @param numeroRegistro
    * @param tipoRegistro
    * @param idioma
    * @param conAnexos
    * @param registroEntradaConsultaEjb
    * @param registroSalidaConsultaEjb
    * @param permisoLibroUsuarioEjb
    * @param anexoEjb
    * @param oficioRemisionEjb
    * @param trazabilidadEjb
    * @param lopdEjb
    * @return
    * @throws Exception
    * @throws I18NException
    */
   public static AsientoRegistralWs getAsientoRegistral(UsuarioEntidad usuario, String numeroRegistro, Long tipoRegistro, String idioma, Boolean conAnexos,
                                                        RegistroEntradaConsultaLocal registroEntradaConsultaEjb, RegistroSalidaConsultaLocal registroSalidaConsultaEjb, PermisoLibroUsuarioLocal permisoLibroUsuarioEjb,
                                                        AnexoLocal anexoEjb, OficioRemisionLocal oficioRemisionEjb, TrazabilidadSirLocal trazabilidadEjb, LopdLocal lopdEjb) throws Exception, I18NException {

      AsientoRegistralWs asientoRegistral = new AsientoRegistralWs(tipoRegistro);
      List<Anexo> anexos;

      if(REGISTRO_ENTRADA.equals(tipoRegistro)){

         RegistroEntrada registro = registroEntradaConsultaEjb.findByNumeroRegistroFormateado(usuario.getEntidad().getCodigoDir3(), numeroRegistro);

         if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro);
         }

         // Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
         if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registro.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA, false)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuario.getUsuario().getNombreCompleto());
         }

         anexos = registro.getRegistroDetalle().getAnexos();

         // Convertimos los campos comunes de AsientoRegistralWs
         setAsientoRegistralComun(asientoRegistral, registro, usuario.getEntidad(), idioma, oficioRemisionEjb, trazabilidadEjb);

         // Campos únicos de RegistroEntrada
         if(registro.getDestino() != null ){
            asientoRegistral.setUnidadTramitacionDestinoCodigo(registro.getDestino().getCodigo());
            asientoRegistral.setUnidadTramitacionDestinoDenominacion(registro.getDestino().getDenominacion());
         }else{
            asientoRegistral.setUnidadTramitacionDestinoCodigo(registro.getDestinoExternoCodigo());
            asientoRegistral.setUnidadTramitacionDestinoDenominacion(registro.getDestinoExternoDenominacion());
         }

         // LOPD
         lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

      }else {

         RegistroSalida registro = registroSalidaConsultaEjb.findByNumeroRegistroFormateado(usuario.getEntidad().getCodigoDir3(), numeroRegistro);

         if (registro == null) {
            throw new I18NException("registroSalida.noExiste", numeroRegistro);
         }

         // Comprobamos que el usuario tiene permisos de lectura para el RegistroSalida
         if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registro.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA, false)) {
            throw new I18NException("registroSalida.usuario.permisos", usuario.getUsuario().getNombreCompleto());
         }

         anexos = registro.getRegistroDetalle().getAnexos();

         // Convertimos los campos comunes de AsientoRegistralWs
         setAsientoRegistralComun(asientoRegistral, registro, usuario.getEntidad(), idioma, oficioRemisionEjb, trazabilidadEjb);

         // Campos únicos de RegistroSalida
         if (registro.getOrigen() != null) {
            asientoRegistral.setUnidadTramitacionOrigenCodigo(registro.getOrigen().getCodigo());
            asientoRegistral.setUnidadTramitacionOrigenDenominacion(registro.getOrigen().getDenominacion());
         } else {
            asientoRegistral.setUnidadTramitacionOrigenCodigo(registro.getOrigenExternoCodigo());
            asientoRegistral.setUnidadTramitacionOrigenDenominacion(registro.getOrigenExternoDenominacion());
         }

         // LOPD
         lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);
      }

      // Obtenemos los anexos si así se ha indicado
      if(conAnexos && anexos != null){
         List<AnexoWs> anexosWs = procesarAnexosWs(anexos, anexoEjb, usuario.getEntidad().getId());

         asientoRegistral.setAnexos(anexosWs);
      }

      return asientoRegistral;
   }

   /**
    * Obtiene un {@link es.caib.regweb3.ws.model.AsientoRegistralWs}, a partir de un {@link es.caib.regweb3.model.RegistroDetalle}
    * @param asientoRegistral
    * @param registro
    * @param entidad
    * @param idioma
    * @param oficioRemisionEjb
    * @param trazabilidadEjb
    * @throws Exception
    */
   private static void setAsientoRegistralComun(AsientoRegistralWs asientoRegistral, IRegistro registro, Entidad entidad, String idioma, OficioRemisionLocal oficioRemisionEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception{

      RegistroDetalle registroDetalle = registro.getRegistroDetalle();

      asientoRegistral.setEstado(registro.getEstado());

      asientoRegistral.setEntidadCodigo(entidad.getCodigoDir3());
      asientoRegistral.setEntidadDenominacion(entidad.getNombre());

      asientoRegistral.setNumeroRegistro(registro.getNumeroRegistro());
      asientoRegistral.setNumeroRegistroFormateado(registro.getNumeroRegistroFormateado());
      asientoRegistral.setFechaRegistro(registro.getFecha());
      asientoRegistral.setCodigoUsuario(registro.getUsuario().getUsuario().getIdentificador());

      asientoRegistral.setEntidadRegistralInicioCodigo(registro.getOficina().getCodigo());//Oficina
      asientoRegistral.setEntidadRegistralInicioDenominacion(registro.getOficina().getDenominacion());
      asientoRegistral.setLibroCodigo(registro.getLibro().getCodigo());

      asientoRegistral.setResumen(registroDetalle.getExtracto());
      asientoRegistral.setTipoDocumentacionFisicaCodigo(registroDetalle.getTipoDocumentacionFisica());

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
