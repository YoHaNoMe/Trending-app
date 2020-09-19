package com.yohanome.moviediscussion.myfragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.models.User;
import com.yohanome.moviediscussion.viewmodels.UserViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {

    private ImageView profileImageView;
    private TextView addImageTextView;
    private TextInputLayout userNameTextField;
    private TextInputLayout nameTextField;
    private TextInputLayout emailTextField;
    private TextInputLayout passwordTextField;
    private TextInputLayout confirmPasswordTextField;
    private NavController navController;
    private UserViewModel userViewModel;
    private String imageUri;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Define Views
        profileImageView = view.findViewById(R.id.register_user_profileImage_imageView);
        addImageTextView = view.findViewById(R.id.register_user_add_photo_textView);
        userNameTextField = view.findViewById(R.id.register_user_name_textField);
        nameTextField = view.findViewById(R.id.register_name_textField);
        emailTextField = view.findViewById(R.id.register_user_email_textField);
        passwordTextField = view.findViewById(R.id.register_user_password_textField);
        confirmPasswordTextField = view.findViewById(R.id.register_user_confirm_password_textField);
        Button signUpButton = view.findViewById(R.id.register_user_sign_up_button);
        TextView alreadyHaveAnAccountTextView = view.findViewById(R.id.register_user_already_have_an_account);

        // Initialize imageUri to Null
        imageUri = null;

        // Set add Photo TextView to visible | To make sure that it show again
        addImageTextView.setVisibility(View.VISIBLE);

        // Get NavController
        navController = Navigation.findNavController(view);

        // Go to Login
        alreadyHaveAnAccountTextView.setOnClickListener(v-> navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()));

        // Sign up Button
        signUpButton.setOnClickListener(v-> getAndValidateUser(view));

        // Get profile image | by setting listener for imageView
        profileImageView.setOnClickListener(v->{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null){
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    private void getAndValidateUser(View view){
        String userName = Objects.requireNonNull(userNameTextField.getEditText()).getText().toString();
        String name = Objects.requireNonNull(nameTextField.getEditText()).getText().toString();
        String email = Objects.requireNonNull(emailTextField.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passwordTextField.getEditText()).getText().toString();
        String confirmPassword = Objects.requireNonNull(confirmPasswordTextField.getEditText()).getText().toString();

        // Check that all the fields are not empty
        if(userName.isEmpty()){
            userNameTextField.setError("This Field is required");
            return;
        }else if(name.isEmpty()){
            nameTextField.setError("This Field is required");
            return;
        }else if(email.isEmpty()){
            emailTextField.setError("This Field is required");
            return;
        }else if(password.isEmpty()){
            passwordTextField.setError("This Field is required");
            return;
        }else if(confirmPassword.isEmpty()){
            confirmPasswordTextField.setError("This Field is required");
            return;
        } else if(!password.equals(confirmPassword)){ // Check if the password doesn't match
            confirmPasswordTextField.setError("The password doesn't match");
            return;
        } else if(imageUri == null || imageUri.isEmpty()) {
            Toast.makeText(getContext(), "There is no image", Toast.LENGTH_LONG).show();
            return;
        }
       // Validation passed successfully
        Log.d("MainFragment", "Inside isLoggedIn From Register****************");
        // Create user Object
        User user = new User(userName, name, email, password, imageUri);

        Log.d("MainActivity", "User has registered from upper: " + user.format());

        // Register User
        userViewModel.registerUser(user).observe(getViewLifecycleOwner(), userLogged->{
            if(userLogged == null){
                Snackbar.make(view, "There is something wrong", Snackbar.LENGTH_LONG).show();
                return;
            }

            Log.d("MainFragment", "Inside isLoggedIn From Register123");
            Log.d("MainActivity", "User has registered  with id: " + userLogged.format());


            // All the data is correct
            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToMainFragment());
        });
    }

    // Simulate image upload
    private String uploadImage(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeStamp + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap image = (Bitmap) extras.get("data");
            profileImageView.setImageBitmap(image);
            imageUri = uploadImage();
            addImageTextView.setVisibility(View.INVISIBLE);
        }
    }
}