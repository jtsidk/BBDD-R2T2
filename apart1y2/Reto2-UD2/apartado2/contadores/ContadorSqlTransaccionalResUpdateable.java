package contadores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ContadorSqlTransaccionalResUpdateable {

    public static void main(String[] args) throws ClassNotFoundException {
        // Consulta SQL que selecciona la fila con bloqueo de actualización en H2
        String sqlConsulta = "SELECT nombre, cuenta FROM contadores WHERE nombre='contador1' FOR UPDATE;";

        // Cargar el controlador JDBC de H2
        Class.forName("org.h2.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:h2:./contadores", "sa", "")) {
            // crear la tabla si no existe
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS contadores (nombre VARCHAR(255) PRIMARY KEY, cuenta INT);");

                // Insertar un contador inicial si no existe
                stmt.execute("MERGE INTO contadores KEY(nombre) VALUES ('contador1', 0);");
            }

            PreparedStatement consulta = connection.prepareStatement(sqlConsulta, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            int cuenta = 0;

            for (int i = 0; i < 1000; i++) {
                connection.setAutoCommit(false); // desactivar auto-commit para manejar la transaccion manualmente
                ResultSet res = consulta.executeQuery();

                if (res.next()) {
                    cuenta = res.getInt("cuenta");
                    cuenta++;

                    // Actualizar el valor de cuenta directamente en el ResultSet
                    res.updateInt("cuenta", cuenta);
                    res.updateRow(); // reflejar el cambio en la base de datos
                } else break;

                connection.commit(); // confirmar la transaccio
                connection.setAutoCommit(false);
            }

            System.out.println("Valor final: " + cuenta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

