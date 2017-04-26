package com.gonna.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gonna.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompleteRegistrationDialog extends DialogFragment {
    @Bind(R.id.edit_age) EditText editAge;
    @Bind(R.id.rb_gender_male) RadioButton rbGenderMale;
    @Bind(R.id.rb_gender_female) RadioButton rbGenderFemale;

    private CompleteRegistrationDialogCallbacks callbacks;

    public static CompleteRegistrationDialog newInstance(@NonNull CompleteRegistrationDialogCallbacks callbacks) {
        CompleteRegistrationDialog completeRegistrationDialog = new CompleteRegistrationDialog();
        completeRegistrationDialog.setCallbacks(callbacks);

        return completeRegistrationDialog;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_complete_registration, container, false);
        ButterKnife.bind(this, contentView);

        this.setCancelable(false);

        return contentView;
    }

    @OnClick(R.id.btn_done) protected void onDoneButtonClicked() {
        if (!rbGenderMale.isChecked() && !rbGenderFemale.isChecked()) {
            Toast.makeText(this.getActivity(), "Please, specify your gender", Toast.LENGTH_SHORT).show();
            return;
        }
        Integer age = Integer.valueOf(editAge.getText().toString());
        if (age == null) {
            Toast.makeText(this.getActivity(), "Please, specify your age", Toast.LENGTH_SHORT).show();
            return;
        }

        UserParameters parameters = new UserParameters(rbGenderMale.isChecked(), age);
        if (callbacks != null) callbacks.onDoneButtonClicked(parameters, this);
    }

    public void setCallbacks(CompleteRegistrationDialogCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface CompleteRegistrationDialogCallbacks {
        void onDoneButtonClicked(UserParameters parameters, DialogFragment dialog);
    }

    public class UserParameters {
        public boolean gender;
        public int age;

        public UserParameters(boolean gender, int age) {
            this.gender = gender;
            this.age = age;
        }
    }
}