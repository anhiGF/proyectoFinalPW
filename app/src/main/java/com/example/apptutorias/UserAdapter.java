package com.example.apptutorias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private UserActionListener actionListener;

    public UserAdapter(Context context, List<User> userList, UserActionListener actionListener) {
        this.context = context;
        this.userList = userList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Mostrar campos en las vistas correspondientes
        holder.numControlTextView.setText(String.valueOf(user.getNumControl()));
        holder.nameTextView.setText(user.getNombre() + " " + user.getPrimerApellido());
        holder.emailTextView.setText(user.getEmail());
        holder.semesterTextView.setText(user.getSemestre() > 0 ? "Semestre: " + user.getSemestre() : "Semestre: N/A");
        holder.roleTextView.setText(user.getTipoUsuario());

        // Configurar acciones de botones
        holder.editButton.setOnClickListener(v -> actionListener.onEditUser(user));
        holder.deleteButton.setOnClickListener(v -> actionListener.onDeleteUser(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView numControlTextView, nameTextView, emailTextView, semesterTextView, roleTextView;
        Button editButton, deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            // Referenciar las vistas desde el diseño
            numControlTextView = itemView.findViewById(R.id.numControlTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            semesterTextView = itemView.findViewById(R.id.semesterTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface UserActionListener {
        void onEditUser(User user);
        void onDeleteUser(User user);
    }
}
