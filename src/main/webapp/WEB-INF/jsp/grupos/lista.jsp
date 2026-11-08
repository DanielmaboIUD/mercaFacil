<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Grupos" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/grupos?accion=nuevo' />">Nuevo grupo</a></p>
<table class="tabla">
    <thead>
    <tr><th>Código</th><th>Descripción</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="g" items="${grupos}">
        <tr>
            <td><c:out value="${g.codGrupo}" /></td>
            <td><c:out value="${g.descripcion}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/grupos">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="id" value="${g.codGrupo}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/grupos' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="id" value="<c:out value='${g.codGrupo}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty grupos}">
        <tr><td colspan="3">No hay grupos para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/grupos" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
