package net.toadless.radio.objects.oauth2;

import net.dv8tion.jda.api.Permission;

import java.util.Set;

public class Guild
{
    private final String id;
    private final String name;
    private final String icon;
    private final Set<Permission> permissions;

    public Guild(String id, String name, String icon, Set<Permission> permissions)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.permissions = permissions;
    }

    public Guild(String id, String name, String icon, long permissions)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.permissions = Permission.getPermissions(permissions);
    }

    public String getId()
    {
        return id;
    }

    public long getIdLong()
    {
        return Long.parseLong(getId());
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