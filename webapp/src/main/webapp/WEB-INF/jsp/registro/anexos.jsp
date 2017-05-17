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

          <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && oficinaRegistral && puedeEditar}">
              <c:if test="${empty maxanexospermitidos || fn:length(registro.registroDetalle.anexos) < maxanexospermitidos }">
                  <c:if test="${teScan}">
                  <a onClick="nuevoAnexoScan()" data-toggle="modal" data-target="#myModal" class="btn btn-${color} btn-xs pull-right margin-left10" role="button"><i class="fa fa-plus"></i> Scan</a>
                  </c:if>
                  <a onClick="nuevoAnexoFichero()" data-toggle="modal" data-target="#myModal" class="btn btn-${color} btn-xs pull-right" role="button"><i class="fa fa-plus"></i> <spring:message code="anexo.archivo.nuevo"/></a>

              </c:if>

          </c:if>
          <h3 class="panel-title"><i class="fa fa-file"></i><strong> <spring:message code="anexo.anexos"/></strong></h3>
      </div>

      <div class="panel-body">

              <div id="anexosdiv" class="">

                 <c:if test="${empty registro.registroDetalle.anexos}">
                     <div class="alert alert-grey alert-dismissable">
                        <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="anexo.anexo"/></strong>
                     </div>
                 </c:if>


                 <c:if test="${not empty registro.registroDetalle.anexos && fn:length(registro.registroDetalle.anexos) >= maxanexospermitidos}">
                      <div class="alert alert-grey alert-dismissable">
                          <strong><spring:message code="anexo.tamanosuperado"/></strong>
                      </div>
                 </c:if>

                 <c:if test="${not empty registro.registroDetalle.anexos}">
                     <table id="anexos" class="table table-bordered table-hover table-striped" style="margin-bottom: 5px;">
                         <colgroup>
                             <col>
                             <col>
                             <col>
                             <col>
                             <col>
                         </colgroup>
                         <thead>
                             <tr>
                                 <th><dt><spring:message code="anexo.titulo"/></dt></th>
                                 <th><spring:message code="anexo.tipoDocumento"/></th>
                                 <th class="center"><spring:message code="anexo.tamano"/></th>
                                 <th class="center">Doc</th>
                                 <th class="center">Firma</th>
                                 <th class="center"><spring:message code="regweb.acciones"/></th>
                             </tr>
                         </thead>

                         <tbody>
                             <c:set var="totalA" value="0" />
                             <c:forEach var="anexo" items="${registro.registroDetalle.anexos}">

                                 <!-- No mostra el justificant ni ho conta pel tamany màxim -->
                                 <c:if test="${!anexo.justificante}">
                                     <tr id="anexo${anexo.id}">

                                         <td>${anexo.titulo}</td>
                                         <td><spring:message code="tipoDocumento.0${anexo.tipoDocumento}"/></td>

                                         <!-- TODO mostrar el tamanyo desde custodia -->
                                         <td class="text-right">
                                             <c:if test="${anexo.modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                                 <c:set var="tamanyAnexo" value="${reg:getSizeOfDocumentCustody(anexo.custodiaID)}" />
                                             </c:if>
                                             <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                                 <c:set var="tamanyAnexo" value="${reg:getSizeOfSignatureCustody(anexo.custodiaID)}" />
                                             </c:if>
                                                 ${tamanyAnexo } KB
                                             <c:set var="totalA" value="${totalA + tamanyAnexo }" />
                                         </td>


                                         <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED}">
                                             <td><a class="btn btn-success btn-default btn-sm"
                                                    href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                    target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                         class="fa fa-download"></span></a></td>
                                             <td><a class="btn btn-info btn-default btn-sm"
                                                    href="<c:url value="/anexo/descargarFirma/${anexo.id}"/>"
                                                    target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                         class="fa fa-download"></span></a></td>
                                         </c:if>
                                         <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA}">
                                             <td><a class="btn btn-success btn-default btn-sm"
                                                    href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                                    target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                     class="fa fa-download"></span></a></td>
                                             <td></td>
                                         </c:if>
                                         <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                             <td></td>
                                             <td><a class="btn btn-info btn-default btn-sm"
                                                    href="<c:url value="/anexo/descargarFirma/${anexo.id}"/>"
                                                    target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                     class="fa fa-download"></span></a></td>
                                         </c:if>


                                         <td class="center">
                                             <c:if test="${!anexo.justificante}">
                                                 <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && oficinaRegistral && puedeEditar}">
                                                     <a class="btn btn-warning btn-sm" data-toggle="modal" data-target="#myModal"  onclick="editarAnexoFull('${anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}')" title="Editar"><span class="fa fa-pencil"></span></a>
                                                     <a class="btn btn-danger btn-default btn-sm"  onclick="eliminarAnexo('${anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.tipoRegistro}', '<spring:message code="anexo.confirmar.eliminar" javaScriptEscape='true'/>')" href="#" title="Eliminar"><span class="fa fa-eraser"></span></a>
                                                 </c:if>
                                                 <c:if test="${(registro.estado != RegwebConstantes.REGISTRO_VALIDO && registro.estado != RegwebConstantes.REGISTRO_RESERVA && registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR) || !oficinaRegistral || !puedeEditar}">
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
                         <td class="senseBorder text-right"><spring:message code="anexo.sumatotaltamany"/>: <b>${totalA} KB</b></td>
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

<div class="modal fade" id="myModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
<div class="modal-dialog" style="width:910px;">
<div class="modal-content">

<div class="modal-header" style="border:hidden; min-width: 5px;">

<button type="button" class="close" onClick="unloadiframe()" data-dismiss="modal" aria-hidden="true" >×</button>
<h3 id="anexoTitulo" style="margin-top: 0px; margin-bottom: 0px;"></h3>
<hr style="margin-top: 5px;margin-bottom: 5px;"  />
</div>

<div class="modal-body" style="padding-top:10px; padding-left:5px; padding-right:0px; padding-bottom:15px;">

<%-- HEIGHT 480px --%>
    <iframe src="" frameborder="0" id="targetiframe" style="width:850px; height:350px; " name="targetframe" allowtransparency="true">
         
    </iframe> <!-- target iframe -->
      
    </div>
  </div>
</div>
</div>

<script type="text/javascript">

    var s ="<html><head></head><body><div class=\"hide col-xs-12 text-center centrat\"><img src=\"<c:url value="/img/712.GIF"/>\" width=\"20\" height=\"20\"/></div></body></html>";
    $('#targetiframe').contents().find('html').html(s);


    
    $('#myModal').on('hidden.bs.modal', function (e) {
        unloadiframe();
      });
    
    
    //load iframe
    function loadiframe(htmlHref)  {
       document.getElementById('targetiframe').src = htmlHref;
    }

    //just for the kicks of it
    function unloadiframe()  {
        $('#targetiframe').attr('src', '');
    }


    function closeAndReload() {
        unloadiframe();
        window.location.href=window.location.href;
    }


    function nouAnnexFull() {

        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');

        loadiframe("<c:url value="/anexo/nou/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${isSir}" />");
    }


    function nuevoAnexoFichero() {

        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');
        // $('#sinfirma').prop("checked", "checked");

        loadiframe("<c:url value="/anexoFichero/ficheros/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${isSir}" />");
    }

    function nuevoAnexoScan() {

        $('#anexoTitulo').html('<spring:message code="anexo.nuevo"/>');

        loadiframe("<c:url value="/anexoScan/new/${registro.registroDetalle.id}/${param.tipoRegistro}/${registro.id}/${isSir}" />");
    }


    function editarAnexoFull(idAnexo, idRegistro, idRegistroDetalle, tipoRegistro) {
        
        $('#anexoTitulo').html('<spring:message code="anexo.editar"/>');
        
        loadiframe("<c:url value="/anexo/editar/"/>" + idRegistroDetalle + "/" + tipoRegistro + "/" + idRegistro + "/" + idAnexo+ "/${isSir}");
    }


    /**
     * Elimina el anexo seleccionado de la Sesion, y la quita en la tabla de anexos.
     * @param idAnexo
     * @param idRegistroDetalle
     */
    function eliminarAnexo(idAnexo, idRegistro, idRegistroDetalle, tipoRegistro, mensaje) {

        confirm("<c:url value="/anexo/delete"/>/"+  idRegistroDetalle + "/" + tipoRegistro + "/" + idRegistro + "/" + idAnexo, mensaje);

    }
 
</script>