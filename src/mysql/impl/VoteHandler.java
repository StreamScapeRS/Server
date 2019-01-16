package mysql.impl;

import com.rspserver.motivote.MotivoteHandler;
import com.rspserver.motivote.Vote;
import com.StreamScape.GameSettings;
import com.StreamScape.model.Item;
import com.StreamScape.util.Misc;
import com.StreamScape.world.World;
import com.StreamScape.world.entity.impl.player.Player;

public class VoteHandler extends MotivoteHandler<Vote> {

	@Override
	public void onCompletion(Vote arg0) {

		Player player = World.getPlayerByName(arg0.username());

		if (player != null) {

			player.sendMessage("Thank you for voting and supporting our server!");
			player.getInventory().add(new Item(
					GameSettings.VOTE_REWARD_IDS[Misc.exclusiveRandom(GameSettings.VOTE_REWARD_IDS.length)], 1));

			arg0.complete();

		}

	}

}
