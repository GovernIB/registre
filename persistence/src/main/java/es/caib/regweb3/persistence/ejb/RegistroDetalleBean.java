package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.RegistroDetalle;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
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

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;


    @Override
    public RegistroDetalle getReference(Long id) throws Exception {

        return em.getReference(RegistroDetalle.class, id);
    }

    @Override
    public RegistroDetalle findById(Long id) throws Exception {

        return em.find(RegistroDetalle.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroDetalle> getAll() throws Exception {

        return  em.createQuery("Select registroDetalle from RegistroDetalle as registroDetalle order by registroDetalle.id").getResultList();
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
    public RegistroDetalle findByRegistroEntrada(Long idRegistroEntrada) throws Exception {

        Query q = em.createQuery("Select re.registroDetalle from RegistroEntrada as re where re.id = :idRegistroEntrada");
        q.setParameter("idRegistroEntrada",idRegistroEntrada);

        return (RegistroDetalle) q.getSingleResult();
    }

    @Override
    public Integer eliminar(Set<Long> ids) throws Exception, I18NException{

        for (Object id : ids) {

            RegistroDetalle registroDetalle = findById((Long)id);

            //Elimina los anexos
            for(Anexo anexo: registroDetalle.getAnexos()){
                anexoEjb.eliminarCustodia(anexo.getCustodiaID(), anexo.isJustificante());
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


    @Override
    public boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle) throws Exception, I18NException {

        Anexo anexo = anexoEjb.findById(idAnexo);
        RegistroDetalle registroDetalle = findById(idRegistroDetalle);


        if (anexo != null && registroDetalle != null) {
            log.info("Eliminar Anexo: " + registroDetalle.getAnexos().remove(anexo));
            merge(registroDetalle);
            anexoEjb.remove(anexo);
        }
        return anexoEjb.eliminarCustodia(anexo.getCustodiaID(), anexo.isJustificante());
    }
}
