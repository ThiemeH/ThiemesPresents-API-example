package nl.thieme.tpe;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.events.custom.PresentOpenEvent;
import nl.thieme.tp.events.custom.PresentSignEvent;
import nl.thieme.tp.events.custom.PresentWrapEvent;
import nl.thieme.tp.models.Present;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin implements Listener {


    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this); // Register events

        Present customPresent = new Present("iron_hoe_present", new ItemStack(Material.IRON_HOE)); // Creating a custom present item
        customPresent.setDisplayName(ChatColor.BOLD + ":thisisfine:");
        customPresent.setLore(Arrays.asList(ChatColor.RED + "Is water literally tasteless...", ChatColor.GOLD + "...or we have defined as a society that the taste of water", ChatColor.BLUE + "is the ground zero of tastes..."));

        ThiemesPresents.getPresentManager().addPresent(customPresent); // Add present to the plugin
        ThiemesPresents.getPresentManager().reloadEssentialItems(); // Add new presents to essentials db IMPORTANT!!
    }
    
    @EventHandler
    public void onPresentOpen(PresentOpenEvent.Pre e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
        p.sendMessage("Nah. You don't get to open a present today");
    }

    @EventHandler
    public void onPresentWrap(PresentWrapEvent.Post e) {
        ItemStack is = e.getPresentItemStack();
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RED + e.getPlayer().getName() + "'s Present!");
        is.setItemMeta(im);
    }

    @EventHandler
    public void onSign(PresentSignEvent.Post e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage("You wrote: " + e.getMessage());
    }

}
