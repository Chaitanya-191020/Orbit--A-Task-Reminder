import { Router } from 'express';
import {
  getGoals,
  createGoal,
  updateGoal,
  updateGoalProgress,
  deleteGoal
} from '../controllers/goals.controller';

const router = Router();

router.get('/', getGoals);
router.post('/', createGoal);
router.put('/:id', updateGoal);
router.patch('/:id/progress', updateGoalProgress);
router.delete('/:id', deleteGoal);

export default router;
