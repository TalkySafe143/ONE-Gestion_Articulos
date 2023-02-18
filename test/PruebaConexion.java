package com.alura.jdbc.test;
// IMPORTANTE ------ HACER EL PROYECTO CON MAVEN Y CONFIGURAR EL POM.XML TAL CUAL

// Vamos a importar varias clases e interfaces de java.sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Hay que agregar el driver del vendor a utilizar al proyecto en el archivo 'dataSources.xml' para el caso de Intellij IDEA
// En caso contario, mirar 'pom.xml'
// Se tiene que investigar como incluir los drivers en diferentes IDE
// https://www.jetbrains.com/help/idea/mysql.html
// https://stackoverflow.com/questions/56487914/how-to-connect-mysql-with-eclipse
public class PruebaConexion {

    public static void main(String[] args) throws SQLException { // El metodo que se quiera implementar para la conexion
        // Debe arrojar una excepción 'SQLException' ya que .getConnection() puede tirar dicha excepción.
        /*
            DriverManager.getConnection() retorna la conexión representada en el tipo 'Connection', esta tiene 3 parametros
            1 -> url de la conexión
                - La URL se debe formar de la siguiente manera:
                jdbc:<<VENDOR>>://<<HOST>>:<<PUERTO>>/<<BASE_DATOS_NOMBRE>>?useTimeZone=true&serverTimeZone=UTC
                Los ultimos parámetros son opcionales pero recomendables y no se ingresa el puerto ya que en este caso es localhost
            2 -> user - Nombre del usuario
            3 -> password - Contraseña del usuario
                - Recordar no compartir credenciales, usar variables de entorno
         */
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC",
                "root",
                System.getenv("DB_PASSWORD")
        );

        // Es importante cuando se abre una conexión, cerrarla inmediatamente
        connection.close(); // Connection.close() permite cerrar la conexión creada con la base de datos
    }

}