package com.github.brunothg.jshooter.gui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;

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
public class LookAndFeelSettingsPanel extends JPanel implements SettingsPanel {
	private static final Logger LOG = LoggerFactory.getLogger(LookAndFeelSettingsPanel.class);
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserProperties userSettings;

	@Autowired
	private I18N language;

	private JComboBox<TitledElement<LookAndFeelInfo>> cbLaF;
	private DefaultComboBoxModel<TitledElement<LookAndFeelInfo>> cbLaFModel;
	private JPanel pnlPreview;

	@PostConstruct
	public void build() {
		removeAll();
		setLayout(new MigLayout("", "[][grow]", "[][][][][][][]"));

		JLabel lblTheme = new JLabel(language.get("theme"));
		add(lblTheme, "cell 0 0");

		cbLaF = new JComboBox<>();
		cbLaFModel = new DefaultComboBoxModel<TitledElement<LookAndFeelInfo>>();
		cbLaF.setModel(cbLaFModel);
		updateAvailableLaFs();
		cbLaF.addItemListener(getLaFChangeListener());
		add(cbLaF, "cell 1 1,growx");

		JLabel lblPreview = new JLabel(language.get("preview"));
		add(lblPreview, "cell 0 3");

		pnlPreview = buildPreviewPanel();

		JTextPane lblRestartInfo = new JTextPane();
		lblRestartInfo.setBorder(BorderFactory.createTitledBorder(language.get("settings-danger")));
		lblRestartInfo.setEditable(false);
		lblRestartInfo.setText(language.get("settings-restart-info"));
		add(lblRestartInfo, "cell 0 6 2 1,growx");
	}

	private JPanel buildPreviewPanel() {
		JPanel pnlPreview = new JPanel();
		pnlPreview.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(pnlPreview, "cell 1 4,grow");
		pnlPreview.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));

		JLabel lblTextfield = new JLabel(language.get("text-field"));
		pnlPreview.add(lblTextfield, "cell 0 0");

		JTextField txtLoremIpsum = new JTextField();
		txtLoremIpsum.setText("Lorem Ipsum");
		pnlPreview.add(txtLoremIpsum, "cell 1 1,growx");
		txtLoremIpsum.setColumns(10);

		JLabel lblButton = new JLabel(language.get("button"));
		pnlPreview.add(lblButton, "cell 0 2");

		JButton btnLoremIpsum = new JButton("Lorem Ipsum");
		pnlPreview.add(btnLoremIpsum, "cell 1 3,growx");

		JLabel lblProgressbar = new JLabel(language.get("progress-bar"));
		pnlPreview.add(lblProgressbar, "cell 0 4");

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		pnlPreview.add(progressBar, "cell 1 5,growx");

		JLabel lblCheckbox = new JLabel(language.get("check-box"));
		pnlPreview.add(lblCheckbox, "cell 0 6");

		JCheckBox chckbxLoremIpsum = new JCheckBox("Lorem Ipsum");
		pnlPreview.add(chckbxLoremIpsum, "cell 1 7,growx");

		return pnlPreview;
	}

	private ItemListener getLaFChangeListener() {
		return new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int stateChange = e.getStateChange();
				if (stateChange == ItemEvent.SELECTED) {
					TitledElement<LookAndFeelInfo> element = cbLaFModel.getElementAt(cbLaF.getSelectedIndex());
					String lookAndFeel = element.getElement().getClassName();
					LOG.debug("Selected LaF '{}'", lookAndFeel);

					LookAndFeelUtils.setLookAndFeelForComponent(lookAndFeel, pnlPreview);
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

	public String getSelectedLookAndFeelClassName() {
		TitledElement<LookAndFeelInfo> element = cbLaFModel.getElementAt(cbLaF.getSelectedIndex());
		String lookAndFeel = element.getElement().getClassName();

		return lookAndFeel;
	}

	public void applySettings() {
		String lookAndFeel = getSelectedLookAndFeelClassName();

		userSettings.setLookAndFeel(lookAndFeel);
		LookAndFeelUtils.updateLookAndFeel(lookAndFeel);
	}

	public JPanel getSettingsPanel() {
		return this;
	}

	public String getTitle() {
		return language.get("laf-settings-title");
	}

	public void showSettingsDialog(java.awt.Component parent) {
		Window windowForComponent = (parent != null) ? SwingUtilities.windowForComponent(parent) : null;

		final JDialog dialog = new JDialog(windowForComponent);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);

		LookAndFeelSettingsPanel settingsPanel = LookAndFeelSettingsPanel.this;
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
