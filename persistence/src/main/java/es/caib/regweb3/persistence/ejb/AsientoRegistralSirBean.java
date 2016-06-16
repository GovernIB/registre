package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.model.AnexoSir;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.InteresadoSir;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */

@Stateless(name = "AsientoRegistralSirEJB")
@SecurityDomain("seycon")
public class AsientoRegistralSirBean extends BaseEjbJPA<AsientoRegistralSir, Long> implements AsientoRegistralSirLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB public OficinaLocal oficinaEjb;
    @EJB public InteresadoSirLocal interesadoSirEjb;
    @EJB public AnexoSirLocal anexoSirEjb;


    @Override
    public AsientoRegistralSir findById(Long id) throws Exception {

        return em.find(AsientoRegistralSir.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<AsientoRegistralSir> getAll() throws Exception {

        return em.createQuery("Select asientoRegistralSir from AsientoRegistralSir as asientoRegistralSir order by asientoRegistralSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(asientoRegistralSir.id) from AsientoRegistralSir as asientoRegistralSir");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<AsientoRegistralSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select asientoRegistralSir from AsientoRegistralSir as asientoRegistralSir order by asientoRegistralSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public AsientoRegistralSir crearAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) throws Exception{

        // Obtenemos la Entidad a la que pertenece la Oficina a la que va dirigida este AsientoRegistralSir
        Entidad entidad = new Entidad(oficinaEjb.obtenerEntidad(asientoRegistralSir.getCodigoEntidadRegistralDestino()));
        asientoRegistralSir.setEntidad(entidad);

        asientoRegistralSir = persist(asientoRegistralSir);

        // Guardamos los Interesados
        if(asientoRegistralSir.getInteresados() != null && asientoRegistralSir.getInteresados().size() > 0){
            for(InteresadoSir interesadoSir: asientoRegistralSir.getInteresados()){
                interesadoSir.setIdAsientoRegistralSir(asientoRegistralSir);

                interesadoSirEjb.persist(interesadoSir);
            }
        }

        // Guardamos los Anexos
        if(asientoRegistralSir.getAnexos() != null && asientoRegistralSir.getAnexos().size() > 0){
            for(AnexoSir anexoSir: asientoRegistralSir.getAnexos()){
                anexoSir.setIdAsientoRegistralSir(asientoRegistralSir);

                anexoSirEjb.persist(anexoSir);
            }
        }
        em.flush();
        return asientoRegistralSir;
    }

    @Override
    public Boolean tieneAsientoRegistralSir(String codigoOficinaActiva) throws Exception {

        Query q = em.createQuery("Select count(asientoRegistralSir.id) from AsientoRegistralSir as asientoRegistralSir where " +
                "asientoRegistralSir.codigoEntidadRegistralDestino = :codigoOficinaActiva");

        q.setParameter("codigoOficinaActiva",codigoOficinaActiva);

        return (Long) q.getSingleResult() > 0;
    }

    public Paginacion busqueda(Integer pageNumber, Integer any, AsientoRegistralSir asientoRegistralSir, String codigoOficinaActiva, Long estado) throws Exception{
        return null;
    }

}