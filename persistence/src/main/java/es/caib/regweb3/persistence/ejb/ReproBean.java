package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.Repro;
import es.caib.regweb3.model.utils.ReproJson;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 16/07/14
 */

@Stateless(name = "ReproEJB")
@SecurityDomain("seycon")
public class ReproBean extends BaseEjbJPA<Repro, Long> implements ReproLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private OrganismoLocal organismoEjb;
    @EJB public OficinaLocal oficinaEjb;


    @Override
    public Repro getReference(Long id) throws Exception {

        return em.getReference(Repro.class, id);
    }

    @Override
    public Repro findById(Long id) throws Exception {

        return em.find(Repro.class, id);
    }

    @Override
    public Repro findByOrden(Long idUsuario, int orden) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro where " +
                "repro.usuario.id = :idUsuario and repro.orden = :orden");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("orden",orden);

        return (Repro) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Repro> getAll() throws Exception {

        return  em.createQuery("Select repro from Repro as repro order by repro.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(repro.id) from Repro as repro");

        return (Long) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Repro> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro order by repro.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Repro> getPaginationUsuario(int inicio, Long idUsuario) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro  " +
                "where repro.usuario.id = :idUsuario order by repro.orden");

        q.setParameter("idUsuario",idUsuario);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Repro> getAllbyUsuario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro  " +
                "where repro.usuario.id = :idUsuario order by repro.orden");

        q.setParameter("idUsuario", idUsuario);

        return  q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Repro> getActivasbyUsuario(Long idUsuario, Long tipoRegistro) throws Exception {

        Query q = em.createQuery("Select repro.id, repro.nombre from Repro as repro  " +
                "where repro.usuario.id = :idUsuario and repro.tipoRegistro = :tipoRegistro and repro.activo = true order by repro.orden");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("tipoRegistro",tipoRegistro);

        List<Repro> repros = new ArrayList<Repro>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Repro repro = new Repro((Long) object[0], (String) object[1]);

            repros.add(repro);
        }

        return repros;
    }

    @Override
    public Long getTotalbyUsuario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select count(repro.id) from Repro as repro " +
                "where repro.usuario.id = :idUsuario");

        q.setParameter("idUsuario",idUsuario);

        return (Long) q.getSingleResult();
    }

    @Override
    public Integer maxOrdenRepro(Long idUsuario) throws Exception{

        Query q = em.createQuery("Select max(repro.orden) from Repro as repro  " +
                "where repro.usuario.id = :idUsuario");

        q.setParameter("idUsuario",idUsuario);

        return (Integer) q.getSingleResult();
    }

    @Override
    public Long obtenerUsuarioRepro(Long idRepro) throws Exception{

        Query q = em.createQuery("Select repro.usuario.id from Repro as repro  " +
                "where repro.id = :idRepro");

        q.setParameter("idRepro",idRepro);

        return (Long) q.getSingleResult();
    }


    @Override
    public void modificarOrden(Long idRepro, int orden) throws Exception{

        Query q = em.createQuery("Update Repro set orden = :orden where " +
                "id = :idRepro");

        q.setParameter("idRepro",idRepro);
        q.setParameter("orden",orden);
        q.executeUpdate();
    }


    @Override
    public Boolean subirOrden(Long idRepro) throws Exception{

        boolean result;

        try{
            Long idUsuario = obtenerUsuarioRepro(idRepro);

            Repro repro = findById(idRepro);

            int ordenActual = repro.getOrden();

            int ordenNuevo = 1;
            if(ordenActual > 1){
                ordenNuevo = ordenActual - 1;
            }

            Repro reproAnterior = findByOrden(idUsuario, ordenNuevo);

            modificarOrden(idRepro, ordenNuevo);

            modificarOrden(reproAnterior.getId(), ordenActual);
            result = true;

        } catch(Exception e){
            e.printStackTrace();
            result = false;
        }

        return result;

    }

    @Override
    public Boolean bajarOrden(Long idRepro) throws Exception{

        boolean result;

        try{
            Long idUsuario = obtenerUsuarioRepro(idRepro);
            List<Repro> repros = getAllbyUsuario(idUsuario);

            Repro repro = findById(idRepro);

            int ordenActual = repro.getOrden();

            int ordenNuevo = repros.size();
            if(ordenActual < repros.size()){
                ordenNuevo = ordenActual + 1;
            }

            Repro reproPosterior = findByOrden(idUsuario, ordenNuevo);

            modificarOrden(idRepro, ordenNuevo);

            modificarOrden(reproPosterior.getId(), ordenActual);
            result = true;

        } catch(Exception e){
            e.printStackTrace();
            result = false;
        }

        return result;

    }

    @Override
    public Boolean cambiarEstado(Long idRepro) throws Exception{

        boolean result;

        try{
            Repro repro = findById(idRepro);

            if(repro.getActivo()){
                repro.setActivo(false);
            }else{
                repro.setActivo(true);
            }

            merge(repro);
            result = true;

        } catch(Exception e){
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> repros =  em.createQuery("select distinct(r.id) from Repro as r where r.usuario.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = repros.size();

        if(repros.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (repros.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = repros.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from Repro where id in (:repros)").setParameter("repros", subList).executeUpdate();
                repros.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from Repro where id in (:repros)").setParameter("repros", repros).executeUpdate();
        }
        return total;

    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(repro.id) from Repro as repro where repro.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return (Long) q.getSingleResult() > 0;
    }

    public ReproJson obtenerRepro(Long idRepro, Entidad entidad) throws Exception{

        Repro repro = findById(idRepro);
        ReproJson reproJson = RegistroUtils.desSerilizarReproXml(repro.getRepro());

        switch (repro.getTipoRegistro().intValue()){

            case 1: //RegistroEntrada

                // Comprobamos la unidad destino
                if(reproJson.getDestinoCodigo()!= null && reproJson.isDestinoExterno()){ // Preguntamos a DIR3 si está Vigente
                    Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
                    UnidadTF unidad = unidadesService.obtenerUnidad(reproJson.getDestinoCodigo(), null, null);

                    if(!unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                        reproJson.setDestinoExterno(null);
                        reproJson.setDestinoCodigo(null);
                        reproJson.setDestinoDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        merge(repro);
                    }

                }else{ // Comprobamos en REGWEB3 si está vigente
                    Organismo organismoDestino = organismoEjb.findByCodigoEntidad(reproJson.getDestinoCodigo(), entidad.getId());

                    if(organismoDestino == null){ // Ya no es vigente
                        reproJson.setDestinoExterno(null);
                        reproJson.setDestinoCodigo(null);
                        reproJson.setDestinoDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        merge(repro);
                    }
                }
                break;

            case 2: //RegistroSalida

                // Comprobamos la unidad origen
                if(reproJson.getOrigenCodigo()!= null && reproJson.isOrigenExterno()){ // Preguntamos a DIR3 si está Vigente
                    Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
                    UnidadTF unidad = unidadesService.obtenerUnidad(reproJson.getOrigenCodigo(), null, null);

                    if(!unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                        reproJson.setOrigenExterno(null);
                        reproJson.setOrigenCodigo(null);
                        reproJson.setOrigenDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        merge(repro);
                    }

                }else{ // Comprobamos en REGWEB3 si está vigente
                    Organismo organismoOrigen = organismoEjb.findByCodigoEntidad(reproJson.getOrigenCodigo(), entidad.getId());
                    if(organismoOrigen == null){ // Ya no es vigente
                        reproJson.setOrigenExterno(null);
                        reproJson.setOrigenCodigo(null);
                        reproJson.setOrigenDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        merge(repro);
                    }
                }

                break;


        }

        // Oficina Origen
        if(reproJson.getOficinaCodigo()!= null  && !reproJson.getOficinaCodigo().equals("-1") && reproJson.isOficinaExterna()){// Preguntamos a DIR3 si está Vigente
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            OficinaTF oficina = oficinasService.obtenerOficina(reproJson.getOficinaCodigo(),null,null);

            if(!oficina.getEstado().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                reproJson.setOficinaCodigo(null);
                reproJson.setOficinaDenominacion(null);
                reproJson.setOficinaExterna(null);
                repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                merge(repro);
            }

        }else{// Comprobamos en REGWEB3 si está vigente
            Oficina oficinaOrigen = oficinaEjb.findByCodigoVigente(reproJson.getOficinaCodigo());
            if(oficinaOrigen == null){
                reproJson.setOficinaCodigo(null);
                reproJson.setOficinaDenominacion(null);
                reproJson.setOficinaExterna(null);
                repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                merge(repro);
            }
        }

        return reproJson;
    }

}
