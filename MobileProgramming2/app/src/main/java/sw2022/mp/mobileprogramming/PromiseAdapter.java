package sw2022.mp.mobileprogramming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PromiseAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<PromiseList> sample;

    public PromiseAdapter(Context context, ArrayList<PromiseList> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PromiseList getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_promise_progress, null);

        TextView mName = (TextView)view.findViewById(R.id.nameValue);
        TextView mDate = (TextView)view.findViewById(R.id.dateValue);
        TextView mTime = (TextView)view.findViewById(R.id.timeValue);
        TextView mFriend = (TextView)view.findViewById(R.id.friendValue);

        mName.setText(sample.get(position).getmPromiseName());
        mDate.setText(sample.get(position).getmDate());
        mTime.setText(sample.get(position).getmTime());
        mFriend.setText(sample.get(position).getmFriend());

        return view;
    }
}
