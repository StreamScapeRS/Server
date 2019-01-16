package com.StreamScape.net.packet.impl.commands.owner;

import com.StreamScape.model.Flag;
import com.StreamScape.model.Item;
import com.StreamScape.model.Position;
import com.StreamScape.model.container.impl.Bank;
import com.StreamScape.model.container.impl.Equipment;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.model.definitions.WeaponAnimations;
import com.StreamScape.model.definitions.WeaponInterfaces;
import com.StreamScape.world.World;
import com.StreamScape.world.content.BonusManager;
import com.StreamScape.world.entity.impl.player.Player;

public class Misc {

	public static void checkCommands(Player player, String wholeCommand, String[] command) {
		if (command[0].equalsIgnoreCase("giveitem")) {
			String targetName = wholeCommand
					.substring(command[0].length() + 1 + command[1].length() + 1 + command[2].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (target.getInventory().isFull()) {
				player.getPacketSender().sendMessage("This players inventory is full plese tell them.");
				target.getPacketSender()
						.sendMessage(player.getUsername() + " is trying to give you an item, please make some room.");
				return;
			}
			int itemId = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			player.getPacketSender().sendMessage("You give " + target.getUsername() + " x" + amount + " of "
					+ ItemDefinition.forId(itemId).getName() + ".");
			target.getPacketSender().sendMessage("You have been given x" + amount + " of "
					+ ItemDefinition.forId(itemId).getName() + " by " + player.getUsername() + ".");
			target.getInventory().add(new Item(itemId, amount));
		}
		if (command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("checkbank")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			for (int i = 0; i < player.getBanks().length; i++) {
				player.getBank(i).resetItems().refreshItems();
				player.getBank(i).add(target.getBank(i).getItems());
			}
			player.getBank(0).open();
		}

		if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3) {
				z = Integer.valueOf(command[3]);
			}
			player.moveTo(new Position(x, y, z));
		}

		if (command[0].equalsIgnoreCase("checkinv")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("No such player exists.");
				return;
			}
			player.getInventory().setItems(target.getInventory().getCopiedItems()).refreshItems();
		}
		if (command[0].equalsIgnoreCase("resetinv")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("No such player exists.");
				return;
			}
			target.getInventory().resetItems().refreshItems();
			target.getPacketSender().sendMessage("Your inventory has been cleared by " + player.getUsername());
			player.getPacketSender()
					.sendMessage("You have successfully cleared " + target.getUsername() + "'s inventory.");
		}
		if (command[0].equalsIgnoreCase("checkequip")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("No such player exists.");
				return;
			}
			player.getEquipment().setItems(target.getEquipment().getCopiedItems()).refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(player);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("resetequip")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("No such player exists.");
				return;
			}
			target.getEquipment().resetItems().refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(player);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			target.getPacketSender().sendMessage("Your equipment has been cleared by " + player.getUsername());
			player.getPacketSender()
					.sendMessage("You have successfully cleared " + target.getUsername() + "'s equipment.");
		}
		if (command[0].equalsIgnoreCase("checkbank")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("No such player exists.");
				return;
			}
			player.getPacketSender().sendConsoleMessage("Loading bank..");
			for (Bank b : player.getBanks()) {
				if (b != null) {
					b.resetItems();
				}
			}
			for (int i = 0; i < target.getBanks().length; i++) {
				for (Item it : target.getBank(i).getItems()) {
					if (it != null) {
						player.getBank(i).add(it, false);
					}
				}
			}
			player.getBank(0).open();
		}
		if (command[0].equalsIgnoreCase("resetbank")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("No such player exists.");
				return;
			}
			for (int i = 0; i < target.getBanks().length; i++) {
				target.getBank(i).resetItems().refreshItems();
			}
		}
	}
}
