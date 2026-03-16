import { uploadCard } from '../styles/classes';

export default function UploadForm({
  file,
  setFile,
  jobDesc,
  setJobDesc,
  loading,
  handleSubmit,
  t,
  error,
}) {
  return (
    <div className={uploadCard.card}>
      <div className={uploadCard.padding}>
        <form onSubmit={handleSubmit}>
          <div className={uploadCard.dropArea}>
            <div className={uploadCard.iconCircle}>📄</div>
            <h3 className={uploadCard.dropTitle}>{t('dropHere')}</h3>
            <p className={uploadCard.dropSubtitle}>{t('orClick')}</p>

            <input
              type="file"
              accept=".pdf"
              className="hidden"
              id="resumeUpload"
              onChange={(e) => setFile(e.target.files?.[0] || null)}
            />
            <label htmlFor="resumeUpload" className={uploadCard.fileLabel}>
              {t('chooseFile')}
            </label>

            {file && (
              <div className={uploadCard.selectedFile}>
                {t('selected')}: <span className="font-bold">{file.name}</span>
              </div>
            )}
          </div>

          <div className="mt-12">
            <label className={uploadCard.textareaLabel}>
              {t('jobDescLabel')}
            </label>
            <textarea
              rows="5"
              value={jobDesc}
              onChange={(e) => setJobDesc(e.target.value)}
              placeholder={t('jobDescPlaceholder')}
              className={uploadCard.textarea}
            />
          </div>

          <button
            type="submit"
            disabled={loading || !file}
            className={uploadCard.submitButton(loading || !file)}
          >
            {loading ? t('loading') : t('analyzeBtn')}
          </button>
        </form>

        {error && <p className={uploadCard.errorMessage}>{error}</p>}
      </div>
    </div>
  );
}