package Controller;

import Model.IModeloAlumnos;
import Model.ModeloAlumnosJDBC;
import UI.VentanaAlumnos;

public class GestionAlumnos {

	public static void main(String[] args) {
		 try {
        	VentanaAlumnos view = new VentanaAlumnos();
        	IModeloAlumnos model = new ModeloAlumnosJDBC();
        	ControladorGestionAlumnos controller = new ControladorGestionAlumnos(model, view);
        	
        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
