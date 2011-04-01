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

public class OficioRemisionBean implements SessionBean {
    
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
    private Date horaTest=null;
    private String SENTENCIA="INSERT INTO BZOFREM (" +
            "REM_OFANY, REM_OFOFI, REM_OFNUM, REM_OFFEC, REM_CONT, REM_SALANY, REM_SALOFI, REM_SALNUM, REM_NULA, REM_NULMTD, REM_NULUSU, REM_NULFEC, "+
            "REM_ENTFEC, REM_ENTDES, REM_ENTUSU, REM_ENTMTD, REM_ENTANY, REM_ENTOFI, REM_ENTNUM"+
            ") VALUES (?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?)";
    private String SENTENCIA_UPDATE="UPDATE BZOFREM " +
    		"SET REM_OFFEC=?, REM_CONT=?, REM_SALANY=?, REM_SALOFI=?, REM_SALNUM=?, REM_NULA=?, REM_NULMTD=?, REM_NULUSU=?, REM_NULFEC=?, "+
            "REM_ENTFEC=?, REM_ENTDES=?, REM_ENTUSU=?, REM_ENTMTD=?, REM_ENTANY=?, REM_ENTOFI=?, REM_ENTNUM=? "+
    		" WHERE REM_OFANY=? AND REM_OFOFI=? AND REM_OFNUM=?";
   
    private String anoOficio=null;
    private String numeroOficio=null;
    private String oficinaOficio=null;
    private String fechaOficio="";
    private String descripcion="";

    private String nulo="";
    private String usuarioNulo="";
    private String motivosNulo="";
    private String fechaNulo="";

    private String anoSalida=null;
    private String numeroSalida=null;
    private String oficinaSalida=null;
    
    private String anoEntrada=null;
    private String numeroEntrada=null;
    private String oficinaEntrada=null;
    private String usuarioEntrada="";
    private String descartadoEntrada="";
    private String motivosDescarteEntrada="";
    private String fechaEntrada="";

    private String[] registros;
    
    
    public String[] getRegistros() {
		return registros;
	}



	public void setRegistros(String[] registros) {
		this.registros = registros;
	}



	public OficioRemisionBean() {
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



	public String getFechaEntrada() {
		return fechaEntrada;
	}



	public void setFechaEntrada(String fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}



	public String getAnoSalida() {
		return anoSalida;
	}



	public void setAnoSalida(String anoSalida) {
		this.anoSalida = anoSalida;
	}



	public String getNumeroSalida() {
		return numeroSalida;
	}



	public void setNumeroSalida(String numeroSalida) {
		this.numeroSalida = numeroSalida;
	}



	public String getOficinaSalida() {
		return oficinaSalida;
	}



	public void setOficinaSalida(String oficinaSalida) {
		this.oficinaSalida = oficinaSalida;
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



	public String getFechaOficio() {
		return fechaOficio;
	}



	public void setFechaOficio(String fechaOficio) {
		this.fechaOficio = fechaOficio;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getNulo() {
		return nulo;
	}



	public void setNulo(String nulo) {
		this.nulo = nulo;
	}



	public String getUsuarioNulo() {
		return usuarioNulo;
	}



	public void setUsuarioNulo(String usuarioNulo) {
		this.usuarioNulo = usuarioNulo;
	}



	public String getMotivosNulo() {
		return motivosNulo;
	}



	public void setMotivosNulo(String motivosNulo) {
		this.motivosNulo = motivosNulo;
	}



	public String getFechaNulo() {
		return fechaNulo;
	}



	public void setFechaNulo(String fechaNulo) {
		this.fechaNulo = fechaNulo;
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

            Calendar c2=Calendar.getInstance();
            c2.setTime(fechaSystem);

            
			fechaTest = dateF.parse(fechaOficio);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			DateFormat date1=new SimpleDateFormat("yyyyMMdd");
			
			int fzaanoe=cal.get(Calendar.YEAR);
			anoOficio=String.valueOf(fzaanoe);
			
			int dataofici=Integer.parseInt(date1.format(fechaTest));
			
            
            int numof=ToolsBD.RecogerNumeroOficio(conn, Integer.parseInt(anoOficio), oficinaOficio, errores);
            numeroOficio = String.valueOf(numof);
            
            /* Ejecutamos sentencias SQL */
            ms=conn.prepareStatement(SENTENCIA);
            
            ms.setInt(1,Integer.parseInt(anoOficio));
            ms.setInt(2,Integer.parseInt(oficinaOficio));
            ms.setInt(3,numof);
            ms.setInt(4, dataofici);
            ms.setString(5,descripcion);
            if(anoSalida!=null) {
            	ms.setInt(6,Integer.parseInt(anoSalida));	
            } else {
            	ms.setInt(6, 0);	
            }
            if(oficinaSalida!=null) {
            	ms.setInt(7,Integer.parseInt(oficinaSalida));
            } else {
            	ms.setInt(7, 0);	
            }
            if(numeroSalida!=null) {
            	ms.setInt(8,Integer.parseInt(numeroSalida));
            } else {
            	ms.setInt(8, 0);	
            }
            ms.setString(9,nulo);
            ms.setString(10,usuarioNulo);
            ms.setString(11,motivosNulo);
            ms.setInt(12,0);
            
            if(fechaEntrada!=null && !fechaEntrada.equals("")) {
            	fechaTest = dateF.parse(fechaEntrada);
                cal.setTime(fechaTest);
                                  
                int fzafent=Integer.parseInt(date1.format(fechaTest));

              	ms.setInt(13,fzafent);            	
            } else {
            	ms.setInt(13, 0);	
            }
            ms.setString(14,descartadoEntrada);
            ms.setString(15,usuarioEntrada);
            ms.setString(16,motivosDescarteEntrada);
            if(anoEntrada!=null) {
                ms.setInt(17,Integer.parseInt(anoEntrada));
            } else {
            	ms.setInt(17, 0);	
            }
            if(oficinaEntrada!=null) {
                ms.setInt(18,Integer.parseInt(oficinaEntrada));
            } else {
            	ms.setInt(18, 0);	
            }
            if(numeroEntrada!=null) {
                ms.setInt(19,Integer.parseInt(numeroEntrada));
            } else {
            	ms.setInt(19, 0);	
            }

            registroGrabado=ms.execute();
            registroGrabado=true;
            
           
            ms.close();
                        
            
    }
    /**
     * Actualitza la taula de gestió dels ofici de remissió.
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
//            if(fechaSalida!=null) {            	
//                fechaTest = dateF.parse(fechaSalida);
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
            if(fechaOficio!=null && !fechaOficio.equals("")) {
            	if (fechaOficio.matches("\\d{8}")) {
                  	ms.setInt(1,Integer.parseInt(fechaOficio));   
            	} else {
            		int fzafent = 0;
            	
            		try{
            			fechaTest = dateF.parse(fechaOficio);
            			Calendar cal=Calendar.getInstance();
            			cal.setTime(fechaTest);
            			DateFormat date1=new SimpleDateFormat("yyyyMMdd");
                                  
            			fzafent=Integer.parseInt(date1.format(fechaTest));
            		} catch (Exception e) {}
            		
            		ms.setInt(1,fzafent);   
            	}
            } else {
            	ms.setInt(1, 0);	
            }
            ms.setString(2, descripcion);
            ms.setInt(3,Integer.parseInt(anoSalida));
            ms.setInt(4,Integer.parseInt(oficinaSalida));
            ms.setInt(5,Integer.parseInt(numeroSalida));
            ms.setString(6,nulo);
            ms.setString(7,motivosNulo);
            ms.setString(8,usuarioNulo);
            if(fechaNulo!=null && !fechaNulo.equals("")) {
        		int fzafent = 0;
            	
        		try{
                	fechaTest = dateF.parse(fechaNulo);
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(fechaTest);
                    DateFormat date1=new SimpleDateFormat("yyyyMMdd");
                                      
                    fzafent=Integer.parseInt(date1.format(fechaTest));
        		} catch (Exception e) {}
        		
              	ms.setInt(9,fzafent);            	
            } else {
            	ms.setInt(9, 0);	
            }
            if(fechaEntrada!=null && !fechaEntrada.equals("")) {
        		int fzafent = 0;
            	
        		try{
                	fechaTest = dateF.parse(fechaEntrada);
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(fechaTest);
                    DateFormat date1=new SimpleDateFormat("yyyyMMdd");
                                      
                    fzafent=Integer.parseInt(date1.format(fechaTest));
        		} catch (Exception e) {}
        		
              	ms.setInt(10,fzafent);            	
            } else {
            	ms.setInt(10, 0);	
            }
            ms.setString(11,descartadoEntrada);
            ms.setString(12,usuarioEntrada);
            ms.setString(13,motivosDescarteEntrada);
            ms.setInt(14,anoEntrada!=null?Integer.parseInt(anoEntrada):0);
            ms.setInt(15,oficinaEntrada!=null?Integer.parseInt(oficinaEntrada):0);
            ms.setInt(16,numeroEntrada!=null?Integer.parseInt(numeroEntrada):0);
            ms.setInt(17,anoOficio!=null?Integer.parseInt(anoOficio):0);
            ms.setInt(18,oficinaOficio!=null?Integer.parseInt(oficinaOficio):0);
            ms.setInt(19,numeroOficio!=null?Integer.parseInt(numeroOficio):0);
            
            
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
            throw new RemoteException("Error inesperat, no s'ha actualitzat la taula de gestió dels ofici de remissió.", ex);
        } finally {
        	ToolsBD.closeConn(conn, ms, null);
        }
    }
    
    
    /**
     * Actualitza el registre d'entrada
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public void anular() throws SQLException, ClassNotFoundException, Exception {
        Connection conn = null;
        PreparedStatement ms = null;
        
        try {
			/* Recuperamos numero de entrada */
			conn=ToolsBD.getConn();
			conn.setAutoCommit(false);

            /* Ejecutamos sentencias SQL */
		    String sentencia_delete="DELETE FROM BZOFRENT " +
    			" WHERE REN_OFANY=? AND REN_OFOFI=? AND REN_OFNUM=?";

            ms=conn.prepareStatement(sentencia_delete);
            ms.setInt(1,anoOficio!=null?Integer.parseInt(anoOficio):0);
            ms.setInt(2,oficinaOficio!=null?Integer.parseInt(oficinaOficio):0);
            ms.setInt(3,numeroOficio!=null?Integer.parseInt(numeroOficio):0);
            
            
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
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
        try {
            conn=ToolsBD.getConn();

            String sentenciaSql="SELECT * FROM BZOFREM " +
                    "WHERE REM_OFANY=? AND REM_OFOFI=? AND REM_OFNUM=?";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setInt(1,Integer.parseInt(anoOficio));
            ps.setInt(2,Integer.parseInt(oficinaOficio));
            ps.setInt(3,Integer.parseInt(numeroOficio));
            rs=ps.executeQuery();
            if (rs.next()) {
            	/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            	
                leidos=true;
                anoOficio=String.valueOf(rs.getInt("REM_OFANY"));
                numeroOficio=String.valueOf(rs.getInt("REM_OFNUM"));
                oficinaOficio=String.valueOf(rs.getInt("REM_OFOFI"));
				String fechaO=String.valueOf(rs.getInt("REM_OFFEC"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaO);
					fechaOficio=(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					fechaOficio=fechaO;
				}

				descripcion = rs.getString("REM_CONT");
                
                anoSalida=String.valueOf(rs.getInt("REM_SALANY"));
                numeroSalida=String.valueOf(rs.getInt("REM_SALNUM"));
                oficinaSalida=String.valueOf(rs.getInt("REM_SALOFI"));

                nulo=rs.getString("REM_NULA");
                motivosNulo=rs.getString("REM_NULMTD");
                usuarioNulo=rs.getString("REM_NULUSU");
				String fechaN=String.valueOf(rs.getInt("REM_NULFEC"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaN);
					fechaNulo=(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					fechaNulo=fechaN;
				}

                anoEntrada=String.valueOf(rs.getInt("REM_ENTANY"));
                numeroEntrada=String.valueOf(rs.getInt("REM_ENTNUM"));
                oficinaEntrada=String.valueOf(rs.getInt("REM_ENTOFI"));
                descartadoEntrada=rs.getString("REM_ENTDES");
                motivosDescarteEntrada=rs.getString("REM_ENTMTD");
                usuarioEntrada=rs.getString("REM_ENTUSU");
				String fechaE=String.valueOf(rs.getInt("REM_ENTFEC"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaE);
					fechaEntrada=(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					fechaEntrada=fechaE;
				}


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