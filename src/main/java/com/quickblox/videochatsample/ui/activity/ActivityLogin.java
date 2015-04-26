package com.quickblox.videochatsample.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.videochat.core.QBVideoChatController;
import com.quickblox.videochatsample.R;
import com.quickblox.videochatsample.VideoChatApplication;
import com.quickblox.videochatsample.network.Pubnub;

import org.jivesoftware.smack.XMPPException;

public class ActivityLogin extends Activity implements View.OnTouchListener {

    private ProgressDialog progressDialog;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;

    private Path path = new Path();
    private float eventX;
    private float eventY;
    private boolean fingerDown = false;


    //Pubnub pubnubInstance = null;
    String mChannelId = "Housing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initChatService();
        setContentView(R.layout.login_layout);
        //setContentView(new SingleTouchEventView(this, null));

        //pubnubInstance = Pubnub.getInstance(this);
        //pubnubInstance.subscribe(mChannelId, pubnubInstance.callback);
        //pubnubInstance._publish(mChannelId,"akjdsjkfds",pubnubInstance.callback);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        RelativeLayout.LayoutParams params;

        textView1 = new TextView(this);
        textView1.setText("kamal");
        params = new RelativeLayout.LayoutParams(1000, 1000);
        params.leftMargin = 20;
        params.topMargin = 200;
        rl.addView(textView1, params);

        textView1 = new TextView(this);
        textView1.setText("kamal");
        params = new RelativeLayout.LayoutParams(1000, 1000);
        params.leftMargin = 100;
        params.topMargin = 1000;
        rl.addView(textView1, params);


        // setup UI
        //
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        findViewById(R.id.loginByFirstUserBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                createSession(VideoChatApplication.FIRST_USER_LOGIN, VideoChatApplication.FIRST_USER_PASSWORD);
            }
        });

        findViewById(R.id.loginBySecondUserBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                createSession(VideoChatApplication.SECOND_USER_LOGIN, VideoChatApplication.SECOND_USER_PASSWORD);
            }
        });
        try {
            Thread.sleep(1000);

            rl = (RelativeLayout) findViewById(R.id.relativeLayout);
            rl.removeAllViews();
            textView1 = new TextView(this);
            textView1.setText("kamaldeep");
            params = new RelativeLayout.LayoutParams(1000, 1000);
            params.leftMargin = 1000;
            params.topMargin = 200;
            rl.addView(textView1, params);

            textView1 = new TextView(this);
            textView1.setText("kamaldeep");
            params = new RelativeLayout.LayoutParams(1000, 1000);
            params.leftMargin = 100;
            params.topMargin = 1000;
            rl.addView(textView1, params);

        }
        catch (Exception e){

        }


    }

    private void createSession(String login, final String password) {
        QBAuth.createSession(login, password, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

                // Save current user
                //
                VideoChatApplication app = (VideoChatApplication)getApplication();
                app.setCurrentUser(qbSession.getUserId(), password);

                // Login to Chat
                //
                QBChatService.getInstance().login(app.getCurrentUser(), new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        try {
                            QBVideoChatController.getInstance().initQBVideoChatMessageListener();
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                        // show next activity
                        showCallUserActivity();
                    }

                    @Override
                    public void onError(List errors) {
                        Toast.makeText(ActivityLogin.this, "Error when login", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onError(List<String> errors) {
                progressDialog.dismiss();
                Toast.makeText(ActivityLogin.this, "Error when login, check test users login and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initChatService(){
        QBChatService.setDebugEnabled(true);

        if (!QBChatService.isInitialized()) {
            Log.d("ActivityLogin", "InitChat");
            QBChatService.init(this);
        }else{
            Log.d("ActivityLogin", "InitChat not needed");
        }
    }

    private void showCallUserActivity() {
        progressDialog.dismiss();
        
        Intent intent = new Intent(this, ActivityVideoChat.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        eventX = event.getX();
        eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fingerDown = true;
                //path.moveTo(eventX, eventY);
                String data = "down";
                String x = new Float(eventX).toString();
                String y = new Float(eventY).toString();

                Pubnub.getInstance(this)._publish("HousingPosition","{\" "+x+"  \":\"  "+y+"   }\"",Pubnub.getInstance(this).callback);
                Toast.makeText(this, new Float(eventX).toString(),
                        Toast.LENGTH_SHORT).show();

                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                fingerDown = false;
                String data1 = "up";
                //Toast.makeText(this.getContext(), data1,
                //	   Toast.LENGTH_SHORT).show();
                Toast.makeText(this, new Float(eventY).toString(),
                        Toast.LENGTH_SHORT).show();

                // nothing to do
                break;
            default:
                return false;
        }

        // Schedules a repaint.
        //invalidateOptionsMenu();
        return true;

    }
}