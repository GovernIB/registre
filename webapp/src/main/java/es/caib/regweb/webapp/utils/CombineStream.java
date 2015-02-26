package es.caib.regweb.webapp.utils;

/*
 * CombineStream.java
 *
 * De Registre Antic
 */

import java.io.InputStream;


public class CombineStream extends InputStream {

    java.io.InputStream inputStream = null;
    @SuppressWarnings("rawtypes")
    java.util.Hashtable values      = null;

    int LENOUT = 1024*40;
    int LENIN  = 1024*40;

    byte bufferOut[] = new byte[LENOUT];
    byte bufferIn[]  = new byte[LENIN];

    int  posReadOut  = 0; // Posició del pròxim caràcter a llegir
    int  posLastOut  = 0; // Posició del primer caràcter no llegit
    boolean EOFOut= false;

    boolean inBuffer = false;

    int posReadIn     = 0;
    int posLastIn     = 0;

    int oldChar = 0;


    public CombineStream(java.io.InputStream is,@SuppressWarnings("rawtypes") java.util.Hashtable val) {
        inputStream = is;
        values      = val;
    }

    private boolean isSeparator(int car) {
        return (car==' ' || car=='\t' || car=='\n' || car=='\r');
    }

    private String getVarValue(String name) {
        Object obj = null;
        if (values!=null) obj = values.get(name);
        if (obj==null) obj="";
        String sobj = obj.toString();

        //System.err.println("utils.flow.CombineStream.getVarValue");
	    /*//System.out.println("utils.flow.CombineStream.getVarValue");
	    System.err.println(name+"="+obj);*/

        return sobj;
    }

    private String replaceGroup(String group) {

        int posf = group.indexOf("MERGEFIELD");
        if (posf==-1) return group;
        int posi = group.substring(0,posf).lastIndexOf("{");
        int posNamei = group.indexOf(" ",posf);
        while (group.substring(posNamei,posNamei+1).equals(" ")) posNamei++;
        int posNamef = posNamei;
        while (!group.substring(posNamef,posNamef+1).equals(" ")) posNamef++;
        String name = group.substring(posNamei,posNamef);
        String newString = group.substring(posi,posf).trim()+" "+getVarValue(name)+"}";

        // System.err.println("replaceGroup->  "+group);
        // System.err.println("replacedGroup-> "+newString);

        return newString;
    }


    private int readTranslatedChar() throws java.io.IOException {

        int currentChar = -1;

        if (!inBuffer) {
            currentChar = inputStream.read();
            if (currentChar==(int)'{' && oldChar!=(int)'\\') {
                ////System.out.println("Primer caracter ok");
                inBuffer = true;
                posReadIn = 0;
                bufferIn[0] = (byte)currentChar;
                posLastIn=1;
                while ( isSeparator(currentChar=inputStream.read()) && currentChar!=-1) {
                    bufferIn[posLastIn] = (byte)currentChar;
                    posLastIn++;
                }
                if (currentChar!=-1) {
                    bufferIn[posLastIn] = (byte)currentChar;
                    posLastIn++;
                }
                if (currentChar=='\\') {
                    ////System.out.println("Segundo caracter ok");
                    String controlWord="";
                    while ( !isSeparator(currentChar=inputStream.read()) && currentChar!=-1 && (currentChar!='\\' || controlWord.endsWith("*")) && currentChar!='{') {
                        controlWord += (char)currentChar;
                        bufferIn[posLastIn] = (byte)currentChar;
                        posLastIn++;
                    }
                    if (currentChar!=-1) {
                        bufferIn[posLastIn] = (byte)currentChar;
                        posLastIn++;
                    }
                    if (controlWord.equalsIgnoreCase("field")) {
                        ////System.out.println("controlWord == 'field'");
                        int oc = currentChar; // Darrer caràcter llegit
                        int n = 1 + ((currentChar=='{')? 1:0);            // Nro de '{' llegits fins ara

                        while (n>0 && (currentChar=inputStream.read())!=-1) {
                            if (currentChar=='{' && oc!='\\') n++;
                            if (currentChar=='}' && oc!='\\') n--;
                            bufferIn[posLastIn] = (byte)currentChar;
                            posLastIn++;
                            oc = currentChar;
                        }
                        // translate:
                        ////System.out.println("Llamando a replaceGroup con : " + new String(bufferIn,0,posLastIn));
                        String newString = replaceGroup(new String(bufferIn,0,posLastIn));
                        if (LENIN < newString.length()) {
                            LENIN = newString.length();
                            bufferIn = new byte[LENIN];
                        }
                        System.arraycopy(newString.getBytes(),0,bufferIn,0,newString.length());
                        posLastIn = newString.length();
                    }
                    if (controlWord.equalsIgnoreCase("*\\xmlnstbl")) {
                        ////System.out.println("controlWord == 'field'");
                        int oc = currentChar; // Darrer caràcter llegit
                        int n = 1 + ((currentChar=='{')? 1:0);            // Nro de '{' llegits fins ara

                        while (n>0 && (currentChar=inputStream.read())!=-1) {
                            if (currentChar=='{' && oc!='\\') n++;
                            if (currentChar=='}' && oc!='\\') n--;
                            bufferIn[posLastIn] = (byte)currentChar;
                            posLastIn++;
                            oc = currentChar;
                        }
                        // translate:
                        ////System.out.println("Llamando a replaceGroup con : " + new String(bufferIn,0,posLastIn));
                        byte[] sololect = (byte[])values.get("(read_only)");
                        if (sololect!=null) {
                            System.arraycopy(sololect,0,bufferIn,posLastIn,sololect.length);
                            posLastIn += sololect.length;
                        }

                    }

                }
            }
        }
        if (inBuffer) {
            currentChar = bufferIn[posReadIn];
            posReadIn++;
            if (posReadIn >= posLastIn) inBuffer = false;
        }

        oldChar = currentChar;

        return currentChar;
    }

    private int readTranslated(byte bOut[]) throws java.io.IOException {
        int i=0;

        int b=readTranslatedChar();
        if (b!=-1) {
            for (;b!=-1 && i<bOut.length;i++) {
                bOut[i] = (byte)b;
                if ((i+1)<bOut.length) b = readTranslatedChar();
            }
        }
        return i;
    }

    private void fillBufferOut() throws java.io.IOException {
        posReadOut = 0;
        posLastOut = readTranslated(bufferOut);
        //posLastOut = inputStream.read(bufferOut);

        // System.err.println("fillBufferOut: "+new String(bufferOut,0,posLastOut) );
    }

    public int read() throws java.io.IOException {
        int car = -1;
        if (!EOFOut) {
            if (posReadOut>=LENOUT || posReadOut>=posLastOut) fillBufferOut();
            EOFOut = posReadOut >= LENOUT || posReadOut >= posLastOut;
            if (!EOFOut) {
                car = bufferOut[posReadOut];
                posReadOut++;
            }
        }
        return car;
    }


}
