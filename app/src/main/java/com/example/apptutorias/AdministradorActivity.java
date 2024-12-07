package com.example.apptutorias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdministradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        // Referencias a los elementos de la interfaz
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        Button gestionUsuariosButton = findViewById(R.id.gestionUsuariosButton);
        Button monitorizacionSistemaButton = findViewById(R.id.monitorizacionSistemaButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Recuperar datos del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre", "Usuario");
        String primerApellido = sharedPreferences.getString("primer_apellido", "");

        // Mostrar el nombre del usuario
        userNameTextView.setText("Administrador: " + nombre + " " + primerApellido);

        // Ir a Gestión de Usuarios
        gestionUsuariosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministradorActivity.this, GestionUsuariosActivity.class);
                startActivity(intent);
            }
        });

        // Ir a Monitorización del Sistema
        monitorizacionSistemaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministradorActivity.this, MonitorizacionSistemaActivity.class);
                startActivity(intent);
            }
        });

        // Cerrar sesión
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    // Cierra la sesión del usuario
    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Elimina todos los datos de la sesión
        editor.apply();

        // Redirige al LoginActivity
        Intent intent = new Intent(AdministradorActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }
}
