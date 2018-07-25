package preprocessing;

import de.hpi.bpt.process.Process;
import ee.ut.bpstruct2.Restructurer;

public class RigidStructurer {
    public Process structureProcess(Process p) {
        int count = 0;
        for (de.hpi.bpt.process.Gateway gw : p.getGateways())
            if (gw.getName().isEmpty())
                gw.setName("gw" + count++);

        Restructurer str = new Restructurer(p);

        if (str.perform()) {
            System.out.println("Process successfully structured");
            return str.proc;
        } else {
            System.out.println("WARNING: Process cannot be structured");
            return null;
        }
    }
}