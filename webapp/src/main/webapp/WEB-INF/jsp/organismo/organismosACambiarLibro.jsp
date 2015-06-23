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
        <c:import url="../modulos/mensajes.jsp"/>
        <!-- PANEL LIBROS A CAMBIAR -->

        <div class="row">
            <div class="col-xs-12">
                <c:if test="${not empty organismosAProcesar}" >
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="organismo.procesar"/></strong></h3>
                    </div>

                    <div  class="panel-body" id="pendientes">
                        <p><spring:message code="organismo.modificar.libros"/></p>

                        <c:forEach var="organismoAProcesar" items="${organismosAProcesar}">
                            <c:if test="${not empty organismoAProcesar.libros}">
                                <div class="row">
                                    <div class="col-xs-12">
                                       <div class="panel panel-success" id="panel${organismoAProcesar.id}">
                                           <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-globe"></i> <strong><spring:message code="organismo.libros"/>: ${organismoAProcesar.denominacion} (${organismoAProcesar.codigo})</strong></h3>
                                           </div>

                                           <div  class="panel-body">
                                              <form  id="organismoAProcesarForm${organismoAProcesar.id}" action="${pageContext.request.contextPath}/entidad/procesarlibroorganismo/${organismoAProcesar.id}/${esPendiente}" method="post" class="form-horizontal">
                                                    <input type="hidden" id="total${organismoAProcesar.id}" value="${fn:length(organismoAProcesar.libros)}"/>
                                                    <c:forEach var="libroorganismoAProcesar" items="${organismoAProcesar.libros}" varStatus="contador">
                                                          <div class="form-group col-xs-8">
                                                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                                    <label for="libro${contador.count}-${organismoAProcesar.id}"><spring:message code="libro.libro"/> <em>${libroorganismoAProcesar.nombre}</em></label>
                                                                    <input id="libro${contador.count}-${organismoAProcesar.id}" type="hidden" class="form-control" value="${libroorganismoAProcesar.id}"/>
                                                                    <span id="libro${contador.count}-${organismoAProcesar.id}Error"></span>
                                                                </div>
                                                                <div class="col-xs-8">
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

                    </div> <!--/.panel body-->

                </div>
                </c:if>
            </div>
            <%--Botonera--%>
            <div class="col-xs-12">
                <button type="button" onclick="goTo('<c:url value="/organismo/list"/>')" class="btn btn-warning btn-sm" title="<spring:message code="regweb.volver"/>">
                  <spring:message code="regweb.volver"/>
                </button>
            </div>

        <!-- /.PANEL LIBROS A CAMBIAR -->
        </div><!-- /.row-->


        <!-- PANEL RESUMEN -->
        <c:if test="${esPendiente}">
            <c:if test="${not empty extinguidosAutomaticos}">
                <div class="row">
                    <div class="col-xs-8">
                        <div class="panel panel-success">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-file-o"></i> <strong><spring:message code="organismos.extinguidos.resumen"/></strong></h3>
                            </div>
                            <div  class="panel-body" id="resumen">
                                <!-- EXTINGUIDOS AUTOMATICOS -->
                                <c:forEach var="extinguidoAutomatico" items="${extinguidosAutomaticos}">
                                    <spring:message code="organismo.extinguido"/>: <strong>${extinguidoAutomatico.key}</strong>
                                    <c:set var="organismoSustituye" value="${extinguidoAutomatico.value}"/>
                                    <table id="automaticos${extinguidoAutomatico.key}" class="table table-bordered table-hover table-striped">
                                         <colgroup>
                                             <col>
                                             <col>
                                         </colgroup>
                                         <thead>
                                             <tr>
                                                 <th><spring:message code="libro.libro"/></th>
                                                 <th><spring:message code="organismo.asignado"/></th>
                                             </tr>
                                         </thead>

                                         <tbody>
                                             <c:forEach var="libro" items="${organismoSustituye.libros}">
                                                 <tr>
                                                     <td>${libro.nombre}</td>
                                                     <td>${organismoSustituye.denominacion}</td>
                                                 </tr>
                                             </c:forEach>
                                         </tbody>
                                    </table>
                                </c:forEach>

                              <!-- LOS NO AUTOMATICOS SE MUESTRAN CON JQUERY en la funcion mostrarProcesado() en organismosaprocesar.js -->


                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:if>
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