package de.blaumeise03.freeElytra2;


import de.blaumeise03.blueUtils.command.Command;
import de.blaumeise03.blueUtils.command.CommandHandler;
import de.blaumeise03.blueUtils.exceptions.CommandNotFoundException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Commands {
    static void addCommands(){
        Permission setPerm = new Permission("");
        CommandHandler handler = FreeElytra2.getPlugin().getHandler();

        Command elytraCmd = new Command("elytra", true, true, new Permission("freeElytra.elytra")) {
            /**
             * This method is called when a player executes this command
             *
             * @param sender         the {@link CommandSender} who executes the command
             * @param args           the arguments passed to the command
             * @param isPlayer       if the <code>sender</code> is a {@link Player Player}
             * @param isThird        if the command was executed by the <code>originalSender</code> for another player
             *                       e.g: /command player args.. - the command, if {@link Command#isThirdExecutable()} is true,
             *                       gets executed at the 'player' passed as first argument
             * @param originalSender The original sender, equals <code>sender</code> if command was not third-executed
             */
            @Override
            public void execute(CommandSender sender, String[] args, boolean isPlayer, boolean isThird, CommandSender originalSender) {
                Elytra.shootPlayer((Player) sender);
            }
        };
        try {
            handler.addCommand(elytraCmd);
        } catch (CommandNotFoundException e) {
            e.printStackTrace();
        }

        Command addCmd = new Command( "addPad", true, true, setPerm) {
            /**
             * This method is called when a player executes this command
             *
             * @param sender         the {@link CommandSender} who executes the command
             * @param args           the arguments passed to the command
             * @param isPlayer       if the <code>sender</code> is a {@link Player Player}
             * @param isThird        if the command was executed by the <code>originalSender</code> for another player
             *                       e.g: /command player args.. - the command, if {@link Command#isThirdExecutable()} is true,
             *                       gets executed at the 'player' passed as first argument
             * @param originalSender The original sender, equals <code>sender</code> if command was not third-executed
             */
            @Override
            public void execute(CommandSender sender, String[] args, boolean isPlayer, boolean isThird, CommandSender originalSender) {
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
        try {
            handler.addCommand(addCmd);
        } catch (CommandNotFoundException e) {
            e.printStackTrace();
        }

        Command delCmd = new Command( "deletePad", false, false, setPerm){
            /**
             * This method is called when a player executes this command
             *
             * @param sender         the {@link CommandSender} who executes the command
             * @param args           the arguments passed to the command
             * @param isPlayer       if the <code>sender</code> is a {@link Player Player}
             * @param isThird        if the command was executed by the <code>originalSender</code> for another player
             *                       e.g: /command player args.. - the command, if {@link Command#isThirdExecutable()} is true,
             *                       gets executed at the 'player' passed as first argument
             * @param originalSender The original sender, equals <code>sender</code> if command was not third-executed
             */
            @Override
            public void execute(CommandSender sender, String[] args, boolean isPlayer, boolean isThird, CommandSender originalSender) {
                if(args.length == 1){
                    StartPad.deletePad(args[0], FreeElytra2.getPadConfig());
                    sender.sendMessage(ChatColor.GREEN + "StartPad gelöscht!");
                }else {
                    sender.sendMessage(ChatColor.RED + "Zu wenig argumente: /deletePad <name>");
                }
            }
        };
        try {
            handler.addCommand(delCmd);
        } catch (CommandNotFoundException e) {
            e.printStackTrace();
        }

        Command listCmd = new Command( "listPads", false, false, setPerm){
            /**
             * This method is called when a player executes this command
             *
             * @param sender         the {@link CommandSender} who executes the command
             * @param args           the arguments passed to the command
             * @param isPlayer       if the <code>sender</code> is a {@link Player Player}
             * @param isThird        if the command was executed by the <code>originalSender</code> for another player
             *                       e.g: /command player args.. - the command, if {@link Command#isThirdExecutable()} is true,
             *                       gets executed at the 'player' passed as first argument
             * @param originalSender The original sender, equals <code>sender</code> if command was not third-executed
             */
            @Override
            public void execute(CommandSender sender, String[] args, boolean isPlayer, boolean isThird, CommandSender originalSender) {
                StringBuilder builder = new StringBuilder(ChatColor.DARK_GREEN + "StartPads:");
                StartPad.getStartPads().forEach(p -> builder.append(ChatColor.GREEN + "\n").append(p.getName()));
                sender.sendMessage(builder.toString());
            }
        };
        try {
            handler.addCommand(listCmd);
        } catch (CommandNotFoundException e) {
            e.printStackTrace();
        }

        /*new Command(handler, "reloadPads", "Lädt die Config neu §cÜberschriebt den Cache!!", false, false){
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
        };*/
    }
}
