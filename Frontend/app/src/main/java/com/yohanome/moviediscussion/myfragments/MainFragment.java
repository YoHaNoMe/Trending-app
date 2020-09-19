package com.yohanome.moviediscussion.myfragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yohanome.moviediscussion.FavouriteActivity;
import com.yohanome.moviediscussion.ProfileActivity;
import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.adapters.DefaultAdapter;
import com.yohanome.moviediscussion.adapters.MainAdapter;
import com.yohanome.moviediscussion.adapters.MovieAdapter;
import com.yohanome.moviediscussion.adapters.ResultItemClickListener;
import com.yohanome.moviediscussion.adapters.SeriesAdapter;
import com.yohanome.moviediscussion.adapters.TrendingAdapter;
import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.movieutils.MovieUtils;
import com.yohanome.moviediscussion.viewmodels.MovieViewModel;
import com.yohanome.moviediscussion.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainFragment extends Fragment implements ResultItemClickListener {

    public static final String EXTRA_RESULT_ITEM = "com.yohanome.moviediscussion.MainActivity.result_item";
    public static final String USER_PROFILE_IMAGE_EXTRA = "com.yohanome.moviediscussion.myfragments.BottomSheetFragment.UserProfileImage";
    public static final String EXTRA_FAVOURITE_LIST = "com.yohanome.moviediscussion.MainActivity.favourite_list";

    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private MovieViewModel movieViewModel;
    private UserViewModel userViewModel;
    private BottomAppBar bottomAppBar;
    private NavController navController;
    private ImageView favouriteImageView;
    private UUID userId;
    private Movie resultItem;
    private List<Favourite> favourites;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Define Views
        recyclerView = view.findViewById(R.id.main_recyclerView_container);
        bottomAppBar = view.findViewById(R.id.bottomAppBar);
        FloatingActionButton bottomAppBarFloatingActionButton = view.findViewById(R.id.bottom_app_bar_floating_action_button);

        // Initialize NavController
        navController = Navigation.findNavController(view);

        // Init RecyclerView
        initRecyclerView();

        // define Navigation onClickListener for bottomAppBar
        bottomAppBar.setNavigationOnClickListener(v -> {
            String userImageUrl = "https://cdn.thegentlemansjournal.com/wp-content/uploads/2014/11/stylish-actor-TGJ.00-900x600-c-center.jpg";
            Intent intent = new Intent(requireContext(), ProfileActivity.class);
            intent.putExtra(USER_PROFILE_IMAGE_EXTRA, userImageUrl);
            requireActivity().startActivity(intent);
//        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        // Menu Listener
        bottomAppBar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    //TODO
                    return true;
                case R.id.action_sign_out:
                    signOut();
                    return true;
            }
            return false;
        });

        // Floating Action Button Listener
        bottomAppBarFloatingActionButton.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), FavouriteActivity.class);
            intent.putParcelableArrayListExtra(EXTRA_FAVOURITE_LIST, (ArrayList<? extends Parcelable>) favourites);
            startActivity(intent);
        });


        // Get Movie | Series Trending
        movieViewModel.getTrending();

        // Get Movies
        movieViewModel.getMovies();

        // Get Series
        movieViewModel.getSeries();

        /*
         ********************* Define Observers **************************
         */
        // Get the user Id and save it
        userViewModel.getUserCachedLiveData().observe(getViewLifecycleOwner(), userCached -> {
            if (userCached != null) {
                userId = userCached.getId();
                userViewModel.getFavourites(userId.toString());
            }
        });

        userViewModel.getFavouritesLiveData().observe(getViewLifecycleOwner(), favourites -> this.favourites = favourites);

        // Result Observer
        movieViewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            // TODO: Sort the favourite data but in the server, then send it | to enhance performance(Using searching algorithms)
            if(favourites != null){
                for(Favourite favourite: favourites){
                    for(int i=0;i<result.getResults().size();++i){
                        Movie res = result.getResults().get(i);
                        if(res.getId() == favourite.getMovieTvId()){
                            res.setIsFavourite(1);
                        }
                    }
                }
            }
            mainAdapter.setResult(result);
            mainAdapter.setTrending(result);
        });

        // Movie Observer
        movieViewModel.getMoviesLiveData().observe(getViewLifecycleOwner(), result -> mainAdapter.setMovies(result));

        // Series Observer
        movieViewModel.getSeriesLiveData().observe(getViewLifecycleOwner(), result -> mainAdapter.setSeries(result));

        // User logged out observer
        userViewModel.getUserLoggedInLiveData().observe(getViewLifecycleOwner(), isLoggedIn->{
            if(isLoggedIn != null && !isLoggedIn)
                navController.navigate(MainFragmentDirections.actionMainFragmentToLoginFragment());
        });

        // Listen for Favourite
        userViewModel.getIsFavouriteLiveData().observe(getViewLifecycleOwner(), this::favouriteLogic);
        /*
         ********************* END Define Observers **************************
         */
    }

    private void favouriteLogic(Integer isFavourite) {
        Log.d("MainFragment", "Inside Like Movie");
        if (isFavourite == null || resultItem == null) {
            return;
        }

        Log.d("MainFragment", "Inside Like Movie300");
        resultItem.setIsFavourite(isFavourite);
        if (isFavourite == 1) {
            favouriteImageView.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            favouriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        TrendingAdapter trendingAdapter = new TrendingAdapter();
        MovieAdapter movieAdapter = new MovieAdapter();
        SeriesAdapter seriesAdapter = new SeriesAdapter();
        DefaultAdapter defaultAdapter = new DefaultAdapter(this);
        mainAdapter = new MainAdapter(getContext(), trendingAdapter, defaultAdapter, movieAdapter, seriesAdapter);
        recyclerView.setAdapter(mainAdapter);
    }

    private void signOut() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sign out")
                .setMessage("Are you sure you want to sign out")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    Log.d("MainFragment", "Yes button clicked1");
                    userViewModel.logoutUser();
                })
                .show();
    }

    @Override
    public void onResultItemClick(int pos, Movie resultItem, ImageView sharedImageView) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RESULT_ITEM, resultItem);

        navController.navigate(
                R.id.resultDetailsActivity,
                bundle
        );
    }

    @Override
    public void onFavouriteImageViewClick(int pos, Movie resultItem, ImageView favouriteImageView) {
        Log.d("MainFragment", "Favourite Clicked: " + String.valueOf(pos));
        this.resultItem = resultItem;
        if (resultItem.getIsFavourite() == 1) {
            this.favouriteImageView = favouriteImageView;
            userViewModel.deleteFavourite(userId.toString(), resultItem.getId());
        } else {
            Favourite favourite = new Favourite(resultItem.getId(), resultItem.getMediaType());
            this.favouriteImageView = favouriteImageView;
            userViewModel.postFavourite(userId.toString(), favourite);
        }
    }

}


//
//public class MainActivity extends AppCompatActivity implements ResultItemClickListener {
//
//    public static final String EXTRA_RESULT_ITEM = "com.yohanome.moviediscussion.MainActivity.result_item";
//    public static final String EXTRA_RESULT_IMAGE_TRANSITION_NAME = "com.yohanome.moviediscussion.MainActivity.transition_name";
//
//    private MainAdapter mainAdapter;
//    private RecyclerView recyclerView;
//    private MovieViewModel movieViewModel;
//    private BottomAppBar bottomAppBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_main);
//
//        Fade fade = new Fade();
//        fade.excludeTarget(android.R.id.statusBarBackground, true);
//        fade.excludeTarget(android.R.id.navigationBarBackground, true);
//
//        getWindow().setEnterTransition(fade);
//        getWindow().setExitTransition(fade);
//
//        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
//        recyclerView = findViewById(R.id.main_recyclerView_container);
//        bottomAppBar = findViewById(R.id.bottomAppBar);
//
//        // Init RecyclerView
//        initRecyclerView();
//
//        // define Navigation onClickListener for bottomAppBar
//        defineNavigationListener();
//
//        // Get Movie | Series Trending
//        movieViewModel.getTrending();
//
//        // Get Movies
////        movieViewModel.getMovies();
//
//        // Get Series
////        movieViewModel.getSeries();
//
//        // Define Observers
//        initObservers();
//    }
//
//    private void initRecyclerView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        TrendingAdapter trendingAdapter = new TrendingAdapter();
//        DefaultAdapter defaultAdapter = new DefaultAdapter(this);
//        MovieAdapter movieAdapter = new MovieAdapter();
//        SeriesAdapter seriesAdapter = new SeriesAdapter();
//        mainAdapter = new MainAdapter(this, trendingAdapter, defaultAdapter, movieAdapter, seriesAdapter);
//        recyclerView.setAdapter(mainAdapter);
//    }
//
//    private void initObservers() {
//        // Result Observer
//        movieViewModel.getResultLiveData().observe(this, result -> {
//            Log.d(MainActivity.this.getClass().getSimpleName(), String.valueOf(result.getTotalResults()));
////            for(Movie movie: result.getResults()){
////                Log.d(MainActivity.this.getClass().getSimpleName(), String.valueOf(movie.getId()));
////            }
//            mainAdapter.setResult(result);
//            mainAdapter.setTrending(result);
//        });
//
//        // Movie Observer
//        movieViewModel.getMoviesLiveData().observe(this, result -> {
//            Log.d(MainActivity.this.getClass().getSimpleName(), String.valueOf(result.getTotalResults()));
//            mainAdapter.setMovies(result);
//        });
//
//        // Series Observer
//        movieViewModel.getSeriesLiveData().observe(this, result -> {
//            mainAdapter.setSeries(result);
//        });
//
//    }
//
//    private void defineNavigationListener(){
//        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
//            }
//        });
//    }
//
//    @Override
//    public void onResultItemClick(int pos, Movie resultItem, ImageView sharedImageView) {
//        Intent intent = new Intent(this, ResultDetailsActivity.class);
//        String transitionName = ViewCompat.getTransitionName(sharedImageView);
//        intent.putExtra(EXTRA_RESULT_ITEM, resultItem);
//        intent.putExtra(EXTRA_RESULT_IMAGE_TRANSITION_NAME, transitionName);
//
//        assert transitionName != null;
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this,
//                sharedImageView,
//                transitionName
//        );
//        startActivity(intent, options.toBundle());
//
//    }
//}