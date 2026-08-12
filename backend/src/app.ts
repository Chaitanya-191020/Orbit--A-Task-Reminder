import express from 'express';
import cors from 'cors';
import { errorHandler } from './middlewares/errorHandler';
// Routes imports will go here
import alarmRoutes from './routes/alarms.routes';
import taskRoutes from './routes/tasks.routes';
import spideyRoutes from './routes/spidey.routes';
import habitRoutes from './routes/habits.routes';
import goalRoutes from './routes/goals.routes';
import focusRoutes from './routes/focus.routes';
import analyticsRoutes from './routes/analytics.routes';

const app = express();

// Middlewares
app.use(cors());
app.use(express.json());

// Routes
app.use('/api/alarms', alarmRoutes);
app.use('/api/tasks', taskRoutes);
app.use('/api/spidey', spideyRoutes);
app.use('/api/habits', habitRoutes);
app.use('/api/goals', goalRoutes);
app.use('/api/focus', focusRoutes);
app.use('/api/analytics', analyticsRoutes);

// Health check
app.get('/api/health', (req, res) => {
  res.status(200).json({ status: 'ok', timestamp: new Date() });
});

// Global Error Handler
app.use(errorHandler);

export default app;
