import { Router } from 'express';
import { logFocusSession, getFocusSessions } from '../controllers/focus.controller';

const router = Router();

router.get('/', getFocusSessions);
router.post('/', logFocusSession);

export default router;
