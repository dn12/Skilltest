package net.adiwilaga.skilltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFBActivityFragment extends Fragment {


    private CallbackManager callbackManager;
    ShareDialog shareDialog;

    private TextView textView;
    private ImageView profilepicturre;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;


    private EditText sharetext;
    private EditText sharelink;
    private EditText sharetitle;

    public LoginFBActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_fb, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.bt);
        textView = (TextView) view.findViewById(R.id.tt);
        profilepicturre = (ImageView) view.findViewById(R.id.imageView);

        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);


        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, sharecallback);

        sharetext= (EditText) view.findViewById(R.id.sharetext);
        sharelink= (EditText) view.findViewById(R.id.sharelink);
        sharetitle= (EditText) view.findViewById(R.id.sharetitle);

        Button share= (Button) view.findViewById(R.id.Sharebtn);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share dialog here
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(sharelink.getText().toString()))
                            .setContentTitle(sharetitle.getText().toString())
                            .setContentDescription(sharetext.getText().toString())
                            .build();

                    ShareDialog.show(getActivity(),content);

                }
            }
        });

    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private FacebookCallback<Sharer.Result> sharecallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Toast.makeText(getActivity(), "Content Successfully Posted",Toast.LENGTH_LONG ).show();
            sharetext.setText("");
            sharetitle.setText("");
            sharelink.setText("");

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
            textView.setText(profile.getName());
            setImagebyURL(profilepicturre, profile.getProfilePictureUri(96, 96).toString());;
            Log.d("profile", profile.getProfilePictureUri(96, 96).toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    public void setImagebyURL(final ImageView iv, final String rl) {


    final Handler hand = new Handler();

    Thread thread = new Thread() {

        @Override
        public void run() {
            try {

                URL url = new URL(rl);
                final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                hand.post(new Runnable() {
                    @Override
                    public void run() {
                        // Code here will run in UI thread
                        iv.setImageBitmap(bmp);
                    }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };

    thread.start();
}
}
