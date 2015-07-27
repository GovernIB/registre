<%@ page import="es.caib.regweb3.utils.RegwebConstantes" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>
<!DOCTYPE html>
<html lang="ca">
<head>
    <title>
        <c:if test="${not empty registroEntrada.id}"><spring:message code="registroEntrada.editar"/> ${registroEntrada.numeroRegistroFormateado}</c:if>
        <c:if test="${empty registroEntrada.id}"><spring:message code="registroEntrada.nuevo"/></c:if>
    </title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

            <!-- Miga de pan -->
           <div class="row">
                <div class="col-xs-12">
                    <ol class="breadcrumb">
                        <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                        <li class="active"><i class="fa fa-pencil-square-o"></i>
                            <c:if test="${not empty registroEntrada.id}"><spring:message code="registroEntrada.editar"/> ${registroEntrada.numeroRegistroFormateado}</c:if>
                            <c:if test="${empty registroEntrada.id}"><spring:message code="registroEntrada.nuevo"/></c:if>
                        </li>
                    </ol>
                </div>
           </div><!-- Fin miga de pan -->

            <div id="mensajes"></div>

            <div class="row">
                <div class="col-xs-12">

                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <c:if test="${empty registroEntrada.id}"><a data-toggle="modal" role="button" href="#modalSelectRepro" class="btn btn-info btn-xs pull-right margin-left10" onclick="cargarRepros('<c:url value="/repro/obtenerRepros"/>','${usuario.id}','<%=RegwebConstantes.REGISTRO_ENTRADA%>')"><span class="fa fa-refresh"></span> <spring:message code="repro.select"/></a></c:if>
                            <a data-toggle="modal" role="button" href="#modalNewRepro" class="btn btn-info btn-xs pull-right" onclick="preparaFormularioRepro('<%=RegwebConstantes.REGISTRO_ENTRADA%>')"><span class="fa fa-plus"></span> <spring:message code="repro.nuevo"/></a>
                            <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                <strong>
                                    <c:if test="${not empty registroEntrada.id}"><spring:message code="registroEntrada.editar"/> ${registroEntrada.numeroRegistroFormateado}</c:if>
                                    <c:if test="${empty registroEntrada.id}"><spring:message code="registroEntrada.nuevo"/></c:if>
                                </strong>
                            </h3>
                        </div>
                        <div class="panel-body">
                            <div class="col-xs-12"><strong>${entidad.nombre}</strong></div>
                            <div class="col-xs-12"><strong>${oficina.denominacion}</strong></div>
                            <div class="form-group col-xs-12">
                                <strong>
                                    <c:if test="${not empty registroEntrada.id}"> <fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></c:if>
                                    <c:if test="${empty registroEntrada.id}"><c:set var="now" value="<%=new java.util.Date()%>" /> <fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/></c:if>
                                </strong>
                            </div>
                            <div class="col-xs-12"><strong>${usuario.nombreCompleto} (${usuario.email})</strong></div>
                        </div>
                    </div>

                </div>
            </div>


           <div class="row">
               <form:form modelAttribute="registroEntrada" method="post" cssClass="form-horizontal">

               <div class="col-xs-6">

                   <div class="panel panel-info">

                       <div class="panel-heading">
                           <h3 class="panel-title"><i class="fa fa-pencil-square"></i> <strong><spring:message code="registro.datos.obligatorios"/></strong></h3>
                       </div>

                       <div class="panel-body">

                           <div class="form-group col-xs-12">
                               <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                                   <label for="libro.id" rel="ayuda" data-content="<spring:message code="registro.ayuda.libro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.libro"/></label>
                               </div>
                               <div class="col-xs-10">
                                   <form:select path="libro.id" items="${libros}" itemValue="id" itemLabel="nombreCompleto" cssClass="chosen-select"/> <form:errors path="libro.id" cssClass="help-block" element="span"/>
                               </div>
                           </div>

                           <div class="form-group col-xs-12">
                               <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                                   <label for="registroDetalle.extracto" rel="ayuda" data-content="<spring:message code="registro.ayuda.extracto.entrada"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.extracto"/></label>
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
                                   <label for="destino.codigo" rel="ayuda" data-content="<spring:message code="registro.ayuda.destino"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.organismoDestino"/></label>
                               </div>
                               <div class="col-xs-8">
                                   <form:select path="destino.codigo" cssClass="chosen-select">
                                       <c:forEach items="${organismosOficinaActiva}" var="organismo">
                                           <c:if test="${registroEntrada.destino.codigo != null}">
                                               <option value="${organismo.codigo}" <c:if test="${registroEntrada.destino.codigo == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                           </c:if>
                                           <c:if test="${registroEntrada.destino.codigo == null}">
                                               <option value="${organismo.codigo}" <c:if test="${registroEntrada.destinoExternoCodigo == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                           </c:if>

                                       </c:forEach>
                                   </form:select>
                                   <form:errors path="destino.codigo" cssClass="help-block" element="span"/>
                               </div>


                               <div class="col-xs-2 boto-panel">
                                  <a data-toggle="modal" role="button" href="#modalBuscadorOrganismoDestino" onclick="inicializarBuscador('#codNivelAdministracionOrganismoDestino','#codComunidadAutonomaOrganismoDestino','${oficina.organismoResponsable.nivelAdministracion.codigoNivelAdministracion}', '${oficina.organismoResponsable.codAmbComunidad.codigoComunidad}', 'OrganismoDestino');" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                               </div>
                               <c:if test="${empty registroEntrada.destinoExternoCodigo}"><!-- Si es interno -->
                                   <form:hidden path="destino.denominacion"/>
                               </c:if>
                               <c:if test="${not empty registroEntrada.destinoExternoCodigo}"><!-- Si es externo -->
                                   <form:hidden path="destinoExternoDenominacion"/>
                               </c:if>
                               <!-- Fin de gestión de organismo destino -->
                           </div>

                           <div class="form-group col-xs-12">
                               <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                                   <label for="registroDetalle.tipoAsunto.id" rel="ayuda" data-content="<spring:message code="registro.ayuda.tipoAsunto"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.tipoAsunto"/></label>
                               </div>
                               <div class="col-xs-10">

                                   <form:select path="registroDetalle.tipoAsunto.id"  cssClass="chosen-select" onchange="actualizarCodigosAsunto(this)">
                                       <form:option value="-1">...</form:option>
                                       <form:options items="${tiposAsunto}" itemValue="id" itemLabel="traduccion.nombre"/>
                                   </form:select>
                                   <form:errors path="registroDetalle.tipoAsunto" cssClass="help-block" element="span"/>
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


                   <div class="panel panel-info">

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
                                               <c:if test="${registroEntrada.registroDetalle.oficinaOrigen.codigo != null}">
                                                   <option value="${oficina.codigo}" <c:if test="${registroEntrada.registroDetalle.oficinaOrigen.codigo == oficina.codigo}">selected="selected"</c:if>>${oficina.denominacion}</option>
                                               </c:if>
                                               <c:if test="${registroEntrada.registroDetalle.oficinaOrigen.codigo == null}">
                                                   <option value="${oficina.codigo}" <c:if test="${registroEntrada.registroDetalle.oficinaOrigenExternoCodigo == oficina.codigo}">selected="selected"</c:if>>${oficina.denominacion}</option>
                                               </c:if>

                                           </c:forEach>
                                       </form:select>
                                       <form:errors path="registroDetalle.oficinaOrigen.codigo" cssClass="help-block" element="span"/>
                                   </div>

                               <div class="col-xs-2 boto-panel">
                                      <a data-toggle="modal" role="button" href="#modalBuscadorOficinaOrigen" onclick="inicializarBuscador('#codNivelAdministracionOficinaOrigen','#codComunidadAutonomaOficinaOrigen','${oficina.organismoResponsable.nivelAdministracion.codigoNivelAdministracion}', '${oficina.organismoResponsable.codAmbComunidad.codigoComunidad}','OficinaOrigen' );" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                               </div>
                               <c:if test="${empty registroEntrada.registroDetalle.oficinaOrigenExternoCodigo}"><!-- Si es interno -->
                                   <form:hidden path="registroDetalle.oficinaOrigen.denominacion"/>
                               </c:if>
                                <c:if test="${not empty registroEntrada.registroDetalle.oficinaOrigenExternoCodigo}"><!-- Si es externo -->
                                   <form:hidden path="registroDetalle.oficinaOrigenExternoDenominacion"/>
                               </c:if>
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
               <c:if test="${empty registroEntrada.id || registroEntrada.estado == RegwebConstantes.ESTADO_PENDIENTE}">
                   <c:set var="registro" value="${registroEntrada}"/>
                   <c:import url="../registro/interesados.jsp">
                       <c:param name="tipo" value="nuevo"/>
                       <c:param name="tipoRegistro" value="entrada"/>
                       <c:param name="comunidad" value="${comunidad.codigoComunidad}"/>
                   </c:import>
               </c:if>

               <!-- Botonera -->
               <div class="col-xs-12">
                   <button type="button" class="btn btn-warning btn-sm" onclick="doForm('#registroEntrada')">
                       <c:if test="${empty registroEntrada.id}">
                           <spring:message code="regweb.registrar"/>
                       </c:if>
                       <c:if test="${not empty registroEntrada.id}">
                           <spring:message code="regweb.actualizar"/>
                       </c:if>
                   </button>

                   <button type="button" onclick="goTo('<c:url value="/registroEntrada/list"/>')" class="btn btn-sm"><spring:message code="regweb.cancelar"/></button>
               </div>

           </div>

            <!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de organismos Destino
                 Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
            <c:import url="../registro/buscadorOrganismosOficinasRE.jsp">
                <c:param name="tipo" value="OrganismoDestino"/>
            </c:import>

            <!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
                 Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
            <c:import url="../registro/buscadorOrganismosOficinasRE.jsp">
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
  <%-- traduccions para busquedaorganismo.js--%>
  var tradorganismo = new Array();
  tradorganismo['organismo.denominacion'] = "<spring:message code='organismo.denominacion' javaScriptEscape='true' />";
  tradorganismo['regweb3.acciones'] = "<spring:message code='regweb.acciones' javaScriptEscape='true' />";
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
  tradsinteresado['persona.razonSocial'] = "<spring:message code='persona.razonSocial' javaScriptEscape='true' />";
  tradsinteresado['interesado.resultados'] = "<spring:message code='interesado.resultados' javaScriptEscape='true' />";
  tradsinteresado['representante.eliminar'] = "<spring:message code='representante.eliminar' javaScriptEscape='true' />";
  tradsinteresado['regweb3.confirmar'] = "<spring:message code='regweb.confirmar' javaScriptEscape='true' />";
  tradsinteresado['usuario.apellido1'] = "<spring:message code='usuario.apellido1' javaScriptEscape='true' />";
</script>

<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/interesados.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/representantes.js"/>"></script>


<script type="text/javascript" >

    $(window).load(function() {

        // Obtenemos el valor que tiene que marcar como selected. Puede ser organismo interno o externo.
        // Cargamos el select de organismo destino.
       <%-- <c:if test="${registroEntrada.destino.codigo != null}">
            actualizarOrganismosLibro('${registroEntrada.destino.codigo}', '#destino\\.codigo');
        </c:if>
        <c:if test="${registroEntrada.destino.codigo == null}">
             actualizarOrganismosLibro('${registroEntrada.destinoExternoCodigo}', '#destino\\.codigo');
        </c:if>

        // Cargamos el select de organismo en interesados
         actualizarOrganismosLibro('', '#organismoInteresado');--%>

         actualizarCodigosAsunto();

         <c:if test="${empty registroEntrada.id}"> // Localidades para nuevos Interesados
            actualizarLocalidad();
        </c:if>

        // CARGA DE INTERESADOS REGISTRO ENTRADA DESDE LA SESION
        <c:if test="${registroEntrada.estado != RegwebConstantes.ESTADO_PENDIENTE}">
            <c:import url="../registro/addInteresadosSesion.jsp">
                <c:param name="variable" value="${RegwebConstantes.SESSION_INTERESADOS_ENTRADA}"/>
            </c:import>
        </c:if>


    });

    function actualizarCodigosAsunto(){
        <c:url var="codigosAsunto" value="/registroEntrada/obtenerCodigosAsunto" />
        actualizarSelectTraduccion('${codigosAsunto}','#registroDetalle\\.codigoAsunto\\.id',$('#registroDetalle\\.tipoAsunto\\.id option:selected').val(),'${registroEntrada.registroDetalle.codigoAsunto.id}',true);
    }

    function actualizarLocalidad(){
        <c:url var="obtenerLocalidades" value="/registroEntrada/obtenerLocalidades" />
        actualizarSelect('${obtenerLocalidades}','#localidad\\.id',$('#provincia\\.id option:selected').val(),$('#localidad\\.id option:selected').val(),false);
    }



   <%-- function actualizarOrganismosLibro(valorSelected, idSelect){
        <c:url var="obtenerOrganismosLibro" value="/registroEntrada/obtenerOrganismoLibro" />

        actualizarSelect2('${obtenerOrganismosLibro}',idSelect,$('#libro\\.id option:selected').val(),valorSelected,false);
    }--%>


    window.onbeforeunload = OnBeforeUnLoad;
    function OnBeforeUnLoad () {
        return "<spring:message code="regweb.cerrar.pagina"/>";
    }

</script>

<script type="text/javascript" src="<c:url value="/js/repro.js"/>"></script>


</body>
</html>