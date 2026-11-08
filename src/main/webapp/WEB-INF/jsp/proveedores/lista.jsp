<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Proveedores" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/proveedores?accion=nuevo' />">Nuevo proveedor</a></p>
<form class="filtro" method="get" action="<c:url value='/proveedores' />">
    <input type="search" name="q" maxlength="100" placeholder="Buscar por palabra clave" aria-label="Buscar" value="<c:out value='${q}' />">
    <button class="boton" type="submit">Buscar</button>
    <c:if test="${not empty q}">
        <a class="boton secundario" href="<c:url value='/proveedores' />">Limpiar</a>
    </c:if>
</form>
<table class="tabla">
    <thead>
    <tr><th>NIT</th><th>Nombre</th><th>Dirección</th><th>Teléfono</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${proveedores}">
        <tr>
            <td><c:out value="${x.nit}" /></td>
            <td><c:out value="${x.nombre}" /></td>
            <td><c:out value="${x.direccion}" /></td>
            <td><c:out value="${x.telefono}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/proveedores">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="q" value="${q}" />
                    <c:param name="id" value="${x.nit}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/proveedores' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="q" value="<c:out value='${q}' />">
                    <input type="hidden" name="id" value="<c:out value='${x.nit}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty proveedores}">
        <tr><td colspan="5">No hay proveedores para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/proveedores" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
