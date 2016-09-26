/*
 * BungeeTabListPlus - a BungeeCord plugin to customize the tablist
 *
 * Copyright (C) 2014 - 2015 Florian Stober
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package codecrafter47.bungeetablistplus.data.vault;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultGroupRankProvider extends VaultDataProvider<Player, Integer> {
    public Integer apply0(Player player) {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            Chat chat = rsp.getProvider();
            if (chat != null && chat.isEnabled()) {
                try {
                    String primaryGroup = chat.getPrimaryGroup(player);
                    if (primaryGroup != null) {
                        int weight = chat.getGroupInfoInteger(player.getWorld(), primaryGroup, "rank", Integer.MIN_VALUE);
                        if (weight != Integer.MIN_VALUE) {
                            return weight;
                        }
                    }
                } catch (UnsupportedOperationException ignored) {
                    // Permission plugin doesn't support groups
                }
            }
        }
        return null;
    }
}
