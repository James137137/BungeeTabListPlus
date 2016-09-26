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

package codecrafter47.bungeetablistplus.bridge;

import codecrafter47.bungeetablistplus.BungeeTabListPlus;
import codecrafter47.bungeetablistplus.api.bungee.placeholder.PlaceholderProvider;
import codecrafter47.bungeetablistplus.api.bungee.tablist.SlotBuilder;
import codecrafter47.bungeetablistplus.api.bungee.tablist.SlotTemplate;
import codecrafter47.bungeetablistplus.api.bungee.tablist.TabListContext;
import codecrafter47.bungeetablistplus.common.BTLPDataKeys;
import codecrafter47.bungeetablistplus.common.Constants;
import codecrafter47.bungeetablistplus.config.old.TabListConfig;
import codecrafter47.bungeetablistplus.placeholder.Placeholder;
import codecrafter47.bungeetablistplus.player.Player;
import com.google.common.collect.Sets;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPIHook implements Listener {
    private final BungeeTabListPlus bungeeTabListPlus;
    private final Set<String> registeredPlaceholders = Sets.newConcurrentHashSet();
    private final Set<String> placeholdersToCheck = Sets.newConcurrentHashSet();

    private static final Pattern PATTERN_PLACEHOLDER = Pattern.compile("%((\\p{Alnum}|_|.|-|@|-)+)%");

    public PlaceholderAPIHook(BungeeTabListPlus bungeeTabListPlus) {
        this.bungeeTabListPlus = bungeeTabListPlus;
        bungeeTabListPlus.getPlugin().getProxy().getScheduler().schedule(bungeeTabListPlus.getPlugin(), () -> bungeeTabListPlus.runInMainThread(this::askServersForPlaceholders), 2, 2, TimeUnit.SECONDS);
    }

    public void addMaybePlaceholder(String s) {
        placeholdersToCheck.add("%" + s + "%");
    }

    public void askServersForPlaceholders() {
        try {
            bungeeTabListPlus.getProxy().getServers().values().stream().filter(Objects::nonNull).forEach(this::askForPlaceholders);
        } catch (ConcurrentModificationException ignored) {

        }
    }

    public void askForPlaceholders(ServerInfo server) {
        if (bungeeTabListPlus.getBridge().get(server, BTLPDataKeys.PLACEHOLDERAPI_PRESENT).orElse(false)) {
            for (String placeholder : placeholdersToCheck) {
                if (!registeredPlaceholders.contains(placeholder) && !server.getPlayers().isEmpty()) {
                    try {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(os);
                        out.writeUTF(Constants.subchannelPlaceholder);
                        out.writeUTF(placeholder);
                        out.close();
                        server.sendData(Constants.channel, os.toByteArray());
                    } catch (Throwable th) {
                        bungeeTabListPlus.reportError(th);
                    }
                }
            }
        }
    }

    public void onPlaceholderConfirmed(String placeholder) {
        if (!registeredPlaceholders.contains(placeholder)) {
            registeredPlaceholders.add(placeholder);
            Placeholder.placeholderAPIDataKeys.put(placeholder.substring(1, placeholder.length() - 1), BTLPDataKeys.createPlaceholderAPIDataKey(placeholder));
            // this implicitly causes a reload
            bungeeTabListPlus.registerPlaceholderProvider0(new PlaceholderProvider() {
                @Override
                public void setup() {
                    bindRegex(Pattern.quote(placeholder)).to((placeholderManager, matcher) -> new SlotTemplate() {
                        @Override
                        public SlotBuilder buildSlot(SlotBuilder builder, TabListContext context) {
                            return builder.appendText(((Player) context.getPlayer()).get(BTLPDataKeys.createPlaceholderAPIDataKey(placeholder)).orElse(""));
                        }
                    });
                }
            });
        }
    }

    public void searchTabList(TabListConfig config) {
        config.header.stream().filter(Objects::nonNull).forEach(this::searchString);
        config.footer.stream().filter(Objects::nonNull).forEach(this::searchString);
        config.playerLines.stream().filter(Objects::nonNull).forEach(this::searchString);
        config.morePlayersLines.stream().filter(Objects::nonNull).forEach(this::searchString);
        config.groupLines.stream().filter(Objects::nonNull).forEach(this::searchString);
        config.tabList.stream().filter(Objects::nonNull).forEach(this::searchString);
    }

    private void searchString(String s) {
        Matcher matcher = PATTERN_PLACEHOLDER.matcher(s);
        while (matcher.find()) {
            placeholdersToCheck.add(matcher.group());
        }
    }
}
