package org.fundaciobit.plugins.alfrescogdapb.custody.helper;

/**
 * Constants del plugin de validació de firmes
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface SignatureConstants {

	public static final String SIGNTYPE_PAdES = "PAdES";
	
	public static final String SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED = "implicit_enveloped/attached";
	public static final String SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED = "implicit_enveloping/attached";
	public static final String SIGNFORMAT_EXPLICIT_DETACHED = "explicit/detached";
	public static final String SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED = "explicit/externally_detached";
	
	public static final String SIGNPROFILE_EPES = "EPES";
	public static final String SIGNPROFILE_T = "T";
	public static final String SIGNPROFILE_C = "C";
	public static final String SIGNPROFILE_X = "X";
	public static final String SIGNPROFILE_XL = "XL";
	public static final String SIGNPROFILE_A = "A";
	public static final String SIGNPROFILE_PADES_LTV = "LTV";
	public static final String SIGNPROFILE_BASELINE_B_LEVEL = "BASELINE B-Level";
	public static final String SIGNPROFILE_BASIC = "Basic";
	
}
