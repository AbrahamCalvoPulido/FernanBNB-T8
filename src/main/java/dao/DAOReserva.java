package dao;

import modules.Propietario;
import modules.Reserva;

import java.util.ArrayList;

public interface DAOReserva {
    public boolean insertarReserva(Reserva reserva, DAOManager daoManager);

    public ArrayList<Reserva> buscarReservabyID(int id, DAOManager daoManager);
    public ArrayList<Reserva> buscarReservabyIDVivienda(int id, DAOManager daoManager);
    public ArrayList<Reserva> buscarReservabyIDUsuario(int id, DAOManager daoManager);

    public boolean updateReserva(Reserva reserva, DAOManager daoManager);
    public boolean delete(Reserva reserva, DAOManager daoManager);

    public boolean deleteAll(DAOManager daoManager);

    public ArrayList<Reserva> readAll(DAOManager daoManager);

}
