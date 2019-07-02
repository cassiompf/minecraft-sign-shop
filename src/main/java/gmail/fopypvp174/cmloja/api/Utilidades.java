package gmail.fopypvp174.cmloja.api;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utilidades {
    private static CmLoja plugin = CmLoja.getPlugin(CmLoja.class);

    public static final Double getPrices(LojaEnum type, Sign placa) {
        String[] linhaPreço = replace(placa.getLine(2)).toLowerCase().split(":");
        if (type.equals(LojaEnum.COMPRAR)) {
            if (linhaPreço[0].contains("c")) {
                return Double.valueOf(linhaPreço[0].replace("c", "")).doubleValue();
            } else if (linhaPreço.length == 2) {
                return Double.valueOf(linhaPreço[1].replace("v", "")).doubleValue();
            }
        } else if (type.equals(LojaEnum.VENDER)) {
            if (linhaPreço[0].contains("v")) {
                return Double.valueOf(linhaPreço[0].replace("v", "")).doubleValue();
            } else if (linhaPreço.length == 2) {
                return Double.valueOf(linhaPreço[1].replace("v", "")).doubleValue();
            }
        }
        return 0.0D;
    }

    @Deprecated
    public static final ItemStack getItemLoja(String[] linha) {
        if (replace(linha[3]).matches("^[1-9](\\d)*(\\#(\\w){4}){1}(\\s|$)")) {
            ItemStack item = CmLoja.getPlugin(CmLoja.class).getLoja().getCustomConfig().getItemStack("itens." + replace(linha[3]));
            if (item == null) {
                return null;
            }
            item.setAmount(1);
            return item;
        }
        if (replace(linha[3]).matches("^[1-9](\\d)*(\\:(\\d){1,2}){1}(\\s|$)")) {
            String[] valores = replace(linha[3]).split(":");
            int idType = Integer.parseInt(valores[0]);
            byte dataId = Byte.parseByte(valores[1]);
            ItemStack item = new ItemStack(idType, 1, (short) 0, Byte.valueOf(dataId));
            return item;
        }
        int idType = Integer.parseInt(replace(linha[3]));
        ItemStack item = new ItemStack(idType, 1);
        return item;
    }

    public static final boolean isLojaValid(String[] linhas) {
        String quantidade = replace(linhas[1]).toLowerCase();
        if (!quantidade.matches("^[1-9](\\d)*(\\s|$)")) {
            return false;
        }
        String preço = replace(linhas[2]).toLowerCase();
        if ((!preço.matches("^c[1-9](\\d)*:v[1-9](\\d)*(\\s|$)")) &&
                (!preço.matches("^c[1-9](\\d)*(\\s|$)")) &&
                (!preço.matches("^v[1-9](\\d)*(\\s|$)"))) {
            return false;
        }
        String item = replace(linhas[3]);
        return item.matches("^[1-9](\\d)*(\\:(\\d){1,2}|\\#(\\w){4,4})?(\\s|$)");
    }

    public static final String replace(String valor) {
        return valor.replace(" ", "").replace("§2", "").replace("§4", "").replace("§0", "");
    }

    public static final String updatePriceSign(String linha) {
        if (replace(linha).matches("^(?i)c([1-9]){1}(\\d)*:(?i)v([1-9]){1}(\\d)*")) {
            return "§2C§r " + replace(linha).split(":")[0].replace("C", "").replace("c", "") + " : §4V§r " + replace(linha).split(":")[1].replace("V", "").replace("v", "");
        }
        if (replace(linha).matches("^(?i)c([1-9]){1}(\\d)*(\\s|$)")) {
            return "§2C§r " + replace(linha).replace("C", "").replace("c", "");
        }
        return "§4V§r " + replace(linha).replace("V", "").replace("v", "");
    }

    public static final boolean temEspacoInvParaItem(Inventory inventory, ItemStack itemStack, int amount) {
        int quantidade = amount;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                quantidade -= 64;
            } else if ((itemStack.isSimilar(item)) &&
                    (itemStack.getMaxStackSize() != 1)) {
                quantidade -= 64 - item.getAmount();
            }
        }
        return quantidade <= 0;
    }

    public static final int quantidadeItemInventory(Inventory inventory, ItemStack itemStack) {
        int quantia = 0;
        for (ItemStack item : inventory.getContents()) {
            if ((item != null) &&
                    (item.isSimilar(itemStack))) {
                quantia += item.getAmount();
            }
        }
        return quantia;
    }
}