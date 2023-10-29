package com.inksy.UI.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonElement;
import com.inksy.Interfaces.OnDialogClickListener;
import com.inksy.Model.ChatMessageModel;
import com.inksy.Model.UserDataModelFirebase;
import com.inksy.R;
import com.inksy.Remote.APIInterface;
import com.inksy.Remote.Resource;
import com.inksy.Remote.Status;
import com.inksy.UI.Adapter.ChatMessagesAdapter;
import com.inksy.UI.Constants;
import com.inksy.UI.Dialogs.CameraGalleryDialog;
import com.inksy.UI.ViewModel.NotificationView;
import com.inksy.Utils.AppUtils;
import com.inksy.Utils.TinyDB;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivityNew extends AppCompatActivity {
    View view;
//    private ImageLoader imageLoader;
    private ArrayList<String> chatFileList;

//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;
//    @BindView(R.id.tvNotChatFound)
//    TextView tvNotChatFound;

    ImageView ivBack;
    TextView tvName;
    CircleImageView ivUserProfile;

    ImageView imgOnlineStatus;
    TextView tvOnlineStatus;
    LinearLayout onlineStatusLayout;

    File selectedFile;
    Uri cameraUri;
    String imagePath;

    private static final int CAMERA_REQUEST = 1888;

    private String mSenderID, mRecieverID, mConnectionID, mChatID, mReceiverAvatar, mReceiverName, mSenderAvatar, mSenderName;
    private DatabaseReference mDatabase;
    private ChildEventListener childEventListener;
    private String mReceiverIDsForNotification = "";

    String chatRoom, userID, userName, userImage, userCountry, comesFrom = "";

    RecyclerView chatList;

    public ArrayList<ChatMessageModel> chatMessageModelArrayList;
    ChatMessagesAdapter chatMessagesAdapter;

    EditText etMessage;
    ImageView imgSend;

    TinyDB prefManager;
    public static boolean isNotificationScreenOpened;
    boolean isFirstMessageToSend = false;
    boolean isSendTextMessage = false;
    boolean isUploadImageToSend = false;
    String firebaseTempImage;
    String textMessage;

    FirebaseStorage storage;
    StorageReference storageReference;

    NotificationView notificationView;
    Observer<Resource<APIInterface.ApiResponse<JsonElement>>> observer;
    Observer<Resource<APIInterface.ApiResponse<JsonElement>>> observerCreateChatNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        isNotificationScreenOpened = true;

        Intent intent = getIntent();
        if (intent != null) {
            chatRoom = getIntent().getStringExtra("ChatRoom");
            userID = getIntent().getStringExtra("UserID");
            userName = getIntent().getStringExtra("UserName");
            userImage = getIntent().getStringExtra("UserImage");
            userCountry = getIntent().getStringExtra("UserCountry");
            comesFrom = getIntent().getStringExtra("ComesFrom");
        }

        prefManager = new TinyDB(this);

        ImageView imgAttach = (ImageView) findViewById(R.id.imgAttach);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        chatList = findViewById(R.id.chatList);
        chatMessageModelArrayList = new ArrayList<>();

        etMessage = findViewById(R.id.etMessage);
        imgSend = findViewById(R.id.imgSend);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(layoutManager);
        chatMessagesAdapter = new ChatMessagesAdapter(ChatActivityNew.this, chatMessageModelArrayList);
        chatList.setAdapter(chatMessagesAdapter);

        tvName.setText(userName);
        String url = userImage;
        
        if (url.startsWith("http://")) {
            url = url.replace("http://", "https://");
        }

        Glide.with(this).load(url).placeholder(R.drawable.ic_empty_user).into(ivUserProfile);

        ImageView Back = (ImageView) findViewById(R.id.ivBack);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CameraGalleryDialog cameraGalleryDialog = new CameraGalleryDialog(ChatActivityNew.this, new OnDialogClickListener() {
                    @Override
                    public void onDialogClick(String callBack) {
                        if (callBack.equals("Camera")) {
                            if (ContextCompat.checkSelfPermission(ChatActivityNew.this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // If storage permissions are not granted, request permissions at run-time,
                                // as per < API 23 guidelines.
                                requestStoragePermissions();
                            } else {

                                String fileName = "" + System.currentTimeMillis();

                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, fileName);
                                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

                                cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                startActivityForResult(intent, CAMERA_REQUEST);
                            }

                        } else if (callBack.equals("Gallery")) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
                        }
                    }
                });
                cameraGalleryDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                cameraGalleryDialog.show();

            }
        });

        SharedPreferences settings = getSharedPreferences(getApplicationContext().getPackageName() + "userDetails", Context.MODE_PRIVATE);
        mSenderAvatar = settings.getString("user_image", "");
        mSenderName = settings.getString("user_name", "");

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().toString().length() == 0) {

                } else {
                    getUsersData2(userID, "SendMessage", etMessage.getText().toString());
                    textMessage = etMessage.getText().toString();
                    etMessage.setText("");
                }
            }
        });

        getChatAndStartListener(chatRoom);
        getUserOnlineStatus(userID);
        userIsOnChat(true);
//        getUsersLastMsg(chatRoom);


    }

    private DatabaseReference getFirebaseDBInstance() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase;
    }

    private void init() {

        notificationView = new ViewModelProvider(this).get(NotificationView.class);
        notificationView.init();

        observer = new Observer<Resource<APIInterface.ApiResponse<JsonElement>>>() {
            @Override
            public void onChanged(Resource<APIInterface.ApiResponse<JsonElement>> jsonElement) {
                if(jsonElement.getStatus() == Status.LOADING){

                } else if(jsonElement.getStatus() == Status.ERROR){

                } else if(jsonElement.getStatus() == Status.SUCCESS){

                }
            }
        };

        observerCreateChatNode = new Observer<Resource<APIInterface.ApiResponse<JsonElement>>>() {
            @Override
            public void onChanged(Resource<APIInterface.ApiResponse<JsonElement>> jsonElement) {
                if(jsonElement.getStatus() == Status.LOADING){

                } else if(jsonElement.getStatus() == Status.ERROR){

                } else if(jsonElement.getStatus() == Status.SUCCESS){
                    isFirstMessageToSend = false;

                    if(isSendTextMessage){
                        isSendTextMessage = false;
                        getUsersData2(userID, "SendMessage", textMessage);

                    } else if(isUploadImageToSend){
                        isUploadImageToSend = false;
                        getUsersData2(userID, "UploadImage", firebaseTempImage);

                    }
                }
            }
        };

        chatFileList = new ArrayList<String>();

//        progressBar = findViewById(R.id.progressBar);
        ivBack = findViewById(R.id.ivBack);
        tvName = findViewById(R.id.tvName);
        ivUserProfile = findViewById(R.id.ivUserProfile);
        imgOnlineStatus = findViewById(R.id.imgOnlineStatus);
        tvOnlineStatus = findViewById(R.id.tvOnlineStatus);
        onlineStatusLayout = findViewById(R.id.onlineStatusLayout);

/*
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.with(imageView.getContext()).load(url).fit().into(imageView);
            }
        };
*/
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camera_gallery, menu);
        menu.setHeaderTitle("Choose Action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.gallery) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);

        } else if (item.getItemId() == R.id.camera) {
            if (ContextCompat.checkSelfPermission(ChatActivityNew.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermissions();
            } else {

                String fileName = "" + System.currentTimeMillis();

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

                cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, CAMERA_REQUEST);
            }

        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri selectedImageURI = data.getData();
                String uriImagePath = AppUtils.getFileName(ChatActivityNew.this, selectedImageURI);
                uploadImage(selectedImageURI, uriImagePath);
                Log.d("", "");

            } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                selectedFile = new File(cameraUri.getPath());
                imagePath = cameraUri.getPath();
                String uriImagePath = AppUtils.getFileName(ChatActivityNew.this, cameraUri);
                uploadImage(cameraUri, uriImagePath);
                Log.d("", "");

            }
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            Snackbar.make(tvName, "Storage access permissions are required to upload/download files.",
                            Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.white))
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE},
                                        13);
                            }
                        }
                    })
                    .show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        13);
            }
        }
    }

    public void getChatAndStartListener(String chatRoom) {

        Query query = getFirebaseDBInstance()
                .child("chats")
                .child(chatRoom);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chatMessageModelArrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    ChatMessageModel chatMessageModel = data.getValue(ChatMessageModel.class);
                    chatMessageModelArrayList.add(chatMessageModel);
                }

                if(chatMessageModelArrayList.size() == 0){
                    isFirstMessageToSend = true;
                } else {
                    chatMessagesAdapter.notifyDataSetChanged();
                    chatList.smoothScrollToPosition((int) dataSnapshot.getChildrenCount());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getUserOnlineStatus(String userID) {

        Query query = getFirebaseDBInstance()
                .child("users")
                .child(userID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    UserDataModelFirebase userDataModelFirebase = dataSnapshot.getValue(UserDataModelFirebase.class);
                    boolean isOnChat = userDataModelFirebase.getIsOnChat();
                    if (isOnChat) {
                        onlineStatusLayout.setVisibility(View.VISIBLE);
                        ImageViewCompat.setImageTintList(imgOnlineStatus, ColorStateList.valueOf(ContextCompat.getColor(ChatActivityNew.this, R.color.yellow_green_color_picker)));
                        tvOnlineStatus.setText("Active Now");
                    } else {
                        onlineStatusLayout.setVisibility(View.VISIBLE);
                        ImageViewCompat.setImageTintList(imgOnlineStatus, ColorStateList.valueOf(ContextCompat.getColor(ChatActivityNew.this, R.color.line_grey2)));
                        tvOnlineStatus.setText("Offline");
                    }

                } catch (NullPointerException e) {
                    onlineStatusLayout.setVisibility(View.VISIBLE);
                    ImageViewCompat.setImageTintList(imgOnlineStatus, ColorStateList.valueOf(ContextCompat.getColor(ChatActivityNew.this, R.color.line_grey2)));
                    tvOnlineStatus.setText("Offline");
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendMessage(String message, String senderID, String senderName, int type, String image, boolean isUnRead) {
        ChatMessageModel chatMessageModel = new ChatMessageModel();
        chatMessageModel.setMessage(message);
        chatMessageModel.setSenderId(Long.parseLong(senderID));
        chatMessageModel.setSenderName(senderName);
        chatMessageModel.setTimestamp(ServerValue.TIMESTAMP);
        chatMessageModel.setType(Long.parseLong(String.valueOf(type)));
        chatMessageModel.setUserImage(image);
/*
        if (isUnRead) {
            chatMessageModel.setIsUnRead(false);
        } else {
            chatMessageModel.setIsUnRead(true);
        }
*/

        String uniqueID = mDatabase.push().getKey();

        mDatabase.child("chats").child(chatRoom).child(uniqueID).setValue(chatMessageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (chatMessageModelArrayList.size() == 1) {
//                            addChatRecords(chatRoom, senderID, userID, image, userImage, senderName, userName,
//                                    sharedPrefernces.getString(Constants.userCountryLogin, ""), userCountry);

                        }
                        getUsersData2(userID, "PushNotify", message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivityNew.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImage(Uri filePath, String imagePath) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference ref = storageReference.child("Media/Android/" + imagePath);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String image = String.valueOf(task.getResult());
                                    firebaseTempImage = image;
                                    getUsersData2(userID, "UploadImage", image);
                                    Log.d("", "");
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Toast.makeText(ChatActivityNew.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

/*
    public void addChatRecords(String chatRoom, String user1, String user2, String user1Image, String user2Image, String user1Name,
                               String user2Name, String user1Country, String user2Country) {

        ApiInterface apiService = ApiClient.getClient(this);
        Call<String> call = apiService.addChatRecords(chatRoom, user1, user2, user1Image, user2Image, user1Name, user2Name, user1Country, user2Country);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    if (response.code() == 200) {

//                        String userChatID = sharedPrefernces.getString(Constants.userChatID, "");
//                        prefManager.setUserChatID(userChatID + " " +userID);
                        Log.d("", "");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                if (t.toString().startsWith("java.net.UnknownHostException")) {
                    Toast.makeText(ChatActivityNew.this, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/

/*
    public void pushNotify(String userID, final String title, String body) {

        ApiInterface apiService = ApiClient.getClient(this);
        Call<String> call = apiService.pushNotify(userID, title, body);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    if (response.code() == 200) {
                        Log.d("", "");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                if (t.toString().startsWith("java.net.UnknownHostException")) {
                    Toast.makeText(ChatActivityNew.this, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/

    @Override
    protected void onPause() {
        super.onPause();
        isNotificationScreenOpened = false;
        userIsOnChat(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        isNotificationScreenOpened = false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isNotificationScreenOpened = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isNotificationScreenOpened = true;
        userIsOnChat(true);

    }

    public void userIsOnChat(boolean isOnChat) {
        String userID = prefManager.getString("id");
        UserDataModelFirebase chatMessageModel = new UserDataModelFirebase();
//        chatMessageModel.setIsActive(true);
        chatMessageModel.setIsOnChat(isOnChat);

        if (!userID.equals("")) {
            getFirebaseDBInstance().child("users").child(userID).setValue(chatMessageModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("", "");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivityNew.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    public void getUsersData2(String userID, String comesFrom, String image) {
        Query query = getFirebaseDBInstance()
                .child("users")
                .child(userID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    UserDataModelFirebase userDataModelFirebase = dataSnapshot.getValue(UserDataModelFirebase.class);
                    boolean isOnChat = userDataModelFirebase.getIsOnChat();

                    if (comesFrom.equals("UploadImage")) {
                        if(isFirstMessageToSend){
                            isUploadImageToSend = true;
                            createChatNode(userID);

                        } else {
                            sendMessage(
                                    image,
                                    prefManager.getString("id"),
                                    prefManager.getString("fullname"),
                                    2,
                                    prefManager.getString("avatar"),
                                    isOnChat);
                        }

                    } else if(comesFrom.equals("SendMessage")){
                        if(isFirstMessageToSend){
                            isSendTextMessage = true;
                            createChatNode(userID);

                        } else {
                            sendMessage(
                                    image,
                                    prefManager.getString("id"),
                                    prefManager.getString("fullname"),
                                    1,
                                    prefManager.getString("avatar"),
                                    isOnChat);
                        }

                    } else {
                        if(!isOnChat){
//                            String message = image.startsWith("http") ? "Image" : image;
                            pushNotify(userID);
                        }

                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }

/*
    public void getUsersLastMsg(String chatRoom) {

        Query query = getFirebaseDBInstance()
                .child("chats")
                .child(chatRoom)
                .limitToLast(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String lastMessageID = postSnapshot.getKey();
                    LastMessageModel post = postSnapshot.getValue(LastMessageModel.class);

                    if (post.getIsUnRead() && !post.getSenderId().equals(sharedPrefernces.getString(Constants.userIDLogin, ""))) {
                        updateIsUnReadMsg(lastMessageID);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivityNew.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
*/

/*
    public void updateIsUnReadMsg(String lastMessageID) {

        mDatabase.child("chats").child(chatRoom).child(lastMessageID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("isUnRead").setValue(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }
*/

    private void pushNotify(String userId) {
        notificationView.sendMessageNotification(Integer.parseInt(userId), prefManager.getString("token").toString()).
                observe(this, observer);
    }

    private void createChatNode(String userId) {
        notificationView.createChatNode(Integer.parseInt(userId), prefManager.getString("token").toString()).
                observe(this, observerCreateChatNode);
    }

}