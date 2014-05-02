package es.caib.regweb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Util {
    public static String getFecha() {
		DateFormat dateF=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaTest=new java.util.Date();
		return dateF.format(fechaTest);
    }
		
	public static String getHorasMinutos() {
			DateFormat dateF=new SimpleDateFormat("HH:mm");
			java.util.Date fechaTest=new java.util.Date();
			return dateF.format(fechaTest);
		
	}
}
