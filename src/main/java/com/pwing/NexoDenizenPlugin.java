package com.pwing;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.tags.TagManager;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class NexoDenizenPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Initialize Denizen and Nexo integration
        getLogger().info("NexoDenizenPlugin has been enabled");

        // Register tags
        registerTags();
    }

    @Override
    public void onDisable() {
        getLogger().info("NexoDenizenPlugin has been disabled");
    }

    private void registerTags() {
        // Register NexoItems.itemFromId tag
        TagManager.registerTagHandler(ItemTag.class, "nexo_item_from_id", (attribute) -> {
            if (!attribute.hasContext(1)) {
                return null;
            }
            String itemId = attribute.getContext(1);
            ItemStack itemStack = NexoItems.itemFromId(itemId).build();
            return new ItemTag(itemStack);
        });

    }
}
