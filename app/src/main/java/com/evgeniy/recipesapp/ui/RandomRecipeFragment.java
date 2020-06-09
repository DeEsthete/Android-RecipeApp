package com.evgeniy.recipesapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.api.SpoonalcularApi;
import com.evgeniy.recipesapp.pojo.RandomResult;
import com.evgeniy.recipesapp.pojo.Recipe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomRecipeFragment extends Fragment {

    private Recipe recipe;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_random_recipe, container, false);
        getRecipe();
        return root;
    }

    private void getRecipe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpoonalcularApi spoonalcularApi = retrofit.create(SpoonalcularApi.class);
        final Call<RandomResult> recipes = spoonalcularApi.randomRecipes(1);
        recipes.enqueue(new Callback<RandomResult>() {
            @Override
            public void onResponse(Call<RandomResult> call, Response<RandomResult> response) {
                assert response.body() != null;
                recipe = response.body().getRecipes().get(0);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fraggy = RecipeInformationFragment.newInstance(recipe);
                fragmentTransaction.add(R.id.fragment, fraggy);
                fragmentTransaction.commit();
            }
            @Override
            public void onFailure(Call<RandomResult> call, Throwable t) {

            }
        });
    }
}
