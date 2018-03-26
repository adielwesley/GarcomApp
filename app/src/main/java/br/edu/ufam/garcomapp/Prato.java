package br.edu.ufam.garcomapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AdielW on 11/02/2015.
 */
public class Prato implements Serializable {

    private int id;
    private String nome;
    private double preco;
    private ArrayList<String> ingredientes;
    private int peso;

    public Prato(int id, String nome, double preco, ArrayList<String> ingredientes, int peso) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.ingredientes = ingredientes;
        this.peso = peso;
    }

    public String converteArrayListDeStringsIngredientesEmString() {
        String s = "";

        for (String i : this.ingredientes) {
            s = s.concat(i + "\n");
        }

        return s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public int getPeso() {
        return peso;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}

