package contadores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContadorBuggy {
    static final String SQL_CONSULTA = "SELECT cuenta FROM contadores WHERE nombre = 'contador1'";
    static final String SQL_ACTUALIZA = "UPDATE contadores SET cuenta = ? WHERE nombre = 'contador1'";

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:./contadores", "sa", "")) {
            
            // Crear tabla y registro inicial si no existen
            try (Statement stmt = con.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS contadores (nombre VARCHAR(255) PRIMARY KEY, cuenta INT);");
                stmt.execute("MERGE INTO contadores KEY(nombre) VALUES ('contador1', 0);");
            }

            int cuenta = 0;
            for (int i = 1; i <= 1000; i++) {
                Statement consulta = con.createStatement();
                PreparedStatement actualiza = con.prepareStatement(SQL_ACTUALIZA);
                
                ResultSet res = consulta.executeQuery(SQL_CONSULTA);
                if (res.next()) {
                    cuenta = res.getInt(1) + 1;
                }
                actualiza.setInt(1, cuenta);
                actualiza.executeUpdate();
                
                consulta.close();  // Cerrar el Statement para liberar recursos
                actualiza.close(); // Cerrar el PreparedStatement
            }
            System.out.println("Valor final: " + cuenta);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
