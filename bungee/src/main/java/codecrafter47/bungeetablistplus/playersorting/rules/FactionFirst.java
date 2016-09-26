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

package codecrafter47.bungeetablistplus.playersorting.rules;

import codecrafter47.bungeetablistplus.BungeeTabListPlus;
import codecrafter47.bungeetablistplus.api.bungee.IPlayer;
import codecrafter47.bungeetablistplus.api.bungee.tablist.TabListContext;
import codecrafter47.bungeetablistplus.context.Context;
import codecrafter47.bungeetablistplus.data.DataKeys;
import codecrafter47.bungeetablistplus.player.Player;
import codecrafter47.bungeetablistplus.playersorting.SortingRule;

import java.util.Optional;

public class FactionFirst implements SortingRule {
    @Override
    public int compare(TabListContext context, IPlayer player1, IPlayer player2) {
        IPlayer viewer = BungeeTabListPlus.getInstance().getConnectedPlayerManager().getPlayer(context.getViewer());
        Optional<String> faction = ((Player) viewer).get(DataKeys.Factions_FactionName);
        if (faction.isPresent()) {
            Optional<String> faction1 = ((Player) player1).get(DataKeys.Factions_FactionName);
            Optional<String> faction2 = ((Player) player2).get(DataKeys.Factions_FactionName);
            if (!faction1.equals(faction2)) {
                if (faction1.equals(faction)) return -1;
                if (faction2.equals(faction)) return 1;
            }
        }
        return 0;
    }

    @Override
    public int compare(Context context, IPlayer player1, IPlayer player2) {
        Player viewer = context.get(Context.KEY_VIEWER);
        Optional<String> faction = viewer.get(DataKeys.Factions_FactionName);
        if (faction.isPresent()) {
            Optional<String> faction1 = ((Player) player1).get(DataKeys.Factions_FactionName);
            Optional<String> faction2 = ((Player) player2).get(DataKeys.Factions_FactionName);
            if (!faction1.equals(faction2)) {
                if (faction1.equals(faction)) return -1;
                if (faction2.equals(faction)) return 1;
            }
        }
        return 0;
    }
}
