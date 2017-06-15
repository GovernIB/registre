<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="detalleInteresado" class="modal fade bs-example-modal-lg">

    <div class="modal-dialog modal-lg" id="formularioInteresado">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        onclick="limpiarInteresadoDetalle()">x
                </button>
                <h3 id="interesadoTitulo"></h3>
            </div>

            <div class="modal-body">

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="regweb.nombre"/></label>
                    </div>
                    <div class="col-xs-7" id="nombre"></div>
                </div>
                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="usuario.apellido1"/></label>
                    </div>
                    <div class="col-xs-7" id="apellido1"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="usuario.apellido2"/></label>
                    </div>
                    <div class="col-xs-7" id="apellido2"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.tipoDocumentoIdentificacion"/></label>
                    </div>
                    <div class="col-xs-7" id="tipoDocIdentificacion"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="usuario.documento"/></label>
                    </div>
                    <div class="col-xs-7" id="documento"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="usuario.email"/></label>
                    </div>
                    <div class="col-xs-7" id="email"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.telefono"/></label>
                    </div>
                    <div class="col-xs-7" id="telefono"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="persona.canal"/></label>
                    </div>
                    <div class="col-xs-7" id="canal"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="interesado.pais"/></label>
                    </div>
                    <div class="col-xs-7" id="pais"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="interesado.provincia"/></label>
                    </div>
                    <div class="col-xs-7" id="provincia"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="interesado.localidad"/></label>
                    </div>
                    <div class="col-xs-7" id="localidad"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="persona.direccion"/></label>
                    </div>
                    <div class="col-xs-7" id="direccion"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.cp"/></label>
                    </div>
                    <div class="col-xs-7" id="cp"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.razonSocial"/></label>
                    </div>
                    <div class="col-xs-7" id="razonSocial"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.direccionElectronica"/></label>
                    </div>
                    <div class="col-xs-7" id="direccionElectronica"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="interesado.observaciones"/></label>
                    </div>
                    <div class="col-xs-7" id="observaciones"></div>
                </div>

                <div class="clearfix"></div>

            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarInteresadoDetalle()">
                    <spring:message code="regweb.cerrar"/></button>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript" src="<c:url value="/js/interesados.js"/>"></script>

<script type="text/javascript">

    var urlCargarInteresado = '<c:url value="/rest/obtenerInteresado"/>';
    var urlCargarInteresadoSir = '<c:url value="/rest/obtenerInteresado"/>';

    var tradsinteresado = new Array();
    tradsinteresado['tipoDocumentoIdentificacion.1'] = "<spring:message code='tipoDocumentoIdentificacion.1' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.2'] = "<spring:message code='tipoDocumentoIdentificacion.2' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.3'] = "<spring:message code='tipoDocumentoIdentificacion.3' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.4'] = "<spring:message code='tipoDocumentoIdentificacion.4' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.5'] = "<spring:message code='tipoDocumentoIdentificacion.5' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.6'] = "<spring:message code='tipoDocumentoIdentificacion.6' javaScriptEscape='true' />";
    tradsinteresado['canalNotificacion.1'] = "<spring:message code='canalNotificacion.1' javaScriptEscape='true' />";
    tradsinteresado['canalNotificacion.2'] = "<spring:message code='canalNotificacion.2' javaScriptEscape='true' />";
    tradsinteresado['canalNotificacion.3'] = "<spring:message code='canalNotificacion.3' javaScriptEscape='true' />";
</script>