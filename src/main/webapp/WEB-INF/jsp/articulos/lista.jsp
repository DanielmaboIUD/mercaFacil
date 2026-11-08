<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Artículos" />
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<p><a class="boton" href="<c:url value='/articulos?accion=nuevo' />">Nuevo artículo</a></p>
<table class="tabla">
    <thead>
    <tr><th>Código</th><th>Nombre</th><th>Unidad</th><th>Grupo</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <c:forEach var="a" items="${articulos}">
        <tr>
            <td><c:out value="${a.codArticulo}" /></td>
            <td><c:out value="${a.nombre}" /></td>
            <td><c:out value="${a.descripcionUnidad}" /></td>
            <td><c:out value="${a.descripcionGrupo}" /></td>
            <td class="acciones">
                <c:url var="urlEditar" value="/articulos">
                    <c:param name="accion" value="editar" />
                    <c:param name="pagina" value="${paginacion.numero}" />
                    <c:param name="id" value="${a.codArticulo}" />
                </c:url>
                <a class="boton secundario" href="${urlEditar}">Editar</a>
                <form method="post" action="<c:url value='/articulos' />">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="pagina" value="${paginacion.numero}">
                    <input type="hidden" name="id" value="<c:out value='${a.codArticulo}' />">
                    <button class="boton peligro" type="submit" onclick="return confirm('¿Eliminar este registro?')">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty articulos}">
        <tr><td colspan="5">No hay artículos para mostrar.</td></tr>
    </c:if>
    </tbody>
</table>
<c:set var="rutaPaginador" value="/articulos" />
<%@ include file="/WEB-INF/jsp/layout/paginador.jspf" %>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
