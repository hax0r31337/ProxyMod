package me.liuli.proxymod.gui;

import me.liuli.proxymod.ProxyMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiProxyManager extends GuiScreen {

    private final GuiScreen parent;

    private GuiTextField txtProxyAddress;
    private GuiButton btnProxyType;
    private GuiButton btnProxyEnabled;

    public GuiProxyManager(final GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.txtProxyAddress = new GuiTextField(3, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.txtProxyAddress.setMaxStringLength(128);
        this.txtProxyAddress.setFocused(true);
        this.txtProxyAddress.setText(ProxyMod.getInstance().getProxyAddress());
        this.btnProxyType = new GuiButton(1, width / 2 - 100, height / 4 + 96, "");
        this.btnProxyEnabled = new GuiButton(2, width / 2 - 100, height / 4 + 120, "");
        updateButtons();
        this.buttonList.add(this.btnProxyType);
        this.buttonList.add(this.btnProxyEnabled);
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 144, "Done"));
    }

    private void updateButtons() {
        this.btnProxyType.displayString = "Type: " + ProxyMod.getInstance().getProxyType().name();
        this.btnProxyEnabled.displayString = ProxyMod.getInstance().isProxyEnabled() ? "§aEnabled" : "§cDisabled";
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.drawBackground(0);
        this.drawCenteredString(mc.fontRendererObj, "Proxy Manager", width / 2, 34, 0xffffff);
        this.txtProxyAddress.drawTextBox();
        if (this.txtProxyAddress.getText().isEmpty() && !this.txtProxyAddress.isFocused()) {
            this.drawString(mc.fontRendererObj, "Enter proxy address here...", width / 2 - 100, 60, 0xffffff);
        }
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }

    @Override
    protected void actionPerformed(GuiButton p_actionPerformed_1_) {
        if (p_actionPerformed_1_.id == 0) {
            this.mc.displayGuiScreen(this.parent);
            return;
        } else if (p_actionPerformed_1_.id == 1) {
            ProxyMod.getInstance().setProxyType(ProxyMod.ProxyType.values()[(ProxyMod.getInstance().getProxyType().ordinal() + 1) % ProxyMod.ProxyType.values().length]);
        } else if (p_actionPerformed_1_.id == 2) {
            ProxyMod.getInstance().setProxyEnabled(!ProxyMod.getInstance().isProxyEnabled());
        }
        updateButtons();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        ProxyMod.getInstance().setProxyAddress(this.txtProxyAddress.getText());
        ProxyMod.getInstance().saveConfig();
    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        if (p_keyTyped_2_ == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(this.parent);
        }

        this.txtProxyAddress.textboxKeyTyped(p_keyTyped_1_, p_keyTyped_2_);
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
    }

    @Override
    protected void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        this.txtProxyAddress.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
        super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }

    @Override
    public void updateScreen() {
        this.txtProxyAddress.updateCursorCounter();
        super.updateScreen();
    }
}
