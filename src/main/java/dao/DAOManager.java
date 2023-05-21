package dao;

import persistencia.Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOManager {
    private Connection conn;
    private final String USER;
    private final String PASS;
    private final String URL;
    private static DAOManager singlenton; //Atributo estático que guarda una referencia al dao

    public DAOManager() {
        this.conn = null;
        this.URL = Persistencia.leerProperties("URL");
        this.USER = Persistencia.leerProperties("USER");
        this.PASS = Persistencia.leerProperties("PASS");

    }

    public static DAOManager getSinglentonInstance() {
        if (singlenton == null) {
            singlenton = new DAOManager();
        }
        return singlenton;
    }

    public Connection getConn() {
        return conn;
    }

    public void open() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw e;
        }
    }

    public void close() throws SQLException {
        try {
            if (this.conn != null) this.conn.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
