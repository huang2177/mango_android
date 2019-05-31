package com.paizhong.manggo.widget.recycle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 可以设置emptyView
 */
public class RecyclerView extends android.support.v7.widget.RecyclerView {
    public RecyclerView(Context context) {
        super(context);
    }

    public OnDataChangedListener mListener;
    public interface OnDataChangedListener{
        void onChanged();
    }

    public void setDataChangedListener(OnDataChangedListener listener){

        this.mListener = listener;
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected View emptyView;
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if(mListener != null)mListener.onChanged();
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if(mListener != null)mListener.onChanged();
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if(mListener != null)mListener.onChanged();
            checkIfEmpty();
        }
    };
    protected void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            boolean isEmpty = isEmpty();
            emptyView.setVisibility(isEmpty ? VISIBLE : GONE);
            setVisibility(isEmpty ? GONE : VISIBLE);
        }
    }

    public boolean isEmpty(){
        final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
        return emptyViewVisible;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }
}
