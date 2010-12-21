package org.woped.core.config;

import java.io.File;

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
