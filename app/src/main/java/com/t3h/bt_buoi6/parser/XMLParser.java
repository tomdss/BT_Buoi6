package com.t3h.bt_buoi6.parser;

import android.widget.Toast;

import com.t3h.bt_buoi6.MainActivity;
import com.t3h.bt_buoi6.model.News;
import com.t3h.bt_buoi6.utils.Constances;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XMLParser extends DefaultHandler {

    private ArrayList<News> arrNews = new ArrayList<>();
    private StringBuilder builder;
    private News item = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        builder = new StringBuilder();
        if (qName.equals(Constances.ITEM)) {
            item = new News();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (item == null) return;


        switch (qName) {
            case Constances.TITLE:
                item.setTitle(builder.toString());
                break;
            case Constances.DESC:
//                item.setDesc(builder.toString());
                //24h

//                desc24h(item);

                descGoogleNews();

                break;

            //Image
//                String s = "url=\"";
//                String value = builder.toString();
//                int index = value.indexOf(s) + s.length();
//                value = value.substring(index);
//                String image = value.substring(0, value.indexOf("\""));


            case Constances.PUB_DATE:
                item.setPubDate(builder.toString());
                break;
            case Constances.LINK:
                item.setLink(builder.toString());
                break;
            case Constances.ITEM:
                arrNews.add(item);
                break;

        }



    }

    private String setImage(News item) {
        String value = item.toString();
        String s = "url=\"";
        int index = value.indexOf(s) + s.length();
        value = value.substring(index);
        String image = value.substring(0, value.indexOf("\""));
        return image;
    }

    private void descGoogleNews() {

        String s = "<p>";
        String value = builder.toString();

        int i = value.indexOf(s);

        if(i>=0){
            String value1;
            int index = value.indexOf(s) + s.length();
            value1 = value.substring(index);
            String desc = value1.substring(0, value1.indexOf("</p>"));
//        item.setDesc("123");
            item.setDesc(desc);
        }else {
            item.setDesc("Description.");
        }


    }


    private void desc24h(News item) {
        String s = "title='";
        String value = builder.toString();
        int index = value.indexOf(s) + s.length();
        value = value.substring(index);
        String desc = value.substring(0, value.indexOf("'"));
        s = "src='";
        index = value.indexOf(s) + s.length();
        value = value.substring(index);
        String image = value.substring(0, value.indexOf("'"));

        item.setDesc(desc);
        item.setImage(image);
    }

    public ArrayList<News> getArrNews() {
        return arrNews;
    }
}
