package co.iudigital.mercafacil.service;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.dao.ArticuloPrecioDAO;
import co.iudigital.mercafacil.dao.DetalleVentaDAO;
import co.iudigital.mercafacil.dao.ErroresSQL;
import co.iudigital.mercafacil.dao.InventarioDAO;
import co.iudigital.mercafacil.dao.VentaDAO;
import co.iudigital.mercafacil.exception.ReglaNegocioException;
import co.iudigital.mercafacil.model.ArticuloPrecio;
import co.iudigital.mercafacil.model.ArticuloPrecioId;
import co.iudigital.mercafacil.model.DetalleVenta;
import co.iudigital.mercafacil.model.Inventario;
import co.iudigital.mercafacil.model.InventarioId;
import co.iudigital.mercafacil.model.Venta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VentaService {

    public static final String TIPO_PRECIO = "DET";

    private static final BigDecimal MAXIMO_TOTAL = new BigDecimal("999999999999.99");

    private final VentaDAO ventaDAO = new VentaDAO();
    private final DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final ArticuloPrecioDAO precioDAO = new ArticuloPrecioDAO();

    public void registrar(Venta venta, List<DetalleVenta> detalles) {
        validar(detalles);
        // Orden fijo de artículos para que dos transacciones no se bloqueen entre sí.
        List<DetalleVenta> ordenados = new ArrayList<>(detalles);
        ordenados.sort(Comparator.comparing(DetalleVenta::getCodArticulo));
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                for (DetalleVenta detalle : ordenados) {
                    descontarDelInventario(conexion, venta.getCodAlmacen(), detalle);
                    detalle.setValorUnitario(precioDe(conexion, detalle.getCodArticulo()));
                }
                venta.setValorTotal(calcularTotal(ordenados));
                ventaDAO.insertar(conexion, venta);
                for (DetalleVenta detalle : ordenados) {
                    detalleDAO.insertar(conexion, detalle);
                }
                conexion.commit();
            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    private void validar(List<DetalleVenta> detalles) {
        if (detalles.isEmpty()) {
            throw new ReglaNegocioException("Debe agregar al menos un artículo.");
        }
        Set<String> articulos = new HashSet<>();
        for (DetalleVenta detalle : detalles) {
            String articulo = detalle.getCodArticulo();
            if (!articulos.add(articulo)) {
                throw new ReglaNegocioException("El artículo " + articulo + " está repetido.");
            }
            if (detalle.getCantidad() <= 0) {
                throw new ReglaNegocioException("La cantidad del artículo " + articulo + " debe ser mayor que cero.");
            }
        }
    }

    private void descontarDelInventario(Connection conexion, String almacen, DetalleVenta detalle)
            throws SQLException {
        String articulo = detalle.getCodArticulo();
        InventarioId id = new InventarioId(almacen, articulo);
        Inventario inventario = inventarioDAO.bloquear(conexion, id);
        if (inventario == null) {
            throw new ReglaNegocioException("El artículo " + articulo + " no tiene inventario en el almacén "
                    + almacen + ". Regístrelo primero.");
        }
        if (inventario.getExistencia() < detalle.getCantidad()) {
            throw new ReglaNegocioException("Existencia insuficiente de " + articulo + ": disponible "
                    + inventario.getExistencia() + ".");
        }
        inventarioDAO.sumarExistencia(conexion, id, -detalle.getCantidad());
    }

    private BigDecimal precioDe(Connection conexion, String articulo) throws SQLException {
        ArticuloPrecio precio = precioDAO.buscarPorId(conexion, new ArticuloPrecioId(articulo, TIPO_PRECIO));
        if (precio == null) {
            throw new ReglaNegocioException("El artículo " + articulo + " no tiene precio de tipo "
                    + TIPO_PRECIO + ".");
        }
        return precio.getValor();
    }

    private BigDecimal calcularTotal(List<DetalleVenta> detalles) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVenta detalle : detalles) {
            total = total.add(detalle.getValorUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }
        if (total.compareTo(MAXIMO_TOTAL) > 0) {
            throw new ReglaNegocioException("El valor total supera el máximo permitido.");
        }
        return total;
    }
}
