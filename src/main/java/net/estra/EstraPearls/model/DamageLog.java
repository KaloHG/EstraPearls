package net.estra.EstraPearls.model;

import net.estra.EstraPearls.PearlPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class DamageLog {
    private final String player;
    private final Map<String, Integer> damagers;

    private long expirestick;

    public DamageLog(Player player) {
        this.player = player.getName();
        this.damagers = new HashMap<String, Integer>();
    }

    public String getName() {
        return player;
    }

    public Player getPlayer() {
        return Bukkit.getPlayerExact(player);
    }

    public int getDamage(Player player) {
        return getDamage(player.getName());
    }

    public int getDamage(String name) {
        Integer i = damagers.get(name);
        if (i == null)
            return 0;
        else
            return i;
    }

    public void recordDamage(Player damager, int damage, long expirestick) {
        Integer i = damagers.get(damager.getName());
        if (i == null)
            i = damage;
        else
            i += damage;
        damagers.put(damager.getName(), i);
        this.expirestick = expirestick;
        PearlPlugin.logger.info("Logging damage on player, damager: " + damager.getName());
    }

    public List<Player> getDamagers(int min) {
        List<Player> players = new ArrayList<Player>();

        for (Map.Entry<String, Integer> entry : damagers.entrySet()) {
            Player player = Bukkit.getPlayerExact(entry.getKey());
            if (player != null && entry.getValue() > min)
                players.add(player);
        }

        Collections.sort(players, new Comparator<Player>() {
            public int compare(Player p0, Player p1) {
                int d0 = damagers.get(p0.getName());
                int d1 = damagers.get(p1.getName());
                if (d0 < d1) // descending order
                    return 1;
                else if (d0 > d1)
                    return -1;
                else
                    return 0;
            }
        });

        return players;
    }

    public long getExpiresTick() {
        return expirestick;
    }
}
