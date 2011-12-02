package es.caib.regweb.logic.ejb;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import javax.ejb.EJBException;
import org.apache.log4j.Logger;


/**
 * SessionBean per a manteniment de registres d'entrada
 *
 * @ejb.bean
 *  name="logic/LocalitzadorsDocsElectronicsFacade"
 *  jndi-name="es.caib.regweb.logic.LocalitzadorsDocsElectronicsFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class LocalitzadorsDocsElectronicsFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	/**
	 * SEPARADOR_LOCDOS caracter utilizado para separar las urls de los docs firmados registrados
	 */
	private static final String SEPARADOR_LOCDOS = ",";

	
	private static final String IDENTIFICADOR_ENTRADAS = "E";
	private static final String IDENTIFICADOR_SALIDAS = "S";

	/**
	 * @throws EJBException
	 * 
	 */
	private String LeerListaLocalizadoresDoc( int anoEntrada, int numeroEntrada, int oficina, String tipus) throws EJBException {
		SQLQuery q = null;
		Session session = getSession();
		ScrollableResults rs = null;
		StringBuffer result = new StringBuffer();
		String rtdo = null;
		
		int i=0;

		try{	
			String sentenciaHql = " SELECT LOC_DOC" + 
			" FROM BZDOCLOC " +
			" WHERE LOC_ANY=? AND LOC_OFI=? AND LOC_NUMREG=? AND LOC_TIPUS=? " +
			" ORDER BY LOC_NUMDOC ASC ";   

			q=session.createSQLQuery(sentenciaHql);
			q.addScalar("LOC_DOC", Hibernate.STRING);

			q.setInteger(0,anoEntrada);
			q.setInteger(1,oficina);
			q.setInteger(2,numeroEntrada);
			q.setString (3, tipus);
			rs=q.scroll();
			while (rs.next()){
				result.append((i++==0)?"":SEPARADOR_LOCDOS);
				result.append(rs.getString(0).trim());      	
			}
		}catch(Exception e){
			throw new EJBException(e);
		}finally{
			if (rs!=null) rs.close();
			close(session);
		}
		rtdo=(result.toString().equals(""))?null:result.toString();
		log.debug("ListaLocDocs leida:"+rtdo);
		return rtdo;
	}
	
	/**
	 * @throws EJBException
	 * 
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public String LeerListaLocalizadoresDocEntrada( int anoEntrada, int numeroEntrada, int oficina) throws EJBException {
		return LeerListaLocalizadoresDoc(  anoEntrada,  numeroEntrada,  oficina, IDENTIFICADOR_ENTRADAS);
	}
	
	/**
	 * @throws EJBException
	 * 
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public String LeerListaLocalizadoresDocSalida( int anoEntrada, int numeroEntrada, int oficina) throws EJBException {
		return LeerListaLocalizadoresDoc(  anoEntrada,  numeroEntrada,  oficina, IDENTIFICADOR_SALIDAS);
	}
	
	/**
	 * @throws EJBException
	 */
	private String[] LeerArrayListaLocalizadoresDoc( int anoEntrada, int numeroEntrada, int oficina, String tipus) throws EJBException {
		String localitzadorsDocs;
		String[] rtdo= null;

		localitzadorsDocs = LeerListaLocalizadoresDoc(anoEntrada,numeroEntrada,oficina,tipus);		
	    try{
	    	rtdo = localitzadorsDocs.split(",");
	    }catch( Exception ex){
	    	rtdo=null;
	    }
		return rtdo;
	}
	/**
	 * @throws EJBException
	 * 
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public String[] LeerArrayListaLocalizadoresDocEntrada( int anoEntrada, int numeroEntrada, int oficina) throws EJBException {
		return LeerArrayListaLocalizadoresDoc(  anoEntrada,  numeroEntrada,  oficina, IDENTIFICADOR_ENTRADAS);
	}
	/**
	 * @throws EJBException
	 * 
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public String[] LeerArrayListaLocalizadoresDocSalida( int anoEntrada, int numeroEntrada, int oficina) throws EJBException {
		return LeerArrayListaLocalizadoresDoc(  anoEntrada,  numeroEntrada,  oficina, IDENTIFICADOR_SALIDAS);
	}
	/**
	 * @throws EJBException
	 */
	private boolean ExisteListaLocalizadoresDoc(int anoEntrada, int numeroEntrada, int oficina, String tipus) throws EJBException {
		Session session = getSession();
		SQLQuery q = null;
		ScrollableResults rs = null;
		boolean result = false;

		try{	
			String sentenciaHql = " SELECT DISTINCT 1" + 
			" FROM BZDOCLOC " +
			" WHERE LOC_ANY=? AND LOC_OFI=? AND LOC_NUMREG=? AND LOC_TIPUS=? ";  

			q=session.createSQLQuery(sentenciaHql);

			q.setInteger(0,anoEntrada);
			q.setInteger(1,oficina);
			q.setInteger(2,numeroEntrada);
			q.setString(3, tipus);
			rs=q.scroll();
			if (rs.next()){
				result=true;      	
			}
		}catch(Exception e){
			throw new EJBException(e);
		}finally{
			if (rs!=null) rs.close();
			close(session);
		}
		log.debug("Consultado si existe localizador Doc electrónicos. Reg ("+" tipus:"+tipus+" of:"+oficina+" codi "+numeroEntrada+"/"+anoEntrada+") -"+"Resultado:"+result);
		return result;
	}
	
	/**
	 * @throws EJBException
	 * 
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public boolean ExisteListaLocalizadoresDocEntrada(int anoEntrada, int numeroEntrada, int oficina) throws EJBException {
		return ExisteListaLocalizadoresDoc( anoEntrada,  numeroEntrada,  oficina,IDENTIFICADOR_ENTRADAS);
	}
	
	/**
	 * @throws EJBException
	 * 
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public boolean ExisteListaLocalizadoresDocSalida(int anoEntrada, int numeroEntrada, int oficina) throws EJBException {
		return ExisteListaLocalizadoresDoc( anoEntrada,  numeroEntrada,  oficina,IDENTIFICADOR_SALIDAS);
	}
    /**
     * @throws EJBException
     *
	 */	
   private void GrabarLocalitzadorDocs(Session session, int fzaanoe, int fzanume, int fzacagc, String locDocs, String tipus) throws EJBException {
	   SQLQuery ms = null;	   
	   int contador = 0;
	   String arrayLocDos[] = locDocs.split(SEPARADOR_LOCDOS);	
	   
	   log.debug("Grabar localizadoresDocs=> LOC_ANY:"+fzaanoe+", LOC_OFI:"+fzacagc+", LOC_NUMREG:"+fzanume+", LOC_TIPUS:"+tipus+".");
	   
	   for( contador=0;contador<arrayLocDos.length;contador++){
		   try{
			   String insertBZNCORR="INSERT INTO BZDOCLOC (LOC_ANY, LOC_OFI, LOC_NUMREG, LOC_NUMDOC, LOC_DOC,LOC_TIPUS)" +
	           "VALUES (?,?,?,?,?,?)";
			   ms=session.createSQLQuery(insertBZNCORR);
			   ms.setInteger(0, fzaanoe);
			   ms.setInteger(1,fzacagc);
			   ms.setInteger(2,fzanume);
			   ms.setInteger(3, contador);
			   ms.setString(4,arrayLocDos[contador]);
			   ms.setString(5,tipus);
			   ms.executeUpdate();
			 
		   }catch(Exception e){
			   throw new EJBException(e);
		   }finally{
		   }
	   }
	   log.debug("Grabados "+contador+" localizadores de documentos electrónicos para el registro "+fzacagc+"-"+fzanume+"/"+fzaanoe);
   }

   /**
    * @throws EJBException
    *
    * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */	
  public void GrabarLocalitzadorDocsEntrada(Session session, int fzaanoe, int fzanume, int fzacagc, String locDocs) throws EJBException {
	  GrabarLocalitzadorDocs(session,fzaanoe, fzanume, fzacagc, locDocs, IDENTIFICADOR_ENTRADAS);
  }
  
  /**
   * @throws EJBException
   *
   * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */	
  public void GrabarLocalitzadorDocsSalida(Session session, int fzaanoe, int fzanume, int fzacagc, String locDocs) throws EJBException {
	  GrabarLocalitzadorDocs(session,fzaanoe, fzanume, fzacagc, locDocs, IDENTIFICADOR_SALIDAS);
 }
}