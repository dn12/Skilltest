package net.adiwilaga.skilltest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.adiwilaga.skilltest.model.statusmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LangitPagesActivity extends Activity {

    ArrayList<statusmodel> statusList = null;
    statusAdapter m_adapter;
    ListView ls;


    statusmodel st;


    Handler hand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langit_pages);

        hand= new Handler();

        ls=(ListView)findViewById(R.id.listView);


        statusList = new ArrayList<statusmodel>();
        m_adapter = new statusAdapter(this, R.layout.status_inflater, statusList);
        ls.setAdapter(this.m_adapter);


        List<statusmodel> st= statusmodel.getAll();
        for(int i=0;i<st.size();i++){
          statusList.add(st.get(i));
        }


        m_adapter.notifyDataSetChanged();

        Log.d("jumlahlist",String.valueOf(statusList.size()));

        Button reload= (Button) findViewById(R.id.reloadbtn);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/177321525623964/feed",
                        null,
                        HttpMethod.GET,
                        graphcallback
                ).executeAsync();
            }
        });

    }



private GraphRequest.Callback graphcallback = new GraphRequest.Callback() {
    @Override
    public void onCompleted(GraphResponse graphResponse) {



        if(graphResponse.getError()==null) {
            statusmodel.Clean();
            statusList.clear();

            Log.d("jsonarray", graphResponse.toString());
            JSONArray list = null;
            try {
                list = (JSONArray) graphResponse.getJSONObject().get("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (list != null)
                for (int i = 0; i < list.length(); i++) {
                    Gson gs = new Gson();
                    try {

                        JsonObject status = gs.fromJson(list.get(i).toString(), JsonObject.class);
                        JsonObject from = status.getAsJsonObject("from");
                        st = gs.fromJson(list.get(i).toString(), statusmodel.class);


                        st.setName(from.get("name").getAsString());
                        st.setPpimage("https://graph.facebook.com/" + from.get("id").getAsString() + "/picture?width=96&height=96");
                        st.setOrdinal(i);
                        Runnable r = new saveThread(st);
                        new Thread(r).start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
        }else{
            String err=graphResponse.getError().getErrorMessage(); //error belum di handle
            Toast.makeText(getApplicationContext(),err,Toast.LENGTH_LONG).show();
        }

    }
};


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_langit_pages, menu);
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







    private class statusAdapter extends ArrayAdapter<statusmodel> {

        private ArrayList<statusmodel> items;

        public statusAdapter(Context context, int textViewResourceId, ArrayList<statusmodel> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.status_inflater, null);
            }
            final statusmodel o = items.get(position);
            if (o != null) {
                final ImageView iv = (ImageView) v.findViewById(R.id.ppimg);
                TextView name = (TextView) v.findViewById(R.id.nametext);
                TextView datetime = (TextView) v.findViewById(R.id.datetimetext);
                TextView status = (TextView) v.findViewById(R.id.statustext);

                LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll);
                name.setTextSize(14);

                try {
                    ColorDrawable dd = new ColorDrawable(Color.parseColor("#22AAAA"));
                    dd.setAlpha(80);
                    ll.setBackgroundDrawable(dd);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                Context context = iv.getContext();
                int id;


                try {
                    Bitmap bmp = toBitmap(o.getPpimage());
                    iv.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                    name.setText(o.getName());
                    datetime.setText(o.getDatetime());
                    status.setText(o.getStatus());
            }
            return v;
        }


    }


    private String toBase64(Bitmap bmp){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap toBitmap(String strbmp){
        byte[] decodedByte = Base64.decode(strbmp, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



    class saveThread implements Runnable {
        private statusmodel mm;
        public saveThread(statusmodel par) {
            mm=par;
        }

        public void run() {
            try {

                URL url = new URL(mm.getPpimage());
                final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                hand.post(new Runnable() {
                    @Override
                    public void run() {

                        mm.setPpimage(toBase64(bmp));
                        mm.save();

                        statusList.clear();
                        List<statusmodel> sts= statusmodel.getAll();
                        for(int i=0;i<sts.size();i++){
                            statusList.add(sts.get(i));
                        }
                        Log.d("JUMLAH",String.valueOf(statusList.size()));
                        m_adapter.notifyDataSetChanged();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
