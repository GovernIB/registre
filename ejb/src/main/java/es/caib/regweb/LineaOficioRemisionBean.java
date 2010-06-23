package es.caib.regweb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.text.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.*;

import java.rmi.*;

import javax.ejb.*;
import java.util.Vector;

import java.io.OutputStream;

/**
 * Bean que gestiona els oficis de remisió
 * @author  AROGEL
 * @version 1.0
 */

public class LineaOficioRemisionBean implements SessionBean {
    
    private SessionContext context;    
    private SessionContext contextoSesion;
    
    private String usuario="";

    private boolean error=false;
    private boolean leidos=false;
    private Hashtable errores=new Hashtable();
    private boolean registroGrabado=false;
    private boolean registroActualizado=false;
    private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
    private Date fechaTest=null;
    private DateFormat horaF=new SimpleDateFormat("HH:mm");
    private Date horaTest=null;;
    private String SENTENCIA="INSERT INTO BZOFRENT (" +
            "REN_ENTANY, REN_ENTOFI, REN_ENTNUM, REN_ENTDES, REN_ENTUSU, REN_ENTMTD, "+
            "REN_OFANY, REN_OFOFI, REN_OFNUM "+
            ") VALUES (?,?,?, ?,?,?, ?,?,?)";
    private String SENTENCIA_UPDATE="UPDATE BZOFRENT " +
    		"SET REN_ENTDES=?,  REN_ENTUSU=?,  REN_ENTMTD=?, "+
    		" REN_OFANY=?, REN_OFOFI=?,  REN_OFNUM=? "+
    		" WHERE REN_ENTANY=? AND REN_ENTOFI=? AND REN_ENTNUM=?";
   
    private String anoEntrada=null;
    private String numeroEntrada=null;
    private String oficinaEntrada=null;
    private String usuarioEntrada="";
    private String descartadoEntrada="";
    private String motivosDescarteEntrada="";

    private String anoOficio=null;
    private String numeroOficio=null;
    private String oficinaOficio=null;
    
    
    public LineaOficioRemisionBean() {
    }
    

    
    /* Grabamos registro si las validaciones son correctas */
    
    public String getAnoEntrada() {
		return anoEntrada;
	}



	public void setAnoEntrada(String anoEntrada) {
		this.anoEntrada = anoEntrada;
	}



	public String getNumeroEntrada() {
		return numeroEntrada;
	}



	public void setNumeroEntrada(String numeroEntrada) {
		this.numeroEntrada = numeroEntrada;
	}



	public String getOficinaEntrada() {
		return oficinaEntrada;
	}



	public void setOficinaEntrada(String oficinaEntrada) {
		this.oficinaEntrada = oficinaEntrada;
	}



	public String getUsuarioEntrada() {
		return usuarioEntrada;
	}



	public void setUsuarioEntrada(String usuarioEntrada) {
		this.usuarioEntrada = usuarioEntrada;
	}



	public String getDescartadoEntrada() {
		return descartadoEntrada;
	}



	public void setDescartadoEntrada(String descartadoEntrada) {
		this.descartadoEntrada = descartadoEntrada;
	}



	public String getMotivosDescarteEntrada() {
		return motivosDescarteEntrada;
	}



	public void setMotivosDescarteEntrada(String motivosDescarteEntrada) {
		this.motivosDescarteEntrada = motivosDescarteEntrada;
	}





	public String getAnoOficio() {
		return anoOficio;
	}



	public void setAnoOficio(String anoOficio) {
		this.anoOficio = anoOficio;
	}



	public String getNumeroOficio() {
		return numeroOficio;
	}



	public void setNumeroOficio(String numeroOficio) {
		this.numeroOficio = numeroOficio;
	}



	public String getOficinaOficio() {
		return oficinaOficio;
	}



	public void setOficinaOficio(String oficinaOficio) {
		this.oficinaOficio = oficinaOficio;
	}


	
	public String getUsuario() {
		return usuario;
	}



	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}



	public Hashtable getErrores() {
		return errores;
	}



	public void setErrores(Hashtable errores) {
		this.errores = errores;
	}



	public boolean isLeidos() {
		return leidos;
	}



	public void setLeidos(boolean leidos) {
		this.leidos = leidos;
	}



	/**
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public void grabar() throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        try {
            conn=ToolsBD.getConn();
            conn.setAutoCommit(false);
            // Ejecuta algoritmo de registro
            cargar (conn);
            conn.commit();
        } catch (Exception ex) {
        	System.out.println(usuario+": Excepció: "+ex.getMessage());
            ex.printStackTrace();
            registroGrabado=false;
            errores.put("","Error inesperat, no s'ha desat l'ofici "+": "+ex.getClass()+"->"+ex.getMessage());
            try {
            	if (conn != null)
            		conn.rollback();
            } catch (SQLException sqle) {
                throw new RemoteException(usuario+": S'ha produït un error i no s'han pogut tornar enrere els canvis efectuats", sqle);
            }
            throw new RemoteException("Error inesperat: No s'ha desat l'ofici", ex);
        } finally {
        	ToolsBD.closeConn(conn, null, null);
        }
    }

   
     /**
      * @throws SQLException
      * @throws ClassNotFoundException
      * @throws Exception
      */
    private void cargar(Connection conn) throws SQLException, ClassNotFoundException, Exception {
        /* Grabamos registro si las validaciones son correctas */
        PreparedStatement ms = null;

        registroGrabado=false;
            
            
            
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
            ms=conn.prepareStatement(SENTENCIA);
            
            ms.setInt(1,Integer.parseInt(anoEntrada));
            ms.setInt(2,Integer.parseInt(oficinaEntrada));
            ms.setInt(3,Integer.parseInt(numeroEntrada));
            ms.setString(4,descartadoEntrada);
            ms.setString(5,usuarioEntrada);
            ms.setString(6,motivosDescarteEntrada);
            if(anoOficio!=null) {
            	ms.setInt(7,Integer.parseInt(anoOficio));	
            } else {
            	ms.setInt(7, 0);	
            }
            if(oficinaOficio!=null) {
            	ms.setInt(8,Integer.parseInt(oficinaOficio));
            } else {
            	ms.setInt(8, 0);	
            }
            if(numeroOficio!=null) {
            	ms.setInt(9,Integer.parseInt(numeroOficio));
            } else {
            	ms.setInt(9, 0);	
            }

            registroGrabado=ms.execute();
            registroGrabado=true;
            
           
            ms.close();
    }
    /**
     * Actualitza el registre d'entrada
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public void actualizar() throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        PreparedStatement ms = null;
        
        registroActualizado=false;
        try {
			/* Recuperamos numero de entrada */
			conn=ToolsBD.getConn();
			conn.setAutoCommit(false);

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
            ms=conn.prepareStatement(SENTENCIA_UPDATE);
//            if(fechaOficio!=null) {            	
//                fechaTest = dateF.parse(fechaOficio);
//                Calendar cal=Calendar.getInstance();
//                cal.setTime(fechaTest);
//                DateFormat date1=new SimpleDateFormat("yyyyMMdd");
//                                
//                int fzafent=Integer.parseInt(date1.format(fechaTest));
//
//            	ms.setInt(1,fzafent);
//            } else {
//            	ms.setInt(1, 0);	
//            }
            ms.setString(1,descartadoEntrada);
            ms.setString(2,usuarioEntrada);
            ms.setString(3,motivosDescarteEntrada);
            ms.setInt(4,Integer.parseInt(anoOficio));
            ms.setInt(5,Integer.parseInt(oficinaOficio));
            ms.setInt(6,Integer.parseInt(numeroOficio));
            ms.setInt(7,anoEntrada!=null?Integer.parseInt(anoEntrada):0);
            ms.setInt(8,oficinaEntrada!=null?Integer.parseInt(oficinaEntrada):0);
            ms.setInt(9,numeroEntrada!=null?Integer.parseInt(numeroEntrada):0);
            
            
            int afectados=ms.executeUpdate();
            if (afectados>0){
                registroActualizado=true;
            } else {
                registroActualizado=false;
            }

            
            conn.commit();

        } catch (Exception ex) {
        	System.out.println("Error inesperat, no s'ha desat el registre: "+ex.getMessage());
            ex.printStackTrace();
            registroActualizado=false;
            errores.put("","Error inesperat, no s'ha desat el registre"+": "+ex.getClass()+"->"+ex.getMessage());
            try {
            	if (conn!=null)
            		conn.rollback();
            } catch (SQLException sqle) {
                throw new RemoteException("S'ha produït un error i no s'han pogut tornar enrere els canvis efectuats", sqle);
            }
            throw new RemoteException("Error inesperat, no s'ha modifcat el registre", ex);
        } finally {
        	ToolsBD.closeConn(conn, ms, null);
        }
    }
    
    
    
    /**
     * Valida la data donada
     * @param fecha
     */
    private void validarFecha(String fecha) {
        try {
            dateF.setLenient(false);
            fechaTest = dateF.parse(fecha);
            error=false;
        } catch (Exception ex) {
        	System.out.println("Error validant la data:"+ex.getMessage());
        	ex.printStackTrace();
            error=true;
        }
    }
    
    /** 
     * Lee un registro del fichero BZENTRA, para ello le
     * deberemos pasar el usuario, el codigo de oficina, el numero de registro de
     * entrada y el año de entrada.
     * @param usuario String
     * @param oficina String
     * @param numeroEntrada String
     * @param anoEntrada String
     * @return void
     */
    public void leer() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        leidos=false;
        try {
            conn=ToolsBD.getConn();

            String sentenciaSql="SELECT * FROM BZREMIS " +
                    "WHERE REN_SALANY=? AND REN_SALOFI=? AND REN_SALNUM=?";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setInt(1,Integer.parseInt(anoOficio));
            ps.setInt(2,Integer.parseInt(oficinaOficio));
            ps.setInt(3,Integer.parseInt(numeroOficio));
            rs=ps.executeQuery();
            if (rs.next()) {
            	/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            	
                leidos=true;
                anoEntrada=String.valueOf(rs.getInt("REN_ENTANY"));
                numeroEntrada=String.valueOf(rs.getInt("REN_ENTNUM"));
                oficinaEntrada=String.valueOf(rs.getInt("REN_ENTOFI"));
                descartadoEntrada=rs.getString("REN_ENTDES");
                motivosDescarteEntrada=rs.getString("REN_ENTMTD");
                usuarioEntrada=rs.getString("REN_ENTUSU");

                anoOficio=String.valueOf(rs.getInt("REN_OFANY"));
                numeroOficio=String.valueOf(rs.getInt("REN_OFNUM"));
                oficinaOficio=String.valueOf(rs.getInt("REN_OFOFI"));

            }
        } catch (Exception e) {
        	System.out.println("ERROR: Leer: "+e.getMessage());
            e.printStackTrace();
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
    }
    
    
    /* Devolvemos si el registro se ha grabado bien */
    /**
     * @return
     */
    public boolean getGrabado() {
        return registroGrabado;
    }
    /* Devolvemos si el registro se ha actualizado  bien */
    /**
     * @return
     */
    public boolean getActualizado() {
        return registroActualizado;
    }
    
    
    
    public void ejbCreate() throws CreateException {
        //System.out.println("EJB creado");
    }
    public void ejbActivate() {
    }
    public void ejbPassivate() {
    }
    public void ejbRemove() {
        // System.out.println("EJB eliminado");
    }
    public void setSessionContext(SessionContext ctx) {
    this.context = ctx;
    }


}