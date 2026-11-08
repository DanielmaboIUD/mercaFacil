<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nuevo artículo' : 'Editar artículo'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/articulos' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="codArticulo">Código</label>
    <input id="codArticulo" name="codArticulo" maxlength="10" required
           value="<c:out value='${articulo.codArticulo}' />" ${esNuevo ? '' : 'readonly'}>
    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" maxlength="80" required value="<c:out value='${articulo.nombre}' />">
    <label for="codUnidad">Unidad de medida</label>
    <select id="codUnidad" name="codUnidad" required>
        <option value="">Seleccione…</option>
        <c:forEach var="u" items="${unidades}">
            <option value="<c:out value='${u.codUnidad}' />" ${u.codUnidad eq articulo.codUnidad ? 'selected' : ''}><c:out value="${u.descripcion}" /></option>
        </c:forEach>
    </select>
    <label for="codGrupo">Grupo</label>
    <select id="codGrupo" name="codGrupo" required>
        <option value="">Seleccione…</option>
        <c:forEach var="g" items="${grupos}">
            <option value="<c:out value='${g.codGrupo}' />" ${g.codGrupo eq articulo.codGrupo ? 'selected' : ''}><c:out value="${g.descripcion}" /></option>
        </c:forEach>
    </select>
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/articulos"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
