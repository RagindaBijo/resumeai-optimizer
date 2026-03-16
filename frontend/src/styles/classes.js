// src/styles/classes.js

export const layout = {
  page: "min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-50 dark:from-gray-950 dark:via-gray-900 dark:to-gray-950 transition-colors duration-300",
  mainContainer: "max-w-6xl mx-auto px-6 py-16",
  heroWrapper: "text-center mb-16",
};

export const header = {
  container: "bg-white dark:bg-gray-950 border-b border-gray-200 dark:border-gray-800 shadow-sm sticky top-0 z-10",
  inner: "max-w-7xl mx-auto px-6 py-5 flex items-center justify-between",
  logoWrapper: "flex items-center gap-3",
  logoIcon: "w-11 h-11 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-2xl flex items-center justify-center text-white text-2xl font-bold shadow-md",
  logoText: "text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent",
  toggles: "flex items-center gap-5",
};

export const uploadCard = {
  card: "bg-white dark:bg-gray-900 rounded-3xl shadow-2xl overflow-hidden mb-16 border border-gray-200 dark:border-gray-800",
  padding: "p-10 md:p-14",
  dropArea: "border-3 border-dashed border-indigo-300 dark:border-indigo-600 rounded-3xl p-16 text-center hover:border-indigo-500 dark:hover:border-indigo-400 transition-all bg-indigo-50/30 dark:bg-indigo-950/30",
  iconCircle: "mx-auto w-24 h-24 bg-indigo-100 dark:bg-indigo-900/50 rounded-full flex items-center justify-center mb-6 text-5xl",
  dropTitle: "text-3xl font-bold text-gray-800 dark:text-gray-100 mb-3",
  dropSubtitle: "text-lg text-gray-600 dark:text-gray-400 mb-8",
  fileLabel: "cursor-pointer inline-block bg-indigo-600 dark:bg-indigo-700 text-white px-10 py-5 rounded-2xl text-lg font-semibold hover:bg-indigo-700 dark:hover:bg-indigo-800 transition-all shadow-lg hover:shadow-xl",
  selectedFile: "mt-8 text-green-700 dark:text-green-400 font-medium text-lg",
  textareaLabel: "block text-xl font-semibold text-gray-800 dark:text-gray-200 mb-4",
  textarea: "w-full px-6 py-5 border border-gray-300 dark:border-gray-700 rounded-2xl focus:outline-none focus:ring-4 focus:ring-indigo-200 dark:focus:ring-indigo-900 focus:border-indigo-500 dark:focus:border-indigo-600 text-lg resize-y bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100",
  submitButton: (isDisabled) => `
    mt-10 w-full py-6 text-xl font-bold rounded-2xl transition-all shadow-lg
    ${isDisabled 
      ? 'bg-gray-300 dark:bg-gray-700 text-gray-500 cursor-not-allowed' 
      : 'bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white dark:from-indigo-700 dark:to-purple-900 dark:hover:from-indigo-800 dark:hover:to-purple-950 hover:shadow-xl'
    }
  `,
  errorMessage: "mt-8 text-red-600 dark:text-red-400 text-center text-lg font-medium bg-red-50 dark:bg-red-950/30 p-4 rounded-2xl",
};

export const results = {
  atsCard: "bg-gradient-to-br from-indigo-600 to-purple-600 dark:from-indigo-800 dark:to-purple-950 text-white rounded-3xl shadow-2xl p-12 text-center",
  atsTitle: "text-3xl font-semibold mb-4",
  atsScore: "text-9xl font-extrabold",
  gridContainer: "grid md:grid-cols-2 gap-10",
  strengthCard: "bg-white dark:bg-gray-900 rounded-3xl shadow-xl p-10 border-t-4 border-green-500 dark:border-green-600",
  strengthTitle: "text-3xl font-bold text-green-700 dark:text-green-400 mb-6 flex items-center gap-3",
  weaknessCard: "bg-white dark:bg-gray-900 rounded-3xl shadow-xl p-10 border-t-4 border-red-500 dark:border-red-600",
  weaknessTitle: "text-3xl font-bold text-red-700 dark:text-red-400 mb-6 flex items-center gap-3",
  suggestionsCard: "bg-white dark:bg-gray-900 rounded-3xl shadow-xl p-10",
  suggestionsTitle: "text-3xl font-bold text-indigo-700 dark:text-indigo-400 mb-6 flex items-center gap-3",
  improvedCard: "bg-white dark:bg-gray-900 rounded-3xl shadow-xl p-10",
  improvedHeader: "flex justify-between items-center mb-6",
  improvedTitle: "text-3xl font-bold text-gray-900 dark:text-gray-100",
  improvedCopyBtn: "bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 px-8 py-4 rounded-2xl font-medium transition flex items-center gap-2 text-gray-800 dark:text-gray-200",
  improvedContent: "bg-gray-50 dark:bg-gray-950 p-10 rounded-2xl border border-gray-200 dark:border-gray-800 max-h-[700px] overflow-auto",
  improvedPre: "whitespace-pre-wrap text-base leading-relaxed font-mono text-gray-800 dark:text-gray-200",
  summaryCard: "bg-white dark:bg-gray-900 rounded-3xl shadow-xl p-10",
  summaryTitle: "text-3xl font-bold text-gray-900 dark:text-gray-100 mb-5",
  summaryText: "text-lg text-gray-700 dark:text-gray-300 leading-relaxed whitespace-pre-line",
};

export const footer = {
  container: "bg-white dark:bg-gray-950 border-t border-gray-200 dark:border-gray-800 mt-20 py-8 text-center text-gray-500 dark:text-gray-400 text-sm",
};