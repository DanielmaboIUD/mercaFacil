<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Merca Fácil</title>
</head>
<body>
    <h1>Hola Merca Fácil</h1>
    <c:set var="prueba" value="JSTL funcionando" />
    <p><c:out value="${prueba}" /></p>
</body>
</html>
