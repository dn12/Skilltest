package net.adiwilaga.skilltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.net.URL;


public class LoginFBActivityFragment extends Fragment {


    private CallbackManager callbackManager;
    private CallbackManager callbackManager1;
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
        callbackManager1 = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
//                if(newToken==null){
//
//                }

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



        sharetext= (EditText) view.findViewById(R.id.sharetext);
        sharelink= (EditText) view.findViewById(R.id.sharelink);
        sharetitle= (EditText) view.findViewById(R.id.sharetitle);

        Button share= (Button) view.findViewById(R.id.Sharebtn);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share dialog here

                if(AccessToken.getCurrentAccessToken()!=null) {
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(sharelink.getText().toString()))
                                .setContentTitle(sharetitle.getText().toString())
                                .setContentDescription(sharetext.getText().toString())
                                .build();

                        ShareDialog.show(getActivity(), content);

                    }
                }else{
                    Toast.makeText(getContext(),"Please Login First!",Toast.LENGTH_LONG).show();
                }
            }
        });



        Button tlpagebtn= (Button) view.findViewById(R.id.TLPageBtn);
        tlpagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AccessToken.getCurrentAccessToken()!=null) {
                    Intent ii= new Intent(getActivity(),LangitPagesActivity.class);
                    startActivity(ii);
                }else{
                    Toast.makeText(getContext(),"Please Login First!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
            Toast.makeText(getActivity(), "Login Successfully",Toast.LENGTH_LONG ).show();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG ).show();
        }
    };

//    private FacebookCallback<Sharer.Result> sharecallback = new FacebookCallback<Sharer.Result>() {
//        @Override
//        public void onSuccess(Sharer.Result result) {
//            Toast.makeText(getActivity(), "Content Successfully Posted",Toast.LENGTH_LONG ).show();
//            sharetext.setText("");
//            sharetitle.setText("");
//            sharelink.setText("");
//
//        }
//
//        @Override
//        public void onCancel() {
//
//
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG ).show();
//        }
//    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        callbackManager1.onActivityResult(requestCode, resultCode, data);
        Log.e("result",data.getExtras().toString());
    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
            textView.setText(profile.getName());
            setImagebyURL(profilepicturre, profile.getProfilePictureUri(96, 96).toString());
            Log.d("profile", profile.getProfilePictureUri(96, 96).toString());
        }else{
            textView.setText("Please Login");
            profilepicturre.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
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

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    thread.start();
}
}
