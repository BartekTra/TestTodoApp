import React, { useState, useEffect } from 'react';
import api from '../.././api/axiosConfig'; // Axios configuration for your backend
import { useTranslation } from 'react-i18next';

function Main() {
  const [tasks, setTasks] = useState([]);
  const [taskTitle, setTaskTitle] = useState('');
  const [taskDescription, setTaskDescription] = useState('');
  const [taskDate, setTaskDate] = useState('');
  const [category, SetCategory] = useState('');
  const [responseMessage, setResponseMessage] = useState('');
  const [priority, setPriority] = useState('HIGH');
  const [sortOption, setSortOption] = useState('date'); // Default sort by date
  const [dateSortOrder, setDateSortOrder] = useState('earliest'); // Default date sort order
  const [priorityFilter, setPriorityFilter] = useState('ALL'); // Default priority filter
  const { t } = useTranslation();

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const response = await api.get('/api/v1/GetTasks'); // Replace with your actual API endpoint
        const formattedTasks = response.data.map((task) => ({
          ...task,
          dueDate: task.dueDate ? formatDate(task.dueDate) : '', // Format dueDate
        }));
        setTasks(formattedTasks);
      } catch (error) {
        console.error('Error fetching tasks:', error);
        setResponseMessage('Failed to load tasks.');
      }
    };

    fetchTasks();
  }, []);

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
          dueDate: taskDate, // Already in YYYY-MM-DD format from the input
        };

        const response = await api.post('/api/v1/AddTask', newTask);
        setTasks([...tasks, { ...response.data, dueDate: taskDate }]);
        setTaskTitle('');
        setTaskDescription('');
        setTaskDate('');
        setPriority(priority);
        setResponseMessage('Task added successfully!');
      } catch (error) {
        console.error('Error adding task:', error);
        setResponseMessage('Failed to add task.');
      }
    } else {
      setResponseMessage('Please fill in all fields before adding a task.');
    }
  };

  const handleDeleteTask = async (taskId) => {
    try {
      await api.delete(`/api/v1/DeleteTask/${taskId}`); // Replace with your actual API endpoint
      setTasks(tasks.filter((task) => task.id !== taskId));
      setResponseMessage('Task deleted successfully!');
    } catch (error) {
      console.error('Error deleting task:', error);
      setResponseMessage('Failed to delete task.');
    }
  };

  const handleMarkAsDone = async (taskId) => {
    try {
      const response = await api.post(`/api/v1/MarkTaskAsDone/${taskId}`); // Replace with your actual API endpoint
      setTasks(
        tasks.map((task) =>
          task.id === taskId ? { ...task, isDone: true } : task
        )
      );
      setResponseMessage('Task marked as done successfully!');
    } catch (error) {
      console.error('Error marking task as done:', error);
      setResponseMessage('Failed to mark task as done.');
    }
  };

  const filteredAndSortedTasks = tasks
    .filter((task) => {
      if (priorityFilter === 'ALL') return true;
      return task.priority === priorityFilter;
    })
    .sort((a, b) => {
      if (sortOption === 'date') {
        if (dateSortOrder === 'earliest') {
          return new Date(a.dueDate) - new Date(b.dueDate);
        } else {
          return new Date(b.dueDate) - new Date(a.dueDate);
        }
      }
      return 0; // No specific sort needed for priority filter
    });

  return (
    <div className="App">
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
          onChange={(e) => SetCategory(e.target.value)}
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
      <button onClick={handleAddTask}>{t('main.buttons.addTask')}</button>

      <div>
        <h3>{t('main.taskList.title')}</h3>
        {filteredAndSortedTasks.length > 0 ? (
          <ul>
            {filteredAndSortedTasks.map((task) => (
              <ul key={task.id}>
                <div>
                  <strong>{t('main.labels.taskTitle')}:</strong> {task.title} <br />
                  <strong>{t('main.labels.taskDescription')}:</strong> {task.description} <br />
                  <strong>{t('main.labels.category')}:</strong> {task.category} <br />
                  <strong>{t('main.labels.priority')}:</strong> {task.priority} <br />
                  <strong>{t('main.labels.date')}:</strong> {task.dueDate} <br />
                  <strong>{t('main.labels.status')}:</strong>{' '}
                  {new Date(task.dueDate) < new Date() && !task.isDone
                    ? t('main.status.overdue')
                    : task.isDone
                    ? t('main.status.done')
                    : t('main.status.notDone')} <br />
                  <button
                    onClick={() => handleMarkAsDone(task.id)}
                    disabled={task.isDone || new Date(task.dueDate) <= new Date()}
                  >
                    {t('main.buttons.markAsDone')}
                  </button>
                  <button onClick={() => handleDeleteTask(task.id)}>
                    {t('main.buttons.deleteTask')}
                  </button>
                </div>
              </ul>
            ))}
          </ul>
        ) : (
          <p>{t('main.taskList.noTasks')}</p>
        )}
      </div>
    </div>
  );
}

export default Main;
