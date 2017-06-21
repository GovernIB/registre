<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca" >
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
    
    <c:if test="${teScan}">
      ${headerScan}
    </c:if>
</head>

<body style="background-image:none !important;">

<c:if test="${not empty closeAndReload}">

<script type="text/javascript" >

  parent.closeAndReload();

</script>


</c:if>

<c:if test="${empty closeAndReload}">

<c:import url="../modulos/mensajes.jsp"/>

<%-- Formulario que contiene el resto de campos del anexo. --%>
<form:form id="anexoForm" action="${pageContext.request.contextPath}/anexo/${(empty anexoForm.anexo.id)?'nou' : 'editar'}" modelAttribute="anexoForm" method="POST" >
                
                        
                        <form:hidden path="anexo.id" />
                        <form:hidden path="anexo.registroDetalle.id" />
                        <form:hidden path="anexo.custodiaID" />
                        <form:hidden path="anexo.fechaCaptura" />

                        <form:hidden path="registroID" />
                        <form:hidden path="tipoRegistro" />
                        <form:hidden path="oficioRemisionSir" />
                        <%--
                        <form:hidden path="returnURL" />

                        <script>
                          document.getElementById("returnURL").value = document.URL;
                        </script>
                        --%>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6">
                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="titulo"><span class="text-danger">*</span>
                                <spring:message code="anexo.titulo"/></label>
                           </div>
                           <div class="col-xs-8">
                               <form:input path="anexo.titulo" class="form-control"  maxlength="200"/>
                               <form:errors path="anexo.titulo" cssClass="label label-danger"/> 
                           </div>
                        </div>


                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="origenCiudadanoAdmin"><span class="text-danger">*</span> <spring:message code="anexo.origen"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:select path="anexo.origenCiudadanoAdmin" class="chosen-select">
                                    <form:option value="0"><spring:message code="anexo.origen.ciudadano"/></form:option>
                                    <form:option value="1"><spring:message code="anexo.origen.administracion"/></form:option>
                                </form:select>
                                <form:errors path="anexo.origenCiudadanoAdmin" cssClass="label label-danger"/>
                            </div>
                        </div>
                    </div>



                    <div class="col-xs-12">

                        <div class="form-group col-xs-6">
                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                               <label for="tipoDocumento"><span class="text-danger">*</span> <spring:message code="anexo.tipoDocumento"/></label>
                           </div>
                           <div class="col-xs-8">
                               <form:select path="anexo.tipoDocumento" class="chosen-select">
                                   <c:forEach items="${tiposDocumentoAnexo}" var="tipoDocumento">
                                       <form:option value="${tipoDocumento}"><spring:message code="tipoDocumento.0${tipoDocumento}"/></form:option>
                                   </c:forEach>
                               </form:select>
                               <form:errors path="anexo.tipoDocumento" cssClass="label label-danger"/>
                           </div>
                        </div>



                       <div class="form-group col-xs-6">
                          <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                              <label for="tipoDocumental"><span class="text-danger">*</span> <spring:message code="anexo.tipoDocumental"/></label>
                          </div>
                          <div class="col-xs-8">
                              <form:select path="anexo.tipoDocumental.id" class="chosen-select">
                                 <c:forEach items="${tiposDocumental}" var="tipoDocumental">
                                     <form:option value="${tipoDocumental.id}"><i:trad value="${tipoDocumental}" property="nombre"/></form:option>
                                 </c:forEach>
                              </form:select>
                              <form:errors path="anexo.tipoDocumental" cssClass="label label-danger"/>
                          </div>
                       </div>
                    </div>


                    <div class="col-xs-12">

                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="validezDocumento"><span class="text-danger">*</span>
                                    <spring:message code="anexo.validezDocumento"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="anexo.validezDocumento" class="chosen-select">
                                        <%--<form:option value="-1">...</form:option>--%>
                                        <c:forEach items="${tiposValidezDocumento}" var="validezDocumento">
                                            <form:option value="${validezDocumento}"><spring:message code="tipoValidezDocumento.${validezDocumento}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="anexo.validezDocumento" cssClass="label label-danger"/>
                                </div>
                        </div>

                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="observacionesAnexo"><spring:message code="anexo.observaciones"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:textarea path="anexo.observaciones" class="form-control" rows="2"  maxlength="50"/>
                                <form:errors path="anexo.observaciones" cssClass="label label-danger"/>
                            </div>
                        </div>
                    </div>



                    <div class="form-group col-xs-12" id="divmodofirma">
                        <div class="col-xs-8 pull-left etiqueta_regweb control-label">
                              <label><spring:message code="anexo.tipofirma"/></label> :
                              <c:choose>
                                  <c:when test="${anexoForm.anexo.modoFirma == 1}">
                                      <spring:message code="anexo.tipofirma.attached"/>
                                  </c:when>
                                  <c:when test="${anexoForm.anexo.modoFirma == 2}">
                                      <spring:message code="anexo.tipofirma.detached"/>
                                  </c:when>
                                  <c:when test="${anexoForm.anexo.modoFirma == 0}">
                                      <spring:message code="anexo.tipofirma.sinfirma"/>
                                  </c:when>
                              </c:choose>
                        </div>

                    </div>




                    <!--ANEXO-->
                    <c:if test="${not empty anexoForm.documentoCustody}">
                        <div class="form-group col-xs-12">
                            <div class="form-group col-xs-4" id="divInputArchivo">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                   <form:label path="documentoFile" id="labelDocumento"><spring:message
                                           code="anexo.archivo"/></form:label>&nbsp;
                                </div>
                                <div class="col-xs-8">
                                    <c:if test="${empty anexoForm.anexo.id}">
                                        <a href="<c:url value="/anexo/descargarDocumentoCustody" />" target="_blank">
                                                ${anexoForm.documentoCustody.name}
                                        </a>
                                    </c:if>
                                    <c:if test="${not empty anexoForm.anexo.id}">
                                        <a href="<c:url value="/anexo/descargarDocumento/${anexoForm.anexo.id}" />" target="_blank">
                                                ${anexoForm.documentoCustody.name}</a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <!--FIN ANEXO-->

                    <!--FIRMA -->
                    <c:if test="${not empty anexoForm.signatureCustody}">
                        <div class="form-group col-xs-12">
                            <div class="form-group col-xs-4" id="divInputFirma">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                   <form:label path="firmaFile" id="labelFirma"><spring:message
                                           code="anexo.firma"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                <c:if test="${empty anexoForm.anexo.id}">
                                    <a href="<c:url value="/anexo/descargarSignatureCustody" />" target="_blank">
                                            ${anexoForm.signatureCustody.name}</a>
                                </c:if>
                                <c:if test="${not empty anexoForm.anexo.id}">
                                    <a href="<c:url value="/anexo/descargarFirma/${anexoForm.anexo.id}" />" target="_blank">
                                            ${anexoForm.signatureCustody.name}</a>
                                </c:if>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <!--FIN FIRMA-->

                
                   <div class="hide col-xs-12 text-center centrat" id="reload">
                        <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
                   </div>

                   <div class="pull-right"> <%--  class="modal-footer" --%>
                        <button id="desaAnnex" type="submit" class="btn btn-warning btn-sm" onclick="eliminarErrors()"><spring:message code="regweb.guardar"/></button>
                   </div>


</form:form>

</c:if>


<!-- INICI JAVASCRIPT INCLOS DEL PEU -->
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- JavaScript -->
<script type="text/javascript" src="<c:url value="/js/bootstrap.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/bootstrap-dropdown-on-hover-plugin.js"/>"></script>
<!-- DateTimePicker -->
<script type="text/javascript" src="<c:url value="/js/datepicker/moment.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/datepicker/bootstrap-datetimepicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/datepicker/bootstrap-datetimepicker.ca.js"/>"></script>
<!-- Selects multiple -->
<script type="text/javascript" src="<c:url value="/js/chosen.jquery.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/regweb.js"/>"></script>
<!-- Upload file jquery -->
<script type="text/javascript" src="<c:url value="/js/jquery.form.js"/>"></script>


<!-- Redimensiona el Modal des del Scan cap a Annexe -->
<script>
    $(document).ready(function () {
        $(function () {
            parent.redimensionaModalAnnexe();
        });
    });
</script>


<%-- Redimensiona el modal si hi ha errors --%>
<script>

    $(document).ready(function () {
        $(function () {

            <%-- Elimina el Height del iframe --%>
            parent.eliminaHeightIframe();

            var incrementError = 85;
            var iframe = parent.$('#targetiframe').height();
            var modal = parent.$('#modalAnexos').find('.modal-content').height();

            if($('#mensajeError').length != 0){
                iframe = iframe + incrementError;
                parent.$('#targetiframe').height(iframe);
                modal = modal + incrementError;
                parent.$('#modalAnexos').find('.modal-content').height(modal);
            }

            if($('.alert-warning').length != 0){
                iframe = iframe + incrementError + 40;
                parent.$('#targetiframe').height(iframe);
                modal = modal + incrementError + 40;
                parent.$('#modalAnexos').find('.modal-content').height(modal);
            }

            if($('.alert-success').length != 0){
                iframe = iframe + incrementError;
                parent.$('#targetiframe').height(iframe);
                modal = modal + incrementError;
                parent.$('#modalAnexos').find('.modal-content').height(modal);
            }
        });
    });
</script>

<!-- Redimensiona el Modal per si ha d'acursar errors antics -->
<script>
    function eliminarErrors() {
        var incrementError = 85;
        var iframe = parent.$('#targetiframe').height();
        var modal = parent.$('#modalAnexos').find('.modal-content').height();

        if($('#mensajeError').length != 0){
            $('#mensajeError').remove();
            iframe = iframe - incrementError;
            parent.$('#targetiframe').height(iframe);
            modal = modal - incrementError;
            parent.$('#modalAnexos').find('.modal-content').height(modal);
        }
        $('#reload').show();
    }
</script>


<!-- FI JAVASCRIPT INCLOS DEL PEU -->


</body>

</html>
