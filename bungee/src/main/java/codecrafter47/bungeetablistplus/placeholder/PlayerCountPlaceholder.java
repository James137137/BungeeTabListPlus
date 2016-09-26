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

package codecrafter47.bungeetablistplus.placeholder;

import codecrafter47.bungeetablistplus.api.bungee.placeholder.PlaceholderProvider;
import codecrafter47.bungeetablistplus.config.old.ConfigParser;

import java.util.Arrays;

public class PlayerCountPlaceholder extends PlaceholderProvider {
    @Override
    public void setup() {
        bind("server_player_count").to(context -> String.format("%d", context.getServerGroup().map(group -> context.getPlayerManager().getPlayerCount(group.getFilterForPlayerManager())).orElse(0)));
        bind("player_count").alias("gcount").to(context -> String.format("%d", context.getPlayerManager().getGlobalPlayerCount()));
        bind("players").alias("rplayers").withArgs().to((context, args) -> {
            if (args == null) {
                return Integer.toString(context.getPlayerManager().getGlobalPlayerCount());
            } else
                return Integer.toString(context.getPlayerManager().getPlayerCount(ConfigParser.parseFilter(Arrays.asList(args.split(",|\\+")))));
        });
    }
}
