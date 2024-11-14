package pruebas;

import java.sql.*;

public class Pruebajdbc {

	public static void main(String[] args) {
		String user = "dam2";
		String password = "asdf.1234";
		String url = "jdbc:mysql://localhost:3306/naciones";
		
		try (Connection conn = DriverManager.getConnection(url,user, password);){
			System.out.println("Conectado correctamente");
			//añadirPais(conn, "Pais inventado", 110.10, "XX", "XXX");
			//infoPais(conn,"Pais inventado");
			//modificarPais(conn,"Pais inventado","Pais_inventado");
			eliminarPais(conn,"Pais_inventado");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Hacer un CRUD para una base de datos a nuestra eleccion (naciones en este caso) 
	 */
	
	/*
	 * CREATE: Metodo para crear (añadir) un pais a la tabla ya existente
	 */
	public static void añadirPais(Connection conn, String nombre, Double area, String code2, String code3) throws SQLException{
		String query = "insert into continents (name) values (?)";
		PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, "pruebas");
		
		int filasAfectadas = statement.executeUpdate();
		int continent_id = -1;
		
		if (filasAfectadas > 0) {
			ResultSet PK_Generada = statement.getGeneratedKeys();
			if (PK_Generada.next()) {
				continent_id = PK_Generada.getInt(1);
				System.out.println("Id Continente: " + continent_id);
				System.out.println("Id Continente conseguido");
			}
			PK_Generada.close();
		}
		statement.close();
		
		if (continent_id == -1) {
			System.out.println("No se ha podido obtener el continent_id");
			return;
		}
		
		
		query = "insert into regions (name, continent_id) values (?,?)";
		statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, "pruebas");
		statement.setInt(2, continent_id);
		
		filasAfectadas = statement.executeUpdate();
		int region_id = -1;
		
		if (filasAfectadas > 0) {
			ResultSet PK_regions = statement.getGeneratedKeys();
			if (PK_regions.next()) {
				region_id = PK_regions.getInt(1);
				System.out.println("Id Region: " + region_id);
				System.out.println("ID region conseguido");
			}
			PK_regions.close();
		}
		statement.close();
		
		if (region_id == -1) {
			System.out.println("No se ha podido obtener el region_id");
			return;
		}
		
		//String query = "insert into countries (country_id, name, area, national_day, country_code2, country_code3, region_id)";
		query = "insert into countries (name, area, country_code2, country_code3, region_id) values (?,?,?,?,?)";
		statement = conn.prepareStatement(query);
		statement.setString(1,nombre);
		statement.setDouble(2, area);
		statement.setString(3, code2);
		statement.setString(4, code3);
		statement.setInt(5, region_id);
		
		filasAfectadas = statement.executeUpdate();
		
		if (filasAfectadas>0) {
			System.out.println("El pais se ha añadido correctamente");
		}else {
			System.out.println("No se ha podido añadido correctamente");
		}
		
		//Acordarnos de cerrar el flujo para liberar recursos
		statement.close();
	}
	/*
	 * READ: Metodo para poder leer todos los paises con el area menor de la cifra introducida 
	 */
	public static void infoPais(Connection conn, String nombre) throws SQLException{
		String query = "select country_id, name, area from countries where name = ?";
		PreparedStatement sql = conn.prepareStatement(query);
		sql.setString(1,nombre);
		ResultSet resultado = sql.executeQuery();
		
		//variables necesarias para la obtencion de los datos
		int idPais = 0;
		String nombrePais = "";
		Double areaPais = 0.0;
		boolean encontrado = false;
		
		while(resultado.next()) {
			idPais = resultado.getInt(1);
			nombrePais = resultado.getString(2);
			areaPais = resultado.getDouble(3);
			
			System.out.println("Id del pais: " + idPais);
			System.out.println("Nombre del pais: " + nombrePais);
			System.out.println("Area del pais: " + areaPais);
			System.out.println("--------------------------------------------------------");
			
			encontrado = true;
		}
		
		if (!encontrado) {
			System.out.println("No se ha podido encontrar el pais");
		}
		
		resultado.close();
		sql.close();
	}
	
	/*
	 * UPDATE: Metodo para poder modificar el nombre de un Pais
	 */
	public static void modificarPais(Connection conn, String nombre_antiguo, String nombre_nuevo) throws SQLException{
		String query = "update countries set name = ? where name = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, nombre_nuevo);
		statement.setString(2, nombre_antiguo);
		
		int filasAfectadas = statement.executeUpdate();
		
		if(filasAfectadas>0) {
			System.out.println("Se ha cambiado el nombre del pais de " + nombre_antiguo + " a " + nombre_nuevo);
		}else {
			System.out.println("No se ha podido encontrar el pais " + nombre_antiguo);
		}
		
		statement.close();
	}
	
	/*
	 * DELETE: Metodo para eliminar un pais recibiendo como parametro su nombre
	 */
	public static void eliminarPais(Connection conn, String nombre) throws SQLException{
		String query = "delete from countries where name = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, nombre);
		
		int filasAfectadas = statement.executeUpdate();
		
		if(filasAfectadas > 0) {
			System.out.println("El pais ha sido eliminado correctamente.");
		}else {
			System.out.println("El pais no ha podido ser eliminados correctamente.");
		}
		
		statement.close();
	} 
	
}
