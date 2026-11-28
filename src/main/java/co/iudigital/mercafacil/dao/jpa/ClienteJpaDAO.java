package co.iudigital.mercafacil.dao.jpa;

import co.iudigital.mercafacil.config.jpa.JpaUtil;
import co.iudigital.mercafacil.dao.ErroresSQL;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


public class ClienteJpaDAO {

    private static final String FILTRO = " WHERE locate("
            + "FUNCTION('translate', lower(:filtro), 'áéíóúü', 'aeiouu'), "
            + "FUNCTION('translate', lower(concat(c.cedula, ' ', c.nombre, ' ', c.direccion, ' ', c.telefono)),"
            + " 'áéíóúü', 'aeiouu')) > 0";

    
    public void insertar(Cliente cliente) {
        escribir(em -> em.persist(cliente), false);
    }

    public Cliente buscarPorId(String id) {
        return leer(em -> em.find(Cliente.class, id));
    }

    public List<Cliente> listar() {
        return leer(em -> em.createQuery("SELECT c FROM Cliente c ORDER BY c.cedula", Cliente.class)
                .getResultList());
    }

    public int contar(String filtro) {
        return leer(em -> em.createQuery("SELECT count(c) FROM Cliente c" + FILTRO, Long.class)
                .setParameter("filtro", texto(filtro))
                .getSingleResult()
                .intValue());
    }

    public List<Cliente> listarPagina(String filtro, int desplazamiento, int tamano) {
        return leer(em -> em.createQuery("SELECT c FROM Cliente c" + FILTRO + " ORDER BY c.cedula", Cliente.class)
                .setParameter("filtro", texto(filtro))
                .setFirstResult(desplazamiento)
                .setMaxResults(tamano)
                .getResultList());
    }

    public void actualizar(Cliente cliente) {
        escribir(em -> {
            Cliente actual = existente(em, cliente.getCedula());
            actual.setNombre(cliente.getNombre());
            actual.setDireccion(cliente.getDireccion());
            actual.setTelefono(cliente.getTelefono());
        }, false);
    }

    public void eliminar(String id) {
        escribir(em -> em.remove(existente(em, id)), true);
    }

    private Cliente existente(EntityManager em, String id) {
        Cliente cliente = em.find(Cliente.class, id);
        if (cliente == null) {
            throw new DAOException("El registro ya no existe.");
        }
        return cliente;
    }

    private <R> R leer(Function<EntityManager, R> consulta) {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            return consulta.apply(em);
        } catch (DAOException e) {
            throw e;
        } catch (RuntimeException e) {
            throw traducir(e, false);
        }
    }

    private void escribir(Consumer<EntityManager> operacion, boolean eliminando) {
        try (EntityManager em = JpaUtil.crearEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                operacion.accept(em);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        } catch (DAOException e) {
            throw e;
        } catch (RuntimeException e) {
            throw traducir(e, eliminando);
        }
    }

    private static DAOException traducir(RuntimeException e, boolean eliminando) {
        for (Throwable causa = e; causa != null; causa = causa.getCause()) {
            if (causa instanceof SQLException sql) {
                return ErroresSQL.traducir(sql, eliminando);
            }
        }
        return new DAOException("Error al acceder a la base de datos.", e);
    }

    private static String texto(String filtro) {
        return filtro == null ? "" : filtro;
    }
}
