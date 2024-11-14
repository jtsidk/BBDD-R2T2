package contadores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContadorSqlTransaccional {

    public static void main(String[] args) throws ClassNotFoundException {
        // Prueba de concepto de transacción con bloqueo de fila para lectura
        // Sería más fácil en el propio sql poner un set cuenta=cuenta+1 pero ilustramos
        // aquí el problema de concurrencia entre varios procesos.
        // con el for update + transacción conseguimos el bloque de fila y atomicidad
        String sqlConsulta = "select nombre,cuenta from contadores where nombre='contador1' for update;";
        String sqlActualización = "update contadores set cuenta=? where nombre='contador1';";

        // Cargar el controlador JDBC de H2
        Class.forName("org.h2.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:h2:./contadores", "sa", "")) {
            PreparedStatement consulta = connection.prepareStatement(sqlConsulta);
            PreparedStatement actualización = connection.prepareStatement(sqlActualización);
            int cuenta = 0;

            for (int i = 0; i < 1000; i++) {
                connection.setAutoCommit(false); // desactivar auto-commit para manejar la transacción manualmente
                ResultSet res = consulta.executeQuery();
                if (res.next()) {
                    cuenta = res.getInt(2); // usamos la segunda columna del ResultSet, como en MariaDB
                    cuenta++;
                    actualización.setInt(1, cuenta);
                    actualización.executeUpdate();
                } else {
                    break;
                }

                connection.commit(); // confirmar la transacción
                connection.setAutoCommit(false); 
            }

            System.out.println("Valor final: " + cuenta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

