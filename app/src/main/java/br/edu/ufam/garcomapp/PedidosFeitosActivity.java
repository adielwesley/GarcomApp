package br.edu.ufam.garcomapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class PedidosFeitosActivity extends ActionBarActivity {
    private PedidoDAO pedidoBD;
    private ArrayList<Pedido> pedidosFeitosArrayList;
    private PedidoAdapter pedidoAdapter;
    private ListView pedidosFeitosListView;
    private TextView nenhumPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_feitos);

        pedidoBD = new PedidoDAO(this);
        pedidosFeitosArrayList = pedidoBD.retornaPedidos();

        nenhumPedido = (TextView) findViewById(R.id.pedidosfeitos_textView_nenhumpedido);
        if (!pedidosFeitosArrayList.isEmpty())
            nenhumPedido.setVisibility(View.INVISIBLE);

        pedidoAdapter = new PedidoAdapter(this, pedidosFeitosArrayList);
        pedidosFeitosListView = (ListView) findViewById(R.id.pedidosfeitos_listView_pedidos);
        pedidosFeitosListView.setAdapter(pedidoAdapter);
        pedidosFeitosListView.setOnItemClickListener(mostraDetalhes(this));
    }

    public void onResume() {
        super.onResume();
        atualizaListView();
        if (pedidosFeitosArrayList.isEmpty())
            nenhumPedido.setVisibility(View.VISIBLE);
        else
            nenhumPedido.setVisibility(View.INVISIBLE);
    }

    private void atualizaListView() {
        pedidosFeitosArrayList = pedidoBD.retornaPedidos();
        pedidoAdapter = new PedidoAdapter(this, pedidosFeitosArrayList);
        pedidosFeitosListView.setAdapter(pedidoAdapter);
    }

    private AdapterView.OnItemClickListener mostraDetalhes(final Context context) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetalhesDePedidoActivity.class);
                intent.putExtra("mesa", pedidosFeitosArrayList.get(position).getNumeroDaMesa());
                intent.putExtra("pratos", pedidosFeitosArrayList.get(position).converteArrayListPratoPedidoEmString());
                intent.putExtra("nota", pedidosFeitosArrayList.get(position).getNota());
                intent.putExtra("preco", pedidosFeitosArrayList.get(position).getPrecoTotal());

                startActivity(intent);
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedidos_feitos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
