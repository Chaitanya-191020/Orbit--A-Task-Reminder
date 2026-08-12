import { Router } from 'express';
import {
  getTasks,
  createTask,
  updateTask,
  toggleTaskCompletion,
  deleteTask
} from '../controllers/tasks.controller';

const router = Router();

router.get('/', getTasks);
router.post('/', createTask);
router.put('/:id', updateTask);
router.patch('/:id/complete', toggleTaskCompletion);
router.delete('/:id', deleteTask);

export default router;
