package user_registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datapacktracker.MenuPage;
import com.example.datapacktracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import home.HomeActivity;

public class login_page extends AppCompatActivity {

    private EditText email,pass;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Button log = findViewById(R.id.loginbutton);
        email=findViewById(R.id.email_text_input);
        pass=findViewById(R.id.password_text_input);
        progressBar = findViewById(R.id.progressBar1);
        auth=FirebaseAuth.getInstance();
        Button have_account = findViewById(R.id.do_not_have_account);
        Button forgotpassword = findViewById(R.id.forgot_password);
        log.setOnClickListener(v -> {
            String txt_email=email.getText().toString();
            String txt_pass=pass.getText().toString();
            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){
                Toast.makeText(login_page.this,"Details is Empty",Toast.LENGTH_SHORT).show();
            }
            else if(txt_pass.length()<6){
                Toast.makeText(login_page.this,"Password Length is less than 6",Toast.LENGTH_SHORT).show();
            }
            else{
                login_user(txt_email,txt_pass);
            }

        });

        have_account.setOnClickListener(v -> {
            startActivity(new Intent(login_page.this, signup_page.class));
            finish();
        });

        forgotpassword.setOnClickListener(v -> resetPassword());
    }
    private void login_user(String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Toast.makeText(login_page.this,"Wrong Credentials ",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }else{
                checkIfEmailVerified();
            }
        });
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (user.isEmailVerified())
        {
            Toast.makeText(login_page.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(login_page.this,MenuPage.class));
            finish();
        }
        else
        {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(login_page.this,"Email not verified, check your Mailbox for verification",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void resetPassword() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_reset_password, null);
        dialogBuilder.setView(dialogView);
        final TextInputEditText editEmail = dialogView.findViewById(R.id.edtTxtEmailInput);
        final Button btnReset = dialogView.findViewById(R.id.send_reset_pass);
        final ProgressBar progress =  dialogView.findViewById(R.id.progressBar);
        final AlertDialog dialog = dialogBuilder.create();
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(login_page.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(login_page.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(login_page.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                                progress.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        });
            }
        });
        dialog.show();
    }
}
