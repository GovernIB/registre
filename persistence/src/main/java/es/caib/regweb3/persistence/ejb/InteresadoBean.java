package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "InteresadoEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class InteresadoBean extends BaseEjbJPA<Interesado, Long> implements InteresadoLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroDetalleLocal registroDetalleEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;


    @Override
    public Interesado getReference(Long id) throws I18NException {

        return em.getReference(Interesado.class, id);
    }

    @Override
    public Interesado findById(Long id) throws I18NException {

        return em.find(Interesado.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Interesado> getAll() throws I18NException {

        return em.createQuery("Select interesado from Interesado as interesado order by interesado.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(interesado.id) from Interesado as interesado");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Interesado> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select interesado from Interesado as interesado order by interesado.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Interesado guardarInteresado(Interesado interesado) throws I18NException {

        interesado.setNombre(StringUtils.capitailizeWord(interesado.getNombre(), false));
        interesado.setApellido1(StringUtils.capitailizeWord(interesado.getApellido1(), false));
        interesado.setApellido2(StringUtils.capitailizeWord(interesado.getApellido2(), false));
        interesado.setRazonSocial(StringUtils.capitailizeWord(interesado.getRazonSocial(), true));

        return persist(interesado);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Interesado> findByRegistroDetalle(Long registroDetalle) throws I18NException {

        Query q = em.createQuery("Select interesado.tipo, interesado.nombre, interesado.apellido1, interesado.apellido2, interesado.documento, interesado.razonSocial, interesado.codigoDir3 from Interesado as interesado " +
                "where interesado.registroDetalle.id = :registroDetalle");

        q.setParameter("registroDetalle", registroDetalle);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> results = q.getResultList();
        List<Interesado> interesados = new ArrayList<>();
        if (results.size() > 0) {

            for (Object[] result : results) {
                Interesado interesado = new Interesado();
                interesado.setTipo((Long) result[0]);
                interesado.setNombre((String) result[1]);
                interesado.setApellido1((String) result[2]);
                interesado.setApellido2((String) result[3]);
                interesado.setDocumento((String) result[4]);
                interesado.setRazonSocial((String) result[5]);
                interesado.setCodigoDir3((String) result[6]);

                interesados.add(interesado);
            }

            return interesados;

        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Interesado findByCodigoDir3RegistroDetalle(String codigoDir3, Long idRegistroDetalle) throws I18NException {
        Query q = em.createQuery("Select interesado.id from Interesado as interesado where interesado.codigoDir3 = :codigoDir3 " +
                "and interesado.registroDetalle.id = :idRegistroDetalle");

        q.setParameter("codigoDir3", codigoDir3);
        q.setParameter("idRegistroDetalle", idRegistroDetalle);
        q.setHint("org.hibernate.readOnly", true);

        Long idInteresado = (Long) q.getSingleResult();

        if (idInteresado != null) {
            return new Interesado(idInteresado);
        } else {
            return null;
        }

    }

    @Override
    public void eliminarInteresadoRegistroDetalle(Long idInteresado, Long idRegistroDetalle) throws I18NException {

        Interesado interesado = findById(idInteresado);
        RegistroDetalle registroDetalle = registroDetalleEjb.findByIdConInteresados(idRegistroDetalle);

        if (interesado != null && registroDetalle != null) {
            if (interesado.getRepresentante() != null) { // Si tiene representante, lo eliminamos
                eliminarInteresado(interesado.getRepresentante(), registroDetalle);
            }
            eliminarInteresado(interesado, registroDetalle);
        }

    }

    /**
     * Operaciones necesarias para eliminar un Interesado
     *
     * @param interesado
     * @param registroDetalle
     * @throws I18NException
     */
    private void eliminarInteresado(Interesado interesado, RegistroDetalle registroDetalle) throws I18NException {
        registroDetalle.getInteresados().remove(interesado);
        registroDetalleEjb.merge(registroDetalle);
        remove(interesado);
    }

    @Override
    public Boolean existeDocumentoNew(String documento) throws I18NException {
        Query q = em.createQuery("Select interesado.id from Interesado as interesado where " +
                "interesado.documento = :documento");

        q.setParameter("documento", documento);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean existeDocumentoEdit(String documento, Long idInteresado) throws I18NException {
        Query q = em.createQuery("Select interesado.id from Interesado as interesado where " +
                "interesado.id != :idInteresado and interesado.documento = :documento");

        q.setParameter("documento", documento);
        q.setParameter("idInteresado", idInteresado);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public String existeInteresadoAdministracion(Long idRegistroDetalle) throws I18NException {
        Query q = em.createQuery("Select interesado.codigoDir3 from Interesado as interesado where " +
                "interesado.registroDetalle.id = :idRegistroDetalle and interesado.tipo = :administracion");

        q.setParameter("idRegistroDetalle", idRegistroDetalle);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setHint("org.hibernate.readOnly", true);

        List<String> result = q.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    @Override
    public List<Interesado> guardarInteresados(List<Interesado> interesadosGuardar, RegistroDetalle registroDetalle) throws I18NException {

        List<Interesado> interesados = new ArrayList<Interesado>();

        for (Interesado interesado : interesadosGuardar) {

            // Lo asociamos al RegistroDetalle
            interesado.setRegistroDetalle(registroDetalle);

            if (!interesado.getIsRepresentante()) { // Solo los Interesados

                if (interesado.getRepresentante() != null) { // Tiene Representante

                    // Obtenemos el Representante
                    Interesado representante = interesadosGuardar.get(interesadosGuardar.indexOf(interesado.getRepresentante()));

                    // Guardamos el Interesado sin referencia al Representante
                    interesado.setRegistroDetalle(registroDetalle);
                    interesado.setId(null); // ponemos su id a null
                    interesado.setRepresentante(null);
                    interesado = guardarInteresado(interesado);

                    // Guardamos el Representante
                    representante.setId(null);
                    representante.setRegistroDetalle(registroDetalle);
                    representante.setRepresentado(interesado);
                    representante = guardarInteresado(representante);

                    // Lo asigamos al interesado y actualizamos
                    interesado.setRepresentante(representante);
                    interesado = merge(interesado);

                    // Los añadimos al Array
                    interesados.add(interesado);
                    interesados.add(representante);

                } else {
                    interesado.setRegistroDetalle(registroDetalle);
                    interesado.setId(null); // ponemos su id a null
                    interesado.setRepresentante(null);
                    interesado = guardarInteresado(interesado);

                    // Lo añadimos al Array
                    interesados.add(interesado);
                }

            }
        }

        return interesados;
    }

    public void postProcesoNuevoInteresado(Interesado interesado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO, false);

        if (postProcesoPlugin != null) {
            if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
                String numeroRegistroFormateado = registroEntradaConsultaEjb.findNumeroRegistroFormateadoByRegistroDetalle(idRegistroDetalle);
                postProcesoPlugin.nuevoInteresadoEntrada(interesado, numeroRegistroFormateado);
            } else {
                String numeroRegistroFormateado = registroSalidaConsultaEjb.findNumeroRegistroFormateadoByRegistroDetalle(idRegistroDetalle);
                postProcesoPlugin.nuevoInteresadoSalida(interesado, numeroRegistroFormateado);
            }
        }

    }

    public void postProcesoActualizarInteresado(Interesado interesado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO, false);

        if (postProcesoPlugin != null) {
            if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
                String numeroRegistroFormateado = registroEntradaConsultaEjb.findNumeroRegistroFormateadoByRegistroDetalle(idRegistroDetalle);
                postProcesoPlugin.actualizarInteresadoEntrada(interesado, numeroRegistroFormateado);
            } else {
                String numeroRegistroFormateado = registroSalidaConsultaEjb.findNumeroRegistroFormateadoByRegistroDetalle(idRegistroDetalle);
                postProcesoPlugin.actualizarInteresadoSalida(interesado, numeroRegistroFormateado);
            }
        }

    }

    public void postProcesoEliminarInteresado(Long idInteresado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO, false);

        if (postProcesoPlugin != null) {
            if (tipoRegistro.equals(REGISTRO_ENTRADA)) {
                String numeroRegistroFormateado = registroEntradaConsultaEjb.findNumeroRegistroFormateadoByRegistroDetalle(idRegistroDetalle);
                postProcesoPlugin.eliminarInteresadoEntrada(idInteresado, numeroRegistroFormateado);
            } else {
                String numeroRegistroFormateado = registroSalidaConsultaEjb.findNumeroRegistroFormateadoByRegistroDetalle(idRegistroDetalle);
                postProcesoPlugin.eliminarInteresadoSalida(idInteresado, numeroRegistroFormateado);
            }
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    @TransactionTimeout(value = 3000)  // 50 minutos
    public void capitalizarInteresadosJuridicos() throws I18NException {

        Query q = em.createQuery("Select interesado.id, interesado.razonSocial from Interesado as interesado  " +
                "where interesado.tipo =:tipoInteresado order by interesado.id");

        q.setParameter("tipoInteresado", RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            Query u = em.createQuery("update Interesado set razonSocial =:razonSocial where id =:idInteresado ");
            u.setParameter("idInteresado", object[0]);
            u.setParameter("razonSocial", StringUtils.capitailizeWord((String) object[1], true));
            u.executeUpdate();
            em.flush();
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    @TransactionTimeout(value = 3000)  // 50 minutos
    public void capitalizarInteresadosFisicas() throws I18NException {

        Query q = em.createQuery("Select interesado.id, interesado.nombre, interesado.apellido1, interesado.apellido2 from Interesado as interesado  " +
                "where interesado.tipo =:tipoInteresado order by interesado.id");

        q.setParameter("tipoInteresado", RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            Query u = em.createQuery("update Interesado set nombre =:nombre, apellido1 =:apellido1, apellido2 =:apellido2 where id =:idInteresado ");
            u.setParameter("idInteresado", object[0]);
            u.setParameter("nombre", StringUtils.capitailizeWord((String) object[1], false));
            u.setParameter("apellido1", StringUtils.capitailizeWord((String) object[2], false));
            u.setParameter("apellido2", StringUtils.capitailizeWord((String) object[3], false));
            u.executeUpdate();
            em.flush();
        }
    }

    /**
     * Actualiza el codigoDir3 y denominación de un Destinatario de tipo Administración
     * @param id
     * @param codigoDir3
     * @param razonSocial
     */
    public void actualizarDestinoExternoExtinguido(Long id, String codigoDir3, String razonSocial){
        Query q = em.createQuery("update Interesado set codigoDir3 = :codigoDir3,  razonSocial = :razonSocial where id = :id");
        q.setParameter("id", id);
        q.setParameter("codigoDir3", codigoDir3);
        q.setParameter("razonSocial", razonSocial);

        q.executeUpdate();
    }

}
