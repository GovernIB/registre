package es.caib.regweb.logic.helper;


public class AgrupacioGeograficaData {

	/**
	 * @param args
	 */
	String codiTipusAgruGeo = null;
	String descTipusAgruGeo = null;
	String codiAgruGeo = null;
	String descAgruGeo = null;
	String dataBaixa = null;
	String codiTipusAgruGeoSuperior = null;
	String codiAgruGeoSuperior = null;
	String descAgruGeoSuperior = null;
	String codiPostal = null;
	
	/**
     * Conté la informació de l'agrupació geogràfica
     */
    public AgrupacioGeograficaData() {
        this.codiTipusAgruGeo = new String("");
        this.codiAgruGeo = new String("");
        this.dataBaixa = new String("");
        this.codiTipusAgruGeoSuperior = new String("");
        this.codiAgruGeoSuperior = new String("");
        this.codiPostal = new String("");
    }

	/**
	 * @return Torna un string amb totes les dades
	 */
    public String toString() {
		return "Agrupació Geogràfica Data: "+codiTipusAgruGeo+" "+codiAgruGeo+" "
				+dataBaixa+" "+codiTipusAgruGeoSuperior+" "+codiAgruGeoSuperior+" "+codiPostal+".";
	}

	/**
	 * @return Returns the codiAgruGeo.
	 */
	public String getCodiAgruGeo() {
		return codiAgruGeo;
	}

	/**
	 * @param codiAgruGeo The codiAgruGeo to set.
	 */
	public void setCodiAgruGeo(String codiAgruGeo) {
		this.codiAgruGeo = codiAgruGeo;
	}

	/**
	 * @return Returns the codiAgruGeoSuperior.
	 */
	public String getCodiAgruGeoSuperior() {
		return codiAgruGeoSuperior;
	}

	/**
	 * @param codiAgruGeoSuperior The codiAgruGeoSuperior to set.
	 */
	public void setCodiAgruGeoSuperior(String codiAgruGeoSuperior) {
		this.codiAgruGeoSuperior = codiAgruGeoSuperior;
	}

	/**
	 * @return Returns the codiTipusAgruGeo.
	 */
	public String getCodiTipusAgruGeo() {
		return codiTipusAgruGeo;
	}

	/**
	 * @param codiTipusAgruGeo The codiTipusAgruGeo to set.
	 */
	public void setCodiTipusAgruGeo(String codiTipusAgruGeo) {
		this.codiTipusAgruGeo = codiTipusAgruGeo;
	}

	/**
	 * @return Returns the codiTipusAgruGeoSuperior.
	 */
	public String getCodiTipusAgruGeoSuperior() {
		return codiTipusAgruGeoSuperior;
	}

	/**
	 * @param codiTipusAgruGeoSuperior The codiTipusAgruGeoSuperior to set.
	 */
	public void setCodiTipusAgruGeoSuperior(String codiTipusAgruGeoSuperior) {
		this.codiTipusAgruGeoSuperior = codiTipusAgruGeoSuperior;
	}

	/**
	 * @return Returns the dataBaixa.
	 */
	public String getDataBaixa() {
		return dataBaixa;
	}

	/**
	 * @param dataBaixa The dataBaixa to set.
	 */
	public void setDataBaixa(String dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	/**
	 * @return Returns the descAgruGeo.
	 */
	public String getDescAgruGeo() {
		return descAgruGeo;
	}

	/**
	 * @param descAgruGeo The descAgruGeo to set.
	 */
	public void setDescAgruGeo(String descAgruGeo) {
		this.descAgruGeo = descAgruGeo;
	}

	/**
	 * @return Returns the codiPostal.
	 */
	public String getCodiPostal() {
		return codiPostal;
	}

	/**
	 * @param codiPostal The codiPostal to set.
	 */
	public void setCodiPostal(String codiPostal) {
		this.codiPostal = codiPostal;
	}

	/**
	 * @return Returns the descTipusAgruGeo.
	 */
	public String getDescTipusAgruGeo() {
		return descTipusAgruGeo;
	}

	/**
	 * @param descTipusAgruGeo The descTipusAgruGeo to set.
	 */
	public void setDescTipusAgruGeo(String descTipusAgruGeo) {
		this.descTipusAgruGeo = descTipusAgruGeo;
	}

	/**
	 * @return Returns the descTipusAgruGeoSuperior.
	 */
	public String getDescAgruGeoSuperior() {
		return descAgruGeoSuperior;
	}

	/**
	 * @param descTipusAgruGeoSuperior The descTipusAgruGeoSuperior to set.
	 */
	public void setDescAgruGeoSuperior(String descTipusAgruGeoSuperior) {
		this.descAgruGeoSuperior = descTipusAgruGeoSuperior;
	}
    


}
