package com.example.apptutorias;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String API_URL = "http://localhost/Scripts/Frontend/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText numControlInput = findViewById(R.id.registerNumControl);
        EditText nombreInput = findViewById(R.id.registerNombre);
        EditText primerApellidoInput = findViewById(R.id.registerPrimerApellido);
        EditText segundoApellidoInput = findViewById(R.id.registerSegundoApellido);
        EditText correoInput = findViewById(R.id.registerCorreo);
        EditText passwordInput = findViewById(R.id.registerPassword);
        EditText semestreInput = findViewById(R.id.registerSemestre);
        EditText fechaNacInput = findViewById(R.id.registerFechaNac);
        Spinner tipoUsuarioSpinner = findViewById(R.id.registerTipoUsuario);
        Button registerButton = findViewById(R.id.registerSubmitButton);

        // Configurar el spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_usuario_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoUsuarioSpinner.setAdapter(adapter);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numControl = numControlInput.getText().toString().trim();
                String nombre = nombreInput.getText().toString().trim();
                String primerApellido = primerApellidoInput.getText().toString().trim();
                String segundoApellido = segundoApellidoInput.getText().toString().trim();
                String correo = correoInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String semestre = semestreInput.getText().toString().trim();
                String fechaNac = fechaNacInput.getText().toString().trim();
                String tipoUsuario = tipoUsuarioSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(numControl) || TextUtils.isEmpty(nombre) ||
                        TextUtils.isEmpty(primerApellido) || TextUtils.isEmpty(correo) ||
                        TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Por favor, llene los campos obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Enviar datos a la API
                registerUser(numControl, nombre, primerApellido, segundoApellido, correo, password, semestre, fechaNac, tipoUsuario);
            }
        });
    }

    private void registerUser(String numControl, String nombre, String primerApellido, String segundoApellido,
                              String correo, String password, String semestre, String fechaNac, String tipoUsuario) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (success) {
                                finish(); // Regresa a la pantalla anterior
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("num_control", numControl);
                params.put("nombre", nombre);
                params.put("primer_apellido", primerApellido);
                params.put("segundo_apellido", segundoApellido);
                params.put("correo_electronico", correo);
                params.put("contraseña", password);
                params.put("semestre", semestre);
                params.put("fecha_nac", fechaNac);
                params.put("tipo_usuario", tipoUsuario);
                return params;
            }
        };

        queue.add(request);
    }
}
