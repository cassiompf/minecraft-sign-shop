package gmail.fopypvp174.cmloja.api;

import gmail.fopypvp174.cmloja.Main;
import gmail.fopypvp174.cmloja.listeners.LojaEnum;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

public final class Utilidades {
    private static TextComponent message = null;

    public final static int valores(LojaEnum type, Sign placa) {
        String[] linha = Utilidades.replace(placa.getLine(2)).replace("C", "").replace("V", "").split(":");
        if (type.equals(LojaEnum.COMPRAR)) {
            return Integer.valueOf(linha[0]);
        }
        //linha 1 = vender
        return Integer.valueOf(linha[1]);
    }

    public final static ItemStack itemLoja(String[] linha) {
        ItemStack item;
        if (replace(linha[3]).matches("(\\d)+(\\#(\\w){4}){1}(\\s|$)")) {
            item = Main.loja.getItem(replace(linha[3]));
            item.setAmount(Integer.parseInt(replace(linha[1])));
            return item;
        } else if (replace(linha[3]).matches("(\\d)+(\\:(\\d){1,2}){1}(\\s|$)")) {
            String[] valores = replace(linha[3]).split(":");
            item = new ItemStack(Integer.parseInt(valores[0]), Integer.parseInt(replace(linha[1])), Byte.parseByte(valores[1]));
            return item;
        } else {
            item = new ItemStack(Integer.parseInt(linha[3]), Integer.parseInt(replace(linha[1])));
            return item;
        }
    }

    public final static boolean isLoja(String[] valores) {
        if (valores[1].matches("([0-9])+(\\s|$)")) {
            if (replace(valores[2]).matches("^(?i)c(\\d)+:(?i)v(\\d)+(\\s|$)")) {
                if (replace(valores[3]).matches("(\\d)+(\\:(\\d){1,2}|\\#(\\w){4,4})?(\\s|$)")) {
                    return true;
                }
            }
        }
        return false;
    }

    public final static String replace(String valor) {
        return valor.replace(" ", "").replace("§2", "").replace("§4", "").replace("§0", "");
    }

    public static void darMoneyVault(OfflinePlayer p, double quantia) {
        EconomyResponse r = Main.econ.depositPlayer(p, quantia);
        if (!r.transactionSuccess()) {
            throw new RuntimeException("Erro ao dar o dinheiro do jogador " + p.getName() + ", consulte o desenvolvedor do plugin!");
        }
    }

    public static void removeMoneyVault(OfflinePlayer p, double quantia) {
        EconomyResponse r = Main.econ.withdrawPlayer(p, quantia);
        if (!r.transactionSuccess()) {
            throw new RuntimeException("Erro ao remover o dinheiro do jogador " + p.getName() + ", consulte o desenvolvedor do plugin!");
        }
    }
}
