package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.AlmacenDAO;
import co.iudigital.mercafacil.dao.ArticuloPrecioDAO;
import co.iudigital.mercafacil.dao.VentaDAO;
import co.iudigital.mercafacil.dao.DetalleVentaDAO;
import co.iudigital.mercafacil.dao.ClienteDAO;
import co.iudigital.mercafacil.dao.TipoPagoDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.exception.ReglaNegocioException;
import co.iudigital.mercafacil.model.Venta;
import co.iudigital.mercafacil.model.DetalleVenta;
import co.iudigital.mercafacil.model.MovimientoListado;
import co.iudigital.mercafacil.service.VentaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ventas")
public class VentaServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/ventas/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/ventas/formulario.jsp";
    private static final String DETALLE = "/WEB-INF/jsp/ventas/detalle.jsp";
    private static final String NO_PERMITIDO = "Las ventas no se pueden editar ni eliminar.";

    private final VentaDAO dao = new VentaDAO();
    private final DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
    private final VentaService service = new VentaService();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final TipoPagoDAO tipoPagoDAO = new TipoPagoDAO();
    private final ArticuloPrecioDAO articuloPrecioDAO = new ArticuloPrecioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            Venta venta = new Venta();
            venta.setFecha(LocalDate.now());
            mostrarFormulario(request, response, venta, new ArrayList<>(), null);
        } else if ("ver".equals(accion)) {
            mostrarDetalle(request, response);
        } else if ("editar".equals(accion) || "eliminar".equals(accion)) {
            mostrarLista(request, response, NO_PERMITIDO);
        } else {
            mostrarLista(request, response, null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("guardar".equals(request.getParameter("accion"))) {
            guardar(request, response);
        } else {
            mostrarLista(request, response, NO_PERMITIDO);
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fecha = limpio(request, "fecha");
        Venta venta = new Venta(limpio(request, "numFactura"), Validador.aFecha(fecha), null,
                limpio(request, "codTipoPago"), limpio(request, "cedula"), limpio(request, "codAlmacen"));
        List<FilaDetalle> filas = FilaDetalle.leer(request);
        String error = Validador.primerError(
                Validador.texto(venta.getNumFactura(), "Número de factura", 15),
                Validador.fecha(fecha, "Fecha"),
                Validador.texto(venta.getCodTipoPago(), "Tipo de pago", 3),
                Validador.texto(venta.getCedula(), "Cliente", 12),
                Validador.texto(venta.getCodAlmacen(), "Almacén", 5),
                validarFilas(filas));
        if (error != null) {
            mostrarFormulario(request, response, venta, filas, error);
            return;
        }
        try {
            service.registrar(venta, convertir(venta.getNumFactura(), filas));
            Flash.exito(request, "Venta registrada correctamente.");
            response.sendRedirect(request.getContextPath() + "/ventas?accion=ver&num="
                    + URLEncoder.encode(venta.getNumFactura(), StandardCharsets.UTF_8));
        } catch (DAOException | ReglaNegocioException e) {
            getServletContext().log("Venta rechazada: " + e.getMessage(), e);
            mostrarFormulario(request, response, venta, filas, e.getMessage());
        }
    }

    private String validarFilas(List<FilaDetalle> filas) {
        for (FilaDetalle fila : filas) {
            String error = Validador.primerError(
                    Validador.texto(fila.getCodArticulo(), "Artículo", 10),
                    Validador.entero(fila.getCantidad(), "Cantidad"));
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    private List<DetalleVenta> convertir(String numFactura, List<FilaDetalle> filas) {
        List<DetalleVenta> detalles = new ArrayList<>();
        for (FilaDetalle fila : filas) {
            detalles.add(new DetalleVenta(numFactura, fila.getCodArticulo(), Validador.aEntero(fila.getCantidad()),
                    null));
        }
        return detalles;
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String numero = request.getParameter("num");
            MovimientoListado movimiento = dao.buscarListado(numero);
            if (movimiento == null) {
                mostrarLista(request, response, "La venta no existe.");
                return;
            }
            request.setAttribute("movimiento", movimiento);
            request.setAttribute("detalles", detalleDAO.listarPorFactura(numero));
            request.getRequestDispatcher(DETALLE).forward(request, response);
        } catch (DAOException e) {
            getServletContext().log("Error en ventas: " + e.getMessage(), e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<MovimientoListado> registros = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            registros = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            getServletContext().log("Error en ventas: " + e.getMessage(), e);
            error = e.getMessage();
        }
        request.setAttribute("registros", registros);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Venta venta,
                                   List<FilaDetalle> filas, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("clientes", clienteDAO.listar());
            request.setAttribute("almacenes", almacenDAO.listar());
            request.setAttribute("tiposPago", tipoPagoDAO.listar());
            request.setAttribute("articulos", articuloPrecioDAO.listarPorTipo(VentaService.TIPO_PRECIO));
        } catch (DAOException e) {
            getServletContext().log("Error en ventas: " + e.getMessage(), e);
            error = e.getMessage();
        }
        if (filas.isEmpty()) {
            filas.add(new FilaDetalle("", "", ""));
        }
        request.setAttribute("venta", venta);
        request.setAttribute("filas", filas);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }
}
