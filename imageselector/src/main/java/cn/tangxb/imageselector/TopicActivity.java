package cn.tangxb.imageselector;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangxb on 2015/10/14.
 */
public class TopicActivity extends AppCompatActivity {
    private ListView mListView;
    private boolean hasAd;
    private List<String> commentList;
    private List<String> favorList;
    private ComplexAdapter complexAdapter;
    private RelativeLayout parentRl;
    private int topbarHeight;
    private View increaseView;
    private MyOnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        parentRl = (RelativeLayout) findViewById(R.id.rl_parent);
        mListView = (ListView) findViewById(R.id.listview);
        hasAd = true;
        buildData();
        onClickListener = new MyOnClickListener();
        complexAdapter = new ComplexAdapter(this, hasAd, commentList);
        topbarHeight = (int) getResources().getDimension(R.dimen.layout_title_heigh);
        increaseView = View.inflate(this, R.layout.layout_topic_content_title, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, topbarHeight);
        layoutParams.topMargin = topbarHeight;
        increaseView.setLayoutParams(layoutParams);
        mListView.setAdapter(complexAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean increaseFlag;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (visibleItemCount == 0) {
//                    return;
//                }
//                if (complexAdapter.getItemViewType(firstVisibleItem + 1) != ComplexAdapter.contentTitleType) {
//                    if (increaseFlag) {
//                        return;
//                    }
//                    parentRl.addView(increaseView);
//                    TextView favorTv = (TextView) increaseView.findViewById(R.id.tv_favor);
//                    TextView commentTv = (TextView) increaseView.findViewById(R.id.tv_comment);
//                    favorTv.setOnClickListener(onClickListener);
//                    commentTv.setOnClickListener(onClickListener);
//                    favorTv.setText("转发" + favorList.size());
//                    commentTv.setText("评论" + commentList.size());
//                    increaseFlag = true;
//                    return;
//                }
//                parentRl.removeView(increaseView);
//                increaseFlag = false;
//                View childAt = view.getChildAt(firstVisibleItem + 1);
//                Log.e("onScroll", "==============" + (childAt.getTop()));
            }
        });
    }

    private void buildData() {
        commentList = new ArrayList<>();
        favorList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            commentList.add("This is Comment" + i);
        }
        for (int i = 0; i < 4; i++) {
            favorList.add("This is Favor" + i);
        }
    }

    private void changeData(List<String> list, boolean isComment) {
        list.clear();
        if (isComment) {
            list.addAll(commentList);
        } else {
            list.addAll(favorList);
        }
        complexAdapter.setCommentFlag(isComment);
        complexAdapter.notifyDataSetChanged();
    }

    class ComplexAdapter extends BaseAdapter {
        private Context context;
        private boolean hasAd;
        private List<String> list;
        private final int headerType = 0x0;
        public static final int contentTitleType = 0x1;
        private final int contentType = 0x2;
        private LayoutInflater inflater;
        private boolean commentFlag;

        public ComplexAdapter(Context context, boolean hasAd, List<String> list) {
            this.context = context;
            this.hasAd = hasAd;
            this.list = new ArrayList<>();
            this.list.addAll(list);
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            commentFlag = true;
        }

        public List<String> getList() {
            return list;
        }

        public void setCommentFlag(boolean commentFlag) {
            this.commentFlag = commentFlag;
        }

        public boolean isCommentFlag() {
            return commentFlag;
        }

        @Override
        public int getCount() {
            int count = 2;
            count += list != null ? list.size() : 0;
            return count;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            int type = 0;
            if (position == 0) {
                type = headerType;
            } else if (position == 1) {
                type = contentTitleType;
            } else {
                type = contentType;
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

            }
            return convertView;
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_favor) {
                if (!complexAdapter.isCommentFlag()) {
                    return;
                }
                changeData(complexAdapter.getList(), false);
            } else if (id == R.id.tv_comment) {
                if (complexAdapter.isCommentFlag()) {
                    return;
                }
                changeData(complexAdapter.getList(), true);
            }
        }
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
