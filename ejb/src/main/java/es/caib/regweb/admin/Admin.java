/*
 * Created on 1 de agost de 2007, 9:53
 */
package es.caib.regweb.admin;

import java.rmi.*;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import es.caib.regweb.RegwebException;


/**
 * Interfície per accedir al Bean que permet gestionar les autoritzacions dels usuaris al registre d'entrades 
 * @author  Sebastià Matas
 * @version 1.0
 */

public interface Admin  extends EJBObject  {
	
	public AutoritzacionsUsuariData getAutoritzacionsUsuari(String usuari) throws RemoteException;
	 
	public int deleteAutoritzacionsUsuari(String usuari) throws RemoteException, RegwebException;
	
	public int addAutoritzacioUsuari(String usuari, String tipusAut, String oficinaAut) throws RemoteException, RegwebException;
	
	public int altaOficina(String codOficina, String descOficina, String dataBaixa) throws RemoteException, RegwebException;

	public int altaOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataBaixa) throws RemoteException, RegwebException;
	
	public int actualitzaHistOficina(String codOficina, String descOficina, String dataAlta, String dataBaixa) throws RemoteException, RegwebException;
	
	public int actualitzaOficina(String codOficina, String descOficina, String dataBaixa) throws RemoteException, RegwebException;
    
	public int actualitzaOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataBaixa) throws RemoteException, RegwebException;
	
	public int altaHistOficina(String codOficina, String descOficina, String dataAlta) throws RemoteException, RegwebException;
	
	public int deleteOrgsOfi(String oficinaGestionar) throws RemoteException, RegwebException;
	
	public int addOrganismeOficina(String oficinaGestionar, String codiOrganisme) throws RemoteException, RegwebException;
	
	public int deleteNoRemsOfi(String oficinaGestionar) throws RemoteException, RegwebException;
	
	public int addNoRemetreOficina(String oficinaGestionar, String codiOrganisme) throws RemoteException, RegwebException;

	public Vector getOficina(String oficina)  throws RemoteException;

	public Vector getOficinaFisica(String oficina, String oficinaFisica)  throws RemoteException;

	public Vector getModelOfici(String model)  throws RemoteException;

	public Vector getModelRebut(String model)  throws RemoteException;

	public Vector getHistOficina(String oficina)  throws RemoteException;
	
	public Vector getHistOrganisme(String organisme)  throws RemoteException;
	
	public Vector getOrganisme( String codiOrganisme)  throws RemoteException;
		
	public Vector getOrganismes( )  throws RemoteException;
	
	public Vector getTotsOrganismes( )  throws RemoteException;
	
	public int altaOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataAltaOrg) throws RemoteException, RegwebException; 
	
	public int actualitzaOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataBaixaOrg) throws RemoteException, RegwebException;
	
	public int actualitzaHistOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataAltaOrg, String dataBaixaOrg
    		, String dataAltaOrgOld) throws RemoteException, RegwebException;
	
	public int altaHistOrganisme(String codOrganisme, String descCurtaOrganisme, String descLlargaOrganisme, String dataAlta) throws RemoteException, RegwebException;
	
    public EntitatData getEntitat( String codiEntitat, String subcodiEntitat )  throws RemoteException, RegwebException;
	
	public Vector getEntitats(String subcadenaCodigo, String subcadenaTexto) throws RemoteException;
	
	public int altaEntitat(String codEntidad, String codEntitat, String subcodEntitat, String descEntidad, String descEntitat, String dataBaixa) throws RemoteException, RegwebException;
	
	public int actualitzaEntitat(String codEntidad, String codEntitat, String subcodEntitat, String descEntidad, String descEntitat, String dataBaixa) throws RemoteException, RegwebException;
	
	public boolean existComptador(String anyGestionar, String oficinaGestionar) throws RemoteException, RegwebException;
	 
	public int altaComptador(String anyGestionar, String entradaSortida, String codiOficina) throws RemoteException, RegwebException;
	
	public String getComptadorOficina( String codiOficina, String ES, String any )  throws RemoteException, RegwebException;
	
	public AgrupacioGeograficaData getAgrupacioGeografica(String codiTipusAgruGeo, String codiAgruGeoGestionar) throws RemoteException, RegwebException;
	 
	public Collection getAgrupacionsGeografiques() throws RemoteException, RegwebException;

	public int altaAgrupacioGeografica(String codTipuAgruGeo, String codAgruGeo, String descAgruGeo, 
	    		String dataBaixa, String codTipusAgruGeoSuperior, String codAgruGeoSuperior, String codiPostal ) throws RemoteException, RegwebException ;
	 
	public int delAgrupacioGeografica(String codTipuAgruGeo, String codAgruGeo) throws RemoteException, RegwebException;
	 
	public TipusDocumentData getTipusDocument(String codiTipDoc) throws RemoteException, RegwebException;
	 
	public Vector getTipusDocuments() throws RemoteException;
	
	public int altaTipusDocument(String codTipDoc, String descTipDoc, String dataBaixa) throws RemoteException, RegwebException; 
		
    public int actualitzaTipusDocument(String codTipDoc, String descTipDoc, String dataBaixa ) throws RemoteException, RegwebException;
    
    public Municipi060Data getMunicipi060(String codiMun060) throws RemoteException, RegwebException;
    
    public Vector getTipusMunicipis060() throws RemoteException;
    
    public int altaMunicipi060(String codMun060, String descMun060, String dataBaixa) throws RemoteException, RegwebException;
    
    public int actualitzaMunicipi060(String codMun060, String descMun060, String dataBaixa ) throws RemoteException, RegwebException;

}