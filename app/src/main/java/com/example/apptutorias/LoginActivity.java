package com.example.apptutorias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

public class LoginActivity extends AppCompatActivity {

    private static final String API_URL = " http://localhost/Scripts/Frontend/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.loginEmail);
        EditText passwordInput = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.loginSubmitButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                JSONObject user = jsonResponse.getJSONObject("user");

                                // Guardar datos en SharedPreferences
                                saveUserSession(user);

                                // Navegar según el rol
                                navigateByRole(user.getString("tipo_usuario"));
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo_electronico", email);
                params.put("contraseña", password);
                return params;
            }
        };

        queue.add(request);
    }

    private void saveUserSession(JSONObject user) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putBoolean("isLoggedIn", true);
            editor.putString("num_control", user.getString("num_control"));
            editor.putString("nombre", user.getString("nombre"));
            editor.putString("primer_apellido", user.getString("primer_apellido"));
            editor.putString("tipo_usuario", user.getString("tipo_usuario"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void navigateByRole(String role) {
        Intent intent;

        switch (role) {
            case "Estudiante":
               intent = new Intent(LoginActivity.this, EstudianteActivity.class);
                break;
            case "Tutor":
            intent = new Intent(LoginActivity.this, TutorActivity.class);
                break;
            case "Administrador":
               intent = new Intent(LoginActivity.this, AdministradorActivity.class);
                break;
            default:
                Toast.makeText(this, "Rol desconocido: " + role, Toast.LENGTH_SHORT).show();
                return;
        }

       startActivity(intent);
        finish();
    }
}
