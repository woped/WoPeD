package org.woped.core.controller;

public interface IStatusBar {

  public static int TYPE = 2;

  public boolean startProgress(String description, int endValue);

  public boolean nextStep();

  public boolean isRunning();
}
