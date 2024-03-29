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
package org.pushingpixels.flamingo.internal.ui.ribbon;

import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import javax.swing.plaf.ComponentUI;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;

/**
 * UI for ribbon ({@link JRibbon}).
 *
 * @author Kirill Grouchnikov
 */
public abstract class RibbonUI extends ComponentUI {

  /** The application icon. This is displayed in the application menu button. */
  public ResizableIcon applicationIcon;

  /**
   * Returns the bounds of the specified contextual task group.
   *
   * @param group Contextual task group.
   * @return The bounds of the specified contextual task group.
   */
  public abstract Rectangle getContextualTaskGroupBounds(RibbonContextualTaskGroup group);

  public abstract boolean isShowingScrollsForTaskToggleButtons();

  public abstract boolean isShowingScrollsForBands();

  public abstract void handleMouseWheelEvent(MouseWheelEvent e);

  /**
   * Returns the application icon. This is displayed on the application menu button.
   *
   * @return the application icon
   */
  public synchronized ResizableIcon getApplicationIcon() {
    return applicationIcon;
  }

  /**
   * Sets the application icon. This is displayed on the application menu button.
   *
   * <p>There is no check performed to see if <code>applicationIcon</code> is <code>null</code>.
   *
   * @param applicationIcon the application icon to set
   */
  public synchronized void setApplicationIcon(ResizableIcon applicationIcon) {
    this.applicationIcon = applicationIcon;
  }
}
