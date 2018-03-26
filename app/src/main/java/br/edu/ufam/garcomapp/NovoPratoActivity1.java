package br.edu.ufam.garcomapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NovoPratoActivity1 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_prato_activity1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teste, menu);
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

    public void prosseguirOnClick(View view) {
        Intent novoPrato2 = new Intent(this, NovoPratoActivity2.class);

        EditText nome = (EditText) findViewById(R.id.novoprato1_editText_nome);
        EditText preco = (EditText) findViewById(R.id.novoprato1_editText_preco);
        EditText peso = (EditText) findViewById(R.id.novoprato1_editText_peso);

        try {
            double testeConversão = Double.parseDouble(preco.getText().toString());
            testeConversão = Integer.parseInt(peso.getText().toString());

            novoPrato2.putExtra("nome", nome.getText().toString());
            novoPrato2.putExtra("preco", preco.getText().toString());
            novoPrato2.putExtra("peso", peso.getText().toString());

            startActivity(novoPrato2);
            finish();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage().toString());
            Toast.makeText(this, "Não deixe campos vazios.", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelarOnClick(View view) {
        finish();
    }
}
