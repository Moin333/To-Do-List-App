# 📝 To-Do List App
A simple and intuitive **To-Do List App** built using **Jetpack Compose**. This app allows users to add, view, and delete tasks in a clean UI with responsive design, making it easy to manage daily tasks.

## 📸 Screenshots
<div align="center">
  <img src="./Screenshot 1.png" alt="Portrait Mode" width="180"/>
  <img src="./Screenshot 0.png" alt="Landscape Mode" width="360"/>
</div>

## 🚀 Features
- **Add Tasks:** Quickly add tasks to your to-do list.
- **Delete Tasks:** Swipe away or tap the delete button to remove tasks.
- **Responsive UI:** Works well in both portrait and landscape orientations.
- **Persistent State:** Keeps the task list intact across screen rotations.

## 🛠️ Technologies Used
- **Kotlin**
- **Jetpack Compose** for building the UI
- **ViewModel** to handle UI-related data
- **State management** using `rememberSaveable`

## 📂 Project Structure
📦 ToDoListApp ┣ 📂 app ┃ ┣ 📂 src ┃ ┃ ┣ 📂 main ┃ ┃ ┃ ┣ 📂 kotlin/com/example/todolistapp ┃ ┃ ┃ ┃ ┣ ToDoScreen.kt ┃ ┃ ┃ ┃ ┣ ToDoViewModel.kt ┃ ┃ ┃ ┣ 📂 res ┃ ┃ ┃ ┃ ┣ 📂 drawable ┃ ┃ ┃ ┃ ┣ 📂 layout ┃ ┃ ┃ ┃ ┣ 📂 values ┃ ┃ ┣ AndroidManifest.xml ┣ .gitignore ┣ build.gradle.kts ┣ README.md ┣ Screenshot 0.png ┣ Screenshot 1.png

## 🔧 Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/todo-list-app.git
   cd todo-list-app
2. Open the project in Android Studio.
3. Build and run the project on an emulator or physical device.

📱 How to Use
1. Enter a task in the input field and click Add Task.
2. Tasks will appear in a list below.
3. Tap the trash icon next to a task to delete it.
4. The task list remains intact across screen rotations.

🛡️ License
This project is licensed under the MIT License. Feel free to use it as you wish.
