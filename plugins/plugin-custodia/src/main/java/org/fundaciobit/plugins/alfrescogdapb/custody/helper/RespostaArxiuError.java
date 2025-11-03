package org.fundaciobit.plugins.alfrescogdapb.custody.helper;

import java.util.List;

public class RespostaArxiuError {
	
	private RespostaArxiuStatus status;
	
	private String message;
	
	private String exception;

	private List<String> callstack;
	
	private String server;
	
	private String time;
	
	public RespostaArxiuStatus getStatus() {
		return status;
	}

	public void setStatus(RespostaArxiuStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public List<String> getCallstack() {
		return callstack;
	}

	public void setCallstack(List<String> callstack) {
		this.callstack = callstack;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
