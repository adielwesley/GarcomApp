package br.edu.ufam.garcomapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class AlterarPedidoActivity extends ActionBarActivity {
    private Intent intent;
    private PratoDAO pratoBD;
    private int numeroMesa;
    private String notaString;
    private PedidoDAO pedidoBD;
    private ArrayList<PratoPedido> pratosPedidos;
    private double precoTotalDouble;
    private TextView precoTotalTextView;
    protected NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
    private PratoPedidoAdapter pratoPedidoAdapter;
    private ListView pratosPedidosListView;
    private ArrayList<Prato> pratosBDArrayList;
    private PratoAdapter pratoAdapter;
    private Spinner pratosBDSpinner;
    private EditText quantidadeEditText;
    private int quantidadeInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_pedido);

        intent = getIntent();
        numeroMesa = intent.getIntExtra("mesa",0);
        this.setTitle("Alterar Pedido da Mesa " + numeroMesa);
        notaString = intent.getStringExtra("nota");
        pedidoBD = new PedidoDAO(this);
        pratosPedidos = pedidoBD.converteStringParaArrayListDePratoPedido(intent.getStringExtra("pratos"));
        precoTotalDouble = intent.getDoubleExtra("preco", 0);

        precoTotalTextView = (TextView) findViewById(R.id.alterarpedido_textView_precoTotal);
        precoTotalTextView.setText("R$ " + format.format(precoTotalDouble));

        pratoBD = new PratoDAO(this);
        pratosBDArrayList = pratoBD.retornaPratos();
        pratoAdapter = new PratoAdapter(this, pratosBDArrayList);
        pratosBDSpinner = (Spinner) findViewById(R.id.alterarpedido_spinner_pratosBD);
        pratosBDSpinner.setAdapter(pratoAdapter);


        pratoPedidoAdapter = new PratoPedidoAdapter(this, pratosPedidos);
        pratosPedidosListView = (ListView) findViewById(R.id.alterarpedido_listView_pratosadicionados);
        pratosPedidosListView.setAdapter(pratoPedidoAdapter);
        pratosPedidosListView.setOnItemClickListener(excluirPrato(this));

        Button nota = (Button) findViewById(R.id.alterarpedido_button_adicionarnota);
        if(notaString.compareTo("")!=0)
            nota.setText("Alterar Nota");
    }

    private AdapterView.OnItemClickListener excluirPrato(final Context context) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Excluir Prato");
                builder.setMessage("Você deseja excluir esse prato?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        precoTotalDouble -= pratosPedidos.get(position).getPreco()*pratosPedidos.get(position).getQuantidade();
                        pratosPedidos.remove(position);
                        precoTotalTextView.setText("R$ " + format.format(precoTotalDouble));
                        atualizaListView();
                        Toast.makeText(context,"Prato excluído!",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Não", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };
    }

    private void atualizaListView() {
        pratoPedidoAdapter = new PratoPedidoAdapter(this, pratosPedidos);
        pratosPedidosListView.setAdapter(pratoPedidoAdapter);
    }

    public void adicionarOnClick(View view) {
        Prato pratoSelecionado = pratosBDArrayList.get(pratosBDSpinner.getSelectedItemPosition());
        precoTotalTextView = (TextView) findViewById(R.id.alterarpedido_textView_precoTotal);
        quantidadeEditText = (EditText) findViewById(R.id.alterarpedido_editText_quantidade);
        try {
            quantidadeInt = Integer.parseInt(quantidadeEditText.getText().toString());
        } catch( Exception e) {
            quantidadeInt = 1;
        }
        precoTotalDouble += pratoSelecionado.getPreco()*quantidadeInt;
        precoTotalTextView.setText("R$ " + format.format(precoTotalDouble));
        pratosPedidos.add(new PratoPedido(pratoSelecionado.getId(), pratoSelecionado.getNome(), pratoSelecionado.getPreco(), quantidadeInt));
        atualizaListView();
    }

    public void alterarOnClick(View view) {
        Pedido pedido = new Pedido(numeroMesa, pratosPedidos, notaString, precoTotalDouble);
        if (pedidoBD.setPedido(pedido)) {
            Toast.makeText(this, "Pedido alterado com sucesso.", Toast.LENGTH_SHORT).show();
            finish();
        } else
            Toast.makeText(this, "Erro! O pedido não foi alterado.", Toast.LENGTH_SHORT).show();
    }

    public void alterarNotaOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View notaView = inflater.inflate(R.layout.nota, null);
        final EditText notaEditText = (EditText) notaView.findViewById(R.id.nota_editText_nota);
        notaEditText.setText(notaString);

        builder.setTitle("Nota");
        builder.setView(notaView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notaString = notaEditText.getText().toString();
            }
        });
        builder.setNegativeButton("Fechar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void cancelarOnClick(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alterar_pedido, menu);
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
