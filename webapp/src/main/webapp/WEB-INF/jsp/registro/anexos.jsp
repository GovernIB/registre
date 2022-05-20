<%@ page import="es.caib.regweb3.utils.Configuracio" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-12">

    <div class="panel panel-${color}">

        <div class="panel-heading">

            <c:if test="${registro.registroDetalle.tipoDocumentacionFisica != RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">

                <c:if test="${empty numeromaxanexossir || fn:length(anexos) < numeromaxanexossir }">

                    <c:if test="${!teScan}">
                        <a onClick="nuevoAnexoFichero()" data-toggle="modal" data-target="#modalAnexos" class="btn btn-${color} btn-xs pull-right margin-left10" role="button"><i class="fa fa-plus"></i> <spring:message code="anexo.nuevo"/></a>
                    </c:if>

                    <c:if test="${teScan}">
                        <div class="btn-group pull-right text12">
                            <button type="button" class="btn btn-${color} btn-xs dropdown-toggle" data-toggle="dropdown">
                                <spring:message code="anexo.nuevo"/> <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu anexoDropdown-${color}">

                                <li class="submenu-complet">
                                    <a onClick="nuevoAnexoFichero()" data-toggle="modal" data-target="#modalAnexos"><spring:message code="anexo.origen.archivo"/></a>
                                </li>
                                <c:if test="${!permiteDigitMasiva}">
                                <li class="submenu-complet">
                                    <a onClick=" nuevoAnexoScan()" data-toggle="modal" data-target="#modalAnexos"><spring:message code="anexo.origen.escaner"/></a>
                                </li>
                                </c:if>
                                <c:if test="${permiteDigitMasiva}">
                                    <li class="submenu-complet">
                                        <a onClick="nuevoAnexoMasivoScan()" data-toggle="modal" data-target="#modalAnexos"><spring:message code="anexo.origen.escaner"/></a>
                                    </li>
                                </c:if>
                            </ul>
                        </div>
                    </c:if>

                </c:if>
            </c:if>

            <h3 class="panel-title">
                <i class="fa fa-pencil-square-o"></i> <strong><spring:message code="anexo.anexos"/></strong>:
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                    <span class="text-vermell text14"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                    <span class="text-taronja text14"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
                <c:if test="${registro.registroDetalle.tipoDocumentacionFisica==RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                    <span class="text-verd text14"><spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}"/></span>
                </c:if>
            </h3>
        </div>

        <div class="panel-body">

            <div id="anexosdiv" class="">

                <c:if test="${empty anexos}">
                    <div class="alert alert-grey alert-dismissable">
                        <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="anexo.anexo"/></strong>
                    </div>
                </c:if>

                <c:if test="${not empty erroresAnexosSir}">
                    <div class="alert alert-danger">
                        <c:forEach var="errorAnexoSir" items="${erroresAnexosSir}">
                            <p><strong>${errorAnexoSir}</strong></p>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${empty erroresAnexosSir && fn:length(anexos) >= numeromaxanexossir }">
                    <div class="alert alert-warning">
                        <p><strong><spring:message code="anexo.numeroMaximo.superado"/></strong></p>
                    </div>
                </c:if>

                <c:if test="${not empty anexos}">

                    <table id="anexos" class="table table-bordered table-hover table-striped" style="margin-bottom: 5px;">
                        <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.sir.validezDocumento"/></th>
                                <th><spring:message code="anexo.tipoDocumento.corto"/></th>
                                <th class="center"><spring:message code="anexo.tamano"/></th>
                                <th class="center"><spring:message code="anexo.anexo"/></th>
                                <th class="center">Firma</th>
                                <th class="center"><spring:message code="regweb.acciones"/></th>
                            </tr>
                        </thead>

                        <tbody>
                        <c:set var="totalA" value="0"/>
                        <c:forEach var="anexoFull" items="${anexos}">

                            <c:if test="${anexoFull.anexo.titulo != RegwebConstantes.FICHERO_REGISTROELECTRONICO}">
                            <tr id="anexo${anexoFull.anexo.id}">

                                <%--TÍTULO--%>
                                <td>
                                    <c:if test="${anexoFull.anexo.titulo != anexoFull.anexo.tituloCorto}">
                                        <p rel="popupAbajo" data-content="<c:out value="${anexoFull.anexo.titulo}" escapeXml="true"/>" data-toggle="popover"><c:out value="${anexoFull.anexo.tituloCorto}" escapeXml="true"/></p>
                                    </c:if>
                                    <c:if test="${anexoFull.anexo.titulo == anexoFull.anexo.tituloCorto}">
                                        <c:out value="${anexoFull.anexo.titulo}" escapeXml="true"/>
                                    </c:if>
                                </td>

                                <%--VALIDEZ DOCUMENTO--%>
                                <td><spring:message code="tipoValidezDocumento.${anexoFull.anexo.validezDocumento}"/></td>

                                <%--TIPO DOCUMENTO--%>
                                <td><spring:message code="tipoDocumento.0${anexoFull.anexo.tipoDocumento}"/></td>

                                <%--TAMAÑO--%>
                                <td class="text-right">
                                    <c:if test="${anexoFull.anexo.modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && !anexoFull.anexo.confidencial}">
                                        <c:set var="tamanyAnexo" value="${anexoFull.docSize}"/>
                                    </c:if>
                                    <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && !anexoFull.anexo.confidencial}">
                                        <c:set var="tamanyAnexo" value="${anexoFull.signSize}"/>
                                    </c:if>
                                    <c:if test="${anexoFull.anexo.confidencial}">
                                        <c:set var="tamanyAnexo" value="${anexoFull.anexo.confidencialSize}"/>
                                    </c:if>
                                        ${tamanyAnexo } KB
                                    <c:set var="totalA" value="${totalA + tamanyAnexo }"/>
                                </td>

                                <%--BOTONES DESCARGA ANEXO Y FIRMA--%>
                                <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED && !anexoFull.anexo.confidencial}">
                                    <td class="center"> <%--ANEXO--%>
                                        <c:if test="${!anexoFull.anexo.purgado}">
                                            <a class="btn btn-success btn-default btn-sm"
                                               href="<c:url value="/anexo/descargarDocumento/${anexoFull.anexo.id}"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-download"></span></a>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.purgado}">
                                            <a class="btn btn-success btn-default btn-sm disabled"
                                               href="<c:url value="/anexo/descargarDocumento/${anexoFull.anexo.id}"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-window-close"></span></a>
                                        </c:if>
                                    </td>
                                    <td class="center"> <%--FIRMA--%>

                                        <%--definimos la clase y la etiqueta en funcion de los estados--%>
                                        <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_VALIDA}">
                                            <c:set var="clase" value="btn btn-info btn-default btn-sm"/>
                                            <c:set var="etiqueta" value="anexo.tipofirma.detached.valido"/>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_INVALIDA || anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_ERROR}">
                                            <c:set var="clase" value="btn btn-danger btn-default btn-sm"/>
                                            <c:set var="etiqueta" value="anexo.tipofirma.detached.invalido"/>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_NOINFO}">
                                            <c:set var="clase" value="btn btn-warning btn-default btn-sm"/>
                                            <c:set var="etiqueta" value="anexo.tipofirma.detached.noinfo"/>
                                        </c:if>
                                        <c:if test="${!anexoFull.anexo.purgado}">
                                            <a class="${clase}"
                                               href="<c:url value="/anexo/descargarFirma/${anexoFull.anexo.id}/true"/>"
                                               target="_blank" title="<spring:message code="${etiqueta}"/>"><span class="fa fa-key"></span></a>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.purgado}"> <%--Si esta purgado se deshabilita--%>
                                            <a class="${clase} disabled"
                                               href="<c:url value="/anexo/descargarFirma/${anexoFull.anexo.id}/true"/>"
                                               target="_blank" title="<spring:message code="${etiqueta}"/>"><span class="fa fa-window-close"></span></a>
                                        </c:if>
                                    </td>
                                </c:if>

                                <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA && !anexoFull.anexo.confidencial}">
                                    <td class="center">
                                        <c:if test="${!anexoFull.anexo.purgado}">
                                            <a class="btn btn-success btn-default btn-sm"
                                               href="<c:url value="/anexo/descargarDocumento/${anexoFull.anexo.id}"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-download"></span></a>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.purgado}">
                                            <a class="btn btn-success btn-default btn-sm disabled"
                                               href="<c:url value="/anexo/descargarDocumento/${anexoFull.anexo.id}"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-window-close"></span></a>
                                        </c:if>
                                    </td>
                                    <td class="center"><p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.sinfirma"/>" data-toggle="popover"><span class="label label-default">No</span></p></td>
                                </c:if>

                                <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && !anexoFull.anexo.confidencial}">
                                    <td class="center">
                                        <c:if test="${!anexoFull.anexo.purgado}">
                                            <a class="btn btn-success btn-default btn-sm"
                                               href="<c:url value="/anexo/descargarFirma/${anexoFull.anexo.id}/true"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-download"></span></a>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.purgado}">
                                            <a class="btn btn-success btn-default btn-sm disabled"
                                               href="<c:url value="/anexo/descargarFirma/${anexoFull.anexo.id}/true"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span class="fa fa-window-close"></span></a>
                                        </c:if>
                                    </td>
                                    <td class="center">
                                        <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_VALIDA}">
                                            <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.attached.valido"/>(<fmt:formatDate value="${anexoFull.anexo.fechaValidacion}" pattern="dd/MM/yyyy"/>)" data-toggle="popover"><span class="label label-success"><span class="fa fa-key"></span></span></p>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_INVALIDA || anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_ERROR}">
                                            <p rel="popupAbajo" data-content="${anexoFull.anexo.motivoNoValidacion}" data-toggle="popover"><span class="label label-danger"><span class="fa fa-key"></span></span></p>
                                        </c:if>
                                        <c:if test="${anexoFull.anexo.estadoFirma == RegwebConstantes.ANEXO_FIRMA_NOINFO}">
                                            <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.attached"/>" data-toggle="popover"><span class="label label-success">Si</span></p>
                                        </c:if>
                                    </td>
                                </c:if>

                                <%--ANEXO CONFIDENCIAL--%>
                                <c:if test="${anexoFull.anexo.confidencial}">
                                    <td class="center">
                                        <a class="btn btn-success btn-default btn-sm disabled" href="" target="_blank" title="<spring:message code="anexo.confidencial"/>">
                                            <span class="fa fa-lock"></span>
                                        </a>
                                    </td>
                                    <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA}">
                                        <td class="center">
                                            <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.sinfirma"/>" data-toggle="popover"><span class="label label-default">No</span></p>
                                        </td>
                                    </c:if>

                                    <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                        <td class="center">
                                            <p rel="popupAbajo" data-content="<spring:message code="anexo.tipofirma.attached"/>" data-toggle="popover"><span class="label label-success">Si</span></p>
                                        </td>
                                    </c:if>

                                    <c:if test="${anexoFull.anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED}">
                                        <td class="center">
                                            <a class="btn btn-warning btn-default btn-sm disabled" href="" target="_blank" title="<spring:message code="anexo.confidencial"/>">
                                                <span class="fa fa fa-lock"></span>
                                            </a>
                                        </td>
                                    </c:if>
                                </c:if>

                                <%--Botonera--%>
                                <td class="center">
                                    <c:if test="${!anexoFull.anexo.justificante}">
                                        <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && puedeEditar && !anexoFull.anexo.confidencial}">

                                            <%--Instalación estándar --%>
                                            <spring:eval expression="@environment.getProperty('es.caib.regweb3.iscaib')" var="isCaib"/>
                                            <c:if test="${not isCaib}">

                                                <a class="btn btn-warning btn-sm" data-toggle="modal" data-target="#modalAnexos"
                                                   onclick="editarAnexoFull('${anexoFull.anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}')"
                                                   title="Editar"><span class="fa fa-pencil"></span></a>
                                                <a class="btn btn-danger btn-default btn-sm"
                                                   onclick="eliminarAnexo('${anexoFull.anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}', '<spring:message code="anexo.confirmar.eliminar" javaScriptEscape='true'/>')" href="#"
                                                   title="Eliminar"><span class="fa fa-eraser"></span></a>
                                            </c:if>

                                            <%--Si se trata de una instalación CAIB, comprobamos si el usuario tiene el rol DIB_USER_RW--%>
                                            <c:if test="${isCaib}">
                                                <%--Si el anexo es copia auténtica y el usuario tiene el rol DIB_USER_RW--%>
                                                <c:if test="${anexoFull.anexo.validezDocumento == RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL && loginInfo.usuarioAutenticado.dib_user_rw}">
                                                    <a class="btn btn-warning btn-sm" data-toggle="modal" data-target="#modalAnexos"
                                                       onclick="editarAnexoFull('${anexoFull.anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}')"
                                                       title="Editar"><span class="fa fa-pencil"></span></a>
                                                    <a class="btn btn-danger btn-default btn-sm"
                                                       onclick="eliminarAnexo('${anexoFull.anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}', '<spring:message code="anexo.confirmar.eliminar" javaScriptEscape='true'/>')" href="#"
                                                       title="Eliminar"><span class="fa fa-eraser"></span></a>
                                                </c:if>

                                                <%--Si el anexo es copia auténtica y el usuario NO tiene el rol DIB_USER_RW--%>
                                                <c:if test="${anexoFull.anexo.validezDocumento == RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL && !loginInfo.usuarioAutenticado.dib_user_rw}">
                                                    <a class="btn btn-warning disabled btn-sm" href="javascript:void(0);" title="Editar"><span class="fa fa-pencil"></span></a>
                                                    <a class="btn btn-danger disabled btn-sm" href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                                </c:if>

                                                <c:if test="${anexoFull.anexo.validezDocumento != RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL}">
                                                    <a class="btn btn-warning btn-sm" data-toggle="modal" data-target="#modalAnexos"
                                                       onclick="editarAnexoFull('${anexoFull.anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}')"
                                                       title="Editar"><span class="fa fa-pencil"></span></a>
                                                    <a class="btn btn-danger btn-default btn-sm"
                                                       onclick="eliminarAnexo('${anexoFull.anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}', '<spring:message code="anexo.confirmar.eliminar" javaScriptEscape='true'/>')" href="#"
                                                       title="Eliminar"><span class="fa fa-eraser"></span></a>
                                                </c:if>
                                            </c:if>

                                        </c:if>
                                        <c:if test="${(registro.estado != RegwebConstantes.REGISTRO_VALIDO && registro.estado != RegwebConstantes.REGISTRO_RESERVA && registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR) || !puedeEditar || anexoFull.anexo.confidencial}">
                                            <a class="btn btn-warning disabled btn-sm" href="javascript:void(0);" title="Editar"><span class="fa fa-pencil"></span></a>
                                            <a class="btn btn-danger disabled btn-sm" href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                            </c:if>
                        </c:forEach>
                            <%-- Fila pel tamany Total dels annexes --%>
                        <tr>
                            <td class="senseBorder"></td>
                            <td class="senseBorder"></td>
                            <td class="senseBorder text-right" colspan="2"><spring:message code="anexo.sumatotaltamany"/>:
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

<!-- MODAL AFEGIR ANNEXES -->
<div class="modal fade" id="modalAnexos" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="unloadiframe()">x</button>
                <h3 class="modal-title" id="anexoTitulo"></h3>
                <input type="hidden" id="tamanyModal" name="tamany" value=""/>
            </div>
            <div class="modal-body">
                <iframe src="" frameborder="0" id="targetiframe" name="targetframe" allowtransparency="true" style="overflow:visible;" scrolling="no"></iframe>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    // Variables de tamany definit al Modal
//    var tamModalAnexo = 340;  SI HI HA L'OPCIO DE FIRMA EN DOCUMENT SEPARAT
//    var tamModalFitxer = 480; SI HI HA L'OPCIO DE FIRMA EN DOCUMENT SEPARAT
//    var tamModalScan = 710;   MODAL MOLT GROS PER SCAN

    var tamModalAnexo;
    var tamModalFitxer;

    /* SI HI HA L'OPCIO DE DOCUMENT AMB FIRMA SEPARADA*/
    if(${anexoDetachedPermitido}) {
        tamModalAnexo = 340;
        tamModalFitxer = 560;
    } else{   /* SI NO HI HA L'OPCIO DE DOCUMENT AMB FIRMA SEPARADA*/
        tamModalAnexo = 260;
        tamModalFitxer = 560;
    }
    var tamModalScan = 650;


    // Afegeix el contingut HTML amb imatge de "pensar"
    var s = "<html><head></head><body><div class=\"hide col-xs-12 text-center centrat\"><img src=\"<c:url value="/img/712.GIF"/>\" width=\"20\" height=\"20\"/></div></body></html>";
    $('#targetiframe').contents().find('html').html(s);

    // Si el modal està desactivat, esborra el seu contingut
    $('#modalAnexos').on('hidden.bs.modal', function (e) {
        unloadiframe();
    });

    // Si el modal està activat, Afegeix la llargària del modal d'annexe
    $('#modalAnexos').on('show.bs.modal', function () {
        $('#modalAnexos').find('.modal-content').css('height', $('#tamanyModal').val());
    });

    // Carregam el iframe amb el Contingut que passam per paràmetre
    function loadiframe(htmlHref) {
        document.getElementById('targetiframe').src = htmlHref;
    }

    <%-- Esborra tamany de l'iframe --%>
    function eliminaHeightIframe() {
        $('#targetiframe').height('');
    }

    // Esborra el contingut del iframe, el height i els css
    function unloadiframe() {
        $('#targetiframe').attr('src', '');
        eliminaCssIframe();
        $('#targetiframe').height('');
        $('#modalAnexos').find('.modal-content').css('height', '');
    }

    // Quan es tanca el modal, esborra el seu contingut
    function closeAndReload() {
        unloadiframe();
        window.location.href = window.location.href;
    }

    // Esborra els class de l'iframe
    function eliminaCssIframe() {
        $('#targetiframe').removeClass('iframeAnexo');
        $('#targetiframe').removeClass('iframeAnexoFile');
        $('#targetiframe').removeClass('iframeAnexoGros');
        $('#targetiframe').removeClass('iframeAnexoFileGros');
        $('#targetiframe').removeClass('iframeScan');
        $('#targetiframe').removeClass('metadades');
    }


    function nouAnnexFull() {
        // Fixa el tamany del modal
        $('#tamanyModal').val(tamModalFitxer);
        // Esborra les classes de iframe
        eliminaCssIframe();
        // Afegeix la classe a iframe
        if(${anexoDetachedPermitido}) {
            $('#targetiframe').addClass('iframeAnexoGros');
        } else {
            $('#targetiframe').addClass('iframeAnexo');
        }

        // Posa el títol al modal
        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');
        // Afegeix el contingut de formularioAnexoFichero.jsp al modal
        loadiframe("<c:url value="/anexo/nou/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${registro.evento == RegwebConstantes.EVENTO_OFICIO_SIR}" />");
    }


    // Quan s'espitja botó de nou Fitxer
    function nuevoAnexoFichero() {
        // Fixa el tamany del modal
        $('#tamanyModal').val(tamModalAnexo);
        // Esborra les classes de iframe
        eliminaCssIframe();
        // Afegeix la classe a iframe
        if(${anexoDetachedPermitido}) {
            $('#targetiframe').addClass('iframeAnexoFileGros');
        } else {
            $('#targetiframe').addClass('iframeAnexoFile');
        }

        // Posa el títol al modal
        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');
        // Afegeix el contingut de formularioAnexoFichero.jsp al modal
        loadiframe("<c:url value="/anexoFichero/ficheros/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${registro.evento == RegwebConstantes.EVENTO_OFICIO_SIR}" />");
    }


    // Quan s'espitja botó de nou Scan
    function nuevoAnexoScan() {
        // Fixa el tamany del modal
        $('#tamanyModal').val(tamModalScan);
        // Esborra les classes de iframe
        eliminaCssIframe();
        // Afegeix la classe a iframe
        $('#targetiframe').addClass('iframeScan');
        // Posa el títol al modal
        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');
        // Afegeix el contingut de formularioAnexoFichero.jsp al modal
        url = "?scanweb_absoluteurl=" + encodeURIComponent(window.location.href);
        loadiframe("<c:url value="/anexoScan/new/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${registro.evento == RegwebConstantes.EVENTO_OFICIO_SIR}" />" +url);
    }

    // Quan s'espitja botó de nou Scan
    function nuevoAnexoMasivoScan() {
        // Fixa el tamany del modal
        $('#tamanyModal').val(tamModalScan);
        // Esborra les classes de iframe
        eliminaCssIframe();
        // Afegeix la classe a iframe
        $('#targetiframe').addClass('iframeScan');
        // Posa el títol al modal
        $('#anexoTitulo').html('<spring:message code="anexo.scan.masivo"/>');
        // Afegeix el contingut de formularioAnexoFichero.jsp al modal
        loadiframe("<c:url value="/anexoScan/new/masivo/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${registro.evento == RegwebConstantes.EVENTO_OFICIO_SIR}" />");
    }


    // Quan s'espitja botó d'Editar Annexe
    function editarAnexoFull(idAnexo, idRegistro, idRegistroDetalle, tipoRegistro) {
        // Fixa el tamany del modal
        $('#tamanyModal').val(tamModalFitxer);
        // Esborra les classes de iframe
        eliminaCssIframe();
        // Afegeix la classe a iframe
        if(${anexoDetachedPermitido}) {
            $('#targetiframe').addClass('iframeAnexoGros');
        } else {
            $('#targetiframe').addClass('iframeAnexo');
        }

        // Posa el títol al modal
        $('#anexoTitulo').html('<spring:message code="anexo.editar"/>');
        // Afegeix el contingut de formularioAnexoFichero.jsp al modal
        loadiframe("<c:url value="/anexo/editar/"/>" + idRegistroDetalle + "/" + tipoRegistro + "/" + idRegistro + "/" + idAnexo + "/${registro.evento == RegwebConstantes.EVENTO_OFICIO_SIR}");
    }


    <!-- Redimensiona el Modal des del Scan cap a Annexe -->
    function redimensionaModalAnnexe() {
        // Fixa el tamany del modal
        document.getElementById('tamanyModal').setAttribute("value", tamModalFitxer);
        $('#modalAnexos').find('.modal-content').css('height', $('#tamanyModal').val());
        // Afegeix la classe a iframe
        if(${anexoDetachedPermitido}) {
            document.getElementById('targetiframe').setAttribute("class", 'iframeAnexoGros');
        } else {
            document.getElementById('targetiframe').setAttribute("class", 'iframeAnexo');
        }
    }

    function redimensionaModalScan(nuevoEstilo) {
        // Fixa el tamany del modal
        document.getElementById('tamanyModal').setAttribute("value", tamModalScan);
        $('#modalAnexos').find('.modal-content').css('height', $('#tamanyModal').val());
        // Afegeix la classe a iframe
        if(${anexoDetachedPermitido}) {
            document.getElementById('targetiframe').setAttribute("class", 'iframeAnexoGros');
        } else {
            document.getElementById('targetiframe').setAttribute("class", 'iframeAnexo');
        }
        if(nuevoEstilo!=null) {
            $("#targetiframe").addClass(nuevoEstilo);
        }
    }

    <!-- Redimensiona el Modal amb Errors cap a AnnexeFile-->
    function redimensionaModalAnnexeErrors() {
        // Fixa el tamany del modal
        document.getElementById('tamanyModal').setAttribute("value", tamModalAnexo);
        $('#modalAnexos').find('.modal-content').css('height', $('#tamanyModal').val());
        // Canvia tamany al height iframe
        $('#targetiframe').height('');
        // Afegeix la classe a iframe
        if(${anexoDetachedPermitido}) {
            document.getElementById('targetiframe').setAttribute("class", 'iframeAnexoFileGros');
        }else {
            document.getElementById('targetiframe').setAttribute("class", 'iframeAnexoFile');
        }
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

