package gmail.fopypvp174.cmloja.config;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Random;

public class LojaConfig extends Config {

    private final String alphabet = "abcdefghijklmnopqrstuvwxqzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int alphabetLenght = alphabet.length();

    public LojaConfig(JavaPlugin plugin, String nome, String defaultName) {
        super(plugin, nome, defaultName);
    }

    public boolean equalsItem(ItemStack itemStack) {
        if (isConfigurationSection("itens")) {
            for (String itens : getConfigurationSection("itens").getKeys(false)) {
                if (getItemStack("itens." + itens).equals(itemStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String nameItem(ItemStack itemStack) {
        if (isConfigurationSection("itens")) {
            for (String itens : getConfigurationSection("itens").getKeys(false)) {
                if (itemStack.equals(getItemStack("itens." + itens))) {
                    return itens;
                }
            }
        }
        return null;
    }

    public boolean isItem(String nome) {
        return isItemStack("itens." + nome);
    }

    public ItemStack getItem(String nome) {
        Map<String, Object> itemSerialize = (Map<String, Object>) get("itens." + nome);
        return ItemStack.deserialize(itemSerialize);
    }


    public void setItem(ItemStack item) {
        ItemStack itemStack = item.clone();
        itemStack.setAmount(1);
        Random r = new Random();
        StringBuilder stringBuilder = new StringBuilder(4);
        for (int i = 0; i < stringBuilder.capacity(); i++) {
            stringBuilder.append(alphabet.charAt(r.nextInt(alphabetLenght)));
        }
        while (isItem(itemStack.getType().getId() + "#" + stringBuilder.toString())) {
            stringBuilder.delete(0, 4);
            for (int i = 0; i < stringBuilder.length(); i++) {
                stringBuilder.append(alphabet.charAt(r.nextInt(alphabetLenght)));
            }
        }

        set("itens." + itemStack.getType().getId() + "#" + stringBuilder.toString(), item.serialize());
        save();
    }
}
