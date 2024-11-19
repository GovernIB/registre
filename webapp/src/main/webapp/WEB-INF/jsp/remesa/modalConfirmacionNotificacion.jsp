<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- Formulario de busqueda compartido. Se usa tanto para buscar organismos como oficinas, en función del parámetro
que se le indica -->

<div id="modalConfirmacionNotificacion" class="modal fade">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">

            <button type="button" class="close margeDret5" data-dismiss="modal" aria-hidden="true"></button>
            
            <!-- Wizard steps -->
            <div class="modal-header">
                <h4 class="modal-title" id="modalTitle"><spring:message code="remesa.modal.paso.1"/></h4>
            </div>

            <div class="modal-body">
            	<input type="hidden" id="identificadorNotificacion">
            	
            	<div id="loading" style="display: none; text-align: center;">
            		<div>
                    	<img src="../img/loader.gif" alt="<spring:message code="remesa.modal.cargando"/>" />
                    </div>
                    <div>
                    	<span><spring:message code="remesa.modal.cargando"/></span>
                    </div>
                </div>
                
                <div id="remesa-btn-lectura" class="wizard-step">
                    <a type="button" class="btn btn-primary" id="btnLeerNotificacion"><spring:message code="remesa.modal.boton.leer"/></a>
                </div>

                <div id="remesa-resumen" class="wizard-step">
                    <div id="remesa-detalle" class="panel panel-default"></div>
					<div id="remesa-carousel-container" class="panel panel-default">
						<div class="panel-heading"><spring:message code="remesa.modal.documentos"/> <span class="badge small"></span></div>
						<div class="panel-body">
							<div class="remesa-iframe-wrapper"></div>
						</div>
						<div class="remesa-nav-buttons">
							<a id="prev"><span class="fa fa-chevron-left"></span></a>
							<a id="next"><span class="fa fa-chevron-right"></span></a>
						</div>
					</div>
                </div>
                    
                <div id="remesa-btn-registrar">         
                  	<a class="btn btn-info" id="btnRegistrarNotificacion" role="button"><span class="fa fa-plus"></span> <spring:message code="remesa.modal.boton.registrar"/></a>
                </div>

                <div id="errorNotificacion" style="color: red; display: none;"></div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="regweb.cerrar"/></button>
            </div>
            
        </div>
    </div>
</div>