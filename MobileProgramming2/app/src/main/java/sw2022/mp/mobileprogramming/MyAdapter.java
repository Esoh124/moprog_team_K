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

public class MyAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<FriendData> sample;

    public MyAdapter(Context context, ArrayList<FriendData> data) {
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
    public FriendData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_layout, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageNum);
        TextView mfriendName = (TextView)view.findViewById(R.id.friendName);
        TextView mfriendId = (TextView)view.findViewById(R.id.friendId);

        Glide.with(view)
                .load(sample.get(position).getImageUri())
                .into(imageView);

        mfriendName.setText(sample.get(position).getFriendName());
        mfriendId.setText(sample.get(position).getFriendEmailId());

        return view;
    }
}
