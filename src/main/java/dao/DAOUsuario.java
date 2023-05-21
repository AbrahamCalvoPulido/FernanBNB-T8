package dao;

import modules.Reserva;
import modules.Usuario;

import java.util.ArrayList;

public interface DAOUsuario {
    public boolean insertarUsuario(Usuario usuario, DAOManager daoManager);

    public ArrayList<Usuario> buscarUsuariobyEmail(String email, DAOManager daoManager);

    public boolean updateUsuario(Usuario usuario, DAOManager daoManager);
    public boolean delete(Usuario usuario, DAOManager daoManager);

    public boolean deleteAll(DAOManager daoManager);

    public ArrayList<Usuario> readAll(DAOManager daoManager);
}
