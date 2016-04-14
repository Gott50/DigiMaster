<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${Name}</title>
</head>
<body>
	<form action="WebClient" method="post">
		<input type="hidden" name="command" value="uploadData">
		<table>
			<c:forEach var="dataName" items="${dataNames}">
				<tr>
					<td>${dataName}</td>
					<td><input type="text" name="${dataName}"
						value="${dataNameVal}"></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2"><input type="submit"></td>
			</tr>
		</table>
	</form>
</body>
</html>