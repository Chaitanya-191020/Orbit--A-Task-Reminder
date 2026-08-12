import { Request, Response, NextFunction } from 'express';
import { prisma } from '../db/prisma';

export const getGoals = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const goals = await prisma.goal.findMany({ orderBy: { created_at: 'desc' } });
    res.json({ success: true, data: goals });
  } catch (error) {
    next(error);
  }
};

export const createGoal = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { title, description } = req.body;
    const newGoal = await prisma.goal.create({ data: { title, description } });
    res.status(201).json({ success: true, data: newGoal });
  } catch (error) {
    next(error);
  }
};

export const updateGoal = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const updatedGoal = await prisma.goal.update({
      where: { id },
      data: req.body
    });
    res.json({ success: true, data: updatedGoal });
  } catch (error) {
    next(error);
  }
};

export const updateGoalProgress = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const { progress_percentage } = req.body;

    const is_achieved = progress_percentage >= 100;

    const updatedGoal = await prisma.goal.update({
      where: { id },
      data: {
        progress_percentage,
        is_achieved
      }
    });
    res.json({ success: true, data: updatedGoal });
  } catch (error) {
    next(error);
  }
};

export const deleteGoal = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    await prisma.goal.delete({ where: { id } });
    res.json({ success: true, message: 'Goal deleted' });
  } catch (error) {
    next(error);
  }
};
