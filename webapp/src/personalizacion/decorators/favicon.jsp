<%
String favicon = System.getProperty("entitat.favicon");
if (favicon == null) {
	favicon = request.getContextPath() + "/img/favicon.ico";
}
%><link href="<%=favicon %>" rel="shortcut icon" type="image/x-icon" />