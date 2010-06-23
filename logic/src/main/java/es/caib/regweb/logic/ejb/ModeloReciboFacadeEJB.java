package es.caib.regweb.logic.ejb;

import es.caib.regweb.logic.helper.RegistroRepro;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;

import java.util.Vector;

import javax.ejb.CreateException;

import org.apache.log4j.Logger;

import es.caib.regweb.model.ModeloRecibo;

/**
 * SessionBean per a manteniment de models de rebut
 *
 * @ejb.bean
 *  name="logic/ModeloReciboFacade"
 *  jndi-name="es.caib.regweb.logic.ModeloReciboFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ModeloReciboFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
 
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public ModeloRecibo leer(String nomModel) throws ClassNotFoundException, Exception {
        Session session = getSession();
        String datosleidos = null;
        java.sql.Blob dades=null;
        ModeloRecibo res = null;

        try {
            String sentenciaHql=" from ModeloRecibo " +
                            "where nombre=? ";
            Query query=session.createQuery(sentenciaHql);
            query.setString(0,nomModel);
            res = (ModeloRecibo) query.uniqueResult();
            
        	session.flush();
        } catch (Exception e) {
        	String mesError = new String("ERROR: Leer: "+e.getMessage());
            throw new Exception(mesError, e);
        } finally {
            close(session);
        }
		
        if (res == null) {
        	//No hemos encontrado la Repro
        	throw new Exception("No s'ha trobat el model. NomModel:"+nomModel);
        }
        return res;
	}
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public void eliminar(String nomModel) throws ClassNotFoundException, Exception {
        Session session = getSession();

        try {
            String sentenciaHql= "delete from ModeloRecibo  " +
                                 "where  ( nombre = ?)";
            Query query = session.createQuery(sentenciaHql);
            query.setString(0,nomModel);
            query.executeUpdate();
            //conn.commit();
        	session.flush();
        } catch (Exception e) {
        	String resposta = "ERROR: No s'ha pogut esborrar el model.";
        	//e.printStackTrace();
            throw new Exception(resposta, e);
        } finally {
            close(session);
        }
		
	}
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean grabar(String nomModel, String conType, byte[] dades) throws ClassNotFoundException, Exception{
        Session session = getSession();
        boolean ok = true;
      
        try {
            ModeloRecibo model = new ModeloRecibo(nomModel, conType, dades);

            session.save(model);
        	session.flush();
        } catch (Exception e) {
        	ok = false;
            e.printStackTrace();
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
        
		try {
            String sentenciaHql="select id.nombre,datos from Repro " +
                                "where id.usuario=? ";
            if(tipo != null){
            	sentenciaHql+=" and tipo=? ";
            }
            sentenciaHql+= " order by nombre asc";

			Query query=session.createQuery(sentenciaHql);
            query.setString(0,usuario);
            if(tipo != null){
            	query.setString(1,tipo);
            }
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);

            while (rs.next()) {
            	RegistroRepro registro = new RegistroRepro();
            	registro.setNomRepro(rs.getString(0).trim());
            	registro.setRepro(rs.getString(1).trim());
            	registro.setCodUsuario(usuario);
            	vectorRegistrosRepro.addElement(registro);
            }
            
			if (rs!=null) rs.close();
        	session.flush();
        }catch(Exception e){
        	String resposta = "ERROR: No s'ha pogut llegir les repros de l'usuari:"+usuario;
        	log.debug(resposta);
            e.printStackTrace();
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
