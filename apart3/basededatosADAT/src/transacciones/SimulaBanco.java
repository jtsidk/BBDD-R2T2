package transacciones;

public class SimulaBanco {

	public static final int NÚMERO_DE_CUENTAS = 10;
	public static final int SALDO_INICIAL = 10000;
	public static final int NÚMERO_DE_TRANSFERENCIAS = 1000;

	public static void main(String[] args) throws InterruptedException {
		Banco banco = new Banco(NÚMERO_DE_CUENTAS, SALDO_INICIAL);

		banco.comprueba();
		// Ejecuta transferencias
		ejecutaTransferencias(banco,NÚMERO_DE_TRANSFERENCIAS, (SALDO_INICIAL/NÚMERO_DE_CUENTAS)); 

		System.out.println("Terminadas todas las transferencias");

		// Comprobación:
		banco.comprueba();

	}

	public static void ejecutaTransferencias(Banco banco, int numTransferencias, int cantidadMáxima ) {
		for (int i = 0; i < numTransferencias; i++) {
			int cuentaOrigen =  (int) (banco.getNúmeroDeCuentas() * Math.random());
			int cuentaDestino = (int) (banco.getNúmeroDeCuentas() * Math.random());
			int cantidad = (int) (cantidadMáxima * Math.random());
			banco.transfiere(cuentaOrigen, cuentaDestino, cantidad);
			
			if (i%100==0 && i!=0) {
				banco.comprueba();
			}
		}
	}
}
