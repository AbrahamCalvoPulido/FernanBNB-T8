package controller;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.*;
import modules.*;
import modules.Admin;
import modules.Propietario;
import modules.Usuario;
import modules.Vivienda;
import persistencia.Persistencia;
import utils.Herramientas;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class GestionAPP implements Serializable {

    //Atrributos
    private static final DAOManager dao=DAOManager.getSinglentonInstance();
    private static final DAOAdminSQL daoAdminSQL= new DAOAdminSQL();
    private static final DAOUsuarioSQL daoUsuarioSQL = new DAOUsuarioSQL();
    private static final DAOPropietarioSQL daoPropietarioSQL= new DAOPropietarioSQL();
    private static final DAOViviendaSQL daoViviendaSQL=new DAOViviendaSQL();
    private static final DAOReservaSQL daoReservaSQL=new DAOReservaSQL();

    //Constructor
    public GestionAPP() throws Exception {
        dao.open();
        cargaViviendasPropietario();
        cargaReservasViviendas();
        cargaReservasUsuario();
        dao.close();

    }
    //G&S
    public ArrayList<Usuario> getUsuarios() {
        return daoUsuarioSQL.readAll(dao);
    }
    public ArrayList<Admin> getAdmins() {
        return daoAdminSQL.readAllAdmins(dao);
    }
    public ArrayList<Propietario> getPropietarios() {
        return daoPropietarioSQL.readAll(dao);
    }
    private  boolean cargaReservasUsuario(){
        for (Usuario u :
                getUsuarios()) {
            u.setReservas(daoReservaSQL.buscarReservabyIDUsuario(u.getId(), dao));
        }
        return false;
    }
    private  boolean cargaReservasViviendas(){
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)) {
                v.setReservas(daoReservaSQL.buscarReservabyIDVivienda(v.getId(), dao));
            }
        }
        return false;
    }
    private  boolean cargaViviendasPropietario(){
        for (Propietario p :
                getPropietarios()) {
            p.setViviendas(daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao));
        }
        return false;
    }

    /**
     * Método que genera un número aleatorio
     * @return número aleatorio
     */
    private int generaAleatorio(){
        return (int)(Math.random()*10000);
    }

    /**
     * Método que genera un ID para el propietario
     * @return ID del propietario
     */
    private int generaIdPropietario(){
        int num;
        boolean encontrado = false;
        do {
            num = generaAleatorio();
            for (Propietario p :
                    getPropietarios()) {
                if (p.getId()== num) encontrado = true;
            }
        }while (encontrado);
        return num;
    }
    public void borraReservaSql(Reserva r){
        daoReservaSQL.delete(r,dao);
    }

    /**
     * Método que genera una ID para la vivienda
     * @return ID de la vivienda
     */
    private int generaIdVivienda(){
        int num;
        boolean encontrado = false;
        do {
            num = generaAleatorio();
            for (Propietario p :
                    getPropietarios()) {
                for (Vivienda v :
                        daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)){
                    if (v.getId()== num) encontrado = true;
                }
            }
        }while (encontrado);
        return num;
    }

    /**
     * Método que genera un ID para el usuario
     * @return id del usuario
     */
    private int generaIdUsuario(){
        int num;
        boolean encontrado = false;
        do {
            num = generaAleatorio();
            for (Usuario u :
                    getUsuarios()) {
                if (u.getId()==num) encontrado=true;
            }
        }while(encontrado);
        return num;
    }

    /**
     * Método que genera una ID para la reserva
     * @return id reserva
     */
    private int generaIdReserva(){
        int num;
        boolean encontrado = false;
        do {
            num = generaAleatorio();
            for (Propietario p :
                    getPropietarios()) {
                for (Vivienda v :
                        daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)) {
                    for (Reserva r :
                            daoReservaSQL.buscarReservabyIDVivienda(v.getId(),dao)) {
                        if (num==r.getIdReserva()) encontrado=true;
                    }
                }
            }

        }while(encontrado);
        return num;
    }

    /**
     * Método que busca un usuario por id
     * @param id
     * @return el usuario encontrado
     */
    public Usuario buscaUsuariobyId(int id) {
        for (Usuario u: getUsuarios()) {
            if (id == u.getId()) return u;

        }
        return null;
    }

    /**
     * Método que busca un propietario por su id
     * @param id
     * @return Propietario
     */
    public Propietario buscaPropietariobyId(int id) {
        for (Propietario p: getPropietarios()) {
            if (id == p.getId()) return p;

        }
        return null;
    }
    public ArrayList<Reserva> devuelveReservaPorPropietario(int id){
        ArrayList<Reserva> reservas=new ArrayList<>();
            for (Vivienda v:
                 daoViviendaSQL.readViviendabyIDPropietario(id,dao)) {
                reservas.addAll(daoReservaSQL.buscarReservabyIDVivienda(v.getId(), dao));
            }
        return reservas;
    }

    /**
     * Método que busca la vivienda por su id
     * @param id
     * @return vivienda
     */
    public Vivienda buscaViviendabyId(int id) {
        for (Propietario p:
                getPropietarios()) {
            for (Vivienda v:
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)) {
                if (id == v.getId()) return v;
            }
        }
        return null;
    }
    public ArrayList<Vivienda> buscarViviendaPorPropietario(int id){
        return daoViviendaSQL.readViviendabyIDPropietario(id,dao);
    }
    /**
     * Método que añade un usuario a la lista de usuarios
     * @param nombre
     * @param clave
     * @param email
     * @return Usuario
     */
    public Usuario addUsuario(String nombre, String clave, String email,boolean verificado){
        Usuario u = new Usuario(generaIdUsuario(), nombre, clave, email,verificado);
        daoUsuarioSQL.insertarUsuario(u,dao);
        return u;
    }
    /**
     * Método que añade un admin a la lista de admins
     * @param nombre String
     * @param clave String
     * @param email String
     * @return Admin
     */
    public Admin addAdmin(String nombre, String clave, String email){
        Admin a = new Admin(generaAleatorio(), nombre, clave, email);
        daoAdminSQL.insertarAdmin(a,dao);
        return a;
    }

    /**
     * Método que añade un propietario a la lista de propietarios
     * @param nombre
     * @param clave
     * @param email
     * @return propietario
     */
    public Propietario addPropietario(String nombre, String clave, String email,boolean verificado){
        Propietario p = new Propietario(generaIdPropietario(), nombre, clave, email,verificado);
        daoPropietarioSQL.insertarPropietario(p,dao);
        return p;
    }

    /**
     * Método que añade una vivienda
     * @param idPropietario
     * @param titulo
     * @param descripcion
     * @param localidad
     * @param provincia
     * @param maxOcupantes
     * @param precioNoche
     * @return vivienda
     */
    public Vivienda addVivienda(int idPropietario,String titulo,String descripcion, String localidad,String provincia,int maxOcupantes,double precioNoche){
        Vivienda v = new Vivienda(generaIdVivienda(),titulo,descripcion,localidad,provincia,maxOcupantes,precioNoche);
        daoViviendaSQL.insertarVivienda(v,dao,idPropietario);
        return v;
    }

    public ArrayList<Reserva> buscaReservaByVivienda(int idVivienda){
        return daoReservaSQL.buscarReservabyIDVivienda(idVivienda,dao);
    }
    /**
     * Método que busca al usuario por email
     * @param email
     * @return Usuario
     */
    public Usuario buscaUsuarioEmail(String email){
        for (Usuario u :
                getUsuarios()) {
            if (email.equals(u.getEmail())) return u;
        }
        return null;
    }

    /**
     * Método que busca al propietario por email
     * @param email
     * @return Propietario
     */
    public Propietario buscaPropietarioEmail(String email){
        for (Propietario p :
                getPropietarios()) {
            if (email.equals(p.getEmail())) return p;
        }
        return null;
    }

    /**
     * Método que busca al admin por email
     * @param email
     * @return admin
     */
    public Admin buscaAdminEmail(String email){
        for (Admin a:
                getAdmins()) {
            if (email.equals(a.getEmail())) return a;
        }
        return null;
    }


    /**
     * Método para logear un usuario , propietario o admin
     * @param usuario
     * @param clave
     * @return el objeto del tipo Usuario, Propietario o Admin
     */
    public Object login(String usuario,String clave){
        Usuario u = buscaUsuarioEmail(usuario);
        Propietario p = buscaPropietarioEmail(usuario);
        Admin a = buscaAdminEmail(usuario);
        if (u!=null)if (u.getClave().equals(clave)) return u;
        if (p!=null)if (p.getClave().equals(clave)) return p;
        if (a!=null) if (a.getClave().equals(clave)) return a;
        return null;
    }

    /**
     * Método que busca alojamiento para hacer una reserva
     * @param provincia
     * @param ciudad
     * @param fechaInicio
     * @param noches
     * @param viajeros
     * @return ArrayList<Vivienda>
     */
    public ArrayList<Vivienda> buscaAlojamiento(String provincia, String ciudad, LocalDate fechaInicio, int noches,int viajeros){
        ArrayList<Vivienda> alojamientosEncontrados = new ArrayList<>();
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)) {
                if ((v.getProvincia().contains(provincia) || v.getLocalidad().contains(ciudad)) && v.tieneDisponibilidad(fechaInicio,noches,viajeros,daoReservaSQL.buscarReservabyIDVivienda(v.getId(),dao))) alojamientosEncontrados.add(v);
            }
        }

        return alojamientosEncontrados;
    }
    public ArrayList<Reserva> devuelveReservasUsuario(int idUsuario){
        return daoReservaSQL.buscarReservabyIDUsuario(idUsuario,dao);
    }

    /**
     * Método que busca alojamientos sin disponibilidad para mostrarlos
     * @param provincia
     * @param ciudad
     * @param fechaInicio
     * @param noches
     * @param viajeros
     * @return ArrayList<Vivienda>
     */
    public ArrayList<Vivienda> buscaAlojamientoSinDisponibilidad(String provincia, String ciudad, LocalDate fechaInicio, int noches, int viajeros) {
        ArrayList<Vivienda> alojamientosNoEncontrados = new ArrayList<>();
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v : daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)) {
                if ((v.getProvincia().contains(provincia) || v.getLocalidad().contains(ciudad)) && !v.tieneDisponibilidad(fechaInicio, noches, viajeros,daoReservaSQL.buscarReservabyIDVivienda(v.getId(),dao))) {
                    alojamientosNoEncontrados.add(v);
                }
            }
        }
        return alojamientosNoEncontrados;
    }
    /**
     * Método que añade una reserva a la lista de reservas de una vivienda
     * @param idUsuario
     * @param v
     * @param fechaInicio
     * @param noches
     * @param ocupantes
     * @return true si la reserva se ha añadido correctamente, false en caso contrario
     */
    public int addReserva2(int idUsuario,Vivienda v,LocalDate fechaInicio,int noches,int ocupantes){
        int idReserva = generaIdReserva();
        buscaUsuariobyId(idUsuario).addReserva(v.addReserva(noches,fechaInicio,idUsuario,idReserva,ocupantes));
        daoReservaSQL.insertarReserva(v.addReserva(noches,fechaInicio,idUsuario,idReserva,ocupantes),dao);
        return idReserva;
    }

    /**
     * Método que cancela una reserva
     * @param idReserva
     * @return true si la reserva se ha cancelado correctamente, false en caso contrario
     */
    public boolean cancelaReserva(int idReserva){
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(),dao)) {
                for (Reserva r :
                        daoReservaSQL.buscarReservabyIDVivienda(v.getId(),dao)) {
                    if (idReserva==r.getIdReserva()){
                        daoReservaSQL.delete(r,dao);
                    }

                    return true;
                }

            }
        }

        return false;
    }

    /**
     * Método que modifical el perfil del usuario
     * @param usuario
     */
    public void modificaPerfil(Object usuario,String nuevo,int cambio) {
        if (usuario instanceof Usuario usuarioModificado && cambio==1) {
            usuarioModificado.setNombre(nuevo);
            daoUsuarioSQL.updateUsuario(usuarioModificado,dao);

        }
        if (usuario instanceof Usuario usuarioModificado && cambio==2) {
            usuarioModificado.setClave(nuevo);
            daoUsuarioSQL.updateUsuario(usuarioModificado,dao);
        }
        if (usuario instanceof Propietario propietarioModificado && cambio==1) {
            propietarioModificado.setNombre(nuevo);
            daoPropietarioSQL.updatePropietario(propietarioModificado,dao);

        }
        if (usuario instanceof Propietario propietarioModificado && cambio==2) {
            propietarioModificado.setClave(nuevo);
            daoPropietarioSQL.updatePropietario(propietarioModificado,dao);

        }
        if (usuario instanceof Admin adminModificado && cambio==1){
            adminModificado.setNombre(nuevo);
            daoAdminSQL.updateAdmin(adminModificado,dao);

        }
        if (usuario instanceof Admin adminModificado && cambio==2){
            adminModificado.setClave(nuevo);
            daoAdminSQL.updateAdmin(adminModificado,dao);
        }
    }


    /**
     * Método que busca la reserva por Id
     * @param idReserva
     * @return devuelve la reserva
     */
    public Reserva buscaReservaById(int idReserva){
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(),dao)) {
                for (Reserva r :
                        daoReservaSQL.buscarReservabyIDVivienda(v.getId(),dao)) {
                    if (idReserva==r.getIdReserva()) return r;
                }
            }
        }
        return null;
    }

    /**
     * Método que Elimina la vivienda de la lista de propietarios
     * @param v
     * @return true o false
     */
    public  boolean deleteVivienda(Vivienda v){
        int cont=0;
        if (!v.tieneReservasPendientes()){
            for (int i = 0; i < getPropietarios().size(); i++) {
                for (int j = 0; j < getPropietarios().get(i).getViviendas().size(); j++) {
                    if (getPropietarios().get(i).getViviendas().get(j).getId()==v.getId()) {
                        getPropietarios().get(i).getViviendas().remove(j);
                        daoViviendaSQL.deleteVivienda(v,dao);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Método que busca al propietario por ID de la Vivienda.
     * @param idVivienda
     * @return devuelve el propietario por ID de la Vivienda.
     */
    public Propietario buscaPropietariobyIdVivienda(int idVivienda){

        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)) {
                if (v.getId()==idVivienda) return p;

            }
        }


        return null;
    }

    /**
     * Método que dice si una reserva está disponible o no.
     * @param v
     * @param fechaInicio
     * @param noches
     * @return True o False dependiendo si la reserva se encuentra o no disponible
     */
    public boolean addPeriodoNoDisponible(Vivienda v,LocalDate fechaInicio,int noches){
        daoReservaSQL.insertarReserva(v.addReserva(noches,fechaInicio,generaIdPropietario(),generaIdReserva(),1),dao);
        return true;
    }

    /**
     * Método que devuelve número de Usuarios.
     * @return int contador
     */
    public int numeroUsuarios(){
        int cont=0;
        for (Usuario u :
                getUsuarios()) {
            cont++;
        }
        for (Propietario p :
                getPropietarios()) {
            cont++;
        }
        return cont;
    }

    /**
     * Método que devuelve número de Reservas
     * @return int contador
     */
    public int numeroReservas(){
        int cont=0;
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(),dao)) {
                for (Reserva r :
                        daoReservaSQL.buscarReservabyIDVivienda(v.getId(),dao)) {
                    cont++;
                }
            }
        }

        return cont;
    }

    /**
     * Método que devuelve número de viviendas
     * @return int contador
     */
    public int numeroViviendas(){
        int cont=0;
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(),dao)) {
                cont++;
            }
        }

        return cont;
    }

    /**
     * Método que devuelve todas las viviendas
     * @return ArrayList de Viviendas
     */
    public ArrayList<Vivienda> getAllViviendas(){
        ArrayList<Vivienda> viviendas = new ArrayList<>();
        for (Propietario p :
                getPropietarios()) {
            viviendas.addAll(daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao));
        }

        return viviendas;
    }

    /**
     * Método que devuelve todas las reservas
     * @return ArrayList de reservas
     */
    public ArrayList<Reserva> getAllReservas(){
        ArrayList<Reserva> reservasArray = new ArrayList<>();
        for (Propietario p :
                getPropietarios()) {
            for (Vivienda v :
                    daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao)) {
                reservasArray.addAll(daoReservaSQL.buscarReservabyIDVivienda(v.getId(),dao));
            }
        }

        return reservasArray;
    }
    public Admin buscaAdminbyId(int id){
        for (Admin a :
                getAdmins()) {
            if (a.getId()==id) return a;
        }
        return null;
    }
    /**
     * Metodo que crea un pdf con la reserva del usuario
     */
    public void crearPDF(LocalDate fechaInicio,String localidad,String provincia,String titulo,Double precioNoche,int noches) {
        try {
            Document document=new Document();
            PdfWriter.getInstance(document,new FileOutputStream("pdf/prueba.pdf"));
            document.open();
            //Cabecera de el pdf en el que se especifica que es una reserva
            PdfPTable cabecera =new PdfPTable(1);
            PdfPCell celdacabecera=new PdfPCell();
            Font fuente= FontFactory.getFont(FontFactory.COURIER,26);
            Chunk texto=new Chunk("DATOS RESERVA FERNANBNB",fuente);
            celdacabecera.addElement(texto);
            celdacabecera.setBorderWidth(0);
            celdacabecera.setHorizontalAlignment(Element.ALIGN_CENTER);
            cabecera.addCell(celdacabecera);
            document.add(cabecera);
            //lineaSeparadora
            for (int i = 0; i < 3; i++) {
                document.add(saltoDeLinea());
            }
            document.add(lineaSeparadora());
            for (int i = 0; i < 3; i++) {
                document.add(saltoDeLinea());
            }
            //Información de la reserva
            PdfPTable infoReserva= new PdfPTable(1);
            PdfPCell celdaInfoReserva= new PdfPCell();
            celdaInfoReserva.setPhrase(
                    new Phrase("Información de la reserva"));
            celdaInfoReserva.setBorderWidth(0);
            celdaInfoReserva.setPaddingBottom(25);
            infoReserva.addCell(celdaInfoReserva);
            document.add(infoReserva);
            document.add(lineaDual("Nombre de la vivienda:",titulo));
            document.add(lineaDual("Localidad:",localidad));
            document.add(lineaDual("Provincia:",provincia));
            document.add(lineaDual("Fecha de inicio:",fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(lineaDual("Fecha de fin:",fechaInicio.plusDays(noches).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(saltoDeLinea());
            PdfPTable costeReserva= new PdfPTable(1);
            PdfPCell celdaCosteReserva= new PdfPCell();
            celdaCosteReserva.setPhrase(
                    new Phrase("Coste Total de su reserva:"));
            celdaCosteReserva.setBorderWidth(0);
            celdaCosteReserva.setPaddingBottom(8);
            costeReserva.addCell(celdaCosteReserva);
            document.add(costeReserva);
            document.add(saltoDeLinea());
            document.add(lineaDual("Precio por noche: ", precioNoche +" €"));
            document.add(lineaDual("Noches: ",String.valueOf(noches)));
            document.add(lineaDual(" "," "));
            document.add(lineaDual("Total", noches * precioNoche +" €"));
            for (int i = 0; i < 3; i++) {
                document.add(saltoDeLinea());
            }
            document.add(lineaSeparadora());
            for (int i = 0; i < 10; i++) {
                document.add(saltoDeLinea());
            }
            //Imagen de fernanbnb en el pie de la página
            document.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static PdfPTable lineaDual(String texto1,String texto2)throws Exception{
        PdfPTable linea=new PdfPTable(2);
        PdfPCell celda1=new PdfPCell();
        PdfPCell celda2=new PdfPCell();
        celda1.setBorderWidth(0);
        celda2.setBorderWidth(0);
        celda1.setPhrase(new Phrase(texto1));
        celda2.setPhrase(new Phrase(texto2));
        celda1.setBackgroundColor(new BaseColor(90,80,65,20));
        linea.addCell(celda1);
        linea.addCell(celda2);
        return linea;
    }
    private static PdfPTable saltoDeLinea() throws Exception{
        PdfPTable linea=new PdfPTable(1);
        PdfPCell lineavacia=new PdfPCell();
        lineavacia.setBorderWidth(0);
        lineavacia.setFixedHeight(8);
        linea.addCell(lineavacia);
        return linea;
    }
    private static PdfPTable lineaSeparadora() throws Exception{
        PdfPTable linea=new PdfPTable(3);
        PdfPCell lineaColor=new PdfPCell();
        PdfPCell vacio=new PdfPCell();
        vacio.setBorderWidth(0);
        lineaColor.setBackgroundColor(new BaseColor(90,80,65,20));
        lineaColor.setFixedHeight(8);
        lineaColor.setBorderWidth(0);
        linea.addCell(vacio);
        linea.addCell(lineaColor);
        linea.addCell(vacio);

        return linea;
    }

    /**
     * Metodo que crea un excel con la informacion de las reservas
     * @param reservas
     */
    public void excel(ArrayList<Reserva> reservas) {
        try {
            File file = new File("excel/reservas.csv");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("Id reserva;Localidad;Provincia;Fecha de inicio;Fecha de fin;Precio por noche;Noches;Coste total\n");
            for (Reserva r : reservas) {
                fileWriter.write(r.getIdReserva()+";"+r.getIdVivienda()+";"+r.getIdUsuario()+";"+r.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+";"+r.getFechaInicio().plusDays(r.getNoches()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+";"+r.getPrecio()+"\n");
            }
            fileWriter.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }
    public void guardarBackupUsers() {
       Persistencia.guardarListadoUsuarios(daoUsuarioSQL.readAll(dao));
    }
    public void guardarBackupPropietario() {
        ArrayList<Propietario> propietariosTemp = daoPropietarioSQL.readAll(dao);
        for (Propietario p: propietariosTemp ) {
            p.setViviendas(daoViviendaSQL.readViviendabyIDPropietario(p.getId(), dao));
            for (Vivienda v: p.getViviendas() ) {
                v.setReservas(daoReservaSQL.buscarReservabyIDVivienda(v.getId(), dao));
            }
        }
        Persistencia.guardarListadoPropietarios(propietariosTemp);
    }
    public void guardarBackupAdmin() {
        Persistencia.guardarListadoAdministradores(daoAdminSQL.readAllAdmins(dao));
    }
    public void cargarBackupUsers() {
        daoUsuarioSQL.deleteAll(dao);
        for (Usuario u :
                Persistencia.cargarListadoUsuarios()) {
            daoUsuarioSQL.insertarUsuario(u, dao);
        }
    }

    public void cargarBackupPropietario() {
        daoPropietarioSQL.deleteALL(dao);
        daoViviendaSQL.deleteAll(dao);
        daoReservaSQL.deleteAll(dao);
        for (Propietario p :
                Persistencia.cargarListadoPropietarios()) {
            daoPropietarioSQL.insertarPropietario(p, dao);
            for (Vivienda v :
                    p.getViviendas()) {
                daoViviendaSQL.insertarVivienda(v, dao,p.getId());
                for (Reserva r :
                        v.getReservas()
                     ) {
                    daoReservaSQL.insertarReserva(r, dao);
                }
            }
        }
    }
    public void cargarBackupAdmin() {
        daoAdminSQL.deleteALL(dao);
        for (Admin a :
                Persistencia.cargarListadoAdministradores()) {
            daoAdminSQL.insertarAdmin(a, dao);
        }
    }

}
