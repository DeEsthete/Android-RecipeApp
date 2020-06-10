package com.evgeniy.recipesapp.ui;

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.evgeniy.recipesapp.MainActivity;
import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.data.LocalDatabase;
import com.evgeniy.recipesapp.dto.Recipe;
import com.evgeniy.recipesapp.entity.RecipeEntity;
import com.evgeniy.recipesapp.helper.DownloadImageTask;
import com.evgeniy.recipesapp.utils.GlobalConstants;

import java.net.URL;

public class RecipeInformationFragment extends Fragment {
    private static final String ARG_RECIPE = "recipe";

    private LocalDatabase db;
    private Recipe recipe;

    public RecipeInformationFragment() {
    }

    public static RecipeInformationFragment newInstance(Recipe recipe) {
        RecipeInformationFragment fragment = new RecipeInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
            db = Room.databaseBuilder(this.requireContext(),
                    LocalDatabase.class, GlobalConstants.LOCAL_DB_NAME).build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe_information, container, false);

        root.findViewById(R.id.addToLibrary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int[] recipeId = {(int) recipe.getId()};
                        if (db.recipeDao().loadAllByIds(recipeId).isEmpty()) {
                            db.recipeDao().insertAll(new RecipeEntity(recipe));
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Recipe already exist")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            Looper.prepare();
                            builder.create().show();
                        }
                    }
                }).start();
            }
        });

        new DownloadImageTask((ImageView) root.findViewById(R.id.image))
                .execute("https://spoonacular.com/recipeImages/" + recipe.getImage());
        ((TextView) root.findViewById(R.id.title)).setText(recipe.getTitle());
        ((TextView) root.findViewById(R.id.readyInMinutes)).setText("Ready in minutes: " + recipe.getReadyInMinutes());
        ((TextView) root.findViewById(R.id.aggregateLikes)).setText("Likes: " + recipe.getAggregateLikes());
        ((TextView) root.findViewById(R.id.healthScore)).setText("Health score: " + recipe.getHealthScore());

        TextView summary = (TextView) root.findViewById(R.id.summary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            summary.setText(Html.fromHtml(recipe.getSummary(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            summary.setText(Html.fromHtml(recipe.getSummary()));
        }

        TextView instructions = (TextView) root.findViewById(R.id.instructions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            instructions.setText(Html.fromHtml(recipe.getInstructions(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            instructions.setText(Html.fromHtml(recipe.getInstructions()));
        }

        return root;
    }
}
