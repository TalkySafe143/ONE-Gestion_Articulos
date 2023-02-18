package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.model.Categoria;
import com.alura.jdbc.model.Producto;
import com.alura.jdbc.persistencia.ProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

    private ProductoDAO productoDAO;

    public ProductoController() {
        this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperaConexion());
    }

    public void modificar(Producto producto) throws SQLException {
        this.productoDAO.modificar(producto);
    }

    public int eliminar(Integer id) throws SQLException {
        return this.productoDAO.eliminar(id);
    }

    public List<Producto> listar() throws SQLException { // Tenemos que lanzar una SQLException en el metodo
        return this.productoDAO.listar();
    }

    public void guardar(Producto producto, int categoria_id) throws SQLException {

        producto.setCategoriaId(categoria_id);

        this.productoDAO.guardar(producto);
    }

    public List<Producto> listar (Categoria categoria) throws SQLException {
        return productoDAO.listar(categoria.getId());
    }

}