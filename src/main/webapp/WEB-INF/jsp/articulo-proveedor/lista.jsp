<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Artículo–Proveedor" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/articulo-proveedor?accion=nuevo' />">Nueva asociación</a></p>
<table class="tabla">
    <thead>
    <tr><th>Artículo</th><th>Proveedor</th><th>Precio de compra</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${registros}">
        <tr>
            <td><c:out value="${x.codArticulo} - ${x.nombreArticulo}" /></td>
            <td><c:out value="${x.nit} - ${x.nombreSegundo}" /></td>
            <td><c:out value="${x.precioCompra}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/articulo-proveedor">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="codArticulo" value="${x.codArticulo}" />
                    <c:param name="nit" value="${x.nit}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/articulo-proveedor' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="codArticulo" value="<c:out value='${x.codArticulo}' />">
                    <input type="hidden" name="nit" value="<c:out value='${x.nit}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty registros}">
        <tr><td colspan="4">No hay registros para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/articulo-proveedor" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
