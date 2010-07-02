/*
 * PdfServlet.java
 *
 * Created on 26 de marzo de 2003, 16:13
 */

package es.caib.regweb.webapp.servlet;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.*;
import javax.servlet.http.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;

import com.lowagie.text.pdf.PdfAction.*;
import com.lowagie.text.Chunk;
import org.apache.log4j.Logger;

/**
 *
 * @author  SNAVA05
 * @version
 */
public class PdfServlet extends HttpServlet {

	private Logger log = Logger.getLogger(this.getClass());
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        System.out.println("Comienza");

        request.setCharacterEncoding("UTF-8");

        String oficina=request.getParameter("oficina");
        String oficinaid=request.getParameter("oficinaid");
        String numeroEntrada=request.getParameter("numero");
        String anoEntrada=request.getParameter("ano");
        String dataEntrada=request.getParameter("data");
        String tipo=request.getParameter("tipo");
        String es=(request.getParameter("ES").equals("E")) ? "ENTRADES":"SORTIDES";
        String auto_print=request.getParameter("auto_print");
        if (auto_print == null) auto_print = "no";

        int x=0;
        int y=0;
        
        if (tipo.equals("1")) {
            x=57;
            y=785;
        } else if (tipo.equals("2")) {
            x=400;
            y=785;
        } else if (tipo.equals("3")) {
            x=57;
            y=171;
        } else if (tipo.equals("4")) {
            x=400;
            y=171;
        } else if (tipo.equals("5")) {
            x=57;
            y=550;
        } else if (tipo.equals("6")) {
            x=590;
            y=550;
        } else if (tipo.equals("7")) {
            x=57;
            y=81;
        } else if (tipo.equals("8")) {
            x=590;
            y=81;
        }

        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",  "inline; filename=\"imprimeSello.pdf\"");
        response.setHeader("Cache-Control",  "store");
        response.setHeader("Pragma", "cache");
        // step 1: creation of a document-object
        Document document=null;
        if (tipo.equals("5") || tipo.equals("6") || tipo.equals("7") || tipo.equals("8")) {
            document = new Document(PageSize.A4.rotate());
        } else {
            document = new Document(PageSize.A4);
        }
        ByteArrayOutputStream salida=new ByteArrayOutputStream();
        try {
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            String nom_oficina = valores.recuperaDescripcionOficina(oficinaid);

            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
//            System.out.println("Empieza getInstance");
            PdfWriter writer = PdfWriter.getInstance(document, salida);   
            writer.setViewerPreferences(PdfWriter.HideMenubar
                | PdfWriter.HideToolbar | PdfWriter.PageLayoutSinglePage 
                | PdfWriter.HideWindowUI);
            // step 3: we open the document
            document.open();
            
            Locale localeLang = Locale.getDefault();
            ResourceBundle messages = ResourceBundle.getBundle("messages", localeLang);

            // step 4: we grab the ContentByte and do some stuff with it
            PdfContentByte cb = writer.getDirectContent();
            
            // first we draw some lines to be able to visualize the text alignment functions
            cb.setLineWidth(0f);
            cb.stroke();
            
            // we tell the ContentByte we're ready to draw text
            cb.beginText();
            
            BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.setFontAndSize(bf, 10);
            // 
            cb.setTextMatrix(x,y);
            cb.showText(checkUTF( messages.getString("pdf.titol") ));
            y=y-13;
            cb.setTextMatrix(x,y);
            cb.showText(nom_oficina);
            y=y-13;
            cb.setTextMatrix(x,y);
            cb.showText(checkUTF(messages.getString("pdf.registre") + " " + es));
            y=y-13;
            cb.setTextMatrix(x,y);
            cb.showText(checkUTF(messages.getString("pdf.num") + " " + numeroEntrada + "/" + anoEntrada));
            y=y-13;
            cb.setTextMatrix(x,y);
            cb.showText(checkUTF(messages.getString("pdf.data") + " " + dataEntrada));
         // we tell the contentByte, we've finished drawing text
            
            if(auto_print.equals("si")){
            			writer.addJavaScript("this.print(true, this.pageNum, this.pageNum);");
            			
            }
            
            cb.setAction(PdfAction.javaScript("this.print(true, this.pageNum, this.pageNum);", writer), 0f, 0f, 800f, 830f);
            
            cb.endText();

        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
            de.printStackTrace();
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }
        catch (Exception e) {
            log.error("REGWEB: Error:");
            log.error(e.getMessage());
            e.printStackTrace();
//            System.out.println("Error:");
        }
        
        // step 5: we close the document
        document.close();
        OutputStream out=response.getOutputStream();
        byte[] ficheropdf=salida.toByteArray();
        response.setHeader("Content-Length", ""+ficheropdf.length);
        out.write(ficheropdf);
        out.flush();
        //out.close();
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    private String checkUTF(String texto) {
        String resultado = texto;
        try {
            resultado = new String(texto.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return resultado;
    }
    
}
