<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil"%>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp"%>

<div id="modalRevocarRegistro" class="modal fade">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			
			<!-- Wizard steps -->
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
				<h4 class="modal-title" id="modalTitle">
					<spring:message code="revocar.modal.titulo" arguments="${registro.numeroRegistro}" />
				</h4>
			</div>

			<div class="modal-body modal_revocar_registro">
				<div class="alert alert-info alert-dismissable">
					<strong><spring:message code="revocar.modal.info" /></strong>
					<br><br>
					<spring:message code="revocar.modal.descripcion" />
				</div>

				<div class="col-xs-12">
					<div class="pull-right margin-top20 margeDret5">
						<a data-toggle="modal" role="button"
							href="#modalRevocarRegistroConfirmacion"
							class="btn btn-warning btn-sm btn-block"> <spring:message code="revocar.modal.accion" />
						</a>
					</div>
					<div class="pull-right margin-top20 margeDret5">
						<button type="button"
							onclick='confirmDistribuir("<spring:message code="regweb.confirmar.distribuir.instancia" htmlEscape="true"/>")'
							class="btn btn-success btn-sm btn-block">
							<spring:message code="regweb.distribuir" />
						</button>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
<c:import url="../registroEntrada/modalRevocarRegistroConfirmacion.jsp" />