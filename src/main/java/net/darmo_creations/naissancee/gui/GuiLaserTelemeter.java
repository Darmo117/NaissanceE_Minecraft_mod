package net.darmo_creations.naissancee.gui;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.network.PacketLaserTelemeterData;
import net.darmo_creations.naissancee.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Optional;

/**
 * GUI for the laser telemeter block.
 */
@SideOnly(Side.CLIENT)
public class GuiLaserTelemeter extends GuiScreen {
  // Button IDs
  public static final int DONE_BUTTON_ID = 0;
  public static final int CANCEL_BUTTON_ID = 1;
  public static final int LENGTH_X_INPUT_ID = 2;
  public static final int LENGTH_Y_INPUT_ID = 3;
  public static final int LENGTH_Z_INPUT_ID = 4;
  public static final int X_OFFSET_INPUT_ID = 5;
  public static final int Y_OFFSET_INPUT_ID = 6;
  public static final int Z_OFFSET_INPUT_ID = 7;

  // Buttons
  private GuiButton doneBtn;
  private GuiButton cancelBtn;
  private GuiTextField lengthXTextField;
  private GuiTextField lengthYTextField;
  private GuiTextField lengthZTextField;
  private GuiTextField xOffsetTextField;
  private GuiTextField yOffsetTextField;
  private GuiTextField zOffsetTextField;

  // Data
  private final TileEntityLaserTelemeter tileEntity;
  private Vec3i size;
  private BlockPos offset;

  // Layout
  public static final int TITLE_MARGIN = 30;
  public static final int MARGIN = 4;
  public static final int BUTTON_WIDTH = 150;
  public static final int BUTTON_HEIGHT = 20;

  /**
   * Creates a GUI for the given tile entity.
   *
   * @param tileEntity The tile entity.
   */
  public GuiLaserTelemeter(TileEntityLaserTelemeter tileEntity) {
    this.tileEntity = tileEntity;
    this.size = tileEntity.getSize();
    this.offset = tileEntity.getOffset();
  }

  @Override
  public void initGui() {
    final int middle = this.width / 2;
    final int leftButtonX = middle - BUTTON_WIDTH - MARGIN;
    final int rightButtonX = middle + MARGIN;

    int y = this.height / 2 - 2 * BUTTON_HEIGHT - MARGIN / 2;
    int btnW = (int) (BUTTON_WIDTH * 0.75);

    this.lengthXTextField = new GuiTextField(LENGTH_X_INPUT_ID, this.mc.fontRenderer, (int) (middle - btnW * 1.5), y, btnW, BUTTON_HEIGHT);
    this.lengthXTextField.setText("" + this.size.getX());
    this.lengthYTextField = new GuiTextField(LENGTH_Y_INPUT_ID, this.mc.fontRenderer, middle - btnW / 2, y, btnW, BUTTON_HEIGHT);
    this.lengthYTextField.setText("" + this.size.getY());
    this.lengthZTextField = new GuiTextField(LENGTH_Z_INPUT_ID, this.mc.fontRenderer, middle + btnW / 2, y, btnW, BUTTON_HEIGHT);
    this.lengthZTextField.setText("" + this.size.getZ());

    y += BUTTON_HEIGHT * 3 + MARGIN + this.fontRenderer.FONT_HEIGHT + 1;

    this.xOffsetTextField = new GuiTextField(X_OFFSET_INPUT_ID, this.mc.fontRenderer, (int) (middle - btnW * 1.5), y, btnW, BUTTON_HEIGHT);
    this.xOffsetTextField.setText("" + this.offset.getX());
    this.yOffsetTextField = new GuiTextField(Y_OFFSET_INPUT_ID, this.mc.fontRenderer, middle - btnW / 2, y, btnW, BUTTON_HEIGHT);
    this.yOffsetTextField.setText("" + this.offset.getY());
    this.zOffsetTextField = new GuiTextField(Z_OFFSET_INPUT_ID, this.mc.fontRenderer, middle + btnW / 2, y, btnW, BUTTON_HEIGHT);
    this.zOffsetTextField.setText("" + this.offset.getZ());

    y += BUTTON_HEIGHT + 8 * MARGIN;

    this.doneBtn = this.addButton(new GuiButton(
        DONE_BUTTON_ID,
        leftButtonX, y,
        BUTTON_WIDTH, BUTTON_HEIGHT,
        I18n.format("gui.done")
    ));

    this.cancelBtn = this.addButton(new GuiButton(
        CANCEL_BUTTON_ID,
        rightButtonX, y,
        BUTTON_WIDTH, BUTTON_HEIGHT,
        I18n.format("gui.cancel")
    ));
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) {
    if (keyCode == 28 || keyCode == 156) {
      this.actionPerformed(this.doneBtn);
    } else if (keyCode == 1) {
      this.actionPerformed(this.cancelBtn);

    } else if (this.lengthXTextField.textboxKeyTyped(typedChar, keyCode)) {
      this.updateLengthField(this.lengthXTextField, 200)
          .ifPresent(i -> this.size = new Vec3i(i, this.size.getY(), this.size.getZ()));
    } else if (this.lengthYTextField.textboxKeyTyped(typedChar, keyCode)) {
      this.updateLengthField(this.lengthYTextField, 200)
          .ifPresent(i -> this.size = new Vec3i(this.size.getX(), i, this.size.getZ()));
    } else if (this.lengthZTextField.textboxKeyTyped(typedChar, keyCode)) {
      this.updateLengthField(this.lengthZTextField, 200)
          .ifPresent(i -> this.size = new Vec3i(this.size.getX(), this.size.getY(), i));

    } else if (this.xOffsetTextField.textboxKeyTyped(typedChar, keyCode)) {
      this.updateLengthField(this.xOffsetTextField, Integer.MAX_VALUE)
          .ifPresent(i -> this.offset = new BlockPos(i, this.offset.getY(), this.offset.getZ()));
    } else if (this.yOffsetTextField.textboxKeyTyped(typedChar, keyCode)) {
      this.updateLengthField(this.yOffsetTextField, Integer.MAX_VALUE)
          .ifPresent(i -> this.offset = new BlockPos(this.offset.getX(), i, this.offset.getZ()));
    } else if (this.zOffsetTextField.textboxKeyTyped(typedChar, keyCode)) {
      this.updateLengthField(this.zOffsetTextField, Integer.MAX_VALUE)
          .ifPresent(i -> this.offset = new BlockPos(this.offset.getX(), this.offset.getY(), i));
    }
  }

  /**
   * Update the given length field’s text color using its value and return it.
   *
   * @param lengthTextField Length field to update.
   * @return The integer value of the text field if valid, empty otherwise.
   */
  private Optional<Integer> updateLengthField(GuiTextField lengthTextField, int maxValue) {
    Integer i = null;
    try {
      i = Integer.parseInt(lengthTextField.getText());
    } catch (NumberFormatException ignored) {
    }
    if (i == null || Math.abs(i) > maxValue) {
      lengthTextField.setTextColor(Utils.RED);
      return Optional.empty();
    } else {
      lengthTextField.setTextColor(Utils.WHITE);
      return Optional.of(i);
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    this.lengthXTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.lengthYTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.lengthZTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.xOffsetTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.yOffsetTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.zOffsetTextField.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
      case DONE_BUTTON_ID:
        this.tileEntity.setSize(this.size);
        this.tileEntity.setOffset(this.offset);
        NaissanceE.network.sendToServer(new PacketLaserTelemeterData(this.tileEntity.getPos(), this.size, this.offset));

      case CANCEL_BUTTON_ID:
        this.mc.displayGuiScreen(null);
        break;
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    final int fontHeight = this.mc.fontRenderer.FONT_HEIGHT;
    final int middle = this.width / 2;

    this.drawDefaultBackground();

    this.drawCenteredString(this.fontRenderer, I18n.format("gui.naissancee.laser_telemeter.title"),
        middle, (TITLE_MARGIN - fontHeight) / 2, Utils.WHITE);

    int y = this.height / 2 - 2 * BUTTON_HEIGHT - MARGIN / 2 - fontHeight - 1;

    int btnW = (int) (BUTTON_WIDTH * 0.75);
    this.drawString(this.fontRenderer, I18n.format("gui.naissancee.laser_telemeter.length_x_field.label"),
        (int) (middle - btnW * 1.5), y, Utils.GRAY);
    this.drawString(this.fontRenderer, I18n.format("gui.naissancee.laser_telemeter.length_y_field.label"),
        middle - btnW / 2, y, Utils.GRAY);
    this.drawString(this.fontRenderer, I18n.format("gui.naissancee.laser_telemeter.length_z_field.label"),
        middle + btnW / 2, y, Utils.GRAY);

    y += 3 * BUTTON_HEIGHT + MARGIN + fontHeight;

    this.drawString(this.fontRenderer, I18n.format("gui.naissancee.laser_telemeter.x_offset_field.label"),
        (int) (middle - btnW * 1.5), y, Utils.GRAY);
    this.drawString(this.fontRenderer, I18n.format("gui.naissancee.laser_telemeter.y_offset_field.label"),
        middle - btnW / 2, y, Utils.GRAY);
    this.drawString(this.fontRenderer, I18n.format("gui.naissancee.laser_telemeter.z_offset_field.label"),
        middle + btnW / 2, y, Utils.GRAY);

    this.lengthXTextField.drawTextBox();
    this.lengthYTextField.drawTextBox();
    this.lengthZTextField.drawTextBox();
    this.xOffsetTextField.drawTextBox();
    this.yOffsetTextField.drawTextBox();
    this.zOffsetTextField.drawTextBox();

    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}
