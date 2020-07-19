package com.example.polichat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRVMensaje extends RecyclerView.Adapter<AdapterRVMensaje.MensajeHolder> {
    private List<MensajeVO> lstMensajes;

    public AdapterRVMensaje(List<MensajeVO> lstMensajes) {
        this.lstMensajes = lstMensajes;
    }

    @NonNull
    @Override
    public MensajeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensje, parent, false);
        return new MensajeHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeHolder holder, int position) {
        holder.tvName.setText(lstMensajes.get(position).getName());
        holder.tvMensaje.setText(lstMensajes.get(position).getMensaje());
    }

    @Override
    public int getItemCount() {
        return lstMensajes.size();
    }

    class MensajeHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvMensaje;
        public MensajeHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txtName);
            tvMensaje = itemView.findViewById(R.id.txtMensaje);
        }
    }
}
