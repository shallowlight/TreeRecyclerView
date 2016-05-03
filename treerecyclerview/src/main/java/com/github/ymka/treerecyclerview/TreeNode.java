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
import java.util.List;

public class TreeNode<T> {

    private TreeNode<T> mParent;
    private final List<TreeNode<T>> mChildren = new ArrayList<>();
    private boolean mIsExpanded;
    private int mLevel;
    private T mDate;

    TreeNode() {
        this(true, -1, null);
    }

    public TreeNode(T date) {
        this(false, -1, date);
    }

    public TreeNode(boolean isExpanded, int level, T date) {
        mIsExpanded = isExpanded;
        mLevel = level;
        mDate = date;
    }

    public void addChild(TreeNode<T> child) {
        int level = mLevel + 1;
        child.setLevel(level);
        child.setParent(this);
        mChildren.add(child);
    }

    public void addChildToPosition(int position, TreeNode<T> child) {
        int level = mLevel + 1;
        child.setLevel(level);
        child.setParent(this);
        mChildren.add(position, child);
    }

    public int getChildPosition(TreeNode<T> child) {
        return mChildren.indexOf(child);
    }

    public void removeChild(TreeNode child) {
        mChildren.remove(child);
    }

    protected void setLevel(int level) {
        mLevel = level;
    }

    public int getLevel() {
        return mLevel;
    }

    protected void setParent(TreeNode parent) {
        mParent = parent;
    }

    @NonNull
    public TreeNode<T> getParent() {
        return mParent;
    }

    public List<TreeNode<T>> getChildren() {
        return mChildren;
    }

    public boolean isRoot() {
        return mLevel == -1;
    }

    public void setIsExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public T getDate() {
        return mDate;
    }

    public static <T> TreeNode<T> createRootNode() {
        return new TreeNode<>();
    }

    public List<TreeNode<T>> toList() {
        List<TreeNode<T>> treeNodeList = new ArrayList<>();
        for (int i = 0; i < mChildren.size(); i++) {
            TreeNode<T> treeNode = mChildren.get(i);
            treeNode.setLevel(mLevel + 1);
            convertToList(treeNodeList, treeNode);
        }

        return treeNodeList;
    }

    private void convertToList(List<TreeNode<T>> list, TreeNode<T> treeNode) {
        list.add(treeNode);
        if (treeNode.isExpanded()) {
            List<TreeNode<T>> children = treeNode.getChildren();
            for (int i = 0; i < children.size(); i++) {
                TreeNode<T> child = children.get(i);
                child.setLevel(treeNode.getLevel() + 1);
                convertToList(list, child);
            }
        }
    }


    public int getExpandedNodesCount() {
        int count = 0;
        if (mIsExpanded) {
            count = mChildren.size();
            for (int i = 0; i < mChildren.size(); i++) {
                count += getVisibleNodeCount(mChildren.get(i));
            }
        }

        return count;
    }

    private int getVisibleNodeCount(TreeNode treeNode) {
        int count = 0;
        if (treeNode.isExpanded()) {
            List<TreeNode> children = treeNode.getChildren();
            count += children.size();
            for (int i = 0; i < children.size(); i++) {
                count += getVisibleNodeCount(children.get(i));
            }
        }

        return count;
    }

}
