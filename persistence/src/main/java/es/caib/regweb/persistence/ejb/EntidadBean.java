package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.*;
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
 * Date: 16/01/14
 */

@Stateless(name = "EntidadEJB")
@SecurityDomain("seycon")
public class EntidadBean extends BaseEjbJPA<Entidad, Long> implements EntidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;

    @EJB public RegistroDetalleLocal registroDetalleEjb;

    @EJB public LibroLocal libroEjb;

    @EJB public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB public OficioRemisionLocal oficioRemisionEjb;

    @EJB public TipoDocumentalLocal tipoDocumentalEjb;

    @EJB public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB public RegistroEntradaLocal registroEntradaEjb;
    @EJB public RegistroSalidaLocal registroSalidaEjb;
    @EJB public OrganismoLocal organismoEjb;



    @Override
    public Entidad findById(Long id) throws Exception {

        return em.find(Entidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getAll() throws Exception {

        return  em.createQuery("Select entidad from Entidad as entidad order by entidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(entidad.id) from Entidad as entidad");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Entidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad order by entidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Entidad findByCodigoDir3(String codigo) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where entidad.codigoDir3 = :codigo ");

        q.setParameter("codigo",codigo);

        List<Entidad> entidad =  q.getResultList();

        if(entidad.size() == 1){
            return entidad.get(0);
        }else{
            return  null;
        }

    }

    @Override
    public Boolean tieneOrganismos(Long idEntidad) throws Exception{
        Query q = em.createQuery("Select organismo.id from Organismo as organismo where organismo.entidad.id = :idEntidad");

        q.setParameter("idEntidad",idEntidad);

        List<Long> organismos = q.getResultList();

        return organismos.size() > 0;
    }

    @Override
    public List<Entidad> getEntidadesAdministrador(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select entidad from Entidad as entidad, UsuarioEntidad as usuarioEntidad where usuarioEntidad in elements(entidad.administradores) " +
                "and usuarioEntidad.id = :idUsuarioEntidad and entidad.activo = true");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Entidad> getEntidadesPropietario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where entidad.propietario.id = :idUsuario " +
                "and entidad.activo = true");

        q.setParameter("idUsuario",idUsuario);

        return q.getResultList();
    }

    @Override
    public Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where " +
                "entidad.id != :idEntidad and entidad.codigoDir3 = :codigo");

        q.setParameter("codigo",codigo);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean esAutorizado(Long idEntidad, Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad, UsuarioEntidad as usuarioEntidad where (entidad.propietario.id = :idUsuario or usuarioEntidad in elements(entidad.administradores) ) and entidad.id=:idEntidad");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;

    }

    @Override
    public void eliminarEntidad(Long idEntidad)throws Exception{

        /********* TRAZABILIDAD *********/
        Query t = em.createQuery("Select id from Trazabilidad where oficioRemision.usuarioResponsable.entidad.id=:idEntidad");
        t.setParameter("idEntidad",idEntidad);
        List<Object> trazabilidades =  t.getResultList();

        for (Object id : trazabilidades) {
            Query trazabilidad = em.createQuery("delete from Trazabilidad where id=:id");
            log.info("trazabilidad: " + trazabilidad.setParameter("id", id).executeUpdate());
        }
        em.flush();

        /********* OFICIOS REMISIÓN *********/
        Query or = em.createQuery("select distinct(id) from OficioRemision where usuarioResponsable.entidad.id=:idEntidad");
        or.setParameter("idEntidad", idEntidad);
        List<Object> oficiosRemision =  or.getResultList();

        for (Object id : oficiosRemision) {
            oficioRemisionEjb.remove(oficioRemisionEjb.findById((Long) id));
            Query oficioRemision = em.createQuery("delete from OficioRemision  where id=:id");
            log.info("oficiosRemision: " + oficioRemision.setParameter("id", id).executeUpdate());
        }
        em.flush();

        /********* PERSONAS *********/
        Query persona = em.createQuery("delete from Persona where entidad.id=:idEntidad");
        log.info("personas: " + persona.setParameter("idEntidad", idEntidad).executeUpdate());
        em.flush();

        /********* REGISTROS ENTRADA *********/

        // HistoricoRegistroEntrada
        Query hre = em.createQuery("Select distinct(hre.id) from HistoricoRegistroEntrada as hre where hre.registroEntrada.usuario.entidad.id =:idEntidad");
        hre.setParameter("idEntidad",idEntidad);
        List<Object> historicosEntrada =  hre.getResultList();

        for (Object id : historicosEntrada) {
            log.info("historicosEntrada: "+ em.createQuery("delete from HistoricoRegistroEntrada where id =:id").setParameter("id",id).executeUpdate());
        }
        em.flush();

        // RegistroEntrada
        Query re = em.createQuery("Select distinct(re.id) from RegistroEntrada as re where re.usuario.entidad.id =:idEntidad");
        re.setParameter("idEntidad",idEntidad);
        List<Object> registrosEntrada =  re.getResultList();

        for (Object id : registrosEntrada) {
            registroEntradaEjb.remove(registroEntradaEjb.findById((Long) id));
            //em.createQuery("delete from RegistroEntrada where id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        // RegistroDetalle
       /* Query rde = em.createQuery("Select distinct(re.registroDetalle.id) from RegistroEntrada as re where re.usuario.entidad.id=:idEntidad");
        rde.setParameter("idEntidad",idEntidad);
        List<Object> registrosDetalleEntrada =  rde.getResultList();

        for (Object id : registrosDetalleEntrada) {
            log.info("RegistroDetalle id: " + id);
            log.info("interesados: "+ em.createQuery("delete from Interesado where registroDetalle.id =:id").setParameter("id",id).executeUpdate());
            log.info("anexos: "+ em.createQuery("delete from Anexo where registroDetalle.id =:id").setParameter("id",id).executeUpdate());
            log.info("registroDetalle: "+ em.createQuery("delete from RegistroDetalle where id =:id").setParameter("id",id).executeUpdate());
            //registroDetalleEjb.remove(registroDetalleEjb.findById((Long) id));
        }
        em.flush();*/

        /********* REGISTROS SALIDA *********/

        // HistoricoRegistroSalida
        Query hrs = em.createQuery("Select distinct(hrs.id) from HistoricoRegistroSalida as hrs where hrs.registroSalida.usuario.entidad.id =:idEntidad");
        hrs.setParameter("idEntidad",idEntidad);
        List<Object> historicosSalida =  hrs.getResultList();

        for (Object id : historicosSalida) {
            em.createQuery("delete from HistoricoRegistroSalida where id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        // RegistroSalida
        Query rs = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where rs.usuario.entidad.id =:idEntidad");
        rs.setParameter("idEntidad",idEntidad);
        List<Object> registrosSalida =  rs.getResultList();

        for (Object id : registrosSalida) {
            registroSalidaEjb.remove(registroSalidaEjb.findById((Long) id));
            //em.createQuery("delete from RegistroSalida where id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        /********* REGISTROS DETALLE *********/

        // RegistroDetalle
        Query rd = em.createQuery("Select distinct(re.id) from RegistroDetalle as re where re.tipoAsunto.entidad.id =:idEntidad");
        rd.setParameter("idEntidad",idEntidad);
        List<Object> registrosDetalle =  rd.getResultList();

        for (Object id : registrosDetalle) {
            registroDetalleEjb.remove(registroDetalleEjb.findById((Long) id));
        }
        em.flush();



        /********* REPRO *********/
        Query repro = em.createQuery("select distinct(r.id) from Repro as r where r.usuario.entidad.id =:idEntidad");
        repro.setParameter("idEntidad", idEntidad);
        List<Object> repros =  repro.getResultList();

        for (Object id : repros) {
            em.createQuery("delete from Repro where id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        /********* LOPD *********/
        Query lopd = em.createQuery("select distinct(l.id) from Lopd as l where l.usuario.entidad.id =:idEntidad");
        lopd.setParameter("idEntidad", idEntidad);
        List<Object> lopds =  lopd.getResultList();

        for (Object id : lopds) {
            em.createQuery("delete from Lopd where id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        /********* PERMISO LIBRO USUARIO *********/
        Query plu = em.createQuery("select distinct(plu.id) from PermisoLibroUsuario as plu where plu.usuario.entidad.id =:idEntidad");
        plu.setParameter("idEntidad", idEntidad);
        List<Object> plus =  plu.getResultList();

        for (Object id : plus) {
            em.createQuery("delete from PermisoLibroUsuario where id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        /********* LIBROS *********/
       /* Query libro = em.createQuery("Select distinct(o.id) from Libro as o where o.organismo.entidad.id =:idEntidad");
        libro.setParameter("idEntidad",idEntidad);
        List<Object> libros =  libro.getResultList();

        for (Object id : libros) {
            log.info("Libro id: " + id);
            Libro l = libroEjb.findById((Long) id);
            log.info("Libro: " + l.getNombre());
            libroEjb.remove(l);
        }
        em.flush();*/

        Query o = em.createQuery("Select distinct(o.id) from Organismo as o where o.entidad.id =:idEntidad");
        o.setParameter("idEntidad",idEntidad);
        List<Object> organismos =  o.getResultList();


        /********* RelacionOrganizativaOfi *********/
        for (Object id : organismos) {
            em.createQuery("delete from RelacionOrganizativaOfi where organismo.id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        /********* RelacionSirOfi *********/
        for (Object id : organismos) {
            em.createQuery("delete from RelacionSirOfi where organismo.id =:id").setParameter("id",id).executeUpdate();
        }
        em.flush();

        /********* USUARIOS ENTIDAD *********/
        Query usuarioEntidad = em.createQuery("delete from UsuarioEntidad as r where entidad.id =:idEntidad");
        usuarioEntidad.setParameter("idEntidad",idEntidad).executeUpdate();

        /********* OFICINAS *********/
        for (Object id : organismos) {
            em.createQuery("delete from Oficina where organismoResponsable.id =:id and oficinaResponsable != null").setParameter("id",id).executeUpdate();
        }
        em.flush();

        for (Object id : organismos) {
            em.createQuery("delete from Oficina where organismoResponsable.id =:id and oficinaResponsable = null").setParameter("id",id).executeUpdate();
        }
        em.flush();

        /********* ORGANISMOS *********/
        for (Object id : organismos) {
            log.info("Organismo id: " + id);
            Organismo organismo = organismoEjb.findById((Long) id);
            organismoEjb.remove(organismo);

            //em.createQuery("delete from Organismo where id =:id").setParameter("id",id).executeUpdate();
            em.flush();
        }


        /********* TIPOS ASUNTO Y CODIGOS ASUNTO *********/
        List<TipoAsunto> tiposAsunto = tipoAsuntoEjb.getAll(idEntidad);

        for (TipoAsunto tipoAsunto : tiposAsunto) {
            for (CodigoAsunto codigoAsunto : tipoAsunto.getCodigosAsunto()) {
                codigoAsuntoEjb.remove(codigoAsunto);
            }
            tipoAsuntoEjb.remove(tipoAsunto);
        }
        em.flush();

        /********* TIPOS DOCUMENTAL *********/
        List<TipoDocumental> tiposDocumental = tipoDocumentalEjb.getByEntidad(idEntidad);

        for (TipoDocumental tipoDocumental : tiposDocumental) {

            tipoDocumentalEjb.remove(tipoDocumental);
        }
        em.flush();

        /********* ENTIDAD *********/
        remove(findById(idEntidad));


    }

}
