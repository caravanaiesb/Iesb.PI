package br.pi.iesb;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrincipalActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ImageView ivFoto;
    private TextView ivEmail,ivID;
    private Button btnSignOut;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        inicializaCompontentes();
        inicializarFireBase();
        conectarGoogleApi();
        eventoClicks();
    }

    private void eventoClicks() {
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        auth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                 alert("Conta Desconectada");
                 finish();
            }
        });
    }

    private void conectarGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    private void inicializarFireBase() {
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    exibirdados(firebaseUser);
                }else{
                    finish();
                }
            }
        };
    }

    private void exibirdados(FirebaseUser firebaseUser) {
        ivEmail.setText(firebaseUser.getEmail());
        ivID.setText(firebaseUser.getUid());

        Glide.with(PrincipalActivity.this).load(firebaseUser.getPhotoUrl()).into(ivFoto);
    }

    private void inicializaCompontentes() {
        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        ivEmail = (TextView) findViewById(R.id.ivEmail);
        ivID = (TextView) findViewById(R.id.ivId);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Falha na conexão");
    }

    private void alert(String s) {
        Toast.makeText(PrincipalActivity.this,s,Toast.LENGTH_LONG).show();
    }
}
