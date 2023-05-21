package modules;

import java.io.Serializable;

public class Admin implements Serializable {

    //Atributos
    private int id;
    private String nombre;
    private String clave;
    private String email;
    private boolean verificado;


    /**
     * Constructor de la clase Admins
     * @param id id del usuario
     * @param nombre nombre del usuario
     * @param clave clave del usuario
     * @param email email del usuario
     */
    public Admin(int id, String nombre, String clave, String email) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        this.verificado=true;
    }


    /**
     * Getters y setters
     */

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    /**
     * @param email1
     * @param clave1
     * @return Metodo que comprueba si el email y la clave son correctos
     */
    public  boolean login(String email1, String clave1) {
        return (email.equals(email1) && clave.equals(clave1));
    }


    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return  "\n════════════ Perfil Administrador ════════════\n"+
                "Usuario: " + email+ "\n"+
                "Nombre: " + nombre ;
    }
}
