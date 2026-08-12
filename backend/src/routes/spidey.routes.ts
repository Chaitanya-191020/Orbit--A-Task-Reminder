import { Router } from 'express';
import { getSpideyBriefing } from '../controllers/spidey.controller';

const router = Router();

router.get('/briefing', getSpideyBriefing);

export default router;
