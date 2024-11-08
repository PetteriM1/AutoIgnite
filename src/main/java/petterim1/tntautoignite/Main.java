package petterim1.tntautoignite;

import cn.nukkit.Player;
import cn.nukkit.event.EventPriority;
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
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase implements Listener {

    private int fuse;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fuse = getConfig().getInt("fuse", 80);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
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
                        .putByte("Fuse", fuse);

                Entity.createEntity(EntityPrimedTNT.NETWORK_ID, b.getLevel().getChunk(b.getChunkX(), b.getChunkZ()), nbt).spawnToAll();

                e.setCancelled(true);
                if (!p.isCreative()) {
                    p.getInventory().decreaseCount(p.getInventory().getHeldItemIndex());
                }
            }
        }
    }
}
