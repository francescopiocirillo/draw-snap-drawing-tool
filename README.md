🎨 DrawSnap – Geometric Drawing Tool
====================================

> 🚀 A **Java-based geometric drawing application** built using the **Scrum methodology**, designed to support shape creation, editing, grouping, and custom shape libraries, developed as part of a university project.

* * *

📌 Overview
-----------

**DrawSnap** is a **geometric drawing tool** developed as part of a **Software Architecture Design** course project at the **University of Salerno**. Built in **Java** using the **Scrum framework**, this application provides users with an intuitive interface for creating and editing shapes with advanced manipulation capabilities.

### 📁 Project Highlights

* ✅ **Multiple shape types**: lines, rectangles, ellipses, polygons, and text
    
* 🧠 **Advanced editing**: move, resize, rotate, mirror, stretch, cut/copy/paste
    
* 🎨 **Customizable UI**: colors, grid display, zoom levels, shape layering
    
* 📦 **Shape grouping & libraries**: group shapes, save reusable custom shapes
    
* 🧪 **JUnit test suite** for ensuring reliability and robustness
    
* 📊 **Backlogs & reports** managed via Trello and GitHub
    
* 📌 **Developed with real-world Scrum practices**: sprints, reviews, retrospectives, the project highlights the skills of the team members in following agile methodologies
    

* * *

🌍 Language Note
----------------

All **code comments and internal documentation** are written in **Italian**, as the project was developed during a group exam at the **University of Salerno (Italy)**.

Despite this, the **codebase follows international best practices**, with clear method names and class structures that make it easily understandable for global developers and recruiters.

* * *

💡 Features
-----------

### 🖌️ Shape Operations

* Create **lines**, **rectangles**, **ellipses**, **polygons**, and **text shapes**
    
* Customize **stroke and fill colors** (8+ colors)
    
* Adjust **shape size, position, and layering** (bring to front/back)
    
* **Cut, Copy, Paste** shapes with unlimited **Undo** levels
    

### 🔄 Advanced Editing

* **Rotate** shapes by arbitrary angles
    
* **Mirror** shapes horizontally or vertically
    
* **Stretch** shapes in both axes
    
* **Group/Ungroup** multiple shapes for combined operations
    

### 📁 Shape Libraries

* **Save custom shapes** as reusable creation commands
    
* **Import/export** shape libraries for specialized domains (e.g., electrical symbols)
    

### 🖥️ Enhanced UI

* **Zoom** support (4+ levels)
    
* **Grid display** (toggleable and resizable)
    
* **Scrollable canvas** larger than the window
    
* **File persistence**: save/load drawings and shape libraries
    

* * *

🧠 Development Process
----------------------

> Developed iteratively over multiple sprints using **Scrum methodology**, simulating an agile software development lifecycle.

1. **Pre-game:** Initial backlog creation, architecture design, and setup (GitHub, Trello).
    
2. **Sprint 1:** Basic drawing operations and file management.
    
3. **Sprint 2:** Advanced editing features and grouping.
    
4. **Sprint 3:** Shape libraries, polishing features, and final presentation.
    

Each sprint included **planning, reviews, retrospectives,** and **burndown tracking**, with artifacts stored in the repository.

* * *

🧪 Testing
----------

**JUnit-based automated tests** were implemented to ensure the correctness of:

* Shape creation & manipulation
    
* Undo operation
    
* File save/load functionality
    

Test classes are located in:

```
test/java/it/unisa/software_architecture_design/drawsnapdrawingtool/
```

Because of a version compatiility issue between Mockito and Java 23, when executing tests it is important to add the following VM Options: `-ea -Dnet.bytebuddy.experimental=true`.

* * *

📋 Documentation
----------------

All documentation is available in the `docs/` folder, including:

* **Pre-game documents:** Initial product backlog, architecture description, and sprint planning.
    
* **Sprint artifacts:** Sprint backlogs, burndown charts, review & retrospective reports for each sprint.
    
* **User Manual:** Guidance for end-users on using DrawSnap’s features.
    

* * *

🧱 Project Structure
--------------------

```
📦 draw-snap-drawing-tool
├── 📁 docs
│   ├── 0 - Pre-game
│   ├── 1 - First sprint
│   ├── 2 - Second sprint
│   ├── 3 - Third sprint
│   └── Manuale d’uso
├── 📁 src/main/java/it/unisa/software_architecture_design/drawsnapdrawingtool
│   ├── commands/
│   ├── enumeration/
│   ├── forme/
│   ├── interactionstate/
│   ├── memento/
│   ├── utils/
│   ├── DrawSnapApplication.java
│   ├── DrawSnapController.java
│   ├── DrawSnapModel.java
│   └── module-info.java
├── 📁 test/java/it/unisa/software_architecture_design/drawsnapdrawingtool
├── .gitignore
├── README.md
├── pom.xml
└── mvnw, mvnw.cmd
```

* * *

📸 User Interface Snapshot
--------------------------

<img width="1690" height="789" alt="image" src="https://github.com/user-attachments/assets/c6e60473-aa07-4b30-b8fc-2e15c55eaa1d" />

* * *

👥 Team – University of Salerno
-------------------------------

* https://github.com/RaffaeleCalabrese2

* https://github.com/Pepi2002

* [@francescopiocirillo](https://github.com/francescopiocirillo) (👑 Team Leader)
    
* [@alefaso-lucky](https://github.com/alefaso-lucky)
    

* * *

🚀 How to Run Locally
---------------------

1. Clone the repository:
    
    ```bash
    git clone https://github.com/your-username/draw-snap-drawing-tool.git
    ```
    
2. Open in your preferred IDE (IntelliJ, VS Code, NetBeans).

3. Add the following VM Options: `--add-modules javafx.controls,javafx.fxml,javafx.graphics`.
        
4. Run `DrawSnapApplication.java` to launch the application.
    

### 🧩 Development Environment

* ☕ Java version: **23**
    
* 🧪 JUnit for testing
    
* 📦 Maven for build automation
    

* * *

📬 Contacts
-----------

✉️ Got feedback or want to contribute? Feel free to open an Issue or submit a Pull Request!

* * *

📈 SEO Tags
-----------

```
Java drawing tool, geometric drawing program, shape editing software, Java graphics project, collaborative software engineering, Scrum-based Java project, University of Salerno software project, UML-based architecture, JUnit tested Java application, Maven Java project, shape libraries Java, undo redo operations Java, custom shape creation tool, agile software development project, academic Java project
```

* * *

📄 License
----------

This project is licensed under the **MIT License**, a permissive open-source license that allows anyone to use, modify, and distribute the software freely, as long as credit is given and the original license is included.

> In plain terms: **use it, build on it, just don’t blame us if something breaks**.

> ⭐ Like what you see? Consider giving the project a star!

* * *
