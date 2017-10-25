package com.github.brunothg.jshooter.gui.settings;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.jshooter.Application;
import com.github.brunothg.jshooter.utils.I18N;
import com.github.brunothg.jshooter.utils.TitledElement;
import com.github.brunothg.jshooter.utils.TitledElement.TitleCallback;
import javax.swing.border.BevelBorder;

@Component
@Scope("prototype")
public class SettingsCentral extends JPanel {
	private static final Logger LOG = LoggerFactory.getLogger(SettingsCentral.class);
	private static final long serialVersionUID = 1L;

	private I18N language;

	private JList<TitledElement<SettingsPanel>> liSettings;
	private DefaultListModel<TitledElement<SettingsPanel>> liSettingsModel;
	private JPanel pnlSettings;

	@Autowired
	public SettingsCentral(I18N language) {
		this.language = language;

		build();
	}

	private void build() {
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		pnlSettings = new JPanel();
		pnlSettings.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlSettings.setLayout(new BorderLayout());
		splitPane.setRightComponent(pnlSettings);

		liSettings = new JList<>();
		liSettingsModel = new DefaultListModel<>();
		liSettings.setModel(liSettingsModel);
		liSettings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateSettingsList();
		liSettings.addListSelectionListener(getSettingsSelectionListener());
		scrollPane.setViewportView(liSettings);

	}

	private ListSelectionListener getSettingsSelectionListener() {
		return new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				SettingsPanel element = liSettingsModel.getElementAt(liSettings.getSelectedIndex()).getElement();
				pnlSettings.removeAll();
				pnlSettings.add(element.getSettingsPanel(), BorderLayout.CENTER);
				pnlSettings.invalidate();
				pnlSettings.revalidate();
				pnlSettings.repaint();
			}
		};
	}

	protected void updateSettingsList() {
		ApplicationContext ctx = Application.getApplicationContext();
		List<SettingsPanel> settingPanels = new ArrayList<>(ctx.getBeansOfType(SettingsPanel.class).values());
		Collections.sort(settingPanels, new Comparator<SettingsPanel>() {
			public int compare(SettingsPanel o1, SettingsPanel o2) {
				return o1.getTitle().compareTo(o2.getTitle());
			}
		});

		liSettingsModel.removeAllElements();
		for (SettingsPanel settingsPanel : settingPanels) {
			liSettingsModel
					.addElement(new TitledElement<SettingsPanel>(settingsPanel, new TitleCallback<SettingsPanel>() {
						public String getTitle(SettingsPanel element) {
							return element.getTitle();
						}
					}));
		}

		liSettings.setSelectedIndex(0);
		SettingsPanel element = liSettingsModel.getElementAt(liSettings.getSelectedIndex()).getElement();
		pnlSettings.removeAll();
		pnlSettings.add(element.getSettingsPanel(), BorderLayout.CENTER);
	}

	public void applySettings() {
		LOG.debug("Apply all changes");
		for (int i = 0; i < liSettingsModel.size(); i++) {
			SettingsPanel element = liSettingsModel.getElementAt(i).getElement();
			LOG.debug("Save settings for '{}'", element.getTitle());
			element.applySettings();
		}
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

		dialog.setSize(800, 600);
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
}
