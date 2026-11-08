<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Precios de artículo" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/articulo-precio?accion=nuevo' />">Nuevo precio</a></p>
<table class="tabla">
    <thead>
    <tr><th>Artículo</th><th>Tipo de precio</th><th>Valor</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${registros}">
        <tr>
            <td><c:out value="${x.codArticulo} - ${x.nombreArticulo}" /></td>
            <td><c:out value="${x.codTipoPrecio} - ${x.nombreSegundo}" /></td>
            <td><c:out value="${x.valor}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/articulo-precio">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="codArticulo" value="${x.codArticulo}" />
                    <c:param name="codTipoPrecio" value="${x.codTipoPrecio}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/articulo-precio' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="codArticulo" value="<c:out value='${x.codArticulo}' />">
                    <input type="hidden" name="codTipoPrecio" value="<c:out value='${x.codTipoPrecio}' />">
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
<c:set var="rutaPaginador" value="/articulo-precio" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
