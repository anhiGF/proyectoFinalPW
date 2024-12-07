package com.example.apptutorias;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class AgregarUsuarioActivity extends AppCompatActivity {

    private static final String API_ADD_USER = "http://10.0.2.2/add_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        // Referencias a los campos de entrada
        EditText numControlEditText = findViewById(R.id.numControlEditText);
        EditText nombreEditText = findViewById(R.id.nombreEditText);
        EditText primerApellidoEditText = findViewById(R.id.primerApellidoEditText);
        EditText segundoApellidoEditText = findViewById(R.id.segundoApellidoEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText fechaNacimientoEditText = findViewById(R.id.fechaNacimientoEditText);
        EditText semestreEditText = findViewById(R.id.semestreEditText);
        Button saveButton = findViewById(R.id.saveUserButton);

        Spinner tipoUsuarioSpinner = findViewById(R.id.tipoUsuarioSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoUsuarioSpinner.setAdapter(adapter);

        // Configuración del botón Guardar
        saveButton.setOnClickListener(v -> {
            String numControl = numControlEditText.getText().toString();
            String nombre = nombreEditText.getText().toString();
            String primerApellido = primerApellidoEditText.getText().toString();
            String segundoApellido = segundoApellidoEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String fechaNacimiento = fechaNacimientoEditText.getText().toString();
            String semestreText = semestreEditText.getText().toString();
            String tipoUsuario = tipoUsuarioSpinner.getSelectedItem().toString();

            if (numControl.isEmpty() || nombre.isEmpty() || primerApellido.isEmpty() || email.isEmpty() || tipoUsuario.isEmpty()) {
                Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int semestre = semestreText.isEmpty() ? 0 : Integer.parseInt(semestreText);

            // Enviar datos a la API
            addUserToApi(numControl, nombre, primerApellido, segundoApellido, email, fechaNacimiento, semestre, tipoUsuario);
        });
    }

    private void addUserToApi(String numControl, String nombre, String primerApellido, String segundoApellido,
                              String email, String fechaNacimiento, int semestre, String tipoUsuario) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("num_control", numControl);
            postData.put("nombre", nombre);
            postData.put("primer_apellido", primerApellido);
            postData.put("segundo_apellido", segundoApellido);
            postData.put("correo_electronico", email);
            postData.put("fecha_nac", fechaNacimiento);
            postData.put("semestre", semestre);
            postData.put("tipo_usuario", tipoUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_ADD_USER, postData,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();
                            finish(); // Regresar a la pantalla anterior
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
