package br.edu.ufam.garcomapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AdielW on 11/02/2015.
 */
public class BancoDeDados extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PedidoFacil.db";
    private static final String SQL_CREATE_TABLE_PRATO = "CREATE TABLE Pratos(" +
            "id INT PRIMARY KEY, nome TEXT, preco DOUBLE, ingredientesListView TEXT, peso INT)";
    private static final String SQL_CREATE_TABLE_PEDIDO = "CREATE TABLE Pedidos(" +
            "mesa INT PRIMARY KEY, pratos TEXT, nota TEXT, preco DOUBLE)";
    private static final String SQL_POPULATE_TABLES = "INSERT INTO Pratos " +
            "VALUES (0000,'Filé de Frango', 15.0, 'Arroz\nFeijão\nSalada\nFritas\n', 600)";
    private static final String SQL_POPULATE_TABLES2 = "INSERT INTO Pedidos " +
            "VALUES (0,'00000*Filé de Frango*15.0*2*|', '', 30.0)";

    private static final String SQL_DELETE_TABLES = "DROP TABLE IF EXISTS Usuarios";

    public BancoDeDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PRATO);
        db.execSQL(SQL_CREATE_TABLE_PEDIDO);
        db.execSQL(SQL_POPULATE_TABLES);
        db.execSQL(SQL_POPULATE_TABLES2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLES);
        onCreate(db);
    }
}
