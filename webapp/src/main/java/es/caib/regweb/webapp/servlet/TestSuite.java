/*
 * Main.java
 *
 * Created on 29 de noviembre de 2004, 12:33
 */

package es.caib.regweb.webapp.servlet;

/**
 *
 * @author smart41
 */
public class TestSuite {
    
    private static int NUMERO_CLIENTES=80;
    
    /** Creates a new instance of Main */
    public TestSuite() {
    }
    
    /**
     * @param args the command line arguments
     */
    public void genera() {
        // TODO code application logic here
       RegistroEntradaClientThread[] rec;
       rec = new RegistroEntradaClientThread[NUMERO_CLIENTES];
       for (int i=0; i<NUMERO_CLIENTES;i++)
       rec[i]=new RegistroEntradaClientThread(i+1);
       
       for (int i=0;i<NUMERO_CLIENTES;i++)
           rec[i].start();
    }
    
}
