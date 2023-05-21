import controller.GestionAPP;
import dao.DAOManager;
import modules.*;
import persistencia.Persistencia;
import utils.Herramientas;
import utils.Menu;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Scanner s = new Scanner(System.in);
    static Comunicaciones com = new Comunicaciones();
    static GestionAPP controller;

    static {
        try {
            controller = new GestionAPP();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {

        Object user = null;
        DAOManager dao = DAOManager.getSinglentonInstance();

        try {
            dao.open();
            do {
                try {
                    Menu.inicioApp();
                    switch (Integer.parseInt(s.nextLine())) {
                        case 1 -> //Login
                                user = login(controller);
                        case 2 ->// Registro
                                user = registro(controller);

                    }
                    if (user != null) {
                        if (user instanceof Usuario) menuUsuario(controller, (Usuario) user);
                        if (user instanceof Propietario) menuPropietario(controller, (Propietario) user);
                        if (user instanceof Admin) menuAdmin(controller, (Admin) user);
                    }
                    user = null;
                } catch (NumberFormatException e) {
                    System.out.println("Opcion introducida incorrecta, vuelva a intentarlo.");
                }
            } while (true);
        } catch (Exception e) {
            System.out.println("Error: al abrir la cone");
        }
        dao.close();
    }

    private static Object login(GestionAPP controller) {
        String usuario = "", pass = "";
        boolean validacion = false;
        do {
            try {
                Menu.loginUsuario();
                usuario = s.nextLine();
                if (usuario.equals("salir")) {
                    validacion = true;
                    System.out.println();
                    break;
                }
                Menu.loginPass();
                pass = s.nextLine();
                if (controller.login(usuario, pass) == null) {
                    System.out.println("Usuario o contraseña incorrectos");
                }
                if (controller.login(usuario, pass) instanceof Usuario) {
                    if (!controller.buscaUsuarioEmail(usuario).isVerificado() && controller.buscaUsuarioEmail(usuario) != null) {
                        System.out.println("Usuario no verificado");

                    } else {
                        System.out.println("Logueado correctamente");
                        Persistencia.whriteLog("Inicio de sesión del usuario: ", usuario);
                        validacion = true;
                    }
                }
                if (controller.login(usuario, pass) instanceof Propietario) {
                    if (!controller.buscaPropietarioEmail(usuario).isVerificado() && controller.buscaPropietarioEmail(usuario) != null) {
                        validacion = true;
                        System.out.println("Logueado correctamente");
                        Persistencia.whriteLog("Inicio de sesión del usuario: ", usuario);
                    } else validacion = true;
                }
                if (controller.login(usuario, pass) instanceof Admin) {
                    if (!controller.buscaAdminEmail(usuario).isVerificado() && controller.buscaAdminEmail(usuario) != null) {
                        validacion = true;
                        System.out.println("Logueado correctamente");
                        Persistencia.whriteLog("Inicio de sesión del usuario: ", usuario);
                    } else validacion = true;
                }
            } catch (Exception e) {
                System.out.println("Error al introducir los datos");
            }

        } while (controller.login(usuario, pass) == null || !validacion);

        return controller.login(usuario, pass);
    }

    private static Object registro(GestionAPP controller) {
        int op = 0;
        do {
            try {
                Menu.menuRegistro();
                op = Integer.parseInt(s.nextLine());
                if (op != 1 && op != 2 && op != 3) {
                    System.out.println("Opción incorrecta, vuelva a intentarlo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error al leer la entrada. Introduzca un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 1 && op != 2 && op != 3);
        return introduceDatosRegistro(op, controller);
    }


    private static Object introduceDatosRegistro(int op, GestionAPP controller) {
        String usuarioR = "";
        int token = 0;
        boolean registrado = false;
        if (op == 3) return null;
        try {
            token = generaToken();
            System.out.print("Introduzca el correo electronico: (Si desea salir introduzca salir)");
            usuarioR = s.nextLine();

        } catch (Exception e) {
            System.out.println("Error al introducuir el correo debe ser gmail");
        }
        if (usuarioR.equals("salir")) {
            System.out.println();
        } else {
            System.out.print("Introduzca la contraseña: ");
            String passR = s.nextLine();
            System.out.print("Introduzca su nombre: ");
            String nombreR = s.nextLine();
            System.out.println();
            com.enviaMail(usuarioR, htmlToken(token), "Bienvenido a FernanBNB", "text/html; charset=utf-8", "");
            System.out.println("Introduce el token que le hemos mandado por correo: ");
            String tokenIntroducido = s.nextLine();
            if (op == 1) {
                if (compruebaToken(tokenIntroducido, token)) {
                    registrado = true;
                    System.out.println("Registrado correctamente");
                    System.out.println();
                } else System.out.println("Token incorrecto");
            } else if (op == 2) {
                if (compruebaToken(tokenIntroducido, token)) {
                    registrado = true;
                    System.out.println("Registrado correctamente");
                } else System.out.println("Token incorrecto");
            }

            if (op == 1 && registrado) return controller.addUsuario(nombreR, passR, usuarioR, registrado);
            if (op == 2 && registrado) return controller.addPropietario(nombreR, passR, usuarioR, registrado);
        }
        return null;
    }

    private static void pintaReservas(ArrayList<Reserva> reservas, GestionAPP controller, Usuario user) {
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println("" + (i + 1) + ". " + reservas.get(i).pintaResumen(controller.buscaViviendabyId(reservas.get(i).getIdVivienda()), user));
            System.out.println();
        }
    }

    private static void pintaVivienda(ArrayList<Vivienda> buscaAlojamiento) {
        for (int i = 0; i < buscaAlojamiento.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + buscaAlojamiento.get(i));
            s.nextLine();
        }
    }

    public static void menuUsuario(GestionAPP controller, Usuario user) throws IOException {
        int opcion = 0;
        do {
            Menu.menuUsuario(controller, user);
            opcion = Integer.parseInt(s.nextLine());
            switch (opcion) {
                case 1:
                    int op = 0;
                    LocalDate fechaInicio = null;
                    int noches = 0, ocupantes = 0;
                    try {
                        System.out.println("""
                                ╔═════════════════════════════════╗
                                  Seleccione el tipo de busqueda:
                                    1.Por provincia
                                    2.Por ciudad
                                    3.Salir
                                     Tipo: 
                                ╚═════════════════════════════════╝""");
                        op = Integer.parseInt(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Introduce un numero");
                    }
                    switch (op) {
                        case 1:
                            System.out.println("Introduce el nombre de la provincia: ");
                            String provincia = Herramientas.formateaCiudad(s.nextLine());
                            do {
                                try {
                                    System.out.println("Introduce la fecha de inicio(dd/MM/yyyy): ");
                                    fechaInicio = LocalDate.parse(s.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                } catch (DateTimeParseException e) {
                                    System.out.println("recuerda que debes introducir la fecha con este formato dd/MM/yyyy");
                                }
                            } while (Objects.requireNonNull(fechaInicio).isBefore(LocalDate.now()));
                            try {
                                System.out.println("Introduce el numero de noches: ");
                                noches = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduce un numero");
                            }
                            try {
                                System.out.println("Introduce el numero de ocupantes: ");
                                ocupantes = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduce un numero");
                            }
                            pintaVivienda(controller.buscaAlojamiento(provincia, "-1", fechaInicio, noches, ocupantes));
                            System.out.println("Viviendas no disponibles con los datos introducidos: ");
                            pintaVivienda(controller.buscaAlojamientoSinDisponibilidad(provincia, "-1", fechaInicio, noches, ocupantes));
                            if (controller.buscaAlojamiento(provincia, "-1", fechaInicio, noches, ocupantes).size() != 0) {
                                int reserva2 = 0;
                                do {
                                    try {
                                        System.out.println("Introduce el numero de vivienda que desea escoger: ");
                                        reserva2 = Integer.parseInt(s.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Introduce un numero");
                                    }
                                    if (reserva2 <= 0 || reserva2 > controller.buscaAlojamiento(provincia, "-1", fechaInicio, noches, ocupantes).size())
                                        System.out.println("Numero de vivienda incorrrecto.");
                                } while (reserva2 <= 0 || reserva2 > controller.buscaAlojamiento(provincia, "-1", fechaInicio, noches, ocupantes).size());
                                com.enviaMensajeTelegram(user.getEmail() + " ha realizado una reserva");
                                Vivienda vaux = controller.buscaAlojamiento(provincia, "-1", fechaInicio, noches, ocupantes).get(reserva2 - 1);
                                Reserva raux = controller.buscaReservaById(controller.addReserva2(user.getId(), vaux, fechaInicio, noches, ocupantes));
                                Factura f = new Factura(raux.getIdReserva(), vaux.getTitulo(), vaux.getLocalidad(), vaux.getProvincia(), vaux.getDescripcion(), vaux.getPrecioNoche(), raux.getNoches());
                                controller.crearPDF(raux.getFechaInicio(), f.getLocalidad(), f.getProvincia(), f.getTitulo(), f.getPrecioNoche(), f.getNoches());
                                com.enviaMail(user.getEmail(), htmlReservaPdf(raux, vaux), "Reservas", "application/pdf", "pdf/prueba.pdf");
                                System.out.println("Pulsa para continuar...");
                                s.nextLine();
                            }
                            break;
                        case 2:
                            System.out.println("Introduce el nombre de la ciudad: ");
                            String ciudad = Herramientas.formateaCiudad(s.nextLine());
                            do {
                                try {
                                    System.out.println("Introduce la fecha de inicio(dd/MM/yyyy): ");
                                    fechaInicio = LocalDate.parse(s.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                } catch (DateTimeParseException e) {
                                    System.out.println("recuerda que debes introducir la fecha con este formato dd/MM/yyyy");
                                }
                            } while (Objects.requireNonNull(fechaInicio).isBefore(LocalDate.now()));
                            try {
                                System.out.println("Introduce el numero de noches: ");
                                noches = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduce un numero");
                            }
                            try {
                                System.out.println("Introduce el numero de ocupantes: ");
                                ocupantes = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduce un numero");
                            }
                            //TODO Revisar este metodo
                            pintaVivienda(controller.buscaAlojamiento("-1", ciudad, fechaInicio, noches, ocupantes));
                            System.out.println("Viviendas no disponibles con los datos introducidos: ");
                            pintaVivienda(controller.buscaAlojamientoSinDisponibilidad("", ciudad, fechaInicio, noches, ocupantes));
                            if (controller.buscaAlojamiento("-1", ciudad, fechaInicio, noches, ocupantes).size() != 0) {
                                int reserva = 0;
                                do {
                                    try {
                                        System.out.println("Introduce el numero de vivienda que desea escoger: ");
                                        reserva = Integer.parseInt(s.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Introduce un numero");
                                    }
                                    if (reserva <= 0 || reserva > controller.buscaAlojamiento("-1", ciudad, fechaInicio, noches, ocupantes).size())
                                        System.out.println("Numero de vivienda incorrrecto.");
                                } while (reserva <= 0 || reserva > controller.buscaAlojamiento("-1", ciudad, fechaInicio, noches, ocupantes).size());
                                com.enviaMensajeTelegram(user.getEmail() + " ha realizado una reserva");
                                Vivienda vaux = controller.buscaAlojamiento("-1", ciudad, fechaInicio, noches, ocupantes).get(reserva - 1);
                                Reserva raux = controller.buscaReservaById(controller.addReserva2(user.getId(), vaux, fechaInicio, noches, ocupantes));
                                Factura f = new Factura(raux.getIdReserva(), vaux.getTitulo(), vaux.getLocalidad(), vaux.getProvincia(), vaux.getDescripcion(), vaux.getPrecioNoche(), raux.getNoches());
                                controller.crearPDF(raux.getFechaInicio(), f.getLocalidad(), f.getProvincia(), f.getTitulo(), f.getPrecioNoche(), f.getNoches());
                                com.enviaMail(user.getEmail(), htmlReservaPdf(raux, vaux), "Reservas", "application/pdf", "pdf/prueba.pdf");
                                System.out.println("Pulsa para continuar...");
                                s.nextLine();
                            }

                            break;
                    }
                    break;
                case 2:
                    if (controller.devuelveReservasUsuario(user.getId()).size() == 0)
                        System.out.println("No tienes reservas");
                    else pintaReservas(controller.devuelveReservasUsuario(user.getId()), controller, user);
                    System.out.println("Pulsa para continuar...");
                    s.nextLine();
                    break;
                case 3:
                    int reserva = 0;
                    ArrayList<Reserva> raux = controller.devuelveReservasUsuario(user.getId());
                    pintaReservas(raux, controller, user);
                    if (raux.size() != 0) {
                        do {
                            try {
                                System.out.println("Introduzca el numero de reserva que deseas eliminar ");
                                reserva = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Intoduce un numero");
                            }

                            if (reserva <= 0 || reserva > raux.size())
                                System.out.println("Numero de reserva incorrecto.");
                        } while (reserva <= 0 || reserva > raux.size());

                        if (controller.buscaReservaById(raux.get(reserva - 1).getIdReserva()) == null)
                            System.out.println("No existe con ese numero");
                        else {
                            Reserva aux2 = controller.buscaReservaById(raux.get(reserva - 1).getIdReserva());
                            //user.deleteReserva(aux);
                            controller.cancelaReserva(aux2.getIdReserva());
                            System.out.println("Reserva cancelada correctamente");

                        }
                        System.out.println("Pulsa para continuar...");
                        s.nextLine();
                    } else {
                        System.out.println("No tienes reservas");
                        System.out.println("Pulsa para continuar...");
                        s.nextLine();
                    }

                    break;
                case 4:
                    System.out.println(user);
                    Menu.cargandoMenu();
                    utils.Herramientas.delay("Cargando menu");
                    System.out.println();
                    System.out.println("Pulsa para continuar...");
                    s.nextLine();
                    break;
                //Hay que tocar este TODO
                case 5:
                    int opc = 0;
                    do {
                        Menu.menuModiUsuario();
                        opc = Integer.parseInt(s.nextLine());
                        switch (opc) {
                            case 1:
                                System.out.println("Introduce el nuevo nombre: ");
                                String nuevoUser = s.nextLine();
                                controller.modificaPerfil(controller.buscaUsuariobyId(user.getId()), nuevoUser, opc);
                                break;
                            case 2:
                                System.out.println("Introduce la nueva clave: ");
                                String nuevaClave = s.nextLine();
                                controller.modificaPerfil(controller.buscaUsuariobyId(user.getId()), nuevaClave, opc);
                                break;
                        }
                        System.out.println("Pulsa para continuar...");
                        s.nextLine();

                    } while (opc != 3);

                    break;
                case 6:
                    Herramientas.delay("Cerrando sesión");
                    System.out.println();
                    Persistencia.addUltimoAcceso(String.valueOf(user.getId()));
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
            }
        } while (opcion != 6);
    }

    public static void menuPropietario(GestionAPP controller, Propietario user) throws IOException {
        int opcion = 0;
        do {
            try {
                Menu.menuPropietario(controller, user);
                opcion = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: La entrada no es un número válido.");
            }

            switch (opcion) {
                case 1:
                    System.out.println(controller.buscarViviendaPorPropietario(user.getId()));
                    System.out.println("Pulsa para continuar...");
                    s.nextLine();
                    break;
                case 2:
                    try {
                        ArrayList<Vivienda> viviendas = controller.buscarViviendaPorPropietario(user.getId());
                        pintaVivienda(viviendas);
                        if (viviendas.size() != 0) {
                            int reserva = 0;
                            do {
                                System.out.println("Introduce el numero de vivienda que desea escoger: ");
                                reserva = Integer.parseInt(s.nextLine());
                                if (reserva <= 0 || reserva > viviendas.size())
                                    System.out.println("Vivienda eliminada correctamente.");
                            } while (reserva <= 0 || reserva > viviendas.size());
                            if (!controller.deleteVivienda(viviendas.get(reserva - 1))) {
                                System.out.println("Esta vivienda tiene reservas pendientes.");
                            } else {
                                controller.deleteVivienda(viviendas.get(reserva - 1));
                                System.out.println("Vivienda eliminada correctamente.");
                            }

                        } else System.out.println("No tienes viviendas.");
                    } catch (NumberFormatException e) {
                        System.out.println("Error de formato al introducir el número de vivienda.");
                    } catch (IndexOutOfBoundsException e) {

                    } catch (Exception e) {
                        System.out.println("Error en la ejecución del programa.");
                    }
                    System.out.println("Pulsa para continuar...");
                    s.nextLine();
                    break;
                case 3:
                    int ocupantes = 0;
                    double precioNoche = 0;
                    System.out.println("Titulo de la vivienda: (Introduzca salir si desea salir)");
                    String titulo = s.nextLine();
                    if (titulo.equals("salir")) break;
                    System.out.println("Descripcion: ");
                    String descripcion = s.nextLine();
                    System.out.println("Localidad: ");
                    String localidad = s.nextLine().toLowerCase();
                    System.out.println("Provincia: ");
                    String provincia = s.nextLine().toLowerCase();
                    try {
                        System.out.println("Maximo ocupantes: ");
                        ocupantes = Integer.parseInt(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Introduce un numero");
                    }
                    try {
                        System.out.println("Precio por noche: ");
                        precioNoche = Double.parseDouble(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Introduce un precio valido");
                    }
                    Vivienda aux = controller.addVivienda(user.getId(), titulo, descripcion, localidad, provincia, ocupantes, precioNoche);
                    com.enviaMail(user.getEmail(), htmlVivienda(aux), "Vivienda Introducida correctamente", "text/html; charset=utf-8", "");
                    com.enviaMensajeTelegram("El usuario " + user.getEmail() + " ha añadido una vivienda.");
                    System.out.println("Pulsa para continuar...");
                    s.nextLine();
                    break;
                case 4:
                    ArrayList<Reserva> reservas = controller.devuelveReservaPorPropietario(user.getId());
                    if (reservas.size() != 0) {
                        for (Reserva r :
                                reservas) {
                            System.out.println(r.pintaDatosParaPropietario(controller.buscaUsuariobyId(r.getIdVivienda()), controller.buscaViviendabyId(r.getIdVivienda())));
                            System.out.println("Pulse para continuar...");
                            s.nextLine();
                        }
                    } else System.out.println("No tienes reservas en ninguna de tus viviendas.");
                    s.nextLine();

                    break;
                case 5:
                    ArrayList<Vivienda> viviendas = controller.buscarViviendaPorPropietario(user.getId());
                    pintaVivienda(viviendas);
                    if (viviendas.size() != 0) {
                        int reserva = 0;
                        do {
                            try {
                                System.out.println("Introduce el numero de vivienda que desea escoger: ");
                                reserva = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Intoduce un numero.");
                            }
                            if (reserva <= 0 || reserva > viviendas.size())
                                System.out.println("Numero de vivienda incorrrecto.");
                        } while (reserva <= 0 || reserva > viviendas.size());

                        int idNodisponible = viviendas.get(reserva - 1).getId();
                        if (controller.buscaViviendabyId(idNodisponible) == null)
                            System.out.println("No existe ninguna vivienda con ese id");
                        else {
                            int noches = 0;
                            System.out.println("Introduce la fecha de inicio: ");
                            LocalDate fechaInicio = null;
                            try {
                                fechaInicio = LocalDate.parse(s.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            } catch (DateTimeParseException e) {
                                System.out.println("Error: formato de fecha incorrecto. Introduce la fecha en formato dd/MM/yyyy.");
                            }
                            do {
                                try {
                                    System.out.println("Introduce el numero de dias que va a durar el periodo: ");
                                    noches = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduce un numero.");
                                    noches = -1;
                                }
                            } while (noches <= 0);
                            controller.addPeriodoNoDisponible(controller.buscaViviendabyId(idNodisponible), fechaInicio, noches);
                        }
                    } else System.out.println("No tienes viviendas para establecer un periodo de no disponible.");
                    System.out.println("Pulsa para continuar...");
                    s.nextLine();

                    break;
                case 6:
                    System.out.println(user);
                    Menu.cargandoMenu();
                    utils.Herramientas.delay("Cargando menu");
                    System.out.println();
                    System.out.println("Pulsa para continuar...");
                    s.nextLine();
                    break;
                case 7:
                    int opc = 0;
                    do {
                        try {
                            Menu.menuModiUsuario();
                            opc = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduce un numero");
                        }
                        switch (opc) {
                            case 1:
                                System.out.println("Introduce el nuevo nombre: ");
                                String nuevoUser = s.nextLine();
                                controller.modificaPerfil(user, nuevoUser, opc);
                                System.out.println("Pulsa para continuar...");
                                s.nextLine();
                                break;
                            case 2:
                                System.out.println("Introduce la nueva clave: ");
                                String nuevaClave = s.nextLine();
                                controller.modificaPerfil(user, nuevaClave, opc);
                                System.out.println("Pulsa para continuar...");
                                s.nextLine();
                                break;
                        }

                    } while (opc != 3);

                    break;
                case 8:
                    controller.excel(controller.devuelveReservaPorPropietario(user.getId()));
                    com.enviaMail(user.getEmail(), "Excel con sus reservas", "Reservas", "application/pdf", "excel/reservas.csv");
                    System.out.println("Excel de reservas enviadas correctamente");
                    break;
                case 9:
                    Herramientas.delay("Cerrando sesión");
                    Persistencia.safeLog("Cierre sesión de: ", user.getEmail());
                    Persistencia.addUltimoAcceso(String.valueOf(user.getId()));
                    System.out.println();
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 9);


    }

    private static void menuAdmin(GestionAPP controller, Admin user) throws IOException {
        int opc = 0;
        try {
            do {
                Menu.menuAdmin(controller, user);
                opc = Integer.parseInt(s.nextLine());

                switch (opc) {
                    case 1:
                        pintaVivienda(controller.getAllViviendas());
                        System.out.println("Pulsa para continuar...");
                        s.nextLine();
                        break;
                    case 2:
                        System.out.println(controller.getUsuarios());
                        System.out.println(controller.getPropietarios());
                        System.out.println(controller.getAdmins());
                        System.out.println("Pulsa para continuar...");
                        s.nextLine();
                        break;
                    case 3:
                        if (controller.getAllReservas().size() != 0) {
                            System.out.println(controller.getAllReservas());
                        } else System.out.println("No hay reservas");
                        System.out.println("Pulsa para continuar...");
                        s.nextLine();
                        break;
                    case 4:
                        System.out.println(user);
                        System.out.println("Pulsa para continuar...");
                        s.nextLine();
                        break;
                    case 5:
                        int op = 0;
                        do {
                            Menu.menuModiUsuario();
                            op = Integer.parseInt(s.nextLine());
                            switch (op) {
                                case 1:
                                    System.out.println("Introduce el nuevo nombre: ");
                                    String nuevoUser = s.nextLine();
                                    controller.modificaPerfil(user, nuevoUser, op);
                                    System.out.println("Pulsa para continuar...");
                                    s.nextLine();
                                    break;
                                case 2:
                                    System.out.println("Introduce la nueva clave: ");
                                    String nuevaClave = s.nextLine();
                                    controller.modificaPerfil(user, nuevaClave, op);
                                    System.out.println("Pulsa para continuar...");
                                    s.nextLine();
                                    break;
                            }

                        } while (op != 3);

                        break;
                    case 6:
                        int opc2 = 0;
                        do {
                            System.out.println("Que quieres hacer un backup o recuperarlo?");
                            System.out.println("1. Backup");
                            System.out.println("2. Recuperar");
                            System.out.println("3. Salir");
                            System.out.print("Introduzca una opcion: ");
                            opc2 = Integer.parseInt(s.nextLine());
                            switch (opc2) {
                                case 1:
                                    com.enviaMensajeTelegram(user.getEmail() + " ha realizado un backup");
                                    guardarBackup();
                                    System.out.println("Guardado correctamente");
                                    break;
                                case 2:
                                    com.enviaMensajeTelegram(user.getEmail() + " ha realizado un recuperar");
                                    cargarBackup();
                                    System.out.println("Cargado correctamente");
                                    break;
                                case 3:
                                    System.out.println("Saliendo");
                                    break;
                            }
                        } while (opc2 != 3);

                        break;
                    case 7:
                        controller.excel(controller.getAllReservas());
                        com.enviaMail(user.getEmail(), "Excel con sus reservas", "Reservas", "application/pdf", "excel/reservas.csv");
                        System.out.println("Excel enviado al correo Correctamente.");
                        break;
                    case 8:
                        System.out.println("Esta es la configuracion del programa:");
                        Persistencia.leeProperties();
                        s.nextLine();
                        System.out.println("Y estos los ultimos usuarios que se conectaron:");
                        Persistencia.leeUltimosUsuarios();
                        s.nextLine();
                        break;
                    case 9:
                        Herramientas.delay("Cerrando sesión");
                        Persistencia.safeLog("Cierre sesión de: ", user.getEmail());
                        Persistencia.addUltimoAcceso(String.valueOf(user.getId()));
                        System.out.println();
                        break;
                }
            } while (opc != 9);
        } catch (NumberFormatException e) {
            System.out.println("Introduce un numero");
        }

    }

    public static boolean compruebaToken(String tokenIntroducido, int token) {
        return String.valueOf(token).equals(tokenIntroducido);
    }

    /**
     * Método que genera el token
     * @return
     */
    public static int generaToken() {
        return (int) (Math.random() * 100000);
    }

    public static String htmlToken(int token) {
        return """
                <body style="background-color: white ">


                <table style="max-width: 500px; padding: 10px; margin:0 auto; border-collapse: collapse;">
                	<tr>
                		<td style="background-color: hsl(192, 16%, 94%); text-align: left;">
                				<img width="512px" height="170px" style="display:block;" src="https://cdn.discordapp.com/attachments/1026233960335941692/1069552797550383167/5d1547c7-7391-483f-afde-7b06838d121c.jpeg">
                		</td>
                	</tr>
                	
                	<tr>
                		<td style="background-color: #ecf0f1">
                			<div style="color: #34495e; margin: 4% 10% 2%; text-align: justify;font-family: sans-serif">
                				<h2 style="color: #9e0000; margin: 0 0 7px;text-align: center;">Bienvenido a FernanBNB </h2>
                				<p style="margin: 2px; font-size: 15px">
                					En esta aplicacion podra reservar habitaciones por todo el mundo al mejor precio Podras registrarte como usuario o propietario para poder alquilar tus viviendas, y siempre con la mayor seguridad</p>
                				<ul style="font-size: 15px;  margin: 10px 0">
                					<li>Reservar viviendas</li>
                					<li>Publicar tus viviendas</li>
                					<li>Notificaciones al instante por correo</li>
                					<li>Seguridad.</li>
                					<li>Muchas sorpresas más.</li>
                				</ul>
                				
                				<div style="width: 100%; text-align: center; padding-top: 10%;">
                                    <p><b>Numero de verificación:</p>
                					<a style="text-decoration: none; border-radius: 5px; padding: 11px 23px; color: white; background-color: #9e0000">""" + token + "</a> </div><p style='color: #b3b3b3; font-size: 12px; text-align: center;margin: 30px 0 0'>FernanBNB Abraham & Jorge &copy; 2023</p></div></td></tr></table></body>";
    }

    public static String htmlReservaPdf(Reserva r, Vivienda v) {
        return "<div style=\"background-color: white; top: 0px;\">\n" +
                "        <h1 style=\"font-family: system-ui, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; text-align: center; color: grey;\">\n" +
                "            Enhorabuena por tu reserva\n" +
                "        </h1>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div style=\"border: 1px solid black;border-radius: 5px; margin-top: 1em; margin-left: 20%; width: 60%; justify-content: left; background-color: #ecf0f1; padding: 5px; box-shadow: 2px 2px 2px #621f21;\">\n" +
                "        <h3 style=\"font-family: system-ui, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; text-align: center;\">\n" +
                "            Datos de la reserva\n" +
                "        </h3>\n" +
                "        <p style=\"font-family: system-ui, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; text-align: center;\">\n" +
                "            Nombre de la vivienda:" + v.getTitulo() + "\n" +
                "        </p>\n" +
                "        <p style=\"font-family: system-ui, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; text-align: center;\">\n" +
                "            Fecha de inicio:" + r.getFechaInicio() + "\n" +
                "        </p>\n" +
                "        <p style=\"font-family: system-ui,  BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; text-align: center;\">\n" +
                "            Fecha de fin:" + r.getFechaInicio().plusDays(r.getNoches()) + "\n" +
                "        </p>\n" +
                "        <p style=\"font-family: system-ui,  BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; text-align: center;\">\n" +
                "            Numero de ocupantes:" + r.getOcupantes() + "\n" +
                "        </p>\n" +
                "        <p style=\"font-family: system-ui,  BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; text-align: center;\">\n" +
                "            Precio Total:" + (r.getNoches() * v.getPrecioNoche()) + "€" + "\n" +
                "        </p>\n" +
                "    </div>\n" +
                "    <img style=\"margin-top: 1em; width: 70%; margin-left: 15%;\" src=\"https://cdn.discordapp.com/attachments/1026233960335941692/1069552797550383167/5d1547c7-7391-483f-afde-7b06838d121c.jpeg\" alt=\"imagen\">\n" +
                "\n" +
                "   \n" +
                "\n";
    }

    public static String htmlReserva(Reserva reserva, Vivienda v, Usuario u) {
        return "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ╬ RESERVA ╬ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n " + "<br>" +
                "ID Reserva: " + reserva.getIdReserva() + "\n" + "<br>" +
                "Usuario: " + u.getNombre() + "\n" + "<br>" +
                "Vivienda: " + v.getTitulo() + " " + v.getDescripcion() + " (ID: " + reserva.getIdVivienda() + ")" + "\n" + "<br>" +
                "Noches: " + reserva.getNoches() + "\n" + "<br>" +
                "Fecha Entrada: " + reserva.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" + "<br>" +
                "Fecha Salida: " + reserva.getFechaInicio().plusDays(reserva.getNoches()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" + "<br>" +
                "Huespedes: " + reserva.getOcupantes() + "\n" + "<br>" +
                "Precio por noche: " + reserva.getPrecio() + "EUR" + "\n" + "<br>" +
                "Total a Pagar: " + reserva.getPrecio() * reserva.getNoches() + "EUR" + "\n" + "<br>" +
                "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓" + "<br>";

    }

    public static String htmlVivienda(Vivienda v1) {
        return "§§§§§§§§§§§§§§§§§§§[ █ VIVIENDA █ ]§§§§§§§§§§§§§§§§§§§ \n" + "<br>" +
                "ID: " + v1.getId() + '\n' + "<br>" +
                " Ciudad: " + v1.getLocalidad() + '\n' + "<br>" +
                " Provincia: " + v1.getProvincia() + '\n' + "<br>" +
                " Nombre: " + v1.getTitulo() + '\n' + "<br>" +
                " Direccion: " + v1.getDescripcion() + '\n' + "<br>" +
                " Numero Maximo Huespedes: " + v1.getMaxOcupantes() + '\n' + "<br>" +
                " Precio por Noche: " + v1.getPrecioNoche() + "EUR\n" + "<br>" +
                "§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§" + "<br>";
    }


    private static void cargarBackup() {
        controller.cargarBackupUsers();
        controller.cargarBackupPropietario();
        controller.cargarBackupAdmin();
    }

    private static void guardarBackup() {
        controller.guardarBackupUsers();
        controller.guardarBackupPropietario();
        controller.guardarBackupAdmin();
    }
}