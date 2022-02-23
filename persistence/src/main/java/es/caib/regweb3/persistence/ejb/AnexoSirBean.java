package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "AnexoSirEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class AnexoSirBean extends BaseEjbJPA<AnexoSir, Long> implements AnexoSirLocal{

    protected final org.apache.log4j.Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public AnexoSir getReference(Long id) throws Exception {

        return em.getReference(AnexoSir.class, id);
    }

    @Override
    public AnexoSir findById(Long id) throws Exception {

        return em.find(AnexoSir.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<AnexoSir> getAll() throws Exception {

        return  em.createQuery("Select anexoSir from AnexoSir as anexoSir order by anexoSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(anexoSir.id) from AnexoSir as anexoSir");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<AnexoSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select anexoSir from AnexoSir as anexoSir order by anexoSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int purgarAnexosAceptados(Long idEntidad) throws Exception{

        Integer numElementos = PropiedadGlobalUtil.getNumElementosPurgoAnexos(idEntidad);

        Query q = em.createQuery("Select anexoSir.id, anexoSir.anexo.id from AnexoSir as anexoSir where anexoSir.registroSir.entidad.id = :idEntidad and " +
                "anexoSir.purgado = false and anexoSir.registroSir.estado = :aceptado order by anexoSir.id");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("aceptado", EstadoRegistroSir.ACEPTADO);
        //q.setFirstResult(0);
        q.setMaxResults(numElementos);

        //q.setParameter("reenviado", EstadoRegistroSir.REENVIADO_Y_ACK);
        //q.setParameter("rechazado", EstadoRegistroSir.RECHAZADO_Y_ACK);
        List<Object[]> result = q.getResultList();
        List<AnexoSir> anexos = new ArrayList<>();

        for (Object[] object : result) {
            AnexoSir anexoSir = new AnexoSir();
            anexoSir.setId((Long) object[0]);
            anexoSir.setAnexo(new Archivo((Long) object[1]));

            anexos.add(anexoSir);
        }

        // Eliminamos los Archivos del RegistroSir
        for (AnexoSir anexoSir: anexos) {
            FileSystemManager.eliminarArchivo(anexoSir.getAnexo().getId());
            em.createQuery("update from AnexoSir set purgado=true where id=:idAnexoSir").setParameter("idAnexoSir", anexoSir.getId()).executeUpdate();
        }

        return anexos.size();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> result = em.createQuery("Select distinct(a.id) from AnexoSir as a where a.registroSir.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = result.size();

        if(result.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (result.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from AnexoSir where id in (:result) ").setParameter("result", subList).executeUpdate();
                result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from AnexoSir where id in (:result) ").setParameter("result", result).executeUpdate();
        }

        return total;

    }
}
