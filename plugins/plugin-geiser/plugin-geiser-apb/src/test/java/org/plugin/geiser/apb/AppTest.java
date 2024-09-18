package org.plugin.geiser.apb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;

public class AppTest {
	
	protected final Logger log = Logger.getLogger(getClass());
	
	@Test
    public void formatDate() throws ParseException {
		try {
			String nomAntic = "prova_dnot_20230809T11564939.xml";
			System.out.println(renameTitle(nomAntic));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	private String renameTitle(String fileName) {
    	return fileName.replaceAll("_dnot_\\d{8}T\\d{8}", "_dnot");
    }


}
