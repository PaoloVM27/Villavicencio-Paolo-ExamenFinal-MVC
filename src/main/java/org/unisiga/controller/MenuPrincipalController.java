package org.unisiga.controller;

import javax.swing.JOptionPane;
import org.unisiga.model.*;
import org.unisiga.view.InscripcionView;
import org.unisiga.view.MenuPrincipalView;

public class MenuPrincipalController {

    private final MenuPrincipalView vista;
    private final InscripcionController inscripcionController;

    public MenuPrincipalController(MenuPrincipalView vista) {
        this.vista = vista;
        this.inscripcionController = new InscripcionController();
        sembrarDatos();
        configurarVentana();
        registrarEventos();
    }

    private void configurarVentana() {
        vista.setSize(650, 500);
        vista.setLocationRelativeTo(null);
    }

    private void sembrarDatos() {
        Asignatura prog01 = new Asignatura("PROG01", "Programacion Basica", 6);
        Asignatura alg01  = new Asignatura("ALG01",  "Algebra Lineal",      5);
        Asignatura poo01  = new Asignatura("POO01",  "POO",                 6);

        poo01.agregarPrerrequisito(prog01);

        inscripcionController.registrarAsignaturaEnDb(prog01);
        inscripcionController.registrarAsignaturaEnDb(alg01);
        inscripcionController.registrarAsignaturaEnDb(poo01);

        Seccion seccionProg01A = prog01.crearSeccion('A', 30, "Lunes 10:00-12:00");
        prog01.crearEvaluacion(1, "Control 1",    0.3f);
        prog01.crearEvaluacion(2, "Examen Final", 0.7f);

        Seccion seccionAlg01A  = alg01.crearSeccion('A', 25, "Martes 14:00-16:00");
        alg01.crearEvaluacion(1, "Prueba 1",     0.4f);
        alg01.crearEvaluacion(2, "Examen Final", 0.6f);

        Seccion seccionPoo01A  = poo01.crearSeccion('A', 20, "Miercoles 08:00-10:00");
        poo01.crearEvaluacion(1, "Tarea 1",         0.2f);
        poo01.crearEvaluacion(2, "Proyecto Final",  0.8f);

        Departamento deptoComp = new Departamento("DCOMP", "Departamento de Computacion");
        Academico docente = new Academico("12345678-9", "Dr. Torres", "torres@unisiga.cl", "EMP001", "Planta");
        deptoComp.asociarAcademico(docente);

        seccionProg01A.asignarDocente(docente);
        seccionAlg01A.asignarDocente(docente);
        seccionPoo01A.asignarDocente(docente);

        Estudiante juan  = new Estudiante("11111111-1", "Juan Perez",      "juan@unisiga.cl",  "2023001", 2023, 5.5f);
        Estudiante maria = new Estudiante("22222222-2", "Maria Gonzalez",  "maria@unisiga.cl", "2023002", 2023, 4.8f);

        juan.inscribirSeccion(seccionProg01A);
        juan.getInscripciones().get(0).setEstadoInscripcion("Aprobado");

        inscripcionController.registrarEstudianteEnDb(juan);
        inscripcionController.registrarEstudianteEnDb(maria);
    }

    private void registrarEventos() {
        vista.btnInscripcion.addActionListener(e -> abrirInscripcion());
        vista.btnRegistrarNota.addActionListener(e -> abrirRegistrarNota());
        vista.btnGestionEstudiantes.addActionListener(e -> abrirGestionEstudiantes());
        vista.btnGestionAsignaturas.addActionListener(e -> abrirGestionAsignaturas());
        vista.btnSalir.addActionListener(e -> salir());
    }

    private void abrirInscripcion() {
        InscripcionView inscripcionVista = new InscripcionView();
        inscripcionController.vincularVista(inscripcionVista, vista);
        vista.setVisible(false);
        inscripcionVista.setVisible(true);
    }

    private void abrirRegistrarNota() {
        JOptionPane.showMessageDialog(vista,
                "Modulo de Registro de Notas — Proximamente",
                "UniSiga", JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirGestionEstudiantes() {
        JOptionPane.showMessageDialog(vista,
                "Modulo de Gestion de Estudiantes — Proximamente",
                "UniSiga", JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirGestionAsignaturas() {
        JOptionPane.showMessageDialog(vista,
                "Modulo de Gestion de Asignaturas — Proximamente",
                "UniSiga", JOptionPane.INFORMATION_MESSAGE);
    }

    private void salir() {
        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Desea salir del sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public InscripcionController getInscripcionController() {
        return inscripcionController;
    }
}
