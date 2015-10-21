package cn.tangxb.imageselector.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangxb on 2015/10/19.
 */
public class DataProvider {
    private List<String> commentList;
    private List<String> favorList;

    public void buildData() {
        commentList = new ArrayList<>();
        favorList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            commentList.add("This is Comment" + i);
        }
        for (int i = 0; i < 2; i++) {
            favorList.add("This is Favor" + i);
        }
    }

    public List<String> getCommentList() {
        return commentList;
    }

    public List<String> getFavorList() {
        return favorList;
    }
}
