<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty error}">

    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>${error}</strong>
        <c:remove var="error" scope="session"/>
    </div>

</c:if>

<c:if test="${not empty aviso}">

    <div class="alert alert-warning alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>${aviso}</strong>
        <c:remove var="aviso" scope="session"/>
    </div>

</c:if>

<c:if test="${not empty infos}">
    <div class="alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <c:forEach var="info" items="${infos}">
            <strong>${info}</strong><br>
        </c:forEach>
        <c:remove var="infos" scope="session"/>
    </div>
</c:if>
