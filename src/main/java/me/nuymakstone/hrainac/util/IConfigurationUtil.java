package me.nuymakstone.hrainac.util;

public interface IConfigurationUtil {
    IConfiguration get(String var1);

    void create(String var1, String var2);

    void save(IConfiguration var1, String var2);

    void delete(String var1);
}
