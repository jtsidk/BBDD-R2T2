package contadores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContadorEnBD {

    public static void main(String[] args) {
        final String sqlConsulta = "SELECT cuenta FROM contadores WHERE nombre = ?;";
        final String sqlActualización = "UPDATE contadores SET cuenta = ? WHERE nombre = ?;";
        final String claveContador = "contador1";

        try {
            // Conectar a la base de datos H2
            Connection connection = DriverManager.getConnection("jdbc:h2:./contadores", "sa", "");

            // Crear tabla y registro inicial si no existen
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS contadores (nombre VARCHAR(255) PRIMARY KEY, cuenta INT);");
                stmt.execute("MERGE INTO contadores KEY(nombre) VALUES ('contador1', 0);");
            }

            PreparedStatement consulta = connection.prepareStatement(sqlConsulta);
            PreparedStatement actualización = connection.prepareStatement(sqlActualización);
            int cuenta = 0;

            // Asignar el valor del parámetro para claveContador
            consulta.setString(1, claveContador);
            actualización.setString(2, claveContador);

            for (int i = 0; i < 1000; i++) {
                ResultSet res = consulta.executeQuery();
                if (res.next()) {
                    cuenta = res.getInt(1) + 1;
                    actualización.setInt(1, cuenta);
                    actualización.executeUpdate();
                } else {
                    System.out.println("Error: no se encontró el registro.");
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
