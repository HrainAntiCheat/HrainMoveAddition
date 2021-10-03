package me.nuymakstone.hrainac.util;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashSet;

public class BukkitConfiguration implements IConfiguration
{
    private final ConfigurationSection configuration;

    public BukkitConfiguration(final ConfigurationSection configuration) {
        this.configuration = configuration;
    }

    public IConfiguration getSection(final String path) {
        final Object section = this.configuration.get(path);
        if (section instanceof ConfigurationSection) {
            return (IConfiguration)new BukkitConfiguration((ConfigurationSection)section);
        }
        return null;
    }

    public Collection<String> getKeys() {
        return (Collection<String>)this.configuration.getKeys(false);
    }

    public Collection<String> getStringList(final String path) {
        if (this.configuration.contains(path)) {
            return new HashSet<String>(this.configuration.getStringList(path));
        }
        return null;
    }

    public String getString(final String path) {
        return this.configuration.getString(path);
    }

    public String getString(final String path, final String def) {
        if (this.contains(path)) {
            return this.getString(path);
        }
        return def;
    }

    public double getDouble(final String path) {
        return this.configuration.getDouble(path);
    }

    public long getLong(final String path) {
        return this.configuration.getLong(path);
    }

    public int getInt(final String path) {
        return this.configuration.getInt(path);
    }

    public boolean getBoolean(final String path) {
        return this.configuration.getBoolean(path);
    }

    public boolean getBoolean(final String path, final boolean def) {
        return this.configuration.getBoolean(path, def);
    }

    public boolean contains(final String path) {
        return this.configuration.contains(path);
    }

    public Object getObject() {
        return this.configuration;
    }
}

