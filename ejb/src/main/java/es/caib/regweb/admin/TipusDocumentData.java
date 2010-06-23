package es.caib.regweb.admin;

public class TipusDocumentData {

	/**
	 * @param args
	 */
	String codiTipusDoc = null;
	String descTipusDoc = null;
	String dataBaixa = null;
	
	
	/**
     *  Conté les dades del tipus de document
     */
    public TipusDocumentData() {
        this.codiTipusDoc = new String("");
        this.descTipusDoc = new String("");
        this.dataBaixa = new String("0");
    }

	/**
	 * @return Returns the codiEntitat.
	 */
	public String getCodiTipusDoc() {
		return codiTipusDoc;
	}

	/**
	 * @param codiEntitat The codiEntitat to set.
	 */
	public void setCodiTipusDoc(String codiEntitat) {
		this.codiTipusDoc = codiEntitat;
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
	public String getDescTipusDoc() {
		return descTipusDoc;
	}

	/**
	 * @param descEntitat The descEntitat to set.
	 */
	public void setDescTipusDoc(String descEntitat) {
		this.descTipusDoc = descEntitat;
	}

}
