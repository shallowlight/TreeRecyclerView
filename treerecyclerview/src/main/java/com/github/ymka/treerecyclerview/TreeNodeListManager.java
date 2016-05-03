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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNodeListManager<T> {

    private final TreeNode<T> mRoot;
    private OnTreeChangeListener mListener;
    private List<TreeNode<T>> mListNode;

    public TreeNodeListManager(TreeNode<T> root) {
        mRoot = root;
        mListener = new FakeTreeChangeListener();
        mListNode = new ArrayList<>(0);
    }

    void setListener(@NonNull OnTreeChangeListener listener) {
        mListener = listener;
    }

    public void init() {
        mListNode = mRoot.toList();
        mListener.onTreeListUpdated();
    }

    public TreeNode<T> getItemByPosition(int position) {
        return mListNode.get(position);
    }

    public int getItemCount() {
        return mListNode.size();
    }

    public void movingNode(int fromPosition, int toPosition) {
        Collections.swap(mListNode, fromPosition, toPosition);
        mListener.onMoveNode(fromPosition, toPosition);
    }

    public void movedNode(int position) {
        TreeNode<T> node = mListNode.get(position);
        node.getParent().removeChild(node);
        if (position == 0) {
            TreeNode<T> nextNode = mListNode.get(position + 1);
            TreeNode<T> parent = nextNode.getParent();
            parent.addChildToPosition(position, node);
        } else if (position == mListNode.size() - 1){
            TreeNode<T> previousNode = mListNode.get(position - 1);
            TreeNode<T> parent = previousNode.getParent();
            parent.addChild(node);
        } else {
            TreeNode<T> previousNode = mListNode.get(position - 1);
            TreeNode<T> previousNodeParent = previousNode.getParent();
            TreeNode<T> nextNode = mListNode.get(position + 1);
            TreeNode<T> nextNodeParent = nextNode.getParent();
            int previousNodeParentChildPosition = previousNodeParent.getChildPosition(previousNode);
            if (previousNodeParent.equals(nextNodeParent)) {
                previousNodeParent.addChildToPosition(previousNodeParentChildPosition + 1, node);
            } else {
                if (previousNode.equals(nextNodeParent)) {
                    previousNode.addChildToPosition(0, node);
                } else {
                    previousNodeParent.addChild(node);
                }
            }
        }

        mListener.onMovedNode(position);
    }

    public void moveInside(int nodePosition, int insertedPosition) {
       moveInside(nodePosition, insertedPosition, true);
    }

    public void moveInside(int nodePosition, int insertedPosition, boolean inBegin) {
        TreeNode<T> parent = mListNode.get(insertedPosition);
        TreeNode<T> child = mListNode.get(nodePosition);
        if (child.getParent().equals(parent)) {
            mListNode.remove(nodePosition);
            mListNode.add(insertedPosition + 1, child);
            mListener.onMovedInsideNode(nodePosition);

            return;
        }

        child.getParent().removeChild(child);
        if (parent.isExpanded() && !parent.getChildren().isEmpty()) {
            mListNode.add(insertedPosition + 1, child);
            parent.addChildToPosition(0, child);
        } else {
            if (inBegin) {
                parent.addChildToPosition(0, child);
            } else {
                parent.addChild(child);
            }

            if (parent.isExpanded()) {
                parent.setIsExpanded(false);
            }
        }

        mListNode.remove(nodePosition);
        mListener.onMovedInsideNode(nodePosition);
    }

    public void expandOrCollapseNode(int position) {
        TreeNode<T> treeNode = mListNode.get(position);
        if (treeNode.isExpanded()) {
            collapseNode(position);
        } else {
            int start = position + 1;
            treeNode.setIsExpanded(true);
            List<TreeNode<T>> list = treeNode.toList();
            mListNode.addAll(start, list);
            mListener.onNodeExpanded(start, list.size());
        }
    }

    public void collapseNode(int position) {
        int start = position + 1;
        TreeNode<T> treeNode = mListNode.get(position);
        if (treeNode.isExpanded()) {
            int end = treeNode.getExpandedNodesCount();
            treeNode.setIsExpanded(false);
            mListNode.subList(start, start + end).clear();
            mListener.onNodeCollapsed(start, end);
        }
    }

    interface OnTreeChangeListener {
        void onTreeListUpdated();
        void onNodeExpanded(int position, int count);
        void onNodeCollapsed(int position, int count);
        void onMoveNode(int fromPosition, int toPosition);
        void onMovedNode(int position);
        void onMovedInsideNode(int nodePosition);
    }

    private static class FakeTreeChangeListener implements OnTreeChangeListener {

        @Override
        public void onTreeListUpdated() {

        }

        @Override
        public void onNodeExpanded(int position, int count) {

        }

        @Override
        public void onNodeCollapsed(int position, int count) {

        }

        @Override
        public void onMoveNode(int fromPosition, int toPosition) {

        }

        @Override
        public void onMovedNode(int position) {

        }

        @Override
        public void onMovedInsideNode(int nodePosition) {

        }
    }

}
