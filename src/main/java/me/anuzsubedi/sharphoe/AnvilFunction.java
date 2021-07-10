package me.anuzsubedi.sharphoe;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Objects;

public class AnvilFunction implements Listener {
    EnchantmentStorageMeta enchantMeta;


    @EventHandler
    public void onEnchantEvent(PrepareAnvilEvent e){
        ItemStack firstSlot = e.getInventory().getItem(0);
        ItemStack secondSlot = e.getInventory().getItem(1);


        if(areCorrectItems(firstSlot,secondSlot)){
            if (secondSlot != null) {
                enchantMeta = (EnchantmentStorageMeta) secondSlot.getItemMeta();
            }
            if (enchantMeta != null) {
                int enchantLevel = enchantMeta.getStoredEnchantLevel(Enchantment.DAMAGE_ALL);
                ItemStack finalHoe = null;
                if (firstSlot != null) {
                    finalHoe = firstSlot.clone();
                }
                if (finalHoe != null) {
                    finalHoe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,enchantLevel);
                }
                e.setResult(finalHoe);
            }
        }
    }

    @EventHandler
    public void moveToInventory(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        ItemStack firstSlot = Objects.requireNonNull(e.getClickedInventory()).getItem(0);
        ItemStack secondSlot = e.getClickedInventory().getItem(1);
        ItemStack thirdSlot = e.getClickedInventory().getItem(2);
        if(e.getRawSlot()==2){
            try{
                AnvilInventory anvilInventory = (AnvilInventory) e.getClickedInventory();
                if(firstSlot!=null && secondSlot!=null && thirdSlot!=null && areCorrectItems(firstSlot,secondSlot)){
                    if(!giveItemToPlayer(player,thirdSlot,e.isShiftClick()))
                        return;
                    anvilInventory.clear();
                }
            } catch (Exception ignored) {
            }
        }
    }

    public boolean isHoe(ItemStack item){
        Material mat = item.getType();
        return mat.name().endsWith("_HOE");
    }

    public boolean isSharpnessBook(ItemStack item){
        if(item.hasItemMeta()){
            try{
                enchantMeta = (EnchantmentStorageMeta) item.getItemMeta();
                if (enchantMeta != null && enchantMeta.hasStoredEnchant(Enchantment.DAMAGE_ALL)) {
                    return true;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }
        return false;
    }

    public boolean areCorrectItems(ItemStack item1, ItemStack item2){
        if(item1!=null && item2!=null){
            if(isHoe(item1)){
                return isSharpnessBook(item2);
            }
        }
        return false;
    }

    public boolean giveItemToPlayer(final Player player, final ItemStack item, final boolean direct){
        System.out.println(direct);
        if(direct){
            if(player.getInventory().firstEmpty()==-1){
                System.out.println("Couldn't add to inventory");
                return false;
            }
            else {
                player.getInventory().addItem(item);
                player.updateInventory();
                return true;
            }
        }
        else
            player.setItemOnCursor(item);
        return true;
    }
}