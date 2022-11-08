<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="registroEntrada.buscador"/></title>
    <c:import url="../modulos/imports.jsp"/>
    <script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
</head>

<body>

	<c:import url="../modulos/menu.jsp"/>
	
	<div class="row-fluid container main">
	
	    <div class="well well-white">
	
	        <div class="row">
	            <div class="col-xs-12">
	                <ol class="breadcrumb">
	                    <c:import url="../modulos/migadepan.jsp">
	                        <c:param name="avisos" value="false"/> <%--Importamos el menú de avisos--%>
	                    </c:import>
	                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="anexos.buscador"/></strong></li>
	                </ol>
	            </div>
	        </div><!-- /.row -->
		
			<c:import url="../modulos/mensajes.jsp"/>
		
			<div class="row">
	               <div class="col-xs-12">
	                   <div class="tab-content">
	                       <div class="panel-body">
	                           <div class="row">
	                               <div class="form-group col-xs-12">
	                                   <form:form modelAttribute="anexoBusqueda" method="post" cssClass="form-horizontal">
	                                       <form:hidden path="pageNumber"/>
	                                   </form:form>
	                               </div>
	                           </div>
	                           
	                        	<c:if test="${empty paginacion.listado}">
					        		<div class="alert alert-grey alert-dismissable">
					            		<spring:message code="regweb.listado.vacio"/> <strong><spring:message code="regweb.elemento"/></strong>
									</div>
								</c:if>
							
								<c:if test="${not empty paginacion.listado}">
									
										 <div class="alert-grey">
		                                    <c:if test="${paginacion.totalResults == 1}">
		                                        <spring:message code="regweb.resultado"/>
		                                        <strong>${paginacion.totalResults}</strong> <spring:message code="regweb.elemento"/>
		                                    </c:if>
		                                    <c:if test="${paginacion.totalResults > 1}">
		                                        <spring:message code="regweb.resultados"/>
		                                        <strong>${paginacion.totalResults}</strong> <spring:message code="regweb.elementos"/>
		                                    </c:if>
		
		                                    <%--Select de "Ir a página"--%>
		                                    <c:import url="../modulos/paginas.jsp"/>
		                                </div>
										<div class="table-responsive">
		                                    <table class="table table-bordered table-hover table-striped">
		                                        <thead>
		                                            <tr>
		                                            	<th><spring:message code="anexos.tiporegistro"/></th>
		                                                <th><spring:message code="anexos.numeroregistro"/></th>
		                                                <th><spring:message code="anexos.id"/></th>
		                                                <th><spring:message code="anexos.estado"/></th>
		                                                <th><spring:message code="anexos.registodetalle"/></th>
		                                                <th></th>
		                                            </tr>
		                                        </thead>
		
		                                        <tbody>
		                                        	<c:forEach items="${paginacion.listado}" var="resultado">
		                                        		<tr>
		                                            	   <td class="center"><spring:message code="anexos.tiporegistro.0"/></td>
		                                            	   <td class="center">${resultado[0]}</td>
		                                            	   <td class="center">${resultado[1].id}</td>
		                                            	   <td class="center"><img src="<c:url value="/img/712.GIF"/>" width="15" height="15" title="<spring:message code="anexos.estado.pendiente"/>"/></td>
		                                            	   <td class="center"><a href="<c:url value="/adminEntidad/registroEntrada/${resultado[2]}/detalle"/>">${resultado[2]}</a></td>
		                                            	   <td class="center"><a class="btn btn-success btn-sm" onclick='confirm("<c:url value="/adminEntidad/anexosfirma/${resultado[1].id}"/>","<spring:message code="anexos.verificar.confirm" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="anexos.verificar"/>"><span class="fa fa-refresh"></span></a>
		                                            	</tr>
													</c:forEach>
		                                        </tbody>
											</table>
											<!-- Paginacion -->
											<c:import url="../modulos/paginacionBusqueda.jsp">
									        	<c:param name="entidad" value="anexo"/>
											</c:import>              
										</div>
								</c:if>               
							</div>
					</div>
				</div>
			</div>
	        <c:import url="../modulos/mensajes.jsp"/>
	    </div>
	</div> <!-- /container -->
	<c:import url="../modulos/pie.jsp"/>
   </body>
</html>