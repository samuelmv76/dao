// ProfesorDTO.java
package dto;

import java.util.List;

public class ProfesorDTO {
    private String codProfesor;
    private String nombreCompleto;
    private List<String> modulosImpartidos;
    
    public ProfesorDTO(String codProfesor, String nombreCompleto, List<String> modulosImpartidos) {
        this.codProfesor = codProfesor;
        this.nombreCompleto = nombreCompleto;
        this.modulosImpartidos = modulosImpartidos;
    }
    
    // Getters y Setters
    public String getCodProfesor() { return codProfesor; }
    public void setCodProfesor(String codProfesor) { this.codProfesor = codProfesor; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    
    public List<String> getModulosImpartidos() { return modulosImpartidos; }
    public void setModulosImpartidos(List<String> modulosImpartidos) { 
        this.modulosImpartidos = modulosImpartidos; 
    }
    
    @Override
    public String toString() {
        return "ProfesorDTO{" +
                "codProfesor='" + codProfesor + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", modulosImpartidos=" + modulosImpartidos +
                '}';
    }
}