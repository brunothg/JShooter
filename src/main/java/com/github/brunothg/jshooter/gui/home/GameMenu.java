package com.github.brunothg.jshooter.gui.home;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.jshooter.gui.settings.SettingsCentral;
import com.github.brunothg.jshooter.gui.settings.SettingsPanel;
import com.github.brunothg.jshooter.utils.I18N;

@Component
@Scope("prototype")
public class GameMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;

	@Autowired
	private I18N language;

	@Autowired
	private SettingsCentral settingsCentral;

	@Autowired
	private List<SettingsPanel> settingPanels;

	@PostConstruct
	public void build() {
		removeAll();

		add(buildSettingsMenu());
	}

	private JMenu buildSettingsMenu() {
		JMenu mSettings = new JMenu(language.get("menu-settings"));

		JMenuItem miCentral = new JMenuItem(language.get("menu-settings-central"));
		miCentral.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsCentral.showSettingsDialog(GameMenu.this);
			}
		});
		mSettings.add(miCentral);

		mSettings.addSeparator();

		for (final SettingsPanel settingsPanel : settingPanels) {
			JMenuItem miSettings = new JMenuItem(settingsPanel.getTitle());
			miSettings.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					settingsPanel.showSettingsDialog(GameMenu.this);
				}
			});
			mSettings.add(miSettings);
		}

		return mSettings;
	}
}
