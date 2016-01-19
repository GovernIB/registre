package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */

@Stateless(name = "PreRegistroEJB")
@SecurityDomain("seycon")
public class PreRegistroBean extends BaseEjbJPA<PreRegistro, Long> implements PreRegistroLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;



    @Override
    public PreRegistro findById(Long id) throws Exception {

        return em.find(PreRegistro.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PreRegistro> getAll() throws Exception {

        return em.createQuery("Select preRegistro from PreRegistro as preRegistro order by preRegistro.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(preRegistro.id) from PreRegistro as preRegistro");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<PreRegistro> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select preRegistro from PreRegistro as preRegistro order by preRegistro.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public PreRegistro preRegistrar(PreRegistro preRegistro) throws Exception {

        // Obtenemos el último Número de preRegistro
        Query q = em.createQuery("Select max(preRegistro.contador) from PreRegistro as preRegistro");
        Long contador = (Long) q.getSingleResult();
        if(contador == null){
            contador = (long) 0;
        }

        // Se asigna el nuevo número de preRegistro
        contador = contador + 1;
        String numeroNuevo = contador.toString();
        String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code = "";
        for (int i = 0; i < 5; i++) {
            code+=(mayusculas.charAt((int)(Math.random() * mayusculas.length())));
        }
        preRegistro.setContador(contador);
        preRegistro.setNumeroPreregistro(code + numeroNuevo);
        preRegistro.setFecha(Calendar.getInstance().getTime());

        if (preRegistro.getRegistroDetalle() != null) {
          List<Interesado> interesados = preRegistro.getRegistroDetalle().getInteresados();
          if (interesados != null && interesados.size() != 0) {
            for (Interesado interesado : interesados) {
              interesado.setRegistroDetalle(preRegistro.getRegistroDetalle());
            }
          }
          
          
          List<Anexo> anexos  = preRegistro.getRegistroDetalle().getAnexos();
          if (anexos != null && anexos.size() != 0) {
            for (Anexo anexo : anexos) {
              anexo.setRegistroDetalle(preRegistro.getRegistroDetalle());
            }
          }
        }
        
        // Guardamos el PreRegistro
        preRegistro = persist(preRegistro);

        //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
//        if (StringUtils.isEmpty(preRegistro.getRegistroDetalle().getNumeroRegistroOrigen())) {
//            preRegistro.getRegistroDetalle().setNumeroRegistroOrigen(preRegistro.getNumeroPreregistro());
//            preRegistro = merge(preRegistro);
//        }

        return preRegistro;

    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Integer any, PreRegistro preRegistro, String codigoOficinaActiva, Long estado) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select preRegistro from PreRegistro as preRegistro ");

//        if(codigoOficinaActiva!= null){where.add(" preRegistro.codigoUnidadTramitacionDestino = :codigoOficinaActiva "); parametros.put("codigoOficinaActiva",codigoOficinaActiva);}

        if (codigoOficinaActiva != null && codigoOficinaActiva.length() > 0) {
            where.add(DataBaseUtils.like("preRegistro.codigoEntidadRegistralDestino", "codigoOficinaActiva", parametros, codigoOficinaActiva));
        }

        if (preRegistro.getRegistroDetalle().getExtracto() != null && preRegistro.getRegistroDetalle().getExtracto().length() > 0) {
            where.add(DataBaseUtils.like("preRegistro.registroDetalle.extracto", "extracto", parametros, preRegistro.getRegistroDetalle().getExtracto()));
        }

        if (preRegistro.getNumeroPreregistro() != null && preRegistro.getNumeroPreregistro().length() > 0) {
            where.add(DataBaseUtils.like("preRegistro.numeroPreregistro", "numeroPreregistro", parametros, preRegistro.getNumeroPreregistro()));
        }

        if (estado != null) {
            where.add(" preregistro.estado = :estado "); parametros.put("estado",estado);
        }

        if(any!= null){where.add(" year(preRegistro.fecha) = :any "); parametros.put("any",any);}

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
            q2 = em.createQuery(query.toString().replaceAll("Select preRegistro from PreRegistro as preRegistro ", "Select count(preRegistro.id) from PreRegistro as preRegistro "));
            query.append(" order by preRegistro.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select preRegistro from PreRegistro as preRegistro ", "Select count(preRegistro.id) from PreRegistro as preRegistro "));
            query.append("order by preRegistro.id desc");
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


    @Override
    public List<PreRegistro> preRegistrosPendientesProcesar(String codigoOficinaActiva) throws Exception {

        Query q = em.createQuery("Select preRegistro from PreRegistro as preRegistro " +
                "where preRegistro.codigoEntidadRegistralDestino = :codigoOficinaActiva and preRegistro.estado = :idEstadoPreRegistro " +
                "order by preRegistro.id desc");


        q.setParameter("codigoOficinaActiva",codigoOficinaActiva);
        q.setParameter("idEstadoPreRegistro", RegwebConstantes.ESTADO_PREREGISTRO_PENDIENTE_PROCESAR);

        return q.getResultList();
    }


    public List<PreRegistro> getUltimosPreRegistrosPendientesProcesar(String codigoOficinaActiva, Integer total) throws Exception{

        Query q = em.createQuery("Select preRegistro from PreRegistro as preRegistro " +
                "where preRegistro.codigoEntidadRegistralDestino = :codigoOficinaActiva and preRegistro.estado = :idEstadoPreRegistro " +
                "order by preRegistro.fecha desc");

        q.setMaxResults(total);
        q.setParameter("codigoOficinaActiva", codigoOficinaActiva);
        q.setParameter("idEstadoPreRegistro", RegwebConstantes.ESTADO_PREREGISTRO_PENDIENTE_PROCESAR);

        return  q.getResultList();
    }


    @Override
    public Boolean tienePreRegistros(String codigoOficinaActiva) throws Exception {

        Query q = em.createQuery("Select count(preRegistro.id) from PreRegistro as preRegistro where " +
                "preRegistro.codigoEntidadRegistralDestino = :codigoOficinaActiva");

        q.setParameter("codigoOficinaActiva",codigoOficinaActiva);

        return (Long) q.getSingleResult() > 0;
    }

}