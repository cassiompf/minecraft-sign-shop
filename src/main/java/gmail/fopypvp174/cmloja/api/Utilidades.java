package gmail.fopypvp174.cmloja.api;

import gmail.fopypvp174.cmloja.Main;
import gmail.fopypvp174.cmloja.enums.LojaEnum;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

public class Utilidades {
    private TextComponent message = null;
    private Main plugin = Main.getPlugin(Main.class);

    public final int valores(LojaEnum type, Sign placa) {
        String[] linha = new String[2];
        boolean comprar = false;
        boolean vender = false;
        if (replace(placa.getLine(2)).matches("^(?i)c(\\d)+(\\s|$)")) {
            linha[0] = replace(placa.getLine(2)).replace("C", "");
            comprar = true;
        }
        if (replace(placa.getLine(2)).matches("^(?i)v(\\d)+(\\s|$)")) {
            linha[1] = replace(placa.getLine(2)).replace("V", "");
            vender = true;
        }
        if (replace(placa.getLine(2)).matches("^(?i)c(\\d)+:(?i)v(\\d)+(\\s|$)")) {
            linha = replace(placa.getLine(2)).replace("C", "").replace("V", "").split(":");
            comprar = true;
            vender = true;
        }
        if (type.equals(LojaEnum.COMPRAR)) {
            if (comprar == true) {
                return Integer.valueOf(linha[0]);
            }
        }
        if (type.equals(LojaEnum.VENDER)) {
            if (vender == true) {
                return Integer.valueOf(linha[1]);
            }
        }
        return 0;
    }

    public final ItemStack itemLoja(String[] linha) {
        ItemStack item;
        if (replace(linha[3]).matches("(\\d)+(\\#(\\w){4}){1}(\\s|$)")) {
            item = plugin.getLoja().getItem(replace(linha[3]));
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

    public final boolean isLoja(String[] valores) {
        if (valores[1].matches("([0-9])+(\\s|$)")) {
            if (replace(valores[2]).matches("^(?i)c(\\d)+:(?i)v(\\d)+(\\s|$)") ||
                    replace(valores[2]).matches("^(?i)c(\\d)+(\\s|$)") ||
                    replace(valores[2]).matches("^(?i)v(\\d)+(\\s|$)")) {
                return replace(valores[3]).matches("(\\d)+(\\:(\\d){1,2}|\\#(\\w){4,4})?(\\s|$)");
            }
        }
        return false;
    }

    public final String replace(String valor) {
        return valor.replace(" ", "").replace("§2", "").replace("§4", "").replace("§0", "");
    }

    public void darMoneyVault(OfflinePlayer p, double quantia) {
        EconomyResponse r = plugin.getEcon().depositPlayer(p, quantia);
        if (!r.transactionSuccess()) {
            throw new RuntimeException("Erro ao dar o dinheiro do jogador " + p.getName() + ", consulte o desenvolvedor do plugin!");
        }
    }

    public void removeMoneyVault(OfflinePlayer p, double quantia) {
        EconomyResponse r = plugin.getEcon().withdrawPlayer(p, quantia);
        if (!r.transactionSuccess()) {
            throw new RuntimeException("Erro ao remover o dinheiro do jogador " + p.getName() + ", consulte o desenvolvedor do plugin!");
        }
    }

    public boolean itemValido(String valor) {
        if (valor.matches("(\\d)+(\\#(\\w){4,4}){1}(\\s|$)")) {
            System.out.println(valor);
            return plugin.getLoja().isItem(valor);
        } else if (valor.matches("(\\d)+(\\:(\\d){1,2}){1}(\\s|$)")) {
            String[] item = valor.split(":");
            return Short.parseShort(item[1]) <= 15;
        }
        return true;
    }

    public boolean checarBau(Block b) {
        Block bau = b.getRelative(((org.bukkit.material.Sign) b.getState().getData()).getAttachedFace());
        return (b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))
                && (bau.getType().equals(Material.CHEST) || bau.getType().equals(Material.TRAPPED_CHEST));
    }

    public String updatePlaca(int position, String linha) {
        String valor = null;
        if (position == 2) {
            String[] CeV = plugin.getUtilidades().replace(linha).replace("v", "").replace("V", "").replace("c", "").replace("C", "").split(":");
            if (CeV[0].matches("^(0)+(\\s|$)")) {
                valor = "§4V§r " + CeV[1];
            } else if (CeV[1].matches("^(0)+(\\s|$)")) {
                valor = "§2C§r " + CeV[0];
            } else {
                valor = "§2C§r " + CeV[0] + " : " + "§4V§r " + CeV[1];
            }
        }
        return valor;
    }
}
