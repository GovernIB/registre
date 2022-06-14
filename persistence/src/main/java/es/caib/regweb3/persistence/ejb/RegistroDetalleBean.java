package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RegistroDetalleEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class RegistroDetalleBean extends BaseEjbJPA<RegistroDetalle, Long> implements RegistroDetalleLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private AnexoLocal anexoEjb;


    @Override
    public RegistroDetalle getReference(Long id) throws Exception {

        return em.getReference(RegistroDetalle.class, id);
    }

    @Override
    public RegistroDetalle findById(Long id) throws Exception {

        return em.find(RegistroDetalle.class, id);
    }

    @Override
    public RegistroDetalle findByIdConInteresados(Long id) throws Exception {

        RegistroDetalle registroDetalle = findById(id);

        Hibernate.initialize(registroDetalle.getInteresados());

        return registroDetalle;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroDetalle> getAll() throws Exception {

        return em.createQuery("Select registroDetalle from RegistroDetalle as registroDetalle order by registroDetalle.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroDetalle.id) from RegistroDetalle as registroDetalle");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroDetalle> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroDetalle from RegistroDetalle as registroDetalle order by registroDetalle.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public RegistroDetalle findByRegistroEntrada(Long idRegistroEntrada) throws Exception {

        Query q = em.createQuery("Select re.registroDetalle from RegistroEntrada as re where re.id = :idRegistroEntrada");
        q.setParameter("idRegistroEntrada", idRegistroEntrada);

        return (RegistroDetalle) q.getSingleResult();
    }

    @Override
    public Integer eliminar(Set<Long> ids, Long idEntidad) throws Exception, I18NException {

        for (Object id : ids) {

            RegistroDetalle registroDetalle = findById((Long) id);

            //Elimina los anexos
            for (Anexo anexo : registroDetalle.getAnexos()) {
                anexoEjb.eliminarCustodia(anexo.getCustodiaID(), anexo, idEntidad);
            }
            remove(registroDetalle);

            em.flush();
        }

        return ids.size();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Set<Long> getRegistrosDetalle(Long idEntidad) throws Exception {
        Set<Long> registrosDetalle = new HashSet<Long>();

        registrosDetalle.addAll(em.createQuery("Select distinct(registroDetalle.id) from RegistroEntrada where entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList());
        registrosDetalle.addAll(em.createQuery("Select distinct(registroDetalle.id) from RegistroSalida where entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList());

        return registrosDetalle;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Set<Long> getRegistrosDetalleConfirmados(Long idEntidad, Date fecha) throws Exception {
        Set<Long> registrosDetalle = new HashSet<Long>();
        //Obtenemos los registros detalle de los registros de entrada que se han aceptado
        Query query = em.createQuery("Select distinct(registroDetalle.id) from RegistroEntrada where entidad.id = :idEntidad and estado =:aceptado");
        query.setParameter("idEntidad", idEntidad);
        query.setParameter("aceptado", RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
        registrosDetalle.addAll(query.getResultList());

        //Obtenemos los registros detalle de los registros de salida que se han aceptado
        Query queryS = em.createQuery("Select distinct(registroDetalle.id) from RegistroSalida where entidad.id = :idEntidad and estado =:aceptado");
        queryS.setParameter("idEntidad", idEntidad);
        queryS.setParameter("aceptado", RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
        registrosDetalle.addAll(queryS.getResultList());

        registrosDetalle.addAll(queryS.getResultList());

        return registrosDetalle;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle, Long idEntidad) throws Exception, I18NException {

        Anexo anexo = anexoEjb.findById(idAnexo);
        RegistroDetalle registroDetalle = findById(idRegistroDetalle);


        if (anexo != null && registroDetalle != null) {
            log.info("Eliminar Anexo: " + registroDetalle.getAnexos().remove(anexo));
            merge(registroDetalle);
            anexoEjb.remove(anexo);
        }
        return anexoEjb.eliminarCustodia(anexo.getCustodiaID(), anexo, idEntidad);
    }
}
