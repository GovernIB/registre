/*
 * RegistroSeleccionado.java
 *
 * Created on 13 de octubre de 2004, 14:48
 */

package es.caib.regweb.logic.helper;

/**
 * Classe per desar la informació desada del control d'accés dels registres E/S
 * @author  Sebastià Matas
 * @version 1.0
 */

public class RegistroESlopdSeleccionado /*implements Comparable*/ {
	
	/** Creates a new instance of RegistroESlopdSeleccionado */
	public RegistroESlopdSeleccionado() {
		// Inicialitzam tot a "";
		tipusAcces="";
		usuCanvi="";
		dataCanvi="";
		horaCanvi="";
		nombreRegistre="";
		anyRegistre="";
		anyRegistre="";
		oficinaRegistre="";
		oficinaRegistreDesc="";
		tipusVisat="";
		dataModif="";
		horaModif="";
	}
//Modificacions de visat
//  FZVTIPAC	FZVCUSU	FZVDATAC	FZVHORAC	FZVCENSA	FZVNUMEN	FZVANOEN	FZVCAGCO	FZVFMODI	FZVHMODI	FAACAGCO	FAADAGCO			FAAFBAJA	
//	INSERT    	U83511  20070410	150217168	S			1			2007		2			20070410	15021716	2			C.I. DE MENORCA     	0	
	
	public String tipusAcces, usuCanvi, dataCanvi, horaCanvi
		, nombreRegistre, anyRegistre, oficinaRegistre, oficinaRegistreDesc, tipusVisat, 
		dataModif, horaModif; //Nota: tipuVisat indica si el visat és d'entrada (E) o sortida (S).
	
	public String getAnyRegistre() {
		return anyRegistre;
	}
	public void setAnyRegistre(String anyRegistre) {
		this.anyRegistre = anyRegistre;
	}
	public String getDataCanvi() {
		return dataCanvi;
	}
	public void setDataCanvi(String dataCanvi) {
		this.dataCanvi = dataCanvi;
	}
	public String getHoraCanvi() {
		return horaCanvi;
	}
	public void setHoraCanvi(String horaCanvi) {
		this.horaCanvi = horaCanvi;
	}
	public String getNombreRegistre() {
		return nombreRegistre;
	}
	public void setNombreRegistre(String nombreRegistre) {
		this.nombreRegistre = nombreRegistre;
	}
	public String getOficinaRegistre() {
		return oficinaRegistre;
	}
	public void setOficinaRegistre(String oficinaRegistre) {
		this.oficinaRegistre = oficinaRegistre;
	}
	public String getOficinaRegistreDesc() {
		return oficinaRegistreDesc;
	}
	public void setOficinaRegistreDesc(String oficinaRegistreDesc) {
		this.oficinaRegistreDesc = oficinaRegistreDesc;
	}
	public String getTipusAcces() {
		return tipusAcces;
	}
	public void setTipusAcces(String tipusAcces) {
		this.tipusAcces = tipusAcces;
	}
	public String getUsuCanvi() {
		return usuCanvi;
	}
	public void setUsuCanvi(String usuCanvi) {
		this.usuCanvi = usuCanvi;
	}
	/**
	 * @return Returns the dataModif.
	 */
	public String getDataModif() {
		return dataModif;
	}
	/**
	 * @param dataModif The dataModif to set.
	 */
	public void setDataModif(String dataModif) {
		this.dataModif = dataModif;
	}
	/**
	 * @return Returns the horaModif.
	 */
	public String getHoraModif() {
		return horaModif;
	}
	/**
	 * @param horaModif The horaModif to set.
	 */
	public void setHoraModif(String horaModif) {
		this.horaModif = horaModif;
	}
	/**
	 * @return Returns the tipusVisat.
	 */
	public String getTipusVisat() {
		return tipusVisat;
	}
	/**
	 * @param tipusVisat The tipusVisat to set.
	 */
	public void setTipusVisat(String tipusVisat) {
		this.tipusVisat = tipusVisat;
	}

	/**
	 * Torna una string amb el contingut del registre de log.
	 */
	public String toString() {
		String tmp="Tipus accés: "+tipusAcces+ " Usuari: "+usuCanvi
				+"\t\n data operació: "+ dataCanvi + " hora operació: "+ horaCanvi
				+ "\t\n nombre registre: "+ nombreRegistre + " any registre: " +
				anyRegistre +" oficinaregistre: "+oficinaRegistre +"-"+ oficinaRegistreDesc;
		
		if (!tipusVisat.equals("")) {
			tmp = tmp+ " tipus visat: "+ tipusVisat + "data modificació: "+ 
				dataModif + " hora modificació: "+ horaModif;
		}
		return tmp;
	}
	/*public int compareTo(Object o) {
		
		if (!(o instanceof RegistroESlopdSeleccionado)) {
			throw new ClassCastException();
		}
		
		RegistroESlopdSeleccionado reg=(RegistroESlopdSeleccionado)o;
		int resultado=anyRegistre-reg.getAnyRegistre();
		if (resultado==0) {
			return numero-reg.getNumeroEntrada();
		} else {
			return numero;
		}
	}*/	
}
