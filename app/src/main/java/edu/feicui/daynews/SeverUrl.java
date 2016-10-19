package edu.feicui.daynews;

/**
 * Created by Administrator on 2016/9/27.
 */

public class SeverUrl {
    /**
     * 根链接
     */
    public static final String BASE_URL="http://118.244.212.82:9092/newsClient";

    /**
     * 子链接 新闻列表
     */
    public static final String NEWS_LIST=BASE_URL+"/news_list";

    /**
     * 注册链接
     */
    public static final String USER_REGISTER=BASE_URL+"/user_register";
    /**
     * 登陆链接
     */
    public static final String USER_LOGIN=BASE_URL+"/user_login";
    /**
     * 忘记密码链接
     */
    public static final String USER_FORGET_PASS=BASE_URL+"/user_forgetpass";
    /**
     * 评论数量链接
     */
    public static final String COMMENT_NUM=BASE_URL+"/cmt_num";
    /**
     * 显示评论链接
     */
    public static final String SHOW_COMMENT=BASE_URL+"/cmt_list";
    /**
     * 发布评论链接
     */
    public static final String PUBLISH_COMMENT=BASE_URL+"/cmt_commit";
}
