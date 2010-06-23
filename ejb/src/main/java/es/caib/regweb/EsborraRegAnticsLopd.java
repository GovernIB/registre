/*
 * Created on 19 de junio de 2002, 18:56
 */

package es.caib.regweb;

import java.rmi.*;

import javax.ejb.*;


/**
 * Interfície per accedir al Bean que permet fer consultes sobre el registre d'accessos al registre d'entrades 
 * @author  Sebastià Matas, basat en codi de FJMARTINEZ
 * @version 1.0
 */

public interface EsborraRegAnticsLopd  extends EJBObject  {
	
	public void esborraDadesAntiguesLOPD()  throws RemoteException;
	
}