package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.model.AnexoSir;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.EstadoAsientoRegistralSir;
import es.caib.regweb3.sir.core.model.InteresadoSir;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        asientoRegistralSir.setEstado(EstadoAsientoRegistralSir.RECIBIDO);


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

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select asr from AsientoRegistralSir as asr ");

//        if(codigoOficinaActiva!= null){where.add(" preRegistro.codigoUnidadTramitacionDestino = :codigoOficinaActiva "); parametros.put("codigoOficinaActiva",codigoOficinaActiva);}

        if (codigoOficinaActiva != null && codigoOficinaActiva.length() > 0) {
            where.add(DataBaseUtils.like("asr.codigoEntidadRegistralDestino", "codigoOficinaActiva", parametros, codigoOficinaActiva));
        }

        if (asientoRegistralSir.getResumen() != null && asientoRegistralSir.getResumen().length() > 0) {
            where.add(DataBaseUtils.like("asr.resumen", "resumen", parametros, asientoRegistralSir.getResumen()));
        }

        if (asientoRegistralSir.getNumeroRegistro() != null && asientoRegistralSir.getNumeroRegistro().length() > 0) {
            where.add(DataBaseUtils.like("asr.numeroRegistro", "numeroRegistro", parametros, asientoRegistralSir.getNumeroRegistro()));
        }

        if (estado != null) {
            where.add(" asr.estado = :estado "); parametros.put("estado",estado);
        }

        if(any!= null){where.add(" year(asr.fechaRegistro) = :any "); parametros.put("any",any);}

        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select asr from AsientoRegistralSir as asr ", "Select count(asr.id) from AsientoRegistralSir as asr "));
            query.append(" order by asr.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select asr from AsientoRegistralSir as asr ", "Select count(asr.id) from AsientoRegistralSir as asr "));
            query.append("order by asr.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    public List<AsientoRegistralSir> getUltimosARSPendientesProcesar(String codigoOficinaActiva, Integer total) throws Exception{

        Query q = em.createQuery("Select ars from AsientoRegistralSir as ars " +
                "where ars.codigoEntidadRegistralDestino = :codigoOficinaActiva and ars.estado = :idEstadoPreRegistro " +
                "order by ars.id desc");

        q.setMaxResults(total);
        q.setParameter("codigoOficinaActiva", codigoOficinaActiva);
        q.setParameter("idEstadoPreRegistro", EstadoAsientoRegistralSir.RECIBIDO);

        return  q.getResultList();
    }

}