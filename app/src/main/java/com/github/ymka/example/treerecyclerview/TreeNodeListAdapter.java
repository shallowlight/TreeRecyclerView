package com.github.ymka.example.treerecyclerview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.ymka.treerecyclerview.BaseItemViewHolder;
import com.github.ymka.treerecyclerview.BaseTreeNodeListAdapter;
import com.github.ymka.treerecyclerview.TreeNode;
import com.github.ymka.treerecyclerview.TreeNodeListManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import timber.log.Timber;



public class TreeNodeListAdapter extends BaseTreeNodeListAdapter<TreeNodeListAdapter.ItemViewHolder, String> {

    public TreeNodeListAdapter(Context context, TreeNodeListManager<String> treeNodeListManager) {
        super(context, treeNodeListManager);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);

        return new ItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        TreeNode<String> treeNode = getItemByPosition(position);
        holder.mTitle.setText(treeNode.getDate());
        int level = treeNode.getLevel();
        Timber.tag("qwe").d("Position %s, text %s, level %s", position, treeNode.getDate(), level);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setMargins(level * 48, 0, 0, 0);
    }

    public static class ItemViewHolder extends BaseItemViewHolder {

        @Bind(R.id.itemTitle)
        TextView mTitle;

        public ItemViewHolder(View itemView, BaseItemViewHolder.ActionItemListener listener) {
            super(itemView, listener);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.itemContainer})
        void onClickItem(View view) {
            switch (view.getId()) {
                case R.id.itemContainer:
                    collapseOrExapnd(getAdapterPosition());
                    break;

                default:
                    throw new IllegalArgumentException("Need implement this action");
            }
        }

        @OnLongClick(R.id.itemContainer)
        boolean onLongClick() {
            Timber.tag("qwe").d("Long click");
            collapse(getAdapterPosition());

            return true;
        }
    }

}
