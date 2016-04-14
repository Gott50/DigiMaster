<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EnergyForce</title>
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
</head>
<link href="https://fonts.googleapis.com/css?family=Lobster"
	rel="stylesheet" type="text/css">
<style>
body {
	font-family: Lobster, Monospace;
}
</style>
<body>
	<div class="container-fluid well">
		<h1>EnergyForce</h1>
		<form action="WebClient" method="get">
			<table>
				<tr>
					<td>Username:</td>
					<td><input type="text" name="username" value="${kunde.name}"></td>
				<tr>
				<tr>
					<td>Password:</td>
					<td><input type="password" name="password"
						value="${kunde.password}"></td>
				<tr>
				<tr>
					<td colspan="2"><input type="submit"></td>
				<tr>
			</table>
		</form>

		<!-- <jsp:include page="client.jsp"></jsp:include> -->
		<!-- <jsp:include page="input.jsp"></jsp:include> -->
	</div>
</body>
</html>