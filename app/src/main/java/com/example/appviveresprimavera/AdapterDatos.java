package com.example.appviveresprimavera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>{
    ItemListenner itemListenner;
    public AdapterDatos(ArrayList<Producto> listDatos, ItemListenner itemListenner) {
        this.listDatos = listDatos;
        this.itemListenner=itemListenner;
    }

    ArrayList<Producto>listDatos;



    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_list,null,false);
        return new ViewHolderDatos(view,itemListenner);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.txtMostrarNombre.setText(listDatos.get(position).getNombre().toString());
        holder.txtMostrarPrecio.setText(listDatos.get(position).getPrecio().toString());

    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtMostrarNombre,txtMostrarPrecio;
        public ViewHolderDatos(View itemView,ItemListenner itemListenner) {
            super(itemView);
            txtMostrarNombre=itemView.findViewById(R.id.txtMostrarNombre);
            txtMostrarPrecio=itemView.findViewById(R.id.txtMostrarPrecio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListenner.onClick(getAdapterPosition());
                }
            });
        }

    }
}
