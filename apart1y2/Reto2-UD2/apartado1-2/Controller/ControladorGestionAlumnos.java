package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Model.Alumno;
import Model.IModeloAlumnos;
import UI.VentanaAlumnos;

public class ControladorGestionAlumnos implements ActionListener, ListSelectionListener {

    private IModeloAlumnos model;
    private VentanaAlumnos view;

    public ControladorGestionAlumnos(IModeloAlumnos model, VentanaAlumnos view) {
        this.model = model;
        this.view = view;
        anadirListeners(this);

        this.view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.view.pack();
        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    private void anadirListeners(ControladorGestionAlumnos controladorGestionAlumnos) {
        view.btnCargarTodos.addActionListener(controladorGestionAlumnos);
        view.btnCrear.addActionListener(controladorGestionAlumnos);
        view.btnModificar.addActionListener(controladorGestionAlumnos);
        view.btnEliminar.addActionListener(controladorGestionAlumnos);

        view.jListaAlumnos.addListSelectionListener(controladorGestionAlumnos);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        System.out.println("Estoy en actionPerformed con la opción " + actionCommand);

        switch (actionCommand) {
            case "Cargar Todos":
                cargarTodosAlumnos();
                break;
            case "Crear Nuevo":
                boolean guardado = guardarAlumno();
                if (guardado) {
                    JOptionPane.showMessageDialog(null, "insertado correctamente el alumno", "Insercion correcta", 1);
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "ha habido un problema en la insercion", "error insercion!", 0);
                }
                break;
            case "Modificar":
                boolean modificado = modificarAlumno();
                if (modificado) {
                    JOptionPane.showMessageDialog(null, "se ha modificado correctamente el alumno", "modificacion correcta", 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Ha habido un problema en la modificacion", "Error modificacion", 0);
                }
                break;
            case "Eliminar":
                boolean eliminado = eliminarAlumno();
                if (eliminado) {
                    JOptionPane.showMessageDialog(null, "Se ha eliminado correctamente el alumno", "eliminacion correcta", 1);
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "Ha habido un problema en la eliminación, no existe un alumno con ese DNI", "error eliminacio!", 0);
                }
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) { // evitar eventos duplicados
            String selectedValue = view.jListaAlumnos.getSelectedValue();
            if (selectedValue != null) {
                String dni = selectedValue.split(" -- ")[0]; // asumiendo que el DNI es la primera parte
                Alumno alumno = model.getAlumnoPorDNI(dni);
                cargarAlumno(alumno);
            }
        }
    }

    private boolean guardarAlumno() {
        String dni = view.textFieldDNI.getText();
        String nombre = view.textFieldNombre.getText();
        String apellidos = view.textFieldApellidos.getText();
        String cp = view.textFieldCP.getText();

        Alumno alumno = new Alumno();
        alumno.setDNI(dni);
        alumno.setNombre(nombre);
        alumno.setApellidos(apellidos);
        alumno.setCP(cp);

        return model.crear(alumno);
    }

    private boolean modificarAlumno() {
        String dni = view.textFieldDNI.getText();
        String nombre = view.textFieldNombre.getText();
        String apellidos = view.textFieldApellidos.getText();
        String cp = view.textFieldCP.getText();

        Alumno alumno = new Alumno();
        alumno.setDNI(dni);
        alumno.setNombre(nombre);
        alumno.setApellidos(apellidos);
        alumno.setCP(cp);

        return model.modificarAlumno(alumno);
    }

    private boolean eliminarAlumno() {
        String dni = view.textFieldDNI.getText();
        return model.eliminarAlumno(dni);
    }

    private void limpiarCampos() {
        view.textFieldDNI.setText("");
        view.textFieldNombre.setText("");
        view.textFieldApellidos.setText("");
        view.textFieldCP.setText("");
    }

    private void cargarAlumno(Alumno alumno) {
        if (alumno == null) {
            limpiarCampos();
        } else {
            view.textFieldDNI.setText(alumno.getDNI());
            view.textFieldNombre.setText(alumno.getNombre());
            view.textFieldApellidos.setText(alumno.getApellidos());
            view.textFieldCP.setText(alumno.getCP());
        }
    }

    private void cargarTodosAlumnos() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (String alumno : model.getAll()) {
            modelo.addElement(alumno);
        }
        view.jListaAlumnos.setModel(modelo);
    }
}
