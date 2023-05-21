package dao;

import modules.Vivienda;

import java.util.ArrayList;

public interface DAOVivienda {
    public boolean insertarVivienda(Vivienda vivienda,DAOManager daoManager,int idPropietario);

    public boolean deleteVivienda(Vivienda vivienda,DAOManager daoManager);

    public boolean deleteAll(DAOManager daoManager);

    public ArrayList<Vivienda> readAll(DAOManager daoManager);

     public ArrayList<Vivienda> readViviendaCiudad(String ciudad,DAOManager daoManager);

     public ArrayList<Vivienda> readViviendaProvincia(String provincia,DAOManager daoManager);
}
