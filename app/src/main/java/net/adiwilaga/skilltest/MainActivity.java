package net.adiwilaga.skilltest;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import net.adiwilaga.skilltest.api.apitv;
import net.adiwilaga.skilltest.model.statusmodel;
import net.adiwilaga.skilltest.model.tv;

import java.util.List;

public class MainActivity extends Activity {

    String baseurl="http://tv.adiwilaga.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Toast.makeText(MainActivity.this,"Hai apa kabar?",Toast.LENGTH_LONG).show();
//
//                Log.i("dna", "Requesting API");
//
//                RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint(baseurl).build();
//                apitv tvapi = restAdapter.create(apitv.class);
//
//                tvapi.gettv("1",new Callback<List<tv>>() {
//                    @Override
//                    public void success(List<tv> tvs, Response response) {
//                        for(tv tipi : tvs){
//                            tipi.save();
//                            Log.i("tipilist",tipi.getName() + " saved");
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Log.e("erorr",error.getMessage());
//                    }
//                });

                Intent ii = new Intent(MainActivity.this,LoginFBActivity.class);
                startActivity(ii);


            }




        });


//        List<statusmodel> lst= statusmodel.getAll();
//        Log.i("statuscount", Integer.toString(lst.size()));
//        for(statusmodel st : lst) {
//            Log.i("ltsdb",st.getName());
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
