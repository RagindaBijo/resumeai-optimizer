
import { useState } from 'react';
import axios from 'axios';
import { useTranslation } from 'react-i18next';
import LanguageToggle from './components/LanguageToggle';
import UploadForm from './components/UploadForm';
import ResultSection from './components/ResultSection';
import { layout, header, footer } from './styles/classes';

function App() {
  const { t } = useTranslation();

  const [file, setFile] = useState(null);
  const [jobDesc, setJobDesc] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

 const handleSubmit = async (e) => {
  e.preventDefault();
  if (!file) {
    setError(t('errorNoFile'));
    return;
  }

  setLoading(true);
  setError('');
  setResult(null);

  const formData = new FormData();
  formData.append('resumeFile', file);
  if (jobDesc.trim()) formData.append('jobDescription', jobDesc.trim());

  // Use environment variable in production, fallback to proxy/localhost in dev
  const API_BASE = import.meta.env.VITE_API_URL;

  try {
    const res = await axios.post(`${API_BASE}/api/resume/analyze`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    setResult(res.data);
  } catch (err) {
    setError(
      err.response?.data?.summary ||
      err.message ||
      t('errorDefault')
    );
  } finally {
    setLoading(false);
  }
};

  const copyToClipboard = (text) => {
    navigator.clipboard.writeText(text);
    alert(t('copySuccess'));
  };

  return (
    
    
    <div className={layout.page}>
      <header className={header.container}>
        <div className={header.inner}>
          <div className={header.logoWrapper}>
            <div className={header.logoIcon}>AI</div>
            <h1 className={header.logoText}>ResumeAI</h1>
          </div>
          <div className={header.toggles}>
            <LanguageToggle />
            
          </div>
        </div>
      </header>

      <main className={layout.mainContainer}>
        <div className={layout.heroWrapper}>
          <h2 className="text-5xl md:text-6xl font-extrabold text-gray-900 dark:text-white tracking-tight mb-5">
            {t('title')}
          </h2>
          <p className="text-xl md:text-2xl text-gray-600 dark:text-gray-300 max-w-3xl mx-auto leading-relaxed">
            {t('subtitle')}
          </p>
        </div>

        <UploadForm
          file={file}
          setFile={setFile}
          jobDesc={jobDesc}
          setJobDesc={setJobDesc}
          loading={loading}
          handleSubmit={handleSubmit}
          t={t}
          error={error}
        />

        <ResultSection
          result={result}
          t={t}
          copyToClipboard={copyToClipboard}
        />
      </main>

      <footer className={footer.container}>
        <p>Built with ❤️ by Luka • Powered by Groq & Spring Boot • 2026</p>
      </footer>
    </div>
    
  );
}

export default App;




give me changed one