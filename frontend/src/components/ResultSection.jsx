import { results } from '../styles/classes';

export default function ResultSection({ result, t, copyToClipboard }) {
  if (!result) return null;

  return (
    <div className="space-y-12">
      <div className={results.atsCard}>
        <h3 className={results.atsTitle}>{t('atsTitle')}</h3>
        <div className={results.atsScore}>{result.atsScore}</div>
        <p className="text-2xl opacity-90">/ 100</p>
      </div>

      <div className={results.gridContainer}>
        <div className={results.strengthCard}>
          <h3 className={results.strengthTitle}>
            <span>✅</span> {t('strengthsTitle')}
          </h3>
          <ul className="space-y-4 text-lg text-gray-700 dark:text-gray-300">
            {result.strengths?.map((item, i) => (
              <li key={i} className="flex gap-4">
                <span className="text-green-500 dark:text-green-400 text-2xl">•</span>
                {item}
              </li>
            )) || <li className="text-gray-500 dark:text-gray-400">{t('noData')}</li>}
          </ul>
        </div>

        <div className={results.weaknessCard}>
          <h3 className={results.weaknessTitle}>
            <span>⚠️</span> {t('weaknessesTitle')}
          </h3>
          <ul className="space-y-4 text-lg text-gray-700 dark:text-gray-300">
            {result.weaknesses?.map((item, i) => (
              <li key={i} className="flex gap-4">
                <span className="text-red-500 dark:text-red-400 text-2xl">•</span>
                {item}
              </li>
            )) || <li className="text-gray-500 dark:text-gray-400">{t('noData')}</li>}
          </ul>
        </div>
      </div>

      {/* Suggestions */}
      <div className={results.suggestionsCard}>
        <h3 className={results.suggestionsTitle}>
          <span>💡</span> {t('suggestionsTitle')}
        </h3>
        <ul className="space-y-5 text-lg text-gray-700 dark:text-gray-300">
          {result.suggestions?.map((item, i) => (
            <li key={i} className="flex gap-5">
              <span className="text-indigo-500 dark:text-indigo-400 text-2xl font-bold">→</span>
              {item}
            </li>
          )) || <li className="text-gray-500 dark:text-gray-400">{t('noData')}</li>}
        </ul>
      </div>

      {/* Improved Resume */}
      <div className={results.improvedCard}>
        <div className={results.improvedHeader}>
          <h3 className={results.improvedTitle}>{t('improvedTitle')}</h3>
          <button
            onClick={() => copyToClipboard(result.improvedResumeText)}
            className={results.improvedCopyBtn}
          >
            📋 {t('copyBtn')}
          </button>
        </div>
        <div className={results.improvedContent}>
          <pre className={results.improvedPre}>
            {result.improvedResumeText || t('noData')}
          </pre>
        </div>
      </div>

      {/* Summary */}
      <div className={results.summaryCard}>
        <h3 className={results.summaryTitle}>{t('summaryTitle')}</h3>
        <p className={results.summaryText}>
          {result.summary || t('noData')}
        </p>
      </div>
    </div>
  );
}