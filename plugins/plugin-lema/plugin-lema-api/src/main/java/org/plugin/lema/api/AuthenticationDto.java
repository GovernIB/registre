package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthenticationDto {
	
	private String keystoreFile;
	private String keystorePass;
	private String keystoreAlias;
	private String keystoreType;

	private String endpointLocation;
	
}
