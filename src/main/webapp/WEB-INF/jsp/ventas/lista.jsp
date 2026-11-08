<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Ventas" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/ventas?accion=nuevo' />">Nueva venta</a></p>
<table class="tabla">
    <thead>
    <tr><th>Factura</th><th>Fecha</th><th>Cliente</th><th>Almacén</th><th>Tipo de pago</th><th>Valor total</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="x" items="${registros}">
        <tr>
            <td><c:out value="${x.numFactura}" /></td>
            <td><c:out value="${x.fecha}" /></td>
            <td><c:out value="${x.idTercero} - ${x.nombreTercero}" /></td>
            <td><c:out value="${x.nombreAlmacen}" /></td>
            <td><c:out value="${x.tipoPago}" /></td>
            <td><c:out value="${x.valorTotal}" /></td>
            <td class="acciones">
                <c:url var="urlVer" value="/ventas">
                    <c:param name="accion" value="ver" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="num" value="${x.numFactura}" />
                </c:url>
                <a class="boton secundario" href="${urlVer}">Ver</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty registros}">
        <tr><td colspan="7">No hay ventas para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/ventas" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
