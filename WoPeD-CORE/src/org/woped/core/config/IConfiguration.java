package org.woped.core.config;

import java.io.File;

/**
 * Interface defining the public capabilities of all configuration sub-interfaces
 * @author Philip Allgaier
 *
 */
public interface IConfiguration
{
    //
    public boolean initConfig();

    //
    public boolean readConfig(File file);

    //
    public boolean saveConfig(File file);

    //
    public boolean saveConfig();
}
