package com.ofcat.expandablerecyclerviewdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ofcat.expandablerecyclerview.ExpandableRecyclerAdapter;

public class ExpandableDemoRecyclerAdapter extends ExpandableRecyclerAdapter {

    private int[] demoChildCount = {10, 9, 5, 4, 3, 2, 1, 8, 7, 6};

    @Override
    public int getGroupCount() {
        return 10;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return demoChildCount[groupPosition];
    }

    @Override
    public ViewHolderGroup onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);

        return new DemoViewGroupHolder(view);
    }

    @Override
    public ViewHolderChild onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);

        return new DemoViewChildHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(ViewHolderGroup holder, int groupPosition, boolean isExpanded) {
        String text = "Group " + groupPosition;
        if (holder instanceof DemoViewGroupHolder) {
            ((DemoViewGroupHolder) holder).bindTextContent(text);
        }
    }

    @Override
    public void onBindChildViewHolder(ViewHolderChild holder, int groupPosition, int childPosition, boolean isLastChild) {
        String text = "Group " + groupPosition + " Child " + childPosition;
        if (holder instanceof DemoViewChildHolder) {
            ((DemoViewChildHolder) holder).bindTextContent(text);
        }
    }

    class DemoViewGroupHolder extends ViewHolderGroup {

        private TextView group;

        public DemoViewGroupHolder(View itemView) {
            super(itemView);
            group = itemView.findViewById(R.id.tvGroup);
        }

        void bindTextContent(String text) {
            group.setText(text);
        }

    }

    class DemoViewChildHolder extends ViewHolderChild {

        private TextView child;

        public DemoViewChildHolder(View itemView) {
            super(itemView);
            child = itemView.findViewById(R.id.tvChild);
        }

        void bindTextContent(String text) {
            child.setText(text);
        }

    }

}
