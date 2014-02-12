<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OU OpenID login</title>
</head>
<body>
	<form name="openid-form-redirection" action="redirect"
		method="get" accept-charset="utf-8">
		<input type="hidden" name="p" value="<%= request.getParameter("p") %>"/>
		<input type="text" name="u" />
		<button type="submit">Login...</button>
	</form>
</body>

</html>