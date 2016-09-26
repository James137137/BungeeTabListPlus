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
package codecrafter47.bungeetablistplus.managers;

import codecrafter47.bungeetablistplus.BungeeTabListPlus;
import codecrafter47.bungeetablistplus.api.bungee.IPlayer;
import codecrafter47.bungeetablistplus.api.bungee.PlayerManager;
import codecrafter47.bungeetablistplus.player.IPlayerProvider;
import codecrafter47.bungeetablistplus.player.Player;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PlayerManagerImpl implements PlayerManager {

    private final ProxiedPlayer viewer;
    private final Collection<IPlayer> players;
    private final boolean includeSpectators;
    private final boolean canSeeHiddenPlayers;

    public PlayerManagerImpl(BungeeTabListPlus plugin, Collection<IPlayerProvider> playerProviders, ProxiedPlayer viewer) {
        this.viewer = viewer;
        this.players = ImmutableList.copyOf(Iterables.concat(Collections2.transform(playerProviders, IPlayerProvider::getPlayers)));
        includeSpectators = plugin.getConfig().showPlayersInGamemode3;
        canSeeHiddenPlayers = plugin.getPermissionManager().hasPermission(viewer, "bungeetablistplus.seevanished");
    }

    @Override
    public List<IPlayer> getPlayers(Filter filter) {
        List<IPlayer> list = new ArrayList<>();
        for (IPlayer p : players) {

            if ((includeSpectators || p.getGameMode() != 3) && (canSeeHiddenPlayers || !BungeeTabListPlus.isHidden((Player) p)) && filter.test(viewer, p)) {
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public int getServerPlayerCount(String server) {
        int num = 0;
        for (IPlayer p : players) {
            Optional<ServerInfo> s = p.getServer();
            if (s.isPresent()) {
                if (s.get().getName().equalsIgnoreCase(server) && (canSeeHiddenPlayers || !BungeeTabListPlus.isHidden((Player) p)) && (includeSpectators || p.getGameMode() != 3)) {
                    num++;
                }
            }
        }
        return num;
    }

    @Override
    public int getGlobalPlayerCount() {
        int num = 0;
        for (IPlayer p : players) {
            if ((canSeeHiddenPlayers || !BungeeTabListPlus.isHidden((Player) p)) && (includeSpectators || p.getGameMode() != 3)) {
                num++;
            }
        }
        return num;
    }

    @Override
    public int getPlayerCount(Filter filter) {
        int num = 0;
        for (IPlayer p : players) {
            if ((includeSpectators || p.getGameMode() != 3) && (canSeeHiddenPlayers || !BungeeTabListPlus.isHidden((Player) p)) && filter.test(viewer, p)) {
                num++;
            }
        }
        return num;
    }
}
