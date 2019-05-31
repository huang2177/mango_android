package com.paizhong.manggo.base;


public abstract class IBaseView<T> implements BaseView {
    /*******内嵌加载*******/
    /**
     * 开始加载dialog
     * @param content dialog显示的内容
     */
   public void showLoading(String content){

   }

    /**
     * 停止加载dialog
     */
    public void stopLoading(){}

    /**
     * 请求失败
     * @param msg  请求异常信息
     * @param type  若有多个请求，用于区分不同请求（不同请求失败或有不同的处理）
     *              PS：无需区分则可传null
     */
    public void showErrorMsg(String msg, String type){}

    public abstract void bindData(T t);
}
