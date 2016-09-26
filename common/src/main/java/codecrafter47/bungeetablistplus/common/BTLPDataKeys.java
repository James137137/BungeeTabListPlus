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

package codecrafter47.bungeetablistplus.common;

import codecrafter47.bungeetablistplus.data.DataKey;

import java.util.List;

public final class BTLPDataKeys {
    public static PlaceholderAPIDataKey createPlaceholderAPIDataKey(String placeholder) {
        return new PlaceholderAPIDataKey(placeholder);
    }

    public static ThirdPartyVariableDataKey createThirdPartyVariableDataKey(String name) {
        return new ThirdPartyVariableDataKey(name);
    }

    public static DataKey<String> createBungeeThirdPartyVariableDataKey(String name) {
        return new BungeeThirdPartyVariableDataKey(name);
    }

    public final static DataKey<List<String>> REGISTERED_THIRD_PARTY_VARIABLES = DataKey.builder().bukkit().server().id("thirdparty-variables").build();
    public final static DataKey<Boolean> PLACEHOLDERAPI_PRESENT = DataKey.builder().bukkit().server().id("placeholderapi-present").build();

    public static class PlaceholderAPIDataKey extends DataKey<String> {
        private final String placeholder;

        private static final long serialVersionUID = 1L;

        private PlaceholderAPIDataKey(String placeholder) {
            super("placeholderAPI", Scope.PLAYER, false);
            this.placeholder = placeholder;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        @Override
        public int hashCode() {
            return super.hashCode() + placeholder.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PlaceholderAPIDataKey && ((PlaceholderAPIDataKey) obj).placeholder.equals(placeholder);
        }

        @Override
        public String toString() {
            return super.toString() + ":" + placeholder;
        }
    }

    public static class BungeeThirdPartyVariableDataKey extends DataKey<String> {
        private final String name;

        private static final long serialVersionUID = 1L;

        private BungeeThirdPartyVariableDataKey(String name) {
            super("bungee-thirdparty", Scope.PLAYER, true);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return super.hashCode() + name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof BungeeThirdPartyVariableDataKey && ((BungeeThirdPartyVariableDataKey) obj).name.equals(name);
        }

        @Override
        public String toString() {
            return super.toString() + ":" + name;
        }
    }

    public static class ThirdPartyVariableDataKey extends DataKey<String> {
        private final String name;

        private static final long serialVersionUID = 1L;

        private ThirdPartyVariableDataKey(String name) {
            super("thirdparty", Scope.PLAYER, false);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return super.hashCode() + name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ThirdPartyVariableDataKey && ((ThirdPartyVariableDataKey) obj).name.equals(name);
        }

        @Override
        public String toString() {
            return super.toString() + ":" + name;
        }
    }
}
