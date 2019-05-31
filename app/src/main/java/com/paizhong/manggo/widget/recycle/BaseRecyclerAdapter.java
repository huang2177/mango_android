package com.paizhong.manggo.widget.recycle;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现点击事件、HeaderView、FooterView的RecyclerView 适配器
 *
 * @param <VH> 内容Item的ViewHolder类型
 * @param <T>  数据的类型
 */
public abstract class BaseRecyclerAdapter<VH extends ViewHolder, T> extends RecyclerView.Adapter<ViewHolder> {
    protected OnItemClick onItemClick; //Item点击事件接口
    private OnLongItemClick onLongItemClick; //item长按事件接口
    protected List<T> data = new ArrayList<>();  //数据源
    protected T item;
    protected static final int TYPE_HEADER = 10000; //head类型
    protected static final int TYPE_FOOT = 20000;   //foot类型
    protected static final int TYPE_CONTENT = 0;   //内容类型
    protected Context mContext;
    protected LayoutInflater mInflater;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>(); //header集合

    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>(); //foot 集合

    protected VH viewHolder;

    protected SparseArray<VH> mHolders;

    public abstract VH mOnCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void mOnBindViewHolder(VH holder, int position, T data);


    public BaseRecyclerAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mHolders = new SparseArray<>();
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<T> data) {
        addData(data, true);
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<T> data, boolean notify) {
        this.data.clear();
        this.data.addAll(data);
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void notifyDataChanged(boolean isRefresh, List<T> list) {
        if (isRefresh) {
            this.data.clear();
            if (list != null && list.size() > 0) {
                this.data.addAll(list);
            }
            notifyDataSetChanged();
        } else {
            if (list != null && list.size() > 0) {
                this.data.addAll(list);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 清除数据
     */
    public void clearData() {
        if (data != null && data.size() != 0) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 获取当前数据集
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * 添加头部
     * PS:若头部偏左显示，加上headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
     *
     * @param headerView
     * @return
     */
    public void addHeaderView(View headerView) {
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderViews.put(TYPE_HEADER + mHeaderViews.size(), headerView);
        notifyItemChanged(mHeaderViews.size() - 1);
    }

    /**
     * 删除所有头部
     */
    public void clearHeaderView() {
        if (mHeaderViews.size() != 0) {
            mHeaderViews.clear();
            notifyDataSetChanged();
        }
    }
    /**
     * 添加尾部
     *
     * @param footView
     * @return
     */
    public void addFooterView(View footView) {
        if (footView.getLayoutParams() == null) {
            footView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        mFooterViews.put(TYPE_FOOT + mFooterViews.size(), footView);
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * 添加尾部
     *
     * @param footView
     * @return
     */
    public void addFooterView(View footView, int height) {
        footView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        mFooterViews.put(TYPE_FOOT + mFooterViews.size(), footView);
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * 删除所有尾部
     *
     * @return
     */
    public void clearFooterView() {
        if (mFooterViews.size() != 0) {
            mFooterViews.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 头部总数
     *
     * @return
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * 尾部总数
     *
     * @return
     */
    public int getFootersCount() {
        return mFooterViews.size();
    }

    /**
     * 当前item是否是头布局
     *
     * @param position
     * @return
     */
    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    /**
     * 当前item是否是尾布局
     *
     * @param position
     * @return
     */
    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * 内容长度
     *
     * @return
     */
    protected int getRealItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setonLongItemClickListener(OnLongItemClick onLongItemClick) {
        this.onLongItemClick = onLongItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new HeaderViewHolder(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return new FooterViewHolder(mFooterViews.get(viewType));
        }

        if (mHolders.get(viewType) == null) {
            viewHolder = mOnCreateViewHolder(parent, viewType);
            mHolders.put(viewType, viewHolder);
        } else {
            viewHolder = mHolders.get(viewType);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            return;
        }
        if (onItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mPosition = holder.getLayoutPosition() - mHeaderViews.size();
                    onItemClick.onItemClick(v, mPosition);
                }
            });
        }
        if (onLongItemClick != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int mPosition = holder.getLayoutPosition() - mHeaderViews.size();
                    onLongItemClick.onLongItemClick(v, holder, mPosition);
                    return true;
                }
            });
        }
        position -= mHeaderViews.size();
        item = data.get(position);
        mOnBindViewHolder(mHolders.get(position), position, item);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - getRealItemCount());
        } else {
            return position - getHeadersCount();
        }
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getRealItemCount() + getFootersCount();
    }


    static class HeaderViewHolder extends ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterViewHolder extends ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    //设置HeaderView与FooterView兼容GridLayoutManager
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null || mFooterViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    //设置HeaderView与FooterView兼容StaggeredGridLayoutManager
    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * recycleView Item点击事件
     * xiejingwen
     */
    public interface OnItemClick {
        void onItemClick(View view, int position);
    }

    /**
     * recycleView Item长按事件
     * xiejingwen
     */
    public interface OnLongItemClick {
        void onLongItemClick(View view, ViewHolder holder, int position);
    }
}

