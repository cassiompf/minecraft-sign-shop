package gmail.fopypvp174.cmloja.utilities;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class Utilidades {
    private static CmLoja plugin = CmLoja.getPlugin(CmLoja.class);

    public static final int getPrices(LojaEnum type, Sign placa) {
        String[] linhaPreço = replace(placa.getLine(2)).toLowerCase().split(":");

        if (type.equals(LojaEnum.COMPRAR)) {
            if (linhaPreço[0].contains("c")) {
                return Integer.valueOf(linhaPreço[0].replace("c", ""));
            }
        } else if (type.equals(LojaEnum.VENDER)) {
            if (linhaPreço[0].contains("v")) {
                return Integer.valueOf(linhaPreço[0].replace("v", ""));
            }
            if (linhaPreço.length == 2) {
                return Integer.valueOf(linhaPreço[1].replace("v", ""));
            }
        }
        return 0;
    }

    public static final ItemStack getItemLoja(String[] linha) {
        ItemStack item;
        String linhaItem = replace(linha[3]);
        if (linhaItem.matches("^[1-9](\\d)*(\\#(\\w){4}){1}(\\s|$)")) {
            item = plugin.getLoja().getCustomConfig().getItemStack("itens." + linhaItem);
            item.setAmount(1);
            return item;
        } else if (linhaItem.matches("^[1-9](\\d)*(\\:(\\d){1,2}){1}(\\s|$)")) {
            String[] valores = linhaItem.split(":");
            int idType = Integer.parseInt(valores[0]);
            byte dataId = Byte.parseByte(valores[1]);
            item = new ItemStack(idType, 1, (short) 0, dataId);
            return item;
        } else {
            int idType = Integer.parseInt(linhaItem);
            item = new ItemStack(idType, 1);
            return item;
        }
    }

    public static final boolean isLojaValid(String[] linhas) {
        String quantidade = replace(linhas[1]).toLowerCase();
        if (!quantidade.matches("^[1-9](\\d)*(\\s|$)")) {
            return false;
        }

        String preço = replace(linhas[2]).toLowerCase();
        if (!preço.matches("^c[1-9](\\d)*:v[1-9](\\d)*(\\s|$)")) {
            if ((!preço.matches("^c[1-9](\\d)*(\\s|$)")) &&
                    (!preço.matches("^v[1-9](\\d)*(\\s|$)"))) {
                return false;
            }
        }

        String item = replace(linhas[3]);
        return item.matches("^[1-9](\\d)*(\\:(\\d){1,2}|\\#(\\w){4,4})?(\\s|$)");
    }

    public static final String replace(String valor) {
        return valor.replace(" ", "")
                .replace("§2", "")
                .replace("§4", "")
                .replace("§r", "")
                .replace("§0", "");
    }

    public static final String updatePriceSign(String linha) {
        String[] linhaUpdate = replace(linha).toLowerCase().split(":");
        StringBuilder stringBuilder = new StringBuilder();
        if (linhaUpdate.length == 1) {
            if (linhaUpdate[0].contains("c")) {
                stringBuilder.append("§2C§r " + linhaUpdate[0].replace("c", ""));
            }
            if (linhaUpdate[0].contains("v")) {
                stringBuilder.append("§4V§r " + linhaUpdate[0].replace("v", ""));
            }
        }
        if (linhaUpdate.length == 2) {
            stringBuilder.append("§2C§r " + linhaUpdate[0].replace("c", "") + " : " +
                    "§4V§r " + linhaUpdate[1].replace("v", ""));
        }
        return stringBuilder.toString();
    }

    public static final boolean temEspacoInvParaItem(Inventory inventory, ItemStack itemStack, int amount) {
        int quantidade = amount;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (quantidade <= 0) {
                return true;
            }
            if (item == null) {
                quantidade -= 64;
                continue;
            }
            if (!itemStack.isSimilar(item)) {
                continue;
            }
            if (itemStack.getMaxStackSize() != 1) {
                quantidade -= (64 - item.getAmount());
            }
        }
        return false;
    }

    public static final int quantidadeItemInventory(Inventory inventory, ItemStack itemStack) {
        int quantia = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                continue;
            }
            if (item.isSimilar(itemStack)) {
                quantia += item.getAmount();
            }
        }
        return quantia;
    }
}