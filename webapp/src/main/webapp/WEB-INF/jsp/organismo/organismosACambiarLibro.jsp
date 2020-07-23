<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>


<!-- Este jsp se usa para cambiar los libros de organismo, ya sea desde el botón "Cambiar Libros" como después
de un proceso de sincronización/actualización de una entidad desde dir3caib -->

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">
        <!-- Miga de pan-->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="organismo.cambiar.libros"/></li>
                </ol>
            </div>
        </div><!-- /.row -->


       <c:import url="../modulos/mensajes.jsp"/>
        <!-- PANEL LIBROS A CAMBIAR -->


        <div class="row"> <!--row principal-->
            <div class="col-xs-12">
                <div class="panel panel-warning"> <!-- Panel principal-->
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i> <strong>${tituloPagina}</strong></h3>
                    </div>
                    <div  class="panel-body"> <!-- Panel body  principal-->
                        <div id="pendientes"></div>
                        <div class="row">
                            <div class="col-xs-12">
                               <%-- <c:if test="${empty organismosAProcesar}" >
                                    <p><spring:message code="organismos.procesados.vacio"/></p>
                                </c:if>--%>
                                <c:if test="${not empty organismosAProcesar}" >
                                   <%-- <div class="panel panel-success">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="organismo.procesar"/></strong></h3>
                                        </div>

                                        <div  class="panel-body" id="pendientes">--%>
                                           <c:if test="${tieneLibros == false}"><p><spring:message code="organismo.cambiar.libros.vacio"/></p></c:if>
                                           <c:if test="${tieneLibros == true}"><p><spring:message code="organismo.modificar.libros"/></p></c:if>
                                           <c:if test="${esPendiente}"><div id="notaimportante"><strong><p class="text-danger"> <spring:message code="organismo.notaimportante" /></p></strong></div></c:if>


                                            <c:forEach var="organismoAProcesar" items="${organismosAProcesar}">
                                                <c:if test="${not empty organismoAProcesar.libros}">
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="panel panel-warning" id="panel${organismoAProcesar.id}">
                                                                <div class="panel-heading">
                                                                    <h3 class="panel-title"><i
                                                                            class="fa fa-globe"></i><strong> <c:if
                                                                            test="${esPendiente}"><spring:message
                                                                            code="organismo.extinguido"/>:</c:if><c:if
                                                                            test="${!esPendiente}"><spring:message
                                                                            code="organismo.actual"/>:</c:if> ${organismoAProcesar.denominacion}
                                                                        (${organismoAProcesar.codigo}) ${organismoAProcesar.estado.descripcionEstadoEntidad}</strong>
                                                                    </h3>
                                                                </div>

                                                                <div  class="panel-body">
                                                                    <form id="organismoAProcesarForm${organismoAProcesar.id}" action="${pageContext.request.contextPath}/entidad/procesarlibroorganismo/${organismoAProcesar.id}/${esPendiente}" method="post" class="form-horizontal">
                                                                        <input type="hidden" id="total${organismoAProcesar.id}" value="${fn:length(organismoAProcesar.libros)}"/>
                                                                        <c:if test="${esPendiente}">
                                                                            <strong><spring:message
                                                                                    code="organismo.cambiar.ayuda"/></strong><br/><br/>
                                                                            <spring:message
                                                                                    code="organismo.historicos"/><br/>
                                                                            <c:forEach items="${organismoAProcesar.historicoUO}" var="organismoSustituye" >
                                                                                ${organismoSustituye.codigo}: ${organismoSustituye.denominacion}
                                                                                <br/>
                                                                            </c:forEach>
                                                                            <br/>
                                                                        </c:if>
                                                                        <c:forEach var="libroorganismoAProcesar" items="${organismoAProcesar.libros}" varStatus="contador">

                                                                            <div class="form-group col-xs-12">
                                                                                <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                                                                    <label for="libro${contador.count}-${organismoAProcesar.id}"><spring:message code="libro.libro"/> <em>${libroorganismoAProcesar.nombre}</em></label>
                                                                                    <input id="libro${contador.count}-${organismoAProcesar.id}" type="hidden" class="form-control" value="${libroorganismoAProcesar.id}"/>
                                                                                    <span id="libro${contador.count}-${organismoAProcesar.id}Error"></span>
                                                                                </div>
                                                                                <div class="col-xs-6">
                                                                                    <select id="organismoSustituye${contador.count}-${organismoAProcesar.id}" name="organismoSustituye${contador.count}-${organismoAProcesar.id}" class="chosen-select">
                                                                                        <option value="-1">...</option>
                                                                                        <c:if test="${esPendiente}">
                                                                                            <!-- Si es pendiente venimos del proceso de actualización y mostramos los organismos históricos que
                                                                                            sustituyen al extinguido -->
                                                                                            <c:forEach items="${organismoAProcesar.historicoUO}" var="organismoSustituye" >
                                                                                                <option value="${organismoSustituye.id}">${organismoSustituye.denominacion}</option>
                                                                                            </c:forEach>
                                                                                        </c:if>
                                                                                        <c:if test="${!esPendiente}">
                                                                                            <!-- Si no es pendiente venimos de cambiar libros de forma manual y se muestran los organismos con
                                                                                            oficinas que lo pueden sustituir -->
                                                                                            <c:forEach items="${organismosSustituyentes}" var="organismoSustituye" >
                                                                                                <option value="${organismoSustituye.id}">${organismoSustituye.denominacion}</option>
                                                                                            </c:forEach>
                                                                                        </c:if>
                                                                                    </select>
                                                                                    <span id="organismoSustituye${contador.count}-${organismoAProcesar.id}Error"></span>
                                                                                </div>
                                                                            </div>
                                                                        </c:forEach>
                                                                        <div class="form-group col-xs-6">
                                                                            <input type="button" onclick="procesarOrganismo('${organismoAProcesar.id}')" title="<spring:message code="regweb.procesar"/>" value="<spring:message code="regweb.procesar"/>" class="btn btn-warning btn-sm">
                                                                        </div>
                                                                    </form>
                                                                </div>
                                                            </div>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:forEach>

                                    <%--    </div> <!--/.panel body-->

                                    </div><!-- /.panel panel-success-->--%>
                                </c:if>
                            </div>
                            <%--Botonera--%>
                            <%--  <div class="col-xs-12">
                                  <button type="button" onclick="goTo('<c:url value="/organismo/list"/>')" class="btn btn-warning btn-sm" title="<spring:message code="regweb.volver"/>">
                                    <spring:message code="regweb.volver"/>
                                  </button>
                              </div>--%>

                            <!-- /.PANEL LIBROS A CAMBIAR -->
                        </div><!-- /.row-->


                        <!-- PANEL RESUMEN -->
                        <c:if test="${esPendiente}">
                            <c:if test="${not empty extinguidosAutomaticos}">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="panel panel-success">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-file-o"></i> <strong><spring:message code="organismos.extinguidos.resumen"/></strong></h3>
                                            </div>
                                            <div  class="panel-body" id="resumen">
                                                <!-- EXTINGUIDOS AUTOMATICOS -->
                                                <c:forEach var="extinguidoAutomatico" items="${extinguidosAutomaticos}">
                                                   <%-- <spring:message code="organismo.extinguido"/>: <strong>${extinguidoAutomatico.key}</strong>--%>
                                                    <c:set var="organismoSustituye" value="${extinguidoAutomatico.value}"/>

                                                    <table id="automaticos${extinguidoAutomatico.key}" class="table table-bordered table-hover table-striped">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="libro.libro"/></th>
                                                            <th><spring:message code="organismo.extinguido"/></th>
                                                            <th>&nbsp;&nbsp;&nbsp;</th>
                                                            <th><spring:message code="organismo.asignado"/></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="libro" items="${organismoSustituye.libros}">
                                                            <tr>
                                                                <td>${libro.nombre}</td>
                                                                <td>${extinguidoAutomatico.key}</td>
                                                                <td><span class="fa fa-arrow-right" aria-hidden="true"></span></td>
                                                                <td>${organismoSustituye.denominacion}</td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </c:forEach>

                                                <!-- LOS NO AUTOMATICOS SE MUESTRAN CON JQUERY en la funcion mostrarProcesado() en organismosaprocesar.js -->


                                            </div> <!-- panel body resumen-->
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${not empty organismosConError}">
                                <div id="organismosconerror">
                                    <strong><spring:message code="organismo.conerror"/></strong>
                                    <br/>
                                    <spring:message code="organismo.conerror.ayuda"/>
                                </div>
                                <c:forEach var="organismoConError" items="${organismosConError}">
                                    <c:if test="${not empty organismoConError.libros}">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel panel-warning" id="panel${organismoConError.id}">
                                                    <div class="panel-heading">
                                                        <h3 class="panel-title"><i class="fa fa-home"></i><strong> <c:if test="${esPendiente}"><spring:message code="organismo.extinguido"/>:</c:if><c:if test="${!esPendiente}"><spring:message code="organismo.actual"/>:</c:if> ${organismoConError.denominacion} (${organismoConError.codigo})</strong></h3>
                                                    </div>

                                                    <div  class="panel-body">
                                                        <form id="organismoAProcesarForm${organismoConError.id}" action="${pageContext.request.contextPath}/entidad/procesarlibroorganismo/${organismoConError.id}/${esPendiente}" method="post" class="form-horizontal">
                                                            <input type="hidden" id="total${organismoConError.id}" value="${fn:length(organismoConError.libros)}"/>
                                                            <c:forEach var="libroorganismoConError" items="${organismoConError.libros}" varStatus="contador">

                                                                <div class="form-group col-xs-8">
                                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                                        <label for="libro${contador.count}-${organismoConError.id}"><spring:message code="libro.libro"/> <em>${libroorganismoConError.nombre}</em></label>
                                                                        <input id="libro${contador.count}-${organismoConError.id}" type="hidden" class="form-control" value="${libroorganismoConError.id}"/>
                                                                        <span id="libro${contador.count}-${organismoConError.id}Error"></span>
                                                                    </div>
                                                                    <div class="col-xs-8">
                                                                        <select id="organismoSustituye${contador.count}-${organismoConError.id}" name="organismoSustituye${contador.count}-${organismoConError.id}" class="chosen-select">
                                                                            <option value="-1">...</option>
                                                                                <!--Como es organismo con error, quiere decir que no tiene històricos y que se le debe asignar otro organismo -->
                                                                                <c:forEach items="${organismosSustituyentes}" var="organismoSustituye" >
                                                                                    <option value="${organismoSustituye.id}">${organismoSustituye.denominacion}</option>
                                                                                </c:forEach>
                                                                        </select>
                                                                        <span id="organismoSustituye${contador.count}-${organismoConError.id}Error"></span>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>
                                                            <div class="form-group col-xs-6">
                                                                <input type="button" onclick="procesarOrganismo('${organismoConError.id}')" title="<spring:message code="regweb.procesar"/>" value="<spring:message code="regweb.procesar"/>" class="btn btn-warning btn-sm">
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </c:if>
                    </div> <!-- ./panel body principal-->
                </div> <!-- ./panel success principal-->
            </div>
        </div> <!-- ./row principal-->
    </div>
</div>

<c:import url="../modulos/pie.jsp"/>
<%-- traduccions para organismosaprocesar.js--%>
<script type="text/javascript">
  var trads = new Array();
  trads['organismo.extinguido'] = "<spring:message code='organismo.extinguido' javaScriptEscape='true' />";
  trads['libro.libro'] = "<spring:message code='libro.libro' javaScriptEscape='true' />";
  trads['organismo.asignado'] = "<spring:message code='organismo.asignado' javaScriptEscape='true' />";
  trads['mensajeprocesadook'] = "<spring:message code='organismo.procesado.ok' javaScriptEscape='true' />";
  trads['mensajeprocesadoerror'] = "<spring:message code='organismo.procesado.error' javaScriptEscape='true' />";
</script>

<script type="text/javascript" src="<c:url value="/js/organismosaprocesar.js"/>"></script>
</body>
</html>