package es.caib.regweb.logic.ejb;

import es.caib.regweb.logic.helper.ParametrosOficioRemision;
import es.caib.regweb.model.OficioRemision;
import es.caib.regweb.model.OficioRemisionId;

import es.caib.regweb.logic.helper.Helper;

import java.util.*;
import java.text.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;

import javax.ejb.*;

import org.apache.log4j.Logger;

/**
 * SessionBean per a manteniment d'oficis de remissió
 *
 * @ejb.bean
 *  name="logic/OficioRemisionFacade"
 *  jndi-name="es.caib.regweb.logic.OficioRemisionFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class OficioRemisionFacadeEJB extends HibernateEJB {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Logger log = Logger.getLogger(this.getClass());
    
    //private String usuario="";

    private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
    private Date fechaTest=null;


    private String SENTENCIA_UPDATE="update OficioRemision " +
    		"set fechaOficio=?, contenido=?, anyoSalida=?, oficinaSalida=?, numeroSalida=?, nula=?, motivosNula=?, usuarioNula=?, fechaNula=?, "+
            "fechaEntrada=?, descarteEntrada=?, usuarioEntrada=?, motivosDescarteEntrada=?, anyoEntrada=?, oficinaEntrada=?, numeroEntrada=? "+
    		" where id.anyoOficio=? and id.oficinaOficio=? and id.numeroOficio=?";
   




	/**
     * @throws ClassNotFoundException
     * @throws Exception
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosOficioRemision grabar(ParametrosOficioRemision parametros) throws ClassNotFoundException, Exception {
        Session session = getSession();
        boolean registroGrabado=false;
        try {




            /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            Date fechaSystem=new Date();
            //DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
            //int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));

            //DateFormat hhmmss=new SimpleDateFormat("HHmmss");
            DateFormat sss=new SimpleDateFormat("S");
            String ss=sss.format(fechaSystem);
            if (ss.length()>2) {
                ss=ss.substring(0,2);
            }
           // int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);

            Calendar c2=Calendar.getInstance();
            c2.setTime(fechaSystem);


            fechaTest = dateF.parse(parametros.getFechaOficio());
            Calendar cal=Calendar.getInstance();
            cal.setTime(fechaTest);
            DateFormat date1=new SimpleDateFormat("yyyyMMdd");

            int fzaanoe=cal.get(Calendar.YEAR);
            parametros.setAnoOficio(String.valueOf(fzaanoe));

            int dataofici=Integer.parseInt(date1.format(fechaTest));


            int numof= Helper.recogerNumeroOficio(session, Integer.parseInt(parametros.getAnoOficio()), parametros.getOficinaOficio(), parametros.getErrores());
            parametros.setNumeroOficio(String.valueOf(numof));
            
            int fzafent = 0;
            if(parametros.getFechaEntrada()!=null && !parametros.getFechaEntrada().equals("")) {
            	fechaTest = dateF.parse(parametros.getFechaEntrada());
                cal.setTime(fechaTest);

                fzafent=Integer.parseInt(date1.format(fechaTest));
            }


            OficioRemision oficio = new OficioRemision(
                new OficioRemisionId(Integer.parseInt(parametros.getAnoOficio()),
                    Integer.parseInt(parametros.getOficinaOficio()),
                    numof
                ),
                dataofici,
                parametros.getDescripcion(),
                (parametros.getAnoSalida()!=null ? Integer.parseInt(parametros.getAnoSalida()) : 0),
                (parametros.getOficinaSalida()!=null ? Integer.parseInt(parametros.getOficinaSalida()) : 0),
                (parametros.getNumeroSalida()!=null ? Integer.parseInt(parametros.getNumeroSalida()) : 0),
                parametros.getNulo(),
                parametros.getMotivosNulo(),
                parametros.getUsuarioNulo(),
                0,
              	fzafent,
                parametros.getDescartadoEntrada(),
                parametros.getUsuarioEntrada(),
                parametros.getMotivosDescarteEntrada(),
                (parametros.getAnoEntrada()!=null ? Integer.parseInt(parametros.getAnoEntrada()) : 0),
                (parametros.getOficinaEntrada()!=null ? Integer.parseInt(parametros.getOficinaEntrada()) : 0),
                (parametros.getNumeroEntrada()!=null ? Integer.parseInt(parametros.getNumeroEntrada()) : 0)
            );
            
            session.save(oficio);

            session.flush();

            registroGrabado=true;


        } catch (HibernateException he) {
            registroGrabado=false;

            throw new EJBException(he);
        } finally {
            close(session);
        }

        parametros.setGrabado(registroGrabado);
        return parametros;
    }

   
   
    /**
     * Actualitza la taula de gestió dels ofici de remissió.
     * @throws ClassNotFoundException
     * @throws Exception
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosOficioRemision actualizar(ParametrosOficioRemision parametros) throws ClassNotFoundException, Exception {
		Session session = getSession();
		Query q = null;
        
        boolean registroActualizado=false;
        try {
            /* Ejecutamos sentencias SQL */
            q=session.createQuery(SENTENCIA_UPDATE);

            		try{
            	q.setInteger(0, Helper.convierteStringFechaAIntFecha(parametros.getFechaOficio()));
            }catch( Exception ex){
            	q.setInteger(0, 0);	
            }
            q.setString(1, parametros.getDescripcion());
            q.setInteger(2,Integer.parseInt(parametros.getAnoSalida()));
            q.setInteger(3,Integer.parseInt(parametros.getOficinaSalida()));
            q.setInteger(4,Integer.parseInt(parametros.getNumeroSalida()));
            q.setString(5,parametros.getNulo());
            q.setString(6,parametros.getMotivosNulo());
            q.setString(7,parametros.getUsuarioNulo());
            	
        		try{
            	q.setInteger(8, Helper.convierteStringFechaAIntFecha(parametros.getFechaNulo()));
            }catch( Exception ex){
            	q.setInteger(8, 0);	
            }
        		try{
            	q.setInteger(9, Helper.convierteStringFechaAIntFecha(parametros.getFechaEntrada()));
            }catch( Exception ex){
            	q.setInteger(9, 0);	
            }
            q.setString(10,parametros.getDescartadoEntrada());
            q.setString(11,parametros.getUsuarioEntrada());
            q.setString(12,parametros.getMotivosDescarteEntrada());
            q.setInteger(13,parametros.getAnoEntrada()!=null?Integer.parseInt(parametros.getAnoEntrada()):0);
            q.setInteger(14,parametros.getOficinaEntrada()!=null?Integer.parseInt(parametros.getOficinaEntrada()):0);
            q.setInteger(15,parametros.getNumeroEntrada()!=null?Integer.parseInt(parametros.getNumeroEntrada()):0);
            q.setInteger(16,parametros.getAnoOficio()!=null?Integer.parseInt(parametros.getAnoOficio()):0);
            q.setInteger(17,parametros.getOficinaOficio()!=null?Integer.parseInt(parametros.getOficinaOficio()):0);
            q.setInteger(18,parametros.getNumeroOficio()!=null?Integer.parseInt(parametros.getNumeroOficio()):0);
            
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

        parametros.setActualizado(registroActualizado);
        return parametros;
    }
    
    /**
     * Anul·la el registre d'entrada
     * @throws ClassNotFoundException
     * @throws Exception
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public boolean anular(ParametrosOficioRemision parametros) throws ClassNotFoundException, Exception {
		Session session = getSession();
		Query q = null;
        
        boolean registroActualizado=false;
        
        try {

            /* Ejecutamos sentencias SQL */
		   // String sentencia_delete="delete from OficioRemision " +
    		//	" where id.anyoOficio=? and id.oficinaOficio=? and id.numeroOficio=?";
 		   String sentencia_delete="Update OficioRemision " +
 		                           "set nula = 'S'" +
    			" where id.anyoOficio=? and id.oficinaOficio=? and id.numeroOficio=?";
    			
            q=session.createQuery(sentencia_delete);
            q.setInteger(0,parametros.getAnoOficio()!=null?Integer.parseInt(parametros.getAnoOficio()):0);
            q.setInteger(1,parametros.getOficinaOficio()!=null?Integer.parseInt(parametros.getOficinaOficio()):0);
            q.setInteger(2,parametros.getNumeroOficio()!=null?Integer.parseInt(parametros.getNumeroOficio()):0);
            
            
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
        return registroActualizado;
    }

    
    /** 
     * Lee un registro del fichero BZENTRA, para ello le
     * deberemos pasar el usuario, el codigo de oficina, el numero de registro de
     * entrada y el año de entrada.
     * @param parametros ParametrosOficioRemision
     * @return ParametrosOficioRemision
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosOficioRemision leer(ParametrosOficioRemision parametros) {
        Session session = getSession();
		ScrollableResults rs=null;
		Query q = null;

        ParametrosOficioRemision res = new ParametrosOficioRemision();
        
        //boolean leidos=false;
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
        try {

            String sentenciaHql="select id.anyoOficio, id.numeroOficio, id.oficinaOficio, fechaOficio, " +
                    " contenido, anyoSalida, numeroSalida, oficinaSalida, nula,  " +
                    " motivosNula, usuarioNula, fechaNula, anyoEntrada, numeroEntrada,  " +
                    " oficinaEntrada, descarteEntrada, motivosDescarteEntrada,  " +
                    " usuarioEntrada, fechaEntrada " +
                    " from OficioRemision " +
            		" where id.anyoOficio=? and id.oficinaOficio=? and id.numeroOficio=?";
            		

            q=session.createQuery(sentenciaHql);
            q.setInteger(0,Integer.parseInt(parametros.getAnoOficio()));
            q.setInteger(1,Integer.parseInt(parametros.getOficinaOficio()));
            q.setInteger(2,Integer.parseInt(parametros.getNumeroOficio()));
            rs=q.scroll();
            if (rs.next()) {
            	/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            	
                //leidos=true;
                res.setAnoOficio(String.valueOf(rs.getInteger(0)));
                res.setNumeroOficio(String.valueOf(rs.getInteger(1)));
                res.setOficinaOficio(String.valueOf(rs.getInteger(2)));
				String fechaO=String.valueOf(rs.getInteger(3));
				try {
					fechaDocumento=yyyymmdd.parse(fechaO);
					res.setFechaOficio(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					res.setFechaOficio(fechaO);
				}

				res.setDescripcion(rs.getString(4));
                
                res.setAnoSalida(String.valueOf(rs.getInteger(5)));
                res.setNumeroSalida(String.valueOf(rs.getInteger(6)));
                res.setOficinaSalida(String.valueOf(rs.getInteger(7)));

                res.setNulo(rs.getString(8));
                res.setMotivosNulo(rs.getString(9));
                res.setUsuarioNulo(rs.getString(10));
				String fechaN=String.valueOf(rs.getInteger(11));
				try {
					fechaDocumento=yyyymmdd.parse(fechaN);
					res.setFechaNulo(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					res.setFechaNulo(fechaN);
				}

                res.setAnoEntrada(String.valueOf(rs.getInteger(12)));
                res.setNumeroEntrada(String.valueOf(rs.getInteger(13)));
                res.setOficinaEntrada(String.valueOf(rs.getInteger(14)));
                res.setDescartadoEntrada(rs.getString(15));
                res.setMotivosDescarteEntrada(rs.getString(16));
                res.setUsuarioEntrada(rs.getString(17));
				String fechaE=String.valueOf(rs.getInteger(18));
				try {
					fechaDocumento=yyyymmdd.parse(fechaE);
					res.setFechaEntrada(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					res.setFechaEntrada(fechaE);
				}


            }

    		session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
        return res;
    }
    
    
	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
    

}
