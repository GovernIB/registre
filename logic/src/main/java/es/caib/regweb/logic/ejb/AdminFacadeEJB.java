package es.caib.regweb.logic.ejb;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.CreateException;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import es.caib.regweb.logic.helper.AgrupacioGeograficaData;
import es.caib.regweb.logic.helper.AutoritzacionsOficinaData;
import es.caib.regweb.logic.helper.AutoritzacionsUsuariData;
import es.caib.regweb.logic.helper.EntitatData;
import es.caib.regweb.logic.helper.ModeloDocumentoData;
import es.caib.regweb.logic.helper.Municipi060Data;
import es.caib.regweb.logic.helper.RegwebException;
import es.caib.regweb.logic.helper.TipusDocumentData;
import es.caib.regweb.model.AgrupacionGeografica;
import es.caib.regweb.model.AgrupacionGeograficaId;
import es.caib.regweb.model.Autorizacion;
import es.caib.regweb.model.AutorizacionId;
import es.caib.regweb.model.CodigoPostal;
import es.caib.regweb.model.CodigoPostalId;
import es.caib.regweb.model.Contador;
import es.caib.regweb.model.ContadorId;
import es.caib.regweb.model.EntidadRemitente;
import es.caib.regweb.model.EntidadRemitenteId;
import es.caib.regweb.model.ModeloEmail;
import es.caib.regweb.model.ModeloEmailId;
import es.caib.regweb.model.Municipio060;
import es.caib.regweb.model.Oficina;
import es.caib.regweb.model.OficinaFisica;
import es.caib.regweb.model.OficinaFisicaHistorico;
import es.caib.regweb.model.OficinaFisicaHistoricoId;
import es.caib.regweb.model.OficinaFisicaId;
import es.caib.regweb.model.OficinaHistorico;
import es.caib.regweb.model.OficinaHistoricoId;
import es.caib.regweb.model.OficinaOrganismo;
import es.caib.regweb.model.OficinaOrganismoId;
import es.caib.regweb.model.OficinaOrganismoNoRemision;
import es.caib.regweb.model.OficinaOrganismoNoRemisionId;
import es.caib.regweb.model.OficinaOrganismoPermetEnviarEmail;
import es.caib.regweb.model.OficinaOrganismoPermetEnviarEmailId;
import es.caib.regweb.model.Organismo;
import es.caib.regweb.model.OrganismoHistorico;
import es.caib.regweb.model.OrganismoHistoricoId;
import es.caib.regweb.model.TipoDocumento;
import es.caib.regweb.model.TipoDocumentoId;
import es.caib.regweb.model.UnidadDeGestion;
import es.caib.regweb.model.UnidadDeGestionId;

/**
 * SessionBean para módulo de administración
 *
 * @ejb.bean
 *  name="logic/AdminFacade"
 *  jndi-name="es.caib.regweb.logic.AdminFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */

public abstract class AdminFacadeEJB extends HibernateEJB {


	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(this.getClass());


    /**
     * Actualitza la informació de l'entitat donada
     * @param codEntitat Codi d'entitat a donar d'alta (català)
     * @param subcodEntitat Subcodi d'entitat
     * @param descEntidad Descripció (castellà)
     * @param descEntitat Descripció (català)
     * @param dataBaixa Data de baixa
     * @return
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaEntitat(String codEntidad, String codEntitat, String subcodEntitat, String descEntidad, String descEntitat, String dataBaixa) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			
			String sentenciaHql="UPDATE EntidadRemitente SET codigoCatalan=?, nombre=?, nombreCatalan=?, id.fechaBaja=? "+
					"WHERE id.codigo=? AND id.numero=?";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0, codEntitat);
			query.setString(1, descEntidad);
			query.setString(2, descEntitat);
			query.setInteger(3, Integer.valueOf(dataBaixa));
			query.setString(4, codEntidad);
			query.setInteger(5, Integer.valueOf(subcodEntitat));
			
			int afectats=query.executeUpdate();
			if (afectats<=0){
				throw new RegwebException("ERROR: No s'ha pogut fer l'update a EntidadRemitente, valors:("+codEntidad+","+codEntitat+","+subcodEntitat+","
						+descEntidad+","+descEntitat+","+dataBaixa+")");
			}

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

	
	/**
     * Actualitza la informació de l'històric de l'oficina 
     * @param codOficina Codi d'oficina a modificar l'històric
     * @param descOficina Descripció de l'oficina 
     * @param dataAlta Data d'alta
     * @param dataBaixa Data de baixa
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaHistOficina(String codOficina, String descOficina, String dataAlta, String dataBaixa) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			String sentenciaHql="UPDATE OficinaHistorico SET nombre=?,fechaBaja=? " +
					"WHERE id.codigo=? AND id.fechaAlta=?";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,descOficina);
			query.setInteger(1, Integer.valueOf(dataBaixa));
			query.setInteger(2, Integer.valueOf(codOficina));
			query.setInteger(3, Integer.valueOf(dataAlta));
			// NO EXECUTAM FINS TENIR CLAR TOT EL QUE S'HA DE FER! 	
			
			int afectats=query.executeUpdate();
			if (afectats<=0){
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina ("+codOficina+","+descOficina+","+dataBaixa+") a Oficina.");
			}
			log.debug("actualitzam l'hist\u00f2ric d'oficina");

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

	/**
     * Actualitza la informació de l'històric de l'oficina fisica
     * @param codOficina Codi d'oficina a modificar l'històric
     * @param codOficinaFisica Codi d'oficina fisica a modificar l'històric
     * @param descOficina Descripció de l'oficina fisica
     * @param dataAlta Data d'alta
     * @param dataBaixa Data de baixa
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaHistOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataAlta, String dataBaixa) throws RemoteException, RegwebException { 
    	Session session = getSession();
		
		try {

			String sentenciaHql="UPDATE OficinaFisicaHistorico "+
			" SET nombre=?, fechaBaja=? " +
			" WHERE id.codigoOficina=? AND id.codigoOficinaFisica=? AND id.fechaAlta=?";
			Query query=session.createQuery(sentenciaHql);
   
			query.setString(0,descOficina);
			query.setInteger(1, new Integer(dataBaixa));
			query.setString(2,codOficina);
			query.setString(3,codOficinaFisica);
			query.setInteger(4, new Integer(dataAlta));
			// NO EXECUTAM FINS TENIR CLAR TOT EL QUE S'HA DE FER! 	
			
			int afectats=query.executeUpdate();
			if (afectats<=0){

				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina fisica ("+codOficina+","+codOficinaFisica+","+descOficina+","+dataBaixa+") a bzofifis.");
			}
			log.debug("actualitzam l'històric d'oficina fisica");
		} catch (Exception e) {
			throw new RegwebException(e.getMessage());
		} finally {
			close(session);
		}
    	return 0;    
    }

	/**
     * Actualitza l'històric de l'organisme
     * @param codiOrganisme Codi de l'organisme a modificar (de l'històric)
     * @param descCurtaOrg Descripció curta
     * @param descLlargaOrg Descripció llarga
     * @param dataAltaOrg Data d'alta 
     * @param dataBaixaOrg Data de baixa
     * @param dataAltaOrgOld Data antiga de l'organisme (juntament amb codiOrganisme és la clau primària)
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaHistOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataAltaOrg, String dataBaixaOrg) throws RemoteException, RegwebException { 
    	Session session = getSession();

    	try {
    		//TODO: Gestionar la transaccionalitat!
    		
    		String sentenciaHql="UPDATE OrganismoHistorico SET nombreCorto=?, nombreLargo=?, fechaBaja=? " +
    		" WHERE id.codigo=? AND id.fechaAlta=? ";
    		Query query=session.createQuery(sentenciaHql);
    		query.setString(0, descCurtaOrg);
    		query.setString(1, descLlargaOrg);
    		query.setInteger(2, Integer.valueOf(dataBaixaOrg));
    		query.setInteger(3, Integer.valueOf(codiOrganisme));
    		query.setInteger(4, Integer.valueOf(dataAltaOrg));
    		
    		int afectats=query.executeUpdate();
    		if (afectats<=0){
    			throw new RegwebException("ERROR: No s'ha pogut actualitzar l'organisme ("+codiOrganisme+","+dataAltaOrg+") a OrganismoHistorico.");
    		}
    		log.debug("actualitzam l'organisme");	

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
	/**
     * Actualitza el model de email
     * @param idioma Idioma del email
     * @param tipo tipus de email
     * @param titulo Titulo del email
     * @param cuerpo Cuerpo del email
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaModelEmail(String idioma, String tipo, String titulo, String cuerpo) throws RemoteException, RegwebException { 
    	Session session = getSession();
    	int afectats=0;
		try {
			
			String sentenciaHql=" UPDATE ModeloEmail "+
			" SET titulo = ?,  cuerpo = ? " +
			" WHERE id.idioma = ? and id.tipo = ?   ";

			Query query=session.createQuery(sentenciaHql);
			
			query.setString(0,titulo);	
			log.debug("cuerpo:"+cuerpo.length());
			query.setString(1,cuerpo);
			log.debug("titulo:"+titulo.length());
			query.setString(2,idioma);					
			query.setString(3, tipo);
			
			afectats=query.executeUpdate();
			if (afectats<=0){
				log.debug("ERROR: No s'ha pogut actualitzar el model ("+idioma+"-"+tipo+"). No s'ha trobat.");
				throw new RegwebException("ERROR: No s'ha pogut actualitzar el model ("+idioma+"-"+tipo+"). No s'ha trobat.");
			}
			
		} catch (Exception e) {
			log.error("ERROR: No s'ha pogut actualitzar el model ("+idioma+"-"+tipo+")",e);
			throw new RegwebException(e.getMessage());
		} finally {
			close(session);
		}
    	return 0;    
    }
	
	/**
     * Actualitza la informació del Municipi 060.
     * @param codMun060 Codi tipus document
     * @param descMun060 Descripció 
     * @param dataBaixa Data de baixa
     * @return
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaMunicipi060(String codMun060, String descMun060, String dataBaixa ) throws RemoteException, RegwebException { 
		Session session = getSession();

		try {
			
			String sentenciaHql="UPDATE Municipio060 SET nombreMunicipio=? , fechaBaja=? " +
					"WHERE codigoMunicipio=?";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,descMun060);
			query.setInteger(1, new Integer(dataBaixa) );
			query.setString(2, codMun060);
			int afectats=query.executeUpdate();
			if (afectats<=0){
				throw new RegwebException("ERROR: No s'ha pogut modificar el municipi ("+codMun060+","+descMun060+","+dataBaixa+") a Municipio060.");
			}

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
	/**
     * Actualitza l'oficina
     * @param codOficina Codi d'oficina a modificar
     * @param descOficina Descripció de l'oficina 
     * @param dataBaixa Data de baixa 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaOficina(String codOficina, String descOficina, String dataBaixa) throws RemoteException, RegwebException { 
    	Session session = getSession();    	
    	
    	try {    		
    		String sentenciaHql="UPDATE Oficina SET nombre=?, fechaBaja=? " +
    		"WHERE codigo=?";
    		Query query=session.createQuery(sentenciaHql);
    		query.setString(0,descOficina);
    		query.setInteger(1, Integer.valueOf(dataBaixa));
    		query.setInteger(2, Integer.valueOf(codOficina));
    		
    		int afectats=query.executeUpdate();
    		if (afectats<=0){
    			throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina ("+codOficina+","+descOficina+","+dataBaixa+") a Oficina.");
    		}
    		log.debug("actualitzam l'oficina");	
    		// Si la data de baixa és "0" HEM DE DONAR PERMISOS DE LECTURA A ENTRADES I SORTIDES D'AQUESTA OFICINA A TOTHOM!!!
    		if (dataBaixa.equals("0")) 
    		    autoritzaConsultaTotsUsuaris(codOficina);

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
	/**
     * Actualitza l'oficina fisica
     * @param codOficina Codi d'oficina a modificar
     * @param codOficina Codi d'oficina fisica a modificar
     * @param descOficina Descripció de l'oficina 
     * @param dataBaixa Data de baixa 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataBaixa) throws RemoteException, RegwebException { 
    	Session session = getSession();
    	
    	try {
    		
    		String sentenciaHql="UPDATE OficinaFisica SET nombre=? , fechaBaja=? " +
    		    "WHERE id.oficina.codigo=? AND id.codigoOficinaFisica=?";
    		Query query=session.createQuery(sentenciaHql);
    		query.setString(0,descOficina);
    		query.setString(1,dataBaixa);
    		query.setInteger(2,Integer.valueOf(codOficina));
    		query.setInteger(3,Integer.valueOf(codOficinaFisica));
    		
    		int afectats=query.executeUpdate();
    		if (afectats<=0){
    			throw new RegwebException("ERROR: No s'ha pogut actualitzar l'oficina ("+codOficina+","+codOficinaFisica+","+descOficina+","+dataBaixa+") a Oficina.");
    		}
    		log.debug("actualitzam l'oficina fisica");	

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
	


	/**
     * Actualitza la informació de l'organisme
     * @param codiOrganisme Codi de l'organisme a modificar
     * @param descCurtaOrg Descripció curta
     * @param descLlargaOrg Descripció lalrga
     * @param dataBaixaOrg Data de baixa
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataBaixaOrg) throws RemoteException, RegwebException { 
    	Session session = getSession();

    	try {    		
    		String sentenciaHql="UPDATE Organismo SET nombreCorto=?, nombreLargo=?, fechaBaja=? WHERE codigo=? ";
    		Query query=session.createQuery(sentenciaHql);
    		query.setString(0,descCurtaOrg);
    		query.setString(1,descLlargaOrg);
    		query.setInteger(2, Integer.valueOf(dataBaixaOrg));
    		query.setInteger(3, Integer.valueOf(codiOrganisme));
    		
    		int afectats=query.executeUpdate();
    		if (afectats<=0){
    			throw new RegwebException("ERROR: No s'ha pogut actualitzar l'organisme ("+codiOrganisme+") a Organismo.");
    		}
    		log.debug("actualitzam l'organisme");	

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
	

	/**
     * Actualitza la informació del tipus de document.
     * @param codTipDoc Codi tipus document
     * @param descTipDoc Descripció 
     * @param dataBaixa Data de baixa
     * @return
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaTipusDocument(String codTipDoc, String descTipDoc, String dataBaixa ) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			
			String sentenciaHql="UPDATE TipoDocumento SET nombre=? , id.fechaBaja=? " +
					"WHERE id.codigo=?";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,descTipDoc);
			query.setInteger(1, new Integer(dataBaixa) );
			query.setString(2, codTipDoc);
			int afectats=query.executeUpdate();
			if (afectats<=0){
				throw new RegwebException("ERROR: No s'ha pogut modificar el tipus de document ("+codTipDoc+","+descTipDoc+","+dataBaixa+") a TipoDocumento.");
			}

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

	/**
     * Actualitza la informació de la unitat de gestió
     * @param codiOficina Codi oficina de registre
     * @param codiUnitat Codi unitat de gestió dins de l'oficina de registre
     * @param nomUnitat Nom de la unitat
     * @param email Adreça de correu electrònic
     * @param actiu Valors: 'S' si esta activa, 'N' sino
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int actualitzaUnitatGestio(int codiOficina, int codiUnitat, String nomUnitat, String email, String actiu) throws RemoteException, RegwebException { 
    	Session session = getSession();
    	int afectats=0;
		try {
			
			String sentenciaHql=" UPDATE UnidadDeGestion "+
			" SET nombre = ?,  actiu = ?, direccionEmail = ?" +
			" WHERE id.codigoUnidad = ? and id.codigoOficina = ?   ";
			
			//Invertimos el valor del parametro actiu
			//actiu =  (actiu.equalsIgnoreCase("S")?"N":"S");
		
			
			Query query=session.createQuery(sentenciaHql);
			
			query.setString(0,nomUnitat);							
			query.setString(1,actiu);
			query.setString(2,email);					
			query.setInteger(3, codiUnitat);
			query.setInteger(4,codiOficina);
			
			afectats=query.executeUpdate();
			if (afectats<=0){
				log.debug("ERROR: No s'ha pogut donar d'actualitzar la unitat de gestió ("+codiOficina+"-"+codiUnitat+", "+nomUnitat+", "+email+")");
				throw new RegwebException("ERROR: No s'ha pogut donar d'actualitzar la unitat de gestió ("+codiOficina+"-"+codiUnitat+", "+nomUnitat+", "+email+")");
			}
			log.debug("Actualitzam unitat organica");
		} catch (Exception e) {
			log.error("ERROR",e);
			throw new RegwebException(e.getMessage());
		} finally {
			close(session);
		}
    	return 0;    
    }

    /**
     * Autoritza a l'usuari a l'oficina i tipus d'entrada donats
     * @param usuari Codi d'usuari a autoritzar
     * @param tipusAut Tipus d'autorització ("AE","CE","CS","AS")
     * @param oficinaAut Codi d'oficina on autoritzar
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int addAutoritzacioUsuari(String usuari, String tipusAut, String oficinaAut) throws RemoteException, RegwebException{ 
    	
		Session session = getSession();
		
		try {
			Autorizacion aut = new Autorizacion(new AutorizacionId(usuari, tipusAut, Integer.valueOf(oficinaAut)));
			session.save(aut);

    		session.flush();


		} catch (Exception e) {
            log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

	/**
     * Relaciona per permetre enviar un email d'una oficina donada a un l'organisme 
     * @param oficinaGestionar Codi d'oficina
     * @param codiOrganisme Codi d'organisme
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int addfiOrgEmail(int oficinaGestionar, int codiOrganisme) throws RemoteException, RegwebException{ 
    	
		Session session = getSession();
		
		try {
            OficinaOrganismoPermetEnviarEmail orgOfi = new OficinaOrganismoPermetEnviarEmail(new OficinaOrganismoPermetEnviarEmailId(oficinaGestionar, codiOrganisme));
			session.save(orgOfi);
			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	   	
    }

	/**
     * Relaciona per a no remetre l'oficina donada amb l'organisme 
     * @param oficinaGestionar Codi d'oficina
     * @param codiOrganisme Codi d'organisme
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int addNoRemetreOficina(String oficinaGestionar, String codiOrganisme) throws RemoteException, RegwebException{ 
    	
		Session session = getSession();
		
		try {
            Oficina oficina = (Oficina) session.load(Oficina.class, Integer.valueOf(oficinaGestionar));
            Organismo organismo = (Organismo) session.load(Organismo.class, Integer.valueOf(codiOrganisme));

            OficinaOrganismoNoRemision orgOfi = new OficinaOrganismoNoRemision(new OficinaOrganismoNoRemisionId(oficina, organismo));
			session.save(orgOfi);

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    	
    }
	
	/**
     * Relaciona l'organisme amb l'oficina donada
     * @param oficinaGestionar Codi d'oficina
     * @param codiOrganisme Codi d'organisme
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int addOrganismeOficina(String oficinaGestionar, String codiOrganisme) throws RemoteException, RegwebException{ 
    	
		Session session = getSession();
		
		try {
            Oficina oficina = (Oficina) session.load(Oficina.class, Integer.valueOf(oficinaGestionar));
            Organismo organismo = (Organismo) session.load(Organismo.class, Integer.valueOf(codiOrganisme));

            OficinaOrganismo orgOfi = new OficinaOrganismo(new OficinaOrganismoId(oficina, organismo));
			session.save(orgOfi);

    		session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    	
    }

	/**
     * Alta de la agrupació geogràfica
     * @param codTipuAgruGeo Codi tipus agrupació geogràfica
     * @param codAgruGeo Codi agrupació geogràfica
     * @param descAgruGeo Descripció agrupació geogràfica 
     * @param dataBaixa Data de baixa 
     * @param codTipusAgruGeoSuperior Codi tipus agrupació geogràfica superior
     * @param codAgruGeoSuperior Codi agrupació geogràfica superior
     * @param codiPostal Codig postal
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaAgrupacioGeografica(String codTipuAgruGeo, String codAgruGeo, String descAgruGeo, String dataBaixa, String codTipusAgruGeoSuperior, String codAgruGeoSuperior, String codiPostal ) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
		    String sentenciaHql="from AgrupacionGeografica where id.tipo=? and id.codigo=? ";
			Query query = session.createQuery(sentenciaHql);
			query.setInteger(0, Integer.valueOf(codTipusAgruGeoSuperior) );
			query.setInteger(1, Integer.valueOf(codAgruGeoSuperior) );
			
		    AgrupacionGeografica padre = (AgrupacionGeografica) query.uniqueResult();
            
            AgrupacionGeografica ag = new AgrupacionGeografica(new AgrupacionGeograficaId(Integer.valueOf(codTipuAgruGeo), Integer.valueOf(codAgruGeo)),
                                descAgruGeo,
                                Integer.valueOf(dataBaixa),
                                padre);
            session.save(ag);

			log.debug("Insertada agrupaci\u00f3 geogr\u00e0fica  ("+codTipuAgruGeo+","+codAgruGeo+","
				+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+") (update count=1) a BAGRUGE)");
				
		    CodigoPostal cp = new CodigoPostal(new CodigoPostalId(ag), Integer.valueOf(codiPostal));
            session.save(cp);
			
			log.debug("Insertada agrupaci\u00f3 geogr\u00e0fica  ("+codTipuAgruGeo+","+codAgruGeo+","
				+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+","+codiPostal+") (update count=1) a AgrupacionGeografica)");

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

	/**
     * Inicialització comptador
     * @param anyGestionar any del comptador
     * @param entradaSortida tipus d'entrada/sortida del comptador
     * @param codiOficina codi de l'oficina del comptador
     * @return
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaComptador(String anyGestionar, String entradaSortida, String codiOficina) throws RemoteException, RegwebException { 
		Session session = getSession();
		
	    
		try {
			Contador cont = new Contador(new ContadorId(Integer.valueOf(anyGestionar), entradaSortida, Integer.valueOf(codiOficina)), 0);
			session.save(cont);
			log.debug("Insertat ("+anyGestionar+","+entradaSortida+","+codiOficina+") (update count=1) a Contador");
		

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
	/**
     * Actualitza el model de email
     * @param idioma Idioma del email
     * @param tipo tipus de email
     * @param titulo Titulo del email
     * @param cuerpo Cuerpo del email
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaModelEmail(String idioma, String tipo, String titulo, String cuerpo) throws RemoteException, RegwebException { 
    	Session session = getSession();
    	
		try {
			ModeloEmail email = new ModeloEmail(new ModeloEmailId(idioma, tipo), titulo, cuerpo );
			session.save(email);

			session.flush();
		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;     
    }

    /**
     * Alta entitat
     * @param codEntidad Codi d'entitat a donar d'alta (castellà)
     * @param codEntitat Codi d'entitat a donar d'alta (català)
     * @param subcodEntitat Subcodi d'entitat
     * @param descEntidad Descripció (castellà)
     * @param descEntitat Descripció (català)
     * @param dataBaixa Data de baixa
     * @return 
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaEntitat(String codEntidad, String codEntitat, String subcodEntitat, String descEntidad, String descEntitat, String dataBaixa) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			EntidadRemitente ent = new EntidadRemitente(new EntidadRemitenteId(codEntidad, Integer.valueOf(subcodEntitat), Integer.valueOf(dataBaixa)),
			                        codEntitat, descEntidad, descEntitat);
			session.save(ent);

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

    /**
     * Alta de històric d'oficina
     * @param codOficina Codi d'oficina
     * @param descOficina Descripció de l'oficina
     * @param dataAlta Data d'alta de l'oficina (juntament amb codOficina és la clau primària)
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaHistOficina(String codOficina, String descOficina, String dataAlta) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
            OficinaHistorico ofi = new OficinaHistorico(new OficinaHistoricoId(Integer.valueOf(codOficina), Integer.valueOf(dataAlta)),
                                descOficina, 0);
            session.save(ofi);
			log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count=1) a OficinaHistorico)");

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

    /**
     * Alta de hist�ric d'oficina fisica
     * @param codOficina Codi d'oficina
     * @param codOficinaFisica Codi d'oficina fisica
     * @param descOficina Descripci� de l'oficina fisica
     * @param dataAlta Data d'alta de l'oficina fisica (juntament amb codOficina i codOficinaFisica �s la clau prim�ria)
     * @return torna 0 si ha anat b� (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaHistOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataAlta) throws RemoteException, RegwebException { 
    	Session session = getSession();
		
		try {					
			OficinaFisicaHistorico ofiHistHist = new OficinaFisicaHistorico( new OficinaFisicaHistoricoId(Integer.valueOf(codOficina),Integer.valueOf(codOficinaFisica),Integer.valueOf(dataAlta),0),descOficina,Integer.valueOf(0));
			session.save(ofiHistHist);
			session.flush();
			log.debug("Insertat ("+codOficina+","+codOficinaFisica+","+descOficina+",0) a OficinaFisicaHistorico)");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RegwebException(e.getMessage());
		} finally {
			close(session);
		}
    	return 0;    	
    }

    
    /**
     * Alta històric de l'organimse
     * @param codOrganisme Codi de l'organisme de qui crear l'històric
     * @param descCurtaOrganisme Descripció curta
     * @param descLlargaOrganisme Descripció llarga
     * @param dataAlta Data d'alta
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaHistOrganisme(String codOrganisme, String descCurtaOrganisme, String descLlargaOrganisme, String dataAlta) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			OrganismoHistorico org = new OrganismoHistorico(new OrganismoHistoricoId(Integer.valueOf(codOrganisme),Integer.valueOf(dataAlta)),
			                                descCurtaOrganisme, descLlargaOrganisme, 0);
			session.save(org);
			log.debug("Insertat ("+codOrganisme+","+descCurtaOrganisme+",0) (update count=1) a OrganismoHistorico)");

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    /**
     * Creació de un municipi del 060
     * @param codMun060 Codi tipus document
     * @param descMun060 Descripció del document
     * @param dataBaixa Data de baixa del document
     * @return
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaMunicipi060(String codMun060, String descMun060, String dataBaixa) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
		    Municipio060 mun = new Municipio060(codMun060, descMun060, Integer.valueOf(dataBaixa));
			session.save(mun);
			
			log.debug("Insertat ("+codMun060+","+descMun060+","+dataBaixa+") (update count=1) a Municipio060)");

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
    /**
     * Alta de nova oficina.
     * @param codOficina Codi d'oficina 
     * @param descOficina Descripció de l'oficina 
     * @param dataAlta Data d'alta 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaOficina(String codOficina, String descOficina, String dataAlta) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			Oficina ofi = new Oficina();
			ofi.setCodigo(Integer.valueOf(codOficina));
			ofi.setNombre(descOficina);
			ofi.setFechaBaja(0);
			session.save(ofi);
			log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count=1) a Oficina)");
			
			// NO AFEGIM LA DADA A L'HISTÒRIC, ES FA DES DEL SERVLET (QUAN CAL)
			// HEM DE DONAR PERMISOS DE LECTURA A ENTRADES I SORTIDES D'AQUESTA OFICINA A TOTHOM!!!
			autoritzaConsultaTotsUsuaris(codOficina);

			session.flush();

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
    /**
     * Alta de nova oficina fisica.
     * @param codOficina Codi d'oficina 
     * @param codOficinaFisica Codi d'oficina 
     * @param descOficina Descripció de l'oficina 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaOficinaFisica(String codOficina, String codOficinaFisica, String descOficina) throws RemoteException, RegwebException { 
    	return altaOficinaFisica(codOficina,codOficinaFisica,descOficina,"0");    	
    }
    
    /**
     * Alta de nova oficina fisica.
     * @param codOficina Codi d'oficina 
     * @param codOficinaFisica Codi d'oficina 
     * @param descOficina Descripció de l'oficina 
     * @param dataBaixa Data d'baixa 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataBaixa) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
            Oficina oficina = (Oficina) session.load(Oficina.class, Integer.valueOf(codOficina));
            OficinaFisica ofi = new OficinaFisica(new OficinaFisicaId(oficina,Integer.valueOf(codOficinaFisica)),
                                            descOficina,Integer.valueOf(dataBaixa));
            session.save(ofi);
			session.flush();
			log.debug("Insertat ("+codOficina+"-"+codOficinaFisica+","+descOficina+","+dataBaixa+") (update count=1) a OficinaFisica)");

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
    /**
     * Afageix l'organisme donat
     * @param codiOrganisme Codi d'organisme a afegir
     * @param descCurtaOrg Descripció curta de l'organimse
     * @param descLlargaOrg Descripció llarga de l'organimse
     * @param dataAltaOrg Data d'alta de l'organisme
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataAltaOrg) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			Organismo organismo = new Organismo(Integer.valueOf(codiOrganisme),
			                                    descCurtaOrg,
			                                    descLlargaOrg,
			                                    "S",
			                                    0);
			
            session.save(organismo);
			log.debug("Insertat ("+codiOrganisme+","+descLlargaOrg+") (update count=1) a Organismo");			

    		session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
    /**
     * Creació de tipus de document
     * @param codTipDoc Codi tipus document
     * @param descTipDoc Descripció del document
     * @param dataBaixa Data de baixa del document
     * @return
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int altaTipusDocument(String codTipDoc, String descTipDoc, String dataBaixa) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			if (dataBaixa==null || dataBaixa.trim().equals("")) dataBaixa="0";
			TipoDocumento tp = new TipoDocumento(new TipoDocumentoId(codTipDoc, Integer.valueOf(dataBaixa)), descTipDoc);
			session.save(tp);
			log.debug("Insertat ("+codTipDoc+","+descTipDoc+","+dataBaixa+") (update count=1) a TipoDocumento)");

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    
    /**
     * Creació de una unitat organica
     * @param codiOficina Codi oficina de registre
     * @param codiUnitat Codi unitat de gestió dins de l'oficina de registre
     * @param nomUnitat Nom de la unitat
     * @param email Adreça de correu electrònic
     * @param actiu Valors: 'S' si esta activa, 'N' sino
     * @return
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public boolean altaUnitatGestio(int codiOficina, int codiUnitat, String nomUnitat, String email, String actiu) throws RemoteException, RegwebException { 
		Session session = getSession();
		boolean rtdo = false;
		try {
			
			UnidadDeGestionId unidadid = new UnidadDeGestionId(codiUnitat,codiOficina);
		    UnidadDeGestion unidad = new UnidadDeGestion( unidadid, nomUnitat, actiu, email);
			session.save(unidad);
			
			log.debug("Insertat unitat("+codiOficina+"-"+codiUnitat+", "+nomUnitat+", "+email+") ");
			session.flush();
			rtdo = true;
		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return rtdo;    	
    }
    
    /**
     * Autoritza tots els usuaris a consultar l'oficina donada (tant entrades com sortides)
     * Això és necessari per quan es dona d'alta/activa una nova oficina.
     * @param codOficina Oficina on autoritzar els usuaris
     * @throws RegwebException
     */
    private void autoritzaConsultaTotsUsuaris(String codOficina) throws RemoteException, RegwebException {
    	Session session = getSession();
    	ScrollableResults rs=null;
    	
    	try {
    		String sentenciaHql="SELECT DISTINCT id.usuario FROM Autorizacion ";
    		Query query=session.createQuery(sentenciaHql);
    		rs = query.scroll();
    		while (rs.next()) {
    			log.debug("Autoritzam usuari "+rs.getString(0)+" a l'oficina "+codOficina);
    			try { 
    				this.addAutoritzacioUsuari(rs.getString(0), "CE", codOficina);
    				this.addAutoritzacioUsuari(rs.getString(0), "CS", codOficina);
    			} catch (RegwebException eRW) {
    				if (eRW.getMessage().indexOf("SQL0803")!=-1 ) {
    					//És clau duplicada. Se veu que l'usuari ja hi estava autoritzat, no passa res, ignoram.
    					log.debug("Usuari "+rs.getString(0)+" prèviament ja autoritzat l'oficina "+codOficina);
    				} else {
    					throw new RegwebException(eRW.getMessage());
    				}
    			}
    		}

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    }

    /**
     * Elimina l'agrupació geogràfica donada
     * @param codTipuAgruGeo Codi tipus agrupació geogràfica
     * @param codAgruGeo Codi agrupació geogràfica
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int delAgrupacioGeografica(String codTipuAgruGeo, String codAgruGeo) throws RemoteException, RegwebException { 
		Session session = getSession();
		
		try {
			//Primer eliminam les referències a l'agrupació geogràfica (codi postal)
			String sentenciaHql="DELETE FROM CodigoPostal " +
					"WHERE id.agrupacionGeografica.id.tipo=? AND id.agrupacionGeografica.id.codigo=?";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,new Integer(codTipuAgruGeo) );
			query.setInteger(1,new Integer(codAgruGeo) );
			log.debug("sentenciaHql="+sentenciaHql);
			int afectats=query.executeUpdate();
			if (afectats<=0){
				throw new RegwebException("ERROR: No s'ha pogut eliminar l'agrupaci\u00f3 geogr\u00e0fica ("+codTipuAgruGeo+","+codAgruGeo+") a bcodpos.");
			}
			log.debug("Eliminat ("+codTipuAgruGeo+","+codAgruGeo+" (update count="+afectats+") a bcodpos");
			
			sentenciaHql="DELETE FROM AgrupacionGeografica " +
				"WHERE id.tipo=? AND id.codigo=?";
			query=session.createQuery(sentenciaHql);
			query.setInteger(0,new Integer(codTipuAgruGeo) );
			query.setInteger(1,new Integer(codAgruGeo) );
			afectats=query.executeUpdate();
			if (afectats<=0){
				throw new RegwebException("ERROR: No s'ha pogut eliminar l'agrupaci\u00f3 geogr\u00e0fica ("+codTipuAgruGeo+","+codAgruGeo+") a AgrupacionGeografica.");
			}
			log.debug("Eliminar ("+codTipuAgruGeo+","+codAgruGeo+" (update count="+afectats+") a AgrupacionGeografica");

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }

    /**
     * Elimina les autoritzacions de l'usuari.
     * @param usuari Codi d'usuari
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int deleteAutoritzacionsUsuari(String usuari) throws RemoteException, RegwebException { 
    	
		Session session = getSession();
		
		try {
			
			String sentenciaHql="DELETE FROM Autorizacion WHERE id.usuario=?";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,usuari);
			int afectats=query.executeUpdate();
			if (afectats<=0){
				log.info("L'usuari ("+usuari+") no t� dades a Autorizacion.");
			}


    		session.flush();

		} catch (Exception e) {
			log.debug("Capturada excepci\u00f3.");
			log.error(e.getMessage());
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	
    }
    

    /**
     * Elimina les relacions de l'oficina amb els organismes per a no remetre
     * @param oficinaGestionar Codi d'oficina
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int deleteNoRemsOfi(String oficinaGestionar) throws RemoteException, RegwebException {    	
		Session session = getSession();
		
		try {			
			String sentenciaHql="DELETE FROM OficinaOrganismoNoRemision WHERE id.oficina.codigo=?";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(oficinaGestionar));
			query.executeUpdate();
    		session.flush();
		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	  	
    }
    
    /**
     * Elimina les relacions de l'oficina amb els organismes amb permís per enviar emails
     * @param oficinaGestionar Codi d'oficina
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int deleteOfiOrgEmail(String oficinaGestionar) throws RemoteException, RegwebException {     	
		Session session = getSession();
		
		try {			
			String sentenciaHql="DELETE FROM OficinaOrganismoPermetEnviarEmail WHERE id.oficina=?";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(oficinaGestionar));
			query.executeUpdate();
    		session.flush();
		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	  	
    }

    /**
     * Elimina les relacions de l'oficina amb els organismes
     * @param oficinaGestionar Codi d'oficina
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public int deleteOrgsOfi(String oficinaGestionar) throws RemoteException, RegwebException {    	
    	Session session = getSession();		
		try {			
			String sentenciaHql="DELETE FROM OficinaOrganismo WHERE id.oficina.codigo=?";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(oficinaGestionar));
			query.executeUpdate();
    		session.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
    	return 0;    	    	
    }

    /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Torna true si ja existeix un comptador donat d'alta per aquesta oficina i any.
     * @param anyGestionar Any
     * @param oficinaGestionar codi d'oficina
     * @return true si ja existeix un comptador donat d'alta per aquesta oficina i any, false en cas contrari.
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public boolean existComptador(String anyGestionar, String oficinaGestionar) throws RemoteException, RegwebException {
    //Torna true si ja existeix un comptador donat d'alta per aquesta oficina i any.
		Session session = getSession();
		ScrollableResults rs=null;
		
		long resultat = 0;
		try {
			
			String sentenciaHql="SELECT COUNT(*) FROM Contador " +
					"WHERE id.anyo=? AND id.oficina=?";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(anyGestionar));
			query.setInteger(1,Integer.valueOf(oficinaGestionar));
			rs = query.scroll();
			
			while (rs.next()) {
				resultat = rs.getLong(0);
				log.debug("existComptador="+resultat);
			}

			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
        return (resultat>0);
    }


    /**
	 * Retorna la informació de l'agrupació geogràfica donada
	 * @param codiTipusAgruGeo Codi del tipus d'agrupació geogràfica
	 * @param codiAgruGeoGestionar Codi d'agrupació geogràfica
	 * @return Dades de la agrupació geogràfica encapsulada dins la classe AgrupacioGeograficaData
	 * @throws RemoteException
	 * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public AgrupacioGeograficaData getAgrupacioGeografica(String codiTipusAgruGeo, String codiAgruGeoGestionar) throws RemoteException, RegwebException{
    	/* Torna el Data d'una agrupació geogràfica */
		AgrupacioGeograficaData agruGeoData = new AgrupacioGeograficaData();
    	Session session = getSession();
		ScrollableResults rs=null;
		
	
		try {
			
			String sentenciaHql="SELECT id.tipo, id.codigo, nombre, fechaBaja, padre.id.tipo, padre.id.codigo FROM AgrupacionGeografica WHERE id.tipo=? AND id.codigo=? ";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(codiTipusAgruGeo));
			query.setInteger(1,Integer.valueOf(codiAgruGeoGestionar));
			rs = query.scroll();
			
			if (rs.next()) {
				agruGeoData.setCodiTipusAgruGeo( (rs.getInteger(0)!= null) ? String.valueOf(rs.getInteger(0)) : "" );
				agruGeoData.setCodiAgruGeo( (rs.getInteger(1)!= null) ? String.valueOf(rs.getInteger(1)) : "" );
				agruGeoData.setDescAgruGeo( (rs.getString(2)!= null) ? rs.getString(2).trim() : "" );
				
				DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
		        DateFormat ddmmyy=new SimpleDateFormat("dd/MM/yy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInteger(3));
                try {
                	data=yymmdd.parse(dataBaixa);
                	agruGeoData.setDataBaixa(ddmmyy.format(data));
                } catch (Exception e) {
                	agruGeoData.setDataBaixa(dataBaixa);
                }
                
				agruGeoData.setCodiTipusAgruGeoSuperior( (rs.getInteger(4)!= null) ? String.valueOf(rs.getInteger(4)) : "" );
				agruGeoData.setCodiAgruGeoSuperior( (rs.getInteger(5)!= null) ? String.valueOf(rs.getInteger(5)) : "" );
				log.debug("Afegim: "+agruGeoData.getCodiTipusAgruGeo()+" "+agruGeoData.getCodiAgruGeo()+" "+agruGeoData.getDescAgruGeo());
			
				//Ara recuperam el codi postal.
				
				sentenciaHql="SELECT codigoPostal FROM CodigoPostal WHERE id.agrupacionGeografica.id.tipo=? AND id.agrupacionGeografica.id.codigo=? ";
				query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(codiTipusAgruGeo));
				query.setInteger(1,Integer.valueOf(codiAgruGeoGestionar));
				rs = query.scroll();
				if (rs.next()) 
					agruGeoData.setCodiPostal( (rs.getInteger(0)!= null) ? String.valueOf(rs.getInteger(0)) : "" );
				else 
					agruGeoData.setCodiPostal( "" );
				
				// Ara recuperam el descriptor del tipus d'agrupació.
				
				sentenciaHql="SELECT nombre FROM TipoAgrupacionGeografica WHERE id.tipo=? ";
				query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(codiTipusAgruGeo));
				rs = query.scroll();
				if (rs.next()) 
					agruGeoData.setDescTipusAgruGeo( (rs.getString(0)!= null) ? rs.getString(0).trim() : "" );
				else 
					agruGeoData.setDescTipusAgruGeo( "" );
				
                // Ara recuperam el descriptor del tipus d'agrupació superior (de la propia taula AgrupacionGeografica)
				
				agruGeoData.setDescAgruGeoSuperior( "" );
                if (!agruGeoData.getCodiTipusAgruGeoSuperior().equals("") && !agruGeoData.getCodiAgruGeoSuperior().equals("")) {
    				sentenciaHql="SELECT nombre FROM AgrupacionGeografica WHERE id.tipo=? AND id.codigo=? ";
    				query=session.createQuery(sentenciaHql);
    				query.setInteger(0,Integer.valueOf(agruGeoData.getCodiTipusAgruGeoSuperior()));
    				query.setInteger(1,Integer.valueOf(agruGeoData.getCodiAgruGeoSuperior()));
    				rs = query.scroll();
    				if (rs.next()) {
    					agruGeoData.setDescAgruGeoSuperior( (rs.getString(0)!= null) ? rs.getString(0).trim() : "" );
                    }
                }
				
			} else {
				agruGeoData = null;
			}
				
			session.flush();

			
        } catch (Exception e) {
         log.error(e.getMessage());
         throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
		return agruGeoData;
    }

    /**
	 * Colecció de totes les agrupacions geogràfiques, estiguin o no de baixa.
	 * @return Collection amb totes les agrupacions geogràfiques
	 * @throws RemoteException
	 * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Collection getAgrupacionsGeografiques() throws RemoteException, RegwebException{
    	/* Fic a un TreeMap les agrupacions geogràfiques */
		Vector agrupacionsGeografiques = new Vector();

    	Session session = getSession();
		ScrollableResults rs=null;
		
	
		try {
			
			String sentenciaHql="SELECT id.tipo, id.codigo, nombre, fechaBaja, padre.id.tipo, padre.id.codigo FROM AgrupacionGeografica " +
					"ORDER BY id.tipo, id.codigo";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll();
			
			int i=0;
			while (rs.next()) {
				AgrupacioGeograficaData agruGeoData = new AgrupacioGeograficaData();
				agruGeoData.setCodiTipusAgruGeo( (rs.getInteger(0)!= null) ? String.valueOf(rs.getInteger(0)) : "" );
				agruGeoData.setCodiAgruGeo( (rs.getInteger(1)!= null) ? String.valueOf(rs.getInteger(1)) : "" );
				agruGeoData.setDescAgruGeo( (rs.getString(2)!= null) ? rs.getString(2).trim() : "" );
				
				DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
		        DateFormat ddmmyy=new SimpleDateFormat("dd/MM/yy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInteger(3));
                try {
                	data=yymmdd.parse(dataBaixa);
                	agruGeoData.setDataBaixa(ddmmyy.format(data));
                } catch (Exception e) {
                	agruGeoData.setDataBaixa(dataBaixa);
                }
				agruGeoData.setCodiTipusAgruGeoSuperior( (rs.getInteger(4)!= null) ? String.valueOf(rs.getInteger(4)) : "" );
				agruGeoData.setCodiAgruGeoSuperior( (rs.getInteger(5)!= null) ? String.valueOf(rs.getInteger(5)) : "" );
				agrupacionsGeografiques.add(agruGeoData);
				i++;
			}
			
			if (i==0) {
				agrupacionsGeografiques = null;
			}
			
				
			session.flush();

			
		} catch (Exception e) {
		    e.printStackTrace();
			log.error(e.getMessage());
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
        return agrupacionsGeografiques;
		
    }
    
    /*
	 * Retorna el usuaris per una oficina determinada.
	 * @param oficina Codi d'oficina a cercar
	 * @return Informació dels usuaris autoritzats a una oficina determinada
	 * @throws RemoteException
	 * 
	 * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getAutoritzacionsOficina(String oficina) throws RemoteException{		
    	/* Fic a un TreeMap els usuaris autoritzats a una oficina.*/
		Vector resposta = new Vector();	
    	AutoritzacionsUsuariData autUsuData = null;  	 	
    	String tipusAut = "";
    	String UsuariAutOlD = "";
    	String UsuariAut = "";
		Vector oficinas=new Vector();		
		Session session = getSession();
		ScrollableResults rs=null;
		
		try {			
			String sentenciaHql="select usuario,codigoAutorizacion from Autorizacion " +
					"where codigoOficina=? " +
					"ORDER BY usuario,codigoAutorizacion";			
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(oficina));
			rs = query.scroll();
			int i=0;
			while (rs.next()) {
				
				UsuariAut = rs.getString(1);
				tipusAut = rs.getString(2);
				 
				
				//Si es un nou usuari
				if(!UsuariAut.equals(UsuariAutOlD)){
					if(autUsuData!=null){					
						resposta.add(autUsuData);
					}
					autUsuData = new AutoritzacionsUsuariData(UsuariAut);
				}
				
				if ( tipusAut.equals("AE")) {
					autUsuData.setAe(true);
				} else if ( tipusAut.equals("CE")) {
					autUsuData.setCe(true);
				} else if ( tipusAut.equals("AS")) {
					autUsuData.setAs(true);
				} else if ( tipusAut.equals("CS")) {
					autUsuData.setCs(true);
				} else if ( tipusAut.equals("VE")) {
					autUsuData.setVe(true);
				} else if ( tipusAut.equals("VS")) {
					autUsuData.setVs(true);
				}
				i++;
				//System.out.println("Afegim("+i+"): "+tipusAut+" "+UsuariAut);
				UsuariAutOlD = UsuariAut;
			}
		} catch (Exception e) {
			oficinas.addElement("");
			oficinas.addElement("getAutoritzacionsOficina Error en la SELECT");
			log.error(e.getMessage());
		} finally {
			close(session);
		}
    	return resposta;
    }

    /**

	
	/**
	 * Retorna les oficines autoritzades a l'usuari.
	 * @param usuari Codi d'usuari a cercar
	 * @return Informació de les oficines autoritzades a l'usuari encapsulada dins la classe AutoritzacionsUsuariData
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public AutoritzacionsUsuariData getAutoritzacionsUsuari(String usuari) throws RemoteException{
    	/* Fic a un TreeMap les oficines i les que està autoritzat. */
    	AutoritzacionsUsuariData autUsuData = new AutoritzacionsUsuariData();
    	
    	TreeMap autUsuAE = new TreeMap();
    	TreeMap autUsuCE = new TreeMap();
    	TreeMap autUsuAS = new TreeMap();
    	TreeMap autUsuCS = new TreeMap();
    	TreeMap autUsuVE = new TreeMap();
    	TreeMap autUsuVS = new TreeMap();
    	
    	String tipusAut = "";
    	//String oficinaAut = "";
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
		try {
			
			String sentenciaHql="SELECT id.codigoAutorizacion, id.codigoOficina FROM Autorizacion " +
					"WHERE id.usuario=? " +
					"ORDER BY id.codigoAutorizacion, id.codigoOficina";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,usuari);
			rs = query.scroll();
			
			int i=0;
			while (rs.next()) {
				tipusAut = rs.getString(0);
				//oficinaAut = String.valueOf(rs.getInteger(1)); 
				if ( tipusAut.equals("AE")) {
					autUsuAE.put(new Integer(rs.getInteger(1)),"X");
				} else if ( tipusAut.equals("CE")) {
					autUsuCE.put(new Integer(rs.getInteger(1)),"X");
				} else if ( tipusAut.equals("AS")) {
					autUsuAS.put(new Integer(rs.getInteger(1)),"X");
				} else if ( tipusAut.equals("CS")) {
					autUsuCS.put(new Integer(rs.getInteger(1)),"X");
				} else if ( tipusAut.equals("VE")) {
					autUsuVE.put(new Integer(rs.getInteger(1)),"X");
				} else if ( tipusAut.equals("VS")) {
					autUsuVS.put(new Integer(rs.getInteger(1)),"X");
				}
				i++;
			}
			if (i==0) {
				return null;
			} else {
				autUsuData.setAutModifEntrada(autUsuAE);
				autUsuData.setAutConsultaEntrada(autUsuCE);
				autUsuData.setAutModifSortida(autUsuAS);
				autUsuData.setAutConsultaSortida(autUsuCS);
				autUsuData.setAutVisaEntrada(autUsuVE);
				autUsuData.setAutVisaSortida(autUsuVS);
			}
				
			session.flush();

        } catch (Exception e) {
         oficinas.addElement("");
         oficinas.addElement("BuscarOficinas Error en la SELECT");
         log.error(e.getMessage());
        } finally {
            close(session);
        }
    	return autUsuData;
    }

    /**
	 * Retorna el valor del comptador de l'oficina, any i tipus d'entrada/sortida donats
	 * @param codiOficina Codi de l'oficina a cercar
	 * @param es String que pot ser "E" per entrades i "S" per sortides
	 * @param any Any del comptador
	 * @return Valor del comptador (null si no existeix)
	 * @throws RemoteException
	 * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public String getComptadorOficina( String codiOficina, String es, String any )  throws RemoteException, RegwebException {
		Session session = getSession();
		ScrollableResults rs=null;
		String result="";
		
		try {
			//log.debug("Cercam el comptador de l'oficina: "+codiOficina);
			
			String sentenciaHql="SELECT numero FROM Contador WHERE " +
					" id.anyo=? AND id.codigoEntradaSalida=? AND id.oficina=? ";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(any));
			query.setString(1,es);
			query.setInteger(2,Integer.valueOf(codiOficina));
			rs = query.scroll();
			
			if (rs.next()) {
				//log.debug("Comptador de l'oficina: "+codiOficina+" ES: "+ES+" i any: "+any+" és: "+rs.getInteger("numero"));
				result = String.valueOf(rs.getInteger(0));
			} 
			session.flush();
        } catch (Exception e) {
         log.error(e.getMessage());
         throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
        return result;
	}
    
    /**
	 * Retorna la informació de l'entitat donada
	 * @param codiEntidad Codi de l'entitat 
	 * @param subcodiEntitat Subcodi de l'entitat
	 * @return Dades de l'entitat encapsulades dins la classe EntitatData
	 * @throws RemoteException
	 * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public EntitatData getEntitat( String codiEntidad, String subcodiEntitat )  throws RemoteException, RegwebException { 
		//Important! Encara que la clau primària sigui el primer camp (codigo) les consultes es fan sobre codigoCatalan (el mateix
		//codi però en català
		Session session = getSession();
		ScrollableResults rs=null;
		
		EntitatData entitat = new EntitatData();
		
			try {
				log.debug("Cercam: "+codiEntidad+" "+subcodiEntitat);
				
				String sentenciaHql="SELECT id.codigo, codigoCatalan, id.numero, nombre, nombreCatalan, id.fechaBaja FROM EntidadRemitente WHERE " +
						" id.codigo=? AND id.numero=? ";
				Query query=session.createQuery(sentenciaHql);
				query.setString(0,codiEntidad);
				query.setInteger(1,Integer.valueOf(subcodiEntitat));
				rs = query.scroll();
				
				while (rs.next()) {
					entitat.setCodigoEntidad( rs.getString(0) );
					entitat.setCodiEntitat(rs.getString(1));
					entitat.setSubcodiEnt(String.valueOf(rs.getInteger(2)));
					entitat.setDescEntidad(rs.getString(3).trim());
					entitat.setDescEntitat(rs.getString(4).trim());
					
			        DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
					String dataBaixa=String.valueOf(rs.getInteger(5));
	                try {
	                	data=yyyymmdd.parse(dataBaixa);
	                	entitat.setDataBaixa(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	entitat.setDataBaixa(dataBaixa);
	                }
					log.debug("Afegim: "+entitat.getCodigoEntidad()+" "+entitat.getDescEntitat());
					entitat.toString();
				}
				session.flush();
				
            } catch (Exception e) {
             log.error(e.getMessage());
             if (rs!=null) rs.close();
             throw new RegwebException(e.getMessage());
            } finally {
                close(session);
            }
		return entitat;
	}
    
    /**
     * Retorna un vector amb les entitats similars o iguals al codi o descripció donat.
     * @param subcadenaCodigo Codi a cercar
     * @param subcadenaTexto Descripció a cercar
     * @return Vector amb la informació de les entitats trobades.
     * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public Vector getEntitats(String subcadenaCodigo, String subcadenaTexto) throws RemoteException {
		//Copy & paste de ValoresBean.buscarRemitentes però torna el codi en castellà ja que és la clau primària i sense
		// mirar si està de baixa o no.
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector remitentes=new Vector();
		String sentenciaHql="";
		try {
			
			sentenciaHql="select id.codigo, id.numero, nombreCatalan  from EntidadRemitente where 1=1 " +
			((!subcadenaTexto.trim().equals("") || !subcadenaCodigo.trim().equals("")) ? " AND " : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? "(" : "") +
			((!subcadenaTexto.trim().equals("")) ? "nombreCatalan LIKE ?" : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? " OR " : "") +
			((!subcadenaCodigo.trim().equals("") && subcadenaCodigo.trim().length()!=6) ? "codigoCatalan LIKE ?" : "") +
			((!subcadenaCodigo.trim().equals("") && subcadenaCodigo.trim().length()==6) ? "codigoCatalan LIKE ? OR codigoCatalan LIKE ?" : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? " )" : "") +
			" ORDER BY codigoCatalan, id.numero ";
			Query query=session.createQuery(sentenciaHql);
			int contador=0;
			if (!subcadenaTexto.trim().equals("")) {
				query.setString(contador++, "%"+subcadenaTexto.toUpperCase()+"%");
			}
			if (!subcadenaCodigo.trim().equals("")) {
				if (subcadenaCodigo.length()==7) {
					query.setString(contador++, subcadenaCodigo.toUpperCase());
				} else if (subcadenaCodigo.length()==6) {
					query.setString(contador++, subcadenaCodigo.toUpperCase()+"%");
					query.setString(contador++, subcadenaCodigo.toUpperCase());
				} else{
					query.setString(contador++, subcadenaCodigo.toUpperCase()+"%");
				}
			}
			rs = query.scroll();
			while (rs.next()) {
				remitentes.addElement(rs.getString(0));
				remitentes.addElement(rs.getInteger(1).toString());
				remitentes.addElement(rs.getString(2));
			}
			if (remitentes.size()==0) {
				remitentes.addElement("");
				remitentes.addElement("No hi ha Remitents");
				remitentes.addElement("");
			}
			session.flush();
        } catch (Exception e) {
	         remitentes.addElement("");
	         remitentes.addElement("Error en la SELECT");
	         remitentes.addElement(";");
	         log.error(e.getMessage());
        } finally {
            close(session);
        }
		return remitentes;
	}
    
    /**
     * Retorna l'històric de l'oficina donada
     * @param oficina Oficina de la que cercar l'històric
     * @return Vector amb tota la informació de l'històric de l'oficina.
     * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public Vector getHistOficina(String oficina)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
			try {
				
				String sentenciaHql="SELECT id.codigo, nombre, id.fechaAlta, fechaBaja FROM OficinaHistorico WHERE id.codigo = ? "
				            + " ORDER BY id.fechaAlta DESC";
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(oficina));
				rs = query.scroll();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInteger(0)));
					oficinas.addElement(rs.getString(1).trim());
					
					DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					String dataAlta=String.valueOf(rs.getInteger(2));
	                try {
	                	data=yyyymmdd.parse(dataAlta);
	                	oficinas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinas.addElement(dataAlta);
	                }
					
			        
					String dataBaixa=String.valueOf(rs.getInteger(3));
	                try {
	                	data=yyyymmdd.parse(dataBaixa);
	                	oficinas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinas.addElement(dataBaixa);
	                }
					log.debug("Llegim histOficines: "+String.valueOf(rs.getInteger(0))+" "+rs.getString(1)
							+" "+String.valueOf(rs.getInteger(2))+" "+String.valueOf(rs.getInteger(3)));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("Oficina "+oficina+" no existeix");
					oficinas.addElement("");
					oficinas.addElement("");
				}
				
				session.flush();

				
            } catch (Exception e) {
	             oficinas.addElement("");
	             oficinas.addElement("BuscarOficinas Error en la SELECT");
	             oficinas.addElement("");
	             oficinas.addElement("");
	             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return oficinas;
	}
    
    /**
     * Retorna l'hist�ric de l'oficina fisica donada
     * @param oficina Oficina de la que cercar l'hist�ric
     * @param oficina Oficina fisica de la que cercar l'hist�ric
     * @return Vector amb tota la informaci� de l'hist�ric de l'oficina fisica.
     * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public Vector getHistOficinaFisica(String oficina, String oficinaFisica)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		Vector oficinasFisicas=new Vector();
		
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		
			try {
				String sentenciaHql="SELECT id.codigoOficina, id.codigoOficinaFisica,id.fechaAlta, nombre, fechaBaja"+
				                    " FROM OficinaFisicaHistorico "+
				                    " WHERE id.codigoOficina = ? AND id.codigoOficinaFisica = ? "+
				                    " ORDER BY fechaBaja ASC";
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(oficina));
				query.setInteger(1,Integer.valueOf(oficinaFisica));
				rs = query.scroll();
				
				
				while (rs.next()) {
					oficinasFisicas.addElement(String.valueOf(rs.getInteger(0))); //id.oficina.codigo
					oficinasFisicas.addElement(String.valueOf(rs.getInteger(1))); //id.codigoOficinaFisica
					oficinasFisicas.addElement(rs.getString(3).trim()); //nombre

					String dataAlta=String.valueOf(rs.getInteger(2)); //fecha alta
	                try {
	                	java.util.Date data=yyyymmdd.parse(dataAlta);
	                	oficinasFisicas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinasFisicas.addElement(dataAlta);
	                }					
			        
					String dataBaixa=String.valueOf(rs.getInteger(4)); //fecha baja
	                try {
	                	java.util.Date data=yyyymmdd.parse(dataBaixa);
	                	oficinasFisicas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinasFisicas.addElement(dataBaixa);
	                }
					//log.debug("Llegim histOficinesFisiques: "+String.valueOf(rs.getInteger(0))+" "+String.valueOf(rs.getInteger(1))+" "+rs.getString("ofh_nom")
					//		+" "+String.valueOf(rs.getInt("ofh_fecalt"))+" "+String.valueOf(rs.getInt("ofh_fecbaj")));
				}
				
				if (oficinasFisicas.size()==0) {
					oficinasFisicas.addElement("");
					oficinasFisicas.addElement("");
					oficinasFisicas.addElement("Oficina "+oficina+" no existeix");
					oficinasFisicas.addElement("");
					oficinasFisicas.addElement("");
				}
				
			} catch (Exception e) {
				oficinasFisicas.addElement("");
				oficinasFisicas.addElement("");
				oficinasFisicas.addElement("BuscarOficinas Error en la SELECT");
				oficinasFisicas.addElement("");
				oficinasFisicas.addElement("");
				log.error(e);
			} finally {
				close(session);
			}
		return oficinasFisicas;
	}
    
    /**
	 * Retorna l'històric de l'organisme donat
	 * @param organisme Codi d'organisme a cercar
	 * @return Vector amb la informació de l'històric de l'organisme
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getHistOrganisme(String organisme)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector organismes=new Vector();
		
			try {
				
				String sentenciaHql="SELECT id.codigo, nombreCorto, nombreLargo, id.fechaAlta, fechaBaja FROM OrganismoHistorico WHERE id.codigo = ? "
                        + " ORDER BY id.fechaAlta DESC";
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(organisme));
				rs = query.scroll();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInteger(0)));
					organismes.addElement(rs.getString(1).trim());
					organismes.addElement(rs.getString(2).trim());
					
					DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					String dataAlta=String.valueOf(rs.getInteger(3));
	                try {
	                	data=yyyymmdd.parse(dataAlta);
	                	organismes.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	organismes.addElement(dataAlta);
	                }
					
			        
					String dataBaixa=String.valueOf(rs.getInteger(4));
	                try {
	                	data=yyyymmdd.parse(dataBaixa);
	                	organismes.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	organismes.addElement(dataBaixa);
	                }
	                
					log.debug("Llegim histOrganismes: "+String.valueOf(rs.getInteger(0))+" "+rs.getString(2).trim()
							+" "+String.valueOf(rs.getInteger(3))+" "+String.valueOf(rs.getInteger(4)));
				}
				
				if (organismes.size()==0) {
					organismes.addElement("");
					organismes.addElement("");
					organismes.addElement("");
					organismes.addElement("organisme "+organisme+" no existeix");
					organismes.addElement("");
				}
				
				session.flush();

				
            } catch (Exception e) {
	             organismes.addElement("");
	             organismes.addElement("");
	             organismes.addElement("");
	             organismes.addElement("Buscarorganisme Error en la SELECT");
	             organismes.addElement("");
	             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return organismes;
	}
    
    /**
	 * Retorna el model d'ofici donat
	 * @param model codi del model d'ofici a cercar
	 * @return Vector amb la informació del model d'ofici donat.
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getModelOfici(String model)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector models=new Vector();
		
			try {
				
				String sentenciaHql="SELECT nombre, contentType FROM ModeloOficio WHERE nombre = ? ";
				Query query=session.createQuery(sentenciaHql);
				query.setString(0,model);
				rs = query.scroll();
				
				while (rs.next()) {
					models.addElement(rs.getString(0).trim());
					models.addElement(rs.getString(1).trim());
	                
					//log.debug("Afegim: "+String.valueOf(rs.getInteger("codigo"))+" "+rs.getString("nombre"));
				}
				
				if (models.size()==0) {
					models.addElement("");
					models.addElement("Model d'ofici "+model+" no existeix");
				}
				
				session.flush();
				
            } catch (Exception e) {
             models.addElement("");
             models.addElement("BuscarModel Error en la SELECT");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return models;
	}
    
    /**
	 * Retorna el model de rebut donat
	 * @param model codi del model de rebut
	 * @return Vector amb la informació del model de rebut
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getModelRebut(String model)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector models=new Vector();
		
			try {
				
				String sentenciaHql="SELECT nombre, contentType FROM ModeloRecibo WHERE nombre = ? ";
				Query query=session.createQuery(sentenciaHql);
				query.setString(0,model);
				rs = query.scroll();
				
				while (rs.next()) {
					models.addElement(rs.getString(0).trim());
					models.addElement(rs.getString(1).trim());
	                
					//log.debug("Afegim: "+String.valueOf(rs.getInteger("codigo"))+" "+rs.getString("nombre"));
				}
				
				if (models.size()==0) {
					models.addElement("");
					models.addElement("Model de rebut "+model+" no existeix");
				}
				
				session.flush();

            } catch (Exception e) {
             models.addElement("");
             models.addElement("BuscarModel Error en la SELECT");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return models;
	}

    /**
     * Retorna el nom de un municipi del 060
     * @param codiMun060 Codi del tipus de document a recuperar
     * @return informació del tipus de document, encapsulat dins la classe TipusDocumentData
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Municipi060Data getMunicipi060(String codiMun060) throws RemoteException, RegwebException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Municipi060Data tipdoc=new Municipi060Data();
		try {
			
			String sentenciaHql="SELECT codigoMunicipio,nombreMunicipio,fechaBaja FROM  Municipio060 WHERE codigoMunicipio=? ";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,codiMun060);
			rs = query.scroll();
			if (rs.next()) {
				tipdoc.setCodiMunicipi060( (rs.getString(0)!= null) ? rs.getString(0) : "" );
				tipdoc.setDescMunicipi060( (rs.getString(1)!= null) ? rs.getString(1).trim() : "" );
				
				DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInteger(2));
	            try {
	            	data=yyyymmdd.parse(dataBaixa);
	            	tipdoc.setDataBaixa(ddmmyyyy.format(data));
	            } catch (Exception e) {
	            	tipdoc.setDataBaixa(dataBaixa);
	            }
			} else {
				tipdoc=null;
			}


			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
		return tipdoc;
	}
	
    
    /**
     * Retorna tots els municipis del 060, estiguin de baixa o no.
     * @return Vector amb tots els municipis del 060.
     * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Vector getMunicipis060() throws RemoteException {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector documentos=new Vector();
		try {
			
			String sentenciaHql="SELECT codigoMunicipio, nombreMunicipio FROM Municipio060 ORDER BY codigoMunicipio";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll();
			documentos.addElement("");
			documentos.addElement("");
			while (rs.next()) {
				documentos.addElement(rs.getString(0));
				documentos.addElement(rs.getString(1).trim());
			}
			if (documentos.size()==0) {
				documentos.addElement("");
				documentos.addElement("No hi ha municipis del 060");
			}
    		session.flush();

        } catch (Exception e) {
         documentos.addElement("");
         documentos.addElement("Error en la SELECT");
         log.error("ERROR: "+e.getMessage());
        } finally {
            close(session);
        }
		return documentos;
	}
    
    /**
	 * Retorna l'oficina donada
	 * @param oficina codi de l'oficina a cercar
	 * @return Vector amb la informació de l'oficina donada.
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getOficina(String oficina)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
			try {
				
				String sentenciaHql="SELECT codigo, nombre, fechaBaja FROM Oficina WHERE codigo = ? ";
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(oficina));
				rs = query.scroll();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInteger(0)));
					oficinas.addElement(rs.getString(1).trim());
					
					String dataBaixa=String.valueOf(rs.getInteger(2));
					if (dataBaixa.length()==5)
						dataBaixa="0"+dataBaixa;
					
					DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					try {
	                	data=yymmdd.parse(dataBaixa);
	                	oficinas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinas.addElement(dataBaixa);
	                }
	                
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("Oficina "+oficina+" no existeix");
					oficinas.addElement("");
				}
				
				session.flush();
				
            } catch (Exception e) {
             oficinas.addElement("");
             oficinas.addElement("BuscarOficinas Error en la SELECT");
             oficinas.addElement("");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return oficinas;
	}
    
    /**
	 * Retorna l'oficina fisica donada
	 * @param oficina codi de l'oficina a cercar
	 * @param oficinaFisica codi de l'oficina fisica a cercar
	 * @return Vector amb la informació de l'oficina donada.
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getOficinaFisica(String oficina, String oficinaFisica)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
			try {
				
				String sentenciaHql="SELECT id.oficina.codigo, id.codigoOficinaFisica, nombre ,fechaBaja FROM OficinaFisica WHERE id.oficina.codigo = ? AND id.codigoOficinaFisica = ? ";
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(oficina));
				query.setInteger(1,Integer.valueOf(oficinaFisica));
				rs = query.scroll();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInteger(0)));
					oficinas.addElement(String.valueOf(rs.getInteger(1)));
					oficinas.addElement(rs.getString(2).trim());
                    
					String dataBaixa=String.valueOf(rs.getInteger(3));
                    if (dataBaixa.length()==5)
                        dataBaixa="0"+dataBaixa;
                    
                    DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
                    DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date data=null;
                    
                    try {
                        data=yymmdd.parse(dataBaixa);
                        oficinas.addElement(ddmmyyyy.format(data));
                    } catch (Exception e) {
                        oficinas.addElement(dataBaixa);
                    }
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("");
					oficinas.addElement("Oficina f�sica "+oficina+"-"+oficinaFisica+" no existeix");
					oficinas.addElement("");
				}
				
				session.flush(); 
				
            } catch (Exception e) {
             oficinas.addElement("");
             oficinas.addElement("");
             oficinas.addElement("BuscarOficinasFisicas Error en la SELECT");
             oficinas.addElement("");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return oficinas;
	}

    /** 
	 * Torna un vector amb les dades de l'organisme
	 * @param codiOrganisme Codi de l'organisme a llegir
	 * @return Vector que conté la informació de l'organisme
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getOrganisme( String codiOrganisme)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector organismes=new Vector();
		
			try {
				
				String sentenciaHql="SELECT codigo, nombreCorto, nombreLargo, fechaBaja FROM Organismo WHERE codigo=? " +
						"ORDER BY codigo";
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(codiOrganisme));
				rs = query.scroll();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInteger(0)));
					organismes.addElement(rs.getString(1).trim());
					organismes.addElement(rs.getString(2).trim());
					
					String dataBaixa=String.valueOf(rs.getInteger(3));
					if (dataBaixa.length()==5)
						dataBaixa="0"+dataBaixa;
					
					DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					try {
	                	data=yymmdd.parse(dataBaixa);
	                	organismes.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	organismes.addElement(dataBaixa);
	                }
				}
				if (organismes.size()==0) {
					organismes.addElement("");
					organismes.addElement("");
					organismes.addElement("Error, no hi ha organismes!");
					organismes.addElement("");
				}
				session.flush();
            } catch (Exception e) {
             organismes.addElement("");
             organismes.addElement("");
             organismes.addElement("getOrganismes Error en la SELECT");
             organismes.addElement("");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return organismes;
	}
    
	 /**
	 * Retorna els organismes existents a la BBDD que no estan de baixa.
	 * @return Vector amb els organismes trobats
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getOrganismes( )  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector organismes=new Vector();
		
			try {
				
				String sentenciaHql="SELECT codigo, nombreCorto, nombreLargo FROM Organismo WHERE fechaBaja=0 ORDER BY codigo";
				Query query=session.createQuery(sentenciaHql);
				rs = query.scroll();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInteger(0)));
					organismes.addElement(rs.getString(1).trim());
					organismes.addElement(rs.getString(2).trim());
				}
				if (organismes.size()==0) {
					organismes.addElement("&nbsp;");
					organismes.addElement("");
					organismes.addElement("Error, no hi ha organismes!");
				}
				session.flush();
            } catch (Exception e) {
             organismes.addElement("&nbsp;");
             organismes.addElement("");
             organismes.addElement("getOrganismes Error en la SELECT");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return organismes;
	}
  
    /**
     * Retorna els tipus de document
     * @param codiTipDoc Codi del tipus de document a recuperar
     * @return informació del tipus de document, encapsulat dins la classe TipusDocumentData
     * @throws RemoteException
     * @throws RegwebException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public TipusDocumentData getTipusDocument(String codiTipDoc) throws RemoteException, RegwebException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		TipusDocumentData tipdoc=new TipusDocumentData();
		try {
			
			String sentenciaHql="SELECT id.codigo, nombre, id.fechaBaja  FROM TipoDocumento WHERE id.codigo=? ";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,codiTipDoc);
			rs = query.scroll();
			if (rs.next()) {
				tipdoc.setCodiTipusDoc( (rs.getString(0)!= null) ? rs.getString(0) : "" );
				tipdoc.setDescTipusDoc( (rs.getString(1)!= null) ? rs.getString(1).trim() : "" );
				
				DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInteger(2));
	            try {
	            	data=yyyymmdd.parse(dataBaixa);
	            	tipdoc.setDataBaixa(ddmmyyyy.format(data));
	            } catch (Exception e) {
	            	tipdoc.setDataBaixa(dataBaixa);
	            }
			} else {
				tipdoc=null;
			}


			session.flush();

		} catch (Exception e) {
			log.error(e);
			throw new RegwebException(e.getMessage());
        } finally {
            close(session);
        }
		return tipdoc;
	}
    
	/**
     * Retorna tots els tipus de documents de la BBDD, estiguin de baixa o no.
     * @return Vector amb tots els tipus de documents.
     * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Vector getTipusDocuments() throws RemoteException {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector documentos=new Vector();
		try {
			
			String sentenciaHql="SELECT id.codigo, nombre FROM TipoDocumento ORDER BY id.codigo";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll();
			documentos.addElement("");
			documentos.addElement("");
			while (rs.next()) {
				documentos.addElement(rs.getString(0));
				documentos.addElement(rs.getString(1).trim());
			}
			if (documentos.size()==0) {
				documentos.addElement("");
				documentos.addElement("No hi ha tipus de documents");
			}

		    session.flush();

        } catch (Exception e) {
         documentos.addElement("");
         documentos.addElement("Error en la SELECT");
         log.error("ERROR: " + e.getMessage());

        } finally {
            close(session);
        }
		return documentos;
	}
    
    /**
	 * Retorna un llistat de totes les unitat de gestio
	 * 
	 * @return Vector amb la informació de la unitat de gestio.
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getTotesUnitatDeGestio()  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector unitats=new Vector();
		
			try {
				
				String sentenciaHql="SELECT id.codigoOficina, id.codigoUnidad, nombre " + 
									"FROM UnidadDeGestion " + "" +
									"order by id.codigoOficina, id.codigoUnidad " ;
				
				Query query=session.createQuery(sentenciaHql);
				rs = query.scroll();
				
				while (rs.next()) {
					unitats.addElement(rs.getInteger(0).toString());
					unitats.addElement(rs.getInteger(1).toString());
					unitats.addElement(rs.getString(2));
					
				}
				
				if (unitats.size()==0) {
					unitats.addElement("");
					unitats.addElement("");
					unitats.addElement("No hi unitats de gestió creades.");
				}
				
				session.flush();
				
            } catch (Exception e) {
             unitats.addElement("");
             unitats.addElement("");
             unitats.addElement("getTotesUnitatDeGestio Error en la SELECT");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return unitats;
	}
	
    /**
	 * Retorna tots els organismes existents a la BBDD, estiguin de baixa o no.
	 * @return Vector amb tots els organismes
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getTotsOrganismes( )  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector organismes=new Vector();
		
			try {
				
				String sentenciaHql="SELECT codigo, nombreCorto, nombreLargo FROM Organismo ORDER BY codigo";
				Query query=session.createQuery(sentenciaHql);
				rs = query.scroll();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInteger(0)));
					organismes.addElement(rs.getString(1).trim());
					organismes.addElement(rs.getString(2).trim());
				}
				if (organismes.size()==0) {
					organismes.addElement("&nbsp;");
					organismes.addElement("");
					organismes.addElement("Error, no hi ha organismes!");
				}
				session.flush();
            } catch (Exception e) {
             organismes.addElement("&nbsp;");
             organismes.addElement("");
             organismes.addElement("getOrganismes Error en la SELECT");
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return organismes;
	}
    
    /**
	 * Retorna les dades d'una unitat de gestio
	 * @param oficinaGestionar codi de la oficina de registre a la que la unitat de gestió esta vinculada
	 * @param oficinaGestionarFisica codi de la unitat de gestió dins l'oficina de registre
	 * @return Vector amb la informació de la unitat de gestio.
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public Vector getUnitatDeGestio(int codiOficina, int codiUnitat)  throws RemoteException{
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector unitats=new Vector();
		
			try {
				
				String sentenciaHql="SELECT id.codigoOficina, id.codigoUnidad, nombre, direccionEmail, actiu " + 
									"FROM UnidadDeGestion " + "" +
									"WHERE id.codigoUnidad = ? AND id.codigoOficina = ? " ;
				
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,codiUnitat);
				query.setInteger(1,codiOficina);
				rs = query.scroll();
				
				if (rs.next()) {
					unitats.addElement(rs.getInteger(0).toString());
					unitats.addElement(rs.getInteger(1).toString());
					unitats.addElement(rs.getString(2));
					unitats.addElement(rs.getString(3));
					unitats.addElement(rs.getString(4));
					
					log.debug("Leida unidad de gestión id: "+codiOficina+"-"+codiUnitat+ ". Nombre:"+rs.getString(2)+" Actiu"+rs.getString(4));
				}
				
				if (unitats.size()==0) {
					unitats.addElement("");
					unitats.addElement("");
					unitats.addElement("La unidad de gestión id: "+codiOficina+"-"+codiUnitat+" no existe.");
					unitats.addElement("");
					unitats.addElement("");					
				}
				
				session.flush();
				
            } catch (Exception e) {
             unitats.addElement("");
             unitats.addElement("");
             unitats.addElement("BuscarModel Error en la SELECT");
             unitats.addElement("");
             unitats.addElement("");   
             
             log.error(e.getMessage());
            } finally {
                close(session);
            }
		return unitats;
	}
	
	/**
	 * Devuelve los usuarios y sus permisos para una oficina determinada. 
	 * @param oficina Codi de la 0ficina a buscar
	 * @return Información de los usuarios autorizados a una oficina encapsulado dentro de la clase AutoritzacionsOficinaData
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public AutoritzacionsOficinaData getUsuarisOficina(String oficina, boolean verPermisosConsulta) throws RemoteException{
    	
		AutoritzacionsOficinaData autUsuData = new AutoritzacionsOficinaData(oficina);
    	
    	TreeMap autUsuAE = new TreeMap();
    	TreeMap autUsuCE = new TreeMap();
    	TreeMap autUsuAS = new TreeMap();
    	TreeMap autUsuCS = new TreeMap();
    	TreeMap autUsuVE = new TreeMap();
    	TreeMap autUsuVS = new TreeMap();
    	Vector UsuariosDeLaOficina = new Vector();
    	
    	String tipusAut = "";
    	String codigoUsuario = "";
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
		try {
			
			String sentenciaHql="SELECT id.codigoAutorizacion, id.usuario  FROM Autorizacion " +
								"WHERE id.codigoOficina=? " +
								(!verPermisosConsulta? " and id.usuario in (select distinct id.usuario FROM Autorizacion where id.codigoAutorizacion not in ('CE','CS','VE','VS')  and id.codigoOficina = '"+oficina+ "') ":"") +
								" ORDER BY id.usuario, id.codigoAutorizacion ";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,new Integer(oficina));		
			rs = query.scroll();
			
			int i=0;
			while (rs.next()) {				
				tipusAut = rs.getString(0);				
				
				//Comprobamos si leemos un nuevo usuario
				if(!codigoUsuario.equals(rs.getString(1))){
					codigoUsuario = rs.getString(1);
					UsuariosDeLaOficina.add(codigoUsuario);
				}
			
				if ( tipusAut.equals("AE")) {
					autUsuAE.put(codigoUsuario,"X");
				} else if ( tipusAut.equals("CE")) {
					autUsuCE.put(codigoUsuario,"X");
				} else if ( tipusAut.equals("AS")) {
					autUsuAS.put(codigoUsuario,"X");
				} else if ( tipusAut.equals("CS")) {
					autUsuCS.put(codigoUsuario,"X");
				} else if ( tipusAut.equals("VE")) {
					autUsuVE.put(codigoUsuario,"X");
				} else if ( tipusAut.equals("VS")) {
					autUsuVS.put(codigoUsuario,"X");
				}
				i++;
			}
			if (i==0) {
				return null;
			} else {
				autUsuData.setAutModifEntrada(autUsuAE);
				autUsuData.setAutConsultaEntrada(autUsuCE);
				autUsuData.setAutModifSortida(autUsuAS);
				autUsuData.setAutConsultaSortida(autUsuCS);
				autUsuData.setAutVisaEntrada(autUsuVE);
				autUsuData.setAutVisaSortida(autUsuVS);
				autUsuData.setUsuariosDeLaOficina(UsuariosDeLaOficina);
			}		
			session.flush();

        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        	throw new RemoteException("getUsuarisOficina Error en la SELECT",e);
        } finally {
            close(session);
        }
    	return autUsuData;
    }
	
	/**
	 * Busca un modelo de documento según su tipo de idioma. 
	 * @param idioma Idioma del modelo (Catalán o castellano)
	 * @param tipo Tipo de modelo de email (Correo interno o externo)
	 * @return Datos del modelo de email encapsulado dentro de la clase ModeloDocumentoData. Si no existe el modelo, devuelve null.
	 * @throws RemoteException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public ModeloDocumentoData getModelEmail(String idioma, String tipo) throws RemoteException{{
		ModeloDocumentoData modeloDoc = null;
		Session session = getSession();
		ScrollableResults rs=null;

			try {
				
				String sentenciaHql="SELECT id.tipo, id.idioma, titulo, cuerpo " + 
									"FROM ModeloEmail " + "" +
									"WHERE id.tipo = ? AND id.idioma = ? " ;
				
				Query query=session.createQuery(sentenciaHql);
				query.setString(0,tipo);
				query.setString(1,idioma);
				rs = query.scroll();
				
				if (rs.next()) {
					modeloDoc = new ModeloDocumentoData();
					modeloDoc.setTipo(tipo);
					modeloDoc.setIdioma(idioma);					
					modeloDoc.setTitulo(rs.getString(2));
					modeloDoc.setCuerpo(rs.getString(3));
		
					log.debug("Leido modelo email id: "+tipo+"-"+idioma+ ". titulo:"+rs.getString(2));
				}
				session.flush();
				
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
            	throw new RemoteException("Error dins getModelEmail()",e);
            } finally {
                close(session);
            }
		return modeloDoc;
		
	}

}
}