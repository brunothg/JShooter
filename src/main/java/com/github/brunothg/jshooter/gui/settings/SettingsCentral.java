package com.github.brunothg.jshooter.gui.settings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.jshooter.config.UserProperties;
import com.github.brunothg.jshooter.utils.I18N;

@Component
@Scope("prototype")
public class SettingsCentral extends JPanel {
	private static final Logger LOG = LoggerFactory.getLogger(SettingsCentral.class);
	private static final long serialVersionUID = 1L;

	private UserProperties userSettings;
	private I18N language;

	@Autowired
	public SettingsCentral(I18N language, UserProperties userSettings) {
		this.language = language;
		this.userSettings = userSettings;

		build();
	}

	private void build() {
		// TODO build
	}

	public void applySettings() {
		// TODO applySettings
	}

	public void showSettingsDialog(java.awt.Component parent) {
		Window windowForComponent = (parent != null) ? SwingUtilities.windowForComponent(parent) : null;

		final JDialog dialog = new JDialog(windowForComponent);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);

		SettingsCentral settingsPanel = SettingsCentral.this;
		dialog.getContentPane().add(settingsPanel, BorderLayout.CENTER);
		dialog.setTitle(language.get("settings-title"));

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.getContentPane().add(pnlButtons, BorderLayout.SOUTH);

		JButton btnOk = new JButton(language.get("dialog-ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applySettings();
				dialog.dispose();
			}
		});
		pnlButtons.add(btnOk);

		JButton btnCancel = new JButton(language.get("dialog-cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		pnlButtons.add(btnCancel);

		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
}
