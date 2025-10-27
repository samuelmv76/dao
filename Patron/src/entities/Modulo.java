package entities;

public class Modulo {
    private String codModulo;
    private String nombreModulo;
    private int horasSemanales;
    private String curso;
    
    // Constructores
    public Modulo() {}
    
    public Modulo(String codModulo, String nombreModulo, int horasSemanales, String curso) {
        this.codModulo = codModulo;
        this.nombreModulo = nombreModulo;
        this.horasSemanales = horasSemanales;
        this.curso = curso;
    }
    
    // Getters y Setters
    public String getCodModulo() {
        return codModulo;
    }
    
    public void setCodModulo(String codModulo) {
        this.codModulo = codModulo;
    }
    
    public String getNombreModulo() {
        return nombreModulo;
    }
    
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }
    
    public int getHorasSemanales() {
        return horasSemanales;
    }
    
    public void setHorasSemanales(int horasSemanales) {
        this.horasSemanales = horasSemanales;
    }
    
    public String getCurso() {
        return curso;
    }
    
    public void setCurso(String curso) {
        this.curso = curso;
    }
    
    @Override
    public String toString() {
        return "Modulo{" +
                "codModulo='" + codModulo + '\'' +
                ", nombreModulo='" + nombreModulo + '\'' +
                ", horasSemanales=" + horasSemanales +
                ", curso='" + curso + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modulo modulo = (Modulo) o;
        return codModulo.equals(modulo.codModulo);
    }
    
    @Override
    public int hashCode() {
        return codModulo.hashCode();
    }
}