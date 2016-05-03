/* Copyright 2016 Alexander Kondenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ymka.treerecyclerview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

public abstract class BaseTreeNodeListAdapter<H extends BaseItemViewHolder, T> extends RecyclerView.Adapter<H> implements TreeNodeListManager.OnTreeChangeListener,
                                                                                                BaseItemViewHolder.ActionItemListener {

    protected final Context mContext;
    private final TreeNodeListManager<T> mTreeNodeListManager;

    public BaseTreeNodeListAdapter(Context context, TreeNodeListManager<T> treeNodeListManager) {
        mContext = context;
        mTreeNodeListManager = treeNodeListManager;
        mTreeNodeListManager.setListener(this);
    }

    public TreeNode<T> getItemByPosition(int position) {
        return mTreeNodeListManager.getItemByPosition(position);
    }

    @Override
    public int getItemCount() {
        return mTreeNodeListManager.getItemCount();
    }

    @Override
    public void onTreeListUpdated() {
        notifyDataSetChanged();
    }

    @Override
    public void onNodeExpanded(int position, int count) {
        notifyItemRangeInserted(position, count);
    }

    @Override
    public void onNodeCollapsed(int position, int count) {
        notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onCollapseOrExpand(int position) {
        mTreeNodeListManager.expandOrCollapseNode(position);
    }

    @Override
    public void onCollapse(int position) {
        mTreeNodeListManager.collapseNode(position);
    }

    @Override
    public void onStartDrag(BaseItemViewHolder itemViewHolder) {

    }

    @Override
    public void onMoveNode(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onMovedNode(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void onMovedInsideNode(int nodePosition) {
        notifyDataSetChanged();
    }
}
