<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- MODAL GUADAR REPRO -->
<div id="modalNewPlantilla" class="modal fade" >
    <div class="modal-dialog modal-medio">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarPlantilla()">x</button>
                <h3 class="modal-title" id="myModalLabel"><spring:message code="plantilla.nuevo"/></h3>
            </div>
            <div class="modal-body">
                <form id="plantillaForm" class="form-horizontal" action="${pageContext.request.contextPath}/plantilla/new"
                      method="post">
                    <input type="hidden" name="tipoRegistro" id="tipoRegistro" value=""/>
                    <div class="form-group col-xs-12">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="nombrePlantilla"><spring:message code="plantilla.nombre"/></label>
                        </div>
                        <div class="col-xs-8" id="nomPlantilla">
                            <input id="nombrePlantilla" class="form-control"/><span class="errors"></span>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                </form>
            </div>
            <div class="modal-footer">
                <input type="button" onclick="return validaFormulario(this)" title="<spring:message code="regweb.guardar"/>" value="<spring:message code="regweb.guardar"/>" class="btn btn-warning btn-sm">
            </div>
        </div>
    </div>
</div>

<!-- MODAL CARREGAR REPRO -->
<div id="modalSelectPlantilla" class="modal fade" >
    <div class="modal-dialog modal-medio">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h3 class="modal-title" id="myModalLabel2"><spring:message code="plantilla.select"/></h3>
            </div>
            <div class="modal-body">
                 <div class="col-xs-12 centrat" id="plantillaList"></div>
            </div>
        </div>
    </div>
</div>