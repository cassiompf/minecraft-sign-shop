package gmail.fopypvp174.cmloja.api;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

public class ItemStackSerializable implements ConfigurationSerializable {

    public Material itemMaterial;
    public Integer itemAmount;
    public ItemMeta itemMeta;
    public MaterialData itemMaterialData;
    public Short itemDurability;

    public ItemStackSerializable(ItemStack item) {
        this.itemMaterial = item.getType();
        this.itemAmount = item.getAmount();
        this.itemMeta = item.getItemMeta();
        this.itemMaterialData = item.getData();
        this.itemDurability = item.getDurability();
    }

    public ItemStack deserialize() {
        ItemStack itemStack = new ItemStack(itemMaterial);
        itemStack.getDurability();
        if (itemAmount != null) {
            itemStack.setAmount(itemAmount);
        }
        if (itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
        }
        if (itemDurability != null) {
            itemStack.setDurability(itemDurability);
        }
        if (itemMaterialData != null) {
            itemStack.setData(itemMaterialData);
        }
        return itemStack;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("itemMaterial", itemMaterial);
        map.put("itemAmount", itemAmount);
        map.put("itemMeta", itemMeta);
        map.put("itemMaterialData", itemMaterialData);
        map.put("itemDurability", itemDurability);
        return map;
    }
}
