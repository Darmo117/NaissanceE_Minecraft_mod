package net.darmo_creations.naissancee.gui;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.todo_list.ToDoList;
import net.darmo_creations.naissancee.todo_list.ToDoListItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Renderer for to-do lists.
 */
@SideOnly(Side.CLIENT)
public class GuiToDoList extends Gui {
  private static final int PADDING = 3;

  private final Minecraft mc;

  public GuiToDoList(final Minecraft mc) {
    this.mc = mc;
  }

  @SubscribeEvent
  public void onRender(RenderGameOverlayEvent.Post event) {
    ToDoList globalList = NaissanceE.TODO_LISTS_MANAGER.getGlobalData();
    ToDoList playerList = NaissanceE.TODO_LISTS_MANAGER.getOrCreatePlayerData(this.mc.player);
    if (globalList.isVisible()) {
      this.renderList(globalList, TextFormatting.ITALIC + "<Global>" + TextFormatting.RESET, true);
    }
    if (playerList.isVisible()) {
      this.renderList(playerList, this.mc.player.getGameProfile().getName(), false);
    }
  }

  /**
   * Render a list on the right or left side of the screen.
   *
   * @param list       List to render.
   * @param playerName Name of the player associated to the list.
   * @param leftSide   True to draw on the left side, false for the right.
   */
  private void renderList(final ToDoList list, final String playerName, boolean leftSide) {
    ScaledResolution scaledresolution = new ScaledResolution(this.mc);
    String title = I18n.format("gui.naissancee.todo_list.title", playerName);

    int width = this.mc.fontRenderer.getStringWidth(title);
    int height = this.mc.fontRenderer.FONT_HEIGHT * 2;

    List<String> itemStrings = new ArrayList<>();
    int nbDigits = 1 + (int) Math.floor(Math.log10(list.size()));
    int i = 0;
    for (ToDoListItem item : list) {
      String text = String.format(
          "[%s%s%s] %0" + nbDigits + "d. %s",
          item.isChecked() ? TextFormatting.GREEN : TextFormatting.RED,
          item.isChecked() ? "V" : "X",
          TextFormatting.RESET,
          i + 1,
          item.getText()
      );
      itemStrings.add(text);
      height += this.mc.fontRenderer.FONT_HEIGHT;
      width = Math.max(width, this.mc.fontRenderer.getStringWidth(text));
      i++;
    }
    width += 2 * PADDING;

    int x = leftSide ? 0 : (scaledresolution.getScaledWidth() - width);
    int y = 0;

//    drawRect(x, y, x + width, y + height + 2 * PADDING, 0x80808080); // FIXME draw transparent rectangle

    y += PADDING;
    this.drawCenteredString(this.mc.fontRenderer, title, x + width / 2, y, Utils.WHITE);
    y += this.mc.fontRenderer.FONT_HEIGHT;
    this.drawCenteredString(this.mc.fontRenderer, "---", x + width / 2, y, Utils.WHITE);
    y += this.mc.fontRenderer.FONT_HEIGHT;
    for (String line : itemStrings) {
      this.drawString(this.mc.fontRenderer, line, x + PADDING, y, Utils.WHITE);
      y += this.mc.fontRenderer.FONT_HEIGHT;
    }
  }
}
