package br.edu.ufam.garcomapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CardapioActivity extends ActionBarActivity {
    private ArrayList<Prato> pratosArrayList;
    private PratoDAO pratoBD;
    private ListView pratosListView;
    private PratoAdapter pratoAdapter;
    protected NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
    private TextView nenhumPrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        pratoBD = new PratoDAO(this);
        pratosArrayList = pratoBD.retornaPratos();
        pratoAdapter = new PratoAdapter(this,pratosArrayList);

        nenhumPrato = (TextView) findViewById(R.id.cardapio_textView_nenhumprato);
        if(!pratosArrayList.isEmpty()) {
            nenhumPrato.setVisibility(View.INVISIBLE);
        }

        pratosListView = (ListView) findViewById(R.id.cardapio_listView_pratos);
        pratosListView.setAdapter(pratoAdapter);
        pratosListView.setOnItemClickListener(chamaActivityPrato(this, pratosArrayList));
    }

    protected void onResume() {
        super.onResume();
        atualizaLista();
        if(pratosArrayList.isEmpty()) {
            nenhumPrato.setVisibility(View.VISIBLE);
        } else
            nenhumPrato.setVisibility(View.INVISIBLE);
    }

    private void atualizaLista() {
        pratosArrayList = pratoBD.retornaPratos();
        pratoAdapter = new PratoAdapter(this, pratosArrayList);
        pratosListView.setAdapter(pratoAdapter);
        pratosListView.setOnItemClickListener(chamaActivityPrato(this, pratosArrayList));
    }

    public AdapterView.OnItemClickListener chamaActivityPrato(final Context context, final ArrayList<Prato> pratos) {
        return (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog alert = builder.create();

                LayoutInflater layout = getLayoutInflater();
                View viewPrato = layout.inflate(R.layout.pratosdetalhes, null);

                TextView preco = (TextView) viewPrato.findViewById(R.id.pratodetalhes_textView_precobanco);
                preco.setText("R$ " + format.format(pratos.get(position).getPreco()));
                TextView peso = (TextView) viewPrato.findViewById(R.id.pratodetalhes_textView_pesobanco);
                peso.setText(String.valueOf(pratos.get(position).getPeso()) + "g");
                TextView ingredientes = (TextView) viewPrato.findViewById(R.id.pratodetalhes_textView_ingredientesbanco);
                ingredientes.setText(pratos.get(position).converteArrayListDeStringsIngredientesEmString());

                alert.setTitle(pratos.get(position).getNome().toUpperCase());
                alert.setView(viewPrato);
                alert.show();

                viewPrato.findViewById(R.id.pratodetalhes_button_alterar).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent alterarPrato = new Intent(context, AlterarPratoActivity.class);
                        alterarPrato.putExtra("id", pratos.get(position).getId());
                        alterarPrato.putExtra("nome", pratos.get(position).getNome().toString());
                        alterarPrato.putExtra("preco", String.valueOf(pratos.get(position).getPreco()));
                        alterarPrato.putExtra("peso", String.valueOf(pratos.get(position).getPeso()));
                        alterarPrato.putExtra("ingredientes", pratos.get(position).converteArrayListDeStringsIngredientesEmString());

                        startActivity(alterarPrato);
                        alert.dismiss();
                    }
                });

                viewPrato.findViewById(R.id.pratodetalhes_button_excluir).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pratoBD.delPrato(pratos.get(position).getId())) {
                            atualizaLista();
                            if(pratosArrayList.isEmpty()) {
                                nenhumPrato.setVisibility(View.VISIBLE);
                            } else
                                nenhumPrato.setVisibility(View.INVISIBLE);
                            alert.dismiss();
                            Toast.makeText(context, "Prato excluído com sucesso",Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "Erro! Não foi possível remover o prato", Toast.LENGTH_SHORT).show();
                    }
                });

                viewPrato.findViewById(R.id.pratodetalhes_button_fechar).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

            }
        });
    }

    public void novoPratoOnClick(View view) {
        Intent novoPrato1 = new Intent(this, NovoPratoActivity1.class);
        startActivity(novoPrato1);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cardapio, menu);
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
