package dao;

import modules.Propietario;

import java.util.ArrayList;

public interface DAOPropietario {

    public boolean insertarPropietario(Propietario propietario, DAOManager daoManager);

    public ArrayList<Propietario> buscarPropietariosbyID(int id,DAOManager daoManager);

    public boolean updatePropietario(Propietario propietario, DAOManager daoManager);
    public boolean deleteALL(DAOManager daoManager);

    public ArrayList<Propietario> readAll(DAOManager daoManager);
}
