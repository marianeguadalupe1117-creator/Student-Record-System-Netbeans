/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.studentrecordsystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminDashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminDashboard.class.getName());

    private static final String DB_NAME = "student_record_system";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static final String[] TABLES = {
            "admins",
            "students",
            "courses",
            "curriculum",
            "departments",
            "enrollments",
            "grades",
            "instructors",
            "rooms",
            "schedules",
            "school_years",
            "sections",
            "semesters",
            "subjects"
    };

    private DefaultTableModel tableModel;
    private JButton archiveButton;
    private String currentTable = "students";
    private final Map<String, JButton> menuButtons = new HashMap<>();

    public AdminDashboard() {
        initComponents();
        setupFormDashboard();
        loadDashboardData();
        loadTableData("students");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        appRootPanel = new javax.swing.JPanel();
        sidebarPanel = new javax.swing.JPanel();
        sidebarScrollPane = new javax.swing.JScrollPane();
        sidebarMenuPanel = new javax.swing.JPanel();
        avatarLabel = new javax.swing.JLabel();
        adminUserLabel = new javax.swing.JLabel();
        roleLabel = new javax.swing.JLabel();
        dashboardButton = new javax.swing.JButton();
        databaseTablesLabel = new javax.swing.JLabel();
        adminsButton = new javax.swing.JButton();
        studentsButton = new javax.swing.JButton();
        coursesButton = new javax.swing.JButton();
        curriculumButton = new javax.swing.JButton();
        departmentsButton = new javax.swing.JButton();
        enrollmentsButton = new javax.swing.JButton();
        gradesButton = new javax.swing.JButton();
        instructorsButton = new javax.swing.JButton();
        roomsButton = new javax.swing.JButton();
        schedulesButton = new javax.swing.JButton();
        schoolYearsButton = new javax.swing.JButton();
        sectionsButton = new javax.swing.JButton();
        semestersButton = new javax.swing.JButton();
        subjectsButton = new javax.swing.JButton();
        bottomActionsPanel = new javax.swing.JPanel();
        crudButton = new javax.swing.JButton();
        adminAIButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        pageTitleLabel = new javax.swing.JLabel();
        pageSubtitleLabel = new javax.swing.JLabel();
        rightHeaderPanel = new javax.swing.JPanel();
        connectionStatusLabel = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        cardsPanel = new javax.swing.JPanel();
        studentCardPanel = new javax.swing.JPanel();
        studentCardTitleLabel = new javax.swing.JLabel();
        studentCardValueLabel = new javax.swing.JLabel();
        studentCardSubtitleLabel = new javax.swing.JLabel();
        coursesCardPanel = new javax.swing.JPanel();
        coursesCardTitleLabel = new javax.swing.JLabel();
        coursesCardValueLabel = new javax.swing.JLabel();
        coursesCardSubtitleLabel = new javax.swing.JLabel();
        subjectsCardPanel = new javax.swing.JPanel();
        subjectsCardTitleLabel = new javax.swing.JLabel();
        subjectsCardValueLabel = new javax.swing.JLabel();
        subjectsCardSubtitleLabel = new javax.swing.JLabel();
        schedulesCardPanel = new javax.swing.JPanel();
        schedulesCardTitleLabel = new javax.swing.JLabel();
        schedulesCardValueLabel = new javax.swing.JLabel();
        schedulesCardSubtitleLabel = new javax.swing.JLabel();
        recordsPanel = new javax.swing.JPanel();
        tableHeaderPanel = new javax.swing.JPanel();
        titleBoxPanel = new javax.swing.JPanel();
        tableTitleLabel = new javax.swing.JLabel();
        actionsPanel = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        tableScrollPane = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setMinimumSize(new java.awt.Dimension(1050, 650));

        appRootPanel.setBackground(new java.awt.Color(236, 240, 245));
        appRootPanel.setLayout(new java.awt.BorderLayout());

        sidebarPanel.setPreferredSize(new java.awt.Dimension(245, 650));
        sidebarPanel.setBackground(new java.awt.Color(18, 58, 91));
        sidebarPanel.setLayout(new java.awt.BorderLayout());

        sidebarScrollPane.setBackground(new java.awt.Color(18, 58, 91));
        sidebarScrollPane.setOpaque(false);

        sidebarMenuPanel.setBackground(new java.awt.Color(18, 58, 91));
        sidebarMenuPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 18, 15, 18));
        sidebarMenuPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        avatarLabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        avatarLabel.setForeground(new java.awt.Color(255, 255, 255));
        avatarLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avatarLabel.setText("●");
        sidebarMenuPanel.add(avatarLabel);

        adminUserLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        adminUserLabel.setForeground(new java.awt.Color(255, 255, 255));
        adminUserLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adminUserLabel.setText("ADMIN USER");
        sidebarMenuPanel.add(adminUserLabel);

        roleLabel.setText("System Administrator");
        roleLabel.setForeground(new java.awt.Color(190, 210, 226));
        roleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sidebarMenuPanel.add(roleLabel);

        dashboardButton.setBackground(new java.awt.Color(15, 98, 146));
        dashboardButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        dashboardButton.setForeground(new java.awt.Color(255, 255, 255));
        dashboardButton.setText("Dashboard");
        dashboardButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        dashboardButton.setBorderPainted(false);
        dashboardButton.setFocusPainted(false);
        dashboardButton.addActionListener(this::dashboardButtonActionPerformed);
        sidebarMenuPanel.add(dashboardButton);

        databaseTablesLabel.setText("DATABASE TABLES");
        databaseTablesLabel.setForeground(new java.awt.Color(145, 178, 204));
        databaseTablesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        databaseTablesLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        sidebarMenuPanel.add(databaseTablesLabel);

        adminsButton.setBackground(new java.awt.Color(18, 58, 91));
        adminsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        adminsButton.setForeground(new java.awt.Color(255, 255, 255));
        adminsButton.setText("Admins");
        adminsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        adminsButton.setBorderPainted(false);
        adminsButton.setFocusPainted(false);
        sidebarMenuPanel.add(adminsButton);

        studentsButton.setBackground(new java.awt.Color(18, 58, 91));
        studentsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        studentsButton.setForeground(new java.awt.Color(255, 255, 255));
        studentsButton.setText("Students");
        studentsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        studentsButton.setBorderPainted(false);
        studentsButton.setFocusPainted(false);
        sidebarMenuPanel.add(studentsButton);

        coursesButton.setBackground(new java.awt.Color(18, 58, 91));
        coursesButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        coursesButton.setForeground(new java.awt.Color(255, 255, 255));
        coursesButton.setText("Courses");
        coursesButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        coursesButton.setBorderPainted(false);
        coursesButton.setFocusPainted(false);
        sidebarMenuPanel.add(coursesButton);

        curriculumButton.setBackground(new java.awt.Color(18, 58, 91));
        curriculumButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        curriculumButton.setForeground(new java.awt.Color(255, 255, 255));
        curriculumButton.setText("Curriculum");
        curriculumButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        curriculumButton.setBorderPainted(false);
        curriculumButton.setFocusPainted(false);
        sidebarMenuPanel.add(curriculumButton);

        departmentsButton.setBackground(new java.awt.Color(18, 58, 91));
        departmentsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        departmentsButton.setForeground(new java.awt.Color(255, 255, 255));
        departmentsButton.setText("Departments");
        departmentsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        departmentsButton.setBorderPainted(false);
        departmentsButton.setFocusPainted(false);
        sidebarMenuPanel.add(departmentsButton);

        enrollmentsButton.setBackground(new java.awt.Color(18, 58, 91));
        enrollmentsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        enrollmentsButton.setForeground(new java.awt.Color(255, 255, 255));
        enrollmentsButton.setText("Enrollments");
        enrollmentsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        enrollmentsButton.setBorderPainted(false);
        enrollmentsButton.setFocusPainted(false);
        sidebarMenuPanel.add(enrollmentsButton);

        gradesButton.setBackground(new java.awt.Color(18, 58, 91));
        gradesButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gradesButton.setForeground(new java.awt.Color(255, 255, 255));
        gradesButton.setText("Grades");
        gradesButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        gradesButton.setBorderPainted(false);
        gradesButton.setFocusPainted(false);
        sidebarMenuPanel.add(gradesButton);

        instructorsButton.setBackground(new java.awt.Color(18, 58, 91));
        instructorsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        instructorsButton.setForeground(new java.awt.Color(255, 255, 255));
        instructorsButton.setText("Instructors");
        instructorsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        instructorsButton.setBorderPainted(false);
        instructorsButton.setFocusPainted(false);
        sidebarMenuPanel.add(instructorsButton);

        roomsButton.setBackground(new java.awt.Color(18, 58, 91));
        roomsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        roomsButton.setForeground(new java.awt.Color(255, 255, 255));
        roomsButton.setText("Rooms");
        roomsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        roomsButton.setBorderPainted(false);
        roomsButton.setFocusPainted(false);
        sidebarMenuPanel.add(roomsButton);

        schedulesButton.setBackground(new java.awt.Color(18, 58, 91));
        schedulesButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        schedulesButton.setForeground(new java.awt.Color(255, 255, 255));
        schedulesButton.setText("Schedules");
        schedulesButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        schedulesButton.setBorderPainted(false);
        schedulesButton.setFocusPainted(false);
        sidebarMenuPanel.add(schedulesButton);

        schoolYearsButton.setBackground(new java.awt.Color(18, 58, 91));
        schoolYearsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        schoolYearsButton.setForeground(new java.awt.Color(255, 255, 255));
        schoolYearsButton.setText("School Years");
        schoolYearsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        schoolYearsButton.setBorderPainted(false);
        schoolYearsButton.setFocusPainted(false);
        sidebarMenuPanel.add(schoolYearsButton);

        sectionsButton.setBackground(new java.awt.Color(18, 58, 91));
        sectionsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        sectionsButton.setForeground(new java.awt.Color(255, 255, 255));
        sectionsButton.setText("Sections");
        sectionsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        sectionsButton.setBorderPainted(false);
        sectionsButton.setFocusPainted(false);
        sidebarMenuPanel.add(sectionsButton);

        semestersButton.setBackground(new java.awt.Color(18, 58, 91));
        semestersButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        semestersButton.setForeground(new java.awt.Color(255, 255, 255));
        semestersButton.setText("Semesters");
        semestersButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        semestersButton.setBorderPainted(false);
        semestersButton.setFocusPainted(false);
        sidebarMenuPanel.add(semestersButton);

        subjectsButton.setBackground(new java.awt.Color(18, 58, 91));
        subjectsButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        subjectsButton.setForeground(new java.awt.Color(255, 255, 255));
        subjectsButton.setText("Subjects");
        subjectsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        subjectsButton.setBorderPainted(false);
        subjectsButton.setFocusPainted(false);
        sidebarMenuPanel.add(subjectsButton);

        sidebarScrollPane.setViewportView(sidebarMenuPanel);

        sidebarPanel.add(sidebarScrollPane, java.awt.BorderLayout.CENTER);

        bottomActionsPanel.setBackground(new java.awt.Color(18, 58, 91));
        bottomActionsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 18, 22, 18));
        bottomActionsPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 8));

        crudButton.setBackground(new java.awt.Color(245, 158, 11));
        crudButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        crudButton.setForeground(new java.awt.Color(255, 255, 255));
        crudButton.setText("Records");
        crudButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        crudButton.setBorderPainted(false);
        crudButton.setFocusPainted(false);
        bottomActionsPanel.add(crudButton);

        adminAIButton.setBackground(new java.awt.Color(15, 98, 146));
        adminAIButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        adminAIButton.setForeground(new java.awt.Color(255, 255, 255));
        adminAIButton.setText("AI Assistant");
        adminAIButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        adminAIButton.setBorderPainted(false);
        adminAIButton.setFocusPainted(false);
        bottomActionsPanel.add(adminAIButton);

        logoutButton.setBackground(new java.awt.Color(150, 50, 50));
        logoutButton.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("Log Out");
        logoutButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        bottomActionsPanel.add(logoutButton);

        sidebarPanel.add(bottomActionsPanel, java.awt.BorderLayout.SOUTH);

        appRootPanel.add(sidebarPanel, java.awt.BorderLayout.WEST);

        contentPanel.setBackground(new java.awt.Color(236, 240, 245));
        contentPanel.setLayout(new java.awt.BorderLayout(18, 18));

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.BorderLayout());

        titlePanel.setOpaque(false);
        titlePanel.setLayout(new java.awt.GridLayout(2, 1));

        pageTitleLabel.setText("Dashboard User");
        pageTitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        pageTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 23)); // NOI18N
        titlePanel.add(pageTitleLabel);

        pageSubtitleLabel.setText("Overview of your student record system data.");
        pageSubtitleLabel.setForeground(new java.awt.Color(99, 115, 129));
        titlePanel.add(pageSubtitleLabel);

        headerPanel.add(titlePanel, java.awt.BorderLayout.WEST);

        rightHeaderPanel.setOpaque(false);
        rightHeaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        connectionStatusLabel.setText("Checking database...");
        connectionStatusLabel.setForeground(new java.awt.Color(22, 163, 74));
        connectionStatusLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rightHeaderPanel.add(connectionStatusLabel);

        headerPanel.add(rightHeaderPanel, java.awt.BorderLayout.EAST);

        contentPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setBackground(new java.awt.Color(236, 240, 245));
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new java.awt.BorderLayout(0, 18));

        cardsPanel.setOpaque(false);
        cardsPanel.setPreferredSize(new java.awt.Dimension(0, 112));
        cardsPanel.setLayout(new java.awt.GridLayout(1, 4, 16, 0));

        studentCardPanel.setBackground(new java.awt.Color(18, 58, 91));
        studentCardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 228, 237)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        studentCardPanel.setLayout(new java.awt.GridLayout(3, 1));

        studentCardTitleLabel.setText("Students");
        studentCardTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        studentCardPanel.add(studentCardTitleLabel);

        studentCardValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        studentCardValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        studentCardValueLabel.setText("0");
        studentCardPanel.add(studentCardValueLabel);

        studentCardSubtitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        studentCardSubtitleLabel.setText("Active: 0 | Archived: 0");
        studentCardPanel.add(studentCardSubtitleLabel);

        cardsPanel.add(studentCardPanel);

        coursesCardPanel.setBackground(new java.awt.Color(255, 255, 255));
        coursesCardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 228, 237)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        coursesCardPanel.setLayout(new java.awt.GridLayout(3, 1));

        coursesCardTitleLabel.setText("Courses");
        coursesCardTitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        coursesCardPanel.add(coursesCardTitleLabel);

        coursesCardValueLabel.setText("0");
        coursesCardValueLabel.setForeground(new java.awt.Color(33, 43, 54));
        coursesCardValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        coursesCardPanel.add(coursesCardValueLabel);

        coursesCardSubtitleLabel.setText("Programs available");
        coursesCardSubtitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        coursesCardPanel.add(coursesCardSubtitleLabel);

        cardsPanel.add(coursesCardPanel);

        subjectsCardPanel.setBackground(new java.awt.Color(255, 255, 255));
        subjectsCardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 228, 237)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        subjectsCardPanel.setLayout(new java.awt.GridLayout(3, 1));

        subjectsCardTitleLabel.setText("Subjects");
        subjectsCardTitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        subjectsCardPanel.add(subjectsCardTitleLabel);

        subjectsCardValueLabel.setText("0");
        subjectsCardValueLabel.setForeground(new java.awt.Color(33, 43, 54));
        subjectsCardValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        subjectsCardPanel.add(subjectsCardValueLabel);

        subjectsCardSubtitleLabel.setText("Curriculum subjects");
        subjectsCardSubtitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        subjectsCardPanel.add(subjectsCardSubtitleLabel);

        cardsPanel.add(subjectsCardPanel);

        schedulesCardPanel.setBackground(new java.awt.Color(255, 255, 255));
        schedulesCardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 228, 237)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        schedulesCardPanel.setLayout(new java.awt.GridLayout(3, 1));

        schedulesCardTitleLabel.setText("Schedules");
        schedulesCardTitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        schedulesCardPanel.add(schedulesCardTitleLabel);

        schedulesCardValueLabel.setText("0");
        schedulesCardValueLabel.setForeground(new java.awt.Color(33, 43, 54));
        schedulesCardValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        schedulesCardPanel.add(schedulesCardValueLabel);

        schedulesCardSubtitleLabel.setText("Generated class schedules");
        schedulesCardSubtitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        schedulesCardPanel.add(schedulesCardSubtitleLabel);

        cardsPanel.add(schedulesCardPanel);

        centerPanel.add(cardsPanel, java.awt.BorderLayout.NORTH);

        recordsPanel.setBackground(new java.awt.Color(255, 255, 255));
        recordsPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 228, 237)), javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        recordsPanel.setLayout(new java.awt.BorderLayout(12, 12));

        tableHeaderPanel.setOpaque(false);
        tableHeaderPanel.setLayout(new java.awt.BorderLayout(12, 12));

        titleBoxPanel.setOpaque(false);
        titleBoxPanel.setLayout(new java.awt.GridLayout(2, 1));

        tableTitleLabel.setText("Students Records");
        tableTitleLabel.setForeground(new java.awt.Color(33, 43, 54));
        tableTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleBoxPanel.add(tableTitleLabel);

        tableHeaderPanel.add(titleBoxPanel, java.awt.BorderLayout.WEST);

        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));

        searchField.setColumns(24);
        searchField.setForeground(new java.awt.Color(33, 43, 54));
        searchField.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        actionsPanel.add(searchField);

        searchButton.setText("Search");
        searchButton.setBackground(new java.awt.Color(14, 89, 130));
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        actionsPanel.add(searchButton);

        refreshButton.setText("Refresh");
        refreshButton.setBackground(new java.awt.Color(100, 116, 139));
        refreshButton.setForeground(new java.awt.Color(255, 255, 255));
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        refreshButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        actionsPanel.add(refreshButton);

        tableHeaderPanel.add(actionsPanel, java.awt.BorderLayout.EAST);

        recordsPanel.add(tableHeaderPanel, java.awt.BorderLayout.NORTH);

        tableScrollPane.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 232, 240)), javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student No.", "Full Name", "Gender", "Birth Date", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setForeground(new java.awt.Color(33, 43, 54));
        dataTable.setGridColor(new java.awt.Color(226, 232, 240));
        dataTable.setRowHeight(32);
        tableScrollPane.setViewportView(dataTable);

        recordsPanel.add(tableScrollPane, java.awt.BorderLayout.CENTER);

        centerPanel.add(recordsPanel, java.awt.BorderLayout.CENTER);

        contentPanel.add(centerPanel, java.awt.BorderLayout.CENTER);

        appRootPanel.add(contentPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(appRootPanel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardButtonActionPerformed

    private void setupFormDashboard() {
        setLocationRelativeTo(null);
        applyDashboardStyle();
        setupTableModel();
        setupButtonActions();
        setActiveMenu("Dashboard");
    }

    private void applyDashboardStyle() {
        Color pageBg = new Color(236, 240, 245);
        Color sidebarBg = new Color(18, 58, 91);
        Color activeBlue = new Color(15, 98, 146);
        Color orange = new Color(245, 158, 11);
        Color red = new Color(150, 50, 50);
        Color text = new Color(33, 43, 54);
        Color muted = new Color(99, 115, 129);

        appRootPanel.setBackground(pageBg);
        contentPanel.setBackground(pageBg);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setLayout(new BorderLayout(18, 18));
        centerPanel.setBackground(pageBg);
        centerPanel.setLayout(new BorderLayout(0, 18));
        headerPanel.setOpaque(false);
        titlePanel.setOpaque(false);
        rightHeaderPanel.setOpaque(false);
        rightHeaderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        cardsPanel.setOpaque(false);

        sidebarPanel.setBackground(sidebarBg);
        sidebarMenuPanel.setOpaque(true);
        sidebarMenuPanel.setBackground(sidebarBg);
        sidebarMenuPanel.setBorder(new EmptyBorder(24, 18, 15, 18));
        sidebarScrollPane.setBorder(null);
        sidebarScrollPane.setOpaque(false);
        sidebarScrollPane.setBackground(sidebarBg);
        sidebarScrollPane.getViewport().setBackground(sidebarBg);
        sidebarScrollPane.getViewport().setOpaque(true);
        sidebarScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        bottomActionsPanel.setBackground(sidebarBg);
        bottomActionsPanel.setBorder(new EmptyBorder(10, 18, 22, 18));

        avatarLabel.setIcon(new AvatarIcon(62, new Color(238, 242, 247), sidebarBg));
        avatarLabel.setText("");
        adminUserLabel.setForeground(Color.WHITE);
        roleLabel.setForeground(new Color(190, 210, 226));
        databaseTablesLabel.setForeground(new Color(145, 178, 204));

        JButton[] sideButtons = {
                dashboardButton, adminsButton, studentsButton, coursesButton, curriculumButton,
                departmentsButton, enrollmentsButton, gradesButton, instructorsButton, roomsButton,
                schedulesButton, schoolYearsButton, sectionsButton, semestersButton, subjectsButton
        };
        for (JButton button : sideButtons) {
            styleSideButton(button, sidebarBg);
        }
        styleSideButton(crudButton, orange);
        styleSideButton(adminAIButton, activeBlue);
        styleSideButton(logoutButton, red);

        pageTitleLabel.setForeground(text);
        pageSubtitleLabel.setForeground(muted);
        connectionStatusLabel.setForeground(muted);

        styleCard(studentCardPanel, true);
        styleCard(coursesCardPanel, false);
        styleCard(subjectsCardPanel, false);
        styleCard(schedulesCardPanel, false);

        studentCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 237)),
                new EmptyBorder(18, 18, 18, 18)
        ));
        coursesCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 237)),
                new EmptyBorder(18, 18, 18, 18)
        ));
        subjectsCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 237)),
                new EmptyBorder(18, 18, 18, 18)
        ));
        schedulesCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 237)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        recordsPanel.setBackground(Color.WHITE);
        recordsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 237)),
                new EmptyBorder(16, 16, 16, 16)
        ));
        tableHeaderPanel.setOpaque(false);
        titleBoxPanel.setOpaque(false);
        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tableTitleLabel.setForeground(text);

        searchField.setColumns(24);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        styleActionButton(searchButton, new Color(14, 89, 130));
        styleActionButton(refreshButton, new Color(100, 116, 139));
    }

    private void styleCard(JPanel card, boolean dark) {
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.setBackground(dark ? new Color(18, 58, 91) : Color.WHITE);
        for (Component c : card.getComponents()) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setForeground(dark ? Color.WHITE : new Color(33, 43, 54));
            }
        }
    }

    private void styleSideButton(JButton button, Color bg) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 12, 10, 12));
    }

    private void styleActionButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 12, 8, 12));
    }

    private void setupTableModel() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dataTable.setModel(tableModel);
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dataTable.setRowHeight(32);
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        dataTable.getTableHeader().setBackground(new Color(248, 250, 252));
        dataTable.getTableHeader().setForeground(new Color(33, 43, 54));
        dataTable.setGridColor(new Color(226, 232, 240));
        dataTable.setShowVerticalLines(true);
        dataTable.setShowHorizontalLines(true);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void setupButtonActions() {
        dashboardButton.addActionListener(e -> {
            currentTable = "students";
            setActiveMenu("Dashboard");
            pageTitleLabel.setText("Dashboard User");
            pageSubtitleLabel.setText("Overview of your student record system data.");
            loadDashboardData();
            loadTableData("students");
        });

        configureTableButton(adminsButton, "admins");
        configureTableButton(studentsButton, "students");
        configureTableButton(coursesButton, "courses");
        configureTableButton(curriculumButton, "curriculum");
        configureTableButton(departmentsButton, "departments");
        configureTableButton(enrollmentsButton, "enrollments");
        configureTableButton(gradesButton, "grades");
        configureTableButton(instructorsButton, "instructors");
        configureTableButton(roomsButton, "rooms");
        configureTableButton(schedulesButton, "schedules");
        configureTableButton(schoolYearsButton, "school_years");
        configureTableButton(sectionsButton, "sections");
        configureTableButton(semestersButton, "semesters");
        configureTableButton(subjectsButton, "subjects");

        searchField.addActionListener(e -> loadTableData(currentTable));
        searchButton.addActionListener(e -> loadTableData(currentTable));
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadDashboardData();
            loadTableData(currentTable);
        });
        crudButton.addActionListener(e -> openCrudDialog());
        adminAIButton.addActionListener(e -> openAdminAIFrame());
        logoutButton.addActionListener(e -> logout());
    }

    private void configureTableButton(JButton button, String table) {
        String key = displayName(table);
        menuButtons.put(key, button);
        button.addActionListener(e -> {
            currentTable = table;
            setActiveMenu(key);
            pageTitleLabel.setText(key);
            pageSubtitleLabel.setText("View and manage records from the " + table + " table.");
            loadTableData(table);
        });
    }

    private void setActiveMenu(String key) {
        Color normal = new Color(18, 58, 91);
        Color active = new Color(15, 98, 146);

        dashboardButton.setBackground(normal);
        for (JButton btn : menuButtons.values()) {
            btn.setBackground(normal);
        }

        if ("Dashboard".equals(key)) {
            dashboardButton.setBackground(active);
            return;
        }

        JButton activeButton = menuButtons.get(key);
        if (activeButton != null) {
            activeButton.setBackground(active);
        }
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        styleActionButton(button, color);
        return button;
    }

    private void loadDashboardData() {
        SwingUtilities.invokeLater(() -> {
            boolean connected = canConnect();
            connectionStatusLabel.setText(connected ? "Database Connected" : "Database Not Connected");
            connectionStatusLabel.setForeground(connected ? new Color(22, 163, 74) : new Color(220, 38, 38));

            int totalStudents = countRows("students");
            int activeStudents = countWhere("students", "status = 'Active'");
            int courses = countRows("courses");
            int subjects = countRows("subjects");
            int schedules = countRows("schedules");
            int archivedStudents = countWhere("students", "status = 'Archived'");

            studentCardValueLabel.setText(String.valueOf(totalStudents));
            studentCardSubtitleLabel.setText("Active: " + activeStudents + " | Archived: " + archivedStudents);
            coursesCardValueLabel.setText(String.valueOf(courses));
            subjectsCardValueLabel.setText(String.valueOf(subjects));
            schedulesCardValueLabel.setText(String.valueOf(schedules));
        });
    }

    private void loadTableData(String table) {
        if (!isAllowedTable(table)) {
            JOptionPane.showMessageDialog(this, "Invalid table selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentTable = table;
        tableTitleLabel.setText(displayName(table) + " Records");
        if (archiveButton != null) {
            archiveButton.setEnabled(tableHasArchiveSupport(table));
        }

        String keyword = searchField == null ? "" : searchField.getText().trim().toLowerCase();
        TableViewQuery viewQuery = getTableViewQuery(table);

        try (Connection conn = getConnection()) {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            StringBuilder sql = new StringBuilder(viewQuery.sql);
            if (!keyword.isEmpty() && !viewQuery.searchExpressions.isEmpty()) {
                sql.append(" WHERE ");
                for (int i = 0; i < viewQuery.searchExpressions.size(); i++) {
                    if (i > 0) sql.append(" OR ");
                    sql.append("LOWER(CAST(").append(viewQuery.searchExpressions.get(i)).append(" AS CHAR)) LIKE ?");
                }
            }
            sql.append(" ORDER BY 1 DESC LIMIT 500");

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                if (!keyword.isEmpty()) {
                    for (int i = 1; i <= viewQuery.searchExpressions.size(); i++) {
                        ps.setString(i, "%" + keyword + "%");
                    }
                }

                try (ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        tableModel.addColumn(meta.getColumnLabel(i));
                    }
                    while (rs.next()) {
                        Object[] row = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            row[i - 1] = rs.getObject(i);
                        }
                        tableModel.addRow(row);
                    }
                }
            }

            hideIdColumns(dataTable);
            applyReadableColumnWidths();
            connectionStatusLabel.setText("Database Connected");
            connectionStatusLabel.setForeground(new Color(22, 163, 74));
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void addRecord(String table) {
        if (!isAllowedTable(table)) return;

        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            Map<String, JComponent> inputs = new LinkedHashMap<>();
            JPanel formPanel = buildRecordForm(columns, null, inputs, false);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    new JScrollPane(formPanel),
                    "Add Record - " + displayName(table),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            List<ColumnInfo> insertColumns = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (ColumnInfo column : columns) {
                if (column.isAutoIncrement || column.isCreatedAt) continue;
                JComponent input = inputs.get(column.name);
                if (input == null) continue;

                Object value = getInputValue(input);
                boolean empty = value == null || value.toString().trim().isEmpty();

                if (empty && (column.nullable || column.defaultValue != null)) {
                    continue;
                }

                insertColumns.add(column);
                values.add(empty ? "" : value);
            }

            if (insertColumns.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter at least one value.");
                return;
            }

            StringBuilder sql = new StringBuilder("INSERT INTO `").append(table).append("` (");
            for (int i = 0; i < insertColumns.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("`").append(insertColumns.get(i).name).append("`");
            }
            sql.append(") VALUES (");
            for (int i = 0; i < insertColumns.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("?");
            }
            sql.append(")");

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < values.size(); i++) {
                    setPreparedValue(ps, i + 1, values.get(i), insertColumns.get(i));
                }
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            afterDataChanged(table);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void editSelectedRecord(String table) {
        if (!isAllowedTable(table)) return;

        int selected = dataTable.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
            return;
        }

        int modelRow = dataTable.convertRowIndexToModel(selected);

        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            ColumnInfo primaryKey = getPrimaryKey(columns);
            if (primaryKey == null) {
                JOptionPane.showMessageDialog(this, "This table has no primary key. Update is not available.");
                return;
            }

            Map<String, Object> rowValues = getSelectedRowValues(columns, modelRow);
            Object primaryValue = rowValues.get(primaryKey.name);

            Map<String, JComponent> inputs = new LinkedHashMap<>();
            JPanel formPanel = buildRecordForm(columns, rowValues, inputs, true);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    new JScrollPane(formPanel),
                    "Update Record - " + displayName(table),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            List<ColumnInfo> updateColumns = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (ColumnInfo column : columns) {
                if (column.isPrimaryKey || column.isAutoIncrement || column.isCreatedAt) continue;
                JComponent input = inputs.get(column.name);
                if (input == null) continue;

                updateColumns.add(column);
                values.add(getInputValue(input));
            }

            if (updateColumns.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No editable columns found.");
                return;
            }

            StringBuilder sql = new StringBuilder("UPDATE `").append(table).append("` SET ");
            for (int i = 0; i < updateColumns.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("`").append(updateColumns.get(i).name).append("` = ?");
            }
            sql.append(" WHERE `").append(primaryKey.name).append("` = ?");

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < values.size(); i++) {
                    setPreparedValue(ps, i + 1, values.get(i), updateColumns.get(i));
                }
                setPreparedValue(ps, values.size() + 1, primaryValue, primaryKey);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            afterDataChanged(table);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void archiveSelectedRecord(String table) {
        if (!isAllowedTable(table)) return;

        int selected = dataTable.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to archive.");
            return;
        }

        if (!tableHasArchiveSupport(table)) {
            JOptionPane.showMessageDialog(this, "This table has no status column, so archive is not available.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Archive selected record?",
                "Confirm Archive",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        int modelRow = dataTable.convertRowIndexToModel(selected);

        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            ColumnInfo primaryKey = getPrimaryKey(columns);
            if (primaryKey == null) {
                JOptionPane.showMessageDialog(this, "This table has no primary key. Archive is not available.");
                return;
            }

            Map<String, Object> rowValues = getSelectedRowValues(columns, modelRow);
            Object primaryValue = rowValues.get(primaryKey.name);
            String archiveValue = "school_years".equals(table) ? "Inactive" : "Archived";

            try (PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET `status` = ? WHERE `" + primaryKey.name + "` = ?")) {
                ps.setString(1, archiveValue);
                setPreparedValue(ps, 2, primaryValue, primaryKey);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record archived successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            afterDataChanged(table);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void deleteSelectedRecord(String table) {
        if (!isAllowedTable(table)) return;

        int selected = dataTable.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete/archive.");
            return;
        }

        if (!tableHasArchiveSupport(table)) {
            JOptionPane.showMessageDialog(this,
                    "This table has no status column, so delete-as-archive is not available.",
                    "Archive Not Available",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Archive selected record instead of permanently deleting it?",
                "Confirm Archive",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        int modelRow = dataTable.convertRowIndexToModel(selected);

        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            ColumnInfo primaryKey = getPrimaryKey(columns);
            if (primaryKey == null) {
                JOptionPane.showMessageDialog(this, "This table has no primary key. Archive is not available.");
                return;
            }

            Map<String, Object> rowValues = getSelectedRowValues(columns, modelRow);
            Object primaryValue = rowValues.get(primaryKey.name);
            String archiveValue = "Archived";

            try (PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET `status` = ? WHERE `" + primaryKey.name + "` = ?")) {
                ps.setString(1, archiveValue);
                setPreparedValue(ps, 2, primaryValue, primaryKey);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record archived successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            afterDataChanged(table);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private JPanel buildRecordForm(List<ColumnInfo> columns, Map<String, Object> existingValues, Map<String, JComponent> inputs, boolean editMode) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        int row = 0;
        for (ColumnInfo column : columns) {
            if (column.isAutoIncrement || column.isCreatedAt) continue;
            if (editMode && column.isPrimaryKey) continue;

            JLabel label = new JLabel(toLabel(column.name));
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(new Color(33, 43, 54));

            JComponent input;
            Object existing = existingValues == null ? null : existingValues.get(column.name);

            if (column.isEnum && !column.enumValues.isEmpty()) {
                JComboBox<String> combo = new JComboBox<>();
                if (column.nullable) combo.addItem("");
                for (String value : column.enumValues) combo.addItem(value);
                if (existing != null) combo.setSelectedItem(existing.toString());
                input = combo;
            } else {
                JTextField textField = new JTextField(24);
                textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240)),
                        new EmptyBorder(7, 9, 7, 9)
                ));
                if (existing != null) textField.setText(existing.toString());
                input = textField;
            }

            inputs.put(column.name, input);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            form.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 1;
            form.add(input, gbc);
            row++;
        }

        if (row == 0) {
            JLabel noFields = new JLabel("No editable fields available.");
            noFields.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            form.add(noFields);
        }

        return form;
    }

private void openCrudDialog() {
        JDialog dialog = new JDialog(this, "RECORDS ACCESS Editor", true);
        dialog.setSize(980, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(12, 12));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBorder(new EmptyBorder(15, 15, 0, 15));
        top.setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("RECORDS ACCESS Editor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(33, 43, 54));

        JComboBox<String> tableCombo = new JComboBox<>();
        for (String table : TABLES) tableCombo.addItem(displayName(table));
        tableCombo.setSelectedItem(displayName(currentTable));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(new JLabel("Table:"));
        right.add(tableCombo);

        top.add(title, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        DefaultTableModel dialogModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable dialogTable = new JTable(dialogModel);
        dialogTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dialogTable.setRowHeight(28);
        dialogTable.setAutoCreateRowSorter(true);
        dialogTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane dialogScroll = new JScrollPane(dialogTable);
        dialogScroll.setBorder(new EmptyBorder(10, 15, 10, 15));
        dialogScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        bottom.setBackground(new Color(236, 240, 245));

        JButton add = createActionButton("Add", new Color(37, 99, 235));
        JButton edit = createActionButton("Update", new Color(22, 163, 74));
        JButton archive = createActionButton("Archive", new Color(245, 158, 11));
        JButton delete = createActionButton("Delete (Archive)", new Color(220, 38, 38));
        JButton close = createActionButton("Close", new Color(100, 116, 139));

        bottom.add(add);
        bottom.add(edit);
        bottom.add(archive);
        bottom.add(delete);
        bottom.add(close);

        Runnable reloadDialogTable = () -> {
            String selectedTable = tableFromDisplayName(String.valueOf(tableCombo.getSelectedItem()));
            loadDialogTable(dialogModel, selectedTable);
            applyDialogColumnWidths(dialogTable);
            archive.setEnabled(tableHasArchiveSupport(selectedTable));
        };

        tableCombo.addActionListener(e -> reloadDialogTable.run());
        add.addActionListener(e -> {
            String selectedTable = tableFromDisplayName(String.valueOf(tableCombo.getSelectedItem()));
            dialog.dispose();
            currentTable = selectedTable;
            setActiveMenu(displayName(selectedTable));
            pageTitleLabel.setText(displayName(selectedTable));
            pageSubtitleLabel.setText("View and manage records from the " + selectedTable + " table.");
            loadTableData(selectedTable);
            addRecord(selectedTable);
        });
        edit.addActionListener(e -> {
            String selectedTable = tableFromDisplayName(String.valueOf(tableCombo.getSelectedItem()));
            Object primaryValue = getSelectedPrimaryValue(dialogTable, dialogModel);
            if (primaryValue == null) return;
            dialog.dispose();
            currentTable = selectedTable;
            setActiveMenu(displayName(selectedTable));
            pageTitleLabel.setText(displayName(selectedTable));
            pageSubtitleLabel.setText("View and manage records from the " + selectedTable + " table.");
            editRecordByPrimaryValue(selectedTable, primaryValue);
        });
        archive.addActionListener(e -> {
            String selectedTable = tableFromDisplayName(String.valueOf(tableCombo.getSelectedItem()));
            Object primaryValue = getSelectedPrimaryValue(dialogTable, dialogModel);
            if (primaryValue == null) return;
            dialog.dispose();
            currentTable = selectedTable;
            setActiveMenu(displayName(selectedTable));
            pageTitleLabel.setText(displayName(selectedTable));
            pageSubtitleLabel.setText("View and manage records from the " + selectedTable + " table.");
            archiveRecordByPrimaryValue(selectedTable, primaryValue);
        });
        delete.addActionListener(e -> {
            String selectedTable = tableFromDisplayName(String.valueOf(tableCombo.getSelectedItem()));
            Object primaryValue = getSelectedPrimaryValue(dialogTable, dialogModel);
            if (primaryValue == null) return;
            dialog.dispose();
            currentTable = selectedTable;
            setActiveMenu(displayName(selectedTable));
            pageTitleLabel.setText(displayName(selectedTable));
            pageSubtitleLabel.setText("View and manage records from the " + selectedTable + " table.");
            deleteRecordByPrimaryValue(selectedTable, primaryValue);
        });
        close.addActionListener(e -> dialog.dispose());

        dialog.add(top, BorderLayout.NORTH);
        dialog.add(dialogScroll, BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        reloadDialogTable.run();
        dialog.setVisible(true);
    }




    private TableViewQuery getTableViewQuery(String table) {
        switch (table) {
            case "students":
                return new TableViewQuery(
                        "SELECT s.student_id AS `Student ID`, " +
                                "s.student_number AS `Student No.`, " +
                                "CONCAT_WS(' ', s.first_name, NULLIF(s.middle_name, ''), s.last_name) AS `Full Name`, " +
                                "s.gender AS `Gender`, s.birth_date AS `Birth Date`, s.address AS `Address`, " +
                                "s.contact_number AS `Contact No.`, s.email AS `Email`, " +
                                "CONCAT(c.course_code, ' - ', c.course_name) AS `Course`, " +
                                "sec.section_name AS `Section`, s.year_level AS `Year Level`, " +
                                "s.student_status AS `Student Status`, s.status AS `Account Status`, s.created_at AS `Created At` " +
                                "FROM students s " +
                                "LEFT JOIN courses c ON s.course_id = c.course_id " +
                                "LEFT JOIN sections sec ON s.section_id = sec.section_id",
                        "s.student_id", "s.student_number", "s.first_name", "s.middle_name", "s.last_name", "s.email", "c.course_code", "c.course_name", "sec.section_name", "s.student_status", "s.status"
                );
            case "courses":
                return new TableViewQuery(
                        "SELECT c.course_id AS `Course ID`, c.course_code AS `Course Code`, c.course_name AS `Course Name`, " +
                                "d.department_name AS `Department`, c.status AS `Status`, c.created_at AS `Created At` " +
                                "FROM courses c LEFT JOIN departments d ON c.department_id = d.department_id",
                        "c.course_id", "c.course_code", "c.course_name", "d.department_name", "c.status"
                );
            case "curriculum":
                return new TableViewQuery(
                        "SELECT cur.curriculum_id AS `Curriculum ID`, " +
                                "CONCAT(c.course_code, ' - ', c.course_name) AS `Course`, " +
                                "CONCAT(sub.subject_code, ' - ', sub.subject_name) AS `Subject`, " +
                                "cur.year_level AS `Year Level`, sem.semester_name AS `Semester`, cur.created_at AS `Created At` " +
                                "FROM curriculum cur " +
                                "LEFT JOIN courses c ON cur.course_id = c.course_id " +
                                "LEFT JOIN subjects sub ON cur.subject_id = sub.subject_id " +
                                "LEFT JOIN semesters sem ON cur.semester_id = sem.semester_id",
                        "cur.curriculum_id", "c.course_code", "c.course_name", "sub.subject_code", "sub.subject_name", "cur.year_level", "sem.semester_name"
                );
            case "departments":
                return new TableViewQuery(
                        "SELECT department_id AS `Department ID`, department_name AS `Department Name`, created_at AS `Created At` FROM departments",
                        "department_id", "department_name"
                );
            case "enrollments":
                return new TableViewQuery(
                        "SELECT e.enrollment_id AS `Enrollment ID`, s.student_number AS `Student No.`, " +
                                "CONCAT_WS(' ', s.first_name, NULLIF(s.middle_name, ''), s.last_name) AS `Student Name`, " +
                                "CONCAT(sub.subject_code, ' - ', sub.subject_name) AS `Subject`, sec.section_name AS `Section`, " +
                                "sy.school_year AS `School Year`, sem.semester_name AS `Semester`, " +
                                "e.enrollment_status AS `Enrollment Status`, e.created_at AS `Created At` " +
                                "FROM enrollments e " +
                                "LEFT JOIN students s ON e.student_id = s.student_id " +
                                "LEFT JOIN subjects sub ON e.subject_id = sub.subject_id " +
                                "LEFT JOIN sections sec ON e.section_id = sec.section_id " +
                                "LEFT JOIN school_years sy ON e.school_year_id = sy.school_year_id " +
                                "LEFT JOIN semesters sem ON e.semester_id = sem.semester_id",
                        "e.enrollment_id", "s.student_number", "s.first_name", "s.middle_name", "s.last_name", "sub.subject_code", "sub.subject_name", "sec.section_name", "sy.school_year", "sem.semester_name", "e.enrollment_status"
                );
            case "grades":
                return new TableViewQuery(
                        "SELECT g.grade_id AS `Grade ID`, s.student_number AS `Student No.`, " +
                                "CONCAT_WS(' ', s.first_name, NULLIF(s.middle_name, ''), s.last_name) AS `Student Name`, " +
                                "CONCAT(sub.subject_code, ' - ', sub.subject_name) AS `Subject`, " +
                                "g.prelim_grade AS `Prelim`, g.midterm_grade AS `Midterm`, g.final_grade AS `Final`, " +
                                "g.remarks AS `Remarks`, g.created_at AS `Created At` " +
                                "FROM grades g " +
                                "LEFT JOIN enrollments e ON g.enrollment_id = e.enrollment_id " +
                                "LEFT JOIN students s ON e.student_id = s.student_id " +
                                "LEFT JOIN subjects sub ON e.subject_id = sub.subject_id",
                        "g.grade_id", "s.student_number", "s.first_name", "s.middle_name", "s.last_name", "sub.subject_code", "sub.subject_name", "g.remarks"
                );
            case "instructors":
                return new TableViewQuery(
                        "SELECT i.instructor_id AS `Instructor ID`, " +
                                "CONCAT_WS(' ', i.first_name, NULLIF(i.middle_name, ''), i.last_name) AS `Full Name`, " +
                                "d.department_name AS `Department`, i.email AS `Email`, i.contact_number AS `Contact No.`, " +
                                "i.employment_status AS `Employment Status`, i.status AS `Status`, i.created_at AS `Created At` " +
                                "FROM instructors i LEFT JOIN departments d ON i.department_id = d.department_id",
                        "i.instructor_id", "i.first_name", "i.middle_name", "i.last_name", "d.department_name", "i.email", "i.contact_number", "i.employment_status", "i.status"
                );
            case "rooms":
                return new TableViewQuery(
                        "SELECT room_id AS `Room ID`, room_name AS `Room Name`, room_type AS `Room Type`, capacity AS `Capacity`, status AS `Status`, created_at AS `Created At` FROM rooms",
                        "room_id", "room_name", "room_type", "capacity", "status"
                );
            case "schedules":
                return new TableViewQuery(
                        "SELECT sch.schedule_id AS `Schedule ID`, " +
                                "CONCAT(sub.subject_code, ' - ', sub.subject_name) AS `Subject`, " +
                                "CONCAT_WS(' ', i.first_name, NULLIF(i.middle_name, ''), i.last_name) AS `Instructor`, " +
                                "sec.section_name AS `Section`, r.room_name AS `Room`, sy.school_year AS `School Year`, " +
                                "sem.semester_name AS `Semester`, sch.day_of_week AS `Day`, " +
                                "TIME_FORMAT(sch.start_time, '%h:%i %p') AS `Start Time`, " +
                                "TIME_FORMAT(sch.end_time, '%h:%i %p') AS `End Time`, sch.created_at AS `Created At` " +
                                "FROM schedules sch " +
                                "LEFT JOIN subjects sub ON sch.subject_id = sub.subject_id " +
                                "LEFT JOIN instructors i ON sch.instructor_id = i.instructor_id " +
                                "LEFT JOIN sections sec ON sch.section_id = sec.section_id " +
                                "LEFT JOIN rooms r ON sch.room_id = r.room_id " +
                                "LEFT JOIN school_years sy ON sch.school_year_id = sy.school_year_id " +
                                "LEFT JOIN semesters sem ON sch.semester_id = sem.semester_id",
                        "sch.schedule_id", "sub.subject_code", "sub.subject_name", "i.first_name", "i.middle_name", "i.last_name", "sec.section_name", "r.room_name", "sy.school_year", "sem.semester_name", "sch.day_of_week"
                );
            case "school_years":
                return new TableViewQuery(
                        "SELECT school_year_id AS `School Year ID`, school_year AS `School Year`, status AS `Status`, created_at AS `Created At` FROM school_years",
                        "school_year_id", "school_year", "status"
                );
            case "sections":
                return new TableViewQuery(
                        "SELECT sec.section_id AS `Section ID`, sec.section_name AS `Section`, " +
                                "CONCAT(c.course_code, ' - ', c.course_name) AS `Course`, sec.year_level AS `Year Level`, " +
                                "sec.status AS `Status`, sec.created_at AS `Created At` " +
                                "FROM sections sec LEFT JOIN courses c ON sec.course_id = c.course_id",
                        "sec.section_id", "sec.section_name", "c.course_code", "c.course_name", "sec.year_level", "sec.status"
                );
            case "semesters":
                return new TableViewQuery(
                        "SELECT semester_id AS `Semester ID`, semester_name AS `Semester Name`, created_at AS `Created At` FROM semesters",
                        "semester_id", "semester_name"
                );
            case "subjects":
                return new TableViewQuery(
                        "SELECT subject_id AS `Subject ID`, subject_code AS `Subject Code`, subject_name AS `Subject Name`, " +
                                "units AS `Units`, lecture_hours AS `Lecture Hours`, laboratory_hours AS `Lab Hours`, " +
                                "subject_type AS `Subject Type`, description AS `Description`, status AS `Status`, created_at AS `Created At` FROM subjects",
                        "subject_id", "subject_code", "subject_name", "units", "subject_type", "description", "status"
                );
            case "admins":
            default:
                return new TableViewQuery(
                        "SELECT admin_id AS `Admin ID`, username AS `Username`, full_name AS `Full Name`, status AS `Status` FROM admins",
                        "admin_id", "username", "full_name", "status"
                );
        }
    }


    private void hideIdColumns(JTable table) {
        if (table == null || table.getColumnModel() == null) return;

        javax.swing.table.TableColumnModel columnModel = table.getColumnModel();
        for (int viewIndex = columnModel.getColumnCount() - 1; viewIndex >= 0; viewIndex--) {
            javax.swing.table.TableColumn column = columnModel.getColumn(viewIndex);
            String columnName = String.valueOf(column.getHeaderValue());

            if (isIdColumnName(columnName)) {
                columnModel.removeColumn(column);
            }
        }
    }

    private boolean isIdColumnName(String columnName) {
        if (columnName == null) return false;

        String name = columnName.trim().toLowerCase();
        return name.equals("id")
                || name.endsWith(" id")
                || name.endsWith("_id")
                || name.contains(" id ")
                || name.contains("_id_");
    }

    private void applyReadableColumnWidths() {
        if (dataTable == null || dataTable.getColumnModel() == null) return;
        for (int i = 0; i < dataTable.getColumnCount(); i++) {
            String name = dataTable.getColumnName(i);
            int width = columnWidthFor(name);
            javax.swing.table.TableColumn column = dataTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(width);
            column.setMinWidth(Math.min(width, 60));
        }
    }

    private void applyDialogColumnWidths(JTable table) {
        if (table == null || table.getColumnModel() == null) return;
        for (int i = 0; i < table.getColumnCount(); i++) {
            String name = table.getColumnName(i);
            int width = columnWidthFor(toLabel(name));
            javax.swing.table.TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width);
            column.setMinWidth(Math.min(width, 60));
        }
    }

    private int columnWidthFor(String columnName) {
        String name = columnName == null ? "" : columnName.toLowerCase();
        if (name.contains("id")) return 90;
        if (name.contains("no.")) return 120;
        if (name.contains("number")) return 125;
        if (name.contains("full name") || name.contains("student name") || name.contains("instructor")) return 190;
        if (name.contains("course") || name.contains("subject") || name.contains("department")) return 250;
        if (name.contains("section")) return 120;
        if (name.contains("address") || name.contains("description")) return 240;
        if (name.contains("email")) return 190;
        if (name.contains("contact")) return 135;
        if (name.contains("created")) return 165;
        if (name.contains("status")) return 130;
        if (name.contains("time")) return 115;
        if (name.contains("semester") || name.contains("school year")) return 145;
        if (name.contains("remarks")) return 120;
        return 115;
    }

    private Object getSelectedPrimaryValue(JTable table, DefaultTableModel model) {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row first.");
            return null;
        }
        int modelRow = table.convertRowIndexToModel(selected);
        if (model.getColumnCount() == 0) {
            JOptionPane.showMessageDialog(this, "No record selected.");
            return null;
        }
        return model.getValueAt(modelRow, 0);
    }

    private Map<String, Object> fetchRecordByPrimaryValue(Connection conn, String table, List<ColumnInfo> columns, ColumnInfo primaryKey, Object primaryValue) throws SQLException {
        Map<String, Object> values = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + table + "` WHERE `" + primaryKey.name + "` = ? LIMIT 1")) {
            setPreparedValue(ps, 1, primaryValue, primaryKey);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return values;
                for (ColumnInfo column : columns) {
                    values.put(column.name, rs.getObject(column.name));
                }
            }
        }
        return values;
    }

    private void editRecordByPrimaryValue(String table, Object primaryValue) {
        if (!isAllowedTable(table)) return;

        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            ColumnInfo primaryKey = getPrimaryKey(columns);
            if (primaryKey == null) {
                JOptionPane.showMessageDialog(this, "This table has no primary key. Update is not available.");
                return;
            }

            Map<String, Object> rowValues = fetchRecordByPrimaryValue(conn, table, columns, primaryKey, primaryValue);
            if (rowValues.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selected record was not found.");
                return;
            }

            Map<String, JComponent> inputs = new LinkedHashMap<>();
            JPanel formPanel = buildRecordForm(columns, rowValues, inputs, true);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    new JScrollPane(formPanel),
                    "Update Record - " + displayName(table),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            List<ColumnInfo> updateColumns = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (ColumnInfo column : columns) {
                if (column.isPrimaryKey || column.isAutoIncrement || column.isCreatedAt) continue;
                JComponent input = inputs.get(column.name);
                if (input == null) continue;

                updateColumns.add(column);
                values.add(getInputValue(input));
            }

            if (updateColumns.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No editable columns found.");
                return;
            }

            StringBuilder sql = new StringBuilder("UPDATE `").append(table).append("` SET ");
            for (int i = 0; i < updateColumns.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("`").append(updateColumns.get(i).name).append("` = ?");
            }
            sql.append(" WHERE `").append(primaryKey.name).append("` = ?");

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < values.size(); i++) {
                    setPreparedValue(ps, i + 1, values.get(i), updateColumns.get(i));
                }
                setPreparedValue(ps, values.size() + 1, primaryValue, primaryKey);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            afterDataChanged(table);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void archiveRecordByPrimaryValue(String table, Object primaryValue) {
        if (!isAllowedTable(table)) return;

        if (!tableHasArchiveSupport(table)) {
            JOptionPane.showMessageDialog(this, "This table has no status column, so archive is not available.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Archive selected record?",
                "Confirm Archive",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            ColumnInfo primaryKey = getPrimaryKey(columns);
            if (primaryKey == null) {
                JOptionPane.showMessageDialog(this, "This table has no primary key. Archive is not available.");
                return;
            }

            String archiveValue = "school_years".equals(table) ? "Inactive" : "Archived";
            try (PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET `status` = ? WHERE `" + primaryKey.name + "` = ?")) {
                ps.setString(1, archiveValue);
                setPreparedValue(ps, 2, primaryValue, primaryKey);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record archived successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            afterDataChanged(table);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void deleteRecordByPrimaryValue(String table, Object primaryValue) {
        if (!isAllowedTable(table)) return;

        if (!tableHasArchiveSupport(table)) {
            JOptionPane.showMessageDialog(this,
                    "This table has no status column, so delete-as-archive is not available.",
                    "Archive Not Available",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Archive selected record instead of permanently deleting it?",
                "Confirm Archive",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            ColumnInfo primaryKey = getPrimaryKey(columns);
            if (primaryKey == null) {
                JOptionPane.showMessageDialog(this, "This table has no primary key. Archive is not available.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE `" + table + "` SET `status` = ? WHERE `" + primaryKey.name + "` = ?")) {
                ps.setString(1, "Archived");
                setPreparedValue(ps, 2, primaryValue, primaryKey);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Record archived successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            afterDataChanged(table);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void loadDialogTable(DefaultTableModel model, String table) {
        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            model.setRowCount(0);
            model.setColumnCount(0);
            for (ColumnInfo column : columns) model.addColumn(column.name);

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + table + "` ORDER BY 1 DESC LIMIT 300");
                 ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int count = meta.getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[count];
                    for (int i = 1; i <= count; i++) row[i - 1] = rs.getObject(i);
                    model.addRow(row);
                }
            }
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void copyDialogSelectionToMain(JTable dialogTable, DefaultTableModel dialogModel) {
        int selected = dialogTable.getSelectedRow();
        if (selected < 0) return;
        int dialogModelRow = dialogTable.convertRowIndexToModel(selected);
        if (dialogModel.getColumnCount() == 0 || tableModel.getColumnCount() == 0) return;

        Object primaryValue = dialogModel.getValueAt(dialogModelRow, 0);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object mainValue = tableModel.getValueAt(i, 0);
            if (String.valueOf(primaryValue).equals(String.valueOf(mainValue))) {
                int viewRow = dataTable.convertRowIndexToView(i);
                dataTable.setRowSelectionInterval(viewRow, viewRow);
                break;
            }
        }
    }

    private void restoreSelectionByPrimaryKey(String table, JTable dialogTable, DefaultTableModel dialogModel) {
        int selected = dialogTable.getSelectedRow();
        if (selected < 0 || dialogModel.getColumnCount() == 0) return;
        Object primaryValue = dialogModel.getValueAt(dialogTable.convertRowIndexToModel(selected), 0);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object mainValue = tableModel.getValueAt(i, 0);
            if (String.valueOf(primaryValue).equals(String.valueOf(mainValue))) {
                int viewRow = dataTable.convertRowIndexToView(i);
                dataTable.setRowSelectionInterval(viewRow, viewRow);
                return;
            }
        }
    }

    private void afterDataChanged(String table) {
        loadDashboardData();
        loadTableData(table);
    }

    private Map<String, Object> getSelectedRowValues(List<ColumnInfo> columns, int modelRow) {
        Map<String, Object> values = new LinkedHashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            if (i < tableModel.getColumnCount()) {
                values.put(columns.get(i).name, tableModel.getValueAt(modelRow, i));
            }
        }
        return values;
    }

    private Object getInputValue(JComponent input) {
        if (input instanceof JComboBox<?>) {
            Object selected = ((JComboBox<?>) input).getSelectedItem();
            return selected == null ? null : selected.toString();
        }
        if (input instanceof JTextField) {
            return ((JTextField) input).getText().trim();
        }
        return null;
    }

    private void setPreparedValue(PreparedStatement ps, int index, Object value, ColumnInfo column) throws SQLException {
        if (value == null || value.toString().trim().isEmpty()) {
            if (column.nullable) {
                ps.setNull(index, Types.NULL);
            } else {
                ps.setString(index, "");
            }
            return;
        }
        ps.setString(index, value.toString().trim());
    }

    private boolean canConnect() {
        try (Connection ignored = getConnection()) {
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException modernDriverMissing) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException oldDriverMissing) {
                throw new SQLException("MySQL Connector/J is missing. Add mysql-connector-j.jar to your NetBeans Libraries.");
            }
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private int countRows(String table) {
        if (!isAllowedTable(table)) return 0;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM `" + table + "`");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception ex) {
            return 0;
        }
    }

    private int countWhere(String table, String whereClause) {
        if (!isAllowedTable(table)) return 0;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM `" + table + "` WHERE " + whereClause);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception ex) {
            return 0;
        }
    }

    private Map<String, Integer> getScheduleDayCounts() {
        Map<String, Integer> data = new LinkedHashMap<>();
        data.put("Mon", 0);
        data.put("Tue", 0);
        data.put("Wed", 0);
        data.put("Thu", 0);
        data.put("Fri", 0);
        data.put("Sat", 0);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT day_of_week, COUNT(*) total FROM schedules GROUP BY day_of_week");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String day = rs.getString("day_of_week");
                int total = rs.getInt("total");
                if (day == null) continue;
                if (day.startsWith("Mon")) data.put("Mon", total);
                else if (day.startsWith("Tue")) data.put("Tue", total);
                else if (day.startsWith("Wed")) data.put("Wed", total);
                else if (day.startsWith("Thu")) data.put("Thu", total);
                else if (day.startsWith("Fri")) data.put("Fri", total);
                else if (day.startsWith("Sat")) data.put("Sat", total);
            }
        } catch (Exception ignored) {
        }
        return data;
    }

    private List<ColumnInfo> getColumns(Connection conn, String table) throws SQLException {
        List<ColumnInfo> columns = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SHOW FULL COLUMNS FROM `" + table + "`");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ColumnInfo column = new ColumnInfo();
                column.name = rs.getString("Field");
                column.type = rs.getString("Type");
                column.nullable = "YES".equalsIgnoreCase(rs.getString("Null"));
                column.key = rs.getString("Key");
                column.defaultValue = rs.getString("Default");
                column.extra = rs.getString("Extra");
                column.isPrimaryKey = "PRI".equalsIgnoreCase(column.key);
                column.isAutoIncrement = column.extra != null && column.extra.toLowerCase().contains("auto_increment");
                column.isCreatedAt = "created_at".equalsIgnoreCase(column.name);
                column.isEnum = column.type != null && column.type.toLowerCase().startsWith("enum(");
                column.enumValues = parseEnumValues(column.type);
                columns.add(column);
            }
        }
        return columns;
    }

    private ColumnInfo getPrimaryKey(List<ColumnInfo> columns) {
        for (ColumnInfo column : columns) {
            if (column.isPrimaryKey) return column;
        }
        return columns.isEmpty() ? null : columns.get(0);
    }

    private List<String> parseEnumValues(String type) {
        List<String> values = new ArrayList<>();
        if (type == null || !type.toLowerCase().startsWith("enum(")) return values;

        int start = type.indexOf('(');
        int end = type.lastIndexOf(')');
        if (start < 0 || end <= start) return values;

        String inside = type.substring(start + 1, end);
        StringBuilder current = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < inside.length(); i++) {
            char ch = inside.charAt(i);
            if (ch == '\'') {
                if (inQuote) {
                    values.add(current.toString());
                    current.setLength(0);
                    inQuote = false;
                } else {
                    inQuote = true;
                }
            } else if (inQuote) {
                current.append(ch);
            }
        }
        return values;
    }

    private boolean tableHasArchiveSupport(String table) {
        if (!isAllowedTable(table)) return false;
        try (Connection conn = getConnection()) {
            List<ColumnInfo> columns = getColumns(conn, table);
            for (ColumnInfo column : columns) {
                if ("status".equalsIgnoreCase(column.name)) return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private boolean isAllowedTable(String table) {
        return Arrays.asList(TABLES).contains(table);
    }

    private String tableFromDisplayName(String displayName) {
        for (String table : TABLES) {
            if (displayName(table).equals(displayName)) return table;
        }
        return "students";
    }

    private String displayName(String table) {
        String[] parts = table.split("_");
        StringBuilder label = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (label.length() > 0) label.append(' ');
            label.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return label.toString();
    }

    private String toLabel(String column) {
        return displayName(column);
    }

    private String iconForTable(String table) {
        switch (table) {
            case "admins": return "♛";
            case "students": return "♟";
            case "courses": return "▣";
            case "curriculum": return "▤";
            case "departments": return "⌂";
            case "enrollments": return "✓";
            case "grades": return "★";
            case "instructors": return "◉";
            case "rooms": return "▦";
            case "schedules": return "◷";
            case "school_years": return "◫";
            case "sections": return "▥";
            case "semesters": return "◌";
            case "subjects": return "◆";
            default: return "•";
        }
    }

    private void openAdminAIFrame() {
        try {
            int selectedId = getSelectedPrimaryIdSilently();
            new AdminAIFrame(currentTable, selectedId).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "AdminAIFrame cannot be opened. Make sure AdminAIFrame.java is in the same package and has no compile errors.",
                    "Admin AI Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getSelectedPrimaryIdSilently() {
        if (dataTable == null || tableModel == null || tableModel.getColumnCount() == 0) {
            return 0;
        }

        int selected = dataTable.getSelectedRow();
        if (selected < 0) {
            return 0;
        }

        int modelRow = dataTable.convertRowIndexToModel(selected);
        Object value = tableModel.getValueAt(modelRow, 0);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception ignored) {
            return 0;
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        dispose();
        openPossibleLoginFrame();
    }

    private void openPossibleLoginFrame() {
        String[] possibleNames = {
                "com.mycompany.studentrecordsystem.LoginFrame",
                "com.mycompany.studentrecordsystem.Login",
                "com.mycompany.studentrecordsystem.LoginForm",
                "com.mycompany.studentrecordsystem.AdminLoginFrame"
        };

        for (String className : possibleNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                Object instance = constructor.newInstance();
                if (instance instanceof JFrame) {
                    ((JFrame) instance).setVisible(true);
                    return;
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void showDatabaseError(Exception ex) {
        connectionStatusLabel.setText("Database Error");
        connectionStatusLabel.setForeground(new Color(220, 38, 38));

        String message = ex.getMessage();
        if (message == null) message = "Unknown database error.";
        if (message.toLowerCase().contains("cannot delete or update a parent row")
                || message.toLowerCase().contains("foreign key constraint fails")) {
            message = "This record is being used by another table. Archive it instead, or delete the related records first.\n\nDetails: " + message;
        }

        JOptionPane.showMessageDialog(this, message, "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    
    public static void main(String args[]) {
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> new AdminDashboard().setVisible(true));
    }


    private static class TableViewQuery {
        String sql;
        List<String> searchExpressions = new ArrayList<>();

        TableViewQuery(String sql, String... searchExpressions) {
            this.sql = sql;
            this.searchExpressions.addAll(Arrays.asList(searchExpressions));
        }
    }

    private static class ColumnInfo {
        String name;
        String type;
        boolean nullable;
        String key;
        String defaultValue;
        String extra;
        boolean isPrimaryKey;
        boolean isAutoIncrement;
        boolean isCreatedAt;
        boolean isEnum;
        List<String> enumValues = new ArrayList<>();
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class BarChartPanel extends JPanel {
        private final Map<String, Integer> data;

        BarChartPanel(Map<String, Integer> data) {
            this.data = data;
            setOpaque(false);
            setPreferredSize(new Dimension(260, 150));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int left = 35;
            int bottom = 30;
            int top = 12;
            int right = 15;

            g2.setColor(new Color(226, 232, 240));
            g2.drawLine(left, height - bottom, width - right, height - bottom);
            g2.drawLine(left, top, left, height - bottom);

            int max = 1;
            for (Integer value : data.values()) max = Math.max(max, value == null ? 0 : value);

            int count = Math.max(1, data.size());
            int availableWidth = width - left - right;
            int gap = 8;
            int barWidth = Math.max(12, (availableWidth - (gap * (count + 1))) / count);
            int x = left + gap;

            int index = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int value = entry.getValue() == null ? 0 : entry.getValue();
                int barHeight = (int) ((height - bottom - top - 10) * (value / (double) max));
                int y = height - bottom - barHeight;

                Color color = index % 2 == 0 ? new Color(18, 58, 91) : new Color(245, 158, 11);
                g2.setColor(color);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

                g2.setColor(new Color(33, 43, 54));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                String valueText = String.valueOf(value);
                int valueWidth = g2.getFontMetrics().stringWidth(valueText);
                g2.drawString(valueText, x + (barWidth - valueWidth) / 2, Math.max(12, y - 4));

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                String label = entry.getKey();
                if (label.length() > 7) label = label.substring(0, 7);
                int labelWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x + (barWidth - labelWidth) / 2, height - 10);

                x += barWidth + gap;
                index++;
            }

            g2.dispose();
        }
    }

    private static class PieChartPanel extends JPanel {
        private final Map<String, Integer> data;

        PieChartPanel(Map<String, Integer> data) {
            this.data = data;
            setOpaque(false);
            setPreferredSize(new Dimension(220, 150));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int total = 0;
            for (Integer value : data.values()) total += value == null ? 0 : value;
            if (total <= 0) total = 1;

            int size = Math.min(getWidth() / 2, getHeight() - 20);
            int x = 20;
            int y = (getHeight() - size) / 2;
            int start = 90;
            int index = 0;

            Color[] colors = {new Color(18, 58, 91), new Color(245, 158, 11), new Color(22, 163, 74)};
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int value = entry.getValue() == null ? 0 : entry.getValue();
                int angle = (int) Math.round(value * 360.0 / total);
                g2.setColor(colors[index % colors.length]);
                g2.fillArc(x, y, size, size, start, -angle);
                start -= angle;
                index++;
            }

            g2.setColor(Color.WHITE);
            int inner = (int) (size * 0.58);
            int innerX = x + (size - inner) / 2;
            int innerY = y + (size - inner) / 2;
            g2.fillOval(innerX, innerY, inner, inner);

            int active = data.containsKey("Active") ? data.get("Active") : 0;
            int percent = (int) Math.round(active * 100.0 / total);
            String pct = percent + "%";
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.setColor(new Color(33, 43, 54));
            int pctWidth = g2.getFontMetrics().stringWidth(pct);
            g2.drawString(pct, x + (size - pctWidth) / 2, y + size / 2 + 7);

            int legendX = x + size + 25;
            int legendY = y + 25;
            index = 0;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                g2.setColor(colors[index % colors.length]);
                g2.fillRoundRect(legendX, legendY - 10, 12, 12, 4, 4);
                g2.setColor(new Color(99, 115, 129));
                g2.drawString(entry.getKey() + ": " + entry.getValue(), legendX + 18, legendY);
                legendY += 24;
                index++;
            }

            g2.dispose();
        }
    }

    private static class AvatarIcon implements Icon {
        private final int size;
        private final Color background;
        private final Color foreground;

        AvatarIcon(int size, Color background, Color foreground) {
            this.size = size;
            this.background = background;
            this.foreground = foreground;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillOval(x, y, size, size);
            g2.setColor(foreground);
            g2.fillOval(x + size / 3, y + size / 5, size / 3, size / 3);
            g2.fillRoundRect(x + size / 4, y + size / 2, size / 2, size / 3, 20, 20);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JButton adminAIButton;
    private javax.swing.JLabel adminUserLabel;
    private javax.swing.JButton adminsButton;
    private javax.swing.JPanel appRootPanel;
    private javax.swing.JLabel avatarLabel;
    private javax.swing.JPanel bottomActionsPanel;
    private javax.swing.JPanel cardsPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JLabel connectionStatusLabel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton coursesButton;
    private javax.swing.JPanel coursesCardPanel;
    private javax.swing.JLabel coursesCardSubtitleLabel;
    private javax.swing.JLabel coursesCardTitleLabel;
    private javax.swing.JLabel coursesCardValueLabel;
    private javax.swing.JButton crudButton;
    private javax.swing.JButton curriculumButton;
    private javax.swing.JButton dashboardButton;
    private javax.swing.JTable dataTable;
    private javax.swing.JLabel databaseTablesLabel;
    private javax.swing.JButton departmentsButton;
    private javax.swing.JButton enrollmentsButton;
    private javax.swing.JButton gradesButton;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton instructorsButton;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel pageSubtitleLabel;
    private javax.swing.JLabel pageTitleLabel;
    private javax.swing.JPanel recordsPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel rightHeaderPanel;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JButton roomsButton;
    private javax.swing.JButton schedulesButton;
    private javax.swing.JPanel schedulesCardPanel;
    private javax.swing.JLabel schedulesCardSubtitleLabel;
    private javax.swing.JLabel schedulesCardTitleLabel;
    private javax.swing.JLabel schedulesCardValueLabel;
    private javax.swing.JButton schoolYearsButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JButton sectionsButton;
    private javax.swing.JButton semestersButton;
    private javax.swing.JPanel sidebarMenuPanel;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JScrollPane sidebarScrollPane;
    private javax.swing.JPanel studentCardPanel;
    private javax.swing.JLabel studentCardSubtitleLabel;
    private javax.swing.JLabel studentCardTitleLabel;
    private javax.swing.JLabel studentCardValueLabel;
    private javax.swing.JButton studentsButton;
    private javax.swing.JButton subjectsButton;
    private javax.swing.JPanel subjectsCardPanel;
    private javax.swing.JLabel subjectsCardSubtitleLabel;
    private javax.swing.JLabel subjectsCardTitleLabel;
    private javax.swing.JLabel subjectsCardValueLabel;
    private javax.swing.JPanel tableHeaderPanel;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JLabel tableTitleLabel;
    private javax.swing.JPanel titleBoxPanel;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
