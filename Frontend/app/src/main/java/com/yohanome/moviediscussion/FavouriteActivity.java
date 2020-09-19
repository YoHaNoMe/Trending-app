package com.yohanome.moviediscussion;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.yohanome.moviediscussion.adapters.FavouriteAdapter;
import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.myfragments.MainFragment;
import com.yohanome.moviediscussion.viewmodels.MovieViewModel;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private List<Favourite> favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // Define Views
        final RecyclerView recyclerView = findViewById(R.id.favourite_recycler_view);

        // Define userViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Get extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            favourites = bundle.getParcelableArrayList(MainFragment.EXTRA_FAVOURITE_LIST);
            Log.d("MainFragment", String.valueOf(favourites.size()));

            movieViewModel.getMovieTvs(favourites);

            FavouriteAdapter adapter = new FavouriteAdapter(null);
            recyclerView.setAdapter(adapter);

            movieViewModel.getMovieTvsLiveData().observe(this, movies -> {
                if (movies != null)
                    adapter.setMovies(movies);
            });
        }
    }
}