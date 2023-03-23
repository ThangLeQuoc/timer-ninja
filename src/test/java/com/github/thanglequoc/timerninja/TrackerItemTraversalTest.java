package com.github.thanglequoc.timerninja;

import org.junit.jupiter.api.Test;

public class TrackerItemTraversalTest {

    @Test
    public void testDFSTraversal() {

        /**
         *            A
         *          B   E
         *        C  D
         *          F G H
         *              I
         * */
        TrackerItemNode doA = new TrackerItemNode(
            new TrackerItemContext(0, "doA()")
        );

        TrackerItemNode doB = new TrackerItemNode(
            new TrackerItemContext(1, "doB()")
        );

        TrackerItemNode doC = new TrackerItemNode(
            new TrackerItemContext(2, "doC()")
        );

        TrackerItemNode doD = new TrackerItemNode(
            new TrackerItemContext(2, "doD()")
        );

        TrackerItemNode doE = new TrackerItemNode(
            new TrackerItemContext(1, "doE()")
        );

        TrackerItemNode doF = new TrackerItemNode(
            new TrackerItemContext(3, "doF()")
        );

        TrackerItemNode doG = new TrackerItemNode(
            new TrackerItemContext(3, "doG()")
        );

        TrackerItemNode doH = new TrackerItemNode(
            new TrackerItemContext(3, "doH()")
        );

        TrackerItemNode doI = new TrackerItemNode(
            new TrackerItemContext(4, "doI()")
        );

        // I belongs to H
        doH.addChild(doI);

        // F,G,H belongs to D
        doD.addChild(doF);
        doD.addChild(doG);
        doD.addChild(doH);

        // C,D belongs to B
        doB.addChild(doC);
        doB.addChild(doD);

        // B, E belongs to A, A is root
        doA.addChild(doB);
        doA.addChild(doE);

        // see the DFS in action
        TrackerItemTraversal itemTraversal = new TrackerItemTraversal();
        itemTraversal.dfsPreOrderTraversal(doA);
    }
}
