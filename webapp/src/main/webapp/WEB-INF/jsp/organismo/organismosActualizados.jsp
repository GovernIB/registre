<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

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

        <!-- Este jsp muestra después del proceso de sincronización el PANEL de organismos EXTINGUIDOS, TRANSITORIOS, ANULADOS POR PROCESAR-->
        <c:if test="${not empty extinguidos}" >
        <div class="row">
            <div class="col-xs-12">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="organismo.actualizados"/></strong></h3>
                    </div>

                    <div  class="panel-body" id="pendientes">
                        <c:forEach var="extinguido" items="${extinguidos}">
                        <div class="row">
                            <div class="col-xs-12">
                               <div class="panel panel-success" id="panel${extinguido.id}">
                                   <div class="panel-heading">
                                        <h3 class="panel-title"><i class="fa fa-globe"></i> <strong><spring:message code="organismo.extinguido"/>: ${extinguido.codigo} - ${extinguido.denominacion}</strong></h3>
                                   </div>

                                   <div  class="panel-body">
                                      <form  id="extinguidoForm${extinguido.id}" action="${pageContext.request.contextPath}/entidad/procesarextinguido/${extinguido.id}/${true}" method="post" class="form-horizontal">
                                            <input type="hidden" id="total${extinguido.id}" value="${fn:length(extinguido.libros)}"/>
                                            <c:forEach var="libroextinguido" items="${extinguido.libros}" varStatus="contador">
                                                  <div class="form-group col-xs-8">
                                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                                            <label for="libro${contador.count}-${extinguido.id}">${libroextinguido.nombre}</label>
                                                            <input id="libro${contador.count}-${extinguido.id}" type="hidden" class="form-control" value="${libroextinguido.id}"/>
                                                            <span id="libro${contador.count}-${extinguido.id}Error"></span>
                                                        </div>
                                                        <div class="col-xs-8">
                                                             <select id="organismoSustituye${contador.count}-${extinguido.id}" name="organismoSustituye${contador.count}-${extinguido.id}" class="chosen-select">
                                                                <c:forEach items="${extinguido.historicoUO}" var="organismoSustituye">
                                                                    <option value="${organismoSustituye.id}">${organismoSustituye.denominacion}</option>
                                                                </c:forEach>
                                                             </select>
                                                             <span id="organismoSustituye${contador.count}-${extinguido.id}Error"></span>
                                                         </div>
                                                  </div>
                                            </c:forEach>
                                            <div class="form-group col-xs-6">
                                                <input type="button" onclick="procesarExtinguido('${extinguido.id}')" title="<spring:message code="regweb.procesar"/>" value="<spring:message code="regweb.procesar"/>" class="btn btn-warning btn-sm">
                                            </div>
                                      </form>
                                   </div>
                               </div>

                            </div>
                        </div>
                        </c:forEach>

                    </div> <!--/.panel body-->

                </div>
            </div>
            <%--Botonera--%>
            <div class="col-xs-12">
                <button type="button" onclick="goTo('<c:url value="/organismo/list"/>')" class="btn btn-warning btn-sm" title="<spring:message code="regweb.volver"/>">
                  <spring:message code="regweb.volver"/>
                </button>
            </div>

        <!-- /.PANEL EXTINGUIDOS, TRANSITORIOS; ANULADOS -->
        </div><!-- /.row-->
        </c:if>

        <!-- /.PANEL RESUMEN -->
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

                      <!-- LOS NO AUTOMATICOS SE MUESTRAN CON JQUERY en la funcion mostrarProcesado() en extinguido.js -->


                    </div>
                </div>

            </div>

        </div>
<%--        </c:if>--%>


</div>
<c:import url="../modulos/pie.jsp"/>
<%-- traduccions para extinguidos.js--%>
<script type="text/javascript">
  var trads = new Array();
  trads['organismo.extinguido'] = "<spring:message code='organismo.extinguido' javaScriptEscape='true' />";
  trads['libro.libro'] = "<spring:message code='libro.libro' javaScriptEscape='true' />";
  trads['organismo.asignado'] = "<spring:message code='organismo.asignado' javaScriptEscape='true' />";
</script>

<script type="text/javascript" src="<c:url value="/js/extinguidos.js"/>"></script>
</body>
</html>