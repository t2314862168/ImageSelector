package cn.tangxb.imageselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.tangxb.imageselector.adapter.ComplexAdapter;
import cn.tangxb.imageselector.utils.DataProvider;

/**
 * Created by tangxb on 2015/10/14.
 */
public class TopicActivity extends AppCompatActivity {
    private ListView mListView;
    private DataProvider dataProvider;
    private ComplexAdapter complexAdapter;
    private RelativeLayout floatView;
    private boolean scrollTop;
    private int top = -1;
    private int blankViewTop;
    private TextView favorTv;
    private TextView commentTv;
    private MyOnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mListView = (ListView) findViewById(R.id.listview);
        floatView = (RelativeLayout) findViewById(R.id.rl_float);
        commentTv = (TextView) floatView.findViewById(R.id.tv_float_comment);
        favorTv = (TextView) floatView.findViewById(R.id.tv_float_favor);
        floatView.setVisibility(View.GONE);

        dataProvider = new DataProvider();
        dataProvider.buildData();
        complexAdapter = new ComplexAdapter(this, dataProvider.getFavorList(), dataProvider.getCommentList());
        commentTv.setText("评论" + dataProvider.getCommentList().size());
        favorTv.setText("转发" + dataProvider.getFavorList().size());
        mListView.setAdapter(complexAdapter);

        onClickListener = new MyOnClickListener();
        commentTv.setOnClickListener(onClickListener);
        favorTv.setOnClickListener(onClickListener);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount == 0) {
                    return;
                }
                showOrHideFloat(firstVisibleItem);
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    View blankView = view.getChildAt(totalItemCount - 1);
                    for (int i = firstVisibleItem; i < visibleItemCount; i++) {
                        if (complexAdapter.getItemViewType(i) == ComplexAdapter.contentTitleType) {
                            if (!complexAdapter.isHasCaculate()) {
                                top = view.getChildAt(i).getTop();
                                break;
                            }
                        }
                    }

                    if (!complexAdapter.isHasCaculate() && blankView != null) {
                        blankViewTop = blankView.getTop();
                        complexAdapter.setBlankHeight(mListView.getMeasuredHeight() - blankViewTop + top);
                        complexAdapter.setHasCaculate(true);
                        complexAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void showOrHideFloat(int firstVisibleItem) {
        if (complexAdapter.getItemViewType(firstVisibleItem) == ComplexAdapter.contentTitleType) {
            floatView.setVisibility(View.VISIBLE);
        }
        if (firstVisibleItem == 0) {
            floatView.setVisibility(View.GONE);
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            final int firstVisiblePosition = mListView.getFirstVisiblePosition();
            if (id == R.id.tv_float_favor) {
                if (complexAdapter.isShowComment()) {
                    complexAdapter.changeData(dataProvider.getFavorList(), false);
                    mListView.setSelection(ListView.FOCUS_DOWN);
//                    mListView.setSelection(3);
                }
            } else if (id == R.id.tv_float_comment) {
                if (!complexAdapter.isShowComment()) {
                    complexAdapter.changeData(dataProvider.getCommentList(), true);
                    mListView.post(new Runnable() {
                        @Override
                        public void run() {
                            mListView.smoothScrollToPosition(firstVisiblePosition);
                        }
                    });
                }
            }
        }
    }
}
