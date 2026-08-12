import { Request, Response, NextFunction } from 'express';
import { prisma } from '../db/prisma';

export const logFocusSession = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { duration_minutes, session_type } = req.body;
    const session = await prisma.focusSession.create({
      data: { duration_minutes, session_type }
    });
    res.status(201).json({ success: true, data: session });
  } catch (error) {
    next(error);
  }
};

export const getFocusSessions = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const sessions = await prisma.focusSession.findMany({
      orderBy: { completed_at: 'desc' }
    });
    res.json({ success: true, data: sessions });
  } catch (error) {
    next(error);
  }
};
