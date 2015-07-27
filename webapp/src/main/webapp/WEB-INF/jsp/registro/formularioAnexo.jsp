<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>
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
<form:form id="anexoForm" action="${pageContext.request.contextPath}/anexo/${(empty anexoForm.anexo.id)?'nou' : 'editar'}" modelAttribute="anexoForm" method="POST"  enctype="multipart/form-data">
                
                        
                        <form:hidden path="anexo.id" />
                        <form:hidden path="anexo.registroDetalle.id" />
                        <form:hidden path="anexo.custodiaID" />
                        <form:hidden path="anexo.fechaCaptura" />

                        <form:hidden path="registroID" />
                        <form:hidden path="tipoRegistro" />
                        <%--
                        <form:hidden path="returnURL" />
                         
                        <script>
                          document.getElementById("returnURL").value = document.URL;
                        </script>
                        --%>

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
                                <label for="validezDocumento"><spring:message code="anexo.validezDocumento"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:select path="anexo.validezDocumento" class="chosen-select" onchange="bloquearFirma('${registro.id}','${registro.registroDetalle.id}','${param.registro}')">
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
                               <label for="tipoDocumento"><span class="text-danger">*</span> <spring:message code="anexo.tipoDocumento"/></label>
                           </div>
                           <div class="col-xs-8">
                               <form:select path="anexo.tipoDocumento" class="chosen-select">
                                   <c:forEach items="${tiposDocumentoAnexo}" var="tipoDocumento">
                                       <form:option value="${tipoDocumento}"><spring:message code="tipoDocumento.${tipoDocumento}"/></form:option>
                                   </c:forEach>
                               </form:select>
                               <form:errors path="anexo.tipoDocumento" cssClass="label label-danger"/>
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


                       <div class="form-group col-xs-10" id="divmodofirma">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                  <label><spring:message code="anexo.tipofirma"/></label>
                            </div>
                            <div class="col-xs-10" >
                                <label class="radio-inline">
                                    <form:radiobutton path="anexo.modoFirma" onclick="cambioTipoFirma();" value="1"/><spring:message code="anexo.tipofirma.attached"/>
                                </label>
                                 <label class="radio-inline">
                                    <form:radiobutton path="anexo.modoFirma" onclick="cambioTipoFirma();" value="2"/><spring:message code="anexo.tipofirma.detached"/>
                                </label>
                                <label class="radio-inline">
                                    <form:radiobutton id="sinfirma" path="anexo.modoFirma" onclick="cambioTipoFirma();" value="0"/><spring:message code="anexo.tipofirma.sinfirma"/>
                                </label>
                            </div>
                            <form:errors path="anexo.modoFirma" cssClass="label label-danger"/>

                        </div>

                        <div class="clearfix"></div>
               
			
			<ul class="nav nav-tabs" id="pestanyes">
				<li class="active"><a href="#fitxer" data-toggle="tab">Fitxer</a></li>
                <c:if test="${teScan}">
				<li><a href="#scan" data-toggle="tab">Scan</a></li>
                </c:if>
			</ul>

			  <div class="tab-content" style="padding-bottom: 0px; padding-top: 5px">
			      <div class="tab-pane active" id="fitxer">
			
                       
	                    <!--ANEXO-->
                        <div class="form-group col-xs-12" style="margin-bottom: 0px;">
    	                    <div class="form-group col-xs-6">
    	                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
    	                            <form:label path="documentoFile"><spring:message code="anexo.archivo"/></form:label>&nbsp;
    	                       </div>
    	                       <div class="col-xs-8">
    	                           <div class="input-group">
    	                               <span class="input-group-btn">
    	                                   <span class="btn btn-success btn-sm btn-file">
    	                                      <spring:message code="regweb.explorar"/>&hellip;
                                              <input id="documentoFile" name="documentoFile" type="file" multiple />
    	                                   </span>
    	                               </span>
    	                               <input type="text" class="form-control" readonly>
    	                           </div>
                                   <form:errors path="documentoFile" cssClass="help-block" element="span"/>
    	                        </div>
    	                    </div>
                            <c:if test="${not empty anexoForm.documentoCustody}">
    
    	                    <div class="form-group col-xs-6">
    	                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
    	                            <label for="documentoFile"><spring:message code="anexo.archivo.existente"/></label>
    	                        </div>
                                
    	                        <div class="col-xs-10 arxiu_actual">
    	                            <a href="<c:url value="/anexo/descargarDocumento/${anexoForm.anexo.id}" />" target="_blank">
                                    ${anexoForm.documentoCustody.name}
                                    </a>
                                    <form:checkbox id="documentoFileDelete" path="documentoFileDelete" />
                                    <spring:message code="anexo.archivo.borrar"/>
    	                        </div>
                                
    	                    </div>
                            </c:if>
                        </div>
                        
	                    <!--FIN ANEXO-->

	                    <!--FIRMA -->
                        <div class="form-group col-xs-12" style="margin-bottom: 0px;">
    	                    <div class="form-group col-xs-6" id="divInputFirma">
    	                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
    	                            <form:label path="firmaFile"><spring:message code="anexo.firma"/></form:label>
    	                       </div>
    	                       <div class="col-xs-8">
    	                           <div class="input-group">
    	                               <span class="input-group-btn">
    	                                   <span class="btn btn-success btn-sm btn-file">
    	                                      <spring:message code="regweb.explorar"/>&hellip;
                                              <input id="firmaFile" name="firmaFile" type="file" multiple />
    	                                   </span>
    	                               </span>
                                       <input type="text" class="form-control" readonly>
    	                           </div>
                                   <form:errors path="firmaFile" cssClass="help-block" element="span"/>	
    	                       </div>
    	                    </div>
    	
                            <c:if test="${not empty anexoForm.signatureCustody}">
    	                    <div id="divFirmaActual" class="form-group col-xs-6">
    	                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
    	                            <label for="firmaFile"><spring:message code="anexo.firma.existente"/></label>
    	                        </div>
    	                        <div class="col-xs-8 arxiu_actual">
    	                            <a href="<c:url value="/anexo/descargarFirma/${anexoForm.anexo.id}" />" target="_blank">
                                    ${anexoForm.signatureCustody.name}
                                    </a>                            
                                    <form:checkbox id="signatureFileDelete" path="signatureFileDelete" />
                                    <spring:message code="anexo.archivo.borrar"/>
    	                        </div>
    	                    </div>
                            </c:if>
                        </div>

	                    <!--FIN FIRMA-->

    		     	</div>
           		    <c:if test="${teScan}">
        				<div class="tab-pane" id="scan">
        					${coreScan}
        				</div>
                    </c:if>
                    

                    
    			</div>
                
               <div class="hide col-xs-12 text-center centrat" id="reload">
                        <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
               </div>
    			
    		    <div class="pull-right" style="margin-top: 15px; "> <%--  class="modal-footer" --%>
 
                    
                    <button id="desaAnnex" type="submit" class="btn btn-warning btn-sm" onclick="$('#reload').show();"><spring:message code="regweb.guardar"/></button>
                    

                    
    			</div>


</form:form>


<script type="text/javascript" >

    
    /**
     *  Oculta o muestra el bloque de firma en función de la validez del documento escogido.
     * @param idRegistro
     * @param idRegistroDetalle
     * @param tipoRegistro
     */
    function bloquearFirma(idRegistro, idRegistroDetalle, tipoRegistro) {
        var d=new Date();
        console.log(d.getMilliseconds() + " :: passa per bloquear firma ---");
    
        // parametrosAnexo(idRegistro, idRegistroDetalle, tipoRegistro);
        // Si la validez del documento es 1-> Copia no se puede adjuntar anexo con firma.
         if ($('#anexo\\.validezDocumento').val() == 1) {
           
           $('#divmodofirma').hide();
             
           $('#divFirmaActual').hide();
           $('#divInputFirma').hide();
    
           $('#sinfirma').prop("checked", "checked");
         } else {
           $('#divmodofirma').show();
    
           $('#divFirmaActual').hide();
           $('#divInputFirma').hide();
    
           cambioTipoFirma();
         }
        
    }
    
    
    
    
    function cambioTipoFirma() {
    
        console.log("-----  Entra dins cambioTipoFirma   -----");
        var autofirma = $('input[name=anexo\\.modoFirma]:radio:checked').val();
        console.log("Entra dins cambioTipoFirma: Valor = " + autofirma);
        if (autofirma == 1 || autofirma == 0) {
            $('#divInputFirma').hide();
            $('#divFirmaActual').hide();
        } else if(autofirma == 2){
            $('#divInputFirma').show();
            $('#divFirmaActual').show();
        }
    
       
    };




  
    // Muestra u Oculta el input firma en función de si es autofirma
    $('input[name=anexo\\.modoFirma]:radio').click(function () {
        cambioTipoFirma();
    });
   
    cambioTipoFirma();
   
    bloquearFirma('${registro.id}','${registro.registroDetalle.id}','${param.registro}');
   
</script>
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

<!-- Input File -->
<script>
    $(document)
            .on('change', '.btn-file :file', function() {
                var input = $(this),
                        numFiles = input.get(0).files ? input.get(0).files.length : 1,
                        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
                input.trigger('fileselect', [numFiles, label]);
            });
    $(document).ready( function() {
        $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
            var input = $(this).parents('.input-group').find(':text'),
                    log = numFiles > 1 ? numFiles + ' files selected' : label;
            if( input.length ) {
                input.val(log);
            } else {
                if( log ) alert(log);
            }
        });
    });
</script>

<!-- COLOR MENU -->
<script>
    $(document).ready(function() {
        $(function () {
            if(${entidadActiva != null}){
                $('.navbar-header').css('background-color','#${entidadActiva.colorMenu}');
                $('.navbar-nav > li > a').css('background-color','#${entidadActiva.colorMenu}');
            }
        });
    });
</script>

<!-- FI JAVASCRIPT INCLOS DEL PEU -->


</body>

</html>
