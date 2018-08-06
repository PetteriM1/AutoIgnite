package idk.plugin.tnt;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.entity.Entity;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Sound;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
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
                .putShort("Fuse", 80);
            Entity tnt = new EntityPrimedTNT(b.getLevel().getChunk(b.getFloorX() >> 4, b.getFloorZ() >> 4), nbt);
            tnt.spawnToAll();
            b.level.addSound(b, Sound.RANDOM_FUSE);
            e.setCancelled(true);
            if (!e.getPlayer().isCreative()) e.getPlayer().getInventory().removeItem(Item.get(Item.TNT, 0, 1));
        }
    }
}
