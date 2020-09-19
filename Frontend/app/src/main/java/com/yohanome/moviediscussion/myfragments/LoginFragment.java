package com.yohanome.moviediscussion.myfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.models.User;
import com.yohanome.moviediscussion.viewmodels.UserViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private TextInputLayout userNameTextField;
    private TextInputLayout passwordTextField;
    private UserViewModel userViewModel;
    private NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Define Views
        userNameTextField = view.findViewById(R.id.login_user_name_textField);
        passwordTextField = view.findViewById(R.id.login_user_password_textField);
        Button signInButton = view.findViewById(R.id.login_user_sign_in_button);
        TextView createAccountTextView = view.findViewById(R.id.login_user_create_new_account);

        // Get navController
        navController = Navigation.findNavController(view);

        // Go to Register
        createAccountTextView.setOnClickListener(v -> navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()));

        // Sign in Button
        signInButton.setOnClickListener(v -> getAndValidateUserData(view));
    }

    private void getAndValidateUserData(View view){
        String userName = Objects.requireNonNull(userNameTextField.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passwordTextField.getEditText()).getText().toString();

        // Validate user Data
        if(userName.isEmpty()){
            userNameTextField.setError("This field is required");
            return;
        }else if(password.isEmpty()){
            passwordTextField.setError("This Field is required");
            return;
        }

        // Create User object
        User user = new User(userName, password);

        // Login user
        userViewModel.loginUser(user).observe(getViewLifecycleOwner(), userLogged -> {
            Log.d("MainActivity", "User has signed First");
            // Check if The user not found, show SnackBar
            if(userLogged == null) {
                Log.d("MainActivity", "User has signed First2");
                Snackbar.make(view, "User not found or password is wrong", Snackbar.LENGTH_LONG).show();
                return;
            }

            // The User has signed in Successfully
            Log.d("MainActivity", "User has signed in with id: " + userLogged.getEmail());
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());
        });
    }

}