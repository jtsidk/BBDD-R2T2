package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BD_Agenda {
    private static final String URL = "jdbc:sqlite:ficheroAgenda.db"; // Ruta para SQLite

    // Constructor para inicializar la base de datos y crear la tabla si no existe
    public BD_Agenda() {
        inicializarBaseDatos();
    }

    // Método para inicializar la base de datos y crear la tabla 'contactos'
    private void inicializarBaseDatos() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS contactos (" +
                                  "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                  "nombre TEXT NOT NULL," +
                                  "telefono TEXT NOT NULL)";
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(createTableQuery)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lee todos los contactos de la base de datos
    public List<Contacto> leeContactos() {
        List<Contacto> contactos = new ArrayList<>();
        String query = "SELECT nombre, telefono FROM contactos";
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                contactos.add(new Contacto(nombre, telefono));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactos;
    }

    // Escribe un nuevo registro de contacto en la base de datos
    public boolean escribeRegistro(Contacto contacto) {
        String query = "INSERT INTO contactos (nombre, telefono) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, contacto.getNombre());
            ps.setString(2, contacto.getTeléfono());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borra un registro de contacto de la base de datos
    public boolean borraRegistro(Contacto contacto) {
        String query = "DELETE FROM contactos WHERE nombre = ? AND telefono = ?";
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, contacto.getNombre());
            ps.setString(2, contacto.getTeléfono());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
