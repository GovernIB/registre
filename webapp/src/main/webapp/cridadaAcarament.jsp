<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); 

   String urls[] = {"http://vd.caib.es/1305104722095-1851648-2648886061039214335","http://vd.caib.es/1305104722095-1851648-2648886061039214335","http://vd.caib.es/1305104722095-1851648-2648886061039214335","http://vd.caib.es/1305104722095-1851648-2648886061039214335","http://vd.caib.es/1305104722095-1851648-2648886061039214335"};
   
   StringBuffer result = new StringBuffer();
   if (urls.length > 0) {
       result.append(urls[0]);
       for (int i=1; i<urls.length; i++) {
           result.append(",");
           result.append(urls[i]);
       }
   }
   String purls = result.toString();

%>
            <jsp:forward page="pedirdatos.jsp" >
                <jsp:param name="localitzadorsDocs" value="<%=purls%>"/>
                <jsp:param name="emailRemitent" value="vherrera@dgtic.caib.es"/>
                <jsp:param name="nomRemitent" value="ciutadá de prova"/>
				<jsp:param name="pextracte" value="Solicitud curs seguretat"/>
            </jsp:forward>
