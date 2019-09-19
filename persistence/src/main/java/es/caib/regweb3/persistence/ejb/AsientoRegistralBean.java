package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.JustificanteReferencia;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by mgonzalez on 06/03/2019.
 * Ejb con TransactionAttributeType.REQUIRES_NEW para permitir la creación
 * de un Registro y generar el justificante conjuntamente.
 */
@Stateless(name = "AsientoRegistralEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AsientoRegistralBean implements AsientoRegistralLocal {

   @EJB private RegistroSalidaLocal registroSalidaEjb;
   @EJB private RegistroEntradaLocal registroEntradaEjb;
   @EJB private UsuarioEntidadLocal usuarioEntidadEjb;
   @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
   @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
   @EJB private AnexoLocal anexoEjb;

   @Override
   public UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws Exception, I18NException{
      return  usuarioEntidadEjb.comprobarUsuarioEntidad(identificador, idEntidad);
   }

   @Override
   public RegistroSalida registrarSalida(RegistroSalida registroSalida,
         UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
      throws Exception, I18NException, I18NValidationException {

      registroSalida = registroSalidaEjb.registrarSalida(registroSalida,usuarioEntidad,interesados,anexos);
      return registroSalida;
   }

   @Override
   public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                         UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
           throws Exception, I18NException, I18NValidationException {

      registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada,usuarioEntidad,interesados,anexos);

      return registroEntrada;
   }

   @Override
   public JustificanteReferencia obtenerReferenciaJustificante(String numeroRegistroformateado, Entidad entidad) throws Exception, I18NException {

      RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoConAnexos(entidad.getCodigoDir3(),numeroRegistroformateado);
      RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateadoConAnexos(entidad.getCodigoDir3(),numeroRegistroformateado);

      if (registroEntrada != null) {

         if(registroEntrada.getRegistroDetalle().getTieneJustificante()){
            String csv = registroEntrada.getRegistroDetalle().getJustificante().getCsv();
            String url = anexoEjb.getUrlValidation(registroEntrada.getRegistroDetalle().getJustificante().getCustodiaID(),true,entidad.getId());

            return new JustificanteReferencia(csv, url);

         }else{
            throw new I18NException("registro.justificante.noTiene");
         }

      }else if(registroSalida != null){

         if(registroSalida.getRegistroDetalle().getTieneJustificante()){
            String csv = registroSalida.getRegistroDetalle().getJustificante().getCsv();
            String url = anexoEjb.getUrlValidation(registroSalida.getRegistroDetalle().getJustificante().getCustodiaID(),true,entidad.getId());
            return new JustificanteReferencia(csv, url);
         }else{
            throw new I18NException("registro.justificante.noTiene");
         }
      }

      return null;

   }
}
