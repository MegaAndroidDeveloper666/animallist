package ru.markstudio.catdog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.markstudio.catdog.data.Animal;
import ru.markstudio.catdog.data.QueryResponse;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Animal> data;

    public Adapter(ArrayList<Animal> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<Animal> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnimal;
        TextView tvText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAnimal = itemView.findViewById(R.id.ivAnimal);
            tvText = itemView.findViewById(R.id.tvText);
        }


        public void bind(int position) {
            Animal animal = data.get(position);
            tvText.setText(animal.getTitle());
            Picasso.get()
                    .load(animal.getPictureUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(ivAnimal, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("PICASSO", "success");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("PICASSO", "error" + e.getLocalizedMessage());
                        }
                    });
        }
    }
}
