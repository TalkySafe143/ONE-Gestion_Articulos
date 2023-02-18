package com.alura.jdbc.persistencia;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.model.Categoria;
import com.alura.jdbc.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO { // DAO como un estandar

    final private Connection connection;

    public ProductoDAO(Connection connection) {
        this.connection = connection;
    }

    public void modificar(Producto producto) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("UPDATE producto SET NOMBRE=? , DESCRIPCION=? WHERE ID=?;");

        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getId());

        statement.execute();

        if(statement.getUpdateCount() == 1) {
            System.out.println("Modificado con exito");
        } else {
            System.out.println("Algo salio mal);");
        }

        statement.close();
    }

    public int eliminar(int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM producto WHERE ID=?;");

        statement.setInt(1, id);

        statement.execute();

        // statement.getUpdateCount() -> retorna cuantas filas fueron modificadas
        return statement.getUpdateCount();
    }

    public void guardar (Producto producto) throws SQLException {
        // Abrimos una nueva conexion


        final PreparedStatement statement;
        try (this.connection) { // try/catch with resources para que se cierre la conexion automatricamente
            // Connection.setAutoCommit() -> Con este metodo podemos tomar control de la transacción SQL ingresando false
            this.connection.setAutoCommit(false);

            // Creamos un nuevo Statement
            // Statement statement = connection.createStatement();

            // E ingresamos el SQL en execute(), hay que tener cuidado con las comillas simples al ingresar un dato String
            // Vamos a preparar primero el String del SQL, sin embargo, cuando se tenga que ingresar los valores vamos a ingresar
            // signos de interrogacion (?) el cual va a representar los valores
            // El objetivo de este formato es para evitar el SQL injection
            String sql = "INSERT INTO producto (nombre, descripcion, cantidad, categoria_id) VALUES (?, ?, ?, ?);";

            // En vez de crear un Statement, vamos a preparar un 'PreparedStatement' con el metodo:
            // Connection.prepareStatement() -> Donde el primer parametro es el SQL con el formato (?)
            //             -> En el segundo parametro podemos indicarle que nos retorne la llave autogenerada por la base de datos
            //             -> Con Statement.RETURN_GENERATED_KEYS
            statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            try (statement) {
                // Apuntes siguen en esta funcion ---
                ejecutaRegistro(statement, producto);
                // Connection.commit() -> Este metodo permite concluir la transacción de forma exitosa
                this.connection.commit();
            } catch (SQLException e) {
                // Connection.rollback() -> Permite hacer un rollback de la transacción SQL
                this.connection.rollback();
            }

            // Tambien podemos cerrar el Statement
            // statement.close(); No es necesario por el try with resources
        }

    }

    public List<Producto> listar() throws SQLException {
        // Primero hacemos la conexión y la guardamos en una variable de tipo Connection
       // Connection connection = new ConnectionFactory().recuperaConexion();

        /*
         * Las Queries de SQL se le llaman Statement entonces java.sql nos va a proveer algunos metodos para crear Statements
         *
         * Connection.createStatement() -> Este metodo retorna un nuevo objeto del tipo Statement
         * */
        Statement statement = connection.createStatement();

        // Sin embargo, para evitar la inyección SQL tenemos que hacer el 'PreparedStatement' de la siguiente manera
        /*
         * PreparedStatement statement = connection.prepareStatement(
         *           "SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO;"
         *           );
         *
         * statement.execute();
         * */

        /*
         * Statement.execute() -> Este metodo ejecuta la sentencia SQL que le pasemos en el parametro como String
         * Este metodo retorna un boolean, el cual indica si la query regresó una lista o un anuncio
         * Si retorna true, quiere decir que existe una lista, si retorna false, un anuncio
         * */
        statement.execute("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO;");

        /*
         * Statement.getResultSet() -> Este metodo retorna un objeto de tipo ResultSet el cual contiene una estructura de
         * datos con los resultados de la query SQL
         * */
        ResultSet resultQuery = statement.getResultSet();

        // Para ir preparando el resultado de la Query hacemos una lista de Map<Columna, valor> - Producto (Clase)
        List<Producto> resultado = new ArrayList<>();

        // ResultSet se comporta como un iterador, por lo tanto para recorrerlo podemos usar .next()
        while (resultQuery.next()) {

            /*
             * Al Producto le asignamos como key el nombre de la columna
             *
             * ResultSet tiene metodos para recuperar los valores de las columnas como un tipo de dato nativo
             * Todos los metodos tienen en común que el parametro puede ser el index de la columna o el nombre de la misma
             *
             * ResulSet.getInt() -> Retorna el valor numerico de la columna indicada
             * ResulSet.getString() -> Retorna el valor como String de la columna indicada
             *
             * La nomenclatura es la misma para todos los tipos de datos
             *
             * */
            Producto fila = new Producto(
                    resultQuery.getInt("ID"),
                    resultQuery.getString("NOMBRE"),
                    resultQuery.getString("DESCRIPCION"),
                    resultQuery.getInt("CANTIDAD")
            ); // Hacemos una nueva instancia del Producto con Producto() y representará una fila

            // Añadimos las filas a la lista
            resultado.add(fila);
        }

        // Siempre hay que cerrar la conexión
        connection.close();
        // Se tiene que hacer con un try/catch with resources, pero por temas de practicidad lo puedes ver en guardar()

        return resultado;
    }


    public List<Producto> listar (Integer categoria_id) throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO WHERE CATEGORIA_ID = " +categoria_id+ ";");

        ResultSet resultQuery = statement.getResultSet();

        List<Producto> resultado = new ArrayList<>();

        while (resultQuery.next()) {

            Producto fila = new Producto(
                    resultQuery.getInt("ID"),
                    resultQuery.getString("NOMBRE"),
                    resultQuery.getString("DESCRIPCION"),
                    resultQuery.getInt("CANTIDAD")
            ); // Hacemos una nueva instancia del Producto con Producto() y representará una fila

            // Añadimos las filas a la lista
            resultado.add(fila);
        }

        // Siempre hay que cerrar la conexión
        connection.close();
        // Se tiene que hacer con un try/catch with resources, pero por temas de practicidad lo puedes ver en guardar()

        return resultado;
    }


    private void ejecutaRegistro (PreparedStatement statement, Producto producto) throws SQLException {
        // Entonces para cada parametro (?) vamos a ingresar el valor con los metodos 'setString()' / 'setInt()' o el tipo de dato
        // Donde el primer parametro es la posición del caracter '?' y como segundo parametro el valor que queremos ingresar
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());
        statement.setInt(4, producto.getCategoriaId());


        // Finalmente vamos a ejecutar 'statement.execute()' para mandar el query SQL
        statement.execute();

        // Si le indicamos que retorne la llave autogenerada, podemos acceder a Statement.getGeneratedKeys() para obtener
        // Un ResultSet con la llave generada
        final ResultSet resultSet = statement.getGeneratedKeys();

        // Se tiene que declarar como final para poder hacer el try/catch with resources
        try (resultSet) { // Se usa try/catch with resources para que se cierre automaticamente el resultSet
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el ID: %d", resultSet.getInt(1)));
                // No sabemos con exactitud como es el ResultSet, entonces le colocamos el primer indice de columna para el ID
            }
        }

    }

}
