package contadores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContadorEnBDUpdatable {

    public static void main(String[] args) {
        final String claveContador = "contador1";
        final String sqlConsulta = "SELECT nombre, cuenta FROM contadores WHERE nombre = ?";

        try {
            // Cargar el controlador JDBC de H2
            Class.forName("org.h2.Driver");

            // Establecer conexión con la base de datos H2
            Connection connection = DriverManager.getConnection("jdbc:h2:./contadores", "sa", "");

            // Crear la tabla y el contador inicial si no existen
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS contadores (nombre VARCHAR(255) PRIMARY KEY, cuenta INT);");
                stmt.execute("MERGE INTO contadores KEY(nombre) VALUES ('contador1', 0);");
            }

            // Preparar la consulta con capacidad de actualización
            PreparedStatement consulta = connection.prepareStatement(sqlConsulta, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            consulta.setString(1, claveContador);
            int cuenta = 0;

            for (int i = 0; i < 1000; i++) {
                if (consulta.execute()) {
                    ResultSet res = consulta.getResultSet();
                    if (res.next()) {
                        cuenta = res.getInt("cuenta") + 1;
                        res.updateInt("cuenta", cuenta);
                        res.updateRow();
                    } else {
                        System.out.println("Error: Contador no encontrado.");
                        break;
                    }
                }
            }
            System.out.println("Valor final: " + cuenta);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
