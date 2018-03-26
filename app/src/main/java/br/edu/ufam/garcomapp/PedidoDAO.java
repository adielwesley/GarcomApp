package br.edu.ufam.garcomapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AdielW on 15/02/2015.
 */
public class PedidoDAO {
    private SQLiteDatabase bancoDeDados;

    public PedidoDAO(Context context) {
        this.bancoDeDados = (new BancoDeDados(context)).getWritableDatabase();
    }

    public Pedido getPedido(int nummesa) {
        Pedido pedido = null;
        String sqlQuery = "SELECT * FROM Pedidos WHERE " +
                "mesa='" + nummesa + "'";
        Cursor cursor = this.bancoDeDados.rawQuery(sqlQuery, null);
        if (cursor.moveToNext()) {
            pedido = new Pedido(cursor.getInt(0), converteStringParaArrayListDePratoPedido(cursor.getString(1)), cursor.getString(2),
                    cursor.getDouble(3));
        }
        cursor.close();
        return pedido;
    }

    public boolean addPedido(Pedido pedido) {
        try {
            String sqlCmd = "INSERT INTO Pedidos VALUES ('" + pedido.getNumeroDaMesa() + "', '" +
                    pedido.converteArrayListPratoPedidoEmString() + "', '" + pedido.getNota() + "', '" +
                    pedido.getPrecoTotal() + "')";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        } catch(Exception e) {
            Log.i("Exception", e.getMessage().toString());
            return false;
        }
    }

    public boolean delPedido(int nummesa) {
        try{
            String sqlCmd = "DELETE FROM Pedidos WHERE mesa  = '" + nummesa + "'";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        } catch(Exception e) {
            Log.i("Exception", e.getMessage().toString());
            return  false;
        }
    }

    public boolean setPedido(Pedido pedido) {
        try {
            ContentValues dados=new ContentValues();
            dados.put("mesa", pedido.getNumeroDaMesa());
            dados.put("pratos", pedido.converteArrayListPratoPedidoEmString());
            dados.put("nota", pedido.getNota());
            dados.put("preco", pedido.getPrecoTotal());
            this.bancoDeDados.update("Pedidos", dados, "mesa=" + pedido.getNumeroDaMesa(), null);
            return true;
        } catch(Exception e) {
            Log.i("Exception", e. getMessage().toString());
            return  false;
        }
    }

    public ArrayList<PratoPedido> converteStringParaArrayListDePratoPedido (String s) {
        ArrayList<PratoPedido> lista = new ArrayList<PratoPedido>();
        int idPrato;
        String nomePrato;
        int quantidadePrato;
        double precoPrato;
        String aux1, aux2;

        if (s=="") return null;

        int i=0,f=s.indexOf('|');
        while (i!=s.length()) {
            aux1 = s.substring(i, f);
            int j=0,k=aux1.indexOf('*');
            idPrato = Integer.parseInt(aux1.substring(j,k));
            aux2 = aux1.substring(k+1, aux1.length());
            j=k+1;
            k = aux2.indexOf('*')+j;
            nomePrato = aux1.substring(j,k);
            aux2 = aux1.substring(k+1, aux1.length());
            j=k+1;
            k = aux2.indexOf('*')+j;
            precoPrato = Double.parseDouble(aux1.substring(j,k));
            aux2 = aux1.substring(k+1, aux1.length());
            j=k+1;
            k = aux2.indexOf('*')+j;
            quantidadePrato = Integer.parseInt(aux1.substring(j, k));
            aux1 = s.substring(f+1,s.length());
            i = f+1;
            f = aux1.indexOf('|')+i;
            lista.add(new PratoPedido(idPrato,nomePrato,precoPrato,quantidadePrato));
        }

        return lista;
    }

    public ArrayList<Pedido> retornaPedidos() {
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        String sqlCmd = "SELECT * FROM Pedidos";

        Cursor cursor = this.bancoDeDados.rawQuery(sqlCmd,null);

        try {
            while (cursor.moveToNext()) {
                Pedido pedido = new Pedido(cursor.getInt(0), converteStringParaArrayListDePratoPedido(cursor.getString(1)),cursor.getString(2),
                        cursor.getDouble(3));
                pedidos.add(pedido);
            }
            cursor.close();
            return pedidos;
        } catch(Exception e) {
            Log.e("Exception ", e.getMessage().toString());
            return null;
        }
    }
}
