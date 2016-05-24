package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (Convertir en EJB)
 * Date: 16/01/14
 */
@Stateless(name = "OficioRemisionUtilsEJB")
@SecurityDomain("seycon")
public class OficioRemisionUtilsBean implements OficioRemisionUtilsLocal {

  public final Logger log = Logger.getLogger(getClass());

  @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
  public RegistroEntradaLocal registroEntradaEjb;

  @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
  public OficioRemisionLocal oficioRemisionEjb;

  @EJB(mappedName = "regweb3/OrganismoEJB/local")
  public OrganismoLocal organismoEjb;

  @EJB(mappedName = "regweb3/LibroEJB/local")
  public LibroLocal libroEjb;

  @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
  public TrazabilidadLocal trazabilidadEjb;

  @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
  public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;



  /**
   * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
   * Crea un RegistroSalida por cada uno de los RegistroEntrada que contenga el OficioRemision
   * Crea la trazabilidad para los RegistroEntrada y RegistroSalida
   * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
   * @param oficinaActiva Oficia en la cual se realiza el OficioRemision
   * @param usuarioEntidad Usuario que realiza el OficioRemision
   * @param idOrganismo
   * @param idLibro
   * @return
   * @throws Exception
   */
 @Override
  public OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada,
      Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro)
          throws Exception, I18NException, I18NValidationException {

      OficioRemision oficioRemision = new OficioRemision();
      oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_INTERNO_ESTADO_ENVIADO);
      oficioRemision.setOficina(oficinaActiva);
      oficioRemision.setFecha(new Date());
      oficioRemision.setRegistrosEntrada(registrosEntrada);
      oficioRemision.setUsuarioResponsable(usuarioEntidad);
      oficioRemision.setLibro(new Libro(idLibro));
      oficioRemision.setOrganismoDestinatario(new Organismo(idOrganismo));

      synchronized (this){
          oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision,
              RegwebConstantes.ESTADO_OFICIO_INTERNO);
      }

      return oficioRemision;

  }

  /**
   * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
   * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
   * @param oficinaActiva Oficia en la cual se realiza el OficioRemision
   * @param usuarioEntidad Usuario que realiza el OficioRemision
   * @param organismoExterno
   * @param idLibro
   * @return
   * @throws Exception
   */

  public OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
      Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno,
      String organismoExternoDenominacion, Long idLibro, String identificadorIntercambioSir)
          throws Exception , I18NException, I18NValidationException {

      //Organismo organismoDestino = organismoEjb.findById(idOrganismo);

      OficioRemision oficioRemision = new OficioRemision();
      oficioRemision.setIdentificadorIntercambioSir(identificadorIntercambioSir);
      
      if (identificadorIntercambioSir == null) { //todo: modificar el estado cuando se implemente SIR
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ESTADO_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
      } else {
        oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ESTADO_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
      }
      
      oficioRemision.setOficina(oficinaActiva);
      oficioRemision.setFecha(new Date());
      oficioRemision.setRegistrosEntrada(registrosEntrada);
      oficioRemision.setUsuarioResponsable(usuarioEntidad);
      oficioRemision.setLibro(new Libro(idLibro));
      oficioRemision.setDestinoExternoCodigo(organismoExterno);
      oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
      oficioRemision.setOrganismoDestinatario(null);

      synchronized (this) {
          oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision,
                  RegwebConstantes.ESTADO_OFICIO_EXTERNO);
      }

      return oficioRemision;

  }

  /**
   * Procesa un OficioRemision pendiente de llegada, creando tantos Registros de Entrada,
   *  como contenga el Oficio.
   * @param oficioRemision
   * @throws Exception
   */
  @Override
  public List<RegistroEntrada> procesarOficioRemision(OficioRemision oficioRemision, 
      UsuarioEntidad usuario, Oficina oficinaActiva, 
      List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException {

      List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

      // Recorremos los RegistroEntrada del Oficio y Libro de registro seleccionado
      for (int i = 0; i < oficios.size(); i++) {

          OficioPendienteLlegada oficio = oficios.get(i);

          RegistroEntrada registroEntrada = registroEntradaEjb.findById(oficio.getIdRegistroEntrada());
          Libro libro = libroEjb.findById(oficio.getIdLibro());

          RegistroEntrada nuevoRE = new RegistroEntrada();
          nuevoRE.setUsuario(usuario);
          nuevoRE.setDestino(new Organismo(oficio.getIdOrganismoDestinatario()));
          nuevoRE.setOficina(oficinaActiva);
          nuevoRE.setEstado(RegwebConstantes.ESTADO_VALIDO);
          nuevoRE.setLibro(libro);
          nuevoRE.setRegistroDetalle(registroEntrada.getRegistroDetalle());

          synchronized (this){
              nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE);
          }

          //Guardamos el HistorioRegistroEntrada
          historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(nuevoRE, usuario, RegwebConstantes.TIPO_MODIF_ALTA,false);
          registros.add(nuevoRE);

          // ACTUALIZAMOS LA TRAZABILIDAD
          Trazabilidad trazabilidad = trazabilidadEjb.getByOficioRegistroEntrada(oficioRemision.getId(),registroEntrada.getId());
          trazabilidad.setRegistroEntradaDestino(nuevoRE);

          trazabilidadEjb.merge(trazabilidad);

      }

      oficioRemision.setEstado(RegwebConstantes.OFICIO_REMISION_ESTADO_ACEPTADO);
      oficioRemision.setFechaEstado(new Date());

      // Actualizamos el oficio de remisión
      oficioRemision = oficioRemisionEjb.merge(oficioRemision);

      return registros;

  }


}
