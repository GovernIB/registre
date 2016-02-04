<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${param.tipoRegistro == 'registroEntrada'}">
    <c:url value="/registroEntrada/${registro.id}/sello" var="urlSello"/>
    <c:url value="/registroEntrada/${registro.id}/enviardestinatarios" var="urlDistribuir"/>
</c:if>

<c:if test="${param.tipoRegistro == 'registroSalida'}">
    <c:url value="/registroSalida/${registro.id}/sello" var="urlSello"/>
    <c:url value="/registroSalida/${registro.id}/enviardestinatarios" var="urlDistribuir"/>
</c:if>

<%--Modal tramitar--%>

<div id="distribuirModal" class="modal fade bs-example-modal-lg">
    <c:if test="${empty destinatarios}">No hay destinatarios</c:if>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h3><spring:message code="regweb.distribuir"/></h3>
            </div>

            <div class="modal-body">
                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong>Llistat de
                            destinataris</strong></h3>
                    </div>

                    <div class="panel-body">
                        <%--<p>Puede modificar los destinos propuestos a los que distribuir el asiento</p>--%>
                        <%--<section class="container">--%>
                            <div class="form-group col-xs-5">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label>Destinos Posibles</label>
                            </div>
                            <div class="col-xs-8">
                                <select id="posibles" name="posibles" size="4" multiple></select>
                            </div>
                        </div>
                            <div class="form-group col-xs-2">
                            <input type="button" id="btnLeft" value="&lt;&lt;"/>
                            <input type="button" id="btnRight" value="&gt;&gt;"/>
                        </div>
                            <div class="form-group col-xs-5">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label>Destinos Propuestos</label>
                            </div>
                            <div class="col-xs-8">
                                <select id="propuestos" name="propuestos" size="4" multiple></select>
                            </div>

                            <%-- <div>
                                 <input type="text" id="txtRight"/>
                             </div>--%>

                            </div>
                            <div class="form-group col-xs-10">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <label>Observaciones</label>
                            </div>
                                <div class="col-xs-8">
                                    <textarea id="observtramit" name="observtramit"></textarea>
                                </div>
                        </div>

                        <%--</section>--%>
                    </div>

            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message
                        code="regweb.cerrar"/></button>
                <button class="btn" data-dismiss="modal" aria-hidden="true"
                        onclick="enviarDestinatarios('${urlDistribuir}')"><spring:message
                        code="regweb.enviar"/></button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $("#btnLeft").click(function () {
        var selectedItem = $("#propuestos option:selected");
        $("#posibles").append(selectedItem);
        $("#posibles option:selected").prop("selected", false);
    });

    $("#btnRight").click(function () {
        var selectedItem = $("#posibles option:selected");
        $("#propuestos").append(selectedItem);
        $("#propuestos option:selected").prop("selected", false);
    });

    $("#propuestos").change(function () {
        var selectedItem = $("#propuestos option:selected");
        // $("#txtRight").val(selectedItem.text());
    });


</script>