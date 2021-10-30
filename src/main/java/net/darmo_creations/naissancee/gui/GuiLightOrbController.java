package net.darmo_creations.naissancee.gui;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.network.PacketLightOrbControllerData;
import net.darmo_creations.naissancee.tile_entities.PathCheckpoint;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * GUI for the light orb controller block.
 */
@SideOnly(Side.CLIENT)
public class GuiLightOrbController extends GuiScreen {
  // Associated tile entity
  private final TileEntityLightOrbController tileEntity;
  private GuiButton doneBtn;
  private GuiButton cancelBtn;
  private GuiButton statusBtn;
  private GuiButton loopBtn;
  public static final int DONE_BUTTON_ID = 0;
  public static final int CANCEL_BUTTON_ID = 1;
  public static final int STATUS_BUTTON_ID = 2;
  public static final int LOOP_BUTTON_ID = 3;

  private boolean active;
  private boolean loops;
  private int lightLevel;
  private double speed;
  private List<PathCheckpoint> checkpoints;

  /**
   * Creates a GUI for the given tile entity.
   *
   * @param tileEntity The tile entity.
   */
  public GuiLightOrbController(TileEntityLightOrbController tileEntity) {
    this.tileEntity = tileEntity;
    this.active = tileEntity.isActive();
    this.loops = tileEntity.loops();
    this.lightLevel = tileEntity.getLightLevel();
    this.speed = tileEntity.getSpeed();
    this.checkpoints = tileEntity.getCheckpoints();
  }

  @Override
  public void initGui() {
    this.doneBtn = this.addButton(new GuiButton(DONE_BUTTON_ID, this.width / 2 - 154, this.height / 4 + 132, 150, 20,
        I18n.format("gui.done")));
    this.cancelBtn = this.addButton(new GuiButton(CANCEL_BUTTON_ID, this.width / 2 + 4, this.height / 4 + 132, 150, 20,
        I18n.format("gui.cancel")));
    String label = this.active ? "active" : "inactive";
    this.statusBtn = this.addButton(new GuiButton(STATUS_BUTTON_ID, this.width / 2 - 154, this.height / 4, 150, 20,
        I18n.format("gui.naissancee.light_orb_controller.status_button." + label)));
    label = this.loops ? "active" : "inactive";
    this.loopBtn = this.addButton(new GuiButton(LOOP_BUTTON_ID, this.width / 2 + 4, this.height / 4, 150, 20,
        I18n.format("gui.naissancee.light_orb_controller.loop_button." + label)));
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
        this.tileEntity.setActive(this.active);
        this.tileEntity.setLoops(this.loops);
        this.tileEntity.setLightLevel(this.lightLevel);
        this.tileEntity.setSpeed(this.speed);
        NaissanceE.network.sendToServer(new PacketLightOrbControllerData(
            this.tileEntity.getPos(), this.active, this.loops, this.lightLevel, this.speed, this.checkpoints));

      case CANCEL_BUTTON_ID:
        this.mc.displayGuiScreen(null);
        break;

      case STATUS_BUTTON_ID:
        this.active = !this.active;
        String label1 = this.active ? "active" : "inactive";
        this.statusBtn.displayString = I18n.format("gui.naissancee.light_orb_controller.status_button." + label1);
        break;

      case LOOP_BUTTON_ID:
        this.loops = !this.loops;
        String label2 = this.loops ? "active" : "inactive";
        this.loopBtn.displayString = I18n.format("gui.naissancee.light_orb_controller.loop_button." + label2);
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
