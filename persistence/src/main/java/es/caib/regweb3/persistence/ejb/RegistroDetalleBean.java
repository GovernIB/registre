package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.persistence.utils.AnnexDocumentCustodyManager;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@SecurityDomain("seycon")
public class RegistroDetalleBean extends BaseEjbJPA<RegistroDetalle, Long> implements RegistroDetalleLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public RegistroDetalle findById(Long id) throws Exception {

        return em.find(RegistroDetalle.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroDetalle> getAll() throws Exception {

        return  em.createQuery("Select registroDetalle from RegistroDetalle as registroDetalle order by registroDetalle.orden").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroDetalle.id) from RegistroDetalle as registroDetalle");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<RegistroDetalle> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroDetalle from RegistroDetalle as registroDetalle order by registroDetalle.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Integer eliminar(Set<Long> ids) throws Exception{

        for (Object id : ids) {

            RegistroDetalle registroDetalle = findById((Long)id);

            //Elimina los anexos
            for(Anexo anexo: registroDetalle.getAnexos()){
                AnnexDocumentCustodyManager.eliminarCustodia(anexo.getCustodiaID());
            }
            remove(registroDetalle);

            em.flush();
        }

        return ids.size();
    }

    @Override
    public Set<Long> getRegistrosDetalle(Long idEntidad) throws Exception{
        Set<Long> registrosDetalle = new HashSet<Long>();

        registrosDetalle.addAll(em.createQuery("Select distinct(registroDetalle.id) from RegistroEntrada where usuario.entidad.id = :idEntidad").setParameter("idEntidad",idEntidad).getResultList()) ;
        registrosDetalle.addAll(em.createQuery("Select distinct(registroDetalle.id) from RegistroSalida where usuario.entidad.id = :idEntidad").setParameter("idEntidad",idEntidad).getResultList()) ;

        return registrosDetalle;
    }
}
