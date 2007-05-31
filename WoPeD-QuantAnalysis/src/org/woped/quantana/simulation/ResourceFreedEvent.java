package org.woped.quantana.simulation;

import java.util.ResourceBundle;
import java.util.logging.Level;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class ResourceFreedEvent extends SimEvent {
	
	private int type = SimEvent.RESOURCE_FREED_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
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
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.ResFreed.Info"), new Object[] {server.getName(), server.getId()});
		
		if (sim.getUseResAlloc() == Simulator.RES_USED){
			server.updateUtilStats(time, sim.getTimeOfLastEvent());

			while ((!(server.getQueue().isEmpty())) && (server.hasFreeCapacity())){
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Queue.Content") + server.printQueue(), new Object[] {server.getName(), server.getId()});
				Case c2 = server.dequeue().get_case();
				Resource r = ru.chooseResourceFromFreeResources(server.getGroup(), server.getRole());
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.ResFreed.Next"), new Object[] {server.getName(), server.getId(), c2.getId(), r.getName()});

				if (r != null){
					ru.useResource(r);
					r.setLastStartTime(time);
					Activity act = new Activity(c2, server, r);
					StartServiceEvent se = new StartServiceEvent(sim, time, act);
					sim.getEventList().add(se);
					protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.StartService") + se.getName() + ENTRY.getString("Sim.Generated.ForCase") + c2.getId() + ENTRY.getString("Sim.StopService.ForServer"), new Object[] {server.getName(), server.getId()});
				}
			}
		}
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Time.LastEvent") + String.format("%,.2f", time));
	}
}
