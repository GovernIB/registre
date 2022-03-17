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
<form:form id="anexoForm" action="${pageContext.request.contextPath}/anexoScan/transforma" modelAttribute="anexoForm" method="POST"  enctype="multipart/form-data">

    <form:hidden path="anexo.id" />
    <form:hidden path="idRegistroDetalle" />
    <form:hidden path="anexo.custodiaID" />
    <form:hidden path="anexo.fechaCaptura" />

    <form:hidden path="registroID" />
    <form:hidden path="tipoRegistro" />
    <form:hidden path="oficioRemisionSir" />

    <div class="clearfix"></div>

    <c:if test="${teScan}">
        <div class="col-xs-12">
            <div class="tab-pane" id="scan">
                <iframe src="${urlToPluginWebPage}" id="myiframe" scrolling="yes" class="iframeScan">
                    <div>NO IFRAME</div>
                </iframe>
            </div>
        </div>
    </c:if>

    <div class="hide col-xs-12 text-center centrat" id="reload">
        <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
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
            if(${loginInfo.entidadActiva != null}){
                $('.navbar-header').css('background-color','#${loginInfo.entidadActiva.colorMenu}');
                $('.navbar-nav > li > a').css('background-color','#${loginInfo.entidadActiva.colorMenu}');
            }
        });
    });
</script>



<!-- FI JAVASCRIPT INCLOS DEL PEU -->
<script type="text/javascript">

var lastSize = 0;

function checkIframeSize() {
    
    var log = true;
    
    if (log) {
        console.log(" checkIFrameScanSize():: ENTRA ");
    }

    setTimeout(checkIframeSize, 5000);

    var iframe = document.getElementById('myiframe');

    var iframeDocument = iframe.contentDocument || iframe.contentWindow.document;

    var h1 = $(iframeDocument.body).height();
    var h2 = iframeDocument.body.scrollHeight;
    

    var h = Math.max(h1,h2);

    

    var d = new Date();
    if (log) {
        console.log(" checkIFrameScanSize() ======= " + d + " (H = " + h +" | H1= " + h1 + " | H2= " + h2 + ") ===================");
    }

    if (h != lastSize) {
        h = h + 300;
        lastSize = h;
        if (log) {
          console.log(" checkIFrameScanSize()::iframeDocument.body.scrollHeight = " + iframeDocument.body.scrollHeight);
          console.log(" checkIFrameScanSize()::$(iframeDocument.body).height() = " + $(iframeDocument.body).height());
          console.log(" checkIFrameScanSize():: SET " + h);
        }
        //document.getElementById('myiframe').style.height=h + "px";
        document.getElementById('myiframe').height= h + "px";
        if (log) {
          console.log(" checkIFrameScanSize():: GET HEIGHT IFRAME " + document.getElementById('myiframe').height);
        }
        
        lastSize =  Math.max($(iframeDocument.body).height(),iframeDocument.body.scrollHeight); <%--  $("#tablefull").height() --%>
        if (log) {
          console.log(" checkIFrameScanSize():: GET HEIGHT CONTENT " + lastSize);
        }
    }
}

$(document).ready(function ()  {
    // XYZ ZZZ setTimeout(checkIframeSize, 3000);
  });

</script>


</body>

</html>
