<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nuevo proveedor' : 'Editar proveedor'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/proveedores' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="nit">NIT</label>
    <input id="nit" name="nit" maxlength="15" required
           value="<c:out value='${proveedor.nit}' />" ${esNuevo ? '' : 'readonly'}>
    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" maxlength="100" required
           value="<c:out value='${proveedor.nombre}' />">
    <label for="direccion">Dirección</label>
    <input id="direccion" name="direccion" maxlength="120" required
           value="<c:out value='${proveedor.direccion}' />">
    <label for="telefono">Teléfono</label>
    <input id="telefono" name="telefono" maxlength="15" required
           value="<c:out value='${proveedor.telefono}' />">
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/proveedores"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
