package gmail.fopypvp174.cmloja.api;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utilidades {
    private static CmLoja plugin = CmLoja.getPlugin(CmLoja.class);

    public static final int getPrices(LojaEnum type, Sign placa) {
        String[] linha = new String[2];
        boolean comprar = false;
        boolean vender = false;
        if (replace(placa.getLine(2)).matches("^(?i)c(\\d)+:(?i)v(\\d)+(\\s|$)")) {
            linha = replace(placa.getLine(2)).replace("C", "").replace("V", "").split(":");
            comprar = true;
            vender = true;
        } else if (replace(placa.getLine(2)).matches("^(?i)c(\\d)+(\\s|$)")) {
            linha[0] = replace(placa.getLine(2)).replace("C", "");
            comprar = true;
        } else if (replace(placa.getLine(2)).matches("^(?i)v(\\d)+(\\s|$)")) {
            linha[1] = replace(placa.getLine(2)).replace("V", "");
            vender = true;
        }

        if (type.equals(LojaEnum.COMPRAR)) {
            if (comprar == true) {
                return Integer.valueOf(linha[0]);
            }
        } else if (type.equals(LojaEnum.VENDER)) {
            if (vender == true) {
                return Integer.valueOf(linha[1]);
            }
        }
        return 0;
    }

    public static final ItemStack getItemLoja(String[] linha) {
        ItemStack item;
        if (replace(linha[3]).matches("^[1-9](\\d)*(\\#(\\w){4}){1}(\\s|$)")) {
            item = plugin.getLoja().getItemDeserialize("itens." + replace(linha[3]));
            if (item == null) {
                return null;
            }
            item.setAmount(1);
            return item;
        } else if (replace(linha[3]).matches("^[1-9](\\d)*(\\:(\\d){1,2}){1}(\\s|$)")) {
            String[] valores = replace(linha[3]).split(":");
            int idType = Integer.parseInt(valores[0]);
            byte dataId = Byte.parseByte(valores[1]);
            item = new ItemStack(idType, 1, (short) 0, dataId);
            return item;
        } else {
            int idType = Integer.parseInt(replace(linha[3]));
            item = new ItemStack(idType, 1);
            return item;
        }
    }

    public static final boolean isLojaValid(String[] linhas) {
        String[] valores = linhas;
        if (!replace(valores[1]).matches("(^[1-9])(\\d)*(\\s|$)")) {
            return false;
        }

        if (!replace(valores[2]).matches("(^(?i)c){1}([1-9]){1}(\\d)*:(?i)v([1-9]){1}(\\d)*")) {
            if ((!replace(valores[2]).matches("(^(?i)c){1}([1-9]){1}(\\d)*(\\s|$)")) &&
                    (!replace(valores[2]).matches("(^(?i)v){1}([1-9]){1}(\\d)*(\\s|$)"))) {
                return false;
            }
        }

        return replace(valores[3]).matches("^[1-9](\\d)*(\\:(\\d){1,2}|\\#(\\w){4,4})?(\\s|$)");
    }

    public static final String replace(String valor) {
        return valor.replace(" ", "").replace("§2", "").replace("§4", "").replace("§0", "");
    }

    public static final String updatePriceSign(String linha) {
        if (replace(linha).matches("^(?i)c([1-9]){1}(\\d)*:(?i)v([1-9]){1}(\\d)*")) {
            return "§2C§r " + replace(linha).split(":")[0].replace("C", "").replace("c", "") + " : §4V§r " + replace(linha).split(":")[1].replace("V", "").replace("v", "");
        } else if (replace(linha).matches("^(?i)c([1-9]){1}(\\d)*(\\s|$)")) {
            return "§2C§r " + replace(linha).replace("C", "").replace("c", "");
        } else {
            return "§4V§r " + replace(linha).replace("V", "").replace("v", "");
        }
    }

    public static final boolean temEspacoInvParaItem(Inventory inventory, ItemStack itemStack, int amount) {
        int quantidade = amount;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                quantidade -= 64;
            }
            if (itemStack.isSimilar(item)) {
                quantidade -= (64 - item.getAmount());
            }
            if (quantidade <= 0) {
                return true;
            }
        }
        return false;
    }

    public static final int quantidadeItemInventory(Inventory inventory, ItemStack itemStack) {
        int quantia = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                if (item.isSimilar(itemStack)) {
                    quantia += item.getAmount();
                }
            }
        }
        return quantia;
    }
}