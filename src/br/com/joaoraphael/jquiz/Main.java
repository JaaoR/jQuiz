package br.com.joaoraphael.jquiz;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Economy econ = null;
	ConsoleCommandSender s = Bukkit.getConsoleSender();
	
	@Override
	public void onEnable() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
			s.sendMessage("§a[jQuiz] Config criada com sucesso.");
		}
		if (!setupEconomy() ) {
            s.sendMessage("§c[jQuiz] Sistema de economia não encontrado.");
            getServer().getPluginManager().disablePlugin(this);
            s.sendMessage("§c[jQuiz] Desativando plugin...");
            return;
        }else {
        	setupEconomy();
        	sendMessages('a');
        }
		getCommand("quiz").setExecutor(new QuizCommand());
		cancelarQuiz();
	}
	
	@Override
	public void onDisable() {
		sendMessages('c');
		cancelarQuiz();
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	
	public static Main getMain() {
		return (Main) Bukkit.getPluginManager().getPlugin("jQuiz");
	}
	
	public void sendMessages(char c){
		String prefixo = "[jQuiz]";
        Bukkit.getConsoleSender().sendMessage("§" + c + prefixo + " Plugin: jQuiz");
        Bukkit.getConsoleSender().sendMessage("§" + c + prefixo + " Desenvolvido por: Jaao");
        Bukkit.getConsoleSender().sendMessage("§" + c + prefixo + " Suporte: @_jaoraphael");
    }
	
	public void cancelarQuiz() {
		getConfig().set("aberto", false);
		getConfig().set("resposta", "padrao-a");
		getConfig().set("pergunta", "padrao-a");
		saveConfig();
	}

}
