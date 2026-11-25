
import { useState } from 'react';
import './App.css';

const EMPTY_RESUME = {
  fullName: '',
  email: '',
  phone: '',
  linkedIn: '',
  professionalSummary: '',
  education: '',
  experience: '',
  targetRole: '',
  technicalSkills: [],
  projects: [],
  certifications: []
};

function App() {
  // Job details
  const [jobDetails, setJobDetails] = useState({
    companyName: '',
    jobRole: '',
    jobDescription: ''
  });

  // Uploaded PDF
  const [uploadedFile, setUploadedFile] = useState(null);

  // Resume details extracted from uploaded PDF
  const [resume, setResume] = useState(EMPTY_RESUME);

  // Analysis and UI states
  const [analysisResult, setAnalysisResult] = useState(null);
  const [showEditPrompt, setShowEditPrompt] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [isResumeSaved, setIsResumeSaved] = useState(false);
  const [showPreview, setShowPreview] = useState(false);

  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [isDownloading, setIsDownloading] = useState(false);
  const [message, setMessage] = useState('');
  const [newSkill, setNewSkill] = useState('');

  // =====================================
  // UPDATE JOB DETAILS
  // =====================================

  const handleJobChange = (event) => {
    const { name, value } = event.target;

    setJobDetails((previous) => ({
      ...previous,
      [name]: value
    }));
  };

  // =====================================
  // UPDATE RESUME DETAILS
  // =====================================

  const handleResumeChange = (event) => {
    const { name, value } = event.target;

    setResume((previous) => ({
      ...previous,
      [name]: value
    }));
  };

  // =====================================
  // UPLOAD PDF
  // =====================================

  const handleFileChange = (event) => {
    const file = event.target.files[0];

    if (!file) return;

    if (
      file.type !== 'application/pdf' &&
      !file.name.toLowerCase().endsWith('.pdf')
    ) {
      setMessage('Please upload a PDF file only.');
      setUploadedFile(null);
      event.target.value = '';
      return;
    }

    setUploadedFile(file);

    // Reset previous analysis and editing information
    setAnalysisResult(null);
    setResume({ ...EMPTY_RESUME });
    setShowEditPrompt(false);
    setIsEditing(false);
    setIsResumeSaved(false);
    setShowPreview(false);
    setMessage('PDF uploaded successfully.');
  };

  // =====================================
  // ANALYZE RESUME
  // =====================================

  const handleAnalyze = async () => {
    if (!jobDetails.companyName.trim()) {
      setMessage('Please enter company name.');
      return;
    }

    if (!jobDetails.jobRole.trim()) {
      setMessage('Please enter job role.');
      return;
    }

    if (!jobDetails.jobDescription.trim()) {
      setMessage('Please enter job description.');
      return;
    }

    if (!uploadedFile) {
      setMessage('Please upload your PDF resume.');
      return;
    }

    try {
      setIsAnalyzing(true);
      setMessage('Uploading resume and analyzing with AI...');

      setAnalysisResult(null);
      setShowEditPrompt(false);
      setIsEditing(false);
      setIsResumeSaved(false);

      const formData = new FormData();

      formData.append('file', uploadedFile);
      formData.append('companyName', jobDetails.companyName.trim());
      formData.append('jobRole', jobDetails.jobRole.trim());
      formData.append(
        'jobDescription',
        jobDetails.jobDescription.trim()
      );

      const response = await fetch(
        'http://localhost:8080/api/resume/analyze-upload',
        {
          method: 'POST',
          body: formData
        }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(
          errorText || `Server error: ${response.status}`
        );
      }

      const result = await response.json();

      // Backend must return resumeDetails
      const details = result.resumeDetails;

      if (!details) {
        throw new Error(
          'Resume details are missing in backend response.'
        );
      }

      // Populate form using actual extracted PDF details
      const extractedResume = {
        fullName: details.fullName || '',
        email: details.email || '',
        phone: details.phone || '',
        linkedIn: details.linkedIn || '',
        professionalSummary: details.professionalSummary || '',
        education: details.education || '',
        experience: details.experience || '',
        targetRole: details.targetRole || jobDetails.jobRole,

        technicalSkills: Array.isArray(details.technicalSkills)
          ? details.technicalSkills
          : [],

        projects: Array.isArray(details.projects)
          ? details.projects
          : [],

        certifications: Array.isArray(details.certifications)
          ? details.certifications
          : []
      };

      setResume(extractedResume);
      setAnalysisResult(result);

      // Ask whether the candidate wants to edit
      setShowEditPrompt(true);
      setIsEditing(false);
      setIsResumeSaved(false);
      setShowPreview(false);

      setMessage('Resume analysis completed successfully.');

    } catch (error) {
      console.error('Resume analysis error:', error);
      setMessage(`Analysis failed: ${error.message}`);

    } finally {
      setIsAnalyzing(false);
    }
  };

  // =====================================
  // YES - EDIT RESUME
  // =====================================

  const handleYesEdit = () => {
    setShowEditPrompt(false);
    setIsEditing(true);
    setIsResumeSaved(false);
    setShowPreview(false);
    setMessage('You can now edit your resume details.');
  };

  // =====================================
  // NO - RETURN TO ANALYSIS RESULTS
  // =====================================

  const handleNoEdit = () => {
    setShowEditPrompt(false);
    setIsEditing(false);
    setIsResumeSaved(false);
    setShowPreview(false);
    setMessage('You can view your resume analysis results.');
  };

  // =====================================
  // ADD SKILL
  // =====================================

  const addSkill = () => {
    const skill = newSkill.trim();

    if (!skill) return;

    const exists = resume.technicalSkills.some(
      (item) => item.toLowerCase() === skill.toLowerCase()
    );

    if (exists) {
      setMessage('This skill already exists.');
      return;
    }

    setResume((previous) => ({
      ...previous,
      technicalSkills: [...previous.technicalSkills, skill]
    }));

    setNewSkill('');
    setMessage('');
  };

  // =====================================
  // REMOVE SKILL
  // =====================================

  const removeSkill = (indexToRemove) => {
    setResume((previous) => ({
      ...previous,
      technicalSkills: previous.technicalSkills.filter(
        (_, index) => index !== indexToRemove
      )
    }));
  };

  // =====================================
  // ADD PROJECT
  // =====================================

  const addProject = () => {
    setResume((previous) => ({
      ...previous,
      projects: [...previous.projects, '']
    }));
  };

  // =====================================
  // UPDATE PROJECT
  // =====================================

  const updateProject = (index, value) => {
    setResume((previous) => ({
      ...previous,
      projects: previous.projects.map((project, i) =>
        i === index ? value : project
      )
    }));
  };

  // =====================================
  // REMOVE PROJECT
  // =====================================

  const removeProject = (indexToRemove) => {
    setResume((previous) => ({
      ...previous,
      projects: previous.projects.filter(
        (_, index) => index !== indexToRemove
      )
    }));
  };

  // =====================================
  // UPDATE CERTIFICATIONS
  // =====================================

  const handleCertificationChange = (event) => {
    const certifications = event.target.value
      .split('\n')
      .filter((item) => item.trim() !== '');

    setResume((previous) => ({
      ...previous,
      certifications
    }));
  };

  // =====================================
  // SAVE CHANGES
  // =====================================

  const handleSaveChanges = () => {
    setIsEditing(false);
    setIsResumeSaved(true);
    setShowPreview(false);
    setMessage('Resume changes saved successfully.');
  };

  // =====================================
  // DOWNLOAD UPDATED PDF
  // =====================================

  const downloadPDF = async () => {
    try {
      setIsDownloading(true);
      setMessage('Generating updated resume PDF...');

      const response = await fetch(
        'http://localhost:8080/api/resume/builder/download-edited',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(resume)
        }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(
          errorText || `PDF download failed: ${response.status}`
        );
      }

      const pdfBlob = await response.blob();

      const pdfUrl = window.URL.createObjectURL(pdfBlob);
      const link = document.createElement('a');

      link.href = pdfUrl;
      link.download = 'Updated_Resume.pdf';

      document.body.appendChild(link);
      link.click();
      link.remove();

      window.URL.revokeObjectURL(pdfUrl);

      setMessage('Updated resume downloaded successfully.');

    } catch (error) {
      console.error('PDF download error:', error);
      setMessage(`Download failed: ${error.message}`);

    } finally {
      setIsDownloading(false);
    }
  };

  // =====================================
  // FRONTEND UI
  // =====================================

  return (
    <div className="app-container">

      {/* HEADER */}

      <header className="app-header">
        <h1>AI Resume Analyzer & Smart Resume Builder</h1>
        <p>
          Analyze your resume, identify skill gaps and
          create an improved professional resume.
        </p>
      </header>

      <main className="main-content">

        <section className="editor">

          {/* STEP 1: JOB DETAILS */}

          <div className="job-details-section">

            <h2>Step 1: Enter Job Details</h2>

            <label>Company Name</label>
            <input
              type="text"
              name="companyName"
              value={jobDetails.companyName}
              onChange={handleJobChange}
              placeholder="Enter company name"
            />

            <label>Job Role</label>
            <input
              type="text"
              name="jobRole"
              value={jobDetails.jobRole}
              onChange={handleJobChange}
              placeholder="Enter job role"
            />

            <label>Job Description</label>
            <textarea
              name="jobDescription"
              value={jobDetails.jobDescription}
              onChange={handleJobChange}
              placeholder="Paste complete job description"
              rows="7"
            />

          </div>

          {/* STEP 2: UPLOAD RESUME */}

          <div className="job-details-section">

            <h2>Step 2: Upload Existing Resume</h2>

            <label>Select Resume PDF</label>

            <input
              type="file"
              accept=".pdf,application/pdf"
              onChange={handleFileChange}
            />

            {uploadedFile && (
              <p className="file-name">
                Selected File: <strong>{uploadedFile.name}</strong>
              </p>
            )}

            <button
              type="button"
              className="analyze-button"
              onClick={handleAnalyze}
              disabled={isAnalyzing}
            >
              {isAnalyzing ? 'Analyzing...' : 'Analyze Resume'}
            </button>

          </div>

          {/* STEP 3: ANALYSIS RESULTS */}

          {analysisResult && (
            <div className="analysis-results">

              <h2>Step 3: Resume Analysis Results</h2>

              <div className="match-card">
                <span>Resume Match Percentage</span>
                <strong>
                  {analysisResult.matchPercentage ?? 0}%
                </strong>
              </div>

              <p>
                <strong>Company:</strong>{' '}
                {analysisResult.companyName || jobDetails.companyName}
              </p>

              <p>
                <strong>Job Role:</strong>{' '}
                {analysisResult.jobRole || jobDetails.jobRole}
              </p>

              <h3>Matched Skills</h3>
              {analysisResult.matchedSkills?.length > 0 ? (
                <ul className="result-list">
                  {analysisResult.matchedSkills.map((skill, index) => (
                    <li key={index}>{skill}</li>
                  ))}
                </ul>
              ) : (
                <p>No matched skills returned.</p>
              )}

              <h3>Missing Skills</h3>
              {analysisResult.missingSkills?.length > 0 ? (
                <ul className="result-list">
                  {analysisResult.missingSkills.map((skill, index) => (
                    <li key={index}>{skill}</li>
                  ))}
                </ul>
              ) : (
                <p>No missing skills returned.</p>
              )}

              <h3>Required Skills</h3>
              {analysisResult.totalRequiredSkills?.length > 0 ? (
                <ul className="result-list">
                  {analysisResult.totalRequiredSkills.map(
                    (skill, index) => (
                      <li key={index}>{skill}</li>
                    )
                  )}
                </ul>
              ) : (
                <p>No required skills returned.</p>
              )}

              <h3>AI Recommendations</h3>
              <p className="recommendation">
                {analysisResult.recommendation ||
                  'No recommendations returned.'}
              </p>

            </div>
          )}

          {/* YES / NO DIALOGUE */}

          {showEditPrompt && (
            <div className="edit-dialog-overlay">
              <div className="edit-dialog">

                <h2>Resume Analysis Completed!</h2>

                <p>
                  Your resume analysis is complete.
                  Would you like to edit your resume?
                </p>

                <div className="dialog-buttons">

                  <button
                    type="button"
                    className="yes-button"
                    onClick={handleYesEdit}
                  >
                    Yes, Edit My Resume
                  </button>

                  <button
                    type="button"
                    className="no-button"
                    onClick={handleNoEdit}
                  >
                    No, Go Back
                  </button>

                </div>

              </div>
            </div>
          )}

          {/* STEP 4: RESUME EDITOR */}

          {isEditing && (
            <div className="resume-edit-section">

              <h2>Step 4: Edit Your Resume</h2>

              <p className="editor-description">
                Review the details extracted from your uploaded PDF
                and make any necessary changes.
              </p>

              <label>Full Name</label>
              <input
                type="text"
                name="fullName"
                value={resume.fullName}
                onChange={handleResumeChange}
              />

              <label>Email</label>
              <input
                type="email"
                name="email"
                value={resume.email}
                onChange={handleResumeChange}
              />

              <label>Phone</label>
              <input
                type="text"
                name="phone"
                value={resume.phone}
                onChange={handleResumeChange}
              />

              <label>LinkedIn</label>
              <input
                type="text"
                name="linkedIn"
                value={resume.linkedIn}
                onChange={handleResumeChange}
              />

              <label>Target Job Role</label>
              <input
                type="text"
                name="targetRole"
                value={resume.targetRole}
                onChange={handleResumeChange}
              />

              <label>Professional Summary</label>
              <textarea
                name="professionalSummary"
                value={resume.professionalSummary}
                onChange={handleResumeChange}
                rows="5"
              />

              {/* EDUCATION */}

              <h3>Education</h3>
              <textarea
                name="education"
                value={resume.education}
                onChange={handleResumeChange}
                rows="4"
              />

              {/* EXPERIENCE */}

              <h3>Experience</h3>
              <textarea
                name="experience"
                value={resume.experience}
                onChange={handleResumeChange}
                rows="5"
              />

              {/* TECHNICAL SKILLS */}

              <h3>Technical Skills</h3>

              <div className="skill-input">
                <input
                  type="text"
                  placeholder="Enter a skill"
                  value={newSkill}
                  onChange={(event) => setNewSkill(event.target.value)}
                  onKeyDown={(event) => {
                    if (event.key === 'Enter') {
                      event.preventDefault();
                      addSkill();
                    }
                  }}
                />

                <button type="button" onClick={addSkill}>
                  Add Skill
                </button>
              </div>

              <div className="skill-list">
                {resume.technicalSkills.map((skill, index) => (
                  <div className="skill-item" key={index}>
                    <span>{skill}</span>
                    <button
                      type="button"
                      className="remove-button"
                      onClick={() => removeSkill(index)}
                    >
                      Remove
                    </button>
                  </div>
                ))}
              </div>

              {/* PROJECTS */}

              <h3>Projects</h3>

              {resume.projects.map((project, index) => (
                <div className="project-item" key={index}>
                  <label>Project {index + 1}</label>

                  <textarea
                    value={project}
                    onChange={(event) =>
                      updateProject(index, event.target.value)
                    }
                    rows="5"
                    placeholder="Project title and complete description"
                  />

                  <button
                    type="button"
                    className="remove-button"
                    onClick={() => removeProject(index)}
                  >
                    Remove Project
                  </button>
                </div>
              ))}

              <button
                type="button"
                className="add-project-button"
                onClick={addProject}
              >
                Add Project
              </button>

              {/* CERTIFICATIONS */}

              <h3>Certifications</h3>

              <textarea
                value={resume.certifications.join('\n')}
                onChange={handleCertificationChange}
                rows="4"
                placeholder="One certification per line"
              />

              {/* PREVIEW AND SAVE */}

              <div className="action-buttons">

                <button
                  type="button"
                  className="preview-button"
                  onClick={() => setShowPreview(!showPreview)}
                >
                  {showPreview ? 'Hide Preview' : 'Preview Resume'}
                </button>

                <button
                  type="button"
                  className="save-button"
                  onClick={handleSaveChanges}
                >
                  Save Changes
                </button>

              </div>

              {/* RESUME PREVIEW */}

              {showPreview && (
                <div className="resume-preview">

                  <h2>{resume.fullName}</h2>

                  <p>
                    {resume.email}
                    {resume.phone && ` | ${resume.phone}`}
                  </p>

                  <p>{resume.linkedIn}</p>

                  <hr />

                  <h3>Professional Summary</h3>
                  <p>{resume.professionalSummary}</p>

                  <h3>Technical Skills</h3>
                  <p>{resume.technicalSkills.join(', ')}</p>

                  <h3>Projects</h3>
                  {resume.projects.map((project, index) => (
                    <p key={index} className="preview-project">
                      {project}
                    </p>
                  ))}

                  <h3>Education</h3>
                  <p>{resume.education}</p>

                  <h3>Experience</h3>
                  <p>{resume.experience}</p>

                  <h3>Certifications</h3>
                  {resume.certifications.map((item, index) => (
                    <p key={index}>{item}</p>
                  ))}

                </div>
              )}

            </div>
          )}

          {/* STEP 5: SAVE AND DOWNLOAD */}

          {isResumeSaved && (
            <div className="download-section">

              <h2>Resume Updated Successfully!</h2>

              <p>
                Your changes are ready. You can download
                your updated resume.
              </p>

              <button
                type="button"
                className="download-button"
                onClick={downloadPDF}
                disabled={isDownloading}
              >
                {isDownloading
                  ? 'Generating PDF...'
                  : 'Download Updated Resume PDF'}
              </button>

              <button
                type="button"
                className="edit-again-button"
                onClick={() => {
                  setIsResumeSaved(false);
                  setIsEditing(true);
                }}
              >
                Edit Again
              </button>

            </div>
          )}

          {/* STATUS MESSAGE */}

          {message && (
            <p className="message">
              {message}
            </p>
          )}

        </section>

      </main>

    </div>
  );
}

export default App;
