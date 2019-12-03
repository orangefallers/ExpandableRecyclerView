package com.ofcat.expandablerecyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by kevin_hsieh on 2019/7/26.
 */

public class ExpandableRecyclerView extends RecyclerView {

    private ExpandableRecyclerAdapter expandableRecyclerAdapter;

    public ExpandableRecyclerView(Context context) {
        this(context, null);
    }

    public ExpandableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context));
    }

    public void expandGroup(int groupPos) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.expandGroup(groupPos);
        }
    }

    public void expandGroupAll() {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.expandAllGroup();
        }
    }

    public void collapseGroup(int groupPos) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.collapseGroup(groupPos);
        }
    }

    public void collapseGroupAll() {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.collapseAllGroup();
        }
    }

    public void setOnGroupExpandListener(ExpandableRecyclerAdapter.OnGroupExpandListener onGroupExpandListener) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.setOnGroupExpandListener(onGroupExpandListener);
        }
    }

    public void setOnGroupCollapseListener(ExpandableRecyclerAdapter.OnGroupCollapseListener onGroupCollapseListener) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.setOnGroupCollapseListener(onGroupCollapseListener);
        }
    }

    public void setOnGroupClickListener(ExpandableRecyclerAdapter.OnGroupItemClickListener onGroupItemClickListener) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.setOnGroupItemClickListener(onGroupItemClickListener);
        }
    }

    public void setOnChildClickListener(ExpandableRecyclerAdapter.OnChildItemClickListener onChildItemClickListener) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.setOnChildItemClickListener(onChildItemClickListener);
        }
    }

    public void setOnGroupStateListener(ExpandableRecyclerAdapter.OnGroupExpandStateListener onGroupExpandStateListener) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.setOnGroupStateListener(onGroupExpandStateListener);
        }
    }

    public void setSelectGroup(int groupPosition) {
        if (expandableRecyclerAdapter != null) {
            expandableRecyclerAdapter.setSelectGroup(groupPosition);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof ExpandableRecyclerAdapter) {
            this.expandableRecyclerAdapter = (ExpandableRecyclerAdapter) adapter;
            super.setAdapter(expandableRecyclerAdapter);
        } else {
            super.setAdapter(adapter);
        }
    }
}
