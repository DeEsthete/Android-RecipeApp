package com.evgeniy.recipesapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.evgeniy.recipesapp.dao.RecipeDao;
import com.evgeniy.recipesapp.entity.RecipeEntity;

@Database(entities = {RecipeEntity.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}
