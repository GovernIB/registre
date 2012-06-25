package es.caib.regweb.logic.ejb;


import java.util.*;
import java.sql.SQLException;
import java.text.*;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;

import es.caib.regweb.logic.helper.Conf;
import es.caib.regweb.logic.helper.Helper;

//import java.rmi.*;

import javax.ejb.*;
//import javax.naming.NamingException;

import java.lang.reflect.InvocationTargetException;

import es.caib.regweb.logic.helper.ParametrosLineaOficioRemision;
import es.caib.regweb.logic.helper.ParametrosRegistroEntrada;
import es.caib.regweb.logic.helper.ParametrosRegistroModificado;
import es.caib.regweb.logic.interfaces.LineaOficioRemisionFacade;
import es.caib.regweb.logic.interfaces.RegistroModificadoEntradaFacade;
import es.caib.regweb.logic.util.LineaOficioRemisionFacadeUtil;
import es.caib.regweb.logic.util.RegistroModificadoEntradaFacadeUtil;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;
import es.caib.regweb.logic.interfaces.AdminFacade;
import es.caib.regweb.logic.util.AdminFacadeUtil;
import es.caib.regweb.logic.helper.ParametrosRegistroPublicadoEntrada;
import es.caib.regweb.model.LogEntradaLopd;
import es.caib.regweb.model.LogEntradaLopdId;
import es.caib.regweb.logic.interfaces.RegistroPublicadoEntradaFacade;
import es.caib.regweb.logic.util.RegistroPublicadoEntradaFacadeUtil;
import es.caib.regweb.logic.interfaces.LocalitzadorsDocsElectronicsFacade;
import es.caib.regweb.logic.util.LocalitzadorsDocsElectronicsFacadeUtil;

import org.apache.log4j.Logger;


/**
 * SessionBean per a manteniment de registres d'entrada
 *
 * @ejb.bean
 *  name="logic/RegistroEntradaFacade"
 *  jndi-name="es.caib.regweb.logic.RegistroEntradaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class RegistroEntradaFacadeEJB extends HibernateEJB {
    
	private static final long serialVersionUID = 3L;
	private static int CORRECTE = 1;
	private static int FECHA_NO_LOGICA = 2;
	private static int FECHA_POSTERIOR_ACTUAL = 3;
	private static int FECHA_DEMASIADO_ANTIGUA = 4;
	
    private Logger log = Logger.getLogger(this.getClass());
    
    /**
     * Texto guardado como comentario en el descarte de los oficios
     */
    static private String DESCARTE_AUTOMATICO =  "Ofici descartat automàticament.";
    
    /**
     * @ ejb.env-entry
     *   name="registro.entrada.view.infoBOIB"
     *   type="java.lang.String"
     *   value="${registro.entrada.view.infoBOIB}"
     */
	 //String infoBOIB;
	 
    /**
     * @ ejb.env-entry
     *   name="registro.oficinaBOIB"
     *   type="java.lang.String"
     *   value="${registro.oficinaBOIB}"
     */
	 //String oficinaBOIB;
		 
    /**
     * @ ejb.env-entry
     *   name="registro.codiOrganismeBOIB"
     *   type="java.lang.String"
     *   value="${registro.codiOrganismeBOIB}"
     */
	 //String codiOrganismeBOIB;	 

    private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
    private Date fechaTest=null;
    private DateFormat horaF=new SimpleDateFormat("HH:mm");
   
    private String SENTENCIA="INSERT INTO BZENTRA (" +
            "FZAANOEN, FZANUMEN, FZACAGCO, FZAFDOCU, FZAREMIT, FZACONEN, FZACTIPE, FZACEDIE, FZAENULA,"+
            "FZAPROCE, FZAFENTR, FZACTAGG, FZACAGGE, FZACORGA, FZAFACTU, FZACENTI, FZANENTI, FZAHORA,"+
            "FZACIDIO, FZACONE2, FZANLOC, FZAALOC, FZANDIS, FZAFSIS, FZAHSIS, FZACUSU, FZACIDI,EMAILREMITENT"+
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


    /**
     * Actualitza el registre d'entrada
     * @throws EJBException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosRegistroEntrada actualizar(ParametrosRegistroEntrada param) throws EJBException {

        String dataentrada=param.getDataEntrada();
        String hora=param.getHora();
        String oficina=param.getOficina();
        String oficinafisica=param.getOficinafisica();
        String data=param.getData();
        String tipo=param.getTipo();
        String idioma=param.getIdioma();
        String entidad2=param.getEntidad2();
        String altres=param.getAltres();
        String balears=param.getBalears();
        String fora=param.getFora();
        String salida1=param.getSalida1();
        String salida2=param.getSalida2();
        String destinatari=param.getDestinatari();
        String idioex=param.getIdioex();
        String disquet=param.getDisquet();
        String comentario=param.getComentario();
        String usuario=param.getUsuario();
        String correo=param.getCorreo();
        String registroAnulado=param.getRegistroAnulado();
        String motivo=param.getMotivo();
        String entidad1Nuevo=param.getEntidad1Nuevo();
        String entidad2Nuevo=param.getEntidad2Nuevo();
        String altresNuevo=param.getAltresNuevo();
        String comentarioNuevo=param.getComentarioNuevo();
        String municipi060=param.getMunicipi060();
        Hashtable errores=param.getErrores();           
        String anoEntrada=param.getAnoEntrada();
        String numeroEntrada=param.getNumeroEntrada();
        String entidadCastellano=param.getEntidadCastellano();
        ParametrosRegistroPublicadoEntrada paramRegPubEnt = param.getParamRegPubEnt();
        String emailRemitent = param.getEmailRemitent();
      
        boolean validado=false;


		Session session = getSession();
		SQLQuery ms = null;
		
        if (!validado) {
            param=validar(param);
            validado=param.getValidado();
        }
        if (!validado) {
            throw new EJBException("No s'ha realitzat la validaci\u00f3 de les dades del registre ");
        }
        
        boolean registroActualizado=false;
        
        // Cogemos los datos sobre el BOIB del fichero SAR de la aplicación
        String codiOrganismeBOIB = Conf.get("codiOficinaBOIB", "1141");
        String oficinaBOIB = Conf.get("oficinaBOIB", "32");
        String infoBOIB = Conf.get("infoBOIB", "false");
        try {
	        //Comprobamos si hay que actualizar datos publicación BOIB
	        if(infoBOIB.equals("true") && oficina.equals(oficinaBOIB)){
	        	RegistroPublicadoEntradaFacade regpubent = RegistroPublicadoEntradaFacadeUtil.getHome().create();
	        	if(paramRegPubEnt!=null){
			        if(destinatari.equals(codiOrganismeBOIB)){
			        	if(paramRegPubEnt.estaVacio()){
			        		regpubent.borrar(paramRegPubEnt);
			        	}else{
			        		regpubent.grabar(paramRegPubEnt);
			        	}
			        }else{
			        		regpubent.borrar(paramRegPubEnt);
			        }
	        	}
	        }
               
            /* Descomponemos el año de la data de entrada, FZAANOEN y preparamos campo
             FZAFENT en formato aaaammdd */
            int fzaanoe;
            
            fechaTest = dateF.parse(dataentrada);
            Calendar cal=Calendar.getInstance();
            cal.setTime(fechaTest);
            
            //DateFormat date1=new SimpleDateFormat("yyyyMMdd");
            
            fzaanoe=Integer.parseInt(anoEntrada);
            
            int fzafent=Helper.convierteStringFechaAIntFecha(dataentrada);
            	
            
            /* Recuperamos numero de entrada */

            
            int fzanume=Integer.parseInt(numeroEntrada);
            
            /* Oficina, fzacagc */
            int fzacagc=Integer.parseInt(oficina);
            int off_codi=0;
            try {
            	off_codi=Integer.parseInt(oficinafisica);
            } catch (Exception e) {}
            
            /* Fecha documento en un campo en formato aaaammdd, fzafdoc */
            int fzafdoc= Helper.convierteStringFechaAIntFecha(data);
            
            /* Si el idioma del extracte es 1=castellano entonces el extracte lo guardamos
               en el campo FZACONE, si es 2=catalan lo guardamos en FZACONE2 */
            String fzacone, fzacone2;
            if (idioex.equals("1")) {
                fzacone=comentario;
                fzacone2="";
            } else {
                fzacone="";
                fzacone2=comentario;
            }
            
            /* Preparamos los campos de Procedencia geografica, tipo de agrupacion geografica
               y codigo de agrupacion geografica */
            String fzaproce;
            int fzactagg, fzacagge;
            if (fora.equals("")) {
                fzactagg=90;
                fzacagge=Integer.parseInt(balears);
                fzaproce="";
            } else {
                fzaproce=fora;
                fzactagg=0;
                fzacagge=0;
            }
            
            /* Codigo de Organismo */
            int fzacorg=Integer.parseInt(destinatari);
            
            /* Numero de entidad, fzanent */
            int fzanent;
            String fzacent;
            if (altres.equals("")) {
                altres="";
                fzanent=Integer.parseInt(entidad2);
                fzacent=entidadCastellano;
            } else {
                fzanent=0;
                fzacent="";
            }
            
            /* Idioma del extracto, fzacidi */
            int fzacidi=Integer.parseInt(idioex);
            
            /* Hora del documento, fzahora mmss */
            /*horaTest=horaF.parse(hora);
            cal.setTime(horaTest);
            DateFormat hhmm=new SimpleDateFormat("HHmm");
            int fzahora=Integer.parseInt(hhmm.format(horaTest));*/
            int fzahora=Helper.convierteStringHoraAIntHora(hora);
            
            /* Numero localizador y año localizador, fzanloc y fzaaloc */
            if (salida1.equals("")) {salida1="0";}
            if (salida2.equals("")) {salida2="0";}
            int fzanloc=Integer.parseInt(salida1);
            int fzaaloc=Integer.parseInt(salida2);
            
            /* Numero de disquette, fzandis */
            if (disquet.equals("")) {disquet="0";}
            int fzandis=Integer.parseInt(disquet);
            
            /* Actualizamos el numero de disquete */
            if (fzandis>0){Helper.actualizaDisqueteEntrada(session, fzandis, oficina, anoEntrada, errores);}
            
            /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
			//int fzahsis=Helper.obtenerHoraSistemaHHmmssS();
			int fzafsis=Helper.obtenerFechaSistemayyyyMMdd();
			
            String deleteOfifis="DELETE FROM BZENTOFF WHERE FOEANOEN=? AND FOENUMEN=? AND FOECAGCO=?";
            ms=session.createSQLQuery(deleteOfifis);
            ms.setInteger(0,fzaanoe);
            ms.setInteger(1,fzanume);
            ms.setInteger(2,fzacagc);
    		ms.executeUpdate();

    		String insertOfifis="INSERT INTO BZENTOFF (FOEANOEN, FOENUMEN, FOECAGCO, OFE_CODI)" +
            "VALUES (?,?,?,?)";
            ms=session.createSQLQuery(insertOfifis);
            ms.setInteger(0,fzaanoe);
            ms.setInteger(1,fzanume);
            ms.setInteger(2,fzacagc);
    		ms.setInteger(3,off_codi);
    		ms.executeUpdate();
    
            // Si hay motivo, generamos objeto Modificacion
            boolean modificado=false;           
            if (!motivo.equals("")) {
                RegistroModificadoEntradaFacade regmod = RegistroModificadoEntradaFacadeUtil.getHome().create();
                ParametrosRegistroModificado registroModificado = new ParametrosRegistroModificado();
                
                registroModificado.setAnoEntrada(fzaanoe);
                registroModificado.setOficina(fzacagc);
                if (!entidad1Nuevo.trim().equals("")) {
                    if (entidad2Nuevo.equals("")) {entidad2Nuevo="0";}
                }
                int fzanentNuevo;
                String fzacentNuevo;
                if (altresNuevo.trim().equals("")) {
                    altresNuevo="";
                    fzanentNuevo=Integer.parseInt(entidad2Nuevo);
                    //fzacentNuevo=entidad2Nuevo;
                    fzacentNuevo=convierteEntidadCastellano(entidad1Nuevo, entidad2Nuevo);
                } else {
                    fzanentNuevo=0;
                    fzacentNuevo="";
                }
                if (!fzacentNuevo.equals(fzacent) || fzanentNuevo!=fzanent) {
                    registroModificado.setEntidad2(fzanentNuevo);
                    registroModificado.setEntidad1(fzacentNuevo);
                } else {
                    registroModificado.setEntidad2(0);
                    registroModificado.setEntidad1("");
                }
                if (!comentarioNuevo.trim().equals(comentario.trim())) {
                    registroModificado.setExtracto(comentarioNuevo);
                } else {
                    registroModificado.setExtracto("");
                }
                registroModificado.setUsuarioModificacion(usuario.toUpperCase());
                registroModificado.setNumeroRegistro(fzanume);
                if (altresNuevo.equals(altres)) {
                    registroModificado.setRemitente("");
                } else {
                    registroModificado.setRemitente(altresNuevo);
                }
                registroModificado.setMotivo(motivo);
                modificado=regmod.generarModificacion(registroModificado, session);
                log.debug("Eliminam regmod");
                regmod.remove();
            }
            if ((modificado && !motivo.equals("")) || motivo.equals(""))  {
                /* Ejecutamos sentencias SQL */
                ms=session.createSQLQuery("UPDATE BZENTRA SET FZAFDOCU=?, FZAREMIT=?, FZACONEN=?, FZACTIPE=?, " +
                        "FZACEDIE=?, FZAENULA=?, FZAPROCE=?, FZAFENTR=?, FZACTAGG=?, FZACAGGE=?, FZACORGA=?, " +
                        "FZACENTI=?, FZANENTI=?, FZAHORA=?, FZACIDIO=?, FZACONE2=?, FZANLOC=?, FZAALOC=?, FZANDIS=?, " +
                        "FZACUSU=?, FZACIDI=?, EMAILREMITENT = ? WHERE FZAANOEN=? AND FZANUMEN=? AND FZACAGCO=?");
                ms.setInteger(0,fzafdoc);
                ms.setString(1,(altres.length()>30) ? altres.substring(0,30) : altres); // 30 pos.
                ms.setString(2,(fzacone.length()>160) ? fzacone.substring(0,160) : fzacone); // 160 pos.
                ms.setString(3,(tipo.length()>2) ? tipo.substring(0,1) : tipo);  // 2 pos.
                ms.setString(4,"N");
                ms.setString(5,(registroAnulado.length()>1) ? registroAnulado.substring(0,1) : registroAnulado);
                ms.setString(6,(fzaproce.length()>25) ? fzaproce.substring(0,25) : fzaproce); // 25 pos.
                ms.setInteger(7,fzafent);
                ms.setInteger(8,fzactagg);
                ms.setInteger(9,fzacagge);
                ms.setInteger(10,fzacorg);   
                ms.setString(11,(fzacent.length()>7) ? fzacent.substring(0,8) : fzacent); // 7 pos.
                ms.setInteger(12,fzanent);
                ms.setInteger(13,fzahora);
                ms.setInteger(14,fzacidi);
                ms.setString(15,(fzacone2.length()>160) ? fzacone2.substring(0,160) : fzacone2); // 160 pos.
                ms.setInteger(16,fzanloc);
                ms.setInteger(17,fzaaloc);
                ms.setInteger(18,fzandis);
                ms.setString(19,(usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()); // 10 pos.
                ms.setString(20,idioma);
                ms.setString(21, emailRemitent);
                // Clave del fichero
                ms.setInteger(22,fzaanoe);
                ms.setInteger(23,fzanume);
                ms.setInteger(24,fzacagc);
                
                int afectados=ms.executeUpdate();
                if (afectados>0){
                    registroActualizado=true;
                } else {
                    registroActualizado=false;
                }
                
                // desacoplamiento cobol
                String remitente="";
                if (!altres.trim().equals("")) {
                    remitente=altres;
                } else {
                    ValoresFacade valor = ValoresFacadeUtil.getHome().create();
                    remitente=valor.recuperaRemitenteCastellano(fzacent, fzanent+"");
                }
                
                try {
                    Class t = Class.forName("es.caib.regweb.module.PluginHook");

                    Class [] partypes = {
                        String.class , Integer.class, Integer.class, Integer.class, Integer.class, String.class,
                     	String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class, String.class 
                    };

                    Object [] params = {
                        "M", new Integer(fzaanoe), new Integer(fzanume), new Integer(fzacagc), new Integer(fzafdoc), remitente, 
                        comentario, tipo, new Integer(fzafent), new Integer(fzacagge), fzaproce, new Integer(fzacorg), idioma, correo
                    };

                    java.lang.reflect.Method metodo = t.getMethod("entrada", partypes);
                    metodo.invoke(null, params);

                    //t.entrada("M", fzaanoe, fzanume, fzacagc, fzafdoc, remitente, comentario, tipo, fzafent, fzacagge, fzaproce, fzacorg, idioma);

                // ignorar excepciones en caso de que no se encuentre la clase o el método, pero pasar las excepciones que genere el metodo invocado
                } catch (IllegalAccessException iae) { 
                } catch (IllegalArgumentException iae) { 
                } catch (InvocationTargetException ite) {
                } catch (NullPointerException npe) { 
                } catch (ExceptionInInitializerError eiie) {
                } catch (NoSuchMethodException nsme) {
                } catch (SecurityException se) {
                } catch (LinkageError le) {
                } catch (ClassNotFoundException le) {
                }  
                
                /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
                int horamili=Helper.obtenerHoraSistemaHHmmssSS();
                /* Nota: l'update es fa tant si hi ha alguna dada diferent com si no. 
                 * Si hi ha canvis a dades que no estan sota el control del visat, s'actualitzen ara.
                 * Les dades sota el control del visat s'actualitzen en visar. */
                logLopdBZENTRA("UPDATE", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()
        				, fzafsis, horamili, fzanume, fzaanoe, fzacagc);
            } else {
                registroActualizado=false;
                errores.put("","Error inesperat, no s'ha modificat el registre");
                throw new EJBException("Error inesperat, no s'ha modifcat el registre");
            }
            
            log.debug("Municipi codi: "+municipi060);
            if(municipi060.equals("000"))
            	eliminar060(param);
            else
            	if(!municipi060.equals(""))
            		actualizar060(param);

            session.flush();
            log.debug("Actualizar registro de entrada ejecutado correctamente.");
        }catch(HibernateException he){
        	// Tratamos las excepciones generadas por Hibernate
        	log.error("Error: S'ha generat una Hibernate Exception " + he.getMessage());
        	registroActualizado=false;
        	errores.put("","Error inesperat, no s'ha desat el registre"+": "+he.getClass()+"->"+he.getMessage());
        	throw new EJBException("Error: S'ha generat una Hibernate Exception. No s'ha desat el registre. " + he.getMessage());
        } catch(ClassNotFoundException cnfe){
        	log.error("Error: S'ha generat una ClassNotFound Exception. No s'ha desat el registre. " + cnfe.getMessage());
        	registroActualizado=false;
        	errores.put("","Error inesperat, no s'ha desat el registre"+": "+cnfe.getClass()+"->"+cnfe.getMessage());
        	throw new EJBException("Error: S'ha generat una ClassNotFound Exception" + cnfe.getMessage());
        } catch (Exception ex) {
        	log.error(usuario+": Excepci\u00f3: "+ex.getMessage());
            registroActualizado=false;
            errores.put("","Error inesperat, no s'ha desat el registre"+": "+ex.getClass()+"->"+ex.getMessage());
            throw new EJBException("Error inesperat, no s'ha modifcat el registre", ex);
        } finally {
        	param.setregistroActualizado(registroActualizado);
			close(session);
        }
        log.debug("Fin");
        return param;
    }
    
    
    /* Grabamos registro si las validaciones son correctas */
    
    /**
     * Actualitza el registre d'entrada 060
     * @throws HibernateException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    private void actualizar060(ParametrosRegistroEntrada param) throws Exception {      
        String oficina=param.getOficina();   
        String municipi060=param.getMunicipi060();      
        String numeroDocumentosRegistro060=param.getNumeroDocumentosRegistro060();               
        String anoEntrada=param.getAnoEntrada();
        String numeroEntrada=param.getNumeroEntrada();
		Session session = getSession();
		SQLQuery q = null;
        int actualizados = 0;
        
        try {

            String sentenciaHql="UPDATE BZENTRA060 SET ENT_CODIMUN=?, ENT_NUMREG=?  WHERE ENT_ANY=? AND ENT_OFI=? AND ENT_NUM=?";
            q=session.createSQLQuery(sentenciaHql);
            q.setString(0,municipi060);
            q.setInteger(1,Integer.valueOf(numeroDocumentosRegistro060));
            q.setInteger(2,Integer.valueOf(anoEntrada));
            q.setInteger(3,Integer.valueOf(oficina));
            q.setInteger(4,Integer.valueOf(numeroEntrada));
           
            actualizados=q.executeUpdate();
            if (actualizados!=0) {
            	log.debug("Municipi 060 actualitzat:"+municipi060);
            }else{
            	cargarMunicipio060(session, Integer.parseInt(anoEntrada), Integer.parseInt(numeroEntrada), Integer.parseInt(oficina), municipi060, Integer.parseInt(numeroDocumentosRegistro060));
            	log.debug("Municipi060 creat.");
            }
        }catch(Exception e ){
        	  throw e;
        }finally{
			close(session);
        }
    }

    
    /**
	* Anula el registro de entrada si las validaciones son correctas
	* 
	* @param registro Identificador del registro afectado
	* @param hay_que_anular Si es verdadero anulamos el registro, si es falso quitamos la anulación
	* 
	* @return verdadero si todo va bien, falso en caso contrario.
	* 
	* @ejb.interface-method
	* @ejb.permission unchecked="true"
	*/
	public boolean anular(ParametrosRegistroEntrada registro, boolean hay_que_anular) throws EJBException{
		Session session = getSession();
		//ScrollableResults rs=null;
		Query q = null;
	
		boolean rtdo= false;
		String estadoAnulacionRegistro = ((hay_que_anular)?"S":"");
		String usuario = registro.getUsuario();
		String oficina = registro.getOficina();
		String numeroRegistro = registro.getNumeroEntrada();
		String anyo = registro.getAnoEntrada();
	
		try {
			if ( tienePermisoEntrada( usuario,  oficina,  session )){
	
				String sentenciaHql= "update Entrada " +
									 "set nula=?" +
									 "where  ( id.anyo = ?  and id.numero = ? and id.oficina = ?)";
				q=session.createQuery(sentenciaHql);
				//Campo a modificar
				q.setString(0,estadoAnulacionRegistro);
				//Claves primarias
				q.setString(1,anyo);
				q.setString(2,numeroRegistro);
				q.setString(3,oficina);
	
				if(q.executeUpdate()>0){    			
					log.debug("Registro de entrada:"+registro.getReferenciaRegistro()+((hay_que_anular)?". Anulado correctamente.":". Quitada la anulación correctamente."));
					rtdo = true;
				}else{
					log.debug("Registro de entrada:"+registro.getReferenciaRegistro()+" no encontrado.");
				}
	
				session.flush();
			}else{
				log.info("INFO: L'usuari " +usuario+" no te permisos per anular registres de l'oficina "+ oficina);
			}
		}catch(HibernateException he){
			// Tratamos las excepciones generadas por Hibernate
			log.error("Error: S'ha generat una Hibernate Exception " + he.getMessage());
			throw new EJBException("Error: S'ha generat una Hibernate Exception. No s'ha anulat el registre "+registro.getReferenciaRegistro()+". " + he.getMessage());
		} catch (Exception ex) {
			log.error(usuario+": Excepci\u00f3: "+ex.getMessage());
			throw new EJBException("Error inesperat: No s'ha anulat el registre "+registro.getReferenciaRegistro(), ex);
		} finally {
			close(session);
		}
		return rtdo;
	}
   
    /**
     * @throws HibernateException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    private void cargarMunicipio060(Session session, int fzaanoe, int fzanume, int fzacagc, String codimun,int numreg_060) throws HibernateException, ClassNotFoundException, Exception {
	   SQLQuery ms = null;
	   try{
		   String insertBZNCORR="INSERT INTO BZENTRA060 (ENT_ANY, ENT_OFI, ENT_NUM, ENT_CODIMUN,ENT_NUMREG)" +
           "VALUES (?,?,?,?,?)";
		   ms=session.createSQLQuery(insertBZNCORR);
		   ms.setInteger(0, fzaanoe);
		   ms.setInteger(1,fzacagc);
		   ms.setInteger(2,fzanume);
		   ms.setString(3, codimun);
		   ms.setInteger(4,numreg_060);
		   ms.executeUpdate();
	   }catch(Exception e){
		   log.error("Error dentro del metodo \"cargarMunicipio060\".", e);
		   throw e;
	   }finally{
	   }
   }

    private String convierteEntidadCastellano(String entidadCatalan, String entidad2Nuevo) {
    	Session sessio = getSession();
        String eCastellano="";
        ScrollableResults rs = null;
        SQLQuery q1 = null;
        try {
            String sentenciaHql="SELECT FZGCENTI FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
            q1=sessio.createSQLQuery(sentenciaHql);
            q1.addScalar("FZGCENTI", Hibernate.STRING);
            q1.setString(0,entidadCatalan);
            q1.setInteger(1,Integer.parseInt(entidad2Nuevo));
            rs=q1.scroll();
            
            if (rs.next()) {
                eCastellano=rs.getString(0);
            } else {
                eCastellano="";
            }
        } catch (Exception e) {
            eCastellano="";
        } finally {
            if (rs!=null) rs.close();
            close(sessio);
        }
        return eCastellano;
    }

    /**
     * Este método hace que un registro de entrada no genere un oficio de remisión
     * 
     * @param anoEntrada
     * @param numeroEntrada
     * @param codiOficina
     * @param usuario
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    private void descartarOficio(int anoEntrada, int numeroEntrada, int codiOficina, String usuario) throws Exception {
    	LineaOficioRemisionFacade lin = LineaOficioRemisionFacadeUtil.getHome().create();
    	ParametrosLineaOficioRemision param = new ParametrosLineaOficioRemision();

    	param.setAnoEntrada(String.valueOf(anoEntrada));
    	param.setOficinaEntrada(String.valueOf(codiOficina));
    	param.setNumeroEntrada(String.valueOf(numeroEntrada));
    	param.setDescartadoEntrada("S");
    	param.setMotivosDescarteEntrada(DESCARTE_AUTOMATICO);
    	param.setUsuarioEntrada(usuario);
    	param.setAnoOficio(null);
    	param.setOficinaOficio(null);
    	param.setNumeroOficio(null);
    	lin.grabar(param);	
    	log.debug("Oficio descartado");
    }
  
    /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Elimina el registre d'entrada 060
     * @throws EJBException
     */
    private void eliminar060(ParametrosRegistroEntrada param) throws Exception {
        String oficina=param.getOficina();
        String municipi060=param.getMunicipi060();
        String anoEntrada=param.getAnoEntrada();
        String numeroEntrada=param.getNumeroEntrada();
		Session session = getSession();
		SQLQuery q = null;        
        int actualizados = 0;
        
        try {

            String sentenciaHql="DELETE FROM BZENTRA060  WHERE ENT_ANY=? AND ENT_OFI=? AND ENT_NUM=?";
            q=session.createSQLQuery(sentenciaHql);
            q.setInteger(0,Integer.valueOf(anoEntrada));
            q.setInteger(1,Integer.valueOf(oficina));
            q.setInteger(2,Integer.valueOf(numeroEntrada));
            actualizados=q.executeUpdate();
            if (actualizados!=0) {
            	log.debug("Municipi 060 eliminat:"+municipi060);
            }else{
            	log.error("Res per eliminar.");
            }
        }catch(Exception e ){
        	  throw new EJBException(e);
        }finally{
			close(session);
        }
    }
    
    /**
     * Comprueba si el asiento registral tienen localizadores de documentos electrónicos asociados
     * 
	 * @throws EJBException
	 * 
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public boolean ExisteListaLocalizadoresDoc(int anoEntrada, int numeroEntrada, int oficina) throws EJBException {		
		LocalitzadorsDocsElectronicsFacade regLocDocElect;
		boolean rtdo = false;
		try {
			regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
			rtdo = regLocDocElect.ExisteListaLocalizadoresDocEntrada(anoEntrada,numeroEntrada,oficina);
		} catch (Exception e) {
			throw new EJBException("Error al consultar els localitzadors de documentes del asiento registral.",e);
		}
		return rtdo;
	}
    
    /**
     * @throws EJBException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosRegistroEntrada grabar(ParametrosRegistroEntrada param) throws EJBException {
        
        String dataentrada=param.getDataEntrada();
        String hora=param.getHora();
        String oficina=param.getOficina();
        String oficinafisica=param.getOficinafisica();
        String data=param.getData();
        String tipo=param.getTipo();
        String idioma=param.getIdioma();
        String entidad2=param.getEntidad2();
        String altres=param.getAltres();
        String balears=param.getBalears();
        String fora=param.getFora();
        String salida1=param.getSalida1();
        String salida2=param.getSalida2();
        String destinatari=param.getDestinatari();
        String idioex=param.getIdioex();
        String disquet=param.getDisquet();
        String comentario=param.getComentario();
        String usuario=param.getUsuario();
        String correo=param.getCorreo();
        String municipi060=param.getMunicipi060();
        String numeroDocumentosRegistro060=(param.getNumeroDocumentosRegistro060()==null)?"1":param.getNumeroDocumentosRegistro060();
        Hashtable errores=param.getErrores();
        String entidadCastellano=param.getEntidadCastellano();
   
        String emailRemitente=param.getEmailRemitent();
        String locDocs=param.getLocalitzadorsDocs();       
        boolean validado=false;
        boolean registroGrabado=false;        
		Session session = getSession();

        try {
        	LocalitzadorsDocsElectronicsFacade regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
            // Ejecuta algoritmo de registro
            /* Grabamos registro si las validaciones son correctas */
            SQLQuery ms = null;
            
            if (!validado) {
                param=validar(param);
                validado=param.getValidado();
            }
            if (!validado) {
                throw new Exception("No s'ha realitzat la validaci\u00f3 de les dades del registre ");
            }

            Calendar cal=Calendar.getInstance();
            cal.setTime(dateF.parse(dataentrada));
            int anoEntrada =cal.get(Calendar.YEAR);
            param.setAnoEntrada(String.valueOf(anoEntrada));

            int fzafent= Helper.convierteStringFechaAIntFecha(dataentrada);

            /* Recuperamos numero de entrada */
            int numeroEntrada=Helper.recogerNumeroEntrada(session, anoEntrada, oficina, errores);
            param.setNumeroEntrada(Integer.toString(numeroEntrada));
            
            /* Oficina, fzacagc */
            int codiOficina=Integer.parseInt(oficina);
            int off_codi=Integer.parseInt(oficinafisica);

            /* Fecha documento en un campo en formato aaaammdd, fzafdoc */
            int dataDocument=Helper.convierteStringFechaAIntFecha(data);

            /* Si el idioma del extracte es 1=castellano entonces el extracte lo guardamos
               en el campo FZACONE, si es 2=catalan lo guardamos en FZACONE2 */
            String fzacone, fzacone2;
            if (idioex.equals("1")) {
                fzacone=comentario;
                fzacone2="";
            } else {
                fzacone="";
                fzacone2=comentario;
            }

            /* Preparamos los campos de Procedencia geografica, tipo de agrupacion geografica
               y codigo de agrupacion geografica */
            String fzaproce;
            int fzactagg, fzacagge;
            if (fora.equals("")) {
                fzactagg=90;
            	fzacagge=Integer.parseInt(balears);
                fzaproce="";
            } else {
                fzaproce=fora;
                fzactagg=0;
                fzacagge=0;
            }

            /* Fecha de actualizacion a ceros */
            int ceros=0;

            /* Codigo de Organismo */
            int codiOrganismo=Integer.parseInt(destinatari);

            /* Numero de entidad, fzanent */
            int fzanent;
            String fzacent;
            if (altres.equals("")) {
                altres="";
                fzanent=Integer.parseInt(entidad2);
                fzacent=entidadCastellano;
            } else {
                fzanent=0;
                fzacent="";
            }

            /* Idioma del extracto, fzacidi */
            int fzacidi=Integer.parseInt(idioex);

            /* Hora del documento, fzahora mmss */
            int fzahora=Helper.convierteStringHoraAIntHora(hora);
            
            /* Numero localizador y año localizador, fzanloc y fzaaloc */
            int fzanloc=Integer.parseInt((salida1.equals(""))?"0":salida1);
            int fzaaloc=Integer.parseInt((salida2.equals(""))?"0":salida2);

            /* Numero de disquette, fzandis */
            int fzandis=Integer.parseInt((disquet.equals(""))?"0":disquet);
            
            /* Actualizamos el numero de disquete */
            if (fzandis>0){Helper.actualizaDisqueteEntrada(session, fzandis, oficina, param.getAnoEntrada(), errores);}

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
            ms=session.createSQLQuery(SENTENCIA);

            ms.setInteger(0,anoEntrada);
            ms.setInteger(1,numeroEntrada);
            ms.setInteger(2,codiOficina);
            ms.setInteger(3,dataDocument);
            ms.setString(4,(altres.length()>30) ? altres.substring(0,30) : altres); // 30 pos.
            ms.setString(5,(fzacone.length()>160) ? fzacone.substring(0,160) : fzacone); // 160 pos.
            ms.setString(6,(tipo.length()>2) ? tipo.substring(0,2) : tipo);  // 2 pos.
            ms.setString(7,"N");
            ms.setString(8,"");
            ms.setString(9,(fzaproce.length()>25) ? fzaproce.substring(0,25) : fzaproce); // 25 pos.
            ms.setInteger(10,fzafent);
            ms.setInteger(11,fzactagg);
            ms.setInteger(12,fzacagge);
            ms.setInteger(13,codiOrganismo);
            ms.setInteger(14,ceros);
            ms.setString(15,(fzacent.length()>7) ? fzacent.substring(0,7) : fzacent); // 7 pos.
            ms.setInteger(16,fzanent);
            ms.setInteger(17,fzahora);
            ms.setInteger(18,fzacidi);
            ms.setString(19,(fzacone2.length()>160) ? fzacone2.substring(0,160) : fzacone2); // 160 pos.
            ms.setInteger(20,fzanloc);
            ms.setInteger(21,fzaaloc);
            ms.setInteger(22,fzandis);
            ms.setInteger(23,fzafsis);
            ms.setInteger(24,fzahsis);
            ms.setString(25,(usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()); // 10 pos.
            ms.setString(26,idioma);
            ms.setString(27,emailRemitente);
            
 //           int numRegistrosGrabado=
            ms.executeUpdate();
            registroGrabado=true;

            if(!municipi060.equals(""))
            	cargarMunicipio060(session, anoEntrada, numeroEntrada, codiOficina, municipi060,Integer.parseInt(numeroDocumentosRegistro060));
            
            /* Guardamos los documentos electrónicos, si tiene. */
            if (locDocs!=null && !locDocs.equals("")) {
            	log.debug("Grabando locDocs:"+locDocs);
            	regLocDocElect.GrabarLocalitzadorDocsEntrada(session, anoEntrada, numeroEntrada, codiOficina,locDocs);
            }
            /* Grabamos numero de correo si tuviera */
            if (correo!=null && !correo.equals("")) {
            	GrabarCorreo(session, anoEntrada, numeroEntrada, codiOficina,correo);
            }
            
            //Grabamos la oficina física del registro.
            GrabarOficinafisica(session, anoEntrada, numeroEntrada, codiOficina,off_codi);
            
            //Descartamos el oficio de remisión en caso de que sea necesario
            if(hayQueDescartarOficio(session, codiOficina , codiOrganismo)){
            	descartarOficio(anoEntrada, numeroEntrada, codiOficina, usuario);
            }
    
            // desacoplamiento cobol
            String remitente="";
            if (!altres.trim().equals("")) {
                remitente=altres;
            } else {
                ValoresFacade valor = ValoresFacadeUtil.getHome().create();
                remitente=valor.recuperaRemitenteCastellano(fzacent, fzanent+"");
            }
            try {
                Class t = Class.forName("es.caib.regweb.module.PluginHook");

                Class [] partypes = {
                    String.class , Integer.class, Integer.class, Integer.class, Integer.class, String.class,
                 	String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class,
                 	String.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class
                };

                Object [] params = {
                    "A", new Integer(anoEntrada), new Integer(numeroEntrada), new Integer(codiOficina), new Integer(dataDocument), remitente, 
                    comentario, tipo, new Integer(fzafent), new Integer(fzacagge), fzaproce, new Integer(codiOrganismo), idioma,
                    null, null, null, null, null, null, null
                };

                java.lang.reflect.Method metodo = t.getMethod("entrada", partypes);
                metodo.invoke(null, params);

                //t.entrada("A", fzaanoe, fzanume, fzacagc, fzafdoc, remitente, comentario, tipo, fzafent, fzacagge, fzaproce, fzacorg, idioma);

            // ignorar excepciones en caso de que no se encuentre la clase o el método, pero pasar las excepciones que genere el metodo invocado
            } catch (IllegalAccessException iae) { 
            } catch (IllegalArgumentException iae) { 
            } catch (InvocationTargetException ite) {
            } catch (NullPointerException npe) { 
            } catch (ExceptionInInitializerError eiie) {
            } catch (NoSuchMethodException nsme) {
            } catch (SecurityException se) {
            } catch (LinkageError le) {
            } catch (ClassNotFoundException le) {
            }

            /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            /*String Stringsss=sss.format(fechaSystem);
            switch (Stringsss.length()) {
    		//Hem d'emplenar amb 0s.
        	case (1):
        		Stringsss="00"+Stringsss;
        		break;
        	case (2):
        		Stringsss="0"+Stringsss;
        		break;
           	}
            int horamili=Integer.parseInt(hhmmss.format(fechaSystem)+Stringsss);*/
            int horamili=Helper.obtenerHoraSistemaHHmmssSS();
            logLopdBZENTRA("INSERT", (usuario.length()>10) ? usuario.substring(0,10) : usuario
            		, fzafsis, horamili, numeroEntrada, anoEntrada, codiOficina);
            session.flush();
        }catch(HibernateException he){
        	// Tratamos las excepciones generadas por Hibernate
        	log.error("Error: S'ha generat una Hibernate Exception " + he.getMessage());
        	registroGrabado=false;
        	errores.put("","Error inesperat, no s'ha desat el registre"+": "+he.getClass()+"->"+he.getMessage());

        	throw new EJBException("Error: S'ha generat una Hibernate Exception " + he.getMessage());
        } catch(ClassNotFoundException cnfe){
        	log.error("Error: S'ha generat una ClassNotFound Exception" + cnfe.getMessage());
        	registroGrabado=false;
        	errores.put("","Error inesperat, no s'ha desat el registre"+": "+cnfe.getClass()+"->"+cnfe.getMessage());

        	throw new EJBException("Error: S'ha generat una ClassNotFound Exception" + cnfe.getMessage());
        } catch (Exception ex) {
        	log.error(usuario+": Excepci\u00f3: "+ex.getMessage());
            registroGrabado=false;
            errores.put("","Error inesperat, no s'ha desat el registre"+": "+ex.getClass()+"->"+ex.getMessage());

            throw new EJBException("Error inesperat: No s'ha desat el registre", ex);
        } finally {
			close(session);
        }
        param.setregistroGrabado(registroGrabado);
        return param;
    }


	/**
	    * @throws HibernateException
	    * @throws ClassNotFoundException
	    * @throws Exception
	    */
	  private void GrabarCorreo(Session session, int fzaanoe, int fzanume, int fzacagc, String correo) throws HibernateException, ClassNotFoundException, Exception {
		   SQLQuery ms = null;	   
	
		   try{
			   String insertBZNCORR="INSERT INTO BZNCORR (FZPCENSA, FZPCAGCO, FZPANOEN, FZPNUMEN, FZPNCORR)" +
			   "VALUES (?,?,?,?,?)";
				ms=session.createSQLQuery(insertBZNCORR);
				ms.setString(0, "E");
				ms.setInteger(1,fzacagc);
				ms.setInteger(2,fzaanoe);
				ms.setInteger(3,fzanume);
				ms.setString(4, correo);
				ms.executeUpdate();			 
		   }catch(Exception e){
			   throw e;
		   }finally{
		   }  
	  }


	/**
	   * Guardamos la oficina física donde se ha hecho el registro
	   * 
	   * @throws HibernateException
	   * @throws ClassNotFoundException
	   * @throws Exception
	   */
	 private void GrabarOficinafisica(Session session, int fzaanoe, int fzanume, int fzacagc, int off_codi) throws HibernateException, ClassNotFoundException, Exception {
		   SQLQuery ms = null;	   
	
		   try{
			   String insertOfifis="INSERT INTO BZENTOFF (FOEANOEN, FOENUMEN, FOECAGCO, OFE_CODI)" +
			   "VALUES (?,?,?,?)";
			   ms=session.createSQLQuery(insertOfifis);
			   ms.setInteger(0,fzaanoe);
			   ms.setInteger(1,fzanume);
			   ms.setInteger(2,fzacagc);
			   ms.setInteger(3,off_codi);
			   ms.executeUpdate();		 
	
			}catch(Exception e){
				   throw e;
			}finally{
			}  
	 }
 
	 /**
     * Método que comprueba si entre una oficina de registro y el organismo destinatario
     * de la documentación registrada hay que descartar el oficio
     * @param codiOficina
     * @param codiOrganismo
     * @return
     */
    private boolean hayQueDescartarOficio(Session session, int codiOficina, int codiOrganismo)throws HibernateException {
		boolean rtdo = false;
		ScrollableResults rs=null;
		String sentenciaHql = "select 1 from OficinaOrganismoNoRemision where id.oficina.codigo=? and id.organismo.codigo = ?";
		
		Query query=session.createQuery(sentenciaHql);
		query.setInteger(0,Integer.valueOf(codiOficina));
		query.setInteger(1,Integer.valueOf(codiOrganismo));
		
		rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
		if(rs.next()) {
			rtdo = true;
			log.debug("Hay que descartar un oficio. oficina - oragnismo:"+codiOficina+" - "+ codiOrganismo);
		}
		return rtdo;
	}
    
    /** 
     * Lee un registro del fichero BZENTRA, para ello le
     * deberemos pasar el usuario, el codigo de oficina, el numero de registro de
     * entrada y el año de entrada.
     * @param param ParametrosRegistroEntrada
     * @return ParametrosRegistroEntrada
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosRegistroEntrada leer(ParametrosRegistroEntrada param) {
        
        ParametrosRegistroEntrada res = new ParametrosRegistroEntrada();
    
        String usuario=param.getUsuario();
        String anoEntrada=param.getAnoEntrada();
        String numeroEntrada=param.getNumeroEntrada();
        String oficina=param.getOficina();      
		Session session = getSession();
        ScrollableResults rs = null;
        ScrollableResults rsHist = null;
        SQLQuery q = null;
        SQLQuery qHist = null;    
        res.setLeido(false);
        
        try {
        	LocalitzadorsDocsElectronicsFacade regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
            String sentenciaHql="SELECT FZAANOEN, FZANUMEN, FZACAGCO, OFF_CODI, OFF_NOM, FZAFENTR, " +
                    " FAADAGCO, FZAFDOCU, FZAFACTU, FZAHORA, FZGCENTI, FZAREMIT, " +
                    " FZGDENT2, FZACORGA, FAXDORGT, FZIDTIPE, FZACTIPE, FZACIDI, " +
                    " FZACIDIO, FZAENULA, FZMDIDI, FZACENTI, FZANENTI, FZAREMIT, " +
                    " FZACAGGE, FZAPROCE, FABDAGGE, FZACONEN, FZACONE2, FZANDIS, " +
                    " FZANLOC, FZAALOC, FZPNCORR, EMAILREMITENT FROM BZENTRA LEFT JOIN BAGECOM ON FAACAGCO=FZACAGCO " +
                    " LEFT JOIN BZENTID ON FZACENTI=FZGCENTI AND FZGNENTI=FZANENTI " +
                    " LEFT JOIN BORGANI ON FAXCORGA=FZACORGA " +
                    " LEFT JOIN BZTDOCU ON FZICTIPE=FZACTIPE " +
                    " LEFT JOIN BZIDIOM ON FZACIDI=FZMCIDI " +
                    " LEFT JOIN BZENTOFF ON FZAANOEN=FOEANOEN AND FZANUMEN=FOENUMEN AND FZACAGCO=FOECAGCO " +
                    " LEFT JOIN BZOFIFIS ON FZACAGCO=FZOCAGCO AND OFF_CODI=OFE_CODI " +
                    " LEFT JOIN BAGRUGE ON FZACTAGG=FABCTAGG AND FZACAGGE=FABCAGGE " +
                    " LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZACAGCO " +
                    " LEFT JOIN BZNCORR ON FZPCENSA='E' AND FZPCAGCO=FZACAGCO AND FZPANOEN=FZAANOEN AND FZPNUMEN=FZANUMEN " +
                    " WHERE FZAANOEN=? AND FZANUMEN=? AND FZACAGCO=? AND FZHCAUT=?";
            q=session.createSQLQuery(sentenciaHql);
            q.addScalar("FZAANOEN", Hibernate.INTEGER);
            q.addScalar("FZANUMEN", Hibernate.INTEGER);
            q.addScalar("FZACAGCO", Hibernate.INTEGER);
            q.addScalar("OFF_CODI", Hibernate.INTEGER);
            q.addScalar("OFF_NOM" , Hibernate.STRING);
            q.addScalar("FZAFENTR", Hibernate.INTEGER);
            q.addScalar("FAADAGCO", Hibernate.STRING);
            q.addScalar("FZAFDOCU", Hibernate.INTEGER);
            q.addScalar("FZAFACTU", Hibernate.INTEGER);
            q.addScalar("FZAHORA" , Hibernate.INTEGER);
            q.addScalar("FZGCENTI", Hibernate.STRING);
            q.addScalar("FZAREMIT", Hibernate.STRING);
            q.addScalar("FZGDENT2", Hibernate.STRING);
            q.addScalar("FZACORGA", Hibernate.INTEGER);
            q.addScalar("FAXDORGT", Hibernate.STRING);
            q.addScalar("FZIDTIPE", Hibernate.STRING);
            q.addScalar("FZACTIPE", Hibernate.STRING);
            q.addScalar("FZACIDI" , Hibernate.STRING);
            q.addScalar("FZACIDIO", Hibernate.STRING);
            q.addScalar("FZAENULA", Hibernate.STRING);
            q.addScalar("FZMDIDI" , Hibernate.STRING);
            q.addScalar("FZACENTI", Hibernate.STRING);
            q.addScalar("FZANENTI", Hibernate.STRING);
            q.addScalar("FZAREMIT", Hibernate.STRING);
            q.addScalar("FZACAGGE", Hibernate.STRING);
            q.addScalar("FZAPROCE", Hibernate.STRING);
            q.addScalar("FABDAGGE", Hibernate.STRING);
            q.addScalar("FZACONEN", Hibernate.STRING);
            q.addScalar("FZACONE2", Hibernate.STRING);
            q.addScalar("FZANDIS" , Hibernate.STRING);
            q.addScalar("FZANLOC" , Hibernate.INTEGER);
            q.addScalar("FZAALOC" , Hibernate.INTEGER);
            q.addScalar("FZPNCORR", Hibernate.STRING);
            q.addScalar("EMAILREMITENT", Hibernate.STRING);
            
            q.setString(0,usuario.toUpperCase());
            q.setInteger(1,Integer.valueOf(anoEntrada));
            q.setInteger(2,Integer.valueOf(numeroEntrada));
            q.setInteger(3,Integer.valueOf(oficina));
            q.setString(4,"CE");
            log.debug("Leyendo registro entrada...");
            log.debug(q);
            rs=q.scroll();
            if (rs.next()) {
            	/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            	Date fechaSystem=new Date();
            	//DateFormat hhmmss=new SimpleDateFormat("HHmmss");
                //DateFormat sss=new SimpleDateFormat("S");
                //String ss=sss.format(fechaSystem);
                DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
                int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
                //int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
                /*String Stringsss=sss.format(fechaSystem);
                switch (Stringsss.length()) {
            		//Hem d'emplenar amb 0s.
                	case (1):
                		Stringsss="00"+Stringsss;
                		break;
                	case (2):
                		Stringsss="0"+Stringsss;
                		break;
               	}
                int horamili=Integer.parseInt(hhmmss.format(fechaSystem)+Stringsss);*/
                int horamili=Helper.obtenerHoraSistemaHHmmssSS();
                logLopdBZENTRA("SELECT", usuario, fzafsis, horamili, Integer.parseInt(numeroEntrada), Integer.parseInt(anoEntrada), Integer.parseInt(oficina));
                
                res.setLeido(true);
                res.setAnoEntrada(String.valueOf(rs.getInteger(0)));
                res.setNumeroEntrada(String.valueOf(rs.getInteger(1)));
                res.setoficina(String.valueOf(rs.getInteger(2)));
				res.setoficinafisica(String.valueOf(rs.getInteger(3)));

				/* Aquí hem d'anar a la taula d'oficines fisiques. */
				String textoOficinaFisica=null;
				textoOficinaFisica=rs.getString(4);
				if (rs.getInteger(3)==null) {
					res.setoficinafisica("0");

	                String sentenciaHqlOfiFis="SELECT OFF_NOM FROM BZOFIFIS WHERE FZOCAGCO=? AND OFF_CODI=0 ";
		            qHist=session.createSQLQuery(sentenciaHqlOfiFis);
		            qHist.addScalar("OFF_NOM", Hibernate.STRING);
		            qHist.setInteger(0,Integer.valueOf(oficina));
		            rsHist=qHist.scroll();
		            if (rsHist.next()) {
						textoOficinaFisica=rsHist.getString(0);
		            }
		    		if (rsHist != null)
		    			rsHist.close();

				}
				if (textoOficinaFisica==null) {
					textoOficinaFisica=" ";
				}

				res.setDescripcionOficinaFisica(textoOficinaFisica);

                String fechaEntra=String.valueOf(rs.getInteger(5));
                res.setdataentrada(Helper.convierteyyyymmddFechaAddmmyyyyFecha(fechaEntra));

                String textoOficina=null;
                String sentenciaHqlHistOfi="SELECT FHADAGCO FROM BHAGECO WHERE FHACAGCO=? AND FHAFALTA<=? " +
                		"AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
                qHist=session.createSQLQuery(sentenciaHqlHistOfi);
                qHist.addScalar("FHADAGCO", Hibernate.STRING);
                qHist.setInteger(0,Integer.valueOf(oficina));
                qHist.setInteger(1,Integer.valueOf(fechaEntra));
                qHist.setInteger(2,Integer.valueOf(fechaEntra));
                rsHist=qHist.scroll();
                if (rsHist.next()) {
                	/* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
                	textoOficina=rsHist.getString(0);
                } else {
                	textoOficina=rs.getString(6);
                	if (textoOficina==null) {
                		textoOficina=" ";
                	}
                }
                //  Tancam el preparedstatement i resultset de l'històric
        		if (rsHist != null)
        			rsHist.close();
        		
                res.setDescripcionOficina(textoOficina);
                
                String fechaDocu=String.valueOf(rs.getInteger(7));
                try {  
                	res.setdata(Helper.convierteyyyymmddFechaAddmmyyyyFecha(fechaDocu));
                } catch (Exception e) {
                    res.setdata("0");
                }

                String fechaVisado=String.valueOf(rs.getInteger(8));                
                try {               
                	res.setDataVisado(Helper.convierteyyyymmddFechaAddmmyyyyFecha(fechaVisado));
                } catch (Exception e) {
                    res.setDataVisado("0");
                }
        		
                res.sethora(String.valueOf(rs.getInteger(9)));
                if (rs.getString(10)==null) {
                    res.setDescripcionRemitente(rs.getString(11));
                } else {
                    res.setDescripcionRemitente(rs.getString(12));
                }
                res.setdestinatari(String.valueOf(rs.getInteger(13)));
                
                String sentenciaHqlHistOrga="SELECT FHXDORGT FROM BHORGAN WHERE FHXCORGA=? AND FHXFALTA<=? " +
                		"AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
                qHist=session.createSQLQuery(sentenciaHqlHistOrga);
                qHist.addScalar("FHXDORGT", Hibernate.STRING);
                qHist.setInteger(0,Integer.valueOf(res.getDestinatari()));
                qHist.setInteger(1,Integer.valueOf(fechaEntra));
                qHist.setInteger(2,Integer.valueOf(fechaEntra));
                rsHist=qHist.scroll();
                
                if (rsHist.next()) {
                	/* Hem trobat un històric de l'organisme sol·licitat, hem de mostrar-ne el descriptiu. */
                	res.setDescripcionOrganismoDestinatario(rsHist.getString(0));
                	//log.debug("Org destinatari: "+descripcionOrganismoDestinatario);
                } else {
                	res.setDescripcionOrganismoDestinatario(rs.getString(14));
                	if (res.getDescripcionOrganismoDestinatario()==null) {
                		res.setDescripcionOrganismoDestinatario(" ");
                	}
                }
                //log.debug("Org destinatari: "+this.getDescripcionOrganismoDestinatario());
                //  Tancam el preparedstatement i resultset de l'històric
        		if (rsHist != null)
        			rsHist.close();
                
                if (rs.getString(15)==null)
                	res.setDescripcionDocumento("");
                else
                	res.setDescripcionDocumento(rs.getString(15));
                
                if (rs.getString(16)==null )
                	res.settipo("");
                else 
                	res.settipo(rs.getString(16));
                
                res.setidioma(rs.getString(17));
                res.setidioex(rs.getString(18));
                res.setRegistroAnulado(rs.getString(19));
                res.setDescripcionIdiomaDocumento(rs.getString(20));
                res.setentidad1(Helper.convierteEntidad(rs.getString(21),session));
                res.setEntidad1Grabada(rs.getString(21));
                res.setentidad2(rs.getString(22));
                res.setaltres((rs.getString(23)==null)?null:rs.getString(23).trim());
                res.setbalears(rs.getString(24));
                res.setfora((rs.getString(25)==null)?null:rs.getString(25).trim());
               
                if (rs.getString(26)==null) {
                	 res.setProcedenciaGeografica(((rs.getString(25)==null)?null:rs.getString(25).trim()));
                } else {
                	 res.setProcedenciaGeografica(rs.getString(26).trim());
                }
                if (rs.getString(18).equals("1")) {
                    res.setIdiomaExtracto("CASTELLA");
                    res.setcomentario(rs.getString(27));
                } else {
                    res.setIdiomaExtracto("CATALA");
                    res.setcomentario(((rs.getString(28)==null)?null:rs.getString(28).trim()));
                }
                if (rs.getString(29).equals("0")) {
                    res.setdisquet("");
                } else {
                    res.setdisquet(rs.getString(29));
                }
                res.setsalida1(String.valueOf(rs.getInteger(30)));
                res.setsalida2(String.valueOf(rs.getInteger(31)));
                res.setCorreo(rs.getString(32));
                res.setEmailRemitent((rs.getString(33)!=null)?rs.getString(33).trim():null);
                res.setLocalitzadorsDocs(regLocDocElect.LeerListaLocalizadoresDocEntrada(Integer.parseInt(anoEntrada),Integer.parseInt(numeroEntrada),Integer.parseInt(oficina)));           
                // leer060() lee el campo de municipio 060 asociado al registro. Este dato se almacena en la tabla BZENTRA060.
                leer060(res, session);
            }
        } catch (Exception e) {
        	log.error("ERROR: Leer: "+e.getMessage());
        } finally {
			if (rs!=null) rs.close();
			close(session);
        }
        return res;
    }
	


   /** 
 * Lee un registro del fichero BZENTRA060 (si existe), para ello le
 * deberemos pasar el codigo de oficina, el numero de registro de
 * entrada y el año de entrada.
 * @param param ParametrosRegistroEntrada
 * @return void
 */
private void leer060(ParametrosRegistroEntrada param, Session session) throws Exception {
    
    String anoEntrada=param.getAnoEntrada();
    String numeroEntrada=param.getNumeroEntrada();
    String oficina=param.getOficina();
    
	ScrollableResults rs = null;
	SQLQuery q = null;

    try {

        String sentenciaHql="SELECT ENT_CODIMUN, MUN_NOM, ENT_NUMREG FROM BZENTRA060,BZMUN_060 WHERE ENT_ANY=? AND ENT_OFI=? AND ENT_NUM=? AND ENT_CODIMUN=MUN_CODI";
        q=session.createSQLQuery(sentenciaHql);
        q.addScalar("ENT_CODIMUN", Hibernate.STRING);
        q.addScalar("MUN_NOM", Hibernate.STRING);
        q.addScalar("ENT_NUMREG", Hibernate.STRING);
        q.setInteger(0,Integer.valueOf(anoEntrada));
        q.setInteger(1,Integer.valueOf(oficina));
        q.setInteger(2,Integer.valueOf(numeroEntrada));
        rs=q.scroll();
        if (rs.next()) {
        	param.setMunicipi060(rs.getString(0));
        	param.setDescripcionMunicipi060(rs.getString(1));
        	param.setNumeroDocumentosRegistro060(rs.getString(2));
        }
        
    }catch(Exception e ){
        throw e;
    }finally{
		if (rs!=null) rs.close();
    }
}

  /**
 * Emplena la taula de control d'accés complint la llei LOPD per la taula BZENTRA
 * @param tipusAcces <code>String</code> tipus d'accés a la taula
 * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
 * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
 * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
 * @param nombreRegistre <code>Int</code> nombre de registre
 * @param any <code>Int</code> any del registre
 * @param oficina <code>Int</code> oficina on s'ha registrat
 * @author Sebastià Matas Riera (bitel)
 */  
private void logLopdBZENTRA(String tipusAcces, String usuari, int data, int hora, int nombreRegistre, int any, int oficina ) {
    Session session = getSession();
	try {
        LogEntradaLopd log = new LogEntradaLopd(new LogEntradaLopdId( tipusAcces, usuari, Integer.valueOf(data), Integer.valueOf(hora), 
                                 Integer.valueOf(nombreRegistre), Integer.valueOf(any), Integer.valueOf(oficina)));

        session.save(log);
        session.flush();

    } catch (HibernateException he) {
        throw new EJBException(he);
    } finally {
        close(session);
    }


}
 

/**
* Anula el registro de salida si las validaciones son correctas
* 
* @param usuario Identificador del usuario que realiza la acción
* @param oficina Oficina de salida que queremos comprobar si tiene permisos
* @param session Session de base de datos
* 
* @return verdadero si tiene permiso para modificar, falso en caso contrario.
* 
*/
private boolean tienePermisoEntrada(String usuario, String oficina, Session session ) throws Exception{
	boolean rtdo = false;
	ScrollableResults rs=null;
	Query q = null;

	String sentenciaHql=" FROM Autorizacion WHERE id.usuario=? AND id.codigoAutorizacion=? AND id.codigoOficina IN " +
	"(SELECT codigo FROM Oficina WHERE fechaBaja=0 AND codigo=?)";
	q=session.createQuery(sentenciaHql);
	q.setString(0,usuario.toUpperCase());
	q.setString(1,"AE");
	q.setInteger(2,Integer.parseInt(oficina));
	rs=q.scroll();

	if (rs.next()) {
		rtdo = true;
	}

	return rtdo;
}

/* Validaciones del registro de entrada */
/**
 * @return
 * @ejb.interface-method
 * @ejb.permission unchecked="true" 
 */
public ParametrosRegistroEntrada validar(ParametrosRegistroEntrada param) {
    Session session = getSession();
    ScrollableResults rs=null;
    Query q = null;
    
    String dataentrada=param.getDataEntrada();
    String hora=param.getHora();
    String oficina=param.getOficina();
    String oficinafisica=param.getOficinafisica();
    String data=param.getData();
    String tipo=param.getTipo();
    String idioma=param.getIdioma();
    String entidad1=param.getEntidad1();
    String entidad2=param.getEntidad2();
    String altres=param.getAltres();
    String balears=param.getBalears();
    String fora=param.getFora();
    String salida1=param.getSalida1();
    String salida2=param.getSalida2();
    String destinatari=param.getDestinatari();
    String idioex=param.getIdioex();
    String disquet=param.getDisquet();
    String comentario=param.getComentario();
    String usuario=param.getUsuario();
    boolean actualizacion=param.getActualizacion();
    String motivo=param.getMotivo();
    String entidad1Nuevo=param.getEntidad1Nuevo();
    String entidad2Nuevo=param.getEntidad2Nuevo();
    String altresNuevo=param.getAltresNuevo();
    String comentarioNuevo=param.getComentarioNuevo();
    //String emailRemitente=param.getEmailRemitent();
    Hashtable errores=param.getErrores();
    boolean errorFechaPublicacion = false;  
    boolean validado=false;
    
    errores.clear();
    try {
    	int error_fecha;
    	
    	errorFechaPublicacion = (param.getParamRegPubEnt()==null)? false:param.getParamRegPubEnt().getErrorfecha();  
    	/* Validamos la fecha de publicación en el BOIB (solo CAIB oficina 32)*/
        if (errorFechaPublicacion) {
            errores.put("dataPublic","Data de publicació no es l\u00f2gica");
        }
        
		/* Validamos la fecha del registro */
		error_fecha = validarFecha(dataentrada);
		if (error_fecha==FECHA_NO_LOGICA) {
			errores.put("datasalida","Data de sortida no \u00e9s l\u00f2gica.");
		}

		if (error_fecha==FECHA_POSTERIOR_ACTUAL) {
			errores.put("datasalida","Data de sortida \u00e9s posterior a la del dia.");
		}
		
		if (error_fecha==FECHA_DEMASIADO_ANTIGUA) {
			errores.put("datasalida","Data de sortida \u00e9s massa antiga.");
		}
		
		/* Validamos Fecha del documento */
		if (data==null) {
			//Si no tenemos fecha de documento, copiamos la del registro
			data=dataentrada;
			param.setdata(data);
		}else{
			error_fecha = validarFecha(data);
			
			if (error_fecha==FECHA_NO_LOGICA) {
				errores.put("data","Data de document \u00e9s no \u00e9s l\u00f2gica.");
			}

			if (error_fecha==FECHA_POSTERIOR_ACTUAL) {
				errores.put("data","Data de document \u00e9s posterior a la del dia.");
			}
			
			if (error_fecha==FECHA_DEMASIADO_ANTIGUA) {
				errores.put("data","Data de document \u00e9s massa antiga.");
			}
		}
        
        /* Validamos Hora */
        if (hora==null) {
            errores.put("hora","Hora d'entrada no es l\u00e0gica");
        } else {
            try {
                horaF.setLenient(false);
                Date horaTest=horaF.parse(hora);
            } catch (ParseException ex) {
                errores.put("hora","Hora d'entrada no es l\u00f2gica");
                errores.put("hora",hora);
            }
        }
        
        /* Validamos el contador de la oficina */
        if (!oficina.equals("")) {
        
            int fzaanoe;

            fechaTest = dateF.parse(dataentrada);
            Calendar cal=Calendar.getInstance();
            cal.setTime(fechaTest);
            DateFormat date1=new SimpleDateFormat("yyyyMMdd");

            fzaanoe=cal.get(Calendar.YEAR);
            
            AdminFacade admin=AdminFacadeUtil.getHome().create();
            if(!admin.existComptador(String.valueOf(fzaanoe),oficina)) {
                errores.put("oficina","Oficina: "+oficina+" no t\u00E9 inicialitzat el comptador.");
            }
        }
        
        /* Validamos la oficina */
        if (!oficina.equals("")) {
            try {
                String sentenciaHql="SELECT id.usuario FROM Autorizacion WHERE id.usuario=? AND id.codigoAutorizacion=? AND id.codigoOficina IN " +
                        "(SELECT codigo FROM Oficina WHERE fechaBaja=0 AND codigo=?)";
                q=session.createQuery(sentenciaHql);
                q.setString(0,usuario.toUpperCase());
                q.setString(1,"AE");
                q.setInteger(2,Integer.parseInt(oficina));
                rs=q.scroll();
                
                if (rs.next()) {
                } else {
                    errores.put("oficina","Oficina: "+oficina+" no v\u00e0lida per a l'usuari: "+usuario);
                }
            } catch (Exception e) {
            	log.error(usuario+": Error en validar l'oficina "+e.getMessage() );
                errores.put("oficina","Error en validar l'oficina: "+oficina+" de l'usuari: "+usuario+": "+e.getClass()+"->"+e.getMessage());
            } finally {
            	rs.close();
            }
        } else {
            errores.put("oficina","Oficina: "+oficina+" no v\u00e0lida per a l'usuari: "+usuario);
        }

        /* Validamos la oficina fisica */
        if (oficinafisica==null || oficinafisica.equals("") || oficinafisica.equals("null")) {
            errores.put("oficinafisica","Oficina f\u00EDsica no v\u00e0lida.");
        }


        
        /* Validamos Tipo de documento */
        try {
            String sentenciaHql=" FROM TipoDocumento WHERE id.codigo=? AND id.fechaBaja=0";
            q=session.createQuery(sentenciaHql);
            q.setString(0,tipo);
            rs=q.scroll();
            
            if (rs.next()) {
            } else {
                errores.put("tipo","Tipus de document : "+tipo+" no v\u00e0lid");
            }
        } catch (Exception e) {
        	log.error(usuario+": Error en validar el tipus de document"+e.getMessage() );
            errores.put("tipo","Error en validar el tipus de document : "+tipo+": "+e.getClass()+"->"+e.getMessage());
        } finally {
        	rs.close();
        }

        /* Validamos el idioma del documento */
        try {
            String sentenciaHql=" FROM Idioma WHERE codigo=?";
            q=session.createQuery(sentenciaHql);
            q.setString(0,idioma);
            rs=q.scroll();
            
            if (rs.next()) {
            } else {
                errores.put("idioma","Idioma del document : "+idioma+" no v\u00e0lid");
            }
        } catch (Exception e) {
        	log.error(usuario+": Error en validar l'idioma del document"+e.getMessage() );
        	errores.put("idioma","Error en validar l'idioma del document: "+idioma+": "+e.getClass()+"->"+e.getMessage());
        } finally {
        	rs.close();
        }
        
        /* Validamos remitente */
        if (entidad1.trim().equals("") && altres.trim().equals("")) {
            errores.put("entidad1","\u00e9s obligatori introduir el remitent");
        } else if(!entidad1.trim().equals("") && !altres.trim().equals("")) {
            errores.put("entidad1","Heu d'introduir: Entitat o Altres");
        } else if (!entidad1.equals("")) {
            if (entidad2.equals("")) {entidad2="0"; param.setentidad2(entidad2);}
            try {
                String sentenciaHql="SELECT id.codigo FROM EntidadRemitente WHERE codigoCatalan=? AND id.numero=? AND id.fechaBaja=0";
                q=session.createQuery(sentenciaHql);
                q.setString(0,entidad1);
                q.setInteger(1,Integer.parseInt(entidad2));
                rs=q.scroll();
                
                if (rs.next()) {
                    param.setEntidadCastellano(rs.getString(0));
                } else {
                    errores.put("entidad1","Entitat Remitent : "+entidad1+"-"+entidad2+" no v\u00e0lid");
                    log.error(usuario+": ERROR: en validar l'entitat remitent : "+entidad1+"-"+entidad2+" no v\u00e0lid");
                }
            } catch (Exception e) {
            	log.error(usuario+": Error en validar l'entitat remitent "+e.getMessage() );
            	errores.put("entidad1","Error en validar l'entitat remitent: "+entidad1+"-"+entidad2+": "+e.getClass()+"->"+e.getMessage());
            } finally {
            	rs.close();
            }
        }
        
        /* Validamos la procedencia geografica */
        if ( (balears.equals("") || balears == null) && ( fora.equals("") || fora == null )) {
            errores.put("balears","Obligatori introduir Proced\u00e8ncia Geogr\u00e0fica");
        } else if (!balears.equals("") && !fora.equals("")) {
            errores.put("balears","Heu d'introduir Balears o Fora de Balears");
        } else if (!balears.equals("")) {
            try {
                String sentenciaHql=" SELECT id.tipo FROM AgrupacionGeografica WHERE id.tipo=90 AND id.codigo=? AND fechaBaja=0";
                q=session.createQuery(sentenciaHql);
                q.setInteger(0,Integer.parseInt(balears));
                rs=q.scroll();
                
                if (rs.next()) {
                } else {
                    errores.put("balears","Proced\u00e0ncia geogr\u00e0fica de Balears : "+balears+" no v\u00e0lid");
                    log.error(usuario+": ERROR: Proced\u00e8ncia geogr\u00e0fica de Balears : "+balears+" no v\u00e0lid");
                }
            } catch (Exception e) {
            	log.error(usuario+": Error en validar la proced\u00e8ncia geogr\u00e0fica de Balears "+e.getMessage() );
                errores.put("balears","Error en validar la proced\u00e8ncia geogr\u00e0fica de Balears : "+balears+": "+e.getClass()+"->"+e.getMessage());
            } finally {
            	rs.close();
            }
        }
        
        /* No hay validacion del numero de salida del documento */
        
        if (salida1.equals("") && salida2.equals("")) {
        } else {
            int chk1=0;
            int chk2=0;
            try {
                chk1=Integer.parseInt(salida1);
                chk2=Integer.parseInt(salida2);
            } catch (Exception e) {
                errores.put("salida1","Ambd\u00f3s camps de numero de sortida han de ser num\u00e8rics");
                log.error(usuario+": ERROR: Ambd\u00f3s camps de numero de sortida han de ser num\u00e8rics");
            }
            if (chk2<1990 || chk2>2050) {
                errores.put("salida1","Any de sortida, incorrecte");
            }
        }
        
        /* Validamos el Organismo destinatari */
        try {
            String sentenciaHql=" FROM OficinaOrganismo WHERE id.oficina.codigo=? AND id.organismo.codigo=?";
            q=session.createQuery(sentenciaHql);
            q.setInteger(0,Integer.parseInt(oficina));
            q.setInteger(1,Integer.parseInt(destinatari));
            rs=q.scroll();
            
            if (rs.next()) {
            } else {
                errores.put("destinatari","Organisme destinatari : "+destinatari+" no v\u00e0lid");
            }
        } catch (NumberFormatException e1) {
        	errores.put("destinatari","Organisme destinatari : "+destinatari+" codi no num\u00e8ric");
        } catch (Exception e) {
        	log.error(usuario+": Error en validar l'organisme destinatari "+e.getMessage() );
            errores.put("destinatari","Error en validar l'organisme destinatari : "+destinatari+": "+e.getClass()+"->"+e.getMessage());
        } finally {
        	rs.close();
        }
        
        /* Validamos el idioma del extracto */      
        if (!idioex.equals("1") && !idioex.equals("2")) {
            errores.put("idioex","L'idioma ha de ser 1 o 2, idioma="+idioex);
        }
     
        /* Validamos el numero de disquete */
        try {
            if (!disquet.equals("")) {
                int chk1=Integer.parseInt(disquet);
            }
        } catch (Exception e) {
            errores.put("disquet","Numero de disquet no v\u00e0lid");
        }
        
        /* Validamos el extracto del documento */
        if (comentario.equals("")) {
            errores.put("comentario","Heu d'introduir un extracte del document ");
        }
        
        /* Validaciones solamente validas para el proceso de actualizacion con modificacion de
         * campos criticos */
        if (actualizacion && !motivo.trim().equals("")) {
            if (comentarioNuevo.equals("")) {
                errores.put("comentario","Heu d'introduir un extracte del document ");
            }

/* Validamos remitente */
            if (entidad1Nuevo.equals("") && altresNuevo.equals("")) {
                errores.put("entidad1","Obligatori introduir remitent");
            } else if(!entidad1Nuevo.equals("") && !altresNuevo.equals("")) {
                errores.put("entidad1","Heu d'introduir: Entitat o Altres");
            } else if (!entidad1Nuevo.equals("")) {
                if (entidad2Nuevo.equals("")) {entidad2Nuevo="0";param.setEntidad2Nuevo(entidad2Nuevo);}
                try {
                    String sentenciaHql=" FROM EntidadRemitente WHERE codigoCatalan=? AND id.numero=? AND id.fechaBaja=0";
                    q=session.createQuery(sentenciaHql);
                    q.setString(0,entidad1Nuevo);
                    q.setInteger(1,Integer.parseInt(entidad2Nuevo));
                    rs=q.scroll();
                    
                    if (rs.next()) {
                    } else {
                        errores.put("entidad1","Entitat Remitent : "+entidad1+"-"+entidad2+" no v\u00e0lid");
                        log.error("Error en validar l'entitat Remitent "+entidad1+"-"+entidad2+" no v\u00e0lid");
                    }
                } catch (Exception e) {
                	log.error(usuario+": Error en validar l'entitat Remitent "+e.getMessage() );
                    errores.put("entidad1","Error en validar l'entitat Remitent : "+entidad1+"-"+entidad2+": "+e.getClass()+"->"+e.getMessage());
                } finally {
                	rs.close();
                }
            }
        }
        
        /* Comprobamos que la ultima fecha introducida en el fichero sea inferior o igual
         * que la fecha de entrada del registro */
        if (!oficina.equals("") && !actualizacion) {
            try {
                String sentenciaHql="SELECT MAX(fechaEntrada) as maximo FROM Entrada WHERE id.anyo=? AND id.oficina=?";
                fechaTest = dateF.parse(dataentrada);
                Calendar cal=Calendar.getInstance();
                cal.setTime(fechaTest);
                DateFormat date1=new SimpleDateFormat("yyyyMMdd");
                
                q=session.createQuery(sentenciaHql);
                q.setInteger(0,cal.get(Calendar.YEAR));
                q.setInteger(1,Integer.parseInt(oficina));
                rs=q.scroll();
                int ultimaFecha=0;
                
                if (rs!=null && rs.next()) {
                    Object result = rs.get(0);
				    if (result!=null) ultimaFecha=(Integer)result;
                    if (ultimaFecha>Integer.parseInt(date1.format(fechaTest))) {
                        errores.put("dataentrada","Data inferior a la darrera entrada");
                    }
                }
                
            } catch (Exception e) {
                errores.put("dataentrada","Error inesperat a data d'entrada");
                log.error(usuario+": Error inesperat a la data d'entrada"+": "+e.getClass()+"->"+e.getMessage());
            } finally {
            	if (rs!=null) rs.close();
            }
        }
        
      /* Fin de validaciones de campos */
    } catch (Exception e) {
        validado=false;
    } finally {
        close(session);
    }
    if (errores.size()==0) {
        validado=true;
    } else {
        validado=false;
    }
    param.setValidado(validado);
    return param;
}

/**
 * Valida la data donada
 * @param fecha
 */
/**
 * @param fecha
 */
private int validarFecha(String fecha) {
	int ok=CORRECTE;
	SimpleDateFormat  dateF= new SimpleDateFormat("dd/MM/yyyy");
	Date fechaTest = null;
	Date fechaHoy = new Date();
	Date fechaMinimo = null;

	try {
		fechaMinimo = dateF.parse("01/01/1900");
		dateF.setLenient(false);
		fechaTest = dateF.parse(fecha);
	} catch (Exception ex) {
		log.error("Error al validar la fecha: "+fecha);
		ok=FECHA_NO_LOGICA;
	}
	
	if(ok==CORRECTE){
		//Comprobamos que la fecha no sea posterior a la actual
		if(fechaTest.after(fechaHoy)){
			log.error("Error al validar la fecha: "+fecha+". Fecha posterior a la actual.");
			ok=FECHA_POSTERIOR_ACTUAL;
		}
		//Comprobamos que la fecha no sea demasiado antigua
		if(fechaTest.before(fechaMinimo)){
			log.error("Error al validar la fecha: "+fecha+". No puede anterior a la fecha 01/01/1950.");
			ok=FECHA_DEMASIADO_ANTIGUA;
		}
	}
	return ok;
}
}