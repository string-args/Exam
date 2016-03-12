
 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
   <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Convert to Text</title>
    </head>
	<body>
		<%
			if (request.getAttribute("result") != null){
				out.println("<h1>"+request.getAttribute("result")+"</h1>");
			}
		%>
	<body>

</html>