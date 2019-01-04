package lwc.xihang.com.apptask.ui.activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import lwc.xihang.com.apptask.BR;
import lwc.xihang.com.apptask.R;
import lwc.xihang.com.apptask.databinding.ActivityMainBinding;
import lwc.xihang.com.apptask.ui.fragment.InspectionTaskFragment;
import lwc.xihang.com.apptask.ui.vm.MainViewModel;
import lwc.xihang.com.apptask.utils.MyAdapter;
import me.goldze.mvvmhabit.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    private BluetoothAdapter bluetoothAdapter;
    private List<String> listItems;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private TextView username;
    private TextView showSelectTooth;
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
        // Drawer
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
                    case R.id.downloadTask:
                        Toast.makeText(getApplicationContext(),"下载巡检任务",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.uploadResult:
                        Toast.makeText(getApplicationContext(),"上传巡检结果",Toast.LENGTH_LONG).show();
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
        // 发现事件
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter);
        IntentFilter filter2=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,filter2);
        // 主界面防止一个Activity
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
            String action=intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    listItems.add(device.getName()+"==>"+device.getAddress());
                }
            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                progressDialog.dismiss();
                showDialog();
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
                alertDialog.dismiss();
            }
        });
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();
    }
}
