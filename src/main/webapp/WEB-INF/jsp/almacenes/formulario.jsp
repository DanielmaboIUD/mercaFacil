<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nuevo almacén' : 'Editar almacén'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/almacenes' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="q" value="<c:out value='${param.q}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="codAlmacen">Código</label>
    <input id="codAlmacen" name="codAlmacen" maxlength="5" required
           value="<c:out value='${almacen.codAlmacen}' />" ${esNuevo ? '' : 'readonly'}>
    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" maxlength="60" required
           value="<c:out value='${almacen.nombre}' />">
    <label for="direccion">Dirección</label>
    <input id="direccion" name="direccion" maxlength="120" required
           value="<c:out value='${almacen.direccion}' />">
    <label for="nombreGerente">Nombre del gerente</label>
    <input id="nombreGerente" name="nombreGerente" maxlength="80" required
           value="<c:out value='${almacen.nombreGerente}' />">
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/almacenes"><c:param name="pagina" value="${param.pagina}" /><c:param name="q" value="${param.q}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
