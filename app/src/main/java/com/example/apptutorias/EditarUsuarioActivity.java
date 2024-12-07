package com.example.apptutorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class EditarUsuarioActivity extends AppCompatActivity {

    private static final String API_UPDATE_USER = "http://localhost/Scripts/Frontend/update_user.php";

    private EditText numControlEditText, nombreEditText, primerApellidoEditText, segundoApellidoEditText;
    private EditText emailEditText, semestreEditText, fechaNacimientoEditText;
    private Spinner tipoUsuarioSpinner;
    private int numControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        // Inicializar vistas
        numControlEditText = findViewById(R.id.numControlEditText);
        nombreEditText = findViewById(R.id.nombreEditText);
        primerApellidoEditText = findViewById(R.id.primerApellidoEditText);
        segundoApellidoEditText = findViewById(R.id.segundoApellidoEditText);
        emailEditText = findViewById(R.id.emailEditText);
        semestreEditText = findViewById(R.id.semestreEditText);
        fechaNacimientoEditText = findViewById(R.id.fechaNacimientoEditText);
        tipoUsuarioSpinner = findViewById(R.id.tipoUsuarioSpinner);
        Button saveButton = findViewById(R.id.saveButton);

        // Configurar Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoUsuarioSpinner.setAdapter(adapter);

        // Obtener datos del usuario desde el Intent
        Intent intent = getIntent();
        numControl = intent.getIntExtra("num_control", -1);
        String nombre = intent.getStringExtra("nombre");
        String primerApellido = intent.getStringExtra("primer_apellido");
        String segundoApellido = intent.getStringExtra("segundo_apellido");
        String email = intent.getStringExtra("correo_electronico");
        int semestre = intent.getIntExtra("semestre", 0);
        String fechaNacimiento = intent.getStringExtra("fecha_nac");
        String tipoUsuario = intent.getStringExtra("tipo_usuario");

        // Mostrar los datos en los campos de texto
        numControlEditText.setText(String.valueOf(numControl));
        nombreEditText.setText(nombre);
        primerApellidoEditText.setText(primerApellido);
        segundoApellidoEditText.setText(segundoApellido != null ? segundoApellido : "");
        emailEditText.setText(email);
        semestreEditText.setText(semestre > 0 ? String.valueOf(semestre) : "");
        fechaNacimientoEditText.setText(fechaNacimiento != null ? fechaNacimiento : "");

        // Seleccionar tipo de usuario en el Spinner
        if (tipoUsuario != null) {
            int spinnerPosition = adapter.getPosition(tipoUsuario);
            tipoUsuarioSpinner.setSelection(spinnerPosition);
        }

        // Evitar que el usuario edite el número de control
        numControlEditText.setEnabled(false);

        // Guardar cambios
        saveButton.setOnClickListener(v -> updateUser());
    }

    private void updateUser() {
        String nombre = nombreEditText.getText().toString().trim();
        String primerApellido = primerApellidoEditText.getText().toString().trim();
        String segundoApellido = segundoApellidoEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String semestreText = semestreEditText.getText().toString().trim();
        String fechaNacimiento = fechaNacimientoEditText.getText().toString().trim();
        String tipoUsuario = tipoUsuarioSpinner.getSelectedItem().toString();

        if (nombre.isEmpty() || primerApellido.isEmpty() || email.isEmpty() || tipoUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int semestre = semestreText.isEmpty() ? 0 : Integer.parseInt(semestreText);

        // Preparar datos para la solicitud
        JSONObject postData = new JSONObject();
        try {
            postData.put("num_control", numControl);
            postData.put("nombre", nombre);
            postData.put("primer_apellido", primerApellido);
            postData.put("segundo_apellido", segundoApellido);
            postData.put("correo_electronico", email);
            postData.put("semestre", semestre);
            postData.put("fecha_nac", fechaNacimiento);
            postData.put("tipo_usuario", tipoUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviar solicitud a la API
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_UPDATE_USER, postData,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                            finish(); // Volver a la pantalla anterior
                        } else {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        queue.add(request);
    }
}
