package com.mabe.productions.prisiukantraste;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    //Google plus login widget init
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final int REQUEST_CODE_FOR_GOOGLE = 1;

    private ImageButton gmailButton;
    private ImageButton facebookButton;
    private ImageView divider_line;
    private TextView login_with_txt;

    private ImageView top_patch;
    private ImageView bottom_patch;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_key));
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        //Google+ init
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //todo: handle
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TEST", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TEST", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }



        gmailButton = (ImageButton) findViewById(R.id.gmail);
        facebookButton = (ImageButton) findViewById(R.id.facebook);
        divider_line = (ImageView) findViewById(R.id.divider_line);
        login_with_txt = (TextView) findViewById(R.id.login_with);
        top_patch = (ImageView) findViewById(R.id.top_patch);
        bottom_patch = (ImageView) findViewById(R.id.bottom_patch);

        Animation from_right_to_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_right_to_left);
        Animation from_left_to_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_left_to_right);
        Animation top_patch_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_patch_anim);
        Animation bottom_patch_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_patch_anim);
        Animation slowly_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_no_delay);
        Animation from_top_to_bottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_top_to_bottom);

        facebookButton.startAnimation(from_right_to_left);
        gmailButton.startAnimation(from_left_to_right);
        divider_line.startAnimation(slowly_fade_in);
        login_with_txt.startAnimation(from_top_to_bottom);

        top_patch.startAnimation(bottom_patch_anim);
        bottom_patch.startAnimation(top_patch_anim);

        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFacebook();
            }
        });



    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            Log.i("TEST", result.getStatus().toString());
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();



                String name = account.getGivenName();
                String user_id = account.getId();
                String unique_token = account.getIdToken();
                String last_name = account.getFamilyName();
                String mail = account.getEmail();
                String device_id = "SAMPLE";

                firebaseAuthWithGoogle(account);

                new ServerManager(LoginActivity.this, "AUTHENTICATE", true, null).execute(user_id, unique_token, name, last_name, device_id,  mail);


            }else{
//                Log.i("TEST", result.toString() + "_______" + result.getStatus().getStatusMessage());
            }

        } else {

        }
    }

    private void signIn() {
        revokeAccess();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("TEST", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d("TEST", "signInWithCredential:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {
//                            Log.w("TEST", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, REQUEST_CODE_FOR_GOOGLE);

                    }
                });
    }

    public void loginFacebook() {


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mabe.productions.prisiukantraste",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("TEST", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(final LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,gender");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),

                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //Log.i("TEST", response.getError().toString());

                                try {
                                    String name = object.getString("first_name");
                                    String last_name = object.getString("last_name");
                                    String user_id = object.getString("id");
                                    String unique_token = loginResult.getAccessToken().getToken().toString();
                                    String mail = object.getString("email");
                                    String device_id = "SAMPLE"; //todo: change

                                    Log.i("TEST", name);
                                    new ServerManager(LoginActivity.this, "AUTHENTICATE", true, null).execute(user_id, unique_token, name, last_name, device_id,  mail);
                                }catch (Exception e ){
                                    Log.i("TEST", "ERROR");
                                }
                            }
                        });



                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("TEST", "cancelTriggered");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("TEST", "errorTriggered");
                exception.printStackTrace();

            }
        });
    }
}
