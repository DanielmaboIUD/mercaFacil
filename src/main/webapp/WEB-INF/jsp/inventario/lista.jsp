<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Inventario" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/inventario?accion=nuevo' />">Nuevo registro</a></p>
<table class="tabla">
    <thead>
    <tr><th>Almacén</th><th>Artículo</th><th>Existencia</th><th>Máxima</th><th>Mínima</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${registros}">
        <tr>
            <td><c:out value="${x.codAlmacen} - ${x.nombreAlmacen}" /></td>
            <td><c:out value="${x.codArticulo} - ${x.nombreArticulo}" /></td>
            <td><c:out value="${x.existencia}" /></td>
            <td><c:out value="${x.cantidadMaxima}" /></td>
            <td><c:out value="${x.cantidadMinima}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/inventario">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="codAlmacen" value="${x.codAlmacen}" />
                    <c:param name="codArticulo" value="${x.codArticulo}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/inventario' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="codAlmacen" value="<c:out value='${x.codAlmacen}' />">
                    <input type="hidden" name="codArticulo" value="<c:out value='${x.codArticulo}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty registros}">
        <tr><td colspan="6">No hay registros para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/inventario" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
