package org.woped.quantana.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.gui.translations.Messages;
import org.woped.quantana.model.ServerStatisticsModel;

public class ServerStatisticsDialog extends JDialog {

	private static final long serialVersionUID = 101L;

	private static final int ROW_HEIGHT = 20;

	private static final int MARGIN_TOP = 10;

	private static final int MARGIN_LEFT = 10;

	private static final int MARGIN_RIGHT = 10;

	private static final int DISTANCE = 10;

	private static final int FACTOR = 4;

	private static final int TIME_HEIGHT = 25;

	private static final int TIME_STEPS = 100;

	private QuantitativeSimulationDialog owner;

	private ServerStatisticsModel ssm;

	private JPanel mainPanel;

	private JPanel btnPanel;

	private TextPanel textPanel;

	private GraphPanel graphPanel;

	private JScrollPane scrollPane;

	private JButton btnClose;

	private double clock;

	private int numRows;

	private Font ftServer;

	private Font ftResource;

	private int[] lineOrdinates;

	private int SSD_WIDTH;

	private int SSD_HEIGHT;

	private int textWidth;

	private int graphWidth;

	private int panelHeight;

	public ServerStatisticsDialog(Dialog owner) {
		super(owner, Messages
				.getString("QuantAna.Simulation.ServerStatistics.Title"), true);
		this.owner = (QuantitativeSimulationDialog) owner;
		this.clock = this.owner.getSimulator().getRunClock();
		ssm = new ServerStatisticsModel(this.owner.getTaskAndResource(),
				this.owner.getSimulator().getActPanelList());
		numRows = ssm.getRowNum();
		panelHeight = MARGIN_TOP + numRows * ROW_HEIGHT + TIME_HEIGHT;
		getLineOrdinates();
		this.ftServer = DefaultStaticConfiguration.DEFAULT_TABLE_BOLDFONT;
		this.ftResource = DefaultStaticConfiguration.DEFAULT_TABLE_FONT;
		initialize();	
		pack();	
	}

	private void getLineOrdinates() {
		String[] tasks = ssm.getSortedTasks();
		lineOrdinates = new int[tasks.length];
		for (int i = 0; i < tasks.length; i++) {
			String s = tasks[i];
			int n = ssm.getSortedResources(s).length;
			if (n == 0)
				n = 1;
			if (i == 0)
				lineOrdinates[i] = MARGIN_TOP + n * ROW_HEIGHT - 15;
			else
				lineOrdinates[i] = lineOrdinates[i - 1] + n * ROW_HEIGHT;
		}
	}

	private void initialize() {
		getContentPane().setLayout(new BorderLayout());

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle(0, 0, screen.width-200, screen.height-200);
		this.setBounds(bounds);
		SSD_WIDTH = bounds.width;
		SSD_HEIGHT = bounds.height;
		graphWidth = bounds.width * FACTOR;

		getContentPane().add(getScrollPane(), BorderLayout.CENTER);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);	
				
		this.setVisible(true);		
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();

			mainPanel.add(getTextPanel());
			mainPanel.add(getGraphPanel());

			int w = textWidth + graphWidth + 200;
			mainPanel.setMinimumSize(new Dimension(w, panelHeight));
			mainPanel.setMaximumSize(new Dimension(w, panelHeight));
			mainPanel.setPreferredSize(new Dimension(w, panelHeight));
		}

		return mainPanel;
	}

	private JPanel getButtonPanel() {
		if (btnPanel == null) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 25, 5, 10);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			btnClose = new JButton();
			btnClose.setText(Messages.getTitle("QuantAna.Button.Close"));
			btnClose.setIcon(Messages.getImageIcon("QuantAna.Button.Close"));
			btnClose.setMinimumSize(new Dimension(120, 25));
			btnClose.setMaximumSize(new Dimension(120, 25));
			btnClose.setPreferredSize(new Dimension(120, 25));
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			constraints.gridx = 0;
			constraints.gridy = 0;
			btnPanel.add(btnClose, constraints);

			JLabel lblDummy = new JLabel();
			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.weightx = 1;
			btnPanel.add(lblDummy, constraints);
		}

		return btnPanel;
	}

	private TextPanel getTextPanel() {
		if (textPanel == null) {
			textPanel = new TextPanel();
		}

		return textPanel;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getMainPanel());

			int w = SSD_WIDTH - 20;
			int h = SSD_HEIGHT - 60;
			scrollPane.setMinimumSize(new Dimension(w, h));
			scrollPane.setMaximumSize(new Dimension(w, h));
			scrollPane.setPreferredSize(new Dimension(w, h));
		}
		return scrollPane;
	}

	private JPanel getGraphPanel() {
		if (graphPanel == null) {
			graphPanel = new GraphPanel();
		}

		return graphPanel;
	}

	class TextPanel extends JPanel {

		private static final long serialVersionUID = 101L;

		private Graphics2D g2;

		private String[] tasks = ssm.getSortedTasks();
		double widthServer = 0.0;
		double widthResource = 0.0;

		private Line2D hLine = new Line2D.Double();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g2 = (Graphics2D) g;

			textWidth = getPanelWidth();
			setSize(textWidth, panelHeight);
			setLocation(0, MARGIN_TOP);

			hLine.setLine(textWidth - 1, 0, textWidth - 1, getHeight());
			g2.draw(hLine);

			int x = MARGIN_LEFT;
			int y = MARGIN_TOP;

			for (String s : tasks) {
				g2.setFont(ftServer);
				g2.drawString(s, x, y);

				g2.setFont(ftResource);
				x += widthServer + DISTANCE;

				String[] ress = ssm.getSortedResources(s);

				if (ress.length > 0) {
					for (String r : ress) {
						g2.drawString(r, x, y);
						y += ROW_HEIGHT;
					}
				} else
					y += ROW_HEIGHT;

				x = MARGIN_LEFT;
			}

			drawLines(g2);
		}

		private int getPanelWidth() {
			FontRenderContext cntxt = g2.getFontRenderContext();

			Rectangle2D bounds;
			for (String s : tasks) {
				String[] resources = ssm.getSortedResources(s);
				bounds = ftServer.getStringBounds(s, cntxt);
				if (widthServer < bounds.getWidth())
					widthServer = bounds.getWidth();

				for (String r : resources) {
					bounds = ftResource.getStringBounds(r, cntxt);
					if (widthResource < bounds.getWidth())
						widthResource = bounds.getWidth();
				}
			}

			return (int) (MARGIN_LEFT + widthServer + DISTANCE + widthResource + MARGIN_RIGHT);
		}

		private void drawLines(Graphics2D g2) {
			Line2D line = new Line2D.Double();

			for (int h : lineOrdinates) {
				line.setLine(MARGIN_LEFT, h, textWidth, h);
				g2.draw(line);
			}
		}
	}

	class GraphPanel extends JPanel {
		private static final long serialVersionUID = 101L;

		private double scale = 1.0;

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			setSize(graphWidth, panelHeight);
			setLocation(textWidth + 1, MARGIN_TOP);

			scale = (graphWidth - MARGIN_RIGHT) / clock;

			drawLines(g2);

			drawTimes(g2);

			drawRects(g2);
		}

		private void drawLines(Graphics2D g2) {
			Line2D line = new Line2D.Double();

			for (int h : lineOrdinates) {
				line.setLine(0, h, graphWidth, h);
				g2.draw(line);
			}
		}

		private void drawTimes(Graphics2D g2) {
			Line2D line = new Line2D.Double();
			int x = (int) (clock * scale);
			g2.setPaint(Color.RED);
			line.setLine(x, 0, x, getHeight());
			g2.draw(line);

			FontRenderContext cntxt = g2.getFontRenderContext();
			Rectangle2D bounds;
			int len = getHeight() - 35;

			g2.setPaint(Color.BLACK);
			g2.setFont(ftServer);
			int numT = (int) Math.floor((graphWidth - MARGIN_RIGHT)
					/ TIME_STEPS);
			for (int i = 1; i <= numT; i++) {
				x = i * TIME_STEPS;
				double time = x / scale;
				String s = String.format("%,.2f", time);
				bounds = ftServer.getStringBounds(s, cntxt);

				line.setLine(x, 0, x, len);
				g2.draw(line);

				g2.drawString(s, (int) (x - bounds.getWidth() / 2), len + 20);
			}
		}

		private void drawRects(Graphics2D g2) {
			ArrayList<ArrayList<ArrayList<ActivityPanel>>> matrix = ssm
					.getSortedMatrix();
			int numTasks = ssm.getNumTasks();
			int y = 0;
			int count = 0;

			for (int i = 0; i < numTasks; i++) {
				int n = matrix.get(i).size();

				if (n > 0) {
					for (int j = 0; j < n; j++) {
						ArrayList<ActivityPanel> list = matrix.get(i).get(j);
						y = count++ * ROW_HEIGHT; //+ MARGIN_TOP;
						for (ActivityPanel ap : list) {
							Rectangle bounds = new Rectangle((int) (ap
									.getTimeStart() * scale), y,
									(int) ((ap.getTimeStop() - ap
											.getTimeStart()) * scale),
									ROW_HEIGHT - 10);
							ap.setBounds(bounds);
							ap.setVisible(true);
							graphPanel.add(ap);
						}
					}
				} else {
					y = count++ * ROW_HEIGHT + MARGIN_TOP;
				}
			}
		}
	}
}
