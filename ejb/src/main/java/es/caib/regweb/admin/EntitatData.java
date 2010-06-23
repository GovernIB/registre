package es.caib.regweb.admin;


public class EntitatData {

	/**
	 * @param args
	 */
	String codigoEntidad = null;
	String codiEntitat = null;
	String subcodiEnt = null;
	String descEntidad = null;
	String descEntitat = null;
	String dataBaixa = null;
	
	
	   /**
	    * Classe que conté les dades d'una entitat
	    */
    public EntitatData() {
        this.codigoEntidad = new String("");
        this.codiEntitat = new String("");
        this.subcodiEnt = new String("");
        this.descEntidad = new String("");
        this.descEntitat = new String("");
        this.dataBaixa = new String("");
    }

	/**
	 * @return Torna un string amb totes les dades
	 */
    public String toString() {
		return "EntitatData: "+codigoEntidad+" "+codiEntitat+" "
				+subcodiEnt+" "+descEntidad+" "+descEntitat+" "+dataBaixa+".";
	}
    
    /**
	 * @return Returns the codigoEntidad.
	 */
	public String getCodigoEntidad() {
		return codigoEntidad;
	}

	/**
	 * @param codigoEntidad The codigoEntidad to set.
	 */
	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	/**
	 * @return Returns the codiEntitat.
	 */
	public String getCodiEntitat() {
		return codiEntitat;
	}

	/**
	 * @param codiEntitat The codiEntitat to set.
	 */
	public void setCodiEntitat(String codiEntitat) {
		this.codiEntitat = codiEntitat;
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
	 * @return Returns the descEntidad.
	 */
	public String getDescEntidad() {
		return descEntidad;
	}

	/**
	 * @param descEntidad The descEntidad to set.
	 */
	public void setDescEntidad(String descEntidad) {
		this.descEntidad = descEntidad;
	}

	/**
	 * @return Returns the descEntitat.
	 */
	public String getDescEntitat() {
		return descEntitat;
	}

	/**
	 * @param descEntitat The descEntitat to set.
	 */
	public void setDescEntitat(String descEntitat) {
		this.descEntitat = descEntitat;
	}

	/**
	 * @return Returns the subcodiEnt.
	 */
	public String getSubcodiEnt() {
		return subcodiEnt;
	}

	/**
	 * @param subcodiEnt The subcodiEnt to set.
	 */
	public void setSubcodiEnt(String subcodiEnt) {
		this.subcodiEnt = subcodiEnt;
	}



}
