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

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.List;


public class TreeItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchListener mTouchListener;
    private int mOriginalPosition = -1;
    private int mDraggingPosition = -1;
    private int mFromPosition = -1;
    private int mToPosition = -1;
    private RecyclerView.ViewHolder mTargetHolder;
    private HolderDecorator mHolderDecorator;

    public TreeItemTouchHelperCallback(@NonNull ItemTouchListener touchListener) {
        mTouchListener = touchListener;
        mHolderDecorator = new FakeHolderDecorator();
    }

    public void setHolderDecorator(@NonNull HolderDecorator holderDecorator) {
        mHolderDecorator = holderDecorator;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mTouchListener.onDragging(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        resetOverlappedHolder();

        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        mFromPosition = fromPos;
        mToPosition = toPos;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mHolderDecorator.resetDraggingHolder(viewHolder);
    }

    @Override
    @CallSuper
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        switch (actionState) {
            case ItemTouchHelper.ACTION_STATE_IDLE:
//                Timber.tag("qwe").d("Original position %s , mDraggin posotion %s, mfromposition %s, mToposition %s, holder != null %s", mOriginalPosition, mDraggingPosition, mFromPosition, mToPosition, mTargetHolder != null);
                Log.d("TreeView", String.format("Original position %s , mDraggin posotion %s, mfromposition %s, mToposition %s, holder != null %s", mOriginalPosition, mDraggingPosition, mFromPosition, mToPosition, mTargetHolder != null));
                if (mTargetHolder == null && mFromPosition != -1 && mOriginalPosition != mToPosition && mFromPosition != mToPosition) {
                    mTouchListener.onDragFinished(mToPosition);
                }

                if (mTargetHolder != null) {
                    mTouchListener.onMovedInto(mDraggingPosition,  mTargetHolder.getAdapterPosition());
                    resetOverlappedHolder();
                }

                mOriginalPosition = -1;
                mFromPosition = -1;
                mToPosition = -1;
                mDraggingPosition = -1;

                break;

            case ItemTouchHelper.ACTION_STATE_DRAG:
                mHolderDecorator.decorateDraggingHolder(viewHolder);

                break;

            default:
                // do nothing
        }
     }

    @Override
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
        final int dy = curY - selected.itemView.getTop();
        int bottom = curY + selected.itemView.getHeight();
        if (mOriginalPosition == -1) {
            mOriginalPosition = selected.getAdapterPosition();
        }

        mDraggingPosition = selected.getAdapterPosition();
        for (int i = 0; i < dropTargets.size(); i++) {
            RecyclerView.ViewHolder target = dropTargets.get(i);
            int delta = target.itemView.getHeight() / 3;
            if (dy < 0) {
                int diff = target.itemView.getTop() - curY;
//                Timber.d("Delta %s diff %s", delta, diff);
                if (mTargetHolder == null && Math.abs(diff) <= delta) {
//                    Timber.tag("qwe").d("!!!DROP INSIDE");
                    mTargetHolder = target;
                    mHolderDecorator.decorateOverlappedHolder(mTargetHolder);
                } else if (mTargetHolder != null && Math.abs(diff) > delta) {
                    resetOverlappedHolder();
                }
            }

            if (dy > 0) {
                int diff = target.itemView.getBottom() - bottom;
//                Timber.d("Delta %s diff %s", delta, diff);
                if (mTargetHolder == null && Math.abs(diff) <= delta) {
//                    Timber.tag("qwe").d("!!!DROP INSIDE");
                    mTargetHolder = target;
                    mHolderDecorator.decorateOverlappedHolder(mTargetHolder);
                } else if (mTargetHolder != null && Math.abs(diff) > delta) {
                    resetOverlappedHolder();
                }
            }
        }

        return super.chooseDropTarget(selected, dropTargets, curX, curY);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // do nothing
    }

    private void resetOverlappedHolder() {
//        Timber.tag("qwe").d("!!! RESET HOLDER");
        if (mTargetHolder != null) {
            mHolderDecorator.resetOverlappedHolder(mTargetHolder);
            mTargetHolder = null;
        }
    }

}
