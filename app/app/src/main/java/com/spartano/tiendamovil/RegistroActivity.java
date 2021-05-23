package com.spartano.tiendamovil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spartano.tiendamovil.model.Usuario;

import java.time.LocalDate;

public class RegistroActivity extends AppCompatActivity {
    private Button btConfirmar;
    private EditText etNombre, etApellido, etTelefono, etDni, etMailRegistro, etClaveRegistro, etConfirmacionClaveRegistro;
    private RegistroActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        viewModel = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getApplication())
                .create(RegistroActivityViewModel.class);

        viewModel.getErrorValidacionMutable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                btConfirmar.setEnabled(true);
                btConfirmar.setText("Crear cuenta");
            }
        });

        viewModel.getRegistroExitosoMutable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Usuario creado correctamente", Toast.LENGTH_LONG).show();
            }
        });
        inicializarVista();
    }

    private void inicializarVista(){
        btConfirmar = findViewById(R.id.btConfirmar);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etTelefono = findViewById(R.id.etTelefono);
        etDni = findViewById(R.id.etDni);
        etMailRegistro = findViewById(R.id.etMailRegistro);
        etClaveRegistro = findViewById(R.id.etClaveRegistro);
        etConfirmacionClaveRegistro = findViewById(R.id.etConfirmacionClaveRegistro);

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btConfirmar.setEnabled(false);
                btConfirmar.setText("Cargando...");
                Usuario u = new Usuario();
                u.setNombre(etNombre.getText().toString());
                u.setApellido(etApellido.getText().toString());
                u.setTelefono(etTelefono.getText().toString());
                u.setDni(etDni.getText().toString());
                u.setEmail(etMailRegistro.getText().toString());
                u.setClave(etClaveRegistro.getText().toString());

                viewModel.validarRegistro(u, etConfirmacionClaveRegistro.getText().toString());
            }
        });
    }
}