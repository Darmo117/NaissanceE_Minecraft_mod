package net.darmo_creations.naissancee.gui;

import io.netty.buffer.Unpooled;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLightOrbController extends GuiScreen {
  private final TileEntityLightOrbController tileEntity;
  private GuiButton doneBtn;
  private GuiButton cancelBtn;
  private GuiButton statusBtn;
  public static final int DONE_BUTTON_ID = 0;
  public static final int CANCEL_BUTTON_ID = 1;
  public static final int STATUS_BUTTON_ID = 2;

  private boolean active;

  public GuiLightOrbController(TileEntityLightOrbController tileEntity) {
    this.tileEntity = tileEntity;
  }

  @Override
  public void initGui() {
    this.doneBtn = this.addButton(new GuiButton(DONE_BUTTON_ID, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20,
        I18n.format("gui.done")));
    this.cancelBtn = this.addButton(new GuiButton(CANCEL_BUTTON_ID, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20,
        I18n.format("gui.cancel")));
    String label = this.tileEntity.isActive() ? "active" : "inactive";
    this.statusBtn = this.addButton(new GuiButton(STATUS_BUTTON_ID, this.width / 2, this.height / 4, 150, 20,
        I18n.format("gui.naissancee.light_orb_controller.status_button." + label)));
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) {
    if (keyCode != 28 && keyCode != 156) {
      if (keyCode == 1) {
        this.actionPerformed(this.cancelBtn);
      }
    } else {
      this.actionPerformed(this.doneBtn);
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
      case DONE_BUTTON_ID:
        // FIXME envoyer un paquet au serveur
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        packetbuffer.writeBoolean(this.active);
        //noinspection ConstantConditions
        this.mc.getConnection().sendPacket(new CPacketCustomPayload("NaissanceE|LightOrbController", packetbuffer));
        this.mc.displayGuiScreen(null);
        break;
      case CANCEL_BUTTON_ID:
        this.mc.displayGuiScreen(null);
        break;
      case STATUS_BUTTON_ID:
        this.active = !this.active;
        String label = this.active ? "active" : "inactive";
        this.statusBtn.displayString = I18n.format("gui.naissancee.light_orb_controller.status_button." + label);
        break;
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    this.drawCenteredString(this.fontRenderer, I18n.format("gui.naissancee.light_orb_controller.title"), this.width / 2, 20, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}
