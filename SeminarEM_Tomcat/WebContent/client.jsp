<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Client</title>
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
</head>
<body>
	<div class="container-fluid well">
		<form action="WebClient" method="post">
			<input type="hidden" name="command" value="loadDataName">
			<table>
				<c:forEach var="btnName" items="${btnNames}">
					<tr>
						<button class="btn" name="dataName" value="${btnName}">${btnName}</button>
					</tr>
				</c:forEach>
			</table>
		</form>
		<jsp:include page="input.jsp"></jsp:include>
		<p style="color: red">${errorMessage}</p>
	</div>
</body>
</html>