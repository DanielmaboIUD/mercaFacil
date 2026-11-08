<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nuevo tipo de pago' : 'Editar tipo de pago'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/tipos-pago' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="codTipoPago">Código</label>
    <input id="codTipoPago" name="codTipoPago" maxlength="3" required
           value="<c:out value='${tipoPago.codTipoPago}' />" ${esNuevo ? '' : 'readonly'}>
    <label for="descripcion">Descripción</label>
    <input id="descripcion" name="descripcion" maxlength="20" required
           value="<c:out value='${tipoPago.descripcion}' />">
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/tipos-pago"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
