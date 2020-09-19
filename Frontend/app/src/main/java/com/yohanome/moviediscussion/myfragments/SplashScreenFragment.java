package com.yohanome.moviediscussion.myfragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.viewmodels.UserViewModel;

public class SplashScreenFragment extends Fragment {

    private View lightView;
    private TextView trendingTextView;
    private NavController navController;
    private UserViewModel userViewModel;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Define Views
        lightView = view.findViewById(R.id.splash_screen_light_background);
        trendingTextView = view.findViewById(R.id.splash_screen_trending_text_view);

        // NavController
        navController = Navigation.findNavController(view);

        // Initialize Views and set Animation
        initViewsAndAnimation();

        // Get User Data | Prepare it for the next screen
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        userViewModel.delete();
        // Handle SplashScreen Time and Navigation
        handleSplashScreenDelay();

    }

    private void handleSplashScreenDelay() {
        new Handler().postDelayed(() -> {
            // User Observer
            userViewModel.getUserCachedLiveData().observe(getViewLifecycleOwner(), userCached -> {
                Log.d("MainFragment", "Heereeeee");
                // If the user is not signed in, navigate to Login | there is no user in Database (not Cached)
                if (userCached == null) {
                    navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment());
                    return;
                }

                // If the user already signed in, continue using the application | the user exists in Database
                navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment());
            });
        }, 3000);
    }

    private void initViewsAndAnimation() {
        // Define Animations
        Animation lightAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.splash_light_animation);
        Animation trendingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.splash_trending_animation);

        // Start Animation for Trending TextView
        trendingTextView.startAnimation(trendingAnimation);

        // Define Animation Listeners
        trendingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lightView.startAnimation(lightAnimation);
                lightView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}