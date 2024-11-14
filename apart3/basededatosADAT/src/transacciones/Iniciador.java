package transacciones;

import java.sql.*;

public class Iniciador {
	
	public static void main(String[] args) {
		
		String URL = "jdbc:h2:./data/banco";
		String user = "sa";
		String password = "";
		
		try(Connection conn = DriverManager.getConnection(URL, user, password)) {
			System.out.println("Conectado correctamente a la base de datos embebida");
			crearBaseDeDatos(conn);
			insertarDatosClientes(conn);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void crearBaseDeDatos(Connection conn) {
		String query = "create table if not exists clientes (id INT PRIMARY KEY, saldo INT)";
		try (PreparedStatement st = conn.prepareStatement(query)) {
			st.execute(query);
			System.out.println("Tabla clientes creada o ya existe.");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertarDatosClientes(Connection conn) {
		String query = "merge into clientes (id, saldo) values (?, ?)";
		try(PreparedStatement statement = conn.prepareStatement(query)) {
			for (int i=0; i<10; i++) {
				statement.setInt(1, i);
				statement.setInt(2, 10000);
				statement.executeUpdate();
			}
			System.out.println("datos de cliente insertados o actualizados");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
