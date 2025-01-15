import React, { useState, useEffect } from 'react';
import api from '../.././api/axiosConfig'; // Axios configuration for your backend
import { useTranslation } from 'react-i18next';

function Main() {
  const [tasks, setTasks] = useState([]);
  const [taskTitle, setTaskTitle] = useState('');
  const [taskDescription, setTaskDescription] = useState('');
  const [taskDate, setTaskDate] = useState('');
  const [category, setCategory] = useState('');
  const [responseMessage, setResponseMessage] = useState('');
  const [priority, setPriority] = useState('HIGH');
  const [isEditMode, setIsEditMode] = useState(false);
  const [editTaskId, setEditTaskId] = useState(null);
  const [showArchived, setShowArchived] = useState(false);
  const { t } = useTranslation();

  useEffect(() => {
    fetchTasks();
  }, [showArchived]);

  const fetchTasks = async () => {
    try {
      const endpoint = showArchived ? '/api/v1/GetArchivedTasks' : '/api/v1/GetTasks';
      const response = await api.get(endpoint);
      const formattedTasks = response.data.map((task) => ({
        ...task,
        dueDate: task.dueDate ? formatDate(task.dueDate) : '',
      }));
      setTasks(formattedTasks);
    } catch (error) {
      console.error('Error fetching tasks:', error);
      setResponseMessage('Failed to load tasks.');
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const handleAddTask = async () => {
    if (taskTitle && taskDescription && taskDate && category && priority) {
      try {
        const newTask = {
          title: taskTitle,
          description: taskDescription,
          category: category,
          priority: priority,
          dueDate: taskDate,
        };

        if (isEditMode) {
          await api.put(`/api/v1/EditTask/${editTaskId}`, newTask);
          setResponseMessage('Task updated successfully!');
          setIsEditMode(false);
          setEditTaskId(null);
        } else {
          await api.post('/api/v1/AddTask', newTask);
          setResponseMessage('Task added successfully!');
        }

        resetForm();
        fetchTasks();
      } catch (error) {
        console.error('Error saving task:', error);
        setResponseMessage('Failed to save task.');
      }
    } else {
      setResponseMessage('Please fill in all fields before saving a task.');
    }
  };

  const handleDeleteTask = async (taskId) => {
    try {
      await api.delete(`/api/v1/DeleteTask/${taskId}`);
      setResponseMessage('Task deleted successfully!');
      fetchTasks();
    } catch (error) {
      console.error('Error deleting task:', error);
      setResponseMessage('Failed to delete task.');
    }
  };

  const handleMarkAsDone = async (taskId) => {
    try {
      await api.post(`/api/v1/MarkTaskAsDone/${taskId}`);
      setResponseMessage('Task marked as done successfully!');
      fetchTasks();
    } catch (error) {
      console.error('Error marking task as done:', error);
      setResponseMessage('Failed to mark task as done.');
    }
  };

  const handleArchiveTask = async (taskId) => {
    try {
      await api.post(`/api/v1/ArchiveTask/${taskId}`);
      setResponseMessage('Task archived successfully!');
      fetchTasks();
    } catch (error) {
      console.error('Error archiving task:', error);
      setResponseMessage('Failed to archive task.');
    }
  };

  const handleEditTask = (task) => {
    setTaskTitle(task.title);
    setTaskDescription(task.description);
    setTaskDate(task.dueDate);
    setCategory(task.category);
    setPriority(task.priority);
    setIsEditMode(true);
    setEditTaskId(task.id);
  };

  const resetForm = () => {
    setTaskTitle('');
    setTaskDescription('');
    setTaskDate('');
    setCategory('');
    setPriority('HIGH');
    setIsEditMode(false);
    setEditTaskId(null);
  };

  const toggleArchivedView = () => {
    setShowArchived(!showArchived);
  };

  return (
    <div className="App" style={{ textAlign: 'center', margin: '0 auto' }}>
      <h1>{t('main.title')}</h1>
      <h2>{t('main.addTask')}</h2>
      <div>
        <label htmlFor="taskTitle">{t('main.labels.taskTitle')}:</label>
        <input
          type="text"
          id="taskTitle"
          value={taskTitle}
          onChange={(e) => setTaskTitle(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="taskDescription">{t('main.labels.taskDescription')}:</label>
        <input
          type="text"
          id="taskDescription"
          value={taskDescription}
          onChange={(e) => setTaskDescription(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="Category">{t('main.labels.category')}:</label>
        <input
          type="text"
          id="Category"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="priority">{t('main.labels.priority')}:</label>
        <select
          id="priority"
          value={priority}
          onChange={(e) => setPriority(e.target.value)}
        >
          <option value="HIGH">{t('main.priorities.high')}</option>
          <option value="NORMAL">{t('main.priorities.normal')}</option>
          <option value="LOW">{t('main.priorities.low')}</option>
        </select>
      </div>
      <div>
        <label htmlFor="taskDate">{t('main.labels.date')}:</label>
        <input
          type="date"
          id="taskDate"
          value={taskDate}
          onChange={(e) => setTaskDate(e.target.value)}
        />
      </div>
      <button onClick={handleAddTask}>
        {isEditMode ? t('main.buttons.editTask') : t('main.buttons.addTask')}
      </button>
      <button onClick={toggleArchivedView}>
        {showArchived ? t('main.buttons.showActive') : t('main.buttons.showArchived')}
      </button>

      <div>
        <h3>{t('main.taskList.title')}</h3>
        {tasks.length > 0 ? (
          <ul>
            {tasks.map((task) => (
              <li key={task.id}>
                <strong>{t('main.labels.taskTitle')}:</strong> {task.title} <br />
                <strong>{t('main.labels.taskDescription')}:</strong> {task.description} <br />
                <strong>{t('main.labels.category')}:</strong> {task.category} <br />
                <strong>{t('main.labels.priority')}:</strong> {task.priority} <br />
                <strong>{t('main.labels.date')}:</strong> {task.dueDate} <br />
                <strong>{t('main.labels.status')}:</strong>{' '}
                {task.isDone ? t('main.status.done') : t('main.status.notDone')} <br />
                {!task.isDone && (
                  <button onClick={() => handleMarkAsDone(task.id)}>
                    {t('main.buttons.markAsDone')}
                  </button>
                )}
                <button onClick={() => handleEditTask(task)}>
                  {t('main.buttons.editTask')}
                </button>
                <button onClick={() => handleArchiveTask(task.id)}>
                  {t('main.buttons.archiveTask')}
                </button>
                <button onClick={() => handleDeleteTask(task.id)}>
                  {t('main.buttons.deleteTask')}
                </button>
              </li>
            ))}
          </ul>
        ) : (
          <p>{t('main.taskList.noTasks')}</p>
        )}
      </div>
      {responseMessage && <p>{responseMessage}</p>}
    </div>
  );
}

export default Main;
