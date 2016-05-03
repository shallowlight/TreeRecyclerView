package com.github.ymka.treerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RecyclerView.ViewHolder.class})
public class TreeItemTouchHelperCallbackTest {

    private TreeItemTouchHelperCallback mHelperCallback;

    @Mock
    ItemTouchListener mTouchListenerMock;
    @Mock
    HolderDecorator mHolderDecorator;

    @Before
    public void createTreeItemTouchHelperCallback() {
        mTouchListenerMock = PowerMockito.mock(ItemTouchListener.class);
        mHolderDecorator = mock(HolderDecorator.class);
        mHelperCallback = new TreeItemTouchHelperCallback(mTouchListenerMock);
        mHelperCallback.setHolderDecorator(mHolderDecorator);
    }

    @Test
    public void onMoveTest() {
        PowerMockito.mockStatic(RecyclerView.ViewHolder.class);
        RecyclerView.ViewHolder viewHolder = PowerMockito.mock(RecyclerView.ViewHolder.class);
        PowerMockito.when(viewHolder.getAdapterPosition()).thenReturn(0);
        RecyclerView.ViewHolder target = PowerMockito.mock(RecyclerView.ViewHolder.class);
        PowerMockito.when(target.getAdapterPosition()).thenReturn(1);
        mHelperCallback.onMove(mock(RecyclerView.class), viewHolder, target);
        verify(mTouchListenerMock, times(1)).onDragging(0, 1);
        verify(mHolderDecorator, never()).resetOverlappedHolder(any(RecyclerView.ViewHolder.class));
    }

    @Test
    public void clearViewTest() {
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(mock(View.class)) {};
        mHelperCallback.clearView(mock(RecyclerView.class), viewHolder);
        verify(mHolderDecorator, times(1)).resetDraggingHolder(viewHolder);
    }

}
