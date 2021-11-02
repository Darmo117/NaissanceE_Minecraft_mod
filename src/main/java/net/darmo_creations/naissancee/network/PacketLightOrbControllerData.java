package net.darmo_creations.naissancee.network;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.tile_entities.PathCheckpoint;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Data packet used to send new light orb controller settings to server from client.
 */
public class PacketLightOrbControllerData implements IMessage {
  private BlockPos tileEntityPos;
  private boolean active;
  private boolean loops;
  private boolean invisible;
  private int lightLevel;
  private double speed;
  private List<PathCheckpoint> checkpoints;

  /**
   * Default constructor for server.
   */
  public PacketLightOrbControllerData() {
  }

  /**
   * Create a packet.
   *
   * @param tileEntityPos Position of the tile entity to update.
   * @param active        Whether it should be active.
   * @param loops         Whether the orb should loop.
   * @param lightLevel    Orb’s light level.
   * @param speed         Orbs movement speed in blocks per second.
   * @param checkpoints   List of checkpoints.
   */
  public PacketLightOrbControllerData(BlockPos tileEntityPos, boolean active, boolean loops, boolean invisible,
                                      int lightLevel, double speed, List<PathCheckpoint> checkpoints) {
    this.tileEntityPos = tileEntityPos;
    this.active = active;
    this.loops = loops;
    this.invisible = invisible;
    this.lightLevel = lightLevel;
    this.speed = speed;
    this.checkpoints = checkpoints;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    //noinspection ConstantConditions
    this.tileEntityPos = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
    this.active = buf.readBoolean();
    this.loops = buf.readBoolean();
    this.invisible = buf.readBoolean();
    this.lightLevel = buf.readInt();
    this.speed = buf.readDouble();
    int listSize = buf.readInt();
    this.checkpoints = new LinkedList<>();
    for (int i = 0; i < listSize; i++) {
      //noinspection ConstantConditions
      this.checkpoints.add(new PathCheckpoint(ByteBufUtils.readTag(buf)));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(this.tileEntityPos));
    buf.writeBoolean(this.active);
    buf.writeBoolean(this.loops);
    buf.writeBoolean(this.invisible);
    buf.writeInt(this.lightLevel);
    buf.writeDouble(this.speed);
    buf.writeInt(this.checkpoints.size());
    for (PathCheckpoint checkpoint : this.checkpoints) {
      ByteBufUtils.writeTag(buf, checkpoint.toNBT());
    }
  }

  /**
   * Server-side handler for {@link PacketLightOrbControllerData} message type.
   */
  public static class Handler implements IMessageHandler<PacketLightOrbControllerData, IMessage> {
    @Override
    public PacketLightOrbControllerData onMessage(PacketLightOrbControllerData message, MessageContext ctx) {
      Utils.getTileEntity(TileEntityLightOrbController.class, ctx.getServerHandler().player.world, message.tileEntityPos)
          .ifPresent(controller -> {
            try {
              controller.setActive(message.active);
              controller.setLoops(message.loops);
              controller.setEntityInvisible(message.invisible);
              controller.setLightLevel(message.lightLevel);
              controller.setSpeed(message.speed);
              controller.setCheckpoints(message.checkpoints);
              controller.spawnOrb();
            } catch (IllegalArgumentException e) {
              NaissanceE.logger.catching(e);
            }
          });
      return null;
    }
  }
}
