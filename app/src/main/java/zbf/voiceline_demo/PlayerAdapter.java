package zbf.voiceline_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zbf on 2017/8/7.
 * 播放列表适配器
 */
public class PlayerAdapter extends BaseAdapter
{
    private List<String> filenames;
    private Context context;

    public PlayerAdapter(List<String> filenames, Context context)
    {
        this.filenames = filenames;
        this.context = context;
        init();
    }

    /**
     * 初始化播放服务
     */
    private void init()
    {

    }

    @Override
    public int getCount()
    {
        return filenames.size();
    }

    @Override
    public Object getItem(int position)
    {
        return filenames.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_fileplay,null);

            viewHolder.filename = (TextView) convertView.findViewById(R.id.tv_filename);
            viewHolder.iv_play = (ImageView) convertView.findViewById(R.id.iv_play);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.filename.setText(filenames.get(position));
        viewHolder.iv_play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        return convertView;
    }

    class ViewHolder
    {
        private TextView filename;
        private ImageView iv_play;
    }
}
