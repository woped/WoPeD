package org.woped.core.controller;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public interface IStatusBar
{

    public static int TYPE = 2;

    public JProgressBar getProgressBar();

    public boolean startProgress(String description, int endValue);

    public boolean nextStep();

    public boolean isRunning();

    public JLabel getStatusLabel();

}
