package com.quickblox.videochatsample.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class Pubnub {

    public static String LOG_TAG = Pubnub.class.getSimpleName();
    private static Pubnub instance = null;

    com.pubnub.api.Pubnub pubnub;
    //SharedPreferences prefs;
    Context context;
    public static String SENDER_ID = "1082378166337";
    public static String REG_ID;
    private static final String APP_VERSION = "3.6.1";

    String PUBLISH_KEY = "pub-c-aa71aec8-a302-4aa2-b541-433fd86db02c";
    String SUBSCRIBE_KEY = "sub-c-aca06252-ab6c-11e4-a431-02ee2ddab7fe";
    String CIPHER_KEY = "";
    String ORIGIN = "pubsub";//.pubnub.com";
    String AUTH_KEY;
    String UUID;
    Boolean SSL = false;

    private Context context_common = this.context;
    private String channelName = "";

    private static final String TAG_Broadcast = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    //private final Handler handler = new Handler();
    //Intent intent = new Intent(BROADCAST_ACTION);
    int counter = 0;


    private Pubnub(Context context){
        this.context_common = context;
//        prefs = context_common.getSharedPreferences(
//                "PUBNUB_DEV_CONSOLE", Context.MODE_PRIVATE);

        //    Log.i("dsfsd", str);
        init();

     //   intent = new Intent(BROADCAST_ACTION);

    }


    public void setChannel(String channelName){
        this.channelName = channelName;
    }

    public String[] getSubscribedChannelsArray(){
        return pubnub.getSubscribedChannelsArray();
    }

    public static synchronized Pubnub getInstance(Context context) {
        if(instance == null) {

            Log.e(LOG_TAG, "first");
            instance = new Pubnub(context);
        }
        Log.e(LOG_TAG, "second");
        return instance;
    }

    /*private Map<String, String> getCredentials() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("PUBLISH_KEY", prefs.getString("PUBLISH_KEY", "demo"));
        map.put("SUBSCRIBE_KEY", prefs.getString("SUBSCRIBE_KEY", "demo"));
        map.put("SECRET_KEY", prefs.getString("SECRET_KEY", "demo"));
        map.put("CIPHER_KEY", prefs.getString("CIPHER_KEY", ""));
        map.put("AUTH_KEY", prefs.getString("AUTH_KEY", null));
        map.put("ORIGIN", prefs.getString("ORIGIN", "pubsub"));
        map.put("UUID", prefs.getString("UUID", null));
        map.put("SSL", prefs.getString("SSL", "false"));
        map.put("SENDER_ID", prefs.getString("SENDER_ID", null));
        return map;
    }*/

    private void init() {

        pubnub = new com.pubnub.api.Pubnub(
                PUBLISH_KEY,
                SUBSCRIBE_KEY,
                CIPHER_KEY,
                SSL
        );
        pubnub.setCacheBusting(false);
        pubnub.setOrigin(ORIGIN);
        pubnub.setAuthKey(AUTH_KEY);
        pubnub.setUUID("kamal");

    }


    static final String TAG = "Register Activity";

    private void notifyUser(Object message) {
        try {
            if (message instanceof JSONObject) {
                final JSONObject obj = (JSONObject) message;

                Log.e("Received msg : ", String.valueOf(obj));

            } else if (message instanceof String) {
                final String obj = (String) message;
                Log.e("Received msg : ", obj.toString());

            } else if (message instanceof JSONArray) {
                final JSONArray obj = (JSONArray) message;
                Log.e("Received msg : ", obj.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void history(String channel, Callback callback) {
        long l = 1393443280000L;
        long end = System.currentTimeMillis();
        pubnub.history(channel, 1000, callback);
    }

    public void historyThroughTimestamp(String channel, Callback callback, long timestamp) {
        pubnub.history(channel, timestamp*10000, false, callback);
    }

    public void historyLastSyncMessage(String channel, Callback callback) {
        pubnub.history(channel, 1, callback);
    }

    public void presence(String channelName, Callback callback){
        try {
            Log.e("presence", "callback");
            pubnub.presence(channelName, callback);
        } catch (PubnubException e) {
            e.printStackTrace();
            Log.e("presence", "callback failed");
        }
    }


    public void hereNow(String channelName, Callback callback){
            //Log.e("hereNow", "callback");
            //pubnub.hereNow(channelName, callback);

        pubnub.hereNow(channelName, new Callback() {
            @Override
            public void successCallback(String channel,
                                        Object message) {
                notifyUser("HERE NOW : " + message);
            }

            @Override
            public void errorCallback(String channel,
                                      PubnubError error) {
                notifyUser("HERE NOW : " + error);
            }
        });
     }


    public Callback presenceCallback = new Callback() {
        @Override
        public void successCallback(String channel,
                                    Object message) {
            Log.e("PRESENCE : " + channel + " : "
                    + message.getClass() + " : "
                    + message.toString(), "");
        }

        @Override
        public void errorCallback(String channel,
                                  PubnubError error) {
            Log.e("PRESENCE : ERROR on channel "
                    + channel + " : "
                    + error.toString(), "");
        }
    };

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    public void gcmAddChannel(String channelName) {
        if (!TextUtils.isEmpty(REG_ID)) {

            pubnub.enablePushNotificationsOnChannel(channelName, REG_ID, new Callback() {
                @Override
                public void successCallback(String channel,
                                            Object message) {
                    notifyUser("GCM ADD : " + message);
                }

                @Override
                public void errorCallback(String channel,
                                          PubnubError error) {
                    notifyUser("GCM ADD : " + error);
                }
            });
        }
    }




    public void _publish(final String channel, String messageStr, Callback callback) {

        Callback publishCallback = new Callback() {
            @Override
            public void successCallback(String channel,
                                        Object message) {

                //notifyUser("PUBLISH : " + message);
            }

            @Override
            public void errorCallback(String channel,
                                      PubnubError error) {
                //notifyUser("PUBLISH : " + error);
            }
        };

        try {
            Integer i = Integer.parseInt(messageStr);
            pubnub.publish(channel, i, new Callback() {});
            return;
        } catch (Exception e) {
        }

        try {
            Double d = Double.parseDouble(messageStr);
            pubnub.publish(channel, d, publishCallback);
            return;
        } catch (Exception e) {
        }


        try {
            JSONArray js = new JSONArray(messageStr);
            pubnub.publish(channel, js, publishCallback);
            return;
        } catch (Exception e) {
        }

        try {
            JSONObject js = new JSONObject(messageStr);
            pubnub.publish(channel, js, publishCallback);
            return;
        } catch (Exception e) {
        }

        pubnub.publish(channel, messageStr, publishCallback);
    }


    public void subscribe(String channel, Callback callback) {
        try {
            pubnub.subscribe(channel, callback);
        } catch (Exception e) {

            notifyUser("SUBSCRIBE : FAILED on channel "
                    + channel + " : "
                    + e.toString());

        }
    }


    public Callback callback = new Callback() {
        @Override
        public void connectCallback(String channel,
                                    Object message) {
            Log.e("","SUBSCRIBE : CONNECT on channel:"
                    + channel
                    + " : "
                    + message.getClass()
                    + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel,
                                       Object message) {
            Log.e("","SUBSCRIBE : DISCONNECT on channel:"
                    + channel
                    + " : "
                    + message.getClass()
                    + " : "
                    + message.toString());
        }

        @Override
        public void reconnectCallback(String channel,
                                      Object message) {
            Log.e("","SUBSCRIBE : RECONNECT on channel:"
                    + channel
                    + " : "
                    + message.getClass()
                    + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel,
                                    Object messageObject) {

            Log.e("","SUBSCRIBE : RECONNECT on channel:"
                    + channel
                    + " : "
                    + messageObject.getClass()
                    + " : "
                    + messageObject.toString());



        }

        @Override
        public void errorCallback(String channel,
                                  PubnubError error) {
            Log.e("","SUBSCRIBE : ERROR on channel "
                    + channel + " : "
                    + error.toString());
        }
    };




}