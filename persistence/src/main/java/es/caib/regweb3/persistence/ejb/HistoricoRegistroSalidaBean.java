package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.HistoricoRegistroSalida;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 30/10/14
 */

@Stateless(name = "HistoricoRegistroSalidaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class HistoricoRegistroSalidaBean extends BaseEjbJPA<HistoricoRegistroSalida, Long> implements HistoricoRegistroSalidaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public HistoricoRegistroSalida getReference(Long id) throws I18NException {

        return em.getReference(HistoricoRegistroSalida.class, id);
    }

    @Override
    public HistoricoRegistroSalida findById(Long id) throws I18NException {

        return em.find(HistoricoRegistroSalida.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroSalida> getAll() throws I18NException {

        return em.createQuery("Select historicoRegistroSalida from HistoricoRegistroSalida as historicoRegistroSalida order by historicoRegistroSalida.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(historicoRegistroSalida.id) from HistoricoRegistroSalida as historicoRegistroSalida");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<HistoricoRegistroSalida> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select historicoRegistroSalida from HistoricoRegistroSalida as historicoRegistroSalida order by historicoRegistroSalida.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroSalida> getByRegistroSalida(Long idRegistro) throws I18NException {

        Query q = em.createQuery("Select hrs.id, hrs.registroSalidaOriginal, hrs.estado, hrs.fecha, hrs.modificacion, hrs.usuario.id, hrs.usuario.usuario from HistoricoRegistroSalida as hrs where hrs.registroSalida.id =:idRegistro order by hrs.fecha desc");
        q.setParameter("idRegistro", idRegistro);
        q.setHint("org.hibernate.readOnly", true);

        List<HistoricoRegistroSalida> hrss = new ArrayList<HistoricoRegistroSalida>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroSalida hrs = new HistoricoRegistroSalida((Long) object[0], (String) object[1], (Long) object[2], (Date) object[3], (String) object[4], (Long) object[5], (Usuario) object[6]);

            hrss.add(hrs);
        }

        return hrss;
    }


    @Override
    public HistoricoRegistroSalida crearHistoricoRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws I18NException {

        HistoricoRegistroSalida historico = new HistoricoRegistroSalida();

        historico.setEstado(registroSalida.getEstado());
        historico.setRegistroSalida(registroSalida);
        historico.setFecha(new Date());
        historico.setModificacion(modificacion);
        historico.setUsuario(usuarioEntidad);
        //Serializamos el RegistroEntrada original
        if (serializar) {
            String registroEntradaOrigial = null;
            try {
                registroEntradaOrigial = RegistroUtils.serilizarXml(registroSalida);
            } catch (JAXBException e) {
                throw new I18NException("Error serializando el registro para crear un Historico");
            }
            historico.setRegistroSalidaOriginal(registroEntradaOrigial);
        }

        // Guardamos el histórico
        return persist(historico);
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException {

        Query q;

        q = em.createQuery("Select count(hrs.id) from HistoricoRegistroSalida as hrs where hrs.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> hrs = em.createQuery("Select distinct(hre.id) from HistoricoRegistroSalida as hre where hre.registroSalida.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = hrs.size();

        if (hrs.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (hrs.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = hrs.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from HistoricoRegistroSalida where id in (:hrs) ").setParameter("hrs", subList).executeUpdate();
                hrs.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from HistoricoRegistroSalida where id in (:hrs) ").setParameter("hrs", hrs).executeUpdate();
        }

        return total;
    }


}