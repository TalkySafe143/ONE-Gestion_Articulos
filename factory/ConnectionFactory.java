package com.alura.jdbc.factory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/*
*
* Vamos a refactorizar el ConnectionFactory para que no se cree una conexión por cada acción
* Vamos a aplicar el concepto de Pool de Conexiones, el cual tiene como objetivo mantener abierta una serie de conexiones
* En busca de mayor rendimiento de la aplicación, para esto hay varias librerías que hacen este trabajo y la mas conocida es
*
* C3P0 --> Esta es una librería para hacer pool de conexiones y la tenemos que importar con sus 'commons' en el 'pom.xml'
*
* Una vez hecho esto, vamos a implementar el pool de conexiones
*
* */
public class ConnectionFactory {

    private DataSource dataSource; // Vamos a tener una variable privada la cual va a contener el Pool de conexiones

    public ConnectionFactory() { // En el constructor de la clase vamos a definir algunas configuraciones
        // ComboPooledDataSource() es una clase que nos provee C3P0 para hacer un pool de conexiones
        var pooledDataSource = new ComboPooledDataSource();

        // Y podemos ingresar cada una de las configuraciones necesarias para la conexion a la base de datos:
        // setJdbcUrl() - setUser() - setPassword() son las mas importantes y reciben en su parametro dicha configuracion
        pooledDataSource.setJdbcUrl("jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC");
        pooledDataSource.setUser("root");
        pooledDataSource.setPassword(System.getenv("DB_PASSWORD"));
        pooledDataSource.setMaxPoolSize(10); // El maximo de conexiones va a ser 10
        // Cuando todas las conexiones esten llenas, el usuario va a esperar a que una de las conexiones este libre para el

        // Vamos a ingresar la variable privada con el pool de conexiones que acabamos de configurar
        this.dataSource = pooledDataSource;
    }
    public Connection recuperaConexion() {
        // ComboPooledDataSource.getConnection() devuelve la conexion que esta abierta en el pool de conexiones
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
