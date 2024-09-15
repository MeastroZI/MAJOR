package error404.gfg.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import error404.gfg.healthcare.databinding.ActivityAuthanticationBinding;
import error404.gfg.healthcare.databinding.ActivityMainBinding;

public class Authantication extends AppCompatActivity {
    FirebaseAuth fAuth;
    Dialog dialog,loadingDailog;


    //activityBinding
    ActivityAuthanticationBinding activityAuthanticationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authantication);
        fAuth = FirebaseAuth.getInstance();

        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.bg));

        //activityBinding
        activityAuthanticationBinding = ActivityAuthanticationBinding.inflate(getLayoutInflater());
        View view = activityAuthanticationBinding.getRoot();
        setContentView(view);

        activityAuthanticationBinding.createNewBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(Authantication.this, home_screen_2.class);
                startActivity(intentSignup);
                finish();
            }
        });

        activityAuthanticationBinding.createNewBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(Authantication.this, Auth_signup.class);
                startActivity(intentSignup);

            }
        });
        LoadingDailog();
        Dailog();
        ActivityChanger();

        //if user come from signup than redirect email password
        Intent upToIn = getIntent();
        String email = upToIn.getStringExtra("Email");
        String password = upToIn.getStringExtra("Password");
        activityAuthanticationBinding.editEmail.setText(email);
        activityAuthanticationBinding.editPassword.setText(password);


        activityAuthanticationBinding.loginBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = activityAuthanticationBinding.editEmail.getText().toString().trim();
                String password = activityAuthanticationBinding.editPassword.getText().toString().trim();

                activityAuthanticationBinding.emailLoginBg.setBackgroundResource(R.drawable.cud_noti);
                activityAuthanticationBinding.passwordBg.setBackgroundResource(R.drawable.cud_noti);


                if (TextUtils.isEmpty(email)) {
                    activityAuthanticationBinding.emailLoginBg.setBackgroundResource(R.drawable.box_empty);
                    activityAuthanticationBinding.emailLoginBg.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    activityAuthanticationBinding.passwordBg.setBackgroundResource(R.drawable.box_empty);
                    activityAuthanticationBinding.passwordBg.requestFocus();
                } else if (password.length() < 6) {
                    activityAuthanticationBinding.passwordBg.setBackgroundResource(R.drawable.box_empty);
                    activityAuthanticationBinding.passwordBg.requestFocus();
                }
                else{
                    loadingDailog.show();
                    //Authenticate the user
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if(fAuth.getCurrentUser().isEmailVerified())
                                {
                                    //if verified user try to login
                                    Toast.makeText(Authantication.this, "Authentication Successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), home_screen_2.class));
                                    finish();
                                }else
                                {
                                    loadingDailog.cancel();
                                    dialog.show();
//                                    Toast.makeText(Authantication.this, "Please Verify your email" , Toast.LENGTH_LONG).show();
                                }


                            } else {
                                loadingDailog.cancel();
                                Toast.makeText(Authantication.this, "Error !!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                            }

                            //nothing

                        }
                    });
                }
            }


        });




    }
    public void Dailog()
    {
        //dailog
        dialog = new Dialog(Authantication.this);
        dialog.setContentView(R.layout.custom_dailog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dailog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        LinearLayout okay = dialog.findViewById(R.id.btn_okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
    }

    public void ActivityChanger()
    {

        if(fAuth.getCurrentUser() != null && fAuth.getCurrentUser().isEmailVerified()) {
            Intent home = new Intent(Authantication.this, home_screen_2.class);
            startActivity(home);
            finish();
        }
    }

    public void LoadingDailog()
    {
        //dailogLoading
        loadingDailog = new Dialog(Authantication.this);
        loadingDailog.setContentView(R.layout.loading_dailog);
        loadingDailog.getWindow().setBackgroundDrawable(getDrawable(R.color.trans));
        loadingDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDailog.setCancelable(false);
        loadingDailog.getWindow().getAttributes().windowAnimations = R.style.animation;
    }


}

