package control;

import modelo.BD_Agenda;
import modelo.Contacto;

import java.util.List;
import java.util.Scanner;

public class PruebaFicheroAgenda {
    public static void main(String[] args) {
        BD_Agenda agenda = new BD_Agenda();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- Menú de Agenda ---");
            System.out.println("1. Ver todos los contactos");
            System.out.println("2. Añadir un nuevo contacto");
            System.out.println("3. Borrar un contacto");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 	

            switch (opcion) {
                case 1:
                    mostrarContactos(agenda);
                    break;
                case 2:
                    agregarContacto(agenda, scanner);
                    break;
                case 3:
                    eliminarContacto(agenda, scanner);
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opcion invalida, por favor intente de nuevo.");
            }
        } while (opcion != 4);

        scanner.close();
    }

    private static void mostrarContactos(BD_Agenda agenda) {
        System.out.println("\nLista de contactos:");
        List<Contacto> contactos = agenda.leeContactos();
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos en la agenda!!");
        } else {
            for (Contacto contacto : contactos) {
                System.out.println(contacto);
            }
        }
    }

    private static void agregarContacto(BD_Agenda agenda, Scanner scanner) {
        System.out.print("Ingrese el nombre del nuevo contacto: ");
        String nombre = scanner.nextLine();
        System.out.print("ingresar el telefono del nuevo contacto: ");
        String telefono = scanner.nextLine();
        
        Contacto nuevoContacto = new Contacto(nombre, telefono);
        if (agenda.escribeRegistro(nuevoContacto)) {
            System.out.println("contacto añadido correctamente.");
        } else {
            System.out.println("error al añadir el contacto.");
        }
    }

    private static void eliminarContacto(BD_Agenda agenda, Scanner scanner) {
        System.out.print("ingrese el nombre del contacto a borrar: ");
        String nombre = scanner.nextLine();
        System.out.print("ingresar telefono del contacto a borrar: ");
        String telefono = scanner.nextLine();
        
        Contacto contacto = new Contacto(nombre, telefono);
        if (agenda.borraRegistro(contacto)) {
            System.out.println("Contacto borrado correctamente.");
        } else {
            System.out.println("error al borrar el contacto. asegurate de que el contacto existe.");
        }
    }
}
