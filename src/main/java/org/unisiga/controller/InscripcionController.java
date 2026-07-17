package org.unisiga.controller;

import java.util.ArrayList;
import java.util.List;
import org.unisiga.model.*;

/**
 * Controlador de lógica de negocio transaccional. Simula llamadas e interacciones de base de datos.
 */
public class InscripcionController {
    private List<Estudiante> estudiantesDb;
    private List<Asignatura> asignaturasDb;

    public InscripcionController() {
        this.estudiantesDb = new ArrayList<>();
        this.asignaturasDb = new ArrayList<>();
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
            return "ERROR: No se encontró un estudiante con matrícula " + matricula + ".";
        }

        Asignatura asignatura = null;
        for (Asignatura a : asignaturasDb) {
            if (a.getCodigo().equals(codigoAsignatura)) {
                asignatura = a;
                break;
            }
        }
        if (asignatura == null) {
            return "ERROR: No se encontró la asignatura con código " + codigoAsignatura + ".";
        }

        Seccion seccion = null;
        for (Seccion s : asignatura.getSecciones()) {
            if (s.getIdGrupo() == idGrupo) {
                seccion = s;
                break;
            }
        }
        if (seccion == null) {
            return "ERROR: No se encontró la sección " + idGrupo + " en la asignatura " + codigoAsignatura + ".";
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
            return "OK: " + estudiante.getNombre() + " inscrito en " + asignatura.getNombre() + " - Sección " + idGrupo + ".";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }
}
