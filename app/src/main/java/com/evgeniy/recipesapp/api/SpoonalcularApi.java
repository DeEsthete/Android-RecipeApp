package com.evgeniy.recipesapp.api;

import com.evgeniy.recipesapp.pojo.RandomResult;
import com.evgeniy.recipesapp.pojo.Recipe;
import com.evgeniy.recipesapp.pojo.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonalcularApi {
    final String API_KEY = "83190b0157044459a26eb7e4c09f2d72";

    @GET("recipes/random?apiKey=" + API_KEY)
    Call<RandomResult> randomRecipes(@Query("number") int number);

    @GET("recipes/{recipeId}/information?apiKey=" + API_KEY)
    Call<Recipe> getRecipeInformation(@Path("recipeId") int recipeId);

    @GET("recipes/search?apiKey=" + API_KEY)
    Call<SearchResult> searchRecipes(@Query("query") String query, @Query("offset") int offset, @Query("number") int number);
}
