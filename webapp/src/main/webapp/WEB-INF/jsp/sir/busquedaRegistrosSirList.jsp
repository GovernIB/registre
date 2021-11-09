<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-search"></i> <spring:message code="registroSir.buscador.recibidos.importacion"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div id="mensajes"></div>

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroSir.buscador.recibidos.importacion"/></strong> </h3>
                    </div>
                    <form:form id="rangoFechasBusqueda" modelAttribute="rangoFechasBusqueda" method="post" cssClass="form-horizontal">
                        <div class="panel-body">
                        
							<div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fechaInicio.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechainicio"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaInicioImportacion">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaInicioImportacion" type="text" cssClass="form-control"  maxlength="19" autocomplete="off" placeholder="dd/mm/yyyy HH:mm:ss" name="fechaInicioImportacion"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <form:errors path="fechaInicioImportacion" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fechaFin.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechafin"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaFinImportacion">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaFinImportacion" type="text" cssClass="form-control"  maxlength="19" autocomplete="off" placeholder="dd/mm/yyyy HH:mm:ss" name="fechaFinImportacion"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.importar"/></button>
                            </div>
                    </form:form>
                    <c:if test="${not empty resultadoImportacion}">
	                    <div id="resultado">
	                    	<label>Resultado última importación:</label>
	                    	<p>${resultadoImportacion}</p>
	                    </div>
                    </c:if>
                </div>
            </div>
        </div>
        <div id="modalImportacioRegistrosSir" class="modal fade">
		    <div class="modal-dialog modal-lg">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
		                <h3>
		                    <spring:message code="registroSir.buscador.recibidos.importacion"/>
		                </h3>
		            </div>
		            <div class="modal-body">
		            	<div class="progress">
						  <div id="bar" class="progress-bar progress-bar-success active" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width:0%">0%</div>
						</div>
						<div id="actualitzacionInfo" class="info">
							<span id="bcursor" class="blinking-cursor">|</span>
						</div>
			            <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true"><spring:message code="regweb.cerrar"/></button>
		            </div>
		            <div class="modal-footer">
		            </div>
		        </div>
		    </div>
		</div>
        
    </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>
<script type="text/javascript" src="<c:url value="/js/sir.js"/>"></script>
<script type="text/javascript">
    var urlReenviarMensaje = '<c:url value="/sir/mensajeControl/reenviar"/>';
    var urlProgreso = '<c:url value="/sir/recuperarRegistrosSirRecibidos/form/progres"/>';
    var tradsMensajesControl = [];
    tradsMensajesControl['mensajeControl.reenviado.ok'] = "<spring:message code='mensajeControl.reenviado.ok' javaScriptEscape='true' />";
    tradsMensajesControl['mensajeControl.reenviado.error'] = "<spring:message code='mensajeControl.reenviado.error' javaScriptEscape='true' />";
</script>


</body>
</html>