/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package orgChart;

import java.util.List;
import java.util.LinkedList;

import nodes.ProcessEdge;
import nodes.ProcessNode;
import models.ProcessModel;
import processing.ProcessUtils;

public class OrgChartUtils extends ProcessUtils {

    @Override
    public ProcessEdge createDefaultEdge(ProcessNode source, ProcessNode target) {
        return new Connection(source, target);
    }

    @Override
    public List<Class<? extends ProcessNode>> getNextNodesRecommendation(
            ProcessModel model, ProcessNode node) {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        if (node instanceof OrgUnit) {
            result.add(OrgUnit.class);
            result.add(ManagerialRole.class);
            result.add(Role.class);
        }
        if (node instanceof ManagerialRole | node instanceof Role) {
            result.add(Person.class);
            result.add(Substitute.class);
        }

        return result;
    }
}
