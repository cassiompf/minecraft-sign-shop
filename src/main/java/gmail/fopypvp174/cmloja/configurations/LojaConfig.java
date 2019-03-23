package gmail.fopypvp174.cmloja.configurations;

import gmail.fopypvp174.cmloja.CmLoja;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public final class LojaConfig extends Config {

    private String alphabet = "abcdefghijklmnopqrstuvwxqzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int alphabetLenght = alphabet.length();

    public LojaConfig(CmLoja plugin, String fileName) {
        super(plugin, fileName);
    }

    public boolean equalsItem(ItemStack itemStack) {
        if (getCustomConfig().isConfigurationSection("itens")) {
            for (String itens : getCustomConfig().getConfigurationSection("itens").getKeys(false)) {
                ItemStack itemConfig = getCustomConfig().getItemStack("itens." + itens);
                if (itemStack.isSimilar(itemConfig)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String nameItem(ItemStack itemStack) {
        if (getCustomConfig().isConfigurationSection("itens")) {
            for (String itens : getCustomConfig().getConfigurationSection("itens").getKeys(false)) {
                ItemStack itemConfig = getCustomConfig().getItemStack("itens." + itens);
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
        while (getCustomConfig().isConfigurationSection("itens." + itemStack.getType().getId() + "#" + stringBuilder.toString())) {
            stringBuilder.delete(0, 4);
            for (int i = 0; i < stringBuilder.length(); i++) {
                stringBuilder.append(alphabet.charAt(r.nextInt(alphabetLenght)));
            }
        }
        getCustomConfig().set("itens." + itemStack.getType().getId() + "#" + stringBuilder.toString(), itemStack);
        saveConfig();
        reloadConfig();
    }
}