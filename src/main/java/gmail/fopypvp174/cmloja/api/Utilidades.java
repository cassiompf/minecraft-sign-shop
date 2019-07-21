package gmail.fopypvp174.cmloja.api;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class Utilidades {

    public static Double getPrices(LojaEnum type, Sign sign) {
        String[] linePrice = replace(sign.getLine(2)).toLowerCase().split(":");
        if (type.equals(LojaEnum.COMPRAR)) {
            if (linePrice[0].contains("c")) {
                return Double.valueOf(linePrice[0].replace("c", ""));
            } else if (linePrice.length == 2) {
                return Double.valueOf(linePrice[1].replace("v", ""));
            }
        } else if (type.equals(LojaEnum.VENDER)) {
            if (linePrice[0].contains("v")) {
                return Double.valueOf(linePrice[0].replace("v", ""));
            } else if (linePrice.length == 2) {
                return Double.valueOf(linePrice[1].replace("v", ""));
            }
        }
        return 0.0D;
    }

    public static ItemStack getItemLoja(String[] lines) {
        if (replace(lines[3]).matches("^[1-9](\\d)*(#(\\w){4}){1}(\\s|$)")) {
            return CmLoja.getPlugin(CmLoja.class).getLoja().getCustomConfig().getItemStack("itens." + replace(lines[3]));
        }
        if (replace(lines[3]).matches("^[1-9](\\d)*(:(\\d){1,2}){1}(\\s|$)")) {
            String[] valores = replace(lines[3]).split(":");
            int idType = Integer.parseInt(valores[0]);
            byte dataId = Byte.parseByte(valores[1]);
            return new MaterialData(idType, dataId).toItemStack();
        }
        if (replace(lines[3]).matches("^[1-9](\\d)*(:(\\d){1,5}){1}(\\s|$)")) {
            String[] valores = replace(lines[3]).split(":");
            int idType = Integer.parseInt(valores[0]);
            short dataId = Short.parseShort(valores[1]);
            ItemStack item = new ItemStack(idType, 1);
            item.setDurability(dataId);
            return item;
        }
        int idType = Integer.parseInt(replace(lines[3]));
        return new ItemStack(idType, 1);
    }

    public static boolean isLojaValid(String[] lines) {
        String amount = replace(lines[1]).toLowerCase();
        if (!amount.matches("^[1-9](\\d)*(\\s|$)")) {
            return false;
        }
        String price = replace(lines[2]).toLowerCase();
        if ((!price.matches("^c[1-9](\\d)*:v[1-9](\\d)*(\\s|$)")) &&
                (!price.matches("^c[1-9](\\d)*(\\s|$)")) &&
                (!price.matches("^v[1-9](\\d)*(\\s|$)"))) {
            return false;
        }
        String item = replace(lines[3]);
        return item.matches("^[1-9](\\d)*(:(\\d){1,5}|#(\\w){4,4})?(\\s|$)");
    }

    public static String replace(String price) {
        return price.replace(" ", "").replace("§2", "").replace("§4", "").replace("§0", "").replace("§l", "");
    }

    public static String replaceShopName(String price) {
        return price.replace("§0", "");
    }

    public static String updatePriceSign(String line) {
        if (replace(line).matches("^(?i)c([1-9]){1}(\\d)*:(?i)v([1-9]){1}(\\d)*")) {
            return "§2§lC§r " + replace(line).split(":")[0].replace("C", "").replace("c", "") + " : §4§lV§r " + replace(line).split(":")[1].replace("V", "").replace("v", "");
        }
        if (replace(line).matches("^(?i)c([1-9]){1}(\\d)*(\\s|$)")) {
            return "§2§lC§r " + replace(line).replace("C", "").replace("c", "");
        }
        return "§4§lV§r " + replace(line).replace("V", "").replace("v", "");
    }

    public static boolean haveSlotClearInv(Inventory inventory, ItemStack itemStack, int amount) {
        int quantidade = amount;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                if (itemStack.getMaxStackSize() == 1) {
                    quantidade -= 1;
                } else {
                    quantidade -= 64;
                }
            } else if ((itemStack.isSimilar(item)) &&
                    (itemStack.getMaxStackSize() != 1)) {
                quantidade -= 64 - item.getAmount();
            }
        }
        return quantidade <= 0;
    }

    public static int quantidadeItemInventory(Inventory inventory, ItemStack itemStack) {
        int quantia = 0;
        for (ItemStack item : inventory.getContents()) {
            if ((item != null) && (item.isSimilar(itemStack))) {
                quantia += item.getAmount();
            }
        }
        return quantia;
    }
}