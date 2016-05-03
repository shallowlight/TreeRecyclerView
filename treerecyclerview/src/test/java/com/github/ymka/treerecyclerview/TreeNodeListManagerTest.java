package com.github.ymka.treerecyclerview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TreeNodeListManagerTest {

    private TreeNodeListManager<String> mNodeListManager;

    @Mock
    TreeNodeListManager.OnTreeChangeListener mOnTreeChangeListener;

    @Before
    public void createTreeNodeListManager() {
        mNodeListManager = new TreeNodeListManager<>(generateFakeTreeNode());
        mOnTreeChangeListener = mock(TreeNodeListManager.OnTreeChangeListener.class);
        mNodeListManager.setListener(mOnTreeChangeListener);
        mNodeListManager.init();
    }

    @Test
    public void testInit() {
        verify(mOnTreeChangeListener, times(1)).onTreeListUpdated();
        assertEquals(mNodeListManager.getItemCount(), 6);
    }

    @Test
    public void testGetItemByPosition() {
        TreeNode<String> node = mNodeListManager.getItemByPosition(3);
        assertEquals(node.getDate(), "1.3");
    }

    @Test
    public void testGetItemCount() {
        assertEquals(mNodeListManager.getItemCount(), 6);
    }

    @Test
    public void testMovingNode() {
        mNodeListManager.movingNode(1, 2);
        verify(mOnTreeChangeListener, times(1)).onMoveNode(1, 2);
        TreeNode<String> node1 = mNodeListManager.getItemByPosition(1);
        TreeNode<String> node2 = mNodeListManager.getItemByPosition(2);
        assertEquals(node1.getDate(), "1.2");
        assertEquals(node2.getDate(), "1.1");
    }

    @Test
    public void testMovedNodeInTheStart() {
        mNodeListManager.movedNode(0);
        TreeNode<String> node = mNodeListManager.getItemByPosition(0);
        TreeNode<String> nextNode = mNodeListManager.getItemByPosition(1);
        assertEquals(node.getParent(), nextNode.getParent());
        verify(mOnTreeChangeListener, times(1)).onMovedNode(0);
    }

    @Test
    public void testMovedNodeInTheEnd() {
        int lastNodePosition = mNodeListManager.getItemCount() - 1;
        mNodeListManager.movedNode(lastNodePosition);
        TreeNode<String> node = mNodeListManager.getItemByPosition(lastNodePosition);
        TreeNode<String> previousNode = mNodeListManager.getItemByPosition(lastNodePosition - 1);
        assertEquals(node.getParent(), previousNode.getParent());
        verify(mOnTreeChangeListener, times(1)).onMovedNode(lastNodePosition);
    }

    @Test
    public void testMovedNodeInTheStartOfParentChildren() {
        TreeNode<String> parent = mNodeListManager.getItemByPosition(0);
        mNodeListManager.movingNode(5, 1);
        mNodeListManager.movedNode(1);
        TreeNode<String> child = mNodeListManager.getItemByPosition(1);
        assertEquals(child.getParent(), parent);
        verify(mOnTreeChangeListener, times(1)).onMovedNode(1);
    }

    @Test
    public void testMovedNodeInTheMiddleOfParentChildren() {
        TreeNode<String> parent = mNodeListManager.getItemByPosition(0);
        mNodeListManager.movingNode(5, 2);
        mNodeListManager.movedNode(2);
        TreeNode<String> child = mNodeListManager.getItemByPosition(1);
        assertEquals(child.getParent(), parent);
        verify(mOnTreeChangeListener, times(1)).onMovedNode(2);
    }

    @Test
    public void testMovedNodeInTheEndOfParentChildren() {
        TreeNode<String> parent = mNodeListManager.getItemByPosition(0);
        mNodeListManager.movingNode(5, 3);
        mNodeListManager.movedNode(3);
        TreeNode<String> child = mNodeListManager.getItemByPosition(1);
        assertEquals(child.getParent(), parent);
        verify(mOnTreeChangeListener, times(1)).onMovedNode(3);
    }

    @Test
    public void testMoveInsideInTheStartCollapsedNode() {
        TreeNode<String> movingNode = mNodeListManager.getItemByPosition(5);
        mNodeListManager.moveInside(5, 4);
        assertEquals(mNodeListManager.getItemCount(), 5);
        verify(mOnTreeChangeListener, times(1)).onMovedInsideNode(5);
        mNodeListManager.expandOrCollapseNode(4);
        TreeNode<String> movedNode = mNodeListManager.getItemByPosition(5);
        assertEquals(movingNode, movedNode);
    }

    @Test
    public void testMoveInsideInTheEndCollapsedNode() {
        TreeNode<String> movingNode = mNodeListManager.getItemByPosition(5);
        mNodeListManager.moveInside(5, 4, false);
        assertEquals(mNodeListManager.getItemCount(), 5);
        verify(mOnTreeChangeListener, times(1)).onMovedInsideNode(5);
        mNodeListManager.expandOrCollapseNode(4);
        TreeNode<String> movedNode = mNodeListManager.getItemByPosition(8);
        assertEquals(movingNode, movedNode);
    }

    @Test
    public void testMoveInsideExpandedNode() {
        TreeNode<String> movingNode = mNodeListManager.getItemByPosition(4);
        mNodeListManager.moveInside(4, 0);
        assertEquals(mNodeListManager.getItemCount(), 6);
        verify(mOnTreeChangeListener, times(1)).onMovedInsideNode(4);
        mNodeListManager.expandOrCollapseNode(4);
        TreeNode<String> movedNode = mNodeListManager.getItemByPosition(1);
        assertEquals(movingNode, movedNode);
    }


    @Test
    public void testExpandOrCollapseExpandWay() {
        int position = 1;
        mNodeListManager.expandOrCollapseNode(position);
        List<TreeNode<String>> nodeList = Arrays.asList(new TreeNode<>("1"), new TreeNode<>("1.1"), new TreeNode<>("1.1.1"), new TreeNode<>("1.2"), new TreeNode<>("1.3"),new TreeNode<>("2"), new TreeNode<>("3"));
        assertEquals(mNodeListManager.getItemCount(), nodeList.size());
        for (int i = 0; i < nodeList.size(); i++) {
            TreeNode<String> originalNode = mNodeListManager.getItemByPosition(i);
            TreeNode<String> verificationNode = nodeList.get(i);
            assertEquals(originalNode.getDate(), verificationNode.getDate());
        }

        verify(mOnTreeChangeListener, times(1)).onNodeExpanded(position + 1, 1);
    }

    @Test
    public void testExpandOrCollapseCollapseWay() {
        testCollapsing();
    }

    private void testCollapsing() {
        int position = 0;
        mNodeListManager.expandOrCollapseNode(position);
        List<TreeNode<String>> nodeList = Arrays.asList(new TreeNode<>("1"), new TreeNode<>("2"), new TreeNode<>("3"));
        assertEquals(mNodeListManager.getItemCount(), nodeList.size());
        for (int i = 0; i < nodeList.size(); i++) {
            TreeNode<String> originalNode = mNodeListManager.getItemByPosition(i);
            TreeNode<String> verificationNode = nodeList.get(i);
            assertEquals(originalNode.getDate(), verificationNode.getDate());
        }

        verify(mOnTreeChangeListener, times(1)).onNodeCollapsed(position + 1, 3);
    }

    @Test
    public void testCollapseExpandedNode() {
        testCollapsing();
    }

    @Test
    public void testCollapseCollapsedNode() {
        mNodeListManager.collapseNode(3);
        verify(mOnTreeChangeListener, never()).onNodeCollapsed(anyInt(), anyInt());

    }

    private TreeNode<String> generateFakeTreeNode() {
        TreeNode<String> root = TreeNode.createRootNode();
        TreeNode<String> child1 = new TreeNode<>("1");
        root.addChild(child1);
        child1.setIsExpanded(true);
        TreeNode<String> subchild11 = new TreeNode<>("1.1");
        child1.addChild(subchild11);
        TreeNode<String> subchild111 = new TreeNode<>("1.1.1");
        subchild11.addChild(subchild111);
        TreeNode<String> subchild12 = new TreeNode<>("1.2");
        child1.addChild(subchild12);
        TreeNode<String> subchild13 = new TreeNode<>("1.3");
        child1.addChild(subchild13);
        TreeNode<String> child2 = new TreeNode<>("2");
        root.addChild(child2);
        TreeNode<String> subchild21 = new TreeNode<>("2.1");
        child2.addChild(subchild21);
        TreeNode<String> subchild22 = new TreeNode<>("2.2");
        child2.addChild(subchild22);
        TreeNode<String> subchild23 = new TreeNode<>("2.3");
        child2.addChild(subchild23);
        TreeNode<String> child3 = new TreeNode<>("3");
        root.addChild(child3);

        return root;
    }

}
