package edu.feicui.daynews.entity;

import java.io.Serializable;

/**
 * 注册，登陆共用实体  接收响应（详细信息）
 * Created by zhaoCe on 2016/9/29.
 */

public class RegisterResponseDetail implements Serializable {
    /**
     * 登陆状态
     */
    public int result;
    /**
     * 登陆成功
     */
    public String explain;
    /**
     * 用户令牌
     */
    public String token;
}
