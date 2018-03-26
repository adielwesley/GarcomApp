package br.edu.ufam.garcomapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AdielW on 15/02/2015.
 */
public class PedidoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Pedido> pedidos;

    public PedidoAdapter(Context context, ArrayList<Pedido> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }

    @Override
    //retorna tamanho da lista
    public int getCount() {
        return pedidos.size();
    }

    public int getPosition(Pedido pedido) {
        return pedidos.indexOf(pedido);
    }

    @Override
    //retorna item da posição recebida como argumento do método
    public Object getItem(int position) {
        return pedidos.get(position);
    }

    @Override
    //A posição será o ID do elemento
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pedido pedido = pedidos.get(position);
        View layout;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.pedido, null);
        } else {
            layout = convertView;
        }

        TextView mesa = (TextView) layout.findViewById(R.id.pedido_textView_numerodamesa);
        mesa.setText("Mesa " + pedido.getNumeroDaMesa());

        return layout;
    }
}
