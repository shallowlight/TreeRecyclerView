package com.github.ymka.example.treerecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.ymka.treerecyclerview.ItemTouchListener;
import com.github.ymka.treerecyclerview.TreeItemTouchHelperCallback;
import com.github.ymka.treerecyclerview.TreeNode;
import com.github.ymka.treerecyclerview.TreeNodeListManager;


public class MainActivity extends AppCompatActivity implements ItemTouchListener {

    private TreeNodeListManager<String> mListManager;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListManager = new TreeNodeListManager<>(generateFakeTreeNode());
        TreeNodeListAdapter adapter = new TreeNodeListAdapter(this, mListManager);
        mListManager.init();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        TreeItemTouchHelperCallback helperCallback = new TreeItemTouchHelperCallback(this);
        helperCallback.setHolderDecorator(new ViewHolderDecorator(this));
        mItemTouchHelper = new ItemTouchHelper(helperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private TreeNode<String> generateFakeTreeNode() {
        TreeNode<String> root = TreeNode.createRootNode();
        TreeNode<String> child1 = new TreeNode<>("Child1");
        root.addChild(child1);
        TreeNode<String> subchild11 = new TreeNode<>("SubChild11");
        child1.addChild(subchild11);
        TreeNode<String> subchild111 = new TreeNode<>("SubChild111");
        subchild11.addChild(subchild111);
        TreeNode<String> subchild12 = new TreeNode<>("SubChild12");
        child1.addChild(subchild12);
        TreeNode<String> subchild13 = new TreeNode<>("SubChild13");
        child1.addChild(subchild13);
        TreeNode<String> child2 = new TreeNode<>("Child2");
        root.addChild(child2);
        child2.setIsExpanded(false);
        TreeNode<String> subchild21 = new TreeNode<>("SubChild21");
        child2.addChild(subchild21);
        TreeNode<String> subchild22 = new TreeNode<>("SubChild22");
        child2.addChild(subchild22);
        TreeNode<String> subchild23 = new TreeNode<>("SubChild23");
        child2.addChild(subchild23);
        TreeNode<String> child3 = new TreeNode<>("Child3");
        root.addChild(child3);

        return root;
    }

    @Override
    public void onDragging(int fromPosition, int toPosition) {
        mListManager.movingNode(fromPosition, toPosition);
    }

    @Override
    public void onDragFinished(int position) {
        mListManager.movedNode(position);
    }

    @Override
    public void onMovedInto(int position, int positionIn) {
        mListManager.moveInside(position, positionIn);
    }
}
