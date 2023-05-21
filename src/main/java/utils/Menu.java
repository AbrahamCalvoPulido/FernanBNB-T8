package utils;

import controller.GestionAPP;
import modules.Admin;
import modules.Propietario;
import modules.Reserva;
import modules.Usuario;
import persistencia.Persistencia;

import java.io.IOException;
import java.util.ArrayList;

public class Menu {
public static void inicioApp() {
    System.out.println("""
            ███████╗███████╗██████╗ ███╗   ██╗ █████╗ ███╗   ██╗██████╗ ███╗   ██╗██████╗\s
            ██╔════╝██╔════╝██╔══██╗████╗  ██║██╔══██╗████╗  ██║██╔══██╗████╗  ██║██╔══██╗
            █████╗  █████╗  ██████╔╝██╔██╗ ██║███████║██╔██╗ ██║██████╔╝██╔██╗ ██║██████╔╝
            ██╔══╝  ██╔══╝  ██╔══██╗██║╚██╗██║██╔══██║██║╚██╗██║██╔══██╗██║╚██╗██║██╔══██╗
            ██║     ███████╗██║  ██║██║ ╚████║██║  ██║██║ ╚████║██████╔╝██║ ╚████║██████╔
            
            Menú de inicio
            1. Login
            2. Registro
            Elija una opción: """);

 }

    public static void loginUsuario(){
        System.out.print("""
                ■■■■■■■■■ ® Fernan BNB ® ■■■■■■■■■
                Si desea salir escriba salir.
                Introduce tu Usuario:
                ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
                """);

}

    public static void loginPass(){
        System.out.print("""
                ■■■■■■■■■ ® Fernan BNB ® ■■■■■■■■■
                
                Introduce tu contraseña:
                ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
                """);
    }

    public static void menuRegistro(){
        System.out.print("""

                ╔════════════ Menu Registro De Perfil ════════════╗
                ║                                                 ║
                ║    Elija la opción como desee registrarse       ║
                ║                                                 ║
                ║   1. Usuario                                    ║
                ║   2. Propietario                                ║
                ║   3. Salir                                      ║
                ║                                                 ║
                ╚═════════════════════════════════════════════════╝
                Introduce una opción:
                """);
    }

    public static void menuUsuario(GestionAPP controller,Usuario user) throws IOException {
        String ultimoAcceso = Persistencia.ultimoAccesoSave(String.valueOf(user.getId()));
        System.out.printf("""
                    ╔════════════ Menu principal Usuario ════════════╗
                    ║                                                ║
                    ║  Bienvenido %s . Tiene %d reservas pendientes 
                    ║  Tu último acceso fue el %s                    
                    ║ 1.- Búsqueda de alojamientos                   ║
                    ║ 2.- Ver mis  reservas                          ║ 
                    ║ 3.- Modificar mis reservas                     ║ 
                    ║ 4.- Ver mi perfil                              ║ 
                    ║ 5.- Modificar mis datos                        ║
                    ║ 6.- Cerrar sesión                              ║ 
                    ║                                                ║
                    ╚════════════════════════════════════════════════╝ 
                    Elija  una  opción: 
                    """, controller.buscaUsuariobyId(user.getId()).getNombre(), controller.devuelveReservasUsuario(user.getId()).size(), ultimoAcceso);

    }

    public static void menuModiUsuario(){
        System.out.print("""
                ╔════════════ Menu Modificador Perfil ════════════╗
                ║                                                 ║
                ║    Elija la opción como desee Modificar         ║
                ║                                                 ║
                ║   1. Nombre                                     ║   
                ║   2. Contraseña                                 ║
                ║   3. Salir                                      ║
                ║                                                 ║
                ╚═════════════════════════════════════════════════╝
                Introduce una opción: 
                """);
    }

    public static void menuPropietario(GestionAPP controller, Propietario user) throws IOException {
        String ultimoAcceso = Persistencia.ultimoAccesoSave(String.valueOf(user.getId()));
        System.out.printf("""
                        ╔══════════ Menu principal Propietario ═════════════╗
                        ║                                                   ║
                        Bienvenido %s . Tiene %d viviendas. 
                         Tu último acceso fue el %s                      
                        ║ 1.- Ver mis viviendas en Alquiler                 ║
                        ║ 2.- Editar mis viviendas                          ║ 
                        ║ 3.- Añadir mis viviendas                          ║ 
                        ║ 4.- Ver las reservas de mis viviendas             ║ 
                        ║ 5.- Establecer periodo de no disponible vivienda  ║ 
                        ║ 6.- Ver mi perfil                                 ║ 
                        ║ 7.- Modificar mi perfil                           ║
                        ║ 8.- Reservas excel.                               ║
                        ║ 9.- Cerrar sesión                                 ║ 
                        ║                                                   ║
                        ╚═══════════════════════════════════════════════════╝  
                        Elija  una  opción: 
                        """, controller.buscaPropietariobyId(user.getId()).getNombre(), controller.buscarViviendaPorPropietario(user.getId()).size(), ultimoAcceso);

    }

    public static void menuAdmin(GestionAPP controller, Admin user) throws IOException {
        String ultimoAcceso = Persistencia.ultimoAccesoSave(String.valueOf(user.getId()));
        System.out.printf("""
                        ╔═════════ Menu principal Administrador ════════════╗
                        ║                                                   ║
                        ║              Bienvenido %s 
                        ║  Actualmente tenemos %d usuarios. Viviendas: %d . Reservas: %d 
                        ║           Tu último acceso fue el %s                      
                        ║  Menu de opciones:                     
                        ║ 1.- Ver todas las viviendas en alquiler           ║
                        ║ 2.- Ver todos los usuarios del sistema            ║ 
                        ║ 3.- Ver todas las reservas de viviendas           ║ 
                        ║ 4.- Ver mi perfil                                 ║ 
                        ║ 5.- Modificar mi perfil                           ║
                        ║ 6.- Backups                                       ║ 
                        ║ 7.- Excel                                         ║ 
                        ║ 8.- Configuracion                                 ║ 
                        ║ 9.- Cerrar sesión                                 ║ 
                        ║                                                   ║
                        ╚═══════════════════════════════════════════════════╝ 
                        Elija  una  opción: 
                        """, controller.buscaAdminbyId(user.getId()).getNombre(), controller.numeroUsuarios(), controller.numeroViviendas(), controller.numeroReservas(), ultimoAcceso);

    }

    public static void cargandoMenu() {
        System.out.print("""
                ╔═════════════════════════════════════════════════╗
                ╚═════════════════════════════════════════════════╝
                """);

    }


}
