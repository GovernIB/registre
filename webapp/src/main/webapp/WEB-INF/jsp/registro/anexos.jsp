<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == 'salida'}">
    <c:set var="color" value="danger"/>
</c:if>
<c:if test="${param.tipoRegistro == 'preRegistro'}">
    <c:set var="color" value="warning"/>
</c:if>

<div class="col-xs-8 col-xs-offset pull-right">

    <div class="panel panel-${color}">

        <div class="panel-heading">

            <c:if test="${empty maxanexospermitidos || fn:length(anexos) < maxanexospermitidos }">

                <a onClick="nuevoAnexoFichero()" data-toggle="modal" data-target="#myModal"
                   class="btn btn-${color} btn-xs pull-right margin-left10" role="button"><i class="fa fa-plus"></i>
                    <spring:message code="anexo.archivo.nuevo"/></a>

                <c:if test="${teScan}">
                    <a onClick="nuevoAnexoScan()" data-toggle="modal" data-target="#myModal"
                       class="btn btn-${color} btn-xs pull-right " role="button"><i class="fa fa-plus"></i> Scan</a>
                </c:if>
            </c:if>

            <h3 class="panel-title">
                <i class="fa fa-pencil-square-o"></i> <strong><spring:message code="anexo.anexos"/></strong>:
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                    <span class="text-vermell"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                    <span class="text-taronja"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                    <span class="text-verd"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
            </h3>
        </div>

        <div class="panel-body">

            <div id="anexosdiv" class="">

                <c:if test="${empty anexos}">
                    <div class="alert alert-grey alert-dismissable">
                        <strong><spring:message code="regweb.listado.vacio"/> <spring:message
                                code="anexo.anexo"/></strong>
                    </div>
                </c:if>

                <c:if test="${not empty anexos && fn:length(anexos) >= maxanexospermitidos}">
                    <div class="alert alert-grey alert-dismissable">
                        <strong><spring:message code="anexo.tamanosuperado"/></strong>
                    </div>
                </c:if>

                <c:if test="${not empty anexos}">

                    <table id="anexos" class="table table-bordered table-hover table-striped"
                           style="margin-bottom: 5px;">
                        <colgroup>
                            <col>
                            <col>
                            <col>
                            <col>
                            <col>
                            <col width="100">
                        </colgroup>
                        <thead>
                        <tr>
                            <th><spring:message code="anexo.titulo"/></th>
                            <th><spring:message code="anexo.tipoDocumento"/></th>
                            <th class="center"><spring:message code="anexo.tamano"/></th>
                            <th class="center">Doc</th>
                            <th class="center">Firma</th>
                            <th class="center"><spring:message code="regweb.acciones"/></th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:set var="totalA" value="0"/>
                        <c:forEach var="anexo" items="${anexos}">

                            <!-- No mostra el justificant ni ho conta pel tamany màxim -->
                            <%--<c:if test="${!anexo.justificante}">--%>
                            <tr id="anexo${anexo.id}">

                                <td>${anexo.titulo}</td>
                                <td><spring:message code="tipoDocumento.0${anexo.tipoDocumento}"/></td>

                                <!-- TODO mostrar el tamanyo desde custodia -->
                                <td class="text-right">
                                    <c:if test="${anexo.modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                        <c:set var="tamanyAnexo"
                                               value="${reg:getSizeOfDocumentCustody(anexo.custodiaID)}"/>
                                    </c:if>
                                    <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                        <c:set var="tamanyAnexo"
                                               value="${reg:getSizeOfSignatureCustody(anexo.custodiaID)}"/>
                                    </c:if>
                                        ${tamanyAnexo } KB
                                    <c:set var="totalA" value="${totalA + tamanyAnexo }"/>
                                </td>


                                <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED}">
                                    <td class="center"><a class="btn btn-success btn-default btn-sm"
                                                          href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                          target="_blank"
                                                          title="<spring:message code="anexo.descargar"/>"><span
                                            class="fa fa-download"></span></a></td>
                                    <td class="center"><a class="btn btn-info btn-default btn-sm"
                                                          href="<c:url value="/anexo/descargarFirma/${anexo.id}"/>"
                                                          target="_blank"
                                                          title="<spring:message code="anexo.tipofirma.detached"/>"><span
                                            class="fa fa-download"></span></a></td>
                                </c:if>
                                <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA}">
                                    <td class="center"><a class="btn btn-success btn-default btn-sm"
                                                          href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                          target="_blank"
                                                          title="<spring:message code="anexo.descargar"/>"><span
                                            class="fa fa-download"></span></a></td>
                                    <td class="center"><span class="label label-danger">No</span></td>
                                </c:if>
                                <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                    <td class="center"><a class="btn btn-success btn-default btn-sm"
                                                          href="<c:url value="/anexo/descargarFirma/${anexo.id}"/>"
                                                          target="_blank"
                                                          title="<spring:message code="anexo.descargar"/>"><span
                                            class="fa fa-download"></span></a></td>
                                    <td class="center"><p rel="ayuda" data-content="<spring:message code="anexo.tipofirma.attached"/>" data-toggle="popover"><span class="label label-success">Si</span></p></td>
                                </c:if>


                                <td class="center">
                                    <c:if test="${!anexo.justificante}">
                                        <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && oficinaRegistral && puedeEditar}">
                                            <a class="btn btn-warning btn-sm" data-toggle="modal" data-target="#myModal"
                                               onclick="editarAnexoFull('${anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}')"
                                               title="Editar"><span class="fa fa-pencil"></span></a>
                                            <a class="btn btn-danger btn-default btn-sm"
                                               onclick="eliminarAnexo('${anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}', '
                                                   <spring:message code="anexo.confirmar.eliminar"
                                                                   javaScriptEscape='true'/>')" href="#"
                                               title="Eliminar"><span class="fa fa-eraser"></span></a>
                                        </c:if>
                                        <c:if test="${(registro.estado != RegwebConstantes.REGISTRO_VALIDO && registro.estado != RegwebConstantes.REGISTRO_RESERVA && registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR) || !oficinaRegistral || !puedeEditar}">
                                            <a class="btn btn-warning disabled btn-sm" href="javascript:void(0);"
                                               title="Editar"><span class="fa fa-pencil"></span></a>
                                            <a class="btn btn-danger disabled btn-sm" href="javascript:void(0);"
                                               title="<spring:message code="regweb.eliminar"/>"><span
                                                    class="fa fa-eraser"></span></a>
                                        </c:if>
                                    </c:if>

                                </td>

                            </tr>
                            <%--</c:if>--%>
                        </c:forEach>
                            <%-- Fila pel tamany Total dels annexes --%>
                        <tr>
                            <td class="senseBorder"></td>
                            <td class="senseBorder"></td>
                            <td class="senseBorder text-right"><spring:message code="anexo.sumatotaltamany"/>:
                                <b>${totalA} KB</b></td>
                            <td class="senseBorder"></td>
                            <td class="senseBorder"></td>
                            <td class="senseBorder"></td>
                        </tr>
                        </tbody>
                    </table>

                </c:if>
                <p class="textPeu">${notainformativa}</p>
            </div>

        </div>
    </div>

</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:910px; height:300px;">
        <div class="modal-content">

            <div class="modal-header" style="border:hidden; min-width: 5px;padding: 5px;">
                <button type="button" class="close" onClick="unloadiframe()" data-dismiss="modal" aria-hidden="true">×
                </button>
                <h3 id="anexoTitulo" style="margin-top: 0px; margin-bottom: 0px;"></h3>
                <hr style="margin-top: 5px;margin-bottom: 5px;"/>
            </div>

            <div class="modal-body" style="padding-top:0px; padding-left:5px; padding-right:0px; padding-bottom:15px;">
                <%-- HEIGHT 480px --%>
                <iframe src="" frameborder="0" id="targetiframe" style="width:850px; height:650px; " name="targetframe"
                        allowtransparency="true">
                </iframe> <!-- target iframe -->
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var s = "<html><head></head><body><div class=\"hide col-xs-12 text-center centrat\"><img src=\"<c:url value="/img/712.GIF"/>\" width=\"20\" height=\"20\"/></div></body></html>";
    $('#targetiframe').contents().find('html').html(s);


    $('#myModal').on('hidden.bs.modal', function (e) {
        unloadiframe();
    });

    $('#myModal').on('show.bs.modal', function () {
        $('.modal-content').css('height', $(window).height() * 0.8);
    });


    //load iframe
    function loadiframe(htmlHref, height) {
        document.getElementById('targetiframe').src = htmlHref;
    }

    //just for the kicks of it
    function unloadiframe() {
        $('#targetiframe').attr('src', '');
    }


    function closeAndReload() {
        unloadiframe();
        window.location.href = window.location.href;
    }


    function nouAnnexFull() {

        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');

        loadiframe("<c:url value="/anexo/nou/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${oficio.sir}" />");
    }


    function nuevoAnexoFichero() {

        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');

        loadiframe("<c:url value="/anexoFichero/ficheros/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${oficio.sir}" />");
    }


    function nuevoAnexoScan() {

        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');

        loadiframe("<c:url value="/anexoScan/new/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${oficio.sir}" />");

        // XYZ ZZZ setTimeout(checkIframeSize, 3000);
    }


    function editarAnexoFull(idAnexo, idRegistro, idRegistroDetalle, tipoRegistro) {

        $('#anexoTitulo').html('<spring:message code="anexo.editar"/>');

        loadiframe("<c:url value="/anexo/editar/"/>" + idRegistroDetalle + "/" + tipoRegistro + "/" + idRegistro + "/" + idAnexo + "/${oficio.sir}");
    }


    /**
     * Elimina el anexo seleccionado de la Sesion, y la quita en la tabla de anexos.
     * @param idAnexo
     * @param idRegistroDetalle
     */
    function eliminarAnexo(idAnexo, idRegistro, idRegistroDetalle, tipoRegistro, mensaje) {

        confirm("<c:url value="/anexo/delete"/>/" + idRegistroDetalle + "/" + tipoRegistro + "/" + idRegistro + "/" + idAnexo, mensaje);

    }

</script>

<script type="text/javascript">

    var lastSize = 0;

    function checkIframeSize() {

        var log = true;

        if (log) {
            console.log(" checkIFrameTargetSize():: ENTRA ");
        }

        setTimeout(checkIframeSize, 5000);

        var iframe = document.getElementById('targetiframe');

        var iframeDocument = iframe.contentDocument || iframe.contentWindow.document;

        var h1 = $(iframeDocument.body).height();
        var h2 = iframeDocument.body.scrollHeight;


        var h = Math.max(h1, h2);


        var d = new Date();
        if (log) {
            console.log(" checkIFrameTargetSize() ======= " + d + " (H = " + h + " | H1= " + h1 + " | H2= " + h2 + ") ===================");
        }

        if (h != lastSize) {
            h = h + 300;
            lastSize = h;
            if (log) {
                console.log(" checkIFrameTargetSize()::iframeDocument.body.scrollHeight = " + iframeDocument.body.scrollHeight);
                console.log(" checkIFrameTargetSize()::$(iframeDocument.body).height() = " + $(iframeDocument.body).height());
                console.log(" checkIFrameTargetSize():: SET " + h);
            }
            //document.getElementById('targetiframe').style.height=h + "px";
            document.getElementById('targetiframe').height = h + "px";
            if (log) {
                console.log(" checkIFrameTargetSize():: GET HEIGHT IFRAME " + document.getElementById('targetiframe').height);
            }

            lastSize = Math.max($(iframeDocument.body).height(), iframeDocument.body.scrollHeight);
            <%--  $("#tablefull").height() --%>
            if (log) {
                console.log(" checkIFrameTargetSize():: GET HEIGHT CONTENT " + lastSize);
            }
        }
    }


</script>

