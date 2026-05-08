package com.mycompany.studentrecordsystem;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AdminAIFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminAIFrame.class.getName());

    private final Color pageBg = new Color(236, 240, 245);
    private final Color shellBg = new Color(6, 22, 43);
    private final Color panelDark = new Color(9, 28, 54);
    private final Color bubbleDark = new Color(18, 39, 70);
    private final Color borderBlue = new Color(39, 96, 153);
    private final Color accentBlue = new Color(15, 98, 146);
    private final Color accentCyan = new Color(38, 198, 218);
    private final Color accentGreen = new Color(34, 197, 94);
    private final Color accentOrange = new Color(245, 158, 11);
    private final Color textLight = new Color(244, 247, 250);
    private final Color textMuted = new Color(154, 169, 191);

    public AdminAIFrame() {
        initComponents();
        setupModernAIFrame();
        addWelcomeMessage();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        appRootPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        titleTextPanel = new javax.swing.JPanel();
        pageSubtitleLabel = new javax.swing.JLabel();
        chipsPanel = new javax.swing.JPanel();
        backHomeButton = new javax.swing.JButton();
        aiReadyChipPanel = new javax.swing.JPanel();
        aiReadyLabel = new javax.swing.JLabel();
        adminModeChipPanel = new javax.swing.JPanel();
        adminModeLabel = new javax.swing.JLabel();
        shellPanel = new javax.swing.JPanel();
        shellHeaderPanel = new javax.swing.JPanel();
        assistantTextPanel = new javax.swing.JPanel();
        assistantNameLabel = new javax.swing.JLabel();
        assistantDescLabel = new javax.swing.JLabel();
        hintsPanel = new javax.swing.JPanel();
        hintLabel1 = new javax.swing.JLabel();
        hintLabel2 = new javax.swing.JLabel();
        hintLabel3 = new javax.swing.JLabel();
        chatSurfacePanel = new javax.swing.JPanel();
        chatScrollPane = new javax.swing.JScrollPane();
        chatMessagesPanel = new javax.swing.JPanel();
        previewBubblePanel = new javax.swing.JPanel();
        previewMessageLabel1 = new javax.swing.JLabel();
        previewMessageLabel2 = new javax.swing.JLabel();
        inputShellPanel = new javax.swing.JPanel();
        promptScrollPane = new javax.swing.JScrollPane();
        promptArea = new javax.swing.JTextArea();
        runButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ana Admin AI - Admin Users Supported");
        setMinimumSize(new java.awt.Dimension(960, 620));

        appRootPanel.setBackground(new java.awt.Color(236, 240, 245));
        appRootPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 18, 22));
        appRootPanel.setLayout(new java.awt.BorderLayout(0, 18));

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.BorderLayout());

        titleTextPanel.setOpaque(false);
        titleTextPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        pageSubtitleLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pageSubtitleLabel.setForeground(new java.awt.Color(99, 115, 129));
        pageSubtitleLabel.setText("Admin assistant AI");
        titleTextPanel.add(pageSubtitleLabel);

        headerPanel.add(titleTextPanel, java.awt.BorderLayout.WEST);

        chipsPanel.setOpaque(false);
        chipsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        backHomeButton.setBackground(new java.awt.Color(15, 98, 146));
        backHomeButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        backHomeButton.setForeground(new java.awt.Color(255, 255, 255));
        backHomeButton.setText("← Back to Home");
        backHomeButton.setBorderPainted(false);
        backHomeButton.setFocusPainted(false);
        backHomeButton.setPreferredSize(new java.awt.Dimension(135, 37));
        chipsPanel.add(backHomeButton);

        aiReadyChipPanel.setBackground(new java.awt.Color(8, 30, 58));
        aiReadyChipPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(30, 64, 100)));
        aiReadyChipPanel.setLayout(new java.awt.BorderLayout());

        aiReadyLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        aiReadyLabel.setForeground(new java.awt.Color(34, 197, 94));
        aiReadyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aiReadyLabel.setText("● AI Ready");
        aiReadyLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(9, 14, 9, 14));
        aiReadyChipPanel.add(aiReadyLabel);

        chipsPanel.add(aiReadyChipPanel);

        adminModeChipPanel.setBackground(new java.awt.Color(245, 158, 11));
        adminModeChipPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 193, 59)));
        adminModeChipPanel.setLayout(new java.awt.BorderLayout());

        adminModeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        adminModeLabel.setForeground(new java.awt.Color(255, 255, 255));
        adminModeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adminModeLabel.setText("ADMIN MODE");
        adminModeLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(9, 14, 9, 14));
        adminModeChipPanel.add(adminModeLabel);

        chipsPanel.add(adminModeChipPanel);

        headerPanel.add(chipsPanel, java.awt.BorderLayout.EAST);

        appRootPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        shellPanel.setBackground(new java.awt.Color(6, 22, 43));
        shellPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        shellPanel.setLayout(new java.awt.BorderLayout(0, 16));

        shellHeaderPanel.setOpaque(false);
        shellHeaderPanel.setLayout(new java.awt.BorderLayout(12, 0));

        assistantTextPanel.setOpaque(false);
        assistantTextPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        assistantNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        assistantNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        assistantNameLabel.setText(" ANA AI ASSISTANT");
        assistantTextPanel.add(assistantNameLabel);

        assistantDescLabel.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        assistantDescLabel.setForeground(new java.awt.Color(154, 169, 191));
        assistantDescLabel.setText(" Ask about students, courses, schedules, grades, sections, rooms, instructors, and other database tables.");
        assistantTextPanel.add(assistantDescLabel);

        shellHeaderPanel.add(assistantTextPanel, java.awt.BorderLayout.CENTER);

        hintsPanel.setOpaque(false);
        hintsPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        hintLabel1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        hintLabel1.setForeground(new java.awt.Color(186, 230, 253));
        hintLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hintLabel1.setText("Try: show all students");
        hintsPanel.add(hintLabel1);

        hintLabel2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        hintLabel2.setForeground(new java.awt.Color(186, 230, 253));
        hintLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hintLabel2.setText("Try: archive student 2026-0001");
        hintsPanel.add(hintLabel2);

        hintLabel3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        hintLabel3.setForeground(new java.awt.Color(186, 230, 253));
        hintLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hintLabel3.setText("Try: show schedules");
        hintsPanel.add(hintLabel3);

        shellHeaderPanel.add(hintsPanel, java.awt.BorderLayout.EAST);

        shellPanel.add(shellHeaderPanel, java.awt.BorderLayout.NORTH);

        chatSurfacePanel.setBackground(new java.awt.Color(10, 18, 32));
        chatSurfacePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(37, 65, 98)));
        chatSurfacePanel.setLayout(new java.awt.BorderLayout());

        chatScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        chatMessagesPanel.setBackground(new java.awt.Color(10, 18, 32));
        chatMessagesPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));
        chatMessagesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        previewBubblePanel.setBackground(new java.awt.Color(18, 39, 70));
        previewBubblePanel.setPreferredSize(new java.awt.Dimension(620, 110));
        previewBubblePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        previewBubblePanel.setLayout(new java.awt.GridLayout(2, 1, 0, 6));

        previewMessageLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        previewMessageLabel1.setForeground(new java.awt.Color(255, 255, 255));
        previewMessageLabel1.setText("Welcome back, Admin. I can help you view records, create records, update records,");
        previewBubblePanel.add(previewMessageLabel1);

        previewMessageLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        previewMessageLabel2.setForeground(new java.awt.Color(255, 255, 255));
        previewMessageLabel2.setText("archive records, delete records, and search your student record database.");
        previewBubblePanel.add(previewMessageLabel2);

        chatMessagesPanel.add(previewBubblePanel);

        chatScrollPane.setViewportView(chatMessagesPanel);

        chatSurfacePanel.add(chatScrollPane, java.awt.BorderLayout.CENTER);

        shellPanel.add(chatSurfacePanel, java.awt.BorderLayout.CENTER);

        inputShellPanel.setBackground(new java.awt.Color(17, 34, 58));
        inputShellPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        inputShellPanel.setLayout(new java.awt.BorderLayout(10, 0));

        promptScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(37, 99, 140)));

        promptArea.setBackground(new java.awt.Color(12, 26, 45));
        promptArea.setColumns(20);
        promptArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        promptArea.setForeground(new java.awt.Color(244, 247, 250));
        promptArea.setLineWrap(true);
        promptArea.setRows(2);
        promptArea.setWrapStyleWord(true);
        promptScrollPane.setViewportView(promptArea);

        inputShellPanel.add(promptScrollPane, java.awt.BorderLayout.CENTER);

        runButton.setBackground(new java.awt.Color(15, 98, 146));
        runButton.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        runButton.setForeground(new java.awt.Color(255, 255, 255));
        runButton.setText("SEND");
        runButton.setBorderPainted(false);
        runButton.setFocusPainted(false);
        runButton.setPreferredSize(new java.awt.Dimension(112, 48));
        inputShellPanel.add(runButton, java.awt.BorderLayout.EAST);

        shellPanel.add(inputShellPanel, java.awt.BorderLayout.SOUTH);

        appRootPanel.add(shellPanel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(appRootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(appRootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void setupModernAIFrame() {
        setSize(1080, 730);
        setMinimumSize(new Dimension(960, 620));
        getContentPane().setBackground(pageBg);
        pageSubtitleLabel.setText("Admin assistant AI");
        chatMessagesPanel.removeAll();
        chatMessagesPanel.setLayout(new BoxLayout(chatMessagesPanel, BoxLayout.Y_AXIS));
        chatMessagesPanel.setBorder(new EmptyBorder(14, 14, 14, 14));
        chatMessagesPanel.setBackground(new Color(10, 18, 32));
        chatScrollPane.getViewport().setBackground(new Color(10, 18, 32));
        chatScrollPane.getVerticalScrollBar().setUnitIncrement(18);
        promptArea.setCaretColor(accentCyan);
        promptArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        promptArea.setText("");
        runButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        runButton.addActionListener(e -> runAdminAction());

        backHomeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backHomeButton.setBackground(accentBlue);
        backHomeButton.setForeground(Color.WHITE);
        backHomeButton.setFocusPainted(false);
        backHomeButton.setBorderPainted(false);
        backHomeButton.addActionListener(e -> dispose());
        styleChip(aiReadyChipPanel, new Color(8, 30, 58), new Color(30, 64, 100));
        styleChip(adminModeChipPanel, accentOrange, new Color(255, 193, 59));
    }

    private void styleChip(JPanel panel, Color bg, Color line) {
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(line),
                new EmptyBorder(0, 0, 0, 0)
        ));
    }

    private void addWelcomeMessage() {
        addAssistantTextMessage("Welcome back, Admin. I can help you view records, create records, update records, archive records, delete records, and search your student record database.");
    }

    private void runAdminAction() {
        String adminPrompt = promptArea.getText().trim();

        if (adminPrompt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an admin prompt.");
            return;
        }

        addUserMessage(adminPrompt);
        promptArea.setText("");
        runButton.setEnabled(false);
        runButton.setText("WAIT...");
        aiReadyLabel.setText("● Processing");
        aiReadyLabel.setForeground(accentOrange);

        JPanel loadingBubble = addAssistantLoadingMessage();

        new Thread(() -> {
            try {
                JSONObject actionJson = OllamaAdminService.getAdminAction(adminPrompt);
                String intent = actionJson.optString("intent", "");

                if (needsConfirmation(intent)) {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to proceed?\n\nThis action may change your database records.",
                            "Confirm Action",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm != JOptionPane.YES_OPTION) {
                        SwingUtilities.invokeLater(() -> {
                            chatMessagesPanel.remove(loadingBubble);
                            addAssistantTextMessage("Action cancelled.");
                            resetSendButton();
                        });
                        return;
                    }
                }

                String result = AdminActionExecutor.execute(actionJson);

                SwingUtilities.invokeLater(() -> {
                    chatMessagesPanel.remove(loadingBubble);
                    addAssistantResultMessage(intent, result);
                    showSuccessDialogIfNeeded(intent, result);
                    resetSendButton();
                    scrollToBottom();
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    chatMessagesPanel.remove(loadingBubble);
                    addAssistantTextMessage("Invalid information.");
                    resetSendButton();
                });
            }
        }).start();
    }

    private void resetSendButton() {
        runButton.setEnabled(true);
        runButton.setText("SEND  ➜");
        aiReadyLabel.setText("● AI Ready");
        aiReadyLabel.setForeground(accentGreen);
        refreshChat();
    }

    private void addUserMessage(String textValue) {
        RoundedBubble bubble = new RoundedBubble(18, accentBlue, new Color(56, 189, 248));
        bubble.setLayout(new BorderLayout());
        bubble.setBorder(new EmptyBorder(12, 14, 12, 14));
        bubble.setMaximumSize(new Dimension(720, Integer.MAX_VALUE));

        JTextArea message = createMessageTextArea(textValue, accentBlue, Color.WHITE);
        bubble.add(message, BorderLayout.CENTER);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(6, 15, 6, 15));
        wrapper.add(Box.createHorizontalGlue());
        wrapper.add(bubble);

        chatMessagesPanel.add(wrapper);
        chatMessagesPanel.add(Box.createVerticalStrut(8));
        refreshChat();
    }

    private JPanel addAssistantLoadingMessage() {
        RoundedBubble bubble = new RoundedBubble(18, new Color(17, 34, 58), accentCyan);
        bubble.setLayout(new BorderLayout());
        bubble.setBorder(new EmptyBorder(12, 14, 12, 14));

        JTextArea message = createMessageTextArea("Ana is analyzing your request...", new Color(17, 34, 58), textLight);
        bubble.add(message, BorderLayout.CENTER);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(6, 15, 6, 15));
        wrapper.add(bubble);
        wrapper.add(Box.createHorizontalGlue());

        chatMessagesPanel.add(wrapper);
        chatMessagesPanel.add(Box.createVerticalStrut(8));
        refreshChat();

        return wrapper;
    }

    private void addAssistantTextMessage(String textValue) {
        RoundedBubble bubble = new RoundedBubble(18, new Color(17, 34, 58), new Color(51, 85, 120));
        bubble.setLayout(new BorderLayout());
        bubble.setBorder(new EmptyBorder(12, 14, 12, 14));
        bubble.setMaximumSize(new Dimension(760, Integer.MAX_VALUE));

        JTextArea message = createMessageTextArea(textValue, new Color(17, 34, 58), textLight);
        bubble.add(message, BorderLayout.CENTER);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(6, 15, 6, 15));
        wrapper.add(bubble);
        wrapper.add(Box.createHorizontalGlue());

        chatMessagesPanel.add(wrapper);
        chatMessagesPanel.add(Box.createVerticalStrut(8));
        refreshChat();
    }

    private void addAssistantResultMessage(String intent, String result) {
        RoundedBubble bubble = new RoundedBubble(22, new Color(17, 34, 58), accentCyan);
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBorder(new EmptyBorder(16, 16, 16, 16));
        bubble.setMaximumSize(new Dimension(840, Integer.MAX_VALUE));

        JLabel title = new JLabel("Ana response");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(new Color(186, 230, 253));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        bubble.add(title);
        bubble.add(Box.createVerticalStrut(10));

        JTable table = buildResultTable(intent, result);

        if (table != null) {
            JScrollPane tableScroll = new JScrollPane(table);
            tableScroll.setBorder(createTitledBorder("Requested Information"));
            tableScroll.setPreferredSize(new Dimension(780, 280));
            tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            bubble.add(tableScroll);
        } else {
            String messageText = cleanResultMessage(result);

            if (messageText == null || messageText.isBlank()) {
                messageText = "No requested information available.";
            }

            JTextArea infoArea = createMessageTextArea(messageText, new Color(12, 26, 45), textLight);
            JScrollPane infoScroll = new JScrollPane(infoArea);
            infoScroll.setBorder(createTitledBorder("Requested Information"));
            infoScroll.setPreferredSize(new Dimension(780, 120));
            infoScroll.getViewport().setBackground(new Color(12, 26, 45));
            infoScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            bubble.add(infoScroll);
        }

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(6, 15, 6, 15));
        wrapper.add(bubble);
        wrapper.add(Box.createHorizontalGlue());

        chatMessagesPanel.add(wrapper);
        chatMessagesPanel.add(Box.createVerticalStrut(8));
        refreshChat();
    }

    private Border createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 85, 120)),
                title,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(186, 230, 253)
        );
    }

    private String cleanResultMessage(String result) {
        String lower = result.toLowerCase();

        if (lower.contains("validation failed")) {
            return "Validation unsuccessful.";
        }

        if (lower.contains("not found")
                || lower.contains("no records found")
                || lower.contains("no active students found")
                || lower.contains("no archived students found")
                || lower.contains("no regular students found")
                || lower.contains("no irregular students found")
                || lower.contains("unsupported intent")
                || lower.contains("invalid ai response")
                || lower.contains("invalid table")) {
            return "Invalid information.";
        }

        return result;
    }

    private JTable buildResultTable(String intent, String result) {
        String cleaned = cleanResultMessage(result);

        if (cleaned.equals("Validation unsuccessful.") || cleaned.equals("Invalid information.")) {
            return null;
        }

        if (intent.equals("get_table_records")
                || intent.equals("get_active_records")
                || intent.equals("get_archived_records")
                || intent.equals("get_admin_users")
                || intent.equals("get_active_admin_users")
                || intent.equals("get_archived_admin_users")
                || intent.equals("get_admin_user_by_id")) {
            return buildGenericTable(result);
        }

        if (!(intent.equals("get_all_students")
                || intent.equals("get_active_students")
                || intent.equals("get_archived_students")
                || intent.equals("get_regular_students")
                || intent.equals("get_irregular_students")
                || intent.equals("get_students_by_section"))) {
            return null;
        }

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Student ID", "Student Number", "Full Name", "Section"});

        String[] lines = result.split("\\n");
        for (String line : lines) {
            if (!line.startsWith("ID:")) continue;
            String[] parts = line.split("\\|");
            String id = parts.length > 0 ? parts[0].replace("ID:", "").trim() : "";
            String studentNumber = parts.length > 1 ? parts[1].trim() : "";
            String fullName = parts.length > 2 ? parts[2].trim() : "";
            String section = parts.length > 3 ? parts[3].replace("Section:", "").trim() : "";
            model.addRow(new Object[]{id, studentNumber, fullName, section});
        }

        JTable table = new JTable(model);
        styleResultTable(table);
        return table;
    }

    private JTable buildGenericTable(String result) {
        String[] lines = result.split("\\n");
        String columnLine = null;
        List<String> rowLines = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("COLUMNS:")) {
                columnLine = line.replace("COLUMNS:", "").trim();
            } else if (line.startsWith("ROW:")) {
                rowLines.add(line.replace("ROW:", "").trim());
            }
        }

        if (columnLine == null || rowLines.isEmpty()) {
            return null;
        }

        String[] columns = columnLine.split("\\|");
        for (int i = 0; i < columns.length; i++) columns[i] = columns[i].trim();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        for (String rowLine : rowLines) {
            String[] values = rowLine.split("\\|", -1);
            for (int i = 0; i < values.length; i++) values[i] = values[i].trim();
            model.addRow(values);
        }

        JTable table = new JTable(model);
        styleResultTable(table);
        return table;
    }

    private void styleResultTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setForeground(new Color(33, 43, 54));
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(226, 232, 240));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(226, 242, 255));
        table.getTableHeader().setForeground(new Color(15, 23, 42));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private JTextArea createMessageTextArea(String textValue, Color bg, Color fg) {
        JTextArea area = new JTextArea(textValue);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(6, 6, 6, 6));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setBackground(bg);
        area.setForeground(fg);
        area.setOpaque(true);
        return area;
    }

    private boolean needsConfirmation(String intent) {
        return intent.equals("create_student")
                || intent.equals("update_student")
                || intent.equals("archive_student")
                || intent.equals("delete_student")
                || intent.equals("create_record")
                || intent.equals("update_record")
                || intent.equals("archive_record")
                || intent.equals("restore_record")
                || intent.equals("restore_student")
                || intent.equals("delete_record");
    }

    private void showSuccessDialogIfNeeded(String intent, String result) {
        if (!result.toLowerCase().contains("successfully")) return;

        String message = switch (intent) {
            case "create_student", "create_record" -> "Record created successfully.";
            case "update_student", "update_record" -> "Record updated successfully.";
            case "archive_student", "archive_record" -> "Record archived successfully.";
            case "restore_student", "restore_record" -> "Record restored successfully.";
            case "delete_student", "delete_record" -> "Record deleted successfully.";
            default -> null;
        };

        if (message != null) {
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshChat() {
        chatMessagesPanel.revalidate();
        chatMessagesPanel.repaint();
        scrollToBottom();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = chatScrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new AdminAIFrame().setVisible(true));
    }

    static class RoundedBubble extends JPanel {
        private final int radius;
        private final Color fill;
        private final Color line;

        RoundedBubble(int radius, Color fill, Color line) {
            this.radius = radius;
            this.fill = fill;
            this.line = line;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape shape = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(fill);
            g2.fill(shape);
            g2.setColor(line);
            g2.draw(shape);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adminModeChipPanel;
    private javax.swing.JLabel adminModeLabel;
    private javax.swing.JPanel aiReadyChipPanel;
    private javax.swing.JLabel aiReadyLabel;
    private javax.swing.JPanel appRootPanel;
    private javax.swing.JLabel assistantDescLabel;
    private javax.swing.JLabel assistantNameLabel;
    private javax.swing.JPanel assistantTextPanel;
    private javax.swing.JButton backHomeButton;
    private javax.swing.JPanel chatMessagesPanel;
    private javax.swing.JScrollPane chatScrollPane;
    private javax.swing.JPanel chatSurfacePanel;
    private javax.swing.JPanel chipsPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel hintLabel1;
    private javax.swing.JLabel hintLabel2;
    private javax.swing.JLabel hintLabel3;
    private javax.swing.JPanel hintsPanel;
    private javax.swing.JPanel inputShellPanel;
    private javax.swing.JLabel pageSubtitleLabel;
    private javax.swing.JPanel previewBubblePanel;
    private javax.swing.JLabel previewMessageLabel1;
    private javax.swing.JLabel previewMessageLabel2;
    private javax.swing.JTextArea promptArea;
    private javax.swing.JScrollPane promptScrollPane;
    private javax.swing.JButton runButton;
    private javax.swing.JPanel shellHeaderPanel;
    private javax.swing.JPanel shellPanel;
    private javax.swing.JPanel titleTextPanel;
    // End of variables declaration//GEN-END:variables
}
