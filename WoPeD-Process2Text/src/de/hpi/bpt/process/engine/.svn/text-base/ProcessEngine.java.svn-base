package de.hpi.bpt.process.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * 
 * @author Artem Polyvyanyy
 *
 */
public class ProcessEngine {
	
	public static void execute(IProcess process) throws IOException {
		while (!process.isTerminated()) {
			process.serialize();
			System.out.println("================================================================================");
			System.out.println("Current process state");
			System.out.println(process);
			
			List<Vertex> as = new ArrayList<Vertex>(process.getEnabledElements());
			System.out.println("--------------------------------------------------------------------------------");
			for (int i=0; i<as.size(); i++)
				System.out.println(String.format("%1s. %1s, %1s", i+1, as.get(i).getName(), as.get(i).getDescription()));
			System.out.println("--------------------------------------------------------------------------------");
			
			InputStreamReader isr = new InputStreamReader(System.in);
		    BufferedReader stdin = new BufferedReader(isr);
		    System.out.print("Select an element to fire: ");
		    String input = stdin.readLine();
		    int option = Integer.parseInt(input);
		    
		    process.fire(as.get(option-1));
		}
		
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println(process);
		process.serialize();
		System.out.println("Process terminated");
	}
}
