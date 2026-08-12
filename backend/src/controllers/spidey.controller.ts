import { Request, Response, NextFunction } from 'express';
import { prisma } from '../db/prisma';

export const getSpideyBriefing = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { alarm_id } = req.query;
    
    let alarm = null;
    let alarmTasks: any[] = [];
    
    if (alarm_id) {
      alarm = await prisma.alarm.findUnique({
        where: { id: alarm_id as string }
      });
      
      alarmTasks = await prisma.task.findMany({
        where: { attached_alarm_id: alarm_id as string, is_completed: false }
      });
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);

    // Get today's due tasks that aren't attached to this specific alarm
    const todaysTasks = await prisma.task.findMany({
      where: {
        is_completed: false,
        due_date: {
          gte: today,
          lt: tomorrow
        },
        NOT: alarm_id ? { attached_alarm_id: alarm_id as string } : undefined
      }
    });

    const activeHabits = await prisma.habit.findMany({
      where: {
        OR: [
          { last_completed_date: null },
          { last_completed_date: { lt: today } } // Not completed today
        ]
      }
    });

    const activeGoals = await prisma.goal.findMany({
      where: { is_achieved: false }
    });

    const hour = new Date().getHours();
    let greeting = 'Good Evening';
    if (hour < 12) greeting = 'Good Morning';
    else if (hour < 17) greeting = 'Good Afternoon';

    // Construct the payload meant for TTS parsing
    const briefingPayload = {
      greeting,
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      alarm: alarm ? { label: alarm.label } : null,
      urgent_tasks: alarmTasks.map(t => t.title),
      todays_tasks: todaysTasks.map(t => t.title),
      habits_to_do: activeHabits.map(h => h.title),
      goals_in_progress: activeGoals.map(g => g.title),
      spoken_summary: `
        ${greeting}. It is ${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}.
        ${alarm ? `Your ${alarm.label} alarm has triggered.` : ''}
        You have ${alarmTasks.length + todaysTasks.length} tasks pending for today.
        ${activeHabits.length > 0 ? `Don't forget to complete your ${activeHabits.length} habits.` : ''}
      `.replace(/\s+/g, ' ').trim()
    };

    res.json({ success: true, data: briefingPayload });
  } catch (error) {
    next(error);
  }
};
