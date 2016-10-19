package edu.feicui.daynews.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/13.
 */
public class UserInfoResponseDetail {
    public String uid;//用户名
    public String portrait;//用户头像
    public int integration;//用户积分
    public int comnum;//用户积分
    public ArrayList<Loginlog> loginlogs;//登陆日志
}
