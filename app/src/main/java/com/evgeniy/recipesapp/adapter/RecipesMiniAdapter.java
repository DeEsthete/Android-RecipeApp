package com.evgeniy.recipesapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.dto.RecipeMini;
import com.google.android.material.button.MaterialButton;

import java.net.URL;
import java.util.List;

public class RecipesMiniAdapter extends RecyclerView.Adapter<RecipesMiniAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<RecipeMini> recipes;
    private View.OnClickListener showMoreListener;

    public RecipesMiniAdapter(Context context, List<RecipeMini> recipes, View.OnClickListener showMoreListener) {
        this.recipes = recipes;
        this.inflater = LayoutInflater.from(context);
        this.showMoreListener = showMoreListener;
    }

    @Override
    public RecipesMiniAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesMiniAdapter.ViewHolder holder, int position) {
        final RecipeMini[] recipe = {recipes.get(position)};

        URL url;
        Bitmap bmp = null;
        try {
            url = new URL("https://spoonacular.com/recipeImages/" + recipe[0].getImage());
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(bmp);

        holder.recipeId.setText(recipe[0].getId());
        holder.titleView.setText(recipe[0].getTitle());
        holder.remainingInMinutes.setText("Ready in minutes: " + recipe[0].getReadyInMinutes());
        holder.showMore.setOnClickListener(showMoreListener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView titleView, remainingInMinutes, recipeId;
        final MaterialButton showMore;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            recipeId = view.findViewById(R.id.recipeId);
            titleView = view.findViewById(R.id.title);
            remainingInMinutes = view.findViewById(R.id.readyInMinutes);
            showMore = view.findViewById(R.id.showMore);
        }
    }
}
