package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.model.Categoria;
import com.alura.jdbc.persistencia.CategoriaDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaController {

    private CategoriaDAO categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO(new ConnectionFactory().recuperaConexion());
    }


    public List<Categoria> listar() throws SQLException {
        return this.categoriaDAO.listar();
    }

    public List<Categoria> cargaReporte() throws SQLException {
        return this.categoriaDAO.listarConProductos();
    }

}