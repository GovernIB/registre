package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
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

    @PersistenceContext(unitName="regweb3")
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
    @EJB public TrazabilidadLocal trazabilidadEjb;
    @EJB public PersonaLocal personaEjb;
    @EJB public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    @EJB public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    @EJB public ReproLocal reproEjb;
    @EJB public LopdLocal lopdEjb;
    @EJB public AnexoLocal anexoEjb;



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
    public void eliminarRegistros(Long idEntidad) throws Exception{

        /********* TRAZABILIDAD *********/
        log.info("Trazabilidades eliminadas: " +trazabilidadEjb.eliminarByEntidad(idEntidad));

        /********* OFICIOS REMISIÓN *********/
        log.info("OficiosRemision eliminados: " +oficioRemisionEjb.eliminarByEntidad(idEntidad));

        /********* PERSONAS *********/
        log.info("Personas eliminadas: " + personaEjb.eliminarByEntidad(idEntidad));


        /********* REGISTROS ENTRADA *********/

        // HistoricoRegistroEntrada
        log.info("HistoricosEntrada eliminados: " + historicoRegistroEntradaEjb.eliminarByEntidad(idEntidad));

        // RegistroEntrada
        log.info("RegistrosEntrada eliminados: " +registroEntradaEjb.eliminarByEntidad(idEntidad));

        /********* REGISTROS SALIDA *********/

        // HistoricoRegistroSalida
        log.info("HistoricosSalida eliminados: " + historicoRegistroSalidaEjb.eliminarByEntidad(idEntidad));

        // RegistroSalida
        log.info("RegistrosSalida eliminados: " +registroSalidaEjb.eliminarByEntidad(idEntidad));


        /********* REGISTROS DETALLE *********/
        log.info("RegistrosDetalle eliminados: " +registroDetalleEjb.eliminarByEntidad(idEntidad));

        /********* REPRO *********/
        log.info("Repros eliminados: " +reproEjb.eliminarByEntidad(idEntidad));

        /********* LOPD *********/
        log.info("Lopds eliminados: " + lopdEjb.eliminarByEntidad(idEntidad));

        /********* LIBROS: PONER CONTADORES A 0  *********/
        List<Libro> libros = libroEjb.getLibrosEntidad(idEntidad);
        for (Libro libro : libros) {
           libroEjb.reiniciarContadores(libro.getId());
        }
        log.info("Libros reiniciados: " +libros.size());

    }

    @Override
    public void eliminarEntidad(Long idEntidad)throws Exception{

        //Eliminamos todos los datos relacionados con los RegistrosEntradad y RegistrosSalida
        eliminarRegistros(idEntidad);

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
