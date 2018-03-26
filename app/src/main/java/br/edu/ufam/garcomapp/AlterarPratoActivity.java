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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class AlterarPratoActivity extends ActionBarActivity {
    String nomeString;
    EditText nomeET;
    EditText precoET;
    EditText pesoET;
    ArrayList<String> stringArrayList;
    PratoDAO pratoDAO;
    Intent alterarPrato;
    IngredienteAdapter ingredienteAdapter;
    ListView ingredientesLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_prato);

        alterarPrato = getIntent();
        nomeString = alterarPrato.getStringExtra("nome");
        String precoString = alterarPrato.getStringExtra("preco");
        String pesoString = alterarPrato.getStringExtra("peso");
        String ingredientesString = alterarPrato.getStringExtra("ingredientes");

        nomeET = (EditText) findViewById(R.id.alterarprato_editText_novonome);
        nomeET.setText(nomeString);

        precoET = (EditText) findViewById(R.id.alterarprato_editText_preco);
        precoET.setText(precoString);

        pesoET = (EditText) findViewById(R.id.alterarprato_editText_peso);
        pesoET.setText(pesoString);

        pratoDAO = new PratoDAO(this);
        stringArrayList = pratoDAO.converteStringParaArrayListDeString(ingredientesString);
        ingredienteAdapter = new IngredienteAdapter(this, stringArrayList);
        ingredientesLV = (ListView) findViewById(R.id.alterarprato_listView_ingredientes);
        ingredientesLV.setAdapter(ingredienteAdapter);
        ingredientesLV.setOnItemClickListener(ingredientesLVListener(this, stringArrayList));
    }

    private AdapterView.OnItemClickListener ingredientesLVListener(final Context context, ArrayList<String> ingredientes) {
        return (new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Excluir Ingrediente");
                builder.setMessage("Deseja excluir esse ingrediente?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stringArrayList.remove(position);
                        atualizaListaDeIngredientes();
                    }
                });
               builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        //não faz nada
                   }
               });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void onResume() {
        super.onResume();
        atualizaListaDeIngredientes();
    }

    public void adicionarIngredienteOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View layout = View.inflate(this, R.layout.novoingrediente, null);

        builder.setTitle("Adicionar Ingrediente");
        builder.setView(layout);
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText ingredienteET = (EditText) layout.findViewById(R.id.novoingrediente_editText_ingrediente);
                String ingrediente = (String) ingredienteET.getText().toString();
                stringArrayList.add(ingrediente);
                atualizaListaDeIngredientes();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void atualizaListaDeIngredientes() {
        ingredienteAdapter = new IngredienteAdapter(this, stringArrayList);
        ingredientesLV.setAdapter(ingredienteAdapter);
    }

    public void alterarPratoOnClick(View view) {
        Prato prato;

        int id = alterarPrato.getIntExtra("id", 0);
        String nome = nomeET.getText().toString();
        double preco = 0;
        int peso = 0;

        try {
            preco = Double.parseDouble(precoET.getText().toString());
            peso = Integer.parseInt(pesoET.getText().toString());

            prato = new Prato(id, nome, preco, stringArrayList, peso);

            if (pratoDAO.setPrato(prato)) {
                Toast.makeText(this, "Prato alterado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else
                Toast.makeText(this,"Erro! Não foi possível alterar o prato.", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this,"Não deixe campos vazios.", Toast.LENGTH_LONG).show();
        }


    }

    public void cancelarOnClick(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alterar_prato, menu);
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
