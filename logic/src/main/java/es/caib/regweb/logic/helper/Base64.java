/*
 
 Copyright (C) 2004  Owen Borseth - owen@name.com
 
 */

package es.caib.regweb.logic.helper;

import java.util.*;
import java.math.*;

/**
 * Classe per codificar/decodificar en Base64
 * @author FJMARTINEZ
 * @version 1.0
 */

class Base64 {
	byte[] bytes;
	HashMap base64Table = new HashMap();
	HashMap base64DecodeTable = new HashMap();
	
	public Base64() {
		setBase64HashMap();
		setBase64DecodeHashMap();
	}
	
	public String decode(String s) {
		String asciiString = "";
		byte[] mask = new byte[] {48,48,48,48,48,48};
		ArrayList bin8BitArray = new ArrayList();
		
		for(int i = 0; i < s.length(); i++) {
			String base64Code = (String)base64DecodeTable.get(s.substring(i, (i+1)));
			String binString = new BigInteger(base64Code).toString(2);
			byte[] binByte = new BigInteger(base64Code).toString(2).getBytes();
			
			if(!binString.equals("1000000")) {
				for(int j = (binByte.length - 1), k = mask.length - 1; j > -1; j--, k--) {
					mask[k] = binByte[j];
				}
			}
			
			binString = new String(mask);
			mask = new byte[] {48,48,48,48,48,48};
			asciiString = asciiString + binString;
		}
		
		while(asciiString.endsWith("00000000")) {
			asciiString = asciiString.substring(0, asciiString.length() - 8);
		}
		
		String bitGroup = "";
		for(int i = 0; i < asciiString.length(); i = i + 8) {
			bitGroup = asciiString.substring(i, i + 8);
			BigInteger bi = new BigInteger(bitGroup, 2);
			Integer asciiValue = new Integer(bi.toString());
			
			bin8BitArray.add(asciiValue);
		}
		
		asciiString = "";
		char[] myChars = new char[bin8BitArray.size()];
		for(int i = 0; i < bin8BitArray.size(); i++) {
			Integer myInteger = (Integer)bin8BitArray.get(i);
			myChars[i] = (char)myInteger.intValue();
		}
		
		asciiString = new String(myChars);
		
		return(asciiString);
	}
	
	public String encode(String s) {
		byte[] bytes = s.getBytes();
		byte[] mask = new byte[] {48,48,48,48,48,48,48,48};
		String pad = "00000000";
		String binString = "";
		String base64String = "";
		ArrayList bin6BitArray = new ArrayList();
		
		for(int i = 0; i < bytes.length; i++) {
			byte[] myByte = new byte[1];
			myByte[0] = bytes[i];
			BigInteger bi = new BigInteger(myByte);
			byte[] binByte = bi.toString(2).getBytes();
			
			for(int j = (binByte.length - 1), k = mask.length - 1; j > -1; j--, k--) {
				mask[k] = binByte[j];
			}
			
			String wholeByte = new String(mask);
			mask = new byte[] {48,48,48,48,48,48,48,48};
			
			binString = binString + wholeByte;
		}
		
		int sLength = binString.length() / 8;
		int sPad = 3 - (sLength % 3);
		
		if(sPad == 1)
			binString = binString + pad;
		else if(sPad == 2)
			binString = binString + pad + pad;
		
		String bitGroup = "";
		for(int i = 0; i < binString.length(); i = i + 6) {
			bitGroup = binString.substring(i, i + 6);
			BigInteger bi = new BigInteger(bitGroup, 2);
			
			if(bitGroup.equals("000000"))
				bin6BitArray.add("64");
			else
				bin6BitArray.add(bi.toString());
		}
		
		for(int i = 0; i < bin6BitArray.size(); i++) {
			String base64Code = (String)bin6BitArray.get(i);
			base64String = base64String + (String)base64Table.get(base64Code);
		}
		
		return(base64String);
	}
	
	public void setBase64HashMap() {
		char i = 0;
		
		char increment = 65;
		for(; i < 26; i++) {
			char[] myChars = new char[] {(char)(i + increment)};
			base64Table.put(new Integer(i).toString(), new String(myChars));
		}
		
		increment = (char)(97 - (i));
		for(; i < 52; i++) {
			char[] myChars = new char[] {(char)(i + increment)};
			base64Table.put(new Integer(i).toString(), new String(myChars));
		}
		
		increment = (char)(48 - (i));
		for(; i < 62; i++) {
			char[] myChars = new char[] {(char)(i + increment)};
			base64Table.put(new Integer(i).toString(), new String(myChars));
		}
		
		base64Table.put(new Integer(62).toString(), "+");
		
		base64Table.put(new Integer(63).toString(), "/");
		
		base64Table.put(new Integer(64).toString(), "=");
	}
	
	public void setBase64DecodeHashMap() {
		char i = 0;
		
		char increment = 65;
		for(; i < 26; i++) {
			char[] myChars = new char[] {(char)(i + increment)};
			base64DecodeTable.put(new String(myChars), new Integer(i).toString());
		}
		
		increment = (char)(97 - (i));
		for(; i < 52; i++) {
			char[] myChars = new char[] {(char)(i + increment)};
			base64DecodeTable.put(new String(myChars), new Integer(i).toString());
		}
		
		increment = (char)(48 - (i));
		for(; i < 62; i++) {
			char[] myChars = new char[] {(char)(i + increment)};
			base64DecodeTable.put(new String(myChars), new Integer(i).toString());
		}
		
		base64DecodeTable.put("+", new Integer(62).toString());
		
		base64DecodeTable.put("/", new Integer(63).toString());
		
		base64DecodeTable.put("=", new Integer(64).toString());
	}
}
