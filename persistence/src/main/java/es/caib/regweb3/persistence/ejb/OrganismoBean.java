package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RelacionSirOfi;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 * Date: 16/01/14
 */

@Stateless(name = "OrganismoEJB")
@SecurityDomain("seycon")
public class OrganismoBean extends BaseEjbJPA<Organismo, Long> implements OrganismoLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @Override
    public Organismo findById(Long id) throws Exception {

        return em.find(Organismo.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getAll() throws Exception {

        return  em.createQuery("Select organismo from Organismo as organismo order by organismo.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(organismo.id) from Organismo as organismo");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotalByEntidad(Long entidad) throws Exception {

        Query q = em.createQuery("Select count(organismo.id) from Organismo as organismo where " +
                            "organismo.entidad.id = :entidad");

        q.setParameter("entidad",entidad);

        return (Long) q.getSingleResult();
    }
    
    @Override
    public List<Organismo> getAllByEntidad(Long entidad) throws Exception {

          Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                      "organismo.entidad.id = :entidad");

          q.setParameter("entidad",entidad);

          return q.getResultList();
    }


    @Override
    public List<Organismo> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo order by organismo.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<Organismo> getPaginationByEntidad(int inicio, Long entidad) throws Exception {

          Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                      "organismo.entidad.id = :entidad");

          q.setParameter("entidad",entidad);
          q.setFirstResult(inicio);
          q.setMaxResults(RESULTADOS_PAGINACION);

          return q.getResultList();
    }

    @Override
    public Organismo findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.codigo = :codigo");

        q.setParameter("codigo",codigo);

        List<Organismo> organismo = q.getResultList();
        if(organismo.size() == 1){
            return organismo.get(0);
        }else{
            return  null;
        }

    }

    @Override
    public Organismo findByCodigoVigente(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.codigo = :codigo and organismo.entidad.id = :idEntidad and " +
                "organismo.estado.codigoEstadoEntidad=:vigente");

        q.setParameter("codigo",codigo);
        q.setParameter("idEntidad",idEntidad);
        q.setParameter("vigente",RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<Organismo> organismo = q.getResultList();
        if(organismo.size() == 1){
            return organismo.get(0);
        }else{
            return  null;
        }

    }

    @Override
     public List<Organismo> findByEntidad(Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad",entidad);

        return  q.getResultList();

    }

    @Override
    public List<Organismo> findByEntidadByEstado(Long entidad, String estado) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.entidad.id = :entidad and organismo.estado.codigoEstadoEntidad = :codigoEstado");

        q.setParameter("entidad",entidad);
        q.setParameter("codigoEstado",estado);

        return  q.getResultList();

    }

    @Override
    public List<Organismo> findByEntidadEstadoConOficinas(Long entidad, String codigoEstado) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.entidad.id = :entidad and organismo.estado.codigoEstadoEntidad =:codigoEstado");

        q.setParameter("entidad",entidad);
        q.setParameter("codigoEstado",codigoEstado);
        List<Organismo> organismos = q.getResultList();
        List<Organismo> organismosConOficinas = new ArrayList<Organismo>();

        for(Organismo organismo: organismos){
           if(oficinaEjb.tieneOficinasOrganismo(organismo.getId())){
               organismosConOficinas.add(organismo);
           }
        }
        return organismosConOficinas;
    }

    @Override
    public List<Organismo> findByEstado(String codigoEstado) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.estado.codigoEstadoEntidad = :codigoEstado");

        q.setParameter("codigoEstado",codigoEstado);

        return  q.getResultList();

    }

    @Override
    public List<Organismo> getHijos(Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.organismoSuperior = :idOrganismo");

        q.setParameter("idOrganismo",idOrganismo);

        return  q.getResultList();

    }

    @Override
    public List<Organismo> getOrganismosPrimerNivel(Long idOrganismoSuperior) throws Exception{

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.organismoSuperior.id = :idOrganismoSuperior");

        q.setParameter("idOrganismoSuperior",idOrganismoSuperior);

        return  q.getResultList();
    }

    @Override
    public List<Organismo> getOrganismosByNivel(Long nivel, Long idEntidad, String estado) throws Exception{

        Query q = em.createQuery("Select organismo.id,organismo.codigo, organismo.denominacion, organismo.organismoSuperior.id from Organismo as organismo where " +
                "organismo.nivelJerarquico = :nivel and organismo.entidad.id = :idEntidad and organismo.estado.codigoEstadoEntidad = :estado order by organismo.codigo");

        q.setParameter("nivel",nivel);
        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        List<Organismo> organismos = new ArrayList<Organismo>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            organismos.add(organismo);
        }

        return organismos;
    }


    @Override
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, String denominacion,Long idCatEstadoEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();
        //CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        StringBuffer query = new StringBuffer("Select organismo from Organismo as organismo ");

        if(denominacion!= null && denominacion.length() > 0){where.add(DataBaseUtils.like("organismo.denominacion","denominacion",parametros,denominacion));}
        if(idCatEstadoEntidad != null && idCatEstadoEntidad > 0) {
          where.add(" organismo.estado.id = :idCatEstadoEntidad");
          parametros.put("idCatEstadoEntidad",idCatEstadoEntidad);
        }

        // Añadimos la Entidad
        where.add("organismo.entidad.id = :idEntidad "); parametros.put("idEntidad",idEntidad);

        // Añadimos el estado vigente, solo retornará los Organismos vigentes
        //where.add("organismo.estado.id = :vigente "); parametros.put("vigente",vigente.getId());


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
             q2 = em.createQuery(query.toString().replaceAll("Select organismo from Organismo as organismo ", "Select count(organismo.id) from Organismo as organismo "));
             query.append("order by organismo.denominacion");
             q = em.createQuery(query.toString());

             for (Map.Entry<String, Object> param : parametros.entrySet()) {
                 q.setParameter(param.getKey(), param.getValue());
                 q2.setParameter(param.getKey(), param.getValue());
             }

         }else{
             q2 = em.createQuery(query.toString().replaceAll("Select persona from Persona as persona ", "Select count(organismo.id) from Organismo as organismo "));
             query.append("order by organismo.denominacion");
             q = em.createQuery(query.toString());
         }
         log.info("Query: " + query);

         Paginacion paginacion = null;

         if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
             Long total = (Long)q2.getSingleResult();
             paginacion = new Paginacion(total.intValue(), pageNumber);
             int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
             q.setFirstResult(inicio);
             q.setMaxResults(RESULTADOS_PAGINACION);
         }else{
             paginacion = new Paginacion(0, 0);
         }

         paginacion.setListado(q.getResultList());

         return paginacion;

     }

  /**
   * Método que obtiene los organismos vigentes y en los que puede registrar de la oficina activa
   * @param oficinaActiva
   * @return List
   * @throws Exception
   */
    @Override
    public Set<Organismo> getByOficinaActiva(Oficina oficinaActiva) throws Exception{

        // Añadimos los organismos funcionales
        Set<Organismo> organismos = oficinaActiva.getOrganismosFuncionales();

        // variable que representa el arbol de los organismos de la oficina activa

        Set<Organismo> hijosTotales = new HashSet<Organismo>();
        obtenerHijosOrganismos(organismos, hijosTotales);
        organismos.addAll(hijosTotales);

        return organismos;

    }

    /**
     * Método que nos devuelve los códigos DIR3 de las oficinas SIR de un organismo
     * @param idOrganismo identificador del organismo
     * @return
     * @throws Exception
     */
    public List<String> organismoSir(Long idOrganismo) throws Exception {
        List<String> oficinasSir = new ArrayList<String>();

        Query q = em.createQuery("Select relacionSirOfi from RelacionSirOfi as relacionSirOfi where " +
                "relacionSirOfi.organismo.id = :idOrganismo ");

        List<RelacionSirOfi> relSir= q.getResultList();
        if(!relSir.isEmpty()){
          for(RelacionSirOfi rel:relSir){
            oficinasSir.add(rel.getOficina().getCodigo());
          }
        }
        return oficinasSir;
    }


    /**
     * Metodo que recursivamente obtiene todos los hijos de una lista de organismos
     * @param organismosPadres organismos de los que obtener sus hijos
     * @param totales organismos totales obtenidos despues del proceso recursivo.
     * @throws Exception
     */
     private void obtenerHijosOrganismos(Set<Organismo> organismosPadres, Set<Organismo> totales) throws Exception {

         // recorremos para todos los organismos Padres
         for(Organismo org: organismosPadres){

           Query q = em.createQuery("select organismo from Organismo as organismo where organismo.organismoSuperior.id =:idOrganismoSuperior and organismo.estado.codigoEstadoEntidad =:vigente ");
           q.setParameter("idOrganismoSuperior", org.getId());
           q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

           Set<Organismo> hijos = new HashSet<Organismo>(q.getResultList());

           totales.addAll(hijos);

           // Hijos de cada organismo
           obtenerHijosOrganismos(hijos, totales);

         }

     }

  /**
   * Obtiene todos los organismos(padres e hijos) a los que da servicio el libro
   * @param idLibro identificador del libro
   * @return
   * @throws Exception
   */
     public List<Organismo> getByLibro(Long idLibro) throws Exception {
       // Obtenemos el organismo responsable del libro.
       Query q2 = em.createQuery("Select libro.organismo from Libro as libro where libro.id=:idLibro");
       q2.setParameter("idLibro",idLibro);
       Set<Organismo> organismosPadres = new HashSet<Organismo>(q2.getResultList());

       // aquí guardaremos todos los organismo, el padre y sus hijos.
       Set<Organismo> totales = new HashSet<Organismo>();
       totales.addAll(organismosPadres);
       // recursivamente obtenemos los hijos de los organismos padre.
       obtenerHijosOrganismos(organismosPadres, totales);

       return  new ArrayList<Organismo>(totales);
     }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> organismos = em.createQuery("Select distinct(id) from Organismo where entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = organismos.size();

        if(organismos.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (organismos.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = organismos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Historico UO eliminados: " + em.createNativeQuery("delete from RWE_HISTORICOUO WHERE CODULTIMA in(:organismos)").setParameter("organismos", subList).executeUpdate());
                em.createQuery("delete from Organismo where id in (:organismos) ").setParameter("organismos", subList).executeUpdate();
                organismos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            log.info("Historico UO eliminados: " + em.createNativeQuery("delete from RWE_HISTORICOUO WHERE CODULTIMA in(:organismos)").setParameter("organismos", organismos).executeUpdate());
            em.createQuery("delete from Organismo where id in (:organismos) ").setParameter("organismos", organismos).executeUpdate();
        }

        return total;

    }
}
