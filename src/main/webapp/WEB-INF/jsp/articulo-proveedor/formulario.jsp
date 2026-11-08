<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nueva asociación' : 'Editar asociación'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/articulo-proveedor' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="codArticulo">Artículo</label>
    <c:choose>
        <c:when test="${esNuevo}">
            <select id="codArticulo" name="codArticulo" required>
                <option value="">Seleccione…</option>
                <c:forEach var="a" items="${articulos}">
                    <option value="<c:out value='${a.codArticulo}' />" ${a.codArticulo eq registro.codArticulo ? 'selected' : ''}><c:out value="${a.codArticulo} - ${a.nombre}" /></option>
                </c:forEach>
            </select>
        </c:when>
        <c:otherwise>
            <input id="codArticulo" name="codArticulo" readonly value="<c:out value='${registro.codArticulo}' />">
        </c:otherwise>
    </c:choose>
    <label for="nit">Proveedor</label>
    <c:choose>
        <c:when test="${esNuevo}">
            <select id="nit" name="nit" required>
                <option value="">Seleccione…</option>
                <c:forEach var="s" items="${proveedores}">
                    <option value="<c:out value='${s.nit}' />" ${s.nit eq registro.nit ? 'selected' : ''}><c:out value="${s.nit} - ${s.nombre}" /></option>
                </c:forEach>
            </select>
        </c:when>
        <c:otherwise>
            <input id="nit" name="nit" readonly value="<c:out value='${registro.nit}' />">
        </c:otherwise>
    </c:choose>
    <label for="precioCompra">Precio de compra</label>
    <input id="precioCompra" name="precioCompra" type="number" step="0.01" required
           value="<c:out value='${registro.precioCompra}' />">
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/articulo-proveedor"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
