/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl.wfnet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Chris
 */
public class AutoLayout {

    private WfNet wfNet;
    private int left = 50, top = 100, cell_width = 70;

    public AutoLayout(WfNet wfNet) {
        this.wfNet = wfNet;
    }
    //private List<Shape> shapes;
    private HashMap<String, Integer> handledDict = new HashMap<String, Integer>();
    ArrayList<ArrayList<WfNetNode>> Columns = new ArrayList<ArrayList<WfNetNode>>();

    public void Layout() {
        //Columns.Add(new List<Shape>());
        ArrayList<WfNetNode> nodes = new ArrayList<WfNetNode>(wfNet.getNodes());

        for (int i = 0; i < nodes.size(); i++) {
            WfNetNode node = nodes.get(i);
            LayoutHelper(node, 0);
        }

        AdjustColumnIndices();

        CreateGrid();

        SetCoordinates();
    }

    void AdjustColumnIndices() {
        int min_index = 999999;


        for (int col_idx : handledDict.values()) {
            min_index = (min_index <= col_idx)
                    ? min_index : col_idx;
        }

        ArrayList<String> keys = new ArrayList<String>(handledDict.keySet());
        for (String key : keys) {
            handledDict.put(key, handledDict.get(key) - min_index);
            //handledDict[key] -= min_index;
        }
    }

    void CreateGrid() {
        for (String id : handledDict.keySet()) {
            int col_index = handledDict.get(id);
            while (col_index >= Columns.size()) {
                Columns.add(new ArrayList<WfNetNode>());
            }
            Columns.get(col_index).add(wfNet.findNode(id));
        }
    }

    void SetCoordinates() {
        for (int iCol = 0; iCol < Columns.size(); iCol++) {
            ArrayList<WfNetNode> col = Columns.get(iCol);
            for (int iShape = 0; iShape < col.size(); iShape++) {
                WfNetNode node = col.get(iShape);

                int x = left + iCol * cell_width;
                int y = top + iShape * cell_width;


                node.moveTo(x, y);
            }
        }
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="sh"></param>
    /// <param name="cur_col_index"></param>
    /// <returns>Column index</returns>
    int LayoutHelper(WfNetNode sh, int cur_col_index) {
        if (HasBeenHandled(sh)) {
            return GetColumnIndex(sh);
        }

        // preliminarily mark the shape as handled to prevent
        // infinite recursion
        SetAsHandled(sh, cur_col_index);

        if (sh.getInputNodes().size() > 0) {
            // recursively go backwards
            for (WfNetNode pred : sh.getInputNodes()) {
                int pidx = LayoutHelper(pred, cur_col_index - 1);
                cur_col_index = (cur_col_index >= pidx)
                        ? cur_col_index : pidx;
            }
            cur_col_index++;
        }

        SetAsHandled(sh, cur_col_index);
        return cur_col_index;

    }

    private boolean HasBeenHandled(WfNetNode sh) {
        return handledDict.containsKey(sh.getId());
    }

    private int GetColumnIndex(WfNetNode sh) {

        return handledDict.get(sh.getId());
    }

    private void SetAsHandled(WfNetNode sh, int col_index) {
        if (!handledDict.containsKey(sh.getId())) {
            handledDict.put(sh.getId(), col_index);
        } else {
            handledDict.put(sh.getId(), col_index);
        }
    }
}
