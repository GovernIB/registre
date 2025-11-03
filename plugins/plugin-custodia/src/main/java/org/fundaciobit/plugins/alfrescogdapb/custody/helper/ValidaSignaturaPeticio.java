package org.fundaciobit.plugins.alfrescogdapb.custody.helper;

public class ValidaSignaturaPeticio {

	private byte[] contingut;
	private AutenticacioDto autenticacio;
	
	public byte[] getContingut() {
		return contingut;
	}
	public void setContingut(byte[] contingut) {
		this.contingut = contingut;
	}
	public AutenticacioDto getAutenticacio() {
		return autenticacio;
	}
	public void setAutenticacio(AutenticacioDto autenticacio) {
		this.autenticacio = autenticacio;
	}
	
}
