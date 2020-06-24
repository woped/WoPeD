package org.woped.quantana.dashboard.storage;

import org.woped.quantana.resourcealloc.ResourceUtilization;
import org.woped.quantana.sim.SimCase;
import org.woped.quantana.sim.SimGraph;
import org.woped.quantana.sim.SimParameters;
import org.woped.quantana.sim.SimRunStats;
import org.woped.quantana.sim.SimRunner;

public class SimRunnerDB extends SimRunner {

	private StorageEngine storageengine = null;
	
	public SimRunnerDB(SimGraph graph, ResourceUtilization resUtil,
			SimParameters sp, StorageEngine se) {
		
		super(graph, resUtil, sp);
		
		
		/*
		super.graph = graph;
		super.resUtil = resUtil;		
		super.resAlloc = this.resUtil.getResAlloc();
		super.params = sp;
		*/
		se.setResAlloc(resAlloc);
		se.setServerList(serverList);
		
		
		this.storageengine = se;
		
		
		//createServerForBirth();	
	}

	@Override
	public void finishCase(SimCase c) {
		
		super.finishCase(c);
	}

	@Override
	protected SimRunStats finishRun() {
		
		SimRunStats srs = super.finishRun();
		
		if(storageengine!= null){
			storageengine.add(srs);
		}
		
		return srs;
	}

	@Override
	protected void generateReport() {
		
		super.generateReport();
		
		storageengine.storeAllocationMatrix();
		storageengine.InsertImage();
	}

	
}
