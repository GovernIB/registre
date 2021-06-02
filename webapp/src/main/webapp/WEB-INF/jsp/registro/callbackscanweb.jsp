<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<html lang="ca">

<body>

<div class="hide col-xs-12 text-center centrat" id="reload">
    <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
</div>

<script>
    window.location.href='${urlFinal}';
    $('#reload').show();

</script>

</body>
</html>