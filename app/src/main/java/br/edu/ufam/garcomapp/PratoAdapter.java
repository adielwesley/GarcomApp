package br.edu.ufam.garcomapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by AdielW on 11/02/2015.
 */
public class PratoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Prato> pratos;
    protected NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    public PratoAdapter(Context context, ArrayList<Prato> pratos) {
        this.context = context;
        this.pratos = pratos;
    }

    @Override
    //retorna tamanho da lista
    public int getCount() {
        return pratos.size();
    }

    public int getPosition(Prato prato) {
        return pratos.indexOf(prato);
    }

    @Override
    //retorna item da posição recebida como argumento do método
    public Object getItem(int position) {
        return pratos.get(position);
    }

    @Override
    //A posição será o ID do elemento
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Prato prato = pratos.get(position);
        View layout;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.pratos, null);
        } else {
            layout = convertView;
        }

        TextView nome = (TextView) layout.findViewById(R.id.pratos_textView_nome);
        nome.setText(prato.getId() + " - " + prato.getNome());

        TextView preco = (TextView) layout.findViewById(R.id.pratos_textView_preco);
        preco.setText("R$ " + format.format(prato.getPreco()));

        return layout;
    }

}
