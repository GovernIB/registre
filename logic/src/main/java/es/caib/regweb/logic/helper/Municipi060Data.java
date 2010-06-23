package es.caib.regweb.logic.helper;

public class Municipi060Data {

	/**
	 * @param args
	 */
	String codMun060 = null;
	String descMun060 = null;
	String dataBaixa = null;
	
	
	/**
     *  Conté les dades del tipus de document
     */
    public Municipi060Data() {
        this.codMun060 = new String("");
        this.descMun060 = new String("");
        this.dataBaixa = new String("0");
    }

	/**
	 * @return Returns the codiEntitat.
	 */
	public String getCodiMunicipi060() {
		return codMun060;
	}

	/**
	 * @param codiMunicipi060 The codiEntitat to set.
	 */
	public void setCodiMunicipi060(String codiMunicipi060) {
		this.codMun060 = codiMunicipi060;
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
	 * @return Returns the descEntitat.
	 */
	public String getDescMunicipi060() {
		return descMun060;
	}

	/**
	 * @param descMunicipi060 The descEntitat to set.
	 */
	public void setDescMunicipi060(String descMunicipi060) {
		this.descMun060 = descMunicipi060;
	}

}
