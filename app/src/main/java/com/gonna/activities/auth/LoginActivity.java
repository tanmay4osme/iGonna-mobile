package com.gonna.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gonna.R;
import com.gonna.activities.BaseActivity;
import com.gonna.activities.MainActivity;
import com.gonna.firebase.FirebaseService;
import com.gonna.models.User;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    public static final String TAG = "LoginActivity";
    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.edit_email) EditText editEmail;
    @Bind(R.id.edit_password) EditText editPassword;

    @Override public int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login) protected void onLoginButtonClicked() {
        String email = editEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = editPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        FirebaseService.loginUser(email, password, new FirebaseService.Callback<User>() {
            @Override public void onSuccess(User response) {
                progressBar.setVisibility(View.GONE);

                MainActivity.start(LoginActivity.this);
                finish();
                Log.d(TAG, "onAuthStateChanged:signed_in:" + response.id);
            }

            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.text_dont_have_account) protected void onDoNotHaveAccountClicked() {
        RegistrationActivity.start(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(starter);
    }
}
