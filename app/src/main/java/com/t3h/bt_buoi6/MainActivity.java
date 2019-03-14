package com.t3h.bt_buoi6;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.t3h.bt_buoi6.adapter.NewsAdapter;
import com.t3h.bt_buoi6.model.News;
import com.t3h.bt_buoi6.parser.XMLParser;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Runnable {


    private static final int WHAT_UPDATE_LIST = 1;
    private ArrayList<News> data = new ArrayList<>();
    private NewsAdapter adapter;
    private RecyclerView lvTinTuc;

    private EditText edtTimKiem;
    private ImageView imTimKiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

//        setTextTest();
//        Thread t = new Thread(this);
//        t.start();

    }

//    private void setTextTest() {
//        String input = "<![CDATA[\n" +
//                "<a href=\"https://vnexpress.net/the-thao/tin-the-thao-toi-14-3-ronaldo-co-nguy-co-bi-cam-mot-tran-vi-chia-vui-phan-cam-3894697.html\"><img width=130 height=100 src=\"https://i-thethao.vnecdn.net/2019/03/14/untitled-1552560472-8920-1552560516_180x108.png\" ></a></br>Ngôi sao của Juventus có thể vắng mặt ở trận tứ kết lượt đi nếu UEFA tìm thấy bằng chứng rằng anh khiêu khích CĐV Atletico.\n" +
//                "]]>";
//        tvTest1.setText(input);
//        String value;
//
//        String s1="src=\"";
//
//        int index = input.indexOf(s1)+s1.length();
//        value = input.substring(index);
//        String image = value.substring(0,value.indexOf("\" >"));
//        tvTest2.setText(image);
//
//        s1 = "</br>";
//        index = input.indexOf(s1)+s1.length();
//        value = input.substring(index);
//        String desc = value.substring(0,value.indexOf("."));
//        tvTest3.setText(desc);
//
//    }

    private void initViews() {

        imTimKiem=findViewById(R.id.im_timkiem);
        lvTinTuc=findViewById(R.id.lv_tintuc);
        edtTimKiem=findViewById(R.id.edt_timkiem);
        imTimKiem.setOnClickListener(this);

        adapter = new NewsAdapter(this, data);
        lvTinTuc.setAdapter(adapter);

        edtTimKiem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeyboard();

                    // Your action on done
                    startThread();
                    return true;
                }
                return false;
            }
        });


//        edtTimKiem.setText(createApiGoogle("Ronaldo"));


    }

    private void hideSoftKeyboard() {

        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.im_timkiem:
                startThread();
                break;

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
            String keysearch = edtTimKiem.getText().toString();
            String api = createApiGoogle(keysearch);
//            String api = "https://www.24h.com.vn/upload/rss/bongda.rss";

            //api goggle
            //https://news.google.de/news/feeds?pz=1&amp;cf=vi_vn&amp;ned=vi_vn&amp;hl=vi_vn&amp;q=


            parser.parse(api, p);
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

    private String createApiGoogle(String keysearch) {
        String apiGoogle = "https://news.google.com/rss/search?amp&amp&amp&q=&hl=vi&gl=VN&ceid=VN:vi";
        String s1 = "&q=";
        String value;
        String value1;
        value = apiGoogle;
        value1 = apiGoogle;
        int index = value.indexOf(s1)+s1.length();
        value = value.substring(0,index);
        value1 = value1.substring(index);
        String API;
        API=value+keysearch+value1;
        return (API);

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

}
