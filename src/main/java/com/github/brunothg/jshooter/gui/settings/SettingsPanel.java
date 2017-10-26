package com.github.brunothg.jshooter.gui.settings;

import java.awt.Component;

import javax.swing.JPanel;

import org.springframework.stereotype.Service;

@Service
public interface SettingsPanel {
	public void applySettings();

	public String getTitle();

	public JPanel getSettingsPanel();

	public void showSettingsDialog(Component parent);
}
