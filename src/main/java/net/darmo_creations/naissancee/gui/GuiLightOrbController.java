package net.darmo_creations.naissancee.gui;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.network.PacketLightOrbControllerData;
import net.darmo_creations.naissancee.tile_entities.PathCheckpoint;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * GUI for the light orb controller block.
 */
@SideOnly(Side.CLIENT)
public class GuiLightOrbController extends GuiScreen {
  // Button IDs
  public static final int DONE_BUTTON_ID = 0;
  public static final int CANCEL_BUTTON_ID = 1;
  public static final int STATUS_BUTTON_ID = 2;
  public static final int LOOP_BUTTON_ID = 3;
  public static final int LIGHT_VALUE_SLIDER_ID = 4;
  public static final int SPEED_INPUT_ID = 5;

  // Buttons
  private GuiButton doneBtn;
  private GuiButton cancelBtn;
  private GuiButton statusBtn;
  private GuiButton loopBtn;
  private GuiPathCheckpointList checkpointList;

  // Data
  private final TileEntityLightOrbController tileEntity;
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
    this.statusBtn = this.addButton(new GuiButton(
        STATUS_BUTTON_ID,
        this.width / 2 - 154, 30,
        150, 20,
        I18n.format("gui.naissancee.light_orb_controller.status_button.label."
            + (this.active ? "active" : "inactive"))
    ));

    this.loopBtn = this.addButton(new GuiButton(
        LOOP_BUTTON_ID,
        this.width / 2 + 4, 30,
        150, 20,
        I18n.format("gui.naissancee.light_orb_controller.loop_button.label."
            + (this.loops ? "active" : "inactive"))
    ));

    this.addButton(new GuiSlider(
        (FloatGuiResponder) (id, value) -> this.lightLevel = this.getTrueLightValue(value),
        LIGHT_VALUE_SLIDER_ID,
        this.width / 2 - 154, 54,
        "gui.naissancee.light_orb_controller.light_level_slider.label",
        0, 15, this.lightLevel,
        (id, name, value) -> name + ": " + this.getTrueLightValue(value)
    ));

    this.addButton(new GuiSlider(
        (FloatGuiResponder) (id, value) -> this.speed = this.getTrueSpeedValue(value),
        SPEED_INPUT_ID,
        this.width / 2 + 4, 54,
        "gui.naissancee.light_orb_controller.speed_slider.label",
        0, 1, (float) this.speed,
        (id, name, value) -> String.format("%s: %.2f blocks/s", name, this.getTrueSpeedValue(value))
    ));

    this.checkpointList = new GuiPathCheckpointList(
        this.mc,
        this.width, this.height,
        78, this.height - 45,
        30
    );
    this.checkpointList.populateEntries(this.checkpoints);

    this.doneBtn = this.addButton(new GuiButton(
        DONE_BUTTON_ID,
        this.width / 2 - 154, this.height - 40,
        150, 20,
        I18n.format("gui.done")
    ));

    this.cancelBtn = this.addButton(new GuiButton(
        CANCEL_BUTTON_ID,
        this.width / 2 + 4,
        this.height - 40,
        150, 20,
        I18n.format("gui.cancel")
    ));
  }

  /**
   * Get the actual light value from the given float.
   */
  private int getTrueLightValue(float v) {
    return Math.round(v);
  }

  /**
   * Get the actual speed value from the given float.
   */
  private float getTrueSpeedValue(float v) {
    return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).floatValue();
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
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.checkpointList.handleMouseInput();
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    if (mouseButton != 0 || !this.checkpointList.mouseClicked(mouseX, mouseY, mouseButton)) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
    }
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    if (state != 0 || !this.checkpointList.mouseReleased(mouseX, mouseY, state)) {
      super.mouseReleased(mouseX, mouseY, state);
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
        this.checkpoints = this.checkpointList.getEntries();
        this.tileEntity.setCheckpoints(this.checkpoints);
        NaissanceE.network.sendToServer(new PacketLightOrbControllerData(
            this.tileEntity.getPos(), this.active, this.loops, this.lightLevel, this.speed, this.checkpoints));

      case CANCEL_BUTTON_ID:
        this.mc.displayGuiScreen(null);
        break;

      case STATUS_BUTTON_ID:
        this.active = !this.active;
        String label1 = this.active ? "active" : "inactive";
        this.statusBtn.displayString = I18n.format("gui.naissancee.light_orb_controller.status_button.label." + label1);
        break;

      case LOOP_BUTTON_ID:
        this.loops = !this.loops;
        String label2 = this.loops ? "active" : "inactive";
        this.loopBtn.displayString = I18n.format("gui.naissancee.light_orb_controller.loop_button.label." + label2);
        break;
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.checkpointList.drawScreen(mouseX, mouseY, partialTicks);
    this.drawCenteredString(this.fontRenderer, I18n.format("gui.naissancee.light_orb_controller.title"), this.width / 2, 12, 0xffffff);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  /**
   * GuiResponder with default empty implemention for {@link #setEntryValue(int, boolean)}
   * and {@link #setEntryValue(int, String)}.
   */
  @SideOnly(Side.CLIENT)
  private interface FloatGuiResponder extends GuiPageButtonList.GuiResponder {
    @Override
    default void setEntryValue(int id, String value) {
    }

    @Override
    default void setEntryValue(int id, boolean value) {
    }
  }
}
