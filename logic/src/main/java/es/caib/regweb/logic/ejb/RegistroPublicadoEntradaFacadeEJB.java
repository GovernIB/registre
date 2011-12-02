package es.caib.regweb.logic.ejb;

import javax.ejb.*;

import es.caib.regweb.logic.helper.ParametrosRegistroPublicadoEntrada;
import org.apache.log4j.Logger;

import org.hibernate.Query;
// import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;

import es.caib.regweb.model.Publicacion;
import es.caib.regweb.model.PublicacionId;

/**
 * SessionBean per a manteniment de registres publicats d'entrada
 *
 * @ejb.bean
 *  name="logic/RegistroPublicadoEntradaFacade"
 *  jndi-name="es.caib.regweb.logic.RegistroPublicadoEntradaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class RegistroPublicadoEntradaFacadeEJB extends HibernateEJB {
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
    
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public ParametrosRegistroPublicadoEntrada leer(ParametrosRegistroPublicadoEntrada param) throws Exception{
		Session session = getSession();
		Query q=null;
		ScrollableResults rs=null;
		ParametrosRegistroPublicadoEntrada res = new ParametrosRegistroPublicadoEntrada();
		res.setLeido(false);
		
		try {
			String sentenciaHql="select id.anyo, id.numero, id.oficina, numeroBocaib, fechaPublicacion, " +
                " numeroPagina, numeroLineas, contenido, observaciones " +
                " from Publicacion where id.anyo=? and id.numero=? and id.oficina=?";
			q=session.createQuery(sentenciaHql);
			q.setInteger(0,param.getAnoEntrada());
			q.setInteger(1,param.getNumero());
			q.setInteger(2,param.getOficina());
			rs=q.scroll();
			if (rs.next()) {
				res.setLeido(true);
				res.setAnoEntrada(rs.getInteger(0));
				res.setNumero(rs.getInteger(1));
				res.setOficina(rs.getInteger(2));
				res.setNumeroBOCAIB(rs.getInteger(3));
				res.setFecha(rs.getInteger(4));
				res.setPagina(rs.getInteger(5));
				res.setLineas(rs.getInteger(6));
				res.setContenido(rs.getString(7));
				res.setObservaciones(rs.getString(8));
			}

			session.flush();
		} catch (Exception e) {
			throw new Exception("S'ha produït un error select BZPUBLI",e);
        } finally {
            close(session);
        }
		return res;
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public void borrar(ParametrosRegistroPublicadoEntrada param) throws Exception{
		Session session = getSession();
		Query q=null;
		try {
			String sentenciaHql="delete from Publicacion where id.anyo=? and id.numero=? and id.oficina=?";
			q=session.createQuery(sentenciaHql);
			q.setInteger(0,param.getAnoEntrada());
			q.setInteger(1,param.getNumero());
			q.setInteger(2,param.getOficina());
			q.executeUpdate();

			session.flush();
		} catch (Exception e) {
			throw new Exception("S'ha produït un error delete BZPUBLI", e);
        } finally {
            close(session);
        }
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public void grabar(ParametrosRegistroPublicadoEntrada param) throws Exception{
		Session session = getSession();
		Query q=null;
		try {
			log.debug("Update de BZPUBLI. Reg:"+param.getNumero()+"/"+param.getAnoEntrada());
			String sentenciaHql="update Publicacion set numeroBocaib=?, fechaPublicacion=?, numeroPagina=?, numeroLineas=?, contenido=?, observaciones=?" +
			" where id.anyo=? and id.numero=? and id.oficina=?";
			q=session.createQuery(sentenciaHql);
			q.setInteger(0,param.getNumeroBOCAIB());
			q.setInteger(1,param.getFecha());
			q.setInteger(2,param.getPagina());
			q.setInteger(3,param.getLineas());
			q.setString(4, param.getContenido());
			q.setString(5, param.getObservaciones());

			q.setInteger(6,param.getAnoEntrada());
			q.setInteger(7,param.getNumero());
			q.setInteger(8,param.getOficina());
			int afectados=q.executeUpdate();
			
			if (afectados==0) {
				log.debug("No hay registro BZPUBLI. Lo insertamos");
			    Publicacion pub = new Publicacion(
			        new PublicacionId(param.getAnoEntrada(),
			            param.getNumero(),
			            param.getOficina() 
			        ),
			        param.getNumeroBOCAIB(),
			        param.getFecha(),
			        param.getPagina(),
			        param.getLineas(),
			        param.getContenido(),
			        param.getObservaciones()
			    );
			    session.save(pub);				
			}
			session.flush();
			
		} catch (Exception e) {
			log.warn("S'ha produït un error grabar BZPUBLI.");
			throw new Exception("S'ha produït un error grabar BZPUBLI.", e);
        } finally {
            close(session);
        }
	}
	
	
	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
	
	
}