import { Request, Response, NextFunction } from 'express';
import { prisma } from '../db/prisma';

export const getHabits = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const habits = await prisma.habit.findMany({ orderBy: { created_at: 'desc' } });
    res.json({ success: true, data: habits });
  } catch (error) {
    next(error);
  }
};

export const createHabit = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { title, description } = req.body;
    const newHabit = await prisma.habit.create({ data: { title, description } });
    res.status(201).json({ success: true, data: newHabit });
  } catch (error) {
    next(error);
  }
};

export const updateHabit = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const updatedHabit = await prisma.habit.update({
      where: { id },
      data: req.body
    });
    res.json({ success: true, data: updatedHabit });
  } catch (error) {
    next(error);
  }
};

export const incrementStreak = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    const habit = await prisma.habit.findUnique({ where: { id } });
    if (!habit) return res.status(404).json({ success: false, message: 'Habit not found' });

    const updatedHabit = await prisma.habit.update({
      where: { id },
      data: {
        streak_count: habit.streak_count + 1,
        last_completed_date: new Date()
      }
    });
    res.json({ success: true, data: updatedHabit });
  } catch (error) {
    next(error);
  }
};

export const deleteHabit = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { id } = req.params;
    await prisma.habit.delete({ where: { id } });
    res.json({ success: true, message: 'Habit deleted' });
  } catch (error) {
    next(error);
  }
};
