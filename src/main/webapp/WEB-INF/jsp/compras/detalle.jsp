<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Compra ${movimiento.numFactura}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<dl class="resumen">
    <dt>Fecha</dt><dd><c:out value="${movimiento.fecha}" /></dd>
    <dt>Proveedor</dt><dd><c:out value="${movimiento.idTercero} - ${movimiento.nombreTercero}" /></dd>
    <dt>Almacén</dt><dd><c:out value="${movimiento.nombreAlmacen}" /></dd>
    <dt>Tipo de pago</dt><dd><c:out value="${movimiento.tipoPago}" /></dd>
    <dt>Valor total</dt><dd><c:out value="${movimiento.valorTotal}" /></dd>
</dl>
<table class="tabla">
    <thead>
    <tr><th>Artículo</th><th>Cantidad</th><th>Valor unitario</th><th>Subtotal</th></tr>
    </thead>
    <tbody>
    <c:forEach var="d" items="${detalles}">
        <tr>
            <td><c:out value="${d.codArticulo} - ${d.nombreArticulo}" /></td>
            <td><c:out value="${d.cantidad}" /></td>
            <td><c:out value="${d.valorUnitario}" /></td>
            <td><c:out value="${d.subtotal}" /></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:url var="urlVolver" value="/compras"><c:param name="pagina" value="${param.pagina}" /></c:url>
<p><a class="boton secundario" href="${urlVolver}">Volver</a></p>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
