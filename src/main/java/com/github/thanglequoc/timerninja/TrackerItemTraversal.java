package com.github.thanglequoc.timerninja;

public class TrackerItemTraversal {

    public void dfsPreOrderTraversal(TrackerItemNode rootNode) {
        if (rootNode != null) {
            visit(rootNode);
            if (!rootNode.isLeafNode()) {
                for (TrackerItemNode child: rootNode.getItemNodeList()) {
                    dfsPreOrderTraversal(child);
                }
            }
        }
    }

    private void visit(TrackerItemNode node) {
        TrackerItemContext trackerItemContext = node.getTrackerItemContext();
        System.out.println(
            String.format("Pointer depth: %d; Detail: %s",
                trackerItemContext.getPointerDepth(),
                trackerItemContext.getMethodExecutionResult())
        );
    }
}
