package me.totalfreedom.essentials;

import com.earth2me.essentials.User;
import com.google.common.base.Function;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Handler
{
    private static Logger logger;
    private static Function<Player, Boolean> adminProvider;

    public static void setLogger(Logger logger)
    {
        Handler.logger = logger;
    }

    public static Plugin getTFM()
    {
        final Plugin tfm = Bukkit.getPluginManager().getPlugin("TotalFreedomMod");
        if (tfm == null)
        {
            logger.warning("Could not resolve plugin: TotalFreedomMod");
        }

        return tfm;
    }

    public boolean isAdmin(User user)
    {
        return isAdmin(user.getBase());
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public boolean isAdmin(Player player)
    {

        if (adminProvider == null)
        {
            final Plugin tfm = getTFM();
            if (tfm == null)
            {
                return false;
            }

            Object provider = null;
            for (RegisteredServiceProvider<?> serv : Bukkit.getServicesManager().getRegistrations(tfm))
            {
                if (Function.class.isAssignableFrom(serv.getService()))
                {
                    provider = serv.getProvider();
                }
            }

            if (provider == null)
            {
                warning("Could not obtain admin service provider!");
                return false;
            }

            adminProvider = (Function<Player, Boolean>)provider;
        }

        return adminProvider.apply(player);
    }

    public static void warning(String warning)
    {
        logger.warning(warning);
    }

    public static void info(String info)
    {
        logger.info(info);
    }
}