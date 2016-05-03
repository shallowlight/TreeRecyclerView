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

package com.github.ymka.example.treerecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import com.github.ymka.treerecyclerview.HolderDecorator;


public class ViewHolderDecorator implements HolderDecorator {

    private final float mDragShadow;

    public ViewHolderDecorator(Context context) {
        mDragShadow = context.getResources().getDimensionPixelSize(R.dimen.drag_shadow);
    }

    public void decorateDraggingHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.setTranslationZ(viewHolder.itemView, mDragShadow);
        } else {
            CardView cardView = (CardView) viewHolder.itemView;
            Context context = cardView.getContext();
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.drag_background));

        }
    }

    @Override
    public void resetDraggingHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.setTranslationZ(viewHolder.itemView, 0);
        } else {
            CardView cardView = (CardView) viewHolder.itemView;
            Context context = cardView.getContext();
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }

    @Override
    public void decorateOverlappedHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void resetOverlappedHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
    }
}
