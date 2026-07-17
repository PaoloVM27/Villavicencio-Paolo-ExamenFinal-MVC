package org.unisiga.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.unisiga.model.*;
import org.unisiga.view.InscripcionView;
import org.unisiga.view.MenuPrincipalView;

/**
 * Controlador de lógica de negocio transaccional. Simula llamadas e interacciones de base de datos.
 */
public class InscripcionController {
    private List<Estudiante> estudiantesDb;
    private List<Asignatura> asignaturasDb;

    private InscripcionView vista;
    private MenuPrincipalView menuPrincipal;

    public InscripcionController() {
        this.estudiantesDb = new ArrayList<>();
        this.asignaturasDb = new ArrayList<>();
    }

    public void vincularVista(InscripcionView vista, MenuPrincipalView menuPrincipal) {
        this.vista = vista;
        this.menuPrincipal = menuPrincipal;
        configurarVentana();
        registrarEventos();
    }

    private void configurarVentana() {
        vista.setSize(650, 500);
        vista.setLocationRelativeTo(null);
    }

    private void registrarEventos() {
        vista.jButton1.addActionListener(e -> procesarInscripcion());
        vista.jButton2.addActionListener(e -> volver());
    }

    private void procesarInscripcion() {
        String matricula        = vista.txtMatricula.getText().trim();
        String codigoAsignatura = vista.txtCodigoAsignatura.getText().trim();
        String grupoTexto       = vista.txtIdGrupo.getText().trim();

        if (matricula.isEmpty() || codigoAsignatura.isEmpty() || grupoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Todos los campos son obligatorios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (grupoTexto.length() != 1) {
            JOptionPane.showMessageDialog(vista,
                    "La sección debe ser un solo carácter (ej: A).",
                    "Dato inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        char idGrupo = Character.toUpperCase(grupoTexto.charAt(0));
        String resultado = inscribirSeccionEstudiante(matricula, codigoAsignatura, idGrupo);

        if (resultado.startsWith("OK")) {
            JOptionPane.showMessageDialog(vista,
                    resultado.substring(4),
                    "Inscripción Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista,
                    resultado.substring(7),
                    "Error de Inscripción",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vista.txtMatricula.setText("");
        vista.txtCodigoAsignatura.setText("");
        vista.txtIdGrupo.setText("");
        vista.txtMatricula.requestFocus();
    }

    private void volver() {
        vista.dispose();
        menuPrincipal.setVisible(true);
    }

    // Métodos de sembrado (seeding) de bases de datos
    public void registrarEstudianteEnDb(Estudiante e) { estudiantesDb.add(e); }
    public void registrarAsignaturaEnDb(Asignatura a) { asignaturasDb.add(a); }

    /**
     * Procesa la solicitud de inscripción de asignaturas.
     * [LÓGICA]: 
     * 1. Buscar estudiante y asignatura.
     * 2. Obtener el grupo solicitado por composición.
     * 3. VALIDAR PRERREQUISITOS (Auto-asociación): El alumno debe tener aprobado el prerrequisito en su historial.
     * 4. Delegar la transacción al dominio del modelo.
     */
    public String inscribirSeccionEstudiante(String matricula, String codigoAsignatura, char idGrupo) {
        Estudiante estudiante = null;
        for (Estudiante e : estudiantesDb) {
            if (e.getMatricula().equals(matricula)) {
                estudiante = e;
                break;
            }
        }
        if (estudiante == null) {
            return "ERROR: No se encontro un estudiante con matricula " + matricula + ".";
        }

        Asignatura asignatura = null;
        for (Asignatura a : asignaturasDb) {
            if (a.getCodigo().equals(codigoAsignatura)) {
                asignatura = a;
                break;
            }
        }
        if (asignatura == null) {
            return "ERROR: No se encontro la asignatura con codigo " + codigoAsignatura + ".";
        }

        Seccion seccion = null;
        for (Seccion s : asignatura.getSecciones()) {
            if (s.getIdGrupo() == idGrupo) {
                seccion = s;
                break;
            }
        }
        if (seccion == null) {
            return "ERROR: No se encontro la seccion " + idGrupo + " en la asignatura " + codigoAsignatura + ".";
        }

        for (Asignatura prereq : asignatura.getPrerrequisitos()) {
            boolean aprobado = false;
            for (Inscripcion insc : estudiante.getInscripciones()) {
                if (insc.getSeccion().getAsignatura().equals(prereq)
                        && insc.getEstadoInscripcion().equals("Aprobado")) {
                    aprobado = true;
                    break;
                }
            }
            if (!aprobado) {
                return "ERROR: El estudiante no ha aprobado el prerrequisito: " + prereq.getNombre() + ".";
            }
        }

        try {
            estudiante.inscribirSeccion(seccion);
            return "OK: " + estudiante.getNombre() + " inscrito en " + asignatura.getNombre() + " - Seccion " + idGrupo + ".";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }
}

