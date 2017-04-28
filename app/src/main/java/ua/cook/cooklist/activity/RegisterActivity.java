package ua.cook.cooklist.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ua.cook.cooklist.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        emailEditText = (EditText) findViewById(R.id.register_activity_email);
        passwordEditText = (EditText) findViewById(R.id.register_activity_password);
        nameEditText = (EditText) findViewById(R.id.register_activity_name);
        surnameEditText = (EditText) findViewById(R.id.register_activity_surname);

        surnameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    registration(emailEditText.getText().toString(),passwordEditText.getText().toString(),
                            nameEditText.getText().toString(), surnameEditText.getText().toString());
                }
                return false;
            }
        });

        registerButton = (Button) findViewById(R.id.register_activity_register_button);
        registerButton.setOnClickListener(this);
    }

    private void registration(String email, String password, final String name, final String surname){
        if(!(email.equals("") || password.equals("") || name.equals("") || surname.equals(""))) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name + " " + surname).build();
                        user.updateProfile(profileUpdates);
                        auth.signOut();
                        Toast.makeText(getApplicationContext(), "Успішно зареєстровано", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Пароль занадто короткий або користувач уже існує", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this,"Не всі поля заповнені", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        registration(emailEditText.getText().toString(),passwordEditText.getText().toString(),
                nameEditText.getText().toString(), surnameEditText.getText().toString());
    }
}
