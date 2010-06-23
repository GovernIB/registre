<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html>
  <head>
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
    <!-- <script LANGUAGE="JavaScript">
      function confirmaProceso() {
          var respuesta = true;

          descCurtaOrganisme=document.gestionaOrganismes.descCurtaOrganisme.value;
          descLlargaOrganisme=document.gestionaOrganismes.descLlargaOrganisme.value;

          if ( (descCurtaOrganisme == null) || (descCurtaOrganisme.length == 0)) {
            alert("<fmt:message key='error_desc_curta_organisme'/>");
                respuesta = false;
            } else {   
            if ( (descLlargaOrganisme == null)|| (descLlargaOrganisme.length == 0)) {
              alert("<fmt:message key='error_desc_llarga_organisme'/>");
              respuesta = false;
            } else {   
              respuesta = true;
            }
          } 
          return respuesta;
      }
    </script> -->
    <script>
		  function addLoadEvent(func) {
    	  var oldonload = window.onload;
	      if (typeof window.onload != 'function') {
	        window.onload = func;
	      } else {
	        window.onload = function() {
	          if (oldonload) {
	            oldonload();
	          }
	          func();
	        }
	      }
	    }

		  function MarcaTot(evnt) {
			  /* Marcam tots els checkboxs de la columna */
			  var valor = false;

			  var elementClickat = null;
			  if (window.event) {
				  elementClickat = window.event.srcElement;
			  } else {
				  elementClickat = evnt.target;
			  }
			  if ( elementClickat.checked == true) {
				  valor=true;
			  } else {
				  valor=false;
			  }
			  //alert('Valor='+valor + ' elementClickat.name='+elementClickat.name+ ' elementClickat.value='+elementClickat.value);
			  var llista = document.getElementById("altaAutorUsu").getElementsByTagName(elementClickat.value);
			  var llistaElementsFormAU = document.getElementById("altaAutorUsu").elements;

			  for (var i=0; i<llistaElementsFormAU.length ;i++) {
				  //window.alert('Element '+i+' '+llistaElementsFormAU[i].name); 
				  if ( llistaElementsFormAU[i].name == elementClickat.value ){
					  llistaElementsFormAU[i].checked=valor;
				  }
			  }
		  }
		
		  function abreOrganismos() {
        miOrganismos=open("<c:url value='/admin/popup.do?accion=totsOrganismes'/>","Organismes","scrollbars,resizable,width=400,height=400");
        miOrganismos.focus();
      }
        
      function setOrganisme(codOrganisme) {
        var formulari = document.getElementById("cercaOrganisme");
      	document.getElementById("organismeGestionar").value=codOrganisme;
      	formulari.submit();
      }

      function validaGestioOrganisme() {
        var formulari = document.getElementById("gestionaOrganismes");
        if (document.getElementById("descCurtaOrganisme").value=="" || document.getElementById("descLlargaOrganisme").value=="" || document.getElementById("dataAlta").value=="" ) {
          alert("<fmt:message key='error_desc_data_alta_organisme'/>");
          return false;
        } else {
          return true;
        }
      }

      function abreError() {
      	miError=open("<c:url value='/error.jsp'/>","Error","scrollbars,resizable,width=500,height=380");
      	miError.focus();
      }

      function getdescMissatge() {
        return '<c:out escapeXml="false" value="${descMissatge}"/>';
      }

      function getmesInfoMissatge() {
        return '<c:out escapeXml="false" value="${mesInfoMissatge}"/>';
      }

      <c:if test="${not empty missatge}"> 
      addLoadEvent(function() {
        abreError();;
      });				
      </c:if>

    </script>
  </head>
  <body>
    <!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="gestio_organismes"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>

    <c:choose>
    <c:when test="${not hayOrganisme}">
	
    <br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>  
    <table align="center" >
      <tr>
        <td>
          <div  id="menuDocAdm" style="width:250px">
	          <form id="cercaOrganisme" name="cercaOrganisme" action="<c:url value='/admin/controller.do?accion=organismes'/>" method="post">
	          	<ul style="margin-right: 5px">
					      <li><p><fmt:message key="organisme_a_gestionar_q"/> <a href="javascript:abreOrganismos()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align=middle alt="<fmt:message key='cercar'/>"></a></p></li>
                <li>
                  <input onKeyPress="return goodchars(event,'0123456789')" style="width:35px;" type="text" name="organismeGestionar" id="organismeGestionar" size="4" maxlength="4" />
							    <input type="submit" value="<fmt:message key='cercar_alta'/>">
					      </li>
            	</ul>
						</form>
        	</div>
        </td>
			</tr>			
    </table>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
				
    </c:when>
    <c:otherwise>

	  <div class="RecuadreCentrat" style="width:500px">
	    <c:choose>
	    <c:when test="${existeOrganisme}">
		  <p><fmt:message key='organisme_a_gestionar'/> <br/> <b><fmt:message key="organisme_a_gestionar_2"><fmt:param value="${organismeGestionar}"/><fmt:param value="${organismes[2]}"/><fmt:param value="${organismes[3]}"/></fmt:message></b></p>
	    </c:when>
	    <c:otherwise>
	    <p><fmt:message key="organisme_no_existeix"><fmt:param value="${organismeGestionar}"/></fmt:message></p>
	    </c:otherwise>
	    </c:choose>

      <br/>
      <form name="gestionaOrganismes" id="gestionaOrganismes" action="<c:url value='/UtilAdmin'/>" method="post" onsubmit="return validaGestioOrganisme();">	
        <input type="hidden" name="accion" id="accion" value="<c:out escapeXml='false' value='${valorAccio}'/>"/>
        <input type="hidden" name="organismeGestionar" id="organismeGestionar" value="<c:out escapeXml='false' value='${organismeGestionar}'/>"/>
        <table>
        <c:choose>
        <c:when test="${existeOrganisme}">
        <tr>
          <th nowrap><fmt:message key='descripcio_curta_organisme'/></th>
          <th><fmt:message key='descripcio_llarga_organisme'/></th>
          <th><fmt:message key='data_alta'/><br/><fmt:message key='data_format'/></th>
          <th><fmt:message key='data_baixa'/><br/><fmt:message key='data_format'/></th>
        </tr>
        <c:if test="${historicOrganismesSize>5}">
        <tr><td colspan="4"><fmt:message key='historic_organisme'/></td></tr>
        </c:if>
        <c:forEach var="item" begin='0' end='${historicOrganismesSize-1}' step='5'>
      	<c:set var="codigo" value="${historicOrganismes[item]}" />
      	<c:set var="descCurtaOrganisme" value="${historicOrganismes[item+1]}" />
      	<c:set var="descLlargaOrganisme" value="${historicOrganismes[item+2]}" />
      	<c:set var="fecAlta" value="${historicOrganismes[item+3]}" />
      	<c:set var="fecBaixa" value="${historicOrganismes[item+4]}" />	
        <c:if test="${fecBaixa != '0' and not empty fecBaixa and item<5 }">
	        <!--El darrer històric d'organisme té data de baixa, aleshores no hi ha cap organisme actiu, donam l'opció de crear d'activar
	        l'organisme donant un nou nom i data d'alta! -->

  			<tr>
	        <td nowrap><input type="hidden" name="codiOrganisme" id="codiOrganisme" readonly="true" value="<c:out escapeXml='false' value='${organismeGestionar}'/>" />
	          <c:out escapeXml='false' value='${codigo}'/>&nbsp;-&nbsp;<input type="text" name="descCurtaOrganisme" id="descCurtaOrganisme" size="15" maxlength="15"  value="" style="width: 120px;"/></td>
  				<td><input type="text" name="descLlargaOrganisme" id="descLlargaOrganisme" size="40" maxlength="40"  value="" style="width: 250px;"/></td>
  				<td><input onKeyPress="return goodchars(event,'0123456789\/')" type="text" name="dataAlta" id="dataAlta" size="10" maxlength="10" value="" style="width: 70px;"/></td>
  				<td><input type="text" readonly="true" name="dataBaixa" id="dataBaixa" size="10" maxlength="10" value="" style="width: 70px;"/></td>
  			</tr>
  			<tr>
	        <td align="center" colspan="4"><input type="submit" value="<fmt:message key='dona_alta'/>"/></td>
        </tr>
  			<tr>
	        <td align="center" colspan="4">&nbsp;</td>
        </tr>
        </c:if>

        <tr>
        <c:choose>
        <c:when test="${(fecBaixa == '0' or empty fecBaixa)}">
          <td nowrap><c:out escapeXml='false' value='${codigo}'/>&nbsp;-&nbsp;<input type="text\" name="descCurtaOrganisme"  id="descCurtaOrganisme" size="15" maxlength="15"  value="<c:out escapeXml='false' value='${descCurtaOrganisme}'/>" style="width: 120px;"/></td>
  				<td><input type="text" name="descLlargaOrganisme"  id="descLlargaOrganisme" size="40" maxlength="40"  value="<c:out escapeXml='false' value='${descLlargaOrganisme}'/>" style="width: 250px;"/></td>
        </c:when>
        <c:otherwise>
	        <td><c:out escapeXml='false' value='${codigo}'/> - <c:out escapeXml='false' value='${descCurtaOrganisme}'/></td>
	        <td><c:out escapeXml='false' value='${descLlargaOrganisme}'/></td>
        </c:otherwise>
        </c:choose>


        <td>
          <c:out escapeXml='false' value='${fecAlta}'/>
          <input type="hidden" name="dataAlta" id="dataAlta" value="<c:out escapeXml='false' value='${fecAlta}'/>"/>
        </td>

        <c:choose>
        <c:when test="${(fecBaixa == '0' or empty fecBaixa)}">
          <td><input onKeyPress="return goodchars(event,'0123456789/')" type="text" name="dataBaixa" id="dataBaixa" size="10" maxlength="10"  value="" style="width: 70px;"/></td>
        </c:when>
        <c:otherwise>
          <td><c:out escapeXml='false' value='${fecBaixa}'/></td>
        </c:otherwise>
        </c:choose>

        </tr>

        <c:if test="${(fecBaixa == '0' or empty fecBaixa) and item<5 }">
	      <tr>
	        <td align="center" colspan="5"><input type="submit" value="<fmt:message key='actualitza'/>"/></td>
	      </tr>
        </c:if>
        </c:forEach>

      </c:when>
      <c:otherwise>

      <tr>
        <th><fmt:message key='codi_organisme'/></th>
        <th><fmt:message key='descripcio_curta_organisme'/></th>
        <th><fmt:message key='descripcio_llarga_organisme'/></th>
        <th><fmt:message key='data_alta_2'/></th>
      </tr>
      <tr>
  		  <td></td>
  		  <td></td>
  		  <td></td>
  		  <td></td>
  	  </tr>  			
      <tr>
      	<td><input type="text" name="codiOrganisme" size="4" maxlength="4" readonly="true" id="codiOrganisme" value="<c:out escapeXml='false' value='${organismeGestionar}'/>" style="width:70px;"/></td>
      	<td><input type="text" name="descCurtaOrganisme" size="15" maxlength="15" id="descCurtaOrganisme" value="" style="width:200px;"/></td>
      	<td><input type="text" name="descLlargaOrganisme" size="40" maxlength="40" id="descLlargaOrganisme" value="" style="width:200px;"/></td>
      	<td><input onKeyPress="return goodchars(event,'0123456789/')" type="text" name="dataAlta" id="dataAlta" size="10" maxlength="10" value="" style="width:70px;"/></td>
      </tr>
      <tr>
      	<td align="center" colspan="4"><input type="submit" value="<fmt:message key='dona_alta'/>"/></td>
      </tr>

      </c:otherwise>
      </c:choose>

    </table>	
  </form>
		
</div>
</c:otherwise>
</c:choose>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<script type="text/javascript">
	 var elFocus = document.getElementById('<c:out escapeXml='false' value='${elementFocus}'/>');
	 if (elFocus!=null) elFocus.focus();
</script>

	</body>
</html>   