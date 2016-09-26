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
import codecrafter47.bungeetablistplus.api.bungee.BungeeTabListPlusAPI;
import codecrafter47.bungeetablistplus.data.AbstractDataAccess;
import codecrafter47.bungeetablistplus.data.DataCache;
import codecrafter47.bungeetablistplus.data.DataKey;
import codecrafter47.bungeetablistplus.data.DataKeys;
import codecrafter47.bungeetablistplus.player.ConnectedPlayer;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Listener;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DataManager extends AbstractDataAccess<ProxiedPlayer> implements Listener {
    private final BungeeTabListPlus bungeeTabListPlus;
    private final PermissionManager permissionManager;

    public DataManager(BungeeTabListPlus bungeeTabListPlus, PermissionManager permissionManager) {
        this.bungeeTabListPlus = bungeeTabListPlus;
        this.permissionManager = permissionManager;
        init();
        ProxyServer.getInstance().getScheduler().schedule(bungeeTabListPlus.getPlugin(), this::updateData, 1, 1, TimeUnit.SECONDS);
    }

    private void init() {
        bind(DataKeys.BungeeCord_PrimaryGroup, permissionManager::getMainGroupFromBungeeCord);
        bind(DataKeys.BungeeCord_Rank, permissionManager::getBungeeCordRank);
        bind(DataKeys.BungeePerms_PrimaryGroup, permissionManager::getMainGroupFromBungeePerms);
        bind(DataKeys.BungeePerms_Prefix, permissionManager::getPrefixFromBungeePerms);
        bind(DataKeys.BungeePerms_DisplayPrefix, permissionManager::getDisplayPrefix);
        bind(DataKeys.BungeePerms_Suffix, permissionManager::getSuffixFromBungeePerms);
        bind(DataKeys.BungeePerms_Rank, permissionManager::getBungeePermsRank);
        bind(DataKeys.BungeePerms_PrimaryGroupPrefix, permissionManager::getPrimaryGroupPrefixFromBungeePerms);
        bind(DataKeys.BungeePerms_PlayerPrefix, permissionManager::getPlayerPrefixFromBungeePerms);
        bind(DataKeys.ClientVersion, player1 -> BungeeTabListPlus.getInstance().getProtocolVersionProvider().getVersionString(player1));
        bind(DataKeys.BungeeCord_SessionDuration, p -> Optional.ofNullable(bungeeTabListPlus.getConnectedPlayerManager().getPlayerIfPresent(p)).map(ConnectedPlayer::getCurrentSessionDuration).orElse(null));
        bind(BungeeTabListPlus.DATA_KEY_GAMEMODE, p -> ((UserConnection) p).getGamemode());
        bind(BungeeTabListPlus.DATA_KEY_SERVER, p -> {
            Server server = p.getServer();
            return server != null ? server.getInfo().getName() : null;
        });
        bind(BungeeTabListPlus.DATA_KEY_ICON, BungeeTabListPlusAPI::getIconFromPlayer);
        bind(DataKeys.BungeeCord_DisplayName, ProxiedPlayer::getDisplayName);
    }

    @SuppressWarnings("unchecked")
    private void updateData() {
        for (ConnectedPlayer player : bungeeTabListPlus.getConnectedPlayerManager().getPlayers()) {
            for (DataKey<?> dataKey : providersByDataKey.keySet()) {
                DataKey<Object> key = (DataKey<Object>) dataKey;
                updateIfNecessary(player, key, getRawValue(key, player.getPlayer()));
            }
        }
    }

    private <T> void updateIfNecessary(ConnectedPlayer player, DataKey<T> key, T value) {
        DataCache data = player.getData();
        if (!Objects.equals(data.getRawValue(key), value)) {
            bungeeTabListPlus.runInMainThread(() -> data.updateValue(key, value));
        }
    }
}
