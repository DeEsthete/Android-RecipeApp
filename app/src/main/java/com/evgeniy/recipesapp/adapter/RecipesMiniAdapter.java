package com.evgeniy.recipesapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.delegates.ShowRecipeDelegate;
import com.evgeniy.recipesapp.dto.RecipeMini;
import com.evgeniy.recipesapp.helper.DownloadImageTask;

import java.net.URL;
import java.util.List;

public class RecipesMiniAdapter extends RecyclerView.Adapter<RecipesMiniAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<RecipeMini> recipes;
    private ShowRecipeDelegate showRecipeDelegate;

    public RecipesMiniAdapter(Context context, List<RecipeMini> recipes, ShowRecipeDelegate showRecipeDelegate) {
        this.recipes = recipes;
        this.inflater = LayoutInflater.from(context);
        this.showRecipeDelegate = showRecipeDelegate;
    }

    @Override
    public RecipesMiniAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesMiniAdapter.ViewHolder holder, int position) {
        final RecipeMini[] recipe = {recipes.get(position)};
        new DownloadImageTask(holder.imageView).execute(!recipe[0].getImage().contains("https") ? "https://spoonacular.com/recipeImages/" + recipe[0].getImage() : recipe[0].getImage());

        holder.titleView.setText(recipe[0].getTitle());
        holder.remainingInMinutes.setText("Ready in minutes: " + recipe[0].getReadyInMinutes());

        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecipeDelegate.showRecipe(recipe[0].getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView titleView, remainingInMinutes;
        final Button showMore;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            titleView = view.findViewById(R.id.title);
            remainingInMinutes = view.findViewById(R.id.readyInMinutes);
            showMore = view.findViewById(R.id.showMore);
        }
    }
}
