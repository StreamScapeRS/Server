package com.StreamScape.net;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import com.StreamScape.net.packet.Packet;
import com.StreamScape.net.packet.PacketConstants;
import com.StreamScape.world.World;
import com.StreamScape.world.entity.impl.player.Player;

/**
 * An implementation of netty's {@link SimpleChannelUpstreamHandler} to handle
 * all of netty's incoming events.
 * 
 * @author Gabriel Hannason
 */
public class ChannelHandler extends IdleStateAwareChannelUpstreamHandler {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ChannelHandler.class.getName());

	private Player player;

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (player != null) {
			if (player.getSession().getState() != SessionState.LOGGED_OUT) {
				if (!World.getLogoutQueue().contains(player)) {
					player.getLogoutTimer().reset();
					World.getLogoutQueue().add(player);
				}
			}
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {

	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		e.getChannel().close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		if (!(e.getCause() instanceof IOException)) { // Because there's no more
														// data to read (happens
														// when client has been
														// forcibly exited via
														// task manager)
			logger.log(Level.WARNING, "Exception occured for channel: " + e.getChannel() + ", closing...",
					e.getCause());
			ctx.getChannel().close();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (e.getMessage() != null) {
			Object msg = e.getMessage();
			if (msg instanceof Player) {
				if (player == null)
					player = (Player) e.getMessage();
			} else if (msg.getClass() == Packet.class) {
				if (msg instanceof Packet) {
					Packet packet = (Packet) msg;
					if (packet.getOpcode() == 41) {
						PacketConstants.PACKETS[packet.getOpcode()].handleMessage(player, packet);
					} else {
						player.getSession().handleIncomingMessage(packet);
					}
				}
			}
		}
	}

}