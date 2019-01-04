package lwc.xihang.com.apptask.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lwc.xihang.com.apptask.R;

/**
 * Created by Administrator on 2019/1/3.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<String> mlist;
    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        mlist = new ArrayList<String>();
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Hodler hodler=null;
        if(view==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            view=inflater.inflate(R.layout.list_booth_item,null);
            hodler=new Hodler();
            hodler.state=view.findViewById(R.id.list_text_id);
            view.setTag(hodler);
        }else {
            hodler= (Hodler) view.getTag();
        }
        hodler.state.setText(mlist.get(i).toString());
        return view;
    }
    protected class Hodler{
        TextView state;
    }
}
