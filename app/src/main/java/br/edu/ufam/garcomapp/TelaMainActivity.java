package br.edu.ufam.garcomapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class TelaMainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_main);
    }

    public void cardapioOnClick(View view) {
        Intent intent = new Intent(this, CardapioActivity.class);
        startActivity(intent);
    }

    public void novoPedidoOnClick(View view) {
        PratoDAO pratoBD = new PratoDAO(this);
        if (!pratoBD.retornaPratos().isEmpty()) {
            Intent intent = new Intent(this, NovoPedidoActivity.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nenhum prato no Cardápio");
            builder.setMessage("Não há nenhum prato cadastrado no Cardápio. Cadastre algum para que seja possível cadastrar um novo pedido.");
            builder.setNegativeButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void pedidosFeitosOnClick(View view) {
        Intent intent = new Intent(this, PedidosFeitosActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_main, menu);
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
