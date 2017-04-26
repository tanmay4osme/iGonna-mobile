package com.gonna.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gonna.R;
import com.gonna.activities.BaseActivity;
import com.gonna.activities.MainActivity;
import com.gonna.dialogs.CompleteRegistrationDialog;
import com.gonna.firebase.FirebaseService;
import com.gonna.models.RegistrationModel;
import com.gonna.models.User;

import butterknife.Bind;
import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity {
    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.edit_firstname) EditText editFirstname;
    @Bind(R.id.edit_lastname) EditText editLastname;
    @Bind(R.id.edit_email) EditText editEmail;
    @Bind(R.id.edit_password) EditText editPassword;

    //    GoogleApiClient googleApiClient;
    // for getLastLocation
    // googleApiClient = new GoogleApiClient.Builder(this).build();
    //        LocationServices.FusedLocationApi.getLastLocation(googleApiClient).getLatitude(),
    //                LocationServices.FusedLocationApi.getLastLocation(googleApiClient).getLongitude())

    @Override public int getLayoutId() {
        return R.layout.activity_registration;
    }

    @OnClick(R.id.btn_register) protected void onRegisterButtonClicked() {
        String firstname = editFirstname.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "First name can not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        String lastname = editLastname.getText().toString();
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(this, "Last name can not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = editEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Username can not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = editPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password can not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        CompleteRegistrationDialog.newInstance((parameters, dialog) -> {
            dialog.dismiss();

            RegistrationModel registrationModel = new RegistrationModel(firstname, lastname, email, password, 0.0, 0.0);

            progressBar.setVisibility(View.VISIBLE);
            FirebaseService.registerUser(registrationModel, new FirebaseService.Callback<User>() {
                @Override public void onSuccess(User response) {
                    progressBar.setVisibility(View.GONE);

                    MainActivity.start(RegistrationActivity.this);
                    RegistrationActivity.this.finish();
                }

                @Override public void onFailure(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }).show(this.getSupportFragmentManager(), null);
    }

    @OnClick(R.id.text_have_account) protected void onHaveAccountTextClicked() {
        LoginActivity.start(this);
        this.finish();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, RegistrationActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(starter);
    }
}