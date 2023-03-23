package com.github.thanglequoc.timerninja;

import java.util.ArrayList;
import java.util.List;

public class TrackerItemNode {

    private TrackerItemContext trackerItemContext;
    private List<TrackerItemNode> itemNodeList;

    public TrackerItemNode(TrackerItemContext trackerItemContext) {
        this.trackerItemContext = trackerItemContext;
        itemNodeList = new ArrayList<>();
    }

    public TrackerItemContext getTrackerItemContext() {
        return trackerItemContext;
    }

    public List<TrackerItemNode> getItemNodeList() {
        return itemNodeList;
    }

    public void addChild(TrackerItemNode trackerItemNode) {
        itemNodeList.add(trackerItemNode);
    }

    // TODO @tle 23/3/2023: This might be redundant
    public void removeChild(TrackerItemNode trackerItemNode) {
        itemNodeList.remove(trackerItemNode);
    }

    public boolean isLeafNode() {
        return itemNodeList.isEmpty();
    }
}
