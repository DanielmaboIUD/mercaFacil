<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${esNuevo ? 'Nueva unidad de medida' : 'Editar unidad de medida'}" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<form class="formulario" method="post" action="<c:url value='/unidades' />">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="pagina" value="<c:out value='${param.pagina}' />">
    <input type="hidden" name="esNuevo" value="${esNuevo}">
    <label for="codUnidad">Código</label>
    <input id="codUnidad" name="codUnidad" maxlength="5" required
           value="<c:out value='${unidadMedida.codUnidad}' />" ${esNuevo ? '' : 'readonly'}>
    <label for="descripcion">Descripción</label>
    <input id="descripcion" name="descripcion" maxlength="40" required
           value="<c:out value='${unidadMedida.descripcion}' />">
    <div class="acciones">
        <button class="boton" type="submit">Guardar</button>
        <c:url var="urlCancelar" value="/unidades"><c:param name="pagina" value="${param.pagina}" /></c:url>
        <a class="boton secundario" href="${urlCancelar}">Cancelar</a>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
