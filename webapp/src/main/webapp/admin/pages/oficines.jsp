<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
  <head>
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
    <script type="text/javascript">
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

		  function abreOficinas() {
        miOficinas=open("<c:url value='/admin/popup.do?accion=totesOficines'/>","Oficines","scrollbars,resizable,width=400,height=400");
        miOficinas.focus();
      }

      function setOficina(codOficina) {
        var formulari = document.getElementById("cercaOficina");
        document.getElementById("oficinaGestionar").value=codOficina;
        formulari.submit();
      }

      function validaGestioOficina() {
        var formulari = document.getElementById("gestionaOficines");
        if (document.getElementById("descOficina").value=="" || document.getElementById("dataAlta").value=="" ) {
          alert("<fmt:message key='error_desc_data_alta_oficina'/>");
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
			<li><fmt:message key="gestio_oficines"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>

    <c:choose>
    <c:when test="${not hayOficina}">
	
    <br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>  
    <table align="center" >
      <tr>
        <td>
          <div  id="menuDocAdm" style="width:250px">
            <form id="cercaOficina" name="cercaOficina" action="<c:url value='/admin/controller.do?accion=oficines'/>" method="post">
            <ul style="margin-right: 5px">
			        <li><p><fmt:message key="oficina_a_gestionar_q"/> <a href="javascript:abreOficinas()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align=middle alt="<fmt:message key='cercar'/>"></a></p></li>
            	<li>
            	  <input  onKeyPress="return goodchars(event,'0123456789')" style="width:20px;" type="text" name="oficinaGestionar" id="oficinaGestionar" size="2" maxlength="2"/>
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

	  <div class="RecuadreCentrat" style="width:500px;">
	    <c:choose>
	    <c:when test="${existeOficina}">
		  <p><fmt:message key="oficina_a_gestionar"/> <b><fmt:message key="oficina_a_gestionar_2"><fmt:param value="${oficines[0]}"/><fmt:param value="${oficines[1]}"/><fmt:param value="${oficines[2]}"/></fmt:message></b></p>
		  </c:when>
		  <c:otherwise>
		  <p><fmt:message key="oficina_no_existeix"><fmt:param value="${oficinaGestionar}"/></fmt:message></p>
		  </c:otherwise>
		  </c:choose>

	    <br/>
      <form name="gestionaOficines" id="gestionaOficines" action="<c:url value='/UtilAdmin'/>" method="post" onSubmit="return validaGestioOficina();">	
        <input type="hidden" name="accion" id="accion" value="<c:out escapeXml='false' value='${valorAccio}'/>"/>
        <input type="hidden" name="oficinaGestionar" id="oficinaGestionar" value="<c:out escapeXml='false' value='${oficinaGestionar}'/>"/>
        <table>
        <c:choose>
        <c:when test="${existeOficina}">
          <tr>
		        <th><fmt:message key='oficina'/></th>
		        <th><fmt:message key='data_alta'/><br/><fmt:message key='data_format'/></th>
		        <th><fmt:message key='data_baixa'/><br/><fmt:message key='data_format'/></th>
	        </tr>
          <c:if test="${historicOficinesSize>4}">
	        <tr><td colspan="3"><fmt:message key='historic_oficina'/></td></tr>
          </c:if>
          <c:forEach var="item" begin='0' end='${historicOficinesSize-1}' step='4'>
        	<c:set var="codigo" value="${historicOficines[item]}" />
        	<c:set var="descripcion" value="${historicOficines[item+1]}" />
        	<c:set var="fecAlta" value="${historicOficines[item+2]}" />
        	<c:set var="fecBaixa" value="${historicOficines[item+3]}" />	
	        <c:if test="${fecBaixa != '0' and not empty fecBaixa and item<4 }">
		        <!--El darrer històric d'oficina té data de baixa, aleshores no hi ha cap oficina activa, donam l'opció de crear d'activar
		        l'oficina donant un nou nom i data d'alta! -->
          <tr>
	          <td><input type="hidden" name="codiOficina" id="codiOficina" readonly="true" value="<c:out escapeXml='false' value='${oficinaGestionar}'/>" />
		          <c:out escapeXml='false' value='${oficinaGestionar}'/> - <input type="text" name="descOficina" id="descOficina" size="20" maxlength="20"  value="" style="width: 200px;"/></td>
	          <td><input onKeyPress="return goodchars(event,'0123456789/')"  type="text" name="dataAlta" id="dataAlta" size="10" maxlength="10" value="" style="width: 70px;"/></td>
	          <td><input type="text" readonly="true" name="dataBaixa" id="dataBaixa" size="10" maxlength="10" value="" style="width: 70px;"/></td>
	        </tr>
          <tr>
	          <td align="center" colspan="3"><input type="submit" value="<fmt:message key='dona_alta'/>"/></td>
          </tr>
          <tr>
	          <td align="center" colspan="3">&nbsp;</td>
          </tr>
	        </c:if>

	        <tr>
          <c:choose>
	        <c:when test="${(fecBaixa == '0' or empty fecBaixa)}">
            <td><c:out escapeXml='false' value='${codigo}'/> - <input type="text" readonly="true" name="descOficina"  id="descOficina" size="20" maxlength="20"  value="<c:out escapeXml='false' value='${descripcion}'/>" style="width: 200px;"/></td>
	        </c:when>
	        <c:otherwise>
		        <td><c:out escapeXml='false' value='${codigo}'/> - <c:out escapeXml='false' value='${descripcion}'/></td>
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

	        <c:if test="${(fecBaixa == '0' or empty fecBaixa) and item<4 }">
		      <tr>
		        <td align="center" colspan="4"><input type="submit" value="<fmt:message key='actualitza'/>"/></td>
		      </tr>
	        </c:if>
          </c:forEach>
          
	        </c:when>
	        <c:otherwise>
          <tr>
          	<th><fmt:message key='codi_oficina'/></th>
          	<th><fmt:message key='descripcio_oficina'/></th>
          	<th><fmt:message key='data_alta_2'/></th>
      	  </tr>
          <tr>
      		  <td></td>
      		  <td></td>
      		  <td></td>
      	  </tr>  			
          <tr>
      	    <td><input type="text" name="codiOficina" id="codiOficina"  size="2" maxlength="2" readonly="true" value="<c:out escapeXml='false' value='${oficinaGestionar}'/>" style="width: 30px;"/></td>
      	    <td><input type="text" name="descOficina" id="descOficina" size="20" maxlength="20"  value="" style="width: 200px;"/></td>
      	    <td><input onKeyPress="return goodchars(event,'0123456789/')" type="text" name="dataAlta" id="dataAlta" size="10" maxlength="10"  value="" style="width: 70px;"/></td>
      	  </tr>		
          <tr>
      	    <td align="center" colspan="3"><input type="submit" value="<fmt:message key='dona_alta'/>"/></td>
          </tr>
  	
  	      </c:otherwise>
  	      </c:choose>

        </table>	
      </form>
    </div>
		<p>&nbsp;</p>
		<p>&nbsp;</p> 
		<p>&nbsp;</p>

    </c:otherwise>
    </c:choose>

    <script type="text/javascript">
    	 var elFocus = document.getElementById('<c:out escapeXml="false" value="${elementFocus}"/>');
    	 if (elFocus!=null) elFocus.focus();
    </script>

	</body>
</html>   