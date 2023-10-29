package com.inksy.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.inksy.Model.ChatMessageModel;
import com.inksy.R;
import com.inksy.UI.Activities.PhotoViewerActivity;
import com.inksy.UI.Activities.ViewOnlyJournal;
import com.inksy.UI.Constants;
import com.inksy.Utils.TinyDB;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ChatMessageModel> imageModelArrayList;
    private Context context;

    TinyDB prefManager;

    public ChatMessagesAdapter(Context ctx, ArrayList<ChatMessageModel> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
        this.context=ctx;
    }

    @Override
    public ChatMessagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.chat_message_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ChatMessagesAdapter.MyViewHolder holder, int position) {

        prefManager = new TinyDB(context);

        long loggedInUserID = Long.parseLong(prefManager.getString("id"));

        if(imageModelArrayList.get(position).getType() == 1){

            if(imageModelArrayList.get(position).getSenderId().equals(loggedInUserID)){

                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.receiverLayout.setVisibility(View.GONE);
                holder.imageSenderLayout.setVisibility(View.GONE);
                holder.imageReceiverLayout.setVisibility(View.GONE);

                holder.senderMessage.setText(imageModelArrayList.get(position).getMessage());

                String url = imageModelArrayList.get(position).getUserImage();
                if(url.startsWith("http://")){
                    url = url.replace("http://", "https://");
                }

                Glide.with(context).load(Constants.BASE_IMAGE + url).placeholder(R.drawable.ic_empty_user).into(holder.senderImage);

            } else {

                holder.receiverLayout.setVisibility(View.VISIBLE);
                holder.senderLayout.setVisibility(View.GONE);
                holder.imageSenderLayout.setVisibility(View.GONE);
                holder.imageReceiverLayout.setVisibility(View.GONE);

                holder.receiverMessage.setText(imageModelArrayList.get(position).getMessage());

                String url = imageModelArrayList.get(position).getUserImage();
                if(url.startsWith("http://")){
                    url = url.replace("http://", "https://");
                }

                Glide.with(context).load(Constants.BASE_IMAGE + url).placeholder(R.drawable.ic_empty_user).into(holder.receiverImage);

            }

        } else if(imageModelArrayList.get(position).getType() == 2){

            if(imageModelArrayList.get(position).getSenderId().equals(loggedInUserID)){

                holder.imageSenderLayout.setVisibility(View.VISIBLE);
                holder.senderLayout.setVisibility(View.GONE);
                holder.receiverLayout.setVisibility(View.GONE);
                holder.imageReceiverLayout.setVisibility(View.GONE);

                String url = imageModelArrayList.get(position).getUserImage();
                if(url.startsWith("http://")){
                    url = url.replace("http://", "https://");
                }

                Glide.with(context).load(Constants.BASE_IMAGE + url).placeholder(R.drawable.ic_empty_user).into(holder.imageSenderImage);

                String url2 = imageModelArrayList.get(position).getMessage();
                if(url2.startsWith("http://")){
                    url2 = url2.replace("http://", "https://");
                }

                Glide.with(context).load(url2).placeholder(R.drawable.ic_empty_user).into(holder.imageSender);

                holder.imageSender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(imageModelArrayList.get(position).getJournalID() != 0){
                            context.startActivity(new Intent(context, ViewOnlyJournal.class).
                                    putExtra(Constants.Companion.getJournalType(), "").
                                    putExtra("JournalId", imageModelArrayList.get(position).getJournalID()));

                        } else {
                            Intent intent = new Intent(context, PhotoViewerActivity.class);
                            intent.putExtra("url", imageModelArrayList.get(position).getMessage());
                            context.startActivity(intent);
                        }
                    }
                });

            } else {

                holder.imageReceiverLayout.setVisibility(View.VISIBLE);
                holder.imageSenderLayout.setVisibility(View.GONE);
                holder.senderLayout.setVisibility(View.GONE);
                holder.receiverLayout.setVisibility(View.GONE);

                String url = imageModelArrayList.get(position).getUserImage();
                if(url.startsWith("http://")){
                    url = url.replace("http://", "https://");
                }

                Glide.with(context).load(Constants.BASE_IMAGE + url).placeholder(R.drawable.ic_empty_user).into(holder.imageReceiverImage);

                String url2 = imageModelArrayList.get(position).getMessage();
                if(url2.startsWith("http://")){
                    url2 = url2.replace("http://", "https://");
                }

                Glide.with(context).load(url2).placeholder(R.drawable.ic_empty_user).into(holder.imageReceiver);

                holder.imageReceiver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(imageModelArrayList.get(position).getJournalID() != 0){
                            context.startActivity(new Intent(context, ViewOnlyJournal.class).
                                    putExtra(Constants.Companion.getJournalType(), "").
                                    putExtra("JournalId", imageModelArrayList.get(position).getJournalID()));

                        } else {
                            Intent intent = new Intent(context, PhotoViewerActivity.class);
                            intent.putExtra("url", imageModelArrayList.get(position).getMessage());
                            context.startActivity(intent);
                        }
                    }
                });

            }

        }


    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView senderMessage, receiverMessage;
        CircleImageView senderImage, receiverImage, imageSenderImage, imageReceiverImage;
        ProgressBar senderImageProgress, receiverImageProgress, imageSenderImageProgress, imageReceiverImageProgress, imageReceiverProgress, imageSenderProgress;
        LinearLayout senderLayout, receiverLayout, imageSenderLayout, imageReceiverLayout;
        ImageView imageSender, imageReceiver;

        public MyViewHolder(View itemView) {
            super(itemView);

            receiverLayout = (LinearLayout) itemView.findViewById(R.id.receiverLayout);
            senderLayout = (LinearLayout) itemView.findViewById(R.id.senderLayout);
            imageSenderLayout = (LinearLayout) itemView.findViewById(R.id.imageSenderLayout);
            imageReceiverLayout = (LinearLayout) itemView.findViewById(R.id.imageReceiverLayout);

            senderImageProgress = (ProgressBar) itemView.findViewById(R.id.senderImageProgress);
            receiverImageProgress = (ProgressBar) itemView.findViewById(R.id.receiverImageProgress);
            imageSenderImageProgress = (ProgressBar) itemView.findViewById(R.id.imageSenderImageProgress);
            imageReceiverImageProgress = (ProgressBar) itemView.findViewById(R.id.imageReceiverImageProgress);
            imageSenderProgress = (ProgressBar) itemView.findViewById(R.id.imageSenderProgress);
            imageReceiverProgress = (ProgressBar) itemView.findViewById(R.id.imageReceiverProgress);

            senderMessage = (TextView) itemView.findViewById(R.id.senderMessage);
            receiverMessage = (TextView) itemView.findViewById(R.id.receiverMessage);

            senderImage = (CircleImageView) itemView.findViewById(R.id.senderImage);
            receiverImage = (CircleImageView) itemView.findViewById(R.id.receiverImage);
            imageSenderImage = (CircleImageView) itemView.findViewById(R.id.imageSenderImage);
            imageReceiverImage = (CircleImageView) itemView.findViewById(R.id.imageReceiverImage);

            imageSender = (ImageView) itemView.findViewById(R.id.imageSender);
            imageReceiver = (ImageView) itemView.findViewById(R.id.imageReceiver);
        }

    }
}
