package gmail.fopypvp174.cmloja.config;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class LojaConfig extends Config {

    private String alphabet = "abcdefghijklmnopqrstuvwxqzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int alphabetLenght = alphabet.length();

    public LojaConfig(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public LojaConfig(JavaPlugin plugin, String nome, String defaultName) {
        super(plugin, nome, defaultName);
    }

    public boolean equalsItem(ItemStack itemStack) {
        if (isConfigurationSection("itens")) {
            for (String itens : getConfigurationSection("itens").getKeys(false)) {
                ItemStack itemConfig = getItemStack("itens." + itens);
                if (itemStack.isSimilar(itemConfig)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String nameItem(ItemStack itemStack) {
        if (isConfigurationSection("itens")) {
            for (String itens : getConfigurationSection("itens").getKeys(false)) {
                ItemStack itemConfig = getItemStack("itens." + itens);
                if (itemStack.isSimilar(itemConfig)) {
                    return itens;
                }
            }
        }
        return null;
    }

    public void setItem(ItemStack item) {
        ItemStack itemStack = item.clone();
        itemStack.setAmount(1);
        Random r = new Random();
        StringBuilder stringBuilder = new StringBuilder(4);
        for (int i = 0; i < stringBuilder.capacity(); i++) {
            stringBuilder.append(alphabet.charAt(r.nextInt(alphabetLenght)));
        }
        while (isConfigurationSection("itens." + itemStack.getType().getId() + "#" + stringBuilder.toString())) {
            stringBuilder.delete(0, 4);
            for (int i = 0; i < stringBuilder.length(); i++) {
                stringBuilder.append(alphabet.charAt(r.nextInt(alphabetLenght)));
            }
        }
        set("itens." + itemStack.getType().getId() + "#" + stringBuilder.toString(), itemStack);
        save();
        reload();
    }
}