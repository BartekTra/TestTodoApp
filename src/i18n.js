import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import HttpBackend from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
  .use(HttpBackend) // Wczytywanie plików językowych
  .use(LanguageDetector) // Wykrywanie języka przeglądarki
  .use(initReactI18next) // Integracja z React
  .init({
    fallbackLng: 'en', // Domyślny język
    debug: true, // Debugowanie
    interpolation: {
      escapeValue: false, // Wyłączenie escapingu (React już to robi)
    },
  });

export default i18n;
