package com.evgeniy.recipesapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.adapter.RecipesMiniAdapter;
import com.evgeniy.recipesapp.data.SpoonalcularApi;
import com.evgeniy.recipesapp.delegates.ShowRecipeDelegate;
import com.evgeniy.recipesapp.dto.Recipe;
import com.evgeniy.recipesapp.dto.RecipeMini;
import com.evgeniy.recipesapp.dto.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private View root;
    private SpoonalcularApi spoonalcularApi;
    private List<RecipeMini> recipes = new ArrayList<>();

    private EditText searchText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        spoonalcularApi = retrofit.create(SpoonalcularApi.class);

        searchText = root.findViewById(R.id.searchText);
        Button searchButton = root.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        return root;
    }

    private void search() {
        final Call<SearchResult> recipeQuery = spoonalcularApi.searchRecipes(searchText.getText().toString(), 0, 15);
        recipeQuery.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                assert response.body() != null;
                SearchResult result = response.body();
                recipes.clear();
                recipes.addAll(result.getResults());
                RecyclerView recyclerView = root.findViewById(R.id.list);
                RecipesMiniAdapter adapter = new RecipesMiniAdapter(root.getContext(), recipes, showMoreClick());
                recyclerView.setLayoutManager( new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private ShowRecipeDelegate showMoreClick() {
        return new ShowRecipeDelegate() {
            @Override
            public void showRecipe(int recipeId) {
                final Call<Recipe> recipeQuery = spoonalcularApi.getRecipeInformation(recipeId);
                recipeQuery.enqueue(new Callback<Recipe>() {
                    @Override
                    public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                        assert response.body() != null;
                        final Recipe recipeInformation = response.body();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment fraggy = RecipeInformationFragment.newInstance(recipeInformation);
                        fragmentTransaction.add(R.id.nav_host_fragment, fraggy);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<Recipe> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        };
    }
}
