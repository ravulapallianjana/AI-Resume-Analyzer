# AI Resume Analyzer & Smart Resume Builder 🚀

An AI-powered Resume Analyzer and Smart Resume Builder built using **Java, Spring Boot, React, and Ollama**. The application analyzes a candidate's resume against a job description, identifies matching and missing skills, calculates a match percentage, and helps candidates customize and download their resumes.

## 📌 Project Overview

Finding a suitable job requires more than just having a resume. Candidates need to understand whether their skills and experience match the job requirements.

This project helps candidates analyze their resumes against specific job descriptions and identify areas for improvement.

Users can upload a PDF resume, enter company and job details, and receive an AI-assisted analysis with matched skills, missing skills, recommendations, and extracted resume information.

## ✨ Features

### 1. AI-Powered Resume Analysis
- Upload resumes in PDF format.
- Enter company name, job role, and job description.
- Extract resume text from uploaded PDF files.
- Extract required skills from the job description.
- Identify skills available in the candidate's resume.
- Calculate resume match percentage.
- Display matched and missing skills.

### 2. Resume Details Extraction
- Extract candidate information from the uploaded resume.
- Identify professional summary and education details.
- Extract technical skills, experience, and projects.
- Display extracted information for review and editing.

### 3. Smart Resume Builder
- Edit extracted resume details.
- Update professional summary and technical skills.
- Add, update, and remove projects.
- Customize resume information according to job requirements.
- Generate and download an updated PDF resume.

### 4. AI Recommendations
- Receive recommendations based on resume analysis.
- Identify missing technical skills.
- Understand areas where the resume can be improved.

## 🛠️ Technologies Used

### Backend
- Java
- Spring Boot
- Spring AI
- REST APIs
- Apache PDFBox for PDF text extraction
- Maven

### Frontend
- React.js
- JavaScript
- HTML5
- CSS3
- Axios / Fetch API

### AI Integration
- Ollama
- Qwen 2.5:3b model

### Development Tools
- Eclipse / Spring Tool Suite
- Visual Studio Code
- Postman
- Git and GitHub

## 🏗️ Project Architecture

The application follows a frontend-backend architecture.

```text
              USER
                |
                v
        REACT FRONTEND
                |
                | REST API
                v
       SPRING BOOT BACKEND
                |
        +-------+--------+
        |                |
        v                v
  PDF TEXT          AI SERVICES
  EXTRACTION        (OLLAMA)
        |                |
        v                v
  RESUME TEXT       SKILL EXTRACTION
                         |
             +-----------+-----------+
             |                       |
             v                       v
       SKILL MATCHING         RESUME DETAILS
          SERVICE              EXTRACTION
             |                       |
             +-----------+-----------+
                         |
                         v
                 ANALYSIS RESPONSE
                         |
                         v
                  REACT FRONTEND
                         |
                         v
                 RESUME EDITOR
                         |
                         v
                  PDF DOWNLOAD
```

## 📂 Project Structure

```text
AI-Resume-Analyzer/
│
├── Grok/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/SpringGrok/Grok/
│   │   │   │       ├── controller/
│   │   │   │       │   ├── ChatController.java
│   │   │   │       │   └── ResumeController.java
│   │   │   │       │
│   │   │   │       ├── request/
│   │   │   │       ├── response/
│   │   │   │       └── service/
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   └── pom.xml
│
├── resume-builder-ui/
│   ├── public/
│   ├── src/
│   │   ├── assets/
│   │   ├── App.jsx
│   │   ├── App.css
│   │   ├── index.css
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

## ⚙️ Installation and Setup

### Prerequisites

Install the following tools:

- Java JDK
- Node.js and npm
- Git
- Ollama

### Step 1: Clone the Repository

```bash
git clone https://github.com/ravulapalliana/AI-Resume-Analyzer.git
cd AI-Resume-Analyzer
```

### Step 2: Configure Ollama

Install Ollama and download the model used by the application:

```bash
ollama pull qwen2.5:3b
```

Make sure the Ollama service is running.

The current backend configuration uses:

```properties
spring.application.name=Grok

spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=qwen2.5:3b
spring.ai.model.chat=ollama
```

### Step 3: Run the Spring Boot Backend

Open a terminal inside the `Grok` folder.

On Windows:

```cmd
mvnw.cmd spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

### Step 4: Run the React Frontend

Open another terminal inside the `resume-builder-ui` folder.

Install dependencies:

```bash
npm install
```

Start the React application:

```bash
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## 🔗 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/resume/analyze` | Analyze resume text against a job description |
| POST | `/api/resume/analyze-upload` | Upload and analyze a PDF resume |
| POST | `/api/resume/builder/download-edited` | Generate and download the edited resume PDF |

### Resume Upload API

**Endpoint:**

```http
POST /api/resume/analyze-upload
```

**Request type:** `multipart/form-data`

| Parameter | Description |
|---|---|
| file | PDF resume |
| companyName | Company name |
| jobRole | Target job role |
| jobDescription | Job description |

## 🔄 Application Workflow

1. Candidate opens the React application.
2. Enters company name, job role, and job description.
3. Uploads a PDF resume.
4. Spring Boot receives the uploaded file.
5. PDFBox extracts the resume text.
6. Spring AI and Ollama help extract skills from the resume and job description.
7. Java SkillMatchingService identifies matched and missing skills.
8. ResumeDetailsExtractionService extracts candidate information.
9. ResumeService combines the analysis results.
10. React displays the analysis and recommendations.
11. Candidate edits resume information.
12. The updated resume is generated and downloaded as a PDF.

## 🎯 Project Objective

The main objective of this project is to help job seekers understand how well their resumes align with job descriptions and make informed improvements.

It combines Java backend development, React frontend development, REST API integration, PDF processing, and AI-based text extraction in one practical application.

## 🚀 Future Enhancements

- User authentication and authorization.
- Database integration for saving resumes and analysis history.
- Semantic skill matching instead of exact text matching.
- Multiple professional resume templates.
- Resume version management.
- Job description recommendations.
- Cloud deployment.

## 👩‍💻 Developer

**Ravulapalli Anjana**

- GitHub: https://github.com/ravulapalliana
- LinkedIn: https://linkedin.com/in/ravulapalli-anjana-5192a731a

---

⭐ If you find this project useful, consider giving this repository a star!
