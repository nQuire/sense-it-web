<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OpenID HTML FORM Redirection</title>
<link rel="stylesheet" href="../html/css/pure-min.css" />
</head>
<body>
	<p>Logged in as: <c:out value="${sessionScope['openid-email']}" />
		
	</p>

<script>

window.opener.handleOpenIdResponse();
window.close();

</script>

</body>


</html>