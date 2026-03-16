// src/i18n/index.js
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    debug: true,
    interpolation: {
      escapeValue: false,
    },
    resources: {
      en: {
        translation: {
          title: "Get Hired Faster",
          subtitle: "Upload your resume — AI instantly gives you ATS score, improvements, and a rewritten version",
          dropHere: "Drop your resume here",
          orClick: "or click to select PDF file",
          chooseFile: "Choose PDF File",
          jobDescLabel: "Job Description (optional — makes results much better)",
          jobDescPlaceholder: "Paste the full job description here...",
          analyzeBtn: "Analyze My Resume Now",
          atsTitle: "Your ATS Score",
          strengthsTitle: "Strengths",
          weaknessesTitle: "Weaknesses",
          suggestionsTitle: "Suggestions & Improvements",
          improvedTitle: "Improved Resume",
          summaryTitle: "Summary",
          copyBtn: "Copy Full Text",
          loading: "Analyzing with AI...",
          errorDefault: "Something went wrong. Is the backend running?",
          // add more keys as needed
        }
      },
      ka: {
        translation: {
          title: "უფრო სწრაფად დასაქმდი",
          subtitle: "ატვირთეთ თქვენი რეზიუმე — AI მყისიერად მოგცემთ ATS ქულას, გაუმჯობესებებს და გადაწერილ ვერსიას",
          dropHere: "ჩააგდეთ თქვენი რეზიუმე აქ",
          orClick: "ან დააჭირეთ PDF ფაილის ასარჩევად",
          chooseFile: "აირჩიეთ PDF ფაილი",
          jobDescLabel: "სამუშაო აღწერა (სურვილისამებრ — შედეგები ბევრად უკეთესი იქნება)",
          jobDescPlaceholder: "ჩასვით სრული სამუშაო აღწერა აქ...",
          analyzeBtn: "ჩემი რეზიუმეს ანალიზი ახლა",
          atsTitle: "თქვენი ATS ქულა",
          strengthsTitle: "ძლიერი მხარეები",
          weaknessesTitle: "სუსტი მხარეები",
          suggestionsTitle: "რჩევები და გაუმჯობესებები",
          improvedTitle: "გაუმჯობესებული რეზიუმე",
          summaryTitle: "შეჯამება",
          copyBtn: "სრული ტექსტის კოპირება",
          loading: "AI ანალიზი მიმდინარეობს...",
          errorDefault: "რაღაც შეცდომა მოხდა. უკანა მხარე მუშაობს?",
          // add more
        }
      }
    }
  });

export default i18n;