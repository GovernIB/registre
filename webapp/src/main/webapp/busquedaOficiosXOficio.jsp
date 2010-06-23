<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    String usuario=request.getRemoteUser();

    ValoresFacade valores = ValoresFacadeUtil.getHome().create();

    Vector oficinasfisicas = valores.buscarOficinasFisicas(usuario, "CE");

%>
<%! 
    void escribeSelect(javax.servlet.jsp.JspWriter out, Vector valores, String referencia) throws java.io.IOException {
        
        for (int i=0;i<valores.size();i=i+2){
                String codigo=valores.get(i).toString();
                String descripcion=valores.get(i+1).toString();
                out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) ? "selected" : "")+">");
                out.write(codigo+"-"+descripcion);
                out.write("</option>\n");
        }
    }

void escribeSelect2(javax.servlet.jsp.JspWriter out, String tipo, Vector valores, String referencia, String referencia2) throws java.io.IOException {

    for (int i=0;i<valores.size();i=i+3){
    	String ofi=valores.get(i).toString();
    	String codigo=valores.get(i+1).toString();
        String descripcion=valores.get(i+2).toString();
        out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) && ofi.equals(referencia2) ? "selected" : "")+">");
        if (tipo.equals("N")) {
            out.write(descripcion);
        } else {
            out.write(codigo+" - "+descripcion);
        }
        out.write("</option>\n");
    }
}

%>

<html>
    <head><title><fmt:message key='oficis_de_remisio'/></title>
        <link type="text/CSS" rel="stylesheet" HREF="estilos.css">
        
        
        <script language="javascript" src="jscripts/TAO.js"></script>
        
        <script>
        var oficinasfisicasarray = new Array();
        <% for (int ii=0; oficinasfisicas!=null && ii<oficinasfisicas.size(); ii=ii+3) { %>
        	oficinasfisicasarray.push([<%= oficinasfisicas.get(ii).toString() %>, <%= oficinasfisicas.get(ii+1).toString() %>,"<%= oficinasfisicas.get(ii+2).toString() %>"]);
        <% } %>

        
        function refrescaFisica(){
           	fis=document.getElementById('oficinafisica').value;
           	oficina=document.getElementById('oficina').value;
           	options=document.getElementById('oficinafisica').options;
           	ofifis=document.getElementById('oficinafisica');
	   	
        	var seleccionado = ofifis.selectedIndex;

           	while(ofifis.hasChildNodes()){
           		ofifis.removeChild(ofifis.childNodes[0]);
           	}
   			opcion= new Option('00 - <fmt:message key='registro.todas_autorizadas'/>','');
   			opcion.value='';
   			opcion.selected=false;
   			options[options.length]=opcion;	
        	
            for(i=0; i<oficinasfisicasarray.length; i++) {
           		of = oficinasfisicasarray[i];
           		if(of[0] == oficina){
           			opcion= new Option(of[2],of[1]);
           			opcion.value=of[1];
           			opcion.selected=false;
           			if (fis==of[1]) seleccionado = options.length;
           			options[options.length]=opcion;	
           		}
           	}
           	document.getElementById('oficinafisica').selectedIndex=0;
           }
        </script>
    </head>

    <body bgcolor="#FFFFFF">

    
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="busquedaOficiosIndex.jsp"><fmt:message key='consulta_oficis'/></a></li>
		<li><fmt:message key='consulta_per_numero_dofici_i_any'/></li>
		</ul>
		<!-- Fi Molla pa-->
    <p>&nbsp;</p>


        <center><font class="titulo"><fmt:message key='consulta_oficis'/></font></center>&nbsp;<p>
   
        <form id="busquedaForm" name="busquedaForm" action="listadoOficios.jsp" method="post" onsubmit="return confirmaProceso()">
        <table align="center" class="recuadroEntradas">
        <tr>
            <td style="border:0">
            &nbsp;
            </td>
            <td style="border:0">
            &nbsp;
            </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;&nbsp;
                <fmt:message key='oficina'/>..............:
                &nbsp;&nbsp;
            </td>
            <td style="border:0">
                <select name="oficina" id="oficina"  onchange="refrescaFisica()">
                    <% escribeSelect(out, valores.buscarOficinas(usuario,"CE"), "");%>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;&nbsp;
                <fmt:message key="oficina_fisica"/>.....:
                &nbsp;&nbsp;
            </td>
            <td style="border:0">
                <select name="oficinafisica" id="oficinafisica">
                    <option value="">00 - <fmt:message key='registro.todas_autorizadas'/> </option>
                    <%  escribeSelect2(out, "N", valores.buscarOficinasFisicas(usuario, "CE"), "", ""); %>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0">
            &nbsp;<br>
            </td>
            <td style="border:0">
            &nbsp;<br>
            </td>
        </tr>
        <tr>
        <td style="border:0">
            &nbsp;&nbsp;
            <fmt:message key='any'/>..................:
            &nbsp;&nbsp;
        </td>
        <td style="border:0">
            <input type="text" name="any" size="4" onKeyPress="return goodchars(event,'0123456789')">
            &nbsp;&nbsp;
        </td>
        </tr>
        <tr>
        <td style="border:0">
            &nbsp;&nbsp;
            <fmt:message key='num_ofici'/>.........:
            &nbsp;&nbsp;
        </td>
        <td style="border:0">
            <input type="text" name="numero" size="6" onKeyPress="return goodchars(event,'0123456789')">
            &nbsp;&nbsp;
        </td>
        </tr>
        <tr>
            <td style="border:0">
            &nbsp;<br>
            </td>
            <td style="border:0">
            &nbsp;<br>
            </td>
        </tr>
        <tr>
        <td style="border:0" colspan="2">
            <p align="center">
            <input type=submit value="<fmt:message key='cercar'/>">
            </P>
        </td>
        </tr>
        </table>
        </form>
 <!--        <div align="center">
            [&nbsp;<a href="busquedaEntradasIndex.jsp"><fmt:message key='tornar'/></a>&nbsp;]
        </div> -->
        <script language="JavaScript">
			document.getElementById('busquedaForm').oficina.focus();
	          function confirmaProceso() {
                ano=trim(document.getElementById('busquedaForm').any.value);
                
               if (ano=="" || ano<=1900 || ano>3000 || ano.length<4) {
                  alert("Heu d'introduir l'any ");
                  return false;
                }
                else {
                  return true;
                }
            }
		</script>
		<script type="text/javascript">
		refrescaFisica();
		</script>
		
		<br/>
        <p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
        
                 
    </body>
</html>
