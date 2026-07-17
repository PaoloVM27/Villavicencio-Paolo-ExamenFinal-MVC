package org.unisiga.main;

import java.awt.EventQueue;
import org.unisiga.controller.InscripcionController;
import org.unisiga.controller.MenuPrincipalController;
import org.unisiga.model.*;
import org.unisiga.view.ConsoleView;
import org.unisiga.view.MenuPrincipalView;

public class Main {
    public static void main(String[] args) {

        ConsoleView consola = new ConsoleView();
        InscripcionController controller = new InscripcionController();

        consola.desplegarMenu();

        Asignatura prog01 = new Asignatura("PROG01", "Programacion Basica", 6);
        Asignatura alg01  = new Asignatura("ALG01",  "Algebra Lineal",      5);
        Asignatura poo01  = new Asignatura("POO01",  "POO",                 6);

        poo01.agregarPrerrequisito(prog01);

        controller.registrarAsignaturaEnDb(prog01);
        controller.registrarAsignaturaEnDb(alg01);
        controller.registrarAsignaturaEnDb(poo01);

        Seccion seccionProg01A = prog01.crearSeccion('A', 30, "Lunes 10:00-12:00");
        prog01.crearEvaluacion(1, "Control 1",    0.3f);
        prog01.crearEvaluacion(2, "Examen Final", 0.7f);

        Seccion seccionAlg01A  = alg01.crearSeccion('A', 25, "Martes 14:00-16:00");
        alg01.crearEvaluacion(1, "Prueba 1",     0.4f);
        alg01.crearEvaluacion(2, "Examen Final", 0.6f);

        Seccion seccionPoo01A  = poo01.crearSeccion('A', 20, "Miercoles 08:00-10:00");
        poo01.crearEvaluacion(1, "Tarea 1",        0.2f);
        poo01.crearEvaluacion(2, "Proyecto Final", 0.8f);

        Departamento deptoComp = new Departamento("DCOMP", "Departamento de Computacion");
        Academico docente = new Academico("12345678-9", "Dr. Torres", "torres@unisiga.cl", "EMP001", "Planta");
        deptoComp.asociarAcademico(docente);

        seccionProg01A.asignarDocente(docente);
        seccionAlg01A.asignarDocente(docente);
        seccionPoo01A.asignarDocente(docente);

        Estudiante juan  = new Estudiante("11111111-1", "Juan Perez",     "juan@unisiga.cl",  "2023001", 2023, 5.5f);
        Estudiante maria = new Estudiante("22222222-2", "Maria Gonzalez", "maria@unisiga.cl", "2023002", 2023, 4.8f);

        juan.inscribirSeccion(seccionProg01A);
        juan.getInscripciones().get(0).setEstadoInscripcion("Aprobado");

        controller.registrarEstudianteEnDb(juan);
        controller.registrarEstudianteEnDb(maria);

        consola.mostrarMensajeProcesamiento("Intentando inscribir a Juan en POO - Seccion A...");
        String resultadoJuan = controller.inscribirSeccionEstudiante("2023001", "POO01", 'A');
        consola.mostrarMensajeProcesamiento(resultadoJuan);
        if (resultadoJuan.startsWith("OK")) {
            consola.imprimirComprobante(juan.getNombre(), poo01.getNombre(), 'A');
        }

        consola.mostrarMensajeProcesamiento("Intentando inscribir a Maria en POO - Seccion A...");
        String resultadoMaria = controller.inscribirSeccionEstudiante("2023002", "POO01", 'A');
        consola.mostrarMensajeProcesamiento(resultadoMaria);
        if (resultadoMaria.startsWith("OK")) {
            consola.imprimirComprobante(maria.getNombre(), poo01.getNombre(), 'A');
        }

        EventQueue.invokeLater(() -> {
            MenuPrincipalView vista = new MenuPrincipalView();
            new MenuPrincipalController(vista);
            vista.setVisible(true);
        });
    }
}

