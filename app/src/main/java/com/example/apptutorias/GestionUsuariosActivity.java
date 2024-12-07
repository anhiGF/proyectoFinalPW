package com.example.apptutorias;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GestionUsuariosActivity extends AppCompatActivity implements UserAdapter.UserActionListener {

    private static final String API_GET_USERS = "http://localhost/Scripts/Frontend/get_users.php";
    private static final String API_DELETE_USER = "http://localhost/Scripts/Frontend/delete_user.php";

    private List<User> userList = new ArrayList<>();
    private List<User> filteredUserList = new ArrayList<>();
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios);

        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        userAdapter = new UserAdapter(this, filteredUserList, this);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(userAdapter);

        loadUsersFromApi();
    }

    private void loadUsersFromApi() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_GET_USERS, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            userList.clear();
                            filteredUserList.clear();

                            for (int i = 0; i < response.getJSONArray("users").length(); i++) {
                                JSONObject userObject = response.getJSONArray("users").getJSONObject(i);

                                int numControl = userObject.getInt("num_control");
                                String nombre = userObject.getString("nombre");
                                String primerApellido = userObject.getString("primer_apellido");
                                String segundoApellido = userObject.optString("segundo_apellido", null);
                                String email = userObject.getString("correo_electronico");
                                int semestre = userObject.optInt("semestre", 0);
                                String fechaNacimiento = userObject.optString("fecha_nac", null);
                                String tipoUsuario = userObject.getString("tipo_usuario");

                                userList.add(new User(numControl, nombre, primerApellido, segundoApellido,
                                        email, tipoUsuario, semestre, fechaNacimiento));
                            }

                            filteredUserList.addAll(userList);
                            userAdapter.notifyDataSetChanged();
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
                    Log.e("API_ERROR", error.getMessage());
                });

        queue.add(request);
    }

    private void deleteUserFromApi(int numControl) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("num_control", numControl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_DELETE_USER, postData,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            loadUsersFromApi(); // Refrescar la lista después de eliminar
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
                    Log.e("API_ERROR", error.getMessage());
                });

        queue.add(request);
    }

    @Override
    public void onEditUser(User user) {
        Intent intent = new Intent(this, EditarUsuarioActivity.class);
        intent.putExtra("num_control", user.getNumControl());
        intent.putExtra("nombre", user.getNombre());
        intent.putExtra("primer_apellido", user.getPrimerApellido());
        intent.putExtra("segundo_apellido", user.getSegundoApellido());
        intent.putExtra("correo_electronico", user.getEmail());
        intent.putExtra("semestre", user.getSemestre());
        intent.putExtra("fecha_nac", user.getFechaNacimiento());
        intent.putExtra("tipo_usuario", user.getTipoUsuario());
        startActivity(intent);
    }

    @Override
    public void onDeleteUser(User user) {
        showDeleteConfirmationDialog(user);
    }

    private void showDeleteConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar al usuario: "
                + user.getNombre() + " " + user.getPrimerApellido() + "?");

        builder.setPositiveButton("Eliminar", (dialog, which) -> deleteUserFromApi(user.getNumControl()));
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
