import React, { useState, useEffect } from 'react';
import api from '../api/axiosConfig.js';
import { useTranslation } from 'react-i18next';

function TaskReport() {
  const [reportData, setReportData] = useState({
    allTasks: 0,
    inProgress: 0,
    overdue: 0,
    completedOnTime: 0,
  });
  const [responseMessage, setResponseMessage] = useState('');
  const { t } = useTranslation();

  useEffect(() => {
    const fetchReport = async () => {
      try {
        const response = await api.get('/api/v1/GetTaskReport');
        const data = response.data;

        // Ensure all fields have values to avoid NaN issues
        setReportData({
          allTasks: data.allTasks || 0,
          inProgress: data.inProgress || 0,
          overdue: data.overdue || 0,
          completedOnTime: data.completedOnTime || 0,
        });
        setResponseMessage('');
      } catch (error) {
        console.error('Error fetching task report:', error);
        setResponseMessage(t('report.errors.fetchFailed'));
      }
    };

    fetchReport();
  }, [t]);

  return (
    <div className="TaskReport" style={{ textAlign: 'center', margin: '0 auto' }}>
      <h1>{t('report.title')}</h1>
      {responseMessage && <p className="error-message">{responseMessage}</p>}
      <div>
        <p>
          <strong>{t('report.allTasks')}:</strong> {reportData.allTasks}
        </p>
        <p>
          <strong>{t('report.inProgress')}:</strong> {reportData.inProgress}
        </p>
        <p>
          <strong>{t('report.overdue')}:</strong> {reportData.overdue}
        </p>
        <p>
          <strong>{t('report.completedOnTime')}:</strong> {reportData.completedOnTime}
        </p>
        <p>
          <strong>
            <em>{t('report.Message1')} </em> {reportData.completedOnTime} <em>{t('report.z')} </em>{' '}
            {reportData.allTasks} <em>{t('report.Message2')} </em>
          </strong>
        </p>
        <p>
          <strong>
            <em>{t('report.Message3')} </em>
            {reportData.allTasks > 0
              ? ((reportData.completedOnTime / reportData.allTasks) * 100).toFixed(0)
              : 0}
            % {t('report.Message4')}
          </strong>
        </p>
      </div>
    </div>
  );
}

export default TaskReport;