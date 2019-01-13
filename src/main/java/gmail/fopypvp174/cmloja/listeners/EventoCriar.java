package gmail.fopypvp174.cmloja.listeners;

import gmail.fopypvp174.cmloja.CmLoja;
import gmail.fopypvp174.cmloja.api.Utilidades;
import gmail.fopypvp174.cmloja.exceptions.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class EventoCriar implements Listener {

    private CmLoja plugin = CmLoja.getPlugin(CmLoja.class);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCriar(SignChangeEvent e) {
        Player jogador = e.getPlayer();

        Sign sign = (Sign) e.getBlock().getState();

        if (!Utilidades.isLojaValid(e.getLines())) {
            return;
        }

        try {
            createSignLoja(jogador, e.getLines(), sign);
        } catch (CreateSignNickOtherPlayerException error) {
            e.getBlock().breakNaturally(null);
            jogador.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro3"));
            return;
        } catch (CreateSignPlayerWithoutPermissionException error) {
            e.getBlock().breakNaturally(null);
            jogador.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro5"));
            return;
        } catch (CreateSignServerWithoutPermissionException error) {
            e.getBlock().breakNaturally(null);
            jogador.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro4"));
            return;
        } catch (CreateSignItemInvalidException error) {
            e.getBlock().breakNaturally(null);
            jogador.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro2"));
            return;
        } catch (CreateSignWithoutChestException error) {
            e.getBlock().breakNaturally(null);
            jogador.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro1"));
            return;
        } catch (CreateSignServerOnChestException error) {
            e.getBlock().breakNaturally(null);
            jogador.sendMessage(plugin.getMessageConfig().message("mensagens.criar_erro6"));
            return;
        }

        e.setLine(2, Utilidades.updatePriceSign(e.getLine(2)));
        jogador.sendMessage(plugin.getMessageConfig().message("mensagens.criar_success"));
    }


    public void createSignLoja(Player jogador, String[] linhas, Sign placa) throws CreateSignPlayerWithoutPermissionException, CreateSignWithoutChestException,
            CreateSignItemInvalidException, CreateSignNickOtherPlayerException, CreateSignServerWithoutPermissionException, CreateSignServerOnChestException {
        if ((!jogador.hasPermission("loja.admin")) || (!jogador.hasPermission("loja.jogador"))) {
            throw new CreateSignPlayerWithoutPermissionException("O jogador " + jogador.getName() + " tentou criar loja sem permissão.");
        }

        if (Utilidades.getItemLoja(linhas) == null) {
            throw new CreateSignItemInvalidException("O jogador " + jogador.getName() + " tentou criar uma loja com um item inválido: " + linhas[3]);
        }

        Block block = placa.getBlock().getRelative(((org.bukkit.material.Sign) placa.getData()).getAttachedFace());
        if (!linhas[0].equals(plugin.getMessageConfig().message("placa.nomeLoja"))) {
            if ((!block.getType().equals(Material.CHEST)) && (!block.getType().equals(Material.TRAPPED_CHEST))) {
                throw new CreateSignWithoutChestException("O jogador " + jogador.getName() + " tentou criar uma loja fora do baú.");
            }

            if (!linhas[0].equals(jogador.getName())) {
                throw new CreateSignNickOtherPlayerException("O jogador " + jogador.getName() + " tentou criar uma loja com o nick de outro jogador.");
            }
            return;
        }

        if ((block.getType().equals(Material.CHEST)) || (block.getType().equals(Material.TRAPPED_CHEST))) {
            throw new CreateSignServerOnChestException("O jogador " + jogador.getName() + " tentou criar uma loja do servidor em um baú.");
        }

        if (!jogador.hasPermission("loja.admin")) {
            throw new CreateSignServerWithoutPermissionException("O jogador " + jogador.getName() + " tentou criar uma loja com o nome do servidor sem permissão.");
        }
    }
}
