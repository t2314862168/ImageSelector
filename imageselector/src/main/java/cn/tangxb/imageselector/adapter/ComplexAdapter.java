package cn.tangxb.imageselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.tangxb.imageselector.R;

/**
 * Created by tangxb on 2015/10/19.
 */
public class ComplexAdapter extends BaseAdapter {
    private Context context;
    private List<String> favorList;
    private List<String> commentList;
    private List<String> list;
    private final int headerType = 0x0;
    public static final int contentTitleType = 0x1;
    private final int contentType = 0x2;
    private final int footType = 0x3;
    private LayoutInflater inflater;
    private MyOnClickListener onClickListener;
    private int blankHeight;
    private boolean hasCaculate;
    private boolean showComment;

    public ComplexAdapter(Context context, List<String> favorList, List<String> commentList) {
        this.context = context;
        this.favorList = favorList;
        this.commentList = commentList;
        list = new ArrayList<>();
        list.addAll(commentList);
        showComment = true;
        onClickListener = new MyOnClickListener();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBlankHeight(int blankHeight) {
        this.blankHeight = blankHeight;
    }

    public void setHasCaculate(boolean hasCaculate) {
        this.hasCaculate = hasCaculate;
    }

    public boolean isHasCaculate() {
        return hasCaculate;
    }

    public boolean isShowComment() {
        return showComment;
    }

    @Override
    public int getCount() {
        int count = 3;
        count += list != null ? list.size() : 0;
        return count;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position == 0) {
            type = headerType;
        } else if (position == 1) {
            type = contentTitleType;
//            } else {
//                type = contentType;
//            }
        } else if (position < list.size() + 3 - 1) {
            type = contentType;
        } else {
            type = footType;
        }
        return type;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == headerType) {
            HeaderViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_topic_header, parent, false);
                viewHolder = new HeaderViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_header);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (HeaderViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource(R.drawable.test_img);
        } else if (itemViewType == contentTitleType) {
            ContentTitleViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_topic_content_title, parent, false);
                viewHolder = new ContentTitleViewHolder();
                viewHolder.favorTv = (TextView) convertView.findViewById(R.id.tv_favor);
                viewHolder.commentTv = (TextView) convertView.findViewById(R.id.tv_comment);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ContentTitleViewHolder) convertView.getTag();
            }
            viewHolder.favorTv.setText("转发" + favorList.size());
            viewHolder.commentTv.setText("评论" + commentList.size());
            viewHolder.favorTv.setOnClickListener(onClickListener);
            viewHolder.commentTv.setOnClickListener(onClickListener);
        } else if (itemViewType == contentType) {
            ContentViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_topic_content, parent, false);
                viewHolder = new ContentViewHolder();
                viewHolder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ContentViewHolder) convertView.getTag();
            }
            viewHolder.contentTv.setText(list.get(position - 2));
        } else if (itemViewType == footType) {
            FootViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_foot, parent, false);
                viewHolder = new FootViewHolder();
                viewHolder.llView = convertView.findViewById(R.id.ll_foot);
                convertView.setTag(viewHolder);
            } else {
                // 动态设置item的高度 http://www.cnblogs.com/likwo/p/3624425.html
                viewHolder = (FootViewHolder) convertView.getTag();
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) viewHolder.llView.getLayoutParams();
                if (hasCaculate) {
                    linearParams.height = blankHeight;
                } else {
                    linearParams.height = 0;
                }
                viewHolder.llView.setLayoutParams(linearParams);
            }
        }
        return convertView;
    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_favor) {
                if (showComment) {
                    changeData(favorList, false);
                }
            } else if (id == R.id.tv_comment) {
                if (!showComment) {
                    changeData(commentList, true);
                }
            }
        }
    }

    public void changeData(List<String> tempList, boolean flag) {
        list.clear();
        list.addAll(tempList);
        showComment = flag;
        hasCaculate = false;
        blankHeight = 0;
        notifyDataSetChanged();
    }

    static final class FootViewHolder {
        View llView;
    }

    static final class HeaderViewHolder {
        ImageView imageView;
    }

    static final class ContentTitleViewHolder {
        TextView favorTv;
        TextView commentTv;
    }

    static final class ContentViewHolder {
        TextView contentTv;
    }
}