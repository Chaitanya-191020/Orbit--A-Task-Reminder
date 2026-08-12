import { Router } from 'express';
import {
  getHabits,
  createHabit,
  updateHabit,
  incrementStreak,
  deleteHabit
} from '../controllers/habits.controller';

const router = Router();

router.get('/', getHabits);
router.post('/', createHabit);
router.put('/:id', updateHabit);
router.patch('/:id/streak', incrementStreak);
router.delete('/:id', deleteHabit);

export default router;
