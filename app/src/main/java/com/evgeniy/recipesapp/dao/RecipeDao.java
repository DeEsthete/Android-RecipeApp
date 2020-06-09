package com.evgeniy.recipesapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.evgeniy.recipesapp.entity.RecipeEntity;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipeentity")
    List<RecipeEntity> getAll();

    @Query("SELECT * FROM recipeentity WHERE id IN (:recipeIds)")
    List<RecipeEntity> loadAllByIds(int[] recipeIds);

    @Insert
    void insertAll(RecipeEntity... recipes);

    @Delete
    void delete(RecipeEntity recipe);
}
