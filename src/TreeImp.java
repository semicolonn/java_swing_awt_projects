import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class TreeImp {
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private JTree tree;

	
    public TreeImp() {
        
    	/*
    	 * tree
    	 *     rootNode
    	 *         treeModel
    	 */
    	
    	rootNode = new DefaultMutableTreeNode("Root");
    	
    	treeModel = new DefaultTreeModel(rootNode);
    	
        treeModel.addTreeModelListener(new MyTreeModelListener());
    	
    	tree = new JTree(treeModel);
    	tree.setEditable(true);
    	tree.getSelectionModel()
    	    .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	tree.setShowsRootHandles(true);
    	
    	// the tree will be added to GUI
    }
    
    
    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    
    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }   
    }

    

    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child);
    }

    
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }
	
        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent, 
                                 parent.getChildCount());
        
        return childNode;
    }
    
    
    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */

                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)(node.getChildAt(index));

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
}
