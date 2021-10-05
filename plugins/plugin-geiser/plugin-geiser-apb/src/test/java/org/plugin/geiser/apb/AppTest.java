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
			//22-09-2021 11:19:56
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			System.out.println(sdf.parse("20210922111956"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
