<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="modalRevocarRegistroConfirmacion" class="modal fade">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">

            <!-- Wizard steps -->
            <div class="modal-header">
            	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h4 class="modal-title" id="modalTitle"><spring:message code="revocar.modal.titulo" arguments="${registro.numeroRegistro}"/></h4>
            </div>

            <div class="modal-body modal_revocar_registro_confirmacion">            
				<div class="procedimiento_container col-xs-12">
					<c:url value="/registroEntrada/${registro.id}/revocar" var="urlRevertirRegistro" />
					<form:form modelAttribute="revocacionDto" method="post" action="${urlRevertirRegistro}">
						<form:hidden path="registroId" />
						
						<fieldset class="datos-notificacion">
							<legend><spring:message code="revocar.modal.datos"/></legend>
							<div class="col-xs-12">
								<div class="col-xs-4 pull-left etiqueta_regweb margin-top20 control-label">
									<label for="codigoSia" rel="popupAbajo"><span class="text-danger">* </span><spring:message code="revocar.modal.campo.procedimiento"/> </label>
								</div>
									
								<div class="col-xs-8 no-pad-right margin-top20">
									<form:select path="codigoSia" cssClass="chosen-select">
										<c:forEach items="${tramites}" var="tramite">
											<form:option value="${tramite.codigoSia}">${tramite.nombre}</form:option>
										</c:forEach>
									</form:select>
								</div>
							</div>
						</fieldset>
						<fieldset class="datos-notificacion">
							<legend><spring:message code="revocar.modal.datos.notificacion"/></legend>
	
							<div class="col-xs-12">
								<div class="col-xs-4 pull-left etiqueta_regweb margin-top20 control-label">
									<label for="caducidad" rel="popupAbajo"><spring:message code="revocar.modal.campo.caducidad"/> </label>
								</div>
									
								<div class="col-xs-8 no-pad-right margin-top20">
									<div class="input-group date no-pad-right">
	                                     <form:input type="text" cssClass="form-control" path="caducidad" maxlength="19" placeholder="dd/mm/yyyy HH:mm:ss" name="caducidad"/>
	                                     <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
	                                </div>
	                                <p class="comentario">
	                                	<spring:message code="revocar.modal.campo.caducidad.comentario"/>
	                                </p>
								</div>
							</div>
							
							<div class="col-xs-12">
								<div class="col-xs-4 pull-left etiqueta_regweb margin-top20 control-label">
									<label for="retardo" rel="popupAbajo"><spring:message code="revocar.modal.campo.retardo"/> </label>
								</div>
									
								<div class="col-xs-8 no-pad-right margin-top20">
									<div class="input-group no-pad-right">
	                                     <form:input type="text" cssClass="form-control" path="retardo" name="retardo"/>
	                                </div>
	                                <p class="comentario">
	                                	<spring:message code="revocar.modal.campo.retardo.comentario"/>
	                                </p>
								</div>
							</div>
						</fieldset>
						<div class="pull-right margin-top20"> 
		                     <button type="submit" id="btn-submit" class="btn btn-warning btn-sm"><spring:message code="revocar.modal.accion"/></button>
		                </div>
					</form:form>
					
					<div id="loadingOverlay" class="loading-overlay hidden">
					  <img src="../../img/loader.gif" alt=""/>
					</div>
				</div>
			</div>
        </div>
    </div>
</div>
<script>
$(document).ready(function() {
	$('#revocacionDto').on('submit', function() {
		const submitBtn = document.getElementById('btn-submit');
	    const overlay = document.getElementById('loadingOverlay');

	    submitBtn.disabled = true;
	    overlay.classList.remove('hidden');
	});
});
</script>
