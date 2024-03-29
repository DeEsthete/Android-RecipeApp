package com.evgeniy.recipesapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.evgeniy.recipesapp.dto.Recipe;
import com.evgeniy.recipesapp.dto.RecipeMini;

import java.util.List;

@Entity
public class RecipeEntity {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "ready_in_minutes")
    private int readyInMinutes;
    @ColumnInfo(name = "servings")
    private int servings;
    @ColumnInfo(name = "title")
    private String title;

    public RecipeEntity() {
    }

    public RecipeEntity(Recipe recipe) {
        setId((int) recipe.getId());
        setImage(recipe.getImage());
        setReadyInMinutes((int) recipe.getReadyInMinutes());
        setServings((int) recipe.getServings());
        setTitle(recipe.getTitle());
    }

    public RecipeMini mapToRecipeMini() {
        RecipeMini result = new RecipeMini();
        result.setId(getId());
        result.setImage(getImage());
        result.setReadyInMinutes(getReadyInMinutes());
        result.setServings(getServings());
        result.setTitle(getTitle());
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
