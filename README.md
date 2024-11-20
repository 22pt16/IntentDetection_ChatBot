
# 🤖 Chatbot Model with Sentiment & Intent Recognition 🌟

Welcome to the **Chatbot Model** project! This application is a Java-based chatbot designed to support users with helpful quotes and responses based on intent recognition and sentiment analysis. It's perfect for mental health support, conversational AI, or as a foundational project for advanced chatbot applications. 🚀

---

## 📌 Features

- **💬 Intent Recognition**: Uses machine learning (Naive Bayes with Weka) to detect the user's intent.
- **🧠 Sentiment Analysis**: Analyzes user sentiment using Stanford CoreNLP to enhance responses.
- **📜 Quote Retrieval**: Provides relevant quotes based on the recognized intent.
- **⚡ Real-time Processing**: Multithreaded for smooth and responsive interaction.
- **📂 Data-Driven**: Uses CSV files for training intents and storing quotes for easy customization.

---

## 🛠️ Tech Stack

- **Java 22**: The core programming language.
- **Stanford CoreNLP**: For advanced NLP and sentiment analysis.
- **Weka**: For training and intent classification using machine learning.
- **Maven**: Dependency management and build automation.
- **SLF4J**: Simple and flexible logging.

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:
- **Java 22+**  
- **Maven**  

### Setup Instructions


1. **Clone the Repository**:
   ```bash
   git clone [https://github.com/22pt16/IntentDetection_ChatBot]
   cd IntentDetection_ChatBot
   ```

2. **Set Up Resources**:
   - Place your `intent.csv` and `quotes.csv` files in the `src/main/resources` directory.

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   java -cp target/Chat-1.0-SNAPSHOT.jar org.example.ChatbotModel
   ```

---

## 💻 Example Usage

1. **Start the chatbot**:
   ```
   Welcome to Mental Support Bot! 
   I'm here to listen and offer support. Please type 'help' for more information or ask a question to get started!
   ```

2. **User Input**:
   ```
   User: I feel stressed.
   ```

3. **Chatbot Response**:
   ```
   Sentiment: Negative
   Intent: mental-support
   Chatbot: "It's okay to feel overwhelmed sometimes. Take a deep breath and focus on one step at a time."
   ```

---

## 🤝 Contributing

Contributions are welcome! 🎉 If you'd like to improve this project:
- Fork the repository.
- Create a new branch for your feature (`git checkout -b feature-name`).
- Commit your changes (`git commit -m 'Add feature-name'`).
- Push to your branch (`git push origin feature-name`).
- Submit a pull request.

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

### 🌟 Let's Build Together

We hope you find this project helpful for building your own conversational AI applications. Feel free to reach out if you have questions or suggestions! 🤗  

---

