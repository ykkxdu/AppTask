package lwc.xihang.com.apptask.ui.activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import lwc.xihang.com.apptask.BR;
import lwc.xihang.com.apptask.R;
import lwc.xihang.com.apptask.database.OperationDBInspectionTask;
import lwc.xihang.com.apptask.databinding.ActivityMainBinding;
import lwc.xihang.com.apptask.entity.InspectionTask;
import lwc.xihang.com.apptask.ui.fragment.InspectionTaskFragment;
import lwc.xihang.com.apptask.ui.vm.InspectionTaskViewModel;
import lwc.xihang.com.apptask.ui.vm.MainViewModel;
import lwc.xihang.com.apptask.utils.MyAdapter;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 *主界面实现逻辑
* */
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    private BluetoothAdapter bluetoothAdapter;
    private List<String> listItems;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private TextView username;
    private TextView showSelectTooth;
    public String blueToothId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       progressDialog=new ProgressDialog(MainActivity.this);
       username=findViewById(R.id.user_name_id);
       showSelectTooth=findViewById(R.id.show_select_tooth);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // 左边抽屉栏的实现
        final DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.drawer_main_nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    // 搜索蓝牙标签控制
                    case R.id.bluetooth_id:
                        if(!bluetoothAdapter.isEnabled()){
                            bluetoothAdapter.enable();
                        }
                        listItems=new ArrayList<>();
                        bluetoothAdapter.startDiscovery();
                        progressDialog.setTitle("正在搜索中....");
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        break;
                    // 下载检查任务控制
                    case R.id.downloadTask:
                        viewModel.downLoadTask(blueToothId);
                        break;
                    // 上传检查结果控制
                    case R.id.uploadResult:
                        viewModel.uploadTaskResult();
                        break;
                    // 退出系统
                    case R.id.exitSystem:
                        viewModel.logout();
                        break;
                    default:
                        break;
                }
                getSupportActionBar().setTitle(item.getTitle());
                drawer.closeDrawers();
                return true;
            }
        });
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        // 蓝牙标签发现事件
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter);
        IntentFilter filter2=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,filter2);
        // 主界面放置一个Activity
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,new InspectionTaskFragment())
                .commit();
    }

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MainViewModel initViewModel() {
        return new MainViewModel(this);
    }
    // 定义广播接收
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action=intent.getAction();
                if(action.equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                        // 如果蓝牙名字为空，则用name代替
                        if(device.getName()==null)
                        {
                            listItems.add("name"+"-"+device.getAddress());
                        }else {
                            listItems.add(device.getName()+"-"+device.getAddress());
                        }
                    }
                }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                    progressDialog.dismiss();
                    showDialog();
                }
            }catch (Exception e){
                ToastUtils.showLong("请先搜索蓝牙标签!");
            }

        }
    };
    // 巡检任务的上传与下载控制
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除注册
        unregisterReceiver(receiver);
    }
    // 蓝牙标签显示界面
    public void showDialog(){
        Context context=MainActivity.this;
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout=inflater.inflate(R.layout.list_activity,null);
        final ListView myListView =layout.findViewById(R.id.formcustomspinner_list);
        MyAdapter adapter = new MyAdapter(context, listItems);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
                //在这里面就是执行点击后要进行的操作
                showSelectTooth.setText(listItems.get(positon));

                blueToothId=listItems.get(positon).split("-")[1];
                alertDialog.dismiss();
            }
        });
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();
    }
    // 右上角的菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.deleteTaskBaseData:
               new AlertDialog.Builder(this)
                       .setTitle("确认删除全部巡检任务吗?")
                       .setNegativeButton("取消",null)
                       .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               OperationDBInspectionTask task=new OperationDBInspectionTask(getApplicationContext());
                               task.clearDB();
                               new InspectionTaskViewModel().initListView();
                               Toast.makeText(getApplicationContext(),"删除成功!",Toast.LENGTH_LONG).show();
                           }
                       }).show();
               break;
           default:
               break;
       }
       return true;
    }

    // 控制平板下方的返回键不返回到登录界面上。
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
