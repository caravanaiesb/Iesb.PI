package br.pi.iesb;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {


    private FragmentManager fragmentManager;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private FirebaseUser firebaseUser;
    private ImageView perfilMenu;
    private TextView menuNome,menuSobrenome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Caravana");
        auth = Dao.getFirebaseAuth();
        conectarGoogleApi();
        eventoClicks();
        setContentView(R.layout.menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.Fragment_container2, new MapsFragment(), "MapsFragment");

        fragmentTransaction.commitAllowingStateLoss();

        firebaseUser = auth.getCurrentUser();

        perfilMenu = (ImageView) findViewById(R.id.menuPerfil);
        menuNome = (TextView) findViewById(R.id.menuNome);
        menuSobrenome = (TextView) findViewById(R.id.menuEmail);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navNome = (TextView) headerView.findViewById(R.id.menuNome);
        TextView navSobrenome = (TextView) headerView.findViewById(R.id.menuEmail);
        ImageView navImage = (ImageView) headerView.findViewById(R.id.menuPerfil);
        navNome.setText(firebaseUser.getDisplayName());
        navSobrenome.setText(firebaseUser.getEmail());
        Glide.with(MenuActivity.this).load(firebaseUser.getPhotoUrl()).into(navImage);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void eventoClicks() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.icon_eventos) {
            Intent i = new Intent(MenuActivity.this,FeedeventActivity.class);
            startActivity(i);
            finish();
        }
        else if(id == R.id.friendList){
            Intent i = new Intent(MenuActivity.this,AmigoLista.class);
            startActivity(i);
            finish();

        }
        else if(id == R.id.recomendacaoList){
            Intent i = new Intent(MenuActivity.this,RecomendacaoLista.class);
            startActivity(i);
            finish();
        }
        else if(id== R.id.menuSair){
            signOut();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        auth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                alert("Conta desconectada");
                finish();
            }
        });
    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Falha na conexão");
    }
}
