<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--Modal Eliminar RegistroSir--%>
<div id="eliminarModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h3><spring:message code="regweb.confirmar.eliminacion"/></h3>
            </div>

            <div class="modal-body">
                <c:url value="/sir/registroSir/eliminar" var="urlEliminarRegistroSir" scope="request"/>
                <form:form modelAttribute="eliminarForm" method="post" action="${urlEliminarRegistroSir}" cssClass="form-horizontal">

                    <form:input type="hidden" path="id" value=""/>

                    <div class="panel panel-danger">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong><spring:message code="registroSir.motivo"/></strong></h3>
                        </div>

                        <div class="panel-body">
                            <div class="form-group col-xs-12">
                                <div class="col-xs-3 pull-left etiqueta_regweb control-label textEsq">
                                    <label for="observaciones" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.anulacion"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroSir.observaciones"/></label>
                                </div>
                                <div class="col-xs-9" id="motivo">
                                    <form:textarea path="observaciones" rows="5" cssClass="form-control"/> <span class="errors"></span>
                                </div>
                            </div>

                        </div> <!-- /.panel body -->
                    </div>
                    <!-- /.panel panel-primary -->
                    <div class="form-actions">
                        <input type="submit" value="<spring:message code="regweb.eliminar"/>" class="btn btn-danger btn-sm" onclick="return eliminarRegistroSir()">
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

    function eliminarRegistroSir() {

        var motivo = $('#observaciones').val();

        if(motivo === ""){
            var variable = "#motivo span.errors";
            var formatoHtml = "<span id='observaciones.errors' class='help-block'><spring:message code="error.valor.requerido"/></span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            return false;
        }else{
            doForm('#eliminarForm');
        }
    }

    function limpiarModalEliminar(idRegistro){
        $('#id').val(idRegistro);
        $('#observaciones').val('');
        var variable = "#motivo span.errors";
        var formatoHtml = "<span id='observaciones.errors' class='help-block'></span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").removeClass("has-error");
    }

</script>