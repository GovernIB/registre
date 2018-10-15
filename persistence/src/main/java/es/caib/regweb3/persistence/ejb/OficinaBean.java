package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
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
 * Date: 16/01/14
 */

@Stateless(name = "OficinaEJB")
@SecurityDomain("seycon")
public class OficinaBean extends BaseEjbJPA<Oficina, Long> implements OficinaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;
    @EJB private RelacionSirOfiLocal relacionSirOfiEjb;
    @EJB private CatServicioLocal catServicioEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;


    @Override
    public Oficina getReference(Long id) throws Exception {

        return em.getReference(Oficina.class, id);
    }

    @Override
    public Oficina findById(Long id) throws Exception {

        Oficina oficina = em.find(Oficina.class, id);
        Hibernate.initialize(oficina.getOrganizativasOfi());
        return oficina;
    }



    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> getAll() throws Exception {

        return  em.createQuery("Select oficina from Oficina as oficina order by oficina.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(oficina.id) from Oficina as oficina");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina order by oficina.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo = :codigo");

        q.setParameter("codigo", codigo);

        List<Oficina> oficina = q.getResultList();
        if(oficina.size() == 1){
            return oficina.get(0);
        }else{
            return  null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo = :codigo and oficina.organismoResponsable.entidad.id=:idEntidad ");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);

        List<Oficina> oficina = q.getResultList();
        if (oficina.size() == 1) {
            return oficina.get(0);
        } else {
            return null;
        }

    }

    @Override
    public Oficina findByCodigoLigero(String codigo) throws Exception {

        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion from Oficina as oficina where " +
                "oficina.codigo = :codigo");

        q.setParameter("codigo", codigo);

        List<Object[]> result = q.getResultList();
        if (result.size() > 0) {
            return new Oficina((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
        }

        return null;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigoEntidad(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo =:codigo and oficina.organismoResponsable.entidad.id = :idEntidad  and " +
                "oficina.estado.codigoEstadoEntidad =:vigente");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<Oficina> oficina = q.getResultList();
        if (oficina.size() == 1) {
            return oficina.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigoVigente(String codigo) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo =:codigo and " +
                "oficina.estado.codigoEstadoEntidad =:vigente");

        q.setParameter("codigo",codigo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<Oficina> oficina = q.getResultList();
        if(oficina.size() == 1){
            return oficina.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByOrganismoResponsable(Long idOrganismo) throws Exception{
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.id =:idOrganismo and " +
                "oficina.estado.codigoEstadoEntidad=:vigente");

        q.setParameter("idOrganismo",idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Oficina> oficinas = q.getResultList();
        for(Oficina oficina:oficinas){
          Hibernate.initialize(oficina.getOrganizativasOfi());
        }
        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> oficinasFuncionales(Long idOrganismo, Boolean oficinaVirtual) throws Exception{

        String oficinaVirtualWhere = "";

        if (!oficinaVirtual) {
            oficinaVirtualWhere = " and :oficinaVirtual not in elements(oficina.servicios)";
        }

        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion as nombre, oficina.organismoResponsable.id from Oficina as oficina where " +
                "oficina.organismoResponsable.id =:idOrganismo and " +
                "oficina.estado.codigoEstadoEntidad=:vigente  " +
                oficinaVirtualWhere);

        q.setParameter("idOrganismo",idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        if (!oficinaVirtual) {
            q.setParameter("oficinaVirtual", catServicioEjb.findByCodigo(RegwebConstantes.REGISTRO_VIRTUAL_NO_PRESENCIAL));
        }

        List<Oficina> oficinas =  new ArrayList<Oficina>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Oficina oficina = new Oficina((Long)object[0],(String)object[1],(String)object[2],(Long)object[3]);

            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    public List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws Exception{
        return  relacionOrganizativaOfiEjb.oficinasOrganizativas(idOrganismo, oficinaVirtual);
    }

    @Override
    public List<Oficina> oficinasSIR(Long idOrganismo) throws Exception{
        return  relacionSirOfiEjb.oficinasSIR(idOrganismo);
    }

    @Override
    public List<Oficina> oficinasSIREntidad(Long idEntidad) throws Exception{
        return  relacionSirOfiEjb.oficinasSIREntidad(idEntidad);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByEntidad(Long idEntidad) throws Exception{
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad");

        q.setParameter("idEntidad",idEntidad);
        List<Oficina> oficinas = q.getResultList();
        for(Oficina oficina:oficinas){
          Hibernate.initialize(oficina.getOrganizativasOfi());
        }
        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByEntidadByEstado(Long idEntidad, String estado) throws Exception{

        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad=:estado");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        List<Oficina> oficinas =  new ArrayList<Oficina>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Oficina oficina = new Oficina((Long)object[0],(String)object[1],(String)object[2]);
            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> responsableByEntidadEstado(Long idEntidad, String estado) throws Exception{
        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion, oficina.organismoResponsable.id from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad =:estado and " +
                "oficina.oficinaResponsable.id = null order by oficina.codigo");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        List<Object[]> result = q.getResultList();
        List<Oficina> oficinas = new ArrayList<Oficina>();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> dependienteByEntidadEstado(Long idEntidad, String estado) throws Exception{
        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion, oficina.oficinaResponsable.id, oficina.organismoResponsable.id from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad =:estado and " +
                "oficina.oficinaResponsable.id != null order by oficina.codigo");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        List<Object[]> result = q.getResultList();
        List<Oficina> oficinas = new ArrayList<Oficina>();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2], (Long) object[3], (Long) object[4]);

            oficinas.add(oficina);
        }

        return oficinas;


        /*for(Oficina oficina:oficinas){
          Hibernate.initialize(oficina.getOrganizativasOfi());
        }*/
    }

    @Override
    public LinkedHashSet<Oficina> oficinasRegistro(List<Libro> libros) throws Exception {

        LinkedHashSet<Oficina> oficinas = new LinkedHashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        // Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
        for (Libro libro : libros) {
            Long idOrganismo = libro.getOrganismo().getId();
            oficinas.addAll(oficinasFuncionales(idOrganismo, RegwebConstantes.OFICINA_VIRTUAL_NO));
            oficinas.addAll(oficinasOrganizativas(idOrganismo, RegwebConstantes.OFICINA_VIRTUAL_NO));
        }

        return oficinas;
    }

    @Override
    public LinkedHashSet<Oficina> oficinasSIR(List<Libro> libros) throws Exception {

        LinkedHashSet<Oficina> oficinas = new LinkedHashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        // Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas con relación SIR
        for (Libro libro : libros) {
            Long idOrganismo = libro.getOrganismo().getId();
            oficinas.addAll(oficinasSIR(idOrganismo));
        }

        return oficinas;
    }

    @Override
    public Boolean isSIRRecepcion(Long idOficina) throws Exception {

        Query q = em.createQuery("Select oficina.id from Oficina as oficina where " +
                "oficina.id =:idOficina and oficina.estado.codigoEstadoEntidad=:vigente and " +
                ":recepcionSir in elements(oficina.servicios)");

        q.setParameter("idOficina",idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("recepcionSir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_RECEPCION));

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean isSIREnvio(Long idOficina) throws Exception {

        Query q = em.createQuery("Select oficina.id from Oficina as oficina where " +
                "oficina.id =:idOficina and oficina.estado.codigoEstadoEntidad=:vigente and " +
                ":envioSir in elements(oficina.servicios)");

        q.setParameter("idOficina",idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("envioSir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_ENVIO));

        return q.getResultList().size() > 0;
    }

    @Override
    public LinkedHashSet<Oficina> oficinasServicio(Long idOrganismo, Boolean oficinaVirtual) throws Exception {

        Organismo organismo = organismoEjb.findById(idOrganismo);

        LinkedHashSet<Oficina> oficinas = new LinkedHashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        oficinas.addAll(oficinasFuncionales(idOrganismo, oficinaVirtual));
        oficinas.addAll(oficinasOrganizativas(idOrganismo, oficinaVirtual));

        if (organismo.getOrganismoSuperior() != null /*&& !organismo.getEdp()*/) {
            oficinas.addAll(oficinasServicio(organismo.getOrganismoSuperior().getId(), oficinaVirtual));
        }

        return  oficinas;

    }

    @Override
    public Boolean tieneOficinasServicio(Long idOrganismo, Boolean oficinaVirtual) throws Exception {

        LinkedHashSet<Oficina> oficinas = oficinasServicio(idOrganismo, oficinaVirtual);

        return oficinas.size() > 0;
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{


        List<?> oficinasRaiz = em.createQuery("Select distinct(id) from Oficina where organismoResponsable.entidad.id =:idEntidad and oficinaResponsable != null ").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = oficinasRaiz.size();

        if (oficinasRaiz.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (oficinasRaiz.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = oficinasRaiz.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Servicios oficinas raiz eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", subList).executeUpdate());
                em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", subList).executeUpdate();
                oficinasRaiz.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            log.info("Servicios oficinas raiz eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", oficinasRaiz).executeUpdate());
            em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", oficinasRaiz).executeUpdate();

        }

        List<?> oficinasAuxiliares = em.createQuery("Select distinct(id) from Oficina  where organismoResponsable.entidad.id =:idEntidad and oficinaResponsable is null ").setParameter("idEntidad", idEntidad).getResultList();
        total = total + oficinasAuxiliares.size();

        if (oficinasAuxiliares.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (oficinasAuxiliares.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = oficinasAuxiliares.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Servicios oficinas auxiliares eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", subList).executeUpdate());
                em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", subList).executeUpdate();
                oficinasAuxiliares.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            log.info("Servicios oficinas auxiliares eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", oficinasAuxiliares).executeUpdate());
            em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", oficinasAuxiliares).executeUpdate();
        }

        return total;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, String codigo, String denominacion, Long idCatEstadoEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select oficina from Oficina as oficina ");

        // Entidad a la que pertenece
        where.add(" oficina.organismoResponsable.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);

        // Estado de la Oficina
        where.add(" oficina.estado.id = :idCatEstadoEntidad");
        parametros.put("idCatEstadoEntidad", idCatEstadoEntidad);


        if (StringUtils.isNotEmpty(codigo)) {
            where.add(DataBaseUtils.like("oficina.codigo", "codigo", parametros, codigo));
        }
        if (StringUtils.isNotEmpty(denominacion)) {
            where.add(DataBaseUtils.like("oficina.denominacion", "denominacion", parametros, denominacion));
        }

        where.add(" oficina.estado.codigoEstadoEntidad = :vigente ");
        parametros.put("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

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
            q2 = em.createQuery(query.toString().replaceAll("Select oficina from Oficina as oficina ", "Select count(oficina.id) from Oficina as oficina "));
            query.append("order by oficina.codigo");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select oficina from Oficina as oficina ", "Select count(oficina.id) from Oficina as oficina "));
            query.append("order by oficina.codigo");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

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

    @Override
    public LinkedHashSet<UsuarioEntidad> usuariosPermisoOficina(Long idOficina) throws Exception{

        LinkedHashSet<Libro> libros = new LinkedHashSet<Libro>();
        LinkedHashSet<UsuarioEntidad> usuarios = new LinkedHashSet<UsuarioEntidad>();

        //Obtener los usuarios que registran en esa oficina
        LinkedHashSet<Organismo> organismos = organismoEjb.getByOficinaActiva(findById(idOficina));

        // Obtenemos los libros de cada Organismo
        for (Organismo organismo : organismos) {
            libros.addAll(libroEjb.getLibrosActivosOrganismo(organismo.getId()));
        }

        usuarios.addAll(permisoLibroUsuarioEjb.getUsuariosPermiso(libros, RegwebConstantes.PERMISO_SIR));

        return usuarios;
    }

    /**
     * Obtiene el id de la Entidad a la que pertenece la Oficina
     * @param codigo
     * @return
     * @throws Exception
     */
    public Long obtenerEntidad(String codigo) throws Exception{
        Query q = em.createQuery("Select oficina.organismoResponsable.entidad.id from Oficina as oficina where " +
                "oficina.codigo =:codigo and oficina.organismoResponsable.entidad.sir = true");

        q.setParameter("codigo",codigo);

        return (Long) q.getSingleResult();
    }
}
