package com.alura.jdbc;

import javax.swing.JFrame;

import com.alura.jdbc.view.ControlDeStockFrame;

import java.sql.SQLException;

public class ControlDeStockMain {

    public static void main(String[] args) throws SQLException {
        ControlDeStockFrame produtoCategoriaFrame = new ControlDeStockFrame();
        produtoCategoriaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}