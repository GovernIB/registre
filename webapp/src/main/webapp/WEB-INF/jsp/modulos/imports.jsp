<%--
<%@page import="es.caib.regweb.webapp.security.LoginInfo"
%>
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" 
%>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="<c:url value="/css/bootstrap.css"/>" rel="stylesheet">

    <!-- Add custom CSS here -->
    <link href="<c:url value="/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet">
    <!-- Select multiple i senzill -->
    <link href="<c:url value="/css/chosen.css"/>" rel="stylesheet">
    <!-- DateTimePicker -->
    <link href="<c:url value="/css/datepicker/bootstrap-datetimepicker.min.css"/>" rel="stylesheet">
    <!-- ColorPicker -->
    <link href="<c:url value="/css/colorpicker/pick-a-color-1.2.3.min.css"/>" rel="stylesheet">

    <!-- Css específico del proyecto -->
    <link href="<c:url value="/css/regweb.css"/>" rel="stylesheet">

    <!-- Timeline -->
    <link href="<c:url value="/css/timeline.css"/>" rel="stylesheet">

    <script type="text/javascript" src="<c:url value="/js/jquery-1.10.2.js"/>"></script>

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="<c:url value="/ico/favicon.png"/>">
<%--
<% session.setAttribute("loginInfo", LoginInfo.getInstance()); %>
 --%>