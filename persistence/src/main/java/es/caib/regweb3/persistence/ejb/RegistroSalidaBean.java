package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
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

@Stateless(name = "RegistroSalidaEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RegistroSalidaBean extends RegistroSalidaCambiarEstadoBean
        implements RegistroSalidaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB private LibroLocal libroEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private InteresadoLocal interesadoEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private PluginLocal pluginEjb;


    @Override
    public synchronized RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                                       UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
            throws Exception, I18NException, I18NValidationException {

        try{

            // Obtenemos el Número de registro
            Libro libro = libroEjb.findById(registroSalida.getLibro().getId());
            Oficina oficina = oficinaEjb.findById(registroSalida.getOficina().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorSalida().getId());
            registroSalida.setNumeroRegistro(numeroRegistro.getNumero());
            registroSalida.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, libro, oficina));

            // Si no ha introducido ninguna fecha de Origen
            if (registroSalida.getRegistroDetalle().getFechaOrigen() == null) {
                registroSalida.getRegistroDetalle().setFechaOrigen(registroSalida.getFecha());
            }

            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen())) {

                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
            }

            // Guardamos el RegistroSalida
            registroSalida = persist(registroSalida);

            //Guardamos el HistorioRegistroSalida
            historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" ),false);

            // Procesamos los Interesados
            if(interesados != null && interesados.size() > 0){
                interesadoEjb.guardarInteresados(interesados, registroSalida.getRegistroDetalle());
                registroSalida.getRegistroDetalle().setInteresados(interesados);
            }

            if (anexos != null && anexos.size() != 0) {
                final Long registroID = registroSalida.getId();
                for (AnexoFull anexoFull : anexos) {
                    anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
                    anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "salida", true);
                }
            }

            postProcesoNuevoRegistro(registroSalida,usuarioEntidad.getEntidad().getId());

            return registroSalida;

        }catch (I18NException i18n){
            log.info("Error registrando la salida");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        }catch (I18NValidationException i18nv){
            log.info("Error de validación registrando la salida");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        } catch (Exception e){
            log.info("Error registrando la salida");
            e.printStackTrace();
            ejbContext.setRollbackOnly();
            throw e;
        }

    }


    @Override
    public void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoAnuladoHistorico(registroSalida, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad, observacionesAnulacion);
    }

    @Override
    public void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoHistorico(registroSalida, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

    }


    @Override
    public void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoHistorico(registroSalida, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);

    }


    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb3.model.utils.RegistroBasico}
     *
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoList(List<Object[]> result) throws Exception {

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        for (Object[] object : result) {
            RegistroBasico registroBasico = new RegistroBasico((Long) object[0], (String) object[1], (Date) object[2], (String) object[3], (String) object[4], (String) object[5]);

            registros.add(registroBasico);
        }

        return registros;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<Object> registros = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where rs.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }


    @Override
    public void cambiarEstadoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception {
        Query q = em.createQuery("update RegistroSalida set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroSalida.getId());
        q.executeUpdate();

        registroSalida.setEstado(idEstado);

        // Creamos el HistoricoRegistroSalida para la modificación de estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida,
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.estado" ), false);
    }

    @Override
    public void cambiarEstadoAnuladoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        Query q = em.createQuery("update RegistroSalida set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroSalida.getId());
        q.executeUpdate();

        registroSalida.setEstado(idEstado);

        // Creamos el HistoricoRegistroSalida para la modificación de estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, observacionesAnulacion, false);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroSalida rectificar(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroSalida rectificado = null;
        Long idRegistro = registroSalida.getId();

        try {
            List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroSalida.getRegistroDetalle().getAnexosFull();

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroSalida);
            session.evict(registroSalida.getRegistroDetalle());
            session.evict(registroSalida.getRegistroDetalle().getInteresados());

            // Nuevas propiedades
            registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            registroSalida.setFecha(new Date());

            // Set Id's a null
            registroSalida.setId(null);
            registroSalida.getRegistroDetalle().setId(null);
            registroSalida.getRegistroDetalle().setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }
            registroSalida.getRegistroDetalle().setAnexos(null);

            registroSalida.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroSalida.getNumeroRegistroFormateado());

            // Registramos el nuevo registro
            rectificado = registrarSalida(registroSalida, usuarioEntidad,interesados, anexos);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro,RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_SALIDA);
            trazabilidad.setRegistroSalida(registroSalida);
            trazabilidad.setRegistroSalidaRectificado(getReference(idRegistro));
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
    public void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.actualizarRegistroSalida(rs);
        }

    }

    @Override
    public void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.nuevoRegistroSalida(rs);
        }
    }
}
