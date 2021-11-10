package idk.plugin.tnt;

import cn.nukkit.Player;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.entity.Entity;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase implements Listener {

    private int fuse;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fuse = getConfig().getInt("fuse", 80);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("tnt.autoignite")) {
            Block b = e.getBlock();
            if (b instanceof BlockTNT) {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", b.x + 0.5))
                                .add(new DoubleTag("", b.y))
                                .add(new DoubleTag("", b.z + 0.5)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0)))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", 0))
                                .add(new FloatTag("", 0)))
                        .putShort("Fuse", fuse);
                new EntityPrimedTNT(b.getLevel().getChunk(b.getFloorX() >> 4, b.getFloorZ() >> 4), nbt).spawnToAll();
                b.getLevel().addLevelEvent(b, LevelEventPacket.EVENT_SOUND_TNT);
                e.setCancelled(true);
                if (!p.isCreative()) {
                    p.getInventory().decreaseCount(p.getInventory().getHeldItemIndex());
                }
            }
        }
    }
}
