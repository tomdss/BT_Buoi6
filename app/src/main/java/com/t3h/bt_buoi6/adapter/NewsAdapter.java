package com.t3h.bt_buoi6.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;
import com.t3h.bt_buoi6.MainActivity;
import com.t3h.bt_buoi6.R;
import com.t3h.bt_buoi6.WebviewActivity;
import com.t3h.bt_buoi6.model.News;


import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.FaceHolder>  {

    private LayoutInflater inflater;//anh xa item_face thanh 1 view
    private List<News> data;
    private FaceItemListener listener;

    public NewsAdapter(Context context, List<News> data) {
        this.data = data;
        inflater=LayoutInflater.from(context);
    }

    public void setListener(FaceItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = inflater.inflate(R.layout.item_tintuc,
                viewGroup,false);
        FaceHolder holder=new FaceHolder(v);//anh xa layout  ra roi truyen vao holder
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FaceHolder faceHolder, final int position) {

        News f =data.get(position);
        faceHolder.bindData(f);
        faceHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(position);
                }
            }
        });

        faceHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(listener!=null){
                    listener.onLongClick(position);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class FaceHolder extends RecyclerView.ViewHolder {
        private ImageView ivNews;
        private TextView tvTitle;
        private TextView tvDesc;
        private TextView tvPubdate;


        public FaceHolder(@NonNull View itemView) {
            super(itemView);
            ivNews=itemView.findViewById(R.id.iv_news);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvDesc=itemView.findViewById(R.id.tv_desc);
            tvPubdate=itemView.findViewById(R.id.tv_pubDate);

        }

        public void bindData(final News news){

            tvTitle.setText(news.getTitle());
            tvPubdate.setText(news.getPubDate());
            tvDesc.setText(news.getDesc());



            Glide.with(ivNews)
                    .load(news.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(ivNews);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(news.getLink()));
//                    itemView.getContext().startActivity(intent);



//                }
//            });

        }

    }

    //xu ly su kien click hay longclick
    public interface FaceItemListener{
        void onClick(int position);
        void onLongClick(int position);

    }

}
