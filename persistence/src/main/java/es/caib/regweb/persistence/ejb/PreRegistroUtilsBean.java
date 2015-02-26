package es.caib.regweb.persistence.ejb;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import es.caib.regweb.model.Libro;
import es.caib.regweb.model.Oficina;
import es.caib.regweb.model.Organismo;
import es.caib.regweb.model.PreRegistro;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.utils.RegwebConstantes;


/**
 * Created by Fundació BIT.
 * Agrupa todas las funcionalidades comunes para trabajar con PreRegistros
 * @author jpernia
 * @author anadal (EJB)
 * Date: 01/12/15
 */
@Stateless(name = "PreRegistroUtilsEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class PreRegistroUtilsBean implements PreRegistroUtilsLocal {
  
  public final Logger log = Logger.getLogger(getClass());
  
  @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
  public RegistroEntradaLocal registroEntradaEjb;

  @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
  public RegistroSalidaLocal registroSalidaEjb;

  @EJB(mappedName = "regweb/HistoricoRegistroEntradaEJB/local")
  public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

  @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
  public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

  @EJB(mappedName = "regweb/PreRegistroEJB/local")
  public PreRegistroLocal preRegistroEjb;

  @EJB(mappedName = "regweb/OficinaEJB/local")
  public OficinaLocal oficinaEjb;

  @EJB(mappedName = "regweb/OrganismoEJB/local")
  public OrganismoLocal organismoEjb;

  @EJB(mappedName = "regweb/LibroEJB/local")
  public LibroLocal libroEjb;

  /**
   * Procesa un PreRegistro, creando un Registro de Entrada
   * @param preRegistro
   * @throws Exception
   */
  public RegistroEntrada procesarPreRegistroEntrada(PreRegistro preRegistro, 
      UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro) throws Exception {

      RegistroEntrada nuevoRE = new RegistroEntrada();

      nuevoRE.setUsuario(usuario);
      nuevoRE.setFecha(new Date());
      
      nuevoRE.setEstado(RegwebConstantes.ESTADO_VALIDO);

      if(preRegistro.getCodigoUnidadTramitacionDestino() != null){
          Organismo organismoDestino = organismoEjb.findByCodigo(preRegistro.getCodigoUnidadTramitacionDestino());
          nuevoRE.setDestino(organismoDestino);
      }

      nuevoRE.setDestinoExternoCodigo(null);
      nuevoRE.setDestinoExternoDenominacion(null);

      if(idLibro != null){
          Libro libro = libroEjb.findById(idLibro);
          nuevoRE.setLibro(libro);
      }
      if(preRegistro.getCodigoEntidadRegistralDestino() != null){
          Oficina oficina = oficinaEjb.findByCodigo(preRegistro.getCodigoEntidadRegistralDestino());
          // Comprobamos si esta Oficina Activa puede procesar este PreRegistro
          if(oficina.getCodigo().equals(oficinaActiva.getCodigo())){
              nuevoRE.setOficina(oficina);
          }
      }
      if(preRegistro.getRegistroDetalle() != null){
          nuevoRE.setRegistroDetalle(preRegistro.getRegistroDetalle());
      }

      // Procesamos el Registro Entrada
      synchronized (this){
          nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE);
      }


      //Guardamos el HistorioRegistroEntrada
      historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(nuevoRE, usuario, RegwebConstantes.TIPO_MODIF_ALTA,false);


      // Cambiamos el estado del PreRegistro
      preRegistro.setEstado(RegwebConstantes.ESTADO_PREREGISTRO_PROCESADO);

      // Actualizamos el PreRegistro
      preRegistro = preRegistroEjb.merge(preRegistro);

      return nuevoRE;

  }


  /**
   * Procesa un PreRegistro, creando un Registro de Salida
   * @param preRegistro
   * @throws Exception
   */
  public RegistroSalida procesarPreRegistroSalida(PreRegistro preRegistro, 
      UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro) throws Exception {

      RegistroSalida nuevoRS = new RegistroSalida();

      nuevoRS.setUsuario(usuario);
      nuevoRS.setFecha(new Date());

      nuevoRS.setEstado(RegwebConstantes.ESTADO_VALIDO);

      if(preRegistro.getCodigoUnidadTramitacionDestino() != null){
          Organismo organismoOrigen = organismoEjb.findByCodigo(preRegistro.getCodigoUnidadTramitacionDestino());
          nuevoRS.setOrigen(organismoOrigen);
      }
      if(preRegistro.getCodigoEntidadRegistralOrigen() != null){
          nuevoRS.setOrigenExternoCodigo(preRegistro.getCodigoEntidadRegistralOrigen());
      }
      if(preRegistro.getDecodificacionEntidadRegistralOrigen() != null){
          nuevoRS.setOrigenExternoDenominacion(preRegistro.getDecodificacionEntidadRegistralOrigen());
      }
      if(idLibro != null){
          Libro libro = libroEjb.findById(idLibro);
          nuevoRS.setLibro(libro);
      }
      if(preRegistro.getCodigoEntidadRegistralDestino() != null){
          Oficina oficina = oficinaEjb.findByCodigo(preRegistro.getCodigoEntidadRegistralDestino());
          if(oficina == oficinaActiva){
              nuevoRS.setOficina(oficina);
          }
      }
      if(preRegistro.getRegistroDetalle() != null){
          nuevoRS.setRegistroDetalle(preRegistro.getRegistroDetalle());
      }

      // Procesamos el Registro Salida
      synchronized (this){
          nuevoRS = registroSalidaEjb.registrarSalida(nuevoRS);
      }


      //Guardamos el HistorioRegistroSalida
      historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(nuevoRS, usuario, RegwebConstantes.TIPO_MODIF_ALTA,false);


      // Cambiamos el estado del PreRegistro
      preRegistro.setEstado(RegwebConstantes.ESTADO_PREREGISTRO_PROCESADO);

      // Actualizamos el PreRegistro
      preRegistro = preRegistroEjb.merge(preRegistro);

      return nuevoRS;

  }

}