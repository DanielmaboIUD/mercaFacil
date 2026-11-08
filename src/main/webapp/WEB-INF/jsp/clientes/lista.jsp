<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Clientes" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/clientes?accion=nuevo' />">Nuevo cliente</a></p>
<table class="tabla">
    <thead>
    <tr><th>Cédula</th><th>Nombre</th><th>Dirección</th><th>Teléfono</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${clientes}">
        <tr>
            <td><c:out value="${x.cedula}" /></td>
            <td><c:out value="${x.nombre}" /></td>
            <td><c:out value="${x.direccion}" /></td>
            <td><c:out value="${x.telefono}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/clientes">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="id" value="${x.cedula}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/clientes' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="id" value="<c:out value='${x.cedula}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty clientes}">
        <tr><td colspan="5">No hay clientes para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/clientes" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
