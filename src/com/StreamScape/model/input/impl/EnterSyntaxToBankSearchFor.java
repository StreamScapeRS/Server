package com.StreamScape.model.input.impl;

import com.StreamScape.model.container.impl.Bank.BankSearchAttributes;
import com.StreamScape.model.input.Input;
import com.StreamScape.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank();
		if (searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
