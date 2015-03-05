package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.*;
import es.caib.regweb.model.utils.RegistroBasico;
import es.caib.regweb.persistence.utils.DataBaseUtils;
import es.caib.regweb.persistence.utils.NumeroRegistro;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;
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
 * Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaEJB")
@SecurityDomain("seycon")
public class RegistroSalidaBean extends BaseEjbJPA<RegistroSalida, Long> implements RegistroSalidaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;

    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb/ContadorEJB/local")
    public ContadorLocal contadorEjb;


    @Override
    public RegistroSalida findById(Long id) throws Exception {

        return em.find(RegistroSalida.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getAll() throws Exception {

        return  em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<RegistroSalida> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<RegistroSalida> getByUsuario(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }

    
    public synchronized RegistroSalida registrarSalida(RegistroSalida registroSalida)throws Exception{

        // Obtenemos el Número de registro
        Libro libro = libroEjb.findById(registroSalida.getLibro().getId());
        NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorSalida().getId());
        registroSalida.setNumeroRegistro(numeroRegistro.getNumero());
        registroSalida.setFecha(numeroRegistro.getFecha());
        if(registroSalida.getLibro().getCodigo() != null && registroSalida.getOficina().getCodigo() != null){
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, registroSalida.getLibro(), registroSalida.getOficina()));
        } else {
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, libroEjb.findById(registroSalida.getLibro().getId()), oficinaEjb.findById(registroSalida.getOficina().getId())));
        }

        // Si no ha introducido ninguna fecha de Origen
        if(registroSalida.getRegistroDetalle().getFechaOrigen() == null){
            registroSalida.getRegistroDetalle().setFechaOrigen(registroSalida.getFecha());
        }
        
        List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
        if (interesados != null && interesados.size() != 0) {
          for (Interesado interesado : interesados) {
            interesado.setRegistroDetalle(registroSalida.getRegistroDetalle());
          }
        }
        
        
        List<Anexo> anexos  = registroSalida.getRegistroDetalle().getAnexos();
        if (anexos != null && anexos.size() != 0) {
          for (Anexo anexo : anexos) {
            anexo.setRegistroDetalle(registroSalida.getRegistroDetalle());
          }
        }

        // Guardamos el RegistroSalida
        registroSalida = persist(registroSalida);

        //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
        if(StringUtils.isEmpty(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen())){

            registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());

            registroSalida = merge(registroSalida);
        }

        return registroSalida;

    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, List<Libro> libros, Boolean anexos) throws Exception{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroSalida from RegistroSalida as registroSalida ");


        if(registroSalida.getNumeroRegistroFormateado()!= null && registroSalida.getNumeroRegistroFormateado().length() > 0){where.add(" registroSalida.numeroRegistroFormateado = :numeroRegistroFormateado"); parametros.put("numeroRegistroFormateado",registroSalida.getNumeroRegistroFormateado());}

        if(registroSalida.getRegistroDetalle().getExtracto() != null && registroSalida.getRegistroDetalle().getExtracto().length() > 0){where.add(DataBaseUtils.like("registroSalida.registroDetalle.extracto","extracto",parametros,registroSalida.getRegistroDetalle().getExtracto()));}

        if(registroSalida.getEstado() != null && registroSalida.getEstado() > 0) {
           where.add(" registroSalida.estado = :idEstadoRegistro");
           parametros.put("idEstadoRegistro",registroSalida.getEstado());
         }

        where.add(" (registroSalida.fecha >= :fechaInicio  ");parametros.put("fechaInicio", fechaInicio);
        where.add(" registroSalida.fecha <= :fechaFin) ");parametros.put("fechaFin", fechaFin);


        // Comprobamos si la búsqueda es sobre un libro en concreto o sobre todos a los que tiene acceso el usuario.
        if(registroSalida.getLibro().getId() != null && registroSalida.getLibro().getId() > 0){
            where.add(" registroSalida.libro.id = :idLibro"); parametros.put("idLibro",registroSalida.getLibro().getId());
        }else{
            where.add(" registroSalida.libro in (:libros)"); parametros.put("libros",libros);
        }

        // Buscamos registros de sañida con anexos
        if(anexos){
            where.add(" registroSalida.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
        }

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
            q2 = em.createQuery(query.toString().replaceAll("Select registroSalida from RegistroSalida as registroSalida ", "Select count(registroSalida.id) from RegistroSalida as registroSalida "));
            query.append(" order by registroSalida.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select registroSalida from RegistroSalida as registroSalida ", "Select count(registroSalida.id) from RegistroSalida as registroSalida "));
            query.append("order by registroSalida.id desc");
            q = em.createQuery(query.toString());
        }


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


    @Override
    public List<RegistroSalida> buscaLibroRegistro(Date fechaInicio, Date fechaFin, List<Libro> libros) throws Exception{

        Query q;

        q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where (registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin) and registroSalida.libro in (:libros) order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<RegistroSalida> buscaIndicadores(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception{

        Query q;

        q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.estado != :anulado and registroSalida.estado != :pendiente and " +
                "registroSalida.libro.organismo.entidad.id = :idEntidad order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return q.getResultList();
    }

    @Override
    public Long buscaSalidaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.oficina.organismoResponsable.id = :conselleria and registroSalida.estado != 8 and registroSalida.estado != 2");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("conselleria", conselleria);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.tipoAsunto.id = :tipoAsunto and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :pendiente and registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("tipoAsunto", tipoAsunto);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.idioma = :idioma and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :pendiente and registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.libro.id = :libro and registroSalida.estado != 8 and registroSalida.estado != 2");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libro", libro);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.oficina.id = :oficina and registroSalida.estado != :anulado and registroSalida.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("oficina", oficina);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<RegistroSalida> buscaSalidaPorUsuario(Date fechaInicio, Date fechaFin, Long usuario, List<Libro> libros) throws Exception{

        Query q;

        q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.usuario.id = :usuario and registroSalida.libro in (:libros) order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("usuario", usuario);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<RegistroSalida> buscaSalidaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception{

        Query q;

        q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.usuario.id = :idUsuario and registroSalida.libro.id = :idLibro and registroSalida.estado != :pendiente order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return q.getResultList();
    }
    
    
    
    @Override
    public RegistroSalida findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception {

        Query q = em.createQuery("Select registroSalida "
            + " from RegistroSalida as registroSalida"
            + " where registroSalida.numeroRegistro = :numero "
              + " AND  YEAR(registroSalida.fecha) = :anyo "
              + " AND  registroSalida.libro.codigo = :libro ");

        q.setParameter("numero", numero);
        q.setParameter("anyo", anyo);
        q.setParameter("libro", libro);
        
        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }
    
    

    @Override
    public List<RegistroSalida> buscaPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception{

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroSalida from RegistroSalida as registroSalida ");

        if(fechaInicio != null){where.add(" registroSalida.fecha >= :fechaInicio"); parametros.put("fechaInicio",fechaInicio);}
        if(fechaFin != null){where.add(" registroSalida.fecha <= :fechaFin"); parametros.put("fechaFin",fechaFin);}
        if(idLibro != null && idLibro > 0){where.add(" registroSalida.libro.id = :idLibro"); parametros.put("idLibro",idLibro);}
        if(numeroRegistro != null && numeroRegistro > 0){where.add(" registroSalida.numeroRegistro = :numeroRegistro"); parametros.put("numeroRegistro",numeroRegistro);}

        query.append("where ");
        int count = 0;
        for (String w : where) {
            if (count != 0) {
                query.append(" and ");
            }
            query.append(w);
            count++;
        }
        query.append(" order by registroSalida.id desc");
        q = em.createQuery(query.toString());

        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
        }

        return q.getResultList();
    }

    @Override
    public void cambiarEstado(Long idRegistro, Long idEstado) throws Exception{
        RegistroSalida registroSalida = findById(idRegistro);
        registroSalida.setEstado(idEstado);
        merge(registroSalida);
    }

    public List<RegistroBasico> getUltimosRegistros(Long idOficina, Integer total) throws Exception{

        Query q;

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, re.registroDetalle.extracto " +
                "from RegistroSalida as re where re.oficina.id = :idOficina " +
                "and re.estado = :idEstadoRegistro " +
                "order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficina", idOficina);
        q.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);

        return  getRegistroBasicoList(q.getResultList());
    }

    @Override
    public RegistroSalida findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.numeroRegistroFormateado = :numeroRegistroFormateado ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);

        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception{

        RegistroSalida old = registroSalida;

        // Estado anulado
        registroSalida.setEstado(RegwebConstantes.ESTADO_ANULADO);

        // Actualizamos el RegistroSalida
        registroSalida = merge(registroSalida);

        // Creamos el HistoricoRegistroSalida para la modificación d estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(old,usuarioEntidad,RegwebConstantes.TIPO_MODIF_ESTADO,false);


    }

    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb.model.utils.RegistroBasico}
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoList(List<Object[]> result) throws Exception{

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        for (Object[] object : result){
            RegistroBasico registroBasico = new RegistroBasico((Long)object[0],(String)object[1],(Date)object[2],(String)object[3],(String)object[4],(String)object[5]);

            registros.add(registroBasico);
        }

        return  registros;
    }

}
