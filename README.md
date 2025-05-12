# 🤖 LangChain ChatBot en Java con Spring Boot, MongoDB y OpenAI

Este proyecto implementa un **chatbot inteligente** desarrollado en **Java 17** con **Spring Boot 3**, utilizando **LangChain4j**, **MongoDB Atlas** como base de datos y la API de **OpenAI** para generar respuestas basadas en embeddings.

## 🚀 ¿Qué hace esta aplicación?

Carga artículos desde un archivo JSON, los transforma en segmentos de texto, calcula embeddings con OpenAI y los almacena en MongoDB. Luego, permite hacer consultas en lenguaje natural a través de una interfaz REST (`/chat-bot?question=...`) y el chatbot responde usando el contenido más relevante.

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **MongoDB Atlas** (conexión cloud vía URI)
- **LangChain4j** (`EmbeddingModel`, `TextSegment`, `Retriever`, etc.)
- **OpenAI API**
- **Maven**
- (Próximamente) **Frontend en React**

## 📂 Estructura del proyecto

src/
├── main/
│ ├── java/
│ │ └── com.chatbot.ai/
│ │ ├── ArticlesRepository.java
│ │ ├── ChatBotConfiguration.java
│ │ ├── ChatBotController.java
│ │ └── LangchainChatbotApplication.java
│ └── resources/
│ └── application.properties (ignorado en Git por seguridad)



## 🧪 Ejemplo de uso

Con el servidor levantado:

GET http://localhost:8080/chat-bot?question=¿Qué es Java?


📬 **Respuesta**: (el chatbot devuelve texto basado en los artículos cargados)

## ⚙️ Instalación y ejecución local

> ⚠️ Asegurate de tener Java 17+, Maven, y una cuenta con clave API válida de OpenAI.

### 1. Cloná el repositorio

```bash
git clone https://github.com/Punkylawer/ChatBotAI.git
cd ChatBotAI

app.mongodb.url=mongodb+srv://<usuario>:<contraseña>@<tu_cluster>.mongodb.net/chatbot_db?retryWrites=true&w=majority&appName=Cluster0
app.mongodb.db-name=chatbot_db
app.openai.apiKey=sk-...tu_clave_aca...
app.load-articles=true


mvn clean install
mvn spring-boot:run


GET http://localhost:8080/chat-bot?question=¿Cuál es el objetivo del chatbot?


