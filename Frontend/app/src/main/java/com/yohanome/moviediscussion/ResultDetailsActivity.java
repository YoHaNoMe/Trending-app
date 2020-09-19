package com.yohanome.moviediscussion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yohanome.moviediscussion.adapters.CastAdapter;
import com.yohanome.moviediscussion.models.Actor;
import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.movieutils.MovieUtils;
import com.yohanome.moviediscussion.movieutils.NetworkUtils;
import com.yohanome.moviediscussion.myfragments.MainFragment;

import java.util.List;

public class ResultDetailsActivity extends AppCompatActivity {

    private Bundle extras;
    private ImageView thumbnail;
    private TextView title;
    private TextView overview;
    private TextView releaseDate;
    private TextView voteCount;
    private RatingBar ratingBar;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        extras = getIntent().getExtras();
        thumbnail = findViewById(R.id.result_details_thumbnail);
        title = findViewById(R.id.result_details_title);
        overview = findViewById(R.id.result_details_overview);
        releaseDate = findViewById(R.id.result_details_release_date);
        voteCount = findViewById(R.id.result_details_vote_count);
        ratingBar = findViewById(R.id.result_details_ratingBar);
        View bottomSheetContainer = findViewById(R.id.result_details_bottomSheet_content);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.result_details_bottomSheet));

        // Populate data and assign it to the views
        populateData();

        RecyclerView recyclerView = findViewById(R.id.cast_section_recyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        CastAdapter castAdapter = new CastAdapter();
        recyclerView.setAdapter(castAdapter);

        // Populate dummy data for Actor
        List<Actor> actors = MovieUtils.populateActorData();
        castAdapter.setActors(actors);

        // Set listener for bottomSheet to open it when tap on it
        bottomSheetContainer.setOnClickListener(v -> {
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void populateData(){
        if(extras != null) {
            Movie result = extras.getParcelable(MainFragment.EXTRA_RESULT_ITEM);
            if(result != null) {

                // Assign Title
                if(result.getName() != null)
                    title.setText(result.getName());
                else
                    title.setText(result.getTitle());

                // Assign Overview
                overview.setText(result.getOverview());

                // Assign ReleaseDate
                if(result.getReleaseDate() != null)
                    releaseDate.setText(result.getReleaseDate());

                // Assign VoteCount
                String voteCountFormatted = getString(R.string.vote_count, result.getVoteCount());
                voteCount.setText(voteCountFormatted);

                // Assign Rating
                double rating = MovieUtils.getRating(result.getVoteAverage());
                ratingBar.setRating((float) rating);

                // Assign ProfileImage
                Picasso.get().load(
                        NetworkUtils.generateImageUrl(result.getPosterPath())
                ).into(thumbnail);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            super.onBackPressed();
        }
    }
}





//public class ResultDetailsActivity extends Fragment {
//
//    private Bundle extras;
//    private ImageView thumbnail;
//    private TextView title;
//    private TextView overview;
//    private TextView releaseDate;
//    private TextView voteCount;
//    private RatingBar ratingBar;
//    private BottomSheetBehavior bottomSheetBehavior;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requireActivity().supportPostponeEnterTransition();
//
//        Fade fade = new Fade();
//        fade.excludeTarget(android.R.id.statusBarBackground, true);
//        fade.excludeTarget(android.R.id.navigationBarBackground, true);
//        requireActivity().getWindow().setEnterTransition(fade);
//        requireActivity().getWindow().setExitTransition(fade);
//
//        extras = getArguments();
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_result_details, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        thumbnail = view.findViewById(R.id.result_details_thumbnail);
//        title = view.findViewById(R.id.result_details_title);
//        overview = view.findViewById(R.id.result_details_overview);
//        releaseDate = view.findViewById(R.id.result_details_release_date);
//        voteCount = view.findViewById(R.id.result_details_vote_count);
//        ratingBar = view.findViewById(R.id.result_details_ratingBar);
//        View bottomSheetContainer = view.findViewById(R.id.result_details_bottomSheet_content);
//        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.result_details_bottomSheet));
//
//        // Populate data and assign it to the views
//        populateData();
//
//        RecyclerView recyclerView = view.findViewById(R.id.cast_section_recyclerView);
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
//        );
//        CastAdapter castAdapter = new CastAdapter();
//        recyclerView.setAdapter(castAdapter);
//
//        // Populate dummy data for Actor
//        List<Actor> actors = MovieUtils.populateActorData();
//        castAdapter.setActors(actors);
//
//        // Set listener for bottomSheet to open it when tap on it
//        bottomSheetContainer.setOnClickListener(v -> {
//            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//    }
//
//    private void populateData(){
//        if(extras != null) {
//            Movie result = extras.getParcelable(MainFragment.EXTRA_RESULT_ITEM);
//            String transitionName = extras.getString(MainFragment.EXTRA_RESULT_IMAGE_TRANSITION_NAME);
//            if(result != null && transitionName != null) {
//                thumbnail.setTransitionName(transitionName);
//
//                // Assign Title
//                if(result.getName() != null)
//                    title.setText(result.getName());
//                else
//                    title.setText(result.getTitle());
//
//                // Assign Overview
//                overview.setText(result.getOverview());
//
//                // Assign ReleaseDate
//                if(result.getReleaseDate() != null)
//                    releaseDate.setText(result.getReleaseDate());
//
//                // Assign VoteCount
//                String voteCountFormatted = getString(R.string.vote_count, result.getVoteCount());
//                voteCount.setText(voteCountFormatted);
//
//                // Assign Rating
//                double rating = MovieUtils.getRating(result.getVoteAverage());
//                ratingBar.setRating((float) rating);
//
//                // Assign ProfileImage
//                Picasso.get().load(
//                        NetworkUtils.generateImageUrl(result.getPosterPath())
//                ).into(thumbnail, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        requireActivity().supportStartPostponedEnterTransition();
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        requireActivity().supportStartPostponedEnterTransition();
//                    }
//                });
//            }
//        }
//    }
