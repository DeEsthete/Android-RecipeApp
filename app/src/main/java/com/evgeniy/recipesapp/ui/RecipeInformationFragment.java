package com.evgeniy.recipesapp.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.dto.Recipe;

public class RecipeInformationFragment extends Fragment {
    private static final String ARG_RECIPE = "recipe";

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe_information, container, false);

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
