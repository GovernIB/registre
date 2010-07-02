

package es.caib.regweb.logic.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.regweb.logic.helper.ParametrosListadoAcceso;
import es.caib.regweb.logic.helper.RegistroESlopdSeleccionado;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;

import org.apache.log4j.Logger;

/**
 * SessionBean per a llistat de registres d'accès
 *
 * @ejb.bean
 *  name="logic/ListadoRegAccesosSortidaFacade"
 *  jndi-name="es.caib.regweb.logic.ListadoRegAccesosSortidaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ListadoRegAccesosSortidaFacadeEJB extends HibernateEJB {
	
    private Logger log = Logger.getLogger(this.getClass());
	
	private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
	
	
	
	
	/** Creates a new instance of Valores */
	public ListadoRegAccesosSortidaFacadeEJB() {
	}
	
	
	/**
	 * Validació de les dates donades per l'usuari.
	 * @param fecha1 Data inicial
	 * @param fecha2 Data final
	 * @param obligatori Indica si és obligatori que hi hagui dates
     */
	private boolean validarFecha(Hashtable errores, String fecha1, String fecha2, boolean obligatori) {
		Date fechaTest1=null;
		Date fechaTest2=null;
		
		boolean error=false;
		log.debug("validarFecha: obligatori="+obligatori);
		try {
			dateF.setLenient(false);
			fechaTest1 = dateF.parse(fecha1);
		} catch (Exception ex) {
			if (obligatori) {
				error=true;
				errores.put("2","Data inici no \u00e9s valida, (dd/mm/aaaa)");
			}
		}
		try {
			dateF.setLenient(false);
			fechaTest2 = dateF.parse(fecha2);
		} catch (Exception ex) {
			if (obligatori) {
				error=true;
				errores.put("3","Data final no \u00e9s valida, (dd/mm/aaaa)");
			}
		}
		if (!error && (fechaTest1!=null && fechaTest2!=null)) {
			if (fechaTest2.before(fechaTest1)) {
				if (obligatori) 
					errores.put("4","Data final \u00e9s inferior a data inici");
			}
		}
		return !error;
	}
	
	

	/**
	 * Mètode per validar els camq de cerca
	 * @return Torna true si els camq son vàlids
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public boolean validarBusqueda(ParametrosListadoAcceso parametros) {
	    boolean validado=false;
		Hashtable errores = parametros.getErrores();
		String oficinaDesde=parametros.getOficinaDesde();
		String oficinaHasta=parametros.getOficinaHasta();
		String fechaDesde=parametros.getFechaDesde();
		String fechaHasta=parametros.getFechaHasta();
		String numRegistre=parametros.getNumRegistre();
		String anyRegistre=parametros.getAnyRegistre();
		String registreES=parametros.getRegistreES();

		errores.clear();
		
		// Validamos que la oficina desde sea < o = a oficina Hasta
		if (oficinaDesde==null || oficinaHasta==null || oficinaDesde.equals("") || oficinaHasta.equals("")) {
			errores.put("1","L\u00edmits incorrectes per a oficines");
		} else {
			int oficinaInicio=Integer.parseInt(oficinaDesde);
			int oficinaFin=Integer.parseInt(oficinaHasta);
			if (oficinaFin<oficinaInicio) {
				errores.put("1","L\u00edmits incorrectes per a oficines");
			}
		}
		// Validamos fecha inicio
		if ( numRegistre == null || !numRegistre.equals("") ) {
			//No fa falta límit de dates, ja que hi ha un número de registre.
			log.debug("Validam dates de forma laxa ja que numRegistre="+numRegistre+" fechaDesde="
					+fechaDesde+" fechaHasta="+fechaHasta);
			validarFecha(errores, fechaDesde, fechaHasta, false);
		} else {
			validarFecha(errores, fechaDesde, fechaHasta, true);
		}
		if (errores.size()==0) {
			validado=true;
		} else {
			validado=false;
		}
		
		if ( !numRegistre.equals("")  ) { 
			try {
				if (Integer.parseInt(numRegistre) <= 0) {
					
					errores.put("2","Nombre de registre incorrecte");
					validado = false;
				}
			} catch (NumberFormatException nfe) {
				errores.put("2","Nombre de registre no v\u00e0lid");
				validado = false;
			}
		}
		if ( (!numRegistre.equals("") && anyRegistre.equals("")) ||
				(numRegistre.equals("") && 
						!anyRegistre.equals("") ) ) {
			log.debug("numRegistre="+numRegistre+" anyRegistre="+anyRegistre);
			errores.put("4","S'ha de posar el n\u00famero de registre i l'any!");
			validado = false;
		}
		
		if ( anyRegistre == null || !anyRegistre.equals("")  ) {
			try {
				if (Integer.parseInt(anyRegistre) <= 0) {
					errores.put("3","Any de registre incorrecte");
					validado = false;
				}
			} catch (NumberFormatException nfe) {
				errores.put("2","Nombre de registre no v\u00e0lid");
				validado = false;
			}		
		}
		return validado;
	}
	


	/** 
	 * Parsejam d'hora agafada de BBDD
	 * @param hora En format String
	 * @return Hora parsejada amb el format: HH:MM:SS:sss
     */
	private String parsejarHora( String hora)  {
		
		switch (hora.length()) {
		case 9:
//			Del tipus: HH:mm:yy:zzz
			return hora.substring(0,2)+":"+hora.substring(2,4)+":"+hora.substring(4,6)+":"+hora.substring(6);
		case 8:
//			Del tipus: H:mm:yy:zzz
			return "0"+hora.substring(0,1)+":"+hora.substring(1,3)+":"+hora.substring(3,5)+":"+hora.substring(5);
		case 7:
//			Del tipus: 00:mm:yy:zzz
			return "00:"+hora.substring(0,2)+":"+hora.substring(2,4)+":"+hora.substring(4);
		case 6:
			//Del tipus: 00:0X:yy:zzz
			return "00:0"+hora.substring(0,1)+":"+hora.substring(1,3)+":"+hora.substring(3);
		case 5:
			//Del tipus: 00:00:yy:zzz
			return "00:00:"+hora.substring(0,2)+":"+hora.substring(2);
		case 4: 
//			Del tipus: 00:00:01:zzz
			return "00:00:0"+hora.substring(0,1)+":"+hora.substring(1);
		case 1:
		case 2:
		case 3:
			// Només hi ha milisegons!
			return "00:00:00"+hora;
		}
		return hora;
			
	}
	
	/**
	 * Recuperam els registres segons els filtres de cerca.
	 * @param usuario Usuari que fa la consulta
	 * @param sizePagina Nombre de registres a tornar
	 * @param pagina Nombre de pàgina a tornar
	 * @return Vector que conté les classes amb la informació de cada registre.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public Vector recuperar(ParametrosListadoAcceso parametros, String usuario, int sizePagina, int pagina) throws java.rmi.RemoteException {
        Session session = getSession();
		ScrollableResults rs=null;
		ScrollableResults rsHist=null;
		SQLQuery q = null;
		Query qHist = null;

		Hashtable errores = parametros.getErrores();
		String oficinaDesde=parametros.getOficinaDesde();
		String oficinaHasta=parametros.getOficinaHasta();
		String fechaDesde=parametros.getFechaDesde();
		String fechaHasta=parametros.getFechaHasta();
		String numRegistre=parametros.getNumRegistre();
		String anyRegistre=parametros.getAnyRegistre();
		String registreES=parametros.getRegistreES();
		
		usuario=usuario.toUpperCase();
		Vector registrosVector=new Vector();
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaEESS=null;        
		String fechaInicio="";
		String fechaFinal="";
		if ( !fechaDesde.equals("") ) {
			log.debug("fechaDesde="+fechaDesde);
			fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
			fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
		}
		try {
			
            // String sentenciaHql="select log.id.tipoAcceso, log.id.usuario, log.id.fecha, log.id.hora, log.id.numero, log.id.anyo, log.id.oficina, ofi.nombre " + 
            //              " from LogModificacionLopd log, Oficina ofi  " +
            //     " where log.id.oficina=ofi.codigo  and log.id.oficina>=? and id.oficina<=? " +
            // (!fechaDesde.equals("") ? " and log.id.fecha>=? and log.id.fecha<=? " : "" ) +
            // (!numRegistre.equals("") ? " and log.id.numero=?" : "" )+
            // (!anyRegistre.equals("") ? " and log.id.anyo=?" : "" )+
            // ( !numRegistre.equals("") || !anyRegistre.equals("") ? 
            //      " order by log.id.anyo, log.id.numero, log.id.fecha, log.id.hora" :
            //      " order by log.id.fecha, log.id.hora, log.id.anyo, log.id.numero ");

			String sentenciaSql="SELECT FZUTIPAC, FZUCUSU, FZUDATAC, FZUHORAC, FZUNUMEN, FZUANOEN, FZUCAGCO, FAADAGCO " +
			" FROM BZSALPD LEFT JOIN BAGECOM ON FAACAGCO=FZUCAGCO " +
			" WHERE FZUCAGCO>=? AND FZUCAGCO<=? " +
			(!fechaDesde.equals("") ? " AND FZUDATAC>=? AND FZUDATAC<=? " : "" ) +
			(!numRegistre.equals("") ? " AND FZUNUMEN=?" : "" )+
			(!anyRegistre.equals("") ? " AND FZUANOEN=?" : "" )+
			( !numRegistre.equals("") || !anyRegistre.equals("") ? 
					" ORDER BY FZUANOEN, FZUNUMEN, FZUDATAC, FZUHORAC" :
					" ORDER BY FZUDATAC, FZUHORAC, FZUANOEN, FZUNUMEN ");			

			q=session.createSQLQuery(sentenciaSql);
			q.addScalar("FZUTIPAC", Hibernate.STRING);
            q.addScalar("FZUCUSU" , Hibernate.STRING);			
            q.addScalar("FZUDATAC", Hibernate.INTEGER);
            q.addScalar("FZUHORAC", Hibernate.INTEGER);
            q.addScalar("FZUNUMEN", Hibernate.INTEGER);
            q.addScalar("FZUANOEN", Hibernate.INTEGER);
            q.addScalar("FZUCAGCO", Hibernate.INTEGER);
            q.addScalar("FAADAGCO", Hibernate.STRING);
            
			int contador=0;
			q.setInteger(contador++,Integer.parseInt(oficinaDesde));
			q.setInteger(contador++,Integer.parseInt(oficinaHasta));
			if ( !fechaDesde.equals("") ) {
				q.setInteger(contador++,Integer.parseInt(fechaInicio));
				q.setInteger(contador++,Integer.parseInt(fechaFinal));
			}
			if (!numRegistre.equals(""))
				q.setInteger(contador++,Integer.parseInt(numRegistre));
			if (!anyRegistre.equals(""))
				q.setInteger(contador++,Integer.parseInt(anyRegistre));
			
			if (pagina<=1 && parametros.isCalcularTotalRegistres()) {
				// La primera vegada, calculam el nombre màxim de registres que tornarà la consulta
				rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
                //	 Point to the last row in resultset.
			      rs.last();      
			      // Get the row position which is also the number of rows in the resultset.
			      int rowcount = rs.getRowNumber()+1;
			      // Reposition at the beginning of the ResultSet to take up rs.next() call.
			      rs.beforeFirst();
			     // log.debug("Total rows for the query using Scrollable ResultSet: "
			     //                    +rowcount);
			      parametros.setTotalFiles(String.valueOf(rowcount));
				}
			    
			    // Numero maximo de registros a devolver
				q.setMaxResults((sizePagina*pagina)+1);
			    rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			if (pagina>1) {
				rs.next();
				rs.scroll((sizePagina*(pagina-1))-1);
			}
			
			while (rs.next()) {
				RegistroESlopdSeleccionado registro=new RegistroESlopdSeleccionado();
				registro.setTipusAcces(rs.getString(0));
				registro.setUsuCanvi(rs.getString(1));			
				String fechaES=String.valueOf(rs.getInteger(2));
				try {
					fechaEESS=yyyymmdd.parse(fechaES);
					registro.setDataCanvi(ddmmyyyy.format(fechaEESS));
				} catch (Exception e) {
					registro.setDataCanvi(fechaES);
				}
				/**  TODO: PARSEJAR HORA */   
				registro.setHoraCanvi( parsejarHora(String.valueOf(rs.getInteger(3))) );
				registro.setNombreRegistre(String.valueOf(rs.getInteger(4)));
				registro.setAnyRegistre(String.valueOf(rs.getInteger(5)));
				registro.setOficinaRegistre(String.valueOf(rs.getInteger(6)));

				/* Aquí hem d'anar a l'històric.
				 IF WSLE-BHAGECO02 AND
				 W-FHACAGCO = WC-FHACAGCO AND
				 W-FHAFALTA <= W-FZAFENTR-1 AND
				 ((W-FZAFENTR-1 <= W-FHAFBAJA AND W-FHAFBAJA NOT = ZEROS) OR
				 W-FHAFBAJA = ZEROS)
				 MOVE W-FHADAGCO TO P3S-DAGCO
				 ELSE
				 PERFORM READR-Oficina01-ROficina
				 IF WSLE-Oficina01
				 MOVE W-FAADAGCO   TO P3S-DAGCO.
				 */
				String textoOficina=null;
			String sentenciaHqlHistOfi="select nombre from OficinaHistorico where id.codigo=? and id.fechaAlta<=? " +
			"and ( (fechaBaja>= ? and not fechaBaja=0) or fechaBaja = 0)";
				qHist=session.createQuery(sentenciaHqlHistOfi);
				qHist.setInteger(0,rs.getInteger(6));
				qHist.setInteger(1,Integer.valueOf(fechaES));
				qHist.setInteger(2,Integer.valueOf(fechaES));
				rsHist=qHist.scroll();
				if (rsHist.next()) {
					/* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
					textoOficina=rsHist.getString(0);
				} else {
					textoOficina=rs.getString(7);
					if (textoOficina==null) {
						textoOficina=" ";
					}
				}
				//  Tancam el preparedstatement i resultset de l'històric
				if (rsHist != null)
					rsHist.close();

				registro.setOficinaRegistreDesc(textoOficina);

				registrosVector.addElement(registro);
			}	
			
		    if (rs!=null) rs.close();
    		session.flush();
        } catch (HibernateException he) {
			errores.put("1","Error intern:"+he.getMessage());
			log.error("Error: "+he.getMessage());
            throw new EJBException(he);
        } finally {
            close(session);
        }

		return registrosVector;
	}

	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
	
	
	
}