package es.caib.regweb.admin;

import java.util.TreeMap;

public class AutoritzacionsUsuariData {

	/**
	 * @param args
	 */
	TreeMap autModifEntrada = null;
	TreeMap autConsultaEntrada = null;
	TreeMap autModifSortida = null;
	TreeMap autConsultaSortida = null;
	TreeMap autVisaEntrada = null;
	TreeMap autVisaSortida = null;
	
	/**
      *  Conté les dades de les autoritzacions de l'usuari.
      */
    public AutoritzacionsUsuariData() {
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

}
