/*
 * Copyright (c) 2005-2010 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.flamingo.api.ribbon;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonUI;
import org.pushingpixels.flamingo.internal.ui.ribbon.RibbonUI;

/**
 * The ribbon component.
 *
 * <p>The ribbon has the following major parts:
 *
 * <ul>
 *   <li>Ribbon tasks added with {@link #addTask(RibbonTask)}
 *   <li>Contextual ribbon task groups added with {@link
 *       #addContextualTaskGroup(RibbonContextualTaskGroup)}
 *   <li>Application menu button set by {@link #setApplicationMenu(RibbonApplicationMenu)}
 *   <li>Taskbar panel populated by {@link #addTaskbarComponent(Component)}
 *   <li>Help button set by {@link #configureHelp(ResizableIcon, ActionListener)}
 * </ul>
 *
 * <p>While multiple ribbon tasks can be added to the ribbon, only one is visible at any given time.
 * This task is called the <strong>selected</strong> task. Tasks can be switched with the task
 * buttons placed along the top part of the ribbon. Once a task has been added to the ribbon, it
 * cannot be removed.
 *
 * <p>The contextual ribbon task groups allow showing and hiding ribbon tasks based on the current
 * selection in the application. For example, Word only shows the table tasks when a table is
 * selected in the document. By default, tasks belonging to the groups adde by {@link
 * #addContextualTaskGroup(RibbonContextualTaskGroup)} are not visible. To show the tasks belonging
 * to the specific group, call {@link #setVisible(RibbonContextualTaskGroup, boolean)} API. Note
 * that you can have multiple task groups visible at the same time.
 *
 * <p>The application menu button is a big round button shown in the top left corner of the ribbon.
 * If the {@link #setApplicationMenu(RibbonApplicationMenu)} is not called, or called with the
 * <code>null</code> value, the application menu button is not shown, and ribbon task buttons are
 * shifted to the left.
 *
 * <p>The taskbar panel allows showing controls that are visible no matter what ribbon task is
 * selected. To add a taskbar component use the {@link #addTaskbarComponent(Component)} API. The
 * taskbar panel lives to the right of the application menu button. Taskbar components can be
 * removed with the {@link #removeTaskbarComponent(Component)} API.
 *
 * <p>The ribbon can be minimized in one of the following ways:
 *
 * <ul>
 *   <li>Calling {@link #setMinimized(boolean)} with <code>true</code>.
 *   <li>User double-clicking on a task button.
 *   <li>User pressing <code>Ctrl+F1</code> key combination.
 * </ul>
 *
 * <p>A minimized ribbon shows the application menu button, taskbar panel, task buttons and help
 * button, but not the ribbon bands of the selected task. Clicking a task button shows the ribbon
 * bands of that task in a popup <strong>without</strong> shifting the application content down.
 *
 * @author Kirill Grouchnikov
 */
@SuppressWarnings("serial")
public class JRibbon extends JComponent {

  /**
   * The property string used when the {@link #applicationIcon} changes. The value is {@value
   * #PROPERTY_APPLICATION_ICON}.
   */
  public static final String PROPERTY_APPLICATION_ICON = "ribbon.icon";

  /**
   * The property string used when the {@link #applicationMenuRichTooltip} changes. The value is
   * {@value #PROPERTY_APPLICATION_MENU_RICH_TOOLTIP}.
   */
  public static final String PROPERTY_APPLICATION_MENU_RICH_TOOLTIP = "applicationMenuRichTooltip";

  /**
   * The property string used when the {@link #applicationMenu} changes. The value is {@value
   * #PROPERTY_APPLICATION_MENU}.
   */
  public static final String PROPERTY_APPLICATION_MENU = "applicationMenu";

  /**
   * The property string used when the {@link #applicationMenuKeyTip} changes. The value is {@value
   * #PROPERTY_APPLICATION_MENU_KEY_TIP}.
   */
  public static final String PROPERTY_APPLICATION_MENU_KEY_TIP = "applicationMenuKeyTip";

  /**
   * The property string used when the {@link #currentlySelectedTask} changes. The value is {@value
   * #PROPERTY_SELECTED_TASK}.
   */
  public static final String PROPERTY_SELECTED_TASK = "selectedTask";

  /**
   * The property string used when the {@link #isMinimized} changes. The value is {@value
   * #PROPERTY_MINIMIZED}.
   */
  public static final String PROPERTY_MINIMIZED = "minimized";

  /**
   * The general tasks for this ribbon.
   *
   * <p>Tasks that get displayed based on a specific context are contextual tasks. See {@link
   * #contextualTaskGroups} for more information about contextual tasks.
   *
   * @see #addTask(RibbonTask)
   * @see #getTaskCount()
   * @see #getTask(int)
   */
  private List<RibbonTask> tasks;

  /**
   * The contextual task groups.
   *
   * @see #addContextualTaskGroup(RibbonContextualTaskGroup)
   * @see #setVisible(RibbonContextualTaskGroup, boolean)
   * @see #isVisible(RibbonContextualTaskGroup)
   * @see #getContextualTaskGroupCount()
   * @see #getContextualTaskGroup(int)
   */
  private List<RibbonContextualTaskGroup> contextualTaskGroups;

  /**
   * The taskbar components (to the right of the application menu button).
   *
   * @see #addTaskbarComponent(Component)
   * @see #getTaskbarComponents()
   * @see #removeTaskbarComponent(Component)
   */
  private List<Component> taskbarComponents;

  /** Currently selected (shown) task. */
  private RibbonTask currentlySelectedTask;

  /**
   * Help icon. When not <code>null</code>, the ribbon will display a help button at the far right
   * of the tab area.
   *
   * @see #helpActionListener
   * @see #configureHelp(ResizableIcon, ActionListener)
   * @see #getHelpIcon()
   */
  private ResizableIcon helpIcon;

  /**
   * When the {@link #helpIcon} is not <code>null</code>, this listener will be invoked when the
   * user activates the help button.
   *
   * @see #configureHelp(ResizableIcon, ActionListener)
   * @see #getHelpActionListener()
   */
  private ActionListener helpActionListener;

  /**
   * Visibility status of the contextual task group. Must contain a value for each group in {@link
   * #contextualTaskGroups}.
   *
   * @see #setVisible(RibbonContextualTaskGroup, boolean)
   * @see #isVisible(RibbonContextualTaskGroup)
   */
  private Map<RibbonContextualTaskGroup, Boolean> groupVisibilityMap;

  /**
   * The application menu.
   *
   * @see #setApplicationMenu(RibbonApplicationMenu)
   * @see #getApplicationMenu()
   */
  private RibbonApplicationMenu applicationMenu;

  /**
   * The rich tooltip of {@link #applicationMenu} button.
   *
   * @see #applicationMenu
   * @see #setApplicationMenuRichTooltip(RichTooltip)
   * @see #getApplicationMenuRichTooltip()
   */
  private RichTooltip applicationMenuRichTooltip;

  /**
   * The key tip of {@link #applicationMenu} button.
   *
   * @see #applicationMenu
   * @see #setApplicationMenuKeyTip(String)
   * @see #getApplicationMenuKeyTip()
   */
  private String applicationMenuKeyTip;

  /**
   * Indicates whether the ribbon is currently minimized.
   *
   * @see #setMinimized(boolean)
   * @see #isMinimized()
   */
  private boolean isMinimized;

  /**
   * The host ribbon frame. Is <code>null</code> when the ribbon is not hosted in a {@link
   * JRibbonFrame}.
   *
   * @deprecated Dropped support in order to decouple the <code>JRibbon</code> from the <code>
   *     JRibbonFrame</code>
   */
  @Deprecated private JRibbonFrame ribbonFrame;

  /** The UI class ID string. */
  public static final String uiClassID = "RibbonUI";

  /** The application icon. This is displayed in the application menu button. */
  public ResizableIcon applicationIcon;

  /**
   * Constructs an empty default <code>JRibbon</code>. Applications are highly encouraged to use
   * {@link JRibbonFrame} and access the ribbon with {@link JRibbonFrame#getRibbon()} API.
   */
  public JRibbon() {
    this((ResizableIcon) null);
  }

  /**
   * Constructs a <code>JRibbon</code> specifying the application icon. The application icon is
   * displayed in the application menu button. Applications are highly encouraged to use {@link
   * JRibbonFrame} and access the ribbon with {@link JRibbonFrame#getRibbon()} API.
   *
   * @param appIcon the application icon
   */
  public JRibbon(ResizableIcon appIcon) {
    this.tasks = new LinkedList<RibbonTask>();
    this.contextualTaskGroups = new ArrayList<RibbonContextualTaskGroup>();
    this.taskbarComponents = new ArrayList<Component>();
    this.currentlySelectedTask = null;
    this.groupVisibilityMap = new HashMap<RibbonContextualTaskGroup, Boolean>();

    updateUI();
    getUI().setApplicationIcon(appIcon);
  }

  /**
   * Creates an empty ribbon for the specified ribbon frame.
   *
   * @param ribbonFrame Host ribbon frame.
   * @deprecated Dropped support in order to decouple the <code>JRibbon</code> from the <code>
   *     JRibbonFrame</code>
   */
  @Deprecated
  JRibbon(JRibbonFrame ribbonFrame) {
    this();
    this.ribbonFrame = ribbonFrame;
  }

  /**
   * Adds the specified taskbar component to this ribbon.
   *
   * <p>Taskbar components are small components placed to the right of the application menu. These
   * components usually perform an action common among the entire application.
   *
   * @param comp the taskbar component to add
   * @see #removeTaskbarComponent(Component)
   * @see #removeAllTaskbarComponents()
   * @see #getTaskbarComponents()
   */
  public synchronized void addTaskbarComponent(Component comp) {
    if (comp instanceof AbstractCommandButton) {
      AbstractCommandButton button = (AbstractCommandButton) comp;
      button.setDisplayState(CommandButtonDisplayState.SMALL);
      button.setGapScaleFactor(0.5);
      button.setFocusable(false);
    }
    taskbarComponents.add(comp);
    fireStateChanged();
  }

  /*
   * Added Remove Tasks from patch provided by Jonathan Giles Jan 2009
   * http://markmail.org/message/vzw3hrntr6qsdlu3
   */

  /**
   * Removes the specified taskbar component from this ribbon.
   *
   * @param comp The taskbar component to remove.
   * @see #addTaskbarComponent(Component)
   * @see #getTaskbarComponents()
   * @see #removeAllTaskbarComponents()
   */
  public synchronized void removeTaskbarComponent(Component comp) {
    taskbarComponents.remove(comp);
    fireStateChanged();
  }

  /**
   * Removes all components added to the taskbar of the ribbon.
   *
   * @see #addTaskbarComponent(Component)
   * @see #getTaskbarComponents()
   * @see #removeTaskbarComponent(Component)
   */
  public void removeAllTaskbarComponents() {
    taskbarComponents.clear();
    fireStateChanged();
  }

  /**
   * Adds the specified task to this ribbon.
   *
   * @param task The ribbon task to add.
   * @see #addContextualTaskGroup(RibbonContextualTaskGroup)
   * @see #getTaskCount()
   * @see #getTask(int)
   */
  public synchronized void addTask(RibbonTask task) {
    task.setRibbon(this);

    tasks.add(task);

    if (tasks.size() == 1) {
      setSelectedTask(task);
    }

    fireStateChanged();
  }

  /**
   * Removes the task at the specified position, if it represents a valid task. Throws an {@link
   * IndexOutOfBoundsException} if not.
   *
   * @param pos The position of the task to remove.
   */
  public void removeTask(int pos) {
    if (pos >= getTaskCount()) {
      throw new IndexOutOfBoundsException(
          "task position  '"
              + pos
              + "' exceeds number of tasks in ribbon ('"
              + getTaskCount()
              + "')");
    }

    removeTask(getTask(pos));
  }

  /**
   * Removes the given task from the ribbon. If this is the currently visible task, the ribbon will
   * move to the task to its left, unless the removed task is the left-most, in which case it will
   * move to the next task to the right.
   *
   * @param task The ribbon task to be removed from the panel.
   * @exception IllegalArgumentException if <code>task</code> is <code>null</code>
   */
  public void removeTask(RibbonTask task) {
    if (task == null) {
      throw new IllegalArgumentException("RibbonTask can not be null");
    }

    int posOfTask = this.tasks.indexOf(task);
    this.tasks.remove(task);

    if (getSelectedTask().equals(task) && tasks.size() > 0) {
      RibbonTask newTask = getTask(posOfTask == 0 ? 1 : posOfTask - 1);
      setSelectedTask(newTask);
    }

    this.fireStateChanged();
  }

  /** Removes all tasks from the ribbon. */
  public void removeAllTasks() {
    this.tasks.clear();
    this.contextualTaskGroups.clear();
    this.fireStateChanged();
  }

  /**
   * Configures the help button of this ribbon.
   *
   * @param helpIcon The icon for the help button.
   * @param helpActionListener The action listener for the help button.
   * @see #getHelpIcon()
   * @see #getHelpActionListener()
   */
  public synchronized void configureHelp(
      ResizableIcon helpIcon, ActionListener helpActionListener) {
    this.helpIcon = helpIcon;
    this.helpActionListener = helpActionListener;
    this.fireStateChanged();
  }

  /**
   * Returns the icon for the help button. Will return <code>null</code> if the help button has not
   * been configured with the {@link #configureHelp(ResizableIcon, ActionListener)} API.
   *
   * @return The icon for the help button.
   * @see #configureHelp(ResizableIcon, ActionListener)
   * @see #getHelpActionListener()
   */
  public ResizableIcon getHelpIcon() {
    return this.helpIcon;
  }

  /**
   * Returns the action listener for the help button. Will return <code>null</code> if the help
   * button has not been configured with the {@link #configureHelp(ResizableIcon, ActionListener)}
   * API.
   *
   * @return The action listener for the help button.
   * @see #configureHelp(ResizableIcon, ActionListener)
   * @see #getHelpIcon()
   */
  public ActionListener getHelpActionListener() {
    return this.helpActionListener;
  }

  /**
   * Adds a component to the 'Help Panel.' This is the area where the help button lives. and is the
   * far right area of the main tab area.
   *
   * <p>Components will be added in left to right fashion, Also, if a help listener is specified
   * then the help button will be the rightmost component on the list.
   *
   * <p>Generally speaking this area should not be abused, as any large amount of components will
   * cause the space available for the task tabs to shrink.
   *
   * <p>This is the area where you would add a "collapse" button like found in Office 2010, or the
   * min/max/close buttons of an integrated desktop area.
   *
   * @param comp the component to be added
   */
  public void addHelpPanelComponent(Component comp) {
    if (comp == null) return;

    try {
      List<Component> existingHelpPanelComponents =
          (List<Component>) getClientProperty(BasicRibbonUI.HELP_PANEL_COMPONENTS);
      if (existingHelpPanelComponents != null) {
        if (!existingHelpPanelComponents.contains(comp)) {
          existingHelpPanelComponents.add(comp);
        }
      } else {
        List<Component> helpComps = new ArrayList<Component>();
        helpComps.add(comp);
        putClientProperty(BasicRibbonUI.HELP_PANEL_COMPONENTS, helpComps);
      }
    } catch (RuntimeException re) {
      // re-write on any error
      List<Component> helpComps = new ArrayList<Component>();
      helpComps.add(comp);
      putClientProperty(BasicRibbonUI.HELP_PANEL_COMPONENTS, helpComps);
    }
    fireStateChanged();
  }

  /**
   * Removes a component from the 'Help Panel'.
   *
   * @param comp The component to remove. If the component is not currently on the help panel this
   *     call will be a no-op.
   */
  public void removeHelpPanelComponent(Component comp) {
    try {
      List<Component> existingHelpPanelComponents =
          (List<Component>) getClientProperty(BasicRibbonUI.HELP_PANEL_COMPONENTS);
      if (existingHelpPanelComponents != null) {
        if (existingHelpPanelComponents.remove(comp)) {
          fireStateChanged();
        }
      }
    } catch (RuntimeException ignore) {
    }
  }

  /** Removes al the components from the 'Help Panel'. */
  public void removeAllHelpPanelComponents() {
    try {
      List<Component> existingHelpPanelComponents =
          (List<Component>) getClientProperty(BasicRibbonUI.HELP_PANEL_COMPONENTS);
      if (existingHelpPanelComponents != null) {
        existingHelpPanelComponents.clear();
        fireStateChanged();
      }
    } catch (RuntimeException ignore) {
    }
  }

  /**
   * Adds the specified contextual task group to this ribbon.
   *
   * @param group Task group to add.
   * @see #addTask(RibbonTask)
   * @see #setVisible(RibbonContextualTaskGroup, boolean)
   * @see #isVisible(RibbonContextualTaskGroup)
   */
  public synchronized void addContextualTaskGroup(RibbonContextualTaskGroup group) {
    group.setRibbon(this);

    this.contextualTaskGroups.add(group);
    this.groupVisibilityMap.put(group, false);

    this.fireStateChanged();
  }

  /**
   * Returns the number of regular tasks in <code>this</code> ribbon. This does not include the
   * contextual ribbon tasks.
   *
   * <p>To find the total number of ribbon tasks (including contextual ribbon tasks) you will have
   * to iterate through the contextual task groups.
   *
   * @return Number of regular tasks in <code>this</code> ribbon.
   * @see #getTask(int)
   * @see #addTask(RibbonTask)
   */
  public synchronized int getTaskCount() {
    return this.tasks.size();
  }

  /**
   * Retrieves the regular task at specified index.
   *
   * @param index task index
   * @return the task that matches the specified index
   * @see #getTaskCount()
   * @see #addTask(RibbonTask)
   */
  public synchronized RibbonTask getTask(int index) {
    return this.tasks.get(index);
  }

  /**
   * Returns the number of contextual task groups in <code>this</code> ribbon.
   *
   * @return number of contextual task groups in <code>this</code> ribbon
   * @see #addContextualTaskGroup(RibbonContextualTaskGroup)
   * @see #getContextualTaskGroup(int)
   */
  public synchronized int getContextualTaskGroupCount() {
    return this.contextualTaskGroups.size();
  }

  /**
   * Retrieves contextual task group at specified index.
   *
   * @param index group index
   * @return group that matches the specified index
   * @see #addContextualTaskGroup(RibbonContextualTaskGroup)
   * @see #getContextualTaskGroupCount()
   */
  public synchronized RibbonContextualTaskGroup getContextualTaskGroup(int index) {
    return this.contextualTaskGroups.get(index);
  }

  /**
   * Selects the specified task. The task can be either regular (added with {@link
   * #addTask(RibbonTask)}) or a task in a visible contextual task group (added with {@link
   * #addContextualTaskGroup(RibbonContextualTaskGroup)}. Fires a <code>selectedTask</code> property
   * change event.
   *
   * @param task task to select
   * @throws IllegalArgumentException if <code>task</code> is not in the ribbon, is <code>null
   *     </code>, or not visible.
   * @see #getSelectedTask()
   */
  public synchronized void setSelectedTask(RibbonTask task) {
    // check for task in general tasks
    boolean valid = tasks.contains(task);
    // if not a general task, then check contextual tasks
    if (!valid) {
      for (int i = 0; i < getContextualTaskGroupCount(); i++) {
        RibbonContextualTaskGroup group = getContextualTaskGroup(i);
        if (!this.isVisible(group)) continue;
        for (int j = 0; j < group.getTaskCount(); j++) {
          if (group.getTask(j) == task) {
            valid = true;
            break;
          }
        }
        if (valid) break;
      }
    }
    if (!valid) {
      throw new IllegalArgumentException(
          "The specified task to be selected is either not "
              + "part of this ribbon or not marked as visible");
    }

    if (currentlySelectedTask != null) {
      for (AbstractRibbonBand<?> ribbonBand : currentlySelectedTask.getBands()) {
        ribbonBand.setVisible(false);
      }
    }

    for (int i = 0; i < task.getBandCount(); i++) {
      AbstractRibbonBand<?> ribbonBand = task.getBand(i);
      ribbonBand.setVisible(true);
    }

    RibbonTask old = currentlySelectedTask;
    currentlySelectedTask = task;

    revalidate();
    repaint();

    firePropertyChange(PROPERTY_SELECTED_TASK, old, this.currentlySelectedTask);
  }

  /**
   * Returns the currently selected task.
   *
   * @return The currently selected task.
   * @see #setSelectedTask(RibbonTask)
   */
  public synchronized RibbonTask getSelectedTask() {
    return this.currentlySelectedTask;
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.swing.JComponent#updateUI()
   */
  @Override
  public void updateUI() {
    if (UIManager.get(getUIClassID()) != null) {
      setUI(UIManager.getUI(this));
    } else {
      setUI(new BasicRibbonUI());
    }
    for (Component comp : this.taskbarComponents) {
      SwingUtilities.updateComponentTreeUI(comp);
    }
  }

  /**
   * Returns the UI object which implements the L&F for this component.
   *
   * @return a <code>RibbonUI</code> object
   * @see #setUI(javax.swing.plaf.ComponentUI)
   */
  public RibbonUI getUI() {
    return (RibbonUI) ui;
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.swing.JComponent#getUIClassID()
   */
  @Override
  public String getUIClassID() {
    return uiClassID;
  }

  /**
   * Gets an unmodifiable list of all taskbar components of <code>this</code> ribbon.
   *
   * @return All taskbar components of <code>this</code> ribbon.
   * @see #addTaskbarComponent(Component)
   * @see #removeTaskbarComponent(Component)
   */
  public synchronized List<Component> getTaskbarComponents() {
    return Collections.unmodifiableList(this.taskbarComponents);
  }

  /**
   * Adds the specified change listener to track changes to this ribbon.
   *
   * @param l Change listener to add.
   * @see #removeChangeListener(ChangeListener)
   */
  public void addChangeListener(ChangeListener l) {
    this.listenerList.add(ChangeListener.class, l);
  }

  /**
   * Removes the specified change listener from tracking changes to this ribbon.
   *
   * @param l Change listener to remove.
   * @see #addChangeListener(ChangeListener)
   */
  public void removeChangeListener(ChangeListener l) {
    this.listenerList.remove(ChangeListener.class, l);
  }

  /** Notifies all registered listeners that the state of this ribbon has changed. */
  protected void fireStateChanged() {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    ChangeEvent event = new ChangeEvent(this);
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        ((ChangeListener) listeners[i + 1]).stateChanged(event);
      }
    }
  }

  /**
   * Sets the visibility of ribbon tasks in the specified contextual task group. Visibility of all
   * ribbon tasks in the specified group is affected. Note that the ribbon can show ribbon tasks of
   * multiple groups at the same time.
   *
   * @param group Contextual task group.
   * @param isVisible If <code>true</code>, all ribbon tasks in the specified group will be visible.
   *     If <code>false</code>, all ribbon tasks in the specified group will be hidden.
   * @see #isVisible(RibbonContextualTaskGroup)
   */
  public synchronized void setVisible(RibbonContextualTaskGroup group, boolean isVisible) {
    this.groupVisibilityMap.put(group, isVisible);

    // special handling of selected tab
    if (!isVisible) {
      boolean isSelectedBeingHidden = false;
      for (int i = 0; i < group.getTaskCount(); i++) {
        if (this.getSelectedTask() == group.getTask(i)) {
          isSelectedBeingHidden = true;
          break;
        }
      }
      if (isSelectedBeingHidden) {
        this.setSelectedTask(this.getTask(0));
      }
    }

    this.fireStateChanged();
    this.revalidate();
    SwingUtilities.getWindowAncestor(this).repaint();
  }

  /**
   * Returns the visibility of ribbon tasks in the specified contextual task group.
   *
   * @param group Contextual task group.
   * @return <code>true</code> if the ribbon tasks in the specified group are visible, <code>false
   *     </code> otherwise.
   */
  public synchronized boolean isVisible(RibbonContextualTaskGroup group) {
    return this.groupVisibilityMap.get(group);
  }

  /**
   * Sets the application menu for this ribbon. If <code>null</code> is passed, the application menu
   * button is hidden. Fires an <code>applicationMenu</code> property change event.
   *
   * @param applicationMenu The new application menu. Can be <code>null</code>.
   * @see #getApplicationMenu()
   */
  public synchronized void setApplicationMenu(RibbonApplicationMenu applicationMenu) {
    RibbonApplicationMenu old = this.applicationMenu;
    if (old != applicationMenu) {
      this.applicationMenu = applicationMenu;
      if (this.applicationMenu != null) {
        this.applicationMenu.setFrozen();
      }
      this.firePropertyChange(PROPERTY_APPLICATION_MENU, old, this.applicationMenu);
    }
  }

  /**
   * Returns the application menu of this ribbon.
   *
   * @return The application menu of this ribbon.
   * @see #setApplicationMenu(RibbonApplicationMenu)
   */
  public synchronized RibbonApplicationMenu getApplicationMenu() {
    return this.applicationMenu;
  }

  /**
   * Sets the rich tooltip of the application menu button. Fires an <code>applicationMenuRichTooltip
   * </code> property change event.
   *
   * @param tooltip The rich tooltip of the application menu button.
   * @see #getApplicationMenuRichTooltip()
   * @see #setApplicationMenu(RibbonApplicationMenu)
   */
  public synchronized void setApplicationMenuRichTooltip(RichTooltip tooltip) {
    RichTooltip old = this.applicationMenuRichTooltip;
    this.applicationMenuRichTooltip = tooltip;
    this.firePropertyChange(
        PROPERTY_APPLICATION_MENU_RICH_TOOLTIP, old, this.applicationMenuRichTooltip);
  }

  /**
   * Returns the rich tooltip of the application menu button.
   *
   * @return The rich tooltip of the application menu button.
   * @see #setApplicationMenuRichTooltip(RichTooltip)
   * @see #setApplicationMenu(RibbonApplicationMenu)
   */
  public synchronized RichTooltip getApplicationMenuRichTooltip() {
    return this.applicationMenuRichTooltip;
  }

  /**
   * Sets the key tip of the application menu button. Fires an <code>applicationMenuKeyTip</code>
   * property change event.
   *
   * @param keyTip The new key tip for the application menu button.
   * @see #setApplicationMenu(RibbonApplicationMenu)
   * @see #getApplicationMenuKeyTip()
   */
  public synchronized void setApplicationMenuKeyTip(String keyTip) {
    String old = this.applicationMenuKeyTip;
    this.applicationMenuKeyTip = keyTip;
    this.firePropertyChange(PROPERTY_APPLICATION_MENU_KEY_TIP, old, this.applicationMenuKeyTip);
  }

  /**
   * Returns the key tip of the application menu button.
   *
   * @return The key tip of the application menu button.
   * @see #setApplicationMenuKeyTip(String)
   * @see #setApplicationMenu(RibbonApplicationMenu)
   */
  public synchronized String getApplicationMenuKeyTip() {
    return this.applicationMenuKeyTip;
  }

  /**
   * Returns the indication whether this ribbon is minimized.
   *
   * @return <code>true</code> if this ribbon is minimized, <code>false</code> otherwise.
   * @see #setMinimized(boolean)
   */
  public synchronized boolean isMinimized() {
    return this.isMinimized;
  }

  /**
   * Changes the minimized state of this ribbon. Fires a <code>minimized</code> property change
   * event.
   *
   * @param isMinimized if <code>true</code>, this ribbon becomes minimized, otherwise it is
   *     unminimized.
   */
  public synchronized void setMinimized(boolean isMinimized) {
    // System.out.println("Ribbon minimized -> " + isMinimized);
    boolean old = this.isMinimized;
    if (old != isMinimized) {
      this.isMinimized = isMinimized;
      this.firePropertyChange(PROPERTY_MINIMIZED, old, this.isMinimized);
    }
  }

  /**
   * Returns the ribbon frame that hosts this ribbon. The result can be <code>null</code>.
   *
   * @return The ribbon frame that hosts this ribbon.
   * @deprecated Dropped support in order to decouple the <code>JRibbon</code> from the <code>
   *     JRibbonFrame</code>
   */
  @Deprecated
  public JRibbonFrame getRibbonFrame() {
    return this.ribbonFrame;
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.swing.JComponent#setVisible(boolean)
   */
  @Override
  public void setVisible(boolean flag) {
    if (!flag && (getRibbonFrame() != null))
      throw new IllegalArgumentException("Can't hide ribbon on JRibbonFrame");
    super.setVisible(flag);
  }

  /**
   * Returns the application icon. The application icon is displayed on the application menu button.
   *
   * <p>This is a convenience method and is equivalent to <code>getUI().getApplicationIcon()</code>.
   *
   * @see #getUI()
   * @see RibbonUI#getApplicationIcon()
   * @see #setApplicationIcon(ResizableIcon)
   * @return the application icon
   */
  public synchronized ResizableIcon getApplicationIcon() {
    return getUI().getApplicationIcon();
  }

  /**
   * Sets the application icon. This is displayed on the application menu button.
   *
   * <p>There is no check performed to see if <code>applicationIcon</code> is <code>null</code>.
   *
   * <p>A <code>PropertyChangeEvent</code> is fired for the {@link #PROPERTY_APPLICATION_ICON}
   * property.
   *
   * @see #getUI()
   * @see RibbonUI#setApplicationIcon(ResizableIcon)
   * @see #getApplicationIcon()
   * @param applicationIcon the application icon to set
   */
  public synchronized void setApplicationIcon(ResizableIcon applicationIcon) {
    ResizableIcon old = getUI().getApplicationIcon();
    getUI().setApplicationIcon(applicationIcon);
    firePropertyChange(PROPERTY_APPLICATION_ICON, old, this.applicationIcon);
    // TODO set the application menu button icon
    // JRibbonApplicationMenuButton button = getApplicationMenuButton();
    // if (button != null) {
    // button.setIcon(this.applicationIcon);
    // }
  }
}
