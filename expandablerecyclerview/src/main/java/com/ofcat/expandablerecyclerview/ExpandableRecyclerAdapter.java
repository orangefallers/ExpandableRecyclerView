package com.ofcat.expandablerecyclerview;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by kevin_hsieh on 2019/7/26.
 */

public abstract class ExpandableRecyclerAdapter extends RecyclerView.Adapter {

    private final String TAG = ExpandableRecyclerAdapter.class.getSimpleName();

    private final static int VIEW_TYPE_NONE = 0;
    private final static int VIEW_TYPE_GROUP = 1;
    private final static int VIEW_TYPE_CHILD = 2;

    private OnGroupItemClickListener onGroupItemClickListener;
    private OnChildItemClickListener onChildItemClickListener;
    private OnGroupExpandListener onGroupExpandListener;
    private OnGroupCollapseListener onGroupCollapseListener;
    private OnGroupExpandStateListener onGroupExpandStateListener;

    //    private ArrayList<Boolean> expandedList = new ArrayList<>();
    private Boolean[] expandedArray;
    private boolean isDefaultExpandAll = false;
    private boolean isExpandable = true;

    private int selectGroupPosition = -1;
    private int groupCount = -1;
    private int childPosition = 0;
    private int groupPosition = 0;
    private int itemCount = -1;

    private HashMap<Integer, Integer> groupChildCountMap = new HashMap<>();

    private SparseIntArray childCountArrayList;
    private SparseBooleanArray expandedArrayList;
    private HashMap<Integer, Boolean> expandedList = new HashMap<>();

    private SparseArray<Integer> mSectionPositionCache;
    private SparseArray<Integer> mSectionCache;

    public abstract int getGroupCount();

    public abstract int getChildrenCount(int groupPosition);

    public abstract ViewHolderGroup onCreateGroupViewHolder(ViewGroup parent, int viewType);

    public abstract ViewHolderChild onCreateChildViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindGroupViewHolder(ViewHolderGroup holder, int groupPosition, boolean isExpanded);

    public abstract void onBindChildViewHolder(
            ViewHolderChild holder, int groupPosition, int childPosition, boolean isLastChild);

    public ExpandableRecyclerAdapter() {
        childCountArrayList = new SparseIntArray();
        expandedArrayList = new SparseBooleanArray();
        mSectionCache = new SparseArray<>();
        mSectionPositionCache = new SparseArray<>();
    }

    public void removeCache() {
        childCountArrayList = new SparseIntArray();
        expandedArray = null;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    protected void setOnGroupItemClickListener(OnGroupItemClickListener onGroupItemClickListener) {
        this.onGroupItemClickListener = onGroupItemClickListener;
    }

    protected void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    protected void setOnGroupExpandListener(OnGroupExpandListener onGroupExpandListener) {
        this.onGroupExpandListener = onGroupExpandListener;
    }

    protected void setOnGroupCollapseListener(OnGroupCollapseListener onGroupCollapseListener) {
        this.onGroupCollapseListener = onGroupCollapseListener;
    }

    protected void setOnGroupStateListener(OnGroupExpandStateListener onGroupExpandStateListener) {
        this.onGroupExpandStateListener = onGroupExpandStateListener;
    }

    public void useDefaultExpandAll() {
        isDefaultExpandAll = true;
    }

    protected void expandGroup(int groupPosition) {
        if (expandedArray != null && groupPosition >= 0
                && groupPosition < expandedArray.length && !expandedArray[groupPosition]) {
            int adapterPosition = getAdapterPositionForGroupPosition(groupPosition);
            expandedArray[groupPosition] = true;

            if (onGroupExpandListener != null) {
                onGroupExpandListener.onGroupExpand(groupPosition);
            }

            if (onGroupExpandStateListener != null) {
                onGroupExpandStateListener.onGroupExpandState(isExpandAll());
            }

            notifyItemChanged(adapterPosition);
            notifyItemRangeInserted(adapterPosition + 1, getChildrenCount(groupPosition));
        }
    }

    protected void collapseGroup(int groupPosition) {
        if (expandedArray != null && groupPosition >= 0
                && groupPosition < expandedArray.length && expandedArray[groupPosition]) {
            int adapterPosition = getAdapterPositionForGroupPosition(groupPosition);
            expandedArray[groupPosition] = false;

            if (onGroupCollapseListener != null) {
                onGroupCollapseListener.onGroupCollapse(groupPosition);
            }

            if (onGroupExpandStateListener != null) {
                onGroupExpandStateListener.onGroupExpandState(false);
            }

            notifyItemChanged(adapterPosition);
            notifyItemRangeRemoved(adapterPosition + 1, getChildrenCount(groupPosition));
        }
    }

    protected void expandAllGroup() {
        if (expandedArray != null) {
            for (int i = 0; i < expandedArray.length; i++) {
                if (!expandedArray[i]) {
                    int adapterPosition = getAdapterPositionForGroupPosition(i);
                    expandedArray[i] = true;
                    // 解決position 0 不會刷新的問題
                    notifyItemChanged(adapterPosition);
                    notifyItemRangeInserted(adapterPosition + 1, getChildrenCount(i));
                }
            }

            if (onGroupExpandStateListener != null) {
                onGroupExpandStateListener.onGroupExpandState(true);
            }
        }
    }

    protected void collapseAllGroup() {
        if (expandedArray != null) {
            for (int i = 0; i < expandedArray.length; i++) {
                if (expandedArray[i]) {
                    int adapterPosition = getAdapterPositionForGroupPosition(i);
                    expandedArray[i] = false;
                    // 解決position 0 不會刷新的問題
                    notifyItemChanged(adapterPosition);
                    notifyItemRangeRemoved(adapterPosition + 1, getChildrenCount(i));
                }
            }

            if (onGroupExpandStateListener != null) {
                onGroupExpandStateListener.onGroupExpandState(false);
            }
        }
    }

    public void setSelectGroup(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < internalGetGroupCount()) {
            this.selectGroupPosition = groupPosition;
            expandGroup(groupPosition);
        }
    }

    public boolean isExpandAll() {
        for (Boolean anExpandedArray : expandedArray) {
            if (!anExpandedArray) {
                return false;
            }
        }

        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_GROUP:
                return onCreateGroupViewHolder(parent, viewType);
            case VIEW_TYPE_CHILD:
                return onCreateChildViewHolder(parent, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderGroup) {
            groupPosition = getGroupPositionForAdapterPosition(position);
            onBindGroupViewHolder((ViewHolderGroup) holder, groupPosition, expandedArray[groupPosition]);
        } else if (holder instanceof ViewHolderChild) {
            groupPosition = getGroupPositionForAdapterPosition(position);
            childPosition = getChildPositionInGroupPositionForAdapterPosition(position);

            boolean isLastChild = (childPosition == getChildrenCount(groupPosition) - 1);
            onBindChildViewHolder((ViewHolderChild) holder, groupPosition, childPosition, isLastChild);
        }
    }

    @Override
    public int getItemCount() {
        if (expandedArray == null) {
            expandedArray = new Boolean[getGroupCount()];

            for (int i = 0; i < expandedArray.length; i++) {
                if (isDefaultExpandAll) {
                    expandedArray[i] = true;
                } else if (selectGroupPosition == i) {
                    expandedArray[i] = true;

                    if (onGroupExpandListener != null) {
                        onGroupExpandListener.onGroupExpand(i);
                    }
                } else {
                    expandedArray[i] = false;
                }
            }
        }

        int count = 0;
        for (int i = 0; i < internalGetGroupCount(); i++) {
            if (expandedArray[i]) {
                count += internalGetChildCountForGroupPosition(i);
            }

            count++; // for the header view
        }

        itemCount = count;
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isSectionHeader(position)) {
            return VIEW_TYPE_GROUP;
        } else {
            return VIEW_TYPE_CHILD;
        }
    }

    public int getAdapterPositionForGroupPosition(int groupPosition) {
        int childCount = 0;
        int groupCount = 0;
        for (int i = 0; i < internalGetGroupCount(); i++) {
            if (groupPosition == 0) {
                return 0;
            } else if (i == groupPosition) {
                return childCount + groupCount;
            } else if (i < groupPosition) {
                if (expandedArray[i]) {
                    childCount += internalGetChildCountForGroupPosition(i);
                }

                groupCount++;
            }
        }

        return 0;
    }

    public int getChildPositionInGroupPositionForAdapterPosition(int position) {
        int groupPositionStart = 0;
        for (int i = 0; i < internalGetGroupCount(); i++) {
            int childCount = 0;
            if (expandedArray[i]) {
                childCount = internalGetChildCountForGroupPosition(i);
            }

            int groupPositionEnd = groupPositionStart + childCount + 1;
            if (position >= groupPositionStart && position < groupPositionEnd) {
                int childPositionInGroupPosition = position - groupPositionStart - 1;
                mSectionPositionCache.put(position, childPositionInGroupPosition);
                return childPositionInGroupPosition;
            }

            groupPositionStart = groupPositionEnd;
        }

        return 0;
    }

    public final int getGroupPositionForAdapterPosition(int position) {
        int groupPositionStart = 0;
        for (int i = 0; i < internalGetGroupCount(); i++) {
            int childCount = 0;
            if (expandedArray[i]) {
                childCount = internalGetChildCountForGroupPosition(i);
            }

            int groupPositionEnd = groupPositionStart + childCount + 1;
            if (position >= groupPositionStart && position < groupPositionEnd) {
                mSectionCache.put(position, i);
                return i;
            }

            groupPositionStart = groupPositionEnd;
        }

        return 0;
    }

    public boolean isSectionHeader(int position) {
        int groupPositionStart = 0;
        for (int i = 0; i < internalGetGroupCount(); i++) {
            if (position == groupPositionStart) {
                return true;
            } else if (position < groupPositionStart) {
                return false;
            }

            if (expandedArray[i]) {
                groupPositionStart += internalGetChildCountForGroupPosition(i) + 1;
            } else {
                groupPositionStart++;
            }
        }

        return false;
    }

    private int internalGetChildCountForGroupPosition(int groupPosition) {
        int cachedChildCount = childCountArrayList.get(groupPosition);
        if (cachedChildCount > 0) {
            return cachedChildCount;
        }

        int childCount = getChildrenCount(groupPosition);
        childCountArrayList.put(groupPosition, childCount);
        return childCount;
    }

    private int internalGetGroupCount() {
        return getGroupCount();
    }

    public abstract class ViewHolderGroup extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolderGroup(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int groupPosition = getGroupPositionForAdapterPosition(getAdapterPosition());
            boolean isExpanded = expandedArray[groupPosition];
            if (isExpanded) {
                expandedArray[groupPosition] = false;
                if (onGroupCollapseListener != null) {
                    onGroupCollapseListener.onGroupCollapse(groupPosition);
                }

                if (onGroupExpandStateListener != null) {
                    onGroupExpandStateListener.onGroupExpandState(false);
                }

                if (isExpandable) {
                    notifyItemRangeRemoved(getAdapterPosition() + 1, getChildrenCount(groupPosition));
                }
            } else {
                expandedArray[groupPosition] = true;
                if (onGroupExpandListener != null) {
                    onGroupExpandListener.onGroupExpand(groupPosition);
                }

                if (onGroupExpandStateListener != null) {
                    onGroupExpandStateListener.onGroupExpandState(isExpandAll());
                }

                if (isExpandable) {
                    notifyItemRangeInserted(getAdapterPosition() + 1, getChildrenCount(groupPosition));
                }
            }

            if (onGroupItemClickListener != null) {
                onGroupItemClickListener.onGroupClick(v, groupPosition, !isExpanded);
            }
        }
    }

    public abstract class ViewHolderChild extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolderChild(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int groupPosition = getGroupPositionForAdapterPosition(getAdapterPosition());
            int childPosition = getChildPositionInGroupPositionForAdapterPosition(getAdapterPosition());

            if (onChildItemClickListener != null) {
                onChildItemClickListener.onChildClick(v, groupPosition, childPosition);
            }
        }
    }

    public interface OnGroupItemClickListener {
        void onGroupClick(View v, int groupPosition, boolean isExpanded);
    }

    public interface OnChildItemClickListener {
        void onChildClick(View v, int groupPosition, int childPosition);
    }

    public interface OnGroupExpandListener {
        void onGroupExpand(int groupPosition);
    }

    public interface OnGroupCollapseListener {
        void onGroupCollapse(int groupPosition);
    }

    public interface OnGroupExpandStateListener {
        void onGroupExpandState(boolean isGroupExpandAll);
    }
}
