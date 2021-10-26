<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:set var="tipoRegistro" value="registroEntrada"/>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:set var="tipoRegistro" value="registroSalida"/>
</c:if>

<%--Modal Anular--%>
<div id="anularModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h3><spring:message code="regweb.confirmar.anular"/></h3>
            </div>

            <div class="modal-body">
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                    <c:url value="/${tipoRegistro}/anular" var="urlAnular" scope="request"/>
                </c:if>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                    <c:url value="/adminEntidad/${tipoRegistro}/anular" var="urlAnular" scope="request"/>
                </c:if>

                <form:form modelAttribute="anularForm" method="post" action="${urlAnular}" cssClass="form-horizontal">

                    <form:input type="hidden" path="idAnular" value=""/>

                    <div class="panel panel-danger">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong><spring:message code="registro.anulacion.motivo"/></strong></h3>
                        </div>

                        <div class="panel-body">
                            <div class="form-group col-xs-12">
                                <div class="col-xs-3 pull-left etiqueta_regweb control-label textEsq">
                                    <label for="motivoAnulacion" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.anulacion"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroSir.motivo"/></label>
                                </div>
                                <div class="col-xs-9" id="motivoAnulacion">
                                    <form:textarea path="observacionesAnulacion" rows="5" cssClass="form-control" maxlength="255"/> <span class="errors"></span>
                                </div>
                            </div>

                        </div> <!-- /.panel body -->
                    </div>
                    <!-- /.panel panel-primary -->
                    <div class="form-actions">
                        <input type="submit" value="<spring:message code="regweb.anular"/>" class="btn btn-danger btn-sm" onclick="return anularRegistro()">
                    </div>
                </form:form>

            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="regweb.cerrar"/></button>
            </div>
        </div>
    </div>
</div>

<script type="application/javascript">

    function anularRegistro() {

        var motivo = $('#observacionesAnulacion').val();

        if(motivo == ""){
            var variable = "#motivoAnulacion span.errors";
            var formatoHtml = "<span id='observaciones.errors' class='help-block'><spring:message code="error.valor.requerido"/></span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            return false;
        }else if(motivo.length > 255){
            var variable = "#motivoAnulacion span.errors";
            var formatoHtml = "<span id='observaciones.errors' class='help-block'><spring:message code="error.valor.maxlenght"/></span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            return false;
        }else{
            doForm('#anularForm');
        }
    }

    function limpiarModalAnulacion(idRegistro){
        $('#idAnular').val(idRegistro);
        $('#observacionesAnulacion').val('');
        var variable = "#motivoAnulacion span.errors";
        var formatoHtml = "<span id='observaciones.errors' class='help-block'></span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").removeClass("has-error");
    }

</script>