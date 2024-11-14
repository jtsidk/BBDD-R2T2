package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeloAlumnosJDBC implements IModeloAlumnos {

    // Cambiamos la URL a H2
    private static String cadenaConexion = "jdbc:h2:./data/alumnosdb";
    private static String user = "sa";
    private static String pass = "";
    private static Connection conexion;

    public ModeloAlumnosJDBC() {
        try {
            conexion = DriverManager.getConnection(cadenaConexion, user, pass);
            inicializarBaseDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Crear la tabla si no existe
    private void inicializarBaseDatos() {
        String crearTabla = "CREATE TABLE IF NOT EXISTS alumnos (" +
                            "dni VARCHAR(20) PRIMARY KEY," +
                            "nombre VARCHAR(50)," +
                            "apellidos VARCHAR(50)," +
                            "cp VARCHAR(10))";
        try (PreparedStatement ps = conexion.prepareStatement(crearTabla)) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAll() {
        List<String> alumnos = new ArrayList<>();
        String consulta = "SELECT * FROM alumnos";

        try (PreparedStatement ps = conexion.prepareStatement(consulta);
             ResultSet res = ps.executeQuery()) {

            while (res.next()) {
                String alumno = res.getString("dni") + " -- " +
                                res.getString("nombre") + " -- " +
                                res.getString("apellidos") + " -- " +
                                res.getString("cp");
                alumnos.add(alumno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alumnos;
    }

    @Override
    public Alumno getAlumnoPorDNI(String DNI) {
        String consulta = "SELECT * FROM alumnos WHERE dni = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, DNI);
            ResultSet resultado = sentencia.executeQuery();

            Alumno alumno = null;
            if (resultado.next()) {
                alumno = new Alumno();
                alumno.setDNI(resultado.getString("dni"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setApellidos(resultado.getString("apellidos"));
                alumno.setCP(resultado.getString("cp"));
            }
            return alumno;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean modificarAlumno(Alumno alumno) {
        String consulta = "UPDATE alumnos SET nombre = ?, apellidos = ?, cp = ? WHERE dni = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidos());
            sentencia.setString(3, alumno.getCP());
            sentencia.setString(4, alumno.getDNI());

            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminarAlumno(String DNI) {
        String consulta = "DELETE FROM alumnos WHERE dni = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, DNI);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean crear(Alumno alumno) {
        String consulta = "INSERT INTO alumnos (dni, nombre, apellidos, cp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, alumno.getDNI());
            sentencia.setString(2, alumno.getNombre());
            sentencia.setString(3, alumno.getApellidos());
            sentencia.setString(4, alumno.getCP());
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
