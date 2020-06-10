package com.evgeniy.recipesapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.adapter.RecipesMiniAdapter;
import com.evgeniy.recipesapp.data.LocalDatabase;
import com.evgeniy.recipesapp.data.SpoonalcularApi;
import com.evgeniy.recipesapp.delegates.ShowRecipeDelegate;
import com.evgeniy.recipesapp.dto.Recipe;
import com.evgeniy.recipesapp.dto.RecipeMini;
import com.evgeniy.recipesapp.entity.RecipeEntity;
import com.evgeniy.recipesapp.utils.GlobalConstants;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyLibraryFragment extends Fragment {

    private SpoonalcularApi spoonalcularApi;
    private LocalDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_library, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        spoonalcularApi = retrofit.create(SpoonalcularApi.class);

        db = Room.databaseBuilder(this.requireContext(),
                LocalDatabase.class, GlobalConstants.LOCAL_DB_NAME).build();

        final List<RecipeMini> recipes = new ArrayList<>();

        db.recipeDao().getAll().observeOn(Schedulers.computation()).subscribe(new Subscriber<List<RecipeEntity>>() {
            @Override
            public void onNext(List<RecipeEntity> recipeEntities) {
                for (RecipeEntity recipe : recipeEntities) {
                    recipes.add(recipe.mapToRecipeMini());
                }
            }

            @Override
            public void onSubscribe(Subscription s) {}
            @Override
            public void onError(Throwable t) {}
            @Override
            public void onComplete() {}
        });

        RecyclerView recyclerView = root.findViewById(R.id.list);
        RecipesMiniAdapter adapter = new RecipesMiniAdapter(root.getContext(), recipes, showMoreClick());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return root;
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
