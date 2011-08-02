package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.Vector;
/**
 * 
 * @author u104848 VHerrera
 *
 */
public class AutoritzacionsOficinaData implements Serializable {

	/**
	 * @param args
	 */
	// Atributs utilitzat per les consultes de usuaris per oficina
	String codiOficina= null; 
	
	boolean as = false;
	boolean ae = false;
	boolean cs = false;
	boolean ce = false;
	boolean vs = false;
	boolean ve = false;
	
	// Atributs utilitzat per les consultes de permisos per usuari
	TreeMap autModifEntrada = null;
	TreeMap autConsultaEntrada = null;
	TreeMap autModifSortida = null;
	TreeMap autConsultaSortida = null;
	TreeMap autVisaEntrada = null;
	TreeMap autVisaSortida = null;
	Vector UsuariosDeLaOficina = null;
	
	/**
     *  Conté les dades de les autoritzacions de d'oficina.
     */
   public AutoritzacionsOficinaData(String codiOficina) {
	   this.codiOficina=codiOficina;
       this.autModifEntrada = new TreeMap();
       this.autConsultaEntrada = new TreeMap();
       this.autModifSortida = new TreeMap();
       this.autConsultaSortida = new TreeMap();
       this.autVisaEntrada = new TreeMap();
       this.autVisaSortida = new TreeMap();
       this.UsuariosDeLaOficina = new Vector();
   }
   
	/**
      *  Conté les dades de les autoritzacions d'oficina.
      */
    public AutoritzacionsOficinaData() {
        this.autModifEntrada = new TreeMap();
        this.autConsultaEntrada = new TreeMap();
        this.autModifSortida = new TreeMap();
        this.autConsultaSortida = new TreeMap();
        this.autVisaEntrada = new TreeMap();
        this.autVisaSortida = new TreeMap();
    }

	/**
	 * @return Returns the autConsultaEntrada.
	 */
	public TreeMap getAutConsultaEntrada() {
		return autConsultaEntrada;
	}

	/**
	 * @param autConsultaEntrada The autConsultaEntrada to set.
	 */
	public void setAutConsultaEntrada(TreeMap autConsultaEntrada) {
		this.autConsultaEntrada = autConsultaEntrada;
	}

	/**
	 * @return Returns the autConsultaSortida.
	 */
	public TreeMap getAutConsultaSortida() {
		return autConsultaSortida;
	}

	/**
	 * @param autConsultaSortida The autConsultaSortida to set.
	 */
	public void setAutConsultaSortida(TreeMap autConsultaSortida) {
		this.autConsultaSortida = autConsultaSortida;
	}

	/**
	 * @return Returns the codiOrganisme.
	 */
	public TreeMap getAutModifEntrada() {
		return autModifEntrada;
	}

	/**
	 * @param codiOrganisme The codiOrganisme to set.
	 */
	public void setAutModifEntrada(TreeMap autModifEntrada) {
		this.autModifEntrada = autModifEntrada;
	}

	/**
	 * @return Returns the autModifSortida.
	 */
	public TreeMap getAutModifSortida() {
		return autModifSortida;
	}

	/**
	 * @param autModifSortida The autModifSortida to set.
	 */
	public void setAutModifSortida(TreeMap autModifSortida) {
		this.autModifSortida = autModifSortida;
	}

	/**
	 * @return Returns the autVisaEntrada.
	 */
	public TreeMap getAutVisaEntrada() {
		return autVisaEntrada;
	}

	/**
	 * @param autVisaEntrada The autVisaEntrada to set.
	 */
	public void setAutVisaEntrada(TreeMap autVisaEntrada) {
		this.autVisaEntrada = autVisaEntrada;
	}

	/**
	 * @return Returns the autVisaSortida.
	 */
	public TreeMap getAutVisaSortida() {
		return autVisaSortida;
	}

	/**
	 * @param autVisaSortida The autVisaSortida to set.
	 */
	public void setAutVisaSortida(TreeMap autVisaSortida) {
		this.autVisaSortida = autVisaSortida;
	}
	/**
	 * @return the codiOficina
	 */
	public String getCodiOficina() {
		return codiOficina;
	}

	/**
	 * @param codiUsuari the codiOficina to set
	 */
	public void setCodiOficina(String codiOficina) {
		this.codiOficina = codiOficina;
	}

	/**
	 * @return the as
	 */
	public boolean isAs() {
		return as;
	}

	/**
	 * @param as the as to set
	 */
	public void setAs(boolean as) {
		this.as = as;
	}

	/**
	 * @return the ae
	 */
	public boolean isAe() {
		return ae;
	}

	/**
	 * @param ae the ae to set
	 */
	public void setAe(boolean ae) {
		this.ae = ae;
	}

	/**
	 * @return the cs
	 */
	public boolean isCs() {
		return cs;
	}

	/**
	 * @param cs the cs to set
	 */
	public void setCs(boolean cs) {
		this.cs = cs;
	}

	/**
	 * @return the ce
	 */
	public boolean isCe() {
		return ce;
	}

	/**
	 * @param ce the ce to set
	 */
	public void setCe(boolean ce) {
		this.ce = ce;
	}

	/**
	 * @return the vs
	 */
	public boolean isVs() {
		return vs;
	}

	/**
	 * @param vs the vs to set
	 */
	public void setVs(boolean vs) {
		this.vs = vs;
	}

	/**
	 * @return the ve
	 */
	public boolean isVe() {
		return ve;
	}

	/**
	 * @param ve the ve to set
	 */
	public void setVe(boolean ve) {
		this.ve = ve;
	}

	/**
	 * @return the usuariosDeLaOficina
	 */
	public Vector getUsuariosDeLaOficina() {
		return UsuariosDeLaOficina;
	}

	/**
	 * @param usuariosDeLaOficina the usuariosDeLaOficina to set
	 */
	public void setUsuariosDeLaOficina(Vector usuariosDeLaOficina) {
		UsuariosDeLaOficina = usuariosDeLaOficina;
	}


}
