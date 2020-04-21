package de.blaumeise03.freeElytra2;

import de.blaumeise03.blueUtils.Command;
import de.blaumeise03.blueUtils.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
    static void addCommands(){
        CommandHandler handler = FreeElytra2.getPlugin().getHandler();
        new Command(handler, "elytra", "Gib´t dir eine Elytra", true, true) {
            @Override
            public void onCommand(String[] strings, CommandSender commandSender, boolean isPlayer, boolean isThirdExecution, CommandSender realSender) {
                Elytra.shootPlayer((Player) commandSender);
            }
        };

        new Command(handler, "addPad", "Fügt ein StartPad hinzu", true, false) {
            @Override
            public void onCommand(String[] args, CommandSender sender, boolean isPlayer, boolean isThirdExecution, CommandSender realSender) {
                if(args.length == 7){
                    for (int i = 1; i < args.length; i++) {
                        try {
                            Integer.parseInt(args[i]);
                        }catch (NumberFormatException ignored){}
                    }
                    if(StartPad.doesExists(args[0])){
                        sender.sendMessage("§4Dieses Pad existiert bereits!");
                        return;
                    }
                    Player p = (Player) sender;
                    new StartPad(args[0],
                            Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]),
                            p.getWorld(), true);
                    p.sendMessage("§aStartpad erstellt!");
                }else {
                    sender.sendMessage(ChatColor.RED + "Zu wenig/viele argumente: /addPad <name> <x1> <y1> <z1> <x2> <y2> <z2>");
                }
            }
        };

        new Command(handler, "deletePad", "Löscht ein StartPad", true, false){
            @Override
            public void onCommand(String[] args, CommandSender sender, boolean isPlayer, boolean isThirdExecution, CommandSender realSender) {
                if(args.length == 1){
                    StartPad.deletePad(args[0], FreeElytra2.getPadConfig());
                    sender.sendMessage(ChatColor.GREEN + "StartPad gelöscht!");
                }else {
                    sender.sendMessage(ChatColor.RED + "Zu wenig argumente: /deletePad <name>");
                }
            }
        };

        new Command(handler, "listPads", "Listet alle StartPads auf", false, false){
            @Override
            public void onCommand(String[] args, CommandSender sender, boolean isPlayer, boolean isThirdExecution, CommandSender realSender) {
                StringBuilder builder = new StringBuilder(ChatColor.DARK_GREEN + "StartPads:");
                StartPad.getStartPads().forEach(p -> builder.append(ChatColor.GREEN + "\n").append(p.getName()));
                sender.sendMessage(builder.toString());
            }
        };

        new Command(handler, "reloadPads", "Lädt die Config neu §cÜberschriebt den Cache!!", false, false){
            @Deprecated
            @Override
            public void onCommand(String[] args, CommandSender sender, boolean isPlayer, boolean isThirdExecution, CommandSender realSender) {
                FreeElytra2.reloadPadConfig();
                try {
                    StartPad.load(FreeElytra2.getPadConfig());
                } catch (CorruptedConfigurationException e) {
                    e.printStackTrace();
                }
                sender.sendMessage(ChatColor.GREEN + "Pad-Config neugeladen!");
            }
        };
    }
}
