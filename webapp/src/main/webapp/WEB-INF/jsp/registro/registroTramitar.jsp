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
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h3><spring:message code="regweb.distribuir"/></h3>
            </div>

            <div class="modal-body">
                <section class="container">
                    <div>
                        <select id="posibles" name="posibles" size="5" multiple></select>
                    </div>
                    <div>
                        <input type="button" id="btnLeft" value="&lt;&lt;"/>
                        <input type="button" id="btnRight" value="&gt;&gt;"/>
                    </div>
                    <div>
                        <select id="propuestos" name="propuestos" size="4" multiple>

                        </select>

                        <div>
                            <input type="text" id="txtRight"/>
                        </div>
                        <div>
                            <textarea id="observtramit" name="observtramit"></textarea>
                        </div>
                    </div>

                </section>
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
    });

    $("#btnRight").click(function () {
        var selectedItem = $("#posibles option:selected");
        $("#propuestos").append(selectedItem);
    });

    $("#propuestos").change(function () {
        var selectedItem = $("#propuestos option:selected");
        $("#txtRight").val(selectedItem.text());
    });


</script>