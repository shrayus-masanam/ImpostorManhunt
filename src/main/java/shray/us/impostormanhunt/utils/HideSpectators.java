package shray.us.impostormanhunt.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import shray.us.impostormanhunt.Main;
import shray.us.impostormanhunt.structures.Competitor;
import shray.us.impostormanhunt.structures.Game;

import java.util.ArrayList;
import java.util.List;

public class HideSpectators {
    public HideSpectators(Main plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {

                @Override
                public void onPacketSending(PacketEvent event) {
                    PacketContainer packet = event.getPacket();

                    // https://www.spigotmc.org/threads/manipulating-player-info-update-packet-1-19-3-protocollib-
                    // displayname-chat-becomes-invalid.588257/
                    List<PlayerInfoData> original = new ArrayList<>(packet.getPlayerInfoDataLists().read(1));

                    boolean changed = false;
                    for (int i = 0; i < original.size(); i++) {
                        PlayerInfoData data = original.get(i);
                        Player p = Bukkit.getPlayer(data.getProfileId());
                        if (p == null) continue;
                        if (p.getUniqueId().equals(event.getPlayer().getUniqueId()))
                            continue; // dont manipulate a packet about ourself
                        Competitor c = Game.getCompetitor(p);
                        if (c == null) continue;
                        if (data.getGameMode() != EnumWrappers.NativeGameMode.SPECTATOR) continue;
                        PlayerInfoData disguised = new PlayerInfoData(
                            data.getProfileId(),
                            data.getLatency(),
                            data.isListed(),
                            EnumWrappers.NativeGameMode.SURVIVAL, // spoof to survival
                            data.getProfile(),
                            null,
                            data.getProfileKeyData()
                        );
                        original.set(i, disguised);
                        changed = true;
                    }

                    if (changed)
                        packet.getPlayerInfoDataLists().write(1, original);
                }

            }
        );
    }
}
