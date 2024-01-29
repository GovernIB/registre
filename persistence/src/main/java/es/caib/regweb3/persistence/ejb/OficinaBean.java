package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.Dir3Caib;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class OficinaBean extends BaseEjbJPA<Oficina, Long> implements OficinaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;
    @EJB private RelacionSirOfiLocal relacionSirOfiEjb;
    @EJB private CatServicioLocal catServicioEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;



    @Override
    public Oficina getReference(Long id) throws I18NException {

        return em.getReference(Oficina.class, id);
    }

    @Override
    public Oficina findById(Long id) throws I18NException {

        Oficina oficina = em.find(Oficina.class, id);
        Hibernate.initialize(oficina.getOrganizativasOfi());
        return oficina;
    }

    @Override
    public Oficina findByIdCompleto(Long id) throws I18NException {

        Oficina oficina = em.find(Oficina.class, id);
        Hibernate.initialize(oficina.getOficinaResponsable());
        Hibernate.initialize(oficina.getOrganizativasOfi());
        Hibernate.initialize(oficina.getLocalidad());
        Hibernate.initialize(oficina.getIsla());
        return oficina;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> getAll() throws I18NException {

        return em.createQuery("Select oficina from Oficina as oficina order by oficina.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(oficina.id) from Oficina as oficina");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select oficina from Oficina as oficina order by oficina.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigo(String codigo) throws I18NException {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo = :codigo");

        q.setParameter("codigo", codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficina = q.getResultList();
        if (oficina.size() == 1) {
            return oficina.get(0);
        } else {
            return null;
        }

    }

    @Override
    public Oficina findByCodigoMultiEntidad(String codigo) throws I18NException {

        Query q = em.createQuery("Select oficina from Oficina as oficina where oficina.codigo = :codigo");

        q.setParameter("codigo", codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficinas = q.getResultList();

        if (oficinas.size() == 1) {
            return oficinas.get(0);
        } else if (oficinas.size() > 1) {
            for (Oficina oficina : oficinas) {
                if (oficina.getOrganismoResponsable().getCodigo().equals(oficina.getOrganismoResponsable().getEntidad().getCodigoDir3())) {
                    return oficina;
                }
            }
        }

        return null;
    }

    @Override
    public Oficina findByMultiEntidad(String codigo) throws I18NException{
        if(multiEntidadEjb.isMultiEntidad()){
            return findByCodigoMultiEntidad(codigo);
        }else{
            return findByCodigo(codigo);
        }
    }

    @Override
    public Oficina findByCodigoByEntidadMultiEntidad(String codigo, Long idEntidad) throws I18NException{
        if(multiEntidadEjb.isMultiEntidad()){
            return findByCodigoMultiEntidad(codigo);
        }else{
            return findByCodigoEntidad(codigo, idEntidad);
        }
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws I18NException {

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
    public Oficina findByCodigoLigero(String codigo) throws I18NException {

        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion from Oficina as oficina where " +
                "oficina.codigo = :codigo");

        q.setParameter("codigo", codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        if (result.size() > 0) {
            return new Oficina((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
        }

        return null;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigoEntidad(String codigo, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo =:codigo and oficina.organismoResponsable.entidad.id = :idEntidad  and " +
                "oficina.estado.codigoEstadoEntidad =:vigente");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficina = q.getResultList();
        if (oficina.size() == 1) {
            return oficina.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Oficina findByCodigoVigente(String codigo) throws I18NException {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo =:codigo and " +
                "oficina.estado.codigoEstadoEntidad =:vigente");

        q.setParameter("codigo", codigo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficina = q.getResultList();
        if (oficina.size() == 1) {
            return oficina.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByOrganismoResponsable(Long idOrganismo) throws I18NException {
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.id =:idOrganismo and " +
                "oficina.estado.codigoEstadoEntidad=:vigente");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficinas = q.getResultList();
        for (Oficina oficina : oficinas) {
            Hibernate.initialize(oficina.getOrganizativasOfi());
        }
        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> oficinasFuncionales(Long idOrganismo, Boolean oficinaVirtual) throws I18NException {

        String oficinaVirtualWhere = "";

        if (!oficinaVirtual) {
            oficinaVirtualWhere = " and :oficinaVirtual not in elements(oficina.servicios)";
        }

        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion as nombre, oficina.organismoResponsable.id, oficina.organismoResponsable.codigo, oficina.organismoResponsable.denominacion from Oficina as oficina where " +
                "oficina.organismoResponsable.id =:idOrganismo and " +
                "oficina.estado.codigoEstadoEntidad=:vigente  " +
                oficinaVirtualWhere);

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        if (!oficinaVirtual) {
            q.setParameter("oficinaVirtual", catServicioEjb.findByCodigo(RegwebConstantes.REGISTRO_VIRTUAL_NO_PRESENCIAL));
        }

        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Oficina> oficinas = new ArrayList<Oficina>();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2], (Long) object[3], (String) object[4], (String) object[5]);

            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    public List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws I18NException {
        return relacionOrganizativaOfiEjb.oficinasOrganizativas(idOrganismo, oficinaVirtual);
    }

    @Override
    public List<Oficina> oficinasSIR(Long idOrganismo) throws I18NException {
        return relacionSirOfiEjb.oficinasSIR(idOrganismo);
    }

    @Override
    public List<Oficina> oficinasSIREntidad(Long idEntidad) throws I18NException {
        return relacionSirOfiEjb.oficinasSIREntidad(idEntidad);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByEntidad(Long idEntidad) throws I18NException {
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficinas = q.getResultList();
        for (Oficina oficina : oficinas) {
            Hibernate.initialize(oficina.getOrganizativasOfi());
        }
        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByEntidadReduce(Long entidad) throws I18NException {

        Query q = em.createQuery("Select oficina.id, oficina.denominacion from Oficina as oficina where " +
           "oficina.organismoResponsable.entidad.id = :entidad");

        q.setParameter("entidad", entidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Oficina> oficinas = new ArrayList<Oficina>();
        for (Object[] object : result) {
            Oficina org = new Oficina((Long) object[0], null,(String) object[1]);
            oficinas.add(org);
        }

        return oficinas;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByEntidadByEstado(Long idEntidad, String estado) throws I18NException {

        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad=:estado");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("estado", estado);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficinas = new ArrayList<Oficina>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2]);
            oficinas.add(oficina);
        }

        return oficinas;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> findByEntidadByEstadoMultiEntidad(Long idEntidad, String estado) throws I18NException{

        List<Oficina> oficinas= findByEntidadByEstado(idEntidad,estado);
        List<Oficina> oficinasAEliminar = new ArrayList<>();

        for(Oficina oficina: oficinas){
            Oficina oficina1= findByCodigoMultiEntidad(oficina.getCodigo());
            Organismo raiz = organismoEjb.getOrganismoRaiz(oficina1.getOrganismoResponsable().getId());
            //Si la raiz es null(es parte de otra entidad) y la entidad del organismo responsable no es igual
            // a la entidad en la que estamos se debe eliminar la oficina porque significa que hay otra entidad
            // que le da servicio
            if(raiz== null  && !oficina1.getOrganismoResponsable().getEntidad().getId().equals( idEntidad)){
                oficinasAEliminar.add(oficina);
            }
        }
        oficinas.removeAll(oficinasAEliminar);
        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> responsableByEntidadEstado(Long idEntidad) throws I18NException {
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad =:estado and " +
                "oficina.oficinaResponsable.id = null order by oficina.codigo");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("estado", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> dependienteByEntidadEstado(Long idEntidad) throws I18NException {
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad =:estado and " +
                "oficina.oficinaResponsable.id != null order by oficina.codigo");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("estado", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    public LinkedHashSet<Oficina> oficinasServicio(List<Organismo> organismos, Boolean oficinaVirtual) throws I18NException {

        LinkedHashSet<Oficina> oficinas = new LinkedHashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        // Recorremos los Organismos al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
        for (Organismo organismo : organismos) {
            oficinas.addAll(oficinasFuncionales(organismo.getId(), oficinaVirtual));
            oficinas.addAll(oficinasOrganizativas(organismo.getId(), oficinaVirtual));
        }

        return oficinas;
    }

    @Override
    public LinkedHashSet<Oficina> oficinasSIR(List<Organismo> organismos) throws I18NException {

        LinkedHashSet<Oficina> oficinas = new LinkedHashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        // Recorremos los Organismos y obtenemos las Oficinas con relación SIR
        for (Organismo organismo : organismos) {
            oficinas.addAll(oficinasSIR(organismo.getId()));
        }

        return oficinas;
    }

    @Override
    public Boolean isSIRRecepcion(Long idOficina) throws I18NException {

        Query q = em.createQuery("Select oficina.id from Oficina as oficina where " +
                "oficina.id =:idOficina and oficina.estado.codigoEstadoEntidad=:vigente and " +
                ":recepcionSir in elements(oficina.servicios)");

        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("recepcionSir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_RECEPCION));
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean isSIREnvio(Long idOficina) throws I18NException {

        Query q = em.createQuery("Select oficina.id from Oficina as oficina where " +
                "oficina.id =:idOficina and oficina.estado.codigoEstadoEntidad=:vigente and " +
                ":envioSir in elements(oficina.servicios)");

        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("envioSir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_ENVIO));
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }


    @Override
    public Boolean isSIRCompleto(Long idOficina) throws I18NException {

        Query q = em.createQuery("Select oficina.id from Oficina as oficina where " +
                "oficina.id =:idOficina and oficina.estado.codigoEstadoEntidad=:vigente and " +
                "(:sir in elements(oficina.servicios) or :sirEnvio in elements(oficina.servicios) or :sirRecepcion in elements(oficina.servicios)) ");

        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("sir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR));
        q.setParameter("sirEnvio", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_ENVIO));
        q.setParameter("sirRecepcion", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_RECEPCION));
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    public LinkedHashSet<Oficina> oficinasServicioCompleto(Long idOrganismo, Boolean oficinaVirtual) throws I18NException {

        Organismo organismoSuperior = organismoEjb.getOrganismoSuperior(idOrganismo);

        LinkedHashSet<Oficina> oficinas = new LinkedHashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        oficinas.addAll(oficinasFuncionales(idOrganismo, oficinaVirtual));
        oficinas.addAll(oficinasOrganizativas(idOrganismo, oficinaVirtual));

        if (organismoSuperior != null /*&& !organismo.getEdp()*/) {
            oficinas.addAll(oficinasServicioCompleto(organismoSuperior.getId(), oficinaVirtual));
        }

        return oficinas;

    }

    @Override
    public Boolean tieneOficinasServicio(Long idOrganismo, Boolean oficinaVirtual) throws I18NException {

        LinkedHashSet<Oficina> oficinas = oficinasServicioCompleto(idOrganismo, oficinaVirtual);

        return oficinas.size() > 0;
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {


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
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, Oficina oficina, Boolean sir) throws I18NException {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select oficina from Oficina as oficina ");

        // Entidad a la que pertenece
        where.add(" oficina.organismoResponsable.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);

        // Estado de la Oficina
        where.add(" oficina.estado.id = :idCatEstadoEntidad ");
        parametros.put("idCatEstadoEntidad", oficina.getEstado().getId());

        // Código DIR3 de la Oficina
        if (StringUtils.isNotEmpty(oficina.getCodigo())) {
            where.add(DataBaseUtils.like("oficina.codigo", "codigo", parametros, oficina.getCodigo()));
        }
        // Denominación DIR3 de la Oficina
        if (StringUtils.isNotEmpty(oficina.getDenominacion())) {
            where.add(DataBaseUtils.like("oficina.denominacion", "denominacion", parametros, oficina.getDenominacion()));
        }

        // OAMR
        if(oficina.getOamr() != null){
            where.add(" oficina.oamr = :oamr ");
            parametros.put("oamr", oficina.getOamr());
        }

        //Isla
        if(oficina.getIsla().getId() != null){
            where.add(" oficina.isla.id = :idIsla ");
            parametros.put("idIsla", oficina.getIsla().getId());
        }

        // SIR
        if(sir != null){
            if(sir){
                where.add(" (:sir in elements(oficina.servicios) or :sirEnvio in elements(oficina.servicios) or :sirRecepcion in elements(oficina.servicios)) ");
            }else{
                where.add(" (:sir not in elements(oficina.servicios) and :sirEnvio not in elements(oficina.servicios) and :sirRecepcion not in elements(oficina.servicios)) ");
            }
            parametros.put("sir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR));
            parametros.put("sirRecepcion", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_RECEPCION));
            parametros.put("sirEnvio", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR_ENVIO));
        }

        // Parámetros
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
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Oficina> oficinas = q.getResultList();
        for(Oficina o:oficinas){
            Hibernate.initialize(o.getLocalidad());
            Hibernate.initialize(o.getIsla());
        }
        paginacion.setListado(oficinas);

        return paginacion;

    }

    /**
     * Obtiene el id de la Entidad a la que pertenece la Oficina
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    public Long obtenerEntidad(String codigo) throws I18NException {
        Query q = em.createQuery("Select oficina.organismoResponsable.entidad.id from Oficina as oficina where " +
                "oficina.codigo =:codigo and oficina.organismoResponsable.entidad.sir = true");

        q.setParameter("codigo", codigo);

        return (Long) q.getSingleResult();
    }


    /**
     * PROVES MULTIENTITAT
     */
    public Long obtenerEntidadMultiEntidad(String codigo) throws I18NException {

        Oficina oficina = findByCodigoMultiEntidad(codigo);

        if (oficina.getOrganismoResponsable().getEntidad().getSir()) {
            return oficina.getOrganismoResponsable().getEntidad().getId();
        } else {
            return null;
        }
    }


    /**
     * Obtiene las oficinas SIR desde dir3caib(via WS) de la unidad indicada en el código
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    public List<OficinaTF> obtenerOficinasSir(String codigo, Dir3Caib dir3caib) throws I18NException {
        Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(dir3caib.getServer(), dir3caib.getUser(), dir3caib.getPassword());
        return oficinasService.obtenerOficinasSIRUnidad(codigo);

    }
}
