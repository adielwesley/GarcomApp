package br.edu.ufam.garcomapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class DetalhesDePedidoActivity extends ActionBarActivity {
    private Intent intent;
    private String notaString;
    private ArrayList<PratoPedido> pratos;
    private PedidoDAO pedidoBD;
    private int numeroMesa;
    private double preco;
    private TextView precoTotal;
    private PratoPedidoAdapter pratoPedidoAdapter;
    private ListView pratosPedidosListView;
    protected NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
    private Button nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_de_pedido);

        intent = getIntent();

        numeroMesa = intent.getIntExtra("mesa",0);
        this.setTitle("Pedido da Mesa " + numeroMesa);
        notaString = intent.getStringExtra("nota");
        pedidoBD = new PedidoDAO(this);
        pratos = pedidoBD.converteStringParaArrayListDePratoPedido(intent.getStringExtra("pratos"));
        preco = intent.getDoubleExtra("preco", 0);

        precoTotal = (TextView) findViewById(R.id.detalhespedido_textView_totalapagar);
        precoTotal.setText("R$ " + format.format(preco));

        pratoPedidoAdapter = new PratoPedidoAdapter(this, pratos);
        pratosPedidosListView = (ListView) findViewById(R.id.detalhespedido_listView_pratospedidos);
        pratosPedidosListView.setAdapter(pratoPedidoAdapter);

        nota = (Button) findViewById(R.id.detalhespedido_button_vernota);
        if(notaString.compareTo("")==0)
            nota.setVisibility(View.INVISIBLE);
    }

    public void onResume() {
        super.onResume();
        Pedido pedido = pedidoBD.getPedido(numeroMesa);
        precoTotal.setText("R$ " + format.format(pedido.getPrecoTotal()));
        notaString = pedido.getNota();
        if(notaString.compareTo("")==0)
            nota.setVisibility(View.INVISIBLE);
        else
            nota.setVisibility(View.VISIBLE);
        pratoPedidoAdapter = new PratoPedidoAdapter(this, pedido.getPratosPedidos());
        pratosPedidosListView.setAdapter(pratoPedidoAdapter);
    }

    public void verNotaOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Nota");
        builder.setMessage(notaString);
        builder.setNeutralButton("Fechar",null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void atendidoOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pedido atendido?");
        builder.setMessage("Marcar o pedido como atendido excluirá ele da lista de pedidos feitos. Você realmente quer marcá-lo como atendido?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pedidoBD.delPedido(numeroMesa);
                finish();
                atualizaListView();
            }
        });
        builder.setNegativeButton("Não", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void atualizaListView() {
        pratoPedidoAdapter = new PratoPedidoAdapter(this, pratos);
        pratosPedidosListView.setAdapter(pratoPedidoAdapter);
    }

    public void alterarOnClick(View view) {
        Intent alterarPedido = new Intent(this,AlterarPedidoActivity.class);
        Pedido pedido = pedidoBD.getPedido(numeroMesa);
        alterarPedido.putExtra("mesa", numeroMesa);
        alterarPedido.putExtra("pratos", pedido.converteArrayListPratoPedidoEmString());
        alterarPedido.putExtra("nota", pedido.getNota());
        alterarPedido.putExtra("preco", pedido.getPrecoTotal());
        startActivity(alterarPedido);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhes_de_pedido, menu);
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
