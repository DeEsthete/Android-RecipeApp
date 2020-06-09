package com.evgeniy.recipesapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.evgeniy.recipesapp.R;
import com.evgeniy.recipesapp.data.LocalDatabase;

public class MyLibraryFragment extends Fragment {

    private LocalDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_library, container, false);
        db = Room.databaseBuilder(this.requireContext(),
                LocalDatabase.class, "local_recipes_db").build();

        return root;
    }
}
