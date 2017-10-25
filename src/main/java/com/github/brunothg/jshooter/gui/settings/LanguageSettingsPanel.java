package com.github.brunothg.jshooter.gui.settings;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.jshooter.config.UserProperties;
import com.github.brunothg.jshooter.utils.I18N;
import com.github.brunothg.jshooter.utils.TitledElement;
import com.github.brunothg.jshooter.utils.TitledElement.TitleCallback;

import net.miginfocom.swing.MigLayout;

@Component
@Scope("prototype")
public class LanguageSettingsPanel extends JPanel implements SettingsPanel {
	private static final Logger LOG = LoggerFactory.getLogger(LanguageSettingsPanel.class);
	private static final long serialVersionUID = 1L;

	private UserProperties userSettings;
	private I18N language;

	private JComboBox<TitledElement<Locale>> cbLocale;
	private DefaultComboBoxModel<TitledElement<Locale>> cbLocaleModel;

	@Autowired
	public LanguageSettingsPanel(I18N language, UserProperties userSettings) {
		this.language = language;
		this.userSettings = userSettings;

		build();
	}

	private void build() {
		setLayout(new MigLayout("", "[][grow]", "[][]"));

		JLabel lblLocale = new JLabel(language.get("language"));
		add(lblLocale, "cell 0 0");

		cbLocale = new JComboBox<>();
		cbLocaleModel = new DefaultComboBoxModel<TitledElement<Locale>>();
		cbLocale.setModel(cbLocaleModel);
		updateAvailableLocales();
		add(cbLocale, "cell 1 1,growx");
	}

	protected void updateAvailableLocales() {
		Map<Locale, String> availableLanguages = language.getAvailableLanguages();
		Locale defaultLocale = language.getDefaultLocale();

		cbLocaleModel.removeAllElements();
		for (final Entry<Locale, String> language : availableLanguages.entrySet()) {
			TitledElement<Locale> element = new TitledElement<Locale>(language.getKey(), new TitleCallback<Locale>() {
				public String getTitle(Locale element) {
					return language.getValue();
				}
			});
			cbLocaleModel.addElement(element);

			if (defaultLocale != null) {
				if (element.getElement().equals(defaultLocale)) {
					cbLocaleModel.setSelectedItem(element);
				}
			}
		}
	}

	public Locale getSelectedLanguage() {
		return cbLocaleModel.getElementAt(cbLocale.getSelectedIndex()).getElement();
	}

	public void applySettings() {
		Locale locale = getSelectedLanguage();
		LOG.info("Setting language -> '{}'", locale);

		userSettings.setLocale(locale);
		Locale.setDefault(locale);
	}

	public JPanel getSettingsPanel() {
		return this;
	}

	public String getTitle() {
		return language.get("language-settings-title");
	}

	public void showSettingsDialog(java.awt.Component parent) {
		Window windowForComponent = (parent != null) ? SwingUtilities.windowForComponent(parent) : null;

		final JDialog dialog = new JDialog(windowForComponent);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);

		LanguageSettingsPanel settingsPanel = LanguageSettingsPanel.this;
		dialog.getContentPane().add(settingsPanel, BorderLayout.CENTER);
		dialog.setTitle(getTitle());

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
