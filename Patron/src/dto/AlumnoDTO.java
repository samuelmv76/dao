// AlumnoDTO.java
package dto;

import java.util.Date;

public class AlumnoDTO {
    private String codigo;
    private String nombreCompleto;
    private Date fechaNacimiento;
    private String grupo;
    
    public AlumnoDTO(String codigo, String nombreCompleto, Date fechaNacimiento, String grupo) {
        this.codigo = codigo;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.grupo = grupo;
    }
    
    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    
    @Override
    public String toString() {
        return "AlumnoDTO{" +
                "codigo='" + codigo + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", grupo='" + grupo + '\'' +
                '}';
    }
}