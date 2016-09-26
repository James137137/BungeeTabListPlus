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

package codecrafter47.bungeetablistplus.skin;

import codecrafter47.bungeetablistplus.api.bungee.Icon;
import codecrafter47.bungeetablistplus.api.bungee.Skin;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode
public class PlayerSkin implements Skin {
    UUID player;
    @NonNull
    String[][] properties;

    @Override
    public String[][] toProperty() {
        return properties;
    }

    @Override
    public UUID getOwner() {
        return player;
    }

    public static Skin fromIcon(Icon icon) {
        return new PlayerSkin(icon.getPlayer(), icon.getProperties());
    }
}
