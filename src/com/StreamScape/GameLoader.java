package com.StreamScape;

import com.StreamScape.drops.NPCDrops;
import com.StreamScape.engine.GameEngine;
import com.StreamScape.engine.task.TaskManager;
import com.StreamScape.engine.task.impl.ServerTimeUpdateTask;
import com.StreamScape.model.container.impl.Shop.ShopManager;
import com.StreamScape.model.definitions.ItemDefinition;
import com.StreamScape.model.definitions.NpcDefinition;
import com.StreamScape.model.definitions.WeaponInterfaces;
import com.StreamScape.net.PipelineFactory;
import com.StreamScape.net.packet.interaction.PacketInteractionManager;
import com.StreamScape.net.security.ConnectionHandler;
import com.StreamScape.world.World;
import com.StreamScape.world.clip.region.RegionClipping;
import com.StreamScape.world.content.*;
import com.StreamScape.world.content.clan.ClanChatManager;
import com.StreamScape.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.StreamScape.world.content.combat.strategy.CombatStrategies;
import com.StreamScape.world.content.dialogue.DialogueManager;
import com.StreamScape.world.content.event.Christmas;
import com.StreamScape.world.content.minigames.MinigameHandler;
import com.StreamScape.world.entity.impl.npc.NPC;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import mysql.MySQLController;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Credit: lare96, StreamScape
 */
public final class GameLoader {

	private final ExecutorService serviceLoader = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());
	private final GameEngine engine;
	private final int port;

	protected GameLoader(int port) {
		this.port = port;
		this.engine = new GameEngine();

	}

	private void executeServiceLoad() {
		//Motivote<Vote> VOTE = new Motivote<Vote>(new VoteHandler(), "http://StreamScape.com/vote/", "088ddd3c");
		//VOTE.start();
		serviceLoader.execute(() -> ItemDefinition.init());
		serviceLoader.execute(() -> NPCDrops.parseDrops().load());
		serviceLoader.execute(() -> NPCDrops.init());
		serviceLoader.execute(() -> ConnectionHandler.init());
		serviceLoader.execute(() -> RegionClipping.init());
		serviceLoader.execute(() -> CustomObjects.init());
		serviceLoader.execute(() -> Lottery.init());
		serviceLoader.execute(() -> Scoreboards.init());
		serviceLoader.execute(() -> WellOfGoodwill.init());
		serviceLoader.execute(() -> ClanChatManager.init());
		serviceLoader.execute(() -> CombatPoisonData.init());
		serviceLoader.execute(() -> CombatStrategies.init());
		serviceLoader.execute(() -> NpcDefinition.parseNpcs().load());
		serviceLoader.execute(() -> WeaponInterfaces.parseInterfaces().load());
		serviceLoader.execute(() -> ShopManager.parseShops().load());
		serviceLoader.execute(() -> DialogueManager.parseDialogues().load());
		serviceLoader.execute(() -> NPC.init());
		serviceLoader.execute(() -> PacketInteractionManager.init());
		serviceLoader.execute(() -> Censor.init());
		//serviceLoader.execute(() -> MySQLController.init());
		serviceLoader.execute(() -> MonsterDrops.initialize());
		serviceLoader.execute(() -> Christmas.init());
	}

	public void finish() throws IOException, InterruptedException {
		if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
			throw new IllegalStateException("The background service load took too long!");
		ExecutorService networkExecutor = Executors.newCachedThreadPool();
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(networkExecutor, networkExecutor));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(port));
		executor.scheduleAtFixedRate(engine, 0, GameSettings.ENGINE_PROCESSING_CYCLE_RATE, TimeUnit.MILLISECONDS);
		TaskManager.submit(new ServerTimeUpdateTask());

		World.minigameHandler = MinigameHandler.defaultHandler();
		World.minigameHandler.loadMinigames();
	}

	public GameEngine getEngine() {
		return engine;
	}

	public void init() {
		Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");
		executeServiceLoad();
		serviceLoader.shutdown();
	}
}