<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nuevo registro de inventario' : 'Editar inventario'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/inventario' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="codAlmacen">Almacén</label>
    <c:choose>
        <c:when test="${esNuevo}">
            <select id="codAlmacen" name="codAlmacen" required>
                <option value="">Seleccione…</option>
                <c:forEach var="s" items="${almacenes}">
                    <option value="<c:out value='${s.codAlmacen}' />" ${s.codAlmacen eq registro.codAlmacen ? 'selected' : ''}><c:out value="${s.codAlmacen} - ${s.nombre}" /></option>
                </c:forEach>
            </select>
        </c:when>
        <c:otherwise>
            <input id="codAlmacen" name="codAlmacen" readonly value="<c:out value='${registro.codAlmacen}' />">
        </c:otherwise>
    </c:choose>
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
    <label for="existencia">Existencia</label>
    <input id="existencia" name="existencia" type="number" step="1" required value="<c:out value='${registro.existencia}' />">
    <label for="cantidadMaxima">Cantidad máxima</label>
    <input id="cantidadMaxima" name="cantidadMaxima" type="number" step="1" required value="<c:out value='${registro.cantidadMaxima}' />">
    <label for="cantidadMinima">Cantidad mínima</label>
    <input id="cantidadMinima" name="cantidadMinima" type="number" step="1" required value="<c:out value='${registro.cantidadMinima}' />">
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/inventario"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
