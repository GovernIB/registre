<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca" >
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
    

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
<form:form id="anexoForm" action="${pageContext.request.contextPath}/anexoFichero/ficheros" modelAttribute="anexoForm" method="POST"  enctype="multipart/form-data">
                
                        
                    <form:hidden path="anexo.id" />
                    <form:hidden path="anexo.registroDetalle.id" />
                    <form:hidden path="anexo.custodiaID" />
                    <form:hidden path="anexo.fechaCaptura" />

                    <form:hidden path="registroID" />
                    <form:hidden path="tipoRegistro" />
                    <form:hidden path="oficioRemisionSir" />



                    <div class="form-group col-xs-10" id="divmodofirma">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                              <label><spring:message code="anexo.tipofirma"/></label>
                        </div>
                        <div class="col-xs-10" >
                            <label class="radio-inline">
                                <form:radiobutton path="anexo.modoFirma" onclick="cambioTipoFirma()" value="1"/><spring:message code="anexo.tipofirma.attached"/>
                            </label>
                             <label class="radio-inline">
                                <form:radiobutton path="anexo.modoFirma" onclick="cambioTipoFirma()" value="2"/><spring:message code="anexo.tipofirma.detached"/>
                            </label>
                            <label class="radio-inline">
                                <form:radiobutton id="sinfirma"  path="anexo.modoFirma" onclick="cambioTipoFirma()" value="0"/><spring:message code="anexo.tipofirma.sinfirma"/>
                            </label>
                        </div>
                        <form:errors path="anexo.modoFirma" cssClass="label label-danger"/>

                    </div>

                    <div class="clearfix"></div>
               

                       
                    <!--ANEXO-->
                    <div class="form-group col-xs-12" style="margin-bottom: 0px;">
                        <div class="form-group col-xs-6" id="divInputArchivo">
                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                               <form:label path="documentoFile" id="labelDocumento"><spring:message
                                       code="anexo.archivo"/></form:label>&nbsp;
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
                    </div>

                    <!--FIN ANEXO-->

                    <!--FIRMA -->
                    <div class="form-group col-xs-12" style="margin-bottom: 0px;">
                        <div class="form-group col-xs-6" id="divInputFirma">
                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                               <form:label path="firmaFile" id="labelFirma"><spring:message
                                       code="anexo.firma"/></form:label>
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
                    </div>

                    <!--FIN FIRMA-->
             

                
<div class="hide col-xs-12 text-center centrat" id="reload">
        <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
</div>

<div class="pull-right" style="margin-top: 15px; "> <%--  class="modal-footer" --%>

    <button id="desaAnnex" type="submit" class="btn btn-warning btn-sm" onclick="$('#reload').show();"><spring:message code="regweb.guardar"/></button>

</div>


</form:form>


<script type="text/javascript" >

    
    function cambioTipoFirma() {
        console.log("-----  Entra dins cambioTipoFirma   -----");
        var autofirma = $('input[name=anexo\\.modoFirma]:radio:checked').val();

        console.log("Entra dins cambioTipoFirma: Valor = " + autofirma);
        if (!autofirma) {
            autofirma = 0;
        }


        switch (autofirma) {
            case '0': <%--doc sense firma --%>

                $('#divInputArchivo').show();
                $('#divInputFirma').hide();

                break;
            case '1':<%--doc amb firma adjunta (PADES)--%>

                if (${anexoForm.documentoCustody != null}) {
                    $("#labelDocumento").html("<spring:message code="anexo.tipofirma.attached"/>");
                    $('#divInputArchivo').show();
                    $('#divInputFirma').hide();

                } else {
                    $("#labelFirma").html("<spring:message code="anexo.tipofirma.attached"/>");
                    $('#divInputArchivo').hide();
                    $('#divInputFirma').show();

                }

                break;
            case '2':<%--firma amb doc separat --%>
                $("#labelFirma").html("<spring:message code="anexo.firma"/>");
                $('#divInputArchivo').show();
                $('#divInputFirma').show();

                break;
            default:
                alert("Modo de firma no suportat(" + autofirma + ")");
        }

    };


    // Muestra u Oculta el input firma en función de si es autofirma
    $('input[name=anexo\\.modoFirma]:radio').click(function () {
        cambioTipoFirma();
    });

    cambioTipoFirma();

   
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
