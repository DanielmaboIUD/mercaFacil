<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Almacenes" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/almacenes?accion=nuevo' />">Nuevo almacén</a></p>
<table class="tabla">
    <thead>
    <tr><th>Código</th><th>Nombre</th><th>Dirección</th><th>Nombre del gerente</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${almacenes}">
        <tr>
            <td><c:out value="${x.codAlmacen}" /></td>
            <td><c:out value="${x.nombre}" /></td>
            <td><c:out value="${x.direccion}" /></td>
            <td><c:out value="${x.nombreGerente}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/almacenes">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="id" value="${x.codAlmacen}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/almacenes' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="id" value="<c:out value='${x.codAlmacen}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty almacenes}">
        <tr><td colspan="5">No hay almacenes para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/almacenes" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
