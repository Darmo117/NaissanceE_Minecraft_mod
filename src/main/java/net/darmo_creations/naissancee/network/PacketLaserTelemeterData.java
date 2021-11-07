package net.darmo_creations.naissancee.network;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Data packet used to send new laser telemeter settings to server from client.
 */
public class PacketLaserTelemeterData implements IMessage {
  private BlockPos tileEntityPos;
  private Vec3i size;
  private BlockPos offset;

  /**
   * Default constructor for server.
   */
  public PacketLaserTelemeterData() {
  }

  /**
   * Create a packet.
   *
   * @param tileEntityPos Position of the tile entity to update.
   * @param size          Box size.
   * @param offset        Box offset.
   */
  public PacketLaserTelemeterData(BlockPos tileEntityPos, Vec3i size, BlockPos offset) {
    this.tileEntityPos = tileEntityPos;
    this.size = size;
    this.offset = offset;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    //noinspection ConstantConditions
    this.tileEntityPos = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
    //noinspection ConstantConditions
    this.size = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
    //noinspection ConstantConditions
    this.offset = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(this.tileEntityPos));
    ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(new BlockPos(this.size)));
    ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(this.offset));
  }

  /**
   * Server-side handler for {@link PacketLaserTelemeterData} message type.
   */
  public static class Handler implements IMessageHandler<PacketLaserTelemeterData, IMessage> {
    @Override
    public PacketLaserTelemeterData onMessage(PacketLaserTelemeterData message, MessageContext ctx) {
      Utils.getTileEntity(TileEntityLaserTelemeter.class, ctx.getServerHandler().player.world, message.tileEntityPos)
          .ifPresent(controller -> {
            try {
              controller.setSize(message.size);
              controller.setOffset(message.offset);
            } catch (IllegalArgumentException e) {
              NaissanceE.logger.catching(e);
            }
          });
      return null;
    }
  }
}
