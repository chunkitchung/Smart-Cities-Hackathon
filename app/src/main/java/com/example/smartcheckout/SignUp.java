package com.example.smartcheckout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //Sign Up Input
    private EditText name, email, password;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signUp = findViewById(R.id.sign_up_button);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
//            reload();
            //Skip into home page?
        }
    }

    /*
        Called to create user's Firebase Auth account
     */
    public void create(View v){
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String nameStr = name.getText().toString();

        if(emailStr.isEmpty() || passwordStr.isEmpty() || nameStr.isEmpty()){
            Toast.makeText(SignUp.this, "Please Fill in these forms!!!", Toast.LENGTH_SHORT).show();
        }else{

            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("DEBUG", "createUserWithEmail:success");

//                                //Create user document
//                                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                user = mAuth.getCurrentUser();
//                                if(user != null){
//                                    Map<String, Object> data = new HashMap<>();
//                                    data.put('userId', user.getUid());
//                                    db.collection("users").document(user.getUid()).set();
//                                }else{
//                                    Log.d("DEBUG","No current user after sign up");
//                                }

                                //Set name
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameStr)
                                        .build();
                                user = mAuth.getCurrentUser();
                                user.updateProfile(userProfileChangeRequest);

                                //Go to Home page, temporarily goes to shop activity
                                Intent i = new Intent(SignUp.this, Menu.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("DEBUG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed, " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}