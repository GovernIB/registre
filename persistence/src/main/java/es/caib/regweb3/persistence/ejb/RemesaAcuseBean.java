package es.caib.regweb3.persistence.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.RemesaAcuse;
import es.caib.regweb3.persistence.utils.LemaPluginHelper;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Stateless(name = "RemesaAcuseEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RemesaAcuseBean extends BaseEjbJPA<RemesaAcuse, Long> implements RemesaAcuseLocal {

	protected final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "regweb3")
	private EntityManager em;

	@Autowired LemaPluginHelper pluginHelper;

	@Override
	public RemesaAcuse getReference(Long id) throws Exception {

		return em.getReference(RemesaAcuse.class, id);
	}

	@Override
	public RemesaAcuse findById(Long id) throws Exception {

		return em.find(RemesaAcuse.class, id);
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<RemesaAcuse> getAll() throws Exception {

		return em.createQuery("Select remesaAcuse from RemesaAcuse as remesaAcuse order by remesaAcuse.id").getResultList();
	}

	@Override
	public Long getTotal() throws Exception {

		Query q = em.createQuery("Select count(remesaAcuse.id) from RemesaAcuse as remesaAcuse");
		q.setHint("org.hibernate.readOnly", true);

		return (Long) q.getSingleResult();
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<RemesaAcuse> getPagination(int inicio) throws Exception {

		Query q = em.createQuery("Select remesaAcuse from RemesaAcuse as remesaAcuse order by remesaAcuse.id");
		q.setFirstResult(inicio);
		q.setMaxResults(RESULTADOS_PAGINACION);
		q.setHint("org.hibernate.readOnly", true);

		return q.getResultList();
	}

	@Override
	public RemesaAcuse crearReferenciaAcuse(byte[] referencia, String csvResguardo, Remesa remesa) throws Exception {
		RemesaAcuse acuse = new RemesaAcuse(null, null, null, referencia, csvResguardo, remesa);
		
		return persist(acuse);
	}

	@Override
	public RemesaAcuse findByRemesa(Long remesaId) {
		Query q = em.createQuery("Select remesaAcuse from RemesaAcuse as remesaAcuse where remesaAcuse.remesa.id = :remesaId");

        q.setParameter("remesaId", remesaId);
        q.setHint("org.hibernate.readOnly", true);

        List<RemesaAcuse> remesaAcuse = q.getResultList();

        if (remesaAcuse.size() > 0) {
            return remesaAcuse.get(0);
        } else {
            return null;
        }
	}
	
	@Override
	public void actualizarAcuseRecibo(String nombre, String mimeType, String metadatos) {
		// TODO Auto-generated method stub
		
	}

}
