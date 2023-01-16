package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.InteresadoSir;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "InteresadoSirEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class InteresadoSirBean extends BaseEjbJPA<InteresadoSir, Long> implements InteresadoSirLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public InteresadoSir getReference(Long id) throws I18NException {

        return em.getReference(InteresadoSir.class, id);
    }

    @Override
    public InteresadoSir findById(Long id) throws I18NException {

        return em.find(InteresadoSir.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<InteresadoSir> getAll() throws I18NException {

        return em.createQuery("Select interesadoSir from InteresadoSir as interesadoSir order by interesadoSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(interesadoSir.id) from InteresadoSir as interesadoSir");
        q.setHint("org.hibernate.readOnly", true);
        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<InteresadoSir> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select interesadoSir from InteresadoSir as interesadoSir order by interesadoSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public InteresadoSir guardarInteresadoSir(InteresadoSir interesadoSir) throws I18NException {

        interesadoSir.setNombreInteresado(StringUtils.capitailizeWord(interesadoSir.getNombreInteresado(), false));
        interesadoSir.setPrimerApellidoInteresado(StringUtils.capitailizeWord(interesadoSir.getPrimerApellidoInteresado(), false));
        interesadoSir.setSegundoApellidoInteresado(StringUtils.capitailizeWord(interesadoSir.getSegundoApellidoInteresado(), false));
        interesadoSir.setRazonSocialInteresado(StringUtils.capitailizeWord(interesadoSir.getRazonSocialInteresado(), true));

        interesadoSir.setNombreRepresentante(StringUtils.capitailizeWord(interesadoSir.getNombreRepresentante(), false));
        interesadoSir.setPrimerApellidoRepresentante(StringUtils.capitailizeWord(interesadoSir.getPrimerApellidoRepresentante(), false));
        interesadoSir.setSegundoApellidoRepresentante(StringUtils.capitailizeWord(interesadoSir.getSegundoApellidoRepresentante(), false));
        interesadoSir.setRazonSocialRepresentante(StringUtils.capitailizeWord(interesadoSir.getRazonSocialRepresentante(), true));

        return persist(interesadoSir);
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> result = em.createQuery("Select distinct(i.id) from InteresadoSir as i where i.registroSir.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = result.size();

        if (result.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (result.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from InteresadoSir where id in (:result) ").setParameter("result", subList).executeUpdate();
                result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from InteresadoSir where id in (:result) ").setParameter("result", result).executeUpdate();
        }

        return total;

    }
}
