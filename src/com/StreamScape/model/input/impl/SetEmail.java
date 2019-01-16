package com.StreamScape.model.input.impl;

import java.sql.PreparedStatement;

import com.StreamScape.model.input.Input;
import com.StreamScape.world.content.Achievements;
import com.StreamScape.world.content.Achievements.AchievementData;
import com.StreamScape.world.content.PlayerPanel;
import com.StreamScape.world.entity.impl.player.Player;

import mysql.MySQLController;
import mysql.MySQLController.Database;
import mysql.MySQLDatabase;

public class SetEmail extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (syntax.length() <= 3 || !syntax.contains("@") || syntax.endsWith("@")) {
			player.getPacketSender().sendMessage("Invalid syntax, please enter a valid one.");
			return;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
			player.getPacketSender().sendMessage("Please visit the nearest bank and enter your pin before doing this.");
			return;
		}
		MySQLDatabase recovery = MySQLController.getController().getDatabase(Database.RECOVERY);
		if (!recovery.active || recovery.getConnection() == null) {
			player.getPacketSender().sendMessage("This service is currently unavailable.");
			return;
		}
		if (player.getEmailAddress() != null && syntax.equalsIgnoreCase(player.getEmailAddress())) {
			player.getPacketSender().sendMessage("This is already your email-address!");
			return;
		}
		syntax = syntax.toLowerCase();
		syntax = syntax.substring(0, 1).toUpperCase() + syntax.substring(1);
		boolean success = false;
		try {
			PreparedStatement preparedStatement = recovery.getConnection()
					.prepareStatement("DELETE FROM email WHERE Username = ?");
			preparedStatement.setString(1, player.getUsername());
			preparedStatement.executeUpdate();
			preparedStatement = recovery.getConnection()
					.prepareStatement("INSERT INTO email (Username,Password,Email) VALUES (?, ?, ?)");
			preparedStatement.setString(1, player.getUsername());
			preparedStatement.setString(2, player.getPassword());
			preparedStatement.setString(3, syntax);
			preparedStatement.executeUpdate();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		if (success) {
			player.setEmailAddress(syntax);
			Achievements.doProgress(player, AchievementData.SET_EMAIL);
			player.getPacketSender().sendMessage("Your account's email-adress is now: " + syntax + ".");
			PlayerPanel.refreshPanel(player);
		} else {
			player.getPacketSender().sendMessage("An error occured. Please try again.");
		}
	}
}
