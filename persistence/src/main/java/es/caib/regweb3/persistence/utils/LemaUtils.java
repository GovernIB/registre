package es.caib.regweb3.persistence.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

public class LemaUtils {

	protected final Logger log = Logger.getLogger(getClass());

	public static Date convertStringToDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String convertDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(date);
	}
	
	public static String getEmailFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(date);
	}

	public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}

	public static XMLGregorianCalendar convertStringToXMLGregorianCalendar(String date)
			throws ParseException, DatatypeConfigurationException {
		try {
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

			GregorianCalendar gregorianCalendar = new GregorianCalendar();

			gregorianCalendar.setTime(javax.xml.bind.DatatypeConverter.parseDateTime(date).getTime());

			XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

			xmlGregorianCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

			xmlGregorianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

			return xmlGregorianCalendar;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
		try {
			// Crear un objeto GregorianCalendar a partir de la fecha
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(date);

			// Convertir GregorianCalendar a XMLGregorianCalendar
			XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gregorianCalendar);

			xmlGregorianCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

			xmlGregorianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

			return xmlGregorianCalendar;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
