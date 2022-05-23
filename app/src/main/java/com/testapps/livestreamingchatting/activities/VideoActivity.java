package com.testapps.livestreamingchatting.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.testapps.livestreamingchatting.R;
import com.testapps.livestreamingchatting.adpaters.MessageAdapter;
import com.testapps.livestreamingchatting.adpaters.UserAdapter;
import com.testapps.livestreamingchatting.modal.MessageModal;
import com.testapps.livestreamingchatting.utils.Helper;
import com.testapps.livestreamingchatting.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

public class VideoActivity extends AppCompatActivity implements Params {

    private static final String TAG = "VideoActivity";

    private Activity activity = VideoActivity.this;
    private Button sendBtn;
    private EditText message_et;
    private TextView typing_tc;
    private RecyclerView recyclerView;
    private RecyclerView recycler_view_users;
    private String userName;
    private String roomId;
    private boolean isTypingB = false;

    private MessageAdapter messageAdapter;
    private UserAdapter userAdapter;

    private Socket mSocket;

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: --  Connect");
                    Helper.showSnackBar(activity, "Connected");
                    mSocket.emit("join_room", roomId, userName);
                }
            });
        }

    };


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: --  onConnectError -- " + args[0].toString());
                    Helper.showSnackBar(activity, "onConnectErrorr");
                }
            });
        }

    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: --  Connect");
                    Helper.showSnackBar(activity, "onDisconnect");
                }
            });
        }

    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("user_name");
                        message = data.getString("comment");
                        Helper.showSnackBar(activity, message);
                    } catch (JSONException e) {
                        Helper.showSnackBar(activity, e.getMessage());
                        Log.d(TAG, "run: Exception -- " + e);
                        return;
                    }

                    addMessage(username, message);
                }
            });
        }

    };

    private Emitter.Listener onNewUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    userAdapter.addItem((String) args[0]);
                }
            });
        }

    };

    private Emitter.Listener isTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray data = (JSONArray) args[0];
                        boolean isTyping = (Boolean) data.get(0);
                        String user = (String) data.get(1);
                        if (isTyping && !TextUtils.equals(user, userName)) {
                            typing_tc.setText(user + " is typing...");
                            typing_tc.setVisibility(View.VISIBLE);
                        } else
                            typing_tc.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Helper.showSnackBar(activity, "Exception -- " + e.getMessage());
                        Log.d(TAG, "run: Exception -- " + e);
                    }
                }
            });
        }

    };

    private void addMessage(String username, String message) {
        MessageModal messageModal = new MessageModal();
        messageModal.setMessage(message);
        messageModal.setName(username);
        messageAdapter.addItem(messageModal);
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        sendBtn = findViewById(R.id.button);
        message_et = findViewById(R.id.comment_et);
        typing_tc = findViewById(R.id.typing_tc);
        recyclerView = findViewById(R.id.recycler_view);
        recycler_view_users = findViewById(R.id.recycler_view_users);

        messageAdapter = new MessageAdapter(activity);
        recyclerView.setAdapter(messageAdapter);

        userAdapter = new UserAdapter(activity);
        recycler_view_users.setAdapter(userAdapter);

        userName = getIntent().getStringExtra(INTENT_USER_NAME);
        roomId = getIntent().getStringExtra(INTENT_ROOM_ID);

        try {
          /*  IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME}; //or Polling.NAME*/
            mSocket = IO.socket(SOCKET_URL);
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on("new_user", onNewUser);
            mSocket.on("new_message", onNewMessage);
            mSocket.on("isTyping", isTyping);

        } catch (URISyntaxException e) {
            Helper.showSnackBar(activity, "Exception -- " + e.getMessage());
            Log.d(TAG, "instance initializer:  Exception -- " + e);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = message_et.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("comment", message);
                        jsonObject.put("user_name", userName);
                        message_et.setText("");
                        mSocket.emit("isTyping", false, roomId, userName);
                        mSocket.emit("new_message", roomId, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Helper.showSnackBar(activity, "Please Enter Message to send");
                }

            }
        });

        message_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isTypingB) {
                    mSocket.emit("isTyping", true, roomId, userName);
                    isTypingB = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSocket.emit("isTyping", false, roomId, userName);
                            isTypingB = false;
                        }
                    }, 10000);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initSocket() {

        try {
            IO.Options options = new IO.Options();
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
       /*

            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })*/

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.MINUTES)
                    .writeTimeout(60, TimeUnit.MINUTES)
                    .sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            options.callFactory = clientBuilder.build();
            options.reconnectionAttempts = Integer.MAX_VALUE;
            options.forceNew = false;
            mSocket = IO.socket(SOCKET_URL, options);
        } catch (Exception e) {
            Helper.showSnackBar(activity, "Exception -- " + e.getMessage());
            Log.d(TAG, "socket connect error: " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off("new_user", onNewUser);
        mSocket.off("new_message", onNewMessage);
        mSocket.off("isTyping", isTyping);
    }

}