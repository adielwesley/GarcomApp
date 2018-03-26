package br.edu.ufam.garcomapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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


public class NovoPedidoActivity extends ActionBarActivity {
    private PedidoDAO pedidoBD;
    private PratoAdapter pratoBDAdapter;
    private PratoPedidoAdapter pratoAddAdapter;
    private ArrayList<Prato> pratosBD;
    private ArrayList<PratoPedido> pratosAdicionados = new ArrayList<>();
    private Spinner pratosOpcoes;
    private ListView pratosAdicionadosListView;
    private TextView precoTotalEditText;
    private EditText quantidadeEditText;
    private double precoTotalDouble = 0.0;
    private int quantidadeInt = 0;
    private String notaString = "";
    private int numMesaInt;
    protected NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_pedido);

        PratoDAO pratoBD = new PratoDAO(this);
        pedidoBD = new PedidoDAO(this);
        pratosBD = pratoBD.retornaPratos();
        pratosOpcoes = (Spinner) findViewById(R.id.novopedido_spinner_pratos);
        pratoBDAdapter = new PratoAdapter(this, pratosBD);
        pratosOpcoes.setAdapter(pratoBDAdapter);

        pratosAdicionadosListView = (ListView) findViewById(R.id.novopedido_listView_pratosadicionados);
        pratoAddAdapter = new PratoPedidoAdapter(this, pratosAdicionados);
        pratosAdicionadosListView.setAdapter(pratoAddAdapter);
        pratosAdicionadosListView.setOnItemClickListener(pratoClicado(this));
    }

    private AdapterView.OnItemClickListener pratoClicado(final Context context) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Excluir Prato Adicionado");
                builder.setMessage("Deseja excluir esse prato?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pratosAdicionados.remove(position);
                        atualizaListView();
                    }
                });
                builder.setNegativeButton("Não",null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }

    public void OnResume() {
        super.onResume();
        atualizaListView();
    }

    public void adicionarOnClick(View view) {
        Prato pratoSelecionado = pratosBD.get(pratosOpcoes.getSelectedItemPosition());
        precoTotalEditText = (TextView) findViewById(R.id.novopedido_textView_precototal);
        quantidadeEditText = (EditText) findViewById(R.id.novopedido_editText_quantidade);
        try {
            quantidadeInt = Integer.parseInt(quantidadeEditText.getText().toString());
        } catch( Exception e) {
            quantidadeInt = 1;
        }
        precoTotalDouble += pratoSelecionado.getPreco()*quantidadeInt;
        precoTotalEditText.setText("R$ " + format.format(precoTotalDouble));
        pratosAdicionados.add(new PratoPedido(pratoSelecionado.getId(), pratoSelecionado.getNome(), pratoSelecionado.getPreco(),quantidadeInt));
        atualizaListView();
    }

    private void atualizaListView() {
        pratoAddAdapter = new PratoPedidoAdapter(this, pratosAdicionados);
        pratosAdicionadosListView.setAdapter(pratoAddAdapter);
    }

    public void adicionarNotaOnClick(View view) {
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

    public void cadastrarOnClick(View view) {
        EditText numMesaEditText = (EditText) findViewById(R.id.novopedido_editText_nummesa);
        try {
            numMesaInt = Integer.parseInt(numMesaEditText.getText().toString());
            if (verificaSeHaPedidoParaEssaMesa(numMesaInt)) {
                Toast.makeText(this, "Já há um pedido cadastrado para essa mesa.", Toast.LENGTH_LONG).show();
            } else {
                Pedido pedido = new Pedido(numMesaInt, pratosAdicionados, notaString, precoTotalDouble);
                if (pedidoBD.addPedido(pedido)) {
                    Toast.makeText(this, "Pedido cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Erro! O pedido não foi cadastrado.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Por favor, informe o número da mesa.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verificaSeHaPedidoParaEssaMesa(int numMesaInt) {
        ArrayList<Pedido> pedidos = pedidoBD.retornaPedidos();
        for(Pedido p:pedidos)
            if (p.getNumeroDaMesa() == numMesaInt)
                return true;
        return false;
    }

    public void cancelarOnClick(View view) {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_novo_pedido, menu);
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
