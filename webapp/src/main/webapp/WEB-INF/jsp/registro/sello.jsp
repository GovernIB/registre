<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>


<c:if test="${param.tipoRegistro == 'registroEntrada'}">
    <c:url value="/registroEntrada/${registro.id}/sello" var="urlSello"/>
</c:if>

<c:if test="${param.tipoRegistro == 'registroSalida'}">
    <c:url value="/registroSalida/${registro.id}/sello" var="urlSello"/>
</c:if>

<div id="selloModal" class="modal fade" role="dialog" aria-hidden="true" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sello">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel"><spring:message code="sello.imprimir"/></h4>
            </div>
            <div class="modal-body">
                <div id="varios">
                    <span id="paginaX"></span><br>
                    <span id="paginaY"></span><br>
                    <span id="cliente"></span><br>
                    <span id="pdfX"></span><br>
                    <span id="pdfY"></span><br>
                </div>
                <div id="sello" style="width: 210px;height: 297px;background: #FF9523;border: 1px groove;cursor:crosshair;margin-right: 0px;"></div>
                <div id="marca" style="position: absolute;text-align:center; background-color:#000000; color:#FFFFFF;  font-family: arial; font-size: 9px; height:23px; width:45px;"></div>
                <div class="row margin-top20">
                    <div class="col-xs-12 pad-left">
                        <h5><spring:message code="sello.informacion"/></h5>
                    </div>
                </div>
                <div class="row margin-top20">
                    <div class="col-xs-6 pad-left" id="verticales">
                        <span id="V1"><img src="<c:url value="/img/sello/V1.jpg"/>" width="23" height="31" alt="V1" title="V1" onclick="posicionaSegell('V1');"/></span>
                        <span id="V2"><img src="<c:url value="/img/sello/V2.jpg"/>" width="23" height="31" alt="V2" title="V2" onclick="posicionaSegell('V2');"/></span>
                        <span id="V3"><img src="<c:url value="/img/sello/V3.jpg"/>" width="23" height="31" alt="V3" title="V3" onclick="posicionaSegell('V3');"/></span><br>
                        <span id="V4"><img src="<c:url value="/img/sello/V4.jpg"/>" width="23" height="31" alt="V4" title="V4" onclick="posicionaSegell('V4');"/></span>
                        <span id="V5"><img src="<c:url value="/img/sello/V5.jpg"/>" width="23" height="31" alt="V5" title="V5" onclick="posicionaSegell('V5');"/></span>
                        <span id="V6"><img src="<c:url value="/img/sello/V6.jpg"/>" width="23" height="31" alt="V6" title="V6" onclick="posicionaSegell('V6');"/></span><br>
                        <span id="V7"><img src="<c:url value="/img/sello/V7.jpg"/>" width="23" height="31" alt="V7" title="V7" onclick="posicionaSegell('V7');"/></span>
                        <span id="V8"><img src="<c:url value="/img/sello/V8.jpg"/>" width="23" height="31" alt="V8" title="V8" onclick="posicionaSegell('V8');"/></span>
                        <span id="V9"><img src="<c:url value="/img/sello/V9.jpg"/>" width="23" height="31" alt="V9" title="V9" onclick="posicionaSegell('V9');"/></span>
                    </div>
                    <div class="col-xs-6" id="horizontales">
                        <span id="H1"><img src="<c:url value="/img/sello/H1.jpg"/>" width="31" height="23" alt="H1" title="H1" onclick="posicionaSegell('H1');"/></span>
                        <span id="H2"><img src="<c:url value="/img/sello/H2.jpg"/>" width="31" height="23" alt="H2" title="H2" onclick="posicionaSegell('H2');"/></span>
                        <span id="H3"><img src="<c:url value="/img/sello/H3.jpg"/>" width="31" height="23" alt="H3" title="H3" onclick="posicionaSegell('H3');"/></span><br>
                        <span id="H4"><img src="<c:url value="/img/sello/H4.jpg"/>" width="31" height="23" alt="H4" title="H4" onclick="posicionaSegell('H4');"/></span>
                        <span id="H5"><img src="<c:url value="/img/sello/H5.jpg"/>" width="31" height="23" alt="H5" title="H5" onclick="posicionaSegell('H5');"/></span>
                        <span id="H6"><img src="<c:url value="/img/sello/H6.jpg"/>" width="31" height="23" alt="H6" title="H6" onclick="posicionaSegell('H6');"/></span><br>
                        <span id="H7"><img src="<c:url value="/img/sello/H7.jpg"/>" width="31" height="23" alt="H7" title="H7" onclick="posicionaSegell('H7');"/></span>
                        <span id="H8"><img src="<c:url value="/img/sello/H8.jpg"/>" width="31" height="23" alt="H8" title="H8" onclick="posicionaSegell('H8');"/></span>
                        <span id="H9"><img src="<c:url value="/img/sello/H9.jpg"/>" width="31" height="23" alt="H9" title="H9" onclick="posicionaSegell('H9');"/></span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">

                <form id="pagina" action="${urlSello}" method="get" target="_blank">
                    <input id="x" name="x" type="text" hidden="hidden" value="">
                    <input id="y" name="y" type="text" hidden="hidden" value="">
                    <input id="orientacion" name="orientacion" type="text" hidden="hidden" value="">
                    <button class="btn" type="submit"><spring:message code="regweb.imprimir"/></button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="regweb.cerrar"/></button>
                </form>

            </div>
        </div>
    </div>

</div>