<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Pedidos" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/pedidos?accion=nuevo' />">Nuevo pedido</a></p>
<table class="tabla">
    <thead>
    <tr><th>Pedido</th><th>Fecha</th><th>Proveedor</th><th>Almacén</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${registros}">
        <tr>
            <td><c:out value="${x.numPedido}" /></td>
            <td><c:out value="${x.fecha}" /></td>
            <td><c:out value="${x.nit} - ${x.nombreProveedor}" /></td>
            <td><c:out value="${x.codAlmacen} - ${x.nombreAlmacen}" /></td>
            <td class="acciones">
                <c:url var="urlVer" value="/pedidos">
                    <c:param name="accion" value="ver" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="num" value="${x.numPedido}" />
                </c:url>
                <c:url var="urlEditar" value="/pedidos">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="num" value="${x.numPedido}" />
                </c:url>
                <a class="boton secundario" href="${urlVer}">Ver</a>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/pedidos' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="num" value="<c:out value='${x.numPedido}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty registros}">
        <tr><td colspan="5">No hay pedidos para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/pedidos" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
