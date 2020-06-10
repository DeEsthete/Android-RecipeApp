package com.evgeniy.recipesapp.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.adapter.RecipesMiniAdapter;
import com.evgeniy.recipesapp.data.LocalDatabase;
import com.evgeniy.recipesapp.delegates.ShowRecipeDelegate;
import com.evgeniy.recipesapp.dto.RecipeMini;
import com.evgeniy.recipesapp.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public class FillMyLibraryTask extends AsyncTask<Object, Void, List<RecipeMini>> {
    private LocalDatabase db;
    private RecyclerView recyclerView;
    private View root;
    private Context context;
    private ShowRecipeDelegate showRecipeDelegate;

    public FillMyLibraryTask(LocalDatabase db, RecyclerView recyclerView, View root, Context context, ShowRecipeDelegate showRecipeDelegate) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.root = root;
        this.context = context;
        this.showRecipeDelegate = showRecipeDelegate;
    }

    @Override
    protected List<RecipeMini> doInBackground(Object... objects) {
        List<RecipeMini> result = new ArrayList<>();
        List<RecipeEntity> recipeEntities = db.recipeDao().getAll();
        for (RecipeEntity entity : recipeEntities) {
            result.add(entity.mapToRecipeMini());
        }
        return result;
    }

    protected void onPostExecute(List<RecipeMini> result) {
        RecipesMiniAdapter adapter = new RecipesMiniAdapter(root.getContext(), result, showRecipeDelegate);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }


}
