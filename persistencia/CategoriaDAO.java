package com.alura.jdbc.persistencia;

import com.alura.jdbc.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Connection connection;
    public CategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Categoria> listar() throws SQLException {
        List<Categoria> resultado = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT ID, NOMBRE FROM CATEGORIA"
        );

        // statement.executeQuery() -> Nos devuelve el ResultSet de una vez
        final ResultSet resultSet = statement.executeQuery();

        try (resultSet) {
            while (resultSet.next()) {
                Categoria nuevaCategoria = new Categoria(resultSet.getInt("ID"), resultSet.getString("NOMBRE"));
                resultado.add(nuevaCategoria);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }

    public List<Categoria> listarConProductos() throws SQLException {
        List<Categoria> resultado = new ArrayList<>();

        // JOIN / INNER JOIN EN SQL
        PreparedStatement statement = connection.prepareStatement(
                "SELECT C.ID, C.NOMBRE, P.ID, P.NOMBRE FROM CATEGORIA C INNER JOIN PRODUCTO P ON C.ID = P.CATEGORIA_ID"
        );

        // statement.executeQuery() -> Nos devuelve el ResultSet de una vez
        final ResultSet resultSet = statement.executeQuery();

        try (resultSet) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("ID");
                String name = resultSet.getString("NOMBRE");

                var categoria = resultado
                        .stream()
                        .filter(cat -> cat.getId().equals(id))
                        .findAny()
                        .orElseGet(() -> {
                            Categoria nuevaCategoria = new Categoria(id,name);
                            resultado.add(nuevaCategoria);

                            return nuevaCategoria;
                        });

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }
}
