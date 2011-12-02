package es.caib.regweb.logic.ejb;

import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.regweb.logic.helper.ParametrosHistoricoEmails;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;

import org.apache.log4j.Logger;

import es.caib.regweb.model.HistoricoEmails;
import es.caib.regweb.model.HistoricoEmailsId;

/**
 * SessionBean per a gestió del històric de emails generats per l'aplicació
 *
 * @author VHERRERA
 *
 * @ejb.bean
 *  name="logic/HistoricoEmailsFacade"
 *  jndi-name="es.caib.regweb.logic.HistoricoEmailsFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class HistoricoEmailsFacadeEJB extends HibernateEJB {

	private Logger log = Logger.getLogger(this.getClass());
    private static final long serialVersionUID = 1L;

    /**
     * Lee los datos de los correos enviados relacionados con un registro
     * 
     * @param anyo Anyo del registro
     * @param numeroRegistro Número identificativo del registro
     * @param codigoOficina Codigo de la oficina de registro
     * @param tipoRegistro Tipo de registro: Entrada o Salida
     * @param tipoCorreo Tipo de correo: Interno o destinado al interesado
     * 
     * @return Vector con los datos de los correos guardados en el histórico que satisfagan los parametros de la consulta.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Vector leer(int anyo, int numeroRegistro, int codigoOficina, String tipoRegistro, String tipoCorreo) throws EJBException {
        Session session = getSession();
		ScrollableResults rs=null;
		Query q = null;
		Vector registrosLeidos = new Vector();
		ParametrosHistoricoEmails res = null;    
        
        try {
            String sentenciaHql="select id.numero,emailDestinatario,codigoUsuario,tipusEmail,fecha,hora "+ 
            					"  from HistoricoEmails " +
	                            " where id.codigoEntradaSalida= '" + tipoRegistro +"'" +
	                            "   and id.anyoEntradaSalida= '" + anyo +"'" +
	                            "   and id.numeroEntradaSalida = '" + numeroRegistro +"'" +
	                            "   and id.oficinaEntradaSalida = '" + codigoOficina +"'" +
	                            ((tipoCorreo==null)?"":"   and tipusEmail = '" + tipoCorreo +"'") +
	                            " order by id.numero ASC";
            log.debug("sentenciaHql:"+sentenciaHql);
            
	        q=session.createQuery(sentenciaHql);
            rs=q.scroll();
            
            while (rs.next()) { 
            	res = new ParametrosHistoricoEmails();
            	res.setAnoRegistro(anyo);
            	res.setCodigoOficina(codigoOficina);
            	res.setTipoRegistro(tipoRegistro);
            	res.setNumeroRegistro(numeroRegistro);
            	res.setNumeroEmail(rs.getInteger(0));
            	res.setEmailDestinatario(rs.getString(1));
            	res.setCodigoUsuario(rs.getString(2));
            	res.setTipoEmail(rs.getString(3));
            	res.setFecha(rs.getString(4));
            	res.setHora(rs.getString(5));
            	
            	registrosLeidos.add(res);
            }
            log.debug("Leidos "+registrosLeidos.size()+" anotaciones del histórico de emails.");
    		session.flush();
        } catch (Exception he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }

        return registrosLeidos;
	}


    /**
     * Graba en el histórico de emails los datos de un email enviado.
     * 
     * @param param Datos de un email
     * 
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public void grabar(ParametrosHistoricoEmails param) throws EJBException{
        Session session = getSession();  
        
    	//Comprobamos si ya tenemos el número de email para el histórico
    	if(param.getNumeroEmail()==-1){
    		param.setNumeroEmail(obtenerNumeroRegistro(param));
    	}
    	
        try {       	       	
        	HistoricoEmailsId id= new HistoricoEmailsId(param.getTipoRegistro(), param.getAnoRegistro(),param.getNumeroRegistro(), param.getCodigoOficina(), param.getNumeroEmail());
        	HistoricoEmails histEmail = new HistoricoEmails(id, param.getTipoEmail(),param.getFecha(), param.getHora(),param.getCodigoUsuario(), param.getEmailDestinatario());
            session.save(histEmail);
    		session.flush();
    		log.debug("HistoricoEmails grabado correctamente.");
        } catch (Exception he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
	}

	/**
	 * Obtiene el proximo valor del contador de correos enviados para un registro
	 * 
	 * @param param Datos del correo enviado
	 * @return
	 * @throws EJBException
	 */
	private int obtenerNumeroRegistro(ParametrosHistoricoEmails param) throws EJBException{
	       Session session = getSession();
			ScrollableResults rs=null;
			Query q = null;
			int rtdo = 1;   
	        
	        try {
	            String sentenciaHql="select max(id.numero) as maximo"+ 
	            					"  from HistoricoEmails " +
		                            " where id.codigoEntradaSalida= '" + param.getTipoRegistro() +"'" +
		                            "   and id.anyoEntradaSalida= " + param.getAnoRegistro() +"" +
		                            "   and id.numeroEntradaSalida = " + param.getNumeroRegistro() +"" +
		                            "   and id.oficinaEntradaSalida = " + param.getCodigoOficina() +"";
  
		        q=session.createQuery(sentenciaHql);
		        rs = q.scroll(ScrollMode.SCROLL_INSENSITIVE);
	            
	            if (rs!=null && rs.next()) { 
	            	rtdo = (rs.getInteger(0)==null)?1:(rs.getInteger(0).intValue()+1);
	            }else{
	            	rtdo = 1;
	            }
	            log.debug("Obtenido el número de email "+rtdo+" para el histórico de email.");
	    		session.flush();
	        } catch (Exception he) {
	            throw new EJBException(he);
	        } finally {
	        	if (rs!=null) rs.close();
	            close(session);
	        }
	        return rtdo;
	}
	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
	
}
