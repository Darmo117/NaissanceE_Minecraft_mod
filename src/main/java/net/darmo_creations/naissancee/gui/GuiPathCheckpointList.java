package net.darmo_creations.naissancee.gui;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.tile_entities.PathCheckpoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom list GUI component that displays a list of checkpoints with buttons to edit/delete/reorder them.
 */
@SideOnly(Side.CLIENT)
public class GuiPathCheckpointList extends GuiListExtended {
  private List<GuiEntry> entries;

  public GuiPathCheckpointList(Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
    super(mc, width, height, top, bottom, slotHeight);
    this.entries = new ArrayList<>();
  }

  /**
   * Set the entries of this list.
   */
  public void populateEntries(List<PathCheckpoint> entries) {
    this.entries = entries.stream().map(GuiEntry::new).collect(Collectors.toList());
    this.updateEntriesButtons();
  }

  /**
   * Get all entries as they have been updated by player.
   */
  public List<PathCheckpoint> getEntries() {
    List<PathCheckpoint> list = new ArrayList<>();
    for (int i = 0; i < this.entries.size(); i++) {
      list.add(this.getListEntry(i).getCheckpoint());
    }
    return list;
  }

  @Override
  public GuiEntry getListEntry(int index) {
    return this.entries.get(index);
  }

  @Override
  protected int getSize() {
    return this.entries.size();
  }

  @Override
  protected int getScrollBarX() {
    return super.getScrollBarX() + 35;
  }

  /**
   * Called when the "stop" button is clicked on an entry.
   */
  protected void onStopButtonClicked() {
    this.updateEntriesButtons();
  }

  /**
   * Called when the "delete" button is clicked on an entry.
   */
  protected void onDeleteButtonClicked(int entryIndex) {
    this.entries.remove(entryIndex);
    this.updateEntriesButtons();
  }

  /**
   * Called when the "move up" button is clicked on an entry.
   */
  protected void onMoveUpButtonClicked(int entryIndex) {
    GuiEntry entry = this.entries.get(entryIndex);
    this.entries.set(entryIndex, this.entries.get(entryIndex - 1));
    this.entries.set(entryIndex - 1, entry);
    this.updateEntriesButtons();
  }

  /**
   * Called when the "move down" button is clicked on an entry.
   */
  protected void onMoveDownButtonClicked(int entryIndex) {
    GuiEntry entry = this.entries.get(entryIndex);
    this.entries.set(entryIndex, this.entries.get(entryIndex + 1));
    this.entries.set(entryIndex + 1, entry);
    this.updateEntriesButtons();
  }

  /**
   * Updates buttons states of all entries.
   */
  private void updateEntriesButtons() {
    int size = this.entries.size();
    for (int i = 0; i < size; i++) {
      GuiEntry entry = this.entries.get(i);
      entry.setDeleteButtonEnabled(size > 1);
      entry.setMoveUpButtonEnabled(i > 0);
      entry.setMoveDownButtonEnabled(i < size - 1);
    }
  }

  /**
   * A list entry for a single checkpoint. Displays its coordinates and buttons to edit it.
   */
  @SideOnly(Side.CLIENT)
  private class GuiEntry implements IGuiListEntry {
    // Buttons
    private final GuiButton stopBtn;
    private final GuiButton deleteBtn;
    private final GuiButton moveUpBtn;
    private final GuiButton moveDownBtn;

    // Data
    private final PathCheckpoint checkpoint;

    /**
     * Create an entry for the given checkpoint.
     */
    public GuiEntry(PathCheckpoint checkpoint) {
      this.checkpoint = checkpoint;
      this.stopBtn = new GuiButton(0, 0, 0, 50, 20,
          I18n.format("gui.naissancee.light_orb_controller.checkpoint_list.entry.stop_button.label."
              + (checkpoint.isStop() ? "active" : "inactive")));
      this.deleteBtn = new GuiButton(0, 0, 0, 20, 20,
          I18n.format("gui.naissancee.light_orb_controller.checkpoint_list.entry.delete_button.label"));
      this.moveUpBtn = new GuiButton(0, 0, 0, 20, 20,
          I18n.format("gui.naissancee.light_orb_controller.checkpoint_list.entry.move_up_button.label"));
      this.moveDownBtn = new GuiButton(0, 0, 0, 20, 20,
          I18n.format("gui.naissancee.light_orb_controller.checkpoint_list.entry.move_down_button.label"));
    }

    public PathCheckpoint getCheckpoint() {
      return this.checkpoint;
    }

    public void setDeleteButtonEnabled(boolean enabled) {
      this.deleteBtn.enabled = enabled;
    }

    public void setMoveUpButtonEnabled(boolean enabled) {
      this.moveUpBtn.enabled = enabled;
    }

    public void setMoveDownButtonEnabled(boolean enabled) {
      this.moveDownBtn.enabled = enabled;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
      Minecraft mc = GuiPathCheckpointList.this.mc;
      mc.fontRenderer.drawString(
          Utils.blockPosToString(this.checkpoint.getPos()), x, y + (slotHeight - mc.fontRenderer.FONT_HEIGHT) / 2, 0xffffff);

      int xOffset = listWidth - this.deleteBtn.width;
      this.deleteBtn.x = x + xOffset;
      this.deleteBtn.y = y + (slotHeight - this.deleteBtn.height) / 2;
      this.deleteBtn.drawButton(mc, mouseX, mouseY, partialTicks);

      xOffset -= this.moveUpBtn.width + 4;
      this.moveUpBtn.x = x + xOffset;
      this.moveUpBtn.y = y + (slotHeight - this.moveUpBtn.height) / 2;
      this.moveUpBtn.drawButton(mc, mouseX, mouseY, partialTicks);

      xOffset -= this.moveDownBtn.width + 4;
      this.moveDownBtn.x = x + xOffset;
      this.moveDownBtn.y = y + (slotHeight - this.moveDownBtn.height) / 2;
      this.moveDownBtn.drawButton(mc, mouseX, mouseY, partialTicks);

      xOffset -= this.stopBtn.width + 4;
      this.stopBtn.x = x + xOffset;
      this.stopBtn.y = y + (slotHeight - this.stopBtn.height) / 2;
      this.stopBtn.drawButton(mc, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
      Minecraft mc = GuiPathCheckpointList.this.mc;
      if (this.stopBtn.mousePressed(mc, mouseX, mouseY)) {
        this.checkpoint.setStop(!this.checkpoint.isStop());
        this.stopBtn.displayString =
            I18n.format("gui.naissancee.light_orb_controller.checkpoint_list.entry.stop_button.label."
                + (this.checkpoint.isStop() ? "active" : "inactive"));
        GuiPathCheckpointList.this.onStopButtonClicked();
        return true;
      } else if (this.deleteBtn.mousePressed(mc, mouseX, mouseY)) {
        GuiPathCheckpointList.this.onDeleteButtonClicked(slotIndex);
        return true;
      } else if (this.moveUpBtn.mousePressed(mc, mouseX, mouseY)) {
        GuiPathCheckpointList.this.onMoveUpButtonClicked(slotIndex);
        return true;
      } else if (this.moveDownBtn.mousePressed(mc, mouseX, mouseY)) {
        GuiPathCheckpointList.this.onMoveDownButtonClicked(slotIndex);
        return true;
      }
      return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
      this.stopBtn.mouseReleased(x, y);
    }

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
    }
  }
}
