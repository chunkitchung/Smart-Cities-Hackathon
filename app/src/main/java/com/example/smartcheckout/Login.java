package com.example.smartcheckout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.widget.Button;
import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity     {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button myButton = findViewById(R.id.help_button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to be executed when the button is clicked
                Snackbar snackbar = Snackbar.make(v, "log in with an existing account or create an account by signing up", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        init();
    }

    public void init(){


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void onLogin(View v){
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        if(emailString.isEmpty() || passwordString.isEmpty()){
            Toast.makeText(Login.this, "Please Fill in all fields!!!", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("DEBUG", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                //Go to Home page
                                Intent i = new Intent(Login.this, Menu.class);
                                startActivity(i);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("DEBUG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Authentication failed, " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void signUp(View v){
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
    }

    //    Button button = (Button) findViewById(R.id.help_button);
//    button.setOnClickListener(new View.OnClickListener() {
//        public void help(View v) {
//            Snackbar snackbar = Snackbar.make(v, "log in with an existing account or create an account by signing up", Snackbar.LENGTH_LONG);
//            snackbar.show();
//        }
//    });
//    myButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Snackbar snackbar = Snackbar.make(v, "log in with an existing account or create an account by signing up", Snackbar.LENGTH_LONG);
//            snackbar.show();
//        }
//    });

}
