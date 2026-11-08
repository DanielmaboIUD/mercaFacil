package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.VistaDetalleVenta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VistaDetalleVentaDAO {

    private static final String CONTAR = "SELECT count(*) FROM v_detalle_ventas";
    private static final String LISTAR_PAGINA =
            "SELECT num_factura, fecha, tipo_pago, cedula, cliente, almacen, cod_articulo, articulo, grupo, "
                    + "unidad, cantidad, valor_unitario, subtotal FROM v_detalle_ventas "
                    + "ORDER BY fecha DESC, num_factura DESC, cod_articulo LIMIT ? OFFSET ?";

    public int contar() {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(CONTAR);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    public List<VistaDetalleVenta> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<VistaDetalleVenta> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new VistaDetalleVenta(rs.getString("num_factura"),
                            rs.getObject("fecha", LocalDate.class), rs.getString("tipo_pago"),
                            rs.getString("cedula"), rs.getString("cliente"), rs.getString("almacen"),
                            rs.getString("cod_articulo"), rs.getString("articulo"), rs.getString("grupo"),
                            rs.getString("unidad"), rs.getInt("cantidad"), rs.getBigDecimal("valor_unitario"),
                            rs.getBigDecimal("subtotal")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }
}
