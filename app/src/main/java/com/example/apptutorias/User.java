package com.example.apptutorias;

import java.util.Date;

public class User {
    private int numControl;
    private String nombre;
    private String primerApellido;
    private String segundoApellido; // Opcional
    private String email;
    private String tipoUsuario;
    private int semestre; // Opcional
    private String fechaNacimiento; // Formato de fecha (YYYY-MM-DD)

    public User(int numControl, String nombre, String primerApellido, String segundoApellido,
                String email, String tipoUsuario, int semestre, String fechaNacimiento) {
        this.numControl = numControl;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.semestre = semestre;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters y Setters
    public int getNumControl() {
        return numControl;
    }

    public void setNumControl(int numControl) {
        this.numControl = numControl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
