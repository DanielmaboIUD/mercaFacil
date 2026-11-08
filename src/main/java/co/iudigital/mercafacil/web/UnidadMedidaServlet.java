package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.UnidadMedidaDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.UnidadMedida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/unidades")
public class UnidadMedidaServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/unidades/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/unidades/formulario.jsp";
    private static final int MAX_CODIGO = 5;
    private static final int MAX_DESCRIPCION = 40;

    private final UnidadMedidaDAO dao = new UnidadMedidaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new UnidadMedida(), true, null);
        } else if ("editar".equals(accion)) {
            editar(request, response);
        } else {
            mostrarLista(request, response, null);
        }
    }

    // guardar y eliminar solo por POST: un GET no debe modificar datos.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("guardar".equals(accion)) {
            guardar(request, response);
        } else if ("eliminar".equals(accion)) {
            eliminar(request, response);
        } else {
            redirigirALista(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            UnidadMedida unidadMedida = dao.buscarPorId(request.getParameter("id"));
            if (unidadMedida == null) {
                mostrarLista(request, response, "La unidad de medida ya no existe.");
            } else {
                mostrarFormulario(request, response, unidadMedida, false, null);
            }
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        UnidadMedida unidadMedida = new UnidadMedida(limpio(request, "codUnidad"), limpio(request, "descripcion"));
        String error = validar(unidadMedida);
        if (error != null) {
            mostrarFormulario(request, response, unidadMedida, esNuevo, error);
            return;
        }
        try {
            if (esNuevo) {
                dao.insertar(unidadMedida);
            } else {
                dao.actualizar(unidadMedida);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarFormulario(request, response, unidadMedida, esNuevo, e.getMessage());
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            dao.eliminar(request.getParameter("id"));
            Flash.exito(request, "Registro eliminado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            Flash.error(request, e.getMessage());
            redirigirALista(request, response);
        }
    }

    private String validar(UnidadMedida unidadMedida) {
        if (unidadMedida.getCodUnidad().isEmpty()) {
            return "El código es obligatorio.";
        }
        if (unidadMedida.getCodUnidad().length() > MAX_CODIGO) {
            return "El código admite máximo " + MAX_CODIGO + " caracteres.";
        }
        if (unidadMedida.getDescripcion().isEmpty()) {
            return "La descripción es obligatoria.";
        }
        if (unidadMedida.getDescripcion().length() > MAX_DESCRIPCION) {
            return "La descripción admite máximo " + MAX_DESCRIPCION + " caracteres.";
        }
        return null;
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<UnidadMedida> unidades = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            unidades = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("unidades", unidades);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   UnidadMedida unidadMedida, boolean esNuevo, String error)
            throws ServletException, IOException {
        request.setAttribute("unidadMedida", unidadMedida);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/unidades?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en unidades de medida: " + e.getMessage(), e);
    }
}
