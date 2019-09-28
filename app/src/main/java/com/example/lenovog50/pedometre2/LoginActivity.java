package com.example.lenovog50.pedometre2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovog50.pedometre2.data.UserRepository;
import com.example.lenovog50.pedometre2.data.model.User;
import com.example.lenovog50.pedometre2.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.button_signup)
    TextView signupButton;
    @BindView(R.id.editText_email)
    EditText emailEditText;
    @BindView(R.id.editText_password)
    EditText passwordEditText;
    @BindView(R.id.text_login)
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRepository userRepository = new UserRepository(this);
        if (userRepository.getConnectedUser()!=null)
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        initViews();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final UserRepository userRepository = new UserRepository(LoginActivity.this);
                userRepository.getUser(email, password, new UserRepository.OnUserSearchComplete() {
                    @Override
                    public void onUserFound(User user) {
                        userRepository.connectUser(user);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onUserNotFound() {
                        Toast.makeText(LoginActivity.this,"email ou mot de passe " +
                                "incorrect",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initViews ()
    {
        ButterKnife.bind(this);
    }
}
