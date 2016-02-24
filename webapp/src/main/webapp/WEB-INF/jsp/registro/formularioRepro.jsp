<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- MODAL GUADAR REPRO -->
<div id="modalNewRepro" class="modal fade bs-example-modal-lg" >
    <div class="modal-dialog modal-medio">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarRepro()">x</button>
                <h3 class="modal-title" id="myModalLabel"><spring:message code="repro.nuevo"/></h3>
            </div>
            <div class="modal-body">
                <form id="reproForm" class="form-horizontal" action="${pageContext.request.contextPath}/repro/new"
                      method="post">
                    <input type="hidden" name="tipoRegistro" id="tipoRegistro" value=""/>
                    <div class="form-group col-xs-12">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="nombreRepro"><spring:message code="repro.nombre"/></label>
                        </div>
                        <div class="col-xs-8" id="nomRepro">
                            <input id="nombreRepro" class="form-control"/><span class="errors"></span>
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
<div id="modalSelectRepro" class="modal fade bs-example-modal-lg" >
    <div class="modal-dialog modal-medio">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h3 class="modal-title" id="myModalLabel2"><spring:message code="repro.select"/></h3>
            </div>
            <div class="modal-body">
                 <div class="col-xs-12 centrat" id="reproList"></div>
            </div>
        </div>
    </div>
</div>