<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Detalle de ventas" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<div class="tabla-scroll">
    <table class="tabla">
        <thead>
        <tr><th>Factura</th><th>Fecha</th><th>Tipo de pago</th><th>Cliente</th><th>Almacén</th><th>Artículo</th><th>Grupo</th><th>Unidad</th><th>Cantidad</th><th>Valor unitario</th><th>Subtotal</th></tr>
        </thead>
        <tbody>
        <c:forEach var="x" items="${registros}">
            <tr>
                <td><c:out value="${x.numFactura}" /></td>
                <td><c:out value="${x.fecha}" /></td>
                <td><c:out value="${x.tipoPago}" /></td>
                <td><c:out value="${x.cedula} - ${x.cliente}" /></td>
                <td><c:out value="${x.almacen}" /></td>
                <td><c:out value="${x.codArticulo} - ${x.articulo}" /></td>
                <td><c:out value="${x.grupo}" /></td>
                <td><c:out value="${x.unidad}" /></td>
                <td><c:out value="${x.cantidad}" /></td>
                <td><c:out value="${x.valorUnitario}" /></td>
                <td><c:out value="${x.subtotal}" /></td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="11">No hay ventas para mostrar.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
<c:set var="rutaPaginador" value="/detalle-ventas" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
