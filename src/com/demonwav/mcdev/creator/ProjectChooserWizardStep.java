package com.demonwav.mcdev.creator;

import com.demonwav.mcdev.exception.MinecraftSetupException;
import com.demonwav.mcdev.platform.PlatformType;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.HyperlinkEvent;

public class ProjectChooserWizardStep extends ModuleWizardStep {

    private final MinecraftProjectCreator creator;

    private JPanel chooserPanel;
    private JPanel panel;
    private JPanel infoPanel;

    private JRadioButton bukkitRadioButton;
    private JRadioButton spigotRadioButton;
    private JRadioButton bungeecordRadioButton;
    private JEditorPane infoPane;
    private JRadioButton spongeRadioButton;
    private JRadioButton paperRadioButton;
    private JRadioButton forgeRadioButton;

    private PlatformType type = PlatformType.BUKKIT;

    private static final String bukkitInfo = "<html><font size=\"4\">Create a standard " +
            "<a href=\"http://bukkit.org/\">Bukkit</a> plugin, for use " +
            "on CraftBukkit, Spigot, and Paper servers.</font></html>";
    private static final String spigotInfo = "<html><font size=\"4\">Create a standard " +
            "<a href=\"https://www.spigotmc.org/\">Spigot</a> plugin, for use " +
            "on Spigot and Paper servers.</font></html>";
    private static final String paperInfo = "<html><font size=\"4\">Create a standard " +
            "<a href=\"https://paper.emc.gs\">Paper</a> plugin, for use " +
            "on Paper servers.</font></html>";
    private static final String bungeeCordInfo = "<html><font size=\"4\">Create a standard " +
            "<a href=\"https://www.spigotmc.org/wiki/bungeecord/\"> BungeeCord</a> plugin, for use " +
            "on BungeeCord servers.</font></html>";
    private static final String spongeInfo = "<html><font size=\"4\">Create a standard " +
            "<a href=\"https://www.spongepowered.org/\"> Sponge</a> plugin, for use " +
            "on Sponge servers.</font></html>";
    private static final String forgeInfo = "<html><font size=\"4\">Create a standard " +
            "<a href=\"http://files.minecraftforge.net/\"> Forge</a> plugin, for use " +
            "on Forge servers.</font></html>";

    public ProjectChooserWizardStep(@NotNull MinecraftProjectCreator creator) {
        super();
        this.creator = creator;
    }

    @Override
    public JComponent getComponent() {
        chooserPanel.setBorder(IdeBorderFactory.createBorder());
        infoPanel.setBorder(IdeBorderFactory.createBorder());

        // HTML parsing and hyperlink support
        infoPane.setContentType("text/html");
        infoPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (URISyntaxException | IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        // Set initial text
        infoPane.setText(bukkitInfo);

        // Set type
        bukkitRadioButton.addChangeListener(e -> {
            if (type != PlatformType.BUKKIT) {
                type = PlatformType.BUKKIT;
                infoPane.setText(bukkitInfo);
                creator.setType(type);
            }
        });
        spigotRadioButton.addChangeListener(e -> {
            if (type != PlatformType.SPIGOT) {
                type = PlatformType.SPIGOT;
                infoPane.setText(spigotInfo);
                creator.setType(type);
            }
        });
        paperRadioButton.addChangeListener(e -> {
            if (type != PlatformType.PAPER) {
                type = PlatformType.PAPER;
                infoPane.setText(paperInfo);
                creator.setType(type);
            }
        });
        spongeRadioButton.addChangeListener(e -> {
            if (type != PlatformType.SPONGE) {
                type = PlatformType.SPONGE;
                infoPane.setText(spongeInfo);
                creator.setType(type);
            }
        });
        forgeRadioButton.addChangeListener(e -> {
            if (type != PlatformType.FORGE) {
                type = PlatformType.FORGE;
                infoPane.setText(forgeInfo);
                creator.setType(type);
            }
        });
        bungeecordRadioButton.addChangeListener(e -> {
            if (type != PlatformType.BUNGEECORD) {
                type = PlatformType.BUNGEECORD;
                infoPane.setText(bungeeCordInfo);
                creator.setType(type);
            }
        });
        return panel;
    }

    @Override
    public boolean validate() throws ConfigurationException {
        try {
            if (spongeRadioButton.isSelected()) {
                throw new MinecraftSetupException("sponge", spongeRadioButton);
            }
            if (forgeRadioButton.isSelected()) {
                throw new MinecraftSetupException("forge", forgeRadioButton);
            }
        } catch (MinecraftSetupException e) {
            String message = e.getError();
            JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(message, MessageType.ERROR, null)
                    .setFadeoutTime(2000)
                    .createBalloon()
                    .show(RelativePoint.getSouthWestOf(e.getJ()), Balloon.Position.below);
            return false;
        }
        return true;
    }

    @Override
    public void updateDataModel() {}
}
