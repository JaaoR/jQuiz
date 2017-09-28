package br.com.joaoraphael.jquiz;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitScheduler;


public class QuizCommand implements CommandExecutor {
	
	private final Main c = Main.getMain();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cApenas jogadores podem executar esse comando.");
		}else if (sender instanceof Player){
			
			Player p = (Player)sender;
			String permp, perma;
			permp = "jquiz.player";
			perma = "jquiz.admin";
			
			if(cmd.getName().equalsIgnoreCase("quiz")) {
				if ((p.hasPermission(permp) || p.hasPermission(perma))) {
					if (args.length == 0) {
						if (!c.getConfig().getString("pergunta").equalsIgnoreCase("padrao-a")) {
							for (String msg : c.getConfig().getStringList("quiz_aberto")) {
								p.sendMessage(msg.replace("&", "§").replace("{pergunta}", c.getConfig().getString("pergunta")));
							}
						}else {
							p.sendMessage(c.getConfig().getString("quiz_fechado").replace("&", "§"));
						}
					}
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("help")) {
							if (p.hasPermission(permp) && !(p.hasPermission(perma))) {
								for (String help : c.getConfig().getStringList("ajuda_player")) {
									p.sendMessage(help.replace("&", "§"));
								}
								}else if (p.hasPermission(perma)) {
									for (String help : c.getConfig().getStringList("ajuda_admin")) {
										p.sendMessage(help.replace("&", "§"));
									}
									
								}
							}
						
						if (args[0].equalsIgnoreCase("dev")) {
							p.sendMessage("§6jQuiz - Plugin");
							p.sendMessage("§eDesenvolvido por: Jaao");
							p.sendMessage("§eTwitter: twitter.com/_jaoraphael");
							
						}
						if (args[0].equalsIgnoreCase("iniciar")) {
							if (p.hasPermission(perma)) {
								if (c.getConfig().getBoolean("aberto") == true) {
									p.sendMessage(c.getConfig().getString("ja_aberto").replace("&", "§"));
								}else {
									p.sendMessage("§cUso: /quiz iniciar (pergunta)");
								}
							}else {
								p.sendMessage("§cVocê não tem permissão para executar esse comando.");
							}
						}
						if (args[0].equalsIgnoreCase("resposta")) {
							if (p.hasPermission(perma)) {
								if (!c.getConfig().getString("pergunta").equalsIgnoreCase("padrao-a")) {
									p.sendMessage("§cUso: /quiz resposta (resposta)");
								}else {
									p.sendMessage(c.getConfig().getString("ja_def_resposta").replace("&", "§"));
								}
							}else {
								p.sendMessage("§cVocê não tem permissão para executar esse comando.");
							}
						}
						if (args[0].equalsIgnoreCase("cancelar")) {
							if (p.hasPermission(perma)) {
								if (c.getConfig().getBoolean("aberto") == true) {
									c.getConfig().set("aberto", false);
									c.getConfig().set("resposta", "padrao-a");
									c.getConfig().set("pergunta", "padrao-a");
									c.saveConfig();
									
									for (String cancelado : c.getConfig().getStringList("cancelado")) {
										for (Player all : Bukkit.getOnlinePlayers()) {
											if (all.hasPermission(permp) || all.hasPermission(perma)) {
												all.sendMessage(cancelado.replace("&", "§"));
											}
										}
									}
									p.sendMessage(c.getConfig().getString("cancelado").replace("&", "§"));
								}else {
									p.sendMessage(c.getConfig().getString("cancelado_fechado").replace("&", "§"));
								}
							}else {
								p.sendMessage("§cVocê não tem permissão para executar esse comando.");
							}
						}
						if (args[0].equalsIgnoreCase("responder")) {
							if (p.hasPermission(perma)) {
								p.sendMessage(c.getConfig().getString("admin_nresp").replace("&", "§"));
							}else {
								if (c.getConfig().getBoolean("aberto") == false) {
									p.sendMessage(c.getConfig().getString("fechado_resp").replace("&", "§"));
								}else {
									p.sendMessage("§cUso: /quiz responder (resposta)");
								}
							}
						}
					}
					if (args.length > 1) {
						if (args[0].equalsIgnoreCase("iniciar")) {
							if (p.hasPermission(perma)) {
								if (c.getConfig().getBoolean("aberto") == false) {
									String perg = "";
			                        for (int c = 1;c < args.length; c++){
			                        	perg = perg + args[c] + " ";
			                        }
			                        perg = ChatColor.translateAlternateColorCodes('&', perg);
			                        c.getConfig().set("pergunta", perg);
			                        c.saveConfig();
			                        p.sendMessage("§ePergunta: §e" + c.getConfig().getString("pergunta").replace("&", "§"));
			                        p.sendMessage(c.getConfig().getString("def_resposta").replace("&", "§"));
								}else {
									p.sendMessage(c.getConfig().getString("ja_aberto").replace("&", "§"));
								}
							}else {
								p.sendMessage("§cVocê não tem permissão para executar esse comando.");
							}
						}
						if (args[0].equalsIgnoreCase("resposta")) {
							if (p.hasPermission(perma)) {
								if (!c.getConfig().getString("pergunta").equalsIgnoreCase("padrao-a")) {
									String resp = "";
			                        for (int c = 1;c < args.length; c++){
			                        	resp = resp + args[c] + " ";
			                        }
			                        c.getConfig().set("resposta", resp);
			                        c.getConfig().set("aberto", true);
			                        c.saveConfig();
			                        announcementQuiz();
			                        p.sendMessage(c.getConfig().getString("resp_def").replace("&", "§").replace("{resposta}", c.getConfig().getString("resposta")));
			                        for (String msg : c.getConfig().getStringList("iniciado")) {
			                        	for (Player all : Bukkit.getOnlinePlayers()) {
			                        		if (all.hasPermission(permp) || all.hasPermission(perma)) {
			                        			all.sendMessage(msg.replace("&", "§").replace("{pergunta}", c.getConfig().getString("pergunta").replace("&", "§")));
			                        		}
			                        	}
			                        }
								}else {
									p.sendMessage(c.getConfig().getString("naoha_pergunta").replace("&", "§"));
								}
							}else {
								p.sendMessage("§cVocê não tem permissão para executar esse comando.");
							}
						}
						if (args[0].equalsIgnoreCase("responder")) {
							if (p.hasPermission(perma)) {
								p.sendMessage(c.getConfig().getString("admin_nresp").replace("&", "§"));
							}else {
								if (c.getConfig().getBoolean("aberto") == false) {
									p.sendMessage(c.getConfig().getString("fechado_resp").replace("&", "§"));
								}else {
									String sresp = "";
			                        for (int c = 1;c < args.length; c++){
			                        	sresp = sresp + args[c] + " ";
			                        }
			                        if (c.getConfig().getString("resposta").equalsIgnoreCase(sresp)) {
			                        	if (c.getConfig().getBoolean("msg_chat") == true) {
			                        		for (String msg : c.getConfig().getStringList("finalizado")) {
				        						for (Player all : Bukkit.getOnlinePlayers()) {
				        							if (all.hasPermission(permp) || all.hasPermission(perma)) {
				        								all.sendMessage(msg.replace("&", "§").replace("{resposta}", c.getConfig().getString("resposta")).replace("{ganhador}", p.getName()));
				        							}
				        						}
				        					}
			                        	}
			                        	
			                        	if (c.getConfig().getBoolean("title") == true) {
			                        		for (Player all : Bukkit.getOnlinePlayers()) {
			                        			if (all.hasPermission(permp) || all.hasPermission(perma)) {
			                        				Title.sendFullTitle(all, 40, 120, 60, c.getConfig().getString("title_finalizado.title").replace("&", "§").replace("{player}", p.getName()), c.getConfig().getString("title_finalizado.subtitle").replace("&", "§").replace("{player}", p.getName()));
			                        			}
			                        		}
			                        	}
			                        	
			                        	if (c.getConfig().getBoolean("firework") == true) {
			                        		Firework fw = (Firework) p.getWorld().spawn(p.getLocation(), Firework.class);
			                    			FireworkMeta fme = fw.getFireworkMeta();
			                    			Builder b = FireworkEffect.builder();
			                    			fme.addEffects(b.withFlicker().withColor(Color.YELLOW).build());
			                    			fme.addEffect(b.withColor(Color.WHITE).build());
			                    			fme.addEffects(b.withTrail().build());
			                    			fme.addEffect(b.withFade(Color.ORANGE).build());
			                    			fme.setPower(0);
			                    			fw.setFireworkMeta(fme);
			                        	}
			                        	
			                        	if (c.getConfig().getBoolean("money") == true) {
			                        		if (c.getConfig().getDouble("money_quantidade") <= 0) {
			                        			Bukkit.getConsoleSender().sendMessage("§c[jQuiz] Não é possível dar valores negativos para os jogadores, altere a quantidade na config. Player: " + p.getName());
			                        			p.sendMessage("§cUm erro ocorreu ao depositar o prêmio em sua conta, entre em contato com a administração do servidor.");
			                        		}else {
			                        			double quantia = c.getConfig().getDouble("money_quantidade");
			                        			c.econ.depositPlayer(p.getName(), c.getConfig().getDouble("money_quantidade"));
			                        			p.sendMessage(c.getConfig().getString("premio_depos").replace("&", "§").replace("{quantia}", Double.toString(quantia)));
			                        		}
			                        	}
			                        	
			                        	c.getConfig().set("pergunta", "padrao-a");
			        					c.getConfig().set("resposta", "padrao-a");
			        					c.getConfig().set("aberto", false);
			        					c.saveConfig();
			                        }else {
			                        	p.sendMessage(c.getConfig().getString("resp_erro").replace("&", "§"));
			                        }
								}
							}
						}
					}
					}else {
						p.sendMessage("§cVocê não tem permissão para executar esse comando.");
					}
				}
		}
		return false;
	}
	
	int vezes = 0;
	
	@SuppressWarnings("deprecation")
	public void announcementQuiz() {
		BukkitScheduler announcement = Bukkit.getServer().getScheduler();
		announcement.scheduleAsyncRepeatingTask(c, new Runnable() {
			@Override
			public void run() {
				
				vezes++;
				if (vezes < 11) {
					if (c.getConfig().getBoolean("aberto") == true) {
						for (String msg : c.getConfig().getStringList("anuncios_automaticos")) {
							for (Player all : Bukkit.getOnlinePlayers()) {
								all.sendMessage(msg.replace("&", "§").replace("{pergunta}", c.getConfig().getString("pergunta")).replace("{chamados}", Integer.toString(11-vezes)));
							}
						}
					}
				}else {
					if (c.getConfig().getBoolean("aberto") == true) {
						c.getConfig().set("aberto", false);
						c.getConfig().set("resposta", "padrao-a");
						c.getConfig().set("pergunta", "padrao-a");
						c.saveConfig();
						
						for (String msg : c.getConfig().getStringList("cancelado_part")) {
							for (Player all : Bukkit.getOnlinePlayers()) {
								all.sendMessage(msg.replace("&", "§"));
							}
						}
					}
				}
				
			}
		}, 20 * 20L, 30 * 20L);
		
	}
}