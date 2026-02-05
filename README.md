# BiblioTech: Algorithmic Library Engine

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java)
![JavaFX](https://img.shields.io/badge/UI-JavaFX%20Reactive-blue?style=for-the-badge&logo=oracle)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20Pattern-purple?style=for-the-badge)

**BiblioTech** is a high-performance library administration system built to demonstrate **GUI Architecture** and **Algorithmic Efficiency**. Unlike standard CRUD apps, it utilizes a custom MVC/MVVM implementation to decouple the reactive JavaFX frontend from the data service layer.

> **Core Focus:** Desktop Application Architecture, State Management, and Data Serialization.

## 🏗 System Architecture

The application implements a strict separation of concerns to ensure maintainability and testability:

* **View Layer (FXML/CSS):** Decoupled UI definitions for a responsive, theme-able interface.
* **Controller Layer:** Handles user events and binds data using **JavaFX Observable Properties** (Reactive Programming).
* **Service Layer:** Orchestrates business logic (Loan validation, Late fee calculation) and data persistence.
* **Data Layer:** Custom CSV parsing engine optimized for low-memory environments.

## 🚀 Key Technical Features

### 1. Algorithmic Search & Sort
* Implemented optimized search routines (Linear/Binary) to filter thousands of book records in milliseconds.
* Dynamic sorting engine for catalog management.

### 2. Smart Automation (AI Assistant)
* Integrated helper module to assist administrators with common tasks.
* Predictive input validation to reduce human error during data entry.

### 3. Reactive Dashboard
* Real-time visualization of library statistics (Active Loans, Overdue Books) using **Live Charts**.
* Event-driven updates that reflect state changes instantly across the application without manual refreshing.

## 🛠 Tech Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Java 11+ (Modular Design) |
| **UI Framework** | JavaFX 17 + CSS Styling |
| **Persistence** | Flat-File (CSV) with Custom Serializer |
| **Build Tool** | PowerShell Automation |

## 🔧 Installation & Execution

### Prerequisites
* Java JDK 11 or higher.
* JavaFX SDK (if not running via bundled script).

### Quick Start (Windows)
The project includes a custom automation script to handle classpath configurations and module dependencies automatically.

```powershell
# 1. Clone the repository
git clone [https://github.com/desagencydes-rgb/BiblioTech-Algorithmic-Archiving.git](https://github.com/desagencydes-rgb/BiblioTech-Algorithmic-Archiving.git)

# 2. Enter directory
cd BiblioTech-Algorithmic-Archiving

# 3. Launch Application (Auto-build)
.\run_app.ps1
```

## Manual Compilation
For environments without PowerShell:

```Bash

javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -d bin src/application/Main.java
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp bin application.
```

## 📂 Project Structure
```Plaintext

BiblioTech/
├── src/
│   ├── controllers/    # Event Handlers & Data Binding
│   ├── models/         # POJOs & Property Wrappers
│   ├── views/          # FXML Layout Definitions
│   └── services/       # Business Logic & IO
├── data/               # Persistent Storage (CSV)
└── lib/                # Dependency Injection
```

## 📜 License
Proprietary Software. Developed by **D.E.S Agency R&D**. Open for technical review.
