package com.github.brunothg.jshooter.gui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.jshooter.config.UserProperties;
import com.github.brunothg.jshooter.utils.I18N;
import com.github.brunothg.jshooter.utils.LookAndFeelUtils;
import com.github.brunothg.jshooter.utils.TitledElement;
import com.github.brunothg.jshooter.utils.TitledElement.TitleCallback;

import net.miginfocom.swing.MigLayout;

@Component
@Scope("prototype")
public class LookAndFeelSettingsPanel extends JPanel {
	private static final Logger LOG = LoggerFactory.getLogger(LookAndFeelSettingsPanel.class);
	private static final long serialVersionUID = 1L;

	private UserProperties userSettings;
	private I18N language;

	private JComboBox<TitledElement<LookAndFeelInfo>> cbLaF;
	private DefaultComboBoxModel<TitledElement<LookAndFeelInfo>> cbLaFModel;

	@Autowired
	public LookAndFeelSettingsPanel(I18N language, UserProperties userSettings) {
		this.language = language;
		this.userSettings = userSettings;

		build();
	}

	private void build() {
		setLayout(new MigLayout("", "[][grow]", "[][]"));

		JLabel lblTheme = new JLabel("Theme");
		add(lblTheme, "cell 0 0");

		cbLaF = new JComboBox<>();
		cbLaFModel = new DefaultComboBoxModel<TitledElement<LookAndFeelInfo>>();
		cbLaF.setModel(cbLaFModel);
		updateAvailableLaFs();
		cbLaF.addItemListener(getLaFChangeListener());
		add(cbLaF, "cell 1 1,growx");
	}

	private ItemListener getLaFChangeListener() {
		return new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int stateChange = e.getStateChange();
				if (stateChange == ItemEvent.SELECTED) {
					TitledElement<LookAndFeelInfo> element = cbLaFModel.getElementAt(cbLaF.getSelectedIndex());
					String lookAndFeel = element.getElement().getClassName();
					LOG.debug("Selected LaF '{}'", lookAndFeel);

					LookAndFeelUtils.setLookAndFeelForComponent(lookAndFeel, LookAndFeelSettingsPanel.this);
				}
			}
		};
	}

	protected void updateAvailableLaFs() {
		LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
		LOG.debug("Update laf -> '{}'", Arrays.toString(lookAndFeels));

		String lookAndFeel = userSettings.getLookAndFeel();
		cbLaFModel.removeAllElements();
		for (LookAndFeelInfo lookAndFeelInfo : lookAndFeels) {
			TitledElement<LookAndFeelInfo> element = new TitledElement<LookAndFeelInfo>(lookAndFeelInfo,
					new TitleCallback<LookAndFeelInfo>() {
						public String getTitle(LookAndFeelInfo element) {
							return element.getName();
						}
					});
			cbLaFModel.addElement(element);

			if (lookAndFeel != null && !lookAndFeel.isEmpty()) {
				if (element.getElement().getClassName().equals(lookAndFeel)) {
					cbLaFModel.setSelectedItem(element);
				}
			}
		}
	}

	public void applySettings() {
		TitledElement<LookAndFeelInfo> element = cbLaFModel.getElementAt(cbLaF.getSelectedIndex());
		String lookAndFeel = element.getElement().getClassName();

		userSettings.setLookAndFeel(lookAndFeel);
		LookAndFeelUtils.updateLookAndFeel(lookAndFeel);
	}

	public void showSettingsDialog(java.awt.Component parent) {
		Window windowForComponent = (parent != null) ? SwingUtilities.windowForComponent(parent) : null;

		final JDialog dialog = new JDialog(windowForComponent);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);

		LookAndFeelSettingsPanel settingsPanel = LookAndFeelSettingsPanel.this;
		dialog.getContentPane().add(settingsPanel, BorderLayout.CENTER);
		dialog.setTitle(language.get("laf-settings-title"));

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.add(pnlButtons, BorderLayout.SOUTH);

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
