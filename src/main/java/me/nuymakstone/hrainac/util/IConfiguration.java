package me.nuymakstone.hrainac.util;

import java.util.Collection;

public interface IConfiguration
{
    IConfiguration getSection(final String p0);

    Collection<String> getKeys();

    Collection<String> getStringList(final String p0);

    String getString(final String p0);

    String getString(final String p0, final String p1);

    double getDouble(final String p0);

    long getLong(final String p0);

    int getInt(final String p0);

    boolean getBoolean(final String p0);

    boolean getBoolean(final String p0, final boolean p1);

    Object getObject();

    boolean contains(final String p0);
}

