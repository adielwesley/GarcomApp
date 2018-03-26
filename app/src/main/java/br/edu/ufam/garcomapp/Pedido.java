package br.edu.ufam.garcomapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AdielW on 15/02/2015.
 */
public class Pedido implements Serializable {
    private int numeroDaMesa;
    private ArrayList<PratoPedido> pratosPedidos;
    private String nota;
    private double precoTotal;

    public Pedido(int numeroDaMesa, ArrayList<PratoPedido> pratosPedidos, String nota, double precoTotal) {
        this.numeroDaMesa = numeroDaMesa;
        this.pratosPedidos = pratosPedidos;
        this.nota = nota;
        this.precoTotal = precoTotal;
    }

    public String converteArrayListPratoPedidoEmString() {
        String s = new String();

        for (PratoPedido p : pratosPedidos) {
            s = s.concat(p.getId() + "*" + p.getNome() + "*" + p.getPreco() + "*" + p.getQuantidade() + "*|");
        }

        return s;
    }

    public int getNumeroDaMesa() {
        return numeroDaMesa;
    }

    public void setNumeroDaMesa(int numeroDaMesa) {
        this.numeroDaMesa = numeroDaMesa;
    }

    public ArrayList<PratoPedido> getPratosPedidos() {
        return pratosPedidos;
    }

    public void setPratosPedidos(ArrayList<PratoPedido> pratosPedidos) {
        this.pratosPedidos = pratosPedidos;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }
}
