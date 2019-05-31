package com.paizhong.manggo.app;

import android.content.Context;

import com.paizhong.manggo.base.BaseModule;
import com.paizhong.manggo.ui.follow.contract.BaseFollowModule;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Des:
 * Created by huang on 2018/9/3 0003 10:03
 */
public class ModuleManager {
    private static ModuleManager instance;
    public static final int TAG_HOME = 1;
    public static final int TAG_FOLLOW = 2;

    private List<BaseModule> mHomeModules;
    private List<BaseModule> mFollowModules;

    private ModuleManager() {
        mHomeModules = new ArrayList<>();
        mFollowModules = new ArrayList<>();
    }

    /**
     * 单一实例
     */
    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }

    /**
     * 添加Module到堆栈
     */
    public void addModule(BaseHomeModule module) {
        if (!mHomeModules.contains(module)) {
            mHomeModules.add(module);
        }
    }

    /**
     * 添加Module到堆栈
     */
    public void addModule(BaseFollowModule module) {
        if (!mFollowModules.contains(module)) {
            mFollowModules.add(module);
        }
    }

    /**
     * 刷新Module
     */
    public void refreshModules(int tag) {
        if (tag == TAG_HOME) {
            for (int i = 0; i < mHomeModules.size(); i++) {
                BaseModule module = mHomeModules.get(i);
                if (module != null) {
                    module.onRefresh(true);
                }
            }
        } else {
            for (int i = 0; i < mFollowModules.size(); i++) {
                BaseModule module = mFollowModules.get(i);
                if (module != null) {
                    module.onRefresh(true);
                }
            }
        }
    }

    /**
     * 刷新Module
     */
    public void visibleModules(int tag, boolean visible) {
        if (tag == TAG_HOME) {
            for (int i = 0; i < mHomeModules.size(); i++) {
                BaseModule module = mHomeModules.get(i);
                if (module != null) {
                    module.onVisible(visible);
                }
            }
        } else {
            for (int i = 0; i < mFollowModules.size(); i++) {
                BaseModule module = mFollowModules.get(i);
                if (module != null) {
                    module.onVisible(visible);
                }
            }
        }
    }

    public void setAuditing(boolean auditing) {
        for (int i = 0; i < mHomeModules.size(); i++) {
            BaseModule module = mHomeModules.get(i);
            if (module != null) {
                module.setAuditing(auditing);
            }
        }
    }

    /**
     * 获取指定的Module
     */
    public <A extends BaseModule> A getModule(Class<A> clazz) {
        A module = null;
        for (int i = 0; i < mHomeModules.size(); i++) {
            if (mHomeModules.get(i).getClass().equals(clazz)) {
                module = (A) mHomeModules.get(i);
                return module;
            }
        }
        for (int i = 0; i < mFollowModules.size(); i++) {
            if (mFollowModules.get(i).getClass().equals(clazz)) {
                module = (A) mFollowModules.get(i);
                return module;
            }
        }
        return module;
    }

    public void bindContext(Context context) {
        for (int i = 0; i < mHomeModules.size(); i++) {
            BaseModule module = mHomeModules.get(i);
            if (module != null) {
                module.bindContext(context);
            }
        }
    }


}
