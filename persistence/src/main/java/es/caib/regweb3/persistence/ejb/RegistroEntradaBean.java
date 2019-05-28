package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Session;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */

@Stateless(name = "RegistroEntradaEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RegistroEntradaBean extends RegistroEntradaCambiarEstadoBean
        implements RegistroEntradaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB private LibroLocal libroEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private InteresadoLocal interesadoEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private PluginLocal pluginEjb;


    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                            UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull)
            throws Exception, I18NException, I18NValidationException {

        try{

            // Obtenemos el Número de registro
            Libro libro = libroEjb.findById(registroEntrada.getLibro().getId());
            Oficina oficina = oficinaEjb.findById(registroEntrada.getOficina().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorEntrada().getId());
            registroEntrada.setNumeroRegistro(numeroRegistro.getNumero());
            registroEntrada.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, libro, oficina));

            // Si no ha introducido ninguna fecha de Origen
            if (registroEntrada.getRegistroDetalle().getFechaOrigen() == null) {
                registroEntrada.getRegistroDetalle().setFechaOrigen(registroEntrada.getFecha());
            }

            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen())) {

                registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            }

            // Guardar RegistroEntrada
            registroEntrada = persist(registroEntrada);

            // Guardar el HistorioRegistroEntrada
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" ),false);

            // Procesamos los Interesados
            if(interesados != null && interesados.size() > 0){
                interesados = interesadoEjb.guardarInteresados(interesados, registroEntrada.getRegistroDetalle());
                registroEntrada.getRegistroDetalle().setInteresados(interesados);
            }

            // TODO Controlar custodyID y si hay fallo borrar todos los Custody
            if (anexosFull != null && anexosFull.size() != 0) {
                final Long registroID = registroEntrada.getId();
                for (AnexoFull anexoFull : anexosFull) {

                    anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
                    anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "entrada", true);

                }
            }
            //Llamamos al plugin de postproceso
            postProcesoNuevoRegistro(registroEntrada,usuarioEntidad.getEntidad().getId());

            return registroEntrada;

        }catch (I18NException i18n){
            log.info("Error registrando la entrada");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        }catch (I18NValidationException i18nv){
            log.info("Error de validación registrando la entrada");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        } catch (Exception e){
            log.info("Error registrando la entrada");
            e.printStackTrace();
            ejbContext.setRollbackOnly();
            throw e;
        }

    }

    @Override
    public void cambiarEstadoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();

        registroEntrada.setEstado(idEstado);

        // Creamos el HistoricoRegistroEntrada para la modificación d estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada,
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), false);
    }

    @Override
    public void cambiarEstadoAnuladoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();

        registroEntrada.setEstado(idEstado);

        // Creamos el HistoricoRegistroEntrada para la modificación de estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, observacionesAnulacion, false);
    }


    @Override
    public void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad,
                                      String observacionesAnulacion) throws Exception {

        // Estado anulado
        cambiarEstadoAnuladoHistorico(registroEntrada, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad, observacionesAnulacion);

    }

    @Override
    public void activarRegistroEntrada(RegistroEntrada registroEntrada,
                                       UsuarioEntidad usuarioEntidad) throws Exception {

        // Actualizamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

    }

    @Override
    public void visarRegistroEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);

    }

    @Override
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada,
                                        UsuarioEntidad usuarioEntidad) throws Exception, I18NValidationException, I18NException {

        // CREAMOS LA TRAZABILIDAD
        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOficioRemision(null);
        trazabilidad.setFecha(new Date());
        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_DISTRIBUCION);
        trazabilidad.setRegistroEntradaOrigen(registroEntrada);
        trazabilidad.setRegistroSalida(null);
        trazabilidad.setRegistroEntradaDestino(null);
        trazabilidadEjb.persist(trazabilidad);

        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_DISTRIBUIDO, usuarioEntidad);

    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> registros = em.createQuery("Select distinct(re.id) from RegistroEntrada as re where re.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada rectificar(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NException {

        RegistroEntrada rectificado = null;
        Long idRegistro = registroEntrada.getId();

        try {
            List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroEntrada.getRegistroDetalle().getAnexosFull();

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroEntrada);
            session.evict(registroEntrada.getRegistroDetalle());
            session.evict(registroEntrada.getRegistroDetalle().getInteresados());

            // Nuevas propiedades
            registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            registroEntrada.setFecha(new Date());

            // Set Id's a null
            registroEntrada.setId(null);
            registroEntrada.getRegistroDetalle().setId(null);
            registroEntrada.getRegistroDetalle().setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }
            registroEntrada.getRegistroDetalle().setAnexos(null);

            registroEntrada.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroEntrada.getNumeroRegistroFormateado());

            // Registramos el nuevo registro
            rectificado = registrarEntrada(registroEntrada, usuarioEntidad,interesados, anexos);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro,RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA);
            trazabilidad.setRegistroEntradaOrigen(getReference(idRegistro));
            trazabilidad.setRegistroEntradaDestino(registroEntrada);
            trazabilidad.setRegistroSir(null);
            trazabilidad.setOficioRemision(null);
            trazabilidad.setRegistroSalida(null);
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

        } catch (I18NException e) {
            e.printStackTrace();
        } catch (I18NValidationException e) {
            e.printStackTrace();
        }

        return rectificado;
    }


    @Override
    public void actualizarDestinoExtinguido(Long idOrganismoExtinguido, Long idOrganismoSustituto) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set destino = :idOrganismoSustituto where destino = :idOrganismoExtinguido and (estado = :valido or estado = :pendienteVisar)");
        q.setParameter("idOrganismoSustituto", idOrganismoSustituto);
        q.setParameter("idOrganismoExtinguido", idOrganismoExtinguido);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("pendienteVisar", RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

        q.executeUpdate();

    }

    @Override
    public RegistroEntrada getConAnexosFull(Long id) throws Exception, I18NException {

        RegistroEntrada re = findById(id);

        return cargarAnexosFull(re);
    }

    /**
     * Carga los Anexos Completos al RegistroEntrada pasado por parámetro
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroEntrada cargarAnexosFull(RegistroEntrada registroEntrada) throws Exception, I18NException {
        Long idEntidad = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        registroEntrada.getRegistroDetalle().setAnexosFull(anexosFull);
        return registroEntrada;
    }

    @Override
    public void postProcesoActualizarRegistro(RegistroEntrada re,Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.actualizarRegistroEntrada(re);
        }

    }

    @Override
    public void postProcesoNuevoRegistro(RegistroEntrada re,Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.nuevoRegistroEntrada(re);
        }
    }



}
