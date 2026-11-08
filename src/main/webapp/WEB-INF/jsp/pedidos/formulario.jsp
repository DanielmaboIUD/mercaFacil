<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nuevo pedido' : 'Editar pedido'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario ancho" method="post" action="<c:url value='/pedidos' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="numPedido">Número de pedido</label>
    <input id="numPedido" name="numPedido" maxlength="15" required
           value="<c:out value='${pedido.numPedido}' />" ${esNuevo ? '' : 'readonly'}>
    <label for="fecha">Fecha</label>
    <input id="fecha" name="fecha" type="date" required value="<c:out value='${pedido.fecha}' />">
    <label for="nit">Proveedor</label>
    <select id="nit" name="nit" required>
        <option value="">Seleccione…</option>
        <c:forEach var="p" items="${proveedores}">
            <option value="<c:out value='${p.nit}' />" ${p.nit eq pedido.nit ? 'selected' : ''}><c:out value="${p.nit} - ${p.nombre}" /></option>
        </c:forEach>
    </select>
    <label for="codAlmacen">Almacén</label>
    <select id="codAlmacen" name="codAlmacen" required>
        <option value="">Seleccione…</option>
        <c:forEach var="s" items="${almacenes}">
            <option value="<c:out value='${s.codAlmacen}' />" ${s.codAlmacen eq pedido.codAlmacen ? 'selected' : ''}><c:out value="${s.codAlmacen} - ${s.nombre}" /></option>
        </c:forEach>
    </select>
    <h2>Artículos</h2>
    <table class="tabla">
        <thead>
        <tr><th>Artículo</th><th>Cantidad</th><th></th></tr>
        </thead>
        <tbody id="filas">
        <c:forEach var="f" items="${filas}">
            <%@ include file="/WEB-INF/jsp/pedidos/fila.jspf" %>
        </c:forEach>
        </tbody>
    </table>
    <template id="plantilla-fila">
        <%@ include file="/WEB-INF/jsp/pedidos/fila.jspf" %>
    </template>
    <div class="acciones">
        <button type="button" class="boton secundario" id="agregar-fila">Agregar artículo</button>
    </div>
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/pedidos"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<script src="<c:url value='/js/detalle.js' />"></script>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
