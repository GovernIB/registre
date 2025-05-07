<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-12">

    <div class="">

        <table id="notificaciones" class="table table-bordered table-hover table-striped">
            <thead>
            <tr>
                <th><spring:message code="registro.notificacion.concepto"/></th>
                <th><spring:message code="registro.notificacion.descripcion"/></th>
                <th><spring:message code="registro.notificacion.fecha.creacion"/></th>
                <th><spring:message code="registro.notificacion.fecha.envio"/></th>
                <th><spring:message code="registro.notificacion.fecha.finalizacion"/></th>
                <th><spring:message code="registro.notificacion.estado"/></th>
                <th><spring:message code="registro.notificacion.estado.notifica"/></th>
                <th class="center"><spring:message code="regweb.acciones"/></th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="notificacion" items="${notificaciones}">
                <tr>
                    <td>${notificacion.concepto}</td>
                    <td>${notificacion.descripcion}</td>
                    <td><fmt:formatDate value="${notificacion.fechaCreacion}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${notificacion.fechaEnvio}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${notificacion.fechaFinalizacion}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                    <td>
                    	<spring:message code="remesa.notificacio.estat.${notificacion.estado}"/>
                    	<c:if test="${not empty notificacion.fechaEstado}">
	                    	<br>
	                    	(<fmt:formatDate value="${notificacion.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>)
					    </c:if>
                    </td>
                    <td>
                    	<c:if test="${not empty notificacion.estadoNotifica}">
 							<spring:message code="remesa.enviament.estat.${notificacion.estadoNotifica}"/>
                    	</c:if>	
                    </td>
                    <td>
                    	<div class="btn-group">
	                    	<button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown">
						        <spring:message code="regweb.acciones"/> <span class="caret"></span>
						    </button>
	                    	<ul class="dropdown-menu">
	                    		<c:if test="${notificacion.estado != 'PROCESSADA'}">
					        		<li><a href="<c:url value="/registroEntrada/actualizarEstado/${notificacion.id}"/>"><i class="fa fa-refresh"></i> <spring:message code="registro.notificacion.accion.actualizar.estado"/></a></li>
				            	</c:if>
				            	<c:if test="${notificacion.estado == 'FINALITZADA' || notificacion.estado == 'PROCESSADA'}">
				            		<li><a href="<c:url value="/registroEntrada/descargarCertificacion/${notificacion.id}"/>"><i class="fa fa-download"></i> <spring:message code="registro.notificacion.accion.certificacion"/></a></li>
								</c:if>
				        	</ul>
			        	</div>
                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>

</div>


