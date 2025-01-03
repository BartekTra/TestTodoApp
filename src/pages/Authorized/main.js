import React, { useState, useEffect } from 'react';
import api from '../.././api/axiosConfig'; // Axios configuration for your backend

function Main() {
  const [tasks, setTasks] = useState([]);
  const [taskTitle, setTaskTitle] = useState('');
  const [taskDescription, setTaskDescription] = useState('');
  const [taskDate, setTaskDate] = useState('');
  const [responseMessage, setResponseMessage] = useState('');

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
    if (taskTitle && taskDescription && taskDate) {
      try {
        const newTask = {
          title: taskTitle,
          description: taskDescription,
          dueDate: taskDate, // Already in YYYY-MM-DD format from the input
        };
  
        const response = await api.post('/api/v1/AddTask', newTask);
        setTasks([...tasks, { ...response.data, dueDate: taskDate }]);
        setTaskTitle('');
        setTaskDescription('');
        setTaskDate('');
        setResponseMessage('Task added successfully!');
      } catch (error) {
        console.error('Error adding task:', error);
        setResponseMessage('Failed to add task.');
      }
    } else {
      setResponseMessage('Please fill in all fields before adding a task.');
    }
  };
  

  // Function to handle deleting tasks
  const handleDeleteTask = async (taskId) => {
    try {
      await api.delete(`/api/v1/tasks/${taskId}`); // Replace with your actual API endpoint
      setTasks(tasks.filter((task) => task.id !== taskId));
      setResponseMessage('Task deleted successfully!');
    } catch (error) {
      console.error('Error deleting task:', error);
      setResponseMessage('Failed to delete task.');
    }
  };

  return (
    <div className="App">
      <h1>To-Do List</h1>
      <h2>Add a Task</h2>
      <div>
        <label htmlFor="taskTitle">Title:</label>
        <input
          type="text"
          id="taskTitle"
          value={taskTitle}
          onChange={(e) => setTaskTitle(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="taskDescription">Description:</label>
        <input
          type="text"
          id="taskDescription"
          value={taskDescription}
          onChange={(e) => setTaskDescription(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="taskDate">Date:</label>
        <input
          type="date"
          id="taskDate"
          value={taskDate}
          onChange={(e) => setTaskDate(e.target.value)}
        />
      </div>
      <button onClick={handleAddTask}>Add Task</button>

      <h3>Task List</h3>
      {tasks.length > 0 ? (
        <ul>
          {tasks.map((task) => (
            <li key={task.id}>
                <strong>{task.title}</strong> - {task.description} ({task.dueDate})
                <button onClick={() => handleDeleteTask(task.id)}>Delete</button>
            </li>
            ))}
        </ul>
      ) : (
        <p>No tasks available. Add a new task above!</p>
      )}

      {/* Response message */}
      {responseMessage && <p>{responseMessage}</p>}
    </div>
  );
}

export default Main;
