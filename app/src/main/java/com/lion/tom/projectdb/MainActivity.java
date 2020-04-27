package com.lion.tom.projectdb;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lion.tom.projectdb.Models.User;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText nameBox, emailBox, passwordBox;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_btn:
                Login();
                break;
            case R.id.sign_up_btn:
                Register();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        nameBox = findViewById(R.id.nameBox);
        emailBox = findViewById(R.id.email);
        passwordBox = findViewById(R.id.password);
        Button signIn = findViewById(R.id.sign_in_btn);
        Button signUp = findViewById(R.id.sign_up_btn);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

        FastSignIn();
    }


    private void Login(){
        String email = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();

        if(validateForm(email, password)){
            showProgressDialog();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void Register(){
        String email = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();

        if (validateForm(email, password)) {
            showProgressDialog();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void FastSignIn(){
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private boolean validateForm(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailBox.setError("Please enter email.");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            passwordBox.setError("Please enter password.");
            return false;
        } else {
            emailBox.setError(null);
            passwordBox.setError(null);
            return true;
        }
    }

    private void onAuthSuccess(FirebaseUser firebaseUser) {
        String email = firebaseUser.getEmail();
        String name = nameBox.getText().toString().trim();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(firebaseUser.getDisplayName())){
            name = firebaseUser.getEmail();
            if (name != null && name.contains("@")) {
                name = name.split("@")[0];
            }
        }
        else if(TextUtils.isEmpty(name)) {
            name = firebaseUser.getDisplayName();
        }
        else if(TextUtils.isEmpty(firebaseUser.getDisplayName())){
            final String finalName = name;
            mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(finalName).build();
                    user.updateProfile(profileUpdates);
                }
            }
        };
        }

        User user = new User(name, email);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);

        startActivity(new Intent(this, PostsActivity.class));
        finish();
    }

}
