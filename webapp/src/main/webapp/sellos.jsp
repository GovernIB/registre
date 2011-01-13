<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!--
 Atributs:
   "data"
   "hora"
   "oficina"
   "oficinaid"
   "numero"
   "ano"
   "ES" (E/S)
-->
<table align="center">
    <tr>
    <c:forEach begin="1" end="4" var="token" varStatus="st">
        <c:if test="${not st.first}">
        <td>&nbsp;&nbsp;</td>
        </c:if>
        <td align="center">
            <c:url var="urlSello" value="/imprimeSello">
                <c:param name="data" value="${data}"/>
                <c:param name="hora" value="${hora}"/>
                <c:param name="tipo" value="${token}"/>
                <c:param name="oficina" value="${oficina}"/>
                <c:param name="oficina" value="${oficinaid}"/>
                <c:param name="numero" value="${numero}"/>
                <c:param name="ano" value="${ano}"/>
                <c:param name="ES" value="${ES}"/>
            </c:url>
            <iframe height="87" width="63" name="pdf${token}" src="${urlSello}"></iframe>
        </td>
    </c:forEach>
    </tr>
    <tr>
    <c:forEach begin="1" end="4" var="token" varStatus="st">
        <c:if test="${not st.first}">
        <td>&nbsp;&nbsp;</td>
        </c:if>
        <td align="center">
            <c:url var="urlSello" value="/imprimeSello">
                <c:param name="data" value="${data}"/>
                <c:param name="hora" value="${hora}"/>
                <c:param name="tipo" value="${token}"/>
                <c:param name="oficina" value="${oficina}"/>
                <c:param name="oficina" value="${oficinaid}"/>
                <c:param name="numero" value="${numero}"/>
                <c:param name="ano" value="${ano}"/>
                <c:param name="ES" value="${ES}"/>
                <c:param name="auto_print" value="si"/>
            </c:url>
            <a target="_blank" href="${urlSello}"><fmt:message key="pdf_${token}"/></a>
        </td>
    </c:forEach>
    </tr>
    <tr>
    <c:forEach begin="5" end="8" var="token" varStatus="st">
        <c:if test="${not st.first}">
        <td>&nbsp;&nbsp;</td>
        </c:if>
        <td align="center">
            <c:url var="urlSello" value="/imprimeSello">
                <c:param name="data" value="${data}"/>
                <c:param name="hora" value="${hora}"/>
                <c:param name="tipo" value="${token}"/>
                <c:param name="oficina" value="${oficina}"/>
                <c:param name="oficina" value="${oficinaid}"/>
                <c:param name="numero" value="${numero}"/>
                <c:param name="ano" value="${ano}"/>
                <c:param name="ES" value="${ES}"/>
            </c:url>
            <iframe height="70" width="87" name="pdf${token}" src="${urlSello}"></iframe>
        </td>
    </c:forEach>
    </tr>
    <tr>
    <c:forEach begin="5" end="8" var="token" varStatus="st">
        <c:if test="${not st.first}">
        <td>&nbsp;&nbsp;</td>
        </c:if>
        <td align="center">
            <c:url var="urlSello" value="/imprimeSello">
                <c:param name="data" value="${data}"/>
                <c:param name="hora" value="${hora}"/>
                <c:param name="tipo" value="${token}"/>
                <c:param name="oficina" value="${oficina}"/>
                <c:param name="oficina" value="${oficinaid}"/>
                <c:param name="numero" value="${numero}"/>
                <c:param name="ano" value="${ano}"/>
                <c:param name="ES" value="${ES}"/>
                <c:param name="auto_print" value="si"/>
            </c:url>
            <a target="_blank" href="${urlSello}"><fmt:message key="pdf_${token}"/></a>
        </td>
    </c:forEach>
    </tr>
</table>