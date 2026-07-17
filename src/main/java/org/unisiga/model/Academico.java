package org.unisiga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa al profesor encargado de dictar cátedras.
 */
public class Academico extends MiembroUniversitario {
    private String idEmpleado;
    private String tipoContrato;
    private Departamento departamento; // Relación de agregación
    private List<Seccion> seccionesDictadas;

    public Academico(String rut, String nombre, String correo, String idEmpleado, String tipoContrato) {
        super(rut, nombre, correo);
        this.idEmpleado = idEmpleado;
        this.tipoContrato = tipoContrato;
        this.seccionesDictadas = new ArrayList<>();
    }

    @Override
    public boolean login(String password) {
        return password != null && password.contains("@");
    }

    /**
     * Registra la nota de un estudiante para una evaluación de la asignatura.
     * [REGLAS]: Validar parámetros, rango de notas [1.0, 7.0] y que la evaluación pertenezca a la asignatura.
     */
    public void registrarNota(Inscripcion inscripcion, Evaluacion evaluacion, float valorNota) {
        if (inscripcion == null || evaluacion == null) {
            throw new IllegalArgumentException("La inscripción y la evaluación no pueden ser nulas.");
        }
        if (valorNota < 1.0f || valorNota > 7.0f) {
            throw new IllegalArgumentException("La nota debe estar en el rango [1.0, 7.0].");
        }
        if (!evaluacion.getAsignatura().equals(inscripcion.getSeccion().getAsignatura())) {
            throw new IllegalArgumentException("La evaluación no pertenece a la asignatura de la sección inscrita.");
        }
        for (Calificacion cal : inscripcion.getCalificaciones()) {
            if (cal.getEvaluacion().equals(evaluacion)) {
                cal.setNota(valorNota);
                return;
            }
        }
        Calificacion nuevaCalificacion = new Calificacion(valorNota, inscripcion, evaluacion);
        inscripcion.getCalificaciones().add(nuevaCalificacion);
        evaluacion.getCalificaciones().add(nuevaCalificacion);
    }

    // Getters y Setters
    public String getIdEmpleado() { return idEmpleado; }
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento depto) { this.departamento = depto; }
    public List<Seccion> getSeccionesDictadas() { return seccionesDictadas; }
}
