package br.edu.ufam.garcomapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AdielW on 11/02/2015.
 */
public class PratoDAO {
    private SQLiteDatabase bancoDeDados;

    public PratoDAO(Context context) {
        this.bancoDeDados = (new BancoDeDados(context)).getWritableDatabase();
    }

    public Prato getPrato(String nome) {
        Prato prato = null;
        String sqlQuery = "SELECT * FROM Pratos WHERE " +
                "nome='" + nome + "'";
        Cursor cursor = this.bancoDeDados.rawQuery(sqlQuery, null);
        if (cursor.moveToNext()) {
            prato = new Prato(cursor.getInt(0), nome, cursor.getDouble(2),
                    converteStringParaArrayListDeString(cursor.getString(3)), cursor.getInt(4));
        }
        cursor.close();
        return prato;
    }

    public boolean addPrato(Prato prato) {
        try {
            String sqlCmd = "INSERT INTO Pratos VALUES ('" + prato.getId() + "', '" +
                    prato.getNome() + "', '" + prato.getPreco() + "', '" +
                    prato.converteArrayListDeStringsIngredientesEmString() + "', '" + prato.getPeso() + "')";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        } catch(Exception e) {
            Log.i("Exception", e.getMessage().toString());
            return false;
        }
    }

    public boolean delPrato(int id) {
        try{
            String sqlCmd = "DELETE FROM Pratos WHERE id  = '" + id + "'";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        } catch(Exception e) {
            Log.i("Exception", e.getMessage().toString());
            return  false;
        }
    }

    public boolean setPrato(Prato prato) {
        try {
            ContentValues dados=new ContentValues();
            dados.put("nome", prato.getNome());
            dados.put("preco", prato.getPreco());
            dados.put("ingredientesListView", prato.converteArrayListDeStringsIngredientesEmString());
            dados.put("peso", prato.getPeso());
            this.bancoDeDados.update("Pratos", dados, "id=" + prato.getId(), null);
            return true;
        } catch(Exception e) {
            Log.i("Exception", e. getMessage().toString());
            return  false;
        }
    }

    public ArrayList<String> converteStringParaArrayListDeString (String s) {
        ArrayList<String> lista = new ArrayList<String>();
        String aux;

        if (s=="") return null;

        int i=0,f=s.indexOf('\n');
        while (i!=s.length()) {
            lista.add(s.substring(i, f));
            aux = s.substring(f+1,s.length());
            i = f+1;
            f = aux.indexOf('\n')+i;
        }

        return lista;
    }

    public ArrayList<Prato> retornaPratos() {
        ArrayList<Prato> pratos = new ArrayList<Prato>();
        String sqlCmd = "SELECT * FROM Pratos";

        Cursor cursor = this.bancoDeDados.rawQuery(sqlCmd,null);

        try {
            while (cursor.moveToNext()) {
                Prato prato = new Prato(cursor.getInt(0), cursor.getString(1),cursor.getDouble(2),
                        converteStringParaArrayListDeString(cursor.getString(3)),cursor.getInt(4));
                pratos.add(prato);
            }
            cursor.close();
            return pratos;
        } catch(Exception e) {
            Log.e("Exception ", e.getMessage().toString());
            return null;
        }
    }
}
