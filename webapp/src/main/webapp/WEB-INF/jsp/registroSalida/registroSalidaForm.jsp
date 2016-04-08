<%@ page import="es.caib.regweb3.utils.RegwebConstantes" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title>
        <c:if test="${not empty registroSalida.id}"><spring:message code="registroSalida.editar"/> ${registroSalida.numeroRegistroFormateado}</c:if>
        <c:if test="${empty registroSalida.id}"><spring:message code="registroSalida.nuevo"/></c:if>
    </title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body onbeforeunload="return cerrar('<spring:message code="regweb.cerrar.pagina"/>')">

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${not empty registroSalida.id}"><spring:message code="registroSalida.editar"/> ${registroSalida.numeroRegistroFormateado}</c:if>
                        <c:if test="${empty registroSalida.id}"><spring:message code="registroSalida.nuevo"/></c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div id="mensajes"></div>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-danger">
                    <div class="panel-heading">
                        <c:if test="${empty registroSalida.id}"><a data-toggle="modal" role="button" href="#modalSelectRepro" class="btn btn-danger btn-xs pull-right margin-left10" onclick="cargarRepros('<c:url value="/repro/obtenerRepros"/>','${usuario.id}','<%=RegwebConstantes.REGISTRO_SALIDA%>')"><span class="fa fa-refresh"></span> <spring:message code="repro.select"/></a></c:if>
                        <a data-toggle="modal" role="button" href="#modalNewRepro" class="btn btn-danger btn-xs pull-right" onclick="preparaFormularioRepro('<%=RegwebConstantes.REGISTRO_SALIDA%>')"><span class="fa fa-plus"></span> <spring:message code="repro.nuevo"/></a>
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong>
                                <c:if test="${not empty registroSalida.id}"><spring:message code="registroSalida.editar"/> ${registroSalida.numeroRegistroFormateado}</c:if>
                                <c:if test="${empty registroSalida.id}"><spring:message code="registroSalida.nuevo"/></c:if>
                            </strong>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <div class="col-xs-12"><strong>${entidad.nombre}</strong></div>
                        <div class="col-xs-12"><strong>${registroSalida.oficina.denominacion}</strong></div>
                        <div class="form-group col-xs-12">
                            <strong>
                                <c:if test="${not empty registroSalida.id}"> <fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></c:if>
                                <c:if test="${empty registroSalida.id}"><c:set var="now" value="<%=new java.util.Date()%>" /> <fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/></c:if>
                            </strong>
                        </div>
                        <div class="col-xs-12"><strong>${usuario.nombreCompleto} (${usuario.email})</strong></div>
                    </div>
                </div>

            </div>
        </div>


    <div class="row">
        <form:form modelAttribute="registroSalida" method="post" cssClass="form-horizontal">

        <div class="col-xs-6">

            <div class="panel panel-danger">

                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-pencil-square"></i> <strong><spring:message code="registro.datos.obligatorios"/></strong></h3>
                </div>

                <div class="panel-body">

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="libro.id" rel="ayuda" data-content="<spring:message code="registro.ayuda.libro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.libro"/></label>
                        </div>
                        <div class="col-xs-10">
                            <c:if test="${empty registroSalida.id}">
                                <form:select path="libro.id" items="${libros}" itemValue="id" itemLabel="nombreCompleto"
                                             cssClass="chosen-select"/> <form:errors path="libro.id"
                                                                                     cssClass="help-block"
                                                                                     element="span"/>
                            </c:if>

                            <c:if test="${not empty registroSalida.id}">
                                <form:select path="libro.id" class="chosen-select">
                                    <form:option
                                            value="${registroSalida.libro.id}">${registroSalida.libro.nombreCompleto}</form:option>
                                </form:select>
                            </c:if>
                        </div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.extracto" rel="ayuda" data-content="<spring:message code="registro.ayuda.extracto.salida"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.extracto"/></label>
                        </div>
                        <div class="col-xs-10">
                            <form:textarea path="registroDetalle.extracto" rows="2" cssClass="form-control" maxlength="240"/> <form:errors path="registroDetalle.extracto" cssClass="help-block" element="span"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.tipoDocumentacionFisica" rel="ayuda" data-content="<spring:message code="registro.ayuda.docFisica"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.documentacionFisica"/></label>
                        </div>
                        <div class="col-xs-10">

                            <form:select path="registroDetalle.tipoDocumentacionFisica" cssClass="chosen-select"> 
                              <c:forEach items="${tiposDocumentacionFisica}" var="tipoDocFisica">
                                 <form:option value="${tipoDocFisica}"><spring:message code="tipoDocumentacionFisica.${tipoDocFisica}" /></form:option>
                              </c:forEach>
                           </form:select>
                           <form:errors path="registroDetalle.tipoDocumentacionFisica" cssClass="help-block" element="span"/>
                            
                        </div>
                    </div>

                    <div class="form-group col-xs-12">

                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="origen.codigo" rel="ayuda" data-content="<spring:message code="registro.ayuda.origen"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroSalida.organismoOrigen"/></label>
                        </div>
                        <div class="col-xs-10">
                            <form:select path="origen.codigo" cssClass="chosen-select">
                                <c:forEach items="${organismosOficinaActiva}" var="organismo">
                                    <c:if test="${registroSalida.origen.codigo != null}">
                                        <option value="${organismo.codigo}" <c:if test="${registroSalida.origen.codigo == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                    </c:if>
                                    <c:if test="${registroSalida.origen.codigo == null}">
                                        <option value="${organismo.codigo}" <c:if test="${registroSalida.origenExternoCodigo == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                    </c:if>

                                </c:forEach>
                            </form:select>
                            <form:errors path="origen.codigo" cssClass="help-block" element="span"/>
                        </div>

                        <form:hidden path="origen.denominacion"/>

                        <!-- Fin de gestión de organismo origen -->
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.tipoAsunto.id" rel="ayuda" data-content="<spring:message code="registro.ayuda.tipoAsunto"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.tipoAsunto"/></label>
                        </div>
                        <div class="col-xs-10">

                            <form:select path="registroDetalle.tipoAsunto.id"  cssClass="chosen-select" onchange="actualizarCodigosAsunto(this)">
                                <form:option value="-1">...</form:option>
                                <form:options items="${tiposAsunto}" itemValue="id"
                                              itemLabel="traducciones['${pageContext.response.locale}'].nombre"/>
                            </form:select>
                            <form:errors path="registroDetalle.tipoAsunto.id" cssClass="help-block" element="span"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.idioma" rel="ayuda" data-content="<spring:message code="registro.ayuda.idioma"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.idioma"/></label>
                        </div>
                        <div class="col-xs-10">
                            <form:select path="registroDetalle.idioma" cssClass="chosen-select">
                            <c:forEach items="${idiomas}" var="idioma">
                               <form:option value="${idioma}"><spring:message code="idioma.${idioma}"/></form:option>
                            </c:forEach>
                           </form:select>
                            <form:errors path="registroDetalle.idioma" cssClass="help-block" element="span"/>
                        </div>
                    </div>

                </div>
            </div>
        </div>


        <!-- Datos Opcionales -->
        <div class="col-xs-6">


            <div class="panel panel-danger">

                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="registro.datos.opcionales"/></strong></h3>
                </div>

                <div class="panel-body">

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.codigoAsunto.id" rel="ayuda" data-content="<spring:message code="registro.ayuda.codigoAsunto"/>" data-toggle="popover"><spring:message code="registroEntrada.codigoAsunto"/></label>
                        </div>
                        <div class="col-xs-10">
                            <form:select path="registroDetalle.codigoAsunto.id" disabled="true" cssClass="chosen-select"/> <form:errors path="registroDetalle.codigoAsunto.id" cssClass="help-block" element="span"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.referenciaExterna" rel="ayuda" data-content="<spring:message code="registro.ayuda.refExterna"/>" data-toggle="popover"><spring:message code="registroEntrada.referenciaExterna"/></label>
                        </div>
                        <div class="col-xs-4">
                            <form:input path="registroDetalle.referenciaExterna" maxlength="16" cssClass="form-control"/> <form:errors path="registroDetalle.referenciaExterna" cssClass="help-block" element="span"/>
                        </div>

                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.expediente" rel="ayuda" data-content="<spring:message code="registro.ayuda.expediente"/>" data-toggle="popover"><spring:message code="registroEntrada.expediente"/></label>
                        </div>
                        <div class="col-xs-4">
                            <form:input path="registroDetalle.expediente" maxlength="80" cssClass="form-control"/> <form:errors path="registroDetalle.expediente" cssClass="help-block" element="span"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.transporte" rel="ayuda" data-content="<spring:message code="registro.ayuda.transporte"/>" data-toggle="popover"><spring:message code="registroEntrada.transporte"/></label>
                        </div>
                        <div class="col-xs-5">
                           <form:select path="registroDetalle.transporte" cssClass="chosen-select"> 
                              <form:option value="-1">...</form:option>
                              <c:forEach items="${transportes}" var="transporte">
                                 <form:option value="${transporte}"><spring:message code="transporte.${transporte}" /></form:option>
                              </c:forEach>
                           </form:select>

                            <form:errors path="registroDetalle.transporte" cssClass="help-block" element="span"/>

                        </div>
                        <div class="col-xs-1 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.numeroTransporte" rel="ayuda" data-content="<spring:message code="registro.ayuda.numeroTransporte"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroTransporte"/></label>
                        </div>
                        <div class="col-xs-4">
                            <form:input path="registroDetalle.numeroTransporte" maxlength="20" cssClass="form-control"/> <form:errors path="registroDetalle.numeroTransporte" cssClass="help-block" element="span"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb12 control-label">
                            <label for="registroDetalle.observaciones" rel="ayuda" data-content="<spring:message code="registro.ayuda.observaciones"/>" data-toggle="popover"><spring:message code="registroEntrada.observaciones"/></label>
                        </div>
                        <div class="col-xs-10">
                            <form:input path="registroDetalle.observaciones" cssClass="form-control" maxlength="50"/> <form:errors path="registroDetalle.observaciones" cssClass="help-block" element="span"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-12">

                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.oficinaOrigen.codigo" rel="ayuda" data-content="<spring:message code="registro.ayuda.origen"/>" data-toggle="popover"><spring:message code="registroEntrada.oficinaOrigen"/></label>
                        </div>
                        <div class="col-xs-8">
                            <form:select path="registroDetalle.oficinaOrigen.codigo" cssClass="chosen-select">
                                <option value="-1">...</option>
                                <c:forEach items="${oficinasOrigen}" var="oficina">
                                    <c:if test="${registroSalida.registroDetalle.oficinaOrigen.codigo != null}">
                                        <option value="${oficina.codigo}" <c:if test="${registroSalida.registroDetalle.oficinaOrigen.codigo == oficina.codigo}">selected="selected"</c:if>>${oficina.denominacion}</option>
                                    </c:if>
                                    <c:if test="${registroSalida.registroDetalle.oficinaOrigen.codigo == null}">
                                        <option value="${oficina.codigo}" <c:if test="${registroSalida.registroDetalle.oficinaOrigenExternoCodigo == oficina.codigo}">selected="selected"</c:if>>${oficina.denominacion}</option>
                                    </c:if>

                                </c:forEach>
                            </form:select>
                            <form:errors path="registroDetalle.oficinaOrigen.codigo" cssClass="help-block" element="span"/>
                        </div>

                        <div class="col-xs-2 boto-panel">
                            <a data-toggle="modal" role="button" href="#modalBuscadorOficinaOrigen"
                               onclick="inicializarBuscador('#codNivelAdministracionOficinaOrigen','#codComunidadAutonomaOficinaOrigen','#provinciaOficinaOrigen','#localidadOficinaOrigen','${oficina.organismoResponsable.nivelAdministracion.codigoNivelAdministracion}', '${oficina.organismoResponsable.codAmbComunidad.codigoComunidad}', 'OficinaOrigen' );"
                               class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                        </div>

                        <form:hidden path="registroDetalle.oficinaOrigen.denominacion"/>
                        <form:hidden path="registroDetalle.oficinaOrigenExternoDenominacion"/>

                        <!-- Fin gestión de oficina origen(se prepara en función de si es interno o externo )-->
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.numeroRegistroOrigen" rel="ayuda" data-content="<spring:message code="registro.ayuda.numRegOrigen"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroRegistroOrigen"/></label>
                        </div>
                        <div class="col-xs-8">
                            <form:input path="registroDetalle.numeroRegistroOrigen" maxlength="20" cssClass="form-control"/> <form:errors path="registroDetalle.numeroRegistroOrigen" cssClass="help-block" element="span"/>
                        </div>
                    </div>
                    <div class="form-group col-xs-6 no-pad-right">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label for="registroDetalle.fechaOrigen" rel="ayuda" data-content="<spring:message code="registro.ayuda.fecha"/>" data-toggle="popover"><spring:message code="registroEntrada.fecha"/></label>
                        </div>
                        <div class="col-xs-10 no-pad-right" id="fechaOrigen">
                            <div class="input-group date no-pad-right">
                                <form:input type="text" cssClass="form-control" path="registroDetalle.fechaOrigen" maxlength="19" placeholder="dd/mm/yyyy HH:mm:ss" name="fechaOrigen"/>
                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                            </div>
                            <form:errors path="registroDetalle.fechaOrigen" cssClass="help-block" element="span"/>
                        </div>

                    </div>

                </div>
            </div>
        </div>
    </form:form>

    <!-- INTERESADOS -->
    <c:if test="${empty registroSalida.id}">
        <c:set var="registro" value="${registroSalida}"/>
        <c:import url="../registro/interesados.jsp">
            <c:param name="tipo" value="nuevo"/>
            <c:param name="tipoRegistro" value="salida"/>
            <c:param name="comunidad" value="${comunidad.codigoComunidad}"/>
        </c:import>
    </c:if>

    <!-- Botonera -->
    <div class="col-xs-12">
        <button type="button" class="btn btn-warning btn-sm" onclick="doForm('#registroSalida')">
            <c:if test="${empty registroSalida.id}">
                <spring:message code="regweb.registrar"/>
            </c:if>
            <c:if test="${not empty registroSalida.id}">
                <spring:message code="regweb.actualizar"/>
            </c:if>
        </button>

        <button type="button" onclick="goTo('<c:url value="/registroSalida/list"/>')" class="btn btn-sm"><spring:message code="regweb.cancelar"/></button>
    </div>

    </div>

    <!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de organismos Destino
         Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
        <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
        <c:param name="tipo" value="OrganismoOrigen"/>
    </c:import>

    <!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
         Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
        <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
        <c:param name="tipo" value="OficinaOrigen"/>
    </c:import>

    <%--Nueva Repro--%>
        <c:import url="../registro/formularioRepro.jsp"/>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    var urlObtenerAnexo = '<c:url value="/anexo/obtenerAnexo"/>';
    var urlObtenerRepro = '<c:url value="/repro/obtenerRepro"/>';
</script>

<script type="text/javascript">
  <%-- Traducciones para interesados.js --%>
  var tradsinteresado = new Array();
  tradsinteresado['interesado.representante.anadir'] = "<spring:message code='interesado.representante.anadir' javaScriptEscape='true' />";
  tradsinteresado['interesado.representante.buscar'] = "<spring:message code='interesado.representante.buscar' javaScriptEscape='true' />";
  tradsinteresado['interesado.representante.editar'] = "<spring:message code='interesado.representante.editar' javaScriptEscape='true' />";
  tradsinteresado['interesado.representante.eliminar'] = "<spring:message code='interesado.representante.eliminar' javaScriptEscape='true' />";
  tradsinteresado['interesado.representante.nuevo'] = "<spring:message code='interesado.representante.nuevo' javaScriptEscape='true' />";
  tradsinteresado['interesado.personafisica.editar'] = "<spring:message code='interesado.personafisica.editar' javaScriptEscape='true' />";
  tradsinteresado['interesado.personajuridica.editar'] = "<spring:message code='interesado.personajuridica.editar' javaScriptEscape='true' />";
  tradsinteresado['interesado.noresultados.escoge'] = "<spring:message code='interesado.noresultados.escoge' javaScriptEscape='true' />";
  tradsinteresado['interesado.noresultados'] = "<spring:message code='interesado.noresultados' javaScriptEscape='true' />";
  tradsinteresado['interesado.hay'] = "<spring:message code='interesado.hay' javaScriptEscape='true' />";
  tradsinteresado['regweb3.editar'] = "<spring:message code='regweb.editar' javaScriptEscape='true' />";
  tradsinteresado['regweb3.nombre'] = "<spring:message code='regweb.nombre' javaScriptEscape='true' />";
  tradsinteresado['persona.documento'] = "<spring:message code='persona.documento' javaScriptEscape='true' />";
  tradsinteresado['persona.razonSocial'] = "<spring:message code='persona.razonSocial' javaScriptEscape='true' />";
  tradsinteresado['persona.persona'] = "<spring:message code='persona.persona' javaScriptEscape='true' />";
  tradsinteresado['persona.tipoPersona'] = "<spring:message code='persona.tipoPersona' javaScriptEscape='true' />";
  tradsinteresado['persona.razonSocial'] = "<spring:message code='persona.razonSocial' javaScriptEscape='true' />";
  tradsinteresado['interesado.resultados'] = "<spring:message code='interesado.resultados' javaScriptEscape='true' />";
  tradsinteresado['representante.eliminar'] = "<spring:message code='representante.eliminar' javaScriptEscape='true' />";
  tradsinteresado['regweb3.confirmar'] = "<spring:message code='regweb.confirmar' javaScriptEscape='true' />";
  tradsinteresado['usuario.apellido1'] = "<spring:message code='usuario.apellido1' javaScriptEscape='true' />";
  tradsinteresado['regweb.acciones'] = "<spring:message code='regweb.acciones' javaScriptEscape='true' />";
  tradsinteresado['persona.fisica'] = "<spring:message code='persona.fisica' javaScriptEscape='true' />";
  tradsinteresado['persona.juridica'] = "<spring:message code='persona.juridica' javaScriptEscape='true' />";
</script>

<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>

<script type="text/javascript" >

    $(window).load(function() {

        actualizarCodigosAsunto();


        // CARGA DE INTERESADOS REGISTRO ENTRADA DESDE LA SESION
        <c:if test="${empty registroSalida.id}">
        <c:import url="../registro/addInteresadosSesion.jsp">
        <c:param name="variable" value="${RegwebConstantes.SESSION_INTERESADOS_SALIDA}"/>
        </c:import>
        </c:if>

    });

    function actualizarCodigosAsunto(){
        <c:url var="codigosAsunto" value="/rest/obtenerCodigosAsunto" />
        actualizarSelectTraduccion('${codigosAsunto}', '#registroDetalle\\.codigoAsunto\\.id', $('#registroDetalle\\.tipoAsunto\\.id option:selected').val(), '${registroSalida.registroDetalle.codigoAsunto.id}', false, '${pageContext.response.locale}');
    }

    function actualizarLocalidad(){
        <c:url var="obtenerLocalidades" value="/rest/obtenerLocalidades" />
        actualizarSelect('${obtenerLocalidades}', '#localidad\\.id', $('#provincia\\.id option:selected').val(), $('#localidad\\.id option:selected').val(), false, false);
    }

    window.onbeforeunload = OnBeforeUnLoad;
    function OnBeforeUnLoad () {
        return "<spring:message code="regweb.cerrar.pagina"/>";
    }
</script>

<script type="text/javascript" src="<c:url value="/js/repro.js"/>"></script>


</body>
</html>