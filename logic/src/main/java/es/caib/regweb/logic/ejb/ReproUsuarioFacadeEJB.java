package es.caib.regweb.logic.ejb;

import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import es.caib.regweb.logic.helper.ParametrosReproUsuario;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.apache.log4j.Logger;
import es.caib.regweb.logic.helper.RegistroRepro;
import es.caib.regweb.model.Repro;
import es.caib.regweb.model.ReproId;

/**
 * SessionBean per a gestió de Repros d'usuari
 *
 * @ejb.bean
 *  name="logic/ReproUsuarioFacade"
 *  jndi-name="es.caib.regweb.logic.ReproUsuarioFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ReproUsuarioFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 2L;
	private Logger log = Logger.getLogger(this.getClass());

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public ParametrosReproUsuario leer(String codUsuario, String nomRepro) throws SQLException, ClassNotFoundException, Exception {
        Session session = getSession();
		ScrollableResults rs=null;
		Query q = null;
        ParametrosReproUsuario res = new ParametrosReproUsuario();
        String datosleidos = null;
        
        log.debug("Preparat per llegir una Repro. Usuari:"+codUsuario+" ,Repro:"+nomRepro);
        try {

            String sentenciaHql="select datos from Repro " +
                                "where id.usuario=? and id.nombre=? ";
            q=session.createQuery(sentenciaHql);
            q.setString(0,codUsuario);
            q.setString(1,nomRepro);
            rs=q.scroll();
            
            if (rs.next()) { 
            	datosleidos = rs.getString(0);
            }else{
            	datosleidos = null;
            }
    		session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
        if (datosleidos != null) {
        	//Hemos encontrado la Repro
        	res.setRepro(datosleidos);
        	res.setCodUsuario(codUsuario);
        	res.setNombre(nomRepro);
        }else{
        	//No hemos encontrado la Repro
        	throw new Exception("No s'ha trobat la Repro. CodUsu:"+codUsuario+". NomRepro:"+nomRepro);
        }
        return res;
	}

	private void eliminar(ParametrosReproUsuario param) throws SQLException, ClassNotFoundException, Exception {
		this.eliminar(param.getUsuario(),param.getNombre());
	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public void eliminar(String codUsuario, String nomRepro) throws SQLException, ClassNotFoundException, Exception {
        Session session = getSession();
		ScrollableResults rs=null;
		Query q = null;
		
		log.debug("Preparat per eliminar una Repro. Usuari:"+codUsuario+" ,Repro:"+nomRepro);
		
        try {
            String sentenciaHql= "delete from Repro  " +
                                 "where  ( id.usuario = ?  and id.nombre = ?)";
            q=session.createQuery(sentenciaHql);
            q.setString(0,codUsuario);
            q.setString(1,nomRepro);
            q.executeUpdate();

    		session.flush();
        } catch (HibernateException he) {
        	log.error("Error al eliminar una Repro. Usuari:"+codUsuario+" ,Repro:"+nomRepro);
            throw new EJBException(he);
        } finally {
            close(session);
        }

	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean grabar(String codUsuario, String nomRepro, String ReproValor, String tipRepro) throws SQLException, ClassNotFoundException, Exception{
        ParametrosReproUsuario param = new ParametrosReproUsuario();
		param.setNombre(nomRepro);
		param.setCodUsuario(codUsuario);
		param.setRepro(ReproValor);
		param.setTipRepro(tipRepro);
		return this.grabar(param);
	}

	private boolean grabar(ParametrosReproUsuario param){
        Session session = getSession();

        boolean ok = true;
      
        log.debug("Preparat per grabar una Repro. Usuari:"+param.getUsuario()+" ,NomRepro:"+param.getNombre()+"Repro:"+param.getRepro());
        try {
            Repro r = new Repro(new ReproId(param.getUsuario(),param.getNombre(),param.getTipRepro()), param.getRepro());
            session.save(r);
    		session.flush();
        } catch (Exception he) {
            ok=false;
            //throw new EJBException(he);
        } finally {
            close(session);
        }
        return ok;
	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperarRepros(String usuario, String tipo){
		Vector vectorRegistrosRepro=new Vector();
        Session session = getSession();
		ScrollableResults rs=null;
		Query q = null;
        
		try {
            String sentenciaHql="select id.nombre,datos from Repro " +
                                "where id.usuario=? ";
            if(tipo != null){
            	sentenciaHql+=" and id.tipo=? ";
            }
            sentenciaHql+= " order by id.nombre asc";
            q=session.createQuery(sentenciaHql);
            q.setString(0,usuario);
            if(tipo != null){
            	q.setString(1,tipo);
            }
            rs=q.scroll();
            while (rs.next()) {
            	RegistroRepro registro = new RegistroRepro();
            	registro.setNomRepro(rs.getString(0).trim());
            	registro.setRepro(rs.getString(1).trim());
            	registro.setCodUsuario(usuario);
            	vectorRegistrosRepro.addElement(registro);
            }
            
    		session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
		return vectorRegistrosRepro;
	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperarRepros(String usuario){
			return recuperarRepros(usuario,null);
	}

	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
	
}
