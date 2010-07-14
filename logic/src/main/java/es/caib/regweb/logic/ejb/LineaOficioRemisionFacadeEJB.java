package es.caib.regweb.logic.ejb;

import es.caib.regweb.logic.helper.ParametrosLineaOficioRemision;

import java.util.*;
import java.text.*;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import es.caib.regweb.model.LineaOficioRemision;
import es.caib.regweb.model.LineaOficioRemisionId;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.ejb.*;

/**
 * SessionBean per a oficis de remissió 
 *
 * @ejb.bean
 *  name="logic/LineaOficioRemisionFacade"
 *  jndi-name="es.caib.regweb.logic.LineaOficioRemisionFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class LineaOficioRemisionFacadeEJB extends HibernateEJB {
    
    private Logger log = Logger.getLogger(this.getClass());
    
    private String usuario="";

    private boolean error=false;
    private boolean leidos=false;
    private Hashtable errores=new Hashtable();
    private boolean registroGrabado=false;
    private boolean registroActualizado=false;
    private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
    private Date fechaTest=null;
    private DateFormat horaF=new SimpleDateFormat("HH:mm");
    private Date horaTest=null;
    private String SENTENCIA_UPDATE=" update LineaOficioRemision " +
    		"set descarteEntrada=?,  usuarioEntrada=?,  motivosDescarteEntrada=?, "+
    		" anyoOficio=?, oficinaOficio=?,  numeroOficio=? "+
    		" where id.anyoEntradaRegistro=? and id.oficinaEntradaRegistro=? and id.numeroEntradaRegistro=?";


	/**
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosLineaOficioRemision grabar(ParametrosLineaOficioRemision parametros) throws SQLException, ClassNotFoundException, Exception {

        Session session = getSession();
        try {

            boolean registroGrabado=false;


            /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            Date fechaSystem=new Date();
            DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
            int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));

            DateFormat hhmmss=new SimpleDateFormat("HHmmss");
            DateFormat sss=new SimpleDateFormat("S");
            String ss=sss.format(fechaSystem);
            if (ss.length()>2) {
                ss=ss.substring(0,2);
            }
            int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);


            /* Ejecutamos sentencias SQL */
            LineaOficioRemision linea = new LineaOficioRemision( 
                new LineaOficioRemisionId( Integer.parseInt(parametros.getAnoEntrada()),
                    Integer.parseInt(parametros.getOficinaEntrada()),
                    Integer.parseInt(parametros.getNumeroEntrada()) 
                ),
                parametros.getDescartadoEntrada(),
                parametros.getUsuarioEntrada(),
                parametros.getMotivosDescarteEntrada(),
                (parametros.getAnoOficio()!=null ? Integer.parseInt(parametros.getAnoOficio()): 0),
                (parametros.getOficinaOficio()!=null ? Integer.parseInt(parametros.getOficinaOficio()): 0),
                (parametros.getNumeroOficio()!=null ? Integer.parseInt(parametros.getNumeroOficio()): 0)
            );
            

            session.save(linea);
            session.flush();

            registroGrabado=true;


        } catch (HibernateException he) {
            registroGrabado=false;
            
            throw new EJBException(he);
        } finally {
            close(session);
        }

        parametros.setRegistroGrabado(registroGrabado);
        return parametros;

    }




    /**
     * Actualitza el registre d'entrada
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosLineaOficioRemision actualizar(ParametrosLineaOficioRemision parametros) throws SQLException, ClassNotFoundException, Exception {
		Session session = getSession();
		Query q = null;
        
        boolean registroActualizado=false;
        try {

            /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            Date fechaSystem=new Date();
            DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
            int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
            
            DateFormat hhmmss=new SimpleDateFormat("HHmmss");
            DateFormat sss=new SimpleDateFormat("S");
            String ss=sss.format(fechaSystem);
            if (ss.length()>2) {
                ss=ss.substring(0,2);
            }
            int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
            
            /* Ejecutamos sentencias SQL */
            q=session.createQuery(SENTENCIA_UPDATE);
            q.setString(0,parametros.getDescartadoEntrada());
            q.setString(1,parametros.getUsuarioEntrada());
            q.setString(2,parametros.getMotivosDescarteEntrada());
            q.setInteger(3,Integer.parseInt(parametros.getAnoOficio()));
            q.setInteger(4,Integer.parseInt(parametros.getOficinaOficio()));
            q.setInteger(5,Integer.parseInt(parametros.getNumeroOficio()));
            q.setInteger(6,parametros.getAnoEntrada()!=null?Integer.parseInt(parametros.getAnoEntrada()):0);
            q.setInteger(7,parametros.getOficinaEntrada()!=null?Integer.parseInt(parametros.getOficinaEntrada()):0);
            q.setInteger(8,parametros.getNumeroEntrada()!=null?Integer.parseInt(parametros.getNumeroEntrada()):0);
            
            
            int afectados=q.executeUpdate();
            if (afectados>0){
                registroActualizado=true;
            } else {
                registroActualizado=false;
            }


    		session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
        parametros.setRegistroActualizado(registroActualizado);
        return parametros;
    }
    
    
    
    /**
     * Valida la data donada
     * @param fecha
     */
    private boolean validarFecha(String fecha) {
        boolean error=false;
        try {
            dateF.setLenient(false);
            fechaTest = dateF.parse(fecha);
            error=false;
        } catch (Exception ex) {
        	log.error("Error validant la data:"+ex.getMessage());
        	ex.printStackTrace();
            error=true;
        }
        return !error;
    }
    
    
    
    
     /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
	
	
    

}