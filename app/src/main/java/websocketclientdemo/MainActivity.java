package websocketclientdemo;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.example.constant.UserInfo;
import com.example.market_supervision.HomeActivity;
import com.example.market_supervision.R;
import com.example.utils.SPUtils;
import websocketclientdemo.adapter.Adapter_ChatMessage;
import websocketclientdemo.im.JWebSocketClient;
import websocketclientdemo.im.JWebSocketClientService;
import websocketclientdemo.modle.ChatMessage;
import websocketclientdemo.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private EditText et_content;
    private ListView listView;
    private Button btn_send;
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息列表
    private ChatMessageReceiver chatMessageReceiver;
    private String bSendUser="x";
    private String userNameCN;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    private class ChatMessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra("message");
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setContent(message.split("[|]")[0]);
//            content=content+"|"+bSendUser+"|"+userNameCN+"|"+nsenduser;//内容+被发送人id+发送人名+发送人id
            String nSendUser=message.split("[|]")[3];
            String bsendUserno = message.split("[|]")[1];
            String userNameCN = message.split("[|]")[2];
            chatMessage.setGetbSendUser(bsendUserno);
            chatMessage.setGetnSendUser(nSendUser);
            chatMessage.setUserNameCN(userNameCN);
            chatMessage.setIsMeSend(0);
            chatMessage.setIsRead(1);
            chatMessage.setTime(System.currentTimeMillis()+"");
            chatMessageList.add(chatMessage);
            initChatMsgListView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        bSendUser=(String)SPUtils.get(getApplicationContext(),"sendUserId","x");
        String userInfoSP= (String) SPUtils.get(getApplicationContext(),"userInfo","userinfo");
        com.example.constant.UserInfo userInfo= JSON.parseObject(userInfoSP, UserInfo.class);
        userNameCN=userInfo.getUserNameCN();
        System.out.println("oncreate");
        //启动服务
        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(mContext);
        findViewById();
        initView();
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }
    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        startService(intent);
    }
    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }


    private void findViewById() {
        listView = findViewById(R.id.chatmsg_listView);
        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.et_content);
        btn_send.setOnClickListener(this);
    }
    private void initView() {
        //监听输入框的变化
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_content.getText().toString().length() > 0) {
                    btn_send.setVisibility(View.VISIBLE);
                } else {
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String content = et_content.getText().toString();
                if (content.length() <= 0) {
                    Util.showToast(mContext, "消息不能为空哟");
                    return;
                }

                if (client != null && client.isOpen()) {
                    String nsenduser=(String)SPUtils.get(getApplicationContext(),"userId","x");
                    content=content+"|"+bSendUser+"|"+userNameCN+"|"+nsenduser;//内容+被发送人id+发送人名+发送人id
                    jWebSClientService.sendMsg(content);
                    //暂时将发送的消息加入消息列表，实际以发送成功为准（也就是服务器返回你发的消息时）
                    ChatMessage chatMessage=new ChatMessage();
                    content=content.split("[|]")[0];
                    chatMessage.setMybSendUser(bSendUser);
                    chatMessage.setMynSendUser(nsenduser);
                    chatMessage.setContent(content);
                    chatMessage.setIsMeSend(1);
                    chatMessage.setIsRead(1);
                    chatMessage.setTime(System.currentTimeMillis()+"");
                    chatMessageList.add(chatMessage);
                    initChatMsgListView();
                    et_content.setText("");
                } else {
                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
                }
                break;
            default:
                break;
        }
    }

    private void initChatMsgListView(){
        List<ChatMessage> nowchatMessageList = new ArrayList<>();
        String nowUser = (String) SPUtils.get(getApplicationContext(), "userId", "1");
        System.out.println(nowUser);
        for (ChatMessage c :
                chatMessageList) {
            if (bSendUser.equals(c.getGetnSendUser()) || bSendUser.equals(c.getMybSendUser())) {
                nowchatMessageList.add(c);
            }
        }
        System.out.println(nowchatMessageList);
//        adapter_chatMessage = new Adapter_ChatMessage(mContext, chatMessageList);
        Adapter_ChatMessage adapter_chatMessage = new Adapter_ChatMessage(mContext, nowchatMessageList);
        adapter_chatMessage.notifyDataSetChanged();
//        adapter_chatMessage.getView()
        listView.setAdapter(adapter_chatMessage);
        listView.setSelection(chatMessageList.size());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onrestart*********");
        bSendUser=(String)SPUtils.get(getApplicationContext(),"sendUserId","x");
        initChatMsgListView();
    }

    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }
    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("activieypause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("mainactidestory");
    }
}
