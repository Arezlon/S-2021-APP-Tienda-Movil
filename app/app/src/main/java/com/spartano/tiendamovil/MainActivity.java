package com.spartano.tiendamovil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    private EditText etMail, etClave;
    private Button btIngresar, btRegistrarse;
    private TextView loginTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getApplication())
                .create(MainActivityViewModel.class);
        inicializarVista();

        viewModel.getMensajeMutable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                btIngresar.setEnabled(true);
                btRegistrarse.setEnabled(true);
                btIngresar.setText("Ingresar");
            }
        });

        viewModel.getResultadoMutable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ok) {
                if (ok){
                    startActivity(new Intent(getApplicationContext(), MenuNavegacionActivity.class));
                    Toast.makeText(getApplicationContext(), "Sesión iniciada correctamente", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void inicializarVista() {
        etMail = findViewById(R.id.etMail);
        etClave = findViewById(R.id.etClave);
        btIngresar = findViewById(R.id.btIngresar);
        btRegistrarse = findViewById(R.id.btRegistrarse);

        //Pruebas para iniciar sesión sin usuario/conexión
        loginTest = findViewById(R.id.test);
        loginTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "login de prueba sin usuario", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MenuNavegacionActivity.class));
            }
        });

        btIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btIngresar.setEnabled(false);
                btRegistrarse.setEnabled(false);
                btIngresar.setText("Cargando...");
                viewModel.verificarDatos(
                        etMail.getText().toString(),
                        etClave.getText().toString()
                );
            }
        });

        btRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
            }
        });
    }
}