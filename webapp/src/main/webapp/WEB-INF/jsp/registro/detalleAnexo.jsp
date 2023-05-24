<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="detalleAnexo" class="modal fade">

    <div class="modal-dialog modal-lg" id="formularioAnexo">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarAnexoDetalle()">x
                </button>
                <h3 id="anexoTitulo"></h3>
            </div>

            <div class="modal-body">

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.titulo"/></label>
                    </div>
                    <div class="col-xs-7" id="titulo"></div>
                </div>
                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="anexo.validezDocumento"/></label>
                    </div>
                    <div class="col-xs-7" id="validezDocumento"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.tipoDocumento"/></label>
                    </div>
                    <div class="col-xs-7" id="tipoDocumento"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.tipoDocumental"/></label>
                    </div>
                    <div class="col-xs-7" id="tipoDocumental"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.origen"/></label>
                    </div>
                    <div class="col-xs-7" id="origen"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.observaciones"/></label>
                    </div>
                    <div class="col-xs-7" id="observacionesAnexo"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.escaneado"/></label>
                    </div>
                    <div class="col-xs-7" id="escaneado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.mime"/></label>
                    </div>
                    <div class="col-xs-7" id="mime"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.nombre.fichero"/></label>
                    </div>
                    <div class="col-xs-7" id="nombreFichero"></div>
                </div>

                <div class="form-group col-xs-10">
                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="anexo.hash"/></label>
                    </div>
                    <div class="col-xs-9" id="hash"></div>
                </div>

                <div id="firmaInformacion">
                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.tipoFirma"/></label>
                        </div>
                        <div class="col-xs-7" id="tipoFirma"></div>
                    </div>
                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.perfilFirma"/></label>
                        </div>
                        <div class="col-xs-7" id="perfilFirma"></div>
                    </div>
                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="anexo.formatoFirma"/></label>
                        </div>
                        <div class="col-xs-7" id="formatoFirma"></div>
                    </div>
                </div>


                <div class="clearfix"></div>

            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarAnexoDetalle()">
                    <spring:message code="regweb.cerrar"/></button>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript" src="<c:url value="/js/anexos.js"/>"></script>

<script type="text/javascript">

    var urlCargarAnexo = '<c:url value="/rest/obtenerAnexo"/>';
    var urlTipoDocumental = '<c:url value="/rest/obtenerTipoDocumental"/>';

    var tradsanexo = new Array();
    tradsanexo['tipoDocumento.01'] = "<spring:message code='tipoDocumento.01' javaScriptEscape='true' />";
    tradsanexo['tipoDocumento.02'] = "<spring:message code='tipoDocumento.02' javaScriptEscape='true' />";
    tradsanexo['tipoDocumento.03'] = "<spring:message code='tipoDocumento.03' javaScriptEscape='true' />";
    tradsanexo['tipoValidezDocumento.1'] = "<spring:message code='tipoValidezDocumento.1' javaScriptEscape='true' />";
    tradsanexo['tipoValidezDocumento.2'] = "<spring:message code='tipoValidezDocumento.2' javaScriptEscape='true' />";
    tradsanexo['tipoValidezDocumento.3'] = "<spring:message code='tipoValidezDocumento.3' javaScriptEscape='true' />";
    tradsanexo['tipoValidezDocumento.4'] = "<spring:message code='tipoValidezDocumento.4' javaScriptEscape='true' />";
    tradsanexo['anexo.origen.0'] = "<spring:message code='anexo.origen.0' javaScriptEscape='true' />";
    tradsanexo['anexo.origen.1'] = "<spring:message code='anexo.origen.1' javaScriptEscape='true' />";

</script>