package transacciones;

import java.sql.*;

public class Banco {
    public final Cuenta[] cuentas;
    private final int saldoInicial;
    private final int númeroDeCuentas;
    private boolean abierto;
    private int cont = 0;
    
    //Variables para h2
    private static final String URL = "jdbc:h2:./data/banco"; //Creamos una base de datos llamada banco
    //private static final String URL = "jdbc:h2:mem:testdb";
    private static final String user = "sa";
    private static final String password = "";

    public Banco(int numCuentas, int saldoInicial) {
        this.abierto = true;
    	this.saldoInicial = saldoInicial;
        this.númeroDeCuentas = numCuentas;
        cuentas = new Cuenta[numCuentas];
        for (int i = 0; i < cuentas.length; i++) {
            cuentas[i] = new Cuenta(i, saldoInicial);
        }
    }
    
    
    public void transfiere(int origen, int destino, int cantidad) {
  	
    	try (Connection conn = DriverManager.getConnection(URL,user,password)) {
    		conn.setAutoCommit(false);
	    	String queryRetirar = "update clientes set saldo = saldo-? where id = ? and saldo >= ?";
	    	String queryIngresar = "update clientes set saldo = saldo+? where id = ? ";
	    	try(PreparedStatement statementRetirar = conn.prepareStatement(queryRetirar);
	    			PreparedStatement statementIngresar = conn.prepareStatement(queryIngresar)) {
	    		
	    		statementRetirar.setInt(1, cantidad);
	    		statementRetirar.setInt(2, origen);
	    		statementRetirar.setInt(3, cantidad);
	    		
	    		int filasAfectadas = statementRetirar.executeUpdate();
	    		
	    		if (filasAfectadas == 0) {
	    			System.out.println("Fondos insuficientes en la cuenta");
	    			conn.rollback();
	    			cont++;
	    			return;
	    		}
	    		
	    		statementIngresar.setInt(1, cantidad);
	    		statementIngresar.setInt(2, destino);
	    		statementIngresar.executeUpdate();
	    		
	    		conn.commit();
	    		System.out.println("Transferencia completada\nOrigen:" + origen + "\nDestino: " + destino + "\nCantidad: " + cantidad);
	    		cont++;
	    		System.out.println("Numero de transferencia = " + cont);
	    	}catch (SQLException e) {
	    		conn.rollback();
	    		e.printStackTrace();
	    	}
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    public void comprueba() {
   	
    	String query = "select sum(saldo) as totalSaldo from clientes";
    	
    	try(Connection conn = DriverManager.getConnection(URL, user,password); 
    		PreparedStatement statement = conn.prepareStatement(query)) {
    		ResultSet rs = statement.executeQuery();
    		
    		if (rs.next()) {
    			int saldoTotal = rs.getInt("totalSaldo");
    			System.out.println("-------------------------------------------");
    			System.out.println("Saldo Total: " + saldoTotal);
    			
    			if(saldoTotal==100000) {
    				System.out.println("Saldo correcto");
    				System.out.println("-------------------------------------------");
    			}else {
    				System.out.println("¡¡¡¡¡No cuadran las cuentas!!!!");
    			}
    		}
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    } // comprueba

    public int getNúmeroDeCuentas() {
        return númeroDeCuentas;
    }

    boolean abierto() {
        return abierto;
    }

    void cierraBanco() {
    	abierto = false;
    }
}
