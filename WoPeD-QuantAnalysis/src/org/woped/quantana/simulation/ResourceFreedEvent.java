package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class ResourceFreedEvent extends SimEvent {
	
	private int type = SimEvent.RESOURCE_FREED_EVENT;
	
	private Server server;
	
	public ResourceFreedEvent(Simulator sim, double time, Server server){
		super(sim, time);
		this.server = server;
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		protocol.info(sim.clckS() + "Server \"" + server.getName() + "(" + server.getId() + ")\" hat eine Ressource freigegeben.");
		
		if (sim.getUseResAlloc() == Simulator.RES_USED){
			server.updateUtilStats(time, sim.getTimeOfLastEvent());

			while ((!(server.getQueue().isEmpty())) && (server.hasFreeCapacity())){
				protocol.info(sim.clckS() + "Inhalt der Warteschlange von Server \"" + server.getName() + "(" + server.getId() + ")\": " + server.printQueue());
				Case c2 = server.dequeue();
				Resource r = ru.chooseResourceFromFreeResources(server.getGroup(), server.getRole());
				protocol.info(sim.clckS() + "Server \"" + server.getName() + "(" + server.getId() + ")\" bedient als nächstes Case # " + c2.getId() + " durch Ressource \"" + r.getName() + "\".");

				if (r != null){
					StartServiceEvent se = new StartServiceEvent(sim, time, server, c2, r);
					sim.getEventList().add(se);
					protocol.info(sim.clckS() + "START_SERVICE_EVENT \"" + se.getName() + "\" für Case # " + c2.getId() + "am Server \"" + server.getName() + "(" + server.getId() + ")\" wurde erzeugt.");
				}
			}
		}
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + "Zeit des letzten Ereignisses ist " + time);
	}
}
