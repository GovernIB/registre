<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>


</head>

<body style="background-image:none !important;">

<c:if test="${not empty closeAndReload}">

    <script type="text/javascript">

        parent.closeAndReload();

    </script>


</c:if>

<c:if test="${empty closeAndReload}">

    <c:import url="../modulos/mensajes.jsp"/>

    <%-- Formulario que contiene el resto de campos del anexo. --%>
    <form:form id="anexoForm" action="${pageContext.request.contextPath}/anexoFichero/ficheros"
               modelAttribute="anexoForm" method="POST" enctype="multipart/form-data" cssClass="form-horizontal">

        <form:hidden path="anexo.id"/>
        <form:hidden path="anexo.registroDetalle.id"/>
        <form:hidden path="anexo.custodiaID"/>
        <form:hidden path="anexo.fechaCaptura"/>
        <form:hidden path="registroID"/>
        <form:hidden path="tipoRegistro"/>
        <form:hidden path="oficioRemisionSir"/>


        <div class="form-group col-xs-12" id="divmodofirma">
            <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                <label for="anexo.modoFirma" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoDocumento"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="anexo.tipoDocumento"/></label>
            </div>
            <div class="col-xs-10 radioButton">
                <label class="radio">
                    <form:radiobutton id="sinfirma" path="anexo.modoFirma" onclick="cambioTipoFirma()" value="0"/><span class="text12"> <spring:message code="anexo.tipofirma.sinfirma"/></span>
                </label>
                <label class="radio">
                    <form:radiobutton path="anexo.modoFirma" onclick="cambioTipoFirma()" value="1"/><span class="text12"> <spring:message code="anexo.tipofirma.attached"/></span>
                </label>
                <c:if test="${anexoForm.permitirAnexoDetached}">
                    <label class="radio">
                            <form:radiobutton path="anexo.modoFirma" onclick="cambioTipoFirma()" value="2"/><span class="text12"> <spring:message code="anexo.tipofirma.detached"/></span>
                    </label>
                </c:if>
            </div>
            <form:errors path="anexo.modoFirma" cssClass="label label-danger"/>

        </div>

        <!--ANEXO-->
        <div class="form-group col-xs-12" id="divInputArchivo">
                <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                    <label for="documentoFile" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.documento"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="anexo.archivo"/></label>
                </div>
                <div class="col-xs-6">
                    <div class="input-group">
                           <span class="input-group-btn">
                               <span class="btn btn-success btn-sm btn-file">
                                  <spring:message code="regweb.explorar"/>&hellip;
                                  <input id="documentoFile" name="documentoFile" type="file" multiple/>
                               </span>
                           </span>
                        <input type="text" id="documentFileText" class="form-control" readonly>
                    </div>
                    <form:errors path="documentoFile" cssClass="label label-danger" element="span"/>
                </div>
                <div class="col-xs-2">
                    <button type="button" class="close centrat-float-left" onClick="borrarCampo('documentFile');">×</button>
                </div>
        </div>

        <!--FIN ANEXO-->

        <!--FIRMA -->
        <div class="form-group col-xs-12" id="divInputFirma">
            <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                <label for="firmaFile" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.firma"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="anexo.firma"/></label>
            </div>
            <div class="col-xs-6">
                <div class="input-group">
                       <span class="input-group-btn">
                           <span class="btn btn-success btn-sm btn-file">
                              <spring:message code="regweb.explorar"/>&hellip;
                              <input id="firmaFile" name="firmaFile" type="file" multiple/>
                           </span>
                       </span>
                    <input type="text" id="firmaFileText" class="form-control" readonly>
                </div>
                <form:errors path="firmaFile" cssClass="label label-danger" element="span"/>
            </div>
            <div class="col-xs-2">
                <button type="button" class="close centrat-float-left" onClick="borrarCampo('firmaFile');">×</button>
            </div>
        </div>

        <!--FIN FIRMA-->


        <div class="hide col-xs-12 text-center centrat" id="reload">
            <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
        </div>

        <div class="pull-right"> <%--  class="modal-footer" --%>

            <button id="desaAnnex" type="submit" class="btn btn-warning btn-sm" onclick="eliminarErrors()"><spring:message
                    code="regweb.guardar"/></button>

        </div>


    </form:form>


</c:if>


<!-- INICI JAVASCRIPT INCLOS DEL PEU -->
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- JavaScript -->
<script type="text/javascript" src="<c:url value="/js/bootstrap.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/bootstrap-dropdown-on-hover-plugin.js"/>"></script>
<!-- Upload file jquery -->
<script type="text/javascript" src="<c:url value="/js/jquery.form.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/anexos.js"/>"></script>

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

<!-- Input File -->
<script>
    $(document)
        .on('change', '.btn-file :file', function () {
            var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            input.trigger('fileselect', [numFiles, label]);
        });
    $(document).ready(function () {
        $('.btn-file :file').on('fileselect', function (event, numFiles, label) {
            var input = $(this).parents('.input-group').find(':text'),
                log = numFiles > 1 ? numFiles + ' files selected' : label;
            if (input.length) {
                input.val(log);
            } else {
                if (log) alert(log);
            }
        });
    });
</script>

<!-- COLOR MENU -->
<script>
    $(document).ready(function () {
        $(function () {
            if (${loginInfo.entidadActiva != null}) {
                $('.navbar-header').css('background-color', '#${loginInfo.entidadActiva.colorMenu}');
                $('.navbar-nav > li > a').css('background-color', '#${loginInfo.entidadActiva.colorMenu}');
            }
        });
    });
</script>

<script type="text/javascript">


    function initAnexoForm(){

        console.log("-----  Entra dins initAnexoForm   -----");
        var autofirma = $('input[name=anexo\\.modoFirma]:radio:checked').val();

        console.log("Entra dins initAnexoForm: Valor = " + autofirma);
        if (!autofirma) {
            autofirma = 0;
        }


        switch (autofirma) {
            case '0': <%--doc sense firma --%>

                $('#divInputArchivo').show();
                $('#divInputFirma').hide();
                borrarCampo('firmaFile');

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
                    borrarCampo('documentoFile');

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

    }

    initAnexoForm();


    function cambioTipoFirma() {
        quitarMensajeError();
        limpiarAnexoForm();
        initAnexoForm();
        parent.redimensionaModalAnnexeErrors();
    }


    function borrarCampo(campo) {
        $('#' + campo).val("");
        $('#' + campo + 'Text').val("");
    }



    <%-- Redimensiona el modal si hi ha errors --%>
    $(document).ready(function () {
        $(function () {

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
                iframe = iframe + incrementError;
                parent.$('#targetiframe').height(iframe);
                modal = modal + incrementError;
                parent.$('#modalAnexos').find('.modal-content').height(modal);
            }

            if($('.alert-success').length != 0){
                iframe = iframe + incrementError;
                parent.$('#targetiframe').height(iframe);
                modal = modal + incrementError;
                parent.$('#modalAnexos').find('.modal-content').height(modal);
            }

            var firmaFile = $('#divInputFirma').find('span.help-block').html();
            if(firmaFile!=null){
                iframe = iframe + incrementError;
                parent.$('#targetiframe').height(iframe);
                modal = modal + incrementError;
                parent.$('#modalAnexos').find('.modal-content').height(modal);
            }

            var documentFile = $('#divInputArchivo').find('span.help-block').html();
            if(documentFile!=null){
                iframe = iframe + incrementError;
                parent.$('#targetiframe').height(iframe);
                modal = modal + incrementError;
                parent.$('#modalAnexos').find('.modal-content').height(modal);
            }
        });
    });

</script>


<!-- FI JAVASCRIPT INCLOS DEL PEU -->


</body>

</html>
