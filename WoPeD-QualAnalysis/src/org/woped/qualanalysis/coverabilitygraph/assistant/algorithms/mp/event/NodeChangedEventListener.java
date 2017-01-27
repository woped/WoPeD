package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import java.util.EventListener;

public interface NodeChangedEventListener extends EventListener{

    void NodeChanged(NodeChangedEvent args);
}
