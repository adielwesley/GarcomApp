package br.edu.ufam.garcomapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class NovoPratoActivity2 extends ActionBarActivity {
    ArrayList<String> ingredientesArrayList = new ArrayList<String>();
    ListView ingredientesListView;
    private IngredienteAdapter ingredienteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_prato_activity2);

        ingredienteAdapter = new IngredienteAdapter(this, ingredientesArrayList);

        ingredientesListView = (ListView) findViewById(R.id.novoprato2_listView_ingredientesadicionados);
        ingredientesListView.setAdapter(ingredienteAdapter);
        ingredientesListView.setOnItemClickListener(excluiIngrediente(this));
    }

    private AdapterView.OnItemClickListener excluiIngrediente(final Context context) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Excluir Ingrediente?");
                builder.setMessage("Você deseja excluir esse ingrediente?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ingredientesArrayList.remove(position);
                        atualizaListView();
                    }
                });
                builder.setNegativeButton("Não", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };
    }

    private void atualizaListView() {
        ingredienteAdapter = new IngredienteAdapter(this, ingredientesArrayList);
        ingredientesListView.setAdapter(ingredienteAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_novo_prato_activity2, menu);
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

    public void addOnClick(View view) {
        EditText ingrediente = (EditText) findViewById(R.id.novoprato2_editText_ingrediente);
        String ingredienteString = ingrediente.getText().toString();

        if (ingredienteString.compareTo("") != 0) {
            ingredientesArrayList.add(ingredienteString);
            ingrediente.setText("");
            atualizaListView();
        } else {
            Toast.makeText(this,"Digite um ingrediente",Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrarOnClick(View view) {
        Prato prato;
        PratoDAO pratoDAO = new PratoDAO(this);

        Intent novoPrato2 = getIntent();
        String nome = novoPrato2.getStringExtra("nome");
        double preco = Double.parseDouble(novoPrato2.getStringExtra("preco"));
        int peso = Integer.parseInt(novoPrato2.getStringExtra("peso"));
        int id;

        do {
            id = (int) (Math.random()*100000);
        } while(verificaExistenciaDeID(id, pratoDAO.retornaPratos()));

        prato = new Prato(id,nome, preco, ingredientesArrayList,peso);
        if (pratoDAO.addPrato(prato)) {
            Toast.makeText(this, "Prato cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Erro! O prato não foi cadastrado.", Toast.LENGTH_SHORT).show();

        finish();
    }

    private boolean verificaExistenciaDeID(int id, ArrayList<Prato> lista) {
        for (Prato p : lista)
            if(p.getId() == id)
                return true;
        return false;
    }

    public void cancelarOnClick(View view) {
        finish();
    }
}
