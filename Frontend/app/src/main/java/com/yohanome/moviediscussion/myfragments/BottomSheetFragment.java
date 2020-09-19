package com.yohanome.moviediscussion.myfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;
import com.yohanome.moviediscussion.ProfileActivity;
import com.yohanome.moviediscussion.R;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String NAME_EXTRA = "com.yohanome.moviediscussion.myfragments.BottomSheetFragment.UserName";
    public static final String USER_EMAIL_EXTRA = "com.yohanome.moviediscussion.myfragments.BottomSheetFragment.UserEmail";
    public static final String USER_PROFILE_IMAGE_EXTRA = "com.yohanome.moviediscussion.myfragments.BottomSheetFragment.UserProfileImage";

    public BottomSheetFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetFragment.STYLE_NORMAL, R.style.appBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.dialog_bottom_sheet, container, false);
        final ImageView userImageImageView = view.findViewById(R.id.dialog_bottom_sheet_userImage_ImageView);
        final TextView nameTextView = view.findViewById(R.id.dialog_bottom_sheet_name_TextView);
        final TextView userEmailTextView = view.findViewById(R.id.dialog_bottom_sheet_userEmail_TextView);
        final TextView profileTextView = view.findViewById(R.id.dialog_bottom_sheet_menu_profileTextView);

        String userImageUrl = "https://cdn.thegentlemansjournal.com/wp-content/uploads/2014/11/stylish-actor-TGJ.00-900x600-c-center.jpg";
        String name = "Yussef hatem";
        String userName = "yohanome";
        String userEmail = "yussefh.galal@gmail.com";


        nameTextView.setText(name);
        userEmailTextView.setText(userEmail);
        Picasso.get().load(userImageUrl).into(userImageImageView);

        // Open Profile Activity
        profileTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            intent.putExtra(NAME_EXTRA, name);
            intent.putExtra(USER_EMAIL_EXTRA, userEmail);
            intent.putExtra(USER_PROFILE_IMAGE_EXTRA, userImageUrl);
            getContext().startActivity(intent);
        });


        return view;
    }
}
