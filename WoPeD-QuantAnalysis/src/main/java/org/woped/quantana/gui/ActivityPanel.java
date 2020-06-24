package org.woped.quantana.gui;

import java.awt.Color;

import javax.swing.JPanel;

import org.woped.quantana.sim.SimCase;
import org.woped.quantana.sim.SimCaseCopy;

public class ActivityPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final int AP_ALPHA = 128;

	private double timeStart;
	private double timeStop;
	private String server;
	private String resource;
	private SimCase _case;
	private Color color;

	public ActivityPanel() {
		super();
	}

	public ActivityPanel(double start, double stop, String server,
			String resource, SimCase _case, Color color) {
		super();
		this.timeStart = start;
		this.timeStop = stop;
		this.server = server;
		this.resource = resource;
		this._case = _case;
		this.color = color;

		this.setBackground(color);

		String st = String.format("%,.2f", timeStart);
		String sp = String.format("%,.2f", timeStop);

		if (_case instanceof SimCaseCopy) {
			SimCaseCopy cc = (SimCaseCopy) _case;
			String ccs = _case.getid() + "(" + cc.getOriginal().getid() + ")";
			this.setToolTipText("Case #" + ccs + "; Start: " + st + ", Stop: "
					+ sp);
		} else {
			this.setToolTipText("Case #" + _case.getid() + "; Start: " + st
					+ ", Stop: " + sp);
		}
	}

	public SimCase get_case() {
		return _case;
	}

	public void set_case(SimCase _case) {
		this._case = _case;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public double getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(double timeStart) {
		this.timeStart = timeStart;
	}

	public double getTimeStop() {
		return timeStop;
	}

	public void setTimeStop(double timeStop) {
		this.timeStop = timeStop;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
