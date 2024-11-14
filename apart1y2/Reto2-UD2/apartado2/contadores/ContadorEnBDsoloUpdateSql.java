package contadores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ContadorEnBDsoloUpdateSql {

    public static void main(String[] args) {
        final String claveContador = "contador1";
        // La actualización atómica en SQL
        final String sqlActualización = "UPDATE contadores SET cuenta = cuenta + 1 WHERE nombre = '" + claveContador + "';";

        try {
            // Cargar el controlador H2
            Class.forName("org.h2.Driver");

            // Establecer conexión con la base de datos H2
            Connection connection = DriverManager.getConnection("jdbc:h2:./contadores", "sa", "");

            // Crear la tabla y el registro inicial si no existen
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS contadores (nombre VARCHAR(255) PRIMARY KEY, cuenta INT);");
                stmt.execute("MERGE INTO contadores KEY(nombre) VALUES ('contador1', 0);");
            }

            // Preparar la actualización SQL
            PreparedStatement actualización = connection.prepareStatement(sqlActualización);

            for (int i = 0; i < 1000; i++) {
                if (actualización.executeUpdate() != 1) break;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
