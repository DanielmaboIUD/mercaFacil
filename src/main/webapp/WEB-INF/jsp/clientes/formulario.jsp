<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nuevo cliente' : 'Editar cliente'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/clientes' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="cedula">Cédula</label>
    <input id="cedula" name="cedula" maxlength="12" required
           value="<c:out value='${cliente.cedula}' />" ${esNuevo ? '' : 'readonly'}>
    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" maxlength="80" required
           value="<c:out value='${cliente.nombre}' />">
    <label for="direccion">Dirección</label>
    <input id="direccion" name="direccion" maxlength="120" required
           value="<c:out value='${cliente.direccion}' />">
    <label for="telefono">Teléfono</label>
    <input id="telefono" name="telefono" maxlength="15" required
           value="<c:out value='${cliente.telefono}' />">
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/clientes"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
