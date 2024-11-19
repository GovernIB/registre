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
import es.caib.regweb3.model.RemesaAnexo;
import es.caib.regweb3.persistence.utils.LemaPluginHelper;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Stateless(name = "RemesaAnexoEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RemesaAnexoBean extends BaseEjbJPA<RemesaAnexo, Long> implements RemesaAnexoLocal {

	protected final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "regweb3")
	private EntityManager em;

	@Autowired LemaPluginHelper pluginHelper;

	@Override
	public RemesaAnexo getReference(Long id) throws Exception {

		return em.getReference(RemesaAnexo.class, id);
	}

	@Override
	public RemesaAnexo findById(Long id) throws Exception {

		return em.find(RemesaAnexo.class, id);
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<RemesaAnexo> getAll() throws Exception {

		return em.createQuery("Select remesaAnexo from RemesaAnexo as remesaAnexo order by remesaAnexo.id").getResultList();
	}

	@Override
	public Long getTotal() throws Exception {

		Query q = em.createQuery("Select count(remesaAnexo.id) from RemesaAnexo as remesaAnexo");
		q.setHint("org.hibernate.readOnly", true);

		return (Long) q.getSingleResult();
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<RemesaAnexo> getPagination(int inicio) throws Exception {

		Query q = em.createQuery("Select remesaAnexo from RemesaAnexo as remesaAnexo order by remesaAnexo.id");
		q.setFirstResult(inicio);
		q.setMaxResults(RESULTADOS_PAGINACION);
		q.setHint("org.hibernate.readOnly", true);

		return q.getResultList();
	}

	@Override
	public RemesaAnexo crearReferenciaAnexo(String enlace, byte[] referencia, Remesa remesa) throws Exception {
		RemesaAnexo anexo = new RemesaAnexo(enlace, referencia, remesa);
		
		return persist(anexo);
	}

	@Override
	public RemesaAnexo findByRemesa(Long remesaId) {
		Query q = em.createQuery("Select remesaAnexo from RemesaAnexo as remesaAnexo where remesaAnexo.remesa.id = :remesaId");

        q.setParameter("remesaId", remesaId);
        q.setHint("org.hibernate.readOnly", true);

        List<RemesaAnexo> remesaAnexo = q.getResultList();

        if (remesaAnexo.size() > 0) {
            return remesaAnexo.get(0);
        } else {
            return null;
        }
	}
	
}
