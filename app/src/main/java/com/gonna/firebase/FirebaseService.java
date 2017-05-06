package com.gonna.firebase;

import android.location.Location;

import com.gonna.models.RegistrationModel;
import com.gonna.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseService {
    public static void loginUser(String email, String password, Callback<User> callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override public void onSuccess(AuthResult authResult) {
                getUser(authResult.getUser().getUid(), callback);
            }
        }).addOnFailureListener(callback::onFailure);
    }

    public static void registerUser(RegistrationModel model, Callback<User> callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(model.email, model.password).addOnSuccessListener
                (response -> createUser(new User(response.getUser().getUid(), model.firstname, model.lastname, model
                        .latitude, model.longitude), callback)).addOnFailureListener(callback::onFailure);
    }

    public static void createUser(User user, Callback<User> callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.id);
        userRef.setValue(user.toJSON()).addOnSuccessListener(aVoid -> callback.onSuccess(user)).addOnFailureListener
                (callback::onFailure);
    }

    public static void getUser(String id, Callback<User> callback) {
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = new User((Map<String, Object>) snapshot.getValue());
                    if (id.equals(user.id)) {
                        callback.onSuccess(user);
                        return;
                    }
                }
                callback.onFailure(new IllegalArgumentException("User with such id is not found"));
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        });
    }

    public static void getAllUsers(Callback<List<User>> callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = new User((Map<String, Object>) snapshot.getValue());
                    users.add(user);
                }
                callback.onSuccess(users);
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        });
    }

    public static void setLocation(Location location, Callback<List<User>> callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid());
        userRef.child("latitude").setValue(location.getLatitude());
        userRef.child("longitude").setValue(location.getLongitude());

        getAllUsers(callback);
    }

    public interface Callback<T> {
        void onSuccess(T response);

        void onFailure(Exception e);
    }
}