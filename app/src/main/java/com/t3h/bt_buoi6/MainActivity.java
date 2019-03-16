package com.t3h.bt_buoi6;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.t3h.bt_buoi6.adapter.NewsAdapter;
import com.t3h.bt_buoi6.model.News;
import com.t3h.bt_buoi6.parser.XMLParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements Runnable,NewsAdapter.FaceItemListener {


    public static final int WHAT_UPDATE_LIST = 1;
    public static final String REQUEST_LINK = "web.link";
    public static final String BUNDLE = "bundle";
    private List<News> data = new ArrayList<>();
    private NewsAdapter adapter;
    private RecyclerView lvTinTuc;

    private Toolbar toolbar;

    private String API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initViews();
        adapter = new NewsAdapter(this, data);
        adapter.setListener(this);
        lvTinTuc = findViewById(R.id.lv_tintuc);
        lvTinTuc.setAdapter(adapter);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


//                    itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(news.getLink()));
//                    itemView.getContext().startActivity(intent);
//                }
//            });



//        setTextTest();
        Thread t = new Thread(this);
        t.start();

    }




    private void hideSoftKeyboard() {

        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }


    private void startThread() {
        XMLParser p = new XMLParser();
        Message message = new Message();
        message.what = WHAT_UPDATE_LIST;
        message.obj = p.getArrNews();
        handler.sendMessage(message);
        Thread t = new Thread(this);
        t.start();
    }


    @Override
    public void run() {

        try {
            XMLParser p = new XMLParser();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
//            CreateApiGoogle();
//            String api = createApiGoogle(keysearch);
//            String api = "https://www.24h.com.vn/upload/rss/bongda.rss";

            //api goggle
            //https://news.google.de/news/feeds?pz=1&amp;cf=vi_vn&amp;ned=vi_vn&amp;hl=vi_vn&amp;q=


            parser.parse(API, p);
//            data.clear();
//            data.addAll(p.getArrNews());
//            adapter.notifyDataSetChanged();

            //send data to main thread
            Message message = new Message();
            message.what = WHAT_UPDATE_LIST;
            message.obj = p.getArrNews();
            handler.sendMessage(message);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_UPDATE_LIST) {
                data.clear();
                data.addAll((Collection<? extends News>) msg.obj);
                adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //khi an search ra o tim kiem
                Toast.makeText(MainActivity.this, "Action View Expanded", Toast.LENGTH_SHORT).show();


                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //khi an back o action view
                Toast.makeText(MainActivity.this, "Action View Collapsed", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchItem.setOnActionExpandListener(onActionExpandListener);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                String keysearch = searchView.getQuery().toString().trim();
                String apiGoogle = "https://news.google.de/news/feeds?pz=1&cf=vi_vn&ned=vi_vn&hl=vi_vn&q=";
                API = apiGoogle.concat(keysearch);
                Toast.makeText(MainActivity.this, API, Toast.LENGTH_SHORT).show();
                hideSoftKeyboard();
//
                // Your action on done
                startThread();


                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        return true;
    }


    public void byExtra(String link){

        Intent intent = new Intent(MainActivity.this, WebviewActivity.class);

        intent.putExtra(REQUEST_LINK,link);
        this.startActivity(intent);
    }


    @Override
    public void onClick(int position) {
        Toast.makeText(this, data.get(position).getLink(), Toast.LENGTH_SHORT).show();
//        byBundle(data.get(position).getLink());
//        byBundle("https://news.zing.vn/");

        byExtra(data.get(position).getLink());

    }

    @Override
    public void onLongClick(int position) {

    }



}
