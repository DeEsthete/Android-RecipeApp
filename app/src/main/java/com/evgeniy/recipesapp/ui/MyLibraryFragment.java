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

import com.evgeniy.recipesapp.MainActivity;
import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.adapter.RecipesMiniAdapter;
import com.evgeniy.recipesapp.data.LocalDatabase;
import com.evgeniy.recipesapp.data.SpoonalcularApi;
import com.evgeniy.recipesapp.delegates.ShowRecipeDelegate;
import com.evgeniy.recipesapp.dto.Recipe;
import com.evgeniy.recipesapp.dto.RecipeMini;
import com.evgeniy.recipesapp.entity.RecipeEntity;
import com.evgeniy.recipesapp.helper.FillMyLibraryTask;
import com.evgeniy.recipesapp.utils.GlobalConstants;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyLibraryFragment extends Fragment {

    private View root;
    private SpoonalcularApi spoonalcularApi;
    private LocalDatabase db;
    private List<RecipeMini> recipes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_library, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        spoonalcularApi = retrofit.create(SpoonalcularApi.class);

        db = Room.databaseBuilder(this.requireContext(),
                LocalDatabase.class, GlobalConstants.LOCAL_DB_NAME).build();
        new FillMyLibraryTask(db, (RecyclerView) root.findViewById(R.id.list), root, getContext(), showMoreClick()).execute();

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
