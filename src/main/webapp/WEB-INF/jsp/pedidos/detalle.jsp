<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Pedido ${pedido.numPedido}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<dl class="resumen">
    <dt>Fecha</dt><dd><c:out value="${pedido.fecha}" /></dd>
    <dt>Proveedor</dt><dd><c:out value="${pedido.nit} - ${pedido.nombreProveedor}" /></dd>
    <dt>Almacén</dt><dd><c:out value="${pedido.codAlmacen} - ${pedido.nombreAlmacen}" /></dd>
</dl>
<table class="tabla">
    <thead>
    <tr><th>Artículo</th><th>Cantidad</th></tr>
    </thead>
    <tbody>
    <c:forEach var="d" items="${detalles}">
        <tr>
            <td><c:out value="${d.codArticulo} - ${d.nombreArticulo}" /></td>
            <td><c:out value="${d.cantidad}" /></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:url var="urlVolver" value="/pedidos"><c:param name="pagina" value="${param.pagina}" /></c:url>
<p><a class="boton secundario" href="${urlVolver}">Volver</a></p>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
