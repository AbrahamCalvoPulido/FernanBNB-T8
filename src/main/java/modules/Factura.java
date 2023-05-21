package modules;

public class Factura {
    private int idReserva;
    private String titulo;
    private String localidad;
    private String provincia;
    private String descripcion;
    private double precioNoche;
    private int noches;

    public Factura(int idReserva, String titulo, String localidad, String provincia, String descripcion, double precioNoche, int noches) {
        this.idReserva = idReserva;
        this.titulo = titulo;
        this.localidad = localidad;
        this.provincia = provincia;
        this.descripcion = descripcion;
        this.precioNoche = precioNoche;
        this.noches = noches;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }

    public int getNoches() {
        return noches;
    }

    public void setNoches(int noches) {
        this.noches = noches;
    }
}
