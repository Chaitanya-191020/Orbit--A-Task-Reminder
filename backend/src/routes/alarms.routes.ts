import { Router } from 'express';
import {
  getAlarms,
  getAlarmById,
  createAlarm,
  updateAlarm,
  toggleAlarmStatus,
  deleteAlarm
} from '../controllers/alarms.controller';

const router = Router();

router.get('/', getAlarms);
router.get('/:id', getAlarmById);
router.post('/', createAlarm);
router.put('/:id', updateAlarm);
router.patch('/:id/toggle', toggleAlarmStatus);
router.delete('/:id', deleteAlarm);

export default router;
