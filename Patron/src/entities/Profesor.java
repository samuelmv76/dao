package entities;

public class Profesor {
    private String codProfesor;
    private String nombreProfesor;
    private String apellidosProfesor;
    
    // Constructores
    public Profesor() {}
    
    public Profesor(String codProfesor, String nombreProfesor, String apellidosProfesor) {
        this.codProfesor = codProfesor;
        this.nombreProfesor = nombreProfesor;
        this.apellidosProfesor = apellidosProfesor;
    }
    
    // Getters y Setters
    public String getCodProfesor() {
        return codProfesor;
    }
    
    public void setCodProfesor(String codProfesor) {
        this.codProfesor = codProfesor;
    }
    
    public String getNombreProfesor() {
        return nombreProfesor;
    }
    
    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
    
    public String getApellidosProfesor() {
        return apellidosProfesor;
    }
    
    public void setApellidosProfesor(String apellidosProfesor) {
        this.apellidosProfesor = apellidosProfesor;
    }
    
    // Métodos útiles
    public String getNombreCompleto() {
        return nombreProfesor + " " + apellidosProfesor;
    }
    
    @Override
    public String toString() {
        return "Profesor{" +
                "codProfesor='" + codProfesor + '\'' +
                ", nombreProfesor='" + nombreProfesor + '\'' +
                ", apellidosProfesor='" + apellidosProfesor + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return codProfesor.equals(profesor.codProfesor);
    }
    
    @Override
    public int hashCode() {
        return codProfesor.hashCode();
    }
}