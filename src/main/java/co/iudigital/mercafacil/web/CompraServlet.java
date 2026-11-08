package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.AlmacenDAO;
import co.iudigital.mercafacil.dao.ArticuloDAO;
import co.iudigital.mercafacil.dao.CompraDAO;
import co.iudigital.mercafacil.dao.DetalleCompraDAO;
import co.iudigital.mercafacil.dao.ProveedorDAO;
import co.iudigital.mercafacil.dao.TipoPagoDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.exception.ReglaNegocioException;
import co.iudigital.mercafacil.model.Compra;
import co.iudigital.mercafacil.model.DetalleCompra;
import co.iudigital.mercafacil.model.MovimientoListado;
import co.iudigital.mercafacil.service.CompraService;
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

@WebServlet("/compras")
public class CompraServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/compras/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/compras/formulario.jsp";
    private static final String DETALLE = "/WEB-INF/jsp/compras/detalle.jsp";
    private static final String NO_PERMITIDO = "Las compras no se pueden editar ni eliminar.";

    private final CompraDAO dao = new CompraDAO();
    private final DetalleCompraDAO detalleDAO = new DetalleCompraDAO();
    private final CompraService service = new CompraService();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final TipoPagoDAO tipoPagoDAO = new TipoPagoDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            Compra compra = new Compra();
            compra.setFecha(LocalDate.now());
            mostrarFormulario(request, response, compra, new ArrayList<>(), null);
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
        Compra compra = new Compra(limpio(request, "numFactura"), Validador.aFecha(fecha), null,
                limpio(request, "codTipoPago"), limpio(request, "nit"), limpio(request, "codAlmacen"));
        List<FilaDetalle> filas = FilaDetalle.leer(request);
        String error = Validador.primerError(
                Validador.texto(compra.getNumFactura(), "Número de factura", 15),
                Validador.fecha(fecha, "Fecha"),
                Validador.texto(compra.getCodTipoPago(), "Tipo de pago", 3),
                Validador.texto(compra.getNit(), "Proveedor", 15),
                Validador.texto(compra.getCodAlmacen(), "Almacén", 5),
                validarFilas(filas));
        if (error != null) {
            mostrarFormulario(request, response, compra, filas, error);
            return;
        }
        try {
            service.registrar(compra, convertir(compra.getNumFactura(), filas));
            Flash.exito(request, "Compra registrada correctamente.");
            response.sendRedirect(request.getContextPath() + "/compras?accion=ver&num="
                    + URLEncoder.encode(compra.getNumFactura(), StandardCharsets.UTF_8));
        } catch (DAOException | ReglaNegocioException e) {
            getServletContext().log("Compra rechazada: " + e.getMessage(), e);
            mostrarFormulario(request, response, compra, filas, e.getMessage());
        }
    }

    private String validarFilas(List<FilaDetalle> filas) {
        for (FilaDetalle fila : filas) {
            String error = Validador.primerError(
                    Validador.texto(fila.getCodArticulo(), "Artículo", 10),
                    Validador.entero(fila.getCantidad(), "Cantidad"),
                    Validador.decimal(fila.getValorUnitario(), "Valor unitario", 10, 2));
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    private List<DetalleCompra> convertir(String numFactura, List<FilaDetalle> filas) {
        List<DetalleCompra> detalles = new ArrayList<>();
        for (FilaDetalle fila : filas) {
            detalles.add(new DetalleCompra(numFactura, fila.getCodArticulo(), Validador.aEntero(fila.getCantidad()),
                    Validador.aDecimal(fila.getValorUnitario())));
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
                mostrarLista(request, response, "La compra no existe.");
                return;
            }
            request.setAttribute("movimiento", movimiento);
            request.setAttribute("detalles", detalleDAO.listarPorFactura(numero));
            request.getRequestDispatcher(DETALLE).forward(request, response);
        } catch (DAOException e) {
            getServletContext().log("Error en compras: " + e.getMessage(), e);
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
            getServletContext().log("Error en compras: " + e.getMessage(), e);
            error = e.getMessage();
        }
        request.setAttribute("registros", registros);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Compra compra,
                                   List<FilaDetalle> filas, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("proveedores", proveedorDAO.listar());
            request.setAttribute("almacenes", almacenDAO.listar());
            request.setAttribute("tiposPago", tipoPagoDAO.listar());
            request.setAttribute("articulos", articuloDAO.listar());
        } catch (DAOException e) {
            getServletContext().log("Error en compras: " + e.getMessage(), e);
            error = e.getMessage();
        }
        if (filas.isEmpty()) {
            filas.add(new FilaDetalle("", "", ""));
        }
        request.setAttribute("compra", compra);
        request.setAttribute("filas", filas);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }
}
