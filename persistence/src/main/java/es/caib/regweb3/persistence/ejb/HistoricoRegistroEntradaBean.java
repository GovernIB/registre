package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "HistoricoRegistroEntradaEJB")
@SecurityDomain("seycon")
public class HistoricoRegistroEntradaBean extends BaseEjbJPA<HistoricoRegistroEntrada, Long> implements HistoricoRegistroEntradaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public HistoricoRegistroEntrada findById(Long id) throws Exception {

        return em.find(HistoricoRegistroEntrada.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroEntrada> getAll() throws Exception {

        return  em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada order by historicoRegistroEntrada.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(historicoRegistroEntrada.id) from HistoricoRegistroEntrada as historicoRegistroEntrada");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<HistoricoRegistroEntrada> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada order by historicoRegistroEntrada.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroEntrada> getByRegistroEntrada(Long idRegistro) throws Exception {

        Query q = em.createQuery("Select hre.id, hre.registroEntradaOriginal, hre.estado, hre.fecha, hre.modificacion, hre.usuario.id, hre.usuario.usuario from HistoricoRegistroEntrada as hre where hre.registroEntrada.id =:idRegistro order by hre.fecha desc");
      q.setParameter("idRegistro", idRegistro);


        List<HistoricoRegistroEntrada> hres = new ArrayList<HistoricoRegistroEntrada>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroEntrada hre = new HistoricoRegistroEntrada((Long) object[0], (String) object[1], (Long) object[2], (Date) object[3], (String) object[4], (Long) object[5], (Usuario) object[6]);

            hres.add(hre);
        }

        return hres;
    }

    @Override
    public Paginacion entradaModificadaPorUsuario(Integer pageNumber, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception{

        Query q;
        Query q2;

        String accio = I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" );

        q = em.createQuery("Select historicoRegistroEntrada.registroEntrada.numeroRegistro, historicoRegistroEntrada.registroEntrada.libro.nombre, " +
                "historicoRegistroEntrada.registroEntrada.oficina.denominacion, historicoRegistroEntrada.registroEntrada.libro.organismo.denominacion, " +
                "historicoRegistroEntrada.registroEntrada.fecha, historicoRegistroEntrada.fecha, historicoRegistroEntrada.modificacion " +
                "from HistoricoRegistroEntrada as historicoRegistroEntrada where historicoRegistroEntrada.fecha >= :fechaInicio " +
                "and historicoRegistroEntrada.fecha <= :fechaFin and historicoRegistroEntrada.usuario.id = :idUsuario " +
                "and historicoRegistroEntrada.modificacion != :accio " +
                "and historicoRegistroEntrada.registroEntrada.libro in (:libros) order by historicoRegistroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);
        q.setParameter("accio", accio);

        List<HistoricoRegistroEntrada> historicosRegistroEntrada = new ArrayList<HistoricoRegistroEntrada>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(historicoRegistroEntrada.id) from HistoricoRegistroEntrada as historicoRegistroEntrada " +
                "where historicoRegistroEntrada.fecha >= :fechaInicio and historicoRegistroEntrada.fecha <= :fechaFin and " +
                "historicoRegistroEntrada.usuario.id = :idUsuario and historicoRegistroEntrada.modificacion != :accio " +
                "and historicoRegistroEntrada.registroEntrada.libro in (:libros)");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("libros", libros);
        q2.setParameter("accio", accio);

        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(BaseEjbJPA.RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroEntrada historicoRegistroEntrada = new HistoricoRegistroEntrada(null, (Integer) object[0], (String) object[1], (String) object[2], (String) object[3], (Date) object[4], (Date) object[5], (String) object[6]);

            historicosRegistroEntrada.add(historicoRegistroEntrada);
        }

        paginacion.setListado(historicosRegistroEntrada);

        return paginacion;
    }

    @Override
    public Paginacion entradaModificadaPorUsuarioLibro(Integer pageNumber, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception{

        Query q;
        Query q2;

        String accio = I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" );

        q = em.createQuery("Select historicoRegistroEntrada.registroEntrada.numeroRegistro, historicoRegistroEntrada.registroEntrada.libro.nombre, " +
                "historicoRegistroEntrada.registroEntrada.oficina.denominacion, historicoRegistroEntrada.registroEntrada.libro.organismo.denominacion, " +
                "historicoRegistroEntrada.registroEntrada.fecha, historicoRegistroEntrada.fecha, historicoRegistroEntrada.modificacion " +
                "from HistoricoRegistroEntrada as historicoRegistroEntrada where historicoRegistroEntrada.fecha >= :fechaInicio " +
                "and historicoRegistroEntrada.fecha <= :fechaFin and historicoRegistroEntrada.usuario.id = :idUsuario and " +
                "historicoRegistroEntrada.registroEntrada.libro.id = :idLibro and historicoRegistroEntrada.modificacion != :accio " +
                "order by historicoRegistroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("accio", accio);

        List<HistoricoRegistroEntrada> historicosRegistroEntrada = new ArrayList<HistoricoRegistroEntrada>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(historicoRegistroEntrada.id) from HistoricoRegistroEntrada as historicoRegistroEntrada " +
                "where historicoRegistroEntrada.fecha >= :fechaInicio and historicoRegistroEntrada.fecha <= :fechaFin and " +
                "historicoRegistroEntrada.usuario.id = :idUsuario and historicoRegistroEntrada.registroEntrada.libro.id = :idLibro and " +
                "historicoRegistroEntrada.modificacion != :accio");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("accio", accio);

        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(BaseEjbJPA.RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroEntrada historicoRegistroEntrada = new HistoricoRegistroEntrada(null, (Integer) object[0], (String) object[1], (String) object[2], (String) object[3], (Date) object[4], (Date) object[5], (String) object[6]);

            historicosRegistroEntrada.add(historicoRegistroEntrada);
        }

        paginacion.setListado(historicosRegistroEntrada);

        return paginacion;
    }

    @Override
    public HistoricoRegistroEntrada crearHistoricoRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws Exception{

        
        HistoricoRegistroEntrada historico = new HistoricoRegistroEntrada();

        historico.setEstado(registroEntrada.getEstado());
        historico.setRegistroEntrada(registroEntrada);
        historico.setFecha(new Date());
        historico.setModificacion(modificacion);
        historico.setUsuario(usuarioEntidad);
        //Serializamos el RegistroEntrada original
        if(serializar){
            String registroEntradaOrigial = RegistroUtils.serilizarXml(registroEntrada);
            historico.setRegistroEntradaOriginal(registroEntradaOrigial);
        }

        // Guardamos el histórico
        return persist(historico);
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(hre.id) from HistoricoRegistroEntrada as hre where hre.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> hre = em.createQuery("Select distinct(hre.id) from HistoricoRegistroEntrada as hre where hre.registroEntrada.usuario.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = hre.size();

        if(hre.size() >0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (hre.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = hre.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from HistoricoRegistroEntrada where id in (:hre) ").setParameter("hre", subList).executeUpdate();
                hre.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from HistoricoRegistroEntrada where id in (:hre) ").setParameter("hre", hre).executeUpdate();
        }

        return total;

    }

}
