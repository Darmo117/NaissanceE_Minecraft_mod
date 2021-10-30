package net.darmo_creations.naissancee.network;

import io.netty.buffer.ByteBuf;
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
import java.util.Optional;

public class PacketLightOrbControllerData implements IMessage {
  private BlockPos tileEntityPos;
  private boolean active;
  private boolean loops;
  private int lightLevel;
  private double speed;
  private List<PathCheckpoint> checkpoints;

  public PacketLightOrbControllerData() {
  }

  public PacketLightOrbControllerData(BlockPos tileEntityPos, boolean active, boolean loops,
                                      int lightLevel, double speed, List<PathCheckpoint> checkpoints) {
    this.tileEntityPos = tileEntityPos;
    this.active = active;
    this.loops = loops;
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
    this.lightLevel = buf.readInt();
    this.speed = buf.readDouble();
    int listSize = buf.readInt();
    this.checkpoints = new LinkedList<>();
    for (int j = 0; j < listSize; j++) {
      //noinspection ConstantConditions
      this.checkpoints.add(new PathCheckpoint(ByteBufUtils.readTag(buf)));
    }
    // DEBUG
    System.out.printf("%s %b %b %d %f %s%n", this.tileEntityPos, this.active, this.loops, this.lightLevel, this.speed, this.checkpoints);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(this.tileEntityPos));
    buf.writeBoolean(this.active);
    buf.writeBoolean(this.loops);
    buf.writeInt(this.lightLevel);
    buf.writeDouble(this.speed);
    buf.writeInt(this.checkpoints.size());
    for (PathCheckpoint checkpoint : this.checkpoints) {
      ByteBufUtils.writeTag(buf, checkpoint.toNBT());
    }
    // DEBUG
    System.out.printf("%s %b %b %d %f %s%n", this.tileEntityPos, this.active, this.loops, this.lightLevel, this.speed, this.checkpoints);
  }

  public static class Handler implements IMessageHandler<PacketLightOrbControllerData, IMessage> {
    @Override
    public PacketLightOrbControllerData onMessage(PacketLightOrbControllerData message, MessageContext ctx) {
      Optional<TileEntityLightOrbController> te =
          Utils.getTileEntity(TileEntityLightOrbController.class, ctx.getServerHandler().player.world, message.tileEntityPos);
      te.ifPresent(controller -> {
        controller.setActive(message.active);
        controller.setLoops(message.loops);
        controller.setLightLevel(message.lightLevel);
        controller.setSpeed(message.speed);
        controller.setCheckpoints(message.checkpoints);
        controller.spawnOrb();
      });
      return null;
    }
  }
}
