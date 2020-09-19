package com.yohanome.moviediscussion;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.yohanome.moviediscussion.models.User;
import com.yohanome.moviediscussion.myfragments.BottomSheetFragment;
import com.yohanome.moviediscussion.viewmodels.UserViewModel;

public class ProfileActivity extends AppCompatActivity {
    private TextInputLayout nameTextField;
    private TextInputLayout emailTextField;
    private TextInputLayout passwordTextField;
    private TextInputLayout confirmPasswordTextField;
    private String imageUri;
    private String userId;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Define Views
        ImageView userImageView = findViewById(R.id.user_profile_image_view);
        nameTextField = findViewById(R.id.profile_update_name_textField);
        emailTextField = findViewById(R.id.profile_update_user_email_textField);
        passwordTextField = findViewById(R.id.profile_update_user_password_textField);
        confirmPasswordTextField = findViewById(R.id.profile_update_user_confirm_password_textField);
        Button updateButton = findViewById(R.id.profile_update_user_update_button);
        ConstraintLayout constraintLayout = findViewById(R.id.activity_profile_main);


        // Define UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            String userImageUrl = bundle.getString(BottomSheetFragment.USER_PROFILE_IMAGE_EXTRA);

            // Assign profile Image
            Picasso.get().load(userImageUrl).into(userImageView);
        }


        // Update button listener
        updateButton.setOnClickListener(v-> updateAndValidate());

        // Assign Data to Views
        userViewModel.getUserCachedLiveData().observe(this, userCached->{
            if(userCached == null)
                return;

            nameTextField.getEditText().setText(userCached.getName());
            emailTextField.getEditText().setText(userCached.getEmail());
            imageUri = userCached.getImageUrl();
            userId = userCached.getId().toString();
        });

        // Listen for userLiveData | if the user has updated successfully
        userViewModel.getUserLiveData().observe(this, user -> {
            if(user != null)
                Snackbar.make(constraintLayout, "User successfully updated", Snackbar.LENGTH_LONG).show();
        });

    }

    private void updateAndValidate(){
        String name = nameTextField.getEditText().getText().toString();
        String email = emailTextField.getEditText().getText().toString();
        String password = passwordTextField.getEditText().getText().toString();
        String confirmPassword = confirmPasswordTextField.getEditText().getText().toString();

        // Check that all the fields are not empty
        if(name.isEmpty()){
            nameTextField.setError("This Field is required");
        }else if(email.isEmpty()){
            emailTextField.setError("This Field is required");
        } else if(!password.equals(confirmPassword)){ // Check if the password doesn't match
            confirmPasswordTextField.setError("The password doesn't match");
        } else if(imageUri == null || imageUri.isEmpty()){
            Toast.makeText(this, "There is no image", Toast.LENGTH_LONG).show();
        }else { // Validation passed successfully

            // Create user Object
            User user = new User(name, email, password, imageUri);

            // Update User
            userViewModel.updateUser(userId, user);
        }
    }

}




//public class ProfileActivity extends Fragment {
//    private String userName;
//    private String userEmail;
//    private String userImageUrl;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_profile, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        ImageView userImageView = view.findViewById(R.id.user_profile_image_view);
//        TextView userNameTextView = view.findViewById(R.id.user_profile_name_text_view);
//
//        if(getIntent().getExtras() != null) {
//            Bundle bundle = getIntent().getExtras();
//            userName = bundle.getString(BottomSheetFragment.NAME_EXTRA);
//            userEmail = bundle.getString(BottomSheetFragment.USER_EMAIL_EXTRA);
//            userImageUrl = bundle.getString(BottomSheetFragment.USER_PROFILE_IMAGE_EXTRA);
//
//            // Assign profile Image
//            Picasso.get().load(userImageUrl).into(userImageView);
//
//            // Assign profile name
//            userNameTextView.setText(userName);
//        }
//    }
//}
