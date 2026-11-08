<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Nueva compra" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario ancho" method="post" action="<c:url value='/compras' />">
    <input type="hidden" name="accion" value="guardar">
    <label for="numFactura">Número de factura</label>
    <input id="numFactura" name="numFactura" maxlength="15" required value="<c:out value='${compra.numFactura}' />">
    <label for="fecha">Fecha</label>
    <input id="fecha" name="fecha" type="date" required value="<c:out value='${compra.fecha}' />">
    <label for="nit">Proveedor</label>
    <select id="nit" name="nit" required>
        <option value="">Seleccione…</option>
        <c:forEach var="p" items="${proveedores}">
            <option value="<c:out value='${p.nit}' />" ${p.nit eq compra.nit ? 'selected' : ''}><c:out value="${p.nit} - ${p.nombre}" /></option>
        </c:forEach>
    </select>
    <label for="codAlmacen">Almacén</label>
    <select id="codAlmacen" name="codAlmacen" required>
        <option value="">Seleccione…</option>
        <c:forEach var="s" items="${almacenes}">
            <option value="<c:out value='${s.codAlmacen}' />" ${s.codAlmacen eq compra.codAlmacen ? 'selected' : ''}><c:out value="${s.codAlmacen} - ${s.nombre}" /></option>
        </c:forEach>
    </select>
    <label for="codTipoPago">Tipo de pago</label>
    <select id="codTipoPago" name="codTipoPago" required>
        <option value="">Seleccione…</option>
        <c:forEach var="t" items="${tiposPago}">
            <option value="<c:out value='${t.codTipoPago}' />" ${t.codTipoPago eq compra.codTipoPago ? 'selected' : ''}><c:out value="${t.descripcion}" /></option>
        </c:forEach>
    </select>
    <h2>Artículos</h2>
    <table class="tabla">
        <thead>
        <tr><th>Artículo</th><th>Cantidad</th><th>Valor unitario</th><th>Subtotal</th><th></th></tr>
        </thead>
        <tbody id="filas">
        <c:forEach var="f" items="${filas}">
            <%@ include file="/WEB-INF/jsp/compras/fila.jspf" %>
        </c:forEach>
        </tbody>
    </table>
    <template id="plantilla-fila">
        <%@ include file="/WEB-INF/jsp/compras/fila.jspf" %>
    </template>
    <div class="acciones">
        <button type="button" class="boton secundario" id="agregar-fila">Agregar artículo</button>
    </div>
    <p class="total">Total estimado: <strong id="total">0</strong></p>
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <a class="boton secundario" href="<c:url value='/compras' />">Cancelar</a>
    </div>
</form>
<script src="<c:url value='/js/detalle.js' />"></script>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
