package net.toadless.radio.objects.oauth2;

import net.dv8tion.jda.api.Permission;

import java.util.Set;

public class Guild
{
    private final long id;
    private final String name;
    private final String icon;
    private final Set<Permission> permissions;

    public Guild(long id, String name, String icon, Set<Permission> permissions)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.permissions = permissions;
    }

    public Guild(long id, String name, String icon, long permissions)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.permissions = Permission.getPermissions(permissions);
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getIcon()
    {
        return icon;
    }

    public Set<Permission> getPermissions()
    {
        return permissions;
    }
}