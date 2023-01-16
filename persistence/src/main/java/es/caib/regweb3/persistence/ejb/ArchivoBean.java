package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.persistence.utils.FileSystemManager;
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
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Stateless(name = "ArchivoEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class ArchivoBean extends BaseEjbJPA<Archivo, Long> implements ArchivoLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @Override
    public Archivo getReference(Long id) throws I18NException {

        return em.getReference(Archivo.class, id);
    }

    @Override
    public Archivo findById(Long id) throws I18NException {

        return em.find(Archivo.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Archivo> getAll() throws I18NException {

        return  em.createQuery("Select archivo from Archivo as archivo order by archivo.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(archivo.id) from Archivo as archivo");

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Archivo> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select archivo from Archivo as archivo order by archivo.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<Long> getAllLigero() throws I18NException {

        Query q = em.createQuery("Select archivo.id from Archivo as archivo order by archivo.id");

        return q.getResultList();
    }

    /**
     * Elimina un archivo de la bbdd y del sistema de archivos
     * @param archivo
     * @return
     */
    @Override
    public boolean borrarArchivo(Archivo archivo) throws I18NException{
        if (archivo != null) {
            try {
                if (findById(archivo.getId()) != null) {
                    remove(archivo);
                }
            } catch (Exception e) {
                log.error("Error borrant archivo fisic amb id=" + archivo.getId()
                        + "(" + archivo.getNombre() + ")");
            }

            return FileSystemManager.eliminarArchivo(archivo.getId());
        }
        return true;
    }
}
