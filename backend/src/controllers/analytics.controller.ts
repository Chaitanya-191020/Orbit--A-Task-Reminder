import { Request, Response, NextFunction } from 'express';
import { prisma } from '../db/prisma';

export const getAnalyticsSummary = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const now = new Date();
    const thirtyDaysAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);
    const sevenDaysAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);

    // Total focus time (last 7 days and 30 days)
    const focusSessions30 = await prisma.focusSession.aggregate({
      _sum: { duration_minutes: true },
      where: { completed_at: { gte: thirtyDaysAgo } }
    });
    const focusSessions7 = await prisma.focusSession.aggregate({
      _sum: { duration_minutes: true },
      where: { completed_at: { gte: sevenDaysAgo } }
    });

    // Task completion rate (completed / total created recently)
    const totalTasks30 = await prisma.task.count({ where: { created_at: { gte: thirtyDaysAgo } } });
    const completedTasks30 = await prisma.task.count({ where: { is_completed: true, updated_at: { gte: thirtyDaysAgo } } });
    
    // Total habit streaks
    const habits = await prisma.habit.findMany({
      select: { streak_count: true }
    });
    const totalStreaks = habits.reduce((acc, curr) => acc + curr.streak_count, 0);

    res.json({
      success: true,
      data: {
        focus_time_7_days: focusSessions7._sum.duration_minutes || 0,
        focus_time_30_days: focusSessions30._sum.duration_minutes || 0,
        tasks_completion_rate_30_days: totalTasks30 > 0 ? (completedTasks30 / totalTasks30) * 100 : 0,
        total_habit_streaks: totalStreaks
      }
    });
  } catch (error) {
    next(error);
  }
};
