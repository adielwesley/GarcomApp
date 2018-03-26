package br.edu.ufam.garcomapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AdielW on 12/02/2015.
 */
public class IngredienteAdapter extends BaseAdapter {
    private ArrayList<String> lista;
    private Context context;

    public IngredienteAdapter(Context context, ArrayList<String> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.ingredientes, null);
        } else {
            layout = convertView;
        }

        TextView nome = (TextView) layout.findViewById(R.id.ingredientes_textView_nomeingrediente);
        nome.setText(lista.get(position));

        return layout;
    }
}
