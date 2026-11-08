package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.TipoPagoDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.TipoPago;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/tipos-pago")
public class TipoPagoServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/tipos-pago/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/tipos-pago/formulario.jsp";
    private static final int MAX_CODIGO = 3;
    private static final int MAX_DESCRIPCION = 20;

    private final TipoPagoDAO dao = new TipoPagoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new TipoPago(), true, null);
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
            TipoPago tipoPago = dao.buscarPorId(request.getParameter("id"));
            if (tipoPago == null) {
                mostrarLista(request, response, "El tipo de pago ya no existe.");
            } else {
                mostrarFormulario(request, response, tipoPago, false, null);
            }
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        TipoPago tipoPago = new TipoPago(limpio(request, "codTipoPago"), limpio(request, "descripcion"));
        String error = validar(tipoPago);
        if (error != null) {
            mostrarFormulario(request, response, tipoPago, esNuevo, error);
            return;
        }
        try {
            if (esNuevo) {
                dao.insertar(tipoPago);
            } else {
                dao.actualizar(tipoPago);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarFormulario(request, response, tipoPago, esNuevo, e.getMessage());
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

    private String validar(TipoPago tipoPago) {
        if (tipoPago.getCodTipoPago().isEmpty()) {
            return "El código es obligatorio.";
        }
        if (tipoPago.getCodTipoPago().length() > MAX_CODIGO) {
            return "El código admite máximo " + MAX_CODIGO + " caracteres.";
        }
        if (tipoPago.getDescripcion().isEmpty()) {
            return "La descripción es obligatoria.";
        }
        if (tipoPago.getDescripcion().length() > MAX_DESCRIPCION) {
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
        List<TipoPago> tiposPago = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            tiposPago = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("tiposPago", tiposPago);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   TipoPago tipoPago, boolean esNuevo, String error)
            throws ServletException, IOException {
        request.setAttribute("tipoPago", tipoPago);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/tipos-pago?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en tipos de pago: " + e.getMessage(), e);
    }
}
