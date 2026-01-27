<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="infoIntegracion" class="modal fade">
    <div class="modal-dialog modal-lg">

        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarIntegracion()">x
                </button>
                <h4 class="modal-title"><spring:message code="integracion.informacion"/></h4>
            </div>
            <div class="modal-body">

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.fecha"/>:</label>
                    </div>
                    <div class="col-xs-10" id="fecha"></div>
                </div>

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.tipo"/>:</label>
                    </div>
                    <div class="col-xs-10" id="tipo"></div>
                </div>

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.descripcion"/>:</label>
                    </div>
                    <div class="col-xs-10" id="descripcionIntegracion"></div>
                </div>

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.tiempo"/>:</label>
                    </div>
                    <div class="col-xs-10" id="tiempo"></div>
                </div>

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.estado"/>:</label>
                    </div>
                    <div class="col-xs-10" id="estadoIntegracion"></div>
                </div>

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.peticion"/>:</label>
                    </div>
                    <div class="col-xs-10" id="peticion"></div>
                </div>

                <div class="form-group col-xs-12" id="errorBox">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.error"/>:</label>
                    </div>
                    <div class="col-xs-10" id="error"></div>
                </div>

                <div class="form-group col-xs-12" id="excepcionBox">
                    <div class="col-xs-12 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="integracion.excepcion"/>:</label>
                    </div>

                    <pre id="excepcion" class=".pre-scrollable"></pre>
                </div>

                <div class="clearfix"></div>

            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarIntegracion()">
                    <spring:message code="regweb.cerrar"/>
                </button>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

<script type="text/javascript">

    var urlobtenerIntegracion = '<c:url value="/rest/obtenerIntegracion"/>';

    var tradsIntegracion = [];
    tradsIntegracion['integracion.tipo.0'] = "<spring:message code='integracion.tipo.0' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.1'] = "<spring:message code='integracion.tipo.1' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.2'] = "<spring:message code='integracion.tipo.2' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.3'] = "<spring:message code='integracion.tipo.3' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.4'] = "<spring:message code='integracion.tipo.4' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.5'] = "<spring:message code='integracion.tipo.5' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.6'] = "<spring:message code='integracion.tipo.6' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.7'] = "<spring:message code='integracion.tipo.7' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.8'] = "<spring:message code='integracion.tipo.8' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.9'] = "<spring:message code='integracion.tipo.9' javaScriptEscape='true' />";
    tradsIntegracion['integracion.estado.2'] = "<spring:message code='integracion.estado.2' javaScriptEscape='true' />";

</script>