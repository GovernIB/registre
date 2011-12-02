

package es.caib.regweb.logic.ejb;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.ejb.CreateException;

import org.apache.log4j.Logger;

/**
 * SessionBean per a esborrar registres antics de la LOPD 
 *
 * @ejb.bean
 *  name="logic/EsborraRegAnticsLopdFacade"
 *  jndi-name="es.caib.regweb.logic.EsborraRegAnticsLopdFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class EsborraRegAnticsLopdFacadeEJB extends HibernateEJB {
	

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	
	/** Crea una nova instància de EsborraRegAnticsLopd */
	public EsborraRegAnticsLopdFacadeEJB() {
	}
	
	/** 
	 * Esborram els registres antics del log de la LOPD
	 * @param nomTaula a esborrar
	 */
	private void esborraDadesAntiguesLOPDEntrades (String nomTaula) throws java.rmi.RemoteException{

		Session session = getSession();

		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		String DATE_FORMAT = "yyyyMMdd";
		java.text.SimpleDateFormat sdf = 
			new java.text.SimpleDateFormat(DATE_FORMAT);
		/*
		 ** on some JDK, the default TimeZone is wrong
		 ** we must set the TimeZone manually!!!
		 **     sdf.setTimeZone(TimeZone.getTimeZone("EST"));
		 */
		sdf.setTimeZone(TimeZone.getDefault());          
		log.debug("Ara: " + sdf.format(cal.getTime()));
//		Creamos calendario con la fecha actual
		GregorianCalendar calendario = new GregorianCalendar();
		
		//	Restam dos anys i 1 dia. Això ens assegurarm que no esborram
		// 	cap registre dins dels dos anys que s'ha de tenir guardada 
		//  la informació
		//  TODO: Hem de tenir en compte els anys bisests?
		// 2 anys i un dia 
		calendario.add(Calendar.DATE, -(365*2+1));
		// Test calendario.add(Calendar.DATE, -(39));
		log.info("Taula de la que s'esborraran els registres antics: " + nomTaula);
		log.info("Data a esborrar: " + Integer.parseInt(sdf.format(calendario.getTime())));
		
		try {
			
			/* Aquí esborram els registres. */

			int contador=0;
			String sentenciaHql="delete from " +nomTaula+" where id.fecha<? ";
			log.debug(sentenciaHql);
			Query query = session.createQuery(sentenciaHql);
			query.setInteger(contador++,Integer.parseInt(sdf.format(calendario.getTime())) );
			int afectats=query.executeUpdate();

			log.info("Esborrats "+afectats+" registres de la taula "+nomTaula);

			session.flush();
		} catch (Exception e) {
			log.error("Error: "+e.getMessage());
			e.printStackTrace();
        } finally {
            close(session);
        }
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
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
	public void esborraDadesAntiguesLOPD() throws java.rmi.RemoteException {
		
		log.debug("Timout expired!");
		log.debug("Aquí hauriem d'esborrar els registres antics de les taules de registre d'accessos.!");

		esborraDadesAntiguesLOPDEntrades("LogEntradaLopd");
		esborraDadesAntiguesLOPDEntrades("LogSalidaLopd" );
		esborraDadesAntiguesLOPDEntrades("LogModificacionLopd");
		//esborraDadesAntiguesLOPDSortides();
		//esborraDadesAntiguesLOPDVisats();
	}
	
	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
}