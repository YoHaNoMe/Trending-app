package com.yohanome.moviediscussion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.yohanome.moviediscussion.viewmodels.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

//        userViewModel.delete();
    }


//    @Override
//    public void onBackPressed() {
//        Log.d("MainActivity", "Back button pressed: " + String.valueOf(getSupportFragmentManager().getFragments().size()));
//        for(int i=getSupportFragmentManager().getFragments().size()-1;i>=0;i--){
//            Fragment f = getSupportFragmentManager().getFragments().get(i);
//            getSupportFragmentManager().beginTransaction().remove(f);
//        }
//        finish();
//        super.onBackPressed();
//    }
}