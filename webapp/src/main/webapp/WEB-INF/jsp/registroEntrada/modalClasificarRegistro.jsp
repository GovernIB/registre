<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="modalClasificarRegistro" class="modal fade">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">

            <button type="button" class="close margeDret5" data-dismiss="modal" aria-hidden="true"></button>
            
            <!-- Wizard steps -->
            <div class="modal-header">
                <h4 class="modal-title" id="modalTitle"><spring:message code="clasificar.modal.titulo" arguments="${registro.numeroRegistro}"/></h4>
            </div>

            <div class="modal-body formulario_container_loading">                
                <div>
                   <spring:message code="clasificar.modal.descripcion"/>
                </div>

                <div class="formulario_container col-xs-12">
                    <div class="wrapper"></div>
                </div>

				<div class="procedimiento_container col-xs-12" style="display: none;">
					<c:url value="/registroEntrada/${registro.id}/clasificar" var="urlRevertirRegistro" />
					<form:form modelAttribute="clasificacionDto" method="post" action="${urlRevertirRegistro}">
						<form:hidden path="registroId" />
						<div class="col-xs-12">
							<div class="col-xs-4 pull-left etiqueta_regweb margin-top20 control-label">
								<label for="codigoSia" rel="popupAbajo"><span class="text-danger">* </span><spring:message code="clasificar.modal.campo.procedimiento"/> </label>
							</div>
								
							<div class="col-xs-8 no-pad-right margin-top20">
								<form:select path="codigoSia" cssClass="chosen-select">
									<c:forEach items="${tramites}" var="tramite">
										<form:option value="${tramite.codigoSia}">${tramite.nombre}</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						
						<fieldset class="datos-notificacion">
							<legend><spring:message code="clasificar.modal.notificacion"/></legend>
	
							<div class="col-xs-12">
								<div class="col-xs-4 pull-left etiqueta_regweb margin-top20 control-label">
									<label for="caducidad" rel="popupAbajo"><spring:message code="clasificar.modal.campo.caducidad"/> </label>
								</div>
									
								<div class="col-xs-8 no-pad-right margin-top20">
									<div class="input-group date no-pad-right">
	                                     <form:input type="text" cssClass="form-control" path="caducidad" maxlength="19" placeholder="dd/mm/yyyy HH:mm:ss" name="caducidad"/>
	                                     <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
	                                </div>
	                                <p class="comentario">
	                                	<spring:message code="clasificar.modal.campo.caducidad.comentario"/>
	                                </p>
								</div>
							</div>
							
							<div class="col-xs-12">
								<div class="col-xs-4 pull-left etiqueta_regweb margin-top20 control-label">
									<label for="retardo" rel="popupAbajo"><spring:message code="clasificar.modal.campo.retardo"/> </label>
								</div>
									
								<div class="col-xs-8 no-pad-right margin-top20">
									<div class="input-group no-pad-right">
	                                     <form:input type="text" cssClass="form-control" path="retardo" name="retardo"/>
	                                </div>
	                                <p class="comentario">
	                                	<spring:message code="clasificar.modal.campo.retardo.comentario"/>
	                                </p>
								</div>
							</div>
						</fieldset>
						<div class="pull-right margin-top20"> 
		                     <button type="submit" class="btn btn-light btn-sm"><spring:message code="clasificar.modal.accion"/></button>
		                </div>
					</form:form>
					
					<div class="pull-right margin-top20 margeDret5"> 
		                    <button type="button" onclick='confirmDistribuir("<spring:message code="regweb.confirmar.distribuir.instancia" htmlEscape="true"/>")'
	                                                class="btn btn-success btn-sm btn-block"><spring:message code="regweb.distribuir"/></button>
					</div>
				</div>
			</div>
        </div>
    </div>
</div>