package transacciones;

public class Cuenta {
    private int saldo;
    private final int id;
    
    public Cuenta(int id, int saldoInicial) {
        this.id = id;
        this.saldo = saldoInicial;
    }

    public int getSaldo() {
        return saldo;
    }

    public boolean retiraDinero(int cantidad) {
        if (cantidad <= saldo) {
            saldo = saldo - cantidad;
            return true;
        } else {
            return false;
        }
    }

    public void meteDinero(int cantidad) {
    	saldo = saldo + cantidad;
    }  
    
    public int getId() {
		return id;
	}

	@Override
    public String toString() {
        return String.format("Cuenta[%d] saldo %d", id, saldo);
    }
}