package br.pi.iesb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private EditText edtEmail,edtSenha;
    private Button btnLogin,btnRegistrar;
    private TextView txtEsqueci;
    private FirebaseAuth auth;
    private SignInButton btnGoogle;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private LoginButton btnLoginFacebook;
    private FirebaseAuth auth2;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firedatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("   Caravana");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login);
        inicializaComponentes();
        eventoClicks();
        conectarGoogleApi();
        accessToken = AccessToken.getCurrentAccessToken();
        AppEventsLogger.activateApp(this);

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }


    }



    private void conectarGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    private void eventoClicks(){
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i= new Intent(getApplicationContext(),SelecionaCadastroActivity.class);
                 i.putExtra("Chave","L");
                i.putExtra("Chave2","L");

                startActivity(i);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")){
                String email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();
                login(email,senha);
            }else
                alert("Insira Email e Senha!");
            }
        });
        txtEsqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ResetsenhaActivity.class);
                startActivity(i);
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginGoogle();
            }
        });

        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLoginFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                alert("Operacao Cancelada");
            }

            @Override
            public void onError(FacebookException error) {
            alert("Erro no Login com Facebook");
            }
        });
    }

    private void firebaseLoginFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(LoginActivity.this,MenuActivity.class);
                            startActivity(i);
                        }else{
                            alert("Erro na autenticação");
                        }
                    }
                });
    }

    private void LoginGoogle() {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseLogin(account);
            }
        }
    }

    private void firebaseLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String chave = "L";
                        auth2 = Dao.getFirebaseAuth();
                        firebaseUser = auth2.getCurrentUser();
                        String emailFireBase = firebaseUser.getEmail();
                        emailFireBase = emailFireBase.replace("@","_");
                        emailFireBase = emailFireBase.replace(".","*");

                        firedatabase = FirebaseDatabase.getInstance();
                        databaseReference = firedatabase.getReference("Usuarios/"+emailFireBase);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getChildren();
                                Usuario c = dataSnapshot.getValue(Usuario.class);
                                if(c==null){
                                    Intent i = new Intent(LoginActivity.this,SelecionaCadastroActivity.class);
                                    i.putExtra("Chave","L");
                                    i.putExtra("Chave2","G");
                                    startActivity(i);

                                }
                                else {
                                    Intent k = new Intent(LoginActivity.this,MenuActivity.class);
                                    k.putExtra("Chave","L");
                                    k.putExtra("Chave2","G");
                                    startActivity(k);

                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }
                    else{
                        alert("Email Ou Senha Invalidos");
                    }
                    }
                });
    }

    private void login(String email, String senha) {
        auth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String chave = "L";
                            Intent i =  new Intent(LoginActivity.this,MenuActivity.class);
                            i.putExtra("Chave",chave);
                            startActivity(i);
                        }else{
                            alert("E-mail ou Senha Invalido");

                        }
                    }
                });
    }

    private void alert(String s) {
        Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes(){
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar= (Button) findViewById(R.id.btnRegistrar);
        txtEsqueci = (TextView) findViewById(R.id.txtEsqueci);
        btnGoogle = (SignInButton) findViewById(R.id.btnGoogle);
        btnLoginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook);
        btnLoginFacebook.setReadPermissions("email","public_profile");

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Dao.getFirebaseAuth();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Erro no Login");
    }
}
